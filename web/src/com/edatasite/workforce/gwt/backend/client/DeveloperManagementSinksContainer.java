package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.CompanyListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.CompanySavedReports;
import com.edatasite.workforce.gwt.backend.client.ui.view.CustomFormsListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.CustomisedPDFTemplatesListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.DynamicLoginViewList;
import com.edatasite.workforce.gwt.backend.client.ui.view.ReportingListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.ReportingXMLTemplatesListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.testview.ReportsListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.tools.SavedReportColumnsChange;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PublicWebHookListView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Mar 27, 2011
 * Time: 3:04:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeveloperManagementSinksContainer extends SinksContainer {

    DeveloperManagementSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    protected void initViews() {
        addView(new CompanyListView(true));
        addView(new CustomisedPDFTemplatesListView());
        addView(new PublicWebHookListView());
        addView(new ReportingListView("XML", "companyXmlTemplates", wfmStrings.companyXMLReportingList()));
        addView(new ReportingListView("XLS", "companyExcelTemplates", wfmStrings.excelReportingList()));
        addView(new ReportingXMLTemplatesListView(wfmStrings.xmlReportingList()));
        addView(new CompanySavedReports());
        addView(new ReportsListView());
        addView(new SavedReportColumnsChange());
        addView(new CustomFormsListView());
        addView(new DynamicLoginViewList());
//            addView(new OpenWorkspace());
//        addView(new CompanyDashletsView());
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
