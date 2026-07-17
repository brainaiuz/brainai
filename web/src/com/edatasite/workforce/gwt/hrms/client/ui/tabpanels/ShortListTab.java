package com.edatasite.workforce.gwt.hrms.client.ui.tabpanels;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.Collections;
import java.util.List;

/**
 * User: Ilxom Lutfullaev
 * Date: 6/21/12
 * Time: 12:10 PM
 */

public class ShortListTab extends CustomTabWidget {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiDataGrid<ContactListItem> dataGrid;
    private ListDataProvider<ContactListItem> dataProvider;
    private boolean isShortList;

    public static final ProvidesKey<ContactListItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectId();

    public ShortListTab(String tabName, boolean isShortList) {
        super(tabName);
        this.isShortList = isShortList;
    }

    public void addDataDisplay(HasData<ContactListItem> display) {
        dataProvider.addDataDisplay(display);
    }

    @Override
    public void initData() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        dataGrid.addStyleName("cellBasedWidget-mod");
        addDataDisplay(dataGrid);
        add(dataGrid);
        initTableColumns();
    }

    public void viewShow() {
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmMessages.thereAreNoSomethingItemsYet(isShortList ? hrmsStrings.shortlistOnly().toLowerCase() :
                wfmStrings.candidates().toLowerCase()), "", null));
        dataGrid.getEmptyTableWidget().getElement().getStyle().setMarginTop(-50, com.google.gwt.dom.client.Style.Unit.PX);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CANDIDATE_ADD_EDIT, ShortListTab.this, (sender, args) -> fillShortlist());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CANDIDATE_DELETE, ShortListTab.this, (sender, args) -> fillShortlist());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CANDIDATE_SELECT, ShortListTab.this, (sender, args) -> fillShortlist());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VACANCY_MATCHED, ShortListTab.this, (sender, args) -> fillShortlist());
        fillShortlist();
    }

    private void fillShortlist() {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setBriefly(false);
        if (isShortList) {
            filterParametrs.setShortList(true);
        }
        RecruitmentService.App.get().listCandidates(filterParametrs, new AbstractAsyncCallback<ListResult<ContactListItem>>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(ListResult<ContactListItem> result) {
                if (result != null && result.getList() != null && !result.getList().isEmpty()) {
                    supplyProvider(result.getList().toArray(new ContactListItem[]{}));
                    dataProvider.refresh();
                }
            }
        });
    }

    private void initTableColumns() {
        // Candidate Name
        Column<ContactListItem, String> candidateName = new Column<ContactListItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(final ContactListItem object) {
                return object.getName() != null ? object.getName() : "";
            }
        };
        candidateName.setFieldUpdater((index, object, value) -> SinksContainerFactory.entryPoint.onHistoryChanged("candidate|summary/" + object.getObjectId()));
        dataGrid.addColumn(candidateName, hrmsStrings.candidateName());
        dataGrid.setColumnWidth(candidateName, 25, com.google.gwt.dom.client.Style.Unit.PCT);
        // Email
        Column<ContactListItem, String> email = new Column<ContactListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ContactListItem object) {
                return object.getPrimaryEmail() != null ? object.getPrimaryEmail() : "";
            }
        };
        dataGrid.addColumn(email, wfmStrings.email());
        dataGrid.setColumnWidth(email, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        // Status
        Column<ContactListItem, String> status = new Column<ContactListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ContactListItem object) {
                return object.getCandidateStatus() != null ? object.getCandidateStatus().getName() : "";
            }
        };
        dataGrid.addColumn(status, wfmStrings.status());
        dataGrid.setColumnWidth(status, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        // Owner
        Column<ContactListItem, String> owner = new Column<ContactListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ContactListItem object) {
                return object.getOwner() != null ? object.getOwner() : "";
            }
        };
        dataGrid.addColumn(owner, wfmStrings.owner());
        dataGrid.setColumnWidth(owner, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        // Matched Vacancies
        Column<ContactListItem, String> vacancy = new Column<ContactListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ContactListItem object) {
                return object.getVacancies() != null && object.getVacancies().size() > 0 ?
                        CRMUtils.getSelectItemsAsCommaDelimeted(object.getVacancies().toArray(new SelectItem[]{}), true) : "";
            }
        };
        dataGrid.addColumn(vacancy, wfmStrings.vacancy());
        dataGrid.setColumnWidth(vacancy, 15, com.google.gwt.dom.client.Style.Unit.PCT);
    }

    private void supplyProvider(ContactListItem[] reportResults) {
        List<ContactListItem> tableses = dataProvider.getList();
        tableses.clear();
        dataGrid.setPageSize(200);
        Collections.addAll(tableses, reportResults);
    }
}