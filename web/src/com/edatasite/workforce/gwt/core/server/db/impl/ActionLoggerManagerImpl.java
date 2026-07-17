package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsActionLogger;
import com.edatasite.workforce.gwt.core.server.db.ActionLoggerManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Aug 28, 2009
 * Time: 4:46:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("actionLoggerManager")
public class ActionLoggerManagerImpl extends BaseManager<EdsActionLogger> implements ActionLoggerManager {
    public ActionLoggerManagerImpl() {
        super(EdsActionLogger.class);
    }

    public List<EdsActionLogger> getList() {
        return find("from  EdsActionLogger a");
    }
}

