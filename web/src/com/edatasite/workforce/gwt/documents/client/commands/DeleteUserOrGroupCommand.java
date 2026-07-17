/*
 * Copyright 2008, 2009 Electronic Business Systems Ltd.
 *
 * This file is part of GSS.
 *
 * GSS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GSS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GSS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.documents.client.DeleteGroupDialog;
import com.edatasite.workforce.gwt.documents.client.DeleteUserDialog;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;


/**
 * @author Sherali
 */
public class DeleteUserOrGroupCommand implements Command {
    private PopupPanel containerPanel;

    /**
     * @param aContainerPanel
     */
    public DeleteUserOrGroupCommand(PopupPanel aContainerPanel) {
        containerPanel = aContainerPanel;
    }

    public void execute() {
        containerPanel.hide();
        if (DocumentsView.get().getCurrentSelection() instanceof GroupMembersViewItem) {
            displayNewGroup();
        } else if (DocumentsView.get().getCurrentSelection() instanceof GroupMemberItem) {
            displayNewUser();
        } else {
            DocumentsView.get().displayError("No user or group selected");
        }
    }

    void displayNewGroup() {
        final DeleteGroupDialog dlg = new DeleteGroupDialog();
        dlg.center();
    }

    void displayNewUser() {
        final DeleteUserDialog dlg = new DeleteUserDialog();
        dlg.center();
    }

}
