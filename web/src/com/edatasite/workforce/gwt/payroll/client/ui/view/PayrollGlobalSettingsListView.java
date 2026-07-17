package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollGlobalSettingsData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;


/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 09/11/2014
 * Time: 04:08:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollGlobalSettingsListView extends BaseListView implements Constants {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private ListingPanel<PayrollGlobalSettingsData> list;

    public PayrollGlobalSettingsListView() {
        super(PAYMENT_LIST, payrollStrings.bulkUpdate());
    }


    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.PaymentDeductionListPanel, getColumn(), getListProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYMENT_DEDUCTION_ADD, PayrollGlobalSettingsListView.this, (sender, args) -> list.reloadPage());

        add(list);
        return null;
    }


    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.PAYROLL_PAYMENT_DEDUCATION_ADD)) {
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("payrollGlobalSettings|add/add"));
                    return addNew;
                } else {
                    return null;
                }
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(payrollStrings.currentlyThereAreNoPayments());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<PayrollGlobalSettingsData> getListProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            PayrollService.App.get().getPaymentDections(filterParametrs, new AbstractAsyncCallback<ListResult<PayrollGlobalSettingsData>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<PayrollGlobalSettingsData> list) {
                    callback.onSuccess(list);
                }
            });
        };
    }

    private ColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[4];

        columnConfig[index] = new ColumnDefinitionConfig<PayrollGlobalSettingsData, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final PayrollGlobalSettingsData item) {
                if (Utils.hasPermission(PermissionConstants.PAYROLL_PAYMENT_DEDUCATION_VIEW)) {

                    int actionItemCount = 0;
                    MenuBar menuBar = new MenuBar(true);

                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), getIconStyle());
                    edit.ensureDebugId("Payroll_global_setting_edit");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("payrollGlobalSettings|summary/" + item.getObjectID()));
                    menuBar.addItem(edit);

                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), getIconStyle());
                    delete.ensureDebugId("Payroll_global__setting_delete");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.OkCancel, true);
                        messageBox.setTitle(wfmStrings.confirmationMessage());
                        messageBox.setMessage(payrollStrings.globalSettingsDeleteConfirmationMessage());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                                messageBox.close();
                            }

                            @Override
                            public void onSubmit() {
                                PayrollService.App.get().deletePaymentDeductionSettings(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                    }

                                    @Override
                                    public void success(Void result) {
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.settings()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_DEDUCTION_ADD, null, PayrollGlobalSettingsListView.this);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    menuBar.addItem(delete);
                    actionItemCount++;


                    ToolItem toolItem = new ToolItem(actionItemCount);
                    toolItem.setWidget(menuBar);
                    return toolItem.getAction();
                } else {
                    return null;
                }
            }

        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[index++].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[index] = new ColumnDefinitionConfig<PayrollGlobalSettingsData, String>(wfmStrings.name(), "name", 140) {
            @Override
            public String getCellValue(PayrollGlobalSettingsData item) {
                return item.getName();
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index++].setMinimumColumnWidth(70);

        columnConfig[index] = new ColumnDefinitionConfig<PayrollGlobalSettingsData, String>(wfmStrings.settings(), "settings", 140) {
            @Override
            public String getCellValue(PayrollGlobalSettingsData item) {
                String value = "";
                if (item.getSettingsType() == null || item.getSettingsType() == 0) {
                    value = payrollStrings.byPayrollBatch();
                } else if (item.getSettingsType() == 1) {
                    value = payrollStrings.byPosition();
                } else {
                    value = payrollStrings.byDepartment();
                }
                return value;
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index++].setMinimumColumnWidth(70);

        columnConfig[index] = new ColumnDefinitionConfig<PayrollGlobalSettingsData, String>(wfmStrings.paymentType(), "type", 140) {
            @Override
            public String getCellValue(PayrollGlobalSettingsData item) {
                String value;
                switch (item.getRateType() != null ? item.getRateType() : "") {
                    case FIXED_RATE:
                        value = wfmStrings.fixedRate();
                        break;
                    case FIXED_OVERTIME_RATE:
                        value = wfmStrings.fixedRateWithOvertime();
                        break;
                    case FIXED_HRMS_OVERTIME_RATE:
                        value = wfmStrings.fixedHourlyRate();
                        break;
                    case FIXED_TIMESHEET_OVERTIME_RATE:
                        value = Property.get(Constants.TIMESHEET, payrollStrings.basedOnMonthlyTimesheet(), wfmStrings.timesheet());
                        break;
                    default:
                        value = "n/a";
                        break;
                }
                return value;
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index++].setMinimumColumnWidth(70);

        columnConfig[index] = new ColumnDefinitionConfig<PayrollGlobalSettingsData, String>(wfmStrings.date(), "date", 140) {
            @Override
            public String getCellValue(PayrollGlobalSettingsData item) {
                return DateUtils.formatInternal(item.getLastUpdate());
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[index++].setMinimumColumnWidth(70);
        return columnConfig;
    }

    public String getIconStyle() {
        return "payroll deduction-list";
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
