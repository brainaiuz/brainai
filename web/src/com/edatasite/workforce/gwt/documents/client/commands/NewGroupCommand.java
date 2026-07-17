package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.documents.client.GroupPropertiesDialog2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;


/**
 * @author Sherali
 */
public class NewGroupCommand implements Command {
    private PopupPanel containerPanel;

    /**
     * @param _containerPanel
     */
    public NewGroupCommand(PopupPanel _containerPanel) {
        containerPanel = _containerPanel;
    }
    /* (non-Javadoc)
      * @see com.google.gwt.user.client.Command#execute()
      */

    public void execute() {
        containerPanel.hide();
        displayNewGroup();
    }

    void displayNewGroup() {
        GroupPropertiesDialog2 dlg = new GroupPropertiesDialog2(true);
        dlg.open();
    }

}
