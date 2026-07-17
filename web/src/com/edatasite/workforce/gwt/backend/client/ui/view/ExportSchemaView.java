package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Nov 12, 2010
 * Time: 7:18:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExportSchemaView extends View implements Constants, CommandConstants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    public ExportSchemaView() {
        super("exportschemaadd", wfmStrings.exportSchema());
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected Widget onInitialize() {

        final SchemaLookUp schemaLookUp = new SchemaLookUp();

        /*DeferredCommand.addCommand(new Command() {
            public void execute() {

            }
        });*/

        final WfmButton2 exportButton = new WfmButton2(wfmStrings.exportSchema());
        final WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        exportButton.addClickHandler(event -> {
            exportButton.setEnabled(false);
            if (schemaLookUp.getSelectedItem() != null) {
                BackendService.App.get().exportSchema(schemaLookUp.getSelectedItem().getId() + "", new AbstractAsyncCallback<Boolean>() {
                    @Override
                    public void failure(Throwable throwable) {
                        exportButton.setEnabled(true);
                    }

                    @Override
                    public void success(Boolean result) {
                        if (result != null && result) {
                            Info.show("Schema successfully exported.", Info.Type.WARNING);
                            exportButton.setEnabled(true);
                        } else {
                            Info.show("Error during the export.", Info.Type.WARNING);
                            exportButton.setEnabled(true);
                        }
                    }
                });
            }
        });
        cancelButton.addClickHandler(clickEvent -> closeTab());

        FlexTable fTable = new FlexTable();
        fTable.addStyleName("backend-ExportShcema-table");
        fTable.setWidget(0, 0, schemaLookUp);
        fTable.getFlexCellFormatter().setColSpan(0, 0, 2);
        fTable.setWidget(1, 0, exportButton);
        fTable.setWidget(1, 1, cancelButton);

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
