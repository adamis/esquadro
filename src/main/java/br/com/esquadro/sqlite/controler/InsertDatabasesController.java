/**
 * 
 */
package br.com.esquadro.sqlite.controler;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import br.com.esquadro.helper.SqliteHelper;
import br.com.esquadro.sqlite.entity.BancoDadosEntity;
import br.com.esquadro.view.panel.BancoDados;

/**
 * @author adamis.rocha
 *
 */
public class InsertDatabasesController extends Thread {

	private BancoDadosEntity dados;
	private BancoDados bancoDados;

	public InsertDatabasesController(BancoDados bancoDados, BancoDadosEntity dados) {
		this.bancoDados = bancoDados;
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

	public void insertDatabase(BancoDadosEntity dados) throws Exception {
		Dao<BancoDadosEntity, Integer> bancoDadosDao = DaoManager.createDao(SqliteHelper.connectionSource,
				BancoDadosEntity.class);
		bancoDadosDao.create(dados);
	}

}
