package com.edatasite.workforce.gwt.core.client.ui.view;


import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omonullo Abdullaev on 08.04.16.
 */
public class CashAdvancePaymentListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {

    private ListingPanel<CashAdvancePayment> list;
    private CoreServiceAsync coreService = CoreService.App.get();

    private Integer objectId;

    public CashAdvancePaymentListView(Integer objectId) {
        super("cashadvancepayments");
        setDescription(property.getSingular(wfmStrings.cashAdvancePaymentList(), wfmStrings.cashAdvance()));
        this.objectId = objectId;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.CashAdvancePaymentListView, getColumns(), getListingRequestProvider(), getListingPanelDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASH_SAVED, CashAdvancePaymentListView.this, (sender, args) -> list.reloadPage());
        add(list);
        list.reloadPage();
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig anchor = new ColumnDefinitionConfig<CashAdvancePayment, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CashAdvancePayment item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (Utils.hasPermission(PermissionConstants.CASH_ADVANCE_PAYMENT_DELETE)) {
                    MenuPopItem view = new MenuPopItem(wfmStrings.delete(), "delete-icon");
                    view.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage(wfmMessages.areYouSureYouWantToDeleteThe(property.getSingular(wfmStrings.cashAdvancePayment(), wfmStrings.cashAdvance())));
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                coreService.deleteCashAdvancePayment(objectId, item.getId(), new AsyncCallback<TestRPC>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                    }

                                    @Override
                                    public void onSuccess(TestRPC testRPC) {
                                        list.reloadPage();
                                        LoadingPanel.loading(false);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(view);
                }


                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        anchor.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        anchor.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        anchor.setColumnSortable(false);
        columns.add(anchor);

        ColumnDefinitionConfig reference = new ColumnDefinitionConfig<CashAdvancePayment, String>(wfmStrings.reference(), CashAdvancePayment.REFERENCE, 100) {

            @Override
            public String getCellValue(CashAdvancePayment item) {
                return item.getReference() != null ? item.getReference() : wfmStrings.notAvailable();
            }
        };
        reference.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(reference);
        ColumnDefinitionConfig amount = new ColumnDefinitionConfig<CashAdvancePayment, String>(wfmStrings.paymentAmount(), CashAdvancePayment.AMOUNT, 100) {
            @Override
            public String getCellValue(CashAdvancePayment item) {
                return item.getPaymentAmount() != null ? Utils.getNumberFormat().format(item.getPaymentAmount()) :wfmStrings.notAvailable();
            }
        };
        amount.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        columns.add(amount);

        ColumnDefinitionConfig paymentDate = new ColumnDefinitionConfig<CashAdvancePayment, String>(wfmStrings.date(), CashAdvancePayment.DATE, 60) {
            @Override
            public String getCellValue(CashAdvancePayment item) {
                return item.getPaymentDate() != null ? DateUtils.format(item.getPaymentDate().getNonConvertedDate()) : wfmStrings.notAvailable();
            }
        };
        paymentDate.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(paymentDate);

        ColumnDefinitionConfig period = new ColumnDefinitionConfig<CashAdvancePayment, String>(wfmStrings.period(), CashAdvancePayment.PERIOD, 60) {
            @Override
            public String getCellValue(CashAdvancePayment item) {
                return item.getPeriod() != null ? item.getPeriod() : wfmStrings.notAvailable();
            }
        };
        period.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(period);
        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private ListingRequestProvider<CashAdvancePayment> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            filterParametrs.setObjectId(objectId);
            coreService.getCashAdvancePayments(filterParametrs, new AsyncCallback<ListResult<CashAdvancePayment>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<CashAdvancePayment> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
            }
        };
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return CASH_ADVANCE_LIST;
    }
}
