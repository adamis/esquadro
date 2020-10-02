/**
 * 
 */
package br.com.esquadro.controler;

import java.sql.ResultSet;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import br.com.esquadro.util.ButtonEditor;
import br.com.esquadro.util.ButtonRenderer;
import br.com.esquadro.util.Conexao;
import br.com.esquadro.util.Conexao.DATABASEFILE;
import br.com.esquadro.util.JtableActionPerformace;

/**
 * @author adamis.rocha
 *
 */
public class ListDatabaseController extends Thread {

	private JTable table;
	private Conexao conexao;

	public ListDatabaseController(JTable jTable) {
		this.table = jTable;
	}

	@Override
	public void run() {

		try {

			conexao = new Conexao(DATABASEFILE.SQLITE, "", "", "bd/esquadro.db", "", "");
			conexao.conect();

			ResultSet executeQuery = conexao.executeQuery("SELECT * FROM \"banco_dados\"");

			String[] columnNames = { "Id", "Database", "Edit", "Delete" };
			DefaultTableModel dtm = new DefaultTableModel(0, 0);

			dtm.setColumnIdentifiers(columnNames);

			while (executeQuery.next()) {
				dtm.addRow(new Object[] { executeQuery.getString(1), executeQuery.getString(2), "Editar", "Deletar" });
			}

			this.table.setModel(dtm);

			this.table.getColumnModel().getColumn(0).setPreferredWidth(30);
			this.table.getColumnModel().getColumn(0).setMaxWidth(30);
			this.table.setRowHeight(30);

			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

			table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

			JtableActionPerformace edit = new JtableActionPerformace(table) {
				@Override
				public void exec(int col, int row) {
					editar();
				};
			};

			// EDITAR
			// SET CUSTOM RENDERER TO TEAMS COLUMN
			table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());

			// SET CUSTOM EDITOR TO TEAMS COLUMN
			table.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JTextField(), edit));

			JtableActionPerformace delete = new JtableActionPerformace(table) {
				@Override
				public void exec(int col, int row) {
					deletar(this.getjTable().getValueAt(col, 0) + "");
				};
			};

			// DELETAR
			// SET CUSTOM RENDERER TO TEAMS COLUMN
			table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());

			// SET CUSTOM EDITOR TO TEAMS COLUMN
			table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JTextField(), delete));

		} catch (Exception e) {
			e.printStackTrace();
		}

		super.run();
	}

	private void editar() {

	}

	private void deletar(String id) {

		try {
			conexao.conect();
			if (conexao.executeQueryUpdate("delete from banco_dados where id=" + id).equals("OK")) {
				JOptionPane.showMessageDialog(null, "Deletado com Sucesso!");
			} else {
				JOptionPane.showMessageDialog(null, "Falha ao Deletar!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
