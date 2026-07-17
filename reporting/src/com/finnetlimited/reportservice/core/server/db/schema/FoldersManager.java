package com.finnetlimited.reportservice.core.server.db.schema;

import com.edatasite.workforce.core.domain.reporting.EdsReportTemplateCategory;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsFolders;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ${Dilsh0d}
 * Date: 06-Mar-2010
 * Time: 16:53:54
 */
public interface FoldersManager extends Manager<EdsFolders> {

    ArrayList<EdsFolders> list(String domainName, Integer companyId, Integer userId);

    ArrayList<EdsFolders> search(String search, String domainName, Integer companyId, Integer userId);

    boolean isFolderYes(FolderRpc folder, Integer companyId, boolean issave);

    EdsFolders getSystemFolder();

    List<EdsFolders> getByCategory(EdsReportTemplateCategory category, String domainName, Integer companyId, Integer userId);

    EdsFolders getByName(String name);

    EdsFolders getByCategoryAndName(String category, String name);
}
