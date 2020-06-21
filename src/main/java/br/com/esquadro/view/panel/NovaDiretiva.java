package br.com.esquadro.view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
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
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import br.com.esquadro.resources.ResourcesImages;
import br.com.esquadro.util.Utils;
import br.com.esquadro.view.ConsoleLog;

public class NovaDiretiva extends JInternalFrame {

	private static final long serialVersionUID = 4593163889948851947L;
	private JTextField inputWorkspace;
	private JTextField nomeDiretiva;
	private JTextField txtPrefixo;
	private JTextField txtEncapsulation;
	private JTextField txtModule;
	private ConsoleLog consoleLog;
	private String prefix = "";
	private String tempPrefix = "";
	private String tempEncapsulamento = "";
	private String tempModulo = "";

	JCheckBox ckSpecOff, ckCssOff, ckTemplateOff, ckImportOff, ckExternal, ckFlatOff, ckPrefixo, ckEncapsulation,
			ckModule;
	private JLabel lblComando;

	/**
	 * Create the panel.
	 */
	public NovaDiretiva(ConsoleLog consoleLog) {
		this.consoleLog = consoleLog;
		getContentPane().setBackground(new Color(255, 255, 255));
		setTitle("Nova Diretiva");
		setMaximizable(true);
		setClosable(true);

		getContentPane().setLayout(null);

		JLabel lblNomeDoProjeto = new JLabel("Nome da diretiva:");
		lblNomeDoProjeto.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblNomeDoProjeto.setBounds(10, 83, 194, 14);
		getContentPane().add(lblNomeDoProjeto);

		setPreferredSize(new Dimension(668, 400));
		setSize(new Dimension(513, 495));
		setAutoscrolls(true);

		nomeDiretiva = new JTextField();
		nomeDiretiva.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
		nomeDiretiva.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				finalizaComand();
			}
		});

		nomeDiretiva.setBounds(10, 100, 481, 20);
		getContentPane().add(nomeDiretiva);
		nomeDiretiva.setColumns(10);

		JButton btnGerarProjeto = new JButton("Gerar Diretiva");
		btnGerarProjeto.setBackground(new Color(255, 255, 255));

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
		btnGerarProjeto.setBounds(189, 418, 134, 36);
		getContentPane().add(btnGerarProjeto);

		ckSpecOff = new JCheckBox("Spec off");
		ckSpecOff.setBackground(new Color(255, 255, 255));
		ckSpecOff.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				finalizaComand();
			}
		});
		ckSpecOff.setBounds(47, 154, 73, 23);
		getContentPane().add(ckSpecOff);

		ckCssOff = new JCheckBox("CSS off");
		ckCssOff.setBackground(new Color(255, 255, 255));
		ckCssOff.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				finalizaComand();
			}
		});
		ckCssOff.setBounds(202, 154, 81, 23);
		getContentPane().add(ckCssOff);

		ckTemplateOff = new JCheckBox("Template Off");
		ckTemplateOff.setBackground(new Color(255, 255, 255));
		ckTemplateOff.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				finalizaComand();
			}
		});
		ckTemplateOff.setBounds(345, 154, 113, 23);
		getContentPane().add(ckTemplateOff);

		ckImportOff = new JCheckBox("Import Off");
		ckImportOff.setBackground(new Color(255, 255, 255));
		ckImportOff.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				finalizaComand();
			}
		});
		ckImportOff.setBounds(346, 181, 97, 23);
		getContentPane().add(ckImportOff);

		ckExternal = new JCheckBox("External modulo");
		ckExternal.setBackground(new Color(255, 255, 255));
		ckExternal.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				finalizaComand();
			}
		});
		ckExternal.setBounds(47, 182, 142, 23);
		getContentPane().add(ckExternal);

		ckFlatOff = new JCheckBox("Flat Off");
		ckFlatOff.setBackground(new Color(255, 255, 255));
		ckFlatOff.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				finalizaComand();
			}
		});
		ckFlatOff.setBounds(202, 182, 97, 23);
		getContentPane().add(ckFlatOff);

		ckPrefixo = new JCheckBox("Prefixo:");
		ckPrefixo.setBackground(new Color(255, 255, 255));
		ckPrefixo.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (ckPrefixo.isSelected()) {
					txtPrefixo.setEnabled(true);
				} else {
					txtPrefixo.setEnabled(false);
				}
				finalizaComand();
			}
		});
		ckPrefixo.setBounds(22, 239, 73, 23);
		getContentPane().add(ckPrefixo);

		ckEncapsulation = new JCheckBox("Encapsulation:");
		ckEncapsulation.setBackground(new Color(255, 255, 255));

		ckEncapsulation.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (ckEncapsulation.isSelected()) {
					txtEncapsulation.setEnabled(true);
				} else {
					txtEncapsulation.setEnabled(false);
				}
				finalizaComand();
			}
		});
		ckEncapsulation.setBounds(22, 265, 113, 23);
		getContentPane().add(ckEncapsulation);

		ckModule = new JCheckBox("Module:");
		ckModule.setBackground(new Color(255, 255, 255));
		ckModule.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (ckModule.isSelected()) {
					txtModule.setEnabled(true);
				} else {
					txtModule.setEnabled(false);
				}
				finalizaComand();
			}
		});
		ckModule.setBounds(22, 291, 73, 23);
		getContentPane().add(ckModule);

		txtPrefixo = new JTextField();
		txtPrefixo.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
		txtPrefixo.setBackground(new Color(255, 255, 255));
		txtPrefixo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				finalizaComand();
			}
		});
		txtPrefixo.setEnabled(false);
		txtPrefixo.setColumns(10);
		txtPrefixo.setBounds(91, 239, 358, 20);
		getContentPane().add(txtPrefixo);

		txtEncapsulation = new JTextField();
		txtEncapsulation.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
		txtEncapsulation.setBackground(new Color(255, 255, 255));
		txtEncapsulation.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				finalizaComand();
			}
		});
		txtEncapsulation.setEnabled(false);
		txtEncapsulation.setColumns(10);
		txtEncapsulation.setBounds(131, 265, 318, 20);
		getContentPane().add(txtEncapsulation);

		txtModule = new JTextField();
		txtModule.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
		txtModule.setBackground(new Color(255, 255, 255));
		txtModule.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				finalizaComand();
			}
		});
		txtModule.setEnabled(false);
		txtModule.setColumns(10);
		txtModule.setBounds(91, 294, 358, 20);
		getContentPane().add(txtModule);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel.setName("");
		panel.setToolTipText("");
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 136, 481, 196);
		getContentPane().add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBorder(new TitledBorder(null, "Comandos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 334, 481, 83);
		getContentPane().add(panel_1);

		lblComando = new JLabel("");
		lblComando.setMaximumSize(new Dimension(300, 0));
		lblComando.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_1.add(lblComando);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 255, 255));
		panel_2.setBounds(0, 0, 497, 72);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);

		inputWorkspace = new JTextField();
		inputWorkspace.setBounds(190, 26, 297, 22);
		inputWorkspace.setMaximumSize(new Dimension(10, 10));
		inputWorkspace.setPreferredSize(new Dimension(10, 0));
		inputWorkspace.setColumns(70);
		panel_2.add(inputWorkspace);

		JButton btnWorkspace = new JButton("     Projeto");
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

		btnWorkspace.setIcon(ResourcesImages.report());

		panel_2.add(btnWorkspace);

	}

	private void execBat() throws IOException, InterruptedException {
//		System.err.println("INICIO");

		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c",
				"cd " + inputWorkspace.getText() + "& ng generate directive " + nomeDiretiva.getText());
		pb.redirectErrorStream(true);
		Process p = pb.start();
		int waitFor = p.waitFor();

		BufferedReader output = Utils.getOutput(p);
		BufferedReader error = Utils.getError(p);

		String ligne = "";

		consoleLog.setVisible(true);
		consoleLog.moveToFront();
		consoleLog.setText("Criando nova Diretiva: " + nomeDiretiva.getText());

		while ((ligne = output.readLine()) != null) {
			consoleLog.setText(ligne);
		}

		while ((ligne = error.readLine()) != null) {
			consoleLog.setText(ligne);
		}

		if (waitFor != 0) {
			System.err.println("ERROR");

			Object[] options = { "OK" };

			JOptionPane.showOptionDialog(null, "Erro ao criar o Diretiva!", "Fail", JOptionPane.OK_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		} else {

			Object[] options = { "OK" };

			JOptionPane.showOptionDialog(null, "Diretiva criado com Sucesso!", "Sucesso", JOptionPane.OK_OPTION,
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
		lblComando.setText("<html><div>" + "ng generate directive " + nomeDiretiva.getText() + " " + getPrefix()
				+ "</div></html>");
		return lblComando.getText();
	}

	private String getPrefix() {
		prefix = "";

		if (ckSpecOff.isSelected()) {
			prefix += " --spec=off ";
		} else {
			prefix = prefix.replace("--spec=off", "");
		}

		if (ckCssOff.isSelected()) {
			prefix += " --inline-style=true ";
		} else {
			prefix = prefix.replace("--inline-style=true", "");
		}

		if (ckTemplateOff.isSelected()) {
			prefix += " --inline-template=true ";
		} else {
			prefix = prefix.replace("--inline-template=true", "");
		}

		if (ckImportOff.isSelected()) {
			prefix += " --skip-import ";
		} else {
			prefix = prefix.replace("--skip-import", "");
		}

		if (ckExternal.isSelected()) {
			prefix += " --export ";
		} else {
			prefix = prefix.replace("--export", "");
		}

		if (ckFlatOff.isSelected()) {
			prefix += " --flat=false ";
		} else {
			prefix = prefix.replace("--flat=false", "");
		}

		if (ckPrefixo.isSelected()) {
			prefix += " --prefix " + txtPrefixo.getText();
			tempPrefix = " --prefix " + txtPrefixo.getText();
		} else {
			prefix = prefix.replace(tempPrefix.trim(), "");
		}

		if (ckEncapsulation.isSelected()) {
			prefix += " --view-encapsulation " + txtEncapsulation.getText();
			tempEncapsulamento = " --view-encapsulation " + txtEncapsulation.getText();
		} else {
			prefix = prefix.replace(tempEncapsulamento.trim(), "");
		}

		if (ckModule.isSelected()) {
			prefix += " --module " + txtModule.getText();
			tempModulo = " --module " + txtModule.getText();
		} else {
			prefix = prefix.replace(tempModulo.trim(), "");
		}

		return prefix;
	}
}
