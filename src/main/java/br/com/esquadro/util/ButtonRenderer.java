package br.com.esquadro.util;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

//BUTTON RENDERER CLASS
public class ButtonRenderer extends JButton implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9093045036935997158L;

	// CONSTRUCTOR
	public ButtonRenderer() {
		// SET BUTTON PROPERTIES
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object obj, boolean selected, boolean focused, int row,
			int col) {

		// SET PASSED OBJECT AS BUTTON TEXT
		setText((obj == null) ? "" : obj.toString());

		return this;
	}

}