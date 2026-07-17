package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Nov 9, 2010
 * Time: 1:39:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddSchemaView extends View implements Constants, CommandConstants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    public AddSchemaView() {
        super("schemaadd", wfmStrings.createSchema());
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override


    protected Widget onInitialize() {

        final TextBox countTextBox = new TextBox();
        final WfmButton2 createButton = new WfmButton2(wfmStrings.create());
        final WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        createButton.addClickHandler(event -> {
            createButton.setEnabled(false);
            if (!countTextBox.getText().trim().equals("") && Integer.parseInt(countTextBox.getText()) > 0) {
                BackendService.App.get().createSchemas(Integer.parseInt(countTextBox.getText()), new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void failure(Throwable throwable) {
                        createButton.setEnabled(true);
                    }

                    @Override
                    public void success(Integer integer) {
                        if (integer != null) {
                            Info.show(integer + " created", Info.Type.WARNING);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SCHEMA_ADD, null, null);
                            closeTab();
                        }
                        createButton.setEnabled(true);
                    }
                });
            }

        });
        cancelButton.addClickHandler(clickEvent -> closeTab());
        final KpiCheckBox backupZeroSchema = new KpiCheckBox();
        final WfmButton2 createButtonSecond = new WfmButton2("Create schema second");
        createButtonSecond.addClickHandler(event -> {
            createButton.setEnabled(false);
            if (!countTextBox.getText().trim().equals("") && Integer.parseInt(countTextBox.getText()) > 0) {
                BackendService.App.get().createSchemasSecond(Integer.parseInt(countTextBox.getText()), backupZeroSchema.getValue(), new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void failure(Throwable throwable) {
                        createButton.setEnabled(true);
                        Info.show(throwable.getMessage(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Integer integer) {
                        if (integer != null) {
                            Info.show(integer + " created", Info.Type.WARNING);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SCHEMA_ADD, null, null);
                            closeTab();
                        }
                        createButton.setEnabled(true);
                    }
                });
            }
        });


        FlexTable fTable = new FlexTable();
        fTable.addStyleName("backend-createShcemas-table");
        fTable.setWidget(0, 0, countTextBox);
        fTable.getFlexCellFormatter().setColSpan(0, 0, 4);
        fTable.setWidget(1, 0, createButton);
        fTable.setWidget(1, 1, backupZeroSchema);
        fTable.setWidget(1, 2, createButtonSecond);
        fTable.setWidget(1, 3, cancelButton);

        add(fTable);

        return null;
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
