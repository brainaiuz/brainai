package com.edatasite.workforce.gwt.invoice.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MeasurementsLookUp;
import com.google.gwt.user.client.Command;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_UNIT_MEASUREMENTS_ADD;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 11/1/13
 * Time: 7:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmartMeasurementsLookUp extends MeasurementsLookUp {

    Command linkCommand;

    public SmartMeasurementsLookUp() {
        super();
    }

    public SmartMeasurementsLookUp(Command linkcommand) {
        super();
        oracle.setLinkCommand(linkcommand);
        oracle.setIsvisiblelink(true);
    }

    public Command getLinkCommand() {
        return linkCommand;
    }

    public void setLinkCommand(Command linkCommand) {
        this.linkCommand = linkCommand;
        oracle.setLinkCommand(linkCommand);
        oracle.setIsvisiblelink(Utils.isLogistics() ? true : Utils.hasPermission(ACCOUNTING_UNIT_MEASUREMENTS_ADD));
    }

}
