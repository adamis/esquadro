/**
 * 
 */
package br.com.esquadro.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adami
 *
 */
public class DependenciasPOM {

	public static List<Dependencia> listPom = contructPom();

	public enum DEPEND {
		LOMBOK, COMMONS, SPRING_SECURITY, SPRING_SECURITY_TESTE, H2, FLYWAY, JPA, MODEL_GEN, MYSQL, ORACLE, EHCACHE,
		SWAGGER, SWAGGER_UI, SPRING_EMAIL, MODEL_MAPPER, CONFIGURATION, METAMODEL_GEN, DEV_TOOLS
	}

	private static List<Dependencia> contructPom() {
		List<Dependencia> response = new ArrayList<Dependencia>();

		Dependencia dependencia;

		// LOMBOK
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.LOMBOK);
		dependencia.setGroupId("org.projectlombok");
		dependencia.setArtifactId("lombok");
		dependencia.setOptional("true");
		response.add(dependencia);

		// COMMONS
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.COMMONS);
		dependencia.setGroupId("org.apache.commons");
		dependencia.setArtifactId("commons-lang3");
		response.add(dependencia);

		// Spring Security
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.SPRING_SECURITY);
		dependencia.setGroupId("org.springframework.boot");
		dependencia.setArtifactId("spring-boot-starter-security");
		response.add(dependencia);

		// Spring Security Teste
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.SPRING_SECURITY_TESTE);
		dependencia.setGroupId("org.springframework.security");
		dependencia.setArtifactId("spring-security-test");
		response.add(dependencia);

		// H2
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.H2);
		dependencia.setGroupId("com.h2database");
		dependencia.setArtifactId("h2");
		response.add(dependencia);

		// FLYWAY
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.FLYWAY);
		dependencia.setGroupId("org.flywaydb");
		dependencia.setArtifactId("flyway-core");
		response.add(dependencia);

		// JPA
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.JPA);
		dependencia.setGroupId("org.springframework.boot");
		dependencia.setArtifactId("spring-boot-starter-data-jpa");
		response.add(dependencia);

		// METAMODEL_GEN
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.MODEL_GEN);
		dependencia.setGroupId("org.hibernate");
		dependencia.setArtifactId("hibernate-jpamodelgen");
		response.add(dependencia);

		// MYSQL
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.MYSQL);
		dependencia.setGroupId("mysql");
		dependencia.setArtifactId("mysql-connector-java");
		response.add(dependencia);

		// ORACLE
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.ORACLE);
		dependencia.setGroupId("com.oracle.ojdbc");
		dependencia.setArtifactId("ojdbc8");
		dependencia.setScope("runtime");
		response.add(dependencia);

		// EhCache
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.EHCACHE);
		dependencia.setGroupId("net.sf.ehcache");
		dependencia.setArtifactId("ehcache");
		response.add(dependencia);

		// Swagger
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.SWAGGER);
		dependencia.setGroupId("io.springfox");
		dependencia.setArtifactId("springfox-swagger2");
		dependencia.setVersion("2.9.2");
		response.add(dependencia);

		// Swagger UI
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.SWAGGER_UI);
		dependencia.setGroupId("io.springfox");
		dependencia.setArtifactId("springfox-swagger-ui");
		dependencia.setVersion("2.9.2");
		response.add(dependencia);

		// SPRING EMAIL
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.SPRING_EMAIL);
		dependencia.setGroupId("org.springframework.boot");
		dependencia.setArtifactId("spring-boot-starter-mail");
		response.add(dependencia);

		// MODEL MAPPER
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.MODEL_MAPPER);
		dependencia.setGroupId("org.modelmapper");
		dependencia.setArtifactId("modelmapper");
		dependencia.setVersion("2.3.0");
		response.add(dependencia);

		// CONFIGURATION
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.CONFIGURATION);
		dependencia.setGroupId("org.springframework.boot");
		dependencia.setArtifactId("spring-boot-configuration-processor");
		dependencia.setOptional("true");
		response.add(dependencia);

		// META MODEL GEN
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.METAMODEL_GEN);
		dependencia.setGroupId("org.hibernate");
		dependencia.setArtifactId("hibernate-jpamodelgen");
		response.add(dependencia);

		// DEV_TOOLS
		dependencia = new Dependencia();
		dependencia.setNome(DEPEND.DEV_TOOLS);
		dependencia.setGroupId("org.springframework.boot");
		dependencia.setArtifactId("spring-boot-devtools");
		response.add(dependencia);

		return response;
	}

}
