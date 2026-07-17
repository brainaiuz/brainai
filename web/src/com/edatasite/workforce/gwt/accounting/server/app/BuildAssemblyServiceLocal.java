package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.core.domain.EdsSavedAssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;

public interface BuildAssemblyServiceLocal {

    void reBuildAssemblyItem(AssemblyItem assemblyItem, Integer oldTransactionId);

    void saveBuildAssemblyHistory(EdsSavedAssemblyItem edsSavedAssemblyItem);

    Integer saveBuildAssemblyNote(Integer savedAssemblyItemId, HistoryListItem historyListItem);


}
