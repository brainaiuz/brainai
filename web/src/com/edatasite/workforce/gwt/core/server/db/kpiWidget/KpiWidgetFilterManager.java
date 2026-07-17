package com.edatasite.workforce.gwt.core.server.db.kpiWidget;

import com.edatasite.workforce.core.domain.reporting.EdsKpiWidgetFilter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface KpiWidgetFilterManager extends Manager<EdsKpiWidgetFilter> {

    EdsKpiWidgetFilter getKpiWidetFilterByType(Integer kpiWidgetID, int filterType);
}
