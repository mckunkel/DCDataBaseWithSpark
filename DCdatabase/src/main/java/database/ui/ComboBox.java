package database.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * ComboBoxDemo.java uses these additional files:
 *   images/Bird.gif
 *   images/Cat.gif
 *   images/Dog.gif
 *   images/Rabbit.gif
 *   images/Pig.gif
 */
public class ComboBox extends JPanel implements ActionListener {
	JLabel picture;

	public ComboBox() {
		super(new BorderLayout());

		String[] numberStrings = { "1", "2", "3", "4", "5", "6" };

		// Create the combo box, select the item at index 4.
		// Indices start at 0, so 4 specifies the pig.
		JComboBox numList = new JComboBox(numberStrings);
		numList.setSelectedIndex(0);
		numList.addActionListener(this);

		// Set up the picture.
		picture = new JLabel();
		picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
		picture.setHorizontalAlignment(JLabel.CENTER);
		updateLabel(numberStrings[numList.getSelectedIndex()]);
		picture.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		// The preferred size is hard-coded to be the width of the
		// widest image and the height of the tallest image + the border.
		// A real program would compute this.
		picture.setPreferredSize(new Dimension(177, 122 + 10));

		// Lay out the demo.
		add(numList, BorderLayout.PAGE_START);
		add(picture, BorderLayout.PAGE_END);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

	/** Listens to the combo box. */
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		String petName = (String) cb.getSelectedItem();
		updateLabel(petName);
	}

	protected void updateLabel(String name) {
		ImageIcon icon = createImageIcon("images/" + name + ".gif");
		picture.setIcon(icon);
		picture.setToolTipText("A drawing of a " + name.toLowerCase());
		if (icon != null) {
			picture.setText(null);
		} else {
			picture.setText("Image not found");
		}
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = ComboBox.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("ComboBoxDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent newContentPane = new ComboBox();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
