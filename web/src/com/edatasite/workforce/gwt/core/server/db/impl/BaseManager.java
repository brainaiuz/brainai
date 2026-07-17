package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.ObjectIdentifier;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaOperations;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Dec 27, 2007 Time: 1:04:26 PM To
 * change this template use File | Settings | File Templates.
 */

public class BaseManager<E extends EdsObject> implements Manager<E> {


    protected static final Logger log = LoggerFactory.getLogger(BaseManager.class);

    @Autowired
    protected WfmJpaOperations jpaTemplate;

    protected Class domainClass;

    @PersistenceContext(unitName = "masterPersistenceUnit")
    protected EntityManager masterEntityManager;

    @PersistenceContext(unitName = "masterPersistenceUnit")
    protected EntityManager slaveEntityManager;

    @PersistenceContext(unitName = "slavePersistenceUnit")
    protected EntityManager slaveEntityManagerDefualt;

//    @Autowired
//    protected SessionFactory sessionFactory;

    public BaseManager(Class objectClass) {
        this.domainClass = objectClass;

    }

    public static String getPublic() {
        return "\"public\"";
    }

    public static String getCompanyId() {
        return "\"" + ServerSecurityContext.getInstance().getCompanyId() + "\"";
    }

    public void setMasterEntityManager(EntityManager masterEntityManager) {
        this.masterEntityManager = masterEntityManager;
    }

    public void setSlaveEntityManager(EntityManager slaveEntityManager) {
        this.slaveEntityManager = slaveEntityManager;
    }

    public void setSlaveEntityManagerDefualt(EntityManager slaveEntityManagerDefualt) {
        this.slaveEntityManagerDefualt = slaveEntityManagerDefualt;
    }

    @Override
    public WfmJpaOperations getJpaTemplate() {
        return jpaTemplate;
    }

    public void setJpaTemplate(WfmJpaOperations jpaTemplate) {
        this.jpaTemplate = jpaTemplate;
    }

    @Override
    public boolean createOrUpdate(E obj) {
        if (obj.getObjectID() != null) {
            update(obj);
            return false;
        } else {
            create(obj);
            return true;
        }
    }

    @Override
    public void create(E obj) {
        if (obj instanceof ObjectHistory history) {
            Date date = new Date();
            history.setLastUpdateTime(date);
            history.setCreationTime(date);
        }
        if (obj instanceof ObjectIdentifier && StringUtils.isBlank(((ObjectIdentifier) obj).getObjectKey())) {
            ((ObjectIdentifier) obj).setObjectKey(String.valueOf(UUID.randomUUID()));
        }
        masterEntityManager.persist(obj);
//        masterEntityManager.flush();
    }

    @Override
    public void update(E obj) {
        if (obj instanceof ObjectHistory) {
            EdsUser user = getUser();
            ObjectHistory history = (ObjectHistory) obj;
            Date date = new Date();
            history.setLastUpdateTime(date);
            if (user != null) {
                history.setUpdater(user);
            }
        }
//        masterEntityManager.merge(obj);
    }

    @Override
    public void updateAll(List<E> entities, int batchSize) {
        final int size = entities == null ? 0 : entities.size();
        for (int i = 0; i < size; i++) {
            Object obj = entities.get(i);

            if (obj instanceof ObjectHistory oh) {
                Date now = new Date();
                if (((EdsObject) obj).getObjectID() == null) {
                    oh.setCreationTime(now);
                }
                oh.setLastUpdateTime(now);
            }
            if (obj instanceof ObjectIdentifier oi && StringUtils.isBlank(oi.getObjectKey())) {
                oi.setObjectKey(String.valueOf(UUID.randomUUID()));
            }

            if (obj instanceof EdsObject eo && eo.getObjectID() == null) {
                masterEntityManager.persist(obj);
            } else {
                masterEntityManager.merge(obj);
            }

            if ((i + 1) % batchSize == 0) {
                masterEntityManager.flush();
                masterEntityManager.clear();
            }
        }
        masterEntityManager.flush();
        masterEntityManager.clear();
    }

    @Override
    public void delete(E obj) {
        masterEntityManager.remove(obj);
    }

