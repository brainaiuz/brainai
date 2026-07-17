package com.edatasite.workforce.gwt.core.server.rpc;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrAdditionalPaymentPresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrAttachmentRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCaseRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCashAdvanceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrChartOfAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseScheduleRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCustomFormConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrDepartmentRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrDocumentRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeAssessmentRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeStepRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrGroupPayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrLeaveRequestConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrNewsRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrOpportunityRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPositionRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSinglePayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrVacancyRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.rbacpermission.TaskPermissionEnum;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.documents.client.rpc.solr.SolrFolderRepresenter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.client.rpc.solr.SolrDepartmentRepresenter.FIELD_LOCATION_ID;

/**
 * User: Abdulaziz
 * Date: Nov 3, 2009
 * Time: 8:05:53 PM
 */
public class QueryBuilderForSolr {


    private static final Set<Character> specialCharacters = new HashSet<>();

    public static String normalaizeKeyword(String keyword, boolean... isLookUp) {
        boolean lookup = (isLookUp != null && isLookUp.length > 0 && isLookUp[0]) && isLookUp[0];
        return normalaizeKeywordByCriteria(keyword, lookup, false);
    }

    public static String normalaizeKeywordForLookUp(String keyword) {
        return normalaizeKeywordByCriteria(keyword, true, false);
    }

    public static String normalaizeKeywordForFacet(String keyword) {
        return normalaizeKeywordByCriteria(keyword, false, true);
    }

    public static String normalaizeKeywordByCriteria(String keyword, boolean isLookUp, boolean isFacet) {
        StringBuilder buffer = new StringBuilder();
        if (specialCharacters.size() == 0) {
            specialCharacters.addAll(Arrays.asList('+', '&', '-', '!', '(', ')', '{', '}', '[', ']', '^', '"', '~', '*', '?', ':', '\\', '/'));
        }
        for (char ch : keyword.toCharArray()) {
            if (specialCharacters.contains(ch)) {
                buffer.append("\\");
            }
            buffer.append(ch);
        }

        if (isFacet) {
            return "\"" + buffer + "\"";
        } else if (isLookUp) {
            return buffer.toString().toLowerCase().trim() + (keyword.endsWith(".") ? "\\*" : "*");
//            return "\"" + buffer.toString().toLowerCase().trim() + "\"^30" + " OR " + buffer.toString().toLowerCase().trim() + "*" + " OR *" + buffer.toString().toLowerCase().trim() + "*";

        } else {
            return "\"" + buffer.toString().toLowerCase().trim() + "\"^30" + " OR *" + buffer.toString().toLowerCase().trim() + "*";
        }
    }

    public static String getQuery(String keyword, SolrDocumentRepresenter doc, Integer[] entityTypes) {
        StringBuilder sql = new StringBuilder();

        sql.append(SolrDocumentRepresenter.getDefaultSearchField() + ":( " + normalaizeKeyword(keyword) + " )");
        if (doc.getCompanyID() != null) {

            sql.append(" AND " + SolrDocumentRepresenter.FIELD_COMPANY_ID + ":" + doc.getCompanyID());
        }
        if (doc.getEntityDescription() != null) {
            sql.append(" AND " + SolrDocumentRepresenter.FIELD_ENTITY_DESCRIPTION + ":" + doc.getEntityDescription());
        }
        if (doc.getEntityName() != null) {
            sql.append(" AND " + SolrDocumentRepresenter.FIELD_ENTITY_NAME + ":" + doc.getEntityName());
        }
        if (entityTypes != null && entityTypes.length > 0) {
            sql.append(" AND " + SolrDocumentRepresenter.FIELD_ENTITY_TYPE + ":(");
            for (Integer entityType : entityTypes) {
                sql.append(" " + entityType);
            }
            sql.append(" )");
        }
        if (doc.getUsersID() != null && doc.getUsersID().length > 0) {
            sql.append(" AND " + SolrDocumentRepresenter.FIELD_USERS_ID + ":(");
            for (Integer uId : doc.getUsersID()) {
                sql.append(" " + uId);
            }
            sql.append(" )");
        }
        return sql.toString();
    }

    public static String getQuery(String keyword, SolrAttachmentRepresenter doc) {
        StringBuilder sql = new StringBuilder();
        keyword = normalaizeKeyword(keyword);
        sql.append("( ");
        sql.append(SolrAttachmentRepresenter.FIELD_ENTITY_NAME + ":( " + keyword + " )");
        sql.append(" OR ");
        sql.append(SolrAttachmentRepresenter.FIELD_ENTITY_DESCRIPTION + ":( " + keyword + " )");
        sql.append(" OR ");
        sql.append(SolrAttachmentRepresenter.FIELD_CONTENT + ":( " + keyword + " )");
        sql.append(" )");
        if (doc.getCompanyID() != null) {
            sql.append(" AND companyID:" + doc.getCompanyID());
        }
        if (doc.getContent() != null) {
            sql.append(" AND content:" + doc.getContent());
        }
        if (doc.getEntityMetaData() != null) {
            sql.append(" AND entityMetaData:" + doc.getEntityMetaData());
        }
        if (doc.getEntityType() != null) {
            sql.append(" AND entityType:" + doc.getEntityType());
        }
        if (doc.getUsersID() != null && doc.getUsersID().length > 0) {
            sql.append(" AND usersID:(");
            for (Integer uId : doc.getUsersID()) {
                sql.append(" " + uId);
            }
            sql.append(" )");
        }
        return sql.toString();
    }

    public static String getQuery(String keyword, SolrEmployeeAssessmentRepresenter doc, Integer[] entityTypes) {
        StringBuilder sql = new StringBuilder();

        sql.append(SolrDocumentRepresenter.getDefaultSearchField() + ":( " + normalaizeKeyword(keyword) + " )");
        if (doc.getCompanyID() != null) {

            sql.append(" AND " + SolrDocumentRepresenter.FIELD_COMPANY_ID + ":" + doc.getCompanyID());
        }

        if (doc.getEntityName() != null) {
            sql.append(" AND " + SolrDocumentRepresenter.FIELD_ENTITY_NAME + ":" + doc.getEntityName());
        }
        if (entityTypes != null && entityTypes.length > 0) {
            sql.append(" AND " + SolrDocumentRepresenter.FIELD_ENTITY_TYPE + ":(");
            for (Integer entityType : entityTypes) {
                sql.append(" " + entityType);
            }
            sql.append(" )");
        }
        if ((doc.getUsersID() != null && doc.getUsersID().length > 0) && (doc.getManagersID() != null && doc.getManagersID().length > 0)) {
            sql.append(" AND ");
            sql.append("( ");
            sql.append(SolrDocumentRepresenter.FIELD_USERS_ID + ":(");
            for (Integer uId : doc.getUsersID()) {
                sql.append(" " + uId);
            }
            sql.append(" )");
            sql.append(" OR ");
            sql.append(SolrEmployeeAssessmentRepresenter.FIELD_MANAGERS_ID + ":(");
            for (Integer mId : doc.getManagersID()) {
                sql.append(" " + mId);
            }
            sql.append(" )");
            sql.append(" ) ");
        } else {
            if (doc.getUsersID() != null && doc.getUsersID().length > 0) {
                sql.append(" AND " + SolrDocumentRepresenter.FIELD_USERS_ID + ":(");
                for (Integer uId : doc.getUsersID()) {
                    sql.append(" " + uId);
                }
                sql.append(" )");
            }
            if (doc.getManagersID() != null && doc.getManagersID().length > 0) {
                sql.append(" AND " + SolrEmployeeAssessmentRepresenter.FIELD_MANAGERS_ID + ":(");
                for (Integer uId : doc.getManagersID()) {
                    sql.append(" " + uId);
                }
                sql.append(" )");

            }
        }
        return sql.toString();

    }

