package br.com.esquadro.view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import br.com.esquadro.resources.ResourcesImages;
import br.com.esquadro.util.Utils;
import br.com.esquadro.view.ConsoleLog;

public class NovaInterface extends JInternalFrame {

	private static final long serialVersionUID = 4593163889948851947L;
	private JTextField nomeInterface;
	private JTextField txtApp;
	private ConsoleLog consoleLog;
	private String tempApp = "";

	JCheckBox ckApp;
	private JLabel lblComando;
	private JPanel panel_2;
	private TextField inputWorkspace;
	private JButton btnWorkspace;

	/**
	 * Create the panel.
	 */
	public NovaInterface(ConsoleLog consoleLog) {
		this.consoleLog = consoleLog;
		setOpaque(true);
		getContentPane().setBackground(new Color(255, 255, 255));
		setTitle("Nova Interface");
		setMaximizable(true);
		setClosable(true);

		getContentPane().setLayout(null);

		JLabel lblNomeDoProjeto = new JLabel("Nome da Interface:");
		lblNomeDoProjeto.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblNomeDoProjeto.setBounds(10, 125, 194, 14);
		getContentPane().add(lblNomeDoProjeto);

		setPreferredSize(new Dimension(668, 400));
		setSize(new Dimension(513, 463));
		setAutoscrolls(true);

		nomeInterface = new JTextField();
		nomeInterface.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
		nomeInterface.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				finalizaComand();
			}
		});

		nomeInterface.setBounds(10, 140, 471, 20);
		getContentPane().add(nomeInterface);
		nomeInterface.setColumns(10);

		JButton btnGerarProjeto = new JButton("      Gerar Interface");
		btnGerarProjeto.setIcon(ResourcesImages.analysis());
		btnGerarProjeto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					execBat();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		btnGerarProjeto.setBounds(161, 376, 177, 33);
		getContentPane().add(btnGerarProjeto);

		ckApp = new JCheckBox("App:");
		ckApp.setBackground(new Color(255, 255, 255));
		ckApp.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (ckApp.isSelected()) {
					txtApp.setEnabled(true);
				} else {
					txtApp.setEnabled(false);
				}
				finalizaComand();
			}
		});
		ckApp.setBounds(12, 205, 97, 23);
		getContentPane().add(ckApp);

		txtApp = new JTextField();
		txtApp.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
		txtApp.setOpaque(false);
		txtApp.setDisabledTextColor(new Color(255, 255, 255));
		txtApp.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				finalizaComand();
			}
		});
		txtApp.setEnabled(false);
		txtApp.setColumns(10);
		txtApp.setBounds(117, 206, 334, 20);
		getContentPane().add(txtApp);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel.setName("");
		panel.setToolTipText("");
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Configura\u00E7\u00F5es",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		panel.setBounds(10, 176, 471, 73);
		getContentPane().add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBorder(new TitledBorder(null, "Comandos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 292, 471, 73);
		getContentPane().add(panel_1);

		lblComando = new JLabel("");
		lblComando.setMaximumSize(new Dimension(300, 0));
		lblComando.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_1.add(lblComando);

		panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(0, 0, 497, 72);
		getContentPane().add(panel_2);

		inputWorkspace = new TextField();
		inputWorkspace.setPreferredSize(new Dimension(10, 0));
		inputWorkspace.setMaximumSize(new Dimension(10, 10));
		inputWorkspace.setColumns(70);
		inputWorkspace.setBounds(190, 26, 297, 22);
		panel_2.add(inputWorkspace);

		btnWorkspace = new JButton("     Projeto");
		btnWorkspace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setCurrentDirectory(new File("."));
				int returnVal = fc.showOpenDialog(fc);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File dir = fc.getSelectedFile();
					inputWorkspace.setText(dir.getPath() + "");
				}
			}
		});
		btnWorkspace.setPreferredSize(new Dimension(125, 23));
		btnWorkspace.setMaximumSize(new Dimension(150, 23));
		btnWorkspace.setBounds(new Rectangle(10, 20, 174, 34));
		btnWorkspace.setBackground(Color.WHITE);
		btnWorkspace.setBounds(10, 20, 174, 34);

		btnWorkspace.setIcon(ResourcesImages.report());

		panel_2.add(btnWorkspace);
		// lblComando.setFont(new Font("Tahoma", Font.BOLD, 14));
	}

	private void execBat() throws IOException, InterruptedException {
//		System.err.println("INICIO");

		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c",
				"cd " + inputWorkspace.getText() + "& ng generate interface " + nomeInterface.getText());
		pb.redirectErrorStream(true);
		Process p = pb.start();
		int waitFor = p.waitFor();

		BufferedReader output = Utils.getOutput(p);
		BufferedReader error = Utils.getError(p);

		String ligne = "";

		consoleLog.setVisible(true);
		consoleLog.moveToFront();
		consoleLog.setText("Criando nova Interface: " + nomeInterface.getText());

		while ((ligne = output.readLine()) != null) {
			consoleLog.setText(ligne);
		}

		while ((ligne = error.readLine()) != null) {
			consoleLog.setText(ligne);
		}

		if (waitFor != 0) {
			System.err.println("ERROR");

			Object[] options = { "OK" };

			JOptionPane.showOptionDialog(null, "Erro ao criar o Component!", "Fail", JOptionPane.OK_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		} else {

			Object[] options = { "OK" };

			JOptionPane.showOptionDialog(null, "Component criado com Sucesso!", "Sucesso", JOptionPane.OK_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			System.err.println("FIM");
		}

	}

	public static void writeLog(List<String> text, String fileName) {

		try {

			File f = new File(fileName);
//			System.out.println(f.getAbsolutePath());

			if (f.exists()) {
				f.delete();
			}

			// O parametro � que indica se deve sobrescrever ou continua no
			// arquivo.
			FileWriter fw = new FileWriter(fileName, true);
			BufferedWriter conexao = new BufferedWriter(fw);

			for (int i = 0; i < text.size(); i++) {
				conexao.write(text.get(i));
				conexao.newLine();
			}
			conexao.newLine();
			conexao.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String finalizaComand() {
		lblComando.setText("<html><div>" + "ng generate interface " + nomeInterface.getText() + " " + getPrefix()
				+ "</div></html>");
		return lblComando.getText();
	}

	private String getPrefix() {
		String prefix = "";

		if (ckApp.isSelected()) {
			prefix += " --app " + txtApp.getText();
			tempApp = " --app " + txtApp.getText();
		} else {
			prefix = prefix.replace(tempApp.trim(), "");
		}

		return prefix;
	}

}
