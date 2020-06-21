package br.com.esquadro.view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import br.com.esquadro.controler.NovoProjetoController;
import br.com.esquadro.resources.ResourcesImages;
import br.com.esquadro.view.ConsoleLog;

/*
 -------------- CHECKLIST ------------

 *Abre a Tela
 *Informa os dados
 *Gera Projeto

 *Gera Core Module
 	-Gera NavBar Component

 *Gera Shared Module 
 	-Gera Message Component

 *install primeng
 *install angular/animation
 *install primeincons
 *install primeflex
 *install bootstrap

 *ajusta (.angular-cli.json) Style(PrimeNG/Bootstrap)
 *Copia e reescreve:
  	-core/*
  	-shared/*


 */

public class NovoProjeto extends JInternalFrame {

	private static final long serialVersionUID = 4593163889948851947L;
	private static JTextField textField;
	private TextField inputWorkspace;
	private JRadioButton rdbtnTop, rdbtnLeft, rdbtnRight;
	private JRadioButton rdbtnScss, rdbtnCss, rdbtnSass, rdbtnLess;
	private JCheckBox chckbxSpec;

	private JButton btnGerarProjeto;

	private ConsoleLog consoleLog;
	private JTextField txtBackEnd;

	/**
	 * Create the panel.
	 */
	public NovoProjeto(final ConsoleLog consoleLog) {
		try {

			setIconifiable(true);
			setClosable(true);
			this.consoleLog = consoleLog;
			getContentPane().setBackground(new Color(255, 255, 255));
			setTitle("Novo Projeto");

			getContentPane().setLayout(null);

			JLabel lblNomeDoProjeto = new JLabel("Nome do Projeto:");
			lblNomeDoProjeto.setFont(new Font("Tahoma", Font.PLAIN, 9));
			lblNomeDoProjeto.setBounds(10, 83, 194, 14);
			getContentPane().add(lblNomeDoProjeto);

			setPreferredSize(new Dimension(668, 400));
			setSize(new Dimension(642, 398));
			setAutoscrolls(true);

			textField = new JTextField();
			textField.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));

			textField.setBounds(10, 100, 427, 20);
			getContentPane().add(textField);
			textField.setColumns(10);

			btnGerarProjeto = new JButton("     Gerar Projeto");

			btnGerarProjeto.setIcon(ResourcesImages.analysis());

