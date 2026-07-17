package com.edatasite.workforce.gwt.core.client.ui.view.recurring;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialLink;

public class RecurringPayDeductionListView extends BaseListView implements Constants {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    private ListingPanel<CashAdvanceItem> listingPanel;

    public RecurringPayDeductionListView() {
        super(RECURRING_PAY_DEDUCTION_LIST);
        setDescription(property.getSingular(wfmStrings.recurringPayDeduction()));
        if (hasPermissionToAdd()) {
            setAddNew("recurringDeduction|add/add");
        }
    }

    @Override
    protected Widget onInitialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYROLL_RECURRING_PD_ADD, RecurringPayDeductionListView.this, (sender, args) -> {
            listingPanel.reloadPage();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYROLL_RECURRING_PD_DELETE, RecurringPayDeductionListView.this, (sender, args) -> {
            listingPanel.reloadPage();
        });
        listingPanel = new GuideListingPanel(ListPanelType.RecurringPayDeductionPanel, getColumn(), getListingRequestProvider(), getListDesign());
        add(listingPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[9];
        columnConfig[index] = new ColumnDefinitionConfig<RecurringPayDeductItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final RecurringPayDeductItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                String link = getLink(item);

                if (!DRAFT.equals(item.getStatus().getCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_RECURRING_PD_VIEW)) {
                    MenuPopItem view = new MenuPopItem(wfmStrings.summaryView(), "icon-employee-edit-profile");
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(link + "|summary/" + item.getObjectID(), item.getEmployeeName(), item.getEmployeeName()));
                    actionItemCount++;
                    menuBar.addItem(view);
                }

                if (!APPROVED.equals(item.getStatus().getCode()) && !POSTED.equals(item.getStatus().getCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_RECURRING_PD_EDIT)) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(link + "|edit/" + item.getObjectID(), item.getEmployeeName(), item.getEmployeeName()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }

