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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.commons.lang3.tuple.Pair;
import org.jlab.groot.base.ColorPalette;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.math.Dimension1D;

import database.process.DataProcessForTest;
import database.utils.ChannelBundles;
import spark.utils.decision.HVPinDecision;

public class MakeLearningFilesFromHistsOriginal {
	private Random rn;
	private DataProcessForTest dataprocess;
	private int superLayer = 1;
	private int sector = 2;
	private H2F channelMap;
	private H2F aNewH2F;
	private H2F aNewH2FNormalized;
	private H2F greyH2F;
	private BufferedImage aImage = null;

	private int nBundlesForChannels = 8;
	private List<H2F> badChannelH2F;

	private IDataSet dataSet;

	public MakeLearningFilesFromHistsOriginal() {
		rn = new Random();
		dataprocess = new DataProcessForTest();
		openFile();
	}

	private void openFile() {
		// dataprocess.openFile("/Users/michaelkunkel/WORK/CLAS/CLAS12/CLAS12Data/pass4/test762.hipo");//
		dataprocess.openFile(
				"/Volumes/Mac_Storage/Work_Codes/GIT_HUB/DCDataBaseWithSpark/DCdatabase/needs/out_clas12_000762_a00000.hipo");
		dataprocess.setNEvents(10000);
		dataprocess.processFile();
		this.badChannelH2F = new ArrayList<>();
		makeHistograms();
	}

	private void makeHistograms() {
		this.channelMap = dataprocess.getHistogramByMap(superLayer, sector);
		this.aNewH2F = dataprocess.getHistogramByMap(superLayer, sector);
		removeBadPinsOrFuse();
		makeArrayofBadChannels();
		createSVMFile(badChannelH2F);

	}

	private void removeBadPinsOrFuse() {
		Map<Pair<Integer, String>, Pair<Integer, Integer>> badHVPin2 = HVPinDecision.BadHVPin(channelMap);
		for (Map.Entry<Pair<Integer, String>, Pair<Integer, Integer>> entry : badHVPin2.entrySet()) {
			Pair<Integer, String> key = entry.getKey();
			for (int i = entry.getValue().getLeft() - 1; i < entry.getValue().getRight(); i++) {
				aNewH2F.setBinContent(i, key.getLeft() - 1, channelMap.getBinContent(i, key.getLeft() - 3));
			}
		}
	}

	private void makeArrayofBadChannels() {

		for (int k = 0; k < nBundlesForChannels; k++) {
			Pair<Integer, Integer> bundle1 = ChannelBundles.switchChannelBundle(k + 1);
			H2F clone = aNewH2F.histClone("aclone");

			for (int j = 0; j < 6; j++) {// layers
				for (int i = 0; i < 112; i++) {
					if ((i + 1) <= bundle1.getRight() && (i + 1) >= bundle1.getLeft()) {
						clone.setBinContent(i, j, rn.nextInt(10 - 1 + 1) + 1);
					}
				}
			}
			System.out.println("made histo " + k);
			badChannelH2F.add(clone);
		}
	}

	public void setBadBundle(int bundleNumber) {

		Pair<Integer, Integer> bundle1 = ChannelBundles.switchChannelBundle(bundleNumber);
		H2F clone = aNewH2F.histClone("aclone");

		for (int j = 0; j < 6; j++) {// layers
			for (int i = 0; i < 112; i++) {
				if ((i + 1) <= bundle1.getRight() && (i + 1) >= bundle1.getLeft()) {
					aNewH2F.setBinContent(i, j, rn.nextInt(10 - 1 + 1) + 1);
					clone.setBinContent(i, j, rn.nextInt(10 - 1 + 1) + 1);
				}
			}
		}
		// aNewH2F = NormalizeHistogram(aNewH2F);
		this.aNewH2FNormalized = NormalizeHistogram(aNewH2F);
		this.dataSet = aNewH2F;
		this.greyH2F = GreyHist(aNewH2F);
		this.aImage = GreyPic(aNewH2F);
		createSVMFiles(bundleNumber, aNewH2F);

	}

	public H2F getChannelMap() {
		return this.channelMap;
	}

	public H2F getaNewH2F() {
		return this.aNewH2F;
	}

	public H2F getGreyH2F() {
		return this.greyH2F;
	}

	public H2F getaNewH2FNormalized() {
		return this.aNewH2FNormalized;
	}

	public BufferedImage getImage() {
		return this.aImage;
	}

