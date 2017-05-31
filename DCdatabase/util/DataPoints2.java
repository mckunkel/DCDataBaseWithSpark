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

public class DataPoints2 {
	private List<DataPoint2D> points;

	public DataPoints2() {
		this.points = new ArrayList<DataPoint2D>();
	}

	public List<DataPoint2D> getPoints() {
		return points;
	}

	public void putPoint2D(int wire, int layer) {
		points.add(new DataPoint2D(wire, layer));
	}

	public void incrementPoint2D(int wire, int layer) {
		for (DataPoint2D p : points) {
			if (p.getWire() == wire && p.getLayer() == layer) {
				p.increment();
			}
		}
	}

	public int getPoint2DValue(int wire, int layer) {
		for (DataPoint2D p : points) {
			if (p.getWire() == wire && p.getLayer() == layer) {
				return p.getValue();
			}
		}
		return 0;
	}

	public int getPointXValue(int wire, int layer) {
		for (DataPoint2D p : points) {
			if (p.getWire() == wire && p.getLayer() == layer) {
				return p.getWire();
			}
		}
		return 0;
	}

	public int getPointYValue(int wire, int layer) {
		for (DataPoint2D p : points) {
			if (p.getWire() == wire && p.getLayer() == layer) {
				return p.getLayer();
			}
		}
		return 0;
	}

}
