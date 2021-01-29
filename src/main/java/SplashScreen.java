import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import br.com.esquadro.helper.SqliteHelper;
import br.com.esquadro.resources.ResourcesImages;
import br.com.esquadro.util.Styles;
import br.com.esquadro.view.Menu;

public class SplashScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JProgressBar progressBar;
	private JLabel lbText;

	/**
	 * Create the frame.
	 */
	public SplashScreen() {
		this.setIconImage(ResourcesImages.icon2());
		getContentPane().setBackground(Color.WHITE);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setUndecorated(true);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel() {

			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image imagem = ResourcesImages.bgImage();
				g.drawImage(imagem, 0, 0, this);
			}
		};

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		contentPane.add(progressBar, BorderLayout.SOUTH);

		JLabel img = new JLabel("");
		img.setBounds(new Rectangle(0, 0, 50, 50));
		img.setHorizontalTextPosition(SwingConstants.CENTER);
		img.setHorizontalAlignment(SwingConstants.CENTER);
		img.setIcon(ResourcesImages.esquadro());
		contentPane.add(img, BorderLayout.CENTER);

		lbText = new JLabel("Iniciando...");
		lbText.setForeground(Color.WHITE);
		lbText.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lbText, BorderLayout.NORTH);

		process();
	}

	private void process() {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					progressBar.setValue(10);
					lbText.setText("Iniciando...");
					Thread.sleep(300);
					lbText.setText("Conectando Banco de Dados Interno...");
					SqliteHelper.init();
					lbText.setText("Conectado!");
					progressBar.setValue(20);
					Thread.sleep(600);
					lbText.setText("Iniciando Menu...");

//					System.err.println("" + progressBar.getValue());
//					System.err.println("" + (100 - progressBar.getValue()));

					int rangeLimbo = (100 - progressBar.getValue());
					for (int i = 0; i < rangeLimbo; i++) {
						Thread.sleep(10);
						progressBar.setValue(progressBar.getValue() + 1);
					}

					UIManager.setLookAndFeel(Styles.style);

					Thread.sleep(1000);

					SplashScreen.this.setVisible(false);

					Menu menu = new Menu();
					menu.visible(true);

				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}

			}
		};

		new Thread(runnable).start();
	}

}
