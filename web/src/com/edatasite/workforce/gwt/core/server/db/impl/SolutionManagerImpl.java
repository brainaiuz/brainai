package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsSolution;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.SolutionManager;
import com.edatasite.workforce.gwt.crm.client.rpc.SolutionItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 17:23:37
 * To change this template use File | Settings | File Templates.
 */
@Repository("solutionManager")
public class SolutionManagerImpl extends BaseManager<EdsSolution> implements SolutionManager {
    public SolutionManagerImpl() {
        super(EdsSolution.class);
    }

    public List<EdsSolution> getList(ListingFilterParameter fp) {
        String sql = "select s from EdsSolution s left join s.status as status left join s.assignee as assignee where (s.deleted<>true or s.deleted is null) ";
        if(!StringUtils.isEmpty(fp.getSqlSearchKey())){
            sql += " and (lower(s.title) like :searchKey or lower(s.question) like :searchKey or lower(s.answer) like :searchKey) ";
        }
        sql += " order by ";
        if(SolutionItem.TITLE.equals(fp.getSortField())){
            sql += " s.title ";
        } else if(SolutionItem.ASSIGNEE.equals(fp.getSortField())){
            sql += " assignee.firstName ";
        } else if(SolutionItem.STATUS.equals(fp.getSortField())){
            sql += " status.name ";
        } else if(SolutionItem.QUESTION.equals(fp.getSortField())){
            sql += " s.question ";
        } else if(SolutionItem.ANSWER.equals(fp.getSortField())){
            sql += " s.answer ";
        } else {
            sql += " s.id ";
        }
        sql += !fp.isAscending() || StringUtils.isEmpty(fp.getSortField()) ? " desc " : "";
        TypedQuery<EdsSolution> query = slaveEntityManager.createQuery(sql, EdsSolution.class);
        if(!StringUtils.isEmpty(fp.getSqlSearchKey())){
            query.setParameter("searchKey", fp.getSqlSearchKey());
        }
        return query.getResultList();
    }

    public Integer getListCount(ListingFilterParameter fp) {
        String sql = "select count(distinct s.objectID) from EdsSolution s where (s.deleted<>true or s.deleted is null) ";
        if(!StringUtils.isEmpty(fp.getSqlSearchKey())){
            sql += " and (lower(s.title) like :searchKey or lower(s.question) like :searchKey or lower(s.answer) like :searchKey) ";
        }
        TypedQuery<Long> query = slaveEntityManager.createQuery(sql, Long.class);
        if(!StringUtils.isEmpty(fp.getSqlSearchKey())){
            query.setParameter("searchKey", fp.getSqlSearchKey());
        }
        return query.getSingleResult().intValue();
    }
}