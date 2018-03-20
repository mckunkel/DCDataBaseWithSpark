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
import org.apache.spark.sql.SparkSession;
import org.jlab.groot.data.H2F;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.io.hipo.HipoDataSource;

import database.objects.StatusChangeDB;
import database.service.MainFrameService;
import database.utils.Coordinate;
import database.utils.MainFrameServiceManager;
import spark.utils.SparkManager;

public class DataProcess {
	private MainFrameService mainFrameService = null;
	private SparkSession spSession = null;
	private List<StatusChangeDB> emptyDataPoints = null;
	private HipoDataSource reader = null;

	private int nEvents = 0;

	public DataProcess() {

	}

	public void openFile(String str) {
		this.reader = new HipoDataSource();
		this.reader.open(str);
		init();

	}

	private void init() {
		this.mainFrameService = MainFrameServiceManager.getSession();
		this.mainFrameService.setRunNumber(getRunNumber());
		this.spSession = SparkManager.getSession();
		this.emptyDataPoints = new ArrayList<StatusChangeDB>();

		setMainFrameServiceNEvents();

	}

	private void checkNEvents() {
		if (nEvents == 0) {
			nEvents = reader.getSize();
		}
		System.out.println("Will process " + nEvents + " events");
	}

	public void processFile() {

		// checkNEvents();

		int counter = 0;
		while (reader.hasEvent()) {// && counter < 400 && counter < nEvents
			if (counter % 10000 == 0) {
				System.out.println("done " + counter + " events");
			}
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
			this.mainFrameService.getHistogramMap()
					.get(new Coordinate(bnkHits.getInt("superlayer", i) - 1, bnkHits.getInt("sector", i) - 1))
					.fill(bnkHits.getInt("wire", i), bnkHits.getInt("layer", i));
			// System.out.println(bnkHits.getInt("superlayer", i) + " " +
			// (bnkHits.getInt("sector", i) - 1) + " "
			// + bnkHits.getInt("wire", i) + " " + bnkHits.getInt("layer", i));
		}
	}

	private void createDataset() {
		for (int i = 0; i < 6; i++) {// superLayer
			for (int j = 0; j < 6; j++) { // sector
				int xbins = this.mainFrameService.getHistogramMap().get(new Coordinate(i, j)).getXAxis().getNBins();
				int ybins = this.mainFrameService.getHistogramMap().get(new Coordinate(i, j)).getYAxis().getNBins();
				double normalization = getHistNormalization(
						this.mainFrameService.getHistogramMap().get(new Coordinate(i, j)));
				for (int k = 0; k < ybins; k++) {
					for (int l = 0; l < xbins; l++) {
						double content = this.mainFrameService.getHistogramMap().get(new Coordinate(i, j))
								.getBinContent(l, k);
						// if (content == 0) {
						if (content <= normalization * this.mainFrameService.getUserPercent() / 100.0) {

							StatusChangeDB statusChangeDB = new StatusChangeDB();
							statusChangeDB.setSector(String.valueOf(j + 1));
							statusChangeDB.setSuperlayer(String.valueOf(i + 1));
							statusChangeDB.setLoclayer(String.valueOf(k + 1));
							statusChangeDB.setLocwire(String.valueOf(l + 1));

							emptyDataPoints.add(statusChangeDB);

						}

					}

				}
				Dataset<StatusChangeDB> df = spSession.createDataset(emptyDataPoints,
						SparkManager.statusChangeDBEncoder());
				// df.select("sector", "superlayer", "loclayer",
				// "locwire").show();//
				this.mainFrameService.getDataSetMap().put(new Coordinate(i, j), df);
				emptyDataPoints.clear();
			}
		}
	}

	private double getHistNormalization(H2F aH2f) {
		int xbins = aH2f.getXAxis().getNBins();
		int ybins = aH2f.getYAxis().getNBins();
		double normalization = 0.0;
		for (int k = 0; k < ybins; k++) {
			for (int l = 0; l < xbins; l++) {
				normalization += aH2f.getBinContent(l, k);
			}
		}

		double val = normalization / (xbins * ybins);
		double retValue = 1.0 / (val * Math.log(1.0 / val));
		return val;
	}

	public int getRunNumber() {
		checkNEvents();

		int retValue = 0;
		for (int i = 0; i < this.nEvents; i++) {

			if (reader.gotoEvent(i).hasBank("TimeBasedTrkg::TBHits")) {
				retValue = this.reader.gotoEvent(i).getBank("RUN::config").getInt("run", 0);
				break;
			}
		}
		return retValue;
	}

	public void setNEvents(int nEvents) {
		this.nEvents = nEvents;
	}

	private void setMainFrameServiceNEvents() {
		this.mainFrameService.setnEventsInFile(reader.getSize());

	}

}
