package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.payment;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollMessages;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrunPayment;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

public class PayrunPaymentListView extends BaseListView implements Constants {

    private static final PayrollMessages payrollMessages = GWT.create(PayrollMessages.class);
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);

    private final Integer groupPayrunID;

    public PayrunPaymentListView(Integer groupPayrunID) {
        super(PAYRUN_PAYMENT_LIST);
        this.groupPayrunID = groupPayrunID;
    }

    private ListingPanel<PayrunPayment> listingPanel;

    protected Widget onInitialize() {
        listingPanel = new GuideListingPanel(ListPanelType.PayslipTableListPanel, getColumn(), getListProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADDITIONAL_PAYMENT_ADD, PayrunPaymentListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADDITIONAL_PAYMENT_DELETE, PayrunPaymentListView.this, (sender, args) -> listingPanel.reloadPage());

        add(listingPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[5];

        //Actions
        columnConfig[index] = new ColumnDefinitionConfig<PayrunPayment, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final PayrunPayment item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_VIEW)) {
                    MenuPopItem view = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("payrunPayment|view/" + item.getObjectID()));
                    actionItemCount++;
                    menuBar.addItem(view);
                }

                if (Utils.hasPermission(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage(payrollMessages.areYouSureWantToDelete(item.getReference()));
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                PayrollService.App.get().deletePayrunPayment(item.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(Boolean result) {
                                        if (Boolean.FALSE.equals(result)) {
                                            Info.show(payrollStrings.payrunPaymentCannotBeDeleted(), Info.Type.WARNING);
                                            return;
                                        }
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.payment()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYRUN_PAYMENT_DELETE, null, PayrunPaymentListView.this);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(delete);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        //Reference
        columnConfig[++index] = new ColumnDefinitionConfig<PayrunPayment, SimpleLink>(wfmStrings.reference(), "reference", 120) {
            @Override
            public SimpleLink getCellValue(PayrunPayment item) {
                return getLink(item.getReference(), "payrunPayment|view/" + item.getObjectID());
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMinimumColumnWidth(100);

        //payment Date
        columnConfig[++index] = new ColumnDefinitionConfig<PayrunPayment, String>(wfmStrings.paymentDate(), "paymentDate", 100) {
            @Override
            public String getCellValue(PayrunPayment item) {
                return item.getPaymentDate() != null ? DateUtils.format(item.getPaymentDate()) : wfmStrings.notAvailable();
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        //Currency
        columnConfig[++index] = new ColumnDefinitionConfig<PayrunPayment, String>(wfmStrings.currency(), "currency", 140) {
            @Override
            public String getCellValue(PayrunPayment item) {
                return item.getCurrency() != null ? item.getCurrency().getName() : wfmStrings.notAvailable();
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);

        //Total
        columnConfig[++index] = new ColumnDefinitionConfig<PayrunPayment, String>(wfmStrings.total(), "total", 100) {
            @Override
            public String getCellValue(PayrunPayment item) {
                return PayrollClientUtils.format(item.getAmount());
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);

        return columnConfig;
    }

    private ListingRequestProvider<PayrunPayment> getListProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setGroupPayrunID(groupPayrunID);
            PayrollService.App.get().getPayrunPaymentList(filterParametrs, new AbstractAsyncCallback<ListResult<PayrunPayment>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<PayrunPayment> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

            }

            @Override
            public Command getAddNewItemCommand() {
                return null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }
        };
    }

    @Override
    public String getIconStyle() {
        return null;
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
}
