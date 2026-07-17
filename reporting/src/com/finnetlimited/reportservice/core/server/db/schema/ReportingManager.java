package com.finnetlimited.reportservice.core.server.db.schema;

import com.edatasite.workforce.core.domain.reporting.EdsCompanyFavouriteReportTemplates;
import com.edatasite.workforce.gwt.core.client.rpc.SavedReportTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingTestDTO;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: ${Dilsh0d}
 * Date: 06-Mar-2010
 * Time: 16:54:55
 */
public interface ReportingManager extends Manager<EdsReport> {

    boolean hasReport(String name, Integer reportId);

    ArrayList<EdsReport> getFolderReports(Integer folderId);

    ArrayList<EdsReport> getFavReports(Integer userid);

    void deleteFavouriteReportTemplate(Integer userid, Integer reportingid, Integer companyId);

    void createFavouriteReportTemplate(Integer userId, Integer reportid, Integer companyId);

    void deleteWithRelation(String permissionCode, String reportCode, Integer reportid, Integer companyID);

    EdsCompanyFavouriteReportTemplates getFavouriteReportTemplate(Integer userid, Integer reportid, Integer companyId);

    ArrayList<Integer> getEmployeeIDsByReportID(Integer reportID);

    boolean makeTestingReportSchema(Integer mySchema);

    List<Object[]> getReportList(ListingFilterParameter filterParameter);

    String setParametersNative(ReportingTestDTO testDTO, Integer companyid);

    String setParametersNative(HashMap<String, String> map, Integer companyid);

    Integer getCategoryByReport(Integer reportid, Integer companyid);

    Boolean getReportStar(Integer reportid, Integer userid);

    Integer getReportListCount(ListingFilterParameter filterParameter);

    String getGenerateUpdateCommand(ListingFilterParameter filterParameter);

    EdsReport getByCode(String code);

    ArrayList<EdsReport> getReportListByCompany(int i);

    EdsReport getByCompany(Integer id, Integer companyID);

    EdsReport getReport(Integer exceltemplateid);

    String changeColumnNamePatch(SavedReportTemplate item);

    ArrayList<EdsReport> findAll();

    List<Object[]> listObject(ListingFilterParameter filter);

    List<Object[]> getCategories();

    ArrayList<SelectItem> getMinimizedReportList(ListingFilterParameter filter);

    void addOrRemoveProject(Integer reportId, boolean addProject);

    ArrayList<SelectListRpc> getReports(ListingFilterParameter filter);

    Integer getReportsCount(ListingFilterParameter filter);

    ArrayList<EdsReport> getReletedProjectReports();
}
