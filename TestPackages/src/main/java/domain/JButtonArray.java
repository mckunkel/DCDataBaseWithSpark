package domain;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JButtonArray extends JFrame {
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int APP_WINDOW_SIZE_WIDTH = (int) screenSize.getWidth() - (int) screenSize.getWidth() / 6;
	public static final int APP_WINDOW_SIZE_HEIGHT = (int) screenSize.getHeight() - (int) screenSize.getHeight() / 6;
	public static final int NUM_OF_COLUMNS = 4;

	private ButtonPanel buttonPanel;
	private HistogramPanel histogramPanel;

	public JButtonArray() {
		super("TestJButtonArray");
		initializeVariables();
		constructLayout();

		constructAppWindow();

	}

	private void initializeVariables() {
		this.buttonPanel = new ButtonPanel();
		this.histogramPanel = new HistogramPanel();
	}

	private void constructAppWindow() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setSize(APP_WINDOW_SIZE_WIDTH, APP_WINDOW_SIZE_HEIGHT);
		setVisible(true);
		// setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	private void constructLayout() {
		JPanel layoutPanel = new JPanel();

		layoutPanel.setLayout(new GridBagLayout());

		// PanelConstraints.addComponent(layoutPanel, histogramPanel, 2, 0, 1,
		// 1, 0.25, 1,
		// GridBagConstraints.FIRST_LINE_END, GridBagConstraints.BOTH, 0, 0);
		PanelConstraints.addComponent(layoutPanel, buttonPanel, 2, 0, 1, 1, 0.25, 1, GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.BOTH, 0, 0);
		setLayout(new BorderLayout());
		add(layoutPanel, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				new JButtonArray();
			}
		});
	}

	static class PanelConstraints extends GridBagConstraints {
		public static final Insets insets = new Insets(0, 0, 0, 0);

		public static void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth,
				int gridheight, int anchor, int fill, int ipadx, int ipady) {
			GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0, 1.0, anchor, fill,
					insets, ipadx, ipady);
			container.add(component, gbc);
		}

		public static void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth,
				int gridheight, double weightx, double weighty, int anchor, int fill, int ipadx, int ipady) {
			GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty,
					anchor, fill, insets, ipadx, ipady);
			container.add(component, gbc);
		}

		public static void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth,
				int gridheight, int anchor, int fill, Insets inset, int ipadx, int ipady) {
			GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0, 1.0, anchor, fill,
					inset, ipadx, ipady);
			container.add(component, gbc);
		}

		public static void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth,
				int gridheight, double weightx, double weighty, int anchor, int fill, Insets inset, int ipadx,
				int ipady) {
			GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty,
					anchor, fill, inset, ipadx, ipady);
			container.add(component, gbc);
		}
	}
}
