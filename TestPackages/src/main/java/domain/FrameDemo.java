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
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/* 
 * LayeredPaneDemo.java requires
 * images/dukeWaveRed.gif. 
 */
public class FrameDemo extends JPanel {

	private JLayeredPane layeredPane;
	private final int width = 800;
	private final int height = 800;
	private String[] layerStrings = { "Yellow (0)", "Magenta (1)", "Cyan (2)", "Red (3)", "Green (4)", "Blue (5)" };
	private Color[] layerColors = { Color.yellow, Color.magenta, Color.cyan, Color.red, Color.green, Color.blue };

	private JPanel histogramPanel;
	private JPanel buttonPanel;

	public FrameDemo() {
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

		this.histogramPanel = createHistogramPanel();
		this.buttonPanel = createButtonPanel();

	}

	private void addPanels() {
		// this.layeredPane.add(createHistogramPanel(), new Integer(0));
		// this.layeredPane.add(createButtonPanel(), new Integer(1));
		this.layeredPane.add(histogramPanel, BorderLayout.CENTER, new Integer(1));
		this.layeredPane.add(buttonPanel, BorderLayout.CENTER, new Integer(0));
		this.layeredPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension size = layeredPane.getSize(); // get size
				histogramPanel.setSize(size); // push size through
				buttonPanel.setSize(size); // push size trhough
				// otherChildOfLayers.setSize(size); // push size trhough
				layeredPane.revalidate(); // revalidate to see updates
				layeredPane.repaint(); // "Always invoke repaint after
										// revalidate"
			}
		});
	}

	private JPanel createHistogramPanel() {

		JPanel histpanel = new JPanel();

		histpanel.setLayout(new GridLayout(2, 3));
		for (int i = 0; i < layerStrings.length; i++) {
			JLabel label = createColoredLabel(layerStrings[i], layerColors[i]);
			histpanel.add(label);
		}
		histpanel.setOpaque(false);
		histpanel.setBounds(10, 10, width, height);
		return histpanel;
	}

	private JLabel createColoredLabel(String text, Color color) {
		JLabel label = new JLabel("");
		label.setVerticalAlignment(JLabel.TOP);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setOpaque(true);
		label.setBackground(color);
		label.setForeground(Color.black);
		label.setBorder(BorderFactory.createLineBorder(Color.black));
		label.setPreferredSize(new Dimension(120, 120));
		return label;
	}

	private JPanel createButtonPanel() {
		ButtonGroup buttons = new ButtonGroup();

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 3));
		for (int i = 0; i < 6; i++) {
			final int placer = i + 1;
			JButton freshButton = new JButton();
			freshButton.addActionListener(e -> {
				System.out.println("Button " + placer + " clicked");
			});

			freshButton.setText("Button " + (i + 1));
			freshButton.setOpaque(true);
			freshButton.setContentAreaFilled(false);
			freshButton.setBorderPainted(false);
			freshButton.setBounds(new Rectangle(132, 75 + (i * 20), 40, 20));
			buttonPanel.add(freshButton, null);
			buttons.add(freshButton);

		}
		buttonPanel.setOpaque(false);

		buttonPanel.setBounds(10, 10, width, height);
		return buttonPanel;
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Testing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = new FrameDemo();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);
		frame.setLocationRelativeTo(null);

		frame.pack();

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
