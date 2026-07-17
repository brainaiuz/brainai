package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SalaryHistoryServiceAsync {

    void list(ListingFilterParameter filterParameter, AsyncCallback<ListResult<SalaryHistory>> async);

    void get(Integer id, AsyncCallback<SalaryHistory> async);

    void save(SalaryHistory salaryHistory, AsyncCallback<Integer> async);

    void delete(Integer id, AsyncCallback<Boolean> async);
}
