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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class PinBundles {
	private static List<Pair<Integer, Integer>> hvPinSegmentation = new ArrayList<Pair<Integer, Integer>>();;

	private static void setuphvPinSegmentation() {
		hvPinSegmentation.add(Pair.of(1, 8));
		hvPinSegmentation.add(Pair.of(9, 16));
		hvPinSegmentation.add(Pair.of(17, 24));
		hvPinSegmentation.add(Pair.of(25, 32));
		hvPinSegmentation.add(Pair.of(33, 40));
		hvPinSegmentation.add(Pair.of(41, 48));
		hvPinSegmentation.add(Pair.of(49, 56));
		hvPinSegmentation.add(Pair.of(57, 64));
		hvPinSegmentation.add(Pair.of(65, 72));
		hvPinSegmentation.add(Pair.of(73, 80));
		hvPinSegmentation.add(Pair.of(81, 96));
		hvPinSegmentation.add(Pair.of(97, 112));

	}

	public static String switchBundle(int placer) {
		setuphvPinSegmentation();
		switch (placer) {
		case 1:
			return "A";
		case 2:
			return "B";
		case 3:
			return "C";
		case 4:
			return "D";
		case 5:
			return "E";
		case 6:
			return "F";
		case 7:
			return "G";
		case 8:
			return "H";
		case 9:
			return "I";
		case 10:
			return "J";
		case 11:
			return "K";
		case 12:
			return "L";
		default:
			return null;
		}
	}

	public static Pair<Integer, Integer> bundleToPair(int bundle) {
		setuphvPinSegmentation();
		switch (bundle) {
		case 1:
			return hvPinSegmentation.get(0);
		case 2:
			return hvPinSegmentation.get(1);
		case 3:
			return hvPinSegmentation.get(2);
		case 4:
			return hvPinSegmentation.get(3);
		case 5:
			return hvPinSegmentation.get(4);
		case 6:
			return hvPinSegmentation.get(5);
		case 7:
			return hvPinSegmentation.get(6);
		case 8:
			return hvPinSegmentation.get(7);
		case 9:
			return hvPinSegmentation.get(8);
		case 10:
			return hvPinSegmentation.get(9);
		case 11:
			return hvPinSegmentation.get(10);
		case 12:
			return hvPinSegmentation.get(11);
		default:
			return null;
		}
	}

	private static int findPlacer(Pair<Integer, Integer> pair) {

		int placer = 0;
		for (Pair<Integer, Integer> temp : hvPinSegmentation) {
			placer++;
			if (temp.equals(pair)) {
				return placer;
			}
		}
		return placer;

	}

	public static String findBundle(int wire) {
		setuphvPinSegmentation();
		int placer = 0;
		// Pair<Integer, Integer> pairs = hvPinSegmentation.stream().filter(x ->
		// x.getLeft() >= wire)
		// .filter(x -> x.getRight() <= wire).findAny().orElse(null);
		//
		// System.out.println(pairs.getLeft() + " " + pairs.getRight() + "
		// Lambda return this");
		for (Pair<Integer, Integer> pair : hvPinSegmentation) {
			// placer++;
			if ((wire + 1) <= pair.getRight() && (wire + 1) >= pair.getLeft()) {
				placer = findPlacer(pair);
				System.out.println(pair.getLeft() + "  " + pair.getRight() + " function return this");

			}
		}

		switch (placer) {
		case 1:
			return "A";
		case 2:
			return "B";
		case 3:
			return "C";
		case 4:
			return "D";
		case 5:
			return "E";
		case 6:
			return "F";
		case 7:
			return "G";
		case 8:
			return "H";
		case 9:
			return "I";
		case 10:
			return "J";
		case 11:
			return "K";
		case 12:
			return "L";
		default:
			return null;
		}
	}

}
