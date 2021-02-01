package br.com.esquadro.helper;

import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;

import br.com.esquadro.sqlite.entity.BancoDadosEntity;

public class MysqlHelper implements GenericHelperInterface {

	BancoDadosEntity bancoDados;
	
	public MysqlHelper(BancoDadosEntity bancoDados) {
		this.bancoDados = bancoDados;
	}

	@Override
	public List<Map> executeSQL(String query) {
		//System.err.println("Query>> "+query);
		open();
		List<Map> findAll = Base.findAll(query);
		close();
		return findAll;
	}

	
	private void open() {
		Base.open(
				"com.mysql.cj.jdbc.Driver", "jdbc:mysql://" + bancoDados.getIp() + ":" + bancoDados.getPorta()
						+ "/" + bancoDados.getNameBd()+ "?autoReconnect=true&relaxAutoCommit=true&useTimezone=true&serverTimezone=UTC",
				bancoDados.getUsuario(), bancoDados.getSenha());		
	}

	
	private void close() {
		Base.close();		
	}

}