    /**
     * <h1>... Task Assignee Facet Filter Solr Query generate ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Updated date {13:43 04/06/2011}...</h3>
     *
     * @param taskFacet
     * @param edsUser
     * @return
     */
    public static String getTaskFacetFilterAssigneesQuery(FacetFilterRpc taskFacet, EdsUser edsUser) {
        Set<EdsGroup> groups = edsUser.getMembershipGroups();
        StringBuilder solrQuery = new StringBuilder();
        boolean appendOperator;
        boolean isClient = edsUser.isClientContact();
        StringBuilder userViewers = new StringBuilder();
        userViewers.append("(");
        userViewers.append(SolrTaskRepresenter.FIELD_VIEWERS).append(":").append(edsUser.getObjectID()).append(SolrTaskRepresenter.SPLIT).append(SolrTaskRepresenter.FIELD_USER_ID);
        for (EdsGroup group : groups) {
            userViewers.append(" OR ").append(SolrTaskRepresenter.FIELD_VIEWERS).append(":").append(group.getObjectID()).append(SolrTaskRepresenter.SPLIT).append(SolrTaskRepresenter.FIELD_GROUP_ID);
        }
        userViewers.append(")");
        // assigness
        if (taskFacet != null && taskFacet.getFacetContentMap() != null && taskFacet.getFacetContentMap().size() > 0
                && taskFacet.getFacetContentMap().containsKey(FacetContentType.TaskFacetFilter.getContentCode()[5])
                && taskFacet.getFacetContentMap().get(FacetContentType.TaskFacetFilter.getContentCode()[5]).getFacetItems().length != 0) {
            SelectItem[] items = taskFacet.getFacetContentMap().get(FacetContentType.TaskFacetFilter.getContentCode()[5]).getFacetItems();
            solrQuery.append(" AND (");
            appendOperator = false;
            for (SelectItem item : items) {
                if (appendOperator) {
                    solrQuery.append(" OR ");
                } else {
                    appendOperator = true;
                }
                if (!isClient) {
                    if (item.getId() != null && item.getId() == -1) {
                        solrQuery.append(" ((-").append(SolrTaskRepresenter.FIELD_USER_ID_NAME).append(":").append("[* TO *] AND *:*)");
                        solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_RELATIONSHIPS).append(":").append(EdsRelationship.TASK_NOT_ASSIGNEE);
                        solrQuery.append(" AND  ").append(userViewers).append(")");
                    } else {
                        solrQuery.append("(").append(SolrTaskRepresenter.FIELD_VIEWERS).append(":").append(item.getId() + SolrTaskRepresenter.SPLIT + SolrTaskRepresenter.FIELD_USER_ID);
                        solrQuery.append(" AND  ").append(userViewers);
                        solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_ASSIGNEE_ID).append(":").append(item.getId());
                        solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_RELATIONSHIPS).append(":").append(EdsRelationship.TASK_ASSIGNEE).append(")");
                    }
                } else {
                    if (item.getId() != null && item.getId() == -1) {
                        solrQuery.append("((-").append(SolrTaskRepresenter.FIELD_USER_ID_NAME).append(":").append("[* TO *] AND *:*)");
                        solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_RELATIONSHIPS).append(":").append(EdsRelationship.TASK_NOT_ASSIGNEE).append(")");
                    } else {
                        solrQuery.append(SolrTaskRepresenter.FIELD_VIEWERS).append(":").append(item.getId() + SolrTaskRepresenter.SPLIT + SolrTaskRepresenter.FIELD_USER_ID);
                        solrQuery.append(" AND  ").append(SolrTaskRepresenter.FIELD_ASSIGNEE_ID).append(":").append(item.getId());
                        solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_RELATIONSHIPS).append(":").append(EdsRelationship.TASK_ASSIGNEE);
                    }
                }
            }
            solrQuery.append(" ) ");
        }

        return solrQuery.toString();
    }

    /**
     * <h1>... Task List Facet Filter Assignee Content Solr Query generated ....</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {16:50 03/06/2011} ...</h3>
     *
     * @param edsUser
     * @param edsCompany
     * @param taskFacetFilter
     * @param groupList
     * @return
     */
    public static String getTaskAssigneeCoreSolrQuery(EdsUser edsUser, EdsCompany edsCompany, FacetFilterRpc taskFacetFilter, Set<EdsGroup> groupList) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrTaskRepresenter.FIELD_COMPANY_ID).append(":").append(edsCompany.getObjectID());
        if (!edsUser.isClientContact()) {
            if (taskFacetFilter == null || (taskFacetFilter.getFacetContentMap().containsKey(FacetContentType.TaskFacetFilter.getContentCode()[5])
                    && taskFacetFilter.getFacetContentMap().get(FacetContentType.TaskFacetFilter.getContentCode()[5]).getFacetItems().length == 0)) {
                solrQuery.append(" AND (").append(SolrTaskRepresenter.FIELD_VIEWERS).append(":").
                        append(edsUser.getObjectID() + SolrTaskRepresenter.SPLIT + SolrTaskRepresenter.FIELD_USER_ID);
                for (EdsGroup group : groupList) {
                    solrQuery.append(" OR ").append(SolrTaskRepresenter.FIELD_VIEWERS).append(":").
                            append(group.getObjectID() + SolrTaskRepresenter.SPLIT + SolrTaskRepresenter.FIELD_GROUP_ID);
                }
                solrQuery.append(") ");
            }
        } else { // this query running current user has role client
            solrQuery.append(" AND (").append(SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID).append(":").append(edsUser.getClientContact().getClientID()).append(")");
        }
        if (taskFacetFilter.getCustomDataValue(FacetFilterCutomField.PROJECTID) != null) {
            solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_TASK_PROJECT_ID).append(":").append(taskFacetFilter.getCustomDataValue(FacetFilterCutomField.PROJECTID));
        }
        solrQuery.append(" AND (").append(SolrTaskRepresenter.FIELD_RELATIONSHIPS).append(":").append(EdsRelationship.TASK_ASSIGNEE);
        solrQuery.append(" OR ").append(SolrTaskRepresenter.FIELD_RELATIONSHIPS).append(":").append(EdsRelationship.TASK_NOT_ASSIGNEE).append(")");
        return solrQuery.toString();
    }

    /**
     * <h1>... Task List Core Solr Query generate ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created data {13:38 04/06/2011}  ...</h3>
     *
     * @param edsUser
     * @param edsCompany
     * @param taskFacetFilter
     * @return
     */
    public static String getTaskCoreSolrQuery(EdsUser edsUser, EdsCompany edsCompany, FacetFilterRpc taskFacetFilter, com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter filterParameter, EdsGroup adminGroup) {
        Set<EdsGroup> membershipsGroups = edsUser.getMembershipGroups();
        boolean showAllTask = false;
        if (filterParameter.isFromMobile() && ServerUtils.hasPermission(PermissionConstants.PM_SHOW_ALL_TASKS)) {
            membershipsGroups.add(adminGroup);
            showAllTask = true;
        } else if (ServerUtils.isCrm() && ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_TASKS_LIST)) {
            membershipsGroups.add(adminGroup);
            showAllTask = true;
        } else if (!ServerUtils.isCrm() && ServerUtils.hasPermission(PermissionConstants.PM_SHOW_ALL_TASKS)) {
            membershipsGroups.add(adminGroup);
            showAllTask = true;
        }

        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrTaskRepresenter.FIELD_COMPANY_ID).append(":").append(edsCompany.getObjectID());

        if (filterParameter.getDepartmentId() != null) {
            solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_TASK_USER_DEPARTMENT_ID).append(":").append(filterParameter.getDepartmentId());
        }
        // Set Project id
        if (filterParameter.getProjectId() != null) {
            solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_TASK_PROJECT_ID).append(":").append(filterParameter.getProjectId());
        }
        if (filterParameter.getStatusID() != null) {
            solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_TASK_STATUS_ID).append(":").append(filterParameter.getStatusID());
        }
        if (filterParameter.getRelationType() != null && !filterParameter.isFromCase()) {
            String relationID = filterParameter.getRelationID() == null ? "[* TO *] AND *:*" : filterParameter.getRelationID().toString();
            solrQuery.append(" AND (").append(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID).append(filterParameter.getRelationType()).append(":(").append(relationID).append("))");
        }

        if (filterParameter.isAssignedItems()) {
            solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_ASSIGNEE_ID).append(":").append(edsUser.getObjectID()).append(" ");
        }
        if (StringUtils.isNotBlank(filterParameter.getExcludedType())) { //exluded statuses
            solrQuery.append(" AND -").append(SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_CODE).append(":(").append(filterParameter.getExcludedType()).append(") ");
        }
        if (!edsUser.isClientContact()) {
            if (taskFacetFilter == null || taskFacetFilter.isOverallSearch() ||
                    (
                            (!taskFacetFilter.getFacetContentMap().containsKey(FacetContentType.TaskFacetFilter.getContentCode()[5])
                                    || taskFacetFilter.getFacetContentMap().get(FacetContentType.TaskFacetFilter.getContentCode()[5]).getFacetItems().length == 0
                            )
                    )
            ) {
                if (!showAllTask) {
                    solrQuery.append(" AND (");
                    solrQuery.append("(").append(SolrTaskRepresenter.FIELD_TRUSTEE_TYPE).append(":").append(EdsTrusteeType.USER);
                    solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_USER_ID).append(":").append(edsUser.getObjectID()).append(")");
                    if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_UNASSIGNED_TASKS)) {
                        solrQuery.append(" OR (-").append(SolrTaskRepresenter.FIELD_ASSIGNEE_NAMES).append(":[* TO *] AND *:*)");
                    }
                    for (EdsGroup group : membershipsGroups) {
                        solrQuery.append(" OR (");
                        solrQuery.append(SolrTaskRepresenter.FIELD_GROUP_ID).append(":").append(group.getObjectID()).append(" ").append("AND ");
                        solrQuery.append(SolrTaskRepresenter.FIELD_TRUSTEE_TYPE).append(":").append(EdsTrusteeType.GROUP).append(" ");
                        solrQuery.append(")");
                    }
                    solrQuery.append(")");
                    solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_PERMISSIONS).append(":").append(TaskPermissionEnum.VIEW.getCode());
                }
            }
        } else { // this query running current user has role client
            if (!edsUser.hasRole(Constants.SUPPLIER) || edsUser.getRoleIds().contains(EdsRole.CLIENT)) {
                solrQuery.append(" AND (").append(SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID).append(":").append(edsUser.getClientContact().getClientID()).append(")");
            }
        }

        // Set Search key
        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            if (filterParameter.isFromMobile()) {
                solrQuery.append(" AND (").append(SolrTaskRepresenter.FIELD_TASK_NAME).append(":(").append(normalaizeKeyword(filterParameter.getSearchKey(), true)).append(")");
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateApiSearchQuery(solrQuery, getApiSearchFields(), filterParameter.getSearchKey());
                solrQuery.append(")");
            } else {
                solrQuery.append(" AND (").append(SolrTaskRepresenter.FIELD_COMPOSITE).append(":").append(SolrSearchUtils.normalaizeKeyword(filterParameter.getSearchKey()));
                if (!filterParameter.isLookUp()) {
                    SolrSearchUtils searchUtils = new SolrSearchUtils();
                    searchUtils.generateSearchQuery(solrQuery, getDynSearchFields(), filterParameter.getSearchKey());
                }
                solrQuery.append(")");
            }
        } else if (taskFacetFilter != null && taskFacetFilter.getSearchKey() != null && !"".equals(taskFacetFilter.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_COMPOSITE).append(":").append(SolrSearchUtils.normalaizeKeyword(taskFacetFilter.getSearchKey()));
        }
        // Set Department Id

//        if (StringUtils.isNotBlank(filterParameter.getStatusCode())) {
//            solrQuery.append(" AND (").append(SolrTaskRepresenter.FIELD_TASK_STATUS_ID_CODE).append(":").append(filterParameter.getStatusCode()).append(") ");
//        }
        return solrQuery.toString();
    }


    public static String getEmployeeStepCoreSolrQuery(EdsUser edsUser, ListingFilterParameter filterParameter) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrEmployeeStepRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        if (!ServerUtils.hasPermission(PermissionConstants.HRMS_SEE_ALL_EMPLOYEE_STEPS_LIST) && edsUser != null) {
            solrQuery.append(" AND ").append(SolrEmployeeStepRepresenter.FIELD_TYPE_CODE).append(":").append(EdsStepEmployee.EMPLOYEE_TYPE);
            solrQuery.append(" AND (").append(SolrEmployeeStepRepresenter.FIELD_CURRENT_APPROVER_ID).append(":").append(edsUser.getObjectID());
            solrQuery.append(" OR ").append(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_ID).append(":").append(edsUser.getObjectID()).append(") ");
        }
        if (filterParameter.getSearchKey() != null && !"".equals(filterParameter.getSearchKey())) {
            solrQuery.append(" AND (").append(SolrEmployeeStepRepresenter.FIELD_COMPOSITE).append(":( ").append(QueryBuilderForSolr.normalaizeKeyword(filterParameter.getSearchKey(), filterParameter.isLookUp())).append(" ))");
        }
        if (filterParameter.getStepID() != null) {
            solrQuery.append(" AND (").append(SolrEmployeeStepRepresenter.FIELD_ONBOARDING_STEP_ID).append(":(").append(filterParameter.getStepID()).append("))");
        }
//        solrQuery.append(" AND (").append(SolrEmployeeStepRepresenter.FIELD_ARCHIVED).append(":").append(filterParameter.isShowArchived()).append(")");
        solrQuery.append(" AND (").append(filterParameter.getWorkflowID() != null ? "" : "-").append(SolrEmployeeStepRepresenter.FIELD_WORKFLOW_ID).append(":").append(filterParameter.getWorkflowID() != null ? filterParameter.getWorkflowID() : "[* TO *] AND *:*").append(")");
        return solrQuery.toString();
    }

    public static String getProductsServicesCoreSolrQuery(ListingFilterParameter filterParameter) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrProductServiceRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID())
                .append(" AND ")
                .append(SolrProductServiceRepresenter.FIELD_DOC_TYPE).append(":").append(SolrProductServiceRepresenter.PRODUCT_SOLR_DOC);

        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            solrQuery.append(" AND ").append(filterParameter.isFromMobile() ? " ( " : "").append(SolrEventRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(filterParameter.getSearchKey()));
            if (!filterParameter.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(solrQuery, getDynSearchFields(), filterParameter.getSearchKey());
            }
            solrQuery.append(")");
        }

        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            if (filterParameter.isFromMobile()) {
                solrQuery.append(" AND (").append(SolrProductServiceRepresenter.FIELD_PRODUCT_NAME).append(":(").append(SolrSearchUtils.normalaizeKeyword(filterParameter.getSearchKey())).append(")");
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateApiSearchQuery(solrQuery, getApiSearchFields(), filterParameter.getSearchKey());
                solrQuery.append(")");
            } else {
                solrQuery.append(" AND ").append(SolrEventRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(filterParameter.getSearchKey()));
                if (!filterParameter.isLookUp()) {
                    SolrSearchUtils searchUtils = new SolrSearchUtils();
                    searchUtils.generateSearchQuery(solrQuery, getDynSearchFields(), filterParameter.getSearchKey());
                }
                solrQuery.append(")");
            }
        }
        return solrQuery.toString();
    }

    public static String getProductsServicesCoreSolrQueryCF(ListingFilterParameter filterParameter, List<String> customFields) {
        StringBuffer solrQuery = new StringBuffer();
        //solrQuery.append(SolrProductServiceRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        solrQuery.append(SolrProductServiceRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID())
                .append(" AND ")
                .append(SolrProductServiceRepresenter.FIELD_DOC_TYPE).append(":").append(SolrProductServiceRepresenter.PRODUCT_SOLR_DOC);

//        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
//
//            SolrSearchUtils searchUtils = new SolrSearchUtils();
//            if (CollectionUtils.isNotEmpty(customFields)) {
//                searchUtils.generateAndSearchQuery(solrQuery, getCustomFields(customFields), filterParameter.getSearchKey());
//            }
//        }
        if (CollectionUtils.isNotEmpty(customFields) && StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            solrQuery.append(" AND " + customFields.get(0) + ":" + "\"").append(filterParameter.getSearchKey()).append("\"");
        }
        return solrQuery.toString();
    }

    public static String getCourseBookingCoreSolrQuery(com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter filterParameter) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrEventRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        if (filterParameter.getSearchKey() != null && !"".equals(filterParameter.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrEventRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(filterParameter.getSearchKey())).append(" )");
        }
        return solrQuery.toString();
    }

    public static String getCourseScheduleCoreSolrQuery(com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter filterParameter) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrEventRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        if (filterParameter.getSearchKey() != null && !"".equals(filterParameter.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrEventRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(filterParameter.getSearchKey())).append(" )");
        }
        return solrQuery.toString();
    }

    /*public static String getExpenseReportsCoreSolrQuery(FacetFilterRpc reportsFacetFilter, ListingFilterParameter filterParameter, EdsUser user, EdsCompany company) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrExpenseReportRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        if (filterParameter.getSearchKey() != null && !"".equals(filterParameter.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrExpenseReportRepresenter.FIELD_COMPOSITE).append(":( ").append(QueryBuilderForSolr.normalaizeKeyword(filterParameter.getSearchKey()));
            if (!filterParameter.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(solrQuery, getDynSearchFields(), filterParameter.getSearchKey());
            }
            solrQuery.append(")");
        }
        if (filterParameter.getProjectId() != null) {
            solrQuery.append(" AND (").append(SolrExpenseReportRepresenter.FIELD_RELATED_PROJECT_ID).append(":").append(filterParameter.getProjectId())
                    .append(" OR ").append(SolrExpenseReportRepresenter.FIELD_MULTI_PROJECT_ID).append(":").append(filterParameter.getProjectId()).append(") ");
        }
        if (StringUtils.isNotBlank(filterParameter.getStatusCode())) {
            solrQuery.append(" AND (").append(SolrExpenseReportRepresenter.FIELD_STATUS_CODE).append(":").append(filterParameter.getStatusCode()).append(") ");
        }
        if (filterParameter.isFromMobile() && !(ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM) || ServerUtils.hasPermission(PermissionConstants.HRMS_CAN_APPROVE_EXPENSE_CLAIM))) {
            solrQuery.append(" AND ");
            solrQuery.append("( ");
            solrQuery.append(SolrExpenseReportRepresenter.FIELD_APPROVER_ID).append(":").append(user.getObjectID());
            solrQuery.append(" )");
        } else if (!ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_FULL_LIST_ACCESS)) {
            solrQuery.append(" AND ");
            solrQuery.append("( ");
            solrQuery.append(SolrExpenseReportRepresenter.FIELD_APPROVER_ID).append(":").append(user.getObjectID());
            solrQuery.append(" OR ").append(SolrExpenseReportRepresenter.FIELD_APPROVER2_ID).append(":").append(user.getObjectID());
            solrQuery.append(" OR ").append(SolrExpenseReportRepresenter.FIELD_REPORTER_ID).append(":").append(user.getObjectID());
            solrQuery.append(" )");
        }
        if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_LIST) && ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_COMPANY_EXPENSE_LIST)) {
            //it's ok do not nothing
        } else if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_LIST)) {
            solrQuery.append(" AND ");
            solrQuery.append("( ");
            solrQuery.append(SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE).append(":").append(Boolean.FALSE);
            solrQuery.append(" )");
        } else if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_COMPANY_EXPENSE_LIST)) {
            solrQuery.append(" AND ");
            solrQuery.append("( ");
            solrQuery.append(SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE).append(":").append(Boolean.TRUE);
            solrQuery.append(" )");
        }
        return solrQuery.toString();
    }*/

    /**
     * <h1>... This is method uses Lead List Solr Core Query and Facet Filter in Assingnee Content  ...</h1>
     * <br>
     * <h2>... Changed by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last Updated  {14:49 11/06/2011} ...</h3>
     *
     * @param edsCompany
     * @param edsUser
     * @param leadFacet  @return
     */
    public static String getLeadListFacetFilterAssigneeQuery(EdsCompany edsCompany, EdsUser edsUser, ListingFilterParameter fp, FacetFilterRpc leadFacet, List<Integer> accountIDs) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrContactRepresenter.FIELD_COMPANY_ID).append(":").append(edsCompany.getObjectID());
        solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_CONTACT_TYPE).append(":").append(EdsCrmContact.LEAD_CONTACT);
        //background uchun
        boolean isPrivileged = ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_LEADS_LIST, edsUser);
        if (!isPrivileged) {
            solrQuery.append(" AND (").append(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID).append(":").append(edsUser.getObjectID());
            solrQuery.append(" OR ").append(SolrContactRepresenter.FIELD_LEAD_BACKUP_ASSIGNEE_ID).append(":").append(edsUser.getObjectID());
            solrQuery.append(" OR ").append(SolrContactRepresenter.FIELD_OWNER_ID).append(":").append(edsUser.getObjectID());
            solrQuery.append(" OR ").append(SolrContactRepresenter.FIELD_CRM_ACCOUNT_OWNER_ID).append(":").append(edsUser.getObjectID());
            solrQuery.append(")");
        }
        if (fp.getCampaignID() == null && leadFacet != null && leadFacet.getCustomDataValue(FacetFilterCutomField.RELATION_ID) != null && leadFacet.getCustomDataValue(FacetFilterCutomField.RELATION_TYPE) != null && RelationItem.TYPE_CAMPAIGN.equalsIgnoreCase(leadFacet.getCustomDataValue(FacetFilterCutomField.RELATION_TYPE))) {
            fp.setCampaignID(Integer.parseInt(leadFacet.getCustomDataValue(FacetFilterCutomField.RELATION_ID)));
        }
        if (fp.getCampaignID() != null) {
            solrQuery.append(" AND (").append(SolrContactRepresenter.FIELD_CAMPAIGN_ID).append(":").append(fp.getCampaignID()).append(")");
        }
        if (fp.getWebFormID() != null) {
            solrQuery.append(" AND (").append(SolrContactRepresenter.FIELD_LEAD_SOURCE_ID).append(":").append(fp.getWebFormID()).append(")");
        }
        // ---- from kanban board ----
        if (Integer.valueOf(-1).equals(fp.getColumnMetadataId())) {
            solrQuery.append(" AND -(").append(SolrContactRepresenter.FIELD_LEAD_STATUS_ID).append(":").append("[* TO *]").append(")");
        } else if (fp.getColumnMetadataId() != null) {
            solrQuery.append(" AND (").append(SolrContactRepresenter.FIELD_LEAD_STATUS_ID).append(":").append(fp.getColumnMetadataId()).append(")");
        }
        //----------------------------
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            if (fp.isFromMobile()) {
                solrQuery.append(" AND (").append(SolrContactRepresenter.FIELD_LEAD_NAME_COMPOSITE).append(":(").append(normalaizeKeyword(fp.getSearchKey(), true)).append(")");
            } else {
                solrQuery.append(" AND (").append(fp.isLookUp() ? SolrContactRepresenter.FIELD_LEAD_NAME_COMPOSITE : SolrContactRepresenter.FIELD_LEAD_COMPOSITE).append(":(").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), fp.isLookUp())).append(")");
            }
            if (!fp.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                if (accountIDs != null && accountIDs.size() > 0) {
                    accountIDs = accountIDs.size() > 200 ? accountIDs.subList(0, 200) : accountIDs;
                    solrQuery.append(" OR ACCOUNT_ID:(").append(ServerUtils.getAsCommoDelimited(accountIDs, "0", " ")).append(")");
                }
                if (fp.isFromMobile()) {
                    searchUtils.generateApiSearchQuery(solrQuery, getApiSearchFields(), fp.getSearchKey());
                } else {
                    searchUtils.generateSearchQuery(solrQuery, getCrmContactSearchFields(), fp.getSearchKey());
                }
            }
            solrQuery.append(")");
        }
        boolean appendOperator = false;
        if (leadFacet != null) {
            String assigneeKey = FacetContentType.LeadFacetFilter.getContentCode()[5];
            if (leadFacet.getFacetContentMap().containsKey(assigneeKey) && leadFacet.getFacetContentMap().get(assigneeKey).getFacetItems().length != 0) {
                SelectItem[] items = leadFacet.getFacetContentMap().get(assigneeKey).getFacetItems();
                solrQuery.append(" AND (");
                appendOperator = false;
                for (SelectItem item : items) {
                    if (appendOperator) {
                        solrQuery.append(" OR ");
                    } else {
                        appendOperator = true;
                    }
                    if (item.getId() == -1) {
                        solrQuery.append("((-").append(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID).append(":[* TO *] AND *:*)");// Checking fiel for null
                        solrQuery.append(" AND (-").append(SolrContactRepresenter.FIELD_LEAD_BACKUP_ASSIGNEE_ID).append(":[* TO *] AND *:*))");// Checking fiel for null
                    } else {
                        solrQuery.append(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID).append(":").append(item.getId());
                        solrQuery.append(" OR ").append(SolrContactRepresenter.FIELD_LEAD_BACKUP_ASSIGNEE_ID).append(":").append(item.getId());
                    }
                }
                solrQuery.append(") ");
            }
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            if (leadFacet.getStartDate() != null && leadFacet.getEndDate() != null) {
//                solrQuery.append(" AND (((").append(SolrContactRepresenter.FIELD_CREATION_DATE).append(":[").append(format.format(leadFacet.getStartDate())).append(" TO * ]) ");
//                solrQuery.append(" AND (").append(SolrContactRepresenter.FIELD_CREATION_DATE).append(":[ * TO ").append(format.format(leadFacet.getEndDate())).append(" ]))");
//                solrQuery.append(" OR ((").append(SolrContactRepresenter.FIELD_UPDATE_DATE).append(":[").append(format.format(leadFacet.getStartDate())).append(" TO * ])");
//                solrQuery.append(" AND (").append(SolrContactRepresenter.FIELD_UPDATE_DATE).append(":[ * TO ").append(format.format(leadFacet.getEndDate())).append(" ])))");
//            }
            if (leadFacet.getSelectedDateSolrCodeName() != null && (leadFacet.getStartDate() != null || leadFacet.getEndDate() != null)) {
                solrQuery.append(" AND (").append(leadFacet.getSelectedDateSolrCodeName()).append(":[").append(format.format(leadFacet.getStartDate())).append(" TO ")
                        .append(leadFacet.getEndDate() != null ? format.format(leadFacet.getEndDate()) : " * ").append("])");
            }
        }
        return solrQuery.toString();
    }

    public static String getCandidateListFacetFilterAssigneeQuery(EdsCompany edsCompany, EdsUser edsUser, ListingFilterParameter fp, FacetFilterRpc leadFacet, List<Integer> accountIDs) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrContactRepresenter.FIELD_COMPANY_ID).append(":").append(edsCompany.getObjectID());
        solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_CONTACT_TYPE).append(":").append(EdsCrmContact.CANDIDATE);
        if (fp.isShortList()) {
            solrQuery.append(" AND (");
            solrQuery.append(SolrContactRepresenter.FIELD_IS_SHORT_LIST).append(":").append(Boolean.TRUE);
            solrQuery.append(" AND ").append(" NOT (").append(SolrCaseRepresenter.STATUS_CODE).append(":").append(ContactListItem.C_S_HIRED);
            solrQuery.append(" OR ").append(SolrCaseRepresenter.STATUS_CODE).append(":").append(ContactListItem.C_S_REJECTED).append(")");
            solrQuery.append(")");
        }
        checkCandidatePermissions(solrQuery, fp, edsUser);
        if (Integer.valueOf(-1).equals(fp.getColumnMetadataId())) {
            solrQuery.append(" AND -(").append(SolrCaseRepresenter.STATUS_ID).append(":").append("[* TO *]").append(")");
        } else if (fp.getColumnMetadataId() != null) {
            solrQuery.append(" AND (").append(SolrCaseRepresenter.STATUS_ID).append(":").append(fp.getColumnMetadataId()).append(")");
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            solrQuery.append(" AND (").append(fp.isLookUp() ? SolrContactRepresenter.FIELD_CONTACT_NAME_COMPOSITE : SolrContactRepresenter.FIELD_COMPOSITE).append(":(").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), fp.isLookUp())).append(")");
            if (!fp.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                if (accountIDs != null && accountIDs.size() > 0) {
                    accountIDs = accountIDs.size() > 200 ? accountIDs.subList(0, 200) : accountIDs;
                    solrQuery.append(" OR ACCOUNT_ID:(").append(ServerUtils.getAsCommoDelimited(accountIDs, "0", " ")).append(")");
                }
                searchUtils.generateSearchQuery(solrQuery, getCrmContactSearchFields(), fp.getSearchKey());
            }
            solrQuery.append(")");
        }
        boolean appendOperator = false;
        if (leadFacet != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            if (leadFacet.getStartDate() != null && leadFacet.getEndDate() != null) {
                solrQuery.append(" AND (((").append(SolrContactRepresenter.FIELD_CREATION_DATE).append(":[").append(format.format(leadFacet.getStartDate())).append(" TO * ]) ");
                solrQuery.append(" AND (").append(SolrContactRepresenter.FIELD_CREATION_DATE).append(":[ * TO ").append(format.format(leadFacet.getEndDate())).append(" ]))");
                solrQuery.append(" OR ((").append(SolrContactRepresenter.FIELD_UPDATE_DATE).append(":[").append(format.format(leadFacet.getStartDate())).append(" TO * ])");
                solrQuery.append(" AND (").append(SolrContactRepresenter.FIELD_UPDATE_DATE).append(":[ * TO ").append(format.format(leadFacet.getEndDate())).append(" ])))");
            }
        }
        return solrQuery.toString();
    }

    public static String getCandidateListSolrQuery(ListingFilterParameter fp, FacetFilterRpc contactFilter, EdsCompany edsCompany, EdsUser user) {
        StringBuffer solrQuery = new StringBuffer("(");
        solrQuery.append(SolrContactRepresenter.FIELD_COMPANY_ID).append(":").append(edsCompany.getObjectID());
        solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_CONTACT_TYPE).append(":").append(EdsCrmContact.CANDIDATE);
        if (fp.isShortList()) {
            solrQuery.append(" AND (");
            solrQuery.append(SolrContactRepresenter.FIELD_IS_SHORT_LIST).append(":").append(Boolean.TRUE);
            solrQuery.append(" AND ").append(" NOT (").append(SolrCaseRepresenter.STATUS_CODE).append(":").append(ContactListItem.C_S_HIRED);
            solrQuery.append(" OR ").append(SolrCaseRepresenter.STATUS_CODE).append(":").append(ContactListItem.C_S_REJECTED).append(")");
            solrQuery.append(")");
        }
        if (fp.getRelationID() != null) {
            solrQuery.append(" AND (");
            solrQuery.append(SolrContactRepresenter.FIELD_VACANCY_ID).append(":").append(fp.getRelationID());
            solrQuery.append(")");
        }
        if (fp.isSelectCandidate()) {
            solrQuery.append(" AND ");
            solrQuery.append(SolrContactRepresenter.FIELD_IS_SHORT_LIST).append(":").append(Boolean.FALSE);
            solrQuery.append(" AND NOT ").append(SolrCaseRepresenter.STATUS_CODE).append(":").append(ContactListItem.C_S_REJECTED);
            solrQuery.append(" AND NOT ").append(SolrCaseRepresenter.STATUS_CODE).append(":").append(ContactListItem.C_S_UNQUALIFIED);
            solrQuery.append(" AND NOT ").append(SolrCaseRepresenter.STATUS_CODE).append(":").append(ContactListItem.C_S_PLACED);
            solrQuery.append(" AND NOT ").append(SolrCaseRepresenter.STATUS_CODE).append(":").append(ContactListItem.C_S_OFFER_DECLINED);
            solrQuery.append(" AND NOT ").append(SolrCaseRepresenter.STATUS_CODE).append(":").append(ContactListItem.C_S_OFFER_WITHDRAWN);
        }
        if (fp.getLocationId() != null){
            solrQuery.append(" AND ");
            solrQuery.append(SolrContactRepresenter.FIELD_PREFERRED_LOCATION_ID).append(":").append(fp.getLocationId());
        }

        checkCandidatePermissions(solrQuery, fp, user);

        // Set Search key
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            solrQuery.append(" AND (").append(fp.isLookUp() ? (Constants.BY_EMAIL.equals(fp.getLookUpBy()) ? SolrContactRepresenter.FIELD_EMAIL_COMPOSITE : SolrContactRepresenter.FIELD_CONTACT_NAME_COMPOSITE) : SolrContactRepresenter.FIELD_COMPOSITE).append(":(").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), fp.isLookUp())).append(")");
            if (!fp.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(solrQuery, getCrmContactSearchFields(), fp.getSearchKey());
            }
            solrQuery.append(")");
        }
        if (fp.getAccountID() != null && !"".equals(fp.getAccountID())) {
            solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID).append(":").append(fp.getAccountID());
        }
        solrQuery.append(")");
        return solrQuery.toString();
    }

    private static StringBuffer checkCandidatePermissions(StringBuffer solrQuery, ListingFilterParameter fp, EdsUser user) {
        boolean showOwned = ServerUtils.hasPermission(PermissionConstants.HRMS_SHOW_OWNED_CANDIDATES);
        boolean showRelated = ServerUtils.hasPermission(PermissionConstants.HRMS_SHOW_RELATED_CANDIDATES);
        boolean seeOwn = ServerUtils.hasPermission(PermissionConstants.HRMS_CANDIDATE_SEE_OWN);
        boolean showlocation = ServerUtils.hasPermission(PermissionConstants.HRMS_SHOW_LOCATION_CANDIDATES);
        if (!ServerUtils.hasPermission(PermissionConstants.HRMS_SHOW_ALL_CANDIDATES)) {

            if (showOwned && showRelated) {
                solrQuery.append(" AND (");
                solrQuery.append(SolrContactRepresenter.FIELD_OWNER_ID).append(":").append(fp.getUserID());
                solrQuery.append(" OR ").append(SolrContactRepresenter.FIELD_CREATOR_ID).append(":").append(fp.getUserID());
                solrQuery.append(")");
            } else if (showOwned) {
                solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_OWNER_ID).append(":").append(fp.getUserID());
            } else if (showRelated) {
                solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_CREATOR_ID).append(":").append(fp.getUserID());
            } else if (seeOwn) {
                solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_OWNER_ID).append(":").append(user.getObjectID());
            } else if (showlocation) {
                solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_PREFERRED_LOCATION_ID).append(":").append(user.getLocation() != null ? user.getLocation().getObjectID() : null);
            } else {
                solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_CONTACT_ID).append(":").append(0);
            }
        }
        return solrQuery;
    }

    public static Map<String, Double> getOpportunitySearchFields() {
        Map<String, Double> fields = new HashMap<>();
        fields.put(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NAME_COMPOSITE, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrOpportunityRepresenter.FIELD_ACCOUNT_NAME_COMPOSITE, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrOpportunityRepresenter.FIELD_CRM_CONTACT_NAME_COMPOSITE, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrOpportunityRepresenter.FIELD_CRM_CONTACT_NAME_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrOpportunityRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    public static Map<String, Double> getCrmContactSearchFields() {
        Map<String, Double> fields = new HashMap<>();
        fields.put(SolrContactRepresenter.FIELD_CONTACT_FIRST_COMPOSITE, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrContactRepresenter.FIELD_CONTACT_LAST_COMPOSITE, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrContactRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrContactRepresenter.FIELD_PRIMARIES_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    /**
     * This is specific method for api. Lead search by only name and account
     *
     * @return java.util.Map
     */
    public static Map<String, Double> getApiSearchFields() {
        Map<String, Double> fields = new HashMap<>();
        fields.put(SolrCrmAccountRepresenter.FIELD_API_COMPOSITE, SolrSearchUtils.HIGH_PRIORITY);
        return fields;
    }

    /**
     * This is specific method for Javlon's Apteka. Search only by specified custom fields
     *
     * @return java.util.Map
     */
    public static Map<String, Double> getCustomFields(List<String> customFields) {
        Map<String, Double> fields = new HashMap<>();
        customFields.forEach(customField -> fields.put(customField, SolrSearchUtils.HIGH_PRIORITY));
        return fields;
    }

    public static Map<String, Double> getDynSearchFields() {
        Map<String, Double> fields = new HashMap<>();
        fields.put(SolrTaskRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    /**
     * <h1>... Client List Core Solr query generated ....</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {12:56 11/06/2011} ...</h3>
     *
     * @param fp
     * @param company
     * @param customer
     * @return
     */
    public static String getClientListSolrQuery(ListingFilterParameter fp, FacetFilterRpc facetFilter, EdsCompany company, EdsReference customer, List<Integer> customerIDList, EdsUser edsUser, String... codeNameList) {

        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrCrmAccountRepresenter.FIELD_COMPANY_ID).append(":").append(company.getObjectID()).append(" AND ");
        solrQuery.append(SolrCrmAccountRepresenter.FIELD_TYPE_ID).append(":").append(customer.getObjectID());
        if (customerIDList != null) {
            solrQuery.append(" AND (");
            solrQuery.append(SolrCrmAccountRepresenter.FIELD_OWNER_ID).append(":").append(edsUser.getObjectID());
            if (customerIDList.size() != 0) {
                solrQuery.append(" OR (");
            }
            for (Integer id : customerIDList) {
                solrQuery.append(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID).append(":").append(id);
                if (!id.equals(customerIDList.get(customerIDList.size() - 1))) {
                    solrQuery.append(" ");
                }
            }
            if (customerIDList.size() != 0) {
                solrQuery.append(")");
            }
            solrQuery.append(")");
        } else if (fp.getClientId() == null && !ServerUtils.hasPermission(fp.isPM() ? PermissionConstants.PM_SEE_ALL_CUSTOMERS_LIST : PermissionConstants.ACCOUNTING_SEE_ALL_CUSTOMERS_LIST)) {
            solrQuery.append(" AND ").append(SolrCrmAccountRepresenter.FIELD_OWNER_ID).append(":").append(edsUser.getObjectID());
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            solrQuery.append(" AND (").append(fp.isLookUp() ? SolrCrmAccountRepresenter.FIELD_COMPOSITE_CRM_ACCOUNT_NAME : SolrCrmAccountRepresenter.FIELD_COMPOSITE)
                    .append(":(").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), fp.isLookUp())).append(")");
            if (!fp.isLookUp()) {
                SolrSearchUtils solrSearchUtils = new SolrSearchUtils();
                solrSearchUtils.generateSearchQuery(solrQuery, getAccountsSearchFields(), fp.getSearchKey());
            }
            solrQuery.append(")");
        }
        if (facetFilter != null) {
            Set<String> keySet = facetFilter.getShowSolrFieldMap().keySet();
            if (codeNameList != null && codeNameList.length != 0) {
                keySet = new HashSet<>(Arrays.asList(codeNameList));
            }
            for (String codeName : keySet) {
                if (facetFilter.getFacetContentMap().containsKey(codeName)) {
                    FacetContentRpc facetContent = facetFilter.getFacetContentMap().get(codeName);
                    if (facetContent != null && facetContent.getFacetItems().length != 0) {
                        SelectItem[] items = facetContent.getFacetItems();
                        solrQuery.append(" AND (");
                        boolean appendOperator = false;
                        for (SelectItem item : items) {
                            if (appendOperator) {
                                solrQuery.append(" OR ");
                            } else {
                                appendOperator = true;
                            }
                            solrQuery.append(facetFilter.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":").append(item.getName());
                        }
                        solrQuery.append(") ");
                    }
                }
            }
        }
        return solrQuery.toString();
    }


    /**
     * <h1>... Supplier List Core Solr query generated ....</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {12:56 11/06/2011} ...</h3>
     *
     * @param fp
     * @param company
     * @param customer
     * @return
     */
    public static String getSupplierListSolrQuery(ListingFilterParameter fp, FacetFilterRpc facetFilter, EdsCompany company, EdsReference customer, EdsUser edsUser, String[] customFullAccessRoles, String... codeNameList) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrCrmAccountRepresenter.FIELD_COMPANY_ID).append(":").append(company.getObjectID());

        boolean isFullAccessCustomRolesEnabled = false;
        if (customFullAccessRoles != null && customFullAccessRoles.length > 0 && edsUser != null) {
            isFullAccessCustomRolesEnabled = edsUser.hasEitherRoles(customFullAccessRoles);
        }
        if (edsUser != null && !isFullAccessCustomRolesEnabled) {
            solrQuery.append(" AND ").append(SolrCrmAccountRepresenter.FIELD_OWNER_ID).append(":").append(edsUser.getObjectID());
        }

        if (fp.isActive()) {
            solrQuery.append(" AND " + SolrCrmAccountRepresenter.FIELD_BLOCKED).append(":").append(!fp.isActive());
        }
        if (fp.getParentID() != null) {
            solrQuery.append(" AND " + SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID).append(":").append(fp.getParentID());
        }

        solrQuery.append(" AND ").append(SolrCrmAccountRepresenter.FIELD_TYPE_ID).append(":").append(customer.getObjectID());
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            solrQuery.append(" AND (").append(fp.isLookUp() ? SolrCrmAccountRepresenter.FIELD_COMPOSITE_CRM_ACCOUNT_NAME : SolrCrmAccountRepresenter.FIELD_COMPOSITE)
                    .append(":(").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), fp.isLookUp())).append(")");
            if (!fp.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(solrQuery, getAccountsSearchFields(), fp.getSearchKey());
            }
            solrQuery.append(")");
        }
        if (facetFilter != null) {
            Set<String> keySet = facetFilter.getShowSolrFieldMap().keySet();
            if (codeNameList != null && codeNameList.length != 0) {
                keySet = new HashSet<>(Arrays.asList(codeNameList));
            }
            for (String codeName : keySet) {
                if (facetFilter.getFacetContentMap().containsKey(codeName)) {
                    FacetContentRpc facetContent = facetFilter.getFacetContentMap().get(codeName);
                    if (facetContent != null && facetContent.getFacetItems().length != 0) {
                        SelectItem[] items = facetContent.getFacetItems();
                        solrQuery.append(" AND (");
                        boolean appendOperator = false;
                        for (SelectItem item : items) {
                            if (appendOperator) {
                                solrQuery.append(" OR ");
                            } else {
                                appendOperator = true;
                            }
                            solrQuery.append(facetFilter.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":").append(item.getName());
                        }
                        solrQuery.append(") ");
                    }
                }
            }
        }
        return solrQuery.toString();
    }

    /**
     * <h1>... Project List Core Solr query generated ....</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {14:30 03/06/2011} ...</h3>
     *
     * @param fp
     * @param user
     * @param company
     * @param userRoles
     * @return
     */
    public static String getProjectSolrQuery(ListingFilterParameter fp, EdsUser user, EdsCompany company, Set<Integer> userRoles, List<Integer> projectIDs) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrProjectListRepresenter.FIELD_COMPANY_ID).append(":").append(company.getObjectID());
        boolean seeAllProjectList = ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS);
        if (fp.isAllByFilter()) {
            seeAllProjectList = false;
        }
        if (userRoles.contains(EdsRole.ADMIN_LOCATION) && !seeAllProjectList) {
            EdsLocation location = user.getLocation();
            if (location != null) {
                solrQuery.append(" AND ").append(SolrProjectListRepresenter.FIELD_USER_LOCATION_ID).append(":").append(location.getObjectID());
            }
        } else if (!user.hasRole(Constants.SUPPLIER) || userRoles.contains(EdsRole.CLIENT) || !seeAllProjectList) {
            if (user.isClientContact()) {
                solrQuery.append(" AND (").append(SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_ID).append(":").append(user.getClientContact().getClientID())
                        .append(" OR ").append(SolrProjectListRepresenter.FIELD_PROJECT_MULTI_CLIENT_ID).append(":").append(user.getClientContact().getClientID()).append(") ");
            } else if (!seeAllProjectList) {
                solrQuery.append(" AND (").append(SolrProjectListRepresenter.FIELD_USER_ID).append(":").append(user.getObjectID());
                solrQuery.append(" OR ").append(SolrProjectListRepresenter.FIELD_PROJECT_MANAGER_ID).append(":").append(user.getObjectID());
                solrQuery.append(" OR ").append(SolrProjectListRepresenter.FIELD_PROJECT_CREATOR_ID).append(":").append(user.getObjectID()).append(") ");
            }
        }
        if (fp.isLookUp() && !fp.isShowPA()) {
            solrQuery.append(" AND NOT ").append(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_CODE).append(":").append(EdsProject.COMPLETED);
            solrQuery.append(" AND NOT ").append(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_CODE).append(":").append(EdsProject.CLOSED);
            if (fp.getEmployeeId() != null && fp.isIDsOnly()) {
                solrQuery.append(" AND ").append(SolrProjectListRepresenter.FIELD_USER_ID).append(":").append(fp.getEmployeeId());
            }
        }
        if (fp.getClientId() != null && fp.getClientId() != 0) {
            solrQuery.append(" AND (").append(SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_ID).append(":").append(fp.getClientId())
                    .append(" OR ").append(SolrProjectListRepresenter.FIELD_PROJECT_MULTI_CLIENT_ID).append(":").append(fp.getClientId());
            if (projectIDs != null && !projectIDs.isEmpty()) {
                solrQuery.append(" OR ").append(SolrProjectListRepresenter.FIELD_PROJECT_ID).append(":(").append(ServerUtils.getAsCommoDelimited(projectIDs, "0", " ")).append(")");
            }
            solrQuery.append(") ");
        }
        // Get projects by parnetId
        if (fp.getProjectId() != null) {
            solrQuery.append(" AND ").append(SolrProjectListRepresenter.FIELD_PARENT_ID).append(":").append(fp.getProjectId());
        } else {
            solrQuery.append(" AND (-").append(SolrProjectListRepresenter.FIELD_PARENT_ID).append(":[* TO *] AND *:*)");
        }
        if (fp.getRelationType() != null) {
            String relationID = fp.getRelationID() == null ? "[* TO *] AND *:*" : fp.getRelationID().toString();
            solrQuery.append(" AND (").append(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID).append(fp.getRelationType()).append(":(").append(relationID).append("))");
        }
        // Set Search key
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            if (fp.isLookUp()) {
                solrQuery.append(" AND (").append(SolrProjectListRepresenter.FIELD_PROJECT_NAME_NUMBER_COMPOSITE).append(":(").append(normalaizeKeyword(fp.getSearchKey(), true)).append(")");
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateApiSearchQuery(solrQuery, getApiSearchFields(), fp.getSearchKey());
                solrQuery.append(")");

            } else {
                solrQuery.append(" AND (").append(SolrProjectListRepresenter.FIELD_COMPOSITE).append(":").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey()));
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(solrQuery, getDynSearchFields(), fp.getSearchKey());
                solrQuery.append(")");
            }
        }
        return solrQuery.toString();
    }

    /**
     * <h1>... News Solr Core query generated ....</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {15:26 13/06/2011} ...</h3>
     *
     * @return
     */
    public static String getNewsListSolrQuery(ListingFilterParameter fp, EdsCompany edsCompany, boolean isBlog) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrNewsRepresenter.FIELD_COMPANY_ID).append(":").append(edsCompany.getObjectID());
        solrQuery.append(" AND ").append(SolrNewsRepresenter.FIELD_IS_BLOG).append(":").append(isBlog);
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            solrQuery.append(" AND ");
            solrQuery.append(SolrNewsRepresenter.FIELD_SUBJECT).append(":( ").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey())).append(" )");
        }
        if (fp.getEmployeeId() != null && "".equals(fp.getEmployeeId())) {
            solrQuery.append(" AND ");
            solrQuery.append(SolrNewsRepresenter.FIELD_USER_ID).append(":").append(fp.getEmployeeId());
        }

        return solrQuery.toString();
    }

    /**
     * <h1>... This is method generated Crm accounting list Solr Query  ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.} ...</h2>
     * <br/>
     * <h3>... Created date {20:46 11/06/2011} ...</h3>
     *
     * @param fp
     * @param edsCompany
     * @param accountType
     * @param edsUser
     * @return
     */

    public static String getCrmAccountListSolrQuery(ListingFilterParameter fp, EdsCompany edsCompany, EdsReference accountType, EdsUser edsUser, String[] customFullAccessRoles) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrCrmAccountRepresenter.FIELD_COMPANY_ID).append(":").append(edsCompany.getObjectID());
        if (!edsUser.hasRole(EdsRole.ADMIN_CODE) && !ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_ACCOUNTS_LIST)) {
            boolean isFullAccessCustomRolesEnabled = false;
            if (customFullAccessRoles != null && customFullAccessRoles.length > 0 && edsUser != null) {
                isFullAccessCustomRolesEnabled = edsUser.hasEitherRoles(customFullAccessRoles);
            }
            if (edsUser != null && !edsUser.hasEitherRoles(EdsRole.PROJECTS_DIRECTOR) && !isFullAccessCustomRolesEnabled) {
                if (edsUser.isClientContact() && edsUser.getClientContact().getCrmContact() != null && fp != null && fp.isLookUp()) {
                    EdsCrmContact contact = edsUser.getClientContact().getCrmContact();
                    Integer crmAccountID = contact != null && contact.getCrmAccount() != null ? contact.getCrmAccount().getObjectID() : null;
                    solrQuery.append(" AND ").append(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID).append(":").append(crmAccountID);
                } else {
                    if (!ServerUtils.hasPermission(fp.isPM() ? PermissionConstants.PM_SEE_ALL_CUSTOMERS_LIST : PermissionConstants.ACCOUNTING_SEE_ALL_CUSTOMERS_LIST)) {
                        if (fp.getClientIds() != null && !fp.getClientIds().isEmpty()) {
                            solrQuery.append(" AND (").append(SolrCrmAccountRepresenter.FIELD_OWNER_ID).append(":").append(edsUser.getObjectID());
                            solrQuery.append(" OR ").append(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID).append(":(").append(ServerUtils.getAsCommoDelimited(fp.getClientIds(), "0", " ")).append("))");
                        } else {
                            solrQuery.append(" AND ").append(SolrCrmAccountRepresenter.FIELD_OWNER_ID).append(":").append(edsUser.getObjectID());
                        }
                    }
                }
            }
        }
        if (!fp.isCRM() && !fp.isWithBlockedAccount()) {
            solrQuery.append(" AND ").append(SolrCrmAccountRepresenter.FIELD_BLOCKED).append(":").append(Boolean.FALSE);
        }
        if (!fp.isDetectDuplicates() && fp.getObjectIDs() != null && fp.getObjectIDs().size() > 0) {
            solrQuery.append(" AND ").append(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID).append(":(").append(ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0", " ")).append(")");
        }
        if (accountType != null) {
            if (fp.isShowHeadOffice()) {
                solrQuery.append(" AND (");
                solrQuery.append(SolrCrmAccountRepresenter.FIELD_TYPE_ID).append(":").append(accountType.getObjectID());
                solrQuery.append(" OR ").append(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID).append(": 1");
                solrQuery.append(" OR ").append(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NAME).append(": \"" + edsCompany.getName() + "\"");
                solrQuery.append(" ) ");
            } else {
                solrQuery.append(" AND ");
                solrQuery.append(SolrCrmAccountRepresenter.FIELD_TYPE_ID).append(":").append(accountType.getObjectID());
            }
        }
        if (Constants.SUPPLIER.equals(fp.getAvoidType()) && fp.getAvoidId() != null) {

            solrQuery.append(" AND ");
            solrQuery.append("-" + SolrCrmAccountRepresenter.FIELD_TYPE_IDS).append(":(").append(fp.getAvoidId()).append(")");
        }

        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            if (fp.isFromMobile()) {
                solrQuery.append(" AND (").append(SolrCrmAccountRepresenter.FIELD_ACCOUNT_NAME_COMPOSITE).append(":(").append(normalaizeKeyword(fp.getSearchKey(), true)).append(")");
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateApiSearchQuery(solrQuery, getApiSearchFields(), fp.getSearchKey());
                solrQuery.append(")");
            } else {
                String searchText = QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), fp.isLookUp());
                if (fp.isLookUp()) {
                    solrQuery.append(" AND (").append(SolrCrmAccountRepresenter.FIELD_COMPOSITE).append(":(").append(searchText).append(")");
                    SolrSearchUtils searchUtils = new SolrSearchUtils();
                    Map<String, Double> fields = new HashMap<>();
                    fields.put(SolrCrmAccountRepresenter.FIELD_COMPOSITE_CRM_ACCOUNT_NAME, SolrSearchUtils.HIGH_PRIORITY);
                    fields.put(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NUMBER, SolrSearchUtils.NORMAL_PRIORITY);

                    searchUtils.generateSearchQuery(solrQuery, fields, fp.getSearchKey());
                }
                if (!fp.isLookUp()) {
                    solrQuery.append(" AND (").append(SolrCrmAccountRepresenter.FIELD_COMPOSITE).append(":(").append(searchText).append(")");
                    SolrSearchUtils searchUtils = new SolrSearchUtils();
                    searchUtils.generateSearchQuery(solrQuery, getAccountsSearchFields(), fp.getSearchKey());
                }
                solrQuery.append(")");
            }
        }
        return solrQuery.toString();
    }

    private static Map<String, Double> getAccountsSearchFields() {
        Map<String, Double> fields = new HashMap<>();
        fields.put(SolrCrmAccountRepresenter.FIELD_COMPOSITE_CRM_ACCOUNT_NAME, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrCrmAccountRepresenter.FIELD_EMAIL, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NUMBER, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrCrmAccountRepresenter.FIELD_VAT_NUMBER, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrCrmAccountRepresenter.FIELD_TRN_NUMBER, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrCrmAccountRepresenter.FIELD_PHONE, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrCrmAccountRepresenter.FIELD_FAX, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrCrmAccountRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    public static String getWorkspaceNewsListCore(ListingFilterParameter fp, EdsUser edsUser, EdsCompany edsCompany) {
        StringBuilder solrQuery = new StringBuilder();
        boolean showAllNews = ServerUtils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS_LIST);
        Integer locationID = edsUser.getLocation() != null ? edsUser.getLocation().getObjectID() : null;
        solrQuery.append(SolrNewsRepresenter.FIELD_COMPANY_ID).append(":").append(edsCompany.getObjectID());
        if (!showAllNews) {
            solrQuery.append(" AND ((-").append(SolrNewsRepresenter.FIELD_LOCATION_ID).append(":[* TO *] AND *:*)");
            if (locationID != null) {
                solrQuery.append(" OR ").append(SolrNewsRepresenter.FIELD_LOCATION_ID).append(":").append(locationID);
            }
            solrQuery.append(")");
        }
        // Set Search key
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrNewsRepresenter.FIELD_COMPOSITE).append(":(").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey())).append(")");
        }
        if (edsUser.isClientContact()) {
            solrQuery.append(" AND ").append(SolrNewsRepresenter.FIELD_NEWS_VISIBILITY).append(":").append("true ");
        }

        if (fp.getStartDate() != null && fp.getEndDate() != null && fp.getEndDate().compareTo(fp.getStartDate()) >= 0) {
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date startDate = (Date) fp.getStartDate().clone();
            startDate.setHours(0);
            startDate.setMinutes(0);
            startDate.setSeconds(0);
            String sDate = format2.format(startDate);
            Date endDate = (Date) fp.getEndDate().clone();
            endDate.setHours(23);
            endDate.setMinutes(59);
            endDate.setSeconds(59);
            String eDate = format2.format(endDate);
            solrQuery.append(" AND ").append(SolrNewsRepresenter.FIELD_DATE).append(":[").append(sDate).append(" TO ").append(eDate).append("]");
        }
        if (fp.getSearchType() == 2 && fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            solrQuery.append(" AND ").append(SolrNewsRepresenter.FIELD_USER_ID).append(":(\"").append(fp.getEmployeeId()).append("\")");
        }
        if (fp.getSearchType() == 2 && fp.getCategoryID() != null && fp.getCategoryID() > 0) {
            solrQuery.append(" AND ").append(SolrNewsRepresenter.FIELD_CATEGORY_ID).append(":(\"").append(fp.getCategoryID()).append("\")");
        }
        return solrQuery.toString();
    }

    public static String getPositionListCore(ListingFilterParameter fp, EdsUser edsUser, EdsCompany edsCompany) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrPositionRepresenter.FIELD_COMPANY_ID).append(":").append(edsCompany.getObjectID());
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            solrQuery.append(" AND (").append(SolrPositionRepresenter.FIELD_COMPOSITE).append(":").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey())).append(")");
        }

        if (fp.getDepartmentId() != null) {
            solrQuery.append(" AND (").append(SolrPositionRepresenter.FIELD_DEPARTMENT_ID).append(":").append(fp.getDepartmentId()).append(")");
        }

        if (!ServerUtils.hasPermission(PermissionConstants.POSITION_LIST_SEE_ALL) && ServerUtils.hasPermission(PermissionConstants.POSITION_LIST_BY_LOCATION) && fp.getLocationId() != null) {
            solrQuery.append(" AND (").append(SolrPositionRepresenter.FIELD_LOCATION_ID).append(":").append(fp.getLocationId()).append(")");
        }

        return solrQuery.toString();
    }

    public static String getDepartmentListCore(ListingFilterParameter fp, EdsUser edsUser, EdsCompany edsCompany) {
        StringBuilder solrQuery = new StringBuilder();

        boolean hasSeeAllDepartmentListPermission = ServerUtils.hasPermission(PermissionConstants.HRMS_SEE_ALL_DEPARTMENT_LIST);
        boolean hasDepartmentListByLocationPermission = ServerUtils.hasPermission(PermissionConstants.HRMS_DEPARTMENT_LIST_BY_LOCATION);

        if (hasDepartmentListByLocationPermission || hasSeeAllDepartmentListPermission) {
            solrQuery.append(SolrDepartmentRepresenter.FIELD_COMPANY_ID).append(":").append(edsCompany.getObjectID());
        }

        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            solrQuery.append(" AND (").append(SolrDepartmentRepresenter.FIELD_COMPOSITE).append(":").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey())).append(")");

        }
        if (!hasSeeAllDepartmentListPermission && hasDepartmentListByLocationPermission && fp.getLocationId() != null) {
            solrQuery.append(" AND (").append(FIELD_LOCATION_ID).append(":").append(fp.getLocationId()).append(")");
        }

        return solrQuery.toString();
    }

    public static String getDocumentsSolrCore(ListingFilterParameter fp, EdsUser edsUser, EdsCompany edsCompany, Set<EdsGroup> membershipsGroups) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrFolderRepresenter.FIELD_COMPANY_ID).append(":").append(edsCompany.getObjectID());
        if (!fp.isEntityBasedAttachmentList() && !fp.isHasFullListAccess()) { // entitBasedAttachmentList means it will show all documents(attachments) uploaded to entire entity.(HAYOT)
            solrQuery.append(" AND (");
            solrQuery.append(" ( ").append(SolrFolderRepresenter.FIELD_USER_VIEWERS).append(":").append(edsUser.getObjectID()).append(")");
            for (EdsGroup group : membershipsGroups) {
                solrQuery.append(" OR (");
                solrQuery.append(SolrFolderRepresenter.FIELD_GROUP_VIEWERS).append(":").append(group.getObjectID())/*.append(" ").append("AND ")*/;
                solrQuery.append(")");
            }
            solrQuery.append(")");
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        solrQuery.append(" AND ").append(SolrFolderRepresenter.FIELD_IS_FILE).append(":").append(true);
        solrQuery.append(" AND ").append(SolrFolderRepresenter.FIELD_DELETED).append(":").append(fp.isDeleted());
        if (SolrFolderRepresenter.FIELD_EXPIRE_DATE.equals(fp.getSortField())) {
            if (fp.getDueDate() != null) { // going to be expired docs
                if (Constants.DASHBOARD_WIDGET_CODE.COMPANY_DOCUMENT.equals(fp.getDataType()) || Constants.DASHBOARD_WIDGET_CODE.EMPLOYEE_DOCUMENT.equals(fp.getDataType())) {
                    solrQuery.append(" AND (").append(SolrFolderRepresenter.FIELD_EXPIRE_DATE).append(":[").append(format.format(fp.getDueDate())).append(" TO *").append("] ");
                    solrQuery.append(" OR (-").append(SolrFolderRepresenter.FIELD_EXPIRE_DATE).append(":[").append("* TO *").append("] AND *:*").append("))");
                } else {
                    solrQuery.append(" AND ").append(SolrFolderRepresenter.FIELD_EXPIRE_DATE).append(":[").append(format.format(fp.getDueDate())).append(" TO *").append("]");
                }
            } else if (fp.getEndDate() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(fp.getEndDate());
                cal.add(Calendar.MONTH, -1);
                Date oneMonthAgo = cal.getTime();

                solrQuery.append(" AND ").append(SolrFolderRepresenter.FIELD_EXPIRE_DATE)
                        .append(":[").append(format.format(oneMonthAgo))
                        .append(" TO ").append(format.format(fp.getEndDate())).append("]");
            }
        }
        if (fp.getDataType() != null && Constants.DASHBOARD_WIDGET_CODE.EXPIRED_DOCUMENTS.equals(fp.getDataType()) && fp.getObjectIDs() != null) {
            solrQuery.append(" AND ").append(SolrFolderRepresenter.FIELD_ENTITY_ID).append(":(").append(ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0", " ")).append(")");
        }
        solrQuery.append(" AND -").append(SolrFolderRepresenter.FIELD_TYPE).append(":").append(EdsObject.TEMP);
        if (fp.getCrmEntityId() != null) {
            solrQuery.append(" AND ").append(SolrFolderRepresenter.FIELD_ENTITY_ID).append(":\\").append(fp.getCrmEntityId());
        } else {
            if (fp.getModule() != null && LayoutRPC.HRMS_SECTION.equals(fp.getModule()) &&
                    !ServerUtils.hasPermission(PermissionConstants.VIEW_ALL_EMPLOYEE_DOCUMENTS) && fp.getFolderType() == Constants.F_EMPLOYEE_PROFILE) {
                solrQuery.append(" AND ").append(SolrFolderRepresenter.FIELD_ENTITY_ID).append(":\\").append(edsUser.getObjectID());
            }
        }
        if (fp.getEntityID() != null && LayoutRPC.HRMS_SECTION.equals(fp.getModule()) && fp.getFolderType() == Constants.F_EMPLOYEE_PROFILE) {
            solrQuery.append(" AND ").append(SolrFolderRepresenter.FIELD_FOLDER_ID).append(":\\").append(fp.getEntityID());
        }
        if (fp.getFolderType() != null && fp.getFolderType() == Constants.F_EVENT && !ServerUtils.hasPermission(PermissionConstants.CRM_ACTIVITY_SEE_ALL_ATTACHMENTS)) {
            solrQuery.append(" AND ").append(SolrFolderRepresenter.FIELD_CREATED_ID).append(":").append(fp.getUserID());
        }
        if (fp.getSearchKey() == null || "".equals(fp.getSearchKey()) || LayoutRPC.HRMS_SECTION.equals(fp.getModule())) {
            if (fp.getFolderId() != null && fp.getFolderId() > 0) {
                solrQuery.append(" AND ").append(SolrFolderRepresenter.FIELD_PARENT_ID).append(":").append(fp.getFolderId());
            }
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            String key = QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey()).toLowerCase();
            solrQuery.append(" AND ").append(SolrFolderRepresenter.FIELD_COMPOSITE).append(":(").append(key).append("~0.6 ").append(key).append(")");
        }
        if (fp.getViewType() != null && fp.getType() != null && !"".equals(fp.getViewType())) {
            solrQuery.append(" AND ").append(SolrFolderRepresenter.FIELD_DOCUMENT_TYPE_ID).append(":").append(fp.getType());
        }
        return solrQuery.toString();
    }

    public static void supplierRelationForProjectList(FacetFilterRpc projectFacFilter, EdsUser user) {
        Integer supplierId = user.getClientContact().getClientID();
        String supplierName = user.getClientContact().getClientName();
        if (projectFacFilter.getFacetContentMap().containsKey(FacetContentType.ProjectFacetFilter.getContentCode()[16])) {
            projectFacFilter.getFacetContentMap().get(FacetContentType.ProjectFacetFilter.getContentCode()[16]).setFacetItems(new SelectItem[]{new SelectItem(supplierId, supplierName)});
        } else {
            projectFacFilter.getFacetContentMap().put(FacetContentType.ProjectFacetFilter.getContentCode()[16], new FacetContentRpc());
            projectFacFilter.getFacetContentMap().get(FacetContentType.ProjectFacetFilter.getContentCode()[16]).setFacetItems(new SelectItem[]{new SelectItem(supplierId, supplierName)});
        }
        FacetSolrField facetSolrField = new FacetSolrField();
        facetSolrField.setSolrFieldCriteriaName(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_SUPPLIER);
        facetSolrField.setSolrFacetFieldName(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_SUPPLIER);
        projectFacFilter.getShowSolrFieldMap().put(FacetContentType.ProjectFacetFilter.getContentCode()[16], facetSolrField);
    }

    public static void supplierRelationForTaskList(FacetFilterRpc taskFacFilter, EdsUser user) {
        Integer supplierId = user.getClientContact().getClientID();
        String supplierName = user.getClientContact().getClientName();
        if (taskFacFilter.getFacetContentMap().containsKey(FacetContentType.TaskFacetFilter.getContentCode()[17])) {
            taskFacFilter.getFacetContentMap().get(FacetContentType.TaskFacetFilter.getContentCode()[17]).setFacetItems(new SelectItem[]{new SelectItem(supplierId, supplierName)});
        } else {
            taskFacFilter.getFacetContentMap().put(FacetContentType.TaskFacetFilter.getContentCode()[17], new FacetContentRpc());
            taskFacFilter.getFacetContentMap().get(FacetContentType.TaskFacetFilter.getContentCode()[17]).setFacetItems(new SelectItem[]{new SelectItem(supplierId, supplierName)});
        }
        FacetSolrField facetSolrField = new FacetSolrField();
        facetSolrField.setSolrFieldCriteriaName(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_SUPPLIER);
        facetSolrField.setSolrFacetFieldName(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_SUPPLIER);
        taskFacFilter.getShowSolrFieldMap().put(FacetContentType.TaskFacetFilter.getContentCode()[17], facetSolrField);
    }

    public static String getCourseScheduleListFacetFilterQuery(FacetFilterRpc facetFieldRpc, EdsCompany edsCompany, String startDateSolrName, String endDateSolrName, String... codeNameList) {
        StringBuilder solrQuery = new StringBuilder();
        if (facetFieldRpc != null) {
            Set<String> keySet = facetFieldRpc.getShowSolrFieldMap().keySet();
            if (codeNameList != null && codeNameList.length != 0) {
                keySet = new HashSet<>(Arrays.asList(codeNameList));
            }
            for (String codeName : keySet) {
                if (facetFieldRpc.getFacetContentMap().containsKey(codeName)) {
                    FacetContentRpc facetContent = facetFieldRpc.getFacetContentMap().get(codeName);
                    if (facetContent != null && facetContent.getFacetItems().length != 0) {
                        SelectItem[] items = facetContent.getFacetItems();
                        solrQuery.append(" AND (");
                        boolean appendOperator = false;
                        for (SelectItem item : items) {
                            if (appendOperator) {
                                solrQuery.append(" OR ");
                            } else {
                                appendOperator = true;
                            }
                            if (FacetContentType.CourseScheduleFaceFilter.getContentCode()[4].equals(codeName)) {
                                if (item.getId() == -1) {
                                    solrQuery.append("(-").append(SolrCourseScheduleRepresenter.FIELD_INSTRUCTOR_ID).append(":[* TO *] AND *:*)");// Checking fiel for null
                                } else {
                                    solrQuery.append(SolrCourseScheduleRepresenter.FIELD_INSTRUCTOR_ID).append(":").append(item.getId());
                                }
                            } else {
                                if (facetFieldRpc.getShowSolrFieldMap().get(codeName).isConditionItemId()) {
                                    solrQuery.append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":").append(item.getId());
                                } else {
                                    solrQuery.append(facetFieldRpc.getShowSolrFieldMap().get(codeName).getSolrFieldCriteriaName()).append(":(").append(QueryBuilderForSolr.normalaizeKeywordForFacet(item.getName())).append(")");
                                }
                            }
                        }
                        solrQuery.append(") ");
                    }
                }
            }
            if (facetFieldRpc.getStartDate() != null && facetFieldRpc.getEndDate() != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                if (startDateSolrName != null && endDateSolrName != null) {
                    solrQuery.append(" AND ((").append(startDateSolrName).append(":[ * TO ")
                            .append(format.format(facetFieldRpc.getEndDate())).append(" ]) AND ");
                    solrQuery.append(" (").append(endDateSolrName).append(":[ ")
                            .append(format.format(facetFieldRpc.getStartDate())).append(" TO * ]))");
                } else if (startDateSolrName != null) {
                    solrQuery.append(" AND (").append(startDateSolrName).append(":[ ").append(format.format(facetFieldRpc.getStartDate()))
                            .append(" TO ").append(format.format(facetFieldRpc.getEndDate())).append(" ]").append(")");
                } else if (endDateSolrName != null) {
                    solrQuery.append(" AND (").append(endDateSolrName).append(":[ ").append(format.format(facetFieldRpc.getStartDate()))
                            .append(" TO ").append(format.format(facetFieldRpc.getEndDate())).append(" ]").append(")");
                }
            }
        }
        return solrQuery.toString();
    }

    public static String getEmployeeSolrQuery(ListingFilterParameter fp, EdsUser user, List<Integer> departmentIds) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrEmployeeRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        if (!fp.isAllEmployees()) { // isAllEmployees() -> getting employee list without permission
            solrQuery = getEmployeeListPermissions(fp, user, solrQuery, departmentIds);
        }
        if (fp.getRoles() != null && !"".equals(fp.getRoles())) {
            solrQuery.append(" AND ").append(SolrEmployeeRepresenter.FIELD_ROLE_CODE).append(":(").append(fp.getRoles()).append(")");
        }
        if (fp.getPositionID() != null && fp.getPositionID() > 0) {
            solrQuery.append(" AND ").append(SolrEmployeeRepresenter.FIELD_POSITION_ID).append(":").append(fp.getPositionID());
        }
        if (fp.getLocationId() != null && fp.getLocationId() > 0) {
            solrQuery.append(" AND ").append(SolrEmployeeRepresenter.FIELD_LOCATION_ID).append(":").append(fp.getLocationId());
        }
        if (fp.getTimeSlotID() != null && fp.getTimeSlotID() > 0) {
            solrQuery.append(" AND ").append(SolrEmployeeRepresenter.FIELD_TIMESLOT_ID).append(":").append(fp.getTimeSlotID());
        }
        if (fp.getDepartmentId() != null && fp.getDepartmentId() > -1) {
            solrQuery.append(" AND ").append(SolrEmployeeRepresenter.FIELD_DEPARTMENT_ID).append(":").append(fp.getDepartmentId());
        }
        if (fp.getQualificationId() != null && fp.getQualificationId() > 0) {
            solrQuery.append(" AND ").append(SolrEmployeeRepresenter.FIELD_QUALIFICATION_ID).append(":").append(fp.getQualificationId());
        }
        if (fp.getPayrollBatchID() != null && fp.getPayrollBatchID() > 0) {
            solrQuery.append(" AND ").append(SolrEmployeeRepresenter.FIELD_PAYROLL_BATCH_ID).append(":").append(fp.getPayrollBatchID());
        }
        if ("FROM_CHART".equals(fp.getParams())) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            solrQuery.append(" AND ").append(SolrEmployeeRepresenter.FIELD_HIRE_DATE).append(":[").append(dateFormat.format(fp.getStartDate())).append(" TO ").append(dateFormat.format(fp.getEndDate())).append("]");
        }
        if (fp.getObjectIDs() != null && fp.getObjectIDs().size() > 0) {
            if (fp.isIDsOnly()) {
                solrQuery.append(" AND ").append(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0", " ")).append(")");
            }
        }
        if (fp.getIgnoreID() != null) {
            solrQuery.append(" AND -").append(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID).append(":(").append(fp.getIgnoreID()).append(")");
        }
        if (fp.getLanguageIDs() != null && !fp.getLanguageIDs().isEmpty()) {
            solrQuery.append(" AND (").append(SolrEmployeeRepresenter.FIELD_LANGUAGE_ID).append(":(").append(fp.getLanguageIDs().replace(",", " ")).append(")) ");
        }
        if (fp.getSkillIDs() != null && !fp.getSkillIDs().isEmpty() && fp.getPositionIDs() != null && !fp.getPositionIDs().isEmpty()) {
            solrQuery.append(" AND (");
            solrQuery.append(" (").append(SolrEmployeeRepresenter.FIELD_SKILL_ID).append(":(").append(fp.getSkillIDs().replace(",", " ")).append("))");
            solrQuery.append(" OR (").append(SolrEmployeeRepresenter.FIELD_POSITION_ID).append(":(").append(fp.getPositionIDs().replace(",", " ")).append("))");
            solrQuery.append(")");
        } else {
            if (fp.getSkillIDs() != null && !fp.getSkillIDs().isEmpty()) {
                solrQuery.append(" AND (").append(SolrEmployeeRepresenter.FIELD_SKILL_ID).append(":(").append(fp.getSkillIDs().replace(",", " ")).append("))");
            }
            if (fp.getPositionIDs() != null && !fp.getPositionIDs().isEmpty()) {
                solrQuery.append(" AND (").append(SolrEmployeeRepresenter.FIELD_POSITION_ID).append(":(").append(fp.getPositionIDs().replace(",", " ")).append("))");
            }
        }
        if (fp.isShowActive()) {
            solrQuery.append(" AND ( ").append(SolrEmployeeRepresenter.FIELD_STATUS_CODE).append(":").append(Constants.EMPLOYEE_STATUS_ACTIVE).append(" ) ");
        } else if (fp.isCRM()) {
            solrQuery.append(" AND ( (").append(SolrEmployeeRepresenter.FIELD_STATUS_CODE).append(":").append(Constants.EMPLOYEE_STATUS_ACTIVE);
            solrQuery.append(") OR (").append(SolrEmployeeRepresenter.FIELD_STATUS_CODE).append(":").append(Constants.EMPLOYEE_STATUS_NO_ACCCESS).append(")) ");
        } else if (!fp.isResignedEmployeesIncluded()) {
            solrQuery.append(" AND -").append(SolrEmployeeRepresenter.FIELD_STATUS_CODE).append(":").append(Constants.EMPLOYEE_STATUS_RESIGNED);
        }
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            if (!fp.isFromMobile()) {
                solrQuery.append(" AND (").append(SolrEmployeeRepresenter.FIELD_COMPOSITE).append(":").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey()));
                if (!fp.isLookUp() && !fp.isCheckNumber()) {
                    SolrSearchUtils searchUtils = new SolrSearchUtils();
                    searchUtils.generateSearchQueryForEmployee(solrQuery, getDynSearchFields(), fp.getSearchKey());
                }
                solrQuery.append(")");
            }
            if (fp.isLookUp()) {
                solrQuery.append(" AND (");
                solrQuery.append(SolrEmployeeRepresenter.FIELD_LOOKUP_COMPOSITE).append(":").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), true));
                solrQuery.append(") ");
            } else if (fp.isFromMobile()) {
                solrQuery.append(" AND (").append(SolrEmployeeRepresenter.FIELD_EMPLOYEE_NAME).append(":(").append(normalaizeKeyword(fp.getSearchKey(), true)).append(")");
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                Map<String, Double> fields = new HashMap<>();
                fields.put(SolrContactRepresenter.FIELD_LOOKUP_COMPOSITE_MOBILE, SolrSearchUtils.HIGH_PRIORITY);
                searchUtils.generateApiSearchQuery(solrQuery, fields, fp.getSearchKey());
                solrQuery.append(")");
            }
        }

        return solrQuery.toString();
    }

    private static StringBuffer getEmployeeListPermissions(ListingFilterParameter fp, EdsUser user, StringBuffer solrQuery, List<Integer> departmentIds) {
        boolean showAllEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_EMPLOYEE_LIST);
        if (fp.getParams() != null && fp.getRoles() != null && fp.isLookUp()) { //LookUps should have their own permissions
            showAllEmployees = true;
        }
        boolean showTeamEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST);
        boolean showLocationEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_LOCATION_EMPLOYEE_LIST);
        boolean showSupervisedEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_SUPERVISED_EMPLOYEE_LIST);
        boolean showProjectEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_PROJECT_EMPLOYEE_LIST);
        boolean showUnderEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_EMPLOYEES_UNDER_SUB_DEPARTMENTS);
        if (PermissionConstants.PM_CONTEXT.equals(fp.getModule())) {
            showAllEmployees = ServerUtils.hasPermission(PermissionConstants.PM_SHOW_ALL_EMPLOYEE_LIST);
            showTeamEmployees = ServerUtils.hasPermission(PermissionConstants.PM_SHOW_DEPARTMENT_EMPLOYEE_LIST);
            showLocationEmployees = ServerUtils.hasPermission(PermissionConstants.PM_SHOW_LOCATION_EMPLOYEE_LIST);
            showSupervisedEmployees = ServerUtils.hasPermission(PermissionConstants.PM_SHOW_SUPERVISED_EMPLOYEE_LIST);
            showProjectEmployees = ServerUtils.hasPermission(PermissionConstants.PM_SHOW_PROJECT_EMPLOYEE_LIST);
        } else if (PermissionConstants.PAYROLL_CONTEXT.equals(fp.getModule())) {
            showAllEmployees = ServerUtils.hasPermission(PermissionConstants.PAYROLL_SHOW_ALL_EMPLOYEE_LIST);
            showTeamEmployees = ServerUtils.hasPermission(PermissionConstants.PAYROLL_SHOW_DEPARTMENT_EMPLOYEE_LIST);
            showLocationEmployees = ServerUtils.hasPermission(PermissionConstants.PAYROLL_SHOW_LOCATION_EMPLOYEE_LIST);
            showSupervisedEmployees = ServerUtils.hasPermission(PermissionConstants.PAYROLL_SHOW_SUPERVISED_EMPLOYEE_LIST);
            showProjectEmployees = ServerUtils.hasPermission(PermissionConstants.PAYROLL_SHOW_PROJECT_EMPLOYEE_LIST);
        }

        boolean hasPermission = showAllEmployees;
        if (Constants.FROM_TRAINING_CENTER.equals(fp.getViewType())) {
            solrQuery.append(" AND ").append(SolrEmployeeRepresenter.FIELD_ROLE_ID).append(":").append(fp.getRoleID());
        } else if (!showAllEmployees) {
            Integer locationID = user.getLocation() != null ? user.getLocation().getObjectID() : null;
            Integer objectID = user.getObjectID();
            if (showTeamEmployees || showLocationEmployees || showSupervisedEmployees || showProjectEmployees || showUnderEmployees || objectID != null) {
                solrQuery.append(" AND (");
            }
            boolean or = false;
            if (!showUnderEmployees && showTeamEmployees) {
                if (departmentIds != null && departmentIds.size() > 0) {
                    solrQuery.append(SolrEmployeeRepresenter.FIELD_DEPARTMENT_ID).append(":(").append(ServerUtils.getAsCommoDelimited(departmentIds, "0", " ")).append(")");
                    or = true;
                }
            }
            if (showLocationEmployees) {
                if (or) {
                    solrQuery.append(" OR ");
                }
                solrQuery.append(SolrEmployeeRepresenter.FIELD_LOCATION_ID).append(":").append(locationID);
                or = true;
            }
            if (showSupervisedEmployees) {
                if (or) {
                    solrQuery.append(" OR ");
                }
                solrQuery.append(SolrEmployeeRepresenter.FIELD_SUPERVISOR_ID).append(":").append(objectID);
                or = true;
            }
            if ((showProjectEmployees || showUnderEmployees) && fp.getEmployeeIDs() != null) {
                if (or) {
                    solrQuery.append(" OR ");
                    solrQuery.append(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID).append(":(").append(fp.getEmployeeIDs().replace(",", " OR ")).append(")");
                }
            }
            if (objectID != null) {
                if (or) {
                    solrQuery.append(" OR ");
                }
                solrQuery.append(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID).append(":").append(objectID);
            }
            if (showTeamEmployees || showLocationEmployees || showSupervisedEmployees || showProjectEmployees || objectID != null) {
                solrQuery.append(")");
                hasPermission = true;
            }
        }
        if (!hasPermission) {
            solrQuery.append(" AND ").append(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID).append(":").append(user.getObjectID());
        }
        return solrQuery;
    }

    public static String getSinglePayrunSolrQuery(ListingFilterParameter fp) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrSinglePayrunRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        if (fp.getEmployeeId() != null) {
            solrQuery.append(" AND ").append(SolrSinglePayrunRepresenter.FIELD_EMPLOYEE_ID).append(":").append(fp.getEmployeeId());
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrSinglePayrunRepresenter.FIELD_COMPOSITE).append(":(").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey())).append(")");
        }

        return solrQuery.toString();
    }

    public static String getGroupPayrunSolrQuery(ListingFilterParameter fp) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrGroupPayrunRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrGroupPayrunRepresenter.FIELD_COMPOSITE).append(":(").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey())).append(")");
        }
        return solrQuery.toString();
    }

    public static String getCashAdvanceSolrQuery(ListingFilterParameter fp, EdsUser edsUser) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrCashAdvanceRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        if (!ServerUtils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_FULL_ACCESS) || (ServerUtils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_FULL_ACCESS) && fp.isFromEmployeeProfile())) {
            if (fp.getEmployeeId() != null) {
                if (fp.isHRMS()) {
                    solrQuery.append(" AND ").append(SolrCashAdvanceRepresenter.FIELD_EMPLOYEE_ID).append(":").append(fp.getEmployeeId());
                } else {
                    solrQuery.append(" AND (").append(SolrCashAdvanceRepresenter.FIELD_EMPLOYEE_ID).append(":").append(fp.getEmployeeId());
                    solrQuery.append(" OR ").append(SolrCashAdvanceRepresenter.FIELD_APPROVER_ID).append(":").append(fp.getEmployeeId()).append(")");
                }
            } else if (fp.getEmployeeId() == null && (edsUser.hasRole(EdsRole.ESS_USER_CODE) || ServerUtils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_LIST))) {
                solrQuery.append(" AND (").append(SolrCashAdvanceRepresenter.FIELD_EMPLOYEE_ID).append(":").append(edsUser.getObjectID());
                solrQuery.append(" OR ").append(SolrCashAdvanceRepresenter.FIELD_APPROVER_ID).append(":").append(edsUser.getObjectID()).append(")");
            }
        }
        if (StringUtils.isNotBlank(fp.getStatusCode())) {
            solrQuery.append(" AND (").append(SolrCashAdvanceRepresenter.FIELD_STATUS_CODE).append(":").append(fp.getStatusCode()).append(")");
        }
        if (fp.isFromMobile() && !ServerUtils.hasPermission(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP)) {
            solrQuery.append(" AND ");
            solrQuery.append("( ");
            solrQuery.append(SolrCashAdvanceRepresenter.FIELD_APPROVER_ID).append(":").append(edsUser.getObjectID());
            solrQuery.append(" )");
        }
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrCashAdvanceRepresenter.FIELD_COMPOSITE).append(":(").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey())).append(")");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            solrQuery.append(" AND ((").append(SolrCashAdvanceRepresenter.FIELD_REQUEST_DATE).append(":[ * TO ")
                    .append(format.format(fp.getEndDate())).append(" ]) AND ");
            solrQuery.append(" (").append(SolrCashAdvanceRepresenter.FIELD_REQUEST_DATE).append(":[ ")
                    .append(format.format(fp.getStartDate())).append(" TO * ]))");

        }
        return solrQuery.toString();
    }

    public static String getAdditionalPaymentSolrQuery(ListingFilterParameter fp, EdsUser edsUser) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrAdditionalPaymentPresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());

        boolean isSeeAllPermission = ServerUtils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SEE_ALL);
        if (!isSeeAllPermission && !edsUser.hasRole(EdsRole.ADMIN_CODE)) {
            boolean isSeeOwnPermission = ServerUtils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SEE_OWN);
            if (isSeeOwnPermission) {
                solrQuery.append(" AND (").append(SolrAdditionalPaymentPresenter.FIELD_CREATOR_ID).append(":").append(edsUser.getObjectID());
                solrQuery.append(" OR ").append(SolrAdditionalPaymentPresenter.FIELD_APPROVER_ID).append(":").append(edsUser.getObjectID());
                if (fp.getObjectIDs() != null) {
                    solrQuery.append(" OR ").append(SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID).append(":(").append(ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0", " ")).append(")");
                }
                if (edsUser.hasRole(EdsRole.TL_CODE) && (fp.getDepartmentIds() != null || fp.getDepartmentId() != null)) {
                    String departmentId = fp.getDepartmentIds() != null ? fp.getDepartmentIds() : fp.getDepartmentId().toString();
                    solrQuery.append(" OR ").append(SolrAdditionalPaymentPresenter.FIELD_PAYROLL_DEPARTMENT_ID).append(":(").append(departmentId).append("))");
                } else {
                    solrQuery.append(")");
                }
            } else {
                solrQuery.append(" AND (").append(SolrAdditionalPaymentPresenter.FIELD_CREATOR_ID).append(":").append(edsUser.getObjectID());
                solrQuery.append(" OR ").append(SolrAdditionalPaymentPresenter.FIELD_APPROVER_ID).append(":").append(edsUser.getObjectID());
                if (fp.getObjectIDs() != null) {
                    solrQuery.append(" OR ").append(SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID).append(":(").append(ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0", " ")).append(")");
                }
                solrQuery.append(")");
            }
        }

        if (StringUtils.isNotBlank(fp.getStatusCode())) {
            solrQuery.append(" AND (").append(SolrAdditionalPaymentPresenter.FIELD_STATUS_CODE).append(":").append(fp.getStatusCode()).append(")");
        }
        if (fp.isFromMobile() && !(edsUser.hasRole(EdsRole.ADMIN_CODE) || edsUser.hasRole(EdsRole.DR_CODE))) {
            solrQuery.append(" AND ");
            solrQuery.append("( ");
            solrQuery.append(SolrCashAdvanceRepresenter.FIELD_APPROVER_ID).append(":").append(edsUser.getObjectID());
            solrQuery.append(" )");
        }
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrAdditionalPaymentPresenter.FIELD_COMPOSITE).append(":(").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey())).append(")");
        }
        return solrQuery.toString();
    }

    public static String getVacancySolrQuery(ListingFilterParameter fp, EdsUser user, EdsCompany company) {
        List<Integer> userMaxRoleIDs = new ArrayList<>();
        for (EdsRole roleIds : user.getRoles()) {
            userMaxRoleIDs.add(roleIds.getObjectID());
        }
        Integer userMaxRoleID = ServerUtils.getUserRolesSorted(userMaxRoleIDs).get(0);

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrVacancyRepresenter.COMPANY_ID).append(":").append(company.getObjectID());
        if (fp.getJobFamilyID() != null) {
            solrQuery.append(" AND ").append(SolrVacancyRepresenter.FIELD_JOB_FAMILY_ID).append(":").append(fp.getJobFamilyID());
        }
        if (StringUtils.isNotBlank(fp.getStatusCode())) {
            solrQuery.append(" AND ").append(SolrVacancyRepresenter.FIELD_VACANCY_STATUS_CODE).append(":").append(fp.getStatusCode());
        }
        if (fp.getPositionID() != null && fp.getPositionID() > 0) {
            solrQuery.append(" AND ").append(SolrVacancyRepresenter.FIELD_POSITION_ID).append(":").append(fp.getPositionID());
        }
        if (!ServerUtils.hasPermission(PermissionConstants.HRMS_VACANCY_SEE_ALL)) {
            solrQuery.append(" AND (").append(SolrVacancyRepresenter.FIELD_MANAGER_ID).append(":").append(user.getObjectID());
            solrQuery.append(" OR ").append(SolrLeaveRequestConst.FIELD_APPROVER_ID).append(":").append(user.getObjectID());
            if (ServerUtils.hasPermission(PermissionConstants.HRMS_VACANCY_SEE_OWN)) {
                Integer employeeDepartmentId = user.getEmployee().getEmployeeDepartment() != null  && user.getEmployee().getEmployeeDepartment().getTeam() != null ? user.getEmployee().getEmployeeDepartment().getTeam().getObjectID() : null;
                if (employeeDepartmentId != null) {
                    solrQuery.append(" OR ").append(SolrVacancyRepresenter.FIELD_DEPARTMENT_ID).append(":").append(employeeDepartmentId);
                }
            }
            solrQuery.append(")");
        }

        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            solrQuery.append(" AND ");
            solrQuery.append(SolrNewsRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey())).append(" )");
        }

        return solrQuery.toString();
    }

    public static String getChartOfAccountSolrQuery(ListingFilterParameter fp) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrChartOfAccountRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());

        if (fp.getAccountType() != null) {
            solrQuery.append(" AND ").append(SolrChartOfAccountRepresenter.FIELD_TYPE_CATEGORY).append(":").append(fp.getAccountType());
        }

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrChartOfAccountRepresenter.FIELD_COMPOSITE);
            solrQuery.append(":(").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey())).append(")");
        }
        return solrQuery.toString();
    }

    public static String getCustomFormItemsQuery(ListingFilterParameter fp, EdsUser edsUser) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrCustomFormConst.FIELD_ITEM_ID).append(":").append(fp.getParentID());

        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            solrQuery.append(" AND (").append(SolrCustomFormConst.FIELD_COMPOSITE).append(":").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey()));
            SolrSearchUtils searchUtils = new SolrSearchUtils();
            searchUtils.generateSearchQueryForEmployee(solrQuery, getDynSearchFields(), fp.getSearchKey());
            solrQuery.append(")");
        }

        return solrQuery.toString();
    }
}
