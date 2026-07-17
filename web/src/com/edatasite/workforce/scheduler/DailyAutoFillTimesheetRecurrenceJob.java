package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.timesheet.server.app.TimesheetServiceLocal;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

/**
 * Created by FARRUH on 7/27/2016.
 */
public class DailyAutoFillTimesheetRecurrenceJob extends BaseRecurrenceJob  {

    private NumberingSettingsManager numberingSettingsManager = (NumberingSettingsManager) ApplicationContextProvider.applicationContext.getBean("numberingSettingsManager");
    private TimesheetServiceLocal timesheetServiceLocal = (TimesheetServiceLocal) ApplicationContextProvider.applicationContext.getBean("timesheetService");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        getLogger().info("DailyAutoFillTimesheetRecurrenceJob started: " + new Date());
        List<EdsCompany> companyList = companyManager.getOccupiedCompanies();
        List<String> schemas = companyManager.getExistingSchemas();

        for (EdsCompany company : companyList) {
            try {
                if (company.hasSchema(schemas) && company.getActive() != null && company.getActive()) {
                    if (company.getCountryZone() != null) {
                        setCompanyAndDatabase(company.getObjectID());
                        EdsNumberingSettings numberingSetting = numberingSettingsManager.getNumberingSetting();

                        if (numberingSetting != null && numberingSetting.getDailyFillTimesheetFromResUtilRequired()) {
                            //Daily Fell Timesheet
                            timesheetServiceLocal.fillTimesheetFromResUtil(company.getObjectID());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getLogger().info("DailyAutoFillTimesheetRecurrenceJob ended: " + new Date());
    }
}
