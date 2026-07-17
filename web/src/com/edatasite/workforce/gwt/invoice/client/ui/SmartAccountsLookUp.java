package com.edatasite.workforce.gwt.invoice.client.ui;

import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.google.gwt.user.client.Command;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 31.01.14
 * Time: 18:00
 * To change this template use File | Settings | File Templates.
 */
public class SmartAccountsLookUp extends AccountsLookUp {

    Command linkCommand;

    public SmartAccountsLookUp(String type) {
        super(type);
    }

    public SmartAccountsLookUp(String type, Command linkCommand) {
        super(type);
        oracle.setLinkCommand(linkCommand);
        oracle.setIsvisiblelink(true);
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
        getTextBox().getElement().getStyle().setColor("#999999");
    }

    public Command getLinkCommand() {
        return linkCommand;
    }

    public void setLinkCommand(Command linkCommand) {
        this.linkCommand = linkCommand;
    }
}


