package com.edatasite.workforce.gwt.reportingsystem.client.ui;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.ReportingFolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Arrays;

public class ReportingSearch extends View implements Colapse {

    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();

    private static ReportingSearch instance;

    public static ReportingSearch getInstance() {
        if (instance == null) {
            instance = new ReportingSearch();
        }
        return instance;
    }

    interface ReportingSearchUiBinder extends UiBinder<HTMLPanel, ReportingSearch> {
    }

    private ReportingFolder folderWidget;
    private KpiTextBox search;
    private static ReportingSearchUiBinder ourUiBinder = GWT.create(ReportingSearchUiBinder.class);
    private HTMLPanel folders;

    private ReportingSearch() {
        super("reportingSearch", wfmStrings.search());
        folders = ourUiBinder.createAndBindUi(this);
        add(folders);
        search = new KpiTextBox();
        search.setPlaceholder(wfmStrings.searchTypeMessage());
        Div operPanelWrapper = new Div("operPanel__wrapper");
        Div operPanel = new Div("operPanel--header operPanel");
        Div operPanelActions = new Div("operPanel__actions");

        operPanelWrapper.add(operPanel);
        operPanel.add(operPanelActions);

        FlowPanel searchPanel = new FlowPanel();
        searchPanel.addStyleName("searchForm");

        ActionButton searchIcon = new ActionButton("", "searchForm__btn");
        searchIcon.addClickHandler(clickEvent -> searchForReports(search.getValue()));
        search.addKeyUpHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                searchForReports(search.getValue());
            }
        });
        searchPanel.setWidth(Constants.MAX_DEFAULT_WIDTH);
        searchPanel.add(search);
        searchPanel.add(searchIcon);
        operPanelActions.add(searchPanel);

        folders.add(operPanelWrapper);

        FolderRpc folder = new FolderRpc();
        folder.setName(reportingStrings.searchResults());
        folder.setDescription(reportingStrings.searchFolderDescription());
        folder.setIcon("ficon--report-issues");
        folderWidget = new ReportingFolder(folder);
    }

    @Override
    protected Widget onInitialize() {

        return null;
    }

    private void searchForReports(String value) {
        if (value != null && !value.isEmpty()) {
            ReportingService.App.get().searchReport(value, new AsyncCallback<SelectListRpc[]>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(SelectListRpc[] result) {
                    folderWidget.setReports(new ArrayList<>(Arrays.asList(result)));
                    folders.add(folderWidget);
                }
            });
        }
    }

    @Override
    protected void onDetach() {
        MainLayout.get().considerBodyHasOperPanel(false);
        super.onDetach();
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.REPORTING_SEARCH_ICON_VISIBLE, true, ReportingSearch.this);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        MainLayout.get().considerBodyHasOperPanel(true);

        if (folders == null) {
            folders = ourUiBinder.createAndBindUi(this);
        }
        add(folders);
        search.setFocus(true);
        search.setSelectionRange(0, search.getValue().length());
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.REPORTING_SEARCH_ICON_VISIBLE, false, ReportingSearch.this);
    }

    @Override
    public String getIconStyle() {
        return "ficon--search";
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
