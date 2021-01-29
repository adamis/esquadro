package br.com.esquadro.view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import br.com.esquadro.enums.DATABASETYPE;
import br.com.esquadro.resources.ResourcesImages;
import br.com.esquadro.sqlite.controler.DeleteDatabasesController;
import br.com.esquadro.sqlite.controler.InsertDatabasesController;
import br.com.esquadro.sqlite.controler.ListTablesController;
import br.com.esquadro.sqlite.controler.UpdateDatabasesController;
import br.com.esquadro.sqlite.entity.BancoDadosEntity;
import br.com.esquadro.view.ConsoleLog;

public class BancoDados extends JInternalFrame {

	private static final long serialVersionUID = 4593163889948851947L;
	private TextField textFieldUrl;
	private ConsoleLog consoleLog;
	private JTable tblTabelas;
	private JTextField txtNome;
	private JTextField txtPorta;
	private JTextField txtUsuario;
	private JTextField txtNomeBD;
	private JTextField txtSchema;
	private JComboBox<String> cbTipo;
	private Integer bdSendoAlterado;
	private JPasswordField txtSenha;
	private JTextField txtCharset;
	private JTextField txtIp;
	private JCheckBox ckboxPsw;
	private JButton btnAlterar;
	private JButton btnCadastrarBd;

	public JPanel panel;
	public JScrollPane scrollPane;

	public void setUrl(TextField textFieldUrl) {
		this.textFieldUrl = textFieldUrl;
	}

	public String getUrl() {
		return this.textFieldUrl.getText();
	}

