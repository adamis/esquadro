/**
 * 
 */
package br.com.esquadro.controler;

import java.io.File;
import java.util.List;

import br.com.esquadro.model.Struckts;
import br.com.esquadro.util.DatabaseUtils;
import br.com.esquadro.util.Utils;
import br.com.esquadro.view.ConsoleLog;

/**
 * @author Adami
 *
 */
public class AddAPIController implements Runnable {

	private String packsBase;

	private List<String> listEntity;
	String urlProject;

	private boolean chkFilter;
	private boolean chkResource;
	private boolean chkRepository;
	private boolean chkServices;
	private boolean chkRepositoryImpl;

	private String packsEntity;
	private String packsFilter;
	private String packsResource;
	private String packsRepository;
	private String packsServices;
	private String packsRepositoryImpl;

	private ConsoleLog consoleLog;
	private DatabaseUtils databaseUtils;

	public AddAPIController(ConsoleLog consoleLog, String urlProject, String packsBase, String packsEntity,
			boolean chkFilter, String packsFilter, boolean chkResource, String packsResource, boolean chkRepository,
			String packsRepository, boolean chkServices, String packsServices, boolean chkRepositoryImpl,
			String packsRepositoryImpl, List<String> listEntity, DatabaseUtils databaseUtils) {

		this.consoleLog = consoleLog;
		this.urlProject = urlProject;
		this.packsBase = packsBase;

		this.packsEntity = packsEntity;
		this.chkFilter = chkFilter;
		this.packsFilter = packsFilter;
		this.chkResource = chkResource;
		this.packsResource = packsResource;
		this.chkRepository = chkRepository;
		this.packsRepository = packsRepository;
		this.chkServices = chkServices;
		this.packsServices = packsServices;
		this.chkRepositoryImpl = chkRepositoryImpl;
		this.packsRepositoryImpl = packsRepositoryImpl;
		this.listEntity = listEntity;

		this.databaseUtils = databaseUtils;
	}

	@Override
	public void run() {

		consoleLog.setText("Iniciando Processamento da API ...");

		for (int i = 0; i < this.listEntity.size(); i++) {

			try {

				Struckts struckts = new Struckts(listEntity.get(i), packsBase, packsEntity, packsResource, packsFilter,
						packsRepository, packsServices, packsRepositoryImpl, databaseUtils, consoleLog);

				consoleLog.setText("Entity identificado: " + Utils.normalizerStringCaps(listEntity.get(i)));

				if (chkResource) {
					consoleLog.setText(
							"Gerando Resource: " + Utils.normalizerStringCaps(listEntity.get(i)) + "Resouce.java");
					String resource = struckts.getResource();
					Utils.writeTxt(urlProject + "/src/main/java/" + getBarra(packsResource) + "/"
							+ Utils.normalizerStringCaps(listEntity.get(i)) + "Resource.java", resource, true);
					consoleLog.setText("Gerando OK!");
				}

				if (chkRepository) {
					consoleLog.setText(
							"Gerando Repository: " + Utils.normalizerStringCaps(listEntity.get(i)) + "Repository.java");
					String repository = struckts.getRepository();
					Utils.writeTxt(
							urlProject + "/src/main/java/" + getBarra(packsRepository) + "/"
									+ Utils.normalizerStringCaps(listEntity.get(i)) + "Repository.java",
							repository, true);
					consoleLog.setText("Gerando OK!");
				}

				if (chkFilter) {
					consoleLog.setText(
							"Gerando Filter: " + Utils.normalizerStringCaps(listEntity.get(i)) + "Filter.java");
					String filter = struckts.getFilter();
					Utils.writeTxt(urlProject + "/src/main/java/" + getBarra(packsFilter) + "/"
							+ Utils.normalizerStringCaps(listEntity.get(i)) + "Filter.java", filter, true);
					consoleLog.setText("Gerando OK!");
				}

				if (chkServices) {
					consoleLog.setText(
							"Gerando Service: " + Utils.normalizerStringCaps(listEntity.get(i)) + "Service.java");
					String services = struckts.getServices();
					Utils.writeTxt(urlProject + "/src/main/java/" + getBarra(packsServices) + "/"
							+ Utils.normalizerStringCaps(listEntity.get(i)) + "Service.java", services, true);
					consoleLog.setText("Gerando OK!");
				}

				if (chkRepositoryImpl) {
					consoleLog.setText("Gerando RepositoryImpl: " + Utils.normalizerStringCaps(listEntity.get(i))
							+ "RepositoryImpl.java e Gerando RepositoryQuery: "
							+ Utils.normalizerStringCaps(listEntity.get(i)) + "RepositoryQuery.java");

					String packFolder = packsRepositoryImpl.replace("{{EntityFolder}}",
							Utils.normalizerStringCommomNotCap(listEntity.get(i)));

					if (!new File(packFolder.replace(".", "/")).exists()) {
						File file = new File(packFolder.replace(".", "/"));
						file.mkdirs();
					}

					String repositoryImpl = struckts.getRepositoryImpl();
					String repositoryQuery = struckts.getRepositoryQuery();

					Utils.writeTxt(
							urlProject + "/src/main/java/" + getBarra(packFolder) + "/"
									+ Utils.normalizerStringCaps(listEntity.get(i)) + "RepositoryImpl.java",
							repositoryImpl, true);
					Utils.writeTxt(
							urlProject + "/src/main/java/" + getBarra(packFolder) + "/"
									+ Utils.normalizerStringCaps(listEntity.get(i)) + "RepositoryQuery.java",
							repositoryQuery, true);
					consoleLog.setText("Gerando OK!");
				}

			} catch (Exception e) {
				consoleLog.setText("ERRO> " + e.getMessage());
				e.printStackTrace();
			}

			consoleLog.setText("Finalizado Processamento da API!");
		}

	}

	private String getBarra(String pack) {
		return pack.replace(".", "/");
	}

}
