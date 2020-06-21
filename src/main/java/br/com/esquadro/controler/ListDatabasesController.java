/**
 * 
 */
package br.com.esquadro.controler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import br.com.esquadro.model.BancoDados;
import br.com.esquadro.util.Conexao;
import br.com.esquadro.view.ConsoleLog;

/**
 * @author adamis.rocha
 *
 */
public class ListDatabasesController extends Thread {

	private Conexao conexao;
	private JTable table;
	// private ConsoleLog consoleLog;

	public ListDatabasesController(Conexao conexao, JTable table, ConsoleLog consoleLog) {
		this.conexao = conexao;
		this.table = table;
		// this.consoleLog = consoleLog;
	}

	@Override
	public void run() {

		try {
			List<BancoDados> databases = getDatabases(conexao);

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
	public List<BancoDados> getDatabases(Conexao conexao) throws Exception {
		conexao.conect();

		List<BancoDados> listBancoDados = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" banco_dados.id, ");
		sql.append(" banco_dados.nome, ");
		sql.append(" banco_dados.ip, ");
		sql.append(" banco_dados.porta, ");
		sql.append(" banco_dados.usuario, ");
		sql.append(" banco_dados.senha, ");
		sql.append(" banco_dados.charset, ");
		sql.append(" banco_dados.nameBd, ");
		sql.append(" banco_dados.schema, ");
		sql.append(" banco_dados.tipo ");
		sql.append(" FROM ");
		sql.append(" banco_dados ");
		sql.append(" order by tipo,nome ");

		conexao.beginTransaction();
		ResultSet executeQuery = conexao.executeQuery(sql.toString());

		while (executeQuery.next()) {
			BancoDados bancoDados = new BancoDados();
			bancoDados.setId(executeQuery.getInt("id"));
			bancoDados.setNome(executeQuery.getString("nome"));
			bancoDados.setIp(executeQuery.getString("ip"));
			bancoDados.setPorta(executeQuery.getString("porta"));
			bancoDados.setUsuario(executeQuery.getString("usuario"));
			bancoDados.setSenha(executeQuery.getString("senha"));
			bancoDados.setCharset(executeQuery.getString("charset"));
			bancoDados.setNameBd(executeQuery.getString("namebd"));
			bancoDados.setSchema(executeQuery.getString("schema"));
			bancoDados.setTipo(executeQuery.getString("tipo"));

			listBancoDados.add(bancoDados);
		}

		conexao.commit();
		return listBancoDados;
	}
}
