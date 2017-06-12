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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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

import database.service.CompareRunFormService;
import database.service.CompareRunFormServiceImpl;
import database.utils.NumberConstants;
import database.utils.StringConstants;

public class FaultPanel extends JPanel implements ActionListener {// implements
																	// ActionListener

	final int space = NumberConstants.BORDER_SPACING;
	Border spaceBorder = null;
	Border titleBorder = null;
	JComboBox<String> faultComboBox = null;
	private CompareRunFormService compareRunFormService;
	JButton applyButton = null;
	JButton sendButton = null;

	private AddOtherForm addOtherForm = null;

	public FaultPanel() {
		initializeVariables();
		loadData();
		initialLayout();
	}

	private void initializeVariables() {
		this.spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		this.compareRunFormService = new CompareRunFormServiceImpl();
		this.titleBorder = BorderFactory.createTitledBorder(StringConstants.FAULT_FORM_LABEL);
		this.faultComboBox = new JComboBox<String>();
		this.applyButton = new JButton("Apply Fault");
		this.sendButton = new JButton("Send to DB");

		this.applyButton.addActionListener(this);
		this.sendButton.addActionListener(this);

	}

	public void loadData() {

		this.faultComboBox.removeAllItems();

		List<String> problems = this.compareRunFormService.getAllProblems();

		for (String str : problems) {
			this.faultComboBox.addItem(str);
		}
		this.faultComboBox.addItem("other");
	}

	private void initialLayout() {
		setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));
		setLayout(new GridLayout(0, 2));
		add(new JLabel("Fault:"));
		add(faultComboBox);
		add(applyButton);
		add(sendButton);

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String str = null;
		if (event.getSource() == this.sendButton) {
			System.out.println("Will send the query with selected faults and wires");
		} else if (event.getSource() == this.applyButton) {

			str = (String) this.faultComboBox.getSelectedItem();
			// synchronized (str) {

			if (str == "other") {
				System.out.println("you chose other");
				this.addOtherForm = new AddOtherForm(this);
				str = addOtherForm.getOtherFault();
				// str.notify();
				System.out.println("prepared query with " + str);

			} else
				System.out.println("prepared query with " + str);
		}

		// }
		// synchronized (str)
		//
		// {
		//
		// // wait for input from field
		// while (str.isEmpty())
		// try {
		// str.wait();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// // String nextInt = str.remove(0);
		// System.out.println(str);
		// // ....
		// }
	}

	class AddOtherForm extends JDialog implements ActionListener {
		private JButton cancelButton;
		private JButton saveButton;
		private JLabel otherLabel;
		private JTextField otherField;
		private String userInputFault;

		public AddOtherForm(FaultPanel faultPanel) {
			super();

			initializeOtherVariables();
			constructLayout();
			setWindow(faultPanel);
		}

		private void setWindow(FaultPanel faultPanel) {
			setSize(NumberConstants.SORT_FORM_WINDOW_SIZE_WIDTH, NumberConstants.SORT_FORM_WINDOW_SIZE_HEIGHT);
			setLocationRelativeTo(faultPanel);
			setVisible(true);

		}

		private void initializeOtherVariables() {
			this.cancelButton = new JButton(StringConstants.FORM_CANCEL);
			this.saveButton = new JButton(StringConstants.FORM_SAVE);
			this.otherLabel = new JLabel(StringConstants.OTHER_FORM_INPUT);
			this.otherField = new JTextField(NumberConstants.OTHER_FORM_WINDOW_FIELD_LENGTH);

			this.cancelButton.addActionListener(this);
			this.saveButton.addActionListener(this);
		}

		private void constructLayout() {

			JPanel otherInfoPanel = new JPanel();
			JPanel buttonsPanel = new JPanel();

			otherInfoPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));

			otherInfoPanel.setLayout(new GridBagLayout());

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
			otherInfoPanel.add(otherLabel, gc);

			gc.gridx++;
			gc.anchor = GridBagConstraints.WEST;
			gc.insets = noPadding;
			otherInfoPanel.add(otherField, gc);

			// ////////// Buttons Panel ///////////////

			buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonsPanel.add(saveButton);
			buttonsPanel.add(cancelButton);

			Dimension btnSize = cancelButton.getPreferredSize();
			saveButton.setPreferredSize(btnSize);

			// Add sub panels to dialog
			setLayout(new BorderLayout());
			add(otherInfoPanel, BorderLayout.CENTER);
			add(buttonsPanel, BorderLayout.SOUTH);

		}

		protected String getOtherFault() {
			return userInputFault;
		}

		private void setOtherFault(String userInputFault) {
			this.userInputFault = userInputFault;
		}

		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == this.cancelButton) {
				setVisible(false);
			} else if (event.getSource() == this.saveButton) {

				String name = this.otherField.getText();
				if (name.isEmpty()) {
					JFrame errorFrame = new JFrame("");
					JOptionPane.showMessageDialog(errorFrame, "In a box? Would you eat them, with a fox? ",
							"Please input the fault", JOptionPane.ERROR_MESSAGE);
					errorFrame.dispose();
				}
				setOtherFault(name);
				this.setVisible(false);
			}
		}

	}
}
