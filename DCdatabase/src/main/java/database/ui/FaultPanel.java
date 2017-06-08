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

import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import database.service.CompareRunFormService;
import database.service.CompareRunFormServiceImpl;
import database.utils.StringConstants;

public class FaultPanel extends JPanel {

	final int space = 15;
	Border spaceBorder = null;
	Border titleBorder = null;
	JComboBox<String> faultComboBox = null;
	private CompareRunFormService compareRunFormService;

	public FaultPanel() {
		initializeVariables();
		loadData();
		initialLayout();
	}

	private void initializeVariables() {
		this.spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		this.titleBorder = BorderFactory.createTitledBorder(StringConstants.FAULT_FORM_LABEL);
		this.compareRunFormService = new CompareRunFormServiceImpl();

		this.faultComboBox = new JComboBox<String>();
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
	}
}