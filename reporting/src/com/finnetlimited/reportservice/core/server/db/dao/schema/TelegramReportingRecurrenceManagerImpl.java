package com.finnetlimited.reportservice.core.server.db.dao.schema;

import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.finnetlimited.reportservice.core.server.db.schema.TelegramReportingRecurrenceManager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsTelegramReportingScheduleRule;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Sardorbek Khalimboev
 * Date: 11-June-2021
 * Time: 15:22:50
 */
@Repository("reportingRecurrenceManager")
public class TelegramReportingRecurrenceManagerImpl extends BaseManager<EdsTelegramReportingScheduleRule> implements TelegramReportingRecurrenceManager, PermissionConstants {
    public TelegramReportingRecurrenceManagerImpl() {
        super(EdsTelegramReportingScheduleRule.class);
    }

    @Override
    public ArrayList<String> getAllRuleNames(Integer reportId) {
        return (ArrayList<String>) find("select r.name from EdsTelegramReportingScheduleRule r where r.edsReport.objectID = ?", reportId);
    }

    @Override
    public List<EdsTelegramReportingScheduleRule> getAllRules(Integer reportId) {
        return (List<EdsTelegramReportingScheduleRule>) find("select r from EdsTelegramReportingScheduleRule r where r.edsReport.objectID = ?", reportId);
    }

    @Transactional
    @Override
    public boolean createOrUpdate(EdsTelegramReportingScheduleRule obj) {
        return super.createOrUpdate(obj);
    }

    @Override
    public EdsTelegramReportingScheduleRule getRuleByReportIdAndName(Integer reportId, String name) {
        return (EdsTelegramReportingScheduleRule) findSingle("select rule from EdsTelegramReportingScheduleRule rule " +
                "where rule.edsReport.objectID = ? " +
                "and rule.name = ? ", reportId, name);
    }

    @Override
    public EdsTelegramReportingScheduleRule getByRecurrenceId(int recurrenceId) {
        return (EdsTelegramReportingScheduleRule) findSingle("select rule from EdsTelegramReportingScheduleRule rule " +
                "where rule.recurrenceId = ? ", recurrenceId);
    }
}
