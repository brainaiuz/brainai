package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsTCScheduledTask;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.TCScheduledTaskManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCScheduleItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/6/12
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("tcScheduledTaskManager")
public class TCScheduledTaskManagerImpl extends BaseManager<EdsTCScheduledTask> implements TCScheduledTaskManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public TCScheduledTaskManagerImpl() {
        super(EdsTCScheduledTask.class);
    }

    @Override
    public EdsTCScheduledTask getLastPendingScheduledTask() {
        return (EdsTCScheduledTask) findSingle("select st from EdsTCScheduledTask st where st.status = ? ORDER BY st.objectID", EdsTCScheduledTask.STATUS_PENDING);
    }

    @Override
    public EdsTCScheduledTask getLastPDFGeneratedScheduledTask() {
        return (EdsTCScheduledTask) findSingle("select st from EdsTCScheduledTask st where (st.status = ? or st.status = ?) ORDER BY st.objectID", EdsTCScheduledTask.STATUS_PDF_GENERATED, EdsTCScheduledTask.STATUS_ZIP_IN_PROGRESS);
    }

    @Override
    public EdsTCScheduledTask getLastZippedScheduledTask() {
        return (EdsTCScheduledTask) findSingle("select st from EdsTCScheduledTask st where st.status = ? ORDER BY st.objectID", EdsTCScheduledTask.STATUS_ZIPPED);
    }

    public List<Integer> getLocationsByScheduledTask(Integer scheduledTaskID) {
        return find("select distinct scti.locationID from EdsTCScheduledTaskItem scti where scti.scheduledTask.objectID = ? order by scti.locationID", scheduledTaskID);
    }

    @Override
    public List<TCScheduleItem> getInvoiceSummaryReportData(Integer scheduledTaskID, Integer locationID) {
        List<Integer> invoiceIDs;
        if (locationID != null) {
            invoiceIDs = find("select scti.invoiceID from EdsTCScheduledTaskItem scti where scti.scheduledTask.objectID = ? and scti.locationID = ?", scheduledTaskID, locationID);
        } else {
            invoiceIDs = find("select scti.invoiceID from EdsTCScheduledTaskItem scti where scti.scheduledTask.objectID = ?", scheduledTaskID);
        }

        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select si.id as objectID, i.number as number, i.invoicedate as date, i.total as amount from " + companyID + ".saleinvoice si ");
        sql.append("inner join " + companyID + ".invoice i on i.id=si.id ");
        sql.append("where " + ServerUtils.checkForDeleted("i.deleted"));
        sql.append(" and i.id in (" + ServerUtils.getAsCommoDelimited(invoiceIDs, "0") + ") ");
        sql.append(" order by i.number ");

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(TCScheduleItem.class));
    }
}
