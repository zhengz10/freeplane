/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitry Polivaev
 *
 *  This file is modified by Dimitry Polivaev in 2008.
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
package org.freeplane.features.cloud.mindmapmode;

import java.awt.event.ActionEvent;

import org.freeplane.core.ui.AMultipleNodeAction;
import org.freeplane.core.ui.IEditHandler;
import org.freeplane.features.cloud.CloudController;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.text.TextController;
import org.freeplane.features.text.mindmapmode.MTextController;

class CloudLabelAction extends AMultipleNodeAction {
    private static final long serialVersionUID = 1L;
    private String actionLabel;

    public CloudLabelAction() {
        super("CloudLabelAction");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        ((MTextController) TextController.getController()).edit(IEditHandler.FirstAction.EDIT_CURRENT, false);
        super.actionPerformed(e);
    }

    /*
     * (non-Javadoc)
     * @see
     * freeplane.modes.mindmapmode.actions.MultipleNodeAction#actionPerformed
     * (freeplane.modes.NodeModel)
     */
    @Override
    protected void actionPerformed(final ActionEvent e, final NodeModel node) {
        final MCloudController cloudController = (MCloudController) CloudController.getController();
        cloudController.setLabel(node, actionLabel);
    }
}
