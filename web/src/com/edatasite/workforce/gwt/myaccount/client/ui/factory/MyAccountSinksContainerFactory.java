package com.edatasite.workforce.gwt.myaccount.client.ui.factory;

import com.edatasite.workforce.gwt.core.client.history.SearchHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.myaccount.client.MyAccountNewSinksContainer;
import com.edatasite.workforce.gwt.myaccount.client.history.MyAccountNewHistoryProcessor;
import com.edatasite.workforce.gwt.myaccount.client.history.MyAccountUsagePlanSummaryHistoryProcessor;
import com.edatasite.workforce.gwt.myaccount.client.history.PricingOrderHistoryProcessor;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountStrings;


/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Nov 25, 2008
 * Time: 4:54:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyAccountSinksContainerFactory extends SinksContainerFactory {
    private final MyAccountStrings myAccountStrings = MyAccountStrings.App.get();
    

    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    public MyAccountSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
        setDefaultContainer("usageplandefault");
    }

    public void initDefaultContainers() {
        SinksContainer myaccount = new MyAccountNewSinksContainer("usageplandefault", wfmStrings.myBilling());
        myaccount.setPreparedView("allPricingView");
        setSinksContainer(myaccount);
    }

    public void registerProcessors() {
        registerHistoryProcessor(SEARCH, new SearchHistoryProcessor());// History processor for search tab
        registerHistoryProcessor("usageplan", new MyAccountNewHistoryProcessor());
//        registerHistoryProcessor("usageplan", new MyAccountHistoryProcessor());
//        registerHistoryProcessor("usageplannew", new MyAccountNewHistoryProcessor());
        registerHistoryProcessor("usagePlanHistory", new MyAccountUsagePlanSummaryHistoryProcessor());
        registerHistoryProcessor("pricingOrder", new PricingOrderHistoryProcessor());


    }

    public void registerMenuItems() {
        disableAddNew();
    }
}
