package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsDynamicQuery;

public interface DynamicQueryManager extends Manager<EdsDynamicQuery> {

    EdsDynamicQuery getQueryByName(String queryName);
}
