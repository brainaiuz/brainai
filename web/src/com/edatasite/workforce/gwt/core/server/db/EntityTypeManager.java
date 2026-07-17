package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEntityType;

import java.util.List;

/**
 * Created by Omonullo on 5/15/2017.
 */
public interface EntityTypeManager extends Manager<EdsEntityType> {

    List<EdsEntityType> list();

}
