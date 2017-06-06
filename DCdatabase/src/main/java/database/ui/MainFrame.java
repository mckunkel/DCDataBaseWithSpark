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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import database.process.DataProcess;
import database.service.MainFrameService;
import database.service.MainFrameServiceImpl;
import database.utils.NumberConstants;
import database.utils.StringConstants;

public class MainFrame extends JFrame {

	private MainFrameService mainFrameService;
	private TablePanel tablePanel;
	private TablePanel tablePanelEast;
	private TablePanel tablePanelWest;

	private StatusPanel statusPanel;

	private JTabbedPane tabbedPane = null;
	ImageIcon icon = new ImageIcon("java-swing-tutorial.JPG");

	JMenuBar menuBar = null;
	JMenu jMenu = null;
	JMenu miniMenu = null;
	JMenuItem addItem = null;
	JMenuItem removeItem = null;

	Dataset<Row> aDF = null;
	JFileChooser fc = null;
	File file = null;

	public MainFrame() {
		super(StringConstants.APP_NAME);
		constructAppWindow();
		setJMenuBar(createFrameMenu());
		initializeVariables();
		constructLayout();
		createFileChooser();

		refreshTable();
		setCallbacks();
	}

	private void createFileChooser() {
		fc = new JFileChooser();
	}

	private void initializeVariables() {
		this.mainFrameService = new MainFrameServiceImpl(aDF);
		this.tablePanel = new TablePanel();
		this.tablePanelEast = new TablePanel();
		this.tablePanelWest = new TablePanel();

		this.tabbedPane = new JTabbedPane();
		this.statusPanel = new StatusPanel();

	}

	private void setCallbacks() {

	}

	private void refreshTable() {

	}

	private void constructLayout() {
		setLayout(new BorderLayout());
		add(tablePanelEast, BorderLayout.EAST);
		add(tablePanel, BorderLayout.CENTER);
		add(tablePanelWest, BorderLayout.WEST);
		add(statusPanel, BorderLayout.SOUTH);
	}

	protected JPanel createInnerPanel(String text) {
		JPanel jplPanel = new JPanel();
		JLabel jlbDisplay = new JLabel(text);
		jlbDisplay.setHorizontalAlignment(JLabel.CENTER);
		jplPanel.setLayout(new GridLayout(1, 1));
		jplPanel.add(jlbDisplay);
		return jplPanel;
	}

	private void constructAppWindow() {
		setSize(NumberConstants.APP_WINDOW_SIZE_WIDTH, NumberConstants.APP_WINDOW_SIZE_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JMenuBar createFrameMenu() {
		menuBar = new JMenuBar();

		menuBar.add(createFileMenu());

		return menuBar;

	}

	private JMenu createFileMenu() {
		jMenu = new JMenu(StringConstants.MAIN_MENU_FILE);
		JMenuItem openItem = new JMenuItem(StringConstants.MAIN_MENU_OPEN);
		JMenuItem exitItem = new JMenuItem(StringConstants.MAIN_MENU_EXIT);

		jMenu.add(openItem);
		jMenu.add(exitItem);

		openItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("you clicked me");
				int returnVal = fc.showOpenDialog(MainFrame.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					// This is where a real application would open the file.
					System.out.println("Opening: " + file.getName() + ".");
					DataProcess dataProcess = new DataProcess(file);
				} else {
					System.out.println("Open command cancelled by user.");
				}
			}
		});
		exitItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int action = JOptionPane.showConfirmDialog(MainFrame.this, StringConstants.MAIN_MENU_EXIT_TEXT,
						StringConstants.MAIN_MENU_EXIT_TITLE, JOptionPane.OK_CANCEL_OPTION);

				if (action == JOptionPane.OK_OPTION) {
					// mainFrameService.shutdown();
					statusPanel.stopTimer();
					System.gc();
					System.exit(0);
				}
			}
		});

		return jMenu;
	}

}
