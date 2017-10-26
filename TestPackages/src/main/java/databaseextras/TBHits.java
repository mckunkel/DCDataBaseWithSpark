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
package databaseextras;

public class TBHits {

	int id;
	int status;
	int sector;
	int superLayer;
	int layer;
	int wire;

	float time;
	float doca;
	float docaError;
	float trkDoca;
	float timeResidual;

	int LR;

	float X;
	float Z;
	float B;

	int clusterID;
	int trkID;

	public TBHits(int id, int status, int sector, int superLayer, int layer, int wire, float time, float doca,
			float docaError, float trkDoca, float timeResidual, int lR, float x, float z, float b, int clusterID,
			int trkID) {
		this.id = id;
		this.status = status;
		this.sector = sector;
		this.superLayer = superLayer;
		this.layer = layer;
		this.wire = wire;
		this.time = time;
		this.doca = doca;
		this.docaError = docaError;
		this.trkDoca = trkDoca;
		this.timeResidual = timeResidual;
		LR = lR;
		X = x;
		Z = z;
		B = b;
		this.clusterID = clusterID;
		this.trkID = trkID;
	}

	public TBHits(int trkID, int status) {
		this.status = status;

		this.trkID = trkID;
	}
	// @Override
	// public String toString() {
	// return "TBHits [id=" + id + ", status=" + status + ", sector=" + sector +
	// ", superLayer=" + superLayer
	// + ", layer=" + layer + ", wire=" + wire + ", time=" + time + ", doca=" +
	// doca + ", docaError="
	// + docaError + ", trkDoca=" + trkDoca + ", timeResidual=" + timeResidual +
	// ", LR=" + LR + ", X=" + X
	// + ", Z=" + Z + ", B=" + B + ", clusterID=" + clusterID + ", trkID=" +
	// trkID + "]";
	// }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getWire() {
		return wire;
	}

	public void setWire(int wire) {
		this.wire = wire;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public float getDoca() {
		return doca;
	}

	public void setDoca(float doca) {
		this.doca = doca;
	}

	public float getDocaError() {
		return docaError;
	}

	public void setDocaError(float docaError) {
		this.docaError = docaError;
	}

	public float getTrkDoca() {
		return trkDoca;
	}

	public void setTrkDoca(float trkDoca) {
		this.trkDoca = trkDoca;
	}

	public float getTimeResidual() {
		return timeResidual;
	}

	public void setTimeResidual(float timeResidual) {
		this.timeResidual = timeResidual;
	}

	public int getLR() {
		return LR;
	}

	public void setLR(int lR) {
		LR = lR;
	}

	public float getX() {
		return X;
	}

	public void setX(float x) {
		X = x;
	}

	public float getZ() {
		return Z;
	}

	public void setZ(float z) {
		Z = z;
	}

	public float getB() {
		return B;
	}

	public void setB(float b) {
		B = b;
	}

	public int getClusterID() {
		return clusterID;
	}

	public void setClusterID(int clusterID) {
		this.clusterID = clusterID;
	}

	public int getTrkID() {
		return trkID;
	}

	public void setTrkID(int trkID) {
		this.trkID = trkID;
	}

}
