package br.com.esquadro.util;

public class IgnoreCaseStr {

	private String str;
	
	public IgnoreCaseStr(String str) {
		this.str = str;
	}
	
	
	public boolean contains(String compare) {
		return str.toLowerCase().contains(compare.toLowerCase());
	}
	
}
