package com.finnetlimited.reportservice.core.server.db.schema;

import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsTelegramReportingScheduleRule;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Sardorbek Khalimboev
 * Date: 11-June-2021
 * Time: 15:22:50
 */
public interface TelegramReportingRecurrenceManager extends Manager<EdsTelegramReportingScheduleRule> {

    ArrayList<String> getAllRuleNames(Integer reportId);

    List<EdsTelegramReportingScheduleRule> getAllRules(Integer id);

    EdsTelegramReportingScheduleRule getRuleByReportIdAndName(Integer reportId, String name);

    EdsTelegramReportingScheduleRule getByRecurrenceId(int recurrenceId);
}
