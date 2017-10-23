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

import java.util.Random;

import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;

import domain.utils.FuseBundles;
import domain.utils.PinBundles;
import domain.utils.SignalConnectors;

public class MouseOverrideCanvas {
	private EmbeddedCanvas canvas = null;;

	private int xBins = 112;
	private int yBins = 6;

	private int xMin = 1;
	private int xMax = 113;

	private int yMin = 1;
	private int yMax = 7;
	final MouseOverrideCanvasListener listener;

	public MouseOverrideCanvas() {
		listener = new MouseOverrideCanvasListener();
		init();
		canvas();
	}

	public EmbeddedCanvas getCanvas() {
		return this.canvas;
	}

	private void init() {
		PinBundles.setupBundles();
		SignalConnectors.setupBundles();
		FuseBundles.setupBundles();

		this.canvas = new EmbeddedCanvas();
		canvas.addMouseListener(listener);
		canvas.addMouseMotionListener(listener);
	}

	private H2F makeH2F() {
		Random r = new Random();
		H2F aH2f = new H2F("test", xBins, xMin, xMax, yBins, yMin, yMax);
		for (int x = 0; x < aH2f.getXAxis().getNBins(); x++) {
			for (int y = 0; y < aH2f.getYAxis().getNBins(); y++) {
				double randomValue = 1.0 + (25.0 - 1.0) * r.nextDouble();

				aH2f.setBinContent(x, y, randomValue);
			}
		}
		return aH2f;
	}

	private EmbeddedCanvas canvas() {

		canvas.setAxisTitleSize(10);
		canvas.setAxisFontSize(50);
		canvas.setAxisLabelSize(10);
		canvas.draw(makeH2F());
		// canvas.getPad().getAxisZ().setLog(true);
		return canvas;

	}
}
