package br.com.esquadro.helper;

import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.DB;

import br.com.esquadro.sqlite.entity.BancoDadosEntity;

public class OracleHelper implements GenericHelperInterface {

	BancoDadosEntity bancoDados;
	private static DB open;
	
	public OracleHelper(BancoDadosEntity bancoDados) {
		this.bancoDados = bancoDados;
		try {
			if(open != null) {
				close();
			}
			open = open();
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	@Override
	public List<Map> executeSQL(String query) throws Exception {
		System.err.println("Query: "+query);				
		//List<Map> findAll = open.findAll(query);		
		return open.findAll(query);
	}

	private DB open() throws Exception {
		DB temp = new DB("oracle");
				temp.open(
				"oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@" + bancoDados.getIp() + ":" + bancoDados.getPorta()
				+ "/" + bancoDados.getNameBd() ,//+ "?oracle.jdbc.timezoneAsRegion=false",
				bancoDados.getUsuario(), bancoDados.getSenha());
				
		return temp;
	}
	
	private void close() {
		open.close();
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
	}

}
