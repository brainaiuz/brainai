package com.edatasite.workforce.gwt.invoice.client.ui;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AccountLookUpForExpense;
import com.google.gwt.user.client.Command;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 31.01.14
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */
public class SmartAccountLookUpForExpense extends AccountLookUpForExpense {

    Command linkCommand;
    boolean hassPermissionToAddNew;

    public SmartAccountLookUpForExpense(String type) {
        super(type);
    }

    public SmartAccountLookUpForExpense(String type, Command linkCommand) {
        super(type);
        oracle.setLinkCommand(linkCommand);
        oracle.setIsvisiblelink(true);
    }

    public SmartAccountLookUpForExpense(String type, Command linkCommand, boolean hassPermissionToAddNew) {
        super(type);
        oracle.setLinkCommand(linkCommand);
        oracle.setIsvisiblelink(hassPermissionToAddNew);
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
        getTextBox().getElement().getStyle().setColor("#999999");
    }


    public void setLinkCommand(Command linkCommand, boolean hassPermissionToAddNew) {
        this.linkCommand = linkCommand;
        this.hassPermissionToAddNew = hassPermissionToAddNew;
        oracle.setLinkCommand(linkCommand);
        oracle.setIsvisiblelink(hassPermissionToAddNew);
    }

}
