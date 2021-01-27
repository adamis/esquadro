package br.com.esquadro.view.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import br.com.esquadro.controler.AddAPIController;
import br.com.esquadro.controler.ListTableController;
import br.com.esquadro.resources.ResourcesImages;
import br.com.esquadro.sqlite.entity.BancoDadosEntity;
import br.com.esquadro.sqlite.helper.SqliteHelper;
import br.com.esquadro.util.Conexao;
import br.com.esquadro.util.DatabaseUtils;
import br.com.esquadro.util.PersonalItem;
import br.com.esquadro.view.ConsoleLog;

public class AddAPI extends JInternalFrame {

	private static final long serialVersionUID = 4593163889948851947L;
	private TextField inputProject;
	private JTable table;
	private BancoDadosEntity bancoDados = null;

	private ConsoleLog consoleLog;
	private JTextField txtPackage;
	public JTextField txtEntity;
	private JTextField txtFilter;
	private JTextField txtResource;
	private JTextField txtRepository;
	private JTextField txtServices;
	private JTextField txtRepositoryImpl;

	private JCheckBox chkResource;
	private JCheckBox chkRepository;
	private JCheckBox chkServices;
	private JCheckBox chkRepositoryImpl;
	private JCheckBox chkFilter;
	private JLabel lblNewLabel_2;
	private String temp;
	private JTextField textField_1;

	//	private String filterTableName;

	public void setUrl(TextField inputProject) {
		this.inputProject = inputProject;
	}

	public String getUrl() {
		return this.inputProject.getText();
	}

