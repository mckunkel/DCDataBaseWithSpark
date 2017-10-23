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
package domain.workingset;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

import domain.PanelConstraints;
import domain.utils.NumberConstants;
import domain.utils.Status_change_type;
import domain.utils.StringConstants;

public class FaultPanel extends JPanel implements ActionListener {// implements
	private MainFrame mainFrame = null;
	private MainFrameService mainFrameService = null;

	final int space = NumberConstants.BORDER_SPACING;
	private Border spaceBorder = null;
	private Border titleBorder = null;
	private JComboBox<String> faultComboBox = null;
	private JButton applyButton = null;
	private List<JRadioButton> jButtons = null;
	private ButtonGroup buttons = null;
	private JCheckBox cb1 = null;
	private JCheckBox cb2 = null;

	public FaultPanel(MainFrame parentFrame) {
		this.mainFrame = parentFrame;
		initializeVariables();
		initialLayout();
	}

	private void initializeVariables() {
		this.mainFrameService = MainFrameServiceManager.getSession();

		this.spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		this.titleBorder = BorderFactory.createTitledBorder(StringConstants.FAULT_FORM_LABEL);
		this.faultComboBox = new JComboBox(StringConstants.PROBLEM_TYPES);

		this.applyButton = new JButton(StringConstants.FAULT_FORM_APPLY);
		this.jButtons = new ArrayList<>();
		this.buttons = new ButtonGroup();

		this.cb1 = new JCheckBox("broken");
		this.cb2 = new JCheckBox("fixed");
		this.applyButton.addActionListener(this);
		this.cb1.addActionListener(this);
		this.cb2.addActionListener(this);

	}

	private void initialLayout() {
		setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));
		setLayout(new GridLayout(1, 2));
		// add(new JLabel("Fault:"));
		// add(faultComboBox);
		add(makeButtonGroup());
		JPanel submitPanel = new JPanel();
		submitPanel.setLayout(new GridBagLayout());
		PanelConstraints.addComponent(submitPanel, checkBoxPanel(), 0, 0, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, 0, 0);
		PanelConstraints.addComponent(submitPanel, applyButton, 0, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, 0, 0);
		// submitPanel.add(checkBoxPanel());
		// submitPanel.add(applyButton);
		//
		// add(checkBoxPanel());
		// add(new JLabel(""));
		//
		// add(applyButton);

		add(submitPanel);
	}

	private JPanel makeButtonGroup() {
		JPanel jPanel1 = new JPanel();
		jPanel1.setLayout(
				new GridLayout(StringConstants.PROBLEM_TYPES.length / 2, StringConstants.PROBLEM_TYPES.length / 2));

		for (int i = 1; i < StringConstants.PROBLEM_TYPES.length; i++) {
			JRadioButton button = new JRadioButton(StringConstants.PROBLEM_TYPES[i]);
			button.addActionListener(this);
			jPanel1.add(button);
			buttons.add(button);
			jButtons.add(button);

		}
		return jPanel1;
	}

	private JPanel checkBoxPanel() {
		checkBoxGroup();
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new GridLayout(0, 2));
		checkBoxPanel.add(this.cb1);
		checkBoxPanel.add(this.cb2);

		return checkBoxPanel;

	}

	private ButtonGroup checkBoxGroup() {
		ButtonGroup checkBoxGroup = new ButtonGroup();
		checkBoxGroup.add(this.cb1);
		checkBoxGroup.add(this.cb2);
		this.cb1.setSelected(true);
		return checkBoxGroup;

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Status_change_type brokenOrfixed = null;
		if (cb1.isSelected()) {
			brokenOrfixed = Status_change_type.broken;
		}
		if (cb2.isSelected()) {
			brokenOrfixed = Status_change_type.fixed;
		}
		if (event.getSource() == this.applyButton) {
			String str = null;
			str = (String) this.faultComboBox.getSelectedItem();

			if (cb1.isSelected() && str.isEmpty()) {
				JFrame errorFrame = new JFrame("");
				JOptionPane.showMessageDialog(errorFrame, "In a box? With a Fox?",
						"Please choose a an Fault with a Broken component", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (event.getSource() == this.jButtons.get(0))
			this.mainFrameService.setFault(0);
		if (event.getSource() == this.jButtons.get(1))
			this.mainFrameService.setFault(1);
		if (event.getSource() == this.jButtons.get(2))
			this.mainFrameService.setFault(2);
		if (event.getSource() == this.jButtons.get(3))
			this.mainFrameService.setFault(3);
		if (event.getSource() == this.jButtons.get(4))
			this.mainFrameService.setFault(4);
		if (event.getSource() == this.jButtons.get(5))
			this.mainFrameService.setFault(5);
	}
}
