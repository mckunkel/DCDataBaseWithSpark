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
package database.process;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.io.hipo.HipoDataSource;

import database.objects.TBHits;
import database.utils.Coordinate;
import database.utils.EmptyDataPoint;

public class DataProcess {
	private Dimension screensize = null;
	private JFrame frame = null;
	private JTabbedPane tabbedPane = null;

	private Map<Coordinate, H2F> occupanciesByCoordinate = new HashMap<Coordinate, H2F>();
	private List<TBHits> tbHitList = new ArrayList<TBHits>();
	private EmbeddedCanvas can1 = null;
	private EmbeddedCanvas can2 = null;

	private int updateTime = 2000;

	private HipoDataSource reader = null;

	public DataProcess() {
		this.reader = new HipoDataSource();
		init();
	}

	public DataProcess(String str) {
		this.reader = new HipoDataSource();
		this.reader.open(str);
		init();
		processEvent();
	}

	public DataProcess(HipoDataSource reader) {
		this.reader = reader;
		init();
		processEvent();
	}

	public void openFile(String str) {
		System.out.println("will open " + str);
		this.reader.open(str);
	}

	private void createHistograms() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				occupanciesByCoordinate.put(new Coordinate(i, j),
						new H2F("Occupancy all hits SL" + i + "sector" + j, "", 112, 1, 113, 6, 1, 7));
				occupanciesByCoordinate.get(new Coordinate(i, j)).setTitleX("Wire Sector" + (j + 1));
				occupanciesByCoordinate.get(new Coordinate(i, j)).setTitleY("Layer SL" + (i + 1));
			}
		}
	}

	private void init() {
		createHistograms();
	}

	public void processEvent() {

		int counter = 0;
		while (reader.hasEvent() && counter < 4000) {// && counter < 4000
			if (counter % 500 == 0)
				System.out.println("done " + counter + " events");
			DataEvent event = reader.getNextEvent();
			counter++;
			if (event.hasBank("TimeBasedTrkg::TBHits")) {
				processTBHits(event);
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
		Dataset<Row> collData = EmptyDataPoint.getEmptyDCData();
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

			tbHitList.add(tbHits);
			occupanciesByCoordinate
					.get(new Coordinate(bnkHits.getInt("superlayer", i) - 1, bnkHits.getInt("sector", i) - 1))
					.fill(bnkHits.getInt("wire", i), bnkHits.getInt("layer", i));
		}
	}

	public static int getRunNumber(HipoDataSource reader) {

		return reader.gotoEvent(0).getBank("RUN::config").getInt("run", 0);
	}

	public Map<Coordinate, H2F> getCoordinateMap() {
		return this.occupanciesByCoordinate;
	}

	public H2F getHistogramByMap(int superLayer, int sector) {
		return this.occupanciesByCoordinate.get(new Coordinate(superLayer - 1, sector - 1));
	}
}
