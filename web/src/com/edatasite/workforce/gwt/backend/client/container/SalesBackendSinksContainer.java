package com.edatasite.workforce.gwt.backend.client.container;

import com.edatasite.workforce.gwt.backend.client.ui.view.CompanyListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 4/13/12
 * Time: 5:35 PM
 */
public class SalesBackendSinksContainer extends SinksContainer {

    public SalesBackendSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new CompanyListView(false));
        //register something UIs
//        boolean userGrant = (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR) || Utils.hasRole(Constants.PM) || Utils.hasRole(Constants.TL));
//        boolean salesRole = (Utils.hasRole(Constants.SALESPERSON) || Utils.hasRole(Constants.SALESMAN));
//        if (userGrant || salesRole) {
//        }
//        if (userGrant) {
//        }
//        addView(new BackendDashboardView(BackendDashboardView.BACKEND_DASHBOARD));
//        addView(new BackendHomeView());
//        addView(new BugListSummaryView());
//        addView(new BugListPerSectionView());
//        addView(new AccessLogListView());
//        addView(new OverallFeaturesListView());
//        addView(new AccessTokenListView());
//        addView(new AccountManagementListView());
//        addView(new PaypalReceiptsListView());
//        addView(new BlackListView());
//        addView(new WFTPlaginView());
//        addView(new SubscriptionManagementView());
//        addView(new GenericSettingsListView());
//        addView(new BugListView());
//        addView(new OnboardingStepListView(true));
    }
}