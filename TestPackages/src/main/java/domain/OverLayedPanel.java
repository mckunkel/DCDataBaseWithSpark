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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.graphics.EmbeddedPad;

/* 
 * LayeredPaneDemo.java requires
 * images/dukeWaveRed.gif. 
 */
public class OverLayedPanel extends JPanel implements ActionListener {

	private JLayeredPane layeredPane;
	private final int width = 800;
	private final int height = 800;
	private EmbeddedCanvas canvas;
	private H2F aH2f;

	private JPanel buttonPanel;

	// private int xBins = 112;
	// private int yBins = 6;
	//
	// private int xMin = 1;
	// private int xMax = 113;
	//
	// private int yMin = 1;
	// private int yMax = 7;

	private int xBins = 10;
	private int yBins = 5;

	private int xMin = 0;
	private int xMax = 10;

	private int yMin = 0;
	private int yMax = 10;

	public OverLayedPanel() {
		setLayout(new BorderLayout());
		init();
		addPanels();

		add(layeredPane, BorderLayout.CENTER);
	}

	private void init() {
		this.layeredPane = new JLayeredPane();
		this.layeredPane.setPreferredSize(new Dimension(width, height));
		this.layeredPane.setBorder(BorderFactory.createTitledBorder("Histogram should go here"));
		this.layeredPane.setLayout(new BorderLayout());
		// layeredPane.setLayout(new GridBagLayout());

		this.canvas = new EmbeddedCanvas() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("I was clicked");
				MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(e.getComponent(), e, getParent());
				int x = convertMouseEvent.getX();
				int y = convertMouseEvent.getY();
				// convertMouseEvent.getComponent().getParent().
				System.out.println(x + "  " + y);
				System.out.println(convertMouseEvent.getXOnScreen() + "  " + convertMouseEvent.getYOnScreen());
				System.out.println(
						convertMouseEvent.getComponent().getX() + "  " + convertMouseEvent.getComponent().getY());
				convertMouseEvent.getComponent().getLocationOnScreen();
				int pad = canvas.getPadByXY(e.getX(), e.getY());
				System.out.println("pad" + pad + "  " + e.getX() + "  " + e.getY());
				EmbeddedPad pad2 = canvas.getPad();
				if (pad2.getAxisFrame().getFrameDimensions().contains(e.getX(), e.getY())) {
					System.out.println("true");
					System.out.println(pad2.getAxisFrame().getFrameDimensions().getDimension(pad));
				}
				IDataSet dSet = pad2.getDatasetPlotters().get(0).getDataSet();
				int xMax = dSet.getDataSize(0);
				int yMax = dSet.getDataSize(1);

				int xPlace = canvas.getPad().getAxisFrame().getAxisPointX(e.getX());
				int yPlace = canvas.getPad().getAxisFrame().getAxisPointY(e.getY());

				System.out
						.println(dSet.getDataX(xPlace) + "  " + dSet.getDataY(yPlace) + "   " + xPlace + "  " + yPlace);

				int xpos = convertMouseEvent.getComponent().getX() - this.getHeight();
				int ypos = convertMouseEvent.getComponent().getY() - this.getWidth();

				System.out.println(xpos + "  " + ypos);
				Object object = convertMouseEvent.getSource();
				System.out.println(object.getClass());
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			// public void mousePressed(MouseEvent e) {
			// if (SwingUtilities.isRightMouseButton(e)) {
			// MouseEvent convertMouseEvent =
			// SwingUtilities.convertMouseEvent(e.getComponent(), e,
			// getParent());
			// canvas.dispatchEvent(convertMouseEvent);
			// }
			// }
		};
		this.aH2f = makeH2F();

