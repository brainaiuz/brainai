package com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.unit;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportDirectoryPathRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingCategoryRPC;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.ReportMetaWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Virus on 9/30/14.
 */
public class NewReportPopup extends KpiSideNavBox {
    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    private static NewReportPopup instance;

    public static NewReportPopup getInstance(ReportingCategoryRPC category) {
        if (instance == null) {
            instance = new NewReportPopup(category);
        } else {
            if (category != null) {
                instance.category = category;
            }
            instance.loadAndInit();
        }
        return instance;
    }

    private ArrayList<ReportDirectoryPathRpc> templateList;

    private ReportingCategoryRPC category;

    private NewReportPopup(ReportingCategoryRPC category) {
        super(true);
        this.category = category;
        WfmStrings wfmStrings = WfmStrings.App.get();
        WfmButton2 hideButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        addFooter(hideButton);
        loadAndInit();
//             //TODO copy
        hideButton.addClickHandler(event -> hide());
    }

    private void loadAndInit() {
        loadData();
        setHeader();
    }

    private void setHeader() {
        getContentHeader().clear();
        Heading h1 = new Heading(HeadingSize.H1);
        h1.addStyleName("hasicon--left");
        Span span = new Span();
        span.setText(category.getName());
        h1.add(span);
        addHeader(h1);
    }

    private void loadData() {
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setCategoryID(this.category.getId());
        getBody().clear();

        ReportingService.App.get().getReportTemplateList(filter, new AbstractAsyncCallback<ArrayList<ReportDirectoryPathRpc>>() {
            @Override
            public void onSuccess(ArrayList<ReportDirectoryPathRpc> result) {
                templateList = result;
                init();
            }
        });
    }

    private void init() {

        HTML link = new HTML("<figure class='figure-h figure-link center'> " +
                "<span class='figure-icon'> " +
                "<svg class=' icon--uploadCloud' style=''>" +
                "<use href='mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#uploadCloud'></use></svg>" +
                "</span>" +
                "<figcaption>" + reportingStrings.connectYouData() + "</figcaption>" +
                "</figure>");
        link.addClickHandler(ch -> {
            Cookies.removeCookie("reportcategoryid");
            Cookies.setCookie("reportcategoryid", String.valueOf(category.getId()), new Date(System.currentTimeMillis() + 1000000000));
            //Utils.redirect(GWT.getHostPageBaseURL() + "Reporting.html");
            Utils.forceReload();
        });
        addBody(link);
        for (ReportDirectoryPathRpc categoryItem : templateList) {
            for (SelectItem templateItem : categoryItem.getFiles()) {
                ReportMetaWidget selectReport = new ReportMetaWidget(templateItem);
                selectReport.setSelectionHandler(NewReportPopup.this::hide);
                selectReport.setDeleteHandler(NewReportPopup.this::hide);
                addBody(selectReport);
            }
        }
    }

}