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

public class MakeLearningFilesFromHistsBackUp {
	private Random rn;
	private DataProcessForTest dataprocess;
	private int superLayer = 1;
	private int sector = 2;
	private H2F channelMap;
	private H2F aNewH2F;
	private H2F aNewH2FNormalized;
	private H2F greyH2F;

	private IDataSet dataSet;

	public MakeLearningFilesFromHistsBackUp() {
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
		makeHistograms();
	}

	private void makeHistograms() {
		channelMap = dataprocess.getHistogramByMap(superLayer, sector);
		aNewH2F = new H2F("something", channelMap.getXAxis().getNBins(), channelMap.getXAxis().min(),
				channelMap.getXAxis().max(), channelMap.getYAxis().getNBins(), channelMap.getYAxis().min(),
				channelMap.getYAxis().max());
		for (int j = 0; j < 6; j++) {// layers

			for (int i = 0; i < 112; i++) {
				aNewH2F.setBinContent(i, j, channelMap.getBinContent(i, j));
			}
		}

		setBadBundle();
	}

	private void setBadBundle() {
		Pair<Integer, Integer> bundle1 = ChannelBundles.switchChannelBundle(1);
		Map<Pair<Integer, String>, Pair<Integer, Integer>> badHVPin2 = HVPinDecision.BadHVPin(channelMap);
		for (Map.Entry<Pair<Integer, String>, Pair<Integer, Integer>> entry : badHVPin2.entrySet()) {
			Pair<Integer, String> key = entry.getKey();
			for (int i = entry.getValue().getLeft() - 1; i < entry.getValue().getRight(); i++) {
				aNewH2F.setBinContent(i, key.getLeft() - 1, channelMap.getBinContent(i, key.getLeft() - 3));
			}

		}
		for (int j = 0; j < 6; j++) {// layers
			for (int i = 0; i < 112; i++) {
				if ((i + 1) <= bundle1.getRight() && (i + 1) >= bundle1.getLeft()) {
					aNewH2F.setBinContent(i, j, rn.nextInt(10 - 1 + 1) + 1);
				}
			}
		}
		// aNewH2F = NormalizeHistogram(aNewH2F);
		this.aNewH2FNormalized = NormalizeHistogram(aNewH2F);
		this.dataSet = aNewH2F;
		this.greyH2F = GreyHist(aNewH2F);

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
		MakeLearningFilesFromHistsBackUp t1 = new MakeLearningFilesFromHistsBackUp();
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

		ColorPalette palette = new ColorPalette();
		IDataSet dataSet = aNewH2F;
		IDataSet dataSetII = somthing;

		int npointsX = dataSet.getDataSize(0);
		int npointsY = dataSet.getDataSize(1);
		Dimension1D dataRegionZ = new Dimension1D();
		dataRegionZ.setMinMax(dataSet.getData(0, 0), dataSet.getData(0, 0));
		for (int xd = 0; xd < dataSet.getDataSize(0); xd++) {
			for (int yd = 0; yd < dataSet.getDataSize(1); yd++) {
				dataRegionZ.grow(dataSet.getData(xd, yd));
			}
		}
		BufferedImage image = new BufferedImage(npointsX, npointsY, BufferedImage.TYPE_INT_RGB);

		for (int xd = 0; xd < dataSet.getDataSize(0); xd++) {
			for (int yd = 0; yd < dataSet.getDataSize(1); yd++) {

				double dataWeight = dataSet.getData(xd, yd);
				double normalizedDataWeight = dataSetII.getData(xd, yd);

				boolean zAxisLog = false;
				// System.out.println("2D plotter axis Z " + zAxisLog);
				Color weightColor;
				weightColor = palette.getColor3D(dataWeight, dataRegionZ.getMax(), zAxisLog);
				image.setRGB(xd, yd, toGray(weightColor).getRGB());
				BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
				img.setRGB(0, 0, toGray(weightColor).getRGB());

				Raster raster = img.getData();
				System.out.println(raster.getSample(0, 0, 0) + " raster sample");

				System.out.println("h2d: dataweight " + dataWeight + "normalized: dataweight " + normalizedDataWeight
						+ "weightColor:" + weightColor + " grayColor:" + toGray(weightColor) + "dataRegionZ.getMax():"
						+ dataRegionZ.getMax());

			}
		}

		Raster raster = image.getData();
		int w = raster.getWidth(), h = raster.getHeight();
		// out.print(file1.getName());
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				System.out.println(raster.getSample(x, y, 0) + " from entire image");
			}
		}
		ImageIcon icon = new ImageIcon(image);
		JLabel jLabel = new JLabel(icon);

		tabbedPane.add("Modified Superlayer " + (t1.getSuperLayer()) + " Sector " + (t1.getSector()), c2);
		tabbedPane.add("Normalized Superlayer " + (t1.getSuperLayer()) + " Sector " + (t1.getSector()), c3);

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

	private void createSVMFiles(int bundle, IDataSet dataSet) throws IOException {
		ColorPalette palette = new ColorPalette();

		try (PrintWriter out = new PrintWriter(
				new BufferedWriter(new FileWriter("data/data-" + bundle + ".txt", true)))) {

			Dimension1D dataRegionZ = this.growDataRegion(dataSet);

			for (int xd = 0; xd < dataSet.getDataSize(0); xd++) {
				for (int yd = 0; yd < dataSet.getDataSize(1); yd++) {

					double dataWeight = dataSet.getData(xd, yd);

					boolean zAxisLog = false;
					// System.out.println("2D plotter axis Z " + zAxisLog);
					Color weightColor = palette.getColor3D(dataWeight, dataRegionZ.getMax(), zAxisLog);
					BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
					img.setRGB(0, 0, toGray(weightColor).getRGB());
					Raster raster = img.getData();
					System.out.println(raster.getSample(0, 0, 0) + " raster sample");

					System.out.println("h2d: dataweight " + dataWeight + "normalized: dataweight " + "weightColor:"
							+ weightColor + " grayColor:" + toGray(weightColor) + "dataRegionZ.getMax():"
							+ dataRegionZ.getMax());

				}
			}

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

	public static Color toGray(Color c) {
		int red = (int) (c.getRed() * 0.21);
		int green = (int) (c.getGreen() * 0.72);
		int blue = (int) (c.getBlue() * 0.07);
		int sum = red + green + blue;
		Color newColor = new Color(sum, sum, sum);
		return newColor;
	}
}
