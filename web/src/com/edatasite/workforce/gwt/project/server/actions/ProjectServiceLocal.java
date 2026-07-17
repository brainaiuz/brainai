package com.edatasite.workforce.gwt.project.server.actions;

import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.app.RejectedImportRecord;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.project.client.rpc.CloneProjectItem;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectViewItem;
import net.sf.mpxj.ProjectFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ProjectServiceLocal {

    Integer saveProject(ProjectSingleItem item) throws NumberExistingException;

    EditProject getProjectForEdit(Integer projectId, Date date, Integer clientID);

    void updateProject(EditProject project) throws NumberExistingException;

    ProjectMember[] getProjectEmployees(Integer projectID);

    NumberData generateProjectNumber(Date date, Integer clintId, Integer projectId);

    void mergeProjectAccounts(Integer objectID, ArrayList<Integer> otherObjectIDs);

    void indexProjectTasks(Integer projectID);

    void indexCompanyProjects(SolrReindexRpc solrReindex);

    void indexCompanyTasks(SolrReindexRpc solrReindexRpc);

    SelectItem[] getLookUpItems(ListingFilterParameter filterParametrs, int type);

    void addMembers(Integer projectId, ProjectMember[] members);

    ProjectFile exportToMSProject(Integer projectID);

    void restartProectNumber();

    Integer saveCloneProject(CloneProjectItem item) throws NumberExistingException;

    void sendContractOverDueEmailNotification(Integer contractID, Integer companyID, EdsRecurrence recurrence);

    void sendEmailNotification(Integer projectID, Integer companyId);

    ArrayList<RejectedImportRecord[]> importProjects(ImportFile importFile, List<String[]> data);

    FileResource[] getProjectAttachments(Integer projectID);

    ListResult<ProjectListItem> getProjectList(ListingFilterParameter filterParameter);

    SelectItem[] searchClientsByProjectId(Integer projectId, String searchKey);

    ProjectViewItem viewProject(Integer objectID);

    void deleteProject(Integer projectId);

    SelectItem[] getProjectStatuses();

    ListResult<SelectItem> getProjectLookUp(ListingFilterParameter filterParameter);

}
