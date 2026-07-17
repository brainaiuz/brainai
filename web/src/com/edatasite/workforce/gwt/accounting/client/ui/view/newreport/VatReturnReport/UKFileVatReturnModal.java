/*
 * Copyright (c) 2023.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VATSettingsItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnService;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;

public class UKFileVatReturnModal extends KpiModal {
    private final Command fileCommand;

    public UKFileVatReturnModal(Command fileCommand) {
        this.fileCommand = fileCommand;
        loadData();
    }

    private void loadData() {
        VatReturnService.App.get().getVATSettings(new AsyncCallback<VATSettingsItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(VATSettingsItem vatSettingsItem) {
                initWidgets(vatSettingsItem);
            }
        });
    }

    private void initWidgets(VATSettingsItem vatSettingsItem) {
        setTitle("Legal Declaration");
        setWidth(650);
        HTMLPanel notice = new HTMLPanel(vatSettingsItem.isAgent() ? wfmStrings.legalDeclarationForAgent() : wfmStrings.legalDeclaration());
        notice.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        notice.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        add(notice);

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> {
            close();
            LoadingPanel.loading(false);
        }));
        addButton(new WfmButton2(wfmStrings.proceed(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            fileCommand.execute();
            close();
        }));
        open();
    }
}