	/**
	 * Create the panel.
	 * 
	 * @throws IOException
	 */
	public BancoDados(ConsoleLog consoleLog) throws IOException {
		setFrameIcon(ResourcesImages.bancoIcon());
		setTitle("Banco de Dados");
		setIconifiable(true);
		setOpaque(true);
		this.consoleLog = consoleLog;
		setClosable(true);
		getContentPane().setBackground(new Color(255, 255, 255));

		getContentPane().setLayout(null);

		setPreferredSize(new Dimension(668, 400));
		setSize(new Dimension(725, 587));
		setAutoscrolls(true);

		this.setBackground(Color.WHITE);

		panel = new JPanel();
		panel.setBorder(
				new TitledBorder(null, "Informa\u00E7\u00F5es", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setVisible(false);
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(15, 48, 675, 481);
		getContentPane().add(panel);

		panel.setLayout(null);

		cbTipo = new JComboBox<String>();
		cbTipo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {

				if ((e.getItem() + "").equals("MySQL")) {
					txtPorta.setText("3306");
					txtCharset.setText("utf_8");
					txtNomeBD.setText("");

				} else if ((e.getItem() + "").equals("Oracle")) {
					txtPorta.setText("1521");
					txtNomeBD.setText("apolo");
					txtCharset.setText("utf_8");

				} else if ((e.getItem() + "").equals("Indefinido")) {
					if (txtPorta != null) {
						txtPorta.setText(" ");
					}
					if (txtCharset != null) {
						txtCharset.setText(" ");
					}
					if (txtNomeBD != null) {
						txtNomeBD.setText("");
					}

				}
			}
		});
		cbTipo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cbTipo.setBackground(Color.WHITE);
		cbTipo.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
		cbTipo.setBounds(65, 58, 545, 37);
		cbTipo.addItem("Indefinido");
		// cbTipo.addItem("Firebird");
		cbTipo.addItem("MySQL");
		cbTipo.addItem("Oracle");

		ckboxPsw = new JCheckBox("");
		ckboxPsw.setSelected(false);
		ckboxPsw.setBackground(Color.WHITE);
		ckboxPsw.setForeground(Color.WHITE);
		ckboxPsw.setBounds(591, 301, 28, 23);

		ckboxPsw.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					txtSenha.setEchoChar((char) 0);
				} else {
					txtSenha.setEchoChar('*');
				}
			}
		});

		panel.add(ckboxPsw);
		// cbTipo.addItem("MSServer");
		// cbTipo.addItem("Postgree");
		// cbTipo.addItem("SQLite");
		panel.add(cbTipo);

		btnAlterar = new JButton("     Alterar");
		btnAlterar.setVisible(false);
		btnAlterar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				boolean valid = true;

				valid = checkValid(valid, !txtNome.getText().isEmpty());
				valid = checkValid(valid, !txtIp.getText().isEmpty());
				valid = checkValid(valid, !txtPorta.getText().isEmpty());
				valid = checkValid(valid, !txtUsuario.getText().isEmpty());
				valid = checkValid(valid, !txtCharset.getText().isEmpty());
				valid = checkValid(valid, !txtNomeBD.getText().isEmpty());
				valid = checkValid(valid, !txtSchema.getText().isEmpty());
				valid = checkValid(valid, !cbTipo.getSelectedItem().equals("Indefinido"));

				if (valid) {

					BancoDadosEntity bancoDadosEntity = getFormData();

					UpdateDatabasesController controller = new UpdateDatabasesController(BancoDados.this,
							bdSendoAlterado, bancoDadosEntity);
					controller.run();

				} else {
					JOptionPane.showMessageDialog(null, "Preencha todos os Campos!", "Atenção",
							JOptionPane.WARNING_MESSAGE);
				}

				// TODO

			}

			/**
			 * @return
			 */

		});

		btnAlterar.setBounds(65, 421, 240, 37);
		btnAlterar.setIcon(ResourcesImages.analysis());
		panel.add(btnAlterar);

		JLabel lblNome = new JLabel("<html>Nome de Identificação<font color='red'>*</font></html>");
		lblNome.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNome.setBounds(65, 119, 224, 14);
		panel.add(lblNome);

		txtNome = new JTextField();
		txtNome.setColumns(10);
		txtNome.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		txtNome.setBackground(Color.WHITE);
		txtNome.setBounds(65, 133, 545, 26);
		panel.add(txtNome);

		JLabel lblPorta = new JLabel("<html>Porta<font color='red'>*</font></html>");
		lblPorta.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblPorta.setBounds(370, 168, 48, 14);
		panel.add(lblPorta);

		txtPorta = new JTextField();
		txtPorta.setColumns(10);
		txtPorta.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		txtPorta.setBackground(Color.WHITE);
		txtPorta.setBounds(370, 182, 240, 26);
		panel.add(txtPorta);

		JLabel lblUsurio = new JLabel("<html>Usuário<font color='red'>*</font></html>");
		lblUsurio.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblUsurio.setBounds(65, 286, 48, 14);
		panel.add(lblUsurio);

		txtUsuario = new JTextField();
		txtUsuario.setColumns(10);
		txtUsuario.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		txtUsuario.setBackground(Color.WHITE);
		txtUsuario.setBounds(65, 300, 240, 26);
		panel.add(txtUsuario);

		JLabel lblNomeDoBanco = new JLabel("<html>Nome do Banco de Dados<font color='red'>*</font></html>");
		lblNomeDoBanco.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNomeDoBanco.setBounds(65, 354, 153, 14);
		panel.add(lblNomeDoBanco);

		txtNomeBD = new JTextField();
		txtNomeBD.setColumns(10);
		txtNomeBD.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		txtNomeBD.setBackground(Color.WHITE);
		txtNomeBD.setBounds(65, 368, 240, 26);
		panel.add(txtNomeBD);

		txtSchema = new JTextField();
		txtSchema.setColumns(10);
		txtSchema.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		txtSchema.setBackground(Color.WHITE);
		txtSchema.setBounds(370, 367, 240, 26);
		panel.add(txtSchema);

		JLabel lblSchema = new JLabel("<html>Schema<font color='red'>*</font></html>");
		lblSchema.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblSchema.setBounds(370, 353, 48, 14);
		panel.add(lblSchema);

		txtSenha = new JPasswordField();
		txtSenha.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
		txtSenha.setBackground(Color.WHITE);
		txtSenha.setBounds(370, 300, 240, 26);
		panel.add(txtSenha);

		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblSenha.setBounds(370, 286, 48, 14);
		panel.add(lblSenha);

		txtCharset = new JTextField();
		txtCharset.setColumns(10);
		txtCharset.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		txtCharset.setBackground(Color.WHITE);
		txtCharset.setBounds(65, 235, 240, 26);
		panel.add(txtCharset);

		JLabel lblCharset = new JLabel("<html>Charset<font color='red'>*</font></html>");
		lblCharset.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblCharset.setBounds(65, 221, 48, 14);
		panel.add(lblCharset);

		txtIp = new JTextField("");
		txtIp.setColumns(10);
		txtIp.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		txtIp.setBackground(Color.WHITE);
		txtIp.setBounds(65, 184, 240, 26);
		panel.add(txtIp);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 35, 685, 500);
		getContentPane().add(scrollPane);

		JLabel lblEndereoDeIp = new JLabel("<html>Endereço de IP<font color='red'>*</font></html>");
		lblEndereoDeIp.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblEndereoDeIp.setBounds(65, 170, 189, 14);
		panel.add(lblEndereoDeIp);

		btnCadastrarBd = new JButton("     Inserir");
		btnCadastrarBd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				boolean valid = true;

				valid = checkValid(valid, !txtNome.getText().isEmpty());
				valid = checkValid(valid, !txtIp.getText().isEmpty());
				valid = checkValid(valid, !txtPorta.getText().isEmpty());
				valid = checkValid(valid, !txtUsuario.getText().isEmpty());
				valid = checkValid(valid, !txtCharset.getText().isEmpty());
				valid = checkValid(valid, !txtNomeBD.getText().isEmpty());
				valid = checkValid(valid, !txtSchema.getText().isEmpty());
				valid = checkValid(valid, !cbTipo.getSelectedItem().equals("Indefinido"));

				if (valid) {

					BancoDadosEntity bancoDadosEntity = getFormData();

					InsertDatabasesController controller = new InsertDatabasesController(BancoDados.this,
							bancoDadosEntity);
					controller.run();

				} else {
					JOptionPane.showMessageDialog(null, "Preencha todos os Campos!", "Atenção",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		btnCadastrarBd.setIcon(ResourcesImages.analysis());
		btnCadastrarBd.setBounds(65, 421, 240, 37);
		panel.add(btnCadastrarBd);

		JButton btnCancelar = new JButton("     Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.setVisible(false);
				scrollPane.setVisible(true);
			}
		});
		btnCancelar.setIcon(ResourcesImages.cancelar());
		btnCancelar.setBounds(370, 421, 240, 37);
		panel.add(btnCancelar);

		JLabel lblTipo = new JLabel("<html>Tipo do Banco<font color='red'>*</font></html>");
		lblTipo.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblTipo.setBounds(65, 36, 141, 14);
		panel.add(lblTipo);

		Vector<String> colHdrs = new Vector<String>(10);
		colHdrs.addElement(new String("ID"));
		colHdrs.addElement(new String("Nome do Banco"));
		colHdrs.addElement(new String("Tipo"));

		DefaultTableModel model = new DefaultTableModel(null, colHdrs);

		tblTabelas = new JTable(model) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 612081287363465231L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblTabelas.setColumnSelectionAllowed(false);
		scrollPane.setViewportView(tblTabelas);
		tblTabelas.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));

		tblTabelas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				// Point point = mouseEvent.getPoint();
				// int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					callEdit();
				}
			}
		});

		JMenuBar menuBar = new JMenuBar();
		menuBar.setMargin(new Insets(0, 10, 0, 10));
		menuBar.setBackground(Color.WHITE);
		menuBar.setBounds(10, 0, 689, 21);
		getContentPane().add(menuBar);

		JMenuItem mntmNovo = new JMenuItem("Novo");
		mntmNovo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearText();
				panel.setVisible(true);
				scrollPane.setVisible(false);
				btnAlterar.setVisible(false);
				btnCadastrarBd.setVisible(true);
			}
		});
		mntmNovo.setPreferredSize(new Dimension(70, 22));
		mntmNovo.setIcon(ResourcesImages.addIcon());
		menuBar.add(mntmNovo);

		JMenuItem mntmAlterar = new JMenuItem("Alterar");
		mntmAlterar.setPreferredSize(new Dimension(70, 22));
		mntmAlterar.setIcon(ResourcesImages.editIcon());
		menuBar.add(mntmAlterar);
		mntmAlterar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				callEdit();
			}

		});

		JMenuItem mntmExcluir = new JMenuItem("Excluir");
		mntmExcluir.setPreferredSize(new Dimension(70, 22));
		mntmExcluir.setIcon(ResourcesImages.deleteIcon());
		menuBar.add(mntmExcluir);
		mntmExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (tblTabelas.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Selecione o registro a ser excluído!");
					panel.setVisible(false);
					scrollPane.setVisible(true);
				} else {
					int dialogResult = JOptionPane.showConfirmDialog(null,
							"Excluir as configurações de banco de dados é um caminho sem volta. \n Deseja prosseguir?",
							null, JOptionPane.YES_NO_OPTION);
					if (dialogResult == JOptionPane.YES_OPTION) {
						DeleteDatabasesController controller = new DeleteDatabasesController(BancoDados.this,
								Integer.valueOf(tblTabelas.getValueAt(tblTabelas.getSelectedRow(), 0).toString()),
								tblTabelas.getValueAt(tblTabelas.getSelectedRow(), 0).toString());
						controller.run();

					}

				}
			}
		});

		loadTable();
	}

	public void loadTable() {
		ListTablesController controller = new ListTablesController(tblTabelas, consoleLog);
		controller.run();
	}

	public void clearText() {
		txtNome.setText("");
		txtIp.setText("");
		txtPorta.setText("");
		txtUsuario.setText("");
		txtSenha.setText("");
		txtCharset.setText("");
		txtNomeBD.setText("");
		txtSchema.setText("");
		cbTipo.setSelectedIndex(0);
		ckboxPsw.setSelected(false);

	}

	private BancoDadosEntity getFormData() {
		BancoDadosEntity bancoDadosEntity = new BancoDadosEntity();
		bancoDadosEntity.setNome(txtNome.getText());
		bancoDadosEntity.setIp(txtIp.getText());
		bancoDadosEntity.setPorta(txtPorta.getText());
		bancoDadosEntity.setUsuario(txtUsuario.getText());
		bancoDadosEntity.setSenha(new String(txtSenha.getPassword()));
		bancoDadosEntity.setCharset(txtCharset.getText());
		bancoDadosEntity.setNameBd(txtNomeBD.getText());
		bancoDadosEntity.setSchema(txtSchema.getText());
		bancoDadosEntity.setTipo(DATABASETYPE.valueOf(cbTipo.getSelectedItem().toString().toUpperCase()));
		return bancoDadosEntity;
	}

	private void callEdit() {
		if (tblTabelas.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(null, "Selecione o registro a ser alterado!");
			panel.setVisible(false);
			scrollPane.setVisible(true);
		} else {
			try {
				panel.setVisible(true);
				scrollPane.setVisible(false);

				btnAlterar.setVisible(true);
				btnCadastrarBd.setVisible(false);
				UpdateDatabasesController controller = new UpdateDatabasesController();
				BancoDadosEntity dados = controller
						.findById(Integer.valueOf(tblTabelas.getValueAt(tblTabelas.getSelectedRow(), 0).toString()));
				panel.setVisible(true);
				scrollPane.setVisible(false);
				txtNome.setText(dados.getNome());
				txtIp.setText(dados.getIp());
				txtPorta.setText(dados.getPorta());
				txtUsuario.setText(dados.getUsuario());
				txtSenha.setText(dados.getSenha());
				txtCharset.setText(dados.getCharset());
				txtNomeBD.setText(dados.getNameBd());
				txtSchema.setText(dados.getSchema());

				for (int i = 0; i < cbTipo.getItemCount(); i++) {

					if (dados.getTipo().toString().toUpperCase().equals(cbTipo.getItemAt(i).toUpperCase())) {

						cbTipo.setSelectedIndex(i);

						System.err.println("i: " + i);

					}

				}

				bdSendoAlterado = dados.getId();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Atenção", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	// private int matchOption(String banco) {
	//
	// if (banco.equals("Firebird"))
	// return 1;
	// if (banco.equals("MYSQL"))
	// return 2;
	// if ((banco.equals("Oracle")) || (banco.equals("ORACLE")))
	// return 3;
	// if (banco.equals("MSServer"))
	// return 4;
	// if (banco.equals("Postgree"))
	// return 5;
	// if (banco.equals("SQLite"))
	// return 6;
	// return 0;
	// }
	private boolean checkValid(boolean valid, boolean condition) {

		if (valid) {
			if (!condition) {
				valid = condition;
			}
		}

		return valid;
	}
}
