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
		open();
		List<Map> findAll = Base.findAll(query);
		close();
		return findAll;
	}

	@Override
	public void open() {
		Base.open(
				"com.mysql.cj.jdbc.Driver", "jdbc:mysql://" + bancoDados.getIp() + ":" + bancoDados.getPorta()
						+ "/" + bancoDados.getNameBd()+ "?autoReconnect=true&relaxAutoCommit=true",
				bancoDados.getUsuario(), bancoDados.getSenha());		
	}

	@Override
	public void close() {
		Base.close();		
	}

}
