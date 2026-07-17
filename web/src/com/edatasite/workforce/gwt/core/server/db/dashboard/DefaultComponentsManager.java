package com.edatasite.workforce.gwt.core.server.db.dashboard;

import com.edatasite.workforce.core.domain.dashboard.EdsDefaultComponents;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 11.04.2018 0:15
 */
public interface DefaultComponentsManager extends Manager<EdsDefaultComponents> {

    EdsDefaultComponents getItemByCode(String code);

    List<EdsDefaultComponents> getListByModuleAndComponentCodes(ModuleEnum moduleEnum, ArrayList<String> componentCodes);

    EdsDefaultComponents getByReportId(Integer reportId, Integer reportWidgetId);

    void deleteComponentByReportId(Integer reportId, Integer reportWidgetId);

    Integer getCachingTimeByReportCode(String reportCode);
}
