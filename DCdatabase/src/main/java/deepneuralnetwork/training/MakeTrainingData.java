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
package deepneuralnetwork.training;

import java.util.Random;

import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.ui.TCanvas;

import database.faults.ChannelLogic;
import database.faults.DeadWireLogic;
import database.faults.FaultLogic;
import database.faults.FuseLogic;
import database.faults.HotWireLogic;
import database.faults.PinLogic;
import database.faults.SignalLogic;
import database.utils.NumberConstants;

public class MakeTrainingData {

	// used to randomize the choosing of faults and number of faults
	private Random random;

	// total number of faults...including NO faults
	private int numFaults;

	// chooses the type of random fault. Faults can be stacked, meaning it can
	// be chosen to have 2 bad hvpins or 2 bad fuses
	// however if the random choice picks a dead wire or hot wire, the number of
	// wires will also be chosen at random
	private int[] randomFault;
	private FaultLogic[] randomFaultLogics;

	public MakeTrainingData() {
		this.random = new Random();

	}

	private void run() {
		// here we choose the maximum number of faults in any given sequence
		// will be 6 i.e. random is 0 inclusive and 7 exclusive..DOES NOT
		// include 7
		this.numFaults = random.nextInt(7);
		randomFault = new int[numFaults];
		randomFaultLogics = new FaultLogic[numFaults];
		for (int i = 0; i < numFaults; i++) {
			// we have 6 types of faults
			// hvchannel, hvpin, lvfuse, signalconnector, dead wire, hotwire
			// see MainFrameServicecImpl for setFault for hints on how I used
			// this in the past
			// lets make this choice from 1-6 for better clarity
			randomFault[i] = random.nextInt(6) + 1;
			randomFaultLogics[i] = getFault(randomFault[i]);
		}
	}

	public int[] getFaults() {
		return randomFault;
	}

	public FaultLogic getFault(int fault) {
		FaultLogic faultLogic = null;
		if (fault == 0) {
			faultLogic = new ChannelLogic();
		} else if (fault == 1) {
			faultLogic = new PinLogic();
		} else if (fault == 2) {
			faultLogic = new FuseLogic();
		} else if (fault == 3) {
			faultLogic = new SignalLogic();
		} else if (fault == 4) {
			faultLogic = new DeadWireLogic();
		} else if (fault == 5) {
			faultLogic = new HotWireLogic();
		} else {
			System.err.println("No Fault defined");
		}
		return faultLogic;
	}

	public TCanvas drawFaults() {
		TCanvas canvas = new TCanvas("name", 800, 800);
		canvas.divide(1, 2);
		H1F h1f = new H1F("ah1f", 100, -1, 10);
		h1f.setTitle("Type of Fault");
		H1F h2f = new H1F("ah2f", 100, -1, 10);
		h2f.setTitle("Number of Faults");

		for (int i = 0; i < 1000; i++) {
			run();
			h2f.fill(numFaults);
			for (int j = 0; j < randomFault.length; j++) {
				// System.out.println(randomFault[j]);
				h1f.fill(randomFault[j]);
			}
		}
		canvas.draw(h1f);
		canvas.cd(1);
		canvas.draw(h2f);
		return canvas;
	}

	public H1F allFaults() {
		H1F h1f = new H1F("ah1f", 100, -1, 10);

		for (int i = 0; i < 1000; i++) {
			run();
			for (int j = 0; j < randomFault.length; j++) {
				// System.out.println(randomFault[j]);
				h1f.fill(randomFault[j]);
			}
		}

		return h1f;
	}

	private H2F makeFaultDiagram() {
		H2F aH2f = new H2F("Occupancy all hits in a Superlayer and Sector", NumberConstants.xBins, NumberConstants.xMin,
				NumberConstants.xMax, NumberConstants.yBins, NumberConstants.yMin, NumberConstants.yMax);

		for (int i = 0; i < aH2f.getXAxis().getNBins(); i++) {
			for (int j = 0; j < aH2f.getYAxis().getNBins(); j++) {
				aH2f.setBinContent(i, j, new Random().nextInt(1800));
			}
		}

		return aH2f;
	}

	public static void main(String[] args) {
		MakeTrainingData makeTrainingData = new MakeTrainingData();
		TCanvas canvas = new TCanvas("name", 800, 800);
		makeTrainingData.drawFaults();
		// makeTrainingData.run();
	}

}
