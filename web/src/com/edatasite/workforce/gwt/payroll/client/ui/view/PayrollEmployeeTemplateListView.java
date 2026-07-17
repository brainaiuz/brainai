package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
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
 * Date: 11/27/15
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollEmployeeTemplateListView extends BaseListView implements Constants, PermissionConstants {


    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private ListingPanel<NewEmployee> list;

    public PayrollEmployeeTemplateListView() {
        super(EMPLOYEE_TEMPLATE_LIST);
        setDescription(property.getPlural(payrollStrings.pendingChanges()));
    }


    @Override
    protected Widget onInitialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYROLL_EMPLOYEE_TEMPLATE_SAVED, PayrollEmployeeTemplateListView.this, (sender, args) -> list.reloadPage());

        list = new ListingPanel<>(ListPanelType.PayrollEmployeeTemplateListPanel, getColumn(), getListingRequestProvider(), getListDesign());
        add(list);

        return null;
    }


    private ColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[6];
        columnConfig[index] = new ColumnDefinitionConfig<NewEmployee, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final NewEmployee item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem employeeSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-employee-small");
                employeeSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("starter|summary/" + item.getEmployeeTemplateID() + "/fromTemplate/view/" + item.getStatus(), item.getFname()));
                actionItemCount++;
                menuBar.addItem(employeeSummary);

                if (!Utils.hasPermission(PAYROLL_EMPLOYEE_APPROVAL)) {
                    final MenuPopItem employeeEdit = new MenuPopItem(wfmStrings.edit(), "icon-client-edit-small");
                    employeeEdit.ensureDebugId("editTemplate");
                    employeeEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("starter|edit/" + item.getEmployeeTemplateID() + "/fromTemplate", item.getFname()));
                    actionItemCount++;
                    menuBar.addItem(employeeEdit);
                }

                if (Utils.hasPermission(PAYROLL_EMPLOYEE_APPROVAL)) {
                    final MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-client-delete-small");
                    delete.ensureDebugId("deleteTemplate");
                    delete.setCommand(() -> PayrollService.App.get().deleteEmployeeTemplate(item.getEmployeeTemplateID(), new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable throwable) {
                            onFailure(throwable);
                        }

                        @Override
                        public void success(Void result) {
                            list.reloadPage();
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(delete);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[index].setColumnSortable(false);

        //first name
        columnConfig[++index] = new ColumnDefinitionConfig<NewEmployee, SimpleLink>(wfmStrings.firstName(), "firstName", 80) {
            @Override
            public SimpleLink getCellValue(NewEmployee item) {
                return getLink(item.getFname(), "starter|summary/" + item.getEmployeeTemplateID() + "/fromTemplate/view/" + item.getStatus(), item.getFname());
            }
        };
        columnConfig[index].setMinimumColumnWidth(70);

        //last name
        columnConfig[++index] = new ColumnDefinitionConfig<NewEmployee, SimpleLink>(wfmStrings.lastName(), "lastName", 80) {
            @Override
            public SimpleLink getCellValue(NewEmployee item) {
                return getLink(item.getLname(), "starter|summary/" + item.getEmployeeTemplateID() + "/fromTemplate/view/" + item.getStatus(), item.getFname());
            }
        };
        columnConfig[index].setMinimumColumnWidth(70);

        //sender name
        columnConfig[++index] = new ColumnDefinitionConfig<NewEmployee, String>(wfmStrings.sender(), "sender", 120) {
            @Override
            public String getCellValue(NewEmployee item) {
                return item.getSender() != null ? item.getSender().getName() : "";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);

        //status
        columnConfig[++index] = new ColumnDefinitionConfig<NewEmployee, String>(wfmStrings.status(), "status", 80) {
            @Override
            public String getCellValue(NewEmployee item) {
                return item.getStatus() != null ? item.getStatus() : "";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);

        //note
        columnConfig[++index] = new ColumnDefinitionConfig<NewEmployee, String>(wfmStrings.rejectionReason(), "rejectionNote", 120) {
            @Override
            public String getCellValue(NewEmployee item) {
                return item.getRejectionNote() != null ? item.getRejectionNote() : "";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setShow(false);


        return columnConfig;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(payrollStrings.currentlyThereAreNoTemplates());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }


    private ListingRequestProvider<NewEmployee> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            PayrollService.App.get().getEmployeeTemplateList(filterParametrs, new AbstractAsyncCallback<ListResult<NewEmployee>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<NewEmployee> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    @Override
    public String getPropertyCode() {
        return EMPLOYEE_TEMPLATE_LIST;
    }

    @Override
    public String getIconStyle() {
        return "employee employee-list";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
