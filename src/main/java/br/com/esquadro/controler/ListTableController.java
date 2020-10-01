/**
 * 
 */
package br.com.esquadro.controler;

import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import br.com.esquadro.model.BancoDados;
import br.com.esquadro.util.DatabaseUtils;
import br.com.esquadro.view.ConsoleLog;

/**
 * @author adamis.rocha
 *
 */
public class ListTableController extends Thread {

	private BancoDados bancoDados;
	private JTable table;
	private Boolean commomCase;
	private String filterTableName;
	// private ConsoleLog consoleLog;

	public ListTableController(BancoDados bancoDados, JTable table, Boolean commomCase, ConsoleLog consoleLog, String filterTableName) {
		this.bancoDados = bancoDados;
		this.table = table;
		this.commomCase = commomCase;
		this.filterTableName = filterTableName;
		// this.consoleLog = consoleLog;
	}

	@Override
	public void run() {

		DatabaseUtils databaseUtils = new DatabaseUtils(this.bancoDados);

		try {

			List<HashMap<String, String>> tables;
			
			if(this.filterTableName != null && this.filterTableName.length() > 0 ) {
				tables = databaseUtils.getTables(this.bancoDados,this.filterTableName);
			}else {
				tables = databaseUtils.getTables(this.bancoDados);
			}
			
			System.err.println("Size>"+ tables.size());
			
			String[] columnNames = { "ck", "Tabelas", "Tipo" };

			DefaultTableModel dtm = new DefaultTableModel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 9083177129978561645L;

				@Override
				public Class<?> getColumnClass(int column) {

					switch (column) {
					case 0:
						return Boolean.class;
					case 1:
						return String.class;
					case 2:
						return String.class;
					case 3:
						return String.class;
					case 4:
						return String.class;

					default:
						return String.class;
					}
				}
			};

			dtm.setColumnIdentifiers(columnNames);

			table.setModel(dtm);

			for (int i = 0; i < tables.size(); i++) {
				if (commomCase) {
					dtm.addRow(new Object[] { Boolean.FALSE, commomCaseParse(tables.get(i).get("tableName")),
							commomCaseParse(tables.get(i).get("tableType")) });
				} else {
					dtm.addRow(new Object[] { Boolean.FALSE, tables.get(i).get("tableName"),
							tables.get(i).get("tableType") });
				}
			}

			this.table.getColumnModel().getColumn(0).setPreferredWidth(40);
			this.table.getColumnModel().getColumn(0).setMaxWidth(40);

			this.table.getColumnModel().getColumn(2).setPreferredWidth(120);
			this.table.getColumnModel().getColumn(2).setMaxWidth(120);

			this.table.setRowHeight(20);

		} catch (Exception e) {
			System.err.println("ERRO AKI>>" + e.getMessage());
			JOptionPane.showMessageDialog(null, "" + e.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		super.run();
	}

	private String commomCaseParse(String text) {

		String[] split = text.split("_");

		if (split.length == 1) {
			return split[0].toLowerCase();
		} else {
			String commom = "";
			for (int i = 0; i < split.length; i++) {
				if (i == 0) {
					commom = split[0].toLowerCase();
				} else {
					commom += "-" + split[i].toLowerCase();
				}
			}
			return commom;
		}

	}

}
