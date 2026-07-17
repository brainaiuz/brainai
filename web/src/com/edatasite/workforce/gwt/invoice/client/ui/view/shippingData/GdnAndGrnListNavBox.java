package com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.util.Collections;
import java.util.List;

import static com.edatasite.workforce.core.domain.accounting.EdsRFP.DRAFT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PS_CLOSED;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_INVOICE_EDIT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.*;
import static com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager.ACCOUNTING_PURCHASE_INVOICE_EDIT;
import static com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager.ACCOUNTING_PURCHASE_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager.ACCOUNTING_SALES_INVOICE_SUMMARY;

public class GdnAndGrnListNavBox extends KpiSideNavBox implements Colapse {

    private DataGrid<ShippingData> dataGrid;
    private ListDataProvider<ShippingData> dataProvider;
    public static final ProvidesKey<ShippingData> KEY_PROVIDER = item -> item == null ? null : item.getId();
    private Integer quoteId;
    private List<ShippingData> shippingDataList;
    private boolean isGrn;
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public GdnAndGrnListNavBox() {
        super(KpiSideNavBox.WIDE_FORM_WIDTH);
    }

    public GdnAndGrnListNavBox(Integer quoteId, boolean isGrn) {
        super(KpiSideNavBox.WIDE_FORM_WIDTH);
        this.quoteId = quoteId;
        this.isGrn = isGrn;
        init();
    }

    public GdnAndGrnListNavBox(List<ShippingData> shippingDataList, boolean isGrn) {
        super(KpiSideNavBox.WIDE_FORM_WIDTH);
        this.shippingDataList = shippingDataList;
        this.isGrn = isGrn;
        init();
    }

