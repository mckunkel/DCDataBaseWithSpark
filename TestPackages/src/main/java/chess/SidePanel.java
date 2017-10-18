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

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SidePanel extends JPanel {

	final static String HORIZONTAL = "horizontal";
	final static String VERTICAL = "vertical";

	public SidePanel(String[] strings, String direction) {
		if (direction.equals(VERTICAL)) {
			setLayout(new GridLayout(8, 0));
		} else {
			setLayout(new GridLayout(0, 8));
		}
		setDoubleBuffered(true);
		for (String string : strings) {
			this.add(new JLabel(string, JLabel.CENTER));
		}

	}
}
