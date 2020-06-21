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
public class InsertDatabasesController extends Thread {

	private Conexao conexao;
	private String[] dados;
	private BancoDados bancoDados;

	public InsertDatabasesController(BancoDados bancoDados, Conexao conexao, String[] dados) {
		this.bancoDados = bancoDados;
		this.conexao = conexao;
		this.dados = dados;
	}

	@Override
	public void run() {

		try {

			insertDatabase(dados);
			JOptionPane.showMessageDialog(null, "Banco inserido com sucesso!");
			this.bancoDados.clearText();
			this.bancoDados.loadTable();
			this.bancoDados.panel.setVisible(false);
			this.bancoDados.scrollPane.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage() + "\n Impossivel Inserir Banco de Dados!", "Erro",
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

	public void insertDatabase(String[] dados) throws Exception {
		conexao.conect();

		StringBuilder sql = new StringBuilder();

		sql.append(" INSERT INTO banco_dados(nome,ip,porta,usuario,senha,charset,nameBd,schema,tipo) VALUES (");
		sql.append(" '" + dados[0] + "', ");
		sql.append(" '" + dados[1] + "', ");
		sql.append(" '" + dados[2] + "', ");
		sql.append(" '" + dados[3] + "', ");
		sql.append(" '" + dados[4] + "', ");
		sql.append(" '" + dados[5] + "', ");
		sql.append(" '" + dados[6] + "', ");
		sql.append(" '" + dados[7] + "', ");
		sql.append(" '" + dados[8] + "'); ");

		conexao.beginTransaction();
		conexao.executeQueryUpdate(sql.toString());
		conexao.commit();
		conexao.disconect();

	}

}
