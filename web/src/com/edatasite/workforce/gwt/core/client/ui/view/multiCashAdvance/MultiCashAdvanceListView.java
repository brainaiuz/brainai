package com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Rajabov Iftixor
 * Date: 17.02.2022
 * Time: 22:50
 */
public class MultiCashAdvanceListView extends BaseListView implements Constants {

    private ListingPanel<MultiCashAdvanceItem> list;

    public MultiCashAdvanceListView() {
        super(MULTI_CASH_ADVANCE_LIST);
        setDescription(property.getSingular(wfmStrings.multiCashAdvance()));
        if (hasPermissionToAdd()) {

            setAddNew("multiCashAdvance|add/add/employee");
        }
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.PAYROLL_MULTI_CASH_ADVANCE_ADD);
    }

    @Override
    protected Widget onInitialize() {

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASH_SAVED, MultiCashAdvanceListView.this, (sender, args) -> {
            list.reloadPage();
        });
        list = new GuideListingPanel(ListPanelType.MultiCashAdvancePanel, getColumn(), getListingRequestProvider(), getListDesign());

//        list.setExcelListener(clickEvent -> {
//            String excelURL = CommandConstants.COMMON_URL + "/cashAdvanceListExcelHandler";
//            ListingFilterParameter filterParameter = list.getFilterParametrs();
//            list.callListExcel(excelURL, filterParameter);
//        });
//        list.setPDFListener(clickEvent -> {
//            String pdfURL = CommandConstants.PDF_URL + "/cashAdvanceListPDFHandler";
//            ListingFilterParameter filterParameter = list.getFilterParametrs();
//            filterParameter.setPropertyCode(getPropertyCode());
//            list.callListPDF(pdfURL, filterParameter);
//        });

        add(list);
        return null;
    }


    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? MultiCashAdvanceListView.this::addNewMultiCashAdvance : null;
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
//                    ActionButton addNew = getAddNewButton();
//                    addNew.addClickHandler(clickEvent -> addNewMultiCashAdvance());
//
                    MenuBar menu = new MenuBar(true);
                    ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);

                    MenuPopItem addNewEmployee = new MenuPopItem(wfmStrings.employee());
                    addNewEmployee.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("multiCashAdvance|add/add/employee"));
                    menu.addItem(addNewEmployee);

                    MenuPopItem location = new MenuPopItem(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()));
                    location.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("multiCashAdvance|add/add/location"));
                    menu.addItem(location);

                    MenuPopItem group = new MenuPopItem(wfmStrings.group());
                    group.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("multiCashAdvance|add/add/group"));
                    menu.addItem(group);

                    MenuPopItem department = new MenuPopItem(wfmStrings.department());
                    department.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("multiCashAdvance|add/add/department"));
                    menu.addItem(department);

                    newItem.setMenu(menu);

                    return newItem;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(wfmStrings.currentlyThereAreNoCashAdvances(), wfmStrings.multiCashAdvance()));
                message.setHref(hasPermissionToAdd() ? "multiCashAdvance|add/add/employee" : "");
                message.setTextBeforeLink(property.getPlural(wfmStrings.youCanAddCashAdvance(), wfmStrings.multiCashAdvance()));
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void addNewMultiCashAdvance() {
        SinksContainerFactory.entryPoint.onHistoryChanged("multiCashAdvance|add/add/employee");
    }


    private ListingRequestProvider<MultiCashAdvanceItem> getListingRequestProvider() {
        return (filterParametrs, listingCallback) -> {

            AllInOneService.App.get().getMultiCashAdvanceList(filterParametrs, new AsyncCallback<ListResult<MultiCashAdvanceItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    if (listingCallback != null) {
                        listingCallback.onFailure(throwable);
                    }
                }

                @Override
                public void onSuccess(ListResult<MultiCashAdvanceItem> listResult) {
                    if (listingCallback != null) {
                        listingCallback.onSuccess(listResult);
                    }
                }
            });
        };
    }

    private ColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[7];
        columnConfig[index] = new ColumnDefinitionConfig<MultiCashAdvanceItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final MultiCashAdvanceItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (!DRAFT.equals(item.getStatus().getDescription()) && Utils.hasPermission(PermissionConstants.PAYROLL_MULTI_CASH_ADVANCE_VIEW)) {
                    MenuPopItem view = new MenuPopItem(wfmStrings.summaryView(), "icon-employee-edit-profile");
                    String status = item.getStatus() != null ? item.getStatus().getDescription() : null;
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("multiCashAdvance|summary/" + item.getObjectID() + "/" + status, item.getNumber(), item.getNumber()));
                    actionItemCount++;
                    menuBar.addItem(view);
                }

                if (Utils.hasPermission(PermissionConstants.PAYROLL_MULTI_CASH_ADVANCE_DELETE)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    delete.setCommand(() -> {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage("Cash Advances that are already used in Payslips will not be deleted.");
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                AllInOneService.App.get().deleteMultiCashAdvance(item.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(Boolean result) {
                                        if (result) {
                                            Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.multiCashAdvance()));
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASH_SAVED, null, MultiCashAdvanceListView.this);
                                        } else {
                                            Info.warn(property.getSingular(wfmStrings.cashAdvanceCannotDelete(), wfmStrings.multiCashAdvance()));
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
        columnConfig[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[index++].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[index] = new ColumnDefinitionConfig<MultiCashAdvanceItem, SimpleLink>(wfmStrings.number(), MultiCashAdvanceItem.NUMBER, 140) {
            @Override
            public SimpleLink getCellValue(MultiCashAdvanceItem item) {
                String number = item.getNumber() != null ? item.getNumber() : "";
                if (Utils.hasPermission(PermissionConstants.PAYROLL_MULTI_CASH_ADVANCE_VIEW)) {
                    String action = null;
                    if (DRAFT.equals(item.getStatus().getDescription()) && Utils.hasPermission(PermissionConstants.PAYROLL_MULTI_CASH_ADVANCE_EDIT)) {
                        action = "multiCashAdvance|add/add/" + item.getType() + "/" + item.getObjectID();
                    } else {
                        String status = item.getStatus() != null ? item.getStatus().getDescription() : null;
                        action = "multiCashAdvance|summary/" + item.getObjectID() + "/" + status;
                    }
                    return getLink(number, action, number, item.getNumber());
                } else {
                    return getLink(number, null);
                }
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<MultiCashAdvanceItem, String>(wfmStrings.date(), MultiCashAdvanceItem.DATE, 140) {
            @Override
            public String getCellValue(MultiCashAdvanceItem item) {
                return DateUtils.format(item.getDate());
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<MultiCashAdvanceItem, String>(wfmStrings.approver(), MultiCashAdvanceItem.APPROVER, 140) {
            @Override
            public String getCellValue(MultiCashAdvanceItem item) {
                return item.getApprover() != null ? item.getApprover().getName() : wfmStrings.notAvailable();
            }
        };
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<MultiCashAdvanceItem, String>(wfmStrings.amount(), MultiCashAdvanceItem.AMOUNT, 140) {
            @Override
            public String getCellValue(MultiCashAdvanceItem item) {
                return item.getTotalAmount() != null ? Utils.getCalculationNumberFormat().format(item.getTotalAmount()) : wfmStrings.notAvailable();
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<MultiCashAdvanceItem, String>(wfmStrings.dueAmount(), MultiCashAdvanceItem.REMAINING_AMOUNT, 140) {
            @Override
            public String getCellValue(MultiCashAdvanceItem item) {
                return item.getRemainingAmount() != null ? Utils.getNumberFormat().format(item.getRemainingAmount()) : wfmStrings.notAvailable();
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<MultiCashAdvanceItem, String>(wfmStrings.status(), MultiCashAdvanceItem.STATUS, 140) {
            @Override
            public String getCellValue(MultiCashAdvanceItem item) {
                return item.getStatus().getName() != null ? item.getStatus().getName() : "";
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[index++].setMinimumColumnWidth(100);

        return columnConfig;
    }


    @Override
    public String getIconStyle() {
        return "payroll payments-list";
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

    public String getPropertyCode() {
        return MULTI_CASH_ADVANCE_LIST;
    }
}