    protected void init() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new DataGrid<>(KEY_PROVIDER);
        dataGrid.addStyleName("gdn-list");
//        dataGrid.setWidth("100%");
//        dataGrid.setHeight("570px");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataProvider.addDataDisplay(dataGrid);
        if (quoteId != null) {
            loadData();
        } else if (shippingDataList != null && shippingDataList.size() > 0) {
            setValues(shippingDataList);
            initTableColumns();
        }
        Heading header = new Heading(HeadingSize.H1);
        header.setText(isGrn ? accountingStrings.goodsReceivedNotes() : accountingStrings.goodsDeliveredNotes());
        addHeader(header);
        addBody(dataGrid);
    }

    private void loadData() {
        LoadingPanel.loading(true);
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEntityID(quoteId);
        fp.setObjectId(quoteId);
        fp.setIsGdn(!isGrn);
        QuoteService.App.get().getShippingDataList(fp, new AbstractAsyncCallback<ListResult<ShippingData>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ListResult<ShippingData> result) {
                if (result != null) {
                    setValues(result);
                    initTableColumns();
                }
                LoadingPanel.loading(false);
            }
        });
    }

    private void initTableColumns() {

        final SimpleLinkCell[] cell = {new SimpleLinkCell()};
        Column<ShippingData, String> number = new Column<ShippingData, String>(cell[0]) {
            @Override
            public String getValue(ShippingData data) {
                cell[0].setClickHandler(e -> {
                    if (isGrn) {
                        if (Utils.hasPermission(ACCOUNTING_GRN_SUMMARY)) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("grn|summary/" + data.getId(), data.getNumber(), data.getClientName());
                            remove();
                        }
                    } else {
                        if (Utils.hasPermission(ACCOUNTING_GDN_SUMMARY)) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("gdn|summary/" + data.getId(), data.getNumber(), data.getClientName());
                            remove();
                        }
                    }
                });
                return data.getNumber();
            }
        };
        dataGrid.addColumn(number, wfmStrings.number());
        dataGrid.setColumnWidth(number, 25, com.google.gwt.dom.client.Style.Unit.PCT);

        //date
        Column<ShippingData, String> date = new Column<ShippingData, String>(new TextCell()) {
            @Override
            public String getValue(final ShippingData data) {
                return data.getShippingDate() != null ? DateUtils.format(data.getShippingDate().getNonConvertedDate()) : "";
            }
        };
        dataGrid.addColumn(date, !isGrn ? wfmStrings.shippedDate() : wfmStrings.receivedDate());
        dataGrid.setColumnWidth(date, 27, com.google.gwt.dom.client.Style.Unit.PCT);

        //Shipping Label
        Column<ShippingData, String> shippingLabel = new Column<ShippingData, String>(new TextCell()) {
            @Override
            public String getValue(final ShippingData data) {
                return data.getShippingLabel() != null ? data.getShippingLabel() : "";
            }
        };
        dataGrid.addColumn(shippingLabel, accountingStrings.shippingLabel());
        dataGrid.setColumnWidth(shippingLabel, 30, com.google.gwt.dom.client.Style.Unit.PCT);


        //Creator
        Column<ShippingData, String> creator = new Column<ShippingData, String>(new TextCell()) {
            @Override
            public String getValue(final ShippingData data) {
                return data.getCreatorName();
            }
        };
        dataGrid.addColumn(creator, wfmStrings.createdBy());
        dataGrid.setColumnWidth(creator, 30, com.google.gwt.dom.client.Style.Unit.PCT);

        SimpleLinkCell simpleLinkCell = new SimpleLinkCell();
        Column<ShippingData, String> convert = new Column<ShippingData, String>(simpleLinkCell) {
            @Override
            public String getValue(ShippingData data) {

                NewInvoice convertedInvoice = data.getInvoice();

                simpleLinkCell.setClickHandler(e -> {

                    if (convertedInvoice != null && data.getInvoice().getInvoiceNumber().trim().length() > 0) {

                        if (isGrn) {

                            if (!DRAFT.equals(convertedInvoice.getStatusCode())) {

                                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_INVOICE_SUMMARY : ACCOUNTING_PURCHASE_INVOICE_SUMMARY)) {
                                    SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|summary/" + convertedInvoice.getID(), convertedInvoice.getInvoiceNumber());
                                }
                            } else {
                                boolean editPermission = Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_INVOICE_EDIT : ACCOUNTING_PURCHASE_INVOICE_EDIT);
                                boolean editFullPermission = Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_INVOICE_FULL_EDIT_ACCESS : ACCOUNTING_PURCHASE_INVOICE_FULL_EDIT_ACCESS);
                                boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(convertedInvoice.getProjectStatusCode()));
                                if (hasAccessToChange && ((convertedInvoice.isSubmitter(Utils.getUserID()) && editPermission) || editFullPermission) && !convertedInvoice.hasAnyPayment()) {
                                    SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|edit/" + convertedInvoice.getID(), convertedInvoice.getInvoiceNumber());
                                }
                            }
                            remove();
                        } else {

                            if (!DRAFT.equals(convertedInvoice.getStatusCode())) {

                                if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_SUMMARY)) {
                                    SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|summary/" + convertedInvoice.getID(), convertedInvoice.getInvoiceNumber());
                                }
                            } else {
                                boolean editPermission = Utils.hasPermission(ACCOUNTING_SALES_INVOICE_EDIT);
                                boolean editFullPermission = Utils.hasPermission(ACCOUNTING_SALES_INVOICE_FULL_EDIT_ACCESS);

                                if (convertedInvoice.isSubmitter(Utils.getUserID()) && editPermission || editFullPermission) {
                                    SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|edit/" + convertedInvoice.getID(), convertedInvoice.getInvoiceNumber());
                                }
                            }
                            remove();
                        }

                    }
                });

                return data.getInvoice() != null ? data.getInvoice().getInvoiceNumber() : "";
            }
        };
        dataGrid.addColumn(convert, wfmStrings.invoiceNumber());
        dataGrid.setColumnWidth(convert, 25, com.google.gwt.dom.client.Style.Unit.PCT);


        Column<ShippingData, String> status = new Column<ShippingData, String>(new TextCell()) {
            @Override
            public String getValue(final ShippingData data) {
                return data.getInvoice() != null ? data.getInvoice().getStatus() : "";
            }
        };
        dataGrid.addColumn(status, wfmStrings.status());
        dataGrid.setColumnWidth(status, 25, com.google.gwt.dom.client.Style.Unit.PCT);

    }

    protected void setValues(List<ShippingData> result) {
        ShippingData[] shippingData = new ShippingData[result.size()];
        int i = 0;
        for (ShippingData shippingData1 : result) {
            shippingData[i] = shippingData1;
            i++;
        }
        initDataProviderApply(shippingData);
        dataProvider.refresh();
    }

    protected void setValues(ListResult<ShippingData> result) {
        ShippingData[] shippingData = new ShippingData[result.getList().size()];
        int i = 0;
        for (ShippingData shippingData1 : result.getList()) {
            shippingData[i] = shippingData1;
            i++;
        }
        initDataProviderApply(shippingData);
        dataProvider.refresh();
    }

    private void initDataProviderApply(ShippingData[] shippingData) {
        List<ShippingData> items = dataProvider.getList();
        items.clear();
        Collections.addAll(items, shippingData);
    }
}
