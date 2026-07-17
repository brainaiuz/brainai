package com.edatasite.workforce.gwt.invoice.client.rpc.service;

import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByAssigneeEntry;
import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByProjectEntry;
import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByTaskEntry;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseInvoiceItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 12.05.2009
 * Time: 17:06:37
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectBaseInvoiceService extends RemoteService {

    ProjectBaseInvoiceItem[] getClientRelatedProjectsForPBI(Integer clientID);

    ProjectBaseData[] getDetailedInvoice(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled, Integer crmAccountId);

    ArrayList<GroupByAssigneeEntry> getGroupedByAssignee(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled);

    ArrayList<GroupByTaskEntry> getGroupedByTask(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled);

    ArrayList<GroupByProjectEntry> getGroupedByProject(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled);

    ArrayList<GroupByProjectEntry> getGroupedByProjectFE(Integer employeeId, Integer[] projects, DateNonConvertable from, DateNonConvertable to);


    class App {
        public static ProjectBaseInvoiceServiceAsync get() {
            ServiceDefTarget target = GWT.create(ProjectBaseInvoiceService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/projectBaseInvoice");
            return (ProjectBaseInvoiceServiceAsync) target;
        }
    }
}
