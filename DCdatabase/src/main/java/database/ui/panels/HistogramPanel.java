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
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;

import database.service.MainFrameService;
import database.utils.Coordinate;
import database.utils.MainFrameServiceManager;
import database.utils.NumberConstants;
import database.utils.StringConstants;

public class HistogramPanel extends JPanel implements UpdatePanel {
	private MainFrameService mainFrameService = null;

	private EmbeddedCanvas canvas = null;
	private Map<Coordinate, H2F> occupanciesByCoordinate = null;

	int updateTime = NumberConstants.CANVAS_UPDATE;

	final int space = NumberConstants.BORDER_SPACING;
	Border spaceBorder = null;
	Border titleBorder = null;

	public HistogramPanel() {
		initializeVariables();
		constructLayout();
	}

	private void constructLayout() {
		setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));
		setLayout(new BorderLayout());

		JPanel aTestPanel = new JPanel();
		int thisSpace = 0;
		aTestPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(thisSpace, thisSpace, thisSpace, thisSpace),
				BorderFactory.createTitledBorder("")));
		aTestPanel.setLayout(new BorderLayout());
		canvas.setAxisTitleSize(10);
		canvas.setAxisFontSize(50);
		canvas.setAxisLabelSize(10);

		aTestPanel.add(canvas, BorderLayout.CENTER);

		add(aTestPanel, BorderLayout.CENTER);

	}

	private void initializeVariables() {
		this.mainFrameService = MainFrameServiceManager.getSession();

		this.canvas = new EmbeddedCanvas();
		this.canvas.initTimer(updateTime);

		this.spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		this.titleBorder = BorderFactory.createTitledBorder(StringConstants.HISTOGRAM_FORM_LABEL);

		this.canvas.draw(this.mainFrameService.getHistogramByMap(NumberConstants.DEFAULT_HIST_SUPERLAYER,
				NumberConstants.DEFAULT_HIST_SECTOR));
	}

	public void updateCanvas(int superLayer, int sector) {
		this.canvas.draw(this.mainFrameService.getHistogramByMap(superLayer, sector));
		this.canvas.update();
	}

	@Override
	public void updatePanel(int superLayer, int sector) {
		this.updateCanvas(superLayer, sector);
	}
}
