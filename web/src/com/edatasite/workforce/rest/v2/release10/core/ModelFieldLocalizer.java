package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.Operands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;

/**
 * Created by Anvar Akramov on 3/27/18.
 */
@Service("modelFieldLocalizer")
public class ModelFieldLocalizer implements CustomFormConstants, Operands {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

//    protected static final WfmMessages wfmMessages = WfmMessages.App.get();

    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;

    @Autowired
    @Qualifier("crmLocalizer")
    private WfmMessageSource crmLocalizer;

    public String localizeByFieldID(String formID, String fieldID) {
        if (formID == null) {
            return localizeByCode(fieldID);
        }
        if (fieldID == null) {
            return null;
        }
        return switch (formID) {
            case LayoutRPC.LEAD_FORM, LayoutRPC.CONTACT_FORM, LayoutRPC.CANDIDATE_FORM -> localizeContact(fieldID);
            case LayoutRPC.ACTIVITY_FORM -> localizeEvent(fieldID);
            case LayoutRPC.OPPORTUNITY_FORM -> localizeOpportunity(fieldID);
            case LayoutRPC.ACCOUNT_FORM, LayoutRPC.CLIENT_FORM, LayoutRPC.SUPPLIER_FORM -> localizeCrmAccount(fieldID);
            case LayoutRPC.TASK_MAX_FORM -> localizeTask(fieldID);
            default -> localizeByCode(fieldID);
        };
    }

    public String localizeByCode(String code) {
        if (code != null) {
            if (Operands.Core.EQUAL.equals(code)) {
                return commonLocalizer.localize("equal");
            } else if (Operands.Core.NOT_EQUAL.equals(code)) {
                return commonLocalizer.localize("notEqual");
            } else if (StringT.CONTAINS.equals(code)) {
                return commonLocalizer.localize("contains");
            } else if (StringT.NOT_CONTAINS.equals(code)) {
                return commonLocalizer.localize("notContains");
            } else if (StringT.MATCHES.equals(code)) {
                return commonLocalizer.localize("matches");
            } else if (NumberT.GREATER.equals(code)) {
                return commonLocalizer.localize("greater");
            } else if (NumberT.GREATER_OR_EQUAL.equals(code)) {
                return commonLocalizer.localize("greaterOrEqual");
            } else if (NumberT.LOWER.equals(code)) {
                return commonLocalizer.localize("lower");
            } else if (NumberT.LOWER_OR_EQUAL.equals(code)) {
                return commonLocalizer.localize("lowerOrEqual");
            } else if (DateT.IS.equals(code)) {
                return commonLocalizer.localize("is");
            } else if (DateT.IS_NOT.equals(code)) {
                return commonLocalizer.localize("isNot");
            } else if (DateT.IS_AFTER.equals(code)) {
                return commonLocalizer.localize("isAfter");
            } else if (DateT.IS_BEFORE.equals(code)) {
                return commonLocalizer.localize("isBefore");
            } else if (DateT.BETWEEN.equals(code)) {
                return commonLocalizer.localize("between");
            } else if (DateT.CURRENT_DAY.equals(code)) {
                return commonLocalizer.localize("currentDayOnly");
            } else if (DateT.NOT_BETWEEN.equals(code)) {
                return commonLocalizer.localize("notBetween");
            } else if (DateT.TODAY.equals(code)) {
                return commonLocalizer.localize("today");
            } else if (DateT.YESTERDAY.equals(code)) {
                return commonLocalizer.localize("yesterday");
            } else if (DateT.TOMORROW.equals(code)) {
                return commonLocalizer.localize("tomorrow");
            } else if (DateT.AGE_IN_DAYS.equals(code)) {
                return commonLocalizer.localize("ageInDays");
            } else if (DateT.AGE_IN_HOURS.equals(code)) {
                return commonLocalizer.localize("ageInHours");
            } else if (DateT.HAS_DAYS_LEFT.equals(code)) {
                return commonLocalizer.localize("hasDaysLeft");
            } else if (CREATED_DATE.equals(code)) {
                return commonLocalizer.localize("createdDate");
            } else if (UPDATED_DATE.equals(code)) {
                return commonLocalizer.localize("updatedDate");
            } else if (EMPLOYEE.equals(code)) {
                return commonLocalizer.localize("employee");
            } else if (STATUS.equals(code)) {
                return commonLocalizer.localize("status");
            } else if (CANDIDAT.equals(code)) {
                return commonLocalizer.localize("candidate");
            } else if (EXPIRY_DATE.equals(code)) {
                return commonLocalizer.localize("expiryDate");
            } else if (PREV_APPROVER.equals(code)) {
                return commonLocalizer.localize("prevApprover");
            } else if (PREV_APPROVER_EMAIL.equals(code)) {
                return commonLocalizer.localize("prevApproverEmail");
            } else if (PREV_APPROVER_STATUS.equals(code)) {
                return commonLocalizer.localize("prevApproverStatus");
            } else if (CURRENT_APPROVER.equals(code)) {
                return commonLocalizer.localize("currentApprover");
            } else if (CURRENT_APPROVER_EMAIL.equals(code)) {
                return commonLocalizer.localize("currentApproverEmail");
            } else if (CURRENT_APPROVER_STATUS.equals(code)) {
                return commonLocalizer.localize("currentApproverStatus");
            } else if (NEXT_APPROVER.equals(code)) {
                return commonLocalizer.localize("nextApprover");
            } else if (NEXT_APPROVER_EMAIL.equals(code)) {
                return commonLocalizer.localize("nextApproverEmail");
            } else if (NEXT_APPROVER_STATUS.equals(code)) {
                return commonLocalizer.localize("nextApproverStatus");
            } else if (HRMS.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE.equals(code)) {
                return commonLocalizer.localize("certificateType");
            } else if (NUMBER.equals(code)) {
                return commonLocalizer.localize("number");
            }
        }
        return null;
    }

