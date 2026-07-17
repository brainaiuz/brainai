package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/16/12
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface NimbleService {
    void importNimbleCommerceData(ImportFile importFile, List<String[]> dataBank);
}
