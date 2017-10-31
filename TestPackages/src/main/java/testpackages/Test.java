package testpackages;

import javax.swing.JOptionPane;

public class Test {
	public static void main(String[] args) {

		// default icon, custom title

		int reply = JOptionPane.showConfirmDialog(null, "Would you like green eggs and ham?", "An Inane Question",
				JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			JOptionPane.showMessageDialog(null, "HELLO");
		} else {
			JOptionPane.showMessageDialog(null, "GOODBYE");
			System.exit(0);
		}
		System.exit(0);
	}
}