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
public class UpdateDatabasesController extends Thread {

	private BancoDadosEntity dados;
	private Integer id;
	private BancoDados bancoDados;

	public UpdateDatabasesController(BancoDados bancoDados, Integer id, BancoDadosEntity dados) {
		this.bancoDados = bancoDados;
		this.dados = dados;
		this.id = id;
	}

	public UpdateDatabasesController() {
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

	public BancoDadosEntity findById(Integer id) throws Exception {
		Dao<BancoDadosEntity, Integer> bancoDadosDao = DaoManager.createDao(SqliteHelper.connectionSource,
				BancoDadosEntity.class);
		return bancoDadosDao.queryForId(id);
	}

	public void editDatabaseExecution(BancoDadosEntity dados, Integer id) throws Exception {
		Dao<BancoDadosEntity, Integer> bancoDadosDao = DaoManager.createDao(SqliteHelper.connectionSource,
				BancoDadosEntity.class);
		bancoDadosDao.updateId(dados, id);
	}

}
