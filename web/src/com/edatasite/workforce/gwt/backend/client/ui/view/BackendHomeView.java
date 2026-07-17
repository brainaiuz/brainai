package com.edatasite.workforce.gwt.backend.client.ui.view;


import com.edatasite.workforce.gwt.backend.client.constants.BackendConstants;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.CountryList;
import com.edatasite.workforce.gwt.backend.client.rpc.CountryListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.IndustryList;
import com.edatasite.workforce.gwt.backend.client.rpc.IndustryListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.SignupsRate;
import com.edatasite.workforce.gwt.backend.client.rpc.Statistics;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabBar;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class BackendHomeView extends View implements BackendConstants {
    private static final BackendStrings backendStrings = BackendStrings.App.get();

    private DataListBox periods;
    private CustomTabWidget periodCustomTabWidget;
    private CustomTabWidget signUpsCustomTabWidget;
    private CustomTabWidget statisticsCustomTabWidget;
    private CustomTabWidget usersByCountryCustomTabWidget;
    private CustomTabWidget usersByIndustryCustomTabWidget;

    private int periodID = 0;

    public BackendHomeView() {
        super("home", backendStrings.backendHome());
    }
    public String getIconStyle() {
        return "backend backendHomeView";
    }

    protected Widget onInitialize() {
        initInternal();
        return null;
    }

    private void initInternal() {

        CustomTabBar periodCustomTabBar = new CustomTabBar(1);

        HorizontalPanel hp1 = new HorizontalPanel();
        HorizontalPanel hp2 = new HorizontalPanel();
        HorizontalPanel hp3 = new HorizontalPanel();
        hp1.setSpacing(5);
        hp3.setSpacing(5);
        HTML reportingPeriodLabel = new HTML("<b class=customTitle>" + backendStrings.reportingPeriod() + ":</b>");
        hp1.add(reportingPeriodLabel);
        hp1.setCellVerticalAlignment(reportingPeriodLabel, HasVerticalAlignment.ALIGN_MIDDLE);
        periods = new DataListBox();
        periods.setWidth("200");
        periods.setAllowFirstItem(true);
        periods.setWithoutNullLabel(true);
        periods.setItems(new SelectItem[]{
                new SelectItem(1, wfmStrings.today()),
                new SelectItem(2, wfmStrings.yesterday()),
                new SelectItem(3, backendStrings.reportingPeriodLast10Days()),
                new SelectItem(4, wfmStrings.thisMonth()),
                new SelectItem(5, wfmStrings.lastMonth()),
                new SelectItem(6, backendStrings.reportingPeriodAllPeriod())});
        periodID = periods.getSelectedIndex();
        periods.addValueChangeHandler(event -> {
            periodID = periods.getSelectedIndex();
            periodCustomTabWidget.viewShow();
            signUpsCustomTabWidget.viewShow();
            statisticsCustomTabWidget.viewShow();
            usersByCountryCustomTabWidget.viewShow();
            usersByIndustryCustomTabWidget.viewShow();
        });

        hp1.add(periods);
        hp1.setCellVerticalAlignment(periods, HasVerticalAlignment.ALIGN_MIDDLE);
        add(hp1);

        periodGenerateTab();

        periodCustomTabBar.addWidget(periodCustomTabWidget);
        periodCustomTabBar.setMargin("5px 5px 5px 5px");
        periodCustomTabBar.setPanelSize(700, 300);
        periodCustomTabBar.selectTab(0);

        hp2.add(periodCustomTabBar);
        add(hp2);

        statisticsGenerateTab();

        signUpsGenerateTab();

        usersByCountryGenerateTab();

        usersByIndustryGenerateTab();

        CustomTabBar statisticsCustomTabBar = new CustomTabBar(4);
        statisticsCustomTabBar.addWidget(statisticsCustomTabWidget);
        statisticsCustomTabBar.addWidget(signUpsCustomTabWidget);
        statisticsCustomTabBar.addWidget(usersByCountryCustomTabWidget);
        statisticsCustomTabBar.addWidget(usersByIndustryCustomTabWidget);
        statisticsCustomTabBar.setMargin("5px 5px 5px 5px");
        statisticsCustomTabBar.setPanelSize(700, 300);
        statisticsCustomTabBar.selectTab(0);

        hp3.add(statisticsCustomTabBar);

        add(hp3);
    }

    public String getStringValueOfPeriod() {
        String str;
        switch (periodID) {
            case 1:
                str = TODAY;
                break;
            case 2:
                str = YESTERDAY;
                break;
            case 3:
                str = LAST_TEN_DAYS;
                break;
            case 4:
                str = THIS_MONTH;
                break;
            case 5:
                str = LAST_MONTH;
                break;
            case 6:
                str = ALL_PERIOD;
                break;
            default:
                str = TODAY;
        }
        return str;
    }

    private String getCustomTITLE(String text) {
        return "<b class=customTitle>" + text + "</b>";
    }

    private void periodGenerateTab() {
        periodCustomTabWidget = new CustomTabWidget(backendStrings.backendHome()) {

            @Override
            public void initData() {
            }

            @Override
            public void viewShow() {
                clear();
                Image lineImage = new Image();
                add(lineImage);
                lineImage.setUrl(GWT.getHostPageBaseURL() + CommandConstants.COMMON_URL
                        + "/displayBackendChart?chartType=dailyActivity&periodId=" + periodID);
            }
        };
    }

    private void statisticsGenerateTab() {
        statisticsCustomTabWidget = new CustomTabWidget(backendStrings.statistics()) {

            @Override
            public void initData() {
            }

            @Override
            public void viewShow() {
                clear();
                final FlexTable statisticsTable = new FlexTable();
                statisticsTable.setCellPadding(5);
                statisticsTable.setCellSpacing(5);
                statisticsTable.setSize("99%", "150px");
                add(statisticsTable);
                BackendService.App.get().getOverallStatistics(new AbstractAsyncCallback<Statistics>() {

                    public void failure(Throwable arg0) {
                    }

                    public void success(Statistics stat) {
                        statisticsTable.clear();
                        statisticsTable.setHTML(0, 0, getCustomTITLE(wfmStrings.companies()));
                        statisticsTable.setHTML(0, 1, stat.getCompaniesCount());
                        statisticsTable.setHTML(1, 0, getCustomTITLE(wfmStrings.users()));
                        statisticsTable.setHTML(1, 1, stat.getUsersCount());
                        statisticsTable.setHTML(2, 0, "<hr>");
                        statisticsTable.getFlexCellFormatter().setColSpan(2, 0, 2);
                        statisticsTable.setHTML(3, 0, getCustomTITLE(backendStrings.counted()) + ":");
                        statisticsTable.setHTML(3, 1, "");
                        statisticsTable.setHTML(4, 0, getCustomTITLE(backendStrings.systemAccess()));
                        statisticsTable.setHTML(4, 1, stat.getSystemAccessCount());
                        statisticsTable.setHTML(5, 0, getCustomTITLE(wfmStrings.departments()));
                        statisticsTable.setHTML(5, 1, stat.getDepartmentCount());
                        statisticsTable.setHTML(6, 0, getCustomTITLE(wfmStrings.projects()));
                        statisticsTable.setHTML(6, 1, stat.getProjectCount());
                        statisticsTable.setHTML(7, 0, getCustomTITLE(wfmStrings.task()));
                        statisticsTable.setHTML(7, 1, stat.getTaskCount());
                        statisticsTable.setHTML(8, 0, getCustomTITLE(Property.get(Constants.TIMESHEET, backendStrings.countTimeSheet(), wfmStrings.timesheet())));
                        statisticsTable.setHTML(8, 1, stat.getTimesheetCount());
                        statisticsTable.setHTML(9, 0, getCustomTITLE(wfmStrings.customer()));
                        statisticsTable.setHTML(9, 1, stat.getClientsCount());
                        statisticsTable.setHTML(10, 0, getCustomTITLE(backendStrings.countLead()));
                        statisticsTable.setHTML(10, 1, stat.getLeadCount());
                    }
                });
            }
        };
    }

    private void signUpsGenerateTab() {
        signUpsCustomTabWidget = new CustomTabWidget(backendStrings.signupsRate()) {

            @Override
            public void initData() {
            }

            @Override
            public void viewShow() {
                clear();
                final FlexTable signUpsTable = new FlexTable();
                signUpsTable.setCellPadding(5);
                signUpsTable.setCellSpacing(5);
                signUpsTable.setSize("99%", "150px");
                add(signUpsTable);

                String days = getStringValueOfPeriod();
                BackendService.App.get().getSignupsRate(days, new AbstractAsyncCallback<SignupsRate>() {

                    public void failure(Throwable arg0) {
                    }

                    public void success(SignupsRate signups) {
                        signUpsTable.clear();
                        signUpsTable.setHTML(0, 0, getCustomTITLE(backendStrings.signups()));
                        signUpsTable.setHTML(0, 1, signups.getSignups());
                        signUpsTable.setHTML(1, 0, getCustomTITLE(backendStrings.newUsers()));
                        signUpsTable.setHTML(1, 1, signups.getNewUsers());
                        signUpsTable.setHTML(2, 0, "<hr>");
                        signUpsTable.getFlexCellFormatter().setColSpan(2, 0, 2);
                        signUpsTable.setHTML(3, 0, getCustomTITLE(wfmStrings.activated()));
                        signUpsTable.setHTML(3, 1, signups.getActivated() + " (" + signups.getActivatedInPercentage() + "%)");
                        signUpsTable.setHTML(4, 0, getCustomTITLE(wfmStrings.used()));
                        signUpsTable.setHTML(4, 1, signups.getUsed() + " (" + signups.getUsedInPercentage() + "%)");
                        signUpsTable.setHTML(5, 0, getCustomTITLE(backendStrings.bounce()));
                        signUpsTable.setHTML(5, 1, signups.getBounce() + " (" + signups.getBounceInPercentage() + "%)");
                        signUpsTable.setHTML(6, 0, getCustomTITLE(wfmStrings.inactive()));
                        signUpsTable.setHTML(6, 1, signups.getInactive() + " (" + signups.getInactiveInPercentage() + "%)");
                    }
                });
            }
        };
    }

    private void usersByCountryGenerateTab() {
        usersByCountryCustomTabWidget = new CustomTabWidget(backendStrings.usersByCountry()) {

            @Override
            public void initData() {
            }

            @Override
            public void viewShow() {
                clear();
                final FlexTable usersByCountryTable = new FlexTable();
                usersByCountryTable.setCellPadding(5);
                usersByCountryTable.setCellSpacing(5);
                usersByCountryTable.setSize("99%", "150px");
                add(usersByCountryTable);

                String days = getStringValueOfPeriod();
                BackendService.App.get().getCountryList(days, new AbstractAsyncCallback<CountryList>() {

                    public void failure(Throwable arg0) {
                        arg0.printStackTrace();
                    }

                    public void success(CountryList list) {
                        if (list.getResults() != null) {
                            usersByCountryTable.clear();
                            CountryListItem[] countryList = list.getResults();
                            for (int i = 0; i < countryList.length; i++) {
                                usersByCountryTable.setHTML(i, 0, countryList[i].getCountry());
                                usersByCountryTable.setHTML(i, 1, countryList[i].getSystemUsedCount());
                                usersByCountryTable.setHTML(i, 2, "(" + countryList[i].getSystemUsedCountInPercentage() + "%)");
                            }
                        }
                    }
                });
            }
        };
    }

    private void usersByIndustryGenerateTab() {
        usersByIndustryCustomTabWidget = new CustomTabWidget(backendStrings.useryByIndustry()) {

            @Override
            public void initData() {
            }

            @Override
            public void viewShow() {
                clear();
                final FlexTable usersByIndustryTable = new FlexTable();
                usersByIndustryTable.setCellPadding(5);
                usersByIndustryTable.setCellSpacing(5);
                usersByIndustryTable.setSize("99%", "150px");
                add(usersByIndustryTable);

                String days = getStringValueOfPeriod();
                BackendService.App.get().getIndustryList(days, new AbstractAsyncCallback<IndustryList>() {

                    public void failure(Throwable arg0) {
                    }

                    public void success(IndustryList list) {
                        if (list.getResults() != null) {
                            usersByIndustryTable.clear();
                            IndustryListItem[] industryList = list.getResults();
                            for (int i = 0; i < industryList.length; i++) {
                                usersByIndustryTable.setHTML(i, 0, industryList[i].getIndustry());
                                usersByIndustryTable.setHTML(i, 1, industryList[i].getSystemUsedCount());
                                usersByIndustryTable.setHTML(i, 2, "(" + industryList[i].getSystemUsedCountInPercentage() + "%)");
                            }
                        }
                    }
                });
            }
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