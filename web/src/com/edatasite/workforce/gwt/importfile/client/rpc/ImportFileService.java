package com.edatasite.workforce.gwt.importfile.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * User: Employee
 * Date: Nov 3, 2009 4:54:05 PM
 */
public interface ImportFileService extends RemoteService {

    String addImportToQueue(ImportFile importFile);

    String getImportPreference();

    class App {
        public static ImportFileServiceAsync get() {
            ServiceDefTarget target = GWT.create(ImportFileService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/importFile");
            return (ImportFileServiceAsync) target;
        }
    }
}