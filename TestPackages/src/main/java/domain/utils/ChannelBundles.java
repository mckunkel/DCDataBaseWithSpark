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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class ChannelBundles {
	private static List<Pair<Integer, Integer>> hvChannelSegmentation = new ArrayList<Pair<Integer, Integer>>();;

	private static void setuphvChannelSegmentation() {
		hvChannelSegmentation.add(Pair.of(1, 8));
		hvChannelSegmentation.add(Pair.of(9, 16));
		hvChannelSegmentation.add(Pair.of(17, 24));
		hvChannelSegmentation.add(Pair.of(25, 32));
		hvChannelSegmentation.add(Pair.of(33, 40));
		hvChannelSegmentation.add(Pair.of(41, 48));
		hvChannelSegmentation.add(Pair.of(49, 56));
		hvChannelSegmentation.add(Pair.of(57, 64));
		hvChannelSegmentation.add(Pair.of(65, 72));
		hvChannelSegmentation.add(Pair.of(73, 80));
		hvChannelSegmentation.add(Pair.of(81, 96));
		hvChannelSegmentation.add(Pair.of(97, 112));

	}

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

	public static Pair<Integer, Integer> findWireRange(int wire) {
		setuphvChannelSegmentation();
		Pair<Integer, Integer> retValuePair = Pair.of(0, 0);
		for (Pair<Integer, Integer> pair : hvChannelSegmentation) {
			if ((wire + 1) <= pair.getRight() && (wire + 1) >= pair.getLeft()) {
				retValuePair = pair;
			}
		}
		return retValuePair;
	}
}
