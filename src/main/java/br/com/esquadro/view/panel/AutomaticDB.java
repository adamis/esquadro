package br.com.esquadro.view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import br.com.esquadro.controler.ListTableController;
import br.com.esquadro.controler.ProcessaComponentesController;
import br.com.esquadro.helper.SqliteHelper;
import br.com.esquadro.resources.ResourcesImages;
import br.com.esquadro.sqlite.entity.BancoDadosEntity;
import br.com.esquadro.util.PersonalItem;
import br.com.esquadro.view.ConsoleLog;

public class AutomaticDB extends JInternalFrame {

	private static final long serialVersionUID = 4593163889948851947L;
	private TextField inputProject;
	private JTable table;
	private BancoDadosEntity bancoDados = null;

	private ConsoleLog consoleLog;
	private JTextField textField;

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

		try {
			Dao<BancoDadosEntity, Integer> bancoDadosDao = DaoManager.createDao(SqliteHelper.connectionSource,
					BancoDadosEntity.class);

			List<BancoDadosEntity> listBancoDados = bancoDadosDao.queryForAll();

			PersonalItem item = new PersonalItem();
			item.setName("Selecione...");
			item.setValue(null);

			comboBox.addItem(item);

			for (Iterator iterator = listBancoDados.iterator(); iterator.hasNext();) {
				BancoDadosEntity bancoDados = (BancoDadosEntity) iterator.next();

				item = new PersonalItem();
				item.setName(bancoDados.getNome() + " (" + bancoDados.getNameBd() + ")");
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
					bancoDados = (BancoDadosEntity) personalItem.getValue();
					updateGrid(bancoDados);
				} else {
					DefaultTableModel dtm = new DefaultTableModel();
					table.setModel(dtm);
				}
			}

		});

		comboBox.setBounds(55, 88, 598, 30);
		getContentPane().add(comboBox);

		table = new JTable();
		table.setBounds(0, 0, 500, 300);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 202, 643, 314);
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
							listComponents, getUrl(), false, consoleLog, false, bancoDados);

					new Thread(processaComponentesController).start();

				}
			}
		});
		btnNewButton.setBounds(240, 527, 198, 36);
		getContentPane().add(btnNewButton);

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

		JLabel lblTabelas = new JLabel("Tabelas (Atenção ao buscar uma tabela ele cancela as anteriores marcas)");
		lblTabelas.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTabelas.setBounds(10, 136, 643, 14);
		getContentPane().add(lblTabelas);

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				JTextField textField = (JTextField) e.getSource();
				String text = textField.getText();

				ListTableController controller = new ListTableController(bancoDados, table, true, consoleLog, text);
				controller.run();

			}
		});
		textField.setToolTipText("Buscar Tabelas");
		textField.setColumns(10);
		textField.setBounds(10, 161, 643, 30);
		getContentPane().add(textField);

	}

	private void updateGrid(BancoDadosEntity bancoDados) {
		ListTableController controller = new ListTableController(bancoDados, table, true, consoleLog, "");
		controller.run();
	}
}
