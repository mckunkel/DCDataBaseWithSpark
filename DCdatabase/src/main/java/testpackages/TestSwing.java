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
package testpackages;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

// Significant changes noted with the //!! comment
public class TestSwing extends JPanel implements ActionListener {
	JButton submit;
	JTextField t1;
	String msg = "No Msg";

	public TestSwing() {
		submit = new JButton("Submit");
		t1 = new JTextField(10);
		submit.addActionListener(this);
		setLayout(new FlowLayout());
		add(t1);
		add(submit);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			msg = t1.getText();

			// !! Display msg only **after** the user has pressed enter.
			System.out.println(msg);
		}

	}

	public void createAndShowUI() { // !! Don't have method return anything
		JFrame f = new JFrame("Sample frame");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // !! close GUI
		f.add(new TestSwing());
		f.pack();
		f.setLocationRelativeTo(null); // center GUI
		f.setVisible(true);
		// !! return msg; // get rid of
	}

	public static void main(String[] arg) {
		new TestSwing().createAndShowUI();
	}

}