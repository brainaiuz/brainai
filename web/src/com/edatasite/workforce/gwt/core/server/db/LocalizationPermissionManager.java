package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLocalizationPermissions;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla Nigmatjonov
 * Date: Jan 8, 2008
 * Time: 5:35:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LocalizationPermissionManager extends Manager<EdsLocalizationPermissions> {
    List<EdsLocalizationPermissions> list();

    EdsLocalizationPermissions getCompanyLocalization(Integer companyId);
}
