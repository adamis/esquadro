/**
 * 
 */
package br.com.esquadro.controler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.esquadro.model.BancoDados;
import br.com.esquadro.model.Dependencia;
import br.com.esquadro.model.DependenciasPOM;
import br.com.esquadro.model.DependenciasPOM.DEPEND;
import br.com.esquadro.util.Statics;
import br.com.esquadro.util.Utils;
import br.com.esquadro.view.ConsoleLog;

/**
 * @author adamis.rocha
 *
 */
public class AddEsquadroSBController implements Runnable {

	private BancoDados bancoDados;
	private ConsoleLog consoleLog;
	private String urlProject;
	private String packages;

	private List<DEPEND> listDepend;
	private Boolean changePOM;
	private boolean configConexao;
	private String BARRA = System.getProperty("file.separator");  

	public AddEsquadroSBController(BancoDados bancoDados, ConsoleLog consoleLog, String urlProject, String packages,
			Boolean changePOM, List<DEPEND> listDepend, Boolean configConexao) {
		this.bancoDados = bancoDados;
		this.consoleLog = consoleLog;
		this.urlProject = urlProject;
		this.packages = packages;
		this.changePOM = changePOM;
		this.listDepend = listDepend;
		this.configConexao = configConexao;
	}

	@Override
	public void run() {

		try {
			consoleLog.setVisible(true);
			consoleLog.moveToFront();

			consoleLog.setText("Iniciando Processamento...");
			packages = packages.replace(".", BARRA);

			consoleLog.setText("Criando Pasta...");
//			File file = new File(this.urlProject + BARRA + packages);
//
//			if (!file.exists()) {
//				file.mkdirs();
//			}

			consoleLog.setText("OK!");

			consoleLog.setText("Copy all...");
			consoleLog.setText("Copiando Arquivos para:" + this.urlProject + BARRA+"src"+BARRA+"main"+BARRA+"java"+BARRA+ packages);
			
			Utils.doCopyDirectory(new File("EsquadroSB"+BARRA+"copyAll"), this.urlProject + BARRA+"src"+BARRA+"main"+BARRA+"java"+BARRA+ packages,false);			
						
			
			consoleLog.setText("OK!");
			
			consoleLog.setText("Replace Package...");
			
//			replacePackage(
//					new File(this.urlProject + "/src/main/java/" + packages + "/config/property/" + "ApiProperty.java"),
//					packages.replace("/", "."), ".config.property");
			
			replacePackage(new File(this.urlProject + BARRA +"src"+ BARRA +"main"+ BARRA +"java"+ BARRA + packages + BARRA +"cors"+ BARRA + "CorsFilter.java"),
					packages.replace(BARRA, "."), ".cors");
			
//			replacePackage(new File(
//					this.urlProject + "/src/main/java/" + packages + "/event/listener/" + "RecursoCriadoListener.java"),
//					packages.replace("/", "."), ".event.listener");
			
//			replacePackage(
//					new File(this.urlProject + "/src/main/java/" + packages + "/event/" + "RecursoCriadoEvent.java"),
//					packages.replace("/", "."), ".event");
			
			replacePackage(new File(this.urlProject + BARRA + "src"+ BARRA +"main"+ BARRA +"java"+ BARRA + packages + BARRA +"exceptionhandler"+ BARRA 
					+ "PersonalExceptionHandler.java"), packages.replace(BARRA, "."), ".exceptionhandler");

			replacePackage(new File(this.urlProject + BARRA + "src"+ BARRA +"main"+ BARRA +"java"+ BARRA + packages + BARRA +"utils"+ BARRA + "Utils.java"),
					packages.replace(BARRA, "."), ".utils");

			replacePackage(new File(this.urlProject + BARRA + "src"+ BARRA +"main"+ BARRA +"java"+ BARRA + packages + BARRA +"config"+ BARRA + "SwaggerConfig.java"),
					packages.replace(BARRA, "."), ".config");

			consoleLog.setText("OK!");

			if (changePOM) {
				consoleLog.setText("Realizando Alterações POM...");
				processandoPOM(this.urlProject, listDepend);
				consoleLog.setText("Alterações Finalizadas!");
			}

			if (configConexao) {
				consoleLog.setText("Configurando Conexao e alterando application.properties");
				configurarConexao(this.bancoDados);
				consoleLog.setText("Configurando Finalizadas!");
			}
			
			Utils.doCopyFile(new File("EsquadroSB"+ BARRA +"messages_pt_BR.properties"), this.urlProject + BARRA + "src"+ BARRA +"main"+ BARRA +"resources"+ BARRA +"messages_pt_BR.properties");
			Utils.doCopyFile(new File("EsquadroSB"+ BARRA +"messages.properties"), this.urlProject + BARRA + "src"+ BARRA +"main"+ BARRA +"resources"+ BARRA +"messages.properties");
			Utils.doCopyFile(new File("EsquadroSB"+ BARRA +"ValidationMessages.properties"), this.urlProject + BARRA + "src"+ BARRA +"main"+ BARRA +"resources"+ BARRA +"ValidationMessages.properties");
			Utils.doCopyFile(new File("EsquadroSB"+ BARRA +".factorypath"), this.urlProject + BARRA +".factorypath");
			
			consoleLog.setText("Criando pastas de Recursos...");
			new File(this.urlProject + BARRA + "src"+ BARRA +"main"+ BARRA +"resources"+ BARRA +"db"+ BARRA +"migration").mkdirs();
			new File(this.urlProject + BARRA +"src"+ BARRA +"main"+ BARRA +"resources"+ BARRA +"hibernate").mkdirs();
			consoleLog.setText("Pastas Criadas!");

			consoleLog.setText("Processamento Finalizado!");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void configurarConexao(BancoDados bancoDados) throws IOException {
		List<String> readApplication = Utils.readTxtList(this.urlProject + "/src/main/resources/application.properties");
		
		List<String> properties = new ArrayList<>();
		
		checkExit(readApplication, properties, "server.port=8080");		
		checkExit(readApplication, properties, "spring.profiles.active=prod");
		properties.add("");
		checkExit(readApplication, properties, "#JSON");
		checkExit(readApplication, properties, "spring.jackson.deserialization.fail-on-unknown-properties=true");
		checkExit(readApplication, properties, "spring.jackson.date-format=yyyy-MM-dd");
		checkExit(readApplication, properties, "spring.jackson.serialization.FAIL_ON_EMPTY_BEANS=false");

		Utils.writeTxtList(this.urlProject + "/src/main/resources/application.properties", properties, true);

		
		// ----------------------- NOVO -----------------------------------------------
		
		File prod = new File(this.urlProject + "/src/main/resources/application-prod.properties");
		prod.createNewFile();
		
		List<String> listProd = new ArrayList<>();

		listProd.add("spring.datasource.url= jdbc:oracle:thin:@//" + bancoDados.getIp() + ":" + bancoDados.getPorta() + "/" + bancoDados.getNameBd());		
		listProd.add("spring.datasource.username=" + bancoDados.getUsuario());
		listProd.add("spring.datasource.password=" + bancoDados.getSenha() );
		listProd.add("");
		listProd.add("spring.datasource.driver-class-name=oracle.jdbc.OracleDriver");
		listProd.add("");
		listProd.add("#hibernate config");
		listProd.add("spring.jpa.database-platform=org.hibernate.dialect.Oracle10gDialect");		
		listProd.add("spring.jpa.show-sql=false");
		listProd.add("spring.jpa.properties.hibernate.format_sql=false");

		
		Utils.writeTxtList(this.urlProject + "/src/main/resources/application-prod.properties", listProd, true);
		
		File dev = new File(this.urlProject + "/src/main/resources/application-dev.properties");
		dev.createNewFile();
		
		List<String> listDev = new ArrayList<>();
		listDev.add("spring.datasource.url= jdbc:oracle:thin:@//" + bancoDados.getIp() + ":" + bancoDados.getPorta() + "/" + bancoDados.getNameBd());		
		listDev.add("spring.datasource.username=" + bancoDados.getUsuario());
		listDev.add("spring.datasource.password=" + bancoDados.getSenha() );
		listDev.add("");
		listDev.add("spring.datasource.driver-class-name=oracle.jdbc.OracleDriver");
		listDev.add("");
		listDev.add("#hibernate config");
		listDev.add("spring.jpa.database-platform=org.hibernate.dialect.Oracle10gDialect");		
		listDev.add("spring.jpa.show-sql=true");
		listDev.add("spring.jpa.properties.hibernate.format_sql=true");
		
		Utils.writeTxtList(this.urlProject + "/src/main/resources/application-dev.properties", listDev, true);
		
	}

	private void processandoPOM(String urlProject, List<DEPEND> list) throws IOException {
		List<Dependencia> listDependencia = DependenciasPOM.listPom;

		List<Dependencia> instalar = new ArrayList<Dependencia>();

		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < listDependencia.size(); j++) {
				if (list.get(i) == listDependencia.get(j).getNome()) {
					instalar.add(listDependencia.get(j));
				}
			}
		}

		
		List<String> readPOM = Utils.readTxtList(urlProject + "/pom.xml");

		for (int i = 0; i < instalar.size(); i++) {

			boolean control = false;

			for (int j = 0; j < readPOM.size(); j++) {

				if (("<artifactId>" + instalar.get(i).getArtifactId() + "</artifactId>")
						.equals(readPOM.get(j).trim())) {
					control = true;		
				}

			}

			if (control) {
				instalar.set(i, null);
			}

		}

		for (int i = 0; i < readPOM.size(); i++) {

			if (readPOM.get(i).trim().equals("<properties>")) {
				String espaco = readPOM.get(i).substring(0, readPOM.get(i).indexOf("<"));
				espaco += "	";

				StringBuilder sb = new StringBuilder();
				sb.append(espaco + "<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>" + "\n");
				sb.append(espaco + "<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>");
				readPOM.set(i, readPOM.get(i) + "\n" + sb.toString());
			}

			if (readPOM.get(i).trim().equals("<dependencies>")) {
				String espaco = readPOM.get(i).substring(0, readPOM.get(i).indexOf("<"));
				espaco += "	";
				StringBuilder dp = new StringBuilder();

				for (int j = 0; j < instalar.size(); j++) {

					if (instalar.get(j) != null) {

						dp.append("\n");
						dp.append(espaco + "<!-- " + instalar.get(j).getNome().toString() + " ESQUADRO SB "
								+ Statics.VERSION + " -->\n");// NOME
						dp.append(espaco + "<dependency>\n");
						dp.append(espaco + "	<groupId>" + instalar.get(j).getGroupId() + "</groupId>\n");// GROUP_ID
						dp.append(espaco + "	<artifactId>" + instalar.get(j).getArtifactId() + "</artifactId>\n");// ARTIFACT_ID

						if (instalar.get(j).getVersion() != null) {
							dp.append(espaco + "	<version>" + instalar.get(j).getVersion() + "</version>\n");// VERSION
						}

						if (instalar.get(j).getScope() != null) {
							dp.append(espaco + "	<scope>" + instalar.get(j).getScope() + "</scope>\n");// SCOPE
						}

						if (instalar.get(j).getOptional() != null) {
							dp.append(espaco + "	<optional>" + instalar.get(j).getOptional() + "</optional>\n");// OPTIONAL
						}

						dp.append(espaco + "</dependency>\n");

					}

				}

				readPOM.set(i, readPOM.get(i) + "\n" + dp.toString());
			}

		}

		Utils.writeTxtList(urlProject + "/pom.xml", readPOM, true);
	}

	public void replacePackage(File classFile, String packBase, String packages) throws IOException {
		List<String> readTxtList = Utils.readTxtList(classFile.getAbsolutePath());

		for (int i = 0; i < readTxtList.size(); i++) {
			if (readTxtList.get(i).contains("package")) {
				readTxtList.set(i, "package " + packBase + packages + ";\n");
			}

			if (readTxtList.get(i).contains("{{pack}}")) {
				readTxtList.set(i, readTxtList.get(i).replace("{{pack}}", packBase));
			}
		}

		Utils.writeTxtList(classFile.getAbsolutePath(), readTxtList, true);

	}

	private void checkExit(List<String> list,List<String> addProperties, String line) {
		boolean control = false;
		
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).equals(line)) {
				control = true;
			}
		}

		if(!control) {
			addProperties.add(line);
		}
		
	}
	
}
