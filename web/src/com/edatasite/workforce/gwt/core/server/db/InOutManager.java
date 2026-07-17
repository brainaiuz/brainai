package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsInOutSettings;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jan 27, 2010
 * Time: 10:02:19 AM
 * To change this template use File | Settings | File Templates.
 */
public interface InOutManager extends Manager<EdsInOutSettings> {
    EdsInOutSettings getCompanyInOutSettings();
}
