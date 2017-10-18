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

import java.awt.Graphics;

import javax.swing.JLabel;

class PiecePanel extends JLabel {

	private String location;

	public PiecePanel(String text, int horizontalAlignment) {
		super("", horizontalAlignment);
		this.location = text;
	}

	PiecePanel(String location) {
		this.location = location;
	}

	public String getPieceLocation() {
		return location;
	}

	public void setPieceLocation(String location) {
		this.location = location;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
