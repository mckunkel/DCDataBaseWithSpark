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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.border.BevelBorder;

public class ChessPieceMouseListener implements MouseListener {

	int counter = 0;
	PiecePanel this_pp, prev_pp;

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		PiecePanel pp = (PiecePanel) e.getComponent();
		if (counter == 0) {
			this_pp = pp;
			this_pp.setBorder(new BevelBorder(0, Color.green, Color.green));
			JOptionPane.showMessageDialog(null, "From " + pp.getPieceLocation());
			counter = 1;
		} else {
			prev_pp = this_pp;
			this_pp = pp;
			prev_pp.setBorder(null);
			JOptionPane.showMessageDialog(null, "To " + pp.getPieceLocation());
			counter = 0;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}