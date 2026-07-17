package com.edatasite.workforce.gwt.importfile.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * User: Employee
 * Date: Nov 3, 2009
 * Time: 4:56:48 PM
 */
public interface ImportFileServiceAsync {
    void addImportToQueue(ImportFile importFile, AsyncCallback<String> async);

    void getImportPreference(AsyncCallback<String> async);
}