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
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

public class ChessBoardTest {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				Dimension boardSize = new Dimension(600, 600);

				JFrame frame = new JFrame("Chess JLayeredPane Test");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(true);

				Container contentPane = frame.getContentPane();

				ChessBoard chessBoard = new ChessBoard();
				SidePanel sp1 = new SidePanel(new String[] { "8", "7", "6", "5", "4", "3", "2", "1" },
						SidePanel.VERTICAL);
				SidePanel sp2 = new SidePanel(new String[] { "A", "B", "C", "D", "E", "F", "G", "H" },
						SidePanel.HORIZONTAL);

				// adding these 2 side panels messes up the layout
				chessBoard.add(sp1, BorderLayout.WEST);
				chessBoard.add(sp2, BorderLayout.SOUTH);

				chessBoard.setPreferredSize(boardSize);
				chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

				ChessPieceLayer chessPieceLayer = new ChessPieceLayer();

				// chessPieceLayer.setPreferredSize(boardSize);
				// chessPieceLayer.setBounds(0, 0, boardSize.width,
				// boardSize.height);

				// i've tried resizing to make up for the side panels but no
				// result
				chessPieceLayer.setPreferredSize(new Dimension(600 - sp1.getWidth(), 600 - sp2.getHeight()));
				chessPieceLayer.setBounds(0 + sp1.getWidth(), 0 + sp2.getHeight(), 600 - sp1.getWidth(),
						600 - sp2.getHeight());

				JLayeredPane jLayeredPane = new JLayeredPane();
				jLayeredPane.setPreferredSize(boardSize);

				jLayeredPane.add(chessBoard, JLayeredPane.FRAME_CONTENT_LAYER);
				jLayeredPane.add(chessPieceLayer, JLayeredPane.MODAL_LAYER);

				contentPane.add(jLayeredPane);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}
