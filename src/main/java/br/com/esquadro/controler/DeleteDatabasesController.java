/**
 * 
 */
package br.com.esquadro.controler;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.com.esquadro.util.Conexao;
import br.com.esquadro.view.panel.BancoDados;

/**
 * @author adamis.rocha
 *
 */
public class DeleteDatabasesController extends Thread {

	private Conexao conexao;
	private String id;
	// private String nomeBD;
	private BancoDados bancoDados;

	public DeleteDatabasesController(BancoDados bancoDados, Conexao conexao, String id, String nomeBD) {
		this.bancoDados = bancoDados;
		this.conexao = conexao;
		this.id = id;
		// this.nomeBD = nomeBD;
	}

	@Override
	public void run() {

		try {

			deleteDatabase(id);
			this.bancoDados.loadTable();
			JOptionPane.showMessageDialog(null, "Exclusão realizada com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage() + "\n Impossivel Excluir o Banco de Dados!", "Erro",
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

	public void deleteDatabase(String id) throws Exception {
		conexao.conect();

		StringBuilder sql = new StringBuilder();

		sql.append(" DELETE FROM banco_dados WHERE id = " + id);

		conexao.beginTransaction();
		conexao.executeQueryUpdate(sql.toString());
		conexao.commit();
	}

}
