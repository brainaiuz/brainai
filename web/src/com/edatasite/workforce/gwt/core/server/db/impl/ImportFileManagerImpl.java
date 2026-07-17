/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/26 7:31:0                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ImportFileManager;
import com.edatasite.workforce.gwt.profile.client.rpc.ImportLogItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 08-Jul-2009
 * Time: 18:37:22
 * To change this template use File | Settings | File Templates.
 */
@Repository("importFileManager")
public class ImportFileManagerImpl extends BaseManager<EdsImportFile> implements ImportFileManager {

    public ImportFileManagerImpl() {
        super(EdsImportFile.class);
    }

    @Override
    public EdsImportFile getQueueByUser(EdsUser user, String fromView, ImportTypeEnum type) {
        if (user == null || user.getCompany() == null || ServerUtils.isNullOrEmpty(fromView) || type == null) {
            return null;
        }
        final String sql = "SELECT DISTINCT on (fi.id) fi.* FROM \"" + user.getCompany().getObjectID() + "\".importFile fi " +
                "  JOIN " + getPublic() + ".businessevent be ON fi.id = be.entityid and be.companyId = :companyID " +
                "      WHERE owner_id = :userId " +
                "      AND fi.type = :typeEnum " +
                "      AND fi.status = :statusCode " +
                "      AND fi.exceptionThrowed IS FALSE " +
                "      AND date_trunc('day', fi.createdDate) = date_trunc('day', current_timestamp) " +
                "      AND be.processorName = :processorName " +
                "      AND be.eventtype = :fromView " +
                "      AND (be.status <> 'FAIL' " +
                "           OR be.processed IS NULL " +
                "           OR be.processed IS FALSE) ";

        final List<EdsImportFile> result = slaveEntityManager.createNativeQuery(sql, EdsImportFile.class)
                .setParameter("companyID", user.getCompany().getObjectID())
                .setParameter("userId", user.getObjectID())
                .setParameter("statusCode", "IN_PROCESS")
                .setParameter("fromView", fromView)
                .setParameter("typeEnum", type.name())
                .setParameter("processorName", "importFileCustomEventListenerString")
                .setMaxResults(1)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<EdsImportFile> getImportEventList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select i.* ").append(getBaseSql(fp));
        sql.append(" order by ");
        if (ImportLogItem.DATE.equals(fp.getSortField())) {
            sql.append(" i.createdDate ");
        } else if (ImportLogItem.STATUS.equals(fp.getSortField())) {
            sql.append(" i.status ");
        } else {
            sql.append(" i.id desc");
        }
        if (!fp.isAscending()) {
            sql.append(" desc ");
        }
        if (fp.getLimit() > 0) {
            sql.append(" limit ").append(fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" offset ").append(fp.getStart());
        }

        return findNative(sql.toString(), EdsImportFile.class);
    }

    @Override
    public Integer getImportEventsCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(i.id) ").append(getBaseSql(fp));
        return ((BigInteger) findNativeSingle(sql.toString())).intValue();
    }

    @Override
    public ArrayList<EdsImportFile> getImportFileStatusByType(String type, String status) {
        StringBuilder query = new StringBuilder()
                .append("select * from ").append(getCompanyId())
                .append(".importFile where type = '").append(type)
                .append("' and status = '").append(status).append("'")
                .append("  and deleted is not true");
        return  (ArrayList<EdsImportFile>) findNative(query.toString(), EdsImportFile.class);
    }

    private String getBaseSql(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append(" from ").append(getCompanyId()).append(".importFile i ");
        sql.append(" where i.deleted is not true");
        if (StringUtils.isNotBlank(fp.getDataType())) {
            sql.append(" and i.type = '").append(ImportTypeEnum.valueOf(fp.getDataType())).append("'");
        }
        if (fp.getFromDate() != 0 && fp.getToDate() != 0) {
            sql.append(" and (");
            sql.append(" i.createdDate >=").append("'").append(ServerUtils.getStartDate(new Date(fp.getFromDate()))).append("'");
            sql.append(" and i.createdDate <=").append("'").append(ServerUtils.getEndDate(new Date(fp.getToDate()))).append("'");
            sql.append(")");
        }
        return sql.toString();
    }
}
