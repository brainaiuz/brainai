package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.factory.PermissionDenyContainerFactory;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioContactItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.client.ui.communication.ContactDetailsItem;
import com.edatasite.workforce.gwt.core.client.ui.communication.handlers.AsteriskCallHandler;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.documents.client.factory.DocumentsSinksContainerFactory;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public class Documents extends WorkforceEntryPoint {

//    public static LinkedList<SelectItem> documentFolders = new LinkedList<>();

    public interface DocumentsResources extends ClientBundle {
        @CssResource.NotStrict
        @Source("com/edatasite/workforce/gwt/documents/resources/doc.css")
        CssResource document();
    }

    public static DocumentsResources resource = GWT.create(DocumentsResources.class);

    public void initSinksContainerFactory() {

        if (Utils.hasPermission(PermissionConstants.DOCUMENTS_MAIN_MENU)) {
            containerFactory = new DocumentsSinksContainerFactory(this);
        } else {
            containerFactory = new PermissionDenyContainerFactory(this);
            String section = Utils.getFirstAvailableSectionName();
            if (section != null) {
                Utils.redirect(GWT.getHostPageBaseURL() + section);
            }
        }
        resource.document().ensureInjected();
    }

    public AsteriskCallHandler initializeAsteriskCallHandler(List<AsteriskSettings> asteriskSettings, String userFullName) {
        AsteriskCallHandler asteriskCallHandler = new AsteriskCallHandler(asteriskSettings, userFullName);
        asteriskCallHandler.setIncommingCallCommand((incomingNumber) -> {
            CommonService.App.get().getIncomingCallerDetails(incomingNumber, new AsyncCallback<TwilioContactItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                    GWT.log("Error: ", throwable);
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