	/**
	 * Create the panel.
	 */
	public AddAPI(final ConsoleLog consoleLog) {
		setTitle("Criando uma API com base em Banco");
		setFrameIcon(ResourcesImages.springBoot());
		this.consoleLog = consoleLog;

		getContentPane().setBackground(new Color(255, 255, 255));
		setClosable(true);

		getContentPane().setLayout(null);

		setPreferredSize(new Dimension(668, 400));
		setSize(new Dimension(1235, 616));
		setAutoscrolls(true);

		JLabel lblBanco = new JLabel("Banco:");
		lblBanco.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBanco.setBounds(10, 143, 62, 14);
		getContentPane().add(lblBanco);

		JComboBox<PersonalItem> comboBox = new JComboBox<PersonalItem>();




		try {
			Dao<BancoDadosEntity, Integer> bancoDadosDao= DaoManager.createDao(SqliteHelper.connectionSource, BancoDadosEntity.class);

			List<BancoDadosEntity> listBancoDados = bancoDadosDao.queryForAll();

			PersonalItem item = new PersonalItem();
			item.setName("Selecione...");
			item.setValue(null);

			comboBox.addItem(item);

			for (int i = 0; i < listBancoDados.size(); i++) {

				item = new PersonalItem();
				item.setName(listBancoDados.get(i).getNome() + " (" + listBancoDados.get(i).getNameBd() + ")");				
				item.setValue(listBancoDados.get(i));

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
					if(bancoDados != null){
						updateGrid(bancoDados);
					}
				} else {
					DefaultTableModel dtm = new DefaultTableModel();
					table.setModel(dtm);
				}
			}

		});

		comboBox.setBounds(55, 134, 598, 30);
		getContentPane().add(comboBox);

		table = new JTable();
		table.setBounds(0, 0, 500, 300);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 244, 643, 269);
		scrollPane.setViewportView(table);
		getContentPane().add(scrollPane);

		JButton btnGerar = new JButton("     Gerar API");
		btnGerar.setBackground(new Color(255, 255, 255));

		btnGerar.setIcon(ResourcesImages.analysis());

		btnGerar.addActionListener(new ActionListener() {
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

					DatabaseUtils databaseUtils = new DatabaseUtils(bancoDados);

					AddAPIController addAPIController = new AddAPIController(consoleLog,

							inputProject.getText(), txtPackage.getText(),

							txtEntity.getText(),

							chkFilter.isSelected(), txtFilter.getText(),

							chkResource.isSelected(), txtResource.getText(),

							chkRepository.isSelected(), txtRepository.getText(),

							chkServices.isSelected(), txtServices.getText(),

							chkRepositoryImpl.isSelected(), txtRepositoryImpl.getText(), listComponents, databaseUtils);
					addAPIController.run();

				}
			}
		});
		btnGerar.setBounds(238, 530, 198, 36);
		getContentPane().add(btnGerar);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 663, 60);
		getContentPane().add(panel);

		inputProject = new TextField();

		inputProject.setPreferredSize(new Dimension(10, 0));
		inputProject.setMaximumSize(new Dimension(10, 10));
		inputProject.setColumns(70);
		inputProject.setBounds(190, 17, 463, 22);
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
		btnWorkspace.setBounds(10, 11, 174, 34);

		btnWorkspace.setIcon(ResourcesImages.report());

		panel.add(btnWorkspace);

		txtPackage = new JTextField();
		txtPackage.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (inputProject.getText() != null) {
					buscaPackage(inputProject.getText());
				}
			}
		});
		txtPackage.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				JTextField textField = (JTextField) e.getSource();
				String text = textField.getText();
				setText(text);
			}
		});
		txtPackage.setBounds(10, 87, 643, 36);
		getContentPane().add(txtPackage);
		txtPackage.setColumns(10);

		JLabel lblPacote = new JLabel("Package");
		lblPacote.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPacote.setBounds(10, 71, 198, 14);
		getContentPane().add(lblPacote);

		JLabel lblTabelas = new JLabel("Tabelas (Atenção ao buscar uma tabela ele cancela as anteriores marcas)");
		lblTabelas.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTabelas.setBounds(10, 184, 643, 14);
		getContentPane().add(lblTabelas);

		JPanel panel_1 = new JPanel();
		TitledBorder border = BorderFactory.createTitledBorder("Packages");
		border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD));
		panel_1.setBorder(border);
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(663, 11, 546, 564);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		chkResource = new JCheckBox("Resource");
		chkResource.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean control;

				if (e.getStateChange() == 1) {

					control = true;

				} else {

					control = false;

				}

				Component[] components = panel_1.getComponents();

				for (int i = 0; i < components.length; i++) {

					if (components[i].getClass().getName().toString().equals("javax.swing.JTextField")) {
						if (components[i].getName().equals("txtResource")) {
							components[i].setEnabled(control);
						}
					}
				}
			}
		});

		chkRepositoryImpl = new JCheckBox("Repository Implements");
		chkRepositoryImpl.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean control;

				if (e.getStateChange() == 1) {

					control = true;

				} else {

					control = false;

				}

				Component[] components = panel_1.getComponents();

				for (int i = 0; i < components.length; i++) {

					if (components[i].getClass().getName().toString().equals("javax.swing.JTextField")) {
						if (components[i].getName().equals("txtRepositoryImpl")) {
							components[i].setEnabled(control);
						}
					}
				}
			}
		});
		chkRepositoryImpl.setSelected(true);
		chkRepositoryImpl.setBackground(Color.WHITE);
		chkRepositoryImpl.setBounds(112, 527, 153, 23);
		panel_1.add(chkRepositoryImpl);
		chkResource.setSelected(true);
		chkResource.setBackground(Color.WHITE);
		chkResource.setBounds(112, 501, 97, 23);
		panel_1.add(chkResource);

		chkRepository = new JCheckBox("Repository");
		chkRepository.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean control;

				if (e.getStateChange() == 1) {

					control = true;

				} else {

					control = false;

				}

				Component[] components = panel_1.getComponents();

				for (int i = 0; i < components.length; i++) {

					if (components[i].getClass().getName().toString().equals("javax.swing.JTextField")) {
						if (components[i].getName().equals("txtRepository")) {
							components[i].setEnabled(control);
						}
					}
				}
			}
		});
		chkRepository.setSelected(true);
		chkRepository.setBackground(Color.WHITE);
		chkRepository.setBounds(211, 501, 97, 23);
		panel_1.add(chkRepository);

		chkServices = new JCheckBox("Services");
		chkServices.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean control;

				if (e.getStateChange() == 1) {

					control = true;

				} else {

					control = false;

				}

				Component[] components = panel_1.getComponents();

				for (int i = 0; i < components.length; i++) {

					if (components[i].getClass().getName().toString().equals("javax.swing.JTextField")) {
						if (components[i].getName().equals("txtServices")) {
							components[i].setEnabled(control);
						}
					}
				}
			}
		});
		chkServices.setSelected(true);
		chkServices.setBackground(Color.WHITE);
		chkServices.setBounds(30, 527, 97, 23);
		panel_1.add(chkServices);

		chkFilter = new JCheckBox("Filter");
		chkFilter.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean control;

				if (e.getStateChange() == 1) {

					control = true;

				} else {

					control = false;

				}

				Component[] components = panel_1.getComponents();

				for (int i = 0; i < components.length; i++) {

					if (components[i].getClass().getName().toString().equals("javax.swing.JTextField")) {
						if (components[i].getName().equals("txtFilter")) {
							components[i].setEnabled(control);
						}
					}
				}
			}
		});

		chkFilter.setSelected(true);
		chkFilter.setBackground(Color.WHITE);
		chkFilter.setBounds(30, 501, 80, 23);
		panel_1.add(chkFilter);

		txtEntity = new JTextField();
		txtEntity.setName("txtEntity");
		txtEntity.setBounds(30, 55, 490, 36);
		panel_1.add(txtEntity);
		txtEntity.setColumns(10);

		txtFilter = new JTextField();
		txtFilter.setName("txtFilter");
		txtFilter.setColumns(10);
		txtFilter.setBounds(30, 127, 490, 36);
		panel_1.add(txtFilter);

		txtResource = new JTextField();
		txtResource.setName("txtResource");
		txtResource.setColumns(10);
		txtResource.setBounds(30, 198, 490, 36);
		panel_1.add(txtResource);

		txtRepository = new JTextField();
		txtRepository.setName("txtRepository");
		txtRepository.setColumns(10);
		txtRepository.setBounds(30, 271, 490, 36);
		panel_1.add(txtRepository);

		txtServices = new JTextField();
		txtServices.setName("txtServices");
		txtServices.setColumns(10);
		txtServices.setBounds(30, 344, 490, 36);
		panel_1.add(txtServices);

		txtRepositoryImpl = new JTextField();
		txtRepositoryImpl.setName("txtRepositoryImpl");
		txtRepositoryImpl.setColumns(10);
		txtRepositoryImpl.setBounds(30, 420, 490, 36);
		panel_1.add(txtRepositoryImpl);

		JLabel lblNewLabel = new JLabel("Entity");
		lblNewLabel.setBounds(20, 28, 198, 14);
		panel_1.add(lblNewLabel);

		JLabel lblFilter = new JLabel("Filter");
		lblFilter.setBounds(20, 102, 198, 14);
		panel_1.add(lblFilter);

		JLabel lblResource = new JLabel("Resource");
		lblResource.setBounds(20, 174, 198, 14);
		panel_1.add(lblResource);

		JLabel lblNewLabel_1 = new JLabel("Repository");
		lblNewLabel_1.setBounds(20, 246, 198, 14);
		panel_1.add(lblNewLabel_1);

		JLabel lblServices = new JLabel("Services");
		lblServices.setBounds(20, 318, 198, 14);
		panel_1.add(lblServices);

		JLabel lblRepositoryImplements = new JLabel("Repository Implements");
		lblRepositoryImplements.setBounds(20, 395, 198, 14);
		panel_1.add(lblRepositoryImplements);

		lblNewLabel_2 = new JLabel("Gerar");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_2.setBounds(20, 470, 198, 14);
		panel_1.add(lblNewLabel_2);

		textField_1 = new JTextField();
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				System.err.println("RELEASED");

				JTextField textField = (JTextField) e.getSource();
				String text = textField.getText();

				ListTableController controller = new ListTableController(bancoDados, table, true, consoleLog, text);
				controller.run();

			}
		});
		textField_1.setToolTipText("Buscar Tabelas");
		textField_1.setBounds(10, 209, 643, 30);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);

	}

	private void updateGrid(BancoDadosEntity bancoDadosEntity) {
		System.err.println("Banco: "+bancoDadosEntity.getNome());
		System.err.println("Banco: "+bancoDadosEntity.getSenha());
		ListTableController controller = new ListTableController(bancoDadosEntity, table, true, consoleLog, "");
		controller.run();
	}

	private void buscaPackage(String url) {
		if (url != null && url.length() > 0) {

			url = url + "/src/main/java";
			File directory = new File(url);
			listar(directory);

			String replace = temp.replace(directory.getAbsolutePath(), "").replace("/", ".");
			replace = replace.substring(1, replace.length());

			if (replace.contains(".utils")) {
				if(System.getProperty("os.name").equals("Linux")) {
					replace = replace.replace(".utils", "");
				}else {
					replace = replace.replace(".cors", "");
				}
			}

			txtPackage.setText(replace);
			setText(replace);
		}
	}

	public void listar(File directory) {
		if (directory.isDirectory()) {
			// System.out.println(directory.getPath());
			temp = directory.getPath();
			String[] subDirectory = directory.list();
			if (subDirectory != null) {
				for (String dir : subDirectory) {
					listar(new File(directory + File.separator + dir));
				}
			}
		}
	}

	public void setText(String text) {
		txtEntity.setText(text + ".entity");
		txtFilter.setText(text + ".filter");
		txtResource.setText(text + ".resource");
		txtRepository.setText(text + ".repository");
		txtServices.setText(text + ".service");
		txtRepository.setText(text + ".repository");
		txtRepositoryImpl.setText(text + ".repository.{{EntityFolder}}");
	}
}
