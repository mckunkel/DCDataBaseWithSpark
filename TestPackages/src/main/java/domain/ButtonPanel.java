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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ButtonPanel extends JPanel implements ActionListener {
	private static final int space = 15;
	Border spaceBorder = null;
	Border titleBorder = null;
	JButton aButton = null;

	// List<JButton> buttons = new
	public ButtonPanel() {
		initializeVariables();
		constructLayout();

	}

	private void initializeVariables() {
		this.spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		this.titleBorder = BorderFactory.createTitledBorder("Test");
		this.aButton = new JButton("testbutton");
		aButton.setOpaque(true);
		aButton.setContentAreaFilled(false);
		aButton.setBorderPainted(false);
		this.aButton.addActionListener(this);

	}

	private void constructLayout() {
		setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));
		setLayout(new GridLayout(2, 2));
		add(aButton);
		add(aButton);
		add(aButton);
		add(aButton);

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.aButton) {
			System.out.println("I'm Clicked");

		}

	}
}
