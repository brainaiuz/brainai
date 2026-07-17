package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsContract;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsProjectPosition;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.ContractManager;
import com.edatasite.workforce.gwt.project.client.rpc.ContractListItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Date: 07.01.2008 Time: 15:27:54 To change
 * this template use File | Settings | File Templates.
 */
@Repository("contractManager")
public class ContractManagerImpl extends AttachmentSupportManager<EdsContract> implements ContractManager {

    public ContractManagerImpl() {
        super(EdsContract.class);
    }

    public void deleteContract(EdsContract contract) {
        update("update EdsContract c set c.deleted=true " +
                "where c=? and c.deleted<>true", contract);
    }

    @Override
    public List<EdsCrmAccount> getClientContract() {
        StringBuilder sql = new StringBuilder();
        sql.append("select ca.* from " + getCompanyId() + ".crmaccount ca ");
        sql.append("left join " + getCompanyId() + ".contract cnr on cnr.clientid = ca.id ");
        sql.append("where cnr.deleted <> true ");
        sql.append(" group by ca.id ");
        sql.append("order by ca.lastUpdateTime desc ");
        return findNative(sql.toString(), EdsCrmAccount.class);
    }

    @Override
    public List<Object[]> getList(ListingFilterParameter fp) {
        String sortDir = !fp.isAscending() ? " DESC " : " ";

        StringBuilder sql = new StringBuilder();
        sql.append("select cnr.id, ");
        sql.append("cnr.allowancebyclient,");
        sql.append("cl.name as client, ");
        sql.append("cnr.number, ");
        sql.append("pj.name as project, ");
        sql.append("cnr.startDate, ");
        sql.append("cnr.dueDate, ");
        sql.append("cnr.creationTime, ");
        sql.append("(select comment from ").append(getCompanyId()).append(".note where related_id = cnr.id and related_to = " + EdsNoteHistory.PM_CONTRACT + " order by id desc limit 1) as comment ");
        sql.append(getContractCoreSql(fp));
        if (StringUtils.isNotBlank(fp.getSortField())) {
            sql.append(" ORDER BY ");
            if (ContractListItem.CLIENT.equals(fp.getSortField())) {
                sql.append(" cl.name ");
            } else if (ContractListItem.NUMBER.equals(fp.getSortField())) {
                sql.append(" cnr.number ");
            } else if (ContractListItem.PROJECT.equals(fp.getSortField())) {
                sql.append(" pj.name ");
            } else if (ContractListItem.CONTRACT_START_DATE.equals(fp.getSortField())) {
                sql.append(" cnr.startDate ");
            } else if (ContractListItem.CONTRACT_END_DATE.equals(fp.getSortField())) {
                sql.append(" cnr.dueDate ");
            } else if (ContractListItem.CONTRACT_REGISTRATION_DATE.equals(fp.getSortField())) {
                sql.append(" cnr.creationTime ");
            } else if (ContractListItem.LAST_NOTE_COMMENT.equals(fp.getSortField())) {
                sql.append(" comment ").append(sortDir);
            } else {
                sql.append(" cnr.lastUpdateTime ");
            }
            sql.append(sortDir);
        } else {
            sql.append(" order by cnr.lastUpdateTime desc ");
        }
        if (fp.getLimit() > 0) {
            sql.append(" LIMIT ").append(fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" OFFSET ").append(fp.getStart());
        }
        return findNative(sql.toString());
    }

    @Override
    public Integer listCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(cnr.id) ");
        sql.append(getContractCoreSql(fp));
        return Integer.valueOf(findNativeSingle(sql.toString()).toString());
    }

    private StringBuilder getContractCoreSql(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append(" from ").append(getCompanyId()).append(".contract cnr ");
        sql.append(" left outer join ").append(getCompanyId()).append(".crmAccount cl on cnr.clientid =  cl.id ");
        sql.append(" left outer join ").append(getCompanyId()).append(".project pj on  cnr.projectid =  pj.id ");
        if ("case".equals(fp.getRelationType())) {
            sql.append(" left join ").append(getCompanyId()).append(".relation r on r.fromType = 'case' and r.toType = 'contract' and r.toID = cnr.id ");
            sql.append(" left join ").append(getCompanyId()).append(".crmCase ccase on ccase.id = r.fromID ");
        }
        sql.append(" where (cnr.deleted is null or cnr.deleted is not true) ");
        if (fp.getContractClientId() != null) {
            sql.append(" and cnr.clientid = ").append(fp.getContractClientId());
        }
        if ("case".equals(fp.getRelationType())) {
            sql.append(" and ccase.id = ").append(fp.getRelationID());
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (cnr.dueDate >= '").append(fp.getStartDate()).append("'");
            sql.append(" and cnr.startDate <= '").append(fp.getEndDate()).append("'");
            sql.append(")");
        }

        if (fp.getSqlSearchKey() != null) {
            sql.append(" and ( lower(cnr.number) like '").append(fp.getSqlSearchKey()).append("'");
            sql.append(" or lower(cl.name) like '").append(fp.getSqlSearchKey()).append("'");
            sql.append(" or lower(pj.name) like '").append(fp.getSqlSearchKey()).append("')");
        }
        return sql;
    }

    @Override
    public List<EdsProjectPosition> getContractPositions(Integer contractID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pp.*, 0 as clazz_ FROM ").append(getCompanyId()).append(".projectPostion pp \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".contract contr on contr.id = pp.contractid \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".project pr on (pr.id = pp.projectid and pr.isDeleted is not true ) \n");
        sql.append("WHERE pp.deleted is not true \n");
        sql.append("AND contr.id = ").append(contractID);

        return findNative(sql.toString(), EdsProjectPosition.class);
    }

    @Override
    public List<EdsCase> getRelatedCases(Integer contractId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cc.*, 0 as clazz_ FROM ").append(getCompanyId()).append(".crmcase cc \n");
        sql.append(" JOIN ").append(getCompanyId()).append(".relation r on r.fromType = 'case' and r.fromID = cc.id and r.toType = 'contract' \n");
        sql.append("WHERE (cc.deleted = false or cc.deleted is null) \n");
        sql.append("AND r.toID = ").append(contractId);

        return findNative(sql.toString(), EdsCase.class);
    }

    @Override
    public String getTotaLChargeFormula(String priceTypeString) {
        return (String) findNativeSingle(getSQLBody(priceTypeString).toString());
    }

    private StringBuilder getSQLBody(String priceTypeString) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(priceTypeString).append(" from ").append(getCompanyId()).append(".contracttotalchargeformula ");
        return sql;
    }

    @Override
    public String getTotaLCharge(String totalcharceString) {
        return String.valueOf(findNativeSingle(getSQLBody(totalcharceString).toString()));
    }

    @Override
    public EdsContract getContractByProjectId(Integer objectID) {
        return (EdsContract) findSingle("from EdsContract c where c.project.objectID=?", objectID);
    }

    @Override
    @Deprecated
    public Date getContractMaxEndDate(Integer contractId) {
        if (contractId == null) {
            return null;
        }
        return (Date) findSingle("select max(contractEndDate) from EdsProjectPosition " +
                "  where contractEndDate is not null " +
                "      and contract.objectID = ?", contractId);
    }

    @Override
    public SelectItem[] getAsSelectItem(ListingFilterParameter fp) {
        List<Object[]> list = getList(fp);
        if (list == null) return new SelectItem[]{};
        return list.stream()
                .map(items -> new SelectItem((Integer) items[0], (String) items[3]))
                .toArray(SelectItem[]::new);
    }
}
