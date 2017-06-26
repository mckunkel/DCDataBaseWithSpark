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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import database.objects.StatusChangeDB;
import database.service.CompareRunFormService;
import database.service.CompareRunFormServiceImpl;
import database.service.MainFrameService;
import database.ui.MainFrame;
import database.utils.MainFrameServiceManager;
import database.utils.NumberConstants;
import database.utils.StringConstants;

public class FaultPanel extends JPanel implements ActionListener {// implements
	private MainFrameService mainFrameService = null;
	private MainFrame mainFrame = null;

	final int space = NumberConstants.BORDER_SPACING;
	private Border spaceBorder = null;
	private Border titleBorder = null;
	private JComboBox<String> faultComboBox = null;
	private CompareRunFormService compareRunFormService;
	private JButton applyButton = null;

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
		this.compareRunFormService = new CompareRunFormServiceImpl();
		this.titleBorder = BorderFactory.createTitledBorder(StringConstants.FAULT_FORM_LABEL);
		this.faultComboBox = new JComboBox(StringConstants.PROBLEM_TYPES);

		this.applyButton = new JButton(StringConstants.FAULT_FORM_APPLY);

		this.cb1 = new JCheckBox("broken");
		this.cb2 = new JCheckBox("fixed");
		this.applyButton.addActionListener(this);
		this.cb1.addActionListener(this);
		this.cb2.addActionListener(this);

	}

	private void initialLayout() {
		setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));
		setLayout(new GridLayout(0, 2));
		add(new JLabel("Fault:"));
		add(faultComboBox);
		add(checkBoxPanel());
		add(applyButton);

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

		String brokenOrfixed = null;
		if (cb1.isSelected()) {
			brokenOrfixed = "broken";
		}
		if (cb2.isSelected()) {
			brokenOrfixed = "fixed";
		}

		if (event.getSource() == this.applyButton) {
			String str = null;
			str = (String) this.faultComboBox.getSelectedItem();

			if (cb1.isSelected() && str.isEmpty()) {
				JFrame errorFrame = new JFrame("");
				JOptionPane.showMessageDialog(errorFrame, "In a box? With a Fox?",
						"Please choose a an Fault with a Broken component", JOptionPane.ERROR_MESSAGE);
			} else {

				TreeSet<StatusChangeDB> queryList = this.mainFrameService.getMYSQLQuery();

				for (StatusChangeDB statusChangeDB : queryList) {
					statusChangeDB.setProblem_type(str);
					statusChangeDB.setStatus_change_type(brokenOrfixed);
					statusChangeDB.setRunno(this.mainFrameService.getRunNumber());
				}

				this.mainFrameService.addToCompleteSQLList(queryList);
				this.mainFrame.getDataPanel().removeItems(queryList);

				this.mainFrameService.clearTempSQLList();
				this.mainFrame.getSqlPanel().setTableModel(this.mainFrameService.getCompleteSQLList());
				this.faultComboBox.setSelectedIndex(0);
			}
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
}
