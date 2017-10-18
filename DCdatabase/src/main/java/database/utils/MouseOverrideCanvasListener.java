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
package database.utils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.apache.commons.lang3.tuple.Pair;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.graphics.EmbeddedCanvas;

public class MouseOverrideCanvasListener implements MouseListener {
	EmbeddedCanvas canvas;

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("I was clicked");
		canvas = (EmbeddedCanvas) e.getComponent();
		double xMax = canvas.getPad().getAxisFrame().getAxisX().getDimension().getMax();
		double xMin = canvas.getPad().getAxisFrame().getAxisX().getDimension().getMin();
		double yMax = canvas.getPad().getAxisFrame().getAxisY().getDimension().getMax();
		double yMin = canvas.getPad().getAxisFrame().getAxisY().getDimension().getMin();
		double yRange = yMax + yMin;

		double xposNew = e.getX() - xMin;
		double yposNew = yRange - e.getY();

		double xSpan = xMax - xMin;
		double ySpan = yMin - yMax;

		findBin(xposNew, yposNew, xSpan, ySpan);

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	private void findBin(double xposNew, double yposNew, double xSpan, double ySpan) {
		if (xposNew >= 0.0 && yposNew >= 0.0 && xposNew <= xSpan && yposNew <= ySpan) {
			Pair<Integer, Integer> bins = getBinFromMouse(xSpan, xposNew, ySpan, yposNew);
			System.out.println(" in range will calculate " + bins.getLeft() + " yBin " + bins.getRight());
		} else {
			System.out.println("not in range. will do nothing");
		}
	}

	private Pair<Integer, Integer> getBinFromMouse(double xSpan, double xposNew, double ySpan, double yposNew) {
		int xBin = -100;
		int yBin = -100;
		IDataSet iSet = canvas.getPad().getDatasetPlotters().get(0).getDataSet();
		int xBins = iSet.getDataSize(0);
		int yBins = iSet.getDataSize(1);

		double xmouseplace = xSpan / xBins;
		double ymouseplace = ySpan / yBins;

		for (int i = 0; i < xBins; i++) {
			double divisions = (double) (i + 1) * xmouseplace;
			if (Math.abs(xposNew - divisions) <= xmouseplace) {
				xBin = i;
			}
		}
		for (int i = 0; i < yBins; i++) {
			double divisions = (double) (i + 1) * ymouseplace;
			if (Math.abs(yposNew - divisions) <= ymouseplace) {
				yBin = i;
			}
		}
		Pair<Integer, Integer> retValue = Pair.of(xBin + 1, yBin + 1);

		return retValue;
	}
}