                if (Utils.hasPermission(PermissionConstants.PAYROLL_RECURRING_PD_DELETE)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                PayrollService.App.get().deleteRecurringPaymentDeduction(item.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(Boolean result) {
                                        String message = PayType.PAYMENT.equals(item.getPayType()) ? payrollStrings.payment() : payrollStrings.deduction();
                                        if (result) {
                                            Info.show(property.getSingular(message) + " " + wfmStrings.messSuccessfulyyDeleted());
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYROLL_RECURRING_PD_DELETE, null, RecurringPayDeductionListView.this);
                                        } else {
                                            Info.warn(property.getSingular(wfmStrings.cashAdvanceCannotDelete(), message));
                                        }
                                    }
                                });
                            }
                        });
                        messageBox.open();

                    });
                    menuBar.addItem(delete);

                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[index++].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[index] = new ColumnDefinitionConfig<RecurringPayDeductItem, String>(wfmStrings.employeeCode(), RecurringPayDeductItem.EMPLOYEE_CODE, 100) {
            @Override
            public String getCellValue(RecurringPayDeductItem item) {
                return item.getEmployeeCode();
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<RecurringPayDeductItem, SimpleLink>(wfmStrings.name(), RecurringPayDeductItem.EMPLOYEE_NAME, 140) {
            @Override
            public SimpleLink getCellValue(RecurringPayDeductItem item) {
                String link = getLink(item);
                if (Utils.hasPermission(PermissionConstants.PAYROLL_RECURRING_PD_VIEW)) {
                    String action = null;
                    if (DRAFT.equals(item.getStatus().getCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_RECURRING_PD_EDIT)) {
                        action = link + "|edit/" + item.getObjectID();
                    } else {
                        action = link + "|summary/" + item.getObjectID();
                    }
                    return getLink(item.getEmployeeName(), action, item.getEmployeeName(), item.getEmployeeName());
                } else {
                    return getLink(item.getEmployeeName(), null);
                }
            }
        };
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<RecurringPayDeductItem, String>(wfmStrings.fromDate(), RecurringPayDeductItem.FROM_DATE, 140) {
            @Override
            public String getCellValue(RecurringPayDeductItem item) {
                return DateUtils.format(item.getFromDate());
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<RecurringPayDeductItem, String>(wfmStrings.toDate(), RecurringPayDeductItem.TO_DATE, 140) {
            @Override
            public String getCellValue(RecurringPayDeductItem item) {
                return DateUtils.format(item.getToDate());
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<RecurringPayDeductItem, String>(wfmStrings.approver(), RecurringPayDeductItem.APPROVER, 140) {
            @Override
            public String getCellValue(RecurringPayDeductItem item) {
                return item.getApprover() != null ? item.getApprover().getName() : wfmStrings.na();
            }
        };
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<RecurringPayDeductItem, String>(wfmStrings.totalAmount(), RecurringPayDeductItem.TOTAL, 140) {
            @Override
            public String getCellValue(RecurringPayDeductItem item) {
                return item.getTotalLimit() != null ? Utils.getCalculationNumberFormat().format(item.getTotalLimit()) : wfmStrings.na();
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<RecurringPayDeductItem, String>(wfmStrings.dueAmount(), RecurringPayDeductItem.REMAINING_AMOUNT, 140) {
            @Override
            public String getCellValue(RecurringPayDeductItem item) {
                return item.getRemainingAmount() != null ? Utils.getNumberFormat().format(item.getRemainingAmount()) : wfmStrings.na();
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<RecurringPayDeductItem, String>(wfmStrings.status(), RecurringPayDeductItem.STATUS, 140) {
            @Override
            public String getCellValue(RecurringPayDeductItem item) {
                return item.getStatus().getName() != null ? item.getStatus().getName() : "";
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[index++].setMinimumColumnWidth(100);

        return columnConfig;
    }

    private ListingRequestProvider<RecurringPayDeductItem> getListingRequestProvider() {
        return (filterParametrs, listingCallback) -> {
            PayrollService.App.get().getRecurringPaymentDeductionList(filterParametrs, new AsyncCallback<ListResult<RecurringPayDeductItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    if (listingCallback != null) {
                        listingCallback.onFailure(throwable);
                    }
                }

                @Override
                public void onSuccess(ListResult<RecurringPayDeductItem> result) {
                    if (listingCallback != null) {
                        listingCallback.onSuccess(result);
                    }
                }
            });
        };
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? RecurringPayDeductionListView.this::addNew : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToAdd()) {
                    MaterialMenuBar menuBar = new MaterialMenuBar();
                    ActionButton addNew = getAddNewButton(ActionButton.Type.TOOLMENU);

                    MaterialLink materialLinkPayment = new MaterialLink(payrollStrings.payment());
                    materialLinkPayment.ensureDebugId("payment");
                    materialLinkPayment.setPaddingBottom(8);
                    materialLinkPayment.setPaddingTop(8);
                    materialLinkPayment.setPaddingLeft(15);
                    materialLinkPayment.setPaddingRight(15);
                    materialLinkPayment.setHoverable(true);
                    materialLinkPayment.setWidth("100%");
                    materialLinkPayment.getElement().getStyle().setColor("#333");
                    materialLinkPayment.setShadow(1);
                    materialLinkPayment.setBorderRadius("5px");
                    materialLinkPayment.setDisplay(Display.BLOCK);
                    materialLinkPayment.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("recurringPayment|add/add/"));
                    menuBar.add(materialLinkPayment);

                    MaterialLink materialLinkDeduction = new MaterialLink(wfmStrings.deduction());
                    materialLinkDeduction.ensureDebugId("deduction");
                    materialLinkDeduction.setPaddingBottom(8);
                    materialLinkDeduction.setPaddingTop(8);
                    materialLinkDeduction.setPaddingLeft(15);
                    materialLinkDeduction.setPaddingRight(15);
                    materialLinkDeduction.setHoverable(true);
                    materialLinkDeduction.getElement().getStyle().setColor("#333");
                    materialLinkDeduction.setShadow(1);
                    materialLinkDeduction.setWidth("100%");
                    materialLinkDeduction.setBorderRadius("5px");
                    materialLinkDeduction.setDisplay(Display.BLOCK);
                    materialLinkDeduction.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("recurringDeduction|add/add/"));
                    menuBar.add(materialLinkDeduction);

                    addNew.setMenu(menuBar);

                    MaterialLink materialLinkCustomDeduction = new MaterialLink(wfmStrings.alimony());
                    materialLinkCustomDeduction.ensureDebugId("deduction");
                    materialLinkCustomDeduction.setPaddingBottom(8);
                    materialLinkCustomDeduction.setPaddingTop(8);
                    materialLinkCustomDeduction.setPaddingLeft(15);
                    materialLinkCustomDeduction.setPaddingRight(15);
                    materialLinkCustomDeduction.setHoverable(true);
                    materialLinkCustomDeduction.getElement().getStyle().setColor("#333");
                    materialLinkCustomDeduction.setShadow(1);
                    materialLinkCustomDeduction.setWidth("100%");
                    materialLinkCustomDeduction.setBorderRadius("5px");
                    materialLinkCustomDeduction.setDisplay(Display.BLOCK);
                    materialLinkCustomDeduction.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("recurringCustomDeduction|add/add/"));
                    menuBar.add(materialLinkCustomDeduction);

                    addNew.setMenu(menuBar);
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(wfmStrings.currentlyThereAreNoRecurringPayDeductions(), wfmStrings.recurringPayDeduction()));
                message.setHref(hasPermissionToAdd() ? "recurringDeduction|add/add" : "");
                message.setTextBeforeLink(property.getPlural(wfmStrings.youCanAddRecurringPayDeductions(), wfmStrings.recurringPayDeduction()));
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private String getLink(RecurringPayDeductItem item) {
        boolean custom = item.getType() == 4;
        return "recurring" + ((PayType.PAYMENT.equals(item.getPayType())) ? "Payment" : custom ? "CustomDeduction" : "Deduction");
    }

    private void addNew() {
        SinksContainerFactory.entryPoint.onHistoryChanged("recurringDeduction|add/add");
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.PAYROLL_RECURRING_PD_ADD);
    }

    public String getPropertyCode() {
        return RECURRING_PAY_DEDUCTION_LIST;
    }

    @Override
    public String getIconStyle() {
        return "payroll payments-list";
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
