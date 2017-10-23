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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
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
	private JLabel nameLabel;

	private JTextField textField = null;
	private List<JRadioButton> jRadioButton = null;
	private List<JButton> jButtons = null;

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

		this.jRadioButton = new ArrayList<>();
		this.jButtons = new ArrayList<>();

		this.buttons = new ButtonGroup();

		this.cb1 = new JCheckBox("broken");
		this.cb2 = new JCheckBox("fixed");
		this.nameLabel = new JLabel(StringConstants.FORM_NAME);

		this.textField = new JTextField(NumberConstants.WINDOW_FIELD_LENGTH);

		this.cb1.addActionListener(this);
		this.cb2.addActionListener(this);

	}

	private void initialLayout() {
		setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));
		setLayout(new BorderLayout());

		add(makeSubmitPanel(), BorderLayout.PAGE_START);

		add(makeSpacerPanel(), BorderLayout.CENTER);

		add(makeButtonGroup(), BorderLayout.PAGE_END);

	}

	private JPanel makeButtonGroup() {
		JPanel jPanel1 = new JPanel();
		jPanel1.setLayout(
				new GridLayout(StringConstants.PROBLEM_TYPES.length / 2, StringConstants.PROBLEM_TYPES.length / 2));
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout());

		for (int i = 1; i < StringConstants.PROBLEM_TYPES.length; i++) {
			JRadioButton button = new JRadioButton(StringConstants.PROBLEM_TYPES[i]);

			JButton aButton = new JButton("?");
			aButton.setBounds(10, 10, 30, 25);
			aButton.setBorder(new RoundedBorder(3)); // 10 is the radius
			aButton.setForeground(Color.BLUE);
			aButton.addActionListener(this);

			button.addActionListener(this);
			jPanel.add(button);
			jPanel.add(aButton);

			jPanel1.add(button);
			// jPanel1.add(jPanel);
			buttons.add(button);
			jRadioButton.add(button);
			jButtons.add(aButton);

		}
		return jPanel1;
	}

	private JPanel makeSpacerPanel() {
		JPanel aJPanel = new JPanel();
		aJPanel.setLayout(new BorderLayout());
		aJPanel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.LINE_START);
		return aJPanel;
	}

	private JPanel makeSubmitPanel() {
		JPanel submitPanel = new JPanel();
		submitPanel.setLayout(new GridLayout(2, 1));
		JPanel namePanel = new JPanel();

		namePanel.setLayout(new GridBagLayout());

		PanelConstraints.addComponent(namePanel, nameLabel, 0, 0, 1, 1, GridBagConstraints.LINE_START,
				GridBagConstraints.BOTH, 0, 0);
		PanelConstraints.addComponent(namePanel, textField, 1, 0, 1, 1, GridBagConstraints.LINE_START,
				GridBagConstraints.BOTH, 0, 0);

		submitPanel.add(checkBoxPanel());
		submitPanel.add(namePanel);
		return submitPanel;

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
		if (event.getSource() == this.jRadioButton.get(0))
			this.mainFrameService.setFault(0);
		if (event.getSource() == this.jRadioButton.get(1))
			this.mainFrameService.setFault(1);
		if (event.getSource() == this.jRadioButton.get(2))
			this.mainFrameService.setFault(2);
		if (event.getSource() == this.jRadioButton.get(3))
			this.mainFrameService.setFault(3);
		if (event.getSource() == this.jRadioButton.get(4))
			this.mainFrameService.setFault(4);
		if (event.getSource() == this.jRadioButton.get(5))
			this.mainFrameService.setFault(5);

		if (event.getSource() == this.jButtons.get(0))
			System.out.println("Will print meaages for 0");
		if (event.getSource() == this.jButtons.get(1))
			System.out.println("Will print meaages for 1");
		if (event.getSource() == this.jButtons.get(2))
			System.out.println("Will print meaages for 2");
		if (event.getSource() == this.jButtons.get(3))
			System.out.println("Will print meaages for 3");
		if (event.getSource() == this.jButtons.get(4))
			System.out.println("Will print meaages for 4");
		if (event.getSource() == this.jButtons.get(5))
			System.out.println("Will print meaages for 5");

		// if (event.getSource() == this.applyButton) {
		// String str = null;
		// str = (String) this.faultComboBox.getSelectedItem();
		//
		// if (cb1.isSelected() && str.isEmpty()) {
		// JFrame errorFrame = new JFrame("");
		// JOptionPane.showMessageDialog(errorFrame, "In a box? With a Fox?",
		// "Please choose a an Fault with a Broken component",
		// JOptionPane.ERROR_MESSAGE);
		// } else {
		//
		// TreeSet<StatusChangeDB> queryList =
		// this.mainFrameService.getMYSQLQuery();
		//
		// for (StatusChangeDB statusChangeDB : queryList) {
		// statusChangeDB.setProblem_type(str);
		// statusChangeDB.setStatus_change_type(brokenOrfixed.toString());
		// statusChangeDB.setRunno(this.mainFrameService.getRunNumber());
		// }
		//
		// this.mainFrameService.addToCompleteSQLList(queryList);
		// this.mainFrame.getDataPanel().removeItems(queryList);
		//
		// this.mainFrameService.clearTempSQLList();
		// this.mainFrame.getSqlPanel().setTableModel(this.mainFrameService.getCompleteSQLList());
		// this.faultComboBox.setSelectedIndex(0);
		// }
		// }
	}

	private static class RoundedBorder implements Border {

		private int radius;

		RoundedBorder(int radius) {
			this.radius = radius;
		}

		public boolean isBorderOpaque() {
			return true;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
		}
	}
}
