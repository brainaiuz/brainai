package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCustomLayout;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 1/25/12
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CustomLayoutManager extends Manager<EdsCustomLayout> {
    EdsCustomLayout getWebFormLayout(String formID);
}
