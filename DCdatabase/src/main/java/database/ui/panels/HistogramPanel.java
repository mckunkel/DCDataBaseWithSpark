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

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.jlab.groot.graphics.EmbeddedCanvas;

import database.process.DataProcess;
import database.utils.NumberConstants;
import database.utils.StringConstants;

public class HistogramPanel extends JPanel implements UpdatePanel {

	private DataProcess dataProcess = null;

	private EmbeddedCanvas canvas = null;
	int updateTime = NumberConstants.CANVAS_UPDATE;

	final int space = NumberConstants.BORDER_SPACING;
	Border spaceBorder = null;
	Border titleBorder = null;

	public HistogramPanel(DataProcess dataProcess) {
		this.dataProcess = dataProcess;
		initializeVariables();
		constructLayout();
	}

	private void constructLayout() {
		JPanel aTestPanel = new JPanel();
		aTestPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));
		aTestPanel.add(canvas, BorderLayout.CENTER);

		// setBorder(BorderFactory.createCompoundBorder(spaceBorder,
		// titleBorder));
		// setLayout(new BorderLayout());
		// add(new JScrollPane(canvas), BorderLayout.CENTER);
		// add(canvas, BorderLayout.CENTER);
		add(aTestPanel);

	}

	private void initializeVariables() {

		this.canvas = new EmbeddedCanvas();
		this.canvas.initTimer(updateTime);

		this.spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		this.titleBorder = BorderFactory.createTitledBorder(StringConstants.HISTOGRAM_FORM_LABEL);

		this.canvas.draw(this.dataProcess.getHistogramByMap(NumberConstants.DEFAULT_HIST_SUPERLAYER,
				NumberConstants.DEFAULT_HIST_SECTOR));
	}

	public void updateCanvas(int superLayer, int sector) {
		this.canvas.draw(this.dataProcess.getHistogramByMap(superLayer, sector));
		this.canvas.update();
	}

	@Override
	public void updatePanel(int superLayer, int sector) {
		this.updateCanvas(superLayer, sector);
	}
}
