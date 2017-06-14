package database.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;

import database.service.MainFrameService;
import database.utils.MainFrameServiceManager;
import database.utils.NumberConstants;
import database.utils.StringConstants;

public class RunForm extends JDialog implements ActionListener {
	private MainFrame mainFrame = null;
	private MainFrameService mainFrameService = null;

	private JButton okButton;
	private JComboBox<String> fileComboBox;
	private JLabel fileLabel;

	private ArrayList<String> fileList = null;
	private String dirLocation = null;

	private JFrame errorFrame;

	public RunForm(MainFrame parentFrame) {
		super(parentFrame, StringConstants.RUN_FORM_TITLE, false);
		this.mainFrame = parentFrame;
		this.mainFrameService = MainFrameServiceManager.getSession();

		initializeVariables();
		constructLayout();
		setWindow(parentFrame);
	}

	public void setFileList(ArrayList<String> fileList) {
		this.fileList = fileList;
		loadData();
	}

	public void setDirectory(String str) {
		this.dirLocation = str;
	}

	private boolean checkValidFile() {
		if (this.fileList.size() == 0 || this.fileList == null) {
			return false;
		} else {
			return true;
		}
	}

	private void setWindow(JFrame parentFrame) {
		setSize(NumberConstants.SORT_FORM_WINDOW_SIZE_WIDTH, NumberConstants.SORT_FORM_WINDOW_SIZE_HEIGHT);
		setLocationRelativeTo(parentFrame);
	}

	private void initializeVariables() {
		this.okButton = new JButton(StringConstants.FORM_RUN);

		this.okButton.addActionListener(this);
		this.fileList = new ArrayList<String>();

		this.fileComboBox = new JComboBox<String>();
		this.fileLabel = new JLabel(StringConstants.FILE_FORM);

		this.errorFrame = new JFrame("");

	}

	public void loadData() {

		this.fileComboBox.removeAllItems();

		for (String str : fileList) {
			String diff = StringUtils.difference(dirLocation, str);
			this.fileComboBox.addItem(diff);
		}
	}

	private void constructLayout() {

		JPanel fileInfoPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		// ////////// Buttons Panel ///////////////
		int space = 15;
		Border spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		Border titleBorder = BorderFactory.createTitledBorder(StringConstants.FILE_FORM_SELECT);

		fileInfoPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));

		fileInfoPanel.setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();

		gc.gridy = 0;

		Insets rightPadding = new Insets(0, 0, 0, 15);
		Insets noPadding = new Insets(0, 0, 0, 0);

		// ///// First row /////////////////////////////

		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = rightPadding;
		fileInfoPanel.add(fileLabel, gc);

		gc.gridx++;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		fileInfoPanel.add(fileComboBox, gc);

		// ////////// Buttons Panel ///////////////

		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(okButton);

		Dimension btnSize = okButton.getPreferredSize();
		okButton.setPreferredSize(btnSize);

		// Add sub panels to dialog
		setLayout(new BorderLayout());
		add(fileInfoPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);

	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.okButton) {
			boolean checkFile = checkValidFile();
			if (checkFile) {
				String str = (String) this.fileComboBox.getSelectedItem();
				this.mainFrame.getDataProcess().openFile(dirLocation + str);

			} else {
				System.out.println("Problem");
				JOptionPane.showMessageDialog(errorFrame, "Eggs are not supposed to be green.", "Please choose a file",
						JOptionPane.ERROR_MESSAGE);
			}
			setVisible(false);
			this.mainFrame.getDataProcess().processFile();
			processCommands();
		}
	}

	private void processCommands() {
		this.mainFrame.getDataPanel().setTableModel(this.mainFrameService.getDatasetAsList());
		this.mainFrame.getDataPanel().setTableModel(this.mainFrameService.getDataset());
		Dataset<String> testDF = this.mainFrameService.getDataset().map(new MapFunction<Row, String>() {
			@Override
			public String call(Row row) throws Exception {
				return "Name: " + row.getInt(0) + "  " + row.getInt(1);
			}
		}, Encoders.STRING());
		testDF.show();
		this.mainFrame.getDataPanel().updateTable();
	}
}
