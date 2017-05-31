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

public class StringConstants {

	private StringConstants() {

	}

	// things for spark
	public static final String APP_NAME = "DC Database Management Application";
	public static final String TEMP_DIR = "spark-warehouse";
	public static final String SPARK_MASTER = "local[2]";

	// things for menu
	public static final String MAIN_MENU_FILE = "File";
	public static final String MAIN_MENU_OPEN = "Open";
	public static final String MAIN_MENU_EXIT = "Exit";

	public static final String MAIN_MENU_EXIT_TEXT = "Do you really want to exit?";
	public static final String MAIN_MENU_EXIT_TITLE = "Confirm Exit";
	public static final String STATUS_PANEL_TEXT = "mkunkel@fz-juelich.de";

}
