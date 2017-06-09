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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TestAnotherSwing extends JPanel {
	public static final String MESSAGE = "Message";
	private JButton submit;
	private JTextField mainTextField;
	private String message = "No Msg";

	private PropertyChangeSupport propSupport = new PropertyChangeSupport(this);

	public TestAnotherSwing() {
		submit = new JButton("Submit");
		mainTextField = new JTextField(10);
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitActionPerformed(e);
			}
		});
		setLayout(new FlowLayout());
		add(mainTextField);
		add(submit);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propSupport.removePropertyChangeListener(listener);
	}

	public void setMessage(String newValue) {
		String oldValue = message;
		this.message = newValue;
		PropertyChangeEvent event = new PropertyChangeEvent(this, MESSAGE, oldValue, newValue);
		propSupport.firePropertyChange(event);
	}

	private void submitActionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			setMessage(mainTextField.getText());
		}
	}

	public static void createAndShowUI() {
		TestAnotherSwing testSwing = new TestAnotherSwing();
		testSwing.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(TestAnotherSwing.MESSAGE)) {
					System.out.println("message = " + evt.getNewValue());
				}
			}
		});

		JFrame f = new JFrame("Sample frame");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(testSwing);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public static void main(String[] arg) {
		createAndShowUI();
	}

}