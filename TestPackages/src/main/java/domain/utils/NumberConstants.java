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
package domain.utils;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;

public class NumberConstants {

	private NumberConstants() {

	}

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int APP_WINDOW_SIZE_WIDTH = (int) screenSize.getWidth() - (int) screenSize.getWidth() / 6;
	public static final int APP_WINDOW_SIZE_HEIGHT = (int) screenSize.getHeight() - (int) screenSize.getHeight() / 6;

	public static final int BORDER_SPACING = 15;

	public static final Insets insets = new Insets(0, 0, 0, 0);
	public static final int WINDOW_FIELD_LENGTH = 15;

}
