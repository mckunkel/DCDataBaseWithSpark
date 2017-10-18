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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jlab.groot.base.ColorPalette;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension1D;

@SuppressWarnings("serial")
public class OverLayedPanelNewApproach extends JPanel implements ActionListener {

	private JLayeredPane layeredPane;
	private final int width = 800;
	private final int height = 800;
	private H2F aH2f;

	private int xBins = 112;
	private int yBins = 6;

	private int xMin = 1;
	private int xMax = 113;

	private int yMin = 1;
	private int yMax = 7;

	// private int xBins = 10;
	// private int yBins = 10;
	//
	// private int xMin = 0;
	// private int xMax = 10;
	//
	// private int yMin = 0;
	// private int yMax = 10;
	SidePanel sp1 = new SidePanel("Layer", SidePanel.VERTICAL);
	SidePanel sp2 = new SidePanel("Wire", SidePanel.HORIZONTAL);
	AxisTicks sideTicks1 = new AxisTicks(new String[] { "8", "7", "6", "5", "4", "3", "2", "1" }, SidePanel.VERTICAL);
	AxisTicks sideTicks2 = new AxisTicks(new String[] { "A", "B", "C", "D", "E", "F", "G", "H" }, SidePanel.HORIZONTAL);

	public OverLayedPanelNewApproach() {
		setLayout(new BorderLayout());
		init();
		addPanels();

		add(layeredPane, BorderLayout.CENTER);
		// add(sideTicks2, BorderLayout.SOUTH);
		// add(sideTicks1, BorderLayout.WEST);
		add(sp1, BorderLayout.WEST);
		add(sp2, BorderLayout.SOUTH);
	}

	private void init() {
		this.layeredPane = new JLayeredPane();
		this.layeredPane.setPreferredSize(new Dimension(width, height));
		// this.layeredPane.setBorder(BorderFactory.createTitledBorder("Histogram
		// should go here"));
		this.layeredPane.setLayout(new GridBagLayout());
		this.aH2f = makeH2F();
	}

	private void addPanels() {
		// addButtonsToPane();
		canvas();
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

	private void canvas() {
		ColorPalette palette = new ColorPalette();
		Insets noPadding = new Insets(0, 0, 0, 0);

		int xMax = aH2f.getXAxis().getNBins();
		int yMax = aH2f.getYAxis().getNBins();
		IDataSet dataSet = aH2f;
		Dimension1D dataRegionZ = this.growDataRegion(dataSet);

		for (int i = 0; i < xMax; i++) {
			for (int j = 0; j < yMax; j++) {
				final int x = i;
				final int y = j;
				double dataWeight = dataSet.getData(i, j);
				Color weightColor = palette.getColor3D(dataWeight, dataRegionZ.getMax(), false);
				JLabel label = createColoredLabel("", weightColor);
				label.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (SwingUtilities.isRightMouseButton(e)) {
							System.out.println("right clicked");
						} else {
							System.out.println("left clicked at " + x + " " + y);
						}
					}
				});
				PanelConstraints.addComponent(layeredPane, label, i, j, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, noPadding, 0, 0);
			}
		}
	}

	private JLabel createColoredLabel(String text, Color color) {
		JLabel label = new JLabel(text);
		label.setVerticalAlignment(JLabel.TOP);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setOpaque(true);
		label.setBackground(color);
		label.setForeground(Color.black);
		// label.setBorder(BorderFactory.createLineBorder(Color.black));
		label.setPreferredSize(new Dimension(240, 240));
		return label;
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

	private Dimension1D growDataRegion(IDataSet dataSet) {
		Dimension1D dataRegion = new Dimension1D();
		dataRegion.setMinMax(dataSet.getData(0, 0), dataSet.getData(0, 0));
		for (int xd = 0; xd < dataSet.getDataSize(0); xd++) {
			for (int yd = 0; yd < dataSet.getDataSize(1); yd++) {
				dataRegion.grow(dataSet.getData(xd, yd));
			}
		}
		return dataRegion;
	}

	public void actionPerformed(ActionEvent e) {

	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Testing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = new OverLayedPanelNewApproach();
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

	private class RotateLabel extends JLabel {

		public RotateLabel(String text, int placement) {
			super(text, placement);
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D gx = (Graphics2D) g;
			gx.rotate(-Math.PI / 2.0, getX() + getWidth() / 2, getY() + getHeight() / 2); // Rotate
																							// 0.2
																							// radians
																							// around
																							// the
																							// center
																							// of
																							// the
																							// label
			super.paintComponent(g);
		}
	}

	class SidePanel extends JPanel {

		final static String HORIZONTAL = "horizontal";
		final static String VERTICAL = "vertical";

		public SidePanel(String string, String direction) {
			if (direction.equals(VERTICAL)) {
				setLayout(new BorderLayout());
				this.add(new RotateLabel(string, JLabel.CENTER));

			} else {
				setLayout(new BorderLayout());
				this.add(new JLabel(string, JLabel.CENTER));

			}
			setDoubleBuffered(true);

		}
	}

	class AxisTicks extends JPanel {

		final static String HORIZONTAL = "horizontal";
		final static String VERTICAL = "vertical";

		public AxisTicks(String[] strings, String direction) {
			if (direction.equals(VERTICAL)) {
				setLayout(new GridLayout(6, 0));
			} else {
				setLayout(new GridLayout(0, 6));
			}
			setDoubleBuffered(true);
			for (String string : strings) {
				this.add(new JLabel(string, JLabel.CENTER));
			}

		}
	}
}
