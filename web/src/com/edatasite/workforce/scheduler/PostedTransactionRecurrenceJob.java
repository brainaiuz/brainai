package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

/**
 * Created by Normurod on 4/13/15.
 */
public class PostedTransactionRecurrenceJob extends BaseRecurrenceJob {

    private FinancialSettingsManager financialSettingsManager = (FinancialSettingsManager) ApplicationContextProvider.applicationContext.getBean("financialSettingsManager");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        getLogger().info("PostedTransactionRecurrenceJob started: " + new Date());
        List<EdsCompany> companyList = companyManager.getOccupiedCompanies();
        List<String> schemas = companyManager.getExistingSchemas();

        for (EdsCompany company : companyList) {
            if (company.hasSchema(schemas) && company.getActive() != null && company.getActive()) {
                if (company.getCountryZone() != null) {
                    getLogger().info("PostedTransactionRecurrenceJob company: " + company.getObjectID());
                    setCompanyAndDatabase(company.getObjectID());
                    try {
                        EdsFinancialSettings settings = financialSettingsManager.getSettingsByCompany(company.getObjectID());

                        if (settings != null && settings.isEnablePostedDateTransaction()) {
                            invoiceServiceLocal.runPostDatedTransactions(company.getObjectID());
                            accountingServiceLocal.runSpendReceivePostDatedTransactions(company.getObjectID());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        getLogger().info("PostedTransactionRecurrenceJob ended: " + new Date());
    }
}