		this.buttonPanel = createButtonPanel();
		canvas();
		// createButtons();
	}

	private void addPanels() {
		// this.layeredPane.add(createHistogramPanel(), new Integer(0));
		// this.layeredPane.add(createButtonPanel(), new Integer(1));
		this.layeredPane.add(canvas, BorderLayout.CENTER, new Integer(1));
		// PanelConstraints.addComponent(layeredPane, canvas, 0, 0, 1, 1, 1.0,
		// 1.0, GridBagConstraints.CENTER,
		// GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		// addButtonsToPane();
		// this.layeredPane.add(buttonPanel, BorderLayout.CENTER, new
		// Integer(0));
		// this.layeredPane.addComponentListener(new ComponentAdapter() {
		// @Override
		// public void componentResized(ComponentEvent e) {
		// Dimension size = layeredPane.getSize(); // get size
		// canvas.setSize(size); // push size through
		// buttonPanel.setSize(size); // push size trhough
		// // otherChildOfLayers.setSize(size); // push size trhough
		// layeredPane.revalidate(); // revalidate to see updates
		// layeredPane.repaint(); // "Always invoke repaint after
		// // revalidate"
		// }
		// });
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

		canvas.setPreferredSize(new Dimension((int) this.layeredPane.getPreferredSize().getWidth(),
				(int) this.layeredPane.getPreferredSize().getHeight()));
		canvas.setAxisTitleSize(10);
		canvas.setAxisFontSize(50);
		canvas.setAxisLabelSize(10);
		canvas.draw(makeH2F());
		// canvas.getPad().getAxisZ().setLog(true);
		return canvas;

	}

	private void addButtonsToPane() {
		ButtonGroup buttons = new ButtonGroup();
		Insets noPadding = new Insets(0, 0, 0, 0);

		int xMax = aH2f.getXAxis().getNBins();
		int yMax = aH2f.getYAxis().getNBins();

		int iI = 0;
		for (int i = 0; i < xMax; i++) {
			for (int j = 0; j < yMax; j++) {
				final int placer = iI + 1;
				JButton freshButton = new JButton();
				freshButton.addActionListener(e -> {
					System.out.println("Button " + placer + " clicked");
				});
				freshButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (SwingUtilities.isRightMouseButton(e)) {
							MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(e.getComponent(), e,
									getParent());
							canvas.dispatchEvent(convertMouseEvent);
						}
					}
				});
				freshButton.setText("Button " + (iI + 1));
				freshButton.setOpaque(true);
				freshButton.setContentAreaFilled(false);
				freshButton.setBorderPainted(false);

				buttons.add(freshButton);

				PanelConstraints.addComponent(layeredPane, freshButton, i, j, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, noPadding, 0, 0);
				iI++;
			}
		}

	}

	private void createButtons() {
		this.buttonPanel = new JPanel();

		ButtonGroup buttons = new ButtonGroup();

		buttonPanel.setLayout(new GridBagLayout());
		Insets noPadding = new Insets(0, 0, 0, 0);

		// buttonPanel.setLayout(new GridLayout(10, 10));
		int xMax = aH2f.getXAxis().getNBins();
		int yMax = aH2f.getYAxis().getNBins();

		int iI = 0;
		for (int i = 0; i < xMax; i++) {
			for (int j = 0; j < yMax; j++) {
				final int placer = iI + 1;
				JButton freshButton = new JButton();
				freshButton.addActionListener(e -> {
					System.out.println("Button " + placer + " clicked");
				});
				freshButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (SwingUtilities.isRightMouseButton(e)) {
							MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(e.getComponent(), e,
									getParent());
							canvas.dispatchEvent(convertMouseEvent);
						}
					}
				});
				freshButton.setText("Button " + (iI + 1));
				freshButton.setOpaque(true);
				freshButton.setContentAreaFilled(false);
				freshButton.setBorderPainted(false);

				buttons.add(freshButton);

				PanelConstraints.addComponent(buttonPanel, freshButton, i, j, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, noPadding, 0, 0);
				iI++;
			}
		}
		buttonPanel.setOpaque(false);
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();

		ButtonGroup buttons = new ButtonGroup();

		buttonPanel.setLayout(new GridBagLayout());
		Insets noPadding = new Insets(0, 0, 0, 0);

		// buttonPanel.setLayout(new GridLayout(10, 10));
		int xMax = aH2f.getXAxis().getNBins();
		int yMax = aH2f.getYAxis().getNBins();

		int iI = 0;
		for (int i = 0; i < xMax; i++) {
			for (int j = 0; j < yMax; j++) {
				final int placer = iI + 1;
				JButton freshButton = new JButton();
				freshButton.addActionListener(e -> {
					System.out.println("Button " + placer + " clicked");
				});
				freshButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (SwingUtilities.isRightMouseButton(e)) {
							MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(e.getComponent(), e,
									getParent());
							canvas.dispatchEvent(convertMouseEvent);
						}
					}
				});
				freshButton.setText("Button " + (iI + 1));
				freshButton.setOpaque(true);
				freshButton.setContentAreaFilled(false);
				freshButton.setBorderPainted(false);

				buttons.add(freshButton);

				PanelConstraints.addComponent(buttonPanel, freshButton, i, j, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, noPadding, 0, 0);
				iI++;
			}
		}
		buttonPanel.setOpaque(false);
		return buttonPanel;
	}

	public void actionPerformed(ActionEvent e) {

	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Testing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = new OverLayedPanel();
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
