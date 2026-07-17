package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;

/**
 * Created by Virus on 9/14/14.
 */
public class ReportingLookUp extends LookUp {
    private String columnName = "";
    private boolean isBusy = false;
    private String lastSearchKey = "";
    private String sucsessedSearchKey = "";
    private ReportingStepControlView view;

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        lastSearchKey = filterParametrs.getSearchKey();
        if (!isBusy) {
            isBusy = true;
            sucsessedSearchKey = filterParametrs.getSearchKey();
            getData();
        }
    }

    private void getData() {
        ReportingService.App.get().getFilterSelectItems(sucsessedSearchKey, view.getReport().getColumnMap().get(columnName), view.getReport(), new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onSuccess(SelectItem[] result) {
                isBusy = false;
                setItems(sucsessedSearchKey, result);
                getSuggestBox().showSuggestions(sucsessedSearchKey == null ? "" : sucsessedSearchKey);
                if (lastSearchKey != null && !lastSearchKey.equals(sucsessedSearchKey)) {
                    boolean searchAgain = true;
                    for (SelectItem item : result) {
                        if (lastSearchKey.contains(item.getName().toLowerCase())) {
                            searchAgain = false;
                            break;
                        }
                    }
                    if (searchAgain) {
                        sucsessedSearchKey = lastSearchKey;
                        getData();
                    }
                }
            }
        });
    }

    public void setView(ReportingStepControlView view) {
        this.view = view;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean hasFocus() {
        return Utils.hasFocus(this.getElement())
                || Utils.hasFocus(this.getTextBox().getElement())
                || Utils.hasFocus(this.getSuggestBox().getElement());
    }
}