    public String localizeContact(String fieldID) {
        if (CONTACT_INFORMATION.equals(fieldID)) {
            return commonLocalizer.localize("contactInformation");
        } else if (LEAD_INFORMATION.equals(fieldID)) {
            return commonLocalizer.localize("basicDetails");
        } else if (ADDRESS_INFORMATION.equals(fieldID)) {
            return commonLocalizer.localize("addressInformation");
        } else if (ADDITIONAL_INFORMATION.equals(fieldID)) {
            return commonLocalizer.localize("additionalInformation");
        } else if (CRM_DETAILS.equals(fieldID)) {
            return commonLocalizer.localize("crmDetails");
        } else if (TITLE.equals(fieldID)) {
            return commonLocalizer.localize("title");
        } else if (MIDDLE_NAME.equals(fieldID)) {
            return commonLocalizer.localize("crmDetails");
        } else if (FIRST_NAME.equals(fieldID)) {
            return commonLocalizer.localize("firstName");
        } else if (OTHER_NAME.equals(fieldID)) {
            return commonLocalizer.localize("otherName");
        } else if (LAST_NAME.equals(fieldID)) {
            return commonLocalizer.localize("lastName");
        } else if (BIRTH_DAY.equals(fieldID)) {
            return commonLocalizer.localize("dateOfBirth");
        } else if (CRM_ACCOUNT_NAME.equals(fieldID)) {
            return commonLocalizer.localize("companyName");
        } else if (CRM_ACCOUNT_TYPE.equals(fieldID)) {
            return commonLocalizer.localize("accountType");
        } else if (JOB_TITLE.equals(fieldID)) {
            return commonLocalizer.localize("jobTitle");
        } else if (CRM_ACCOUNT_ORGANIZATION_TYPE.equals(fieldID)) {
            return commonLocalizer.localize("organizationType");
        } else if (DEPARTMENT.equals(fieldID)) {
            return commonLocalizer.localize("department");
        } else if (CRM_ACCOUNT_ANNUAL_REVENUE.equals(fieldID)) {
            return commonLocalizer.localize("annualRevenue");
        } else if (REF_IND_NUMBER.equals(fieldID)) {
            return "Ref Ind Number";
        } else if (ASSETS.equals(fieldID)) {
            return commonLocalizer.localize("assetsUnderManagement");
        } else if (CRM_ACCOUNT_INDUSTRY.equals(fieldID)) {
            return commonLocalizer.localize("industry");
        } else if (CRM_ACCOUNT_OWNERSHIP.equals(fieldID)) {
            return commonLocalizer.localize("ownership");
        } else if (CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(fieldID)) {
            return commonLocalizer.localize("numberOfEmployees");
        } else if (EMAIL.equals(fieldID)) {
            return commonLocalizer.localize("email");
        } else if (PHONE.equals(fieldID)) {
            return commonLocalizer.localize("phone");
        } else if (IM_ADDRESS.equals(fieldID)) {
            return commonLocalizer.localize("imAddress");
        } else if (WEB_ADDRESS.equals(fieldID)) {
            return commonLocalizer.localize("webAddress");
        } else if (LEAD_OWNER.equals(fieldID)) {
            return commonLocalizer.localize("leadOwner");
        } else if (LEAD_NAME.equals(fieldID)) {
            return commonLocalizer.localize("name");
        } else if (ADDRESS.equals(fieldID)) {
            return commonLocalizer.localize("primaryContactAddress");
        } else if (COPY_CRMACCOUNT_ADDRESS.equals(fieldID)) {
            return commonLocalizer.localize("accoundAddress");
        } else if (PARENT_ADDRESSES.equals(fieldID)) {
            return commonLocalizer.localize("parentAccountAddress");
        } else if (CATEGORY.equals(fieldID)) {
            return commonLocalizer.localize("category");
        } else if (RELATIONSHIP.equals(fieldID)) {
            return commonLocalizer.localize("relationship");
        } else if (REPORTS_TO.equals(fieldID)) {
            return commonLocalizer.localize("supervisor");
        } else if (OWNER.equals(fieldID)) {
            return commonLocalizer.localize("owner");
        } else if (CRM_CAMPAIGN_NAME.equals(fieldID)) {
            return commonLocalizer.localize("campaign");
        } else if (EMAIL_OPT_OUT.equals(fieldID)) {
            return commonLocalizer.localize("emailOptOut");
        } else if (SUBSCRIPTION_LIST.equals(fieldID)) {
            return commonLocalizer.localize("subscriptionLists");
        } else if (CRM_ACTIVITIES.equals(fieldID)) {
            return commonLocalizer.localize("latestOpenActivities");
        } else if (CRM_NOTE.equals(fieldID)) {
            return commonLocalizer.localize("notes");
        } else if (ATTACHMENTS.equals(fieldID)) {
            return commonLocalizer.localize("attachments");
        } else if (ATTACHMENTS_MINI.equals(fieldID)) {
            return commonLocalizer.localize("attachments");
        } else if (LINKS.equals(fieldID)) {
            return commonLocalizer.localize("links");
        } else if (LEAD_INFORMATION.equals(fieldID)) {//leads fields start
            return commonLocalizer.localize("basicDetails");
        } else if (LEAD_SOURCE.equals(fieldID)) {
            return commonLocalizer.localize("source");
        } else if (STATUS.equals(fieldID)) {
            return commonLocalizer.localize("status");
        } else if (ASSIGNEE.equals(fieldID)) {
            return commonLocalizer.localize("assignee");
        } else if (BACKUP_ASSIGNEE.equals(fieldID)) {
            return commonLocalizer.localize("backupAssignee");
        } else if (RATING.equals(fieldID)) {
            return commonLocalizer.localize("rating");
        } else if (NUMBER.equals(fieldID)) {//candidate start
            return commonLocalizer.localize("number");
        } else if (CREATED_DATE.equals(fieldID)) {
            return commonLocalizer.localize("createdDate");
        } else if (WORK_EXPERIENCE.equals(fieldID)) {
            return commonLocalizer.localize("workExperience");
        } else if (CURRENT_EMPLOYER.equals(fieldID)) {
            return commonLocalizer.localize("currentEmployer");
        } else if (EXPECTED_SALARY.equals(fieldID)) {
            return commonLocalizer.localize("expectedSalary");
        } else if (SKILLS.equals(fieldID)) {
            return commonLocalizer.localize("skills");
        } else if (STATUS_HISTORY.equals(fieldID)) {
            return commonLocalizer.localize("statusHistory");
        } else if (CustomFormConstants.CANDIDATE.LOCATION.equals(fieldID)) {
            return commonLocalizer.localize("location");
        } else if (VACANCIES.equals(fieldID)) {
            return commonLocalizer.localize("matchedVacancies");
        } else if (CustomFormConstants.CANDIDATE.OTHER_INFORMATION.equals(fieldID)) {
            return commonLocalizer.localize("otherInformation");
        } else if (IMAGE_UPLOAD.equals(fieldID)) {
            return commonLocalizer.localize("uploadImage");
        } else if (STREET_ADDRESS1.equals(fieldID)) {
            return commonLocalizer.localize("streetAddress1");
        } else if (STREET_ADDRESS2.equals(fieldID)) {
            return commonLocalizer.localize("streetAddress2");
        } else if (CITY.equals(fieldID)) {
            return commonLocalizer.localize("city");
        } else if (STATE.equals(fieldID)) {
            return commonLocalizer.localize("state");
        } else if (COUNTRY.equals(fieldID)) {
            return commonLocalizer.localize("country");
        } else if (POST_CODE.equals(fieldID)) {
            return commonLocalizer.localize("postCode");
        } else if (CANDIDATE.CANDIDATE_PROJECT.equals(fieldID)) {
            return commonLocalizer.localize("project");
        } else if (CANDIDATE.ALLOWANCE_INFORMATION.equals(fieldID)) {
            return commonLocalizer.localize("allowanceInformation");
        }
        return null;
    }

