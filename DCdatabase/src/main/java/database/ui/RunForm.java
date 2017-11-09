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
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.apache.commons.lang3.StringUtils;

import database.service.MainFrameService;
import database.ui.panels.SortPanel;
import database.utils.MainFrameServiceManager;
import database.utils.NumberConstants;
import database.utils.PanelConstraints;
import database.utils.StringConstants;

public class RunForm extends JDialog implements ActionListener {
	private MainFrame mainFrame = null;
	private MainFrameService mainFrameService = null;
	private SortPanel sortPanel = null;

	private JButton okButton;
	private JTextField percentField;
	private JLabel percentLabel;
	private JComboBox<String> fileComboBox;
	private JLabel fileLabel;

	private ArrayList<String> fileList = null;
	private String dirLocation = null;

	private JFrame errorFrame;

	public RunForm(MainFrame parentFrame) {
		super(parentFrame, StringConstants.RUN_FORM_TITLE, false);
		this.mainFrame = parentFrame;
		this.mainFrameService = MainFrameServiceManager.getSession();
		this.sortPanel = new SortPanel();

		initializeVariables();
		constructLayout();
		setWindow(parentFrame);
		pack();

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

		this.percentField = new JTextField(NumberConstants.PERCENT_FORM_WINDOW_FIELD_LENGTH);
		this.percentLabel = new JLabel(StringConstants.RUNPERCENT);

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
		Border spaceBorder = BorderFactory.createEmptyBorder(NumberConstants.BORDER_SPACING,
				NumberConstants.BORDER_SPACING, NumberConstants.BORDER_SPACING, NumberConstants.BORDER_SPACING);
		Border titleBorder = BorderFactory.createTitledBorder(StringConstants.FILE_FORM_SELECT);
		fileInfoPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));
		fileInfoPanel.setLayout(new GridBagLayout());

		// Insets rightPadding = new Insets(0, 0, 0, 15);
		Insets leftPadding = new Insets(0, 0, 0, 0);
		Insets rightPadding = new Insets(0, -300, 0, 0);
		Insets noPadding = new Insets(0, 0, 0, 0);

		///// First row /////////////////////////////
		PanelConstraints.addComponent(fileInfoPanel, fileLabel, 0, 0, 1, 1, GridBagConstraints.LINE_START,
				GridBagConstraints.BOTH, leftPadding, 0, 0);
		PanelConstraints.addComponent(fileInfoPanel, fileComboBox, 1, 0, 1, 1, GridBagConstraints.LINE_END,
				GridBagConstraints.BOTH, rightPadding, 0, 0);
		PanelConstraints.addComponent(fileInfoPanel, sortPanel, 0, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, noPadding, 0, 0);
		PanelConstraints.addComponent(fileInfoPanel, getPercentagePanel(), 0, 2, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, 0, 0);
		// ////////// Buttons Panel ///////////////
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		Dimension btnSize = okButton.getPreferredSize();
		okButton.setPreferredSize(btnSize);
		buttonsPanel.add(okButton);

		// make percentage panel

		// Add sub panels to dialog

		setLayout(new GridBagLayout());// BorderLayout
		PanelConstraints.addComponent(this, fileInfoPanel, 1, 0, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, 0, 0);
		// PanelConstraints.addComponent(this, getPercentagePanel(), 0, 1, 1, 1,
		// GridBagConstraints.CENTER,
		// GridBagConstraints.BOTH, 0, 0);
		PanelConstraints.addComponent(this, buttonsPanel, 1, 1, 1, 1, GridBagConstraints.LAST_LINE_END,
				GridBagConstraints.BOTH, 0, 0);
		// add(fileInfoPanel, BorderLayout.CENTER);
		// add(getPercentagePanel(), BorderLayout.SOUTH);
		// add(buttonsPanel, BorderLayout.SOUTH);

	}

	private JPanel getPercentagePanel() {
		JPanel fileInfoPanel = new JPanel();

		fileInfoPanel.setLayout(new GridBagLayout());
		Insets rightPadding = new Insets(0, 0, 0, 15);
		Insets noPadding = new Insets(0, 0, 0, 0);

		///// First row /////////////////////////////
		PanelConstraints.addComponent(fileInfoPanel, percentLabel, 0, 0, 0, 0, GridBagConstraints.LINE_START,
				GridBagConstraints.NONE, rightPadding, 0, 0);
		PanelConstraints.addComponent(fileInfoPanel, percentField, 1, 0, 0, 0, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, noPadding, 0, 0);

		return fileInfoPanel;
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.okButton) {
			if (checkValidFile()) {

				System.out.println("you enetered " + getUserPercent(percentField.getText()));
				this.mainFrameService.setUserPercent(getUserPercent(percentField.getText()));
				String str = (String) this.fileComboBox.getSelectedItem();
				this.mainFrame.getDataProcess().openFile(dirLocation + str);
				this.mainFrameService.setMouseReady();
				setVisible(false);

				processCommands();

			} else {
				System.out.println("Problem");
				JOptionPane.showMessageDialog(errorFrame, "Eggs are not supposed to be green.", "Please choose a file",
						JOptionPane.ERROR_MESSAGE);
				setVisible(false);

			}
			// setVisible(false);
			// processCommands();
		}
	}

	private double getUserPercent(String str) {
		double retValue = 0.0;
		try {
			retValue = Double.parseDouble(str);
		} catch (Exception e) {
			retValue = 0.0;
		}
		return retValue;

	}

	private void processCommands() {
		this.mainFrameService.setSentTodb(false);
		this.mainFrameService.getCompleteSQLList().clear();
		this.mainFrame.getSqlPanel().setTableModel(this.mainFrameService.getCompleteSQLList());

		this.mainFrame.getDataProcess().processFile();
		this.mainFrameService.setSelectedSector(Integer.parseInt(this.sortPanel.getSelectedSector()));
		this.mainFrameService.setSelectedSuperlayer(Integer.parseInt(this.sortPanel.getSelectedSuperLayer()));
		this.mainFrame.getDataPanel().setTableModel(this.mainFrameService.getBySectorAndSuperLayer(
				this.mainFrameService.getSelectedSector(), this.mainFrameService.getSelectedSuperlayer()));

		this.mainFrame.getHistogramPanel().updateCanvas(Integer.parseInt(this.sortPanel.getSelectedSuperLayer()),
				Integer.parseInt(this.sortPanel.getSelectedSector()));
	}
}
