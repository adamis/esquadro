/**
 * 
 */
package br.com.esquadro.sqlite.controler;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import br.com.esquadro.sqlite.entity.BancoDadosEntity;
import br.com.esquadro.sqlite.helper.SqliteHelper;
import br.com.esquadro.view.panel.BancoDados;

/**
 * @author adamis.rocha
 *
 */
public class DeleteDatabasesController extends Thread {

	
	private Integer id;
	// private String nomeBD;
	private BancoDados bancoDados;

	public DeleteDatabasesController(BancoDados bancoDados, Integer id, String nomeBD) {
		this.bancoDados = bancoDados;		
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

	public void deleteDatabase(Integer id) throws Exception {
		Dao<BancoDadosEntity, Integer> bancoDadosDao = DaoManager.createDao(SqliteHelper.connectionSource, BancoDadosEntity.class);		
		bancoDadosDao.deleteById(id);
		
	}

}
