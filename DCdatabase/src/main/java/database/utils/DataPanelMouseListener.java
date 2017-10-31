package database.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.TreeSet;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import database.objects.StatusChangeDB;
import database.service.MainFrameService;

public class DataPanelMouseListener implements ActionListener, MouseListener, MouseMotionListener {
	private MainFrameService mainFrameService = MainFrameServiceManager.getSession();
	private TreeSet<StatusChangeDB> queryList = new TreeSet<>();

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

		if (e.getButton() == 1 && e.getClickCount() == 1) {

			final JTable target = (JTable) e.getSource();
			// target.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			int[] selection = target.getSelectedRows();
			for (int i : selection) {
				StatusChangeDB statusChangeDB = new StatusChangeDB();
				statusChangeDB.setSector(target.getValueAt(i, 0).toString());
				statusChangeDB.setSuperlayer(target.getValueAt(i, 1).toString());
				statusChangeDB.setLoclayer(target.getValueAt(i, 2).toString());
				statusChangeDB.setLocwire(target.getValueAt(i, 3).toString());
				statusChangeDB.setProblem_type(StringConstants.PROBLEM_TYPES[this.mainFrameService.getFaultNum() + 1]);
				statusChangeDB.setStatus_change_type(this.mainFrameService.getBrokenOrFixed().toString());
				statusChangeDB.setRunno(this.mainFrameService.getRunNumber());
				queryList.add(statusChangeDB);
			}

		}
		if (SwingUtilities.isRightMouseButton(e)) {
			int reply = JOptionPane.showConfirmDialog(null, "Add Selected Fault to DB list?",
					"User Selected Add to List", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				this.mainFrameService.prepareMYSQLQuery(queryList);
				this.mainFrameService.addToCompleteSQLList(queryList);
				this.mainFrameService.getDataPanel().removeItems(queryList);
				this.mainFrameService.clearTempSQLList();
				this.mainFrameService.getSQLPanel().setTableModel(this.mainFrameService.getCompleteSQLList());
				JOptionPane.showMessageDialog(null,
						"Entering values selected by user " + this.mainFrameService.getUserName());
			} else {
				JOptionPane.showMessageDialog(null, "electrons do not grow on trees");
			}

		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
