package com.edatasite.workforce.gwt.core.server.db.wfp;

import com.edatasite.workforce.core.domain.reporting.EdsReportTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.ReportingListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 20.10.2011
 * Time: 15:08
 * To change this template use File | Settings | File Templates.
 */

public interface ReportTemplateManager extends Manager<EdsReportTemplate> {

    ArrayList<EdsReportTemplate> getReportTemplateList(String categoryFromFilter, String roles, Integer companyID);

    ArrayList<EdsReportTemplate> getReportTemplateList(Boolean isCustom);

    ArrayList<EdsReportTemplate> getReportTemplateList(Boolean isCustom, ListingFilterParameter filterParameter);

    ArrayList<Integer> getReportTemplateIds(Boolean isCustom);

    ListResult<ReportingListItem> getReportingXMLTemplateList(ListingFilterParameter filterParameter);

    void create(EdsReportTemplate edsReportTemplate);

    EdsReportTemplate getByCode(String viewCode);

    Integer getIdByCode(String viewCode);

    void updateTemplate(String code, EdsReportTemplate changeTemplate);

    void insertTemplate(String code, EdsReportTemplate changeTemplate);

    ArrayList<Integer> getReportTemplateIdsForBackup();

}
