package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * User: Ilhombek
 * Date: 21.07.2010
 * Time: 19:50:55
 */
public interface EmailTemplateServiceAsync {
    void generateEmailTemplate(EntityToEmailTemplate emailTemplate, AsyncCallback<EmailTemplateItem> callback);

    void generateEmailTemplateForBalance(EntityToEmailTemplate emailTemplate, AsyncCallback<EmailTemplateItem> callback);

    void generateReplyToReporterCaseItem(EntityToEmailTemplate emailTemplate, Integer autoResponseID, AsyncCallback<EmailTemplateItem> callback);

    void generateEmailTemplateData(EntityToEmailTemplate entityToEmailTemplate, Integer senderID, AsyncCallback<EmailTemplateItem> callback);

    void generateEmailTemplateForAccountingComposeView(EntityToEmailTemplate entityToEmailTemplate, Integer senderID, AsyncCallback<EmailTemplateItem> callback);

    void generateExpenseClaimTemplateItem(EntityToEmailTemplate entityToEmailTemplate, AsyncCallback<EmailTemplateItem> callback);

    void generateBatchPaymentTemplateItem(EntityToEmailTemplate entityToEmailTemplate, AsyncCallback<EmailTemplateItem> callback);

    void generateMessageCenterTemplateItem(EntityToEmailTemplate entityToEmailTemplate, Integer rfqId, Integer employeeId, Integer opportunityId, AsyncCallback<EmailTemplateItem> callback);

    void getEmailTemplates(String templateModule, AsyncCallback<SelectItem[]> async);

    void getMessageCenterEmailTemplates(ArrayList<String> templateModules, AsyncCallback<SelectItem[]> async);

    void getReplyToById(Integer id, AsyncCallback<String> async);

    void getEmailTemplateItemForRFP(EntityToEmailTemplate item, String rfpStatus, AsyncCallback<EmailTemplateItem> callback);
}
