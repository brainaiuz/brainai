/*
package com.finnetlimited.reportservice.core.server.db.dao;

import com.edatasite.workforce.core.domain.EdsUser;
import com.finnetlimited.reportservice.core.server.db.IDao;
import com.finnetlimited.reportservice.core.server.domain.FldObject;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

*/
/**
 * User: ${Dilsh0d}
 * Date: 05-Mar-2010
 * Time: 13:00:31
 *//*

public class BaseDao<T extends FldObject> implements IDao<T> {

    private Class<T> entityType;

    @PersistenceContext
    protected EntityManager entityManager;


    public BaseDao(Class<T> entity) {
        this.entityType = entity;
    }

    public void create(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
    }

    public EdsUser getUser() {
        return (EdsUser) ServerSecurityContext.getInstance().getUser();
    }

    public static String getCompanyId() {
        return "\"" + ServerSecurityContext.getInstance().getCompanyId() + "\"";
    }

    public void update(T entity) {

    }

    public void delete(T entity) {
        entityManager.remove(entity);
    }

    public void merge(T entity) {
        entityManager.merge(entity);
    }

    public T get(Integer id) {
        return entityManager.find(entityType, id);
    }

    public List<T> findAll() {
        String all = "SELECT t FROM " + entityType.getSimpleName() + " t ORDER BY t.id DESC ";
        Query list = entityManager.createQuery(all);
        list.setMaxResults(5000);
        return list.getResultList();
    }
}
*/
