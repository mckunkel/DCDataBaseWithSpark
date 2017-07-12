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
package testpackages;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.collections.functors.NullPredicate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;

import database.process.DataProcessForTest;
import spark.utils.decision.HVChannelDecision;
import spark.utils.decision.HVPinDecision;

public class BadBadMisterWire {

<<<<<<< HEAD
	//
	// import org.jlab.groot.data.GraphErrors;
	// import org.jlab.groot.data.H2F;
	// import org.jlab.groot.graphics.EmbeddedCanvas;

	private MainFrameService mainFrameService = null;
	private DataProcess dataprocess = new DataProcess();
=======
	private DataProcessForTest dataprocess = new DataProcessForTest();
>>>>>>> ae36b949f8bce9e8ebbc0fc6e97b290c15f97a6d
	private EmbeddedCanvas can1 = null;
	JFrame frame = null;

	List<Pair<Integer, Integer>> hvChannelSegmentation = null;
	List<Pair<Integer, Integer>> hvPinSegmentation = null;
	Map<String, Pair<Integer, Integer>> testHash = null;

	Map<Integer, NaughtyContainer> badMap = null;
	List<NaughtyContainer> naughtyList = null;
	Map<Integer, Double> layerCountMapForPin = null;
	Map<Integer, Double> layerCountMapForChannel = null;

	Map<Integer, Double> rmsLayerCountMapForPin = null;
	Map<Integer, Double> rmsLayerCountMapForChannel = null;

	public BadBadMisterWire() {
		// this.dataprocess.openFile("/Users/michaelkunkel/WORK/CLAS/CLAS12/CLAS12Data/pass4/test762.hipo");//
		// out_clas12_000762_a00000.hipo
		this.dataprocess.openFile(
				"/Volumes/Mac_Storage/Work_Codes/GIT_HUB/DCDataBaseWithSpark/DCdatabase/needs/out_clas12_000762_a00000.hipo");
		can1 = new EmbeddedCanvas();
		can1.initTimer(200);
		frame = new JFrame("BadBadMisterWire");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screensize = null;
		screensize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize((int) (screensize.getHeight() * .75 * 1.618), (int) (screensize.getHeight() * .75));

		this.hvChannelSegmentation = new ArrayList<Pair<Integer, Integer>>();
		this.hvPinSegmentation = new ArrayList<Pair<Integer, Integer>>();
		this.testHash = new HashMap<>();
		setuphvChannelSegmentation();
		setuphvPinSegmentation();
		setuphvChannelMap();

		this.badMap = new HashMap<Integer, NaughtyContainer>();
		this.naughtyList = new ArrayList<NaughtyContainer>();

		this.layerCountMapForPin = new HashMap<>();
		this.layerCountMapForChannel = new HashMap<>();

		this.rmsLayerCountMapForPin = new HashMap<>();
		this.rmsLayerCountMapForChannel = new HashMap<>();

	}

	private void setuphvChannelSegmentation() {
		this.hvChannelSegmentation.add(Pair.of(1, 8));
		this.hvChannelSegmentation.add(Pair.of(9, 16));
		this.hvChannelSegmentation.add(Pair.of(17, 24));
		this.hvChannelSegmentation.add(Pair.of(25, 32));
		this.hvChannelSegmentation.add(Pair.of(33, 48));
		this.hvChannelSegmentation.add(Pair.of(49, 64));
		this.hvChannelSegmentation.add(Pair.of(65, 80));
		this.hvChannelSegmentation.add(Pair.of(81, 112));
	}

	private void setuphvPinSegmentation() {
		this.hvPinSegmentation.add(Pair.of(1, 8));
		this.hvPinSegmentation.add(Pair.of(9, 16));
		this.hvPinSegmentation.add(Pair.of(17, 24));
		this.hvPinSegmentation.add(Pair.of(25, 32));
		this.hvPinSegmentation.add(Pair.of(33, 40));
		this.hvPinSegmentation.add(Pair.of(41, 48));
		this.hvPinSegmentation.add(Pair.of(49, 56));
		this.hvPinSegmentation.add(Pair.of(57, 64));
		this.hvPinSegmentation.add(Pair.of(65, 72));
		this.hvPinSegmentation.add(Pair.of(73, 80));
		this.hvPinSegmentation.add(Pair.of(81, 96));
		this.hvPinSegmentation.add(Pair.of(97, 112));

	}

