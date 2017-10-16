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

public class MakeLearningFilesFromHists {
	private Random rn;
	private DataProcessForTest dataprocess;
	private int superLayer = 1;
	private int sector = 2;
	private H2F channelMap;
	private H2F aNewH2F;

	private int nBundlesForChannels = 8;
	private List<H2F> badChannelH2F;
	private List<H2F> normalizedBadChannelH2F;
	private List<BufferedImage> badChannelImage;

	public MakeLearningFilesFromHists() {
		rn = new Random();
		dataprocess = new DataProcessForTest();
		openFile();
	}

	private void openFile() {
		this.badChannelH2F = new ArrayList<>();
		this.badChannelImage = new ArrayList<>();
		this.normalizedBadChannelH2F = new ArrayList<>();

		// dataprocess.openFile("/Users/michaelkunkel/WORK/CLAS/CLAS12/CLAS12Data/pass4/test762.hipo");//
		dataprocess.openFile(
				"/Volumes/Mac_Storage/Work_Codes/GIT_HUB/DCDataBaseWithSpark/DCdatabase/needs/out_clas12_000762_a00000.hipo");
		dataprocess.setNEvents(10000);
		dataprocess.processFile();

		makeHistograms();
	}

	private void makeHistograms() {
		this.channelMap = dataprocess.getHistogramByMap(superLayer, sector);
		this.aNewH2F = dataprocess.getHistogramByMap(superLayer, sector);
		removeBadPinsOrFuse();
		makeArrayofBadChannels();

		createSVMFile(badChannelH2F);
		createNormalizedSVMFile(normalizedBadChannelH2F);
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
			badChannelH2F.add(clone);
			normalizedBadChannelH2F.add(NormalizeHistogram(clone));
			badChannelImage.add(GreyPic(clone));
		}
	}

	public List<H2F> getH2FList() {
		return this.badChannelH2F;
	}

	public List<H2F> getH2FNormalizedList() {
		return this.normalizedBadChannelH2F;
	}

	public List<BufferedImage> getImageList() {
		return this.badChannelImage;
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
				new BufferedWriter(new FileWriter("data/ChannelLearningSVMFormat.txt", false)))) {
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

	private void createNormalizedSVMFile(List<H2F> normalizedBadChannelH2F) {
		try (PrintWriter out = new PrintWriter(
				new BufferedWriter(new FileWriter("data/NormalizedChannelLearningSVMFormat.txt", false)))) {
			int bundle = 1;
			for (H2F h2f : normalizedBadChannelH2F) {
				IDataSet dataSet = h2f;
				int featureVector = 0;
				out.print(bundle + " ");
				for (int xd = 0; xd < dataSet.getDataSize(0); xd++) {
					for (int yd = 0; yd < dataSet.getDataSize(1); yd++) {

						double dataWeight = dataSet.getData(xd, yd);
						if (dataWeight == 0) {
							dataWeight = 0.0000000000000001;
						}
						out.print(featureVector + ":" + dataWeight);
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

	public static Color toGray(Color c) {
		int red = (int) (c.getRed() * 0.21);
		int green = (int) (c.getGreen() * 0.72);
		int blue = (int) (c.getBlue() * 0.07);
		int sum = red + green + blue;
		Color newColor = new Color(sum, sum, sum);
		return newColor;
	}

	public static void main(String[] args) {
		Dimension screensize = null;
		screensize = Toolkit.getDefaultToolkit().getScreenSize();

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		MakeLearningFilesFromHists t1 = new MakeLearningFilesFromHists();

		List<H2F> aList = t1.getH2FList();
		List<H2F> aNormalizedList = t1.getH2FNormalizedList();
		int bundleNum = 1;
		for (H2F h2f : aList) {
			EmbeddedCanvas ctemp = new EmbeddedCanvas();
			ctemp.draw(h2f);
			tabbedPane.add("test " + bundleNum, ctemp);
			EmbeddedCanvas ctempNorm = new EmbeddedCanvas();
			ctempNorm.draw(aNormalizedList.get(bundleNum - 1));
			tabbedPane.add("Normalized " + bundleNum, ctempNorm);
			bundleNum++;
		}
		//
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new BorderLayout());
		JTabbedPane imageTabbedPane = new JTabbedPane();
		List<BufferedImage> aBufferedImages = t1.getImageList();
		bundleNum = 1;
		for (BufferedImage bufferedImage : aBufferedImages) {
			imageTabbedPane.add("GreyImage " + bundleNum, new JLabel(new ImageIcon(bufferedImage)));

			bundleNum++;

		}

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize((int) (screensize.getHeight() * .95 * 1.618), (int) (screensize.getHeight() * .95));

		imagePanel.add(imageTabbedPane);
		imagePanel.setSize((int) (screensize.getHeight() * .75 * 1.618), (int) (screensize.getHeight() * .75));
		frame.add(imagePanel);
		// frame.setTitle("Graphics ");
		// frame.pack();
		frame.setVisible(true);

		JFrame frame2 = new JFrame("");
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame2.setSize((int) (screensize.getHeight() * .75 * 1.618), (int) (screensize.getHeight() * .75));
		mainPanel.add(tabbedPane);
		frame2.add(mainPanel);
		frame2.setVisible(true);
	}
}
