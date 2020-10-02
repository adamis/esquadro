package br.com.esquadro.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import br.com.esquadro.resources.ResourcesImages;
import br.com.esquadro.util.Statics;
import br.com.esquadro.view.panel.AddAPI;
import br.com.esquadro.view.panel.AddEsquadroSB;
import br.com.esquadro.view.panel.AutomaticDB;
import br.com.esquadro.view.panel.BancoDados;
import br.com.esquadro.view.panel.NovaDiretiva;
import br.com.esquadro.view.panel.NovaInterface;
import br.com.esquadro.view.panel.NovoClass;
import br.com.esquadro.view.panel.NovoComponent;
import br.com.esquadro.view.panel.NovoEnum;
import br.com.esquadro.view.panel.NovoGuard;
import br.com.esquadro.view.panel.NovoModulo;
import br.com.esquadro.view.panel.NovoPipe;
import br.com.esquadro.view.panel.NovoProjeto;
import br.com.esquadro.view.panel.NovoService;

public class Menu extends JFrame {

	private static final long serialVersionUID = -7453321014891785234L;
	private JPanel contentPane;
	private JTree fileTree;
	private JDesktopPane conteudo;
	private ConsoleLog consoleLog;
	private int height = 500;
	private int width = 500;
	private JLabel lblLogo;
	private JLabel dialogAviso;
	private JLabel lblAsk;
	private JLabel lblCopyart;

	public void visible(boolean control) {
		this.setVisible(control);
	}

	/**
	 * Create the frame.
	 */
	public Menu() {

		setTitle("" + Statics.NAME_SYS + " v" + Statics.VERSION);

		try {
			setDefaultLookAndFeelDecorated(true);
			this.setIconImage(ResourcesImages.icon2());
		} catch (Exception whoJackedMyIcon) {
			System.out.println("Could not load program icon.");
		}

		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent e) {
				height = getHeight();
				width = getWidth();

				if (conteudo != null) {
					conteudo.setBounds(1, 0, width - 5, height - 63);
					lblLogo.setBounds(10, 11, width, height - 71);
					dialogAviso.setBounds(width - 250, 52, 190, 114);
					lblAsk.setBounds(width - 90, 2, 52, 57);
					lblCopyart.setBounds((width / 2) - 80, height - 100, 148, 14);
					conteudo.repaint();
				}

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		height = screenSize.height;
		width = screenSize.width;

		setBounds(100, 100, width - 150, height - 100);
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.BLUE);
		setJMenuBar(menuBar);
		JMenu menuArquivo = new JMenu("Arquivo");
		menuArquivo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuArquivo.setIcon(ResourcesImages.coding());
		menuBar.add(menuArquivo);

