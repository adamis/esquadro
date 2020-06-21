import javax.swing.JOptionPane;

/**
 * 
 */

/**
 * @author adamis.rocha
 *
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			SplashScreen frame = new SplashScreen();
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

}
