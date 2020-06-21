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
			packages = packages.replace(".", "/");

			consoleLog.setText("Criando Pasta...");
			File file = new File(this.urlProject + "/" + packages);

			if (!file.exists()) {
				file.mkdirs();
			}

			consoleLog.setText("OK!");

			consoleLog.setText("Copy all...");
			consoleLog.setText("Copiando Arquivos para:" + this.urlProject + "/src/main/java/" + packages);
			Utils.doCopyDirectory(new File("EsquadroSB/copyAll"), this.urlProject + "/src/main/java/" + packages,
					false);
			consoleLog.setText("OK!");

			consoleLog.setText("Replace Package...");

			replacePackage(
					new File(this.urlProject + "/src/main/java/" + packages + "/config/property/" + "ApiProperty.java"),
					packages.replace("/", "."), ".config.property");
			replacePackage(new File(this.urlProject + "/src/main/java/" + packages + "/cors/" + "CorsFilter.java"),
					packages.replace("/", "."), ".cors");
			replacePackage(new File(
					this.urlProject + "/src/main/java/" + packages + "/event/listener/" + "RecursoCriadoListener.java"),
					packages.replace("/", "."), ".event.listener");
			replacePackage(
					new File(this.urlProject + "/src/main/java/" + packages + "/event/" + "RecursoCriadoEvent.java"),
					packages.replace("/", "."), ".event");
			replacePackage(new File(this.urlProject + "/src/main/java/" + packages + "/exceptionhandler/"
					+ "PersonalExceptionHandler.java"), packages.replace("/", "."), ".exceptionhandler");

			replacePackage(new File(this.urlProject + "/src/main/java/" + packages + "/utils/" + "Utils.java"),
					packages.replace("/", "."), ".utils");

			replacePackage(new File(this.urlProject + "/src/main/java/" + packages + "/config/" + "SwaggerConfig.java"),
					packages.replace("/", "."), ".config");

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
			
			Utils.doCopyFile(new File("EsquadroSB/messages_pt_BR.properties"), this.urlProject + "/src/main/resources/messages_pt_BR.properties");
			Utils.doCopyFile(new File("EsquadroSB/messages.properties"), this.urlProject + "/src/main/resources/messages.properties");
			Utils.doCopyFile(new File("EsquadroSB/ValidationMessages.properties"), this.urlProject + "/src/main/resources/ValidationMessages.properties");

			consoleLog.setText("Criando pastas de Recursos...");
			new File(this.urlProject + "/src/main/resources/db/migration").mkdirs();
			new File(this.urlProject + "/src/main/resources/hibernate").mkdirs();
			consoleLog.setText("Pastas Criadas!");

			consoleLog.setText("Processamento Finalizado!");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void configurarConexao(BancoDados bancoDados) throws IOException {
		List<String> readApplication = Utils.readTxtList(this.urlProject + "/src/main/resources/application.properties");
		StringBuilder sb = new StringBuilder();

		if (bancoDados.getTipo().equalsIgnoreCase("MySQL")) {// MYSQL
			sb.append("# CONEXAO MYSQL" + "\n");
			sb.append("spring.jpa.database=MYSQL" + "\n");
			sb.append("spring.datasource.url=jdbc:mysql://" + bancoDados.getIp() + ":" + bancoDados.getPorta() + "/"
					+ bancoDados.getNameBd()
					+ "?createDatabaseIfNotExist=true&useSSL=false&useTimezone=true&serverTimezone=UTC" + "\n");
			sb.append("spring.datasource.username=" + bancoDados.getUsuario() + "" + "\n");
			sb.append("spring.datasource.password=" + bancoDados.getSenha() + "\n");

		} else if (bancoDados.getTipo().equalsIgnoreCase("ORACLE")) { // ORACLE
			sb.append("# CONEXAO ORACLE" + "\n");
			sb.append("spring.datasource.url= jdbc:oracle:thin:@//" + bancoDados.getIp() + ":" + bancoDados.getPorta()
					+ "/" + bancoDados.getNameBd() + "\n");
			sb.append("spring.datasource.username=" + bancoDados.getUsuario() + "\n");
			sb.append("spring.datasource.password=" + bancoDados.getSenha() + "\n");
			sb.append("spring.datasource.driver-class-name=oracle.jdbc.OracleDriver" + "\n");
			sb.append("\n");
			sb.append("#hibernate config" + "\n");
			sb.append("spring.jpa.database-platform=org.hibernate.dialect.Oracle10gDialect" + "\n");
		}

		sb.append("\n");
		sb.append("# SERVER PORT" + "\n");
		sb.append("server.port=8080" + "\n");
		sb.append("\n");
		sb.append("spring.jpa.hibernate.ddl-auto=none" + "\n");
		sb.append("spring.jpa.show-sql=true" + "\n");
		sb.append("spring.jpa.properties.hibernate.use_sql_comments=true" + "\n");
		sb.append("spring.jpa.properties.hibernate.format_sql=true" + "\n");
		sb.append("spring.jpa.properties.hibernate.type=trace" + "\n");
		sb.append("\n");
		sb.append("spring.jackson.deserialization.fail-on-unknown-properties=true" + "\n");
		sb.append("spring.jackson.serialization.FAIL_ON_EMPTY_BEANS=false" + "\n");

		if (readApplication.size() > 0) {
			readApplication.set(0, sb.toString() + "\n" + readApplication.get(0));
		} else {
			readApplication.add(sb.toString());
		}

		Utils.writeTxtList(this.urlProject + "/src/main/resources/application.properties", readApplication, true);

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

		System.err.println("" + urlProject + "/pom.xml");
		List<String> readPOM = Utils.readTxtList(urlProject + "/pom.xml");

		for (int i = 0; i < instalar.size(); i++) {

			boolean control = false;

			for (int j = 0; j < readPOM.size(); j++) {

				if (("<artifactId>" + instalar.get(i).getArtifactId() + "</artifactId>")
						.equals(readPOM.get(j).trim())) {
					control = true;
					System.err.println("Encontrado: " + instalar.get(i).getArtifactId());
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

}
