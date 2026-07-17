package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaOperations;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 27, 2007
 * Time: 12:51:45 PM
 * To change this template use File | Settings | File Templates.
 */

public interface Manager<E extends EdsObject> {

    boolean createOrUpdate(E obj);

    void create(E obj);

    void update(E obj);

    void updateAll(List<E> entities, int batchSize);

    void delete(E obj);

    void deleteAll(List<E> entities);

    void deleteAllByIds(List<Integer> ids);  // optional bulk variant

    E merge(E obj);

    Object merge(Object obj);

    void persist(final Object entity);

    E get(Number id);

    Map<Integer, E> getByIds(Set<? extends Number> ids);

    EdsObject get(Number id, String table, String schemaName);

    EdsObject get(Class domainClazz, Number id);

    void createObject(Object obj);

    EdsUser getUser();
//    EdsUser getUser(String sessionId);

    WfmJpaOperations getJpaTemplate();

    List<String> getUsesSchemaNames();

    E load(Number objectID);

    void clear();

    void flush();

    void flushAndClear();

    List findLimited(String queryString, int maxResult, Object... values);

    List findInterval(final String queryString, final int start, final int limit, final Object... values);

    List findIntervalByNamedParams(final String queryString, final int start, final int limit, final Map<String, ?> params);

    List findNative(String queryString, Class clazz) throws DataAccessException;

    List findNative(String queryString) throws DataAccessException;

    List findNativeFromSlave(String queryString) throws DataAccessException;

    List find(String queryString);

    List find(String queryString, Object... values);

    List find(String queryString, Map<String, Object> namedParams);

    Object findNativeSingle(String queryString);

    Object findNativeSingle(String queryString, final Class clazz);

    Object findNativeSingleFromSlave(String queryString);

    List findNative(final String queryString, final Object... values);

    void updateNative(final String updateQuery) throws DataAccessException;

    void updateNativeByParams(final String queryString, final Object... values);

    void update(final String updateQuery, final Object... values);

    void updateByNamedParams(final String queryString, final Map<String, ? extends Object> params);

    List findNativeLimited(final String queryString, final int maxResults, final Object... values);

    Object findNativeSingle(final String queryString, final Object... values);

    List findNativeByNamedParams(String queryString, Map<String, ? extends Object> params);

    Object findNativeSingleByNamedParams(final String queryString, final Map<String, ? extends Object> params);

    Object findNativeSingleByParams(final String queryString, final Class clazz, final Object... values);

    List findByNamedParams(String queryString, Map<String, ?> params);

    List findByNamedQuery(String queryName) throws DataAccessException;

    List findByNamedQuery(String queryName, Object... values);

    List findByNamedQueryAndNamedParams(String queryName, Map<String, ?> params);

    Object findSingle(final String queryString);

    Object findSingleByNamedParams(final String queryString, final Map<String, ? extends Object> params);

    Object findSingle(final String queryString, final Object... values);

    <T extends SelectItem> E get(T status);

    List<E> get(List<Integer> ids);
}
