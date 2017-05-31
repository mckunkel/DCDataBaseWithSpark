/*  +__^_________,_________,_____,________^-.-------------------,
 *  | |||||||||   `--------'     |          |                   O
 *  `+-------------USMC----------^----------|___________________|
 *    `\_,---------,---------,--------------'
 *      / X MK X /'|       /'
 *     / X MK X /  `\    /'
 *    / X MK X /`-------'
 *   / X MK X /
 *  / X MK X /
 * (________(                @author m.c.kunkel
 *  `------'
*/
package database.model;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

public class DataProcess {
	JPanel mainPanel = null;
	DataSourceProcessorPane processorPane = null;
	private JTabbedPane tabbedPane = null;

	private Map<Coordinate, H2F> occupanciesByCoordinate = new HashMap<Coordinate, H2F>();
	private Map<Coordinate, H2F> occupanciesintrack = new HashMap<Coordinate, H2F>();
	private Map<Coordinate, DataPoints2> occupanciesByMySQL = new HashMap<Coordinate, DataPoints2>();
	private Map<Coordinate, DataPoints> occupanciesByMySQLtest = new HashMap<Coordinate, DataPoints>();

	private List<TBHits> tbHitList = new ArrayList<TBHits>();

	private List<DataPointMultiDimension> test = new ArrayList<DataPointMultiDimension>();

	private EmbeddedCanvas can1 = null;
	private EmbeddedCanvas can2 = null;

	int counter = 0;
	int updateTime = 2000;

	CLASDecoder clasDecoder = new CLASDecoder();

	DCHBEngine enHB = new DCHBEngine();

	DCTBEngine enTB = new DCTBEngine();

	private HipoDataSource reader = null;

	public DataProcess() {

		// create main panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();

		processorPane = new DataSourceProcessorPane();
		processorPane.setUpdateRate(100);

		mainPanel.add(tabbedPane);
		mainPanel.add(processorPane, BorderLayout.PAGE_END);

		this.processorPane.addEventListener(this);

		createCanvas();
		addCanvasToPane();
		init();

		enHB.init();
		enTB.init();
	}

	public DataProcess(HipoDataSource reader) {
		this.reader = reader;
		init();
		processEvent();
	}

