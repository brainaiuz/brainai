package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.NumberUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
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
import com.edatasite.workforce.gwt.payroll.client.rpc.EoSCalculationData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.END_OF_SERVICE_GRATUITY;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12.05.14
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
public class EndOfServiceGratuityListView extends BaseListView {

    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private ListingPanel<EoSCalculationData> list;

    public EndOfServiceGratuityListView() {
        super("endOfServiceList");
        setDescription(property.getPlural(wfmStrings.endOfServiceGratuity()));
        if (hasPermissionToAdd()) {
            setAddNew("endOfService|add/add");
        }
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.END_OF_SERVICE_GRATUITY_ADD);
    }

    @Override
    protected Widget onInitialize() {

        list = new GuideListingPanel(ListPanelType.EndOfServiceGratuityListPanel, getColumn(), getListingRequestProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EOS_CALCULATION_SAVE, EndOfServiceGratuityListView.this, (sender, args) -> list.reloadPage());

        add(list);

        return null;
    }

    private ColumnDefinitionConfig[] getColumn() {
        int index = -1;
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[6];
        columns[++index] = new ColumnDefinitionConfig<EoSCalculationData, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final EoSCalculationData item) {
                MenuBar menuBar = new MenuBar(true);

                if (Utils.hasPermission(PermissionConstants.END_OF_SERVICE_GRATUITY_EDIT)) {
                    MenuPopItem view = new MenuPopItem(wfmStrings.summaryView(), "icon-employee-edit-profile");
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("endOfService|summary/view/" + item.getObjectID(), item.getEmployeeCode(), item.getEmployee().getName()));
                    menuBar.addItem(view);
                }
                if (Utils.hasPermission(PermissionConstants.END_OF_SERVICE_GRATUITY_DELETE)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    delete.setCommand(() -> PayrollService.App.get().deleteEndOfServiceGratuity(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable throwable) {

                        }

                        @Override
                        public void success(Void result) {
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.endOfServiceGratuity()), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EOS_CALCULATION_SAVE, null, EndOfServiceGratuityListView.this);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYSLIP_SAVED, null, EndOfServiceGratuityListView.this);
                        }
                    }));
                    menuBar.addItem(delete);
                }
                ToolItem toolItem = new ToolItem(2);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[index].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);

        columns[++index] = new ColumnDefinitionConfig<EoSCalculationData, SimpleLink>(wfmStrings.employeeCode(), "employeeCode", 100) {

            @Override
            public SimpleLink getCellValue(EoSCalculationData item) {
                return getLink(item.getEmployeeCode(), "endOfService|summary/view/" + item.getObjectID(), item.getEmployeeCode(), item.getEmployee().getName());
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(100);

        columns[++index] = new ColumnDefinitionConfig<EoSCalculationData, String>(wfmStrings.employee(), "employeeName", 140) {

            @Override
            public String getCellValue(EoSCalculationData item) {
                return item.getEmployee() != null ? item.getEmployee().getName() : "";
            }
        };
        columns[index].setMinimumColumnWidth(100);

        columns[++index] = new ColumnDefinitionConfig<EoSCalculationData, String>(wfmStrings.date(), "date", 100) {
            @Override
            public String getCellValue(EoSCalculationData item) {
                return DateUtils.format(item.getDate());
            }
        };
        columns[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[++index] = new ColumnDefinitionConfig<EoSCalculationData, String>(wfmStrings.reason(), "reason", 100) {

            @Override
            public String getCellValue(EoSCalculationData item) {
                return Constants.EMPLOYEE_RESIGNATION.equals(item.getReasonCode()) ? wfmStrings.employeeResignation() : wfmStrings.contractTermination();
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[++index] = new ColumnDefinitionConfig<EoSCalculationData, String>(wfmStrings.total(), "total", 100) {

            @Override
            public String getCellValue(EoSCalculationData item) {
                return item.getEosAmount() != null ? NumberUtils.getCurrencyFormat(item.getEosAmount().doubleValue(), item.getCurrency() != null ? item.getCurrency().getSymbol() : null) : "";
            }
        };
        columns[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        return columns;
    }


    private ListingRequestProvider<EoSCalculationData> getListingRequestProvider() {
        return (filterParametrs, listingCallback) -> PayrollService.App.get().getEndOfServiceGratuityList(filterParametrs, new AsyncCallback<ListResult<EoSCalculationData>>() {
            @Override
            public void onFailure(Throwable caught) {
                listingCallback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<EoSCalculationData> result) {
                listingCallback.onSuccess(result);
            }
        });
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? EndOfServiceGratuityListView.this::addNewGratuity : null;
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
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> {
                        addNewGratuity();
                    });
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(payrollStrings.currentlyThereAreNoCalc());
                message.setHref("endOfService|add/add");
                message.setTextBeforeLink(payrollStrings.messAddingEOSPayments());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    @Override
    public String getPropertyCode() {
        return END_OF_SERVICE_GRATUITY;
    }

    private void addNewGratuity() {
        SinksContainerFactory.entryPoint.onHistoryChanged("endOfService|add/add");
    }

    @Override
    public String getIconStyle() {
        return "payroll efile-to-hmrc";
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
}
