/**
 * 
 */
package br.com.esquadro.controler;

import br.com.esquadro.sqlite.entity.BancoDadosEntity;
import br.com.esquadro.util.DatabaseUtils;

/**
 * @author adamis.rocha
 *
 */
public class AutomaticDBController extends Thread {

	private BancoDadosEntity bancoDados;

	public AutomaticDBController(BancoDadosEntity bancoDados) {
		this.bancoDados = bancoDados;
	}

	@Override
	public void run() {

		DatabaseUtils databaseUtils = new DatabaseUtils(bancoDados);

		try {

			databaseUtils.getTables(bancoDados);

		} catch (Exception e) {
			e.printStackTrace();
		}

		super.run();
	}

}
