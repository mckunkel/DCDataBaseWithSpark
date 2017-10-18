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
package domain;

import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;

public class MouseOverrideCanvas {
	private H2F aH2f;
	private EmbeddedCanvas canvas;

	private int xBins = 112;
	private int yBins = 6;

	private int xMin = 1;
	private int xMax = 113;

	private int yMin = 1;
	private int yMax = 7;
	final MouseOverrideCanvasListener listener;

	// private int xBins = 10;
	// private int yBins = 5;
	//
	// private int xMin = 0;
	// private int xMax = 10;
	//
	// private int yMin = 0;
	// private int yMax = 10;

	public MouseOverrideCanvas() {
		listener = new MouseOverrideCanvasListener();

		init();
		canvas();
	}

	public EmbeddedCanvas getCanvas() {
		return this.canvas;
	}

	private void init() {

		// this.canvas = new EmbeddedCanvas() {
		// @Override
		// public void mousePressed(MouseEvent e) {
		// System.out.println("I was clicked");
		//
		// double xMax =
		// this.getPad().getAxisFrame().getAxisX().getDimension().getMax();
		// double xMin =
		// this.getPad().getAxisFrame().getAxisX().getDimension().getMin();
		// double yMax =
		// this.getPad().getAxisFrame().getAxisY().getDimension().getMax();
		// double yMin =
		// this.getPad().getAxisFrame().getAxisY().getDimension().getMin();
		// double yRange = yMax + yMin;
		//
		// double xposNew = e.getX() - xMin;
		// double yposNew = yRange - e.getY();
		//
		// double xSpan = xMax - xMin;
		// double ySpan = yMin - yMax;
		//
		// findBin(xposNew, yposNew, xSpan, ySpan);
		//
		// }
		// };
		this.canvas = new EmbeddedCanvas();
		canvas.addMouseListener(listener);
		this.aH2f = makeH2F();

	}

	// private void findBin(double xposNew, double yposNew, double xSpan, double
	// ySpan) {
	// if (xposNew >= 0.0 && yposNew >= 0.0 && xposNew <= xSpan && yposNew <=
	// ySpan) {
	// Pair<Integer, Integer> bins = getBinFromMouse(xSpan, xposNew, ySpan,
	// yposNew);
	// System.out.println(" in range will calculate " + bins.getLeft() + " yBin
	// " + bins.getRight());
	// } else {
	// System.out.println("not in range. will do nothing");
	// }
	// }
	//
	// private Pair<Integer, Integer> getBinFromMouse(double xSpan, double
	// xposNew, double ySpan, double yposNew) {
	// int xBin = -100;
	// int yBin = -100;
	// double xmouseplace = xSpan / aH2f.getXAxis().getNBins();
	// double ymouseplace = ySpan / aH2f.getYAxis().getNBins();
	//
	// for (int i = 0; i < aH2f.getXAxis().getNBins(); i++) {
	// double divisions = (double) (i + 1) * xmouseplace;
	// if (Math.abs(xposNew - divisions) <= xmouseplace) {
	// xBin = i;
	// }
	// }
	// for (int i = 0; i < aH2f.getYAxis().getNBins(); i++) {
	// double divisions = (double) (i + 1) * ymouseplace;
	// if (Math.abs(yposNew - divisions) <= ymouseplace) {
	// yBin = i;
	// }
	// }
	// Pair<Integer, Integer> retValue = Pair.of(xBin + 1, yBin + 1);
	//
	// return retValue;
	// }

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

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Testing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = new MouseOverrideCanvas().getCanvas();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);
		// frame.setLayout(new BorderLayout());
		// frame.add(new OverLayedPanel(), BorderLayout.CENTER);
		// frame.setLocationRelativeTo(null);

		frame.pack();
		// frame.setSize(1200, 1200);

		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
