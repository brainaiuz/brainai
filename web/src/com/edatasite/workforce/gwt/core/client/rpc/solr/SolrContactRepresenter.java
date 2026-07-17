package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sher
 * Date: 05.08.2010
 * Time: 19:16:40
 * To change this template use File | Settings | File Templates.
 */
public class SolrContactRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_CONTACT_ID = "contactId";
    public static final String FIELD_CONTACT_NAME = "contactName";
    public static final String FIELD_CONTACT_NAME_COMPOSITE = "contactNameComposite";
    public static final String FIELD_EMAIL_COMPOSITE = "emailComposite";
    public static final String FIELD_EMAIL_NAME_COMPOSITE = "emailNameComposite";
    public static final String FIELD_LOOKUP_COMPOSITE_MOBILE = "lookupCompositeMobile";
    public static final String FIELD_CONTACT_NAME_TEXT_FIELD = "n";
    public static final String FIELD_CONTACT_NAME_L_TEXT_FIELD = "nl";
    public static final String FIELD_PRIMARIES_COMPOSITE = "primariesComposite";

    public static final String FIELD_CONTACT_FIRST_COMPOSITE = "contactFirstComposite";
    public static final String FIELD_CONTACT_LAST_COMPOSITE = "contactLastComposite";
    public static final String FIELD_ACCOUNT_NAME_COMPOSITE = "accountNameComposite";
    public static final String FIELD_DYN_STRING_COMPOSITE = "dynStringComposite";

    public static final String FIELD_COUNTRY_ID = "countryId";
    public static final String FIELD_COUNTRY_NAME = "countryName";
    public static final String FIELD_COUNTRY_CODE = "countryCode";
    public static final String FIELD_COUNTRY_ID_CODE = "countryIdCode";
    public static final String FIELD_COUNTRY_ID_CODE_NAME = "countryIdCodeName";
    public static final String FIELD_STATE_ID = "stateId";
    public static final String FIELD_STATE_NAME = "stateName";
    public static final String FIELD_STATE_ID_NAME = "stateIdName";
    public static final String FIELD_STREET = "street";
    public static final String FIELD_STREET2 = "street2";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_POST_CODE = "postCode";
    public static final String FIELD_LONGITUDE = "longitude";
    public static final String FIELD_LATITUDE = "latitude";

    public static final String FIELD_OWNER_ID = "ownerId";
    public static final String FIELD_OWNER_NAME = "ownerName";
    public static final String FIELD_OWNER_ID_NAME = "ownerIdName";
    public static final String FIELD_CONTACT_TYPE = "contactType";//values like LEAD/CONTACT/CANDIDATE
    public static final String FIELD_FIRST_NAME = "firstName";
    public static final String FIELD_MIDDLE_NAME = "middleName";
    public static final String FIELD_LAST_NAME = "lastName";
    public static final String FIELD_REF_IND_NUMBER = "refIndNumber";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_IS_PRIMARY_CONTACT = "isPrimaryContact";
    public static final String FIELD_PRIMARY_EMAIL = "primaryEmail";
    public static final String FIELD_PRIMARY_PHONE = "primaryPhone";
    public static final String FIELD_EXTENSION = "extension";
    public static final String FIELD_FAX = "fax";
    public static final String FIELD_MOBILE = "mobile";
    public static final String FIELD_WORK_PHONE = "workPhone";
    public static final String FIELD_CREATOR_ID = "creatorId";
    public static final String FIELD_CREATOR_NAME = "creatorName";
    public static final String FIELD_CREATOR_ID_NAME = "creatorIdName";
    public static final String FIELD_UPDATER_ID = "updaterId"; //new
    public static final String FIELD_UPDATER_NAME = "updaterName";//new
    public static final String FIELD_UPDATER_ID_NAME = "updaterIdName";//new

    public static final String FIELD_WEBSITE = "website";
    public static final String FIELD_UPDATE_DATE = "updateDate";
    public static final String FIELD_CATEGORY_ID = "categoryId";
    public static final String FIELD_CATEGORY_NAME = "categoryName";
    public static final String FIELD_CATEGORY_ID_NAME = "categoryIdName";
    public static final String FIELD_CATEGORY_NAME_SORT = "categoryNameSort";

    public static final String FIELD_CRM_ACCOUNT_ID = "accountId";
    public static final String FIELD_CRM_ACCOUNT_OWNER_ID = "accountOwnerId";
    public static final String FIELD_CRM_ACCOUNT_NAME = "accountName";
    public static final String FIELD_CRM_ACCOUNT_NUMBER = "accountNumber";
    public static final String FIELD_CRM_ACCOUNT_ID_NAME = "accountIdName";
    public static final String FIELD_CRM_ACCOUNT_TYPE = "accountType";
    public static final String FIELD_CRM_ACCOUNT_INDUSTRY = "accountIndustry";
    public static final String FIELD_CRM_ACCOUNT_INDUSTRY_ID = "accountIndustryId";

    public static final String FIELD_CLIENTCONTACT_ID = "clientContactId";
    public static final String FIELD_ACCESS_ENABLED = "accessEnabled";

    public static final String FIELD_CAMPAIGN_ID = "campaignId";
    public static final String FIELD_CAMPAIGN_NAME = "campaignName";
    public static final String FIELD_CAMPAIGN_ID_NAME = "campaignIdName";
    public static final String FIELD_DEPARTMENT = "department";
    public static final String FIELD_DATE_OF_BIRTH = "dateOfBirth";
    public static final String FIELD_REPORTS_TO = "reportsTo";
    public static final String FIELD_REPORTS_TO_ID = "reportsToId";
    public static final String FIELD_EMAIL_ALLOWED = "emailAllowed";
    public static final String FIELD_GOOGLEID = "googleId";
    public static final String FIELD_CATEGORY_IDS = "categoryIds"; //new
    public static final String FIELD_COMPOSITE = "composite";
    //LEAD FIELDS
    public static final String FIELD_LEAD_COMPOSITE = "leadComposite";
    public static final String FIELD_LEAD_NAME_COMPOSITE = "leadNameComposite";
    public static final String FIELD_LEAD_ASSIGNEE = "assignee";
    public static final String FIELD_LEAD_ASSIGNEE_ID = "assigneeId";
    public static final String FIELD_LEAD_ASSIGNEE_ID_NAME = "assigneeIdName";
    public static final String FIELD_LEAD_BACKUP_ASSIGNEE = "backupAssignee";
    public static final String FIELD_LEAD_BACKUP_ASSIGNEE_ID = "backupAssigneeId";
    public static final String FIELD_LEAD_BACKUP_ASSIGNEE_ID_NAME = "backupAssigneeIdName";
    public static final String FIELD_LEAD_RATING = "rating";
    public static final String FIELD_LEAD_RATING_ID = "ratingId";
    public static final String FIELD_LEAD_RATING_CODE = "ratingCode";
    public static final String FIELD_LEAD_RATING_ID_CODE = "ratingIdCode";
    //    public static final String FIELD_LEAD_RATING_ID_NAME = "RATING_ID_NAME";
    public static final String FIELD_LEAD_STATUS = "status";
    public static final String FIELD_LEAD_STATUS_ID = "statusId";
    public static final String FIELD_LEAD_STATUS_CODE = "statusCode";
    public static final String FIELD_LEAD_STATUS_ID_CODE = "statusIdCode";
    public static final String FIELD_LEAD_STATUS_ID_CODE_NAME = "statusIdCodeName";
    public static final String FIELD_LEAD_STATUS_SORDER = "statusSorder";
    //    public static final String FIELD_LEAD_STATUS_ID_NAME = "STATUS_ID_NAME";
    public static final String FIELD_LEAD_SOURCE = "leadSource";
    public static final String FIELD_LEAD_SOURCE_ID = "leadSourceId";
    public static final String FIELD_LEAD_SOURCE_CODE = "leadSourceCode";
    public static final String FIELD_LEAD_SOURCE_ID_CODE = "leadSourceIdCode";
    public static final String FIELD_LEAD_SOURCE_ID_CODE_NAME = "leadSourceIdCodeName";
    //    public static final String FIELD_LEAD_SOURCE_ID_NAME = "LEAD_SOURCE_ID_NAME";
    public static final String FIELD_LEAD_SOURCE_OTHER = "leadSourceOther";
    public static final String FIELD_LEAD_KANBAN_ORDER = "LEAD_KANBAN_ORDER";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_JOB_TITLE = "jobTitle";
    //CANDIDATE
    public static final String FIELD_NUMBER = "number";
    public static final String FIELD_WORK_EXPERIENCE = "workExperience";
    public static final String FIELD_WORK_EXPERIENCE_MONTH_YEAR = "workExperienceMonthYear";
    public static final String FIELD_CURRENT_EMPLOYER = "currentEmployer";
    public static final String FIELD_EXPECTED_SALARY = "expectedSalary";
    public static final String FIELD_PREFERRED_LOCATION = "preferredLocation";
    public static final String FIELD_PREFERRED_LOCATION_ID = "preferredLocationId";
    public static final String FIELD_PREFERRED_LOCATION_ID_NAME = "preferredLocationIdName";
    public static final String FIELD_CANDIDATE_DEPARTMENT = "candidateDepartment";
    public static final String FIELD_CANDIDATE_DEPARTMENT_ID = "candidateDepartmentId";
    public static final String FIELD_CANDIDATE_DEPARTMENT_ID_NAME = "candidateDepartmentIdName";
    public static final String FIELD_CANDIDATE_POSITION = "candidatePosition";
    public static final String FIELD_CANDIDATE_POSITION_ID = "candidatePositionId";
    public static final String FIELD_CANDIDATE_POSITION_ID_NAME = "candidatePositionIdName";
    public static final String FIELD_VACANCY_ID = "vacancyId";
    public static final String FIELD_VACANCY_NAME = "vacancyName";
    public static final String FIELD_VACANCY_ID_NAME = "vacancyIdName";
    public static final String FIELD_IS_SHORT_LIST = "isShortList";
    public static final String FIELD_CANDIDATE_SKILLS = "candidateSkills";
    public static final String FIELD_CANDIDATE_PROJECT = "candidateProject";
    public static final String FIELD_CANDIDATE_PROJECT_ID = "candidateProjectId";
    public static final String FIELD_CANDIDATE_PROJECT_ID_NAME = "candidateProjectIdName";
    public static final String FIELD_CANDIDATE_STATUS = "candidateStatus";
    public static final String FIELD_CANDIDATE_STATUS_ID = "candidateStatusId";
    public static final String FIELD_CANDIDATE_STATUS_ID_NAME = "candidateStatusIdName";

    public static final String CUSTOM_FIELD_STRING = "stringValue";
    public static final String CUSTOM_FIELD_DOUBLE = "doubleValue";
    public static final String CUSTOM_FIELD_DATE = "dateValue";

    public static final String FIELD_CANDIDATE_CREATED_BY = "candidateCreatedBy";
    public static final String FIELD_CANDIDATE_CREATED_BY_ID = "candidateCreatedById";
    public static final String FIELD_CANDIDATE_CREATED_BY_ID_NAME = "candidateCreatedByIdName";
    public static final String FIELD_CANDIDATE_KANBAN_ORDER = "candidateKanbarOrder";//new

    public static final String FIELD_MAIL_LIST_ID = "mailListId";
    public static final String FIELD_MAIL_LIST_NAME = "mailListName";
    public static final String FIELD_MAIL_LIST_ID_NAME = "mailListIdName";

    public static final String FIELD_IS_FAVOURITED = "isFavourited";

    // Sor Sortable Fields
    public static final String SORTABLE_CONTACT_NAME = "sortableContactName";
    public static final String SORTABLE_FIRST_NAME = "sortableFirstName";
    public static final String SORTABLE_LAST_NAME = "sortableLastName";
    public static final String SORTABLE_OWNER_NAME = "sortableOwnerName";
    public static final String SORTABLE_ACCOUNT_NAME = "sortableAccountName";
    public static final String SORTABLE_PRIMARY_PHONE = "sortablePrimaryPhone";
    public static final String SORTABLE_COUNTRY_NAME = "sortableCountryName";
    public static final String SORTABLE_PRIMARY_EMAIL = "sortablePrimaryEmail";
    public static final String SORTABLE_CONTACT_TYPE = "sortableContactType";
    public static final String SORTABLE_DEPARTMENT = "sortableDepartment";
    public static final String SORTABLE_POSITION = "SORTABLE_POSITION";
    public static final String SORTABLE_REPORTS_TO = "sortableReportsTo";
    public static final String SORTABLE_CAMPAIGN_NAME = "sortableCampaignName";
    public static final String SORTABLE_LEAD_ASSIGNEE = "sortableAssignee";
    public static final String SORTABLE_LEAD_BACKUP_ASSIGNEE = "sortableBackupAssignee";
    public static final String SORTABLE_JOB_TITLE = "sortableJobTitle";
    public static final String SORTABLE_STREET = "sortableStreet";
    public static final String SORTABLE_CITY = "sortableCity";
    public static final String SORTABLE_STATE_NAME = "sortableStateName";
    public static final String SORTABLE_LEAD_SOURCE = "sortableLeadSource";
    public static final String SORTABLE_FAX = "sortableFax";
    public static final String SORTABLE_LEAD_RATING = "sortableRating";
    public static final String SORTABLE_MOBILE = "sortableMobile";
    public static final String SORTABLE_TITLE = "sortableTitle";
    public static final String SORTABLE_POST_CODE = "sortablePostCode";
    public static final String SORTABLE_NUMBER = "SORTABLE_NUMBER";
    public static final String SORTABLE_CANDIDATE_SKILLS = "sortableCandidateSkills";
    public static final String SORTABLE_WEBSITE = "sortableWebsite";
    public static final String SORTABLE_CREATOR = "sortableCreator";
    public static final String SORTABLE_PROJECT = "sortableProject";
    public static final String CREATOR_NAME = "creatorName"; //new
    public static final String UPDATER_NAME = "updaterName"; //new
    public static final String SORTABLE_CATEGORY_NAME_SORT = "sortableCategoryNameSort";
    public static final String LEAD_KANBAN_ORDER = "leadKanbanOrder";

    public static String getSortingField(String sortingFieldOfClientSide) {
        if (SolrContactRepresenter.FIELD_CONTACT_ID.equals(sortingFieldOfClientSide)) {
            return FIELD_CONTACT_ID;
        } else if (ContactListItem.CONTACT_NAME.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CONTACT_NAME;
        } else if (ContactListItem.OWNER.equals(sortingFieldOfClientSide)) {
            return SORTABLE_OWNER_NAME;
        } else if (ContactListItem.FIRST_NAME.equals(sortingFieldOfClientSide)) {
            return SORTABLE_FIRST_NAME;
        } else if (ContactListItem.LAST_NAME.equals(sortingFieldOfClientSide)) {
            return SORTABLE_LAST_NAME;
        } else if (ContactListItem.CRM_ACCOUNT.equals(sortingFieldOfClientSide)) {
            return SORTABLE_ACCOUNT_NAME;
        } else if (ContactListItem.PHONE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_PRIMARY_PHONE;
        } else if (ContactListItem.COUNTRY.equals(sortingFieldOfClientSide)) {
            return SORTABLE_COUNTRY_NAME;
        } else if (ContactListItem.EMAIL.equals(sortingFieldOfClientSide)) {
            return SORTABLE_PRIMARY_EMAIL;
        } else if (ContactListItem.TITLE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_TITLE;
        } else if (ContactListItem.TYPE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CONTACT_TYPE;
        } else if (ContactListItem.DEPARTMENT.equals(sortingFieldOfClientSide)) {
            return SORTABLE_DEPARTMENT;
        } else if (ContactListItem.POSITION.equals(sortingFieldOfClientSide)) {
            return SORTABLE_POSITION;
        } else if (ContactListItem.DATE_OF_BIRTH.equals(sortingFieldOfClientSide)) {
            return FIELD_DATE_OF_BIRTH;
        } else if (ContactListItem.REPORTS_TO.equals(sortingFieldOfClientSide)) {
            return SORTABLE_REPORTS_TO;
        } else if (ContactListItem.EMAIL_ALLOWED.equals(sortingFieldOfClientSide)) {
            return FIELD_EMAIL_ALLOWED;
        } else if (ContactListItem.CATEGORIES.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CATEGORY_NAME_SORT;
        } else if (ContactListItem.LEAD_ASSIGNEE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_LEAD_ASSIGNEE;
        } else if (ContactListItem.LEAD_BACKUP_ASSIGNEE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_LEAD_BACKUP_ASSIGNEE;
        } else if (ContactListItem.JOB_TITLE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_JOB_TITLE;
        } else if (ContactListItem.STREET.equals(sortingFieldOfClientSide)) {
            return SORTABLE_STREET;
        } else if (ContactListItem.CITY.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CITY;
        } else if (ContactListItem.POST_CODE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_POST_CODE;
        } else if (ContactListItem.COUNTRY.equals(sortingFieldOfClientSide)) {
            return SORTABLE_COUNTRY_NAME;
        } else if (ContactListItem.STATE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_STATE_NAME;
        } else if (ContactListItem.LEAD_SOURCE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_LEAD_SOURCE;
        } else if (ContactListItem.CAMPAIGN.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CAMPAIGN_NAME;
        } else if (ContactListItem.LEAD_STATUS.equals(sortingFieldOfClientSide)) {
            return FIELD_LEAD_STATUS;
        } else if (ContactListItem.EMAIL.equals(sortingFieldOfClientSide)) {
            return SORTABLE_PRIMARY_EMAIL;
        } else if (ContactListItem.PHONE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_PRIMARY_PHONE;
        } else if (ContactListItem.FAX.equals(sortingFieldOfClientSide)) {
            return SORTABLE_FAX;
        } else if (ContactListItem.MOBILE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_MOBILE;
        } else if (ContactListItem.EMAIL_ALLOWED.equals(sortingFieldOfClientSide)) {
            return FIELD_EMAIL_ALLOWED;
        } else if (ContactListItem.LEAD_RATING.equals(sortingFieldOfClientSide)) {
            return SORTABLE_LEAD_RATING;
        } else if (ContactListItem.LAST_MODIFIED.equals(sortingFieldOfClientSide)) {
            return FIELD_UPDATE_DATE;
        } else if (ContactListItem.CREATION_DATE.equals(sortingFieldOfClientSide)) {
            return FIELD_CREATION_DATE;
        } else if (ContactListItem.CANDIDATE_SKILLS.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CANDIDATE_SKILLS;
        } else if (ContactListItem.WEBSITE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_WEBSITE;
        } else if (ContactListItem.PROJECT.equals(sortingFieldOfClientSide)) {
            return SORTABLE_PROJECT;
        } else if (ContactListItem.CREATED_BY.equals(sortingFieldOfClientSide)) {
            return CREATOR_NAME;
        } else if (ContactListItem.KANBAN_ORDER.equals(sortingFieldOfClientSide)) {
            return LEAD_KANBAN_ORDER;
        } else if (ContactListItem.UPDATED_BY.equals(sortingFieldOfClientSide)) {
            return UPDATER_NAME;
        }
        return null;
    }
}