    public String localizeOpportunity(String fieldID) {
        if (CRM_NOTE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("notes");
        } else if (OPPORTUNITY_INFORMATION.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("basicDetails");
        } else if (CRM_OPPORTUNITY_ASSIGNEE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("assignee");
        } else if (CRM_OPPORTUNITY_BACKUP_ASSIGNEE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("backupAssignee");
        } else if (CRM_OPPORTUNITY_NUMBER.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("number");
        } else if (CRM_OPPORTUNITY_NAME.equalsIgnoreCase(fieldID)) {
            return crmLocalizer.localize("name");
        } else if (CRM_OPPORTUNITY_ACCOUNT_NAME.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("accountName");
        } else if (CRM_OPPORTUNITY_CONTACT_NAME.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("contactName");
        } else if (CRM_OPPORTUNITY_TYPE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("type");
        } else if (CRM_OPPORTUNITY_NEXT_STEP.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("nextStep");
        } else if (CRM_OPPORTUNITY_AMOUNT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("amount");
        } else if (CURRENCY.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("currency");
        } else if (CRM_OPPORTUNITY_CLOSING_DATE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("closeDate");
        } else if (CRM_OPPORTUNITY_STAGE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("stage");
        } else if (CRM_OPPORTUNITY_PROBABILITY.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("probability");
        } else if (CRM_OPPORTUNITY_EXPECTED_REVENUE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("expectedRevenue");
        } else if (CRM_OPPORTUNITY_CAMPAIGN_SOURCE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("campaignSource");
        } else if (CRM_OPPORTUNITY_LEAD_SOURCE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("leadSource");
        } else if (CRM_ESTIMETOR.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("estimator");
        } else if (CRM_OPPORTUNITY_ATTACHMENTS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("attachments");
        } else if (GET_PRODUCT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("getProduct");
        } else if (CRM_OPPORTUNITY_INCLUDE_ITEMS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("productsOrServices");
        } else if (ADDITIONAL_INFORMATION.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("additionalInformation");
        } else if (CRM_OPPORTUNITY_LINKS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("links");
        } else if (CRM_ACTIVITIES.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("latestOpenActivities");
        } else if (CRM_OPPORTUNITY_STAGE_HISTORY.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("stageHistory");
        } else if (CRM_OPPORTUNITY_CONTACT_PRIMARY_EMAIL.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("email");
        } else if (CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("phone");
        }
        return null;
    }

