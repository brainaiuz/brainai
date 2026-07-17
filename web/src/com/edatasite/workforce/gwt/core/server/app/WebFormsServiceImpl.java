package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsLayout;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.crm.contact.WebFormInterface;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.domain.webforms.EdsWebField;
import com.edatasite.workforce.core.domain.webforms.EdsWebForm;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryList;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CaptchaManager;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.LayoutManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.CrmCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.rbac.email.UserEmailRbacManager;
import com.edatasite.workforce.gwt.core.server.db.webforms.WebFormManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.messagecenter.server.MessageCenterServiceLocal;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.WebFormItem;
import com.edatasite.workforce.gwt.webforms.client.WebFormsService;
import com.edatasite.workforce.gwt.webforms.client.forms.CandidateField;
import com.edatasite.workforce.gwt.webforms.client.forms.CaseField;
import com.edatasite.workforce.gwt.webforms.client.forms.LeadField;
import com.edatasite.workforce.gwt.webforms.client.forms.WebField;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 29, 2010
 * Time: 7:46:58 PM
 * To change this template use File | Settings | File Templates.
 */

@Transactional
@Service("webFormsService")
public class WebFormsServiceImpl implements WebFormsService, Constants {

    @Autowired
    private CaptchaManager captchaManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private WebFormManager webFormManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CRMService crmService;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    @Qualifier("emailTemplateService")
    private EmailTemplateServiceLocal emailTemplateServiceLocal;
    @Autowired
    private CompanyCustomFieldsManager companyCFSettingsManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private CrmCustomFieldsManager crmCustomFieldsManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ClientService clientService;
    @Autowired
    private LayoutManager layoutManager;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private MassMailService massMailService;
    @Autowired
    private MessageCenterServiceLocal messageCenterServiceLocal;
    @Autowired
    private UserEmailRbacManager userEmailRbacManager;
    @Autowired
    private EmailSettingsManager emailSettingsManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WebForm getWebForm(Integer objectID, Integer companyID) {
        WebForm webForm = null;
        EdsWebForm edsWebForm = null;
        if (companyID != null && objectID != null) {
            SecurityContext.getInstance().setCompanyId(companyID);
            edsWebForm = webFormManager.get(objectID);
        }
        if (edsWebForm != null) {
            if (edsWebForm.getOwner() != null) {
                SecurityContext.getInstance().setStaticUserID(edsWebForm.getOwner().getObjectID());
            }
            webForm = fillDropDowns(edsWebForm.getRPC(false));
            if (edsWebForm.getLayoutID() != null) {
                EdsLayout layout = layoutManager.get(edsWebForm.isCustomLayout() ? SecurityContext.getCompanyID() : null, edsWebForm.getLayoutID());
                if (layout != null) {
                    webForm.setCustomForm(layout.getRPC());
                }
            }

            if (SecurityContext.getInstance().getDatabase() != null && SecurityContext.getCompanyID() != null) {
                webForm.userSettings.put(COMPANY_ID, SecurityContext.getCompanyID().toString());
                if (edsWebForm.getOwner() != null) {
                    webForm.userSettings.put(USER_ID, edsWebForm.getOwner().getObjectID().toString());
                }
                webForm.userSettings.put(SESSION_ID, SecurityContext.getInstance().getDatabase() + "$" + SecurityContext.getCompanyID() + "$" + (webForm.userSettings.getOrDefault(USER_ID, "")));
                System.out.println(webForm.userSettings);
                System.out.println(webForm.userSettings.get(SESSION_ID));
            }
            SecurityContext.getInstance().removeCompanyId();
            return webForm;
        } else {
            SecurityContext.getInstance().removeCompanyId();
            return webForm;
        }
    }

