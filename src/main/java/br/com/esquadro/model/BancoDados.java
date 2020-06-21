/**
 * 
 */
package br.com.esquadro.model;

/**
 * @author adamis.rocha
 *
 */
public class BancoDados {

	private int id;
	private String nome;
	private String ip;
	private String porta;
	private String usuario;
	private String senha;
	private String charset;
	private String nameBd;
	private String schema;
	private String tipo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPorta() {
		return porta == null ? "" : porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCharset() {
		return charset == null ? "" : charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getNameBd() {
		return nameBd;
	}

	public void setNameBd(String nameBd) {
		this.nameBd = nameBd;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
