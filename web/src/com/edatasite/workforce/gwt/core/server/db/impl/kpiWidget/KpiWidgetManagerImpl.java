package com.edatasite.workforce.gwt.core.server.db.impl.kpiWidget;

import com.edatasite.workforce.core.domain.reporting.EdsKpiWidget;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.kpiWidget.KpiWidgetManager;
import org.springframework.stereotype.Repository;

@Repository("kpiWidgetManager")
public class KpiWidgetManagerImpl extends BaseManager<EdsKpiWidget> implements KpiWidgetManager {

    public KpiWidgetManagerImpl() {
        super(EdsKpiWidget.class);
    }
}
