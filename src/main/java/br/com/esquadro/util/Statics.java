package br.com.esquadro.util;

import java.io.File;
import java.io.FileReader;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

public class Statics {

	public static String NAME_SYS = "ESQUADRO";
	public static String VERSION = version();
	public static Integer UPDATE_UP_LEVEL = 900;
	public static String MODULE_NAME = "modules";

	private static String version() {
		String version = null;

		try {
			MavenXpp3Reader reader = new MavenXpp3Reader();
			
			String fileT = "";
			
			if(new File("META-INF/maven/Esquadro/Esquadro/pom.xml").exists()) {
				fileT = "META-INF/maven/Esquadro/Esquadro/pom.xml";
			}else if (new File("pom.xml").exists()) {
				fileT = "pom.xml";
			}
			
			
	        Model model = reader.read(new FileReader(fileT));
	        version = model.getVersion();
	        
		} catch (Exception e) {		
			e.printStackTrace();
		}

		return version;

	}
}
