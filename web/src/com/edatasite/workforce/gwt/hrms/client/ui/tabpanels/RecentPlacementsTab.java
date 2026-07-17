package com.edatasite.workforce.gwt.hrms.client.ui.tabpanels;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
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
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
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
 * Time: 12:22 PM
 */

public class RecentPlacementsTab extends CustomTabWidget {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiDataGrid<PlacementItem> dataGrid;
    private ListDataProvider<PlacementItem> dataProvider;

    public static final ProvidesKey<PlacementItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectID();

    public RecentPlacementsTab(String tabName) {
        super(tabName);
    }

    public void addDataDisplay(HasData<PlacementItem> display) {
        dataProvider.addDataDisplay(display);
    }

    @Override
    public void initData() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        dataGrid.addStyleName("cellBasedWidget-mod");
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmMessages.thereAreNoSomethingItemsYet(hrmsStrings.placementsOnly().toLowerCase()), "", null));
        dataGrid.getEmptyTableWidget().getElement().getStyle().setMarginTop(-50, com.google.gwt.dom.client.Style.Unit.PX);
        addDataDisplay(dataGrid);
        add(dataGrid);
        initTableColumns();
    }

    public void viewShow() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PLACEMENT_ADD_EDIT, RecentPlacementsTab.this, (sender, args) -> fillPlacements());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PLACEMENT_DELETE, RecentPlacementsTab.this, (sender, args) -> fillPlacements());
        fillPlacements();
    }

    private void fillPlacements() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        RecruitmentService.App.get().getPlacementList(filterParameter, new AbstractAsyncCallback<ListResult<PlacementItem>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ListResult<PlacementItem> result) {
                if (result != null && result.getList() != null && !result.getList().isEmpty()) {
                    supplyProvider(result.getList().toArray(new PlacementItem[]{}));
                    dataProvider.refresh();
                }
            }
        });
    }

    private void initTableColumns() {
        // Candidate Name
        Column<PlacementItem, String> candidateName = new Column<PlacementItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(final PlacementItem object) {
                return object.getCandidateName();
            }
        };
        candidateName.setFieldUpdater((index, object, value) -> SinksContainerFactory.entryPoint.onHistoryChanged("placement|summary/" + object.getObjectID() + "/" + object.isEditable()));
        dataGrid.addColumn(candidateName, wfmStrings.candidate());
        dataGrid.setColumnWidth(candidateName, 15, com.google.gwt.dom.client.Style.Unit.PCT);
        // Position Offered
        Column<PlacementItem, String> positionOffered = new Column<PlacementItem, String>(new TextCell()) {
            @Override
            public String getValue(final PlacementItem object) {
                return object.getPositionName();
            }
        };
        dataGrid.addColumn(positionOffered, wfmStrings.position());
        dataGrid.setColumnWidth(positionOffered, 15, com.google.gwt.dom.client.Style.Unit.PCT);
        // Date Offered
        Column<PlacementItem, String> dateOffered = new Column<PlacementItem, String>(new TextCell()) {
            @Override
            public String getValue(final PlacementItem object) {
                return object.getDateOffed() != null ? DateUtils.format(object.getDateOffed())+ Utils.getHijriDate(object.getDateOffed()) : "";
            }
        };
        dataGrid.addColumn(dateOffered, hrmsStrings.dateOffered());
        dataGrid.setColumnWidth(dateOffered, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        // Offer status
        Column<PlacementItem, String> offerStatus = new Column<PlacementItem, String>(new TextCell()) {
            @Override
            public String getValue(final PlacementItem object) {
                return object.getStatusName();
            }
        };
        dataGrid.addColumn(offerStatus, hrmsStrings.offerStatus());
        dataGrid.setColumnWidth(offerStatus, 15, com.google.gwt.dom.client.Style.Unit.PCT);
    }

    private void supplyProvider(PlacementItem[] reportResults) {
        List<PlacementItem> placementItems = dataProvider.getList();
        placementItems.clear();
        dataGrid.setPageSize(200);
        Collections.addAll(placementItems, reportResults);
    }
}