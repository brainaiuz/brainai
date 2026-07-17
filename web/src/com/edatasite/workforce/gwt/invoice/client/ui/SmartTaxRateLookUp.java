package com.edatasite.workforce.gwt.invoice.client.ui;

import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.google.gwt.user.client.Command;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/29/13
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmartTaxRateLookUp extends TaxLookUp {

    Command linkCommand;

    public SmartTaxRateLookUp(String type) {
        super(type);
    }

    public SmartTaxRateLookUp(String type, Command linkcommand) {
        super(type);
        oracle.setLinkCommand(linkcommand);
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
        oracle.setLinkCommand(linkCommand);
        oracle.setIsvisiblelink(true);
    }
}
