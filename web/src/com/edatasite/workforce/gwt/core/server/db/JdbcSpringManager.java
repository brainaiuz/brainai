package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.backend.client.rpc.AccessLogList;
import com.edatasite.workforce.gwt.backend.client.rpc.AccountManagerItemList;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.impl.JdbcSpringManagerImpl;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 23-Oct-2010
 * Time: 14:16:27
 * To change this template use File | Settings | File Templates.
 */
public interface JdbcSpringManager {
    AccountManagerItemList findOverallInActiveUsers(ListingFilterParameter fp);

    AccessLogList getAccessLogSection(ListingFilterParameter fp);

    List<EdsCompany> getSchemaNameList();

    Long getEmployeesCountByDateLimit(Timestamp sdate, Timestamp edate);

    String generateSqlQuery(Integer companyId);

    void deleteCompanyAndSchema(Integer companyID);

    JdbcSpringManagerImpl getSimpleJdbcTemplate();

    JdbcOperations getSimJdbcOperations();

    Map<String, Object> getUploadAmazonSettingsList(Integer uploadId);

    Map<String, Object> getUploadMinIOSettingsList(Integer uploadId);

    List<Integer> getFileRbacEntries(Integer fileObjectID);

    List<Integer> getFolderRbacEntries(Integer folderObjectID);

    List<EdsCompany> getCompanyList();

    String getTemplateSchema(String pattern);

    List<String> getExistingTemplateSchemas(String pattern);
}
