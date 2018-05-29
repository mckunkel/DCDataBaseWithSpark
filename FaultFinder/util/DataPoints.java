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
package database.util;

import java.util.ArrayList;
import java.util.List;

public class DataPoints {
	private List<DataPointMultiDimension> points;

	public DataPoints() {
		this.points = new ArrayList<DataPointMultiDimension>();
	}

	public List<DataPointMultiDimension> getPoints() {
		return points;
	}

	public void putPoint2D(int superLayer, int sector, int wire, int layer) {
		points.add(new DataPointMultiDimension(superLayer, sector, wire, layer));
	}

	public void incrementPoint2D(int superLayer, int sector, int wire, int layer) {
		for (DataPointMultiDimension p : points) {
			if (p.getSuperLayer() == superLayer && p.getSector() == sector && p.getWire() == wire
					&& p.getLayer() == layer) {
				p.increment();
			}
		}
	}

	public int getPoint2DValue(int superLayer, int sector, int wire, int layer) {
		for (DataPointMultiDimension p : points) {
			if (p.getSuperLayer() == superLayer && p.getSector() == sector && p.getWire() == wire
					&& p.getLayer() == layer) {
				return p.getValue();
			}
		}
		return 0;
	}

	public int getPointXValue(int superLayer, int sector, int wire, int layer) {
		for (DataPointMultiDimension p : points) {
			if (p.getSuperLayer() == superLayer && p.getSector() == sector && p.getWire() == wire) {
				return p.getWire();
			}
		}
		return 0;
	}

	public int getPointYValue(int superLayer, int sector, int wire, int layer) {
		for (DataPointMultiDimension p : points) {
			if (p.getSuperLayer() == superLayer && p.getSector() == sector && p.getWire() == wire) {
				return p.getLayer();
			}
		}
		return 0;
	}

}
