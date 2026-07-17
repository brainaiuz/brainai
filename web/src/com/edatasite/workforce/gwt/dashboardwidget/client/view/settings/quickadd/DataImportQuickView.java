package com.edatasite.workforce.gwt.dashboardwidget.client.view.settings.quickadd;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.ImportGuideItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Italic;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;

public class DataImportQuickView extends KpiSideNavBox {
    interface DataImportQuickViewUiBinder extends UiBinder<Widget, DataImportQuickView> {}

    DataImportQuickViewUiBinder uiBinder = GWT.create(DataImportQuickViewUiBinder.class);

    @UiField
    HTMLPanel container;
    @UiField
    HTMLPanel mediaHeader;
    @UiField
    HTMLPanel mediaBody;
    @UiField
    HTMLPanel importHeader;
    @UiField
    HTMLPanel importPanel;
    @UiField
    HTMLPanel infoPanel;

    private WfmButton2 markAsDone, cancel;

    public DataImportQuickView() {
        super(DEFAULT_WIDTH);
        uiBinder.createAndBindUi(this);

        addOpeningHandler(o -> loadData());
        show();
        initInternal();
    }

    private void loadData() {
        LoadingPanel.loading(true, getBody());
        DashboardWidgetService.App.get().getDataImportGettingStarted(new AsyncCallback<ArrayList<ImportGuideItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<ImportGuideItem> importItems) {
                LoadingPanel.loading(false);

                for (ImportGuideItem item : importItems) {
                    switch (item.getType()) {
                        case EMPLOYEE:
                            item.setName(wfmStrings.employee());
                            item.setUrl(ImportFilePopUp.SAMPLE_EMPLOYEE_CSV);
                            break;
                        case CUSTOMER:
                            item.setName(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
                            item.setUrl(ImportFilePopUp.SAMPLE_CLIENT_CSV);
                            break;
                        case SUPPLIER:
                            item.setName(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()));
                            item.setUrl(ImportFilePopUp.SAMPLE_SUPPLIER_CSV);
                            break;
                        case PRODUCT:
                            item.setName(wfmStrings.product());
                            item.setUrl(ImportFilePopUp.SAMPLE_PRODUCT_CSV);
                            break;
                        case CHART_OF_ACCOUNTS:
                            item.setName(wfmStrings.chartOfAccounts());
                            item.setUrl(ImportFilePopUp.SAMPLE_CHARTOFACCOUNTS_CSV);
                            break;
                        case CONTACT:
                            item.setName(wfmStrings.contact());
                            item.setUrl(ImportFilePopUp.SAMPLE_CONTACT_CSV);
                            break;
                        case LEAD:
                            item.setName(wfmStrings.lead());
                            item.setUrl(ImportFilePopUp.SAMPLE_LEAD_CSV);
                            break;
                        case OPPORTUNITY:
                            item.setName(wfmStrings.opportunity());
                            item.setUrl(ImportFilePopUp.SAMPLE_OPPORTUNITY_CSV);
                            break;
                    }

                    drawImportItem(item);
                }

                initBodyScrollContent();
            }
        });
    }

    private void initInternal() {
        //header
        addHeader(new HTML(wfmStrings.dataMigration()));

        mediaHeader.getElement().setInnerHTML(wfmStrings.dataImportMediaHeader());
        mediaHeader.getElement().getStyle().setProperty("lineHeight", "2");
        Element iFrame = DOM.createIFrame();
        iFrame.setAttribute("id", "caseSummary" + this.hashCode());
        iFrame.setAttribute("width", "100%");
        iFrame.setAttribute("height", "200px");
        iFrame.setAttribute("frameborder", "0");
        iFrame.setAttribute("src", "https://www.youtube.com/embed/SR1HhNDI13Y?list=PLa7P0Qa2jO2UcMXtZAqbrKMwoTlB-WY8S");
        iFrame.setAttribute("allowfullscreen", "");
        mediaBody.getElement().appendChild(iFrame);

        importHeader.getElement().setInnerHTML("DOWNLOAD CSV TEMPLATES");

        infoPanel.getElement().setInnerHTML(wfmMessages.dataImportGuideInfo("https://www.kpi.com/wiki/importing-data-to-kpi-com/", Utils.getProductName()));
        infoPanel.getElement().getStyle().setProperty("lineHeight", "2");
        //body
        addBody(container);

        markAsDone = new WfmButton2(wfmStrings.markAsDone(), WfmButton2.BTN_SUCCESS, event -> command.execute());
        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, event -> remove());

        //footer
        addFooter(markAsDone);
        addFooter(cancel);
    }

    private void drawImportItem(ImportGuideItem item) {
        MaterialLink link = new MaterialLink();
        link.setClass("bar");

        if (item.isEnabled()) {
            link.setHref(item.getUrl());
        } else {
            link.setEnabled(false);
        }

        Span csv = new Span("CSV");
        csv.setClass("badge badge--primary");

        Span title = new Span(item.getName());
        title.setClass("bar__title");

        Span icon = new Span();
        icon.setClass("bar__icon");
        Italic i = new Italic();
        i.setClass("ficon--download-cloud");
        icon.add(i);

        link.add(csv);
        link.add(title);
        link.add(icon);

        importPanel.add(link);
    }
}
