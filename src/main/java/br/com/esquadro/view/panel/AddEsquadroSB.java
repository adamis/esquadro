package br.com.esquadro.view.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JTextField;

import br.com.esquadro.controler.AddEsquadroSBController;
import br.com.esquadro.model.BancoDados;
import br.com.esquadro.model.DependenciasPOM.DEPEND;
import br.com.esquadro.resources.ResourcesImages;
import br.com.esquadro.util.Conexao;
import br.com.esquadro.util.HintTextField;
import br.com.esquadro.util.PersonalItem;
import br.com.esquadro.util.SqliteHelper;
import br.com.esquadro.view.ConsoleLog;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class AddEsquadroSB extends JInternalFrame {

	private static final long serialVersionUID = 4593163889948851947L;
	private TextField inputProject;
	private BancoDados bancoDados = null;

	private ConsoleLog consoleLog;
	private JTextField txtPackages;
	private JPanel pPom;
	private JCheckBox chkConfigBanco;
	private JCheckBox chckbxAlterarPomxml;
	private JCheckBox chkLombok;
	private JCheckBox chkCommons;
	private JCheckBox chkSpringSecurity;
	private JCheckBox chkH2;
	private JCheckBox chkFlyway;
	private JCheckBox chkJPA;
	private JCheckBox chkMysql;
	private JCheckBox chkOracle;
	private JCheckBox chkEhCache;
	private JCheckBox chkSwagger;
	private JCheckBox chckbxSpringEmail;
	private JCheckBox chckbxModelMapper;
	private JCheckBox chckbxConfiguration;
	private JCheckBox chckbxMetamodelGen;

	private boolean lombok = true;
	private boolean commons = true;
	private boolean springSecurity = true;
	private boolean h2 = true;
	private boolean flyway = true;
	private boolean jpa = true;
	private boolean mysql = true;
	private boolean oracle = true;
	private boolean ehCache = true;
	private boolean swagger = true;
	private boolean springEmail = true;
	private boolean modelMapper = true;
	private boolean configuration = true;
	private boolean metamodelGen = true;
	private boolean devTools = true;
	private String temp = "";
	
	private JCheckBox chckbxDevtools;

	public void setUrl(TextField inputProject) {
		this.inputProject = inputProject;
	}

	public String getUrl() {
		return this.inputProject.getText();
	}

	/**
	 * Create the panel.
	 */
	public AddEsquadroSB(final ConsoleLog consoleLog) {
		setFrameIcon(ResourcesImages.springBoot());
		setTitle("Adicionar Padrão Esquadro Spring Boot");
		this.consoleLog = consoleLog;

		getContentPane().setBackground(new Color(255, 255, 255));
		setMaximizable(true);
		setClosable(true);

		getContentPane().setLayout(null);

		setPreferredSize(new Dimension(668, 400));
		setSize(new Dimension(679, 604));
		setAutoscrolls(true);

		JComboBox<PersonalItem> comboBox = new JComboBox<PersonalItem>();
		comboBox.setEnabled(false);

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
					
					if(bancoDados.getTipo().equalsIgnoreCase("mysql")) {
						chkMysql.setSelected(true);
						chkOracle.setSelected(false);
					}else {
						chkMysql.setSelected(false);
						chkOracle.setSelected(true);
					}
					
				} else {
					bancoDados = null;
				}
				
				
			}

		});

		comboBox.setBounds(10, 175, 643, 30);
		getContentPane().add(comboBox);

		JButton btnNewButton = new JButton("     Gerar Componentes");
		btnNewButton.setBackground(new Color(255, 255, 255));

		btnNewButton.setIcon(ResourcesImages.analysis());

		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg) {
				consoleLog.setVisible(true);
				consoleLog.moveToFront();

				List<DEPEND> listDepend = new ArrayList<DEPEND>();

				if (chckbxAlterarPomxml.isSelected()) {

					if (lombok) {
						listDepend.add(DEPEND.LOMBOK);
					}

					if (commons) {
						listDepend.add(DEPEND.COMMONS);
					}

					if (flyway) {
						listDepend.add(DEPEND.FLYWAY);
					}

					if (mysql) {
						listDepend.add(DEPEND.MYSQL);
					}

					if (ehCache) {
						listDepend.add(DEPEND.EHCACHE);
					}

					if (springSecurity) {
						listDepend.add(DEPEND.SPRING_SECURITY);
						listDepend.add(DEPEND.SPRING_SECURITY_TESTE);
					}

					if (h2) {
						listDepend.add(DEPEND.H2);
					}

					if (jpa) {
						listDepend.add(DEPEND.JPA);
					}

					if (oracle) {
						listDepend.add(DEPEND.ORACLE);
					}

					if (swagger) {
						listDepend.add(DEPEND.SWAGGER);
						listDepend.add(DEPEND.SWAGGER_UI);
					}

					if (springEmail) {
						listDepend.add(DEPEND.SPRING_EMAIL);
					}

					if (modelMapper) {
						listDepend.add(DEPEND.MODEL_MAPPER);
					}

					if (configuration) {
						listDepend.add(DEPEND.CONFIGURATION);
					}

					if (metamodelGen) {
						listDepend.add(DEPEND.METAMODEL_GEN);
					}

					if (devTools) {
						listDepend.add(DEPEND.DEV_TOOLS);
					}
				}

				AddEsquadroSBController addEsquadroSBController = new AddEsquadroSBController(bancoDados, consoleLog,
						inputProject.getText(), txtPackages.getText(), chckbxAlterarPomxml.isSelected(), listDepend,
						chkConfigBanco.isSelected());

				new Thread(addEsquadroSBController).run();

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
		inputProject.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(inputProject.getText() != null) {
					buscaPackage(inputProject.getText());
				}
			}


		});
		inputProject.setPreferredSize(new Dimension(10, 0));
		inputProject.setMaximumSize(new Dimension(10, 10));
		inputProject.setColumns(70);
		inputProject.setBounds(160, 22, 493, 30);
		panel.add(inputProject);

		JButton btnWorkspace = new JButton("Projeto");
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
		btnWorkspace.setBounds(10, 20, 144, 34);

		btnWorkspace.setIcon(ResourcesImages.report());

		panel.add(btnWorkspace);

		txtPackages = new JTextField(" br.com.project");
		txtPackages.setBounds(10, 101, 643, 30);
		getContentPane().add(txtPackages);
		txtPackages.setColumns(10);

		JLabel lblPackage = new JLabel("Package");
		lblPackage.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPackage.setBounds(10, 83, 241, 14);
		getContentPane().add(lblPackage);

		chkConfigBanco = new JCheckBox("Configurar Banco");
		chkConfigBanco.setFont(new Font("Tahoma", Font.BOLD, 11));
		chkConfigBanco.setBackground(Color.WHITE);
		chkConfigBanco.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					comboBox.setEnabled(true);
				} else {
					comboBox.setEnabled(false);
				}
			}
		});
		chkConfigBanco.setBounds(10, 145, 166, 23);
		getContentPane().add(chkConfigBanco);

		chckbxAlterarPomxml = new JCheckBox("Alterar POM.xml");
		chckbxAlterarPomxml.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					// pPom.setEnabled(true);
					setPanelEnabled(pPom, true);
				} else {
					setPanelEnabled(pPom, false);
				}
			}
		});
		chckbxAlterarPomxml.setFont(new Font("Tahoma", Font.BOLD, 11));
		chckbxAlterarPomxml.setBackground(Color.WHITE);
		chckbxAlterarPomxml.setBounds(10, 228, 166, 23);
		getContentPane().add(chckbxAlterarPomxml);

		pPom = new JPanel();
		pPom.setEnabled(false);
		pPom.setBounds(10, 258, 643, 244);
		getContentPane().add(pPom);
		pPom.setLayout(null);

		chkLombok = new JCheckBox("LOMBOK");
		chkLombok.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO
				if (e.getStateChange() == 1) {
					lombok = true;
				} else {
					lombok = false;
				}

			}
		});
		chkLombok.setSelected(true);
		chkLombok.setBounds(6, 7, 97, 23);
		pPom.add(chkLombok);

		chkCommons = new JCheckBox("COMMONS");
		chkCommons.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					commons = true;
				} else {
					commons = false;
				}
			}
		});
		chkCommons.setSelected(true);
		chkCommons.setBounds(105, 7, 97, 23);
		pPom.add(chkCommons);

		chkSpringSecurity = new JCheckBox("Spring Security");
		chkSpringSecurity.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					springSecurity = true;
				} else {
					springSecurity = false;
				}
			}
		});
		chkSpringSecurity.setBounds(399, 7, 196, 23);
		pPom.add(chkSpringSecurity);

		chkH2 = new JCheckBox("H2");
		chkH2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					h2 = true;
				} else {
					h2 = false;
				}
			}
		});
		chkH2.setToolTipText("");
		chkH2.setBounds(105, 33, 97, 23);
		pPom.add(chkH2);

		chkFlyway = new JCheckBox("FLYWAY");
		chkFlyway.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					flyway = true;
				} else {
					flyway = false;
				}
			}
		});
		chkFlyway.setBounds(201, 7, 97, 23);
		pPom.add(chkFlyway);

		chkJPA = new JCheckBox("JPA");
		chkJPA.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					jpa = true;
				} else {
					jpa = false;
				}
			}
		});
		chkJPA.setSelected(true);
		chkJPA.setBounds(201, 33, 97, 23);
		pPom.add(chkJPA);

		chkMysql = new JCheckBox("MYSQL");
		chkMysql.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					mysql = true;
				} else {
					mysql = false;
				}
			}
		});
		chkMysql.setBounds(300, 7, 97, 23);
		pPom.add(chkMysql);

		chkOracle = new JCheckBox("ORACLE");
		chkOracle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					oracle = true;
				} else {
					oracle = false;
				}
			}
		});
		chkOracle.setBounds(300, 33, 97, 23);
		pPom.add(chkOracle);

		chkEhCache = new JCheckBox("EhCache");
		chkEhCache.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					ehCache = true;
				} else {
					ehCache = false;
				}
			}
		});
		chkEhCache.setBounds(105, 59, 97, 23);
		pPom.add(chkEhCache);

		chkSwagger = new JCheckBox("Swagger");
		chkSwagger.setSelected(true);
		chkSwagger.setBounds(6, 59, 97, 23);
		chkSwagger.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					swagger = true;
				} else {
					swagger = false;
				}
			}
		});
		pPom.add(chkSwagger);

		chckbxSpringEmail = new JCheckBox("Spring Email");
		chckbxSpringEmail.setSelected(true);
		chckbxSpringEmail.setBounds(6, 33, 97, 23);
		chckbxSpringEmail.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					springEmail = true;
				} else {
					springEmail = false;
				}
			}
		});

		pPom.add(chckbxSpringEmail);

		chckbxModelMapper = new JCheckBox("Model Mapper");
		chckbxModelMapper.setSelected(true);
		chckbxModelMapper.setBounds(399, 33, 196, 23);
		chckbxModelMapper.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					modelMapper = true;
				} else {
					modelMapper = false;
				}
			}
		});
		pPom.add(chckbxModelMapper);

		chckbxConfiguration = new JCheckBox("Configuration");
		chckbxConfiguration.setSelected(true);
		chckbxConfiguration.setBounds(201, 59, 97, 23);
		chckbxConfiguration.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					configuration = true;
				} else {
					configuration = false;
				}
			}
		});
		pPom.add(chckbxConfiguration);

		chckbxMetamodelGen = new JCheckBox("Metamodel Gen");
		chckbxMetamodelGen.setSelected(true);
		chckbxMetamodelGen.setBounds(399, 59, 196, 23);
		chckbxMetamodelGen.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					metamodelGen = true;
				} else {
					metamodelGen = false;
				}
			}
		});
		pPom.add(chckbxMetamodelGen);

		chckbxDevtools = new JCheckBox("DevTools");
		chckbxDevtools.setSelected(true);
		chckbxDevtools.setBounds(300, 59, 97, 23);
		chckbxDevtools.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					devTools = true;
				} else {
					devTools = false;
				}
			}
		});
		pPom.add(chckbxDevtools);

		setPanelEnabled(pPom, false);
	}

	void setPanelEnabled(JPanel panel, Boolean isEnabled) {
		panel.setEnabled(isEnabled);

		Component[] components = panel.getComponents();

		for (Component component : components) {
			if (component instanceof JPanel) {
				setPanelEnabled((JPanel) component, isEnabled);
			}
			component.setEnabled(isEnabled);
		}
	}

	
	
	private void buscaPackage(String url) {
		if(url != null && url.length() > 0) {
			
			url = url+"/src/main/java";
			File directory = new File(url);
			listar(directory);
			
			String replace = temp.replace(directory.getAbsolutePath(),"").replace("\\", ".");
			replace = replace.substring(1, replace.length());
			
			System.err.println(""+replace);		
			txtPackages.setText(replace);
			
		}
	}
	
	 public void listar(File directory) {
	        if(directory.isDirectory()) {
	            //System.out.println(directory.getPath());
	            temp = directory.getPath();
	            String[] subDirectory = directory.list();
	            if(subDirectory != null) {
	                for(String dir : subDirectory){
	                    listar(new File(directory + File.separator  + dir));
	                }
	            }
	        }
	    }
	
}