	private void setuphvChannelMap() {
		this.testHash.put("A", hvChannelSegmentation.get(0));
		this.testHash.put("B", hvChannelSegmentation.get(1));
		this.testHash.put("C", hvChannelSegmentation.get(2));
		this.testHash.put("D", hvChannelSegmentation.get(3));
		this.testHash.put("E", hvChannelSegmentation.get(4));
		this.testHash.put("F", hvChannelSegmentation.get(5));
		this.testHash.put("G", hvChannelSegmentation.get(6));
		this.testHash.put("H", hvChannelSegmentation.get(7));

	}

	public List<Pair<Integer, Integer>> hvChannelSegmentation() {
		return this.hvChannelSegmentation;
	}

	public List<Pair<Integer, Integer>> hvPinSegmentation() {
		return this.hvPinSegmentation;
	}

	public Map<String, Pair<Integer, Integer>> hvChannelMap() {
		return this.testHash;
	}

	public void setNEvents(int nEvents) {
		this.dataprocess.setNEvents(nEvents);
	}

	public void getMaps(int i, int j) {
		can1.draw(this.dataprocess.getHistogramByMap(i, j));
		frame.add(can1);
		frame.setVisible(true);
		// return this.can1;
	}

	public H2F getHist(int i, int j) {
		return this.dataprocess.getHistogramByMap(i, j);
	}

