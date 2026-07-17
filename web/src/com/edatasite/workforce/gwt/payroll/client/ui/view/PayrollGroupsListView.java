package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollBatchData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/21/15
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollGroupsListView extends BaseListView implements Constants {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private ListingPanel<PayrollBatchData> list;

    public PayrollGroupsListView() {
        super(PAYROLL_BATCH, payrollStrings.payrollBatches());
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.PayrollBatchListPanel, getColumn(), getListProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYROLL_BATCH_ADD, PayrollGroupsListView.this, (sender, args) -> list.reloadPage());

        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[7];
        columnConfig[index] = new ColumnDefinitionConfig<PayrollBatchData, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final PayrollBatchData item) {
                MenuBar menuBar = new MenuBar(true);
                int actionItemCount = 0;
                if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_ADD)) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), getIconStyle());
                    edit.ensureDebugId("Payroll_group_edit");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("payrollBatch|summary/" + item.getObjectID(), item.getName()));
                    menuBar.addItem(edit);
                    actionItemCount++;
                }
                if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_DELETE)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    delete.ensureDebugId("Payroll_group_delete");
                    delete.setCommand(() -> {
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmMessages.deletePayrollBatchConfirmation(item.getName()), new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                PayrollService.App.get().deletePayrollBatch(item.getObjectID(), new AsyncCallback<Void>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        list.reloadPage();
                                    }
                                });
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                        wfmMessageBox.setTitle(wfmStrings.confirmation());
                        wfmMessageBox.open();
                    });
                    menuBar.addItem(delete);
                    actionItemCount++;
                }

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }

        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[++index] = new ColumnDefinitionConfig<PayrollBatchData, String>(wfmStrings.name(), "name", 100) {

            @Override
            public String getCellValue(PayrollBatchData data) {
                return data.getName();
            }
        };

        columnConfig[++index] = new ColumnDefinitionConfig<PayrollBatchData, String>(wfmStrings.description(), "description", 200) {

            @Override
            public String getCellValue(PayrollBatchData data) {
                return data.getDescription();
            }
        };

        columnConfig[++index] = new ColumnDefinitionConfig<PayrollBatchData, String>(wfmStrings.type(), "type", 200) {

            @Override
            public String getCellValue(PayrollBatchData data) {
                return data.getType() != null && data.getType() == 0 ? payrollStrings.byDepartment() : payrollStrings.byPosition();
            }
        };
        columnConfig[index].setColumnSortable(false);

        columnConfig[++index] = new ColumnDefinitionConfig<PayrollBatchData, String>(wfmStrings.currency(), "currency", 100) {

            @Override
            public String getCellValue(PayrollBatchData data) {
                return data.getCurrency() != null ? data.getCurrency().getName() : "N/A";
            }
        };
        columnConfig[index].setColumnSortable(false);

        columnConfig[++index] = new ColumnDefinitionConfig<PayrollBatchData, String>(wfmStrings.assignedEmployees(), "assignedEmployees", 100) {

            @Override
            public String getCellValue(PayrollBatchData data) {
                return data.getEmployeesAmount() != null ? String.valueOf(data.getEmployeesAmount()) : "N/A";
            }
        };
        columnConfig[index].setColumnSortable(true);

        columnConfig[++index] = new ColumnDefinitionConfig<PayrollBatchData, String>(payrollStrings.payrollManager(), "payrollManager", 100) {

            @Override
            public String getCellValue(PayrollBatchData data) {
                StringBuilder manager = new StringBuilder();
                if (data.getManagers() != null && data.getManagers().length > 0) {
                    for (SelectItem item : data.getManagers()) {
                        manager.append(item.getName()).append(", ");
                    }
                }
                return manager.toString().replaceAll(", $", "");
            }
        };
        columnConfig[index].setColumnSortable(true);

        return columnConfig;
    }

    public String getIconStyle() {
        return "payroll deduction-list";
    }


    public ListingRequestProvider<PayrollBatchData> getListProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            PayrollService.App.get().getPayrollBatches(filterParametrs, new AbstractAsyncCallback<ListResult<PayrollBatchData>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<PayrollBatchData> list) {
                    callback.onSuccess(list);
                }
            });
        };
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_ADD)) {
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("payrollBatch|add/add"));
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(payrollStrings.currentlyThereAreNoBatches());
                if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_ADD)) {
                    message.setTextBeforeLink(payrollStrings.noGroupsBeforeLinkMessage());
                    message.setHref("payrollBatch|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
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
