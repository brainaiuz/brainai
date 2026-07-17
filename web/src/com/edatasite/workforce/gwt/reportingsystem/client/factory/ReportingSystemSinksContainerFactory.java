package com.edatasite.workforce.gwt.reportingsystem.client.factory;

import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.googlecalendar.client.history.GoogleCalendarHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.SaveReportScheduleViewHistoryProcessor;
import com.edatasite.workforce.gwt.reportingsystem.client.history.ImportReportingDataHistoryProcessor;
import com.edatasite.workforce.gwt.reportingsystem.client.history.ReportingHistoryProcessor;
import com.edatasite.workforce.gwt.reportingsystem.client.history.ReportingSearchHistoryProcessor;
import com.edatasite.workforce.gwt.reportingsystem.client.history.ReportingWelcomeHistoryProcessor;
import com.edatasite.workforce.gwt.reportingsystem.client.sinkscontainer.ReportingHomeSinksContainer;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.unit.AddEditReportingFolder;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.unit.NewReportPopup;
import gwt.material.design.client.ui.MaterialIcon;

/**
 * Created by Virus on 9/10/14.
 */
public class ReportingSystemSinksContainerFactory extends SinksContainerFactory implements Constants {
    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public ReportingSystemSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
    }

    @Override
    public void initDefaultContainers() {
        SinksContainer reportingWelcomeSinksContainer = new ReportingHomeSinksContainer(REPORTING_CATEGORY, wfmStrings.sections(), null);
        reportingWelcomeSinksContainer.setPreparedView(REPORTING_HOME);
        MaterialIcon icon = new MaterialIcon();
        icon.addStyleName("ficon--search main-directories__search-icon");
        icon.addClickHandler((event) -> {
            event.stopPropagation();
            event.preventDefault();
            SinksContainerFactory.entryPoint.onHistoryChanged("reportSearch|view/");
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.REPORTING_SEARCH_ICON_VISIBLE, (sender, args) -> {
            if (args instanceof Boolean) {
                icon.setVisible((Boolean) args);
            }
        });
        reportingWelcomeSinksContainer.setIcon(icon);
        setSinksContainer(reportingWelcomeSinksContainer);
    }

    @Override
    public void registerProcessors() {
        registerHistoryProcessor(REPORTING_CATEGORY, new ReportingWelcomeHistoryProcessor());// History processor for search tab
        registerHistoryProcessor("reporting", new ReportingHistoryProcessor());// History processor for search tab
        registerHistoryProcessor("reportSearch", new ReportingSearchHistoryProcessor());// History processor for search tab
        registerHistoryProcessor("importReportDataFromCSV", new ImportReportingDataHistoryProcessor());// History processor for search tab
        registerHistoryProcessor("calendar", new GoogleCalendarHistoryProcessor());
        registerHistoryProcessor("savereportalert", new SaveReportScheduleViewHistoryProcessor());
    }

    @Override
    public void registerMenuItems() {
        addNewMenuItem(reportingStrings.newReport(), (event) -> NewReportPopup.getInstance(null).show());
        addNewMenuItem(wfmStrings.addFolder(), (event) -> {
            AddEditReportingFolder addFolderPopup = new AddEditReportingFolder();
            addFolderPopup.center();
        });
    }

}
