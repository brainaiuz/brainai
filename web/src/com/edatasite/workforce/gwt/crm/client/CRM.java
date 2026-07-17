package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.factory.PermissionDenyContainerFactory;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.client.ui.communication.ContactDetailsItem;
import com.edatasite.workforce.gwt.core.client.ui.communication.handlers.AsteriskCallHandler;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.crm.client.factory.CRMSinksContainerFactory;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 13:08:15
 * To change this template use File | Settings | File Templates.
 */
public class CRM extends WorkforceEntryPoint {
    public static List<SelectItem> dashboards = new ArrayList<>();
    public static String defaultDashboardName;

    public interface CrmResource extends ClientBundle {
//        @CssResource.NotStrict
//        @Source("com/edatasite/workforce/gwt/crm/client/crm.css")
//        CssResource crm();

        @CssResource.NotStrict
        @Source("com/edatasite/workforce/gwt/documents/resources/doc.css")
        CssResource docs();

        @CssResource.NotStrict
        @ClientBundle.Source("com/edatasite/workforce/gwt/crm/client/crmwelcome/CRMWelcome.css")
        CssResource crmWelcome();

        @CssResource.NotStrict
        @ClientBundle.Source("com/edatasite/workforce/gwt/crm/client/crmwelcome/CRMWelcomeRU.css")
        CssResource crmWelcomeRU();

        @CssResource.NotStrict
        @ClientBundle.Source("com/edatasite/workforce/gwt/crm/client/crmwelcome/CRMWelcomeAR.css")
        CssResource crmWelcomeAR();

        @CssResource.NotStrict
        @ClientBundle.Source("com/edatasite/workforce/gwt/crm/client/crmwelcome/CRMWelcomeNL.css")
        CssResource crmWelcomeNL();

        @CssResource.NotStrict
        @ClientBundle.Source("com/edatasite/workforce/gwt/crm/client/crmwelcome/CRMWelcomeTH.css")
        CssResource crmWelcomeTH();

        @CssResource.NotStrict
        @ClientBundle.Source("com/edatasite/workforce/gwt/payroll/client/Payroll.css")
        CssResource payroll();

        @CssResource.NotStrict
        @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/client/Accounting.css")
        CssResource accounting();
    }

    public static CrmResource resource = GWT.create(CrmResource.class);

    @Override
    protected void initUserSettings() {
        if (Utils.isClientContact()) {
            initDefaultUserSettings();
        } else {
            loadUserDashboards();
        }
    }

