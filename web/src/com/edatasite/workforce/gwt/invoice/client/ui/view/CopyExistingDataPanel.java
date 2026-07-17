package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceQuoteLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12.03.2010
 * Time: 20:16:26
 */
public class CopyExistingDataPanel extends HorizontalPanel {
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final String type;
    private String linkName;
    private InvoiceQuoteLookUp invoiceQuoteLookUp;
    private CopyExistingDataPanelListener listener;
    private SimpleLink copyFromExistingLink;
    private final String copyExisitingDataPanel = "copy_exisiting_data_panel_";

    private HTML errorLabel;

    public CopyExistingDataPanel(String type) {
        this(type, null);
    }

    public CopyExistingDataPanel(String type, CopyExistingDataPanelListener listener) {
        this.type = type;
        if (Constants.SALE_QUOTE.equals(type)) {
            linkName = wfmStrings.salesQuote();
        } else if (Constants.SALE_ORDER.equals(type)) {
            linkName = accountingStrings.salesOrder();
        } else if (Constants.SALE_INVOICE.equals(type)) {
            linkName = wfmStrings.salesInvoice();
        } else if (Constants.PURCHASE_INVOICE.equals(type)) {
            linkName = wfmStrings.purchaseinvoice();
        } else if (Constants.PURCHASE_ORDER.equals(type)) {
            linkName = wfmStrings.purchaseorder();
        }
        this.listener = listener;
        initialize();
    }

    private void initialize() {
        copyFromExistingLink = new SimpleLink(accountingStrings.copyFrom());
        copyFromExistingLink.addClickHandler(event -> showCopyFromExistingShell());
        add(new Label());
        add(copyFromExistingLink);
        setCellWidth(getWidget(0), "85px");
    }

    private void showCopyFromExistingShell() {
        final KpiModal shell = new KpiModal();
        shell.setSize(200, 120);
        shell.setTitle(accountingMessages.select(linkName));
        invoiceQuoteLookUp = new InvoiceQuoteLookUp(type);
        invoiceQuoteLookUp.ensureDebugId(copyExisitingDataPanel+"invoiceQuoteLookUp");
        invoiceQuoteLookUp.getSuggestBox().setWidth("150px");

        errorLabel = new HTML();

        WfmButton2 select = new WfmButton2(accountingMessages.select(""));
        select.ensureDebugId(copyExisitingDataPanel+"select");
        select.addClickHandler(be -> {
            if (invoiceQuoteLookUp.getSelectedItemID() == null) {
                return;
            }
            LoadingPanel.loading(true);
            listener.setData(invoiceQuoteLookUp.getSelectedItemID());
            shell.close();
            LoadingPanel.loading(false);
        });
        FlexTable table = new FlexTable();
        table.setWidget(0, 0, invoiceQuoteLookUp);
        table.setWidget(1, 0, errorLabel);
        table.setWidget(2, 0, select);
        table.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        table.setCellSpacing(10);
        shell.add(table);
        shell.open();
    }

    public SimpleLink getCopyFromExistingLink() {
        return copyFromExistingLink;
    }

    public void setListener(CopyExistingDataPanelListener listener) {
        this.listener = listener;
    }

    @Override
    public void setVisible(boolean visible) {
        if (copyFromExistingLink != null)
            copyFromExistingLink.setVisible(visible);
    }
}
