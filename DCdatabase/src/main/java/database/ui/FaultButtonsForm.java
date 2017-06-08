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

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FaultButtonsForm extends JPanel {
	private static final String INITIAL_TEXT = "Nothing Pressed";
	private static final String ADDED_TEXT = " was Pressed";
	private JLabel positionLabel;
	private JButton resetButton;
	private static int gridSize = 4;

	public FaultButtonsForm() {
		createAndDisplayGUI();
	}

	private void createAndDisplayGUI() {

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
		contentPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		JPanel labelPanel = new JPanel();
		positionLabel = new JLabel(INITIAL_TEXT, JLabel.CENTER);
		JPanel buttonLeftPanel = new JPanel();
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				positionLabel.setText(INITIAL_TEXT);
			}
		});
		labelPanel.add(positionLabel);
		buttonLeftPanel.add(resetButton);
		leftPanel.add(labelPanel);
		leftPanel.add(buttonLeftPanel);

		contentPane.add(leftPanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(gridSize, gridSize, 10, 10));
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				JButton button = new JButton("(" + i + ", " + j + ")");
				button.setActionCommand("(" + i + ", " + j + ")");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						JButton but = (JButton) ae.getSource();
						positionLabel.setText(but.getActionCommand() + ADDED_TEXT);
					}
				});
				buttonPanel.add(button);
			}
		}
		contentPane.add(buttonPanel);

	}
}
