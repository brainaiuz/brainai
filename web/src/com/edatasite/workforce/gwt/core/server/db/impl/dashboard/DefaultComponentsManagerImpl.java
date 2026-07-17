package com.edatasite.workforce.gwt.core.server.db.impl.dashboard;

import com.edatasite.workforce.core.domain.dashboard.EdsDefaultComponents;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.server.db.dashboard.DefaultComponentsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 11.04.2018 0:16
 */
@Repository("defaultComponentsManager")
public class DefaultComponentsManagerImpl extends BaseManager<EdsDefaultComponents> implements DefaultComponentsManager {

    public DefaultComponentsManagerImpl() {
        super(EdsDefaultComponents.class);
    }

    @Override
    public List<EdsDefaultComponents> getListByModuleAndComponentCodes(ModuleEnum moduleEnum, ArrayList<String> componentCodes) {
        boolean hasCodes = componentCodes != null && componentCodes.size() > 0;
        String sqlQuery = "select dc from EdsDefaultComponents dc " +
                          " where dc.modules like :module ";
        if (hasCodes) {
            sqlQuery += " and dc.componentCode not in (:componentCodes) ";
        }
        sqlQuery += " order by dc.componentName";
        TypedQuery<EdsDefaultComponents> query = slaveEntityManager.createQuery(sqlQuery, EdsDefaultComponents.class)
                                                              .setParameter("module", "%" + moduleEnum.name() + "%");
        if (hasCodes) {
            query.setParameter("componentCodes", componentCodes);
        }
        return query.getResultList();
    }

    @Override
    public EdsDefaultComponents getItemByCode(String code) {
        if (code != null) {
            String sql = "select dc from EdsDefaultComponents dc where dc.componentCode =?";
            return (EdsDefaultComponents) findSingle(sql, code);
        }
        return null;
    }

    @Override
    public EdsDefaultComponents getByReportId(Integer reportId, Integer reportWidgetId) {
        if (reportWidgetId == null) {
            return (EdsDefaultComponents) findSingle("SELECT dc FROM EdsDefaultComponents dc WHERE dc.report.objectID = ? and kpiWidget is null", reportId);
        } else {
            return (EdsDefaultComponents) findSingle("SELECT dc FROM EdsDefaultComponents dc WHERE dc.report.objectID = ? and kpiWidget.objectID =? ", reportId, reportWidgetId);
        }
    }

    @Override
    public void deleteComponentByReportId(Integer reportId, Integer reportWidgetId) {

        if (reportId == null) {
            return;
        }

        EdsDefaultComponents reportComponent;
        if (reportWidgetId == null) {
            reportComponent = (EdsDefaultComponents) findSingle("SELECT dc FROM EdsDefaultComponents dc WHERE dc.report.objectID = ? and kpiWidget is null ", reportId);
        } else {
            reportComponent = (EdsDefaultComponents) findSingle("SELECT dc FROM EdsDefaultComponents dc WHERE dc.report.objectID = ? and kpiWidget.objectID =? ", reportId, reportWidgetId);
        }

        if (reportComponent != null) {
            //delete component from dashboard
            update("DELETE FROM EdsDashboardComponents dc WHERE dc.component.objectID = ?", reportComponent.getObjectID());

            //delete component from user dashboard settings
            update("DELETE FROM EdsUserDashboardSettings uds WHERE uds.component.objectID = ?", reportComponent.getObjectID());

            update("DELETE FROM EdsDefaultComponents dc WHERE dc.objectID = ?", reportComponent.getObjectID());
        }
    }

    @Override
    public Integer getCachingTimeByReportCode(String reportCode) {
        return (Integer) findNativeSingle("select caching_time from " + getCompanyId() + ".default_components where report_code = ' " + reportCode + "'");
    }
}
