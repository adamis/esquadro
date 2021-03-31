package br.com.esquadro.helper;

import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DB;

import br.com.esquadro.sqlite.entity.BancoDadosEntity;

public class MysqlHelper implements GenericHelperInterface {

	BancoDadosEntity bancoDados;
	private static DB open;
	
	public MysqlHelper(BancoDadosEntity bancoDados) {
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
		
		System.err.println("com.mysql.cj.jdbc.Driver" + " jdbc:mysql://" + bancoDados.getIp() + ":" + bancoDados.getPorta()
		+ "/" + bancoDados.getNameBd()+ "?autoReconnect=true&relaxAutoCommit=true&useTimezone=true&serverTimezone=UTC"+" USUARIO/SENHA>"+
bancoDados.getUsuario()+ bancoDados.getSenha());
		
				DB temp = new DB("mysql");
				temp.open(
						"com.mysql.cj.jdbc.Driver", "jdbc:mysql://" + bancoDados.getIp() + ":" + bancoDados.getPorta()
								+ "/" + bancoDados.getNameBd()+ "?autoReconnect=true&relaxAutoCommit=true&useTimezone=true&serverTimezone=UTC",
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

	
//	private DB open() {
//		
//	}

	

}
