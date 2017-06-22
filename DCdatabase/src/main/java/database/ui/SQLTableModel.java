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
package database.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.swing.table.AbstractTableModel;

import database.objects.StatusChangeDB;
import database.utils.NumberConstants;

public class SQLTableModel extends AbstractTableModel {

	private List<StatusChangeDB> wireList;
	private String[] colNames = { "Sector", "SuperLayer", "Layer", "Wire", "Problem Type", "Status Change" };

	public SQLTableModel() {
		this.wireList = new ArrayList<StatusChangeDB>();
	}

	public int getRowCount() {
		return this.wireList.size();
	}

	public int getColumnCount() {
		return NumberConstants.NUM_OF_COLUMNS + 2;
	}

	public String getColumnName(int column) {
		return this.colNames[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {

		// StatusChangeDB dataPoint = this.wireList.get(rowIndex);
		StatusChangeDB dataPoint = this.wireList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return dataPoint.getSector();
		case 1:
			return dataPoint.getSuperlayer();
		case 2:
			return dataPoint.getLoclayer();
		case 3:
			return dataPoint.getLocwire();
		case 4:
			return dataPoint.getProblem_type();
		case 5:
			return dataPoint.getStatus_change_type();
		default:
			return null;
		}
	}

	public void setWireSet(TreeSet<StatusChangeDB> wireDF) {
		List<StatusChangeDB> test = new ArrayList<StatusChangeDB>();
		test.addAll(wireDF);
		setWireList(test);
		updateTable();
	}

	public void setWireList(List<StatusChangeDB> wireList) {
		this.wireList = wireList;
	}

	public void updateTable() {
		fireTableDataChanged();
	}

}