	public List<H2F> getH2FList() {
		return this.badChannelH2F;
	}

	public int getSector() {
		return this.sector;
	}

	public int getSuperLayer() {
		return this.superLayer;
	}

	public static void main(String[] args) {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		MakeLearningFilesFromHistsOriginal t1 = new MakeLearningFilesFromHistsOriginal();
		t1.setBadBundle(1);
		// lets check out sl 1 sec 2 and
		// sl 3 sec 2
		// chosen sl 1 and sec 2 because it was more statistically filled

		EmbeddedCanvas c1 = new EmbeddedCanvas();
		c1.draw(t1.getChannelMap());
		tabbedPane.add("Superlayer " + (t1.getSuperLayer()) + " Sector " + (t1.getSector()), c1);

		EmbeddedCanvas c2 = new EmbeddedCanvas();
		H2F aNewH2F = t1.getaNewH2F();
		c2.draw(aNewH2F);
		EmbeddedCanvas c3 = new EmbeddedCanvas();
		H2F somthing = t1.getaNewH2FNormalized();
		c3.draw(somthing);

		EmbeddedCanvas c4 = new EmbeddedCanvas();
		H2F greyHist = t1.getGreyH2F();
		c4.draw(greyHist);

		ImageIcon icon = new ImageIcon(t1.getImage());
		JLabel jLabel = new JLabel(icon);

		tabbedPane.add("Modified Superlayer " + (t1.getSuperLayer()) + " Sector " + (t1.getSector()), c2);
		tabbedPane.add("Normalized Superlayer " + (t1.getSuperLayer()) + " Sector " + (t1.getSector()), c3);
		// tabbedPane.add("Grey " + (t1.getSuperLayer()) + " Sector " +
		// (t1.getSector()), jLabel);

		List<H2F> aList = t1.getH2FList();
		int bundleNum = 1;
		for (H2F h2f : aList) {
			EmbeddedCanvas ctemp = new EmbeddedCanvas();
			ctemp.draw(h2f);
			tabbedPane.add("test " + bundleNum, ctemp);
			System.out.println("test" + bundleNum);
			bundleNum++;

		}

		JFrame frame = new JFrame();
		frame.setContentPane(jLabel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Graphics " + (t1.getSuperLayer()) + " Sector " + (t1.getSector()));
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		// tabbedPane.add("Graphics " + (superLayer) + " Sector " + (sector),
		// frame);

		JFrame frame2 = new JFrame("");
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screensize = null;
		screensize = Toolkit.getDefaultToolkit().getScreenSize();
		frame2.setSize((int) (screensize.getHeight() * .75 * 1.618), (int) (screensize.getHeight() * .75));
		mainPanel.add(tabbedPane);
		frame2.add(mainPanel);
		frame2.setVisible(true);
	}

	private Dimension1D growDataRegion(IDataSet dataSet) {
		Dimension1D dataRegion = new Dimension1D();
		dataRegion.setMinMax(dataSet.getData(0, 0), dataSet.getData(0, 0));
		for (int xd = 0; xd < dataSet.getDataSize(0); xd++) {
			for (int yd = 0; yd < dataSet.getDataSize(1); yd++) {
				dataRegion.grow(dataSet.getData(xd, yd));
			}
		}
		return dataRegion;
	}

	private void createSVMFile(List<H2F> badChannelH2F) {
		ColorPalette palette = new ColorPalette();

		try (PrintWriter out = new PrintWriter(
				new BufferedWriter(new FileWriter("data/ChannelLearningSVMFormat.txt", true)))) {
			int bundle = 1;
			for (H2F h2f : badChannelH2F) {
				IDataSet dataSet = h2f;
				Dimension1D dataRegionZ = this.growDataRegion(dataSet);
				int featureVector = 0;
				out.print(bundle + " ");
				for (int xd = 0; xd < dataSet.getDataSize(0); xd++) {
					for (int yd = 0; yd < dataSet.getDataSize(1); yd++) {

						double dataWeight = dataSet.getData(xd, yd);
						Color weightColor = palette.getColor3D(dataWeight, dataRegionZ.getMax(), false);
						int greyColor = toGray(weightColor).getBlue();
						if (greyColor != 0) {
							out.print(featureVector + ":" + greyColor);
						}
						out.print(" ");
						featureVector++;
					}
				}
				out.println("");
				bundle++;
			}

		} catch (

		IOException e) {
			// exception handling skipped for the reader
		}

	}

	private void createSVMFiles(int bundle, IDataSet dataSet) {// throws
																// IOException
		ColorPalette palette = new ColorPalette();

		try (PrintWriter out = new PrintWriter(
				new BufferedWriter(new FileWriter("data/data-" + bundle + ".txt", true)))) {

			Dimension1D dataRegionZ = this.growDataRegion(dataSet);
			int featureVector = 0;
			out.print(bundle + " ");
			for (int xd = 0; xd < dataSet.getDataSize(0); xd++) {
				for (int yd = 0; yd < dataSet.getDataSize(1); yd++) {

					double dataWeight = dataSet.getData(xd, yd);
					Color weightColor = palette.getColor3D(dataWeight, dataRegionZ.getMax(), false);
					int greyColor = toGray(weightColor).getBlue();
					if (greyColor != 0) {
						out.print(featureVector + ":" + greyColor);
					}
					out.print(" ");
					featureVector++;
				}
			}
			out.println("");

		} catch (

		IOException e) {
			// exception handling skipped for the reader
		}

	}

	private Pair<Integer, Integer> getKeys(Map<Pair<Integer, String>, Pair<Integer, Integer>> badHVPin2,
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

	private double findMax(H2F aH2f) {
		double max = 0.0;
		for (int i = 0; i < aH2f.getXAxis().getNBins(); i++) {
			for (int j = 0; j < aH2f.getYAxis().getNBins(); j++) {
				if (max < aH2f.getBinContent(i, j)) {
					max = aH2f.getBinContent(i, j);
				}
			}
		}
		return max;
	}

	private H2F NormalizeHistogram(H2F aH2f) {
		H2F clone = aH2f.histClone("aclone");
		double norm = findMax(aH2f);

		// System.out.println("Sum of Hitogram is:" + sum);
		for (int i = 0; i < aH2f.getXAxis().getNBins(); i++) {
			for (int j = 0; j < aH2f.getYAxis().getNBins(); j++) {
				clone.setBinContent(i, j, aH2f.getBinContent(i, j) / norm);
			}
		}
		return clone;
	}

	private BufferedImage GreyPic(H2F aH2f) {
		IDataSet dataSet = aH2f;
		ColorPalette palette = new ColorPalette();
		Dimension1D dataRegionZ = this.growDataRegion(dataSet);
		int sizeX = 10;
		int sizeY = sizeX * 10;

		BufferedImage image = new BufferedImage(dataSet.getDataSize(0) * sizeX, dataSet.getDataSize(1) * sizeY,
				BufferedImage.TYPE_INT_RGB);
		for (int xd = 0; xd < dataSet.getDataSize(0); xd++) {
			for (int yd = 0; yd < dataSet.getDataSize(1); yd++) {

				double dataWeight = dataSet.getData(xd, yd);
				Color weightColor = palette.getColor3D(dataWeight, dataRegionZ.getMax(), false);
				for (int i = 0; i < sizeX; i++) {
					for (int j = 0; j < sizeY; j++) {
						image.setRGB(sizeX * xd + i, sizeY * yd + j, toGray(weightColor).getRGB());

					}

				}
			}
		}

		return image;
	}

	private H2F GreyHist(H2F aH2f) {
		IDataSet dataSet = aH2f;
		ColorPalette palette = new ColorPalette();
		Dimension1D dataRegionZ = this.growDataRegion(dataSet);

		H2F clone = aH2f.histClone("aclone");

		for (int xd = 0; xd < dataSet.getDataSize(0); xd++) {
			for (int yd = 0; yd < dataSet.getDataSize(1); yd++) {

				double dataWeight = dataSet.getData(xd, yd);

				// System.out.println("2D plotter axis Z " + zAxisLog);
				Color weightColor = palette.getColor3D(dataWeight, dataRegionZ.getMax(), false);
				Color newColor = toGray(weightColor);
				BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
				img.setRGB(0, 0, toGray(weightColor).getRGB());

				Raster raster = img.getData();
				clone.setBinContent(xd, yd, raster.getSample(0, 0, 0));

			}
		}

		return clone;
	}

	public static Color toGray(Color c) {
		int red = (int) (c.getRed() * 0.21);
		int green = (int) (c.getGreen() * 0.72);
		int blue = (int) (c.getBlue() * 0.07);
		int sum = red + green + blue;
		Color newColor = new Color(sum, sum, sum);
		return newColor;
	}
}