			btnGerarProjeto.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					try {
						execBat();
					} catch (Exception e) {
						consoleLog.setText(e.getMessage());
						e.printStackTrace();
					}

				}
			});
			btnGerarProjeto.setBounds(222, 316, 181, 41);
			getContentPane().add(btnGerarProjeto);

			JPanel panel = new JPanel();
			panel.setLayout(null);
			panel.setBackground(Color.WHITE);
			panel.setBounds(0, 0, 611, 72);
			getContentPane().add(panel);

			inputWorkspace = new TextField();
			inputWorkspace.setPreferredSize(new Dimension(10, 0));
			inputWorkspace.setMaximumSize(new Dimension(10, 10));
			inputWorkspace.setColumns(70);
			inputWorkspace.setBounds(177, 26, 424, 22);
			panel.add(inputWorkspace);

			JButton btnWorkspace = new JButton("     Workspace");
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
			btnWorkspace.setBounds(10, 20, 161, 34);

			btnWorkspace.setIcon(ResourcesImages.report());

			panel.add(btnWorkspace);

			JLabel lblPosioDoMenu = new JLabel("Posi\u00E7\u00E3o do Menu:");
			lblPosioDoMenu.setFont(new Font("Tahoma", Font.PLAIN, 9));
			lblPosioDoMenu.setBounds(10, 234, 194, 14);
			getContentPane().add(lblPosioDoMenu);

			ButtonGroup bGroupPositionMenu = new ButtonGroup();
			ButtonGroup bGroupStyle = new ButtonGroup();

			rdbtnTop = new JRadioButton("Top");

			rdbtnTop.setBackground(Color.WHITE);
			rdbtnTop.setBounds(266, 255, 109, 23);
			getContentPane().add(rdbtnTop);
			bGroupPositionMenu.add(rdbtnTop);

			rdbtnLeft = new JRadioButton("Left");
			rdbtnLeft.setSelected(true);

			rdbtnLeft.setBackground(Color.WHITE);
			rdbtnLeft.setBounds(10, 255, 109, 23);
			getContentPane().add(rdbtnLeft);
			bGroupPositionMenu.add(rdbtnLeft);

			rdbtnRight = new JRadioButton("Right");

			rdbtnRight.setBackground(Color.WHITE);
			rdbtnRight.setBounds(131, 255, 109, 23);
			getContentPane().add(rdbtnRight);
			bGroupPositionMenu.add(rdbtnRight);

			JLabel lblStyle = new JLabel("Style:");
			lblStyle.setFont(new Font("Tahoma", Font.PLAIN, 9));
			lblStyle.setBounds(10, 183, 194, 14);
			getContentPane().add(lblStyle);

			rdbtnCss = new JRadioButton("CSS");
			rdbtnCss.setSelected(true);
			rdbtnCss.setBackground(Color.WHITE);
			rdbtnCss.setBounds(10, 204, 109, 23);
			getContentPane().add(rdbtnCss);

			rdbtnSass = new JRadioButton("SASS");
			rdbtnSass.setBackground(Color.WHITE);
			rdbtnSass.setBounds(155, 204, 109, 23);
			getContentPane().add(rdbtnSass);

			rdbtnLess = new JRadioButton("LESS");
			rdbtnLess.setBackground(Color.WHITE);
			rdbtnLess.setBounds(266, 204, 109, 23);
			getContentPane().add(rdbtnLess);

			rdbtnScss = new JRadioButton("SCSS");
			rdbtnScss.setBackground(Color.WHITE);
			rdbtnScss.setBounds(390, 204, 109, 23);
			getContentPane().add(rdbtnScss);

			bGroupStyle.add(rdbtnCss);
			bGroupStyle.add(rdbtnSass);
			bGroupStyle.add(rdbtnLess);
			bGroupStyle.add(rdbtnScss);

			chckbxSpec = new JCheckBox("Spec");
			chckbxSpec.setSelected(true);
			chckbxSpec.setBackground(Color.WHITE);
			chckbxSpec.setBounds(500, 98, 97, 23);
			getContentPane().add(chckbxSpec);

			JLabel lblUrlDoBack = new JLabel("URL do Back End:");
			lblUrlDoBack.setFont(new Font("Tahoma", Font.PLAIN, 9));
			lblUrlDoBack.setBounds(10, 131, 194, 14);
			getContentPane().add(lblUrlDoBack);

			txtBackEnd = new JTextField();
			txtBackEnd.setColumns(10);
			txtBackEnd.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
			txtBackEnd.setBounds(10, 148, 539, 20);
			getContentPane().add(txtBackEnd);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO" + e.getMessage(), "Atenção", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void execBat() throws IOException, InterruptedException {

		// System.err.println("INICIO");
		// UrlValidator urlValidator = new UrlValidator();
		if (!txtBackEnd.getText().isEmpty()) {

			consoleLog.setVisible(true);
			consoleLog.moveToFront();

			NovoProjetoController runnable = new NovoProjetoController(getContentPane(), btnGerarProjeto, consoleLog,
					inputWorkspace, textField, this, rdbtnSass, rdbtnLess, rdbtnScss, chckbxSpec, rdbtnRight, rdbtnLeft,
					rdbtnTop, txtBackEnd);

			new Thread(runnable).start();

		} else {
			// TODO
			JOptionPane.showMessageDialog(null, "Informe uma URL valida!", "Atenção", JOptionPane.WARNING_MESSAGE);
		}
	}
}
