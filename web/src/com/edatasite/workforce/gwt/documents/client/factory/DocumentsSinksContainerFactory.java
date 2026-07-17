package com.edatasite.workforce.gwt.documents.client.factory;

import com.edatasite.workforce.gwt.core.client.history.SearchHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.documents.client.container.DocumentsSinksContainer;
import com.edatasite.workforce.gwt.googlecalendar.client.history.GoogleCalendarHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailHistoryProcessor;

public class DocumentsSinksContainerFactory extends SinksContainerFactory {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public DocumentsSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
        setDefaultContainer("documents");
    }

    public void initDefaultContainers() {
        SinksContainer home = new DocumentsSinksContainer("documents", wfmStrings.all(), null);
        putContainer(home);
        //Add Left Shortcut items for folders
        MainLayout.get().getSideNavBar().addDocumentsContainer(home);
    }

    public void registerProcessors() {
        registerHistoryProcessor(SEARCH, new SearchHistoryProcessor());
        registerHistoryProcessor("email",new EmailHistoryProcessor());
        // History processor for search tab
        registerHistoryProcessor("calendar", new GoogleCalendarHistoryProcessor());
    }

    public void registerMenuItems() {
        disableAddNew();
    }
}
