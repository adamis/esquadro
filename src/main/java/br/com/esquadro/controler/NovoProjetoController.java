/**
 * 
 */
package br.com.esquadro.controler;

import java.awt.Component;
import java.awt.Container;
import java.awt.TextField;
import java.io.BufferedReader;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.esquadro.util.Statics;
import br.com.esquadro.util.Utils;
import br.com.esquadro.view.ConsoleLog;

/**
 * @author adamis.rocha
 *
 */
public class NovoProjetoController implements Runnable {

	Container container;
	JButton btnGerarProjeto;
	ConsoleLog consoleLog;
	TextField inputWorkspace;
	JTextField textField;
	JInternalFrame jInternalFrame;

	JRadioButton rdbtnSass;
	JRadioButton rdbtnLess;
	JRadioButton rdbtnScss;
	JCheckBox chckbxSpec;
	JRadioButton rdbtnRight;
	JRadioButton rdbtnLeft;
	JRadioButton rdbtnTop;
	JTextField inputUrlBackEnd;

	public NovoProjetoController(Container container, JButton btnGerarProjeto, ConsoleLog consoleLog,
			TextField inputWorkspace, JTextField textField, JInternalFrame jInternalFrame, JRadioButton rdbtnSass,
			JRadioButton rdbtnLess, JRadioButton rdbtnScss, JCheckBox chckbxSpec, JRadioButton rdbtnRight,
			JRadioButton rdbtnLeft, JRadioButton rdbtnTop, JTextField inputUrlBackEnd) {
		this.container = container;
		this.btnGerarProjeto = btnGerarProjeto;
		this.consoleLog = consoleLog;
		this.inputWorkspace = inputWorkspace;
		this.textField = textField;
		this.jInternalFrame = jInternalFrame;

		this.rdbtnSass = rdbtnSass;
		this.rdbtnLess = rdbtnLess;
		this.rdbtnScss = rdbtnScss;
		this.chckbxSpec = chckbxSpec;
		this.rdbtnRight = rdbtnRight;
		this.rdbtnLeft = rdbtnLeft;
		this.rdbtnTop = rdbtnTop;
		this.inputUrlBackEnd = inputUrlBackEnd;
	}

