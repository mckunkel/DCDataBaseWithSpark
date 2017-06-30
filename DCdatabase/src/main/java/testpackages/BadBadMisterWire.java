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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;

import database.process.DataProcess;
import database.service.MainFrameService;
import database.utils.MainFrameServiceManager;

public class BadBadMisterWire {

	private MainFrameService mainFrameService = null;
	private DataProcess dataprocess = new DataProcess();
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
		this.mainFrameService = MainFrameServiceManager.getSession();
		this.dataprocess.openFile("/Users/michaelkunkel/WORK/CLAS/CLAS12/CLAS12Data/pass4/test762.hipo");// out_clas12_000762_a00000.hipo
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
		can1.draw(this.mainFrameService.getHistogramByMap(i, j));
		frame.add(can1);
		frame.setVisible(true);
		// return this.can1;
	}

	public H2F getHist(int i, int j) {
		return this.mainFrameService.getHistogramByMap(i, j);
	}

	public void run() {
		dataprocess.processFile();
	}

	public String switchBundle(int placer) {
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

	public static void main(String[] args) {
		Logger.getLogger("org.apache.spark.SparkContext").setLevel(Level.WARN);
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);
		BadBadMisterWire badBadMisterWire = new BadBadMisterWire();
		badBadMisterWire.setNEvents(10000);
		badBadMisterWire.run();
		int superLayer = 1;
		int sector = 2;
		badBadMisterWire.getMaps(superLayer, sector);

		H2F testH2F = badBadMisterWire.getHist(superLayer, sector);
		// byPin
		System.out.println("#### PIN ######");
		badBadMisterWire.sumByPin(testH2F);

		// badBadMisterWire.getlayerCountMapForPin();
		//
		// // OK lets try this circular logic you drew
		// // but first lets rename the bundles alphabetically
		// // i.e. bundle 1 = A ; 2 = B ; 3 = C ; 4 = D ; 5 = E ; 6 = F ; 7 = G
		// ; 8
		// // = H
		// // now the circular logic is as
		// // A = B = H/4 ; B = C = A ; C = D = B
		// // D = E/2 = C ; E = F = 2D ; F = G = E
		// // G = H/2 = F ; H =2G = 4A
		// System.out.println("For A:");
		// System.out.println(layerList.get(1).getCountsInWireBundle() + " " +
		// layerList.get(2).getCountsInWireBundle()
		// + " " + layerList.get(8).getCountsInWireBundle() / 4);
		// System.out.println(layerList.get(1).getCountsInWireBundle() /
		// layerList.get(1).getCountsInWireBundle() + " "
		// + layerList.get(2).getCountsInWireBundle() /
		// layerList.get(1).getCountsInWireBundle() + " "
		// + layerList.get(8).getCountsInWireBundle() / (4 *
		// layerList.get(1).getCountsInWireBundle()));
		// EmbeddedCanvas can2 = new EmbeddedCanvas();
		//
		// System.out.println("For B:");
		// System.out.println(layerList.get(2).getCountsInWireBundle() + " " +
		// layerList.get(3).getCountsInWireBundle()
		// + " " + layerList.get(1).getCountsInWireBundle());
		// System.out.println(layerList.get(2).getCountsInWireBundle() /
		// layerList.get(2).getCountsInWireBundle() + " "
		// + layerList.get(3).getCountsInWireBundle() /
		// layerList.get(2).getCountsInWireBundle() + " "
		// + layerList.get(1).getCountsInWireBundle() /
		// (layerList.get(2).getCountsInWireBundle()));
		//
		// System.out.println("For C:");
		// System.out.println(layerList.get(3).getCountsInWireBundle() + " " +
		// layerList.get(4).getCountsInWireBundle()
		// + " " + layerList.get(2).getCountsInWireBundle());
		// System.out.println(layerList.get(3).getCountsInWireBundle() /
		// layerList.get(3).getCountsInWireBundle() + " "
		// + layerList.get(4).getCountsInWireBundle() /
		// layerList.get(3).getCountsInWireBundle() + " "
		// + layerList.get(2).getCountsInWireBundle() /
		// (layerList.get(3).getCountsInWireBundle()));
		//
		// can2.draw(newH2F);
		// JFrame frame2 = new JFrame("");
		// frame2.add(can2);
		// frame2.setVisible(true);

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

}
