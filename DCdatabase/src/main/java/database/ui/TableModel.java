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

import javax.swing.table.AbstractTableModel;

import database.utils.EmptyDataPoint;
import database.utils.NumberConstants;

public class TableModel extends AbstractTableModel {

	private List<EmptyDataPoint> wireList;
	private String[] colNames = { "Sector", "SuperLayer", "Layer", "Wire" };

	public TableModel() {
		this.wireList = new ArrayList<EmptyDataPoint>();
	}

	public int getRowCount() {
		return this.wireList.size();
	}

	public int getColumnCount() {
		return NumberConstants.NUM_OF_COLUMNS;
	}

	public String getColumnName(int column) {
		return this.colNames[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {

		EmptyDataPoint dataPoint = this.wireList.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return dataPoint.getSector();
		case 1:
			return dataPoint.getSuperLayer();
		case 2:
			return dataPoint.getLayer();
		case 3:
			return dataPoint.getWire();
		default:
			return null;
		}
	}

	public void setStudentList(List<EmptyDataPoint> wireList) {
		this.wireList = wireList;
	}

	public void updateTable() {
		fireTableDataChanged();
	}

}
