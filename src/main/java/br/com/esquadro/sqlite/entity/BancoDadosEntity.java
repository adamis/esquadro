/**
 * 
 */
package br.com.esquadro.sqlite.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.esquadro.enums.DATABASETYPE;
import lombok.Data;

/**
 * @author adamis.rocha
 *
 */
@Data
@DatabaseTable(tableName = "banco_dados")
public class BancoDadosEntity {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false)
	private String nome;

	@DatabaseField(canBeNull = false)
	private String ip;

	@DatabaseField(canBeNull = false)
	private String porta;

	@DatabaseField(canBeNull = false)
	private String usuario;

	@DatabaseField(dataType = DataType.STRING)
	private String senha;

	@DatabaseField(dataType = DataType.STRING)
	private String charset;

	@DatabaseField(canBeNull = false)
	private String nameBd;

	@DatabaseField(canBeNull = false)
	private String schema;

	@DatabaseField(dataType = DataType.ENUM_STRING)
	private DATABASETYPE tipo;

}
