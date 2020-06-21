/**
 * 
 */
package br.com.esquadro.controler;

import br.com.esquadro.model.BancoDados;
import br.com.esquadro.util.DatabaseUtils;

/**
 * @author adamis.rocha
 *
 */
public class AutomaticDBController extends Thread {

	private BancoDados bancoDados;

	public AutomaticDBController(BancoDados bancoDados) {
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
