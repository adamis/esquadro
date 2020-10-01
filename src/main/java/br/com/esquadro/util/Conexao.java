package br.com.esquadro.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Conexao {
	private Statement statement;
	private ResultSet resultSet;
	private Connection conexao = null;
	private DATABASETYPE databaseType;
	private DATABASEFILE databaseFile;
	private String path = "";
	private String user = "";
	private String password = "";
	private String ip = "";
	private String port = "";
	private String nameBD = "";
	private String charset = "";
	// private int cont = 0;

	public static enum ALLDATABASETYPE {
		MYSQL, SYBASE, POSTGRESS, ORACLE, ANYWHERE, FIREBIRD, ACCESS, SQLITE
	}

	public static enum DATABASETYPE {
		MYSQL, SYBASE, POSTGRESS, ORACLE, ANYWHERE
	}

	public static enum DATABASEFILE {
		FIREBIRD, ACCESS, SQLITE
	}

	/**
	 * @param databaseType = 'MYSQL' or 'SYBASE' or 'POSTGRESS' or 'ORACLE' or
	 *                     'ANYWHERE'
	 * @param user
	 * @param password
	 * @param ip           = ip or url for Database
	 * @param port         = default 'MYSQL' = 3306 and 'SYBASE' = 2638 and
	 *                     'POSTGRESS' = 5432 and 'ORACLE' = 1521 and 'ANYWHERE' =
	 *                     2638
	 * @param nameBD
	 * @param charset      = default(utf_8)
	 */
	public Conexao(DATABASETYPE databaseType, String user, String password, String ip, String port, String nameBD,
			String charset) {
		this.databaseType = databaseType;
		this.user = user;
		this.password = password;
		this.ip = ip;
		this.port = port;
		this.nameBD = nameBD;
		this.charset = charset;
	}

	/**
	 * @param databasefile = 'FIREBIRD'or 'ACCESS' or 'SQLITE'
	 * @param user
	 * @param password
	 * @param path         = path for database
	 * @param ip           = ip or url for Database
	 * @param charset      = default(utf_8)
	 */
	public Conexao(DATABASEFILE databasefile, String user, String password, String path, String ip, String charset) {
		this.databaseFile = databasefile;
		this.path = path;
		this.user = user;
		this.password = password;
		this.ip = ip;
		this.charset = charset;
	}

	public void conect() throws Exception {

		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", password);
		if (charset.isEmpty()) {
			charset = "utf_8";
		}

		if (databaseType != null && !databaseType.toString().equals(DATABASEFILE.SQLITE)) {
			props.setProperty("encoding", charset);
		}

		try {
			if (databaseType != null) {

				switch (databaseType) {

				case MYSQL:
					if (port.isEmpty()) {
						port = "3306";
					}

					System.err.println("jdbc:mysql://" + ip + ":" + port + "/" + nameBD);
					System.err.println("" + props.toString());

					// props.setProperty("relaxAutoCommit", "true");
					props.setProperty("autoReconnect", "true");
					props.setProperty("useTimezone", "true");
					props.setProperty("serverTimezone", "UTC");

					// Class.forName("org.gjt.mm.mysql.Driver");
					Class.forName("com.mysql.cj.jdbc.Driver");
					conexao = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + nameBD
							// +
							// "?relaxAutoCommit=true&autoReconnect=true&useTimezone=true&serverTimezone=UTC"
							// +"?relaxAutoCommit=true"
							+ "?autoReconnect=true&relaxAutoCommit=true", props);

					break;

				case SYBASE:
					if (port.isEmpty()) {
						port = "2638";
					}
					Class.forName("com.sybase.jdbc3.jdbc.SybDriver");
					conexao = DriverManager.getConnection("jdbc:sybase:Tds:" + ip + ":" + port + "/" + nameBD, props);
					break;

				case POSTGRESS:
					if (port.isEmpty()) {
						port = "5432";
					}
					Class.forName("org.postgresql.Driver");
					conexao = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/" + nameBD, props);
					break;

				case ORACLE:
					if (port.isEmpty()) {
						port = "1521";
					}
					Class.forName("oracle.jdbc.driver.OracleDriver");

					props.setProperty("NLS_DATE_FORMAT ", "dd/MM/yyyy hh24:mi:ss");

					props.setProperty("NLS_TERRITORY ", "BRAZIL");
					props.setProperty("NLS_CURRENCY ", "R$");
					props.setProperty("NLS_ISO_CURRENCY ", "BRAZIL");
					props.setProperty("NLS_NUMERIC_CHARACTERS ", ".,");
					props.setProperty("NLS_CALENDAR ", "GREGORIAN");
					props.setProperty("NLS_DATE_LANGUAGE ", "BRAZILIAN PORTUGUESE");
					props.setProperty("NLS_SORT ", "WEST_EUROPEAN_AI");
					props.setProperty("NLS_TIME_FORMAT ", "HH24:MI:SSXFF");
					props.setProperty("NLS_TIMESTAMP_FORMAT ", "DD/MM/RR HH24:MI:SSXFF");
					props.setProperty("NLS_TIME_TZ_FORMAT ", "HH24:MI:SSXFF TZR");
					props.setProperty("NLS_TIMESTAMP_TZ_FORMAT ", "DD/MM/RR HH24:MI:SSXFF TZR");
					props.setProperty("NLS_DUAL_CURRENCY ", "Cr$");
					props.setProperty("NLS_COMP ", "LINGUISTIC");
					props.setProperty("NLS_LENGTH_SEMANTICS ", "BYTE");
					props.setProperty("NLS_NCHAR_CONV_EXCP ", "FALSE");

					conexao = DriverManager.getConnection("jdbc:oracle:thin:@" + ip + ":" + port + ":" + nameBD, props);

					break;

				case ANYWHERE:
					if (port.isEmpty()) {
						port = "2638";
					}
					Class.forName("com.sybase.jdbc3.jdbc.SybDriver");
					conexao = DriverManager.getConnection("jdbc:sybase:Tds:" + ip + ":" + port + "/" + nameBD, props);
					break;

				default:
					System.out.println("Conexao n�o Encontrada");
					break;
				}

			} else if (databaseFile != null) {

				switch (databaseFile) {
				case FIREBIRD:
					Class.forName("org.firebirdsql.jdbc.FBDriver");
					path = path.replace('\\', '/').trim();
					conexao = DriverManager.getConnection("jdbc:firebirdsql:" + ip + "/3050:" + path, props);
					break;

				case ACCESS:
					String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
					path = path.replace('\\', '/').trim();
					String caminho = "jdbc:odbc:" + path;
					Class.forName(driver);
					conexao = DriverManager.getConnection(caminho, props);
					break;

				case SQLITE:
					String driverSQLITE = "org.sqlite.JDBC";
					path = path.replace('\\', '/').trim();
					String caminhoSQLITE = "jdbc:sqlite:" + path;
					// System.err.println("caminho> " + caminhoSQLITE);
					Class.forName(driverSQLITE);
					conexao = DriverManager.getConnection(caminhoSQLITE, props);
					break;

				default:
					System.out.println("Conexao não Encontrada");
					break;
				}
			}

			beginTransaction();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR Conect120 : " + e.getMessage());
			System.out.println("Falha na Conexão com o Banco.\nVerfique os dados de Conexão e o banco de dados!");
			throw new Exception("Falha na Conexão com o Banco.\nVerfique os dados de Conexão e o banco de dados!" + "\n"
					+ "Detalhe: ERROR Conect120:\n" + e.getMessage());
		}

	}

	public void disconect() {
		try {
			if (conexao != null && !conexao.isClosed()) {
				// conexao.commit();
				conexao.close();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * INSERT OR DELETE OR UPDATE
	 * 
	 * @param sql
	 * @return String "OK" or "null"
	 * @throws SQLException
	 */
	public String executeQueryUpdate(String sql) throws SQLException {
		String resultado = "";

		try {
			//System.out.println(sql);

			statement = conexao.createStatement();
			statement.executeUpdate(sql);
			// System.out.println(""+sql);
			resultado = "OK";

		} catch (SQLException e) {
			System.err.println("SQL: " + sql + " ERROR: " + e.getMessage());
			rollBack();
			e.printStackTrace();
			resultado = "ERROR: " + e.getMessage();
		}
		return resultado;
	}

	/**
	 * SELECT
	 * 
	 * @param sql
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sql) throws SQLException {

		System.out.println(sql);
		ResultSet resultSettemp;
		
		statement = conexao.createStatement();		
		resultSettemp = statement.executeQuery(sql);
		return resultSettemp;
	}

	public void beginTransaction() {
		try {
			conexao.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getCause() != null ? e.getCause().toString() : "Erro Interno");
		}
	}

	public void commit() {
		try {
			conexao.commit();
			conexao.setAutoCommit(true);
			if (statement != null)
				statement.close();
			if (resultSet != null)
				resultSet.close();
			// conexao.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getCause() != null ? e.getCause().toString() : "Erro Interno");
		}
	}

	public void rollBack() {
		try {
			conexao.rollback();
			conexao.setAutoCommit(true);
			if (statement != null)
				statement.close();
			if (resultSet != null)
				resultSet.close();
			// conexao.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getCause() != null ? e.getCause().toString() : "Erro Interno");
		}
	}

	public static String checkDate(Date date) {
		String guia = "";
		if (date == null) {
			guia = "null";
		} else {
			SimpleDateFormat dateFormatParse = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			// System.err.println("DATA: "+date);

			guia = "to_date('" + dateFormatParse.format(date) + "','dd/MM/yyyy hh24:mi:ss')";

		}
		return guia;
	}

	public static String checkString(String string) {
		String guia = "";

		string = string.replace("'", "");
		if (string.isEmpty() || string.equals("null")) {
			guia = "''";
		} else {
			guia = "'" + string + "'";
		}
		return guia;
	}

	public static DATABASETYPE getDatabaseType(String tipo) {
		// MYSQL,SYBASE,POSTGRESS,ORACLE,ANYWHERE
		DATABASETYPE result = null;
		switch (tipo.trim().toUpperCase()) {
		case "MYSQL":
			result = DATABASETYPE.MYSQL;
			break;

		case "SYBASE":
			result = DATABASETYPE.SYBASE;
			break;

		case "POSTGRES":
			result = DATABASETYPE.POSTGRESS;
			break;

		case "ORACLE":
			result = DATABASETYPE.ORACLE;
			break;

		case "ANYWHERE":
			result = DATABASETYPE.ANYWHERE;
			break;

		default:
			break;
		}

		return result;

	}

}
