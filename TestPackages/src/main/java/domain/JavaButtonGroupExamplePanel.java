package domain;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;

public class JavaButtonGroupExamplePanel extends JPanel {
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel1 = new JPanel();
	JButton button1 = new JButton();
	JButton button2 = new JButton();
	JButton button3 = new JButton();
	ButtonGroup buttons = new ButtonGroup();

	List<JButton> jButtons = new ArrayList<>();

	public JavaButtonGroupExamplePanel() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		this.setLayout(borderLayout1);
		GridLayout gl = new GridLayout(10, 10);
		jPanel1.setLayout(gl);

		for (int i = 0; i < 100; i++) {
			final int placer = i + 1;
			JButton freshButton = new JButton();
			freshButton.addActionListener(e -> {
				System.out.println("Button " + placer + " clicked");
			});
			freshButton.setText("Button " + (i + 1));
			freshButton.setOpaque(true);
			freshButton.setContentAreaFilled(false);
			freshButton.setBorderPainted(false);
			freshButton.setBounds(new Rectangle(132, 75 + (i * 25), 91, 23));
			jButtons.add(freshButton);
			jPanel1.add(freshButton, null);

		}
		// this is where the radio buttons are added to the button group
		for (JButton jButton : jButtons) {
			buttons.add(jButton);
		}
		jPanel1.add(canvas(), null);

		// button1.setText("Button 1");
		// button1.setBounds(new Rectangle(132, 75, 91, 23));
		// button2.setText("Button 2");
		// button2.setBounds(new Rectangle(132, 100, 91, 23));
		// button3.setText("Button 3");
		// button3.setBounds(new Rectangle(132, 124, 91, 23));
		this.add(jPanel1, BorderLayout.CENTER);
		// jPanel1.add(button1, null);
		// jPanel1.add(button2, null);
		// jPanel1.add(button3, null);

		// buttons.add(button1);
		// buttons.add(button2);
		// buttons.add(button3);
	}

	private H2F makeH2F() {
		H2F aH2f = new H2F("test", 10, 0, 10, 10, 0, 10);
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				aH2f.setBinContent(x, y, x * y + 1);
			}
		}
		return aH2f;
	}

	private EmbeddedCanvas canvas() {
		EmbeddedCanvas canvas = new EmbeddedCanvas();
		canvas.setAxisTitleSize(10);
		canvas.setAxisFontSize(50);
		canvas.setAxisLabelSize(10);
		canvas.draw(makeH2F());

		return canvas;

	}

	public static void main(String[] args) {
		JFrame aFrame = new JFrame("Title");
		aFrame.add(new JavaButtonGroupExamplePanel());
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aFrame.setLocationRelativeTo(null);
		aFrame.pack();
		aFrame.setSize(800, 800);
		aFrame.setVisible(true);
	}
}
