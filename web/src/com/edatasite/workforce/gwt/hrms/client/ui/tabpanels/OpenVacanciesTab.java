package com.edatasite.workforce.gwt.hrms.client.ui.tabpanels;

import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.Collections;
import java.util.List;

/**
 * User: Ilxom Lutfullaev
 * Date: 6/21/12
 * Time: 12:13 PM
 */

public class OpenVacanciesTab extends CustomTabWidget {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiDataGrid<VacancyItem> dataGrid;
    private ListDataProvider<VacancyItem> dataProvider;

    public static final ProvidesKey<VacancyItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectID();

    public OpenVacanciesTab(String tabName) {
        super(tabName);
    }

    public void addDataDisplay(HasData<VacancyItem> display) {
        dataProvider.addDataDisplay(display);
    }

    @Override
    public void initData() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        dataGrid.addStyleName("cellBasedWidget-mod");
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmMessages.thereAreNoSomethingItemsYet(hrmsStrings.openVacancies().toLowerCase()), "", null));
        dataGrid.getEmptyTableWidget().getElement().getStyle().setMarginTop(-50, com.google.gwt.dom.client.Style.Unit.PX);
        addDataDisplay(dataGrid);
        add(dataGrid);
        initTableColumns();
    }

    public void viewShow() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VACANCY_ADDED, OpenVacanciesTab.this, (sender, args) -> fillVacancies());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VACANCY_DELETE, OpenVacanciesTab.this, (sender, args) -> fillVacancies());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VACANCY_MATCHED, OpenVacanciesTab.this, (sender, args) -> fillVacancies());
        fillVacancies();
    }

    private void fillVacancies() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setBriefly(false);
        filterParameter.setShowActive(true);
        RecruitmentService.App.get().getVacancyList(filterParameter, new AbstractAsyncCallback<ListResult<VacancyItem>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ListResult<VacancyItem> result) {
                if (result != null && result.getList() != null && !result.getList().isEmpty()) {
                    supplyProvider(result.getList().toArray(new VacancyItem[]{}));
                    dataProvider.refresh();
                }
            }
        });
    }

    private void initTableColumns() {
        // Job title
        Column<VacancyItem, String> jobTitle = new Column<VacancyItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(final VacancyItem object) {
                return object.getJobTitle();
            }
        };
        jobTitle.setFieldUpdater((index, object, value) -> SinksContainerFactory.entryPoint.onHistoryChanged("vacancy|summary/" + object.getObjectID()));
        dataGrid.addColumn(jobTitle, wfmStrings.name());
        dataGrid.setColumnWidth(jobTitle, 15, Style.Unit.PCT);
        // Position
        Column<VacancyItem, String> position = new Column<VacancyItem, String>(new TextCell()) {
            @Override
            public String getValue(final VacancyItem object) {
                return object.getPositionItem() != null ? object.getPositionItem().getName() : "";
            }
        };
        dataGrid.addColumn(position, wfmStrings.position());
        dataGrid.setColumnWidth(position, 20, Style.Unit.PCT);
        //Manager
        Column<VacancyItem, String> manager = new Column<VacancyItem, String>(new TextCell()) {
            @Override
            public String getValue(VacancyItem object) {
                return object.getManager() != null ? object.getManager().getName() : "";
            }
        };
        dataGrid.addColumn(manager, wfmStrings.manager());
        dataGrid.setColumnWidth(manager, 20, Style.Unit.PCT);
        //BackupManager --- Remowed from Open Vacancies
       /* Column<VacancyItem, String> backupManager = new Column<VacancyItem, String>(new TextCell()) {
            @Override
            public String getValue(VacancyItem object) {
                return object.getBackupManager() != null ? object.getBackupManager().getName() : "";
            }
        };
        dataGrid.addColumn(backupManager, wfmStrings.backupManager());
        dataGrid.setColumnWidth(backupManager, 20, Style.Unit.PCT);*/

    }

    private void supplyProvider(VacancyItem[] reportResults) {
        List<VacancyItem> tables = dataProvider.getList();
        tables.clear();
        dataGrid.setPageSize(200);
        Collections.addAll(tables, reportResults);
    }
}
