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

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class ClassA {
	protected final static int dimesionsY = 800;
	protected final static int dimesionsX = 1000; // !!
	private static JFrame window;
	private static JLayeredPane layeredPane;

	public void init() {
		window = new JFrame("Foo");
		// !! dimesionsX = // some user input

		// !! window.setPreferredSize(new Dimension(dimesionsX, dimesionsY));
		window.setLayout(new BorderLayout());

		layeredPane = new JLayeredPane();
		// !! layeredPane.setBounds(0, 0, dimesionsX, dimesionsY);
		layeredPane.setPreferredSize(new Dimension(dimesionsX, dimesionsY));
		window.add(layeredPane, BorderLayout.CENTER);

		ClassB myGraphic = new ClassB();
		myGraphic.drawGraphic();

		myGraphic.setSize(layeredPane.getPreferredSize());
		myGraphic.setLocation(0, 0);
		// !! layeredPane.add(myGraphic, new Integer(0), 0);

		ClassC mClassC = new ClassC();
		mClassC.drawGraphic();
		mClassC.setSize(new Dimension(dimesionsX / 2, dimesionsY));
		mClassC.setLocation(10, 10);
		// !! layeredPane.add(myGraphic, new Integer(0), 0);
		layeredPane.add(myGraphic, new Integer(0), 0);
		layeredPane.add(mClassC, new Integer(1), 1);

		window.pack();
		window.setVisible(true);
	}

	public static void main(String[] args) {
		new ClassA().init();
	}
}

class ClassB extends JPanel {
	public void drawGraphic() {
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		g.fillRect(10, 10, 100, 100);
	}

}

class ClassC extends JPanel {
	public void drawGraphic() {
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLUE);
		g.fillRect(10, 10, 50, 50);
	}
}