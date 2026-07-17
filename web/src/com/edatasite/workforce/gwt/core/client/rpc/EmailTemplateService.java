package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * User: Ilhombek
 * Date: 21.07.2010
 * Time: 19:50:54
 */
public interface EmailTemplateService extends RemoteService {

    EmailTemplateItem generateEmailTemplate(EntityToEmailTemplate emailTemplate);

    EmailTemplateItem generateReplyToReporterCaseItem(EntityToEmailTemplate emailTemplate, Integer autoResponseID);

    EmailTemplateItem generateEmailTemplateData(EntityToEmailTemplate entityToEmailTemplate, Integer senderID);

    EmailTemplateItem generateEmailTemplateForAccountingComposeView(EntityToEmailTemplate entityToEmailTemplate, Integer senderID);

    EmailTemplateItem generateExpenseClaimTemplateItem(EntityToEmailTemplate entityToEmailTemplate);

    EmailTemplateItem generateBatchPaymentTemplateItem(EntityToEmailTemplate entityToEmailTemplate);

    EmailTemplateItem generateMessageCenterTemplateItem(EntityToEmailTemplate entityToEmailTemplate, Integer rfqId, Integer employeeId, Integer opportunityId);

    SelectItem[] getMessageCenterEmailTemplates(ArrayList<String> templateModules);

    SelectItem[] getEmailTemplates(String templateCategory);

    String getReplyToById(Integer id);

    EmailTemplateItem getEmailTemplateItemForRFP(EntityToEmailTemplate item, String rfpStatus);

    EmailTemplateItem generateEmailTemplateForBalance(EntityToEmailTemplate emailTemplate);

    /**
     * Utility/Convenience class.
     * Use EmailTemplateService.App.get() to access static instance of EmailTemplateServiceAsync
     */
    class App {
        public static EmailTemplateServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/emailTemplate");
            return (EmailTemplateServiceAsync) target;
        }
    }
}
