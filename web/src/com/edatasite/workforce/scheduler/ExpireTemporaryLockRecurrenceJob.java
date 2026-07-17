package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.trainingcenter.server.TCServiceLocal;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/16/12
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ExpireTemporaryLockRecurrenceJob extends BaseRecurrenceJob {
    private ModuleManager moduleManager = (ModuleManager) ApplicationContextProvider.applicationContext.getBean("moduleManager");
    private TCServiceLocal tcService = (TCServiceLocal) ApplicationContextProvider.applicationContext.getBean("tcService");

    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        getLogger().info("ExpireTemporaryLockRecurrenceJob started");
        List<EdsCompany> companyList = companyManager.getOccupiedCompanies();
        List<String> schemas = companyManager.getExistingSchemas();

        for (EdsCompany company : companyList) {
            try {
                if (company.hasSchema(schemas) && company.getActive() != null && company.getActive()) {
					setCompanyAndDatabase(company.getObjectID());
                    EdsModule trainingCenterEnabled = moduleManager.getModuleByCode(PermissionConstants.TRAINING_CENTER);
                    if (trainingCenterEnabled != null) {
                        tcService.expireTemporaryLocks(company.getObjectID());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getLogger().info("ExpireTemporaryLockRecurrenceJob ended");
    }
}
