package br.com.esquadro.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.esquadro.sqlite.entity.BancoDadosEntity;
import br.com.esquadro.util.Conexao.DATABASETYPE;

public class DatabaseUtils {

	private Conexao conexao;
	private StringBuilder sql = null;
	private ResultSet executeQuery;
	private List<HashMap<String, String>> listTable;
	private List<HashMap<String, String>> listTableHash;
	private HashMap<String, String> hm;

	private DATABASETYPE tipo;
	String owner;

	public DatabaseUtils(BancoDadosEntity bancoDados) {
		this.tipo = Conexao.getDatabaseType(bancoDados.getTipo());
		this.owner = bancoDados.getSchema();
		conexao = new Conexao(tipo, bancoDados.getUsuario(), bancoDados.getSenha(), bancoDados.getIp(),
				bancoDados.getPorta(), bancoDados.getNameBd(), bancoDados.getCharset());
	}

	/**
	 * List All Tables in Database
	 * 
	 * @param schema
	 * @return
	 * @throws SQLException
	 */
	public List<HashMap<String, String>> getTables(BancoDadosEntity bancoDados) throws Exception {
		conexao.conect();
		sql = new StringBuilder();

		listTable = new ArrayList<>();

		if (tipo.toString().equals("ORACLE")) {

			sql.append("SELECT owner as schematic, table_name as tableName FROM dba_tables WHERE dba_tables.owner =");
			sql.append("'");
			sql.append(bancoDados.getSchema().toUpperCase());
			sql.append("'");
			sql.append("order by tableName");

			executeQuery = conexao.executeQuery(sql.toString());

			while (executeQuery.next()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("tableName", executeQuery.getString("tableName"));
				hm.put("tableType", "BASE TABLE");
				listTable.add(hm);
			}

			sql = new StringBuilder();
			sql.append("SELECT view_name,owner as schematic FROM all_views WHERE all_views.owner = '"
					+ bancoDados.getSchema().toUpperCase() + "'");
			executeQuery = conexao.executeQuery(sql.toString());

			while (executeQuery.next()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("tableName", executeQuery.getString("view_name"));
				hm.put("tableType", "VIEW");
				listTable.add(hm);
			}

		} else if (tipo.toString().equals("MYSQL")) {

			sql.append("SHOW FULL TABLES IN " + bancoDados.getNameBd());

			executeQuery = conexao.executeQuery(sql.toString());

			while (executeQuery.next()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("tableName", executeQuery.getString(1));
				hm.put("tableType", executeQuery.getString(2));
				listTable.add(hm);

			}

		}

		conexao.commit();
		return listTable;
	}

	/**
	 * List All with Tables in Database
	 * 
	 * @param schema
	 * @return
	 * @throws SQLException
	 */
	public List<HashMap<String, String>> getTables(BancoDadosEntity bancoDados, String findTableName) throws Exception {
		conexao.conect();
		sql = new StringBuilder();

		listTable = new ArrayList<>();

		if (tipo.toString().equals("ORACLE")) {
			findTableName = findTableName.replace("-", "_");

			sql.append("SELECT owner as schematic, table_name as tableName FROM dba_tables WHERE dba_tables.owner = ");
			sql.append("'");
			sql.append(bancoDados.getSchema().toUpperCase());
			sql.append("'");
			sql.append("order by table_name");

			executeQuery = conexao.executeQuery(sql.toString());

			while (executeQuery.next()) {

				// System.err.println("TABLE: "+executeQuery.getString("tableName")+" FILTER> "
				// +findTableName.toUpperCase());

				if (executeQuery.getString("tableName").contains(findTableName.toUpperCase())) {
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("tableName", executeQuery.getString("tableName"));
					hm.put("tableType", "BASE TABLE");
					listTable.add(hm);
				}
			}

			sql = new StringBuilder();
			sql.append("SELECT view_name,owner as schematic FROM all_views WHERE owner = ");
			sql.append("'");
			sql.append(bancoDados.getSchema().toUpperCase());
			sql.append("'");
			sql.append("order by view_name");

			executeQuery = conexao.executeQuery(sql.toString());

			while (executeQuery.next()) {
				if (executeQuery.getString("view_name").contains(findTableName.toUpperCase())) {
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("tableName", executeQuery.getString("view_name"));
					hm.put("tableType", "VIEW");
					listTable.add(hm);
				}
			}

		} else if (tipo.toString().equals("MYSQL")) {

			String filter = findTableName.equals("") ? "" : " LIKE '%" + findTableName + "%'";

			sql.append("SHOW FULL TABLES IN " + bancoDados.getNameBd() + filter);

			executeQuery = conexao.executeQuery(sql.toString());

			while (executeQuery.next()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("tableName", executeQuery.getString(1));
				hm.put("tableType", executeQuery.getString(2));
				listTable.add(hm);

			}

		}

		conexao.commit();
		return listTable;
	}

