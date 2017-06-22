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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class ProgressBarDemo extends JFrame {

	public final static int ONE_SECOND = 1000;

	JProgressBar progressBar;
	Timer timer;
	JButton startButton;
	LongTask task;

	public ProgressBarDemo() {
		super("ProgressBarDemo");
		task = new LongTask();

		// create the demo's UI
		startButton = new JButton("Start");
		startButton.setActionCommand("start");
		startButton.addActionListener(new ButtonListener());

		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(task.getLengthOfTask());
		progressBar.setValue(0);

		JPanel contentPane = new JPanel();
		contentPane.add(startButton);
		contentPane.add(progressBar);
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setContentPane(contentPane);

		// create a timer and a task
		timer = new Timer(ONE_SECOND, new TimerListener());
	}

	// the actionPerformed method in this class
	// is called each time the Timer "goes off"
	class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			progressBar.setValue(task.getCurrent());
			if (task.done()) {
				Toolkit.getDefaultToolkit().beep();
				timer.stop();
				startButton.setEnabled(true);
			}
		}
	}

	// the actionPerformed method in this class
	// when the user presses the start button
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			startButton.setEnabled(false);
			progressBar.setValue(progressBar.getMinimum());
			task.go();
			timer.start();
		}
	}

	public static void main(String[] args) {

		JFrame frame = new ProgressBarDemo();

		WindowListener l = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		frame.addWindowListener(l);

		frame.pack();
		frame.setVisible(true);
	}
}
