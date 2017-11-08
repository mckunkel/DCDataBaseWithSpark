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


public class NaughtyContainer {
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