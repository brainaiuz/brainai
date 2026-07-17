package com.edatasite.workforce.gwt.importfile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 25, 2011
 * Time: 3:11:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportStatus implements IsSerializable{
    private String[] importedAccounts;
    private String[] notImportedAccounts;

    public ImportStatus() {
    }

    public String[] getImportedAccounts() {
        return importedAccounts;
    }

    public void setImportedAccounts(String[] importedAccounts) {
        this.importedAccounts = importedAccounts;
    }

    public String[] getNotImportedAccounts() {
        return notImportedAccounts;
    }

    public void setNotImportedAccounts(String[] notImportedAccounts) {
        this.notImportedAccounts = notImportedAccounts;
    }
}
