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
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import database.query.DBQuery;

public class MyTable {
	private DBQuery dbQuery = new DBQuery();
	private JButton scriptButton;
	private JButton executeButton;
	private JButton cancelButton;

	public MyTable() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(createTable()));
		JFrame frame = new JFrame("My Table");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.setSize(new Dimension(400, 130));
		frame.setVisible(true);
		// JDialog jDialog = new JDialog();
		// jDialog.setLocationRelativeTo(null);
		// jDialog.setLayout(new BorderLayout());
		// jDialog.add(new JScrollPane(createTable(),
		// JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		// JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		// jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		// // setModalityType(ModalityType.APPLICATION_MODAL);
		// jDialog.pack();
		// jDialog.setVisible(true);
		// JTable aJTable = createTable();
		//
		// JOptionPane.showInputDialog(new JFrame(""), "Please Select which
		// Run", "Customized Dialog",
		// JOptionPane.PLAIN_MESSAGE, null, new JScrollPane(aJTable),
		// aJTable.getValueAt(0, 0));
		//
		// JOptionPane.showMessageDialog(null, new JScrollPane(aJTable), "Slect
		// this", JOptionPane.ERROR_MESSAGE);
		// JOptionPane.showMessageDialog(null, new JScrollPane(aJTable));
	}

	private JTable createTable() {
		List<String> runList = dbQuery.getAllRuns();
		Object[][] data = new Object[runList.size()][runList.size()];
		for (int i = 0; i < runList.size(); i++) {
			for (int j = 0; j < runList.size(); j++) {
				data[i][j] = runList.get(i);
			}
		}
		String[] columnNames = { "Runs" };

		DefaultTableModel model = new DefaultTableModel(data, columnNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		return table;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				MyTable myTable = new MyTable();
			}
		});
	}
}
