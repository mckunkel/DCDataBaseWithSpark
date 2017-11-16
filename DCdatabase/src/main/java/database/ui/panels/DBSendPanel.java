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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

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
	final int space = NumberConstants.BORDER_SPACING;
	Border spaceBorder = null;
	Border titleBorder = null;
	// JButton removeButton = null;
	JButton sendButton = null;

	public DBSendPanel(MainFrame parentFrame) {
		initializeVariables();
		initialLayout();
	}

	private void initializeVariables() {
		this.insertMYSqlQuery = InsertMYSqlServiceManager.getSession();
		this.mainFrameService = MainFrameServiceManager.getSession();

		this.spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		this.titleBorder = BorderFactory.createTitledBorder(StringConstants.DBSEND_FORM_LABEL);

		// this.removeButton = new JButton(StringConstants.DBSEND_FORM_REMOVE);
		this.sendButton = new JButton(StringConstants.DBSEND_FORM_SEND);

		// this.removeButton.addActionListener(this);
		this.sendButton.addActionListener(this);

	}

	private void initialLayout() {
		setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));
		// setLayout(new GridLayout(0, 2));
		// setLayout(new FlowLayout());
		// add(new JLabel(""));
		// add(new JLabel(""));
		//
		// add(new JLabel(""));
		// // add(removeButton);
		// add(sendButton);

		setLayout(new GridBagLayout());
		PanelConstraints.addComponent(this, new JLabel(""), 0, 0, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, 0, 0);
		PanelConstraints.addComponent(this, sendButton, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				200, 0);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.sendButton) {
			System.out.println("Will send the query with selected faults and wires from the sql panel");
			this.insertMYSqlQuery.prepareMYSQLQuery();
			this.mainFrameService.getCompleteSQLList().clear();
			this.mainFrameService.getSQLPanel().setTableModel(this.mainFrameService.getCompleteSQLList());
			this.mainFrameService.setSentTodb(true);
			SparkManager.restart();

		}
	}
}
