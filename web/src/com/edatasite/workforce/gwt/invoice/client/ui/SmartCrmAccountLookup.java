package com.edatasite.workforce.gwt.invoice.client.ui;

import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.google.gwt.user.client.Command;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 10/2/12
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmartCrmAccountLookup extends CrmAccountLookUp {

    public SmartCrmAccountLookup(String typecode, boolean searchbyparent, Command linkcommand, boolean codeAlso) {
        super(typecode, searchbyparent, codeAlso);
        oracle.setLinkCommand(linkcommand);
        oracle.setIsvisiblelink(true);
    }

    public SmartCrmAccountLookup(String typecode, boolean searchbyparent, Command linkcommand) {
        this(typecode, searchbyparent, linkcommand, false);
    }

    public SmartCrmAccountLookup(String typecode, boolean searchbyparent, Command linkcommand, boolean codeAlso, boolean isVisableLink) {
        super(typecode, searchbyparent, codeAlso);
        oracle.setLinkCommand(linkcommand);
        oracle.setIsvisiblelink(isVisableLink);
    }

    public SmartCrmAccountLookup(String typecode, boolean searchbyparent, Command linkcommand, boolean codeAlso, boolean isVisableLink, boolean withBlockedAccounts) {
        super(typecode, searchbyparent, codeAlso, withBlockedAccounts);
        oracle.setLinkCommand(linkcommand);
        oracle.setIsvisiblelink(isVisableLink);
    }

}
