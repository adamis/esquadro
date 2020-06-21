/**
 * 
 */
package br.com.esquadro.controler;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.com.esquadro.util.Conexao;
import br.com.esquadro.view.panel.BancoDados;

/**
 * @author adamis.rocha
 *
 */
public class UpdateDatabasesController extends Thread {

	private Conexao conexao;
	private String[] dados;
	private String id;
	private BancoDados bancoDados;

	public UpdateDatabasesController(BancoDados bancoDados, Conexao conexao, String id, String[] dados) {
		this.bancoDados = bancoDados;
		this.conexao = conexao;
		this.dados = dados;
		this.id = id;
	}

	public UpdateDatabasesController(Conexao conexao) {
		this.conexao = conexao;
	}

	@Override
	public void run() {

		try {
			editDatabaseExecution(dados, id);
			JOptionPane.showMessageDialog(null, "Banco alterado com sucesso!");
			this.bancoDados.panel.setVisible(false);
			this.bancoDados.scrollPane.setVisible(true);
			this.bancoDados.clearText();
			this.bancoDados.loadTable();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage() + "\n Impossivel Alterar Banco de Dados!", "Erro",
					JOptionPane.ERROR_MESSAGE);
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

	public String[] editDatabaseRequest(String id) throws Exception {
		conexao.conect();

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * ");
		sql.append(" FROM ");
		sql.append(" banco_dados ");
		sql.append(" WHERE id = " + id + "");

		conexao.beginTransaction();
		ResultSet executeQuery = conexao.executeQuery(sql.toString());
		String[] dados = new String[10];
		while (executeQuery.next()) {
			dados[0] = executeQuery.getString("nome");
			dados[1] = executeQuery.getString("ip");
			dados[2] = executeQuery.getString("porta");
			dados[3] = executeQuery.getString("usuario");
			dados[4] = executeQuery.getString("senha");
			dados[5] = executeQuery.getString("charset");
			dados[6] = executeQuery.getString("namebd");
			dados[7] = executeQuery.getString("schema");
			dados[8] = executeQuery.getString("tipo");
			dados[9] = executeQuery.getString("id");
		}

		conexao.disconect();
		return dados;

	}

	public void editDatabaseExecution(String[] dados, String id) throws Exception {
		conexao.conect();

		StringBuilder sql = new StringBuilder();

		sql.append(" UPDATE banco_dados SET ");
		sql.append("nome = '" + dados[0] + "', ");
		sql.append("ip = '" + dados[1] + "', ");
		sql.append("porta = '" + dados[2] + "', ");
		sql.append("usuario = '" + dados[3] + "', ");
		sql.append("senha = '" + dados[4] + "', ");
		sql.append("charset = '" + dados[5] + "', ");
		sql.append("nameBd = '" + dados[6] + "', ");
		sql.append("schema = '" + dados[7] + "', ");
		sql.append("tipo = '" + dados[8] + "' ");
		sql.append("WHERE id = " + id);

		conexao.beginTransaction();
		conexao.executeQueryUpdate(sql.toString());
		conexao.commit();

	}

}
