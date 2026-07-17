package com.edatasite.workforce.gwt.project.client.ui.view.projectcost;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: ${Dilsh0d}
 * Date: 05-May-2010
 * Time: 09:56:37
 */
public class AddResource extends View implements Constants {
    private final ProjectCostServiceAsync projectCostService = ProjectCostService.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private HTML errorLabel;
    private final KpiModal shell;
    private TextBox resourceName;
    private DataListBox resourceNameBox;
    private TextBox resourceRate;
    private final Command command;
    private WfmButton2 save;
    private WfmButton2 close;

    private Float rate;
    private Integer employeeId;
    private final Integer resourcePoolId;
    private final Integer resourceTypeId;
    private String name;
    private final String resourceTypeName;

    public AddResource(Command command, Integer resourceTypeId, String resourceTypeName, Integer resourcePoolId) {
        this.command = command;
        this.resourceTypeId = resourceTypeId;
        this.resourcePoolId = resourcePoolId;
        this.resourceTypeName = resourceTypeName;
        shell = new KpiModal();
        shell.setTitle(projectStrings.addResource());
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
        if (RESOURCE_EMPLOYEE.equals(resourceTypeName)) {
            resourceNameBox = new DataListBox();
            resourceNameBox.setWidth("150px");
        } else {
            resourceName = new TextBox();
            resourceName.setWidth("150px");
        }
        resourceRate = new TextBox();
        resourceRate.setWidth("150px");
        resourceRate.addKeyPressHandler(event -> {
            char code = event.getCharCode();
            if (KeyCodes.KEY_DELETE != code && KeyCodes.KEY_BACKSPACE != code && KeyCodes.KEY_HOME != code &&
                    KeyCodes.KEY_END != code && KeyCodes.KEY_RIGHT != code && KeyCodes.KEY_LEFT != code
                    && (!Character.isDigit(code) && code != '.')) {
                resourceRate.cancelKey();
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

        table.setHTML(row, 0, "<b>" + projectStrings.resourceName() + "<font color='red'>*</font>:</b>");
        table.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);
        if (RESOURCE_EMPLOYEE.equals(resourceTypeName)) {
            table.setWidget(row, 1, resourceNameBox);
            save.setEnabled(false);
            close.setEnabled(false);
            projectCostService.getCompanyEmployeesResourceIdNull(new AbstractAsyncCallback<SelectItem[]>() {
                public void failure(Throwable caught) {
                    save.setEnabled(true);
                    close.setEnabled(true);
                }

                public void success(SelectItem[] result) {
                    save.setEnabled(true);
                    close.setEnabled(true);
                    resourceNameBox.setItems(result);
                }
            });
        } else {
            table.setWidget(row, 1, resourceName);
        }
        table.getFlexCellFormatter().setAlignment(row++, 1, HorizontalPanel.ALIGN_RIGHT, VerticalPanel.ALIGN_MIDDLE);

        table.setHTML(row, 0, "<b>" + wfmStrings.rate() + ":</b>");
        table.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);

        table.setWidget(row, 1, this.resourceRate);
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
            if (RESOURCE_EMPLOYEE.equals(resourceTypeName)) {
                name = resourceNameBox.getSelectedItem().getName();
                employeeId = resourceNameBox.getSelectedItem().getId();
            } else {
                name = resourceName.getText();
            }

            projectCostService.saveResource(name, rate, resourceTypeId, resourcePoolId, employeeId, new AbstractAsyncCallback<Void>() {
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
        if (resourceRate.getText() != null && !"".equals(resourceRate.getText().trim())) {
            rate = Float.parseFloat(resourceRate.getText());
        }
        if (resourceName != null && (resourceName.getText() == null || "".equals(resourceName.getText().trim()))) {
            error++;
            errorLabel.setHTML("<b style='color:red;'>" + projectStrings.pleaseSelectRequiredField() + "</b>");
        }

        if (resourceNameBox != null && resourceNameBox.getSelectedIndex() == 0) {
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
