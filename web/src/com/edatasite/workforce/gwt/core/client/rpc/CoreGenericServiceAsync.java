package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by Normurod on 1/20/16.
 */
public interface CoreGenericServiceAsync extends AllInOneServiceAsync,
        CommonServiceAsync,
        ClockWidgetServiceAsync,
        BugReportServiceAsync,
        EmailTemplateServiceAsync,
        EventServiceRemoteServiceAsync,
        LoginServiceAsync,
        NotificationMsgServiceAsync,
        RbacServiceAsync,
        ReportServiceAsync,
        RolePermissionServiceAsync,
        SmsSenderServiceAsync,
        StatusServiceAsync,
        CurrencyServiceAsync,
        MessageCenterServiceAsync,
        TelegramChatServiceAsync,
        WhatsAppServiceAsync,
        ModuleDashboardServiceAsync,
        SalaryHistoryServiceAsync,
        AiReportServiceAsync {
    @Override
    default void getWorkflowActivitiesList(ListingFilterParameter fp, AsyncCallback<ListResult<WorkflowRule>> asyncCallback) {

    }
}
