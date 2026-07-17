package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.AccessLogListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.UserSessionHistoryItem;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.QuickViewPanel;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionField;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionLabel;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.ProvidesKey;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 12.06.2009                               `
 * Time: 22:46:36
 * To change this template use File | Settings | File Templates.
 */
public class AccessLogListQuickView extends QuickViewPanel {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public AccessLogListQuickView(AccessLogListView accessLogListView) {
    }

    KpiDataGrid<UserSessionHistoryItem> dataGrid = new KpiDataGrid<>(KEY_PROVIDER);

    public static final ProvidesKey<UserSessionHistoryItem> KEY_PROVIDER = item -> item == null ? null : item.getAccessedSectionName();

    protected boolean doPreview(Object o) {

        if (!(o instanceof AccessLogListItem)) {
            return false;
        }
        clear();
        final AccessLogListItem item = (AccessLogListItem) o;

        PreviewSectionField field;
        PreviewSectionLabel label;
        final PreviewSectionContainer container = new PreviewSectionContainer();
        label = new PreviewSectionLabel(backendStrings.accessLog(), "");
        field = new PreviewSectionField();
        field.addField(backendStrings.companyID(), item.getCompanyid().toString());
        field.addField(wfmStrings.companyName(), item.getCompanyName());
        field.addField(wfmStrings.user(), item.getUserName());
        field.addField(wfmStrings.userEmail(), item.getEmail());
        field.addField(backendStrings.dateAccessed(), item.getLastAccessDate().toString());
        field.addField(backendStrings.browser(), item.getBrowserType());
        //field.addField("Accessed Section", item.getAccessedSection());
        container.addSection(label, field);
        //accessedSectionHistory
        label = new PreviewSectionLabel(backendStrings.accessedSectionHistory(), "");
        field = new PreviewSectionField();

        dataGrid.supplyProvider(item.getUserSessionHistory());
        dataGrid.setWidth("600px");
        dataGrid.setHeight("200px");
        dataGrid.setTitle(backendStrings.sectionsHistory());

        Column<UserSessionHistoryItem, String> sectionName = new Column<UserSessionHistoryItem, String>(new TextCell()) {

            @Override
            public String getValue(final UserSessionHistoryItem object) {
                return object.getAccessedSectionName();
            }
        };
        dataGrid.addColumn(sectionName, wfmStrings.sectionName());
        dataGrid.setColumnWidth(sectionName, 200, Style.Unit.PX);

        Column<UserSessionHistoryItem, String> accessedDateTime = new Column<UserSessionHistoryItem, String>(new TextCell()) {

            @Override
            public String getValue(final UserSessionHistoryItem object) {
                return object.getLastAccessDate();
            }
        };

        dataGrid.addColumn(accessedDateTime, backendStrings.accessedDateTime());
        dataGrid.setColumnWidth(accessedDateTime, 200, Style.Unit.PX);

        Column<UserSessionHistoryItem, String> moduleLoadedTime = new Column<UserSessionHistoryItem, String>(new TextCell()) {

            @Override
            public String getValue(final UserSessionHistoryItem object) {
                return object.getModuleLoadedTime();
            }
        };
        dataGrid.addColumn(moduleLoadedTime, backendStrings.moduleLoadedTime());
        dataGrid.setColumnWidth(moduleLoadedTime, 200, Style.Unit.PX);

        FlowPanel wc = new FlowPanel();
        wc.add(dataGrid);
        if (item.getUserSessionHistory().length > 0) {
            field.addWidget(wc);
        }

        container.addSection(label, field);

        add(container);
        return true;
    }
}
