package com.edatasite.workforce.gwt.core.client.ui.search;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.ShowSearchViewType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Nov 3, 2009
 * Time: 2:09:09 PM
 */
public class OverallSearchView extends View {


    public static String searchKey;

    private Button search;
    private TextBox searchBox;
    private Label selectSectionName;
    private Anchor advancedSearchLink;
    private SearchViewWidget searchView;
    private ModuleSearchViewWidget moduleSearchView;

    public OverallSearchView() {
        super("overalSearch", wfmStrings.overallSearch());
    }

    protected Widget onInitialize() {

        searchBox = new TextBox();
        searchBox.setWidth("400px");
        searchBox.setHeight("25px");
        searchBox.setStyleName("wft-searchTextBox");

        search = new Button();
        search.setStyleName("search-button");
        search.setHeight("25px");
        search.setText(wfmStrings.search());

        selectSectionName = new Label("");
        selectSectionName.setStyleName("customTitle");
        selectSectionName.getElement().getStyle().setFontSize(20, Style.Unit.PX);
        selectSectionName.getElement().getStyle().setPaddingLeft(20, Style.Unit.PX);

        searchView = new SearchViewWidget();
        moduleSearchView = new ModuleSearchViewWidget(searchView, selectSectionName);

        advancedSearchLink = new Anchor(wfmStrings.advancedSearch());
        advancedSearchLink.setStyleName("wft-advancedSearchLink");
        advancedSearchLink.addClickHandler(event -> searchView.showContent(ShowSearchViewType.AdvancedSearchView));

        searchBox.addKeyPressHandler(event -> {
            if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                setSearchKey(searchBox.getText());
                moduleSearchView.initSearchEngine(searchView.getAdvancedSearchRpc());
            }
        });
        searchBox.addKeyUpHandler(event -> {
            if (event.getNativeKeyCode() != KeyCodes.KEY_ENTER) {
                TextBox tBox = (TextBox) event.getSource();
                WorkforceEntryPoint entryPoint = (WorkforceEntryPoint) SinksContainerFactory.entryPoint;
                entryPoint.getSearchBox().setText(tBox.getText());
            } else {
                setSearchKey(searchBox.getText());
                moduleSearchView.initSearchEngine(searchView.getAdvancedSearchRpc());
            }
        });

        search.addClickHandler(clickEvent -> {
            setSearchKey(searchBox.getText());
            searchView.showContent(ShowSearchViewType.ResultSearchView);
            moduleSearchView.initSearchEngine(searchView.getAdvancedSearchRpc());
        });

        FlexTable searchTable = new FlexTable();

        searchTable.setWidget(0, 0, searchBox);
        searchTable.getFlexCellFormatter().setAlignment(0, 0, HorizontalPanel.ALIGN_LEFT, HorizontalPanel.ALIGN_MIDDLE);

        searchTable.setHTML(0, 1, "&nbsp;&nbsp;&nbsp;&nbsp;");

        searchTable.setWidget(0, 2, search);
        searchTable.getFlexCellFormatter().setAlignment(0, 2, HorizontalPanel.ALIGN_LEFT, HorizontalPanel.ALIGN_MIDDLE);

        searchTable.setWidget(0, 3, selectSectionName);
        searchTable.getFlexCellFormatter().setRowSpan(0, 3, 2);
        searchTable.getFlexCellFormatter().setAlignment(0, 3, HorizontalPanel.ALIGN_CENTER, HorizontalPanel.ALIGN_TOP);

        searchTable.setWidget(1, 0, advancedSearchLink);
        searchTable.getFlexCellFormatter().setColSpan(1, 0, 3);
        searchTable.getFlexCellFormatter().setAlignment(1, 0, HorizontalPanel.ALIGN_RIGHT, HorizontalPanel.ALIGN_TOP);


        HorizontalPanel hpSearch = new HorizontalPanel();
        hpSearch.setWidth("100%");
        hpSearch.setHeight("70px");
        hpSearch.getElement().getStyle().setBackgroundColor("#F5F5F5");
        hpSearch.getElement().getStyle().setProperty("borderBottom", "1px solid #6B90DA");
        hpSearch.setSpacing(10);
        hpSearch.add(searchTable);
        hpSearch.setCellHorizontalAlignment(searchTable, HorizontalPanel.ALIGN_LEFT);
        hpSearch.setCellVerticalAlignment(searchTable, HorizontalPanel.ALIGN_MIDDLE);

        add(hpSearch);
        add(searchView);


        final WorkforceEntryPoint entryPoint = (WorkforceEntryPoint) SinksContainerFactory.entryPoint;
        final String query = entryPoint.getSearchBox().getText();

        if (query != null || !"".equals(query)) {
            searchBox.setText(query);
            setSearchKey(searchBox.getText());
            searchView.getAdvancedSearchRpc().setSearchKey(query);
            moduleSearchView.initSearchEngine(searchView.getAdvancedSearchRpc());
        }


        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SEARCH_CLICKED, OverallSearchView.this, (sender, args) -> onSearchClick(args));

        return null;
    }

    private void onSearchClick(Object args) {
        if (searchBox != null) {
            String keyword = (String) args;
            searchBox.setText(keyword);
            setSearchKey((String) args);
            moduleSearchView.initSearchEngine(searchView.getAdvancedSearchRpc());
        }
    }

    public String getIconStyle() {
        return null;
    }

    public FlowPanel getHelpContainer() {
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.add(moduleSearchView);
        return flowPanel;
    }


    public static void setSearchKey(String _searchKey) {
        searchKey = _searchKey;
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