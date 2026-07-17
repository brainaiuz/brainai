package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsActionLogger;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Aug 28, 2009
 * Time: 4:45:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ActionLoggerManager extends Manager<EdsActionLogger> {
    List<EdsActionLogger> getList();
}

