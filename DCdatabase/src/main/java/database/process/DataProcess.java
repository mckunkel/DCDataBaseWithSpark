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

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.io.hipo.HipoDataSource;

import database.objects.TBHits;
import database.service.MainFrameService;
import database.utils.Coordinate;
import database.utils.EmptyDataPoint;
import database.utils.MainFrameServiceManager;
import spark.utils.SparkManager;

public class DataProcess {
	private MainFrameService mainFrameService = null;
	private SparkSession spSession = null;
	private List<TBHits> tbHitList = null;

	private HipoDataSource reader = null;

	public DataProcess() {
		this.reader = new HipoDataSource();
		init();
	}

	public void openFile(String str) {
		System.out.println("will open " + str);
		this.reader.open(str);
	}

	private void init() {
		this.mainFrameService = MainFrameServiceManager.getSession();
		this.spSession = SparkManager.getSession();
		this.tbHitList = new ArrayList<TBHits>();
		// createHistograms();
	}

	public void processFile() {

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
		createDataset();
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

			this.tbHitList.add(tbHits);
			this.mainFrameService.getHistogramMap()
					.get(new Coordinate(bnkHits.getInt("superlayer", i) - 1, bnkHits.getInt("sector", i) - 1))
					.fill(bnkHits.getInt("wire", i), bnkHits.getInt("layer", i));
		}
	}

	private void createDataset() {
		Encoder<TBHits> TBHitsEncoder = Encoders.bean(TBHits.class);
		Dataset<Row> tbHitDfRow = this.spSession.createDataset(this.tbHitList, TBHitsEncoder).toDF();

		// OK, we have the CLAS data in a dataset, now we have to sort it to
		// only the relevant data
		// Then create a temporary view of this to compare it to an empty set
		// Empty set is for comparing and finding wires with 0 hits.
		// This is done because when filling a dataset, its relational, so
		// it doesnt know if a wire should be zero
		Dataset<Row> testDF = tbHitDfRow.groupBy("sector", "layer", "superLayer", "wire").count()
				.sort("sector", "layer", "superLayer", "wire").toDF("sector", "layer", "superLayer", "wire", "counts");
		testDF.createOrReplaceTempView("DataView");
		Dataset<Row> dataDF = spSession.sql("SELECT layer, superLayer, sector, wire FROM DataView").sort("sector",
				"superlayer", "layer", "wire");

		// Empty Data to compare to the dataset above
		Dataset<Row> emptyData = EmptyDataPoint.getEmptyDCData();
		emptyData.createOrReplaceTempView("testView");
		Dataset<Row> emptyDF = spSession.sql("SELECT layer, superLayer, sector, wire FROM testView").sort("sector",
				"superlayer", "layer", "wire");

		// Now find those in emptyDF that are not in dataDF and return this
		// dataset
		// Dataset<Row> subDf = emptyDF.except(dataDF);
		// System.out.println("I should show here");
		// subDf.show();

		this.mainFrameService.setDataset(emptyDF.except(dataDF));
	}

	public static int getRunNumber(HipoDataSource reader) {

		return reader.gotoEvent(0).getBank("RUN::config").getInt("run", 0);
	}

}
