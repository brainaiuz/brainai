package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by dilsh0d on 14.01.16.
 */
public class SolrVacancyRepresenter implements IsSerializable {

    public static final String SPLIT = "@";
    public static final String COMPOSITE_ID = "oid";
    public static final String COMPOSITE = "composite";
    public static final String COMPANY_ID = "companyId";
    public static final String FIELD_VACANCY_ID = "vacancyId";
    public static final String FIELD_VACANCY_NUMBER = "vacancyNumber";
    public static final String FIELD_JOB_FAMILY_ID = "jobFamilyId";
    public static final String FIELD_JOB_FAMILY_NAME = "jobFamilyName";
    public static final String FIELD_JOB_FAMILY_NAME_ID = "jobFamilyNameId";
    public static final String FIELD_JOB_TYPE_ID = "jobTypeId";
    public static final String FIELD_JOB_TYPE_NAME = "jobTypeName";
    public static final String FIELD_JOB_TYPE_NAME_ID = "jobTypeNameId";
    public static final String FIELD_JOB_TITLE = "jobTitle";

    public static final String FIELD_VACANCY_STATUS = "vacancyStatus";
    public static final String FIELD_VACANCY_STATUS_ID = "vacancyStatusId";
    public static final String FIELD_VACANCY_STATUS_ID_CODE = "vacancyStatusIdCode";
    public static final String FIELD_VACANCY_STATUS_ID_CODE_NAME = "vacancyStatusIdCodeName";
    public static final String FIELD_VACANCY_STATUS_CODE = "vacancyStatusCode";
    public static final String FIELD_VACANCY_STATUS_SORDER = "vacancyStatusSorder";

    public static final String FIELD_RDEGREE_STATUS = "rdegreeStatus";
    public static final String FIELD_RDEGREE_STATUS_ID = "rdegreeStatusId";
    public static final String FIELD_RDEGREE_STATUS_ID_CODE = "rdegreeStatusIdCode";
    public static final String FIELD_RDEGREE_STATUS_ID_CODE_NAME = "rdegreeStatusIdCodeName";
    public static final String FIELD_RDEGREE_STATUS_CODE = "rdegreeStatusCode";
    public static final String FIELD_RDEGREE_STATUS_SORDER = "rdegreeStatusSorder";

    public static final String FIELD_MANAGER_ID = "managerId";
    public static final String FIELD_MANAGER_NAME = "managerName";
    public static final String FIELD_MANAGER_ID_NAME = "managerIdName";

    public static final String FIELD_BACKUP_MANAGER_ID = "backupManagerId";
    public static final String FIELD_BACKUP_MANAGER_NAME = "backupManagerName";
    public static final String FIELD_BACKUP_MANAGER_ID_NAME = "backupManagerIdName";

    public static final String FIELD_POSITION_ID = "positionId";
    public static final String FIELD_POSITION_NAME = "positionName";
    public static final String FIELD_POSITION_ID_NAME = "positionIdName";

    public static final String FIELD_LOCATION_ID = "locationId";
    public static final String FIELD_LOCATION_NAME = "locationName";
    public static final String FIELD_LOCATION_ID_NAME = "locationIdName";

    public static final String FIELD_START_DATE = "startDate";
    public static final String FIELD_END_DATE = "endDate";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";

    public static final String FIELD_PROJECT_ID = "projectId";
    public static final String FIELD_PROJECT_NAME = "projectName";
    public static final String FIELD_PROJECT_ID_NAME = "projectIdName";
    public static final String FIELD_COUNTRY_ID = "countryId";
    public static final String FIELD_COUNTRY_NAME = "countryName";
    public static final String FIELD_COUNTRY_ID_NAME = "countryIdName";
    public static final String FIELD_EMBASSY_ID = "embassyId";
    public static final String FIELD_EMBASSY_NAME = "embassyName";
    public static final String FIELD_EMBASSY_ID_NAME = "embassyIdName";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_PROPOSED_SALARY = "proposedSalary";
    public static final String FIELD_JOB_REQUIREMENTS = "jobRequirements";
    public static final String FIELD_CONTRACT_FROM = "contractFrom";
    public static final String FIELD_CONTRACT_TO = "contractTo";
    public static final String FIELD_VACANCY_TYPE = "vacancyType";
    public static final String FIELD_VACANCY_TYPE_NAME = "vacancyTypeName";
    public static final String FIELD_VACANCY_RELIGION = "vacancyReligion";
    public static final String FIELD_CREATED_DATE = "createdDate";
    public static final String FIELD_MODIFIED_BY = "modifiedBy";
    public static final String FIELD_CREATED_BY= "createdBy";

    public static final String FIELD_SORTABLE_LAST_UPDATE_DATE = "sortableLastUpdateDate";

    public static final String FIELD_SORTABLE_VACANCY_NUMBER = "sortableVacancyNumber";
    public static final String FIELD_SORTABLE_JOB_FAMILY_NAME = "sortableJobFamilyName";
    public static final String FIELD_SORTABLE_JOB_TYPE_NAME = "sortableJobTypeName";
    public static final String FIELD_SORTABLE_JOB_TITLE = "sortableJobTitle";
    public static final String FIELD_SORTABLE_VACANCY_STATUS = "sortableVacancyStatus";
    public static final String FIELD_SORTABLE_RDEGREE_STATUS = "sortableRdegreeStatus";
    public static final String FIELD_SORTABLE_MANAGER_NAME = "sortableManagerName";
    public static final String FIELD_SORTABLE_POSITION_NAME = "sortablePositionName";
    public static final String FIELD_SORTABLE_LOCATION_NAME = "sortableLocationName";

    public static final String FIELD_SORTABLE_MODIFIED_BY = "sortableModifiedBy";

    public static final String FIELD_SORTABLE_CURRENCY = "sortableCurrency";

    public static final String FIELD_DEPARTMENT_ID = "departmentId";
    public static final String FIELD_DEPARTMENT_NAME = "departmentName";
    public static final String FIELD_DEPARTMENT_ID_NAME = "departmentIdName";

    public static final String FIELD_APPROVAL_STATUS_ID = "approvalStatusId";
    public static final String FIELD_APPROVAL_STATUS_NAME = "approvalStatusName";
    public static final String FIELD_APPROVAL_STATUS_ID_NAME = "approvalStatusIdName";

    public static final String FIELD_APPROVER_ID = "approverId";
    public static final String FIELD_APPROVER_NAME = "approverName";
    public static final String FIELD_APPROVER_ID_NAME = "approverIdName";

    public static final String FIELD_SORTABLE_DEPARTMENT_NAME = "sortableDepartmentName";

    public static final String FIELD_CURRENCY_ID = "currencyId";
    public static final String FIELD_CURRENCY_NAME = "currencyName";
    public static final String FIELD_CURRENCY_ID_NAME = "currencyIdName";

    public static final String FIELD_NAME_EN = "nameEn";
    public static final String FIELD_NAME_RU = "nameRu";
    public static final String FIELD_NAME_AR = "nameAr";
    public static final String FIELD_NAME_UZ = "nameUz";
    public static final String FIELD_NAME = "NAME_";

    public static final String FIELD_SORTABLE_NAME = "SORTABLE_NAME_";
}
