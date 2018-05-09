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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import database.objects.CCDBWireStatusObject;
import database.query.DBQuery;
import database.service.InsertMYSqlQuery;
import database.service.InsertMYSqlServiceManager;
import database.service.MainFrameService;
import database.ui.MainFrame;
import database.utils.MainFrameServiceManager;
import database.utils.NumberConstants;
import database.utils.PanelConstraints;
import database.utils.StringConstants;
import spark.utils.SparkManager;

public class DBSendPanel extends JPanel implements ActionListener {
	private InsertMYSqlQuery insertMYSqlQuery = null;
	private MainFrameService mainFrameService = null;
	private DBQuery dbQuery = null;
	final int space = NumberConstants.BORDER_SPACING;
	Border spaceBorder = null;
	Border titleBorder = null;
	// JButton removeButton = null;
	JButton sendButton = null;
	JButton ccDBButton = null;
	private JFrame errorFrame;

	BufferedWriter writer = null;
	FileOutputStream fileOutputStream = null;

	public DBSendPanel(MainFrame parentFrame) {
		initializeVariables();
		initialLayout();
	}

	private void initializeVariables() {
		this.insertMYSqlQuery = InsertMYSqlServiceManager.getSession();
		this.mainFrameService = MainFrameServiceManager.getSession();
		this.dbQuery = new DBQuery();

		this.spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		this.titleBorder = BorderFactory.createTitledBorder(StringConstants.DBSEND_FORM_LABEL);

		// this.removeButton = new JButton(StringConstants.DBSEND_FORM_REMOVE);
		this.sendButton = new JButton(StringConstants.DBSEND_FORM_SEND);
		this.ccDBButton = new JButton(StringConstants.CCDBSEND_FORM_SEND);
		// this.removeButton.addActionListener(this);
		this.sendButton.addActionListener(this);
		this.ccDBButton.addActionListener(this);

		this.errorFrame = new JFrame("");

	}