	public void run() {
		dataprocess.processFile();
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

	private static Pair<Integer, Integer> switchChannelBundle(int placer) {
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

	public void setLayerCountMapForPins() {
		Map<Integer, NaughtyContainer> layerList = new HashMap<>();
		int sizeOfList = naughtyList.size() / 6;
		for (int i = 1; i <= sizeOfList; i++) {
			NaughtyContainer naughtyContainer = new NaughtyContainer();
			naughtyContainer.setWireBundle(i);
			naughtyContainer.setWireBundleCode(switchBundle(i));
			layerList.put(i, naughtyContainer);
		}
		System.out.println(naughtyList.size() / 6);
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

	public void setLayerCountMapForChannels() {
		Map<Integer, NaughtyContainer> layerList = new HashMap<>();
		for (int i = 1; i < 9; i++) {
			NaughtyContainer naughtyContainer = new NaughtyContainer();
			naughtyContainer.setWireBundle(i);
			naughtyContainer.setWireBundleCode(switchBundle(i));
			layerList.put(i, naughtyContainer);
		}

		for (NaughtyContainer naughtyContainer : naughtyList) {
			layerList.get(naughtyContainer.getWireBundle()).incrementCounts(naughtyContainer.getCountsInWireBundle());
			// setting parital RMS
			layerList.get(naughtyContainer.getWireBundle()).incrementRMS(naughtyContainer.getCountsInWireBundle());
		}

		for (NaughtyContainer value : layerList.values()) {
			layerCountMapForChannel.put(value.getWireBundle(), value.getCountsInWireBundle());
			System.out.println(value.getWireBundle() + " " + value.getRMS() + " " + Math.sqrt(1. / 8. * value.getRMS())
					+ " " + value.getCountsInWireBundle());
			rmsLayerCountMapForChannel.put(value.getWireBundle(), Math.sqrt(1. / 8. * value.getRMS()));

		}
		doSomethingWithBadMapAndRMS(badMap, rmsLayerCountMapForChannel);
	}

	private void doSomethingWithBadMapAndRMS(Map<Integer, NaughtyContainer> badMap, Map<Integer, Double> rmsMap) {
		for (NaughtyContainer value : badMap.values()) {
			System.out.println("bundle = " + value.getWireBundle() + " code of " + value.getWireBundleCode()
					+ " counts in bundle = " + value.getCountsInWireBundle() + " layer = " + value.getLayer()
					+ " rms value " + rmsMap.get(value.getWireBundle()) + " ratio is "
					+ value.getCountsInWireBundle() / rmsMap.get(value.getWireBundle()));
			double Ri = value.getCountsInWireBundle() / rmsMap.get(value.getWireBundle());
			double cutValue = 0.5;
			if (Ri < cutValue || Ri > (1. / cutValue)) {
				System.out.println("bundle = " + value.getWireBundle() + " code of " + value.getWireBundleCode()
						+ " counts in bundle = " + value.getCountsInWireBundle() + " layer = " + value.getLayer()
						+ " rms value " + rmsMap.get(value.getWireBundle()) + " will be eliminated. ratio is "
						+ value.getCountsInWireBundle() / rmsMap.get(value.getWireBundle()));
			}

		}
	}

	private void findBadPinsBadMapAndRMS(Map<Integer, NaughtyContainer> badMap, Map<Integer, Double> rmsMap) {
		Map<Integer, NaughtyContainer> badLayerBundle = new HashMap<>();// bundle,

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
				System.out.println("bundle = " + value.getWireBundle() + " code of " + value.getWireBundleCode()
						+ " counts in bundle = " + value.getCountsInWireBundle() + " layer = " + value.getLayer()
						+ " rms value " + rmsMap.get(value.getWireBundle()) + " will be eliminated. ratio is "
						+ value.getCountsInWireBundle() / rmsMap.get(value.getWireBundle()));
				badLayerBundle.put(value.getWireBundle(), value);
			}
		}
		if (badLayerBundle.size() > 0) {
			recalculateRMS(badMap, rmsMap, badLayerBundle);
		} else {
			System.out.println("No more bad pins with this ratio cut");
		}
	}

	public void recalculateRMS(Map<Integer, NaughtyContainer> badMap, Map<Integer, Double> rmsMap,
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

	public Map<Integer, Double> getlayerCountMapForChannel() {
		return this.layerCountMapForChannel;
	}

	public Map<Integer, Double> getlayerCountMapForPin() {
		return this.layerCountMapForPin;
	}

	public void sumByChannel(H2F aH2f) {

		int bundle = 0;
		int placer = 0;
		this.badMap.clear();
		this.naughtyList.clear();
		for (Pair<Integer, Integer> pair : hvChannelSegmentation) {// wire
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
				NaughtyContainer naughtyContainer = new NaughtyContainer(j + 1, bundle, switchBundle(bundle), sum);
				badMap.put(placer, naughtyContainer);
				naughtyList.add(naughtyContainer);
			}
		}
		setLayerCountMapForChannels();
	}

	public void sumByPin(H2F aH2f) {

		int bundle = 0;
		int placer = 0;
		this.badMap.clear();
		this.naughtyList.clear();
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

	public Map<Integer, NaughtyContainer> getBadMap() {
		return this.badMap;
	}

	public List<NaughtyContainer> getNaughtyList() {
		return this.naughtyList;
	}

	public static <K, V> boolean hasAllNullValues(Map<K, V> map) {
		return !CollectionUtils.exists(map.values(), NotNullPredicate.INSTANCE);
	}

	public static <K, V> boolean hasAllNullValuesMap(Map<K, V> map) {
		int size = map.size();
		return CollectionUtils.countMatches(map.values(), NullPredicate.INSTANCE) == size;
	}

	private static List<H2F> aTempContainer = new ArrayList<H2F>();
	// private static H2F aNewH2F = null;

	private static H2F hotWire(H2F aH2f, int value) {
		List<Pair<Integer, Integer>> hvPinSegmentation = new ArrayList<Pair<Integer, Integer>>();
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

		H2F aNewH2F = new H2F("something" + value, aH2f.getXAxis().getNBins(), aH2f.getXAxis().min(),
				aH2f.getXAxis().max(), aH2f.getYAxis().getNBins(), aH2f.getYAxis().min(), aH2f.getYAxis().max());
		int bundle = 0;
		boolean recalculateRMS = false;
		for (Pair<Integer, Integer> pair : hvPinSegmentation) {// wire
			// bundles
			bundle++;

			for (int j = 0; j < 6; j++) {// layers
				double sum = 0;
				double average = 0;
				double rms = 0;
				double partialRMS = 0;

				int wiresInBundle = 0;

				for (int i = pair.getLeft() - 1; i < pair.getRight(); i++) {
					// System.out.println(rmsLayerCountMapForPin.get(bundle) + "
					// in hotwire");
					if (aH2f.getBinContent(i, j) != 0) {
						wiresInBundle++;
					}
					partialRMS = partialRMS + Math.pow(aH2f.getBinContent(i, j), 2);
					average = average + aH2f.getBinContent(i, j);
					// if ((bundle == 9 || bundle == 8) && (j == 0 || j == 1)) {
					// System.out.println(aH2f.getBinContent(i, j) + " at wire "
					// + (i + 1));
					// }
				}
				rms = Math.sqrt(partialRMS / wiresInBundle);
				sum = average;
				average = average / wiresInBundle;
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
					boolean aDecision = false;
					if (aH2f.getBinContent(i, j) > 1.6 * rms) {
						// System.out.println("RMS is " + rms + " of the sum " +
						// sum + " possible hotwire at " + (i + 1)
						// + " in bundle " + switchBundle(bundle) + " at layer "
						// + (j + 1) + " with wire count "
						// + aH2f.getBinContent(i, j) + " with neighborRMS = " +
						// neighborRMS + " RMS Decision");
						rmsDecision = true;

					}
					if (aH2f.getBinContent(i, j) > 1.6 * neighborRMS) {
						// System.out.println("RMS is " + rms + " of the sum " +
						// sum + " possible hotwire at " + (i + 1)
						// + " in bundle " + switchBundle(bundle) + " at layer "
						// + (j + 1) + " with wire count "
						// + aH2f.getBinContent(i, j) + " with neighborRMS = " +
						// neighborRMS
						// + " Neighbor Decision");
						neighborDecision = true;

					}
					if (aH2f.getBinContent(i, j) > 1.6 * average) {
						System.out.println("Average is " + average + " of the sum " + sum + " possible hotwire at "
								+ (i + 1) + " in bundle " + switchBundle(bundle) + " at layer " + (j + 1)
								+ " with wire count " + aH2f.getBinContent(i, j) + " with neighborRMS = " + neighborRMS
								+ " Average Decision");
						aDecision = true;

					}
					if (aDecision) {
						double testValue = (neighborRMS + rms) / 2.0;
						aNewH2F.setBinContent(i, j, average);
						recalculateRMS = true;
					} else {
						aNewH2F.setBinContent(i, j, aH2f.getBinContent(i, j));

					}
				}

			}
		}
		aTempContainer.add(aNewH2F);

		if (recalculateRMS && value < 3) {
			System.out.println("RECAL  " + value);
			value++;
			hotWire(aNewH2F, value);
		}
		return aNewH2F;
	}

	public static void main(String[] args) {
		Logger.getLogger("org.apache.spark.SparkContext").setLevel(Level.WARN);
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);
		BadBadMisterWire badBadMisterWire = new BadBadMisterWire();
		badBadMisterWire.setNEvents(100000);
		badBadMisterWire.run();
		int superLayer = 3;
		int sector = 2;
		badBadMisterWire.getMaps(superLayer, sector);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();

		H2F testH2F = badBadMisterWire.getHist(superLayer, sector);
		// byPin
		System.out.println("#### PIN ######");

		for (int i = 0; i < 6; i++) {
			for (int j = 1; j < 2; j++) {
				H2F aH2F = badBadMisterWire.getHist(i + 1, j + 1);
				// MischievousPin mischievousPin = new MischievousPin();
				// mischievousPin.setSector(j + 1);
				// mischievousPin.setSuperLayer(i + 1);
				// mischievousPin.setMischievousPin(HVPinDecision.BadHVPin(aH2F));
				// mischievousPinList.add(mischievousPin);
				// can1.draw(aH2F);
				EmbeddedCanvas can2 = new EmbeddedCanvas();
				can2.draw(aH2F);

				tabbedPane.add("Superlayer " + (i + 1) + " Sector " + (j + 1), can2);
				System.out.println("Superlayer " + (i + 1) + " Sector " + (j + 1));
				// H2F hotFixH2FAgain = hotWire(aH2F, 0);

				Map<Pair<Integer, String>, Pair<Integer, Integer>> badHVPin2 = HVPinDecision.BadHVPin(aH2F);
				System.out.println(hasAllNullValues(badHVPin2) + " " + hasAllNullValuesMap(badHVPin2));
				for (Map.Entry<Pair<Integer, String>, Pair<Integer, Integer>> entry : badHVPin2.entrySet()) {
					Pair<Integer, String> key = entry.getKey();

					Integer leftValue = entry.getValue().getLeft();
					Integer rightValue = entry.getValue().getRight();

					System.out.println("I have located a bad pin at layer " + key.getLeft() + " with wires from "
							+ leftValue + " to " + rightValue);

				}
				System.out.println("#################################");

			}
		}

		System.out.println("#### CHANNEL ######");
		Random rn = new Random();
		int bundleMin = 1;
		int bundleMax = 8;
		int sectorMin = 1;
		int sectorMax = 6;
		int superLayerMin = 1;
		int superLayerMax = 6;
		int rndmBundle = 6;// rn.nextInt(bundleMax - bundleMin + 1) + bundleMin;
		int rndmSector = rn.nextInt(sectorMax - sectorMin + 1) + sectorMin;
		int rndmSuperLayer = rn.nextInt(superLayerMax - superLayerMin + 1) + superLayerMin;
		Pair<Integer, Integer> rndmPair = switchChannelBundle(rndmBundle);

		System.out.println(rndmBundle);
		H2F channelMap = badBadMisterWire.getHist(1, 2);
		H2F aNewH2F = new H2F("something", channelMap.getXAxis().getNBins(), channelMap.getXAxis().min(),
				channelMap.getXAxis().max(), channelMap.getYAxis().getNBins(), channelMap.getYAxis().min(),
				channelMap.getYAxis().max());
		for (int j = 0; j < 6; j++) {// layers

			for (int i = 0; i < 112; i++) {
				if ((i + 1) <= rndmPair.getRight() && (i + 1) >= rndmPair.getLeft()) {
					aNewH2F.setBinContent(i, j, rn.nextInt(100 - 8 + 1) + 8);
				} else
					aNewH2F.setBinContent(i, j, channelMap.getBinContent(i, j));
			}
		}
		Map<Integer, Pair<Integer, Integer>> badHVChannel = HVChannelDecision.BadHVChannel(aNewH2F);
		EmbeddedCanvas channelCanvas = new EmbeddedCanvas();
		channelCanvas.setSize(500, 500);
		channelCanvas.draw(aNewH2F);

		channelCanvas.save("data/atest.png");

		tabbedPane.add("Rndm SL " + rndmSuperLayer + " Rndm Sector " + 2, channelCanvas);
		GraphErrors graph = new GraphErrors("AGraph", HVChannelDecision.getXValues(), HVChannelDecision.getYValues());
		graph.setMarkerColor(1); // color from 0-9 for given palette
		graph.setMarkerSize(15); // size in points on the screen
		graph.setMarkerStyle(1); // Style can be 1 or 2
		graph.setLineColor(1); // Style can be 1 or 2

		EmbeddedCanvas graphCanvas = new EmbeddedCanvas();
		graphCanvas.draw(graph);
		tabbedPane.add("Rndm SL " + rndmSuperLayer + " Rndm Sector " + 2, graphCanvas);
		graphCanvas.draw(HVChannelDecision.getFunc(), "same");

		JFrame frame2 = new JFrame("");
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screensize = null;
		screensize = Toolkit.getDefaultToolkit().getScreenSize();
		frame2.setSize((int) (screensize.getHeight() * .75 * 1.618), (int) (screensize.getHeight() * .75));
		mainPanel.add(tabbedPane);
		frame2.add(mainPanel);
		frame2.setVisible(true);

	}

