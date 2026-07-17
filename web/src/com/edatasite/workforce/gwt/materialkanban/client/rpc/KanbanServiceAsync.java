package com.edatasite.workforce.gwt.materialkanban.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public interface KanbanServiceAsync {

    void getKanbanDefaultColumns(ReferenceParentEnum parentCode, AsyncCallback<ArrayList<SelectItem>> async);

    void getKanbanBoardSettings(ListPanelType type, AsyncCallback<String> callback);

    void saveKanbanBoardSettings(ListPanelType type, Integer pageSize, ArrayList<KanbanColumn> columns,boolean applyForAll, AsyncCallback<Void> callback);


}
