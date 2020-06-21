package br.com.esquadro.util;

import br.com.esquadro.util.Conexao.DATABASEFILE;

public class SqliteHelper {

	public static Conexao conexaoSQLITE = getConexao();

	private static Conexao getConexao() {
		if (conexaoSQLITE == null) {
			Conexao conexao = new Conexao(DATABASEFILE.SQLITE, "", "", "bd/esquadro.db", "", "");
			try {
				conexao.conect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// System.err.println("INICOU CONEXAO");
			return conexao;
		} else {
			// System.err.println("MATEM CONEXAO");
			return conexaoSQLITE;
		}
	}

}