	private void initialLayout() {
		setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));
		setLayout(new GridBagLayout());
		// PanelConstraints.addComponent(this, new JLabel(""), 0, 0, 1, 1,
		// GridBagConstraints.CENTER,
		// GridBagConstraints.BOTH, 0, 0);
		PanelConstraints.addComponent(this, sendButton, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				200, 0);
		PanelConstraints.addComponent(this, ccDBButton, 0, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				200, 0);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.sendButton) {
			System.out.println("Will send the query with selected faults and wires from the sql panel");
			if (this.mainFrameService.getCompleteSQLList() == null
					|| this.mainFrameService.getCompleteSQLList().size() == 0) {
				JOptionPane.showMessageDialog(errorFrame, "Nothing processed for MySQL!",
						"Eggs are not supposed to be green.", JOptionPane.ERROR_MESSAGE);
			} else {
				this.insertMYSqlQuery.prepareMYSQLQuery();
				this.mainFrameService.getCompleteSQLList().clear();
				this.mainFrameService.getSQLPanel().setTableModel(this.mainFrameService.getCompleteSQLList());
				this.mainFrameService.setSentTodb(true);
				SparkManager.restart();
				this.mainFrameService.addRunToList(this.mainFrameService.getRunNumber());
			}

		}
		if (event.getSource() == this.ccDBButton) {
			if (this.mainFrameService.getRunList() == null || this.mainFrameService.getRunList().size() == 0) {
				showNoDataChoice();
			} else {
				Object[] choices = { "Manual", "Automatic" };
				int n = JOptionPane.showOptionDialog(errorFrame,
						"Manual or Automatic insertation into the CCDB? \n\tManual: will create scripts for user. \n\tUser will follow standard Jlab protocal to insert values to CCDB \n\n\tAutomatic: assumes user in on Jlab cluster and has permissions to send to CCDB",
						"I do like them Sam-I-Am", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						choices, choices[1]);// showConfirmDialog
				if (n == -1) {// hit the x button
					JOptionPane.showMessageDialog(errorFrame, "Nothing processed for CCDB!",
							"Eggs are not supposed to be green.", JOptionPane.ERROR_MESSAGE);
				} else if (n == 0) {// user hit yes and wants to create the
									// scripts
					System.out.println("Creating scripts");
				} else {
					System.out.println("Just process CCDB");
					fileDelete();
					openFile();
					makeFileHeader();
					processCCDBRequest();
					closeFile();

				}

			}
		}
	}

	private void openFile() {
		try {
			this.writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("SubmitStatusTablesToCCDB.sh"), "utf-8"));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void appendFile() {
		try {
			this.writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("SubmitStatusTablesToCCDB.sh", true), "utf-8"));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void makeFileHeader() {
		try {
			writer.write("#!/bin/csh");
			writer.newLine();
			writer.write("source /group/clas12/gemc/environment.csh");
			writer.newLine();
			writer.write("setenv CCDB_CONNECTION mysql://clas12writer:geom3try@clasdb.jlab.org/clas12");
			writer.newLine();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void closeFile() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showNoDataChoice() {
		int n = JOptionPane.showConfirmDialog(errorFrame,
				"No data has been sent to the MySQL. \nDo you want to create CCDB entries from past MySQL entries?",
				"Eggs are not supposed to be green.", JOptionPane.YES_NO_OPTION);
		if (n == 1) {// No was pressed
			JOptionPane.showMessageDialog(errorFrame, "Please insert new entires into the MySQL database",
					"Eggs are not supposed to be green.", JOptionPane.ERROR_MESSAGE);
		} else {
			String[] stringArray = dbQuery.getAllRuns().toArray(new String[0]);
			String s = (String) JOptionPane.showInputDialog(errorFrame, "Please Select which Run", "Customized Dialog",
					JOptionPane.PLAIN_MESSAGE, null, stringArray, stringArray[0]);
			JTable aJTable = createTable();
			JOptionPane.showMessageDialog(null, new JScrollPane(aJTable));

			if (s != null) {
				if (fileExist()) {
					appendFile();
					addToQueryFile(Integer.valueOf(s));
					closeFile();
				} else {
					openFile();
					makeFileHeader();
					addToQueryFile(Integer.valueOf(s));
					closeFile();
				}
			}
		}
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

	private void processCCDBRequest() {

		for (Integer i : this.mainFrameService.getRunList()) {
			List<CCDBWireStatusObject> aList = dbQuery.getBadComponentList(i);
			List<CCDBWireStatusObject> allComponents = new ArrayList<>();

			for (int sector = 1; sector <= 6; sector++) {
				for (int superLayer = 1; superLayer <= 6; superLayer++) {
					for (int locLayer = 1; locLayer <= 6; locLayer++) {
						int layer = (superLayer - 1) * 6 + locLayer;
						for (int wire = 1; wire <= 112; wire++) {
							allComponents.add(findLike(sector, layer, wire, aList));
						}
					}
				}
			}

			createCCDBFile(i, allComponents);
			addToQueryFile(i);
		}

	}

	private void addToQueryFile(Integer i) {
		try {
			writer.write("ccdb add calibration/dc/tracking/wire_status -r " + i + "-2147483647 Run_" + i
					+ ".txt #Adding run " + i);
			writer.newLine();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private CCDBWireStatusObject findLike(int sector, int layer, int wire, List<CCDBWireStatusObject> aList) {
		for (int status = 1; status <= 7; status++) {
			if (aList.contains(new CCDBWireStatusObject(sector, layer, wire, status))) {
				return new CCDBWireStatusObject(sector, layer, wire, status);
			}
		}
		return new CCDBWireStatusObject(sector, layer, wire, 0);
	}

	private void createCCDBFile(Integer i, List<CCDBWireStatusObject> allComponents) {
		try (BufferedWriter runWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("Run_" + i + ".txt"), "utf-8"))) {
			runWriter.write("#& sector layer component status");
			runWriter.newLine();
			for (CCDBWireStatusObject ccdbWireStatusObject : allComponents) {
				runWriter.write(ccdbWireStatusObject.getSector() + " " + ccdbWireStatusObject.getLayer() + " "
						+ ccdbWireStatusObject.getComponent() + " " + ccdbWireStatusObject.getStatus());
				runWriter.newLine();
			}
			runWriter.close();
		} catch (

		UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void fileDelete() {
		File file = new File("SubmitStatusTablesToCCDB.sh");
		if (file.exists() && !file.isDirectory()) {
			file.delete();
		}
	}

	private boolean fileExist() {
		File file = new File("SubmitStatusTablesToCCDB.sh");
		return file.exists();
	}

}
