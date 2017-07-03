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
package spark.utils.decision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.jlab.groot.data.H2F;

import database.objects.NaughtyContainer;

public class HVPinDecision {

	private static List<Pair<Integer, Integer>> hvPinSegmentation = new ArrayList<Pair<Integer, Integer>>();

	private static Map<Integer, NaughtyContainer> badMap = new HashMap<Integer, NaughtyContainer>();
	private static List<NaughtyContainer> naughtyList = new ArrayList<NaughtyContainer>();
	private static Map<Integer, Double> layerCountMapForPin = new HashMap<>();
	private static Map<Integer, Double> rmsLayerCountMapForPin = new HashMap<>();

	private static Map<Integer, Pair<Integer, Integer>> returnMap = new HashMap<>();

	public static Map<Integer, Pair<Integer, Integer>> BadHVPin(H2F aH2F) {
		sumByPin(aH2F);
		return returnMap;
	}

	// public static void BadHVPin(H2F aH2F) {
	// sumByPin(aH2F);
	// }

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

	private static void sumByPin(H2F aH2f) {
		setuphvPinSegmentation();
		int bundle = 0;
		int placer = 0;
		badMap.clear();
		naughtyList.clear();
		for (Pair<Integer, Integer> pair : hvPinSegmentation) {// wire
																// bundles
			bundle++;
			for (int j = 0; j < 6; j++) {// layers
				placer++;
				double sum = 0;
				for (int i = pair.getLeft() - 1; i < pair.getRight(); i++) {// sum
																			// in
																			// each
																			// wire
																			// bundle
					sum = sum + aH2f.getBinContent(i, j);
				}
				// NaughtyContainer naughtyContainer = new
				// NaughtyContainer(layer, wireBundle, wireBundleCode,
				// countsInWireBundle);
				NaughtyContainer naughtyContainer = new NaughtyContainer(j + 1, bundle, switchBundle(bundle), sum);
				badMap.put(placer, naughtyContainer);
				naughtyList.add(naughtyContainer);
			}
		}
		setLayerCountMapForPins();

	}

	private static void setLayerCountMapForPins() {
		Map<Integer, NaughtyContainer> layerList = new HashMap<>();
		int sizeOfList = naughtyList.size() / 6;
		for (int i = 1; i <= sizeOfList; i++) {
			NaughtyContainer naughtyContainer = new NaughtyContainer();
			naughtyContainer.setWireBundle(i);
			naughtyContainer.setWireBundleCode(switchBundle(i));
			layerList.put(i, naughtyContainer);
		}
		// System.out.println(naughtyList.size() / 6);
		for (NaughtyContainer naughtyContainer : naughtyList) {
			layerList.get(naughtyContainer.getWireBundle()).incrementCounts(naughtyContainer.getCountsInWireBundle());
			// setting parital RMS
			layerList.get(naughtyContainer.getWireBundle()).incrementRMS(naughtyContainer.getCountsInWireBundle());
		}

		for (NaughtyContainer value : layerList.values()) {
			layerCountMapForPin.put(value.getWireBundle(), value.getCountsInWireBundle());
			rmsLayerCountMapForPin.put(value.getWireBundle(), Math.sqrt(1. / (double) sizeOfList * value.getRMS()));

		}
		findBadPinsBadMapAndRMS(badMap, rmsLayerCountMapForPin);

	}

