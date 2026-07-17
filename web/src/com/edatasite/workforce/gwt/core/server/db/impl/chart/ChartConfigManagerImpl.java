package com.edatasite.workforce.gwt.core.server.db.impl.chart;

import com.edatasite.workforce.core.domain.reporting.EdsChartConfig;
import com.edatasite.workforce.gwt.core.server.db.chart.ChartConfigManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("chartConfigManager")
public class ChartConfigManagerImpl extends BaseManager<EdsChartConfig> implements ChartConfigManager {

    public ChartConfigManagerImpl() {
        super(EdsChartConfig.class);
    }
}
