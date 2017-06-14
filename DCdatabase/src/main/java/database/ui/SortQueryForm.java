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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import database.service.MainFrameService;
import database.utils.MainFrameServiceManager;
import database.utils.NumberConstants;
import database.utils.StringConstants;

public class SortQueryForm extends JDialog implements ActionListener {// implements
	// ActionListener
	private MainFrame mainFrame = null;
	private MainFrameService mainFrameService = null;

	private JButton cancelButton;
	private JButton sortButton;
	private JLabel sectorLabel;
	private JLabel superLayerLabel;
	private JTextField sectorField;
	private JTextField superLayerField;
	private String[] numberStrings = { "", "1", "2", "3", "4", "5", "6" };
	JComboBox<String> sectorList;
	JComboBox<String> superLayerList;
	private boolean isReady = false;

	public SortQueryForm(MainFrame parentFrame) {
		super(parentFrame, StringConstants.SORT_FORM_TITLE, false);

		this.mainFrame = parentFrame;
		this.mainFrameService = MainFrameServiceManager.getSession();

		initializeVariables();
		constructLayout();
		setWindow(parentFrame);
		// parentFrame.dataProcess.getDataset();
	}

	private void setWindow(JFrame parentFrame) {
		setSize(NumberConstants.SORT_FORM_WINDOW_SIZE_WIDTH, NumberConstants.SORT_FORM_WINDOW_SIZE_HEIGHT);
		setLocationRelativeTo(parentFrame);
	}

	private void initializeVariables() {
		this.cancelButton = new JButton(StringConstants.SORT_FORM_CANCEL);
		this.sortButton = new JButton(StringConstants.SORT_FORM_SAVE);
		this.sectorLabel = new JLabel(StringConstants.SORT_FORM_SECTOR);
		this.superLayerLabel = new JLabel(StringConstants.SORT_FORM_SUPERLAYER);
		this.sectorField = new JTextField(NumberConstants.SORT_FORM_WINDOW_FIELD_LENGTH);
		this.superLayerField = new JTextField(NumberConstants.SORT_FORM_WINDOW_FIELD_LENGTH);

		this.cancelButton.addActionListener(this);
		this.sortButton.addActionListener(this);

		this.sectorList = new JComboBox(numberStrings);
		this.superLayerList = new JComboBox(numberStrings);
		this.sectorList.setSelectedIndex(0);
		this.superLayerList.setSelectedIndex(0);

	}

	private void constructLayout() {

		JPanel sortInfoPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();

		int space = 15;
		Border spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		Border titleBorder = BorderFactory.createTitledBorder(StringConstants.SORT_FORM_SUBTITLE);

		sortInfoPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));

		sortInfoPanel.setLayout(new GridBagLayout());

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
		sortInfoPanel.add(sectorLabel, gc);

		gc.gridx++;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		sortInfoPanel.add(sectorList, gc);

		// ////// Next row ////////////////////////////

		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = rightPadding;
		sortInfoPanel.add(superLayerLabel, gc);

		gc.gridx++;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		sortInfoPanel.add(superLayerList, gc);

		// ////////// Buttons Panel ///////////////

		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(sortButton);
		buttonsPanel.add(cancelButton);

		Dimension btnSize = cancelButton.getPreferredSize();
		sortButton.setPreferredSize(btnSize);

		// Add sub panels to dialog
		setLayout(new BorderLayout());
		add(sortInfoPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);

	}

	public void resetList() {
		this.sectorList.setSelectedIndex(0);
		this.superLayerList.setSelectedIndex(0);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.cancelButton) {
			setVisible(false);
		} else if (event.getSource() == this.sortButton) {
			int sectorString = 2;
			int superLayerString = 2;
			boolean wasReady = false;
			if (isReady) {
				wasReady = true;
				sectorString = Integer.parseInt(this.sectorList.getSelectedItem().toString());
				superLayerString = Integer.parseInt(this.superLayerList.getSelectedItem().toString());
			} else {
				JFrame errorFrame = new JFrame("");
				System.out.println("Problem");
				JOptionPane.showMessageDialog(errorFrame, "Would you like some green eggs to go with that ham?",
						"Please choose a file", JOptionPane.ERROR_MESSAGE);
			}

			this.setVisible(false);
			updateQuery(wasReady, sectorString, superLayerString);

		}
	}

	public void setReady() {
		isReady = true;
	}

	public boolean getIsReady() {
		return isReady;
	}

	protected void updateQuery(boolean wasReady, int sectorSelection, int superLayerSelection) {
		if (wasReady) {
			this.mainFrame.getDataPanel().setTableModel(
					this.mainFrameService.getBySectorAndSuperLayer(sectorSelection, superLayerSelection));
			this.mainFrame.getHistogramPanel().updateCanvas(superLayerSelection, sectorSelection);
		}

	}

}
