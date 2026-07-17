package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.google.gwt.user.client.rpc.AsyncCallback;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 04.11.13
 * Time: 19:37
 * To change this template use File | Settings | File Templates.
 */
public class PhonePopup {
    protected static final WfmStrings strings = WfmStrings.App.get();
    protected static final CrmStrings crmStrings = CrmStrings.App.get();

    private ContactListItem contact;
    private final String phoneNumber;
    private Span phoneSpan;
    //    private boolean mobile = false;
    private boolean fromListing = false;
    private MaterialLink call;
    private Icon sms;
    private String relationType;
    private Integer relationId;
    private String relationName;
    private RelationItem relationItem;
    private boolean hasPermission = true;

    public PhonePopup(String phone, ContactListItem contactItem, boolean mobile) {
        this(phone, contactItem, mobile, false);
    }

    public PhonePopup(String phone, ProfileItem contactItem, boolean hasPermission) {
        this(phone, contactItem, false, false);
        this.hasPermission = hasPermission;
    }

    public PhonePopup(String phone, ContactListItem contactItem, boolean mobile, boolean fromListing) {
        String cleanPhone = (phone != null ? phone.trim().replaceAll("[^+\\d]", "") : "");
        this.phoneNumber = !Utils.isNullOrEmpty(cleanPhone) && !"n/a".equalsIgnoreCase(cleanPhone)
                ? (!cleanPhone.startsWith("+") ? "+" + cleanPhone : cleanPhone)
                : "";
        this.contact = contactItem;
        this.fromListing = fromListing;

        relationId = contact != null ? contact.getObjectId() : null;
        relationType = contact != null && contact.isLeadContact() ? RelationItem.TYPE_LEAD : contact != null && contact.isCandidate() ? RelationItem.TYPE_CANDIDATE : RelationItem.TYPE_CONTACT;
        relationName = contact != null && contact.getName() != null ? contact.getName() : null;
        if (contact instanceof ProfileItem) {
            relationId = ((ProfileItem) contact).getContactID();
        }
        draw(null);
    }

    public PhonePopup(String phone, ContactListItem contactItem, boolean mobile, boolean fromListing, RelationItem relationItem) {
        String cleanPhone = (phone != null ? phone.trim().replaceAll("[^+\\d]", "") : "");
        this.phoneNumber = !Utils.isNullOrEmpty(cleanPhone) && !"n/a".equalsIgnoreCase(cleanPhone)
                ? (!cleanPhone.startsWith("+") ? "+" + cleanPhone : cleanPhone)
                : "";
        this.contact = contactItem;
        this.fromListing = fromListing;
        this.relationItem = relationItem;

        relationId = contact != null ? contact.getObjectId() : null;
        relationType = contact != null && contact.isLeadContact() ? RelationItem.TYPE_LEAD : contact != null && contact.isCandidate() ? RelationItem.TYPE_CANDIDATE : RelationItem.TYPE_CONTACT;
        relationName = contact != null && contact.getName() != null ? contact.getName() : null;
        if (contact instanceof ProfileItem) {
            relationId = ((ProfileItem) contact).getContactID();
        }
        draw(null);
    }

    public PhonePopup(String phone, String relationType, Integer relationId, String relationName, boolean mobile, boolean fromListing) {
        String cleanPhone = (phone != null ? phone.trim().replaceAll("[^+\\d]", "") : "");
        this.phoneNumber = !Utils.isNullOrEmpty(cleanPhone) && !"n/a".equalsIgnoreCase(cleanPhone)
                ? (!cleanPhone.startsWith("+") ? "+" + cleanPhone : cleanPhone)
                : "";
        if (RelationItem.TYPE_EMPLOYEE.equals(relationType)) {
            ContactService.App.get().editProfile(relationId, Utils.isPM() ? "FROM_PM_EMPLOYEE_VIEW" : "FROM_HRMS_EMPLOYEE_VIEW", true, new AbstractAsyncCallback<ProfileItem>() {
                public void success(ProfileItem o) {
                    LoadingPanel.loading(false);
                    draw(o);
                }
            });
        } else {
            this.relationType = relationType;
            this.relationId = relationId;
            this.relationName = relationName;
            this.fromListing = fromListing;
            draw(null);
        }
    }

