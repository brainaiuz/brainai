package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.core.client.View;

import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabBar;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.ui.tabpanels.CallsForTodayTab;
import com.edatasite.workforce.gwt.hrms.client.ui.tabpanels.CurrentInterviewsTab;
import com.edatasite.workforce.gwt.hrms.client.ui.tabpanels.OpenVacanciesTab;
import com.edatasite.workforce.gwt.hrms.client.ui.tabpanels.RecentPlacementsTab;
import com.edatasite.workforce.gwt.hrms.client.ui.tabpanels.ShortListTab;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilxom Lutfullaev
 * Date: 6/21/12
 * Time: 11:51 AM
 */

public class RecruitmentHomeView extends View {

    
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public RecruitmentHomeView() {
        super("recruitmentHome", wfmStrings.home());
    }

    @Override
    public String getIconStyle() {
        return "bgMark recruitmentHome";
    }

    protected Widget onInitialize() {
        CustomTabBar currentInterviewsTabBar = new CustomTabBar(1);
        currentInterviewsTabBar.getElement().setId("current-interviewTab");
        currentInterviewsTabBar.setPanelSize(485, 140);
        currentInterviewsTabBar.setMargin("5px 5px 5px 15px");
        currentInterviewsTabBar.addWidget(new CurrentInterviewsTab(hrmsStrings.currentInterviews()));
        currentInterviewsTabBar.selectTab(0);

        CustomTabBar currentSelectionsTab = new CustomTabBar(1);
        currentSelectionsTab.getElement().setId("shortListTab");
        currentSelectionsTab.setPanelSize(485, 140);
        currentSelectionsTab.setMargin("5px 5px 5px 15px");
        currentSelectionsTab.addWidget(new ShortListTab(hrmsStrings.shortlistOnly(), true));
        currentSelectionsTab.selectTab(0);

        CustomTabBar openVacanciesTab = new CustomTabBar(1);
        openVacanciesTab.getElement().setId("open-vacanciesTab");
        openVacanciesTab.setPanelSize(485, 140);
        openVacanciesTab.setMargin("5px 5px 5px 15px");
        openVacanciesTab.addWidget(new OpenVacanciesTab(hrmsStrings.openVacancies()));
        openVacanciesTab.selectTab(0);

        CustomTabBar callsForTodayTab = new CustomTabBar(1);
        callsForTodayTab.getElement().setId("calls-for-todayTab");
        callsForTodayTab.setPanelSize(485, 140);
        callsForTodayTab.setMargin("5px 5px 5px 15px");
        callsForTodayTab.addWidget(new CallsForTodayTab(hrmsStrings.callsForToday()));
        callsForTodayTab.selectTab(0);

        CustomTabBar recentPlacementTab = new CustomTabBar(1);
        recentPlacementTab.getElement().setId("recent-placementsTab");
        recentPlacementTab.setPanelSize(485, 140);
        recentPlacementTab.setMargin("5px 5px 5px 15px");
        recentPlacementTab.addWidget(new RecentPlacementsTab(hrmsStrings.recentPlacements()));
        recentPlacementTab.selectTab(0);

        CustomTabBar recentCandidateTab = new CustomTabBar(1);
        recentCandidateTab.getElement().setId("recent-candidatesTab");
        recentCandidateTab.setPanelSize(485, 140);
        recentCandidateTab.setMargin("5px 5px 5px 15px");
        recentCandidateTab.addWidget(new ShortListTab(hrmsStrings.recentCandidates(), false));
        recentCandidateTab.selectTab(0);

        FlexTable mainPanel = new FlexTable();
        mainPanel.setWidget(0, 0, currentInterviewsTabBar);
        mainPanel.setWidget(0, 1, currentSelectionsTab);
        mainPanel.setWidget(1, 0, openVacanciesTab);
        mainPanel.setWidget(1, 1, callsForTodayTab);
        mainPanel.setWidget(2, 0, recentPlacementTab);
        mainPanel.setWidget(2, 1, recentCandidateTab);
        add(mainPanel);
        return null;
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