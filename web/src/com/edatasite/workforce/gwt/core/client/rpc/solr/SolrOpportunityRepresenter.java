package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 2/6/12
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class SolrOpportunityRepresenter implements IsSerializable {

    public static final String SPLIT = "@";
    public static final String ARROW = " -> ";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";

    public static final String FIELD_CRM_ACCOUNT_ID = "crmAccountId";
    public static final String FIELD_CRM_ACCOUNT_NAME = "crmAccountName";
    public static final String FIELD_CRM_ACCOUNT_NUMBER = "crmAccountNumber";
    public static final String FIELD_CRM_ACCOUNT_ID_NAME = "crmAccountIdName";

    public static final String FIELD_CRM_ACCOUNT_COUNTRY_ID = "crmAccountCountryId";
    public static final String FIELD_CRM_ACCOUNT_COUNTRY_NAME = "crmAccountCountryName";
    public static final String FIELD_CRM_ACCOUNT_COUNTRY_ID_NAME = "crmAccountCountryIdName";

    //Opportunity
    public static final String FIELD_OPPORTUNITY_ID = "opportunityId";
    public static final String FIELD_OPPORTUNITY_NAME = "opportunityName";
    public static final String FIELD_OPPORTUNITY_ID_NAME = "opportunityIdName";
    public static final String FIELD_OPPORTUNITY_NUMBER = "opportunityNumber";
    public static final String FIELD_OPPORTUNITY_STRING_NUMBER = "opportunityStringNumber";
    public static final String FIELD_OPPORTUNITY_INT_NUMBER = "opportunityIntNumber";
    public static final String FIELD_ASSIGNEE_ID = "assigneeId";
    public static final String FIELD_ASSIGNEE_NAME = "assigneeName";
    public static final String FIELD_ASSIGNEE_ID_NAME = "assigneeIdName";
    public static final String FIELD_OWNER_ID = "ownerId";
    public static final String FIELD_OWNER_NAME = "ownerName";
    public static final String FIELD_OWNER_ID_NAME = "ownerIdName";
    public static final String FIELD_BACKUP_ASSIGNEE_ID = "backupAssigneeId";
    public static final String FIELD_BACKUP_ASSIGNEE_NAME = "backupAssigneeName";
    public static final String FIELD_BACKUP_ASSIGNEE_ID_NAME = "backupAssigneeIdName";
    public static final String FIELD_CRM_CONTACT_ID = "crmContactId";
    public static final String FIELD_CRM_CONTACT_NAME = "crmContactName";
    public static final String FIELD_CRM_CONTACT_ID_NAME = "crmContactIdName";
    public static final String FIELD_CRM_CONTACT_PRIMARY_EMAIL = "crmContactPrimaryEmail";
    public static final String FIELD_CRM_CONTACT_EMAIL_ALLOWED = "crmContactEmailAllowed";
    public static final String FIELD_CRM_CONTACT_PRIMARY_PHONE = "crmContactPrimaryPhone";
    public static final String FIELD_OPPORTUNITY_STAGE_ID = "opportunityStageId";
    public static final String FIELD_OPPORTUNITY_STAGE_NAME = "opportunityStageName";
    public static final String FIELD_OPPORTUNITY_STAGE_UZ_NAME = "stageUzName";
    public static final String FIELD_OPPORTUNITY_STAGE_AR_NAME = "stageArName";
    public static final String FIELD_OPPORTUNITY_STAGE_EN_NAME = "stageEnName";
    public static final String FIELD_OPPORTUNITY_STAGE_RU_NAME = "stageRuName";
    public static final String FIELD_OPPORTUNITY_STAGE_CODE = "opportunityStageCode";
    public static final String FIELD_ESTIMATOR_ID = "estimatorId";
    //    public static final String FIELD_OPPORTUNITY_STAGE_ID_NAME = "OPPORTUNITY_STAGE_ID_NAME";
    public static final String FIELD_OPPORTUNITY_STAGE_ID_CODE = "opportunityStageIdCode";
    public static final String FIELD_OPPORTUNITY_STAGE_ID_CODE_NAME = "opportunityStageIdCodeName";
    public static final String FIELD_OPPORTUNITY_STAGE_SORDER = "opportunityStageSorder";
    public static final String FIELD_AMOUNT = "amount";
    public static final String FIELD_AMOUNT_BASE_CURRENCY = "amountBaseCurrency";
    public static final String FIELD_EXPECTED_REVENUE = "expectedRevenue";
    public static final String FIELD_OPPORTUNITY_CONVERT_PROJECT = "opportunityConvertProject";
    public static final String FIELD_CLOSING_DATE = "closingDate";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_CREATOR_ID = "creatorId";
    public static final String FIELD_CREATOR_NAME = "creatorName";
    public static final String FIELD_CREATOR_ID_NAME = "creatorIdName";
    public static final String FIELD_START_DATE = "START_DATE";
    public static final String FIELD_DUE_DATE = "DUE_DATE";
    public static final String FIELD_CONVERTED_FROM_LEAD = "convertedFromLead";

    public static final String FIELD_CAMPAIGN_ID = "campaignId";
    public static final String FIELD_CAMPAIGN_NAME = "campaignName";
    public static final String FIELD_CAMPAIGN_ID_NAME = "campaignIdName";
    public static final String FIELD_CAMPAIGN_COMPOSITE = "CAMPAIGN_COMPOSITE";

    public static final String FIELD_TYPE_ID = "typeId";
    public static final String FIELD_TYPE_ID_NAME = "typeIdName";
    public static final String FIELD_TYPE_NAME = "typeName";

    public static final String FIELD_NEXT_STEP = "nextStep";
    public static final String FIELD_LEAD_SOURCE_NAME = "leadSourceName";
    public static final String FIELD_LEAD_SOURCE_ID = "leadSourceId";
    public static final String FIELD_LEAD_SOURCE_ID_NAME = "leadSourceIdName";
    public static final String FIELD_PROBABILITY = "probability";
    public static final String FIELD_DESCRIPTION = "DESCRIPTION";

    public static final String FIELD_OPPORTUNITY_NAME_COMPOSITE = "opportunityNameComposite";
    public static final String FIELD_ACCOUNT_NAME_COMPOSITE = "accountNameComposite";
    public static final String FIELD_DYN_STRING_COMPOSITE = "dynStringComposite";
    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_MODIFICATION_DATE = "modificationDate";
    public static final String FIELD_CRM_CONTACT_NAME_COMPOSITE = "crmContactNameComposite";
    public static final String HAS_ATTACHMENT = "hasAttachment";

    // Solr sortable fields
    public static final String SORTABLE_OPPORTUNITY_STRING_NUMBER = "sortableOpportunityStringNumber";
    public static final String SORTABLE_ASSIGNEE_NAME = "sortableAssigneeName";
    public static final String SORTABLE_OPPORTUNITY_NAME = "sortableOpportunityName";
    public static final String SORTABLE_CRM_ACCOUNT_NAME = "sortableCrmAccountName";

    //related to fileds
    public static final String DYNAMIC_FIELD_RELATED_ID = "relatedId_*";
    public static final String DYNAMIC_FIELD_RELATED_NAME = "relatedName_*";
    public static final String DYNAMIC_FIELD_RELATED_ID_NAME = "relatedIdName_*";

    public static final String FIELD_RELATED_PROJECT_NAME = "relatedProjectName";
    public static final String FIELD_RELATED_PROJECT_CODE = "relatedProjectCode";
    public static final String FIELD_RELATED_PROJECT_NUMBER = "relatedProjectNumber";
    public static final String FIELD_RELATED_PROJECT_ID = "relatedProjectId";
    public static final String FIELD_RELATED_PROJECT_ID_NAME = "relatedProjectIdName";

    public static final String FIELD_MULTI_PROJECT_NAME = "multiProjectName";
    public static final String FIELD_MULTI_PROJECT_NUMBER = "multiProjectNumber";
    public static final String FIELD_MULTI_PROJECT_ID = "multiProjectId";
    public static final String FIELD_MULTI_PROJECT_ID_NAME = "multiProjectIdName";
    public static final String FIELD_MULTI_PROJECT_NUMBER_NAME = "multiProjectNumberName";

    //KANBAN ORDER
    public static final String OPPORTUNITY_KANBAN_ORDER = "opportunityKanbanOrder";

    public static String getSortField(String sortField) {
        if (sortField != null) {
            if (OpportunityListItem.NUMBER.equals(sortField)) {
                return SolrOpportunityRepresenter.SORTABLE_OPPORTUNITY_STRING_NUMBER;
            } else if (OpportunityListItem.ASSIGNEE_NAME.equals(sortField)) {
                return SolrOpportunityRepresenter.SORTABLE_ASSIGNEE_NAME;
            } else if (OpportunityListItem.OPPORTUNITY_NAME.equals(sortField)) {
                return SolrOpportunityRepresenter.SORTABLE_OPPORTUNITY_NAME;
            } else if (OpportunityListItem.AMOUNT.equals(sortField)) {
                return SolrOpportunityRepresenter.FIELD_AMOUNT;
            } else if (OpportunityListItem.STAGE.equals(sortField)) {
                return SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_CODE;
            } else if (OpportunityListItem.CLOSING_DATE.equals(sortField)) {
                return SolrOpportunityRepresenter.FIELD_CLOSING_DATE;
            } else if (OpportunityListItem.CREATED_DATE.equals(sortField)) {
                return SolrOpportunityRepresenter.FIELD_CREATION_DATE;
            } else if (OpportunityListItem.UPDATED_DATE.equals(sortField)) {
                return SolrOpportunityRepresenter.FIELD_MODIFICATION_DATE;
            } else if (OpportunityListItem.ACCOUNT_NAME.equals(sortField)) {
                return SolrOpportunityRepresenter.SORTABLE_CRM_ACCOUNT_NAME;
            } else if (OpportunityListItem.ISCONVERTEDTOPROJECT.equals(sortField)) {
                return SolrOpportunityRepresenter.FIELD_OPPORTUNITY_CONVERT_PROJECT;
            } else if (OpportunityListItem.CREATOR_NAME.equals(sortField)) {
                return SolrOpportunityRepresenter.FIELD_CREATOR_NAME;
            } else if (OpportunityListItem.OPPORTUNITY_LEAD_SOURCE.equals(sortField)) {
                return SolrOpportunityRepresenter.FIELD_LEAD_SOURCE_NAME;
            } else if (OpportunityListItem.CAMPAIGN.equals(sortField)) {
                return SolrOpportunityRepresenter.FIELD_CAMPAIGN_NAME;
            } else if (OpportunityListItem.OBJECT_ID.equals(sortField)) {
                return SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID;
            } else if (OpportunityListItem.COUNTRY_NAME.equals(sortField)) {
                return SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_COUNTRY_NAME;
            } else if (OpportunityListItem.OPPORTUNITY_CONTACT_NAME.equals(sortField)) {
                return SolrOpportunityRepresenter.FIELD_CRM_CONTACT_NAME;
            } else if (OpportunityListItem.KANBAN_ORDER.equals(sortField)) {
                return OPPORTUNITY_KANBAN_ORDER;
            } else if (OpportunityListItem.OPPORTUNITY_ATTACHMENT.equals(sortField)) {
                return SolrOpportunityRepresenter.HAS_ATTACHMENT;
            }
        }
        return null;

    }


}
