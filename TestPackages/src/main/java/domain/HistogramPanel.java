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
package domain;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;

public class HistogramPanel extends JPanel {

	private EmbeddedCanvas canvas = null;

	final int space = 15;
	Border spaceBorder = null;
	Border titleBorder = null;

	public HistogramPanel() {
		initializeVariables();
		constructLayout();
	}

	private void initializeVariables() {
		this.canvas = new EmbeddedCanvas();
		this.spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
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

		H2F aH2f = new H2F("test", 10, 0, 10, 10, 0, 10);
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				aH2f.setBinContent(x, y, x * y + 1);
			}
		}
		canvas.draw(aH2f);
		aTestPanel.add(canvas, BorderLayout.CENTER);

		add(aTestPanel, BorderLayout.CENTER);

	}

}
