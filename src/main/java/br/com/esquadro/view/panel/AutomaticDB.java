package br.com.esquadro.view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import br.com.esquadro.controler.ListTableController;
import br.com.esquadro.controler.ProcessaComponentesController;
import br.com.esquadro.model.BancoDados;
import br.com.esquadro.resources.ResourcesImages;
import br.com.esquadro.util.Conexao;
import br.com.esquadro.util.PersonalItem;
import br.com.esquadro.util.SqliteHelper;
import br.com.esquadro.view.ConsoleLog;

public class AutomaticDB extends JInternalFrame {

	private static final long serialVersionUID = 4593163889948851947L;
	private TextField inputProject;
	private JTable table;
	private BancoDados bancoDados = null;
	private JCheckBox chckbxSpec;
	private JCheckBox chckbxMockJson;

	private ConsoleLog consoleLog;

	public void setUrl(TextField inputProject) {
		this.inputProject = inputProject;
	}

	public String getUrl() {
		return this.inputProject.getText();
	}

	/**
	 * Create the panel.
	 */
	public AutomaticDB(final ConsoleLog consoleLog) {
		this.consoleLog = consoleLog;

		getContentPane().setBackground(new Color(255, 255, 255));
		setMaximizable(true);
		setClosable(true);

		getContentPane().setLayout(null);

		setPreferredSize(new Dimension(668, 400));
		setSize(new Dimension(679, 604));
		setAutoscrolls(true);

		JLabel lblBanco = new JLabel("Banco:");
		lblBanco.setBounds(10, 97, 46, 14);
		getContentPane().add(lblBanco);

		JComboBox<PersonalItem> comboBox = new JComboBox<PersonalItem>();

		Conexao conexao = SqliteHelper.conexaoSQLITE;

		ResultSet executeQuery;
		try {
			executeQuery = conexao.executeQuery("SELECT * FROM \"banco_dados\"");

			PersonalItem item = new PersonalItem();
			item.setName("Selecione...");
			item.setValue(null);

			comboBox.addItem(item);

			while (executeQuery.next()) {
				item = new PersonalItem();
				item.setName(executeQuery.getString("nome") + " (" + executeQuery.getString("nameBd") + ")");

				BancoDados bancoDados = new BancoDados();
				bancoDados.setId(executeQuery.getInt("id"));
				bancoDados.setNome(executeQuery.getString("nome"));
				bancoDados.setIp(executeQuery.getString("ip"));
				bancoDados.setPorta(executeQuery.getString("porta"));
				bancoDados.setUsuario(executeQuery.getString("usuario"));
				bancoDados.setSenha(executeQuery.getString("senha"));
				bancoDados.setCharset(executeQuery.getString("charset"));
				bancoDados.setNameBd(executeQuery.getString("nameBd"));
				bancoDados.setSchema(executeQuery.getString("schema"));
				bancoDados.setTipo(executeQuery.getString("tipo"));
				item.setValue(bancoDados);

				comboBox.addItem(item);
			}
		} catch (SQLException e) {
			this.consoleLog.setText("Erro: " + e.getMessage());
			e.printStackTrace();
		}

		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent item) {
				PersonalItem personalItem = (PersonalItem) item.getItem();
				if (personalItem.getValue() != null) {
					bancoDados = (BancoDados) personalItem.getValue();
					updateGrid(bancoDados);
				} else {
					DefaultTableModel dtm = new DefaultTableModel();
					table.setModel(dtm);
				}
			}

		});

		comboBox.setBounds(55, 88, 455, 30);
		getContentPane().add(comboBox);

		table = new JTable();
		table.setBounds(0, 0, 500, 300);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 128, 643, 388);
		scrollPane.setViewportView(table);
		getContentPane().add(scrollPane);

		JButton btnNewButton = new JButton("     Gerar Componentes");
		btnNewButton.setBackground(new Color(255, 255, 255));

		btnNewButton.setIcon(ResourcesImages.analysis());

		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				consoleLog.setVisible(true);
				consoleLog.moveToFront();

				DefaultTableModel model = (DefaultTableModel) table.getModel();

				List<String> listComponents = new ArrayList<>();

				for (int count = 0; count < model.getRowCount(); count++) {

					if (model.getValueAt(count, 0).toString().equals("true")) {
						listComponents.add(model.getValueAt(count, 1).toString());
					}
				}

				if (listComponents.size() > 0) {

					ProcessaComponentesController processaComponentesController = new ProcessaComponentesController(
							listComponents, getUrl(), chckbxSpec.isSelected(), consoleLog, chckbxMockJson.isSelected(),
							bancoDados);
					// processaComponentesController.run();

					new Thread(processaComponentesController).start();

				}
			}
		});
		btnNewButton.setBounds(240, 527, 198, 36);
		getContentPane().add(btnNewButton);

		chckbxSpec = new JCheckBox("Spec");

		chckbxSpec.setSelected(true);
		chckbxSpec.setBounds(101, 534, 133, 23);
		getContentPane().add(chckbxSpec);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 663, 72);
		getContentPane().add(panel);

		inputProject = new TextField();
		inputProject.setPreferredSize(new Dimension(10, 0));
		inputProject.setMaximumSize(new Dimension(10, 10));
		inputProject.setColumns(70);
		inputProject.setBounds(190, 26, 463, 22);
		panel.add(inputProject);

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
					inputProject.setText(dir.getPath() + "");
				}
			}
		});
		btnWorkspace.setPreferredSize(new Dimension(125, 23));
		btnWorkspace.setMaximumSize(new Dimension(150, 23));
		btnWorkspace.setBounds(new Rectangle(10, 20, 174, 34));
		btnWorkspace.setBackground(Color.WHITE);
		btnWorkspace.setBounds(10, 20, 174, 34);

		btnWorkspace.setIcon(ResourcesImages.report());

		panel.add(btnWorkspace);

		chckbxMockJson = new JCheckBox("Mock Json");
		chckbxMockJson.setSelected(true);
		chckbxMockJson.setBounds(464, 534, 133, 23);
		getContentPane().add(chckbxMockJson);

	}

	private void updateGrid(BancoDados bancoDados) {
		ListTableController controller = new ListTableController(bancoDados, table, true, consoleLog);
		controller.run();
	}
}
