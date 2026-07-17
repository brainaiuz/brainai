package com.edatasite.workforce.gwt.employee.client.ui.filter;


import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.ReportServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Hurshid
 * Date: 5/29/14
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListingPopupFilter extends KpiModal {

    //public final static long BY_EMPLOYEE_LIST = DEPARTMENT_LIST + STATUS_LIST + LOCATION_LIST + POSITION_LIST + DATE;

    private ReportServiceAsync reportService = ReportService.App.get();
    

    private ListingFilterParameter fp;
    private boolean reset = false;

    private DataListBox department;
    private DataListBox status;
    private DataListBox location;
    private DataListBox position;
    private DatePicker fromDate;
    private DatePicker toDate;

    private String baseWidth = "200px";

    public ListingPopupFilter() {
        setSize("600px", "400px");
        setScrollable(true);
        getScrollPanel().getElement().getStyle().setOverflowX(Style.Overflow.HIDDEN);
        asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(Throwable reason) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.failedToDownloadCodeForThisWidget() + " (" + reason + ")", Info.Type.WARNING);
            }

            public void success(Widget result) {
                LoadingPanel.loading(false);
            }
        });
//        onInitialize();
    }

    private Widget onInitialize() {
        department = new DataListBox();
        department.setWidth(baseWidth);
        department.setNullLabel(wfmStrings.all());
        department.setAllowFirstItem(true);
        department.addValueChangeHandler(arg0 -> reset = false);

        status = new DataListBox();
        status.setWidth(baseWidth);
        status.setAllowFirstItem(true);
        status.setNullLabel(wfmStrings.all());
        status.addValueChangeHandler(event -> reset = false);
        location = new DataListBox();
        location.setWidth(baseWidth);
        location.setAllowFirstItem(true);
        location.setNullLabel(wfmStrings.all());
        location.addValueChangeHandler(event -> reset = false);

        position = new DataListBox();
        position.setWidth(baseWidth);
        position.setAllowFirstItem(true);
        position.setNullLabel(wfmStrings.all());
        position.addValueChangeHandler(event -> reset = false);

        fromDate = new DatePicker();
        fromDate.setDefaultValue();

        toDate = new DatePicker();
        toDate.setDefaultValue();

        fromDate.setWidth(baseWidth);
        toDate.setWidth(baseWidth);

        return null;
    }

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
