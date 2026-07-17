package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RadioButton;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_DEFAULT_OUTLINE;

public class SupplierPODialog {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SupplierPODialog() {
    }

    void alertMessage(final NewInvoice[] items, final Command listener) {
        final KpiModal dialogBox = new KpiModal();
        dialogBox.setWidth(700);
        dialogBox.setTitle(wfmStrings.information());
        dialogBox.add(new HTML("<p>" + accountingStrings.purchaseConvertMessage() + "</p>"));

        final FlexTable table = new FlexTable();
        table.setStyleName("flexTable");
        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setWidget(0, 0, new HTML(""));
        table.setWidget(0, 1, new HTML("<span  style='width:50%'>" + wfmStrings.number() + "</span>"));
        table.setWidget(0, 2, new HTML("<span  style='width:15%'>" + wfmStrings.type() + "</span>"));
        table.setWidget(0, 3, new HTML("<span  style='width:15%'>" + wfmStrings.date() + "</span>"));
        table.setWidget(0, 4, new HTML("<span  style='width:20%'>" + wfmStrings.total() + "</span>"));
        table.getFlexCellFormatter().setStyleName(0, 0, "flexTable-Label");
        table.getFlexCellFormatter().setStyleName(0, 1, "flexTable-Label");
        table.getFlexCellFormatter().setStyleName(0, 2, "flexTable-Label");
        table.getFlexCellFormatter().setStyleName(0, 3, "flexTable-Label");
        table.getFlexCellFormatter().setStyleName(0, 4, "flexTable-Label text-right");
        int i = 1;
        for (NewInvoice item : items) {
            table.setWidget(i, 0, new KpiRadioButton("items"));
            table.setText(i, 1, item.getPoNumber());
            table.setWidget(i, 2, new HTML(item.getStatus()));
            table.setWidget(i, 3, new HTML(DateUtils.format(item.getInvoiceDate())));
            table.setWidget(i, 4, new HTML(AccountingUtils.get().formatPrice(item.getTotal())));
            table.getFlexCellFormatter().setStyleName(i, 0, "flexTable-td");
            table.getFlexCellFormatter().setStyleName(i, 1, "flexTable-td");
            table.getFlexCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_LEFT);
            table.getFlexCellFormatter().setStyleName(i, 2, "flexTable-td");
            table.getFlexCellFormatter().setStyleName(i, 3, "flexTable-td");
            table.getFlexCellFormatter().setStyleName(i, 4, "flexTable-td text-right");
            i++;
        }
        dialogBox.add(table);

        WfmButton2 okButton = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY);
        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), BTN_DEFAULT_OUTLINE);
        okButton.addClickHandler(clickEvent -> {
            int i1 = 1;
            Integer purchaseOrderID = null;
            for (NewInvoice item : items) {
                RadioButton rd = (RadioButton) table.getWidget(i1, 0);
                if (rd.getValue()) {
                    purchaseOrderID = item.getID();
                }
                i1++;
            }
            if (purchaseOrderID != null) {
                convertToInvoice(purchaseOrderID);
                dialogBox.close();
            } else {
                Info.show(accountingStrings.convertErrorMessage(), Info.Type.WARNING);
            }
        });
        cancelButton.addClickHandler(clickEvent -> {
            dialogBox.close();
            if (listener != null) {
                listener.execute();
            }
        });
        dialogBox.addButton(cancelButton);
        dialogBox.addButton(okButton);
        dialogBox.open();
    }

    private void convertToInvoice(Integer purchaseOrderID) {
        LoadingPanel.loading(true);
        MainLayout.get().getNavToolBar().getTabContainer().getSelectedTab().onClose();
        SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_INVOICE + "|add/add/convertToInvoice/" + purchaseOrderID);
    }
}