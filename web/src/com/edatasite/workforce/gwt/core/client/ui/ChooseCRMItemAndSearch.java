package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 06.09.2010
 * Time: 14:50:48
 * To change this template use File | Settings | File Templates.
 */
public class ChooseCRMItemAndSearch extends Composite implements Constants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DataListBox reportedBy;
    private CRMLookUp account;
    private CRMLookUp contact;
    private CRMLookUp lead;

    private TextBox firstName;
    private TextBox lastName;
    private TextBox company;
    private TextBox email;
    private TextBox phone;
    private TextBox fax;
    private CustomForm customForm;
    private InputGroup inputGroup;
    private boolean isProperties;
    private final MaterialPanel otherFields = new MaterialPanel();

    private final ArrayList<SelectItem> reportedBys = new ArrayList<>(
            Arrays.asList(
                    new SelectItem(CrmConstants.CRM_CONTACT_ID, Property.get(Constants.Contacts, wfmStrings.contact())),
                    new SelectItem(CrmConstants.CRM_ACCOUNT_ID, wfmStrings.crmAccount()),
                    new SelectItem(-1, wfmStrings.other()))
    );

    private final ArrayList<SelectItem> reportedByProperties = new ArrayList<>(
            Arrays.asList(
                    new SelectItem(CrmConstants.CRM_CONTACT_ID, Property.get(Constants.Contacts, wfmStrings.contact())),
                    new SelectItem(CrmConstants.CRM_ACCOUNT_ID, wfmStrings.crmAccount())
            )
    );

    public ChooseCRMItemAndSearch(CustomForm customForm) {
        this.customForm = customForm;
        init();
    }

    public ChooseCRMItemAndSearch(boolean isProperties) {
        this.isProperties = isProperties;
        init();
    }

    private void init() {
        reportedBy = new DataListBox();
        reportedBy.setWidth("140px");
        reportedBy.setAllowFirstItem(false);
        reportedBy.setWithoutNullLabel(true);
        if (Utils.hasModuleEnabled(PermissionConstants.LEAD_MANAGEMENT)) {
            reportedBys.add(new SelectItem(CrmConstants.CRM_LEAD_ID, Property.get(Constants.LEADS, wfmStrings.lead())));
            reportedByProperties.add(new SelectItem(CrmConstants.CRM_LEAD_ID, Property.get(Constants.LEADS, wfmStrings.lead())));
        }
        if (isProperties) {
            reportedBy.setItems(reportedByProperties.toArray(new SelectItem[]{}));
        } else {
            reportedBy.setItems(reportedBys.toArray(new SelectItem[]{}));
        }
        reportedBy.addValueChangeHandler(event -> addFieldsByType(((DataListBox) event.getSource()).getSelectedId()));
        inputGroup = new InputGroup();
        inputGroup.add(reportedBy);
        initWidget(inputGroup);
        addFieldsByType(CrmConstants.CRM_CONTACT_ID);
    }

    private void addFieldsByType(Integer type) {
        if (inputGroup.getWidgetCount() == 2) {
            inputGroup.remove(1);
        }
        otherFields.setVisible(false);
        reportedBy.setWidth("140px");
        inputGroup.removeStyleName("form-group");

        if (CrmConstants.CRM_LEAD_ID == type) {
            inputGroup.add(getLeadField());
        } else if (CrmConstants.CRM_CONTACT_ID == type) {
            inputGroup.add(getContactField());
        } else if (CrmConstants.CRM_ACCOUNT_ID == type) {
            inputGroup.add(getCrmAccountField());
        }else {
            initOtherField();
            reportedBy.getElement().getStyle().clearWidth();
            otherFields.setVisible(true);
            inputGroup.addStyleName("form-group");
        }
    }

    private CRMLookUp getLeadField() {
        if (lead == null) {
            lead = new CRMLookUp(CRMLookUp.CRM_LEAD_ID);
            lead.setWidth("100%");
        }
        return lead;
    }

    private CRMLookUp getContactField() {
        if (contact == null) {
            contact = new CRMLookUp(CRMLookUp.CRM_CONTACT_ID);
            contact.setWidth("100%");
        }
        return contact;
    }

    private CRMLookUp getCrmAccountField() {
        if (account == null) {
            account = new CRMLookUp(CRMLookUp.CRM_ACCOUNT_ID);
            account.setWidth("100%");
        }
        return account;
    }

    private void initOtherField() {
        otherFields.clear();
        firstName = new TextBox();
        firstName.setWidth("100%");
        FlowPanel f = new FlowPanel();
        f.setStyleName("form-group");
        Span firstNameLabel = new Span(wfmStrings.firstName());
        firstNameLabel.setStyleName("form-label");
        f.add(firstNameLabel);
        f.add(firstName);
        otherFields.add(f);

        lastName = new TextBox();
        lastName.setWidth("100%");
        f = new FlowPanel();
        f.setStyleName("form-group");
        Span lastNameLabel = new Span(wfmStrings.lastName());
        lastNameLabel.setStyleName("form-label");
        f.add(lastNameLabel);
        f.add(lastName);
        otherFields.add(f);

        company = new TextBox();
        company.setWidth("100%");
        f = new FlowPanel();
        f.setStyleName("form-group");
        Span companyLabel = new Span(wfmStrings.company());
        companyLabel.setStyleName("form-label");
        f.add(companyLabel);
        f.add(company);
        otherFields.add(f);

        email = new TextBox();
        email.setWidth("100%");
        f = new FlowPanel();
        f.setStyleName("form-group");
        Span emailLabel = new Span(wfmStrings.email());
        emailLabel.setStyleName("form-label");
        f.add(emailLabel);
        f.add(email);
        otherFields.add(f);

        phone = new TextBox();
        phone.setWidth("100%");
        f = new FlowPanel();
        f.setStyleName("form-group");
        Span phoneLabel = new Span(wfmStrings.phone());
        phoneLabel.setStyleName("form-label");
        f.add(phoneLabel);
        f.add(phone);
        otherFields.add(f);

        fax = new TextBox();
        fax.setWidth("100%");
        f = new FlowPanel();
        f.setStyleName("form-group");
        Span faxLabel = new Span(wfmStrings.fax());
        faxLabel.setStyleName("form-label");
        f.add(faxLabel);
        f.add(fax);
        otherFields.add(f);
    }

    public void setValues(String from, SelectItem... selectedItems) {
        SelectItem selectedItem = selectedItems != null && selectedItems.length > 0 ? selectedItems[0] : null;
        reportedBy.setSelected(getTypeID(from));
        addFieldsByType(getTypeID(from));
        if (selectedItem != null) {
            CRMLookUp lookUP;

            if (CrmConstants.CRM_LEAD.equals(from)) {
                lookUP = lead;
            } else if (CrmConstants.CRM_CONTACT.equals(from)) {
                lookUP = contact;
            } else if (CrmConstants.CRM_ACCOUNT.equals(from)) {
                lookUP = account;
            } else {
                lookUP = null;
            }

            if (lookUP != null) {
                lookUP.addOracle(selectedItem);
                lookUP.getTextBox().getElement().getStyle().setColor("#000");
                lookUP.setValue(selectedItem.getName(), true);
            }
        }
    }

    public void disableIfAnonim(){
        reportedBy.setEnabled(false);
        contact.setEnabled(false);
    }

    private Integer getTypeID(String type) {
        if (CrmConstants.CRM_CONTACT.equals(type)) {
            return CrmConstants.CRM_CONTACT_ID;
        } else if (CrmConstants.CRM_ACCOUNT.equals(type)) {
            return CrmConstants.CRM_ACCOUNT_ID;
        }
        return CrmConstants.CRM_LEAD_ID;
    }

    public int validate() {
        int errors = 0;
        if (reportedBy.getSelectedId() != null && reportedBy.getSelectedId() == -1 && customForm != null) {
            errors += customForm.markAsError(email, Utils.isNullOrEmpty(email.getText()) || !Utils.validateEmail(email.getText(), false));
            errors += customForm.markAsError(lastName, Utils.isNullOrEmpty(lastName.getText()));
            errors += customForm.markAsError(company, Utils.isNullOrEmpty(company.getText()));
        } else if (reportedBy.getSelectedId() != null) {
            if (reportedBy.getSelectedId() == CrmConstants.CRM_LEAD_ID) {
                errors += customForm.markAsError(lead, lead.getSelectedItem() == null);
            } else if (CrmConstants.CRM_ACCOUNT_ID == reportedBy.getSelectedId()) {
                errors += customForm.markAsError(account, account.getSelectedItem() == null);
            } else if (CrmConstants.CRM_CONTACT_ID == reportedBy.getSelectedId()) {
                errors += customForm.markAsError(contact, contact.getSelectedItem() == null);
            }
        }
        return errors;
    }

    public SelectItem getReporter() {
        if (isLeadChecked()) {
            return lead != null ? lead.getSelectedItem() : null;
        } else if (isAccountChecked()) {
            return account != null ? account.getSelectedItem() : null;
        } else if (isContactChecked()) {
            return contact != null ? contact.getSelectedItem() : null;
        }
        return null;
    }

    public Map<String, String> getOtherReporterInformation() {
        Map<String, String> map = new LinkedHashMap<>();
        if (isOtherChecked()) {
            map.put("firstName", firstName.getText() != null ? firstName.getText() : "");
            map.put("lastName", lastName.getText() != null ? lastName.getText() : "");
            map.put("company", company.getText() != null ? company.getText() : "");
            map.put("email", email.getText() != null ? email.getText() : "");
            map.put("phone", phone.getText() != null ? phone.getText() : "");
            map.put("fax", fax.getText() != null ? fax.getText() : "");
        }
        return map;
    }

    public boolean isLeadChecked() {
        return reportedBy.getSelectedId(true) != null && reportedBy.getSelectedId(true) == CrmConstants.CRM_LEAD_ID;
    }

    public boolean isAccountChecked() {
        return reportedBy.getSelectedId(true) != null && reportedBy.getSelectedId(true) == CrmConstants.CRM_ACCOUNT_ID;
    }

    public boolean isContactChecked() {
        return reportedBy.getSelectedId(true) != null && reportedBy.getSelectedId(true) == CrmConstants.CRM_CONTACT_ID;
    }

    public boolean isOtherChecked() {
        return reportedBy.getSelectedId(true) != null && reportedBy.getSelectedId(true) == -1;
    }

    public void clear() {
        if (account != null) {
            account.clear();
        }
        if (contact != null) {
            contact.clear();
        }
        if (lead != null) {
            lead.clear();
        }

        if (reportedBy != null) {
            reportedBy.setSelected(CrmConstants.CRM_LEAD_ID);
            addFieldsByType(CrmConstants.CRM_LEAD_ID);
        }
    }

    public MaterialPanel getOtherFields() {
        return otherFields;
    }
}
