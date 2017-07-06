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

	private static List<Pair<Integer, Integer>> hvPinSegmentation = null;

	private static Map<Integer, NaughtyContainer> badMap = null;
	private static List<NaughtyContainer> naughtyList = null;
	private static Map<Integer, Double> layerCountMapForPin = null;
	private static Map<Integer, Double> rmsLayerCountMapForPin = null;

	private static Map<Integer, Pair<Integer, Integer>> returnMap = null;
	private static Map<Pair<Integer, String>, Pair<Integer, Integer>> returnPairMap = null;

	public static Map<Pair<Integer, String>, Pair<Integer, Integer>> BadHVPin(H2F aH2F) {
		hvPinSegmentation = new ArrayList<Pair<Integer, Integer>>();
		badMap = new HashMap<Integer, NaughtyContainer>();
		naughtyList = new ArrayList<NaughtyContainer>();
		layerCountMapForPin = new HashMap<>();
		rmsLayerCountMapForPin = new HashMap<>();
		returnMap = new HashMap<Integer, Pair<Integer, Integer>>();
		returnPairMap = new HashMap<Pair<Integer, String>, Pair<Integer, Integer>>();
		returnMap.clear();
		sumByPin(aH2F);
		// hotWire(aH2F);

		return returnPairMap;
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

	private static void hotWire(H2F aH2f) {
		setuphvPinSegmentation();
		H2F aNewH2F = new H2F("something", aH2f.getXAxis().getNBins(), aH2f.getXAxis().min(), aH2f.getXAxis().max(),
				aH2f.getYAxis().getNBins(), aH2f.getYAxis().min(), aH2f.getYAxis().max());
		int bundle = 0;
		boolean recalculateRMS = false;
		for (Pair<Integer, Integer> pair : hvPinSegmentation) {// wire
			// bundles
			bundle++;

			for (int j = 0; j < 6; j++) {// layers
				double sum = 0;
				double average = 0;
				int wiresInBundle = 0;

				for (int i = pair.getLeft() - 1; i < pair.getRight(); i++) {
					// System.out.println(rmsLayerCountMapForPin.get(bundle) + "
					// in hotwire");
					if (aH2f.getBinContent(i, j) != 0) {
						wiresInBundle++;
					}
					sum = sum + Math.pow(aH2f.getBinContent(i, j), 2);

					// if (bundle == 12 && j == 0) {
					// System.out.println(aH2f.getBinContent(i, j) + " at " + (i
					// + 1));
					// }
				}
				average = Math.sqrt(sum / wiresInBundle);
				// System.out.println("RMS is " + average + " of the sum " + sum
				// + " in bundle " + switchBundle(bundle)
				// + " at layer " + (j + 1));

				for (int i = pair.getLeft() - 1; i < pair.getRight(); i++) {
					double neighborRMS = 0;
					int neighborCounts = 0;
					if (aH2f.getBinContent(i, j) != 0) {
						neighborCounts++;
						neighborRMS = Math.pow(aH2f.getBinContent(i, j), 2);
						if (i != 0 && i != 111) {
							if (aH2f.getBinContent(i - 1, j) != 0) {
								neighborCounts++;
								neighborRMS = neighborRMS + +Math.pow(aH2f.getBinContent(i - 1, j), 2);

							}
							if (aH2f.getBinContent(i + 1, j) != 0) {
								neighborCounts++;
								neighborRMS = neighborRMS + +Math.pow(aH2f.getBinContent(i + 1, j), 2);
							}
						}
						if (i == 0) {
							if (aH2f.getBinContent(i + 1, j) != 0) {
								neighborCounts++;
								neighborRMS = neighborRMS + Math.pow(aH2f.getBinContent(i + 1, j), 2);
							}
						}
						if (i == 111) {
							if (aH2f.getBinContent(i - 1, j) != 0) {
								neighborCounts++;
								neighborRMS = neighborRMS + Math.pow(aH2f.getBinContent(i - 1, j), 2);
							}
						}
						neighborRMS = Math.sqrt(neighborRMS / neighborCounts);
					}
					boolean rmsDecision = false;
					boolean neighborDecision = false;
					if (aH2f.getBinContent(i, j) > 1.6 * average) {
						System.out.println("RMS is " + average + " of the sum " + sum + " possible hotwire at "
								+ (i + 1) + " in bundle " + switchBundle(bundle) + " at layer " + (j + 1)
								+ " with wire count " + aH2f.getBinContent(i, j) + " with neighborRMS = " + neighborRMS
								+ " RMS Decision");
						rmsDecision = true;

					}
					if (aH2f.getBinContent(i, j) > 1.6 * neighborRMS) {
						System.out.println("RMS is " + average + " of the sum " + sum + " possible hotwire at "
								+ (i + 1) + " in bundle " + switchBundle(bundle) + " at layer " + (j + 1)
								+ " with wire count " + aH2f.getBinContent(i, j) + " with neighborRMS = " + neighborRMS
								+ " Neighbor Decision");
						neighborDecision = true;

					}
					if (neighborDecision || rmsDecision) {
						aNewH2F.setBinContent(i, j, average);
						recalculateRMS = true;
					} else {
						aNewH2F.setBinContent(i, j, aH2f.getBinContent(i, j));

					}
				}

			}
		}
		if (recalculateRMS) {
			System.out.println("RECAL");
			hotWire(aNewH2F);
		} else {

			// sumByPin(aNewH2F);
		}
	}

	private static void findBadPinsBadMapAndRMS(Map<Integer, NaughtyContainer> badMap, Map<Integer, Double> rmsMap) {
		Map<Integer, NaughtyContainer> badLayerBundle = new HashMap<>();// bundle,
		// System.out.println("size of badmap is " + badMap.size());

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
				// System.out.println("CUTTING !!!!!!!! bundle = " +
				// value.getWireBundle() + " code of "
				// + value.getWireBundleCode() + " counts in bundle = " +
				// value.getCountsInWireBundle()
				// + " layer = " + value.getLayer() + " rms value " +
				// rmsMap.get(value.getWireBundle())
				// + " ratio is " + value.getCountsInWireBundle() /
				// rmsMap.get(value.getWireBundle()));
				badLayerBundle.put(value.getWireBundle(), value);
				returnMap.put(value.getLayer(), bundleToPair(value.getWireBundle()));
				returnPairMap.put(Pair.of(value.getLayer(), value.getWireBundleCode()),
						bundleToPair(value.getWireBundle()));
				// System.out.println(value.getLayer() + " " +
				// bundleToPair(value.getWireBundle()) + " "
				// + returnMap.get(value.getLayer()).getLeft() + " "
				// + returnMap.get(value.getLayer()).getRight());

			}
		}
		// printReturnMap();

		if (badLayerBundle.size() > 0) {
			recalculateRMS(badMap, badLayerBundle);
		}
		// else {
		// // System.out.println("No more bad pins with this ratio cut");
		// // printReturnMap();
		// }
	}

	private static void printReturnMap() {
		for (Map.Entry<Pair<Integer, String>, Pair<Integer, Integer>> entry : returnPairMap.entrySet()) {
			Pair<Integer, String> key = entry.getKey();

			Integer leftValue = entry.getValue().getLeft();
			Integer rightValue = entry.getValue().getRight();
			System.out.println("#################################");

			System.out.println("I am in HVPinDecision. I have located a bad pin at layer " + key.getLeft()
					+ " with wires from " + leftValue + " to " + rightValue);
			System.out.println("#################################");

		}
	}

	private static void recalculateRMS(Map<Integer, NaughtyContainer> badMap,
			Map<Integer, NaughtyContainer> badLayerBundle) {
		System.out.println("In another round of selection");
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
