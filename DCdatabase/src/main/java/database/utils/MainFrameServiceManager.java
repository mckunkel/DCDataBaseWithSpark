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

import database.service.MainFrameService;
import database.service.MainFrameServiceImpl;

public enum MainFrameServiceManager {
	INSTANCE;
	private static MainFrameService mainFrameService = null;

	private static void getService() {
		mainFrameService = new MainFrameServiceImpl();
	}

	public static MainFrameService getSession() {
		if (mainFrameService == null) {
			getService();
		}
		return mainFrameService;
	}

}
