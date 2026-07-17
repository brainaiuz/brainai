package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Azazello on 2/7/16.
 */
public class SolrEmployeeStepRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";

    public static final String FIELD_ONBOARDING_STEP_ID = "onboardingStepId";
    public static final String FIELD_ONBOARDING_STEP_FORM_ID = "onboardingStepFormId";
    public static final String FIELD_ONBOARDING_STEP_NAME = "onboardingStepName";
    public static final String FIELD_ONBOARDING_STEP_ID_NAME = "onboardingStepIdName";

    public static final String FIELD_STEP_ID = "stepId";
    public static final String FIELD_WORKFLOW_ID = "workflowId";

    public static final String FIELD_EMPLOYEE_ID = "employeeId";
    public static final String FIELD_EMPLOYEE_NAME = "employeeName";
    public static final String FIELD_EMPLOYEE_CODE = "employeeCode";
    public static final String FIELD_CANDIDATE_CODE = "candidateCode";
    public static final String FIELD_EMPLOYEE_ID_NAME = "employeeIdName";

    public static final String FIELD_EMPLOYEE_LOCATION_ID = "employeeLocationId";
    public static final String FIELD_EMPLOYEE_LOCATION_NAME = "employeeLocationName";
    public static final String FIELD_EMPLOYEE_LOCATION_ID_NAME = "employeeLocationIdName";
    public static final String FIELD_EMPLOYEE_LOCATION_STATE = "employeeLocationState";
    public static final String FIELD_EMPLOYEE_LOCATION_CITY = "employeeLocationCity";

    public static final String FIELD_CREATOR_ID = "creatorId";
    public static final String FIELD_CREATOR_NAME = "creatorName";
    public static final String FIELD_CREATOR_ID_NAME = "creatorIdName";

    public static final String FIELD_TYPE_ID = "typeId";
    public static final String FIELD_TYPE_NAME = "typeName";
    public static final String FIELD_TYPE_CODE = "typeCode";
    public static final String FIELD_TYPE_ID_NAME = "typeIdName";

    public static final String FIELD_STATUS_ID = "statusId";
    public static final String FIELD_STATUS_NAME = "statusName";
    public static final String FIELD_STATUS_ID_NAME = "statusIdName";

    public static final String FIELD_MODIFICATION_DATE = "modificationDate";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_ARCHIVED = "archived";

    public static final String FIELD_CURRENT_APPROVER_ID = "currentApproverId";
    public static final String FIELD_APPROVER_APPROVE_STATUS_ID = "approverApproveStatusId";
    public static final String FIELD_APPROVER_REJECT_STATUS_ID = "approverRejectStatusId";

    public static final String FIELD_DYN_STRING_COMPOSITE = "dynStringComposite";
    public static final String FIELD_COMPOSITE = "composite";

    // Solr sortable fields
    public static final String SORTABLE_STATUS_NAME = "sortableStatusName";
    public static final String SORTABLE_EMPLOYEE_NAME = "sortableEmployeeName";
    public static final String SORTABLE_EMPLOYEE_CODE = "sortableEmployeeCode";
    public static final String SORTABLE_CANDIDATE_CODE = "sortableCandidateCode";

    public static String getSortField(String sortField) {
        if (sortField != null) {
            if (EmployeeStepItem.EMPLOYEE.equals(sortField)) {
                return SORTABLE_EMPLOYEE_NAME;
            } else if (EmployeeStepItem.EMPLOYEE_CODE.equals(sortField)) {
                return SORTABLE_EMPLOYEE_CODE;
            } else if (EmployeeStepItem.CANDIDATE_CODE.equals(sortField)) {
                return SORTABLE_CANDIDATE_CODE;
            } else if (EmployeeStepItem.STATUS.equals(sortField)) {
                return SORTABLE_STATUS_NAME;
            } else if (EmployeeStepItem.CREATION_DATE.equals(sortField)) {
                return FIELD_CREATION_DATE;
            } else if (EmployeeStepItem.UPDATED_DATE.equals(sortField)) {
                return FIELD_MODIFICATION_DATE;
            }
        }
        return null;
    }
}
