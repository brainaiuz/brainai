package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollPDFTemplateSelector;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollMessages;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Span;

import java.util.HashMap;

/**
 * Created by Shohruh on 05 Dec 2016.
 */
public class AdditionalPaymentItemListView extends BaseListView implements Constants {

    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private static final PayrollMessages payrollMessages = GWT.create(PayrollMessages.class);
    private ListingPanel<AdditionalPayment> listingPanel;

    private Integer employeeId;

    public AdditionalPaymentItemListView(Integer employeeId) {
        super(ADDITIONAL_PAYMENT_ITEM_LIST);
        setDescription(property.getPlural(payrollStrings.singlePayments()));
        this.employeeId = employeeId;
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.AdditionalPayment, getColumn(), getListingRequestProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADDITIONAL_PAYMENT_ADD, AdditionalPaymentItemListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADDITIONAL_PAYMENT_DELETE, AdditionalPaymentItemListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYROLL_PAYMENT_ADD, AdditionalPaymentItemListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYROLL_PAYMENT_DELETE, AdditionalPaymentItemListView.this, (sender, args) -> listingPanel.reloadPage());

        add(listingPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[7];
        columnConfig[index] = new ColumnDefinitionConfig<AdditionalPayment, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final AdditionalPayment item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_ITEM_VIEW)) {
                    MenuPopItem view = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalPayment|view/" + item.getObjectID() + "/" + item.getStatusCode() + "/" + item.getEmployee().getId(), item.getEmployeeCode(), item.getEmployee().getName()));
                    actionItemCount++;
                    menuBar.addItem(view);
                }

                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_ITEM_PDF)) {
                    final HTMLPanel panel = new HTMLPanel("");
                    MenuPopItem pdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                    pdf.setCommand(() -> new PayrollPDFTemplateSelector(ADDITIONAL_PAYMENT_TEMPLATE, new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(panel, item);
                        }
                    }));
                    add(panel);
                    actionItemCount++;
                    menuBar.addItem(pdf);
                }

                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_ITEM_DELETE)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage(payrollMessages.areYouSureWantToDelete(item.getReference()));
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                PayrollService.App.get().deleteAdditionalPaymentItem(item.getObjectID(), item.getEmployee().getId(), new AbstractAsyncCallback<Integer>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(Integer result) {
                                        if (result == -1) {
                                            if (PayrollConstants.CATEGORY_DEDUCTION.equals(item.getCategoryType())) {
                                                Info.show(payrollStrings.additionalDeductionsCannotBeDeleted(), Info.Type.WARNING);
                                            } else {
                                                Info.show(Property.getPluralWithObjectCodeWithReplace(ADDITIONAL_PAYMENT_LIST, payrollStrings.additionalPaymentsCannotBeDeleted(), payrollStrings.additionals()), Info.Type.WARNING);
                                            }
                                            return;
                                        }
                                        if (PayrollConstants.CATEGORY_DEDUCTION.equals(item.getCategoryType())) {
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), payrollStrings.additionalDeduction()), Info.Type.INFO);
                                        } else {
                                            Info.show(Property.get(ADDITIONAL_PAYMENT_LIST, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.additionalPayment()), Info.Type.INFO);
                                        }
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADDITIONAL_PAYMENT_DELETE, null, AdditionalPaymentItemListView.this);
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

        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, SimpleLink>(wfmStrings.reference(), "reference", 120) {
            @Override
            public SimpleLink getCellValue(AdditionalPayment item) {
                if (item.getEmployee() != null) {
                    String action = "additionalPayment|view/" + item.getObjectID() + "/" + item.getStatus() + "/" + item.getEmployee().getId();
                    return getLink(item.getReference(), action);
                }
                return null;
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMinimumColumnWidth(100);

        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.employeeCode(), "employeeCode", 100) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return item.getEmployeeCode() != null ? item.getEmployeeCode() : "N/A";
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.employee(), "employeeName", 120) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return item.getEmployee() != null ? item.getEmployee().getName() : "N/A";
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.period(), "period", 100) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return item.getPeriod();
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.approver(), "approver", 120) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return item.getApprover() != null ? item.getApprover().getName() : "N/A";
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.total(), "total", 100) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return PayrollClientUtils.format(item.getTotal());
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);

        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.status(), "status", 120) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return item.getStatus() != null ? item.getStatus() : "N/A";
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        return columnConfig;
    }

    private ListingRequestProvider<AdditionalPayment> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            PayrollService.App.get().getAdditionalPaymentItemList(filterParametrs, new AbstractAsyncCallback<ListResult<AdditionalPayment>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<AdditionalPayment> result) {
                    callback.onSuccess(result);
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
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(Property.getPluralWithObjectCodeWithReplace(ADDITIONAL_PAYMENT_LIST, payrollStrings.currentlyThereAreNotAdditionalPayments(), payrollStrings.additionals()));
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void generatePDF(HTMLPanel panel, AdditionalPayment item) {
        RequestObject requestObject = new RequestObject(item.getObjectID(), item.getEmployee().getId());
        String pdfURL = CommandConstants.PDF_URL + "/additionalPaymentItemPdfHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parametrs, "_blank");
    }

    @Override
    public String getIconStyle() {
        return "payroll aeo-list";
    }

    @Override
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

    @Override
    public String getPropertyCode() {
        return ADDITIONAL_PAYMENT_ITEM_LIST;
    }


    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadDependents(new ListingFilterParameter(), null, container);
    }

    private void loadDependents(ListingFilterParameter fp, ListingCallback callback, Span container) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        PayrollService.App.get().getAdditionalPaymentItemList(fp, new AbstractAsyncCallback<ListResult<AdditionalPayment>>() {
            @Override
            public void failure(Throwable caught) {
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }

            @Override
            public void success(ListResult<AdditionalPayment> result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result.getTotal() != null && result.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(result.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }


}
