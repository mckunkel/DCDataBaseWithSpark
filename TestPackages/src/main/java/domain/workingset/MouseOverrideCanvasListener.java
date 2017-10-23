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
package domain.workingset;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.graphics.EmbeddedCanvas;

import domain.utils.FuseBundles;
import domain.utils.PinBundles;
import domain.utils.SignalConnectors;

public class MouseOverrideCanvasListener implements MouseListener, MouseMotionListener {
	private EmbeddedCanvas canvas;
	private H2F mouseH2F;
	private IDataSet ds;

	private double xMax;
	private double xMin;
	private double yMax;
	private double yMin;
	private double yRange;
	private double xSpan;
	private double ySpan;

	private int xBins;
	private int yBins;
	private MainFrameService mainFrameService = null;

	public MouseOverrideCanvasListener() {
		this.mainFrameService = MainFrameServiceManager.getSession();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("I was clicked");
		double xposNew = e.getX() - xMin;
		double yposNew = yRange - e.getY();
		findBin(xposNew, yposNew);
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		canvas = (EmbeddedCanvas) e.getComponent();
		ds = canvas.getPad(0).getDatasetPlotters().get(0).getDataSet();
		xBins = ds.getDataSize(0);
		yBins = ds.getDataSize(1);

		xMax = canvas.getPad().getAxisFrame().getAxisX().getDimension().getMax();
		xMin = canvas.getPad().getAxisFrame().getAxisX().getDimension().getMin();
		yMax = canvas.getPad().getAxisFrame().getAxisY().getDimension().getMax();
		yMin = canvas.getPad().getAxisFrame().getAxisY().getDimension().getMin();
		yRange = yMax + yMin;
		xSpan = xMax - xMin;
		ySpan = yMin - yMax;

		int xMinx = 1;
		int xMaxx = 113;

		int yMiny = 1;
		int yMaxy = 7;

		mouseH2F = new H2F("", xBins, xMinx, xMaxx, yBins, yMiny, yMaxy);
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	private void findBin(double xpos, double ypos) {
		if (inBounds(xpos, ypos)) {
			Pair<Integer, Integer> bins = getBinFromMouse(xpos, ypos);
			System.out.println(" xBin =  " + bins.getLeft() + " yBin = " + bins.getRight());
		} else {
			System.out.println("not in range. will do nothing");
		}
	}

	private Pair<Integer, Integer> getBinFromMouse(double xposNew, double yposNew) {
		int xBin = -100;
		int yBin = -100;

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

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		double xpos = e.getX() - xMin;
		double ypos = yRange - e.getY();

		Pair<Integer, Integer> aPair = getBinFromMouse(xpos, ypos);
		int xBin = aPair.getLeft();
		int yBin = aPair.getRight();
		if (inBounds(xpos, ypos)) {
			drawDefinedFault(this.mainFrameService.getFault(), xBin, yBin);
		}

	}

	private void drawDefinedFault(int faultType, int xBin, int yBin) {
		switch (faultType) {
		case 0:
			drawOverLayedChannelHist(PinBundles.findWireRange(xBin), yBin);
			break;
		case 1:
			drawOverLayedPinHist(PinBundles.findWireRange(xBin), yBin);
			break;
		case 2:
			drawOverLayedFuseHist(FuseBundles.getBundle(xBin, yBin));
			break;
		case 3:
			drawOverLayedSignalHist(SignalConnectors.getBundle(xBin, yBin));
			break;
		case 4:
			drawOverLayedHist(xBin, yBin);
			break;
		case 5:
			drawOverLayedHist(xBin, yBin);
			break;
		default:
			break;
		}
	}

	private boolean inBounds(double xpos, double ypos) {
		boolean retValue = false;
		if (xpos >= 0.0 && ypos >= 0.0 && xpos <= xSpan && ypos <= ySpan) {
			retValue = true;
		}
		return retValue;

	}

	private void drawOverLayedHist(int xBin, int yBin) {

		for (int i = 0; i < xBins; i++) {
			for (int j = 0; j < yBins; j++) {
				// System.out.println(xBin + " " + yBin + " " + i + " " + j);
				if (i == xBin - 1 && j == yBin - 1) {
					mouseH2F.setBinContent(i, j, 0.0);
				} else {
					mouseH2F.setBinContent(i, j, ds.getData(i, j));
				}

			}

		}

		canvas.draw(mouseH2F, "same");
		canvas.update();
	}

	private void drawOverLayedPinHist(Pair<Integer, Integer> xPair, int yBin) {

		for (int i = 0; i < xBins; i++) {
			for (int j = 0; j < yBins; j++) {
				mouseH2F.setBinContent(i, j, ds.getData(i, j));
			}

		}
		for (int i = xPair.getLeft(); i <= xPair.getRight(); i++) {
			mouseH2F.setBinContent(i - 1, yBin - 1, 0.0);

		}

		canvas.draw(mouseH2F, "same");
		canvas.update();
	}

	private void drawOverLayedChannelHist(Pair<Integer, Integer> xPair, int yBin) {

		for (int i = 0; i < xBins; i++) {
			for (int j = 0; j < yBins; j++) {
				mouseH2F.setBinContent(i, j, ds.getData(i, j));
			}

		}
		for (int i = xPair.getLeft(); i <= xPair.getRight(); i++) {
			for (int j = 0; j < yBins; j++) {

				mouseH2F.setBinContent(i - 1, j, 0.0);
			}

		}

		canvas.draw(mouseH2F, "same");
		canvas.update();
	}

	private void drawOverLayedSignalHist(Map<Integer, Pair<Integer, Integer>> aMap) {

		for (int i = 0; i < xBins; i++) {
			for (int j = 0; j < yBins; j++) {
				mouseH2F.setBinContent(i, j, ds.getData(i, j));
			}
		}

		for (int j = 0; j < yBins; j++) {
			Pair<Integer, Integer> xPair = aMap.get(j + 1);
			for (int i = xPair.getLeft(); i <= xPair.getRight(); i++) {
				mouseH2F.setBinContent(i - 1, j, 0.0);
			}

		}

		canvas.draw(mouseH2F, "same");
		canvas.update();
	}

	private void drawOverLayedFuseHist(Map<Integer, Pair<Integer, Integer>> aMap) {

		for (int i = 0; i < xBins; i++) {
			for (int j = 0; j < yBins; j++) {
				mouseH2F.setBinContent(i, j, ds.getData(i, j));
			}
		}

		for (int j = 0; j < yBins; j++) {
			Pair<Integer, Integer> xPair = aMap.get(j + 1);
			for (int i = xPair.getLeft(); i <= xPair.getRight(); i++) {
				mouseH2F.setBinContent(i - 1, j, 0.0);
			}

		}

		canvas.draw(mouseH2F, "same");
		canvas.update();
	}

}
