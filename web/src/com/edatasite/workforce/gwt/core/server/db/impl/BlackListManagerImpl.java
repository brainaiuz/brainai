package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBlackList;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.BlackListManager;
import com.edatasite.workforce.utils.EdsContextParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.List;

/**
 * User: admin
 * Date: Jan 5, 2010
 * Time: 4:03:13 PM
 */
@Repository("blackListManager")
public class BlackListManagerImpl extends BaseManager<EdsBlackList> implements BlackListManager {

    public BlackListManagerImpl() {
        super(EdsBlackList.class);
    }

    public boolean isEmailValid(String email) {
        if (StringUtils.isNotBlank(email)) {
            try {
                EdsBlackList bl = slaveEntityManager.createQuery("select bl from EdsBlackList bl where lower(bl.email)=:blackEmail and bl.hostName=:hostName", EdsBlackList.class)
                        .setParameter("blackEmail", email).setParameter("hostName", EdsContextParams.getHostname()).getSingleResult();
                return bl == null;
            } catch (NoResultException | NonUniqueResultException | EmptyResultDataAccessException ex) {
                return true;
            }
        }
        return false;
    }

    public List<String> getBlackList() {
        return slaveEntityManager.createQuery("select lower(bl.email) from EdsBlackList bl where bl.hostName=:hostName", String.class)
                .setParameter("hostName", EdsContextParams.getHostname()).getResultList();
    }

    public List<EdsBlackList> blackLists(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder("SELECT bl from EdsBlackList bl ");
        sql.append(" WHERE 1=1 ");
        if (fp.getParams() != null && !"".equals(fp.getParams())) {
            if (!"All".equals(fp.getParams())) {
                sql.append(" AND bl.hostName IN (").append(Utils.getStringEachValueWithParentheses(fp.getParams())).append(") ");
            }
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (LOWER(bl.email) LIKE '").append(fp.getSqlSearchKey()).append("')");

        }
        sql.append(" ORDER BY bl.email  ");
        sql.append(fp.isAscending() ? "ASC" : "DECS");
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    public Integer blackListsCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder("SELECT  COUNT(*) from EdsBlackList bl ");
        sql.append(" WHERE 1=1 ");
        if (fp.getParams() != null && !"".equals(fp.getParams())) {
            if (!"All".equals(fp.getParams())) {
                sql.append(" AND bl.hostName IN (").append(Utils.getStringEachValueWithParentheses(fp.getParams())).append(") ");
            }
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (LOWER(bl.email) LIKE '").append(fp.getSqlSearchKey()).append("')");
        }
        return Integer.parseInt(findSingle(sql.toString()).toString());
    }
}