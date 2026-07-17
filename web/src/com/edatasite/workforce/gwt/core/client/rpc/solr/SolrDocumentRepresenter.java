package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class represents confing file located in core0
 * It partly covers basic fields
 * User: Abdulaziz
 * Date: Nov 3, 2009
 * Time: 6:48:36 PM
 */
public class SolrDocumentRepresenter implements IsSerializable {

    public static final Integer ENTITY_TYPE_TASK = 1;
    public static final Integer ENTITY_TYPE_PROJECT = 2;
    public static final Integer ENTITY_TYPE_ASSESSMENT_360 = 3;
    public static final Integer ENTITY_TYPE_ASSESSMENT_SIMPLE = 4;
    public static final Integer ENTITY_TYPE_EMPLOYEE = 5;

    public static final String FIELD_ENTITY_TYPE = "entityType";
    public static final String FIELD_COMPOSITE_ID = "compositeID";
    public static final String FIELD_COMPANY_ID = "companyID";
    public static final String FIELD_ENTITY_ID = "entityID";
    public static final String FIELD_USERS_ID = "usersID";
    public static final String FIELD_GROUPS_ID = "groupsID";
    public static final String FIELD_ENTITY_NAME = "entityName";
    public static final String FIELD_ENTITY_DESCRIPTION = "entityDescription";
    public static final String FIELD_ASSIGNEES = "assignees";
    //default search field == keyword
    public static final String text = "text";

    public static final String DYNAMIC_FIELD_APPROVER_ID = "approverId_";
    public static final String DYNAMIC_FIELD_APPROVER_NAME = "approverName_";
    public static final String DYNAMIC_FIELD_APPROVER_ID_NAME = "approverIdName_";

    public static final String DYNAMIC_FIELD_APPROVER_STATUS_ID = "approverStatusId_";
    public static final String DYNAMIC_FIELD_APPROVER_STATUS_CODE = "approverStatusCode_";

    public static final String DYNAMIC_FIELD_APPROVER_EXACT_EMPLOYEE_ID = "approverExactEmployeeId_";
    public static final String DYNAMIC_FIELD_APPROVER_EXACT_EMPLOYEE_NAME = "approverExactEmployeeName_";

    public static final String DYNAMIC_FIELD_PREVIOUS_APPROVER_ID = "previousApproverId";
    public static final String DYNAMIC_FIELD_PREVIOUS_APPROVER_NAME = "previousApproverName";
    public static final String DYNAMIC_FIELD_PREVIOUS_APPROVER_ID_NAME = "previousApproverIdName";

    public static final String DYNAMIC_FIELD_PREVIOUS_APPROVER_STATUS_ID = "previousApproverStatusId";
    public static final String DYNAMIC_FIELD_PREVIOUS_APPROVER_STATUS_CODE = "previousApproverStatusCode";

    public static final String DYNAMIC_FIELD_PREVIOUS_APPROVER_EXACT_EMPLOYEE_ID = "previousApproverExactEmployeeId";
    public static final String DYNAMIC_FIELD_PREVIOUS_APPROVER_EXACT_EMPLOYEE_NAME = "previousApproverExactEmployeeName";

    public static final String DYNAMIC_FIELD_CURRENT_APPROVER_ID = "currentApproverId";
    public static final String DYNAMIC_FIELD_CURRENT_APPROVER_NAME = "currentApproverName";
    public static final String DYNAMIC_FIELD_CURRENT_APPROVER_ID_NAME = "currentApproverIdName";

    public static final String DYNAMIC_FIELD_CURRENT_APPROVER_STATUS_ID = "currentApproverStatusId";
    public static final String DYNAMIC_FIELD_CURRENT_APPROVER_STATUS_CODE = "currentApproverStatusCode";

    public static final String DYNAMIC_FIELD_CURRENT_APPROVER_EXACT_EMPLOYEE_ID = "currentApproverExactEmployeeId";
    public static final String DYNAMIC_FIELD_CURRENT_APPROVER_EXACT_EMPLOYEE_NAME = "currentApproverExactEmployeeName";

    public static final String DYNAMIC_FIELD_OVERALL_STATUS_ID = "overallStatusId";
    public static final String DYNAMIC_FIELD_OVERALL_STATUS_NAME = "overallStatusName";
    public static final String DYNAMIC_FIELD_OVERALL_STATUS_CODE = "overallStatusCode";

    /*
     compositeid generates by concatinating  companyid entityid and entitytype : 1_232_task  or 1_343_project or 1_543_assessment
     By default all fields are stored='false' except entityID. We use solr document only for textual search, but we are retrving data from real database
     */
    private String compositeID;
    private Integer companyID;
    /*
     entity type may be project, task , assessment
     */
    private String entityType;
//Entity id is field that stores entity's id it may be  EdsTask, EdsProject, EdsAssessment .  199    321   3838
    private Integer entityID;
//several users can have access to this document so this field will have a value something like this : 12   4533   3453    6994    21
    private Integer[] usersID;
//members of different groups also can have an access to this document : ADMINISTRATORS     PROJECT MANAGERS      DEPARTMENT LEADERS
    private Integer[] groupsID;
//entity name
    private String entityName;
//entity description
    private String entityDescription;
/*
entity assignees; In case of Project assignees are project members, in case fo Task assignees are employees assigned
to this task and managers of project that task belongs, In case of Assessment assignees are managers, colloborators, clients  ......
 */
    private String assignees;

    public static final String getDefaultSearchField() {
        return text;
    }

    public String getCompositeID() {
        return compositeID;
    }

    public void setCompositeID(String compositeID) {
        this.compositeID = compositeID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Integer[] getUsersID() {
        return usersID;
    }

    public void setUsersID(Integer[] usersID) {
        this.usersID = usersID;
    }

    public Integer[] getGroupsID() {
        return groupsID;
    }

    public void setGroupsID(Integer[] groupsID) {
        this.groupsID = groupsID;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityDescription() {
        return entityDescription;
    }

    public void setEntityDescription(String entityDescription) {
        this.entityDescription = entityDescription;
    }

    public String getAssignees() {
        return assignees;
    }

    public void setAssignees(String assignees) {
        this.assignees = assignees;
    }
}
