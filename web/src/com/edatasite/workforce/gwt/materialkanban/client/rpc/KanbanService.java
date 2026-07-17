package com.edatasite.workforce.gwt.materialkanban.client.rpc;


import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

public interface KanbanService extends RemoteService {

    ArrayList<KanbanColumn> getKanbanDefaultColumns(ReferenceParentEnum parentCode);
    String getKanbanBoardSettings(ListPanelType type);
    void saveKanbanBoardSettings(ListPanelType type, Integer pageSize, ArrayList<KanbanColumn> columns,boolean applyForAll);


    class App {
        public static KanbanServiceAsync get() {
            ServiceDefTarget target = GWT.create(KanbanService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/kanban");
            return (KanbanServiceAsync) target;
        }
    }
}
