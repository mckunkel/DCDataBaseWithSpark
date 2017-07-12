package testpackages;
/******************************************************************************
 *  Compilation:  javac GrayPicture.java
 *  Execution:    java GrayPicture filename
 *
 *  Data type for manipulating individual pixels of an image. The original
 *  image can be read from a file in JPEG, GIF, or PNG format, or the
 *  user can create a blank image of a given size. Includes methods for
 *  displaying the image in a window on the screen or saving to a file.
 *
 *  % java GrayPicture image.jpg
 *
 *  Remarks
 *  -------
 *   - pixel (0, 0) is upper left hand corner
 *
 *   - converts color image to grayscale
 *
 ******************************************************************************/

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public final class GrayPicture implements ActionListener {
	private BufferedImage image; // the rasterized image
	private JFrame frame; // on-screen view
	private String filename; // name of file

	// create a blank width-by-height image
	public GrayPicture(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		filename = width + "-by-" + height;
	}

	// create an image by reading in the PNG, GIF, or JPEG from a filename
	public GrayPicture(String filename) {
		this.filename = filename;
		BufferedImage colorImage;

		try {
			// try to read from file in working directory
			File file = new File(filename);
			if (file.isFile()) {
				colorImage = ImageIO.read(file);
			}

			// now try to read from file in same directory as this .class file
			else {
				URL url = getClass().getResource(filename);
				if (url == null)
					url = new URL(filename);
				colorImage = ImageIO.read(url);
			}
		} catch (IOException e) {
			// e.printStackTrace();
			throw new RuntimeException("Could not open file: " + filename);
		}

		// check that image was read in
		if (colorImage == null) {
			throw new RuntimeException("Invalid image file: " + filename);
		}

		// convert to grayscale
		int w = colorImage.getWidth(null);
		int h = colorImage.getHeight(null);
		image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color color = new Color(colorImage.getRGB(i, j));
				Color gray = Luminance.toGray(color);
				image.setRGB(i, j, gray.getRGB());
			}
		}
	}

	// create an image by reading in the PNG, GIF, or JPEG from a file
	public GrayPicture(File file) {
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not open file: " + file);
		}
		if (image == null) {
			throw new RuntimeException("Invalid image file: " + file);
		}
		filename = file.getName();
	}

	// to embed in a JPanel, JFrame or other GUI widget
	public JLabel getJLabel() {
		if (image == null)
			return null; // no image available
		ImageIcon icon = new ImageIcon(image);
		return new JLabel(icon);
	}

	// view on-screen, creating new frame if necessary
	public void show() {

		// create the GUI for viewing the image if needed
		if (frame == null) {
			frame = new JFrame();

			JMenuBar menuBar = new JMenuBar();
			JMenu menu = new JMenu("File");
			menuBar.add(menu);
			JMenuItem menuItem1 = new JMenuItem(" Save...   ");
			menuItem1.addActionListener(this);
			menuItem1.setAccelerator(
					KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menu.add(menuItem1);
			frame.setJMenuBar(menuBar);

			frame.setContentPane(getJLabel());
			// f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setTitle(filename);
			frame.setResizable(false);
			frame.pack();
			frame.setVisible(true);
		}

		// draw
		frame.repaint();
	}

	// accessor methods
	public int height() {
		return image.getHeight(null);
	}

	public int width() {
		return image.getWidth(null);
	}

	// return Color of pixel (i, j)
	public Color get(int i, int j) {
		Color color = new Color(image.getRGB(i, j));
		return Luminance.toGray(color);
	}

	// change color of pixel (i, j) to c
	public void set(int i, int j, Color c) {
		if (c == null)
			throw new NullPointerException("can't set Color to null");
		Color gray = Luminance.toGray(c);
		image.setRGB(i, j, gray.getRGB());
	}

	// save to given filename - suffix must be png, jpg, or gif
	public void save(String name) {
		save(new File(name));
	}

	// save to given filename - suffix must be png, jpg, or gif
	public void save(File file) {
		filename = file.getName();
		String suffix = filename.substring(filename.lastIndexOf('.') + 1);
		suffix = suffix.toLowerCase();
		if (suffix.equals("jpg") || suffix.equals("png")) {
			try {
				ImageIO.write(image, suffix, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Error: filename must end in .jpg or .png");
		}
	}

	// open a save dialog when the user selects "Save As" from the menu
	public void actionPerformed(ActionEvent e) {
		FileDialog chooser = new FileDialog(frame, "Use a .png or .jpg extension", FileDialog.SAVE);
		chooser.setVisible(true);
		String name = chooser.getFile();
		if (name != null) {
			save(chooser.getDirectory() + File.separator + chooser.getFile());
		}
	}

	// test client: read in input file and display
	public static void main(String[] args) {
		GrayPicture pic = new GrayPicture("data/atest.png");
		pic.show();
	}

}
