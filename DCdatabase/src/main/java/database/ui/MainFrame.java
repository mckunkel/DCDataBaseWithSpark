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
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import database.ui.panels.DataPanel;
import database.ui.panels.FaultPanel;
import database.ui.panels.HistogramPanel;
import database.ui.panels.SQLPanel;
import database.ui.panels.StatusPanel;
import database.utils.NumberConstants;
import database.utils.StringConstants;

public class MainFrame extends JFrame {

	private SQLPanel sqlPanel;
	private DataPanel dataPanel;
	private HistogramPanel histogramPanel;
	private JLabel dataLabel;
	private JLabel sqlLabel;
	private JLabel histLabel;

	private StatusPanel statusPanel;

	private RunForm runForm;
	private SortQueryForm sortQueryForm;
	private CompareQueryForm compareRunForm;
	private FaultPanel faultPanel;

	private JTabbedPane tabbedPane = null;
	ImageIcon icon = new ImageIcon("java-swing-tutorial.JPG");

	private JMenuBar menuBar = null;
	private JMenu jMenu = null;

	private FileDialog fileDialog = null;

	private File[] fileList = null;
	private ArrayList<String> fileArray = null;

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

	private void constructAppWindow() {
		setSize(NumberConstants.APP_WINDOW_SIZE_WIDTH, NumberConstants.APP_WINDOW_SIZE_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JMenuBar createFrameMenu() {
		menuBar = new JMenuBar();

		menuBar.add(createFileMenu());
		menuBar.add(createRunMenu());

		menuBar.add(createSortMenu());
		// menuBar.add(createTestMenu());
		menuBar.add(createCompareMenu());

		return menuBar;

	}

	private void initializeVariables() {
		this.sqlPanel = new SQLPanel();
		this.dataPanel = new DataPanel();
		this.histogramPanel = new HistogramPanel();

		this.dataLabel = new JLabel(StringConstants.MAIN_FORM_DATA);
		dataLabel.setFont(new Font(dataLabel.getFont().getName(), Font.PLAIN, 18));
		this.sqlLabel = new JLabel(StringConstants.MAIN_FORM_SQL);
		sqlLabel.setFont(new Font(dataLabel.getFont().getName(), Font.PLAIN, 18));
		this.histLabel = new JLabel(StringConstants.MAIN_FORM_HIST);
		histLabel.setFont(new Font(dataLabel.getFont().getName(), Font.PLAIN, 18));

		this.tabbedPane = new JTabbedPane();
		this.statusPanel = new StatusPanel();

		this.runForm = new RunForm(this);
		this.sortQueryForm = new SortQueryForm(this);
		this.compareRunForm = new CompareQueryForm(this);
		this.faultPanel = new FaultPanel();

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
				// fc.setMultiSelectionEnabled(true);
				// fc.showOpenDialog(null);
				// fileList = fc.getSelectedFiles();
				// for (File file : fileList) {
				// System.out.println("Readying file " + file);
				// fileArray.add(file.toString());
				// }
				// This was with awt fileDialog

				HipoFilter filter = new HipoFilter();
				fileDialog.setFilenameFilter(filter);
				fileDialog.setVisible(true);
				fileList = fileDialog.getFiles();
				String dir = fileDialog.getDirectory();
				fileArray = new ArrayList<String>();
				for (File file : fileList) {
					fileArray.add(file.toString());

				}
				runForm.setDirectory(dir);
				runForm.setFileList(fileArray);
				compareRunForm.setReady();
				sortQueryForm.setReady();

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

	private JMenu createRunMenu() {
		jMenu = new JMenu(StringConstants.RUN_FORM_TITLE);
		JMenuItem sortList = new JMenuItem(StringConstants.FORM_RUN);
		jMenu.add(sortList);

		sortList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				runForm.setVisible(true);
			}
		});
		return jMenu;
	}

	private JMenu createSortMenu() {

		jMenu = new JMenu(StringConstants.MAIN_MENU_SORT);
		JMenuItem sortList = new JMenuItem(StringConstants.SORT_FORM_TITLE);
		jMenu.add(sortList);

		sortList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				sortQueryForm.resetList();
				sortQueryForm.setVisible(true);
			}
		});
		return jMenu;
	}

	private JMenu createCompareMenu() {

		jMenu = new JMenu(StringConstants.MAIN_MENU_COMPARE);
		JMenuItem sortList = new JMenuItem(StringConstants.COMPARE_FORM_COMPARE);
		jMenu.add(sortList);

		sortList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				compareRunForm.setVisible(true);
			}
		});
		return jMenu;
	}

	private void createFileChooser() {
		this.fileDialog = new FileDialog(this, "FileDialog", FileDialog.LOAD);
		fileDialog.setMultipleMode(true);
	}

	private void constructLayout() {
		JButton b = new JButton("Just fake button");
		Dimension buttonSize = b.getPreferredSize();
		int maxGap = 10;

		// for lists
		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new GridLayout(0, 3));
		// layoutPanel.setPreferredSize(new Dimension((int)
		// (buttonSize.getWidth() * 2.5) + maxGap,
		// (int) (buttonSize.getHeight() * 3.5) + maxGap * 2));
		layoutPanel.add(dataPanel);
		layoutPanel.add(sqlPanel);
		// for histograms and controls to go into layoutPanel
		JPanel histgramControlsPanel = new JPanel();
		histgramControlsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40; // make this component tall
		c.weightx = 0.0;
		// c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		histgramControlsPanel.add(histogramPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		// add controls to histogram panel
		histgramControlsPanel.add(faultPanel, c);
		// add histogram panel to layout
		layoutPanel.add(histgramControlsPanel);

		setLayout(new BorderLayout());
		add(layoutPanel, BorderLayout.CENTER);
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

	private void setCallbacks() {
		// TODO Auto-generated method stub

	}

	private void refreshTable() {
		// TODO Auto-generated method stub

	}

	class HipoFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".hipo"));
		}
	}

}
