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

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import database.utils.NumberConstants;

public class TableModel extends AbstractTableModel {

	private List<Row> wireList;
	private Dataset<Row> wireDF;
	private String[] colNames = { "Sector", "SuperLayer", "Layer", "Wire" };

	public TableModel() {
		this.wireList = new ArrayList<Row>();
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

		Row dataPoint = this.wireList.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return dataPoint.get(2);
		case 1:
			return dataPoint.get(1);
		case 2:
			return dataPoint.get(0);
		case 3:
			return dataPoint.get(3);
		default:
			return null;
		}
	}

	public void setWireList(List<Row> wireList) {
		this.wireList = wireList;
	}

	public void setWireSet(Dataset<Row> wireDF) {
		this.wireDF = wireDF;
	}

	public void updateTable() {
		fireTableDataChanged();
	}

}
