package br.com.esquadro.helper;

import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;

import br.com.esquadro.sqlite.entity.BancoDadosEntity;

public class OracleHelper implements GenericHelperInterface {

	BancoDadosEntity bancoDados;

	public OracleHelper(BancoDadosEntity bancoDados) {
		this.bancoDados = bancoDados;
	}

	@Override
	public List<Map> executeSQL(String query) {
		//System.err.println("SQL> "+query);
		open();
		List<Map> findAll = Base.findAll(query);
		close();
		return findAll;
	}

	private void open() {
		Base.open(
				"oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@" + bancoDados.getIp() + ":" + bancoDados.getPorta()
				+ "/" + bancoDados.getNameBd() ,//+ "?oracle.jdbc.timezoneAsRegion=false",
				bancoDados.getUsuario(), bancoDados.getSenha());
	}
	
	
	private void close() {
		Base.close();
	}

}
