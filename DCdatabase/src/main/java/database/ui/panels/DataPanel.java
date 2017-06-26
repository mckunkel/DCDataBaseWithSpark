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
package database.ui.panels;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.spark.sql.Dataset;

import database.objects.StatusChangeDB;
import database.service.MainFrameService;
import database.ui.TableModel;
import database.utils.MainFrameServiceManager;
import database.utils.NumberConstants;
import database.utils.StringConstants;

public class DataPanel extends JPanel {
	private MainFrameService mainFrameService = null;

	private JTable aTable;
	private TableModel tableModel;

	final int space = NumberConstants.BORDER_SPACING;
	Border spaceBorder = null;
	Border titleBorder = null;

	public DataPanel() {
		initializeVariables();
		initializeTableAlignment();
		initializeHeaderAlignment();
		mouseListener();
		constructLayout();
	}

	private void initializeTableAlignment() {

		DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
		tableCellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

		this.aTable.getColumnModel().getColumn(0).setCellRenderer(tableCellRenderer);
		this.aTable.getColumnModel().getColumn(1).setCellRenderer(tableCellRenderer);
		this.aTable.getColumnModel().getColumn(2).setCellRenderer(tableCellRenderer);
		this.aTable.getColumnModel().getColumn(3).setCellRenderer(tableCellRenderer);

	}

	private void initializeHeaderAlignment() {
		DefaultTableCellRenderer headerCellRenderer = new DefaultTableCellRenderer();
		headerCellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		this.aTable.getTableHeader().setDefaultRenderer(headerCellRenderer);
	}

	private void constructLayout() {
		setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));
		setLayout(new BorderLayout());
		add(new JScrollPane(aTable), BorderLayout.CENTER);
		// TableConstraints.setJTableColumnsWidth(this.aTable, 200, 70, 10, 10,
		// 10);
	}

	private void initializeVariables() {
		this.mainFrameService = MainFrameServiceManager.getSession();

		this.spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		this.titleBorder = BorderFactory.createTitledBorder(StringConstants.DATA_FORM_LABEL);
		this.tableModel = new TableModel();
		this.aTable = new JTable(tableModel);
	}

	public void setTableModel(Dataset<StatusChangeDB> wireSet) {
		this.tableModel.setWireSet(wireSet);
	}

	public void updateTable() {
		this.tableModel.updateTable();
	}

	public void loadData() {
		this.aTable.removeAll();
	}

	public void removeItems(TreeSet<StatusChangeDB> statusChangeDBs) {
		this.tableModel.removeRow(statusChangeDBs);
	}

	public void compareWithSQLPanel(Dataset<StatusChangeDB> wireSet) {
		this.tableModel.compareWithSQLPanel(wireSet, this.mainFrameService.getCompleteSQLList());
	}
	// private final int[] selection = null;

	private void mouseListener() {
		// List<StatusChangeDB> queryList = new ArrayList<>();
		TreeSet<StatusChangeDB> queryList = new TreeSet<>();
		TreeSet<Integer> intList = new TreeSet<>();

		this.aTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 1) {
					final JTable target = (JTable) e.getSource();
					// target.setRowSelectionAllowed(true);
					// target.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					int[] selection = target.getSelectedRows();
					for (int i : selection) {
						intList.add(i);
						StatusChangeDB statusChangeDB = new StatusChangeDB();
						statusChangeDB.setSector(target.getValueAt(i, 0).toString());
						statusChangeDB.setSuperlayer(target.getValueAt(i, 1).toString());
						statusChangeDB.setLoclayer(target.getValueAt(i, 2).toString());
						statusChangeDB.setLocwire(target.getValueAt(i, 3).toString());
						queryList.add(statusChangeDB);
					}
				}
			}
		});

		this.mainFrameService.prepareMYSQLQuery(queryList);
		this.mainFrameService.setListIndices(intList);

	}
}