    @Override
    protected void loadUserDashboards() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setModule(ModuleEnum.CRM.getCode());
        fp.setLimit(50);
        ModuleDashboardService.App.get().getModuleDashboards(fp, new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                initDefaultUserSettings();
            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                defaultDashboardName = null;
                dashboards.clear();

                if (result.size() > 0) {
                    for (SelectItem selectItem : result) {
                        if (selectItem != null && selectItem.isSelected()) {
                            defaultDashboardName = selectItem.getId().toString();
                            break;
                        }
                    }
                    if (defaultDashboardName == null && result.get(0) != null) {
                        defaultDashboardName = result.get(0).getName();
                    }
                    dashboards = result;
                }
                initDefaultUserSettings();
            }
        });
    }

    public void initSinksContainerFactory() {

        if (Utils.hasPermission(PermissionConstants.CRM_MAIN_MENU)) {
            containerFactory = new CRMSinksContainerFactory(this);
        } else {
            containerFactory = new PermissionDenyContainerFactory(this);
            String section = Utils.getFirstAvailableSectionName();
            if (section != null) {
                Utils.redirect(GWT.getHostPageBaseURL() + section);
            }
        }
        resource.docs().ensureInjected();
        resource.payroll().ensureInjected();
        resource.accounting().ensureInjected();
        String language = Utils.userSettings.get(LANGUAGE_FOR_USER);
        if (language != null) {
            if (language.contains("ru")) {
                resource.crmWelcomeRU().ensureInjected();
            } else if (language.contains("ar")) {
                resource.crmWelcomeAR().ensureInjected();
            } else if (language.contains("nl")) {
                resource.crmWelcomeNL().ensureInjected();
            } else if (language.contains("th")) {
                resource.crmWelcomeTH().ensureInjected();
            } else {
                resource.crmWelcome().ensureInjected();
            }
        } else {
            resource.crmWelcome().ensureInjected();
        }
    }

    public AsteriskCallHandler initializeAsteriskCallHandler(List<AsteriskSettings> asteriskSettings, String userFullName) {
        AsteriskCallHandler asteriskCallHandler = new AsteriskCallHandler(asteriskSettings, userFullName);
        asteriskCallHandler.setIncommingCallCommand((incomingNumber) -> {
            CommonService.App.get().getIncomingCallerDetails(incomingNumber, new AsyncCallback<TwilioContactItem>() {
                @Override
                public void onFailure(Throwable throwable) {
//                    GWT.log("Error: ", throwable);
                }

                @Override
                public void onSuccess(TwilioContactItem twilioContactItem) {

                    if (twilioContactItem != null) {
                        ContactDetailsItem incomingCallerDetails = new ContactDetailsItem();
                        incomingCallerDetails.setId(twilioContactItem.getObjectID());
                        incomingCallerDetails.setOwner(twilioContactItem.getOwner());
                        incomingCallerDetails.setOwnerId(twilioContactItem.getOwnerId());
                        if (twilioContactItem.getMobile() != null && !twilioContactItem.getMobile().isEmpty()) {
                            incomingCallerDetails.setMobile(twilioContactItem.getMobile().get(0));
                        }
                        incomingCallerDetails.setName(twilioContactItem.getName() != null ? twilioContactItem.getName() : incomingNumber);
                        incomingCallerDetails.setPhoneNumber(incomingNumber);
                        incomingCallerDetails.setPrimaryEmail(twilioContactItem.getEmail());
                        incomingCallerDetails.setContactType(twilioContactItem.getContactType());
                        incomingCallerDetails.setOtherFields(twilioContactItem.getOtherTypes());
                        if (twilioContactItem.getVacancy() != null) {
                            incomingCallerDetails.setVacancy(twilioContactItem.getVacancy());
                        }
                        if (twilioContactItem.getStatus() != null) {
                            incomingCallerDetails.setStatus(twilioContactItem.getStatus());
                        }
                        if (twilioContactItem.getEmployee() != null) {
                            incomingCallerDetails.setEmployee(twilioContactItem.getEmployee());
                        }
                        if (twilioContactItem.getCompany() != null) {
                            incomingCallerDetails.setCompany(twilioContactItem.getCompany());
                        }
                        if (twilioContactItem.getCompanyId() != null) {
                            incomingCallerDetails.setCompanyId(twilioContactItem.getCompanyId());
                        }
                        if (twilioContactItem.getOpportunity() != null) {
                            incomingCallerDetails.setOpportunity(twilioContactItem.getOpportunity());
                        }
                        if (twilioContactItem.getAccountIndustry() != null) {
                            incomingCallerDetails.setAccountIndustry(twilioContactItem.getAccountIndustry());
                        }
                        if (twilioContactItem.getEmployee() != null) {
                            incomingCallerDetails.setEmployee(twilioContactItem.getEmployee());
                        }


                        RelationItem relationItem = RelationItem.newEventRelation(twilioContactItem.getContactType() == null || !twilioContactItem.getContactType().equals(5) //LEAD_CONTACT=5
                                        ? RelationItem.TYPE_CONTACT
                                        : RelationItem.TYPE_LEAD,
                                twilioContactItem.getObjectID(),
                                twilioContactItem.getName() != null ? twilioContactItem.getName() : incomingNumber);

                        ContactListItem contactListItem = new ContactListItem();
                        contactListItem.setObjectId(twilioContactItem.getObjectID());
                        contactListItem.setContactType(twilioContactItem.getContactType());
                        contactListItem.setContactName(twilioContactItem.getName());


                        incomingCallerDetails.setTaskCommand((s) -> new TaskQuickAddView(s, relationItem));
                        incomingCallerDetails.setSmsCommand((s) -> new ActivityQuickAddForm(Appointment.SMS, contactListItem, s, relationItem));
                        incomingCallerDetails.setCallCommand((s) -> new ActivityQuickAddForm(Appointment.CALL_LOG, incomingNumber, contactListItem, s, relationItem));
                        incomingCallerDetails.setEventCommand((s) -> new ActivityQuickAddForm(Appointment.EVENT, incomingNumber, contactListItem, s, relationItem));

                        asteriskCallHandler.setIncomingCallerDetails(incomingCallerDetails);
                    }
                }
            });
        });
        return asteriskCallHandler;
    }
}
