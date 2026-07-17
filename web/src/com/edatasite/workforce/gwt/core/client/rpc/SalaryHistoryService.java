package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface SalaryHistoryService extends RemoteService {

    ListResult<SalaryHistory> list(ListingFilterParameter filterParameter);

    Integer save(SalaryHistory salaryHistory);

    SalaryHistory get(Integer id);

    Boolean delete(Integer id);

    class App {
        public static SalaryHistoryServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/salaryhistory");
            return (SalaryHistoryServiceAsync) target;
        }
    }
}
