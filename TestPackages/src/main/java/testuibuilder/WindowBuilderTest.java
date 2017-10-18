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
package testuibuilder;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class WindowBuilderTest extends JFrame {

	private JPanel contentPane;
	private JTextField txtAddNameHere;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowBuilderTest frame = new WindowBuilderTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WindowBuilderTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnNewButton = new JButton("Submit");
		btnNewButton.setBounds(186, 224, 117, 29);
		contentPane.add(btnNewButton);

		txtAddNameHere = new JTextField();
		txtAddNameHere.setText("Add Name Here");
		txtAddNameHere.setBounds(169, 198, 134, 28);
		contentPane.add(txtAddNameHere);
		txtAddNameHere.setColumns(10);

		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(38, 200, 52, 27);
		contentPane.add(comboBox);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(38, 6, 304, 137);
		contentPane.add(splitPane);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(438, 141, -87, -129);
		contentPane.add(layeredPane);
	}
}