    public PhonePopup(String phone, String relationType, Integer relationId, String relationName, boolean mobile, boolean fromListing, OpportunityListItem opportunity, String type, EmployeeListItem employee, Integer id) {
        String cleanPhone = (phone != null ? phone.trim().replaceAll("[^+\\d]", "") : "");
        this.phoneNumber = !Utils.isNullOrEmpty(cleanPhone) && !"n/a".equalsIgnoreCase(cleanPhone)
                ? (!cleanPhone.startsWith("+") ? "+" + cleanPhone : cleanPhone)
                : "";
        if (RelationItem.TYPE_EMPLOYEE.equals(relationType)) {
            ContactService.App.get().editProfile(relationId, Utils.isPM() ? "FROM_PM_EMPLOYEE_VIEW" : "FROM_HRMS_EMPLOYEE_VIEW", true, new AbstractAsyncCallback<ProfileItem>() {
                public void success(ProfileItem o) {
                    LoadingPanel.loading(false);
                    draw(o);
                }
            });
        } else {
            this.relationType = relationType;
            this.relationId = relationId;
            this.relationName = relationName;
            this.fromListing = fromListing;
            draw(null);
        }
    }

    private void draw(ProfileItem profileItem) {
        if (profileItem != null) {
            this.relationId = profileItem != null ? profileItem.getObjectId() : null;
            relationType = RelationItem.TYPE_CONTACT;
            relationName = profileItem != null && profileItem.getName() != null ? profileItem.getName() : null;
            relationId = profileItem.getContactID();
        }
        call = new MaterialLink();
        call.setStyleName("cp_phone__number");
        Icon phoneIcon = new Icon();
        phoneIcon.setStyleName("ficon--phone");

        phoneSpan = new Span(phoneNumber);

        call.addClickHandler(clickEvent -> {
            LoadingPanel.loading(true);
            if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_QUICK_CALL_CANDIDATE : PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL) && !Utils.isNullOrEmpty(phoneNumber)) {
                ContactService.App.get().getContact(relationId, false, new AsyncCallback<ContactListItem>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(ContactListItem contactListItem) {
                        if (relationItem != null) {
                            new ActivityQuickAddForm(Appointment.CALL_LOG, phoneNumber, contactListItem, relationItem, RelationItem.newEventRelation(relationType, relationId, relationName));
                        } else {
                            new ActivityQuickAddForm(Appointment.CALL_LOG, phoneNumber, contactListItem, RelationItem.newEventRelation(relationType, relationId, relationName));
                        }
                        LoadingPanel.loading(false);
                    }
                });
            } else if (!Utils.isNullOrEmpty(phoneNumber)) {
                Info.warn(crmStrings.youDontHavePermissiontoCall());
                LoadingPanel.loading(false);
            }
        });

        call.add(phoneIcon);
        call.add(phoneSpan);

        sms = new Icon();
        sms.setStyleName("ficon--sms");
        sms.addClickHandler(clickEvent -> {
            LoadingPanel.loading(true);
            if ((Utils.isCRM() && Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITY_SEND_SMS)) || (Utils.isHRMS() && Utils.hasPermission(PermissionConstants.HRMS_CONDIDATE_SMS_SEND)) || (Utils.isAccounting() && Utils.hasPermission(PermissionConstants.CLIENT_SEND_SMS))) {
                ContactService.App.get().getContact(relationId, false, new AsyncCallback<ContactListItem>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(ContactListItem contactListItem) {
                        if (relationItem != null) {
                            new ActivityQuickAddForm(Appointment.SMS, phoneNumber, contactListItem, RelationItem.newEventRelation(relationType, relationId, relationName), relationItem);
                        } else {
                            new ActivityQuickAddForm(Appointment.SMS, phoneNumber, contactListItem, RelationItem.newEventRelation(relationType, relationId, relationName));
                        }
                        LoadingPanel.loading(false);
                    }
                });
            } else {
                Info.warn(strings.youDontHavePermission());
                LoadingPanel.loading(false);
            }
        });


    }

    public Div getPhoneWidget() {
        Div phoneDiv = new Div("cp_phone");
        Div phoneCellDiv = new Div("cp_phone__call");
        if (!fromListing && hasPermission) {
            Div smsDiv = new Div("cp_phone__text");
            smsDiv.add(sms);
            phoneDiv.add(smsDiv);
        }
        phoneCellDiv.add(call);

        if (Utils.isNullOrEmpty(phoneNumber)) {
            phoneSpan.setText(strings.noNumber());
            phoneDiv.addStyleName("cp_phone--no-number");
        }
        phoneDiv.add(phoneCellDiv);
        return phoneDiv;
    }
}
