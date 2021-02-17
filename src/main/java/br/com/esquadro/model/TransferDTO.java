package br.com.esquadro.model;

import lombok.Data;

@Data
public class TransferDTO {

	private String column;
	private String type;
	private Integer tamanho;
	private Integer precisao;
	private String nulo;
	private String fk = "";	
	
}
