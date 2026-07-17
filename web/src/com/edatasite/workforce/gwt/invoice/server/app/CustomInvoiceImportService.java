package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/3/14
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CustomInvoiceImportService {
    String importCustomInvoices(ImportFile importFile, List<String[]> dataBank);
}
