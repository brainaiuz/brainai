package com.finnetlimited.reportservice.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.RelatedFilterOptions;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Mar 12, 2011
 * Time: 3:43:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilterLookUp extends LookUp {

    private ReportRpc report;
    private ColumnRpc column;
    private Integer sett;

    public FilterLookUp(ReportRpc report) {
        this.report = report;
        this.getSuggestBox().setWidth("140px");
    }

    public ColumnRpc getColumn() {
        return column;
    }

    public void setColumn(ColumnRpc column) {
        this.column = column;
    }

    public void setReport(ReportRpc report) {
        this.report = report;
    }

    public void setSett(Integer sett) {
        this.sett = sett;
    }

    public Integer getSett() {
        return this.sett;
    }

    private void updateReportFilter() {

        RelatedFilterOptions options = report.getRelatedFilters().get(sett);
        if (options == null) {
            options = new RelatedFilterOptions();
        }

        if (!(options.getBoolTypes().size() == 1 && !"And".equals(options.getBoolTypes().get(0)))) {

            report.setBoolType(options.getBoolTypes());
            report.setFieldd(options.getFields());
            report.setValues(options.getValues());
            report.setOperators(options.getOperators());
            report.setSett(options.getSets());
        } else {
            report.clearBoolType();
            report.getFieldd().clear();
            report.getValues().clear();
            report.getOperators().clear();
            report.getSett().clear();
        }
    }


    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        if (column != null) {
            String searchKey = filterParametrs.getSearchKey();
            updateReportFilter();
            CoreService.App.get().getFilterSelectItems(searchKey, report, column, new AsyncCallback<LinkedList<SelectItem>>() {

                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(LinkedList<SelectItem> result) {
                    setItems(filterParametrs.getSearchKey(), result.toArray(new SelectItem[]{}));
                    String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                    getSuggestBox().showSuggestions(searchKey);
                }
            });
        }
    }
}
