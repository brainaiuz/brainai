package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.PaypalReceiptsListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Alisher
 * Date: 03.05.2010
 * Time: 17:18:56
 * To change this template use File | Settings | File Templates.
 */
public class PaypalReceiptsListView extends BaseListView implements Constants {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private ListingPanel<PaypalReceiptsListItem> list;

    public PaypalReceiptsListView() {
        super("paypalreceiptsview", backendStrings.paymentReceipts());
    }

    public String getIconStyle() {
        return "backend payRecListView";
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.BackendPaypalReceiptsListView, getColumns(), getProvider(), getDesigner());
        add(list);
        return null;
    }

    private ListingPanelDesign getDesigner() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return ListingChooseFilter.EMPLOYEE + ListingChooseFilter.DATE;
                    }
                };
            }

//            @Override
//            public HorizontalPanel initTopToolBarWidgets() {
//              ToolBar toolBar = new ToolBar();
//              toolBar.setHeight(30);
//              toolBar.setStyleName("quickViewHeader");
//              topPanel.add(toolBar);
//              topPanel.setCellWidth(toolBar, "100px");
//              topPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
//            }


            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };

    }

    private ColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[7];
        int index = 0;
        columns[index] = new ColumnDefinitionConfig<PaypalReceiptsListItem, String>(backendStrings.companyID(), PaypalReceiptsListItem.COMPANY_ID, 65) {

            @Override
            public String getCellValue(PaypalReceiptsListItem item) {
                return item.getCompanyID() != null ? item.getCompanyID().toString() : "";
            }
        };
        columns[index].setMinimumColumnWidth(65);
        columns[index++].setMaximumColumnWidth(80);
        //columns[index++].setColumnSortable(true);

        columns[index] = new ColumnDefinitionConfig<PaypalReceiptsListItem, String>(wfmStrings.companyName(), PaypalReceiptsListItem.COMPANY_NAME, 200) {

            @Override
            public String getCellValue(PaypalReceiptsListItem item) {
                return item.getCompanyName();
            }
        };
        columns[index++].setMinimumColumnWidth(200);
        //columns[index++].setColumnSortable(true);


        columns[index] = new ColumnDefinitionConfig<PaypalReceiptsListItem, String>(wfmStrings.startDate(), PaypalReceiptsListItem.SUBSCSTARTDATE, 115) {

            @Override
            public String getCellValue(PaypalReceiptsListItem item) {
                return item.getSubscStartDate().substring(0, 19);
            }
        };
        columns[index++].setMinimumColumnWidth(115);
        //columns[index++].setColumnSortable(true);

        columns[index] = new ColumnDefinitionConfig<PaypalReceiptsListItem, String>(wfmStrings.endDate(), PaypalReceiptsListItem.SUBSCENDDATE, 115) {

            @Override
            public String getCellValue(PaypalReceiptsListItem item) {
                return item.getSubscEndDate().substring(0, 19);
            }
        };
        columns[index++].setMinimumColumnWidth(115);
        // columns[index++].setColumnSortable(true);

        columns[index] = new ColumnDefinitionConfig<PaypalReceiptsListItem, Integer>(wfmStrings.numberOfEmployees(), PaypalReceiptsListItem.NUMBEROFEMPLOYEES, 120) {

            @Override
            public Integer getCellValue(PaypalReceiptsListItem item) {
                return item.getNumberOfEmployees();
            }
        };
        //columns[index].setAlignment(ListPanelColumn.ALLIGN_CENTER);
        columns[index++].setMinimumColumnWidth(120);
        //columns[index++].setColumnSortable(true);
        columns[index] = new ColumnDefinitionConfig<PaypalReceiptsListItem, Float>(wfmStrings.paidAmount(), PaypalReceiptsListItem.PAIDAMOUNT, 80) {

            @Override
            public Float getCellValue(PaypalReceiptsListItem item) {
                return item.getPaidAmount();
            }
        };
        columns[index++].setMinimumColumnWidth(80);
        //columns[index++].setColumnSortable(true);

        columns[index] = new ColumnDefinitionConfig<PaypalReceiptsListItem, String>(wfmStrings.paymentType(), PaypalReceiptsListItem.PAYMENTTYPE, 80) {

            @Override
            public String getCellValue(PaypalReceiptsListItem item) {
                return item.getPaymenttype();
            }
        };
        columns[index++].setMinimumColumnWidth(80);


        columns[index] = new ColumnDefinitionConfig<PaypalReceiptsListItem, String>(wfmStrings.status(), PaypalReceiptsListItem.STATUS, 50) {

            @Override
            public String getCellValue(PaypalReceiptsListItem item) {
                return item.getStatus();
            }
        };
        columns[index].setMinimumColumnWidth(50);
        //columns[index++].setColumnSortable(true);

        return columns;
    }

    private ListingRequestProvider<PaypalReceiptsListItem> getProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }

            BackendService.App.get().getPaypalReceiptsList(filterParametrs, new AsyncCallback<ListResult<PaypalReceiptsListItem>>() {

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<PaypalReceiptsListItem> paypalReceiptsListItemListResult) {
                    gStatus();
                    callback.onSuccess(paypalReceiptsListItemListResult);

                }
            });

        };
//
    }

    private void gStatus() {

        BackendService.App.get().getPaypalStatus(new ListingFilterParameter(), new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] items) {
                list.getChooseFilter().setEmployeeList(items);
                list.getChooseFilter().setStatusname("Status");
            }

        });
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
