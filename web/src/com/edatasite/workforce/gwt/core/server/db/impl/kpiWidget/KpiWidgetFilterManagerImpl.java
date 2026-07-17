package com.edatasite.workforce.gwt.core.server.db.impl.kpiWidget;

import com.edatasite.workforce.core.domain.reporting.EdsKpiWidgetFilter;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.kpiWidget.KpiWidgetFilterManager;
import org.springframework.stereotype.Repository;

@Repository("kpiWidgetFilterManager")
public class KpiWidgetFilterManagerImpl extends BaseManager<EdsKpiWidgetFilter> implements KpiWidgetFilterManager {

    public KpiWidgetFilterManagerImpl() {
        super(EdsKpiWidgetFilter.class);
    }

    @Override
    public EdsKpiWidgetFilter getKpiWidetFilterByType(Integer kpiWidgetID, int filterType) {
        return (EdsKpiWidgetFilter) findSingle("select ab from EdsKpiWidgetFilter ab where ab.kpiWidgetId.objectID = ? and ab.filterType = ? ", kpiWidgetID, filterType);
    }
}
