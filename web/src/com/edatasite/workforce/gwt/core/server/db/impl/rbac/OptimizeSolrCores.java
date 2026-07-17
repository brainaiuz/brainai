package com.edatasite.workforce.gwt.core.server.db.impl.rbac;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.mail.EdsTemplates;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid's
 * Date: 01-Oct-2010
 * Time: 15:42:10
 */
public class OptimizeSolrCores extends QuartzJobBean {

    final MessageManager messageManager = (MessageManager) ApplicationContextProvider.applicationContext.getBean("messageManager");

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ServerSecurityContext.getInstance().setDatabase(SpringPropertiesUtil.getProperty("bg_databaseType"));
        Exception messageException = null;
        String[] solrCores = new String[]{Constants.SOLR_TASK_CORE, Constants.SOLR_PROJECT_CORE, Constants.SOLR_PURCHASE_ORDER_CORE,
                Constants.SOLR_FOLDER_CORE, Constants.SOLR_CONTACT_CORE, Constants.SOLR_CASE_CORE, Constants.SOLR_CRM_ACCOUNT_CORE,
                Constants.SOLR_SALEINVOICE_CORE, Constants.SOLR_SALEQUOTE_CORE, Constants.SOLR_NEWS_CORE, /*Constants.SOLR_NETWORK_CORE,*/
                /*Constants.SOLR_NETWORK_NEWS_CORE,*/ Constants.SOLR_OPPORTUNITY_CORE, Constants.SOLR_EVENT_CORE, Constants.SOLR_PRODUCTS_SERVICES_CORE,
                Constants.SOLR_PURCHASE_INVOICE_CORE, Constants.SOLR_EXPENSE_REPORT_CLAIMS_CORE, Constants.SOLR_SINGLE_PAYRUN_CORE,
                Constants.SOLR_GROUP_PAYRUN_CORE, Constants.SOLR_CASH_ADVANCE_CORE};

        final Map<String, Object> values = new HashMap<>();
        try {
            System.out.print("<------------------------------------- Starting optimization for solr " + new Date() + " ------------------------------------->");
            for (String core : solrCores) {
                SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(core);
                if (solrServer != null) {
                    UpdateResponse coreResp = solrServer.optimize(true, false);
                    values.put(core, "Elapsed time for " + coreResp.getElapsedTime() + " secs .QTime  " + coreResp.getQTime() + " secs");
                }
            }
            System.out.print("<------------------------------------- Ending optimization for solr " + new Date() + " ------------------------------------->");
        } catch (SolrServerException | IOException e) {
            messageException = e;
            e.printStackTrace();
        } finally {
            try {
                try {
                    if (messageException == null) {
                        System.out.print("Ending optimization for solr");
                        values.put("date", new Date());
                        final String updatedMessage = EdsTemplates.processTemplate(values, EdsTemplates.SOLR_OPTIMIZE);
                        messageManager.sendMessageFromUser(null, "alert@workforcetrack.com", null, null, "Solr Optimization Completed", updatedMessage, false, null, null, false, null, null, null);
                    } else {
                        messageManager.sendMessageFromUser(null, "alert@workforcetrack.com", null, null, "Solr Optimization Failed", messageException.getMessage(), false, null, null, false, null, null, null);
                    }
                } catch (EdsDbException e) {
                    e.printStackTrace();
                }
            } catch (EdsTemplateException e) {
                e.printStackTrace();
            }
        }

    }
}