    @Override
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public WebForm decryptLink(String url) {
        WebForm webForm = null;
        try {
            url = url.substring(url.indexOf("link=") + 5);
            Map<String, Integer> info = decrypt(url);
            if (info == null) {
                return webForm;
            }
            SecurityContext.setCompanyID(info.get("companyID"));
            GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager = ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class);
            SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(SecurityContext.getCompanyID()));
            return getWebForm(info.get("objectID"), info.get("companyID"));
        } catch (Exception e) {
            e.printStackTrace();
            return webForm;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Map<String, Integer> decrypt(String url) {
        String decrypted = EncryptionHelper.decrypt(url, WebFormConstants.WEB_FORM);
        if (decrypted.contains("#")) {
            String[] slices = decrypted.split("#");
            Map<String, Integer> map = new HashMap<>();
            map.put("objectID", Integer.valueOf(slices[0]));
            map.put("companyID", Integer.valueOf(slices[1]));
            return map;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<String, SelectItem[]> fillDropDowns(Integer companyID, String form) {
        return crmService.fillDropDowns(form);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WebForm fillDropDowns(final WebForm webForm) {
        Map<String, SelectItem[]> dropDowns = fillDropDowns(webForm.getCompanyID(), webForm.getWebFormType(true));
        if (webForm.getWebFields() != null) {
            for (WebField webField : webForm.getWebFields()) {
                Integer savingField = webField.getSavingField();
                boolean isShownInForm = webField.isShowInForm();
                if (isShownInForm) {
                    if (((!webField.isCustomField() && (savingField.equals(LeadField.FIELD_ASSIGNEE) || savingField.equals(LeadField.FIELD_BACKUP_ASSIGNEE))) || (webForm.getWebFormType(true).equals(WebFormConstants.CASE_FORM) && (savingField.equals(CaseField.FIELD_ASSIGNEE) || savingField.equals(CaseField.FIELD_RESOLVER))))) {
                        webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_ASSIGNEES));
                    } else if (WebFormConstants.LEAD_FORM.equals(webForm.getWebFormType(true))) {
                        if (!webField.isCustomField()) {
                            if (savingField.equals(LeadField.FIELD_COUNTRY)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_COUNTIRES));
                                continue;
                            }
                            if (savingField.equals(LeadField.FIELD_STATE)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_STATES));
                                continue;
                            }
                            if (savingField.equals(LeadField.FIELD_LEADSOURCE)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_SOURCES));
                                continue;
                            }
                            if (savingField.equals(LeadField.FIELD_CAMPAIGNSOURCE)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_CAMPAIGNS));
                                continue;
                            }
                            if (savingField.equals(LeadField.FIELD_LEADSTATUS)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_STATUSES));
                                continue;
                            }
                            if (savingField.equals(LeadField.FIELD_INDUSTRY)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_INDUSTRIES));
                                continue;
                            }
                            if (savingField.equals(LeadField.FIELD_RATING)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_RATINGS));
                            }
                        } else {
                            EdsCompanyCustomFieldsSettings customFieldSetting = companyCFSettingsManager.get(webField.getSavingField());
                            if (customFieldSetting != null) {
                                webField.setValues(customFieldSetting.getPredefinedValuesWithSorting());
                            }
                        }
                    } else if (WebFormConstants.CASE_FORM.equals(webForm.getWebFormType(true))) {
                        if (!webField.isCustomField()) {
                            if (savingField.equals(CaseField.FIELD_CASE_ORIGIN)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_CASEORIGINS));
                                continue;
                            }
                            if (savingField.equals(CaseField.FIELD_STATUS)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_STATUSES));
                                continue;
                            }
                            if (savingField.equals(CaseField.FIELD_TYPE)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_TYPES));
                                continue;
                            }
                            if (savingField.equals(CaseField.FIELD_PRIORITY)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_PRIORITIES));
                                continue;
                            }
                            if (savingField.equals(CaseField.FIELD_CASE_REASON)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_CASEREASONS));
                                continue;
                            }
                            if (savingField.equals(CaseField.FIELD_RESOLVER)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_RESOLVERS));
                            }
                        } else {
                            EdsCompanyCustomFieldsSettings customFieldsSettings = companyCFSettingsManager.get(webField.getSavingField());
                            if (customFieldsSettings != null) {
                                webField.setValues(customFieldsSettings.getPredefinedValuesWithSorting());
                            }
                        }
                    } else if (WebFormConstants.CANDIDATE_FORM.equals(webForm.getWebFormType(true))) {
                        if (!webField.isCustomField()) {
                            if (savingField.equals(CandidateField.FIELD_OWNER)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_OWNERS));
                                continue;
                            }
                            if (savingField.equals(CandidateField.FIELD_SOURCE)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_SOURCES));
                                continue;
                            }
                            if (savingField.equals(CandidateField.FIELD_STATUS)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_STATUSES));
                                continue;
                            }
                            if (savingField.equals(CandidateField.FIELD_MATCHED_VACANCIES)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_VACANCIES));
                                continue;
                            }
                            if (savingField.equals(CandidateField.FIELD_PREFERRED_LOCATION)) {
                                webField.setValues(dropDowns.get(WebFormConstants.DROPDOWNITEMS_LOCATIONS));
                            }
                        } else {
                            EdsCompanyCustomFieldsSettings customFieldsSettings = companyCFSettingsManager.get(webField.getSavingField());
                            if (customFieldsSettings != null) {
                                webField.setValues(customFieldsSettings.getPredefinedValuesWithSorting());
                            }
                        }
                    }
                }
            }
        }
        return webForm;
    }

    @Transactional
    public HashMap<String, String> saveForm(WebFormItem item) {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(item.getAntibot());
        System.out.println("-------------------------------------------------------------------------------");

        if (item != null && item.getCompanyID() != null) {
            if (globalAuthJdbcSpringManager == null) {
                globalAuthJdbcSpringManager = ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class);
            }

            SecurityContext.setCompanyID(item.getCompanyID());
            SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(item.getCompanyID()));
            System.out.println(SecurityContext.getInstance().getCompanyId());
            System.out.println(SecurityContext.getInstance().getDatabase());
        }

        if (item.getAntibot() == null || captchaManager.validateCaptcha(item.getAntibot())) {
            HashMap<String, String> returning = null;
            SecurityContext.getInstance().setCompanyId(item.getCompanyID());
            if (item.getWebformID() != null) {
                EdsWebForm webForm = webFormManager.get(item.getWebformID());
                if (webForm != null && webForm.getOwner() != null) {
                    SecurityContext.getInstance().setStaticUserID(webForm.getOwner().getObjectID());
                }
            }

            if (WebFormConstants.LEAD_FORM.equals(item.getWebformType())) {
                try {
                    boolean enableAccess = item.isEnableAccess();
                    boolean fromSubscriptionForm = item.isFromSubscriptionForm();
                    item.getContactListItem().setAntibot(NO_CAPTCHA_USED);
                    returning = saveLeadForm(item.getContactListItem(), enableAccess, fromSubscriptionForm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (WebFormConstants.CASE_FORM.equals(item.getWebformType())) {
                CaseItem caseItem = item.getCaseItem();
                caseItem.setAddingFromWebForms(true);
                returning = saveCaseForm(caseItem);
            } else if (WebFormConstants.CANDIDATE_FORM.equals(item.getWebformType())) {
                ContactListItem candidateListItem = item.getContactListItem();
                candidateListItem.setAddingFromWebForms(true);
                candidateListItem.setAntibot(NO_CAPTCHA_USED);
                Integer candidateID = contactService.saveCandidate(candidateListItem);
                HashMap<String, String> map = new HashMap<>();
                map.put("ID", candidateID.toString());
                returning = map;
            }
            if (returning != null && returning.size() > 0 && returning.containsKey("ID")) {
                try {
                    Integer.valueOf(returning.get("ID"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    returning.remove("ID");
                }
                if (returning.size() == 0) {
                    returning = null;
                }
            }
            return returning != null && returning.size() > 0 ? returning : null;
        } else {
            final HashMap<String, String> returning = new HashMap<>();
            returning.put("ERROR_CAPTCHA", "The characters that you entered didn't match the word verification. Please try again!");
            return returning;
        }
    }

    private HashMap<String, String> saveCaseForm(CaseItem item) {
        Integer objectID = crmService.saveCase(item, true).getId();
        HashMap<String, String> map = new HashMap<>();
        map.put("ID", objectID.toString());
        return map;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<CompanyCustomFieldItem> getCustomFields(ViewName viewName) {
        return commonService.getCompanyCustomFields(viewName);
    }

    @Override
    public void sendEmailNotifications(Integer webFormID, Integer entityID) {
        WebForm webForm = getWebForm(webFormID, SecurityContext.getCompanyID());
        if (webForm != null && webForm.getCustomForm() != null) {
            EdsWebForm edsWebForm = webFormManager.get(webFormID);
            EdsUser user = userManager.getUser();
            user = user == null ? edsWebForm.getOwner() : null;
            Set<String> idsOfFields = LayoutRPC.getFieldIDs(webForm.getCustomForm().getLayout());
            if (entityID != null && user != null && webForm.getEmailAddress() != null && !"".equals(webForm.getEmailAddress()) && (webForm.getEmailAddress().matches(Constants.REGEX_EMAIL_SERVERSIDEONLY) || webForm.getEmailAddress().contains(","))) {
                if (entityID != null) {
                    SecurityContext.setCompanyID(webForm.getCompanyID());
                    SecurityContext.getInstance().setStaticUserID(user.getObjectID());
                    sendEmailToOwner(edsWebForm, entityID, user, idsOfFields);
                }
            }
            //send thank you email
            if (entityID != null && !"".equals(entityID) && webForm.getEmailTemplateID() != null && user != null) {
                sendThankYouEmail(entityID, webForm.getEmailTemplateID(), edsWebForm.getType(), user);
            }
        }
    }

    private HashMap<String, String> saveLeadForm(ContactListItem item, boolean enableAccess, boolean fromSubscriptionForm) {
        EdsWebForm webForm = webFormManager.get(item.getWebFormID());
        List<String> emails = item.getHomeEmail();
        if (item.getPrimaryEmail() != null && !"".equals(item.getPrimaryEmail())) {
            emails.add(item.getPrimaryEmail());
        }
        Map<String, String[]> existence = checkEmailExistenceForLead(webForm.getCompany().getObjectID(), null, emails.toArray(new String[]{}));
        HashMap<String, String> map = new HashMap<>();
        if (existence != null && existence.size() > 0) {
            map.put("ERROR", "Lead with one of these emails exist");
            return map;
        }
        item.setOwnerId(webForm.getOwner().getObjectID());
        item.setAddingFromWebForms(true);
        Integer objectID = crmService.saveLead(item, item.getSubscriptionIDs());
        item.setObjectId(objectID);
        if (enableAccess) {
            EdsReference reference = referenceManager.findReference(EdsOpportunity._OPPORTUNITY_STAGE, EdsOpportunity.CLOSED_WON);
            reference = reference == null ? referenceManager.findReferenceByDescription(EdsOpportunity._OPPORTUNITY_STAGE, "100") : reference;
            Integer stageID = null;
            if (reference != null) {
                stageID = reference.getObjectID();
            }
            OpportunityListItem opportunityItem = new OpportunityListItem();
            opportunityItem.setOpportunityName(item.getName());
            opportunityItem.setStageId(stageID);
            opportunityItem.setAssigneeId(item.getLeadAssigneeID());
            opportunityItem.setClosingDate(new Date());
            opportunityItem.setCopyLeadDetails(false);
            crmService.convertLead(opportunityItem, objectID);
            clientService.enableAccess(objectID, fromSubscriptionForm);
        }
        map.put("ID", objectID.toString());
        return map;
    }

    private void sendEmailToOwner(EdsWebForm webForm, Integer objectID, EdsUser user, Set<String> idsOfFields) {
        StringBuffer message = new StringBuffer();
        EdsReference type = webForm.getType();
        boolean isCustomFormUsed = webForm.getLayoutID() != null;
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        if (objectID != null && !"".equals(objectID)) {

            WebFormInterface entity = null;
            boolean isCandidate = WebFormConstants.CANDIDATE_FORM.equals(type.getCode());
            boolean isLead = WebFormConstants.LEAD_FORM.equals(type.getCode());
            boolean isCase = WebFormConstants.CASE_FORM.equals(type.getCode());
            if (isLead || isCandidate) {
                entity = crmContactManager.get(objectID);
            } else if (isCase) {
                entity = caseManager.get(objectID);
            }
            if (entity != null) {
                ListingFilterParameter fp = new ListingFilterParameter();
                if (isLead) {
                    fp.setLeadID(objectID);
                }
                fp.setRelationID(objectID);
                fp.setRelationType(isLead ? RelationItem.TYPE_LEAD : isCase ? RelationItem.TYPE_CASE : RelationItem.TYPE_CANDIDATE);
                HistoryList note = crmServiceLocal.getCrmNoteHistory(fp);
                if (note != null && note.getResult() != null && note.getResult().length > 0 && note.getResult()[0] != null) {
                    entity.setNote(note.getResult()[0].getComment());
                }
            }

            if (isCustomFormUsed) {
                fillMessageForCustomForms(entity, user, idsOfFields, message, objectID, type.getCode(), format);
            } else {
                fillMessageForWebForm(entity, user, message, webForm, objectID, type.getCode(), format);
            }
            try {
                if (webForm != null && message != null) {
                    String header = "New data received from " + webForm.getType().getName() + " titled as \"" + webForm.getTitle() + "\".<br/>";
                    header += "Received Data:<br/>";
                    header += message.toString();
                    String subject = "New Data Received from " + webForm.getType().getName() + (webForm.getTitle() != null ? " - " + webForm.getTitle() : "");
                    messageManager.registerInternalMessageBasic(webForm.getEmailAddress(), subject, header, webForm.getCompany().getObjectID());
                }
            } catch (EdsDbException ignored) {

            }
        }
    }

    private void fillMessageForCustomForms(WebFormInterface entity, EdsUser user, Set<String> idsOfFields, StringBuffer message, Integer objectID, String type, SimpleDateFormat format) {
        boolean isCandidate = WebFormConstants.CANDIDATE_FORM.equals(type);
        boolean isLead = WebFormConstants.LEAD_FORM.equals(type);
        boolean isCase = WebFormConstants.CASE_FORM.equals(type);
        if (idsOfFields != null && idsOfFields.size() > 0) {
            for (String fieldID : idsOfFields) {
                if (fieldID != null) {
                    System.out.println(fieldID);
                    if (!(fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value") || fieldID.toLowerCase().contains("attachment"))) {
                        String label = entity.getFieldLabelByCode(fieldID);
                        String value = entity.getFieldValueByCode(fieldID);
                        value = value == null ? "N/A" : value;
                        if (label != null && !"".equals(label)) {
                            message.append(label).append(":").append(value).append("<br/>");
                        }
                    } else {
                        if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
                            FileResource[] files = crmService.getCrmAttachments(objectID, isCandidate ? CrmConstants.CANDIDATE : isLead ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CASE);
                            message.append("<p>").append("Attachments:");
                            if (files != null && files.length > 0) {
                                String delimtr = "";
                                for (FileResource file : files) {
                                    if (file != null && file.getEncodedName() != null) {
                                        message.append(delimtr).append(" ").append(file.getEncodedName());
                                        delimtr = ",";
                                    }
                                }
                            } else {
                                message.append("No Files");
                            }
                        } else {
                            String value = null;
                            EdsCompanyCustomFieldsSettings customFieldSetting = companyCFSettingsManager.getByColumnCode(isCandidate ? ViewName.Candidate.name() : isLead ? ViewName.Lead.name() : ViewName.CrmCase.name(), fieldID);
                            if (customFieldSetting != null) {
                                EdsCrmCustomFields customField = entity.getCustomFields();
                                if (customField != null) {
                                    value = customField.getValueAsString(customFieldSetting.getDataType(), customFieldSetting.getColumnCode(), user.getUserTimezone(), format);
                                }
                                if (value == null || "".equals(value)) {
                                    value = "N/A";
                                }
                                message.append("<p>").append(customFieldSetting.getFieldName()).append(" : ").append(value).append("</p>");
                            }
                        }
                    }
                }
            }
        }
    }

    private void fillMessageForWebForm(WebFormInterface entity, EdsUser user, StringBuffer message, EdsWebForm webForm, Integer objectID, String type, SimpleDateFormat format) {
        boolean isCandidate = WebFormConstants.CANDIDATE_FORM.equals(type);
        boolean isLead = WebFormConstants.LEAD_FORM.equals(type);
        boolean isCase = WebFormConstants.CASE_FORM.equals(type);
        for (EdsWebField webField : webForm.getFields()) {
            if (webField.getShowInForm() != null && webField.getShowInForm()) {
                if (webField.getCustomField() == null || !webField.getCustomField()) {
                    if (webField.getSavingField() != null) {
                        if (webField.getType().equals(WebFormConstants.INPUT_ATTACHMENT)) {
                            FileResource[] files = crmService.getCrmAttachments(objectID, isLead ? "lead" : isCase ? RelationItem.TYPE_CASE : RelationItem.TYPE_CANDIDATE);
                            message.append("<p>").append(webField.getLabel()).append(":");
                            if (files != null && files.length > 0) {
                                String delimtr = "";
                                for (FileResource file : files) {
                                    if (file != null && file.getEncodedName() != null) {
                                        message.append(delimtr).append(" ").append(file.getEncodedName());
                                        delimtr = ",";
                                    }
                                }
                            } else {
                                message.append("No Files");
                            }
                        } else if (!isCase && webField.getType().equals(WebFormConstants.INPUT_MAILING_LIST)) {
                            SelectItem[] list = massMailService.getMailListByCrmEntityID(objectID);
                            final StringBuilder s = new StringBuilder();
                            if (list != null) {
                                for (SelectItem item : list) {
                                    if (item.isSelected()) {
                                        s.append(item.getName() + ",<br>");
                                    }
                                }
                            }
                            message.append("<p>").append(webField.getLabel()).append(" : ").append("".contentEquals(s) ? "N/A" : s);
                        } else {
                            message.append("<p>").append(webField.getLabel()).append(" : ").append(returnNAifNull(entity.getFieldAsString(webField.getSavingField()))).append("</p>");
                        }
                    }
                } else {
                    String value = null;
                    EdsCompanyCustomFieldsSettings customFieldSetting = companyCFSettingsManager.get(webField.getSavingField());
                    EdsCrmCustomFields customField = crmCustomFieldsManager.getCustomFieldByLead(objectID);
                    if (customField != null) {
                        value = customField.getValueAsString(customFieldSetting.getDataType(), customFieldSetting.getColumnCode(), user.getUserTimezone(), format);
                    }
                    if (value == null || "".equals(value)) {
                        value = "N/A";
                    }
                    message.append("<p>").append(webField.getLabel()).append(" : ").append(value).append("</p>");
                }
            }
        }
    }

    private void sendAutoResponseToCase(CaseItem crmCase, EdsEmailSetting emailSettings) {
        crmServiceLocal.sendAutoResponseToCase(crmCase != null ? crmCase.getObjectId() : null, emailSettings != null ? emailSettings.getObjectID() : null, null);
    }

    private void sendThankYouEmail(Integer objectID, Integer emailTemplateID, EdsReference type, EdsUser user) {
        String toEmail = null;
        RelationItem relation = null;
        if (WebFormConstants.LEAD_FORM.equals(type.getCode())) {
            EdsCrmContact edsLead = crmContactManager.get(objectID);
            if (edsLead != null) {
                toEmail = edsLead.getPrimaryEmail();
                relation = new RelationItem(null, edsLead.getObjectID(), RelationItem.TYPE_LEAD, edsLead.getName(), null, null, null);
            }
        } else if (WebFormConstants.CASE_FORM.equals(type.getCode())) {
            EdsCase edsCase = caseManager.get(objectID);
            if (edsCase != null) {
                toEmail = edsCase.getEmail();
                relation = new RelationItem(null, edsCase.getObjectID(), RelationItem.TYPE_CASE, edsCase.getName(), null, null, null);
            }
        }
        //send email
        if (toEmail != null && !"".equals(toEmail) && (toEmail.matches(Constants.REGEX_EMAIL_SERVERSIDEONLY) || toEmail.contains(","))) {
            EmailTemplateItem emailTemplateItem = emailTemplateServiceLocal.generateWebFormThankYouEmailTemplateItem(emailTemplateID, toEmail, user);
            if (emailTemplateItem != null) {
                user = emailTemplateItem.getFromUserID() != null && emailTemplateItem.getFromUserID() != -1 ? userManager.get(emailTemplateItem.getFromUserID()) : (user != null ? user : userManager.getUser());
                EdsEmailSetting settings = emailSettingsManager.getCompanyEmailSetting(null);
                settings = settings == null ? emailSettingsManager.getUserDefaultEmailAccount() : settings;
                Email email = new Email(toEmail, emailTemplateItem.getSubject(), emailTemplateItem.getMessageHTML());
                email.setFromEmail(settings != null ? settings.getEmail() : user.getEmail());
                email.setFromName(user.getFullName());
                email.addRelations(relation);
                messageCenterServiceLocal.sendMessage(email);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Map<String, String[]> checkEmailExistenceForLead(Integer companyID, Integer leadID, String[] email) {
        return crmServiceLocal.checkEmailExistenceInternally(leadID, email);
    }

    private String returnNAifNull(String value) {
        return value == null || "".equals(value) ? "N/A" : value;
    }

}
