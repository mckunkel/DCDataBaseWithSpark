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
package database.utils;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;

public class NumberConstants {

	private NumberConstants() {

	}

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int APP_WINDOW_SIZE_WIDTH = (int) screenSize.getWidth() - (int) screenSize.getWidth() / 3;
	public static final int APP_WINDOW_SIZE_HEIGHT = (int) screenSize.getHeight() - (int) screenSize.getHeight() / 3;
	public static final int NUM_OF_COLUMNS = 4;

	public static final int SORT_FORM_WINDOW_SIZE_HEIGHT = APP_WINDOW_SIZE_HEIGHT / 3;
	public static final int SORT_FORM_WINDOW_SIZE_WIDTH = APP_WINDOW_SIZE_WIDTH / 3;
	public static final int SORT_FORM_WINDOW_FIELD_LENGTH = 15;
	public static final int SORT_REMOVE_FORM_WINDOW_SIZE_HEIGHT = 160;
	public static final int OTHER_FORM_WINDOW_FIELD_LENGTH = 15;
	public static final int BORDER_SPACING = 15;
	public static final int CANVAS_UPDATE = 2000;
	public static final int DEFAULT_HIST_SUPERLAYER = 2;
	public static final int DEFAULT_HIST_SECTOR = 2;
	public static final Insets insets = new Insets(0, 0, 0, 0);
	public static final int WINDOW_FIELD_LENGTH = 15;
	public static final int PERCENT_FORM_WINDOW_FIELD_LENGTH = 5;
	public static final double DEFAULT_USER_PERCENTAGE = 5;

}
