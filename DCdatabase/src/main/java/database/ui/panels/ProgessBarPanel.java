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

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import database.service.MainFrameService;
import database.utils.MainFrameServiceManager;

public class ProgessBarPanel extends JPanel {

	private MainFrameService mainFrameService = null;

	JProgressBar pbar = null;

	private int MINIMUM;

	private int MAXIMUM;

	public ProgessBarPanel() {
		initializeVariables();
		constructLayout();
	}

	private void initializeVariables() {
		this.mainFrameService = MainFrameServiceManager.getSession();
		this.MINIMUM = 0;
		this.MAXIMUM = this.mainFrameService.getnEventsInFile();

		this.pbar = new JProgressBar();
		this.pbar.setMinimum(MINIMUM);
		this.pbar.setMaximum(MAXIMUM);
	}

	private void constructLayout() {
		add(pbar);
	}

	public void updateBar(int newValue) {
		pbar.setValue(newValue);
	}

	public void setRunning() {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					it.updateBar(percent);
				}
			});
			java.lang.Thread.sleep(100);
		} catch (InterruptedException e) {
			;
		}
	}

}
