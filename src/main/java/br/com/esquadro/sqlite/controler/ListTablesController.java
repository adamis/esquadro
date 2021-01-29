/**
 * 
 */
package br.com.esquadro.sqlite.controler;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import br.com.esquadro.helper.SqliteHelper;
import br.com.esquadro.sqlite.entity.BancoDadosEntity;
import br.com.esquadro.view.ConsoleLog;

/**
 * @author adamis.rocha
 *
 */
public class ListTablesController extends Thread {

	private JTable table;
	private ConsoleLog consoleLog;

	public ListTablesController(JTable table, ConsoleLog consoleLog) {
		this.table = table;
		this.consoleLog = consoleLog;
	}

	@Override
	public void run() {

		try {
			List<BancoDadosEntity> databases = getDatabases();

			String[] columnNames = { "id", "Nome", "Banco de Dados", "tipo", "usuario" };

			DefaultTableModel dtm = new DefaultTableModel() {

				private static final long serialVersionUID = 1L;

				@Override
				public Class<?> getColumnClass(int column) {

					switch (column) {
					case 0:
						return Integer.class;
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

			for (int i = 0; i < databases.size(); i++) {

				dtm.addRow(new Object[] { databases.get(i).getId(), databases.get(i).getNome(),
						databases.get(i).getNameBd(), databases.get(i).getTipo(), databases.get(i).getUsuario(), });

			}

			this.table.getColumnModel().getColumn(0).setPreferredWidth(0);
			this.table.getColumnModel().getColumn(0).setMinWidth(0);
			this.table.getColumnModel().getColumn(0).setMaxWidth(0);
			// this.table.setRowHeight(20);

		} catch (Exception e) {
			this.consoleLog.setText(e.getMessage());
			this.consoleLog.setVisible(true);
			this.consoleLog.moveToFront();
			e.printStackTrace();
		}

		super.run();
	}

	/**
	 * List All Tables in Database
	 * 
	 * @param schema
	 * @return
	 * @throws SQLException
	 */
	public List<BancoDadosEntity> getDatabases() throws Exception {
		Dao<BancoDadosEntity, Integer> bancoDadosDao = DaoManager.createDao(SqliteHelper.connectionSource,
				BancoDadosEntity.class);
		return bancoDadosDao.queryForAll();
	}
}
