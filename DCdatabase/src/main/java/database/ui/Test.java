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

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Test extends JFrame {
	public static void main(String[] args) {
		Test window = new Test();
		window.setVisible(true);
	}

	public Test() {
		this.setSize(200, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final FileDialog fileDialog = new FileDialog(this, "FileDialog");
		fileDialog.setMultipleMode(true);

		JButton fileDialogButton = new JButton("File Dialog");
		fileDialogButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileDialog.setVisible(true);
				File files[] = fileDialog.getFiles();
				for (File file : files) {
					System.out.println("File: " + file.getName());
				}
			}
		});
		this.add(fileDialogButton);
	}
}
