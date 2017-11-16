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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import database.service.MainFrameService;
import database.ui.MainFrame;
import database.utils.MainFrameServiceManager;
import database.utils.PanelConstraints;
import database.utils.StringConstants;

public class BottomSortPanel extends JPanel implements ActionListener {
	private MainFrameService mainFrameService = null;
	private JButton sortButton;

	private JLabel sectorLabel;
	private JLabel superLayerLabel;

	private String[] numberStrings = { "1", "2", "3", "4", "5", "6" };
	private JComboBox<String> sectorList;
	private JComboBox<String> superLayerList;

	public BottomSortPanel(MainFrame parentFrame) {
		this.mainFrameService = MainFrameServiceManager.getSession();

		initializeVariables();
		initialLayout();

	}

	private void initializeVariables() {
		this.sortButton = new JButton(StringConstants.SORT_FORM_SAVE);
		this.sortButton.addActionListener(this);

		this.sectorLabel = new JLabel(StringConstants.SORT_FORM_SECTOR);
		this.superLayerLabel = new JLabel(StringConstants.SORT_FORM_SUPERLAYER);
		this.sectorList = new JComboBox(numberStrings);
		this.superLayerList = new JComboBox(numberStrings);
		this.sectorList.setSelectedIndex(0);
		this.superLayerList.setSelectedIndex(0);

	}

	private void initialLayout() {

		setLayout(new GridBagLayout());
		// // ///// First row ////////////////////////////

		PanelConstraints.addComponent(this, sectorLabel, 0, 0, 1, 0, GridBagConstraints.LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		PanelConstraints.addComponent(this, sectorList, 1, 0, 1, 0, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);

		// ////// Next row ////////////////////////////
		PanelConstraints.addComponent(this, superLayerLabel, 0, 1, 1, 1, GridBagConstraints.LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		PanelConstraints.addComponent(this, superLayerList, 1, 1, 1, 1, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		PanelConstraints.addComponent(this, sortButton, 1, 2, 1, 1, GridBagConstraints.LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);

	}

	public void resetList() {
		this.sectorList.setSelectedIndex(0);
		this.superLayerList.setSelectedIndex(0);
	}

	public JComboBox<String> getSectorList() {
		return sectorList;
	}

	public JComboBox<String> getSuperLayerList() {
		return superLayerList;
	}

	public String getSelectedSector() {
		return this.sectorList.getSelectedItem().toString();
	}

	public String getSelectedSuperLayer() {
		return this.superLayerList.getSelectedItem().toString();
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.sortButton) {
			if (getIsReady()) {

				if (this.getSelectedSector().isEmpty() || this.getSelectedSector().equalsIgnoreCase("")) {
					JFrame errorFrame = new JFrame("");
					JOptionPane.showMessageDialog(errorFrame, "Please enter a sector to sort",
							"Missing Sector Selection", JOptionPane.ERROR_MESSAGE);
				} else if (this.getSelectedSuperLayer().isEmpty()
						|| this.getSelectedSuperLayer().equalsIgnoreCase("")) {
					JFrame errorFrame = new JFrame("");
					JOptionPane.showMessageDialog(errorFrame, "Please enter a superlayer to sort",
							"Missing Superlayer Selection", JOptionPane.ERROR_MESSAGE);

				} else {
					int sectorString = Integer.parseInt(this.getSectorList().getSelectedItem().toString());
					int superLayerString = Integer.parseInt(this.getSuperLayerList().getSelectedItem().toString());
					this.mainFrameService.setSelectedSector(sectorString);
					this.mainFrameService.setSelectedSuperlayer(superLayerString);
					updateQuery(sectorString, superLayerString);

				}
			} else {
				JFrame errorFrame = new JFrame("");
				JOptionPane.showMessageDialog(errorFrame, "Would you like some green eggs to go with that ham?",
						"Please choose a file", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public boolean getIsReady() {
		return this.mainFrameService.getMouseReady();
	}

	protected void updateQuery(int sectorSelection, int superLayerSelection) {

		this.mainFrameService.getDataPanel().compareWithSQLPanel(
				this.mainFrameService.getBySectorAndSuperLayer(sectorSelection, superLayerSelection));
		this.mainFrameService.getHistogramPanel().updateCanvas(superLayerSelection, sectorSelection);

	}

}
