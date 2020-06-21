package br.com.esquadro.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;

//BUTTON EDITOR CLASS
public class ButtonEditor extends DefaultCellEditor {
	private int col;
	private int row;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JButton btn;
	private String lbl;
	private Boolean clicked;
	private JtableActionPerformace jtableActionPerformace;

	public ButtonEditor(JTextField txt, JtableActionPerformace jtableActionPerformace) {
		super(txt);

		this.jtableActionPerformace = jtableActionPerformace;

		btn = new JButton();
		btn.setOpaque(true);

		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				fireEditingStopped();
			}
		});
	}

	// OVERRIDE A COUPLE OF METHODS
	@Override
	public Component getTableCellEditorComponent(JTable table, Object obj, boolean selected, int row, int col) {

		this.row = row;
		this.col = col;

		// SET TEXT TO BUTTON,SET CLICKED TO TRUE,THEN RETURN THE BTN OBJECT
		lbl = (obj == null) ? "" : obj.toString();
		btn.setText(lbl);
		clicked = true;
		return btn;
	}

	// IF BUTTON CELL VALUE CHNAGES,IF CLICKED THAT IS
	@Override
	public Object getCellEditorValue() {

		if (clicked) {
			// SHOW US SOME MESSAGE
			jtableActionPerformace.exec(row, col);
		}
		// SET IT TO FALSE NOW THAT ITS CLICKED
		clicked = false;
		return new String(lbl);
	}

	@Override
	public boolean stopCellEditing() {

		// SET CLICKED TO FALSE FIRST
		clicked = false;
		return super.stopCellEditing();
	}

	@Override
	protected void fireEditingStopped() {
		// TODO Auto-generated method stub
		super.fireEditingStopped();
	}
}