package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsDynamicQuery;
import com.edatasite.workforce.gwt.core.server.db.DynamicQueryManager;
import org.springframework.stereotype.Repository;

@Repository("dynamicQueryManager")
public class DynamicQueryManagerImpl extends BaseManager<EdsDynamicQuery> implements DynamicQueryManager {

    public DynamicQueryManagerImpl() {
        super(EdsDynamicQuery.class);
    }

    @Override
    public EdsDynamicQuery getQueryByName(String queryName) {
        return (EdsDynamicQuery) findSingle("select dq from EdsDynamicQuery dq where dq.query_name=?", queryName);
    }
}
