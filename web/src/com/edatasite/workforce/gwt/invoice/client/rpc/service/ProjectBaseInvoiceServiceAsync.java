package com.edatasite.workforce.gwt.invoice.client.rpc.service;

import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByAssigneeEntry;
import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByProjectEntry;
import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByTaskEntry;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseInvoiceItem;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 12.05.2009
 * Time: 17:07:15
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectBaseInvoiceServiceAsync {

    Request getClientRelatedProjectsForPBI(Integer clientID, AsyncCallback<ProjectBaseInvoiceItem[]> async);

    void getDetailedInvoice(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled, Integer crmAccountId, AsyncCallback<ProjectBaseData[]> async);

    void getGroupedByAssignee(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled, AsyncCallback<ArrayList<GroupByAssigneeEntry>> async);

    void getGroupedByTask(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled, AsyncCallback<ArrayList<GroupByTaskEntry>> async);

    void getGroupedByProject(Integer[] projects, DateNonConvertable from, DateNonConvertable to, boolean isMonthlyTimeSheetEnabled, AsyncCallback<ArrayList<GroupByProjectEntry>> async);

    void getGroupedByProjectFE(Integer employeeId, Integer[] projects, DateNonConvertable from, DateNonConvertable to, AsyncCallback<ArrayList<GroupByProjectEntry>> async);
}
