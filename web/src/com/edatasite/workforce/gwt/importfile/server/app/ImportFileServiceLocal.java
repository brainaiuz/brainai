package com.edatasite.workforce.gwt.importfile.server.app;

import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;

public interface ImportFileServiceLocal {
    String addImportToQueue(ImportFile importFile);

}
