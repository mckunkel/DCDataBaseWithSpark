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
package database.objects;

import java.io.Serializable;
import java.sql.Timestamp;

public class StatusChangeDB implements Serializable {
	private static final long serialVersionUID = 1L;

	private int statchangeid;
	private Timestamp dateofentry;
	private int runno;
	private String status_change_type;
	private String problem_type;
	private String region;
	private String sector;
	private String superlayer;
	private String loclayer;
	private String locwire;
	private String hvcrateid;
	private String hvslotid;
	private String hvchannelid;
	private String hvpinid_region;
	private String hvpinid_quad;
	private String hvpinid_doublet;
	private String hvpinid_doublethalf;

	private String hvpinid_pin;
	private String dcrbconnectorid_slot;
	private String dcrbconnectorid_connector;
	private String lvfuseid_row;
	private String lvfuseid_col;

	public StatusChangeDB() {
	}

	public int getStatchangeid() {
		return statchangeid;
	}

	public void setStatchangeid(int statchangeid) {
		this.statchangeid = statchangeid;
	}

	public Timestamp getDateofentry() {
		return dateofentry;
	}

	public void setDateofentry(Timestamp dateofentry) {
		this.dateofentry = dateofentry;
	}

	public int getRunno() {
		return runno;
	}

	public void setRunno(int runno) {
		this.runno = runno;
	}

	public String getStatus_change_type() {
		return status_change_type;
	}

	public void setStatus_change_type(String status_change_type) {
		this.status_change_type = status_change_type;
	}

	public String getProblem_type() {
		return problem_type;
	}

	public void setProblem_type(String problem_type) {
		this.problem_type = problem_type;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getSuperlayer() {
		return superlayer;
	}

	public void setSuperlayer(String superlayer) {
		this.superlayer = superlayer;
	}

	public String getLoclayer() {
		return loclayer;
	}

	public void setLoclayer(String loclayer) {
		this.loclayer = loclayer;
	}

	public String getLocwire() {
		return locwire;
	}

	public void setLocwire(String locwire) {
		this.locwire = locwire;
	}

	public String getHvcrateid() {
		return hvcrateid;
	}

	public void setHvcrateid(String hvcrateid) {
		this.hvcrateid = hvcrateid;
	}

	public String getHvslotid() {
		return hvslotid;
	}

	public void setHvslotid(String hvslotid) {
		this.hvslotid = hvslotid;
	}

	public String getHvchannelid() {
		return hvchannelid;
	}

	public void setHvchannelid(String hvchannelid) {
		this.hvchannelid = hvchannelid;
	}

	public String getHvpinid_region() {
		return hvpinid_region;
	}

	public void setHvpinid_region(String hvpinid_region) {
		this.hvpinid_region = hvpinid_region;
	}

	public String getHvpinid_quad() {
		return hvpinid_quad;
	}

	public void setHvpinid_quad(String hvpinid_quad) {
		this.hvpinid_quad = hvpinid_quad;
	}

	public String getHvpinid_doublet() {
		return hvpinid_doublet;
	}

	public void setHvpinid_doublet(String hvpinid_doublet) {
		this.hvpinid_doublet = hvpinid_doublet;
	}

	public String getHvpinid_doublethalf() {
		return hvpinid_doublethalf;
	}

	public void setHvpinid_doublethalf(String hvpinid_doublethalf) {
		this.hvpinid_doublethalf = hvpinid_doublethalf;
	}

	public String getHvpinid_pin() {
		return hvpinid_pin;
	}

	public void setHvpinid_pin(String hvpinid_pin) {
		this.hvpinid_pin = hvpinid_pin;
	}

	public String getDcrbconnectorid_slot() {
		return dcrbconnectorid_slot;
	}

	public void setDcrbconnectorid_slot(String dcrbconnectorid_slot) {
		this.dcrbconnectorid_slot = dcrbconnectorid_slot;
	}

	public String getDcrbconnectorid_connector() {
		return dcrbconnectorid_connector;
	}

	public void setDcrbconnectorid_connector(String dcrbconnectorid_connector) {
		this.dcrbconnectorid_connector = dcrbconnectorid_connector;
	}

	public String getLvfuseid_row() {
		return lvfuseid_row;
	}

	public void setLvfuseid_row(String lvfuseid_row) {
		this.lvfuseid_row = lvfuseid_row;
	}

	public String getLvfuseid_col() {
		return lvfuseid_col;
	}

	public void setLvfuseid_col(String lvfuseid_col) {
		this.lvfuseid_col = lvfuseid_col;
	}

	@Override
	public String toString() {
		return "StatusChangeDB [statchangeid=" + statchangeid + ", dateofentry=" + dateofentry + ", runno=" + runno
				+ ", status_change_type=" + status_change_type + ", problem_type=" + problem_type + ", region=" + region
				+ ", sector=" + sector + ", superlayer=" + superlayer + ", loclayer=" + loclayer + ", locwire="
				+ locwire + ", hvcrateid=" + hvcrateid + ", hvslotid=" + hvslotid + ", hvchannelid=" + hvchannelid
				+ ", hvpinid_region=" + hvpinid_region + ", hvpinid_quad=" + hvpinid_quad + ", hvpinid_doublet="
				+ hvpinid_doublet + ", hvpinid_doublethalf=" + hvpinid_doublethalf + ", hvpinid_pin=" + hvpinid_pin
				+ ", dcrbconnectorid_slot=" + dcrbconnectorid_slot + ", dcrbconnectorid_connector="
				+ dcrbconnectorid_connector + ", lvfuseid_row=" + lvfuseid_row + ", lvfuseid_col=" + lvfuseid_col + "]";
	}

}
