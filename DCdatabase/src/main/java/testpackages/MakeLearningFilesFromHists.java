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
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.commons.lang3.tuple.Pair;
import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;

import database.process.DataProcessForTest;
import database.utils.ChannelBundles;
import spark.utils.decision.HVPinDecision;

public class MakeLearningFilesFromHists {

	public static void main(String[] args) {
		Random rn = new Random();

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();

		DataProcessForTest dataprocess = new DataProcessForTest();
		dataprocess.openFile("/Users/michaelkunkel/WORK/CLAS/CLAS12/CLAS12Data/pass4/test762.hipo");//
		// dataprocess.openFile(
		// "/Volumes/Mac_Storage/Work_Codes/GIT_HUB/DCDataBaseWithSpark/DCdatabase/needs/out_clas12_000762_a00000.hipo");

		dataprocess.setNEvents(10000);
		dataprocess.processFile();
		// lets check out sl 1 sec 2 and
		// sl 3 sec 2
		// chosen sl 1 and sec 2 because it was more statistically filled
		int superLayer = 1;
		int sector = 2;
		H2F channelMap = dataprocess.getHistogramByMap(superLayer, sector);
		H2F aNewH2F = new H2F("something", channelMap.getXAxis().getNBins(), channelMap.getXAxis().min(),
				channelMap.getXAxis().max(), channelMap.getYAxis().getNBins(), channelMap.getYAxis().min(),
				channelMap.getYAxis().max());
		EmbeddedCanvas c1 = new EmbeddedCanvas();
		c1.draw(channelMap);
		tabbedPane.add("Superlayer " + (superLayer) + " Sector " + (sector), c1);

		Pair<Integer, Integer> bundle1 = ChannelBundles.switchChannelBundle(1);
		Map<Pair<Integer, String>, Pair<Integer, Integer>> badHVPin2 = HVPinDecision.BadHVPin(channelMap);

		EmbeddedCanvas c2 = new EmbeddedCanvas();

		for (int j = 0; j < 6; j++) {// layers

			for (int i = 0; i < 112; i++) {
				aNewH2F.setBinContent(i, j, channelMap.getBinContent(i, j));
			}
		}

		for (Map.Entry<Pair<Integer, String>, Pair<Integer, Integer>> entry : badHVPin2.entrySet()) {
			Pair<Integer, String> key = entry.getKey();

			Integer leftValue = entry.getValue().getLeft();
			Integer rightValue = entry.getValue().getRight();

			for (int i = entry.getValue().getLeft() - 1; i < entry.getValue().getRight(); i++) {
				System.out
						.println(i + "  " + (key.getLeft() - 1) + "  " + channelMap.getBinContent(i, key.getLeft() - 1)
								+ "  " + channelMap.getBinContent(i, key.getLeft() - 3));
				aNewH2F.setBinContent(i, key.getLeft() - 1, channelMap.getBinContent(i, key.getLeft() - 3));

			}
			System.out.println("I have located a bad pin at layer " + key.getLeft() + " with wires from " + leftValue
					+ " to " + rightValue + " with string code " + key.getRight());

		}
		for (int j = 0; j < 6; j++) {// layers

			for (int i = 0; i < 112; i++) {

				if ((i + 1) <= bundle1.getRight() && (i + 1) >= bundle1.getLeft()) {
					aNewH2F.setBinContent(i, j, rn.nextInt(100 - 8 + 1) + 8);
				}
			}
		}

		c2.draw(aNewH2F);
		tabbedPane.add("Modified Superlayer " + (superLayer) + " Sector " + (sector), c2);

		JFrame frame2 = new JFrame("");
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screensize = null;
		screensize = Toolkit.getDefaultToolkit().getScreenSize();
		frame2.setSize((int) (screensize.getHeight() * .75 * 1.618), (int) (screensize.getHeight() * .75));
		mainPanel.add(tabbedPane);
		frame2.add(mainPanel);
		frame2.setVisible(true);
	}

	private static Pair<Integer, Integer> getKeys(Map<Pair<Integer, String>, Pair<Integer, Integer>> badHVPin2,
			Pair<Integer, String> value) {
		Pair<Integer, Integer> testing = null;
		for (Map.Entry<Pair<Integer, String>, Pair<Integer, Integer>> entry : badHVPin2.entrySet()) {
			Pair<Integer, String> key = entry.getKey();

			if (key.equals(value)) {
				testing = entry.getValue();
			}
		}
		return testing;
	}
}
