package domain;

import javax.swing.SwingUtilities;

public class JButtonArray {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				new JButtonArray();
			}
		});
	}
}