    public String localizeCrmAccount(String fieldID) {
        if (CRM_ACCOUNT_INFORMATION.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("basicInfo");
        } else if (CRM_ACCOUNT_ADDRESS_INFORMATION.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("addressInformation");
        } else if (CRM_ACTIVITIES.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("activities");
        } else if (CRM_ACCOUNT_LATEST_CONTACTS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("latestContacts");
        } else if (ATTACHMENTS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("attachments");
        } else if (CRM_ACCOUNT_FINANCIAL_INFORMATION.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("financialInformation");
        } else if (CRM_NOTE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("notes");
        } else if (CRM_ACCOUNT_OWNER.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("accountEmployee");
        } else if (CRM_ACCOUNT_NAME.equalsIgnoreCase(fieldID) || SUPPLIER_ACCOUNT_NAME.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("accountName");
        } else if (CRM_ACCOUNT_PARENT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("parentaccount");
        } else if (CRM_ACCOUNT_NUMBER.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("accountNumber");
        } else if (PRIMARY_CONTACT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("primaryContact");
        } else if (CRM_ACCOUNT_TYPE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("accountType");
        } else if (CLIENT_TYPE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("clientType");
        } else if (CRM_ACCOUNT_INDUSTRY.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("industry");
        } else if (CRM_ACCOUNT_OWNERSHIP.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("ownership");
        } else if (CRM_ACCOUNT_EMAIL.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("email");
        } else if (CRM_ACCOUNT_PHONE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("phone");
        } else if (CRM_ACCOUNT_WEBSITE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("website");
        } else if (CRM_ACCOUNT_FAX.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("fax");
        } else if (CRM_ACCOUNT_ORGANIZATION_TYPE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("organizationType");
        } else if (CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("numberOfEmployees");
        } else if (CRM_ACCOUNT_ANNUAL_REVENUE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("annualRevenue");
        } else if (CRM_ACCOUNT_RATING.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("rating");
        } else if (CRM_ACCOUNT_BILLING_ADDRESS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("billingAddress");
        } else if (CRM_ACCOUNT_SHIPPING_ADDRESS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("shippingAddress");
        } else if (CURRENCY.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("currency");
        } else if (VAT_NUMBER.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("vatNumber");//Utils.ConvertNiffToRucc(wfmStrings.vatNumber());
        } else if (PAYMENT_METHOD.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("paymentMethod");
        } else if (CRM_ACCOUNT_PAYMENT_TYPE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("paymentType");
        } else if (PAY_FOR_NO_SHOWS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("payForNoShows");
        } else if (REGISTRATION_NUMBER.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("registrationNumber");
        } else if (CLIENT_AS_OF_DATE.equalsIgnoreCase(fieldID) || SUPPLIER_AS_OF_DATE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("asOfDate");
        } else if (CLIENT_AMOUNT.equalsIgnoreCase(fieldID) || SUPPLIER_AMOUNT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("openingBalance");
        } else if (CLIENT_SUBSIDIARIES.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("subsidiary");
            //return (Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE) && Utils.isMultiCompanySubsidiary()) ? wfmStrings.headOffice() : wfmStrings.subsidiary()/*wfmStrings.subsidiary()*/;
        } else if (CLIENT_CREDIT_LIMIT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("creditLimit");
        } else if (CLIENT_QUOTE_CREDIT_LIMIT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("quoteCreditLimit");
        } else if (CLIENT_INVOICE_TERM.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("terms");
        } else if (SUPPLIER_BANK_ACCOUNT_DETAILS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("bankAccountDetails");
        } else if (SUPPLIER_BANK_NAME.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("bankName");
        } else if (SUPPLIER_ACCOUNT_NUMBER.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("accountNo");
        } else if (SUPPLIER_SWIFT_CODE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("swiftCode");
        } else if (SUPPLIER_SORT_CODE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("sortCode");
        } else if (SUPPLIER_IBAN_CODE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("ibanCode");
        } else if (SUPPLIER_BRANCH.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("branch");
        } else if (SUPPLIER_BANK_ADDRESS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("bankAddress");
        } else if (ADDITIONAL_INFORMATION.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("additionalInformation");
        } else if (CLIENT_VAT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("tax");
        } else if (CLIENT_DISCOUNT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("discount");
        } else if (GL_ACCOUNT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("createGLAccount");//wfmStrings.createGLAccount();
        } else if (PRICE_LEVEL.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("priceLevel");
        } else if (CRM_ACCOUNT_INFORMATION.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("basicInfo");
        } else if (CRM_ACCOUNT_LATEST_CONTACTS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("latestContacts");
        } else if (CRM_ACCOUNT_FINANCIAL_INFORMATION.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("financialInformation");
        } else if (CLIENT_BANK_ACCOUNT.equalsIgnoreCase(fieldID)) {
            return "Bank Accounts";
        } else if (VAT_CATEGORIES.equalsIgnoreCase(fieldID)) {
            return "Tax Exemption Reason";
        } else if (SUPPLIER_VAT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("tax");
        } else if (ACCOUNTS_RECEIVABLE_PAYABLE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("accountsPayable");
        } else if (PRIMARY_CONTACT_ADDRESSES.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("primaryContactAddress");
        }
        return null;
    }

