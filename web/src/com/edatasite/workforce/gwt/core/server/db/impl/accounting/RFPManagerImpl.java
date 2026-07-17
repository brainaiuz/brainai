package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsRFP;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFPManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPData;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/8/13
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("rfpManager")
public class RFPManagerImpl extends BaseManager<EdsRFP> implements RFPManager {
    public RFPManagerImpl() {
        super(EdsRFP.class);
    }

    @Override
    public Integer getRfpLastIntNumber() {
        return (Integer) findSingle("select rfp.intNumber from EdsRFP rfp where " + ServerUtils.checkForDeleted("rfp.deleted") + " order by rfp.intNumber desc");
    }


    public List<EdsRFP> getEdsRFPList(ListingFilterParameter fp, boolean isTotalQuery) {
        EdsUser user = getUser();
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("SELECT rfp.* from ").append(companyID).append(".RFP rfp \n");
        sql.append("left join ").append(companyID).append(".myuser myc on myc.id = rfp.creatorid \n");
        sql.append("left join ").append(companyID).append(".approvers appr on rfp.currentApprover = appr.id \n");
        sql.append("left join ").append(companyID).append(".myuser mym on appr.exactApprover = mym.id \n");
        sql.append("left join ").append(companyID).append(".reference ref on ref.id = rfp.overallstatus \n");
        sql.append("left join ").append(companyID).append(".project p on p.id = rfp.projectid \n");
        sql.append("left join ").append(companyID).append(".crmAccount client on client.id = rfp.client_id \n");

        sql.append("where ").append(ServerUtils.checkForDeleted("rfp.deleted "));
        if (fp.getProjectId() != null) {
            sql.append(" \n and (p.id =").append(fp.getProjectId()).append(" or p.parnetId=").append(fp.getProjectId()).append(" )");
        }
        if (!ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS)) {
            if (ServerUtils.hasPermission(PermissionConstants.RFP_SEE_OWN) && !user.hasRole(EdsRole.ADMIN_CODE)) {
                sql.append(" \n and (");
                sql.append(" p.managerid = ").append(user.getObjectID()).append(" or \n");
                sql.append(" rfp.creatorid = ").append(user.getObjectID()).append(" or \n");
                sql.append(" client.id in (select co.crmaccount_id from ").append(getCompanyId()).append(".crmaccount_owners co where co.owner_id = ").append(user.getObjectID()).append(")) \n");
            } else {
                sql.append(" and (p.managerid = ").append(user.getObjectID()).append(" or rfp.creatorid = ").append(user.getObjectID()).append(") \n");
            }
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (");
            sql.append("lower(myc.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(myc.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(mym.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(client.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(mym.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(p.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(rfp.number) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(ref.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") ");
        }
        if (fp.getObjectsIds() != null) {
            sql.append(" and (");
            sql.append("rfp.id in (" + fp.getObjectsIds() + ")");
        }

        if (isTotalQuery) {
            return findNative(sql.toString(), EdsRFP.class);
        }

        if (fp.getSortField() != null) {
            sql.append(" order by ");
            if (RFPData.NUMBER.equals(fp.getSortField())) {
                sql.append("rfp.number ");
            } else if (RFPData.RELATED_PROJECT.equals(fp.getSortField())) {
                sql.append("p.name ");
            } else if ("employee".equals(fp.getSortField())) {
                if (fp.isAscending()) {
                    sql.append(" myc.firstname, myc.lastname ");
                } else {
                    sql.append(" myc.firstname desc, myc.lastname ");
                }
            } else if (RFPData.MANAGER.equals(fp.getSortField())) {
                if (fp.isAscending()) {
                    sql.append(" mym.firstname, mym.lastname ");
                } else {
                    sql.append(" mym.firstname desc, mym.lastname ");
                }
            } else if (RFPData.CUSTOMER.equals(fp.getSortField())) {
                sql.append(" rfp.client_id ");
            } else if (RFPData.DUE_DATE.equals(fp.getSortField())) {
                sql.append(" rfp.duedate ");
            } else if ("status".equals(fp.getSortField())) {
                sql.append(" ref.name ");
            } else {
                sql.append(" rfp.objectID ");
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sql.append(" asc");
                } else {
                    sql.append(" desc");
                }
            } else {
                sql.append(" desc");
            }
        } else {
            sql.append("order by rfp.id desc");
        }
        if (fp.getLimit() > 0) {
            sql.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        }

        return findNative(sql.toString(), EdsRFP.class);
    }

    @Override
    public void deleteRFPItems(Integer rfpID) {
        update("delete from EdsRFPItem where rfp.objectID = ?", rfpID);
    }

    @Override
    public Map<Integer, BigDecimal> getRequestedRFPItems(List<Integer> ids) {
        List<Object[]> objects = findByNamedParams("select item.entityID, item.qty from EdsRFPItem item join item.rfp r where r.overallStatus.code='APPROVE' " +
                        "and item.entityID in (:ids) and " + ServerUtils.checkForDeleted("r.deleted "),
                preparing(new Entry("ids", ids)));

        return objects.stream()
                .collect(Collectors
                        .toMap(obj -> (Integer) obj[0], obj -> (BigDecimal) obj[1], BigDecimal::add));
    }

    @Override
    public Map<Integer, BigDecimal> getRemainingQtys(List<Integer> ids) {
        StringBuffer sql = new StringBuffer();
        String companyID = getCompanyId();
        sql.append("select r.entityid, b.qty-sum(r.qty) as remaining from ").append(companyID).append(".rfpitem r ");
        sql.append("left join ").append(companyID).append(".billofmaterial b on b.id = r.entityid ");
        sql.append("left join ").append(companyID).append(".rfp rfp on rfp.id=r.rfpid ");
        sql.append("left join ").append(companyID).append(".reference ref on ref.id=rfp.overallStatus ");
        sql.append("where r.entityid in (:ids) and ref.code='APPROVE' and ").append(ServerUtils.checkForDeleted("rfp.deleted "));
        sql.append("group by r.entityid, b.qty");

        List<Object[]> objects = findNativeByNamedParams(sql.toString(), preparing(new Entry("ids", ids)));

        return objects.stream()
                .collect(Collectors
                        .toMap(obj -> (Integer) obj[0], obj -> (BigDecimal) obj[1]));
    }

    @Override
    public boolean isRFPNumberExist(String number, Integer objectID) {
        List numberList;
        if (objectID != null) {
            numberList = find("select rfp.intNumber from EdsRFP rfp where " + ServerUtils.checkForDeleted("rfp.deleted") + " and rfp.number= ? and rfp.objectID <>? ", number, objectID);
        } else {
            numberList = find("select rfp.intNumber from EdsRFP rfp where " + ServerUtils.checkForDeleted("rfp.deleted") + " and rfp.number= ?", number);
        }
        return numberList != null && numberList.size() > 0 ;
    }
}