	private void createHistograms() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				occupanciesByCoordinate.put(new Coordinate(i, j),
						new H2F("Occupancy all hits SL" + i + "sector" + j, "", 112, 1, 113, 6, 1, 7));
				occupanciesByCoordinate.get(new Coordinate(i, j)).setTitleX("Wire Sector" + (j + 1));
				occupanciesByCoordinate.get(new Coordinate(i, j)).setTitleY("Layer SL" + (i + 1));
				occupanciesintrack.put(new Coordinate(i, j),
						new H2F("Occupancy used in track" + i, "", 112, 1, 113, 6, 1, 7));
				occupanciesintrack.get(new Coordinate(i, j)).setTitleX("Wire Sector" + (j + 1));
				occupanciesintrack.get(new Coordinate(i, j)).setTitleY("Layer SL" + (i + 1));
			}
		}
	}

	private void createDataList() {
		for (int superLayer = 1; superLayer <= 6; superLayer++) {
			for (int sector = 1; sector <= 6; sector++) {
				for (int wire = 1; wire <= 112; wire++) {
					for (int layer = 1; layer <= 6; layer++) {
						test.add(new DataPointMultiDimension(superLayer, sector, wire, layer));
					}
				}
			}
		}
	}

	private void createCanvas() {
		can1 = new EmbeddedCanvas();
		can1.initTimer(updateTime);
		can2 = new EmbeddedCanvas();
		can2.initTimer(updateTime);

		can1.divide(6, 6);
		can2.divide(6, 6);

	}

	private void init() {
		createHistograms();
		createDataList();
		// drawPlots();
	}

	@Override
	public void dataEventAction(DataEvent event) {
		counter++;
		if (counter % 200 == 0)
			System.out.println("done " + counter + " events");

		HipoDataEvent hipo = null;
		if (event instanceof EvioDataEvent) { // starting from raw event
			hipo = (HipoDataEvent) clasDecoder.getDataEvent(event);
		} else {
			hipo = (HipoDataEvent) event;
		}
		if (hipo.getBank("TimeBasedTrkg::TBHits").rows() == 0 && hipo.getBank("DC::tdc").rows() > 0) {
			// run HBT
			enHB.processDataEvent(hipo);
			// Processing TBT
			enTB.processDataEvent(hipo);
			// hipo.show();
		}
		if (hipo.hasBank("TimeBasedTrkg::TBHits")) {
			processTBHits(event);
		}
	}

	private void processEvent() {

		int counter = 0;
		while (reader.hasEvent() && counter < 400) {// && counter < 4000
			if (counter % 500 == 0)
				System.out.println("done " + counter + " events");
			DataEvent event = reader.getNextEvent();
			counter++;
			if (event.hasBank("TimeBasedTrkg::TBHits")) {
				processTBHits(event);
			}

		}
	}

	private void incrementCoordinate(int superLayer, int sector, int wire, int layer) {
		for (DataPointMultiDimension dDim : test) {
			if (dDim.getSuperLayer() == superLayer && dDim.getSector() == sector && dDim.getWire() == wire
					&& dDim.getLayer() == layer) {
				dDim.increment();
			}
		}
	}

	private int getCoordinateValue(int superLayer, int sector, int wire, int layer) {
		for (DataPointMultiDimension dDim : test) {
			if (dDim.getSuperLayer() == superLayer && dDim.getSector() == sector && dDim.getWire() == wire
					&& dDim.getLayer() == layer) {
				return dDim.getValue();
			}
		}
		return 0;
	}

	private void displayStuff() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				for (int x = 0; x < 112; x++) {
					for (int y = 0; y < 6; y++) {
						int histValue = (int) occupanciesByCoordinate.get(new Coordinate(i, j)).getBinContent(x, y);
						int listValue = this.getCoordinateValue(i + 1, j + 1, x + 1, y + 1);

						// if (histValue != 0 && listValue != 0) {
						// System.out.println("#####################################");
						// System.out
						// .println(" By SP" + (i + 1) + " Sec" + (j + 1) + "
						// x:" + (x + 1) + " y:" + (y + 1));
						//
						// System.out.println("histvalue " + histValue);
						// System.out.println("listValue " + listValue);
						//
						// }

					}
				}
			}
		}
	}

	private void processMyJunk(SparkSession spSession) {

		System.out.println("My Junk");

		// data
		Encoder<TBHits> TBHitsEncoder = Encoders.bean(TBHits.class);
		// Dataset<TBHits> tbHitDf = spSession.createDataset(tbHitList,
		// TBHitsEncoder);
		Dataset<Row> tbHitDfRow = spSession.createDataset(tbHitList, TBHitsEncoder).toDF();

		System.out.println("Contents of TBHits DF : ");
		Dataset<Row> testDF = tbHitDfRow.groupBy("sector", "layer", "superLayer", "wire").count()
				.sort("sector", "layer", "superLayer", "wire").toDF("sector", "layer", "superLayer", "wire", "counts");
		// testDF.select("wire", "sector", "layer", "superLayer",
		// "counts").write().format("com.databricks.spark.csv")
		// .option("header", "true").save("data/countedData.csv"); //
		// csv("/data/countedData.csv");
		testDF.createOrReplaceTempView("DataView");
		Dataset<Row> dataDF = spSession.sql("SELECT layer, superLayer, sector, wire FROM DataView").sort("sector",
				"superlayer", "layer", "wire");
		// dataDF.show();
		// Compare with histgram data
		// System.out.println("occupanciesByCoordinate");
		// System.out.println("|layer|superLayer|sector|wire|");
		// for (int ybin = 0; ybin < 6; ybin++) {
		// for (int xbin = 0; xbin < 112; xbin++) {
		// double value = occupanciesByCoordinate.get(new Coordinate(0,
		// 1)).getData(xbin, ybin);
		// if (value == 0) {
		// System.out.println(" " + (ybin + 1) + " | 1 | 2 \t| " + (xbin + 1));
		// }
		// }
		// }

		System.out.println("TESTDF");
		// Empty Data
		Dataset<Row> collData = EmptyDCFrame.getEmptyDCData();
		collData.createOrReplaceTempView("testView");
		Dataset<Row> emptyDF = spSession.sql("SELECT layer, superLayer, sector, wire FROM testView").sort("sector",
				"superlayer", "layer", "wire");

		System.out.println("subtract : ");

		Dataset<Row> subDf = emptyDF.except(dataDF);
		subDf.show();

		Dataset<Row> subDfII = subDf.filter(subDf.col("wire").equalTo(1).and(subDf.col("layer").equalTo(2)));
		subDfII.show();

		// subDf.foreach((ForeachFunction<Row>) row -> System.out.println("layer
		// " + row.get(0) + " superlayer "
		// + row.get(1) + " sector " + row.get(2) + " wire " + row.get(3) +
		// ""));

	}

	// public Dataset<Row> findDiff(SparkSession spSession, Dataset<Row> left,
	// Dataset<Row> right) {
	// if (left == null || right == null) {
	// return null;
	// }
	// StructType schema = left.schema();
	// JavaRDD<Row> leftRDD = left.toJavaRDD();
	// JavaRDD<Row> rightRDD = right.toJavaRDD();
	// System.out.println("here");
	// // diff which is there in right but not in left deleted value
	// JavaRDD<Row> diffRDD = rightRDD.subtract(leftRDD);
	// System.out.println("here");
	//
	// // Dataset newdf = spSession.createDataFrame(diffRDD, schema);
	// Dataset<Row> newdf = spSession.createDataFrame(diffRDD, schema).toDF();
	// System.out.println("here");
	//
	// return newdf;
	//
	// }

	private void processMyJunkOriginal(SparkSession spSession) {

		System.out.println("My Junk");
		// for (TBHits tbHits : tbHitList) {
		// System.out.println(tbHits);
		//
		// }
		// lets try TBHits object
		Dataset<Row> collData = EmptyDCFrame.getEmptyDCData();
		System.out.println("Contents of Empty set DF : ");
		collData.show();
		// above here

		Encoder<TBHits> TBHitsEncoder = Encoders.bean(TBHits.class);
		Dataset<TBHits> tbHitDf = spSession.createDataset(tbHitList, TBHitsEncoder);
		// Dataset<Row> tbHitsDF = spSession.createDataFrame(tbHitList,
		// TBHits.class);
		System.out.println("Contents of TBHits DF : ");
		// tbHitDf.show();
		// tbHitDf.filter(tbHitDf.col("trkID").gt(0)).show();
		// tbHitDf.groupBy("wire", "layer", "sector",
		// "superLayer").count().sort("superLayer", "layer").show();
		// tbHitDf.filter(tbHitDf.col("wire").equalTo(88)).groupBy("wire",
		// "layer", "sector", "superLayer").count()
		// .sort("superLayer", "layer").show();

		Dataset<Row> testDF = tbHitDf.groupBy("wire", "layer", "sector", "superLayer").count()
				.sort("wire", "layer", "superLayer").toDF("wire", "layer", "sector", "superLayer", "counts");

		System.out.println("TESTDF");
		testDF.filter(testDF.col("counts").equalTo(1)).sort("layer", "superLayer", "wire").show();

		// Dataset<Row> testSumDF = tbHitDf.groupBy("wire", "layer", "sector",
		// "superLayer").agg(sum()
		// testDF.agg(sum(testDF.col("counts"))).show();
		// testDF.groupBy("wire", "layer", "sector", "superLayer",
		// "counts").agg(avg(testDF.col("counts")))
		// .sort("wire", "layer", "superlayer").show();

		// deptDf.agg(sum(deptDf.col(""))
		// deptDf.groupBy(col("trkID"), col("sector")).count().show();
		// lets try for multi
		Encoder<DataPointMultiDimension> multiDataEncoder = Encoders.bean(DataPointMultiDimension.class);
		Dataset<DataPointMultiDimension> df = null;
		df = spSession.createDataset(test, multiDataEncoder);

		// df.show(400);
		df.createOrReplaceTempView("types");
		df.printSchema();
		// df.select("wire").show();
		df.filter(df.col("value").gt(0)).filter(df.col("sector").equalTo(2)).sort("layer", "superLayer", "wire").show();
		df.groupBy("wire").count().show();

		// df.show();
		// df.filter(df.col("value").gt(0)).show();
		// System.out.println("Groupby wire");
		// df.groupBy(col("wire")).count().show();

		// df.filter(df.col("superLayer").equalTo(6)).filter(df.col("sector").equalTo(2)).filter(df.col("wire").gt(45))
		// .show();

		// System.out
		// .println(df.filter(df.col("sector").equalTo(2)).filter(df.col("value").equalTo(0)).count()
		// + " zeros");
		testDF.createOrReplaceTempView("testView");
		Dataset<Row> teenagersDF = spSession
				.sql("SELECT wire, layer,counts FROM testView WHERE counts BETWEEN 13 AND 19").sort("counts");
		teenagersDF.show();
		spSession.sql("SELECT layer, superLayer, sector, avg(counts) FROM testView GROUP BY superLayer, sector, layer")
				.sort("superlayer", "layer").show(40);
		spSession.sql("SELECT wire, superLayer, sector, avg(counts) FROM testView GROUP BY superLayer, sector, wire")
				.sort("superlayer", "wire").show(40);
		spSession.sql("SELECT superLayer, sector, sum(counts) FROM testView GROUP BY superLayer, sector")
				.sort("superlayer", "sector").show(40);
		// testView GROUP BY wire, layer").show();
		// +----------+------+------------------+
		// |superLayer|sector| avg(counts)|
		// +----------+------+------------------+
		// | 1| 2| 8.309278350515465|
		// | 1| 2| 8.117117117117116|
		// | 1| 2| 7.846846846846847|
		// | 1| 2| 6.972727272727273|
		// | 1| 2| 7.801980198019802|
		// | 1| 2|7.4324324324324325|
		// | 2| 2|7.3238095238095235|
		// | 2| 2| 7.461538461538462|
		// | 2| 2| 7.010416666666667|
		// | 2| 2| 7.073684210526316|
		// +----------+------+------------------+

		// |superLayer|sector| avg(counts)|
		// +----------+------+------------------+
		// | 1| 2| 9.666666666666666|
		// | 1| 2| 5.333333333333333|
		// | 1| 2| 9.8|
		// | 1| 2| 7.333333333333333|
		// | 1| 2|13.333333333333334|
		// | 1| 2| 6.5|
		// | 1| 2|3.3333333333333335|
		// | 1| 2|3.6666666666666665|
		// | 1| 2| 9.833333333333334|
		// | 1| 2|3.8333333333333335|
		// +----------+------+------------------+

		// // df.filter(df.col("value").equalTo(0)).show();
		// // df.filter(df.col("value").equalTo(0)).show();
		// // df.filter(df.col("value").equalTo(0)).show();
		// System.out.println("Create Dataframe from a DB Table");
		// Dataset<Row> demoDf =
		// spSession.read().format("jdbc").options(SparkMySQLConnection.jdbcOptions()).load();
		// demoDf.select("runno", "region").show();

	}

	@Override
	public void timerUpdate() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void resetEventListener() {
		counter = 0;
		this.init();
	}

	private void processTBHits(DataEvent event) {
		DataBank bnkHits = event.getBank("TimeBasedTrkg::TBHits");
		for (int i = 0; i < bnkHits.rows(); i++) {

			TBHits tbHits = new TBHits((bnkHits.getInt("id", i)), (bnkHits.getInt("status", i)),
					(bnkHits.getInt("sector", i)), (bnkHits.getInt("superlayer", i)), (bnkHits.getInt("layer", i)),
					(bnkHits.getInt("wire", i)), (bnkHits.getFloat("time", i)), (bnkHits.getFloat("doca", i)),
					(bnkHits.getFloat("docaError", i)), (bnkHits.getFloat("trkDoca", i)),
					(bnkHits.getFloat("timeResidual", i)), (bnkHits.getInt("LR", i)), (bnkHits.getFloat("X", i)),
					(bnkHits.getFloat("Z", i)), (bnkHits.getFloat("B", i)), (bnkHits.getInt("clusterID", i)),
					(bnkHits.getInt("trkID", i)));
			// TBHits tbHits = new TBHits((bnkHits.getInt("trkID", i)),
			// (bnkHits.getInt("status", i)));
			tbHitList.add(tbHits);
			// System.out.println(tbHits);
			occupanciesByCoordinate
					.get(new Coordinate(bnkHits.getInt("superlayer", i) - 1, bnkHits.getInt("sector", i) - 1))
					.fill(bnkHits.getInt("wire", i), bnkHits.getInt("layer", i));
			this.incrementCoordinate(bnkHits.getInt("superlayer", i), bnkHits.getInt("sector", i),
					bnkHits.getInt("wire", i), bnkHits.getInt("layer", i));

			if (bnkHits.getByte("trkID", i) > 0) {
				occupanciesintrack
						.get(new Coordinate(bnkHits.getInt("superlayer", i) - 1, bnkHits.getInt("sector", i) - 1))
						.fill(bnkHits.getInt("wire", i), bnkHits.getInt("layer", i));
			}
		}
	}

	private void drawPlots() {

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				can1.cd(6 * i + j);
				can1.draw(occupanciesByCoordinate.get(new Coordinate(i, j)));
				can2.cd(6 * i + j);
				can2.draw(occupanciesintrack.get(new Coordinate(i, j)));
			}
		}
		can1.update();
		can2.update();
	}

	private void addCanvasToPane() {
		tabbedPane.add("Occupancies all", can1);
		tabbedPane.add("Occupancies track", can2);
	}

	public static void main(String[] args) {

		HipoDataSource chain = new HipoDataSource();
		chain.open("/Users/michaelkunkel/WORK/CLAS/CLAS12/CLAS12Data/pass4/out_clas12_000762_a00000.hipo");
		DataProcess dataProcess = new DataProcess(chain);
		dataProcess.displayStuff();
		Logger.getLogger("org.apache.spark.SparkContext").setLevel(Level.WARN);
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);
		SparkSession spSession = SparkConnection.getSession();

		JavaSparkContext spContext = SparkConnection.getContext();
		dataProcess.processMyJunk(spSession);
		// JFrame frame = new JFrame("DC DataBase");
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Dimension screensize = null;
		// screensize = Toolkit.getDefaultToolkit().getScreenSize();
		// frame.setSize((int) (screensize.getHeight() * .75 * 1.618), (int)
		// (screensize.getHeight() * .75));
		// DataProcess viewer = new DataProcess();
		// // frame.add(viewer.getPanel());
		// frame.add(viewer.mainPanel);
		// frame.setVisible(true);

	}
}