	public String getRowsJson(String table) throws Exception {

		table = table.replace("-", "_");

		List<HashMap<String, String>> coluns = getColuns(table);

		String select = "";

		for (HashMap<String, String> hm : coluns) {

			for (Entry<String, String> entry : hm.entrySet()) {

				String v = entry.getValue();
				// String k = entry.getKey();

				// System.err.println("k: " + k + " v: " + v);

				if (entry.getKey().equals("colum")) {

					if (select.isEmpty()) {
						select += v;
					} else {
						select += "," + v;
					}
				}
			}

		}

		try {

			// System.err.println("select: " + select);
			String sql;

			if (this.tipo.toString().equals("ORACLE")) {
				sql = "select " + select.toLowerCase() + " from " + table + " where ROWNUM <= 5";
			} else {
				sql = "select " + select.toLowerCase() + " from " + table + " limit 5";
			}

			conexao.conect();
			executeQuery = conexao.executeQuery(sql.toString());

			// System.err.println("passou>>: " + sql);

			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			ResultSetMetaData rsMetaData = executeQuery.getMetaData();

			while (executeQuery.next()) { // convert each object to an human readable JSON object

				Map<String, String> map = new HashMap<String, String>();
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {

					String key = rsMetaData.getColumnName(i);
					// System.err.println("key: " + key);

					// int columnType = rsMetaData.getColumnType(i);
					// System.err.println("ColumnType: "+columnType);

					map.put(key, executeQuery.getString(key));

				}
				list.add(map);
			}

			conexao.commit();

			Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
			// System.err.println("" + gson.toJson(list));

			return gson.toJson(list);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * List All Coluns in Table
	 * 
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public List<HashMap<String, String>> getColuns(String table) throws Exception {
		table = table.replace("-", "_");

		List<HashMap<String, String>> fks = getFks(table);

		conexao.conect();
		sql = new StringBuilder();

		if (tipo.toString().equals("ORACLE")) {
			sql.append("SELECT column_name,	data_type FROM	user_tab_cols WHERE	table_name = '" + table.toUpperCase()
					+ "' order by column_name");
		} else if (tipo.toString().equals("MYSQL")) {
			sql.append("SHOW COLUMNS FROM " + table.toUpperCase());
		}

		executeQuery = conexao.executeQuery(sql.toString());

		listTableHash = new ArrayList<>();

		while (executeQuery.next()) {
			hm = new HashMap<>();
			hm.put("colum", executeQuery.getString(1));
			hm.put("type", executeQuery.getString(2));
			boolean control = false;
			String tableNome = "";
			for (int i = 0; i < fks.size(); i++) {
				if (fks.get(i).get("column").equals(executeQuery.getString(1))) {
					control = true;
					tableNome = fks.get(i).get("tableRef");
					break;
				}
			}

			if (control) {
				hm.put("fk", tableNome);
			} else {
				hm.put("fk", "");
			}
			// System.err.println("HM>> "+hm.toString());
			listTableHash.add(hm);
		}

		conexao.commit();

		return listTableHash;
	}

	public List<HashMap<String, String>> getFks(String table) throws Exception {
		table = table.replace("-", "_");

		conexao.conect();
		sql = new StringBuilder();

		if (tipo.toString().equals("ORACLE")) {

			sql.append(" SELECT ");
			sql.append("   A.COLUMN_NAME, ");
			sql.append("   c_pk.table_name ");
			sql.append(" FROM ");
			sql.append("   all_cons_columns A, ");
			sql.append("   all_constraints c, ");
			sql.append("   all_constraints c_pk ");
			sql.append(" WHERE ");
			sql.append("   A. OWNER = c.OWNER AND ");
			sql.append("   A.constraint_name = c.constraint_name AND ");
			sql.append("   c.r_owner = c_pk.OWNER AND ");
			sql.append("   c.r_constraint_name = c_pk.constraint_name AND ");
			sql.append("   c.constraint_type = 'R' AND ");
			sql.append("   A.table_name = '" + table.toUpperCase() + "' AND ");
			sql.append("   A.OWNER = '" + owner.toUpperCase() + "'");

		} else if (tipo.toString().equals("MYSQL")) {

			sql.append(" SELECT DISTINCT ");
			sql.append("   (k.COLUMN_NAME), ");
			sql.append("   k.REFERENCED_TABLE_NAME ");
			sql.append(" FROM ");
			sql.append("   information_schema.TABLE_CONSTRAINTS i, ");
			sql.append("   information_schema.KEY_COLUMN_USAGE k ");
			sql.append(" WHERE ");
			sql.append("   i.CONSTRAINT_NAME = k.CONSTRAINT_NAME AND ");
			sql.append("   i.CONSTRAINT_TYPE = 'FOREIGN KEY' AND ");
			sql.append("   k.TABLE_SCHEMA = '" + owner.toUpperCase() + "' AND ");
			sql.append("   i.TABLE_NAME = '" + table.toUpperCase() + "'");

		}

		executeQuery = conexao.executeQuery(sql.toString());

		listTableHash = new ArrayList<>();

		while (executeQuery.next()) {
			hm = new HashMap<>();
			hm.put("column", executeQuery.getString(1));
			hm.put("tableRef", executeQuery.getString(2));
			listTableHash.add(hm);
		}

		conexao.commit();

		return listTableHash;
	}

	public DATABASETYPE getTipo() {
		return tipo;
	}

	public void setTipo(DATABASETYPE tipo) {
		this.tipo = tipo;
	}

}
