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
package chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

class ChessBoard extends JPanel {

	public ChessBoard() {
		super(new BorderLayout(), true);

		this.add(populateBoard(Color.white, Color.black), BorderLayout.CENTER);
	}

	private JPanel populateBoard(Color c1, Color c2) {
		JPanel panel = new JPanel(new GridLayout(8, 8));
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				JPanel square = new JPanel(new BorderLayout());
				square.setBackground((i + j) % 2 == 0 ? c1 : c2);
				panel.add(square);
			}
		}
		return panel;
	}
}
