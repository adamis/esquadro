package br.com.esquadro.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import br.com.esquadro.enums.DATABASETYPE;

public class Utils {

	public static BufferedReader getOutput(Process p) {
		return new BufferedReader(new InputStreamReader(p.getInputStream()));
	}

	public static BufferedReader getError(Process p) {
		return new BufferedReader(new InputStreamReader(p.getErrorStream()));
	}

	public static void main(String[] args) {
		// String table = "alunos-cursos";
		// System.err.println("1 "+normalizerStringCapHifen(table));
		// System.err.println("1 "+normalizerString(table));
		// System.err.println("2 "+normalizerStringCaps(table));
		// System.err.println("3 "+normalizerStringCommom(table));
		// System.err.println("4 "+normalizerStringCommomNotCap(table));
	}

	/**
	 * (alunos-cursos) Replace "_" to "-"
	 * 
	 * @param text
	 * @return
	 */
	public static String normalizerString(String text) {
		text = text.replace("_", "-");
		// String[] split = text.toLowerCase().split("-");
		//
		// for (int i = 0; i < split.length; i++) {
		//
		// if(i > 0) {
		//
		// text += (Character.toUpperCase(split[i].charAt(0)) + split[i].substring(1));
		//
		// }else {
		//
		// text = split[i];
		//
		// }
		// }

		return text.toLowerCase();
	}

	/**
	 * (alunosCursos) Replace "_" to "-" Not Capitalization in CammonCase
	 * 
	 * @param text
	 * @return
	 */
	public static String normalizerStringCommomNotCap(String text) {
		text = text.replace("_", "-");
		String[] split = text.toLowerCase().split("-");

		for (int i = 0; i < split.length; i++) {

			if (i > 0) {

				text += (Character.toUpperCase(split[i].charAt(0)) + split[i].substring(1));

			} else {

				text = split[i];

			}
		}

		return text;
	}

	public static String normalizerStringCapHifen(String text, DATABASETYPE databaseType) {

		if (databaseType != DATABASETYPE.ORACLE) {

			String temp = "";

			char[] palavraArray = text.toCharArray();

			for (char c : palavraArray) {
				if (Character.isUpperCase(c)) {
					temp += "_" + c;
				} else {
					temp += c;
				}
			}

			return temp;

		} else {
			return text;
		}
	}

	/**
	 * (alunoscursos)
	 * 
	 * @param text
	 * @return
	 */
	public static String normalizerStringCommom(String text) {
		text = text.replace("_", "-");
		String[] split = text.toLowerCase().split("-");

		for (int i = 0; i < split.length; i++) {

			if (i > 0) {

				text += (Character.toUpperCase(split[i].charAt(0)) + split[i].substring(1));

			} else {

				text = split[i];

			}
		}

		return text.toLowerCase();
	}

	/**
	 * (AlunosCursos)
	 * 
	 * @param text
	 * @return
	 */
	public static String normalizerStringCaps(String text) {
		//System.err.println("TEXT: " + text);

		text = text.replace("_", "-");
		String[] split = text.toLowerCase().split("-");

		for (int i = 0; i < split.length; i++) {

			if (i > 0) {

				text += (Character.toUpperCase(split[i].charAt(0)) + split[i].substring(1));

			} else {

				text = split[i];

			}
		}

		return (Character.toUpperCase(text.charAt(0)) + text.substring(1));
	}

	public static String readTxt(String url) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(url));
		String text = "";
		while (br.ready()) {
			text += br.readLine();
		}
		br.close();
		return text;
	}

	public static List<String> readTxtList(String url) throws IOException {
		List<String> lista = new ArrayList<>();

		final BufferedReader br = new BufferedReader(new FileReader(url));

		while (br.ready()) {
			lista.add(br.readLine());
		}
		br.close();
		return lista;
	}

	public static void writeTxt(String file, String text, boolean replaceFile) throws Exception {

		file = file.trim();

//		System.err.println("File: " + file);
//		System.err.println("text: " + text);
//		System.err.println("Replace: " + replaceFile);

		new File(file).mkdirs();

		if (replaceFile && new File(file).exists()) {
			new File(file).delete();
		}

		// FileWriter arq = new FileWriter(file);
		PrintWriter gravarArq = new PrintWriter(new File(file), "UTF-8");

		// System.err.println(""+text);

		// gravarArq.printf(text);
		gravarArq.write(text);
		gravarArq.close();
	}

	public static void writeTxtList(String file, List<String> textList, boolean replaceFile) throws Exception {
		if (replaceFile && new File(file).exists()) {
			new File(file).delete();
		}
		FileWriter arq = new FileWriter(file);
		PrintWriter gravarArq = new PrintWriter(arq);

		for (int i = 0; i < textList.size(); i++) {
			gravarArq.print(textList.get(i) + "\r\n");
		}
		arq.close();
	}

	public static void doCopyFile(File source, String destination) throws Exception {
		if (new File(destination).exists()) {
			delete(new File(destination));
		}
		FileUtils.copyFile(source, new File(destination));

	}

	public static void doCopyDirectory(File source, String destination) throws Exception {
		if (new File(destination).exists()) {
			delete(new File(destination));
		}
		FileUtils.copyDirectory(source, new File(destination));

	}

	public static void doCopyDirectory(File source, String destination, boolean replace) throws Exception {
		if (replace && new File(destination).exists()) {
			delete(new File(destination));
		}

		FileFilter fileFilter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				String[] ignoreList = new String[] { ".gitignore" };

				return !(ignoreFile(pathname, ignoreList) && pathname.isFile());
			}

		};

		FileUtils.copyDirectory(source, new File(destination), fileFilter);

	}

	public static boolean ignoreFile(File file, String[] ignoreList) {
		for (final String ignoreStr : ignoreList)
			if (file.getAbsolutePath().contains(ignoreStr))
				return true;
		return false;
	}

	public static void delete(File file) throws Exception {

		if (file.isDirectory()) {
			File[] entries = file.listFiles();
			if (entries != null) {
				for (File entry : entries) {
					delete(entry);
				}
			}
		}
		if (!file.delete()) {
			throw new Exception("Failed to delete " + file);
		}
	}

}
