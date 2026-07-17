package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Abdulaziz
 * Date: Jan 5, 2011
 * Time: 2:17:41 PM
 */
@Transactional
public class CompanyRegistrationCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsCompany> TYPE = new WfmType<>(EventTypes.companyRegistrationCustomEventListener);
    public static String EVENT_REGISTRATION = "REGISTER_COMPANY";
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CRMService crmService;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_REGISTRATION.equals(event.getEventType())) {
            onCreation(event);
        }
    }

    public void onCreation(EdsBusinessEvent event) {
        String host = EdsContextParams.getHostname();
        if (!host.contains("kpi") && EdsContextParams.getLeadSignUpCompany() == null) {
            event.setStatus(EventStatus.COMPLETED.name());
            return;
        }
        EdsEmployee wfmDR = employeeManager.get(event.getSourceID());
        NewCompany company = NewCompany.fromString(event.getCustomStringField());
        if (company != null) {
            ContactListItem lead = new ContactListItem();
            lead.getCrmAccount().setName(company.getName());
            lead.getCrmAccount().setCompanyId(company.getCompanyId());

            EdsReference pending = referenceManager.findReference(EdsCrmContact._LEAD_RATING, "LRT_ACTIVE");
            if (pending != null) {
                lead.setLeadRatingID(pending.getObjectID());
                System.out.println("Lead onCreation LRT_ACTIVE = " + pending.getName());
            } else {
                System.out.println("Lead onCreation: LRT_ACTIVE is null");
            }

            lead.getHomeEmail().add(company.getAdminEmail());
            lead.setFirstName(company.getAdminFName());
            lead.setLastName(company.getAdminLName());
            lead.getHomePhone().add(company.getCallCode() != null ? company.getCallCode() + company.getPhone() : company.getPhone());
            lead.setOwnerId(wfmDR.getObjectID());
            SecurityContext.getInstance().setStaticUserID(wfmDR.getObjectID());
            lead.setContactType(ContactListItem.LEAD_CONTACT);
            Address address = new Address();
            address.setCountryId(company.getCountryID());
            address.setPrimary(true);
            lead.getAddresses().add(address);

            //T6819: save lead source (one of EMAIL/FACEBOOK/LINKEDIN/MICROSOFT)
            try {
                if (company.getRegistrationType() != null) {
                    List<EdsReference> leadSources = referenceManager.listReferences(EdsCrmContact._LEAD_SOURCE);
                    for (EdsReference leadSource : leadSources) {
                        if (StringUtils.isNotBlank(leadSource.getName()) && leadSource.getName().toUpperCase().contains(company.getRegistrationType().getType())) {
                            lead.setLeadSourceID(leadSource.getObjectID());
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //End of T6819

            List<CompanyCustomFieldItem> leadCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.Lead);

            ArrayList<CompanyCustomFieldItem> customFields = new ArrayList<>();
            if (leadCustomFields != null && leadCustomFields.size() > 0) {
                for (CompanyCustomFieldItem customFieldItem : leadCustomFields) {
                    if (!Constants.DATA_TYPE_DATE.equals(customFieldItem.getDataType())) {
                        if ("Sign Up From".equals(customFieldItem.getFieldName())) {
                            //
                            customFieldItem.setFieldStringValue(company.getCompanySignedUpFrom() != null ? company.getCompanySignedUpFrom() : "");    //signedUpFrom
                        } else if ("Affiliate".equals(customFieldItem.getFieldName())) {
                            //
                            customFieldItem.setFieldStringValue(company.getValue1() != null ? company.getValue1() : "");                               //aff
                        } else if ("Campaign".equals(customFieldItem.getFieldName())) {
                            //
                            customFieldItem.setFieldStringValue(company.getValue2() != null ? company.getValue2() : "");                                //kcpn
                            if (company.getUtm_campaign() != null) {
                                customFieldItem.setFieldStringValue(company.getUtm_campaign());
                            }
                        } else if ("Source".equals(customFieldItem.getFieldName())) {
                            //
//                            customFieldItem.setFieldStringValue(company.getValue3() != null ? company.getValue3() : "");                                  //referer
                            if (company.getUtm_source() != null) {
                                customFieldItem.setFieldStringValue(company.getUtm_source());
                            }

                        } else if ("Keyword".equals(customFieldItem.getFieldName())) {
                            //
                            customFieldItem.setFieldStringValue(company.getUtm_keyword() != null ? company.getUtm_keyword() : "");                                  //keyword
                        } else if ("Medium".equals(customFieldItem.getFieldName())) {
                            //
                            customFieldItem.setFieldStringValue(company.getUtm_medium() != null ? company.getUtm_medium() : "");                                  //medium
                        } else if ("Content".equals(customFieldItem.getFieldName())) {
                            //
                            customFieldItem.setFieldStringValue(company.getUtm_content() != null ? company.getUtm_content() : "");                                  //content
                        } else if ("Utm Term".equals(customFieldItem.getFieldName())) {
                            //
                            customFieldItem.setFieldStringValue(company.getUtm_term() != null ? company.getUtm_term() : "");                                  //content
                        } else if ("Redirected".equals(customFieldItem.getFieldName())) {
                            //
                            customFieldItem.setFieldStringValue(company.getRedirected() != null ? company.getRedirected() : "");                                  //redirected
                            if (company.getRedirected() == null)
                                customFieldItem.setFieldStringValue(company.getValue3() != null ? company.getValue3() : "");                                  //referer
                        } else if ("Utm Button".equals(customFieldItem.getFieldName())) {
                            //
                            customFieldItem.setFieldStringValue(company.getUtm_btn() != null ? company.getUtm_btn() : "");

                        } else if ("Referrer".equals(customFieldItem.getFieldName())) {
                            //
                            customFieldItem.setFieldStringValue(company.getReferrer() != null ? company.getReferrer() : "");                                  //referrer
                        } else if ("Gclid".equals(customFieldItem.getFieldName())) {
                            //
                            customFieldItem.setFieldStringValue(company.getGclid() != null ? company.getGclid() : "");                                  //referrer
                        } else if ("Selected Apps".equals(customFieldItem.getFieldName())) {
                            customFieldItem.setFieldStringValue(company.getSelectedApps());                                  //Selected Apps
                        }
                        customFields.add(customFieldItem);
                    }
                }
                lead.setCustomFields(customFields);
            }

            //checkemailexistence Doniyor check qilmagin yozuvirsin degani uchun email existencega tekshirilmagan....
            String source = company.getValue3();
            String campaignName = "n/a";
            if (source != null && !source.isEmpty()) {
                if (source.contains("accounting") && source.contains("gclid")) {//should contain, keyword: accounting in url and the following keyword also: gclid  google adwords accounting
                    campaignName = "google adwords accounting";
                }
                if ((source.contains("hr") || source.contains("HRMS") || source.contains("attendance")) && source.contains("gclid")) {//should contain, keyword: hr or HRMS or attendance in url and the following keyword also: gclid  google adwords hrms
                    campaignName = "google adwords hrms";
                }
                if (source.contains("payroll") && source.contains("gclid")) {//should contain, keyword: payroll in url and the following keyword also: gclid    google adwords payroll
                    campaignName = "google adwords payroll";
                }
                if (source.contains("project") && source.contains("gclid")) {//should contain, keyword: project in url and the following keyword also: gclid    google adwords PM
                    campaignName = "google adwords PM";
                }
                if (source.contains("crm") && source.contains("gclid")) {//should contain, keyword: crm in url and the following keyword also: gclid    google adwords CRM
                    campaignName = "google adwords CRM";
                }
                if (source.contains("training") && source.contains("gclid")) {//should contain, keyword: training in url and the following keyword also: gclid  google adwords training
                    campaignName = "google adwords training";
                }

                if ((source.contains("project") || source.contains("timesheet")) && !source.contains("gclid")) {//should contain, keyword: project without: gclid   PM    and //should contain, keyword: timesheet without: gclid   PM
                    campaignName = "PM";
                }
                //should contain, keyword: all-in-one without: gclid    n/a
                if (source.contains("crm") && !source.contains("gclid")) {//should contain, keyword: crm without: gclid CRM
                    campaignName = "CRM";
                }
                if (source.contains("e-commerce") && !source.contains("gclid")) {//should contain, keyword: e-commerce without: gclid   e-commerce
                    campaignName = "e-commerce";
                }
                if (source.contains("reporting") && !source.contains("gclid")) {//should contain, keyword: reporting without: gclid reporting
                    campaignName = "reporting";
                }
                if (source.contains("document") && !source.contains("gclid")) {//should contain, keyword: document without: gclid   document
                    campaignName = "document";
                }
                if (source.contains("cms") && !source.contains("gclid")) {//should contain, keyword: cms without: gclid add-on
                    campaignName = "add-on";
                }
                if (source.contains("website-template") && !source.contains("gclid")) {//should contain, keyword: website-template without: gclid   website
                    campaignName = "website";
                }
                if (source.contains("invoice-template") && !source.contains("gclid")) {//should contain, keyword: invoice-template without: gclid   invoice-template
                    campaignName = "invoice-template";
                }
                if (source.contains("training") && !source.contains("gclid")) {//should contain, keyword: training without: gclid   training center
                    campaignName = "training center";
                }
                if ((source.contains("pricing") || source.contains("comparison")) && !source.contains(":gclid")) {//should contain, keyword: pricing without: gclid pricing       and      //should contain, keyword: comparison without: gclid pricing
                    campaignName = "pricing";
                }
                if (source.contains("TextileFinds")) {
                    EdsReference textilefindsSource = referenceManager.findReference(
                            EdsCrmContact._LEAD_SOURCE, "TEXTILEFINDS"
                    );
                    if (textilefindsSource != null) {
                        lead.setLeadSourceID(textilefindsSource.getObjectID());
                    }
                    lead.setOtherLeadSource(source);
                    if (company.getTfData() != null) {
                        String[] parts = company.getTfData().split("\\|", -1);
                        String tfEmail   = parts.length > 0 ? parts[0] : "";
                        String tfPhone   = parts.length > 1 ? parts[1] : "";
                        String tfTariff  = parts.length > 2 ? parts[2] : "";
                        lead.setNote("TextileFinds\n" +
                                "Company: " + company.getName() + "\n" +
                                "Email: " + tfEmail + "\n" +
                                "Phone: " + tfPhone + "\n" +
                                "Package: " + tfTariff);
                    }
                }
            }
            if (company.getCompanySignedUpFrom() != null) {
                System.out.println("++++++++++++++++++++++++++++++++++++++++" + company.getCompanySignedUpFrom() + "++++++++++++++++++++++++++++++++++++++++");
                if (company.getCompanySignedUpFrom().toLowerCase().contains("android")) {//should the field "signed up from" contain keyword "android"    android
                    campaignName = "android";
                }
                if (company.getCompanySignedUpFrom().toLowerCase().contains("iphone")) {//should the field "signed up from" contain keyword "iphone"  iphone
                    campaignName = "iphone";
                }
                if (company.getCompanySignedUpFrom().toLowerCase().contains("google_marketplace")) {//should the field "signed up from" contain keyword "google_marketplace"  google apps marketplace
                    campaignName = "google apps marketplace";
                }
            }
            EdsCampaign campaign = campaignManager.getCampaignByName(campaignName);
            if (campaign != null) {
                lead.setCampaignId(campaign.getObjectID());
                lead.setCampaign(campaign.getName());
            }
            if (company.getUtm_campaign() != null) {
                lead.setCampaign(company.getUtm_campaign() + " " + lead.getCampaign());
            }

            System.out.println(">>>>>>>>>>>SIGNUP BEFORE SAVING LEAD EMAIL = " + company.getAdminEmail());
            System.out.println(">>>>>>>>>>>SIGNUP BEFORE SAVING LEAD COMPANYID = " + ServerSecurityContext.getInstance().getCompanyId());
            System.out.println(">>>>>>>>>>>SIGNUP BEFORE SAVING LEAD USER = " + ServerSecurityContext.getInstance().getStaticUserID());
            try {
                //SAVE LEAD
                crmService.saveLead(lead, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println(">>>>>>>>>>>SIGNUP AFTER SAVING LEAD COMPANYID = " + ServerSecurityContext.getInstance().getCompanyId());
            System.out.println(">>>>>>>>>>>SIGNUP AFTER SAVING LEAD USERID = " + ServerSecurityContext.getInstance().getStaticUserID());

            SecurityContext.getInstance().setStaticUserID(null);
        } else {
            System.out.println(">>>>>>>>>>>CompanyRegistrationCustomEventListenerImpl COMPANY DATA MISSING = " + ServerSecurityContext.getInstance().getCompanyId());
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }

}