		JMenuItem mntmSair = new JMenuItem("Sair");
		mntmSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		JMenuItem mntmConsole = new JMenuItem("Console");
		mntmConsole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (consoleLog.isClosed()) {
					// System.err.println("true");
					consoleLog = new ConsoleLog();
					consoleLog.setVisible(true);
					consoleLog.moveToFront();
					conteudo.revalidate();
					conteudo.repaint();

				} else {
					// System.err.println("false");
					consoleLog.setVisible(true);
					consoleLog.moveToFront();
				}
			}
		});
		menuArquivo.add(mntmConsole);
		mntmSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		menuArquivo.add(mntmSair);
		
		JMenu menu_12_1 = new JMenu("    ");
		menu_12_1.setFocusable(false);
		menu_12_1.setEnabled(false);
		menuBar.add(menu_12_1);
		
		JMenu mnNewMenu = new JMenu("Gerenciar");
		mnNewMenu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnNewMenu.setIcon(ResourcesImages.database2());
		menuBar.add(mnNewMenu);
		
				JMenuItem mntmNewMenuItem_1 = new JMenuItem("Banco de Dados");
				mntmNewMenuItem_1.setIcon(null);
				mnNewMenu.add(mntmNewMenuItem_1);
				mntmNewMenuItem_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.ALT_MASK));
				mntmNewMenuItem_1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						BancoDados bancoDados;
						try {

							bancoDados = new BancoDados(consoleLog);
							bancoDados.setVisible(true);
							conteudo.add(bancoDados);
							conteudo.revalidate();
							conteudo.repaint();
							bancoDados.moveToFront();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});

		JMenu menu_12 = new JMenu("    ");
		menu_12.setFocusable(false);
		menu_12.setEnabled(false);
		menuBar.add(menu_12);

		JMenu spBoot = new JMenu("Spring");
		spBoot.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		spBoot.setIcon(ResourcesImages.springBoot());
		menuBar.add(spBoot);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Add EsquadroSB");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// ADICIONAR PADRAO DE PROJETO
				AddEsquadroSB addEsquadroSB = new AddEsquadroSB(consoleLog);
				addEsquadroSB.setVisible(true);
				conteudo.add(addEsquadroSB);
				conteudo.revalidate();
				conteudo.repaint();
				addEsquadroSB.moveToFront();

			}
		});
		spBoot.add(mntmNewMenuItem_2);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Add API");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// GERAR API
				AddAPI addAPI = new AddAPI(consoleLog);
				addAPI.setVisible(true);
				conteudo.add(addAPI);
				conteudo.revalidate();
				conteudo.repaint();
				addAPI.moveToFront();
			}
		});
		spBoot.add(mntmNewMenuItem_3);
		JMenu menu_1_1 = new JMenu("    ");
		menu_1_1.setFocusable(false);
		menu_1_1.setEnabled(false);
		menuBar.add(menu_1_1);

		JMenu mnAutomatic = new JMenu("Angular");
		mnAutomatic.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnAutomatic.setIcon(ResourcesImages.angularMini());
		menuBar.add(mnAutomatic);

		JMenuItem mntmComponentDb = new JMenuItem("Components DB");
		mntmComponentDb.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
		mntmComponentDb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				AutomaticDB automaticDB = new AutomaticDB(consoleLog);
				automaticDB.setVisible(true);
				conteudo.add(automaticDB);
				conteudo.revalidate();
				conteudo.repaint();
				automaticDB.moveToFront();

			}
		});

		JMenuItem mntmNovoProjeto = new JMenuItem("Novo Projeto");
		mnAutomatic.add(mntmNovoProjeto);
		mntmNovoProjeto.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mntmNovoProjeto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NovoProjeto novoProjeto = new NovoProjeto(consoleLog);
				novoProjeto.setVisible(true);
				conteudo.add(novoProjeto);
				conteudo.revalidate();
				conteudo.repaint();
				novoProjeto.moveToFront();
			}
		});
		mnAutomatic.add(mntmComponentDb);

		JMenu menu_1 = new JMenu("    ");
		menu_1.setFocusable(false);
		menu_1.setEnabled(false);
		menuBar.add(menu_1);

		JMenu mnGerar = new JMenu("Gerar");
		mnGerar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnGerar.setIcon(ResourcesImages.editing());
		menuBar.add(mnGerar);

		JMenuItem mntmNovoComponent = new JMenuItem("Component");
		mntmNovoComponent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmNovoComponent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				NovoComponent novoComponent = new NovoComponent(consoleLog);
				novoComponent.setVisible(true);
				conteudo.add(novoComponent);
				conteudo.revalidate();
				conteudo.repaint();
				novoComponent.moveToFront();
			}
		});
		mnGerar.add(mntmNovoComponent);

		JMenuItem mntmNovaDiretiva = new JMenuItem("Diretiva");
		mntmNovaDiretiva.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmNovaDiretiva.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NovaDiretiva novaDiretiva = new NovaDiretiva(consoleLog);
				novaDiretiva.setVisible(true);
				conteudo.add(novaDiretiva);
				conteudo.revalidate();
				conteudo.repaint();
				novaDiretiva.moveToFront();
			}
		});
		mnGerar.add(mntmNovaDiretiva);

		JMenuItem mntmGuard = new JMenuItem("Guard");
		mntmGuard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmGuard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				NovoGuard novoGuard = new NovoGuard(consoleLog);
				novoGuard.setVisible(true);
				conteudo.add(novoGuard);
				conteudo.revalidate();
				conteudo.repaint();
				novoGuard.moveToFront();
			}
		});
		mnGerar.add(mntmGuard);

		JMenuItem mntmModule = new JMenuItem("Module");
		mntmModule.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmModule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				NovoModulo novoModulo = new NovoModulo(consoleLog);
				novoModulo.setVisible(true);
				conteudo.add(novoModulo);
				conteudo.revalidate();
				conteudo.repaint();
				novoModulo.moveToFront();
			}
		});
		mnGerar.add(mntmModule);

		JMenuItem mntmPipe = new JMenuItem("Pipe");
		mntmPipe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmPipe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				NovoPipe novoPipe = new NovoPipe(consoleLog);
				novoPipe.setVisible(true);
				conteudo.add(novoPipe);
				conteudo.revalidate();
				conteudo.repaint();
				novoPipe.moveToFront();
			}
		});
		mnGerar.add(mntmPipe);

		JMenuItem mntmNewMenuItem = new JMenuItem("Service");
		mntmNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				NovoService novoService = new NovoService(consoleLog);
				novoService.setVisible(true);
				conteudo.add(novoService);
				conteudo.revalidate();
				conteudo.repaint();
				novoService.moveToFront();
			}
		});
		mnGerar.add(mntmNewMenuItem);

		JMenuItem mntmClass = new JMenuItem("Class");
		mntmClass.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmClass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				NovoClass novoClass = new NovoClass(consoleLog);
				novoClass.setVisible(true);
				conteudo.add(novoClass);
				conteudo.revalidate();
				conteudo.repaint();
				novoClass.moveToFront();
			}
		});
		mnGerar.add(mntmClass);

		JMenuItem mntmEnum = new JMenuItem("Enum");
		mntmEnum.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmEnum.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				NovoEnum novoEnum = new NovoEnum(consoleLog);
				novoEnum.setVisible(true);
				conteudo.add(novoEnum);
				conteudo.revalidate();
				conteudo.repaint();
				novoEnum.moveToFront();
			}
		});
		mnGerar.add(mntmEnum);

		JMenuItem mntmInterface = new JMenuItem("Interface");
		mntmInterface.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmInterface.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				NovaInterface novaInterface = new NovaInterface(consoleLog);
				novaInterface.setVisible(true);
				conteudo.add(novaInterface);
				conteudo.revalidate();
				conteudo.repaint();
				novaInterface.moveToFront();

			}
		});
		mnGerar.add(mntmInterface);

		JMenu menu_2 = new JMenu("    ");
		menu_2.setFocusable(false);
		menu_2.setEnabled(false);
		menuBar.add(menu_2);

		JMenu mnAjuda = new JMenu(" Ajuda");
		mnAjuda.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnAjuda.setIcon(ResourcesImages.information());
		menuBar.add(mnAjuda);

		JMenuItem mntmGerao = new JMenuItem("Geração de Projetos");
		mntmGerao.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Help help;
				help = new Help();
				help.setVisible(true);

			}
		});
		mnAjuda.add(mntmGerao);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBounds(new Rectangle(0, 0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		File dir = new File(".");

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(300, 2));

		fileTree = new JTree();
		fileTree.setMaximumSize(new Dimension(150, 64));
		fileTree = refreshTree(fileTree, dir);

		scrollPane.setViewportView(fileTree);

		// ImageIcon image = new ImageIcon(getClass().getResource("cats.png"));
		// ImageIcon image2 = new ImageIcon(getClass().getResource("faq.png"));
		// ImageIcon image3 = new ImageIcon(getClass().getResource("dialogue-box.png"));
		contentPane.setLayout(null);

		conteudo = new JDesktopPane() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5499401181862536578L;
			ImageIcon icon = ResourcesImages.bg();
			Image image = icon.getImage();

			// Image newimage = image.getScaledInstance(icon.getIconWidth(),
			// icon.getIconHeight(), Image.SCALE_SMOOTH);
			Image newimage = image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (newimage != null) {
					g.drawImage(newimage, 0, 0, this.getWidth(), this.getHeight(), this);
				}
				// g.drawImage(newimage, 0, 0, this);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(width, height);
			}

		};

		conteudo.setBounds(1, 0, width, height - 60);
		// conteudo.setPreferredSize(new Dimension(100, 0));
		conteudo.setBorder(null);
		conteudo.setBackground(new Color(255, 255, 255));
		contentPane.add(conteudo);

		JLabel label = new JLabel((Icon) null);
		label.setBackground(Color.WHITE);
		label.setBounds(1181, 177, 52, 57);
		conteudo.add(label);

		lblAsk = new JLabel(ResourcesImages.faq());
		lblAsk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				dialogAviso.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {

				dialogAviso.setVisible(false);
			}
		});

		dialogAviso = new JLabel(ResourcesImages.dialogue());
		dialogAviso.setVisible(false);
		dialogAviso.setBounds(1138, 52, 190, 114);
		dialogAviso.setBackground(Color.WHITE);
		conteudo.add(dialogAviso);
		lblAsk.setBackground(Color.WHITE);
		lblAsk.setBounds(1300, 2, 52, 57);
		conteudo.add(lblAsk);

		lblLogo = new JLabel(ResourcesImages.cats());
		lblLogo.setBackground(new Color(255, 255, 255));
		lblLogo.setBounds(10, 11, width + 116, height - 71);
		conteudo.add(lblLogo);

		consoleLog = new ConsoleLog();
		consoleLog.setVisible(false);

		lblCopyart = new JLabel(" Esquadro © 2020 Adamis Starling");
		lblCopyart.setForeground(new Color(255, 255, 255));
		lblCopyart.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblCopyart.setBounds(576, 666, 300, 14);
		conteudo.add(lblCopyart);
		conteudo.add(consoleLog);

		Calendar.getInstance();
		conteudo.revalidate();
		conteudo.repaint();

	}

	/** Add nodes from under "dir" into curTop. Highly recursive. */
	DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
		String curPath = dir.getPath();
		DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
		if (curTop != null) { // should only be null at root
			curTop.add(curDir);
		}
		Vector ol = new Vector<>();
		String[] tmp = dir.list();
		for (int i = 0; i < tmp.length; i++)
			ol.addElement(tmp[i]);
		Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
		File f;
		Vector files = new Vector();
		// Make two passes, one for Dirs and one for Files. This is #1.
		for (int i = 0; i < ol.size(); i++) {
			String thisObject = (String) ol.elementAt(i);
			String newPath;
			if (curPath.equals("."))
				newPath = thisObject;
			else
				newPath = curPath + File.separator + thisObject;
			if ((f = new File(newPath)).isDirectory())
				addNodes(curDir, f);
			else
				files.addElement(thisObject);
		}
		// Pass two: for files.
		for (int fnum = 0; fnum < files.size(); fnum++)
			curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
		return curDir;
	}

	private JTree refreshTree(JTree fileTree, File dir) {
		if (dir != new File(".")) {
			DefaultMutableTreeNode newAddNodes = addNodes(null, dir);

			// Make a tree list with all the nodes, and make it a JTree
			DefaultTreeModel modelTree = new DefaultTreeModel(newAddNodes);
			if (modelTree != null) {

				fileTree.setModel(modelTree);

				// Add a listener
				fileTree.addTreeSelectionListener(new TreeSelectionListener() {

					@Override
					public void valueChanged(TreeSelectionEvent e) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
						// System.out.println("You selected " + node);
					}

				});
			}
		}
		return fileTree;
	}
}
