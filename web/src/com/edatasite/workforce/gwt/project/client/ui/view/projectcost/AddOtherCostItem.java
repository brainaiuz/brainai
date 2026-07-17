package com.edatasite.workforce.gwt.project.client.ui.view.projectcost;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostServiceAsync;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectOtherCostItem;
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
 * User: Dilsh0d
 * Date: 16-May-2010
 * Time: 20:07:35
 */
public class AddOtherCostItem extends View {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    
    private final ProjectCostServiceAsync projectCostService = ProjectCostService.App.get();
    public static final String WIDTH = "150px";

    private Float amount;
    private Float percent;
    private Integer resourceTypeId;

    private Command command;
    private KpiModal shell;
    private TextBox name;
    private TextBox percentCharge;
    private TextBox amountCharge;
    private KpiCheckBox isPercent;
    private HTML errorLabel;

    private WfmButton2 save;
    private WfmButton2 close;

    public AddOtherCostItem(Command command, Integer resourceTypeId) {
        this.command = command;
        this.resourceTypeId = resourceTypeId;
        shell = new KpiModal();
        shell.setTitle(projectStrings.addOtherCostItem());
        shell.setSize(320, 250);

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

        name = new TextBox();
        name.setWidth(WIDTH);

        isPercent = new KpiCheckBox();
        isPercent.addClickHandler(event -> {
            if (isPercent.getValue()) {
                amountCharge.setEnabled(false);
                percentCharge.setEnabled(true);
            } else {
                amountCharge.setEnabled(true);
                percentCharge.setEnabled(false);
            }
        });

        amountCharge = new TextBox();
        amountCharge.setWidth(WIDTH);
        amountCharge.addKeyPressHandler(event -> {
            char code = event.getCharCode();
            if (KeyCodes.KEY_DELETE != code && KeyCodes.KEY_BACKSPACE != code && KeyCodes.KEY_HOME != code &&
                    KeyCodes.KEY_END != code && KeyCodes.KEY_RIGHT != code && KeyCodes.KEY_LEFT != code
                    && (!Character.isDigit(code) && code != '.')) {
                amountCharge.cancelKey();
            }
        });

        percentCharge = new TextBox();
        percentCharge.setWidth(WIDTH);
        percentCharge.setEnabled(false);
        percentCharge.addKeyPressHandler(event -> {
            char code = event.getCharCode();
            if (KeyCodes.KEY_DELETE != code && KeyCodes.KEY_BACKSPACE != code && KeyCodes.KEY_HOME != code &&
                    KeyCodes.KEY_END != code && KeyCodes.KEY_RIGHT != code && KeyCodes.KEY_LEFT != code
                    && (!Character.isDigit(code) && code != '.')) {
                amountCharge.cancelKey();
            }
        });

        save = new WfmButton2(" " + wfmStrings.save() + " ");
        save.addClickHandler(event -> saveCostItem());

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

        table.setHTML(row, 0, "<b>" + wfmStrings.name() + "<font color='red'>*</font>:</b>");
        table.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);
        table.setWidget(row, 1, name);
        table.getFlexCellFormatter().setAlignment(row++, 1, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);

        table.setHTML(row, 0, "<b>is " + wfmStrings.percent() + ":</b>");
        table.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);
        table.setWidget(row, 1, isPercent);
        table.getFlexCellFormatter().setAlignment(row++, 1, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);

        table.setHTML(row, 0, "<b>" + projectStrings.amountCharge() + ":</b>");
        table.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);
        table.setWidget(row, 1, amountCharge);
        table.getFlexCellFormatter().setAlignment(row++, 1, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);

        table.setHTML(row, 0, "<b>" + projectStrings.percentCharge() + ":</b>");
        table.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);
        table.setWidget(row, 1, percentCharge);
        table.getFlexCellFormatter().setAlignment(row++, 1, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);

        table.setWidget(row, 0, hPanel);
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
        table.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_RIGHT, VerticalPanel.ALIGN_MIDDLE);

        shell.add(table);
        shell.open();

        return null;
    }

    private void saveCostItem() {
        if (validation()) {
            save.setEnabled(false);
            close.setEnabled(false);
            ProjectOtherCostItem costItem = new ProjectOtherCostItem();
            costItem.setName(name.getText());
            costItem.setResourceTypeId(resourceTypeId);
            costItem.setPercent(isPercent.getValue());
            costItem.setPercentCharge(percent);
            costItem.setAmountCharge(amount);
            projectCostService.saveOtherCostItem(costItem, new AbstractAsyncCallback<Void>() {
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

    private boolean validation() {
        int error = 0;
        errorLabel.setHTML("&nbsp;");
        if (name.getText() == null || "".equals(name.getText().trim())) {
            error++;
            errorLabel.setHTML("<b style='color:red;'>" + projectStrings.pleaseSelectRequiredField() + "</b>");
        }

        if (isPercent.getValue()) {
            if (percentCharge.getText() != null && !"".equals(percentCharge.getText().trim())) {
                percent = Float.parseFloat(percentCharge.getText());
            }
        } else {
            if (amountCharge.getText() != null && !"".equals(amountCharge.getText().trim())) {
                amount = Float.parseFloat(amountCharge.getText());
            }
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
