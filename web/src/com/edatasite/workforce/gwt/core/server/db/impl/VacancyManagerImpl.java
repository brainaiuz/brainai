
package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.gwt.contact.client.rpc.CommonItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: Hayot
 * Date: 09-Jul-2012
 * Time: 16:43:45
 */
@Repository("vacancyManager")
public class VacancyManagerImpl extends BaseManager<EdsVacancy> implements VacancyManager {
    public VacancyManagerImpl() {
        super(EdsVacancy.class);
    }

    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    @Override
    public List<EdsVacancy> list(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        EdsUser edsUser = getUser();
        List<EdsRole> userRoles = new LinkedList<>(edsUser.getRoles());
        List<Integer> userMaxRoleIDs = new ArrayList<>();
        for (EdsRole roleIds : userRoles) {
            userMaxRoleIDs.add(roleIds.getObjectID());
        }
        Integer userMaxRoleID = ServerUtils.getUserRolesSorted(userMaxRoleIDs).get(0);


        StringBuilder sql = new StringBuilder();

        sql.append("SELECT v.id, v.* \n");
        sql.append("FROM ").append(getCompanyId()).append(".vacancy v \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".myUser man ON (man.id = v.manager_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".position po ON (po.id = v.position_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".location loc ON (loc.id = v.location_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getPublic()).append(".country cont ON (cont.id = loc.countryid) \n");
        sql.append("LEFT OUTER JOIN ").append(getPublic()).append(".region stat ON (stat.id = loc.stateid) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".reference vst ON (vst.id = v.status_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".reference req ON (req.id = v.requireddegree_id) \n");
        if (fp.getCrmContactId() != null) {
            sql.append("left join ").append(getCompanyId()).append(".candidate_vacancies cv on v.id = cv.vacancy_id \n");
            sql.append("left join ").append(getCompanyId()).append(".crmcontact c on cv.candidate_id = c.id \n");
        }
        sql.append("WHERE v.deleted is not true \n");
        if (fp.getCrmContactId() != null) {
            sql.append("and c.contactType=").append(EdsCrmContact.CANDIDATE).append(" and c.id =").append(fp.getCrmContactId()).append(" \n");
        }

        //filter by vacancy status -
        if (!fp.isBriefly()) { //bu listing uchun ishlatilmaydi, qolgan joylarda status bo'yincha vancancy larni olib chiqadi
            EdsReference openStatus = referenceManager.findReference(EdsVacancy.VACANCY_STATUSES, EdsVacancy.VS_OPEN);
            EdsReference inProgressStatus = referenceManager.findReference(EdsVacancy.VACANCY_STATUSES, EdsVacancy.VS_IN_PROGRESS);
            EdsReference partiallyFilledStatus = referenceManager.findReference(EdsVacancy.VACANCY_STATUSES, EdsVacancy.VS_PARTIALLY_FILLED);
            EdsReference filledStatus = referenceManager.findReference(EdsVacancy.VACANCY_STATUSES, EdsVacancy.VS_FILLED);
            String statusIds = "";
            boolean isOpenStatus = fp.isShowActive();
            statusIds += isOpenStatus ? (String.valueOf(openStatus.getObjectID())) : ((openStatus != null ? openStatus.getObjectID() : "") + (inProgressStatus != null ? "," + inProgressStatus.getObjectID() : "") + (partiallyFilledStatus != null ? "," + partiallyFilledStatus.getObjectID() : "") + (filledStatus != null ? "," + filledStatus.getObjectID() : ""));

            if (!"".equals(statusIds)) {
                sql.append("AND vst.id IN (").append(statusIds).append(") \n");
            }

        }
        if (fp.getProjectId() != null) {
            sql.append(" AND v.project_id=" + fp.getProjectId());
        }
        //searching
        if (fp.getSqlSearchKey() != null) {
            sql.append(" AND (");
            sql.append("  LOWER(v.jobTitle) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(v.vacancynumber) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(po.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(loc.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(cont.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(stat.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(vst.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(req.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(man.firstName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(man.lastName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") \n");
        }
        sql.append(" ORDER BY ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (VacancyItem.VACANCY_ID.equals(fp.getSortField())) {
                sql.append("v.vacancynumber");
            } else if (VacancyItem.VACANCY_JOB_TITLE.equals(fp.getSortField())) {
                sql.append("v.jobTitle");
            } else if (VacancyItem.VACANCY_START_DATE.equals(fp.getSortField())) {
                sql.append("v.startDate");
            } else if (VacancyItem.VACANCY_END_DATE.equals(fp.getSortField())) {
                sql.append("v.endDate");
            } else if (VacancyItem.VACANCY_STATUS.equals(fp.getSortField())) {
                sql.append("vst.name");
            } else if (VacancyItem.VACANCY_MANAGER.equals(fp.getSortField())) {
                sql.append("man.firstName, man.lastName");
            } else if (VacancyItem.VACANCY_POSITION.equals(fp.getSortField())) {
                sql.append("po.name");
            } else if (VacancyItem.VACANCY_LOCATION.equals(fp.getSortField())) {
                sql.append("cont.name, stat.name");
            } else if (VacancyItem.VACANCY_REQUIRED_DEGREE.equals(fp.getSortField())) {
                sql.append("req.name");
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
            sql.append(" v.lastModifiedDate desc nulls last");
        }
        return findNative(sql.toString(), EdsVacancy.class);
    }

    @Override
    public Integer getVacancyLastIntNumber() {
        return (Integer) findSingle("select v.intNumber from EdsVacancy v order by v.intNumber desc");
    }

    @Override
    public List<Object[]> getList() {
        return findNative("select lower(v.jobTitle), v.id  from " + getCompanyId() + ".vacancy v where v.deleted is not true");
    }

    /**
     * Delete candidate vacancies
     *
     * @param candidateID - candidate ID
     */
    public void deleteCandidateVacancies(Integer candidateID) {
        updateNative("DELETE FROM " + getCompanyId() + ".candidate_vacancies cv WHERE cv.candidate_id = " + candidateID);
    }

    public List<Object[]> getVacancyMatchedCandidates(Integer vacancyId) {
        String sql = "SELECT con.id, (con.firstName ||' '|| con.lastName) as candidateName, ref.name as candidateStatus \n" +
                "FROM " + getCompanyId() + ".crmcontact con \n" +
                "INNER JOIN " + getCompanyId() + ".candidate_vacancies cv on cv.candidate_id=con.id \n" +
                "LEFT JOIN " + getCompanyId() + ".reference ref on ref.id = con.status \n" +
                "where  con.contactType=" + EdsCrmContact.CANDIDATE + " and con.deleted is not true and cv.vacancy_id=" + vacancyId;
        return findNative(sql);
    }

    public List<CommonItem> getCandidatePerVacancyChartData() {
        String sql = "SELECT v.id as objectID,v.jobtitle as name,count(con.id) as count \n" +
                "FROM " + getCompanyId() + ".crmcontact con \n" +
                "INNER JOIN " + getCompanyId() + ".candidate_vacancies cv on con.id = cv.candidate_id \n" +
                "INNER JOIN " + getCompanyId() + ".vacancy v on cv.vacancy_id= v.id \n" +
                "WHERE con.contactType=" + EdsCrmContact.CANDIDATE + " and con.deleted is not true and v.deleted is not true \n" +
                "GROUP BY v.id,v.jobtitle ORDER BY v.id";
        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(CommonItem.class));

    }

    @Override
    public List<EdsVacancy> getUndeletedVacancyIn(String vacancyIds) {
        return find("SELECT v FROM EdsVacancy v WHERE v.objectID IN (" + vacancyIds + ") AND (v.deleted IS NULL or v.deleted<>true)");
    }

    @Override
    public List<Integer> getUndeletedVacancyIdList(String vacancyIds) {
        return (List<Integer>) find("SELECT v.objectID FROM EdsVacancy v WHERE v.objectID IN (" + vacancyIds + ") AND (v.deleted IS NULL or v.deleted<>true)");
    }

    @Override
    public List<Integer> getVacancyIdListWithLimit(Integer companyID, int startat, int limit) {
        String query = "SELECT v.id FROM \"" + companyID + "\".vacancy v WHERE (v.deleted is null or v.deleted<>true) AND v.id >" + startat + " order by v.id asc limit " + limit;
        Query queryObject = slaveEntityManager.createNativeQuery(query);
        return queryObject.getResultList();
    }

    @Override
    public List<Integer> getCompanyDeleteVacancyForSolr(SolrReindexRpc solrReindexRpc) {
        return (List<Integer>) slaveEntityManager.createQuery("SELECT v.objectID FROM EdsVacancy v WHERE v.deleted=true" + " AND v.lastUpdatedTime>=" + "'" + solrReindexRpc.getLastUpdateTime() + "'" +
                (solrReindexRpc.getLastUpdateEndTime() != null ? " and v.lastUpdatedTime<='" + solrReindexRpc.getLastUpdateEndTime() + "'" : "")).getResultList();
    }

    @Override
    public List<EdsVacancy> getVacancyListForSolr(SolrReindexRpc solrReindex, Integer start, int limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder vacancySqlQuery = new StringBuilder("SELECT v FROM EdsVacancy v WHERE (v.deleted is null or v.deleted<>true) ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            vacancySqlQuery.append(" AND v.lastUpdatedTime >= :modifiedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                vacancySqlQuery.append(" and v.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        vacancySqlQuery.append(" order by v.id asc ");
        return findIntervalByNamedParams(vacancySqlQuery.toString(), start, limit, params);
    }

    @Override
    public List<Integer> getVacancyIdsForSolr(List<Integer> idsFromSolrDocument) {
        return (List<Integer>) find("select v.objectID from EdsVacancy v where v.objectID in (" + ServerUtils.getAsCommoDelimited(idsFromSolrDocument, "0", ",") + ") and " + ServerUtils.checkForDeleted("v.deleted"));
    }

    @Override
    public EdsVacancy getByIntegrationId(String integrationId) {
        return (EdsVacancy) findSingle("select v from EdsVacancy v where v.integrationId = ?", integrationId);
    }

    @Override
    public EdsVacancy getVacancyByNumber(String number) {
        return (EdsVacancy) findSingle("select v from EdsVacancy v where v.deleted IS NULL or v.deleted<>true and v.vacancyNumber = ?", number);
    }
}
