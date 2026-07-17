package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.documents.client.UserAddDialog;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;


/**
 * @author Sherali
 */
public class NewUserCommand implements Command {
    private PopupPanel containerPanel;

    /**
     * @param _containerPanel
     */
    public NewUserCommand(PopupPanel _containerPanel) {
        containerPanel = _containerPanel;
    }

    public void execute() {
        containerPanel.hide();
        displayNewUser();
    }

    private void displayNewUser() {
        UserAddDialog dlg = new UserAddDialog();
        dlg.center();
    }

}
