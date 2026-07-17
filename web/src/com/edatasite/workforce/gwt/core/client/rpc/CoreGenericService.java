package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Created by Normurod on 1/20/16.
 */
public interface CoreGenericService extends RemoteService,
        AllInOneService,
        CommonService,
        ClockWidgetService,
        BugReportService,
        EmailTemplateService,
        EventServiceRemoteService,
        LoginService,
        NotificationMsgService,
        RbacService,
        ReportService,
        RolePermissionService,
        SmsSenderService,
        StatusService,
        CurrencyService,
        MessageCenterService,
        TelegramChatService,
        WhatsAppService,
        ModuleDashboardService,
        SalaryHistoryService,
        AiReportService {
}
