package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.documents.client.GroupPropertiesDialog2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Created by IntelliJ IDEA.
 * User: sherali
 * Date: 25.06.2010
 * Time: 20:17:35
 * To change this template use File | Settings | File Templates.
 */
public class EditGroupCommand implements Command {
    private PopupPanel containerPanel;

    /**
     * @param _containerPanel
     */
    public EditGroupCommand(PopupPanel _containerPanel) {
        containerPanel = _containerPanel;
    }
    /* (non-Javadoc)
    * @see com.google.gwt.user.client.Command#execute()
    */

    public void execute() {
        containerPanel.hide();
        displayEditGroup();
    }

    void displayEditGroup() {
        GroupPropertiesDialog2 dlg = new GroupPropertiesDialog2(false);
        dlg.center();
    }

}
