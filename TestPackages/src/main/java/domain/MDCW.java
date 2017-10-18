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

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MDCW extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MDCW frame = new MDCW();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MDCW() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1013, 551);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 139, 139));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JPanel panel = new TransparentPanel();

		panel.setBackground(new Color(0, 0, 0, 50));
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				panel.setBorder(new LineBorder(new Color(255, 255, 255), 5));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				panel.setBorder(null);
			}
		});
		panel.setBounds(360, 155, 215, 215);
		contentPane.add(panel);

		final JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(0, 0, 0));
		panel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				panel_1.setBorder(new LineBorder(new Color(255, 255, 255), 5));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				panel_1.setBorder(null);
			}
		});
		panel_1.setBounds(84, 155, 215, 215);
		contentPane.add(panel_1);
	}

	private class TransparentPanel extends JPanel {
		{
			setOpaque(false);
		}

		public void paintComponent(Graphics g) {
			g.setColor(getBackground());
			Rectangle r = g.getClipBounds();
			g.fillRect(r.x, r.y, r.width, r.height);
			super.paintComponent(g);
		}
	}
}
