/*
package com.edatasite.workforce.gwt.invoice.client.ui.view.itemized;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LinkInformation;
import com.edatasite.workforce.gwt.core.client.ui.ModifiableViewPanel;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ListTableItem;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionField;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionLabel;
import com.edatasite.workforce.gwt.invoice.client.localization.InvoiceStrings;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.google.gwt.user.client.ui.HorizontalPanel;
import java.util.ArrayList;

public class ItemViewPanel extends ModifiableViewPanel {

    //	static NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private static final InvoiceStrings invoiceStrings = InvoiceStrings.App.get();

    protected boolean doPreview(Object object, ListTableItem item) {
        if (!(object instanceof NewInvoiceItem)) {
            return false;
        }

        clear();
        final NewInvoiceItem invoiceItem = (NewInvoiceItem) object;
        final ListTableItem tableItem = item;

        PreviewSectionField field;
        PreviewSectionLabel label;
        final PreviewSectionContainer container = new PreviewSectionContainer();
        container.setDescriptionFieldWidth("50%");
        container.setSummaryFieldWidth("50%");

        label = new PreviewSectionLabel(invoiceStrings.itemInformation(), invoiceStrings.summaryOfItem() + ".");
        if (Utils.hasRole(Constants.DR) || Utils.hasRole(Constants.PM) || Utils.hasRole(Constants.ADMIN)) {
            ToolItem edit = new ToolItem(Style.PUSH);
            edit.setText(invoiceStrings.edit());
            edit.setStyleName("whiteText");
            edit.addSelectionListener(new SelectionListener() {
                public void widgetSelected(BaseEvent be) {

                    SinksContainerFactory.entryPoint.onHistoryChanged("item/" + invoiceItem.getID());
                }
            });


            ArrayList links = new ArrayList();
            links.add(new LinkInformation(HorizontalPanel.ALIGN_LEFT, edit));
            setLinks(links);
        }
        field = new PreviewSectionField();
        field.addField(invoiceStrings.itemName(), invoiceItem.getItemName());
        field.addField(invoiceStrings.description(), invoiceItem.getDescription());
        field.addField(invoiceStrings.type(), invoiceItem.getType().getName());
        field.addField(invoiceStrings.quantity(), invoiceItem.getQuantity().compareTo(AccountingConstants.ZERO) == 0 ? "" : AccountingUtils.get().getMoneyFormat(invoiceItem.getQuantity()));
        field.addField(invoiceStrings.unitPrice(), invoiceItem.getUnitPrice().compareTo(AccountingConstants.ZERO) == 0 ? "" : AccountingUtils.get().getMoneyFormat(invoiceItem.getUnitPrice()));
        field.addField(invoiceStrings.unitCost(), invoiceItem.getUnitCost().compareTo(AccountingConstants.ZERO) == 0 ? "" : AccountingUtils.get().getMoneyFormat(invoiceItem.getUnitCost()));
        field.addField(invoiceStrings.vat(), invoiceItem.getTaxItem()!=null ? invoiceItem.getTaxItem().getName() : "");

        container.addSection(label, field);


        clear();
//        setScrollEnabled(true);
        add(container);
//        layout(true);

        return true;

    }

    protected boolean doPreview(Object object) {
        return false;
    }

}
*/
