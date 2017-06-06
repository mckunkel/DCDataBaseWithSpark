package database.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import database.utils.NumberConstants;
import database.utils.StringConstants;

public class ChooseFileForm extends JDialog implements ActionListener {// implements
	// ActionListener

	private JButton okButton;

	public ChooseFileForm(JFrame parentFrame) {
		super(parentFrame, StringConstants.CHOOSEFILE_FORM_TITLE, false);

		initializeVariables();
		constructLayout();
		setWindow(parentFrame);
	}

	private void setWindow(JFrame parentFrame) {
		setSize(NumberConstants.SORT_FORM_WINDOW_SIZE_WIDTH, NumberConstants.SORT_FORM_WINDOW_SIZE_HEIGHT / 4);
		setLocationRelativeTo(parentFrame);
	}

	private void initializeVariables() {
		this.okButton = new JButton(StringConstants.FORM_OK);

		this.okButton.addActionListener(this);

	}

	private void constructLayout() {

		JPanel buttonsPanel = new JPanel();
		// ////////// Buttons Panel ///////////////

		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(okButton);

		Dimension btnSize = okButton.getPreferredSize();
		okButton.setPreferredSize(btnSize);

		// Add sub panels to dialog
		setLayout(new BorderLayout());
		add(buttonsPanel, BorderLayout.SOUTH);

	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.okButton) {
			setVisible(false);
		}
	}

}
