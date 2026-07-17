package com.edatasite.workforce.gwt.backend.client.ui.view.dashboard;

import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.ui.MaterialLink;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by Abdulaziz on 05.05.2016.
 */
public class CompaniesNotLoggedWidget extends DashboardBaseWidget {
    private BackendServiceAsync backendService = BackendService.App.get();


    private DataListBox monthListBox;
    private DataListBox yearListBox;
    private HTML statisticPanel;

    Anchor moreLink;


    @Override
    protected void initInternal() {
        title.setTitle("Company Not Logging");

        moreLink = new Anchor();
        moreLink.setText("More");
        moreLink.setHref(BACKEND_URL + " ");
        moreLink.setStyleName("link-more left");

        MaterialLink clickButton = new MaterialLink(wfmStrings.refresh());
        clickButton.setTitle("Refresh");
        clickButton.addClickHandler(clickEvent -> loadComponentData());
        filterPanel.add(clickButton);
//        getData();
    }

    public CompaniesNotLoggedWidget() {
        onInitialize();
    }

    @Override
    protected void getData() {
        LoadingWidgets.get(getCode()).show();
        backendService.getNotLoggingCompanyRatio(new AsyncCallback<LinkedHashMap<String, String>>() {

            @Override
            public void onFailure(Throwable throwable) {
                clearPanel();
                LoadingWidgets.get(getCode()).hide();

            }

            @Override
            public void onSuccess(LinkedHashMap<String, String> result) {

                LoadingWidgets.get(getCode()).hide();
                if (result.size() >= 0) {
                    clearPanel();
                    draw(result);
                } else {
                    statisticPanel.setHTML("");
                    clearPanel();
                }
            }
        });
    }

    @Override
    protected void getSampleData(boolean nodata) {

    }

    @Override
    public String getCode() {
        return "COMPANY_NOT_LOGGING";
    }

    private void draw(LinkedHashMap<String, String> result) {
        Element table = DOM.createTable();
        table.addClassName("table table-documents");
        Element tHead = DOM.createTHead();
        tHead.setClassName("regTable");

        Element iterationTr = DOM.createTR();
        Element title1 = DOM.createTH();
        Element title2 = DOM.createTH();

        title1.setInnerText(wfmStrings.company());
        title2.setInnerText(wfmStrings.notLoggingSince());

        iterationTr.appendChild(title1);
        iterationTr.appendChild(title2);
        tHead.appendChild(iterationTr);
        table.appendChild(tHead);

        Element tBody = DOM.createTBody();
        Double[] companyDiff = new Double[2];

        Set<String> keys = result.keySet();
        for (String key : keys) {
            Element tr = DOM.createTR();
            Element td = DOM.createTD();

            Anchor a;
            if (key != null && !"".equals(key)) {
                a = new Anchor(key.length() > 13 ? key.substring(0, 13) : key);
            } else {
                a = new Anchor(key != null ? key : "N/A");
            }


            td.appendChild(a.getElement());
            Element td1 = DOM.createTD();

            String expireDate = result.get(key);
            td1.setInnerText(expireDate);

            tr.appendChild(td);
            tr.appendChild(td1);
            tBody.appendChild(tr);
        }
        table.appendChild(tBody);
        contentPanel.getElement().appendChild(table);
    }
}