    @Override
    public void deleteAll(List<E> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }
        for (E entity : entities) {
            masterEntityManager.remove(entity);
        }
        masterEntityManager.flush();
    }

    @Override
    public void deleteAllByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        masterEntityManager
                .createQuery("DELETE FROM " + domainClass.getSimpleName() + " e WHERE e.id IN :ids")
                .setParameter("ids", ids)
                .executeUpdate();
    }

    @Override
    public E merge(E obj) {
        return masterEntityManager.merge(obj);
    }

    @Override
    public Object merge(Object obj) {
        return masterEntityManager.merge(obj);
    }

    @Override
    public void persist(Object entity) {
        masterEntityManager.persist(entity);
    }

    @Override
    public E get(Number objectID) {
        if (objectID == null) {
            return null;
        }
        try {
            return (E) masterEntityManager.find(domainClass, objectID);
        } catch (Throwable e) {
            log.debug("Exception in EdsManager.getObject(): " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Map<Integer, E> getByIds(Set<? extends Number> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            List<E> resultList = masterEntityManager
                    .createQuery(
                            "SELECT e FROM " + domainClass.getSimpleName() + " e WHERE e.id IN :ids",
                            domainClass
                    )
                    .setParameter("ids", ids)
                    .getResultList();

            return resultList.stream()
                    .collect(Collectors.toMap(
                            EdsObject::getObjectID, // assuming getId() exists
                            Function.identity()
                    ));

        } catch (Exception e) {
            log.debug("Exception in getByIds(): " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public E load(Number objectID) {
        try {
            return (E) masterEntityManager.getReference(domainClass, objectID);
//            return (E) getEntityManager().findLimited(domainClass, objectID);
        } catch (Throwable e) {
            log.debug("Exception in EdsManager.getObject(): " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public <T extends SelectItem> E get(T selectItem) {
        if (isOk(selectItem) && isOk(selectItem.getId())) {
            return get(selectItem.getId());
        }
        return null;
    }

    @Override
    public List<E> get(List<Integer> ids) {
        if (ids == null) {
            return Collections.emptyList();
        }
        ids.removeAll(Collections.singleton(null));
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        final String sql = "select e from " + domainClass.getSimpleName() + " e " +
                           "    where e.id in (:ids)";
        return masterEntityManager.createQuery(sql)
                            .setParameter("ids", ids)
                            .getResultList();
    }

    @Override
    public EdsObject get(Number id, String table, String schemaName) {
        try {
            return (EdsObject) findNativeSingle(("select t.id,t.*, 0 as clazz_  from \"" + schemaName + "\"." + table + " t   where id=" + id), domainClass);
        } catch (Throwable e) {
            log.debug("Exception in \"" + schemaName + "\"." + table + " get id: " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public EdsObject get(Class domainClazz, Number objectID) {
        try {
            return (EdsObject) masterEntityManager.find(domainClazz, objectID);
        } catch (Throwable e) {
            log.debug("Exception in EdsManager.getObject(): " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void createObject(Object obj) {
        if (obj instanceof ObjectHistory) {
            ObjectHistory history = (ObjectHistory) obj;
            Date date = new Date();
            history.setLastUpdateTime(date);
            history.setCreationTime(date);
        }
        masterEntityManager.persist(obj);
        masterEntityManager.flush();
    }

    @Override
    public void clear() {
        masterEntityManager.clear();
    }

    @Override
    public void flush() {
        masterEntityManager.flush();
    }

    @Override
    public void flushAndClear() {
        Session session = masterEntityManager.unwrap(Session.class);
        if (session != null && session.isOpen() && session.isConnected()) {
            session.flush();
            session.clear();
        }
    }

    @Override
    public List findLimited(String queryString, int maxResult, Object... values) {
        Query queryObject = masterEntityManager.createQuery(values != null && values.length > 0 ? parseQuery(queryString) : queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i + 1, values[i]);
            }
        }
        if (maxResult != 0) {
            queryObject.setMaxResults(maxResult);
        }
        return queryObject.getResultList();
    }

    /**
     * findInterval()
     * <p/>
     * This method get data by query request and start, limit parameters
     * <p/>
     * this method added by Normurod
     *
     * @param queryString
     * @param start
     * @param limit
     * @param values
     * @return
     * @throws DataAccessException
     */
    @Override
    public List findInterval(String queryString, int start, int limit, Object... values) throws DataAccessException {
        Query queryObject = masterEntityManager.createQuery(values != null && values.length > 0 ? parseQuery(queryString) : queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i + 1, values[i]);
            }
        }

        if (start != 0) {
            queryObject.setFirstResult(start);
        }

        if (limit != 0) {
            queryObject.setMaxResults(limit);
        }

        return queryObject.getResultList();
    }

    /**
     * Use only for read-only transactions
     * findIntervalByNamedParams()
     * <p/>
     * This method get data by query request and start, limit parameters
     * <p/>
     * this method added by Sherali
     *
     * @param queryString
     * @param start
     * @param limit
     * @param params
     * @return
     */
    @Override
    public List findIntervalByNamedParams(String queryString, int start, int limit, Map<String, ?> params) {
        Query queryObject = masterEntityManager.createQuery(queryString);
        if (params != null) {
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                queryObject.setParameter(entry.getKey(), entry.getValue());
            }
        }

        if (start != 0) {
            queryObject.setFirstResult(start);
        }

        if (limit != 0) {
            queryObject.setMaxResults(limit);
        }

        return queryObject.getResultList();
    }

    @Override
    public List findNative(String queryString, Class clazz) {
        Query queryObject = masterEntityManager.createNativeQuery(queryString, clazz);
        return queryObject.getResultList();
    }

    @Override
    public List findNative(String queryString) throws DataAccessException {
        Query queryObject = masterEntityManager.createNativeQuery(queryString);
        return queryObject.getResultList();
    }

    @Override
    public List findNativeFromSlave(String queryString) throws DataAccessException {
        Query queryObject = slaveEntityManagerDefualt.createNativeQuery(queryString);
        return queryObject.getResultList();
    }

    @Override
    public List find(String queryString) {
        return masterEntityManager.createQuery(queryString).getResultList();
    }

    @Override
    public List find(String queryString, Object... values) {
        Query queryObject = masterEntityManager.createQuery(values != null && values.length > 0 ? parseQuery(queryString) : queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i + 1, values[i]);
            }
        }
        return queryObject.getResultList();
    }

    @Override
    public List find(String queryString, Map<String, Object> namedParams) {
        Query queryObject = masterEntityManager.createQuery(queryString); // parseQuery would break :ids
        if (namedParams != null) {
            for (Map.Entry<String, Object> entry : namedParams.entrySet()) {
                queryObject.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return queryObject.getResultList();
    }

    @Override
    public Object findNativeSingle(String queryString) {
        Query queryObject = masterEntityManager.createNativeQuery(queryString);
        queryObject.setMaxResults(1);
        try {
            return queryObject.getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Object findNativeSingle(String queryString, Class clazz) {
        Query queryObject = masterEntityManager.createNativeQuery(queryString, clazz);
        queryObject.setMaxResults(1);
        try {
            return queryObject.getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }
    @Override
    public Object findNativeSingleFromSlave(String queryString) {
        Query queryObject = slaveEntityManagerDefualt.createNativeQuery(queryString);
        queryObject.setMaxResults(1);
        try {
            return queryObject.getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List findNative(String queryString, Object... values) {
        return findNativeLimited(queryString, 0, values);
    }

    @Override
    public Object findNativeSingle(String queryString, Object... values) {
        Query queryObject = masterEntityManager.createNativeQuery(values != null && values.length > 0 ? parseQuery(queryString) : queryString);
        queryObject.setMaxResults(1);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i + 1, values[i]);
            }
        }
        try {
            return queryObject.getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List findNativeByNamedParams(String queryString, Map<String, ?> params) {
        Query queryObject = masterEntityManager.createNativeQuery(queryString);
        if (params != null) {
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                queryObject.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return queryObject.getResultList();
    }

    @Override
    public Object findNativeSingleByNamedParams(String queryString, Map<String, ?> params) {
        Query queryObject = masterEntityManager.createNativeQuery(queryString);
        if (params != null) {
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                queryObject.setParameter(entry.getKey(), entry.getValue());
            }
        }
        queryObject.setMaxResults(1);
        try {
            return queryObject.getSingleResult();
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Object findNativeSingleByParams(String queryString, Class clazz, Object... values) {
        Query queryObject = masterEntityManager.createNativeQuery(values != null && values.length > 0 ? parseQuery(queryString) : queryString, clazz);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i + 1, values[i]);
            }
        }
        queryObject.setMaxResults(1);
        try {
            return queryObject.getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public void updateNative(String updateQuery) throws DataAccessException {
        Query queryObject = masterEntityManager.createNativeQuery(updateQuery);
        queryObject.executeUpdate();
    }

    @Override
    public void updateNativeByParams(String queryString, Object... values) {
        Query queryObject = masterEntityManager.createNativeQuery(values != null && values.length > 0 ? parseQuery(queryString) : queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i + 1, values[i]);
            }
        }
        queryObject.executeUpdate();
    }

    @Override
    public void update(String updateQuery, Object... values) {
        Query queryObject = masterEntityManager.createQuery(values != null && values.length > 0 ? parseQuery(updateQuery) : updateQuery);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i + 1, values[i]);
            }
        }
        queryObject.executeUpdate();
    }

    @Override
    public void updateByNamedParams(String queryString, Map<String, ?> params) {
        Query queryObject = masterEntityManager.createQuery(queryString);
        if (params != null) {
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                queryObject.setParameter(entry.getKey(), entry.getValue());
            }
        }
        queryObject.executeUpdate();
    }

    @Override
    public List findNativeLimited(String queryString, int maxResults, Object... values) {
        Query queryObject = masterEntityManager.createNativeQuery(values != null && values.length > 0 ? parseQuery(queryString) : queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i + 1, values[i]);
            }
        }
        if (maxResults != 0) {
            queryObject.setMaxResults(maxResults);
        }
        return queryObject.getResultList();
    }

    @Override
    public List findByNamedParams(String queryString, Map<String, ?> params) {
        Query queryObject = masterEntityManager.createQuery(queryString);
        if (params != null) {
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                queryObject.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return queryObject.getResultList();
    }

    @Override
    public List findByNamedQuery(String queryName) throws DataAccessException {
        return findByNamedQuery(queryName, (Object[]) null);
    }

    @Override
    public List findByNamedQuery(String queryName, Object... values) {
        Query queryObject = masterEntityManager.createNamedQuery(queryName);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i + 1, values[i]);
            }
        }
        return queryObject.getResultList();
    }

    @Override
    public List findByNamedQueryAndNamedParams(String queryName, Map<String, ?> params) {
        Query queryObject = masterEntityManager.createNamedQuery(queryName);
        if (params != null) {
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                queryObject.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return queryObject.getResultList();
    }

    @Override
    public Object findSingle(String queryString) {
        Query queryObject = masterEntityManager.createQuery(queryString);
        queryObject.setMaxResults(1);
        try {
            return queryObject.getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Object findSingleByNamedParams(String queryString, Map<String, ?> params) {
        Query queryObject = masterEntityManager.createQuery(queryString);
        queryObject.setMaxResults(1);
        if (params != null) {
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                queryObject.setParameter(entry.getKey(), entry.getValue());
            }
        }
        try {
            return queryObject.getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Object findSingle(String queryString, Object... values) {
        List list = findLimited(queryString, 1, values);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public EdsUser getUser() {
        return (EdsUser) ServerSecurityContext.getInstance().getUser();
    }

    @Override
    public List<String> getUsesSchemaNames() {
        return (List<String>) findNative("select id||'' from company where isFree=false");
    }

    protected Map<String, Object> preparing(Entry... params) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> item : params) {
            if (item == null) continue;
            map.put(item.getKey(), item.getValue());
        }
        return map;
    }

    protected static class Entry implements Map.Entry {
        private final String key;
        private Object value;

        public Entry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Object getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            return this.value = value;
        }
    }

    private String parseQuery(String query) {
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("=? *\\?");
        Matcher matcher = pattern.matcher(query);
        int counter = 1;

        while (matcher.find()) {
            String matchedText = matcher.group();
            matcher.appendReplacement(sb, (matchedText.startsWith("=") ? "= " : " ") + "\\?" + counter);
            counter++;
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
