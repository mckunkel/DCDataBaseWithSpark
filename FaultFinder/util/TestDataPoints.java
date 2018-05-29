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

public class TestDataPoints {
	private List<DataPointMultiDimension> points;

	public TestDataPoints() {
		this.points = new ArrayList<DataPointMultiDimension>();
	}

	public List<DataPointMultiDimension> getPoints() {
		return points;
	}

	public void putPoint2D(Integer... size) {
		points.add(new DataPointMultiDimension(new Coordinate(size)));
	}

	public void incrementPoint2D(int x) {
		for (DataPointMultiDimension p : points) {
			if (p.getCoordinate(x) == x) {
				p.increment();
			}
		}
	}

	public int getPoint2DValue(int x) {
		for (DataPointMultiDimension p : points) {
			if (p.getCoordinate(x) == x) {
				return p.getValue();
			}
		}
		return 0;
	}
}
