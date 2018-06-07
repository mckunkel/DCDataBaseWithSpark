package faultfinder.ui;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import spark.utils.SparkManager;

public class DCFaultFinderApp {

	public static void main(String[] args) {

		if (SparkManager.isMySQLOpen()) {

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
		} else {
			JOptionPane.showMessageDialog(null, "Your MySQL is no started or Database does not exist",
					"Eggs are not supposed to be green.", JOptionPane.ERROR_MESSAGE);
		}
	}
}