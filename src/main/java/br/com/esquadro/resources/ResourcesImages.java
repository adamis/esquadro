/**
 * @autor Adamis.Rocha
 * @since 1.0, 31 de ago de 2017
 */
package br.com.esquadro.resources;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

/**
 * @author Adamis.Rocha
 * @since 1.0, 31 de ago de 2017
 */
public class ResourcesImages {
	private static String version = "1.3 b";

	public static String version() {
		return version;
	}

	public static ImageIcon springBoot() {
		return toolkitIcon("SpringBoot.png");
	}

	public static Image springBootImg() {
		return toolkit("SpringBoot.png");
	}

	public static ImageIcon add() {
		return toolkitIcon("add.png");
	}

	public static Image icon2() {
		return toolkit("icon2.png");
	}

	public static ImageIcon analysis() {
		return toolkitIcon("analysis.png");
	}

	public static ImageIcon cancelar() {
		return toolkitIcon("cancel.png");
	}

	public static ImageIcon angular() {
		return toolkitIcon("angular.png");
	}

	public static ImageIcon bg() {
		return toolkitIcon("bg.jpg");
	}

	public static ImageIcon esquadro() {
		return toolkitIcon("esquadro.png");
	}

	public static Image bgImage() {
		return toolkit("bg.jpg");
	}

	public static ImageIcon cats() {
		return toolkitIcon("cats.png");
	}

	public static ImageIcon database() {
		return toolkitIcon("database.png");
	}

	public static ImageIcon dialogue() {
		return toolkitIcon("dialogue-box.png");
	}

	public static ImageIcon information() {
		return toolkitIcon("information.png");
	}

	public static ImageIcon faq() {
		return toolkitIcon("faq.png");
	}

	public static ImageIcon report() {
		return toolkitIcon("report.png");
	}

	public static ImageIcon coding() {
		return toolkitIcon("coding.png");
	}

	public static ImageIcon bancoIcon() {
		return toolkitIcon("banco-icon.png");
	}

	public static ImageIcon editing() {
		return toolkitIcon("editing.png");
	}

	public static ImageIcon deleteIcon() {
		return toolkitIcon("delete-icon.png");
	}

	public static ImageIcon editIcon() {
		return toolkitIcon("edit-icon.png");
	}

	public static ImageIcon addIcon() {
		return toolkitIcon("add-icon.png");
	}

	public static ImageIcon database2() {
		return toolkitIcon("database2.png");
	}

	public static ImageIcon tutorial1() {
		return toolkitIcon("tutorial-1.png");
	}

	public static ImageIcon tutorial2() {
		return toolkitIcon("tutorial-2.png");
	}

	private static Image toolkit(String img) {

		Class<?> clazz = ResourcesImages.class;
		ClassLoader classLoader = clazz.getClassLoader();
		return Toolkit.getDefaultToolkit().getImage(classLoader.getResource(img));
	}

	private static ImageIcon toolkitIcon(String img) {

		Class<?> clazz = ResourcesImages.class;
		ClassLoader classLoader = clazz.getClassLoader();
		return new ImageIcon(classLoader.getResource(img));

		// return new ImageIcon(ResourcesImages.class.getResource(img));
	}

}
