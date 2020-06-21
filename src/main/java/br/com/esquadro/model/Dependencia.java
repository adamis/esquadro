package br.com.esquadro.model;

import br.com.esquadro.model.DependenciasPOM.DEPEND;

public class Dependencia {

	private DEPEND nome;
	private String groupId;
	private String artifactId;
	private String optional;
	private String scope;
	private String version;

	public DEPEND getNome() {
		return nome;
	}

	public void setNome(DEPEND nome) {
		this.nome = nome;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getOptional() {
		return optional;
	}

	public void setOptional(String optional) {
		this.optional = optional;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