	@Override
	public void run() {

		for (int i = 0; i < this.container.getComponents().length; i++) {
			Component component = this.container.getComponent(i);
			component.setEnabled(false);
		}

		btnGerarProjeto.setText("Processando...");

		consoleLog.setVisible(true);
		consoleLog.moveToFront();

		try {

			CheckNodeAngularCLI angularCLI = new CheckNodeAngularCLI(consoleLog);
			if (angularCLI.checkMinimalWorkspace()) {
				String style = "";
				if (rdbtnSass.isSelected()) {
					style = "--style=sass";
				} else if (rdbtnLess.isSelected()) {
					style = "--style=less";
				} else if (rdbtnScss.isSelected()) {
					style = "--style=scss";
				}

				String spec = "";
				if (chckbxSpec.isSelected()) {
					spec = "--skipTests=false";
				} else {
					spec = "--skipTests=true";
				}

				// new File(inputWorkspace.getText()+"/"+Statics.MODULE_NAME+"").mkdirs();

				String bat = inputWorkspace.getText().substring(0, 2) + " & " + "cd " + inputWorkspace.getText() + " & "
						+ " ng new " + textField.getText() + " --skip-git " + style
						+ " --skipInstall=true --routing=true " + spec + " & " + "cacls " + textField.getText()
						+ " /E /P Total:F" + " & " + " cd " + textField.getText() + " & " + " ng g m "
						+ Statics.MODULE_NAME + "/home --routing=true " // + spec
						+ " & " + " ng g c " + Statics.MODULE_NAME + "/home --export " // + spec
						+ " & " + " ng g m core " // + spec
						+ " & " + " ng g c core/navbar --export " // + spec
						+ " & " + " ng g s core/error-handler " // + spec
						+ " & " + " ng g m shared " // + spec
				// + " & " + " ng g c shared/message --export "+ spec
				;

				// INSTALAÇÃO DOS RECURSOS(NPM)
				bat += " & " + " npm cache verify" + " & " + " npm i primeng@~9 --save " + " & "
						+ " npm i --save quill " + " & " + " npm i --save @fullcalendar/core " + " & "
						+ " npm i @angular/animations --save " + " & " + " npm i primeicons --save " + " & "
						+ " npm i primeflex --save " + " & " + " npm i @angular/cdk --save " + " & "
						+ " npm i moment  --save " + " & " + " npm install ngx-currency --save" // MASK MONEY
																								// https://www.npmjs.com/package/ngx-currency
						+ " & " + " npm i angular-imask --save" + " & " + " npm i chart.js --save" + " & "
						+ " npm i ngx-show-hide-password --save" + " & "
						+ " npm i @fortawesome/angular-fontawesome --save" + " & "
						+ " npm i @fortawesome/fontawesome-svg-core --save" + " & "
						+ " npm i @fortawesome/free-solid-svg-icons --save"
				// +" & "//ADD JQUERY
				// +" npm install jquery --save"//ADD JQUERY
				;

				// System.err.println(bat);

				ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", bat);

				pb.redirectErrorStream(true);
				Process p = pb.start();

				BufferedReader output = Utils.getOutput(p);
				BufferedReader error = Utils.getError(p);

				String ligne = "";

				consoleLog.setText("Criando novo Projeto: " + textField.getText());

				while ((ligne = output.readLine()) != null) {
					consoleLog.setText(ligne);
				}

				while ((ligne = error.readLine()) != null) {
					consoleLog.setText(ligne);
				}

				int waitFor = p.waitFor();

				if (waitFor != 0) {
					System.err.println("ERROR");

					Object[] options = { "OK" };

					JOptionPane.showOptionDialog(null, "Erro ao criar o Projeto!", "Fail", JOptionPane.OK_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				} else {

					consoleLog.setText("Ajustando package.json ...");
					configurePackageJson();
					consoleLog.setText("OK");

					consoleLog.setText("Ajustando styles.css ...");
					ajustStyle();
					consoleLog.setText("OK");

					consoleLog.setText("Copiando e Reescrevendo Core e Shared Module ...");
					copyfiles();
					consoleLog.setText("OK");

					consoleLog.setText("Ajustando app.module.ts ...");
					ajustaAppModule();
					consoleLog.setText("OK");

					consoleLog.setText("Ajustando environments.ts ...");
					ajustaEnviroment();
					consoleLog.setText("OK");

					// OK
					messageOK();

				}
			} else {
				consoleLog.setText("FAIL");
				consoleLog.setText("Tente novamente após ter instalado no NODE!");
			}
//			System.err.println("FIM");

		} catch (Exception e) {
			consoleLog.setText("ERRO> " + e.getMessage());
			e.printStackTrace();
		}

		for (int i = 0; i < this.container.getComponents().length; i++) {
			Component component = this.container.getComponent(i);
			component.setEnabled(true);
		}
		btnGerarProjeto.setText("Gerar Projeto");
	}

	private void configurePackageJson() throws Exception {

		List<String> readTxtList = Utils
				.readTxtList(inputWorkspace.getText() + "/" + textField.getText() + "/package.json");

		for (int i = 0; i < readTxtList.size(); i++) {

			if (readTxtList.get(i).contains("\"start\"")) {
				readTxtList.set(i, " 	\"start\": \"ng serve --open\",");
			}

			if (readTxtList.get(i).contains("\"build\"")) {
				readTxtList.set(i, " 	\"build\": \"ng b --optimization=true --baseHref= --outputHashing=all\",");
			}

			if (readTxtList.get(i).contains("\"lint\"")) {
				readTxtList.set(i,
						readTxtList.get(i) + "\n 	\"pro\": \"ng build --prod --baseHref= --outputHashing=all\",");
			}
		}

		Utils.writeTxtList(inputWorkspace.getText() + "/" + textField.getText() + "/package.json", readTxtList, true);

	}

	private void ajustaAppModule() throws Exception {
		String urlFile = inputWorkspace.getText() + "/" + textField.getText() + "/src/app/app.module.ts";
		List<String> readTxtList = Utils.readTxtList(urlFile);

		for (int i = 0; i < readTxtList.size(); i++) {

			if (readTxtList.get(i).contains("/home/home/")) {
				readTxtList.set(i, readTxtList.get(i).replace("/home/home/", "/home/"));
			}

			if (readTxtList.get(i).contains("import { HomeComponent }")) {
				readTxtList.remove(i);
			} else if (readTxtList.get(i).contains("exports: [")) {
				readTxtList.set(i, readTxtList.get(i).replace("HomeComponent,", "").replace("HomeComponent", ""));
			} else if (readTxtList.get(i).contains("HomeComponent")) {
				readTxtList.set(i - 1, readTxtList.get(i - 1).replace(",", ""));
				readTxtList.remove(i);
			}

			if (readTxtList.get(i).contains("import { NavbarComponent }")) {
				readTxtList.remove(i);
			} else if (readTxtList.get(i).contains("exports: [")) {
				readTxtList.set(i, readTxtList.get(i).replace("NavbarComponent,", "").replace("NavbarComponent", ""));
			} else if (readTxtList.get(i).contains("NavbarComponent")) {
				readTxtList.set(i - 1, readTxtList.get(i - 1).replace(",", ""));
				readTxtList.remove(i);
			}

			if (readTxtList.get(i).contains("@NgModule")) {
				readTxtList.set(i, "import { CoreModule } from './core/core.module';\n"
						+ "import {SidebarModule} from 'primeng/sidebar';\n"
						+ "import { BrowserAnimationsModule } from '@angular/platform-browser/animations';\n"
						+ "import { MenubarModule } from 'primeng/menubar';\n"
						+ "import { SharedModule } from './shared/shared.module';\n\n" + readTxtList.get(i) + "\n");
			}

			if (readTxtList.get(i).contains("imports: [") || readTxtList.get(i).contains("imports:[")) {
				readTxtList.set(i, "  " + readTxtList.get(i).trim()
						+ "\n    CoreModule,\n    SharedModule,\n    BrowserAnimationsModule,\n    SidebarModule,\n    MenubarModule,");
			}

		}

		Utils.writeTxtList(urlFile, readTxtList, true);

	}

	private void copyfiles() throws Exception {
		String urlFile = inputWorkspace.getText() + "/" + textField.getText() + "/";

		if (new File("samples").exists()) {

			// SHARED
			Utils.doCopyDirectory(new File("samples/shared"), urlFile + "src/app/shared");

			// HOME
			Utils.doCopyDirectory(new File("samples/home"), urlFile + "src/app/" + Statics.MODULE_NAME + "/home");

			// SEGURANCA
			Utils.doCopyDirectory(new File("samples/seguranca"), urlFile + "src/app/seguranca");

			// IMAGES
			Utils.doCopyDirectory(new File("samples/images"), urlFile + "src/assets");

			// THEME
			Utils.doCopyDirectory(new File("samples/theme"), urlFile + "src/theme");

			// ENVIRONMENTS
			Utils.doCopyFile(new File("samples/environments/environment.ts"),
					urlFile + "src/environments/environment.ts");

			// ENVIRONMENTS.PROD
			Utils.doCopyFile(new File("samples/environments/environment.prod.ts"),
					urlFile + "src/environments/environment.prod.ts");

			// CORE
			Utils.doCopyFile(new File("samples/core/core.module.ts"), urlFile + "src/app/core/core.module.ts");
			Utils.doCopyFile(new File("samples/core/pagina-nao-encontrada.component.ts"),
					urlFile + "src/app/core/pagina-nao-encontrada.component.ts");
			Utils.doCopyFile(new File("samples/core/error-handler.service.ts"),
					urlFile + "src/app/core/error-handler.service.ts");

			// STYLES
			Utils.doCopyFile(new File("samples/style-principal/styles.css"), urlFile + "src/styles.css");

			// APP >8
			Utils.doCopyFile(new File("samples/app-component-start/app.component.html"),
					urlFile + "src/app/app.component.html");
			Utils.doCopyFile(new File("samples/app-component-start/app.component.css"),
					urlFile + "src/app/app.component.css");
			Utils.doCopyFile(new File("samples/app-component-start/app.component.ts"),
					urlFile + "src/app/app.component.ts");
			Utils.doCopyFile(new File("samples/app-component-start/index.html"), urlFile + "src/index.html");
			Utils.doCopyFile(new File("samples/app-component-start/app-routing.module.ts"),
					urlFile + "src/app/app-routing.module.ts");
			// .editorconfig
			Utils.doCopyFile(new File("samples/app-component-start/.editorconfig"), urlFile + ".editorconfig");

			// NAVBAR
			if (rdbtnRight.isSelected()) {
				Utils.doCopyFile(new File("samples/navbar-right/navbar.component.css"),
						urlFile + "src/app/core/navbar/navbar.component.css");
				Utils.doCopyFile(new File("samples/navbar-right/navbar.component.ts"),
						urlFile + "src/app/core/navbar/navbar.component.ts");
				Utils.doCopyFile(new File("samples/navbar-right/navbar.component.html"),
						urlFile + "src/app/core/navbar/navbar.component.html");
			} else if (rdbtnLeft.isSelected()) {
				Utils.doCopyFile(new File("samples/navbar-left/navbar.component.css"),
						urlFile + "src/app/core/navbar/navbar.component.css");
				Utils.doCopyFile(new File("samples/navbar-left/navbar.component.ts"),
						urlFile + "src/app/core/navbar/navbar.component.ts");
				Utils.doCopyFile(new File("samples/navbar-left/navbar.component.html"),
						urlFile + "src/app/core/navbar/navbar.component.html");
			} else if (rdbtnTop.isSelected()) {
				Utils.doCopyFile(new File("samples/navbar-top/navbar.component.css"),
						urlFile + "src/app/core/navbar/navbar.component.css");
				Utils.doCopyFile(new File("samples/navbar-top/navbar.component.ts"),
						urlFile + "src/app/core/navbar/navbar.component.ts");
				Utils.doCopyFile(new File("samples/navbar-top/navbar.component.html"),
						urlFile + "src/app/core/navbar/navbar.component.html");
			}
		} else {
			this.consoleLog.setText("Pasta Sample não encontrada!");
			throw new Exception("Pasta Sample não encontrada!");
		}

	}

	private void ajustaEnviroment() throws Exception {

		// ENVIRONMENTS
		String urlFile = inputWorkspace.getText() + "/" + textField.getText() + "/src/environments/environment.ts";
		List<String> readTxtList = Utils.readTxtList(urlFile);

		for (int i = 0; i < readTxtList.size(); i++) {
			if (inputUrlBackEnd.getText().substring(inputUrlBackEnd.getText().length() - 1).equals("/")) {
				readTxtList.set(i, readTxtList.get(i).replace("{apiBackEnd}", inputUrlBackEnd.getText()));
			} else {
				readTxtList.set(i, readTxtList.get(i).replace("{apiBackEnd}", inputUrlBackEnd.getText() + "/"));
			}
		}

		Utils.writeTxtList(urlFile, readTxtList, true);

		// ENVIRONMENTS.PROD
		urlFile = inputWorkspace.getText() + "/" + textField.getText() + "/src/environments/environment.prod.ts";
		readTxtList = Utils.readTxtList(urlFile);

		for (int i = 0; i < readTxtList.size(); i++) {
			if (inputUrlBackEnd.getText().substring(inputUrlBackEnd.getText().length() - 1).equals("/")) {
				readTxtList.set(i, readTxtList.get(i).replace("{apiBackEnd}", inputUrlBackEnd.getText()));
			} else {
				readTxtList.set(i, readTxtList.get(i).replace("{apiBackEnd}", inputUrlBackEnd.getText() + "/"));
			}
		}

		Utils.writeTxtList(urlFile, readTxtList, true);

	}

	private void ajustStyle() throws Exception {

		// ANGULAR CLI >=6
		String urlFile = inputWorkspace.getText() + "/" + textField.getText() + "/angular.json";

		// ANGULAR CLI <6
		// String urlFile =
		// inputWorkspace.getText()+"/"+textField.getText()+"/.angular-cli.json";

		String readTxt = Utils.readTxt(urlFile);

		// ANGULAR CLI >=6
		JSONObject jsonObject = new JSONObject(readTxt);
		JSONObject jsonProjects = jsonObject.getJSONObject("projects");
		JSONObject jsonProjeto = jsonProjects.getJSONObject(textField.getText().trim());
		JSONObject jsonArchitect = jsonProjeto.getJSONObject("architect");
		JSONObject jsonBuild = jsonArchitect.getJSONObject("build");
		JSONObject jsonOptions = jsonBuild.getJSONObject("options");

		/*
		 * //ANGULAR CLI <6 JSONObject jsonObject = new JSONObject(readTxt); JSONArray
		 * AppArray = jsonObject.getJSONArray("apps"); JSONObject jon =
		 * AppArray.getJSONObject(0);
		 */

		// ANGULAR CLI >=6

		JSONArray styles = (JSONArray) jsonOptions.get("styles");
		styles.put("./node_modules/primeicons/primeicons.css");
		// object.put("./node_modules/primeng/resources/themes/nova-light/theme.css");
		styles.put("./src/theme/theme.css");
		styles.put("./node_modules/primeng/resources/primeng.min.css");
		// object.put("./node_modules/bootstrap/dist/css/bootstrap.css");
		styles.put("./node_modules/primeflex/primeflex.css");

		// jsonOptions.get("scripts");
		JSONArray scripts = (JSONArray) jsonOptions.get("scripts");
		scripts.put("./node_modules/chart.js/dist/Chart.js");

		System.err.println("" + jsonObject.toString());

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Object json = gson.fromJson(jsonObject.toString(), Object.class);
		String styleFormated = gson.toJson(json); // done

//		ObjectMapper mapper = new ObjectMapper();
//		Object json = mapper.readValue(jsonObject.toString(), Object.class);
//		String styleFormated = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

		Utils.writeTxt(urlFile, styleFormated, true);

	}

	private void messageOK() {
		consoleLog.setText("Projeto Finalizado!");
		Object[] options = { "OK" };

		int input = JOptionPane.showOptionDialog(null, "Projeto criado com Sucesso!", "Sucesso", JOptionPane.OK_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (input == JOptionPane.OK_OPTION) {

			int resposta = JOptionPane.showConfirmDialog(null, "Deseja instalar as dependencias do projeto ?", "",
					JOptionPane.YES_NO_OPTION);

			if (resposta == JOptionPane.YES_OPTION) {
				try {

					String bat = inputWorkspace.getText().substring(0, 2) + " & " + "cd " + inputWorkspace.getText()
							+ " & " + "cd " + textField.getText() + " & " + " npm install " + " & "
							+ " npm audit fix --force ";

					ProcessBuilder pb = new ProcessBuilder("cmd.exe", " /c " + bat);

					pb.redirectErrorStream(true);
					Process p = pb.start();

					BufferedReader output = Utils.getOutput(p);
					BufferedReader error = Utils.getError(p);

					String ligne = "";

					consoleLog.setText("Instalando dependencias...");

					while ((ligne = output.readLine()) != null) {
						consoleLog.setText(ligne);
					}

					while ((ligne = error.readLine()) != null) {
						consoleLog.setText(ligne);
					}

					p.waitFor();

					consoleLog.setText("dependencias Instaladas...OK!");

					JOptionPane.showOptionDialog(null, "Dependencias Instaladas!", "Sucesso", JOptionPane.OK_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				} catch (Exception e) {
					consoleLog.setText("ERRO> " + e.getMessage());
					e.printStackTrace();
				}

			}

			this.jInternalFrame.moveToFront();
		}
	}

}
