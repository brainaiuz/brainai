package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.List;

public interface BuildAssemblyService extends RemoteService {

    ListResult<AssemblyItem> getBuildAssemblyList(ListingFilterParameter fp);

    AssemblyItem getBuildAssemblyItem(Integer id);

    Integer buildAssemblyItem(AssemblyItem assemblyItem);

    void unBuildAsseblyItems(ArrayList<AssemblyBuildItem> items);

    ArrayList<AssemblyBuildItem> getAssemblyBuildItems(Integer assemblyID);

    NewProduct getProductForBuildAssembly(Integer productId);

    void updateStatusBuildAssembly(Integer savedAssemblyId, String statusCode);

    SelectItem unBuildAssemblyItem(Integer savedAssemblyItemId);

    void deleteSavedAssembly(Integer savedAssemblyId);

    void deleteSelectedSavedAssemblyList(ArrayList<Integer> savedAssemblyIds);

    Integer saveBuildAssemblyNote(Integer savedAssemblyItemId, HistoryListItem hisItem);

    List<HistoryNote> loadBuildAssembyNotes(Integer savedAssemblyItemId);

    class App {
        public static BuildAssemblyServiceAsync get() {
            ServiceDefTarget target = GWT.create(BuildAssemblyService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/buildAssembly");
            return (BuildAssemblyServiceAsync) target;
        }
    }
}
