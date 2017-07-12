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

import org.apache.commons.lang3.tuple.Pair;

public class ChannelBundles {

	public static Pair<Integer, Integer> switchChannelBundle(int placer) {
		switch (placer) {
		case 1:
			return Pair.of(1, 8);
		case 2:
			return Pair.of(9, 16);
		case 3:
			return Pair.of(17, 24);
		case 4:
			return Pair.of(25, 32);
		case 5:
			return Pair.of(33, 48);
		case 6:
			return Pair.of(49, 64);
		case 7:
			return Pair.of(65, 80);
		case 8:
			return Pair.of(81, 112);
		default:
			return null;
		}
	}
}
