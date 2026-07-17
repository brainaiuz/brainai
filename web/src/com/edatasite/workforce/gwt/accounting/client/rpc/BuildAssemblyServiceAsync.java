package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

public interface BuildAssemblyServiceAsync {

    Request getBuildAssemblyList(ListingFilterParameter fp, AsyncCallback<ListResult<AssemblyItem>> async);

    void getBuildAssemblyItem(Integer id, AsyncCallback<AssemblyItem> callback);

    void buildAssemblyItem(AssemblyItem assemblyItem, AsyncCallback<Integer> callback);

    void unBuildAsseblyItems(ArrayList<AssemblyBuildItem> items, AsyncCallback<Void> callback);

    void getAssemblyBuildItems(Integer assemblyID, AsyncCallback<ArrayList<AssemblyBuildItem>> async);

    void getProductForBuildAssembly(Integer productId, AsyncCallback<NewProduct> callback);

    void updateStatusBuildAssembly(Integer savedAssemblyId, String statusCode, AsyncCallback<Void> callback);

    void unBuildAssemblyItem(Integer savedAssemblyItemId, AsyncCallback<SelectItem> callback);

    void deleteSavedAssembly(Integer savedAssemblyId, AsyncCallback<Void> callback);

    void deleteSelectedSavedAssemblyList(ArrayList<Integer> savedAssemblyIds, AsyncCallback<Void> callback);

    void saveBuildAssemblyNote(Integer savedAssemblyItemId, HistoryListItem hisItem, AsyncCallback<Integer> async);

    void loadBuildAssembyNotes(Integer savedAssemblyItemId, AsyncCallback<List<HistoryNote>> async);
}
