package br.com.esquadro.helper;

import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DB;

import br.com.esquadro.sqlite.entity.BancoDadosEntity;

public class OracleHelper implements GenericHelperInterface {

	BancoDadosEntity bancoDados;
	private static DB open;
	
	public OracleHelper(BancoDadosEntity bancoDados) {
		this.bancoDados = bancoDados;		
	}

	@Override
	public List<Map> executeSQL(String query) {
		System.err.println("Query: "+query);
		if(open == null) {
			open = open();
		}		
		List<Map> findAll = open.findAll(query);		
		return findAll;
	}

	private DB open() {
		return Base.open(
				"oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@" + bancoDados.getIp() + ":" + bancoDados.getPorta()
				+ "/" + bancoDados.getNameBd() ,//+ "?oracle.jdbc.timezoneAsRegion=false",
				bancoDados.getUsuario(), bancoDados.getSenha());
	}
	
	private void close() {
		open.close();
	}

}
