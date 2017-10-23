package domain.workingset;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import domain.PanelConstraints;
import domain.utils.NumberConstants;

public class MainFrame extends JFrame {

	private MouseOverrideCanvas mouseOverrideCanvas = null;
	private FaultPanel faultPanel = null;
	private MainFrameService mainFrameService = null;

	public MainFrame() {
		super("Testing");

		initializeVariables();
		constructLayout();
		constructAppWindow();

	}

	private void initializeVariables() {
		this.mainFrameService = MainFrameServiceManager.getSession();

		this.mouseOverrideCanvas = new MouseOverrideCanvas();
		this.faultPanel = new FaultPanel(this);

	}

	private void constructLayout() {
		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new GridBagLayout());

		PanelConstraints.addComponent(layoutPanel, histogramControlsPanel(), 2, 0, 1, 1, 0.25, 1,
				GridBagConstraints.FIRST_LINE_END, GridBagConstraints.BOTH, 0, 0);

		setLayout(new BorderLayout());
		add(layoutPanel, BorderLayout.CENTER);
	}

	private JPanel histogramControlsPanel() {
		JPanel histgramControlsPanel = new JPanel();
		histgramControlsPanel.setLayout(new GridBagLayout());
		PanelConstraints.addComponent(histgramControlsPanel, mouseOverrideCanvas.getCanvas(), 0, 0, 1, 1,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, 0, 435);

		// add controls to histogram panel
		PanelConstraints.addComponent(histgramControlsPanel, faultPanel, 0, 1, 1, 1, GridBagConstraints.PAGE_END,
				GridBagConstraints.RELATIVE, 0, 0);
		return histgramControlsPanel;

	}

	private void constructAppWindow() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setSize(NumberConstants.APP_WINDOW_SIZE_WIDTH, NumberConstants.APP_WINDOW_SIZE_HEIGHT);
		setVisible(true);
		// setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public void aTest() {

	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Logger.getLogger("org.apache.spark.SparkContext").setLevel(Level.WARN);
			Logger.getLogger("org").setLevel(Level.OFF);
			Logger.getLogger("akka").setLevel(Level.OFF);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				new MainFrame();
			}
		});
	}
}
