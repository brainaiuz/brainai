package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsReportingPermission;
import com.edatasite.workforce.core.domain.EdsUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: May 8, 2012
 * Time: 5:35:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReportingPermissionManager extends Manager<EdsReportingPermission> {

    HashSet<String> getPermissionCodeList();

    EdsReportingPermission findByCode(Integer companyID, String code, String context);

    List<EdsReportingPermission> childByCode(String code, String context);

    HashSet<String> getUsersPermissionsListNative(String context, EdsUser user);

    boolean hasPermission(String code, EdsUser user);

    boolean hasPermission(ArrayList<String> codes, EdsUser user);

}
