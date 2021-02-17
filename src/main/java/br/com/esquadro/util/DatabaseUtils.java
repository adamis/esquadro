package br.com.esquadro.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.esquadro.enums.DATABASETYPE;
import br.com.esquadro.helper.GenericHelperInterface;
import br.com.esquadro.helper.MysqlHelper;
import br.com.esquadro.helper.OracleHelper;
import br.com.esquadro.model.TransferDTO;
import br.com.esquadro.sqlite.entity.BancoDadosEntity;

public class DatabaseUtils {

	private GenericHelperInterface genericHelperInterface;
	private BancoDadosEntity bancoDados;
	private StringBuilder sql = null;
	private HashMap<String, String> hm;
	private List<HashMap<String, String>> listTable;



	public DatabaseUtils(BancoDadosEntity bancoDados) {
		this.bancoDados = bancoDados;
		System.err.println(bancoDados.getTipo()+"");
		if (bancoDados.getTipo().toString().equals(DATABASETYPE.MYSQL.toString())) {

			genericHelperInterface = new MysqlHelper(bancoDados);

		} else if (bancoDados.getTipo().toString().equals(DATABASETYPE.ORACLE.toString())) {

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

		if (bancoDados.getTipo().toString().equals(DATABASETYPE.ORACLE.toString())) {

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

		} else if (bancoDados.getTipo().toString().equals(DATABASETYPE.MYSQL.toString())) {

			sql.append("SHOW FULL TABLES IN " + bancoDados.getNameBd());

			executeSQL = this.genericHelperInterface.executeSQL(sql.toString());

			for (Iterator iterator = executeSQL.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();

				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("tableName", map.get("Tables_in_"+bancoDados.getNameBd().toLowerCase()).toString());
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

		if (bancoDados.getTipo().toString().equals(DATABASETYPE.ORACLE.toString())) {
			findTableName = findTableName.replace("-", "_");

			sql.append("SELECT owner as schematic, table_name as tableName FROM dba_tables WHERE dba_tables.owner = ");
			sql.append("'");
			sql.append(bancoDados.getSchema().toUpperCase());
			sql.append("'");
			if(!findTableName.isEmpty()) {
				sql.append(" AND table_name like '%"+findTableName.toUpperCase()+"%' ");
			}
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
			if(!findTableName.isEmpty()) {
				sql.append(" AND view_name like '%"+findTableName.toUpperCase()+"%' ");
			}
			sql.append("order by view_name");

			executeSQL = this.genericHelperInterface.executeSQL(sql.toString());

			for (Iterator iterator = executeSQL.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("tableName", map.get("view_name").toString());
					hm.put("tableType", "VIEW");
					listTable.add(hm);
				
			}

		} else if (bancoDados.getTipo().toString().equals(DATABASETYPE.MYSQL.toString())) {

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

		List<TransferDTO> coluns = getColuns(table);

		String select = "";

		for (int i = 0; i < coluns.size(); i++) {
			if(coluns.get(i).getColumn().isEmpty()) {				
				select += coluns.get(i).getColumn();
			} else {
				select += "," + coluns.get(i).getColumn();
			}


		}

		try {

			// System.err.println("select: " + select);
			String sql;
			List<Map> executeSQL;
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			if (bancoDados.getTipo().toString().equals(DATABASETYPE.ORACLE.toString())) {
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
	public List<TransferDTO> getColuns(String table) throws Exception {

		System.err.println("TABLE:: >>"+table);

		List<TransferDTO> listTableHashColum = new ArrayList<>();
		sql = new StringBuilder();
		List<Map> executeSQL;

		table = table.replace("-", "_");
		List<TransferDTO> fks = getFks(table);

		if (bancoDados.getTipo().toString().equals(DATABASETYPE.ORACLE+"") ) {
			sql = new StringBuilder();
			sql.append("SELECT column_name as coluna, data_type as tipo, data_length as tamanho, data_precision as precisao , nullable as nulo WHERE table_name = '"
					+ table.toUpperCase() + "' order by column_name");

		} else if (bancoDados.getTipo().toString().equals(DATABASETYPE.MYSQL+"")) {
			sql = new StringBuilder();
			//sql.append("SHOW COLUMNS FROM " + table.toUpperCase());
			sql.append("SELECT ");
			sql.append("column_name as coluna," 
					+ "	data_type as tipo,"
					+ "	replace(replace(replace(COLUMN_type,data_type, \"\"),\"(\", \"\"),\")\",\"\") as tamanho , "
					+ "	NUMERIC_PRECISION as precisao, "
					+ "	is_nullable as nulo");
			
			sql.append("FROM INFORMATION_SCHEMA.COLUMNS ");
			sql.append("WHERE table_name = '"+table.toUpperCase()+"'");
			;

		}

		executeSQL = this.genericHelperInterface.executeSQL(sql.toString());

		for (Iterator iterator = executeSQL.iterator(); iterator.hasNext();) {

			//fks = getFks(table);
			Map map = (Map) iterator.next();

			System.err.println("COLUNA>> "+map.get("coluna").toString().toUpperCase());
			
			TransferDTO transferDTO = new TransferDTO();

			if (bancoDados.getTipo().toString().equals(DATABASETYPE.ORACLE+"") ) {
				
				transferDTO.setColumn(map.get("coluna").toString());
				transferDTO.setType(map.get("tipo").toString());
				transferDTO.setPrecisao(Integer.valueOf(map.get("precisao").toString()));
				transferDTO.setTamanho(Integer.valueOf(map.get("tamanho").toString()));
				transferDTO.setNulo(map.get("nulo").toString().toUpperCase().contains("Y")?"SIM":"NAO");

				for (int i = 0; i < fks.size(); i++) {		
					
					TransferDTO transferDTOFk = fks.get(i);					
					
					if (transferDTOFk.getColumn().toUpperCase().equals(map.get("coluna").toString().toUpperCase())) {
						System.err.println("FK ENCOTRADA>> "+transferDTOFk.getColumn().toUpperCase());
						transferDTO.setFk(transferDTOFk.getType());						
						break;
					}					
				}
				
			} else if (bancoDados.getTipo().toString().equals(DATABASETYPE.MYSQL+"")) {
				//System.err.println("MYSQL");

				transferDTO.setColumn(map.get("coluna").toString());
				transferDTO.setType(map.get("tipo").toString());

				if(fks.size() > 0) {					

					
					for (int i = 0; i < fks.size(); i++) {
						//System.err.println(fk.get("column"));					
						//System.err.println(map.get("field").toString());
						TransferDTO fk = fks.get(i);
							if ( fk.getColumn().equals(map.get("coluna").toString())) {								
								transferDTO.setFk(fk.getType());
								break;
							}						
					}

				}

				
			}



			listTableHashColum.add(transferDTO);			

		}

		return listTableHashColum;
	}

	public List<TransferDTO> getFks(String table) throws Exception {
		table = table.replace("-", "_");

		sql = new StringBuilder();
		List<TransferDTO> listTableHashFKs = new ArrayList<TransferDTO>();
		List<Map> executeSQL;

		if (bancoDados.getTipo().toString().equals(DATABASETYPE.ORACLE.toString())) {
			sql = new StringBuilder();
			sql.append(" SELECT ");
			sql.append("   A.COLUMN_NAME coluna, ");
			sql.append("   c_pk.table_name tableRef");
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

		} else if (bancoDados.getTipo().toString().equals(DATABASETYPE.MYSQL.toString())) {
			sql = new StringBuilder();
			sql.append(" SELECT DISTINCT ");
			sql.append("   (k.COLUMN_NAME) coluna, ");
			sql.append("   k.REFERENCED_TABLE_NAME tableRef");
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
			TransferDTO transferDTO = new TransferDTO();
			transferDTO.setColumn(map.get("coluna").toString());
			transferDTO.setType(map.get("tableRef").toString());			
			listTableHashFKs.add(transferDTO);
		}

		return listTableHashFKs;
	}

	public BancoDadosEntity getBancoDados() {
		return bancoDados;
	}

	public void setBancoDados(BancoDadosEntity bancoDados) {
		this.bancoDados = bancoDados;
	}

}
