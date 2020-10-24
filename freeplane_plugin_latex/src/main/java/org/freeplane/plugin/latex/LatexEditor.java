package org.freeplane.plugin.latex;

/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2012 dimitry
 *
 *  This file author is Felix Natter (copied from FormulaEditor)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.freeplane.features.map.NodeModel;
import org.freeplane.features.text.mindmapmode.EditNodeDialog;

/**
 *  @author Felix Natter (copied from FormulaEditor)
 */
class LatexEditor extends EditNodeDialog {
	
	private JRadioButton mathMode;
	private JRadioButton textMode;
	
	class ModeListener implements DocumentListener{

		@Override
		public void insertUpdate(DocumentEvent e) {
			e.getDocument();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
		}
		
	}

	LatexEditor(NodeModel nodeModel, String text, KeyEvent firstEvent, IEditControl editControl,
                          boolean enableSplit, JEditorPane textEditor) {
	    super(nodeModel, text, firstEvent, editControl, enableSplit, textEditor);
	    super.setModal(false);
	    Document document = textEditor.getDocument();
	    document.addDocumentListener(new ModeListener());
		mathMode = new JRadioButton("Math");
		textMode = new JRadioButton("Text");
		ButtonGroup modes = new ButtonGroup();
		modes.add(mathMode);
		modes.add(textMode);
		
    }

	@Override
	protected void configureDialog(JDialog dialog, JPanel buttonPane, JPanel optionPane) {
		optionPane.add(textMode);
		optionPane.add(mathMode);
		super.configureDialog(dialog, buttonPane, optionPane);
	}
	
	void onUpdate(Document document) {
	}
	
	
}