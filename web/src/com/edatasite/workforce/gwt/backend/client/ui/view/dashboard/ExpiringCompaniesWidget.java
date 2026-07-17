package com.edatasite.workforce.gwt.backend.client.ui.view.dashboard;

import com.edatasite.workforce.gwt.backend.client.rpc.BackendManagementListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;

public class ExpiringCompaniesWidget extends DashboardBaseWidget {

    private final BackendServiceAsync backendService = BackendService.App.get();



    private DataListBox monthListBox;
    private DataListBox yearListBox;
    private HTML statisticPanel;


    Anchor moreLink;


    @Override
    protected void initInternal() {
        title.setTitle("Expiring Company ");

        moreLink = new Anchor();
        moreLink.setText("More");
        moreLink.setHref(BACKEND_URL + "");
        moreLink.setStyleName("link-more left");

        monthListBox = new DataListBox();
        monthListBox.setWidth("70px");
        yearListBox = new DataListBox();
        yearListBox.setWidth("50px");
        yearListBox.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);

        filterPanel.add(monthListBox);
        filterPanel.add(yearListBox);

        FlexTable filter = new FlexTable();
        filter.setStyleName("table-3cols");
        filter.setWidth("100%");
        filter.setWidget(0, 1, statisticPanel);
        filterPanel.add(filter);
        filterPanel.setWidth("100px");

        initFilter();
    }

    private void initFilter() {
        Date currentDate = new Date();
        int currentYear = Integer.valueOf(DateUtils.yearFormat.format(currentDate));
        SelectItem[] monthItems = new SelectItem[12];
        Date date = DateUtil.getYearFirstDay(currentDate);
        for (int i = 0; i < 12; i++) {
            monthItems[i] = new SelectItem(i, DateUtils.monthFullFormat.format(date));
            date = DateUtil.addMonths(date, 1);
        }
        monthListBox.setWithoutNullLabel(true);
        monthListBox.setItems(monthItems);
        monthListBox.setSelected(Integer.valueOf(DateUtils.monthShortFormat.format(new Date())) - 1);
        SelectItem[] yearItem = new SelectItem[5];
         monthListBox.setWithoutNullLabel(true);
        monthListBox.setItems(monthItems);
        monthListBox.setSelected(Integer.valueOf((DateUtils.monthShortFormat.format(new Date())))-1);
        monthListBox.addValueChangeHandler(changeEvent -> loadComponentData());

        yearItem = new SelectItem[5];
        for (int i = 2, j = 0; j < 2; i--, j++) {
            yearItem[j] = new SelectItem(currentYear - i, String.valueOf((currentYear - i)));
        }

        yearItem[2] = new SelectItem(currentYear, String.valueOf(currentYear));

        for (int i = 1, j = 3; i <= 2; i++, j++) {
            yearItem[j] = new SelectItem(currentYear + i, String.valueOf(currentYear + i));

        }

        for (int i = 1, j = 3; i <= 2; i++, j++) {
            yearItem[j] = new SelectItem(currentYear + i, String.valueOf(currentYear + i));
        }

        yearListBox.setWithoutNullLabel(true);
        yearListBox.setItems(yearItem);
        yearListBox.setSelected(currentYear);
        yearListBox.addValueChangeHandler(changeEvent -> loadComponentData());
        loadComponentData();
    }

    public ExpiringCompaniesWidget() {
        onInitialize();
    }

    @Override
    protected void getData() {
        LoadingWidgets.get(getCode()).show();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setModule(LayoutRPC.Backend_SECTION);
        fp.setSelectedMonth(monthListBox.getSelectedId()+1);
        fp.setSelectedYear(yearListBox.getSelectedId());
       // fp.getSelectedDay(new Date().getDate());// change
        fp.setLimit(10);
        fp.setSortField(BackendManagementListItem.Expire_Date);
        fp.setParams("FROM_CHART");

        backendService.getExpiringCompanyRatio(fp, new AsyncCallback<LinkedHashMap<String, String>>() {
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
        return "EXPIRING_COMPANY";
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
        title2.setInnerText(wfmStrings.expiryDate());

        iterationTr.appendChild(title1);
        iterationTr.appendChild(title2);
        tHead.appendChild(iterationTr);
        table.appendChild(tHead);

        Element tBody = DOM.createTBody();
        Double[] companyDiff = new Double[2];

        Set<String> keys = result.keySet();
        for(String key : keys) {
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
