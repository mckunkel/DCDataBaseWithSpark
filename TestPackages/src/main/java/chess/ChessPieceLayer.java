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

import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class ChessPieceLayer extends JComponent {

	private HashMap<PiecePanel, String> panelsMap = new HashMap<>(64);
	final ChessPieceMouseListener listener;

	ChessPieceLayer() {
		super();
		listener = new ChessPieceMouseListener();
		setLayout(new GridLayout(8, 8));
		setDoubleBuffered(true);

		fillPanelsMap();
	}

	private void fillPanelsMap() {
		String[] cols = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };
		int[] rows = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };
		String row, col;
		int rowCount = 7, colCount = 0, trigger = 8;

		for (int i = 0; i < 64; i++) {

			if (trigger == 0) {
				colCount = 0;
				trigger = 8;
				rowCount--;
			}
			col = cols[colCount++];
			row = rows[rowCount] + "";
			trigger--;

			String location = col + row;

			PiecePanel square = createAndAddPiecesWithMouseListener(location);

			panelsMap.put(square, location);

		}
	}

	private PiecePanel createAndAddPiecesWithMouseListener(String location) {
		PiecePanel square = new PiecePanel(location, JLabel.CENTER);
		square.addMouseListener(listener);
		square.setText(location);
		this.add(square);
		return square;
	}
}
