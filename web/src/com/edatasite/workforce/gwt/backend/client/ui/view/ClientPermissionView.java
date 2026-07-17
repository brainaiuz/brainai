package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 5/28/11
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientPermissionView extends View {

    public ClientPermissionView() {
        super("clientPermission", "Client Permission View");
    }

    public String getIconStyle() {
        return "backend cliendPermView";
    }

    @Override
    protected Widget onInitialize() {
        FlexTable flexTable = new FlexTable();
        flexTable.setCellPadding(10);
        flexTable.setCellSpacing(10);

        final SchemaLookUp schemaLookUp = new SchemaLookUp();

        Button applyButton = new Button("Apply", (ClickHandler) clickEvent -> {
            if (schemaLookUp.getSelectedItem() != null && schemaLookUp.getSelectedItem().getId() != null) {
                apply(schemaLookUp.getSelectedItem().getId());
            }
        });

        flexTable.setHTML(0, 0, "Add to Company Clients to CLIENTS group");
        flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);

        flexTable.setHTML(1, 0, "Select Company: ");
        flexTable.setWidget(1, 1, schemaLookUp);

        flexTable.setWidget(2, 0, applyButton);
        flexTable.getFlexCellFormatter().setColSpan(2, 0, 2);
        flexTable.getFlexCellFormatter().setAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);

        add(flexTable);
        return null;
    }

    private void apply(Integer companyID) {
        LoadingPanel.loading(true);
        BackendService.App.get().createClientGroupsToClientContactForCompany(companyID, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Boolean aBoolean) {
                LoadingPanel.loading(false);
            }
        });
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