	private static void findBadPinsBadMapAndRMS(Map<Integer, NaughtyContainer> badMap, Map<Integer, Double> rmsMap) {
		Map<Integer, NaughtyContainer> badLayerBundle = new HashMap<>();// bundle,
		// Map<Integer, Pair<Integer, Integer>> returnMap = new HashMap<>();
		for (NaughtyContainer value : badMap.values()) {
			// System.out.println("bundle = " + value.getWireBundle() + " code
			// of " + value.getWireBundleCode()
			// + " counts in bundle = " + value.getCountsInWireBundle() + "
			// layer = " + value.getLayer()
			// + " rms value " + rmsMap.get(value.getWireBundle()) + " ratio is
			// "
			// + value.getCountsInWireBundle() /
			// rmsMap.get(value.getWireBundle()));
			double Ri = value.getCountsInWireBundle() / rmsMap.get(value.getWireBundle());
			double cutValue = 0.5;
			// NaughtyContainer
			if (Ri < cutValue || Ri > (1. / cutValue)) {
				// System.out.println("bundle = " + value.getWireBundle() + "
				// code of " + value.getWireBundleCode()
				// + " counts in bundle = " + value.getCountsInWireBundle() + "
				// layer = " + value.getLayer()
				// + " rms value " + rmsMap.get(value.getWireBundle()) + " will
				// be eliminated. ratio is "
				// + value.getCountsInWireBundle() /
				// rmsMap.get(value.getWireBundle()));
				badLayerBundle.put(value.getWireBundle(), value);
				returnMap.put(value.getLayer(), bundleToPair(value.getWireBundle()));
			}
		}
		if (badLayerBundle.size() > 0) {
			recalculateRMS(badMap, badLayerBundle);
		}
		// else {
		// System.out.println("No more bad pins with this ratio cut");
		// }
	}

	private static void recalculateRMS(Map<Integer, NaughtyContainer> badMap,
			Map<Integer, NaughtyContainer> badLayerBundle) {
		// System.out.println("In another round of selection");
		List<NaughtyContainer> tempList = new ArrayList<>();
		Map<Integer, NaughtyContainer> layerList = new HashMap<>();
		Map<Integer, NaughtyContainer> badMapAgain = new HashMap<>();
		// badMap.forEach(badMapAgain::putIfAbsent);

		for (Map.Entry<Integer, NaughtyContainer> entry : badMap.entrySet()) {
			Integer key = entry.getKey();
			NaughtyContainer value = entry.getValue();
			for (NaughtyContainer pair : badLayerBundle.values()) {
				if (pair.getWireBundle() == value.getWireBundle()) {
					badMapAgain.put(key, value);
				}
				if (pair.equals(value)) {
					badMapAgain.remove(key, value);
				}

			}
		}
		for (NaughtyContainer pair : badLayerBundle.values()) {
			NaughtyContainer naughtyContainer = new NaughtyContainer();
			naughtyContainer.setWireBundle(pair.getWireBundle());
			naughtyContainer.setWireBundleCode(pair.getWireBundleCode());
			layerList.put(pair.getWireBundle(), naughtyContainer);

			for (NaughtyContainer nc : naughtyList) {
				if (pair.getWireBundle() == nc.getWireBundle()) {
					tempList.add(nc);
				}
			}
			tempList.remove(pair);
		}

		for (NaughtyContainer naughtyContainer : tempList) {

			layerList.get(naughtyContainer.getWireBundle()).incrementCounts(naughtyContainer.getCountsInWireBundle());
			// setting parital RMS
			layerList.get(naughtyContainer.getWireBundle()).incrementRMS(naughtyContainer.getCountsInWireBundle());
			layerList.get(naughtyContainer.getWireBundle()).incrementCountsInList();

		}
		Map<Integer, Double> rmsLayerCountMapForPinAgain = new HashMap<>();
		for (NaughtyContainer value : layerList.values()) {
			// System.out.println(value.getWireBundle() + " " +
			// value.getCountsInWireBundle() + " "
			// + Math.sqrt(1. / value.getCountsInList() * value.getRMS()));
			rmsLayerCountMapForPinAgain.put(value.getWireBundle(),
					Math.sqrt(1. / value.getCountsInList() * value.getRMS()));
		}

		findBadPinsBadMapAndRMS(badMapAgain, rmsLayerCountMapForPinAgain);

	}

	private static String switchBundle(int placer) {
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

	private static Pair<Integer, Integer> bundleToPair(int bundle) {
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
}