	static class NaughtyContainer {
		private int layer;
		private int wireBundle;
		private String wireBundleCode;
		private double countsInWireBundle;
		private double rmsInLayer;
		private double countsInList; // to know how many entries were used to
										// calculate temp RMS. This number will
										// be
										// the quotient of the RMS

		public NaughtyContainer() {

		}

		public NaughtyContainer(int layer, int wireBundle, String wireBundleCode, double countsInWireBundle) {
			this.layer = layer;
			this.wireBundle = wireBundle;
			this.wireBundleCode = wireBundleCode;
			this.countsInWireBundle = countsInWireBundle;
		}

		public int getLayer() {
			return layer;
		}

		public void setLayer(int layer) {
			this.layer = layer;
		}

		public int getWireBundle() {
			return wireBundle;
		}

		public void setWireBundle(int wireBundle) {
			this.wireBundle = wireBundle;
		}

		public String getWireBundleCode() {
			return wireBundleCode;
		}

		public void setWireBundleCode(String wireBundleCode) {
			this.wireBundleCode = wireBundleCode;
		}

		public double getCountsInWireBundle() {
			return countsInWireBundle;
		}

		public void setCountsInWireBundle(double countsInWireBundle) {
			this.countsInWireBundle = countsInWireBundle;
		}

		public void incrementCounts(double counts) {
			this.countsInWireBundle = this.countsInWireBundle + counts;
		}

		public void incrementRMS(double counts) {
			this.rmsInLayer = this.rmsInLayer + counts * counts;
		}

		public void incrementCountsInList() {
			this.countsInList++;
		}

		public double getCountsInList() {
			return this.countsInList;
		}

		public double getRMS() {
			return this.rmsInLayer;
		}

		public void setRMS(double rms) {
			this.rmsInLayer = rms;
		}

	}

	static class MischievousPin {

		private Map<Integer, Pair<Integer, Integer>> mischievousPin;
		private int sector;
		private int superLayer;

		public MischievousPin() {
			this.mischievousPin = new HashMap<>();
		}

		public Map<Integer, Pair<Integer, Integer>> getMischievousPin() {
			return mischievousPin;
		}

		public void setMischievousPin(Map<Integer, Pair<Integer, Integer>> mischievousPin) {
			this.mischievousPin = mischievousPin;
		}

		public int getSector() {
			return sector;
		}

		public void setSector(int sector) {
			this.sector = sector;
		}

		public int getSuperLayer() {
			return superLayer;
		}

		public void setSuperLayer(int superLayer) {
			this.superLayer = superLayer;
		}

	}
}
