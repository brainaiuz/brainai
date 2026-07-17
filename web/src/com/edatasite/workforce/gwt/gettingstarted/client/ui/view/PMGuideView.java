package com.edatasite.workforce.gwt.gettingstarted.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.wfmTabPanel.WfmTabListener;
import com.edatasite.workforce.gwt.core.client.ui.wfmTabPanel.WfmTabPanel;
import com.edatasite.workforce.gwt.gettingstarted.client.ui.GuideButtonClickListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;


/**
 * User: Abdulaziz
 * Date: 14.06.2009
 * Time: 19:22:17
 */
public class PMGuideView extends View implements Colapse, HistoryListener {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final int tabs = 6;
    private WfmTabPanel tabPanel;
    private int currentTab;
    private int allowedTabs;
    private String historyToken;
    private GettingStartedMainView[] views;

    public PMGuideView() {
        super("pmguide", wfmStrings.gettingStarted());
        History.addHistoryListener(this);
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        views = new GettingStartedMainView[6];
        tabPanel = new WfmTabPanel();
        tabPanel.addTab(views[0] = new IntroductionGuideView(), wfmStrings.introduction());
        tabPanel.addTab(views[1] = new CreateEmployeeGuideView(), wfmStrings.addingCompanyEmployees());
        tabPanel.addTab(views[2] = new CreateDepartmentGuideView(), wfmStrings.organizeCompanyEmployees());
        tabPanel.addTab(views[3] = new CreateProjectGuideView(), wfmStrings.addingCompanyProjects());
        tabPanel.addTab(views[4] = new CreateTaskGuideView(), wfmStrings.addingCompanyTasks());
        tabPanel.addTab(views[5] = new FinishingGuideView(), wfmStrings.done());
        tabPanel.addTabListener(new WfmTabListener() {

            public boolean onBeforeTabSelected(int tabIndex) {
                return allowedTabs >= tabIndex;
            }

            public void onTabSelected(int tabIndex) {
                if (!views[tabIndex].isShown()) {
                    views[tabIndex].showView();
                } else {
                    views[tabIndex].refresh();
                }
                currentTab = tabIndex;
            }
        });
        History.newItem(String.valueOf(allowedTabs = currentTab = 0));
        tabPanel.setWidth("970px");

        for (int i = 0; i < tabs; i++) {
            views[i].addButtonClickListener(new GuideButtonClickListener() {
                public void onBackButtonClick() {
                    if (tabPanel.getSelectedTab() != 0)
                    {
                        History.newItem(String.valueOf(--currentTab));
                    }
                }

                public void onNextButtonClick() {
                    if (tabPanel.getTabCount() - 1 > tabPanel.getSelectedTab())
                    {
                        History.newItem(String.valueOf(allowedTabs = ++currentTab));
                    }
                }
            });
        }
        tabPanel.selectTab(0);
        add(tabPanel);
        views[tabPanel.getSelectedTab()].refresh();
        return null;
    }

    public void onHistoryChanged(String historyToken) {
        int tabIndex;
        if (!historyToken.equals("")) {
            try {
                tabIndex = Integer.parseInt(historyToken);
                tabPanel.selectTab(tabIndex);
            } catch (NumberFormatException e) {
                tabPanel.selectTab(0);
            }

        }
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}