    private String localizeEvent(String fieldID) {
        if (SUBJECT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("subject");
        } else if (START_DATE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("startDate");
        } else if (END_DATE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("endDate");
        } else if (DESCRIPTION.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("description");
        } else if (PROJECT.LOCATION.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("location");
        } else if (CREATOR.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("creator");
        }
        return null;
    }

    public String localizeTask(String fieldID) {
        if (TASK.PROJECT.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("projectField");
        } else if (NAME.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("taskName");
        } else if (NUMBER.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("taskNumber");
        } else if (TASK.PROJECT_CLIENTS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("client");
        } else if (TASK.TASK_DETAILS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("taskDetails");
        } else if (DESCRIPTION.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("taskDescription");
        } else if (START_DATE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("startDate");
        } else if (DUE_DATE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("dueDateField");
        } else if (WORKFLOW_DATE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("date");
        } else if (WORKFLOW_TIME_BASED.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("executionTime");
        } else if (ASSIGNEE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("assignees");
        } else if (RECALCULATE_HOURS_ON_RESOURCE_UTIL.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("recalculateHoursOnResourceUtilizationTool");
        } else if (PRIORITY.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("priority");
        } else if (TASK.BILLIBLE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("billable");
        } else if (TASK.PARENT_WORKSTREAM.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("parentWorkstream");
        } else if (TASK.DUE_DATE_REMINDER.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("duedatereminder");
        } else if (TASK.PREDECESSOR_TASK.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("predeccessorTasks");
        } else if (TASK.SUCCESSOR_TASK.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("successorTasks");
        } else if (ATTACHMENTS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("attachments");
        } else if (STATUS.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("status");
        } else if (TASK.TASK_NOTE.equalsIgnoreCase(fieldID)) {
            return commonLocalizer.localize("notes");
        }
        return null;
    }
}
