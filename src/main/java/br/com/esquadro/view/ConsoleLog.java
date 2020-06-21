package br.com.esquadro.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.MatteBorder;
import javax.swing.text.DefaultCaret;

import br.com.esquadro.util.Statics;

public class ConsoleLog extends JInternalFrame {

	private static final long serialVersionUID = 4593163889948851947L;
	private JTextArea txtrLoading;

	/**
	 * Create the panel.
	 */
	public ConsoleLog() {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setClosable(true);
		getContentPane().setBackground(new Color(255, 255, 255));
		setTitle("Console");

		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 1000, 500);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("<html><div style='text-align: center;'>clear console</div></html>");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtrLoading.setText("");
				txtrLoading.setText("(" + Statics.NAME_SYS + " v" + Statics.VERSION + " by Adamis Starling)\r\n");

			}
		});
		lblNewLabel.setBorder(new MatteBorder(1, 1, 1, 1, new Color(255, 0, 0)));
		lblNewLabel.setFont(new Font("Open Sans", Font.BOLD, 14));
		lblNewLabel.setForeground(new Color(255, 0, 0));
		lblNewLabel.setBounds(878, 426, 100, 25);
		panel.add(lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 1000, 463);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane);

		txtrLoading = new JTextArea();
		txtrLoading.setEditable(false);
		txtrLoading.setText(Statics.NAME_SYS + "( v" + Statics.VERSION + " by Adamis Starling)\r\n");
		txtrLoading.setForeground(new Color(0, 204, 0));
		txtrLoading.setBackground(Color.BLACK);
		txtrLoading.setBounds(0, 0, 588, 182);
		scrollPane.setViewportView(txtrLoading);

		DefaultCaret crList = (DefaultCaret) txtrLoading.getCaret();
		crList.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		setPreferredSize(new Dimension(668, 400));
		setSize(new Dimension(1000, 494));
		setAutoscrolls(true);
	}

	public void setText(String text) {
		txtrLoading.setText(txtrLoading.getText() + text + "\n");
	}
}
