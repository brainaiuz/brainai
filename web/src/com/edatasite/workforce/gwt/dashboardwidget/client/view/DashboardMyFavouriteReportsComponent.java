package com.edatasite.workforce.gwt.dashboardwidget.client.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.RepRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class DashboardMyFavouriteReportsComponent extends DashboardBaseWidget {
    private final MaterialLink selectedLink = new MaterialLink();

    private DataListBox categoryListBox;
    private MaterialPanel content;
    private MaterialPanel footerPanel;
    private final WfmButton2 moreButton = new WfmButton2(null, "btn btn-lg btn-block text-center");
    private MaterialPanel header;
    private final TextBox nameBox = new TextBox();

    private List<SelectListRpc> reports;
    private List<SelectListRpc> selectListRpcs;

    private String searchKey;
    private Integer totalCount = 0;
    private LinkedHashMap<SelectItem, List<SelectListRpc>> items;

    public DashboardMyFavouriteReportsComponent(DashboardComponentItem conf) {
        this.gridItemConfig = conf;
    }

    @Override
    protected void initInternal() {
//        setLimit(15);
        setTitle(wfmStrings.myFavouriteReports());
        content = new MaterialPanel("widget-content");
        categoryListBox = new DataListBox();
        if (!enableToShowSample) {
            categoryListBox.addValueChangeHandler(event -> {
                content.clear();
                if (items.get(event.getValue()) != null) {
                    for (SelectListRpc report : items.get(event.getValue())) {
                        content.add(drawRow(report));
                        contentPanel.add(content);
                    }
                    resetPaging();
                } else getData();
            });
            filterPanel.add(categoryListBox);
        }
        footerPanel = new MaterialPanel("widget-footer");
        moreButton.getElement().setInnerText(wfmStrings.loadMore());
        moreButton.addClickHandler(clickEvent -> {
            drawReportWidget();
        });
        footerPanel.add(moreButton);
        mainPanel.add(footerPanel);
        contentPanel.add(addSearchPanel());
        contentPanel.add(content);
        getData();

    }

    @Override
    protected void getData() {
        LoadingPanel.loading(true);
        ReportingService.App.get().queryForReportsByCategory(0, new AsyncCallback<RepRpc>() {

            @Override
            public void onFailure(Throwable throwable) {
                LoadingWidgets.get(getCode()).hide();
            }

            @Override
            public void onSuccess(RepRpc result) {
                if (result == null || result.getFolders().isEmpty()) noData();
                else {
                    LoadingPanel.loading(false);
                    resetPaging();
                    content.clear();
                    items = new LinkedHashMap<>();
                    reports = result.getFolders().get(0).getReports();
                    for (SelectListRpc selectListRpc : reports) {
                        if (selectListRpc != null) {
                            SelectItem folder = new SelectItem(selectListRpc.getFolderId(), selectListRpc.getFolder());
                            if (items.get(folder) == null) {
                                List<SelectListRpc> favouriteReports = new ArrayList<>();
                                favouriteReports.add(selectListRpc);
                                items.put(folder, favouriteReports);
                            } else {
                                List<SelectListRpc> favouriteReports = items.get(folder);
                                favouriteReports.add(selectListRpc);
                                items.remove(folder);
                                Collections.sort(favouriteReports);
                                items.put(folder, favouriteReports);
                            }
                        }
                    }
                    List<SelectItem> selectItems = new ArrayList<>(items.keySet());
                    Collections.sort(selectItems);
                    selectListRpcs = new ArrayList<>();
                    for (SelectItem item : selectItems) {
                        selectListRpcs.addAll(items.get(item));
                    }
//                    selectListRpcs = reports;
                    drawReportWidget();
                    categoryListBox.setItems(selectItems.toArray(new SelectItem[]{}));
                }
            }
        });
    }

    private void drawReportWidget() {
        totalCount = selectListRpcs.size();
        if (totalCount > start + limit) {
            footerPanel.setVisible(true);
            for (int i = start; i < start + limit; i++) {
                content.add(drawRow(selectListRpcs.get(i)));
            }
            start += limit;
        } else {
            for (int i = start; i < totalCount; i++) {
                content.add(drawRow(selectListRpcs.get(i)));
            }
            footerPanel.setVisible(false);
        }
    }

    @Override
    protected void getSampleData(boolean nodata) {
        if (nodata) {
            SelectListRpc rpc = new SelectListRpc();
            rpc.setName("Aged Payables");
            rpc.setDescription("A report allows you to view the balances you owe to other companies for supplies, inventory and services your company receives.");
            content.add(drawRow(rpc));

            SelectListRpc rpc1 = new SelectListRpc();
            rpc1.setName("Profit and Loss");
            rpc1.setDescription("A financial statement that summarizes the revenues, costs and expenses incurred during a specific period of time.");
            content.add(drawRow(rpc1));

            SelectListRpc rpc2 = new SelectListRpc();
            rpc2.setName("Cash Flow");
            rpc2.setDescription("Actual changes in cash as opposed to accounting revenues and expenses.");
            content.add(drawRow(rpc2));

            SelectListRpc rpc3 = new SelectListRpc();
            rpc3.setName("Employee Profile Information");
            rpc3.setDescription("Record of your all employee’s information; includes personal information, job information and emergency contact information sections...");
            content.add(drawRow(rpc3));
        }
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.MY_FAVOURITE_REPORTS;
    }

    @Override
    protected String getEmptyText() {
        return wfmStrings.currentlyYouDontHaveAnyTasks();
    }

    private Div drawRow(SelectListRpc item) {
        Div rowDiv = new Div("widget-row-expandable");
        Div favouriteDiv = new Div("widget-row");
        favouriteDiv.addStyleName("widget-row--favourite");

        Div iconDiv = new Div("widget-row__icon");
        Icon icon = new Icon();
        icon.setStyleName("ficon--star");
        iconDiv.add(icon);
        favouriteDiv.add(iconDiv);

        Div itemDiv = new Div("widget-row__item widget-row__item--grow");
        itemDiv.add(drawReportPanel(item));
        favouriteDiv.add(itemDiv);

        Div endDiv = new Div("widget-row__end");
        favouriteDiv.add(endDiv);
        rowDiv.add(favouriteDiv);

        return rowDiv;
    }

    private Div drawReportPanel(SelectListRpc item) {
        Div mainPanel = new Div("cp_profile-min");
        Div titlePanel = new Div("cp_profile-min__title");
        Div reportingNameDiv = new Div("cp_profile-min__name");
        reportingNameDiv.getElement().setInnerText(item.getName());
        titlePanel.add(reportingNameDiv);

        Div reportingDescriptionDiv = new Div("cp_profile-min__company");
        reportingDescriptionDiv.setStyle("font-style: italic");
        String reportingDescription = item.getDescription();
        reportingDescriptionDiv.getElement().setInnerText(reportingDescription);
        titlePanel.add(reportingDescriptionDiv);

        titlePanel.addClickHandler((event) -> {
            String url = item.isFakeReport() ? (GWT.getHostPageBaseURL() + item.getTargetLink())
                    : ("Reporting.html#" + "reporting|stepControl/" + item.getId() + "/savedreport/" + Utils.encrypt(item.getName()));
            Utils.openURL(url);
        });
        titlePanel.setStyle("cursor:pointer");

        mainPanel.add(titlePanel);
        return mainPanel;
    }

    private MaterialPanel addSearchPanel() {
        header = new MaterialPanel("widget-row widget-finder");
        Div nameDiv = new Div("widget-finder-search");
        nameBox.setStyleName("form-control");
        nameBox.setPlaceHolder(Property.get(Constants.REPORTING, wfmStrings.search(), wfmStrings.report()));
        nameDiv.add(nameBox);
        if (!enableToShowSample) {
            nameBox.addKeyPressHandler(keyPressEvent -> {
                if (keyPressEvent.getNativeEvent().getKeyCode() == (char) KeyCodes.KEY_ENTER) {
                    searchAndView(nameBox);
                }
            });
        }
        header.add(nameDiv);
        Div endDiv = new Div("widget-row__end");
        MaterialLink searchLink = new MaterialLink();
        searchLink.addClickHandler(event -> {
            selectedLink.removeStyleName("cp_abc__letter--selected");
            searchAndView(nameBox);
        });
        searchLink.setStyleName("widget-finder-search__button");
        Icon searchIcon = new Icon();
        searchIcon.setStyleName("ficon--search");
        searchLink.add(searchIcon);
        endDiv.add(searchLink);
        header.add(endDiv);
        return header;
    }

    private void resetPaging() {
        totalCount = 0;
        start = 0;
        limit = 30;
    }

    private void searchAndView(TextBox nameBox) {
        if (nameBox.getText() != null && !nameBox.getText().isEmpty()) {
            searchKey = nameBox.getText();
            content.clear();
            for (SelectListRpc report : reports) {
                if (report.getName().toLowerCase().startsWith(searchKey.toLowerCase()) ||
                        report.getName().toLowerCase().contains(searchKey.toLowerCase())) {
                    content.add(drawRow(report));
                }
            }
            footerPanel.setVisible(false);
        } else getData();
        resetPaging();
    }
}
