package br.com.esquadro.controler;

import java.io.BufferedReader;
import java.io.File;

import br.com.esquadro.util.Statics;
import br.com.esquadro.util.Utils;
import br.com.esquadro.view.ConsoleLog;

public class CheckNodeAngularCLI {

	private ConsoleLog consoleLog;

	public CheckNodeAngularCLI(ConsoleLog consoleLog) {
		this.consoleLog = consoleLog;
	}

	public boolean checkMinimalWorkspace() throws Exception {
		Integer node = checkNode();

		if (node == 0) {
			this.consoleLog.setText("Versão do Node não encontada, Instalando o NODE...");
			installChocolatey();
			return false;

		} else {

			Integer angular = checkAngular();

			if (angular == 0) {
				this.consoleLog.setText("Instalando ANGULAR CLI...");
				installAngular();

//			}else if(angular >= 800) {
//				this.consoleLog.setText("Realizando Downgrade do ANGULAR CLI...");
//				this.consoleLog.setText("Realizando Downgrade de: "+angular+" para: 7XX");
//				downgradeCLI();
//				checkNode();
//				
//				return false;
			} else if (angular < Statics.UPDATE_UP_LEVEL) {
				this.consoleLog.setText("Atualizando ANGULAR CLI...");
				updateAngular();
			}

			return true;
		}
	}

	private void downgradeCLI() throws Exception {
		String bat = "npm uninstall -g @angular/cli" + " & " + "npm cache clean" + " & " + " npm cache verify " + " & "
				+ "npm install -g @angular/cli@latest";
		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", bat);

		pb.redirectErrorStream(true);
		Process p = pb.start();

		BufferedReader output = Utils.getOutput(p);
		BufferedReader error = Utils.getError(p);

		String ligne = "";
		String version = "";
		String erro = "";

		while ((ligne = output.readLine()) != null) {
			this.consoleLog.setText("" + ligne);
			version = ligne;			
		}

		while ((ligne = error.readLine()) != null) {
			this.consoleLog.setText("" + ligne);
			erro = ligne;
		}

		p.waitFor();
		if (erro.equals("")) {
			this.consoleLog.setText("Downgrade... OK!");
		}
	}

	public Integer checkNode() throws Exception, Exception {
		String bat = " node -v";
		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", bat);

		pb.redirectErrorStream(true);
		Process p = pb.start();

		BufferedReader output = Utils.getOutput(p);
		BufferedReader error = Utils.getError(p);

		String ligne = "";
		String version = "";
		String erro = "";

		while ((ligne = output.readLine()) != null) {
			this.consoleLog.setText("" + ligne);
			version = ligne;

		}

		while ((ligne = error.readLine()) != null) {
			this.consoleLog.setText("" + ligne);
			erro = ligne;

		}

		p.waitFor();


		this.consoleLog.setText("Versão do Node: " + version);

		if (version.equals("")) {
			return 0;
		} else {

			try {
				return Integer.valueOf(version.replace(".", "").replace("v", ""));
			} catch (Exception e) {
				return 0;
			}

		}

	}

	public Integer checkAngular() throws Exception {
		String bat = "ng --version";
		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", bat);

		pb.redirectErrorStream(true);
		Process p = pb.start();

		BufferedReader output = Utils.getOutput(p);
		BufferedReader error = Utils.getError(p);

		String ligne = "";
		String version = "";
		while ((ligne = output.readLine()) != null) {
			if (ligne.contains("cli") || ligne.contains("CLI")) {
				this.consoleLog.setText("Angular CLI: " + ligne);
				version = ligne;
			}
		}

		while ((ligne = error.readLine()) != null) {
			this.consoleLog.setText("" + ligne);
		}

		p.waitFor();

		if (version.equals("")) {
			this.consoleLog.setText("Versão do Angular: 0");
			return 0;
		} else {
			this.consoleLog.setText("Versão do Angular: " + (version.split(":")[1]).trim());
			return Integer.valueOf((version.split(":")[1]).trim().replace(".", ""));
		}

	}

	public boolean installChocolatey() throws Exception {
		File teste = new File("src/Main.java");
		String path = teste.getAbsolutePath().replace("src\\Main.java", "");
		String os = System.getProperty("sun.arch.data.model");

		String choco = "cd " + path + "elevation\\x" + os.trim() + " & Elevate.exe cmd /c start " + path
				+ "elevation\\x" + os.trim() + "\\chocolatey.bat\"";

		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", choco);

		pb.redirectErrorStream(true);
		Process p = pb.start();

		BufferedReader output = Utils.getOutput(p);
		BufferedReader error = Utils.getError(p);

		String ligne = "";
		String result = "";
		while ((ligne = output.readLine()) != null) {
			this.consoleLog.setText("" + ligne);
			result = ligne;
		}

		while ((ligne = error.readLine()) != null) {
			this.consoleLog.setText("" + ligne);

		}

		p.waitFor();
		if (result.contains("cmd could not be launched")) {
			return false;
		} else {
			return true;
		}

	}

	public boolean installAngular() throws Exception {
		String cmd = "npm install -g @angular/cli@latest && npm install -g json-server";

		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", cmd);

		pb.redirectErrorStream(true);
		Process p = pb.start();

		BufferedReader output = Utils.getOutput(p);
		BufferedReader error = Utils.getError(p);

		String ligne = "";
		String result = "";
		while ((ligne = output.readLine()) != null) {
			this.consoleLog.setText("" + ligne);
			result = ligne;
		}

		while ((ligne = error.readLine()) != null) {
			this.consoleLog.setText("" + ligne);
		}

		p.waitFor();
		if (result.contains("cmd could not be launched")) {
			return false;
		} else {
			return true;
		}
	}

	private static boolean updateAngular() throws Exception {


		// String cmd = "ng remove -g @angular/cli && npm cache verify && npm install -g
		// @angular/cli@^7";
		String cmd = "npm uninstall -g @angular/cli && npm cache verify && npm install -g @angular/cli@latest";

		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", cmd);

		pb.redirectErrorStream(true);
		Process p = pb.start();

		BufferedReader output = Utils.getOutput(p);
		BufferedReader error = Utils.getError(p);

		String ligne = "";
		String result = "";
		while ((ligne = output.readLine()) != null) {

			result = ligne;
		}

		while ((ligne = error.readLine()) != null) {

		}

		p.waitFor();

		if (result.contains("cmd could not be launched")) {
			return false;
		} else {
			return true;
		}

	}

}
