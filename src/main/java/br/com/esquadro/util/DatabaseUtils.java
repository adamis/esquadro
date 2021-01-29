package br.com.esquadro.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.esquadro.enums.DATABASETYPE;
import br.com.esquadro.helper.GenericHelperInterface;
import br.com.esquadro.helper.MysqlHelper;
import br.com.esquadro.helper.OracleHelper;
import br.com.esquadro.sqlite.entity.BancoDadosEntity;

public class DatabaseUtils {

	private GenericHelperInterface genericHelperInterface;
	private BancoDadosEntity bancoDados;
	private StringBuilder sql = null;
	private HashMap<String, String> hm;
	private List<HashMap<String, String>> listTable;
	private List<HashMap<String, String>> listTableHash;
	
	
	public DatabaseUtils(BancoDadosEntity bancoDados) {
		this.bancoDados = bancoDados;
		
		if (bancoDados.getTipo() == DATABASETYPE.MYSQL) {

			genericHelperInterface = new MysqlHelper(bancoDados);

		} else if (bancoDados.getTipo() == DATABASETYPE.ORACLE) {

			genericHelperInterface = new OracleHelper(bancoDados);

		}

	}

	/**
	 * List All Tables in Database
	 * 
	 * @param schema
	 * @return
	 * @throws SQLException
	 */
	public List<HashMap<String, String>> getTables(BancoDadosEntity bancoDados) throws Exception {

		sql = new StringBuilder();
		listTable = new ArrayList<>();
		List<Map> executeSQL;

		if (bancoDados.getTipo() == DATABASETYPE.ORACLE) {

			// LISTANDO TODAS TABLE
			sql.append("SELECT owner as schematic, table_name as tableName FROM dba_tables WHERE dba_tables.owner =");
			sql.append("'");
			sql.append(bancoDados.getSchema().toUpperCase());
			sql.append("'");
			sql.append("order by tableName");
			executeSQL = this.genericHelperInterface.executeSQL(sql.toString());

			for (Iterator iterator = executeSQL.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();

				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("tableName", map.get("tableName").toString());
				hm.put("tableType", "BASE TABLE");
				listTable.add(hm);
			}

			// LISTANDO TODAS VIEW
			sql = new StringBuilder();
			sql.append("SELECT view_name,owner as schematic FROM all_views WHERE all_views.owner = '"
					+ bancoDados.getSchema().toUpperCase() + "'");
			executeSQL = this.genericHelperInterface.executeSQL(sql.toString());

			for (Iterator iterator = executeSQL.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();

				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("tableName", map.get("view_name").toString());
				hm.put("tableType", "VIEW");
				listTable.add(hm);
			}

		} else if (bancoDados.getTipo() == DATABASETYPE.MYSQL) {

			sql.append("SHOW FULL TABLES IN " + bancoDados.getNameBd());

			executeSQL = this.genericHelperInterface.executeSQL(sql.toString());

			for (Iterator iterator = executeSQL.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("tableName", map.get("Tables_in_checkTest").toString());
				hm.put("tableType", map.get("Table_type").toString());
				listTable.add(hm);
			}

		}
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

		sql = new StringBuilder();
		listTable = new ArrayList<>();
		List<Map> executeSQL;

		if (bancoDados.getTipo() == DATABASETYPE.ORACLE) {
			findTableName = findTableName.replace("-", "_");

			sql.append("SELECT owner as schematic, table_name as tableName FROM dba_tables WHERE dba_tables.owner = ");
			sql.append("'");
			sql.append(bancoDados.getSchema().toUpperCase());
			sql.append("'");
			sql.append("order by table_name");

			executeSQL = this.genericHelperInterface.executeSQL(sql.toString());

			for (Iterator iterator = executeSQL.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();

				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("tableName", map.get("tableName").toString());
				hm.put("tableType", "BASE TABLE");
				listTable.add(hm);
			}

			sql = new StringBuilder();
			sql.append("SELECT view_name,owner as schematic FROM all_views WHERE owner = ");
			sql.append("'");
			sql.append(bancoDados.getSchema().toUpperCase());
			sql.append("'");
			sql.append("order by view_name");

			executeSQL = this.genericHelperInterface.executeSQL(sql.toString());

			for (Iterator iterator = executeSQL.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();

				if (map.get("view_name").toString().contains(findTableName.toUpperCase())) {
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("tableName", map.get("view_name").toString());
					hm.put("tableType", "VIEW");
					listTable.add(hm);
				}
			}

		} else if (bancoDados.getTipo() == DATABASETYPE.MYSQL) {

			String filter = findTableName.equals("") ? "" : " LIKE '%" + findTableName + "%'";

			sql.append("SHOW FULL TABLES IN " + bancoDados.getNameBd() + filter);

			executeSQL = this.genericHelperInterface.executeSQL(sql.toString());

			for (Iterator iterator = executeSQL.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();

				if (map.get("view_name").toString().contains(findTableName.toUpperCase())) {
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("tableName", map.get("tableName").toString());
					hm.put("tableType", map.get("tableType").toString());
					listTable.add(hm);
				}
			}

		}

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
			List<Map> executeSQL;
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			if (bancoDados.getTipo() == DATABASETYPE.ORACLE) {
				sql = "select " + select.toLowerCase() + " from " + table + " where ROWNUM <= 5";
			} else {
				sql = "select " + select.toLowerCase() + " from " + table + " limit 5";
			}

			executeSQL = this.genericHelperInterface.executeSQL(sql.toString());

			for (Iterator iterator = executeSQL.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				list.add(map);
			}

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

		listTableHash = new ArrayList<>();
		sql = new StringBuilder();
		List<Map> executeSQL;

		table = table.replace("-", "_");
		List<HashMap<String, String>> fks = getFks(table);

		if (bancoDados.getTipo() == DATABASETYPE.ORACLE) {
			sql.append("SELECT column_name as colum, data_type as type FROM	user_tab_cols WHERE	table_name = '"
					+ table.toUpperCase() + "' order by column_name");
		} else if (bancoDados.getTipo() == DATABASETYPE.MYSQL) {
			sql.append("SHOW COLUMNS FROM " + table.toUpperCase());
		}

		executeSQL = this.genericHelperInterface.executeSQL(sql.toString());

		for (Iterator iterator = executeSQL.iterator(); iterator.hasNext();) {
			Map map = (Map) iterator.next();
			hm = new HashMap<>();
			hm.put("colum", map.get("colum").toString());
			hm.put("type", map.get("type").toString());
			boolean control = false;
			String tableNome = "";
			for (int i = 0; i < fks.size(); i++) {
				if (fks.get(i).get("column").equals(map.get("colum").toString())) {
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

		return listTableHash;
	}

	public List<HashMap<String, String>> getFks(String table) throws Exception {
		table = table.replace("-", "_");

		sql = new StringBuilder();
		listTableHash = new ArrayList<>();
		List<Map> executeSQL;

		if (bancoDados.getTipo() == DATABASETYPE.ORACLE) {

			sql.append(" SELECT ");
			sql.append("   A.COLUMN_NAME as column, ");
			sql.append("   c_pk.table_name as tableRef");
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
			sql.append("   A.OWNER = '" + bancoDados.getSchema().toUpperCase() + "'");

		} else if (bancoDados.getTipo() == DATABASETYPE.MYSQL) {

			sql.append(" SELECT DISTINCT ");
			sql.append("   (k.COLUMN_NAME) as column, ");
			sql.append("   k.REFERENCED_TABLE_NAME as tableRef");
			sql.append(" FROM ");
			sql.append("   information_schema.TABLE_CONSTRAINTS i, ");
			sql.append("   information_schema.KEY_COLUMN_USAGE k ");
			sql.append(" WHERE ");
			sql.append("   i.CONSTRAINT_NAME = k.CONSTRAINT_NAME AND ");
			sql.append("   i.CONSTRAINT_TYPE = 'FOREIGN KEY' AND ");
			sql.append("   k.TABLE_SCHEMA = '" + bancoDados.getSchema().toUpperCase() + "' AND ");
			sql.append("   i.TABLE_NAME = '" + table.toUpperCase() + "'");

		}

		executeSQL = this.genericHelperInterface.executeSQL(sql.toString());

		for (Iterator iterator = executeSQL.iterator(); iterator.hasNext();) {
			Map map = (Map) iterator.next();
			hm = new HashMap<>();
			hm.put("column", map.get("column").toString());
			hm.put("tableRef", map.get("tableRef").toString());
			listTableHash.add(hm);
		}

		return listTableHash;
	}

	public BancoDadosEntity getBancoDados() {
		return bancoDados;
	}

	public void setBancoDados(BancoDadosEntity bancoDados) {
		this.bancoDados = bancoDados;
	}

}
