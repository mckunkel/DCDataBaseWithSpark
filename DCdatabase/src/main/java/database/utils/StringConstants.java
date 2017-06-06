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
	public static final String MAIN_MENU_SORT = "Sort";

	public static final String MAIN_MENU_EXIT_TEXT = "Do you really want to exit?";
	public static final String MAIN_MENU_EXIT_TITLE = "Confirm Exit";
	public static final String STATUS_PANEL_TEXT = "mkunkel@fz-juelich.de";

	// For sort query
	public static final String SORT_FORM_TITLE = "Sort By:";
	public static final String SORT_FORM_CANCEL = "Cancel";
	public static final String SORT_FORM_SAVE = "Sort";
	public static final String SORT_FORM_SECTOR = "Sector";
	public static final String SORT_FORM_SUPERLAYER = "Superlayer";
	public static final String SORT_FORM_SUBTITLE = "Add New Student";

	public static final String CHOOSEFILE_FORM_TITLE = "PLEASE CHOOSE A FILE";
	public static final String FORM_OK = "OK";

}
