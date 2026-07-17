package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.AccountManagementListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Dec 4, 2009
 * Time: 8:02:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountManagementListView extends BaseListView {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private ListingPanel<AccountManagementListItem> list;
    private ListingFilterParameter filterParametrs;
    private boolean isFromPartnerBackend = false;

    public AccountManagementListView() {
        super("summaryView", backendStrings.accountManager());
    }

    public AccountManagementListView(boolean isFromPartnerBackend) {
        super("summaryView", backendStrings.accountManager());
        this.isFromPartnerBackend = isFromPartnerBackend;
    }

    @Override
    public String getIconStyle() {
        return "backend accManListView";
    }

    public void refresh() {
        list.reloadPage();
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.AccountManagementListPanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());
        add(list);
        return null;
    }

    private ColumnDefinitionConfig[] drawColumns() {
        final ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();
        //Company ID
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<AccountManagementListItem, String>(backendStrings.companyID(), "companyID", 65) {
            @Override
            public String getCellValue(AccountManagementListItem item) {
                return item.getCompanyID() != null ? "" + item.getCompanyID() : "";
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);
        //Company Name
        column = new ColumnDefinitionConfig<AccountManagementListItem, String>(wfmStrings.companyName(), "companyName", 180) {
            @Override
            public String getCellValue(AccountManagementListItem item) {
                return item.getCompanyName();
            }
        };
        columns.add(column);
        //Account Name
        column = new ColumnDefinitionConfig<AccountManagementListItem, SimpleLink>(wfmStrings.accountName(), "account", 150) {
            @Override
            public SimpleLink getCellValue(AccountManagementListItem item) {
                SimpleLink link = new SimpleLink(!Utils.isNullOrEmpty(item.getName()) ? item.getName() : "");
                link.addClickHandler(clickEvent -> new AccountManagementView(item, () -> refresh()));
                return link;
            }
        };
        columns.add(column);
        //Registration Date
        column = new ColumnDefinitionConfig<AccountManagementListItem, SimpleLink>(wfmStrings.registeredDate(), "date", 100) {
            @Override
            public SimpleLink getCellValue(AccountManagementListItem item) {
                SimpleLink link = new SimpleLink(!Utils.isNullOrEmpty(item.getSignUpDate()) ? item.getSignUpDate() : "");
                link.addClickHandler(clickEvent -> new AccountManagementView(item, () -> refresh()));
                return link;
            }
        };
        columns.add(column);
        //Company Phone
        column = new ColumnDefinitionConfig<AccountManagementListItem, SimpleLink>(wfmStrings.phone(), "phone", 110) {
            @Override
            public SimpleLink getCellValue(AccountManagementListItem item) {
                SimpleLink link = new SimpleLink(!Utils.isNullOrEmpty(item.getPhone()) ? item.getPhone() : "");
                link.addClickHandler(clickEvent -> new AccountManagementView(item, () -> refresh()));
                return link;
            }
        };
        columns.add(column);
        //User Status
        column = new ColumnDefinitionConfig<AccountManagementListItem, SimpleLink>(wfmStrings.status(), "status", 70) {
            @Override
            public SimpleLink getCellValue(AccountManagementListItem item) {
                String status = "Active";
                if (Constants.EMPLOYEE_STATUS_PENDING.equals(item.getEmployeeStatus())) {
                    status = "Pending";
                } else if (Constants.EMPLOYEE_STATUS_INACTIVE.equals(item.getEmployeeStatus())) {
                    status = "Inactive";
                }
                SimpleLink link = new SimpleLink(status);
                link.addClickHandler(clickEvent -> new AccountManagementView(item, () -> refresh()));
                return link;
            }
        };
        columns.add(column);
        //User Role
        column = new ColumnDefinitionConfig<AccountManagementListItem, SimpleLink>(wfmStrings.role(), "role", 150) {
            @Override
            public SimpleLink getCellValue(AccountManagementListItem item) {
                SimpleLink link = new SimpleLink(!Utils.isNullOrEmpty(item.getRoles()) ? item.getRoles() : "");
                link.addClickHandler(clickEvent -> new AccountManagementView(item, () -> refresh()));
                return link;
            }
        };
        columns.add(column);

        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    public ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public Widget getFirstAdditionalPanel() {
                final SchemaLookUp schemaLookUp = new SchemaLookUp(isFromPartnerBackend);
                schemaLookUp.setSelected(new SelectItem(Integer.valueOf(Utils.getEncryptedCompanyID()), Utils.getEncryptedCompanyID() + " ( " + Utils.getCompanyName() + " )"));
                schemaLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                    if (schemaLookUp.getSelectedItemID() != null && schemaLookUp.getSelectedItemID() > 0) {
                        filterParametrs = list.getFilterParametrs();
                        if (filterParametrs == null) {
                            filterParametrs = new ListingFilterParameter();
                        }
                        filterParametrs.setCompanyID(schemaLookUp.getSelectedItemID());
                        refresh();
                    }
                });
                return schemaLookUp;
            }

            @Override
            public Widget getAddAdditionalPanel() {
                SelectItem[] statuses = new SelectItem[]{new SelectItem(1, wfmStrings.active(), Constants.EMPLOYEE_STATUS_ACTIVE),
                        new SelectItem(2, wfmStrings.pending(), Constants.EMPLOYEE_STATUS_PENDING),
                        new SelectItem(3, wfmStrings.inactive(), Constants.EMPLOYEE_STATUS_INACTIVE)};
                DataListBox statusBox = new DataListBox();
                statusBox.setWithoutNullLabel(true);
                statusBox.setItems(statuses);
                statusBox.addValueChangeHandler(changeEvent -> changeStatus(changeEvent.getValue().getDescription()));
                return statusBox;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage emptyMessage = new DefaultNoItemsMessage(backendStrings.currentlyThereAreNoAnyItems());
                emptyDataTable.initEmptyDataTable(emptyMessage);
            }
        };
    }

    private void changeStatus(String employeeStatusActive) {
        filterParametrs = list.getFilterParametrs();
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        filterParametrs.setStatusCode(employeeStatusActive);
        refresh();
    }

    private ListingRequestProvider<AccountManagementListItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            BackendService.App.get().getValidUsers(filterParametrs, new AsyncCallback<ListResult<AccountManagementListItem>>() {
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void onSuccess(ListResult<AccountManagementListItem> list) {
                    if (list != null) {
                        callback.onSuccess(list);
                    }
                }
            });
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