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

import java.util.HashMap;
import java.util.Map;

import org.jlab.groot.data.H2F;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.io.hipo.HipoDataSource;

import database.utils.Coordinate;

public class DataProcessForTest {
	private HipoDataSource reader = null;
	private Map<Coordinate, H2F> occupanciesByCoordinate = new HashMap<Coordinate, H2F>();

	private int nEvents = 0;

	public DataProcessForTest() {
	}

	public void openFile(String str) {
		this.reader = new HipoDataSource();
		this.reader.open(str);
		init();

	}

	private void init() {
		createHistograms();
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

	private void checkNEvents() {
		if (nEvents == 0) {
			nEvents = reader.getSize();
		}
		System.out.println("Will process + " + nEvents + " events");
	}

	public void processFile() {

		checkNEvents();

		int counter = 0;
		while (reader.hasEvent() && counter < nEvents) {// && counter < 400
			if (counter % 10000 == 0) {
				System.out.println("done " + counter + " events");
			}
			DataEvent event = reader.getNextEvent();
			counter++;
			if (event.hasBank("TimeBasedTrkg::TBHits")) {
				processTBHits(event);
			}

		}
	}

	private void processTBHits(DataEvent event) {
		DataBank bnkHits = event.getBank("TimeBasedTrkg::TBHits");
		for (int i = 0; i < bnkHits.rows(); i++) {
			occupanciesByCoordinate
					.get(new Coordinate(bnkHits.getInt("superlayer", i) - 1, bnkHits.getInt("sector", i) - 1))
					.fill(bnkHits.getInt("wire", i), bnkHits.getInt("layer", i));
		}
	}

	public int getRunNumber() {
		return this.reader.gotoEvent(1).getBank("RUN::config").getInt("run", 0);
	}

	public void setNEvents(int nEvents) {
		this.nEvents = nEvents;
	}

	public H2F getHistogramByMap(int superLayer, int sector) {
		return this.occupanciesByCoordinate.get(new Coordinate(superLayer - 1, sector - 1));
	}

}
