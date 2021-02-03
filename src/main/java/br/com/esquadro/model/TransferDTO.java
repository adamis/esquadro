package br.com.esquadro.model;

import lombok.Data;

@Data
public class TransferDTO {

	private String column;
	private String type;
	private String fk = "";	
}
