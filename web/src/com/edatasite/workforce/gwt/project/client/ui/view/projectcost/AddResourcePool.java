package com.edatasite.workforce.gwt.project.client.ui.view.projectcost;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * User: ${Dilsh0d}
 * Date: 05-May-2010
 * Time: 09:56:26
 */
public class AddResourcePool extends View {

    private final ProjectCostServiceAsync projectCostService = ProjectCostService.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private HTML errorLabel;
    private KpiModal shell;
    private TextBox poolName;
    private TextBox rateAvge;
    private Command command;
    private WfmButton2 save;
    private WfmButton2 close;

    private Float rate;
    private Integer resourceTypeId;

    public AddResourcePool(Command command, Integer resourceTypeId) {
        this.command = command;
        this.resourceTypeId = resourceTypeId;
        shell = new KpiModal();
        shell.setTitle(projectStrings.addResourcePool());
        shell.setSize(320, 200);

        LoadingPanel.loading(true);
        asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(Throwable reason) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.failedToDownloadCodeForThisWidget() + " (" + reason + ")", Info.Type.WARNING);
            }

            public void success(Widget result) {
                LoadingPanel.loading(false);
            }
        });
    }

    public Widget onInitialize() {
        errorLabel = new HTML("&nbsp;");
        poolName = new TextBox();
        poolName.setWidth("150px");
        rateAvge = new TextBox();
        rateAvge.setWidth("150px");
        rateAvge.addKeyPressHandler(event -> {
            char code = event.getCharCode();
            if (KeyCodes.KEY_DELETE != code && KeyCodes.KEY_BACKSPACE != code && KeyCodes.KEY_HOME != code &&
                    KeyCodes.KEY_END != code && KeyCodes.KEY_RIGHT != code && KeyCodes.KEY_LEFT != code
                    && (!Character.isDigit(code) && code != '.')) {
                rateAvge.cancelKey();
            }
        });

        save = new WfmButton2(" " + wfmStrings.save() + " ");
        save.addClickHandler(event -> saveResourcePool());
        close = new WfmButton2(" " + wfmStrings.close() + " ");
        close.addClickHandler(event -> shell.close());
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setSpacing(5);
        hPanel.add(save);
        hPanel.add(close);

        int row = 0;
        FlexTable table = new FlexTable();
        table.setCellPadding(5);
        table.setCellSpacing(5);

        table.setWidget(row, 0, errorLabel);
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
        table.getFlexCellFormatter().setAlignment(row++, 0, HorizontalPanel.ALIGN_RIGHT, VerticalPanel.ALIGN_MIDDLE);

        table.setHTML(row, 0, "<b>" + projectStrings.resourcePoolName() + "<font color='red'>*</font>:</b>");
        table.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);

        table.setWidget(row, 1, poolName);
        table.getFlexCellFormatter().setAlignment(row++, 1, HorizontalPanel.ALIGN_RIGHT, VerticalPanel.ALIGN_MIDDLE);

        table.setHTML(row, 0, "<b>Avg Rate:</b>");
        table.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);

        table.setWidget(row, 1, rateAvge);
        table.getFlexCellFormatter().setAlignment(row++, 1, HorizontalPanel.ALIGN_RIGHT, VerticalPanel.ALIGN_MIDDLE);

        table.setWidget(row, 0, hPanel);
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
        table.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_RIGHT, VerticalPanel.ALIGN_MIDDLE);

        shell.add(table);
        shell.open();
        return null;
    }

    private void saveResourcePool() {
        if (validate()) {
            save.setEnabled(false);
            close.setEnabled(false);
            projectCostService.saveResourcePool(resourceTypeId, poolName.getText(), rate, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable caught) {
                    shell.close();
                }

                public void success(Void result) {
                    command.execute();
                    shell.close();
                }
            });
        }
    }

    private boolean validate() {
        int error = 0;
        errorLabel.setHTML("&nbsp;");
        if (rateAvge.getText() != null && !"".equals(rateAvge.getText().trim())) {
            rate = Float.parseFloat(rateAvge.getText());
        }
        if (poolName.getText() == null || "".equals(poolName.getText().trim())) {
            error++;
            errorLabel.setHTML("<b style='color:red;'>" + projectStrings.pleaseSelectRequiredField() + "</b>");
        }

        return error == 0;
    }


    public String getIconStyle() {
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
