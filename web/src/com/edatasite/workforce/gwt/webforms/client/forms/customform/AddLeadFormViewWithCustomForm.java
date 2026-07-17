package com.edatasite.workforce.gwt.webforms.client.forms.customform;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.CountryStates;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.upload.WebFormsFileUpload;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.WebFormItem;
import com.edatasite.workforce.gwt.webforms.client.forms.LeadField;
import com.edatasite.workforce.gwt.webforms.client.forms.WebField;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Aug 12, 2010
 * Time: 5:30:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddLeadFormViewWithCustomForm extends AbstractAddFormViewWithCustomForm implements Constants {


    private Map<Integer, List> customFields;
    private CountryStates countryStates;
    private ContactListItem item;
    private final WebFormItem webFormItem;
    private final boolean enableAccess;
    private final boolean fromSubscriptionForm;
    private Map<String, Widget> widgetsMap;


    public AddLeadFormViewWithCustomForm(WebForm webForm, VerticalPanel antibotPanel, boolean enableAccess, boolean fromSubscriptionForm) {
        super(webForm, antibotPanel);
        this.enableAccess = enableAccess;
        this.fromSubscriptionForm = fromSubscriptionForm;
        webFormItem = new WebFormItem();
    }

    @Override
    public void setWebFormID(Integer webFormID) {
        if (item == null) {
            item = new ContactListItem();
        }
        item.setWebFormID(webFormID);
        countryStates = new CountryStates();
        widgetsMap = new HashMap<>();
    }

    public void addField(WebField webField) {
        if (!webField.isCustomField()) {
            Widget widget = addFieldToForm(webField);
            widgetsMap.put("SF_" + webField.getSavingField(), widget);
            switch (webField.getSavingField()) {
                case LeadField.FIELD_COUNTRY:
                    countryStates.setCountryField((DataListBox) widget);
                    countryStates.setCountries(countryStates.getCountryField().getItems());
                    break;
                case LeadField.FIELD_STATE:
                    countryStates.setStateField((DataListBox) widget);
                    countryStates.setStates(countryStates.getStateField().getItems());
                    Integer countryID = item.getPrimaryAddress(true).getCountryId();
                    SelectItem selectedState = countryStates.getStateField().getSelectedItem();
                    countryStates.getStateField().removeListItems();
                    if (countryID != null) {
                        countryStates.checkForStates(countryID, countryStates.getStateField());
                        if (selectedState != null) {
                            countryStates.getStateField().setSelected(selectedState);
                        }
                    }
                    break;
            }
        } else {
            if (customFields == null) {
                customFields = new HashMap<>();
            }
            if (!customFields.containsKey(webField.getSavingField())) {
                addCustomFieldToMap(webField);
                if (customFields.get(webField.getSavingField()) != null && customFields.get(webField.getSavingField()).size() > 0 && customFields.get(webField.getSavingField()).get(1) != null) {
                    widgetsMap.put("CF_" + webField.getSavingField(), (Widget) customFields.get(webField.getSavingField()).get(1));
                }
            }
        }
    }

    @Override
    protected String getFormID() {
        return null;
    }

    @Override
    protected void getDataToFillFields() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void fillItem(WebField webField, Object value, Widget widget) {
        boolean validate = false;
        if (webField.isMandatory() && webField.isShowInForm()) {
            validate = true;
        }
        if (!webField.isCustomField()) {
            switch (webField.getSavingField()) {
                case LeadField.FIELD_ASSIGNEE:
                    item.setLeadAssigneeID(getAsSelectItem(value).getId());
                    item.setLeadAssignee(getAsSelectItem(value).getName());
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_BACKUP_ASSIGNEE:
                    item.setLeadBackupAssigneeID(getAsSelectItem(value).getId());
                    item.setLeadBackupAssignee(getAsSelectItem(value).getName());
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_COMPANYNAME:
                    item.getCrmAccount().setName(getAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_FIRSTNAME:
                    item.setFirstName(getAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_LASTNAME:
                    item.setLastName(getAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_JOBTITLE:
                    item.setJobTitle(getAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_CITYTOWN:
                    item.getAddresses(true).get(0).setCity(getAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_STREET:
                    item.getAddresses(true).get(0).setAddress(getAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_COUNTRY:
                    item.getAddresses(true).get(0).setCountryId(getAsSelectItem(value).getId());
                    item.getAddresses(true).get(0).setCountry(getAsSelectItem(value).getName());
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_STATE:
                    item.getAddresses(true).get(0).setStateId(getAsSelectItem(value).getId());
                    item.getAddresses(true).get(0).setState(getAsSelectItem(value).getName());
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_POSTCODE:
                    item.getAddresses(true).get(0).setZipCode(getAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_LEADSOURCE:
                    item.setLeadSourceID(getAsSelectItem(value).getId());
                    item.setLeadSource(getAsSelectItem(value).getName());
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_CAMPAIGNSOURCE:
                    item.setCampaignId(getAsSelectItem(value).getId());
                    item.setCampaign(getAsSelectItem(value).getName());
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_LEADSTATUS:
                    item.setLeadStatus(getAsSelectItem(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_INDUSTRY:
                    item.getCrmAccount().setIndustryID(getAsSelectItem(value).getId());
                    item.getCrmAccount().setIndustry(getAsSelectItem(value).getName());
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_EMAIL1:
                    item.setHomeEmail(new ArrayList<>());
                    item.getHomeEmail().add(getAsString(value));
                    if (validate) {
                        validate(webField, widget, true);
                    }
                    break;
                case LeadField.FIELD_EMAIL2:
                    item.setWorkEmail(new ArrayList<>());
                    item.getWorkEmail().add(getAsString(value));
                    if (validate) {
                        validate(webField, widget, true);
                    }
                    break;
                case LeadField.FIELD_EMAIL3:
                    item.setOtherEmail(new ArrayList<>());
                    item.getOtherEmail().add(getAsString(value));
                    if (validate) {
                        validate(webField, widget, true);
                    }
                    break;
                case LeadField.FIELD_PHONE1:
                    item.setHomePhone(new ArrayList<>());
                    item.getHomePhone().add(getAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_PHONE2:
                    item.setWorkPhone(new ArrayList<>());
                    item.getWorkPhone().add(getAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_PHONE3:
                    item.setOtherPhone(new ArrayList<>());
                    item.getOtherPhone().add(getAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_FAX:
                    item.setHomeFax(new ArrayList<>());
                    item.setHomeFax(getPhoneAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_MOBILE:
                    item.setMobile(new ArrayList<>());
                    item.setMobile(getPhoneAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_WEBSITE:
                    item.setHomeWebSite(new ArrayList<>());
                    item.getHomeWebSite().add(getAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_EMAILOPTOUT:
                    item.setEmailOptOut(getAsBoolean(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_RATING:
                    item.setLeadRatingID(getAsSelectItem(value).getId());
                    item.setLeadRating(getAsSelectItem(value).getName());
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_NOTE:
                    item.setNote(getAsString(value));
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_ATTACHMENT:
                    if (value instanceof FileItem || value instanceof FileItem[]) {
                        item.setAttachments((FileItem[]) value);
                    }
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
                case LeadField.FIELD_MAILING_LIST:
                    if (value instanceof ArrayList) {
                        item.setSubscriptionIDs((ArrayList<Integer>) value);
                    }
                    if (validate) {
                        validate(webField, widget);
                    }
                    break;
            }
        } else {
            if (customFields != null && customFields.containsKey(webField.getSavingField())) {
                List fieldCustomFieldAndWidgets = customFields.get(webField.getSavingField());
                CompanyCustomFieldItem customField = (CompanyCustomFieldItem) fieldCustomFieldAndWidgets.get(0);
                Widget widgets = (Widget) fieldCustomFieldAndWidgets.get(1);
                setCustomFieldValues(webField, customField, widgets);
                fieldCustomFieldAndWidgets = new ArrayList();
                fieldCustomFieldAndWidgets.add(customField);
                fieldCustomFieldAndWidgets.add(widgets);
                customFields.remove(webField.getSavingField());
                customFields.put(webField.getSavingField(), fieldCustomFieldAndWidgets);
            }
        }
    }

    private void addCustomFieldToMap(WebField webField) {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        List fieldAndCustomField = new ArrayList();
        CompanyCustomFieldItem companyCustomField = new CompanyCustomFieldItem(webField.getSavingField());
        if (webField.getValues() != null && webField.getValues().length > 0) {
            companyCustomField.setPredefinedValues(Arrays.stream(webField.getValues()).map(SelectItem::getName).collect(Collectors.toList()).toArray(new String[]{}));
        }
        if (webField.getType().equals(WebFormConstants.INPUT_DATEPICKER)) {
            companyCustomField.setFieldDateNonConvertedValue(new DateNonConvertable((Date) webField.getDefaultValue()));
        } else {
            companyCustomField.setFieldStringValue((String) webField.getDefaultValue(true));
        }
        Widget widget = addFieldToForm(webField);
        fieldAndCustomField.add(companyCustomField);
        fieldAndCustomField.add(widget);
        customFields.put(webField.getSavingField(), fieldAndCustomField);
    }

    private void setCustomFieldValues(WebField webField, final CompanyCustomFieldItem customField, Widget widgets) {
        Integer type = webField.getType();
        if (widgets != null) {
            if (type.equals(WebFormConstants.INPUT_TEXTBOX)) {
                TextBox textBox = (TextBox) widgets;
                if (textBox != null) {
                    customField.setFieldStringValue(textBox.getText());
                }
            } else if (type.equals(WebForm.INPUT_DATEPICKER)) {
                DatePicker datePicker = (DatePicker) widgets;
                if (datePicker != null) {
                    customField.setFieldDateNonConvertedValue(new DateNonConvertable(datePicker.getDate()));
                }
            } else if (type.equals(WebForm.INPUT_DROPDOWN)) {
                DataListBox dataListBox = (DataListBox) widgets;
                if (dataListBox != null) {
                    customField.setFieldStringValue(dataListBox.getSelectedItem() != null ? dataListBox.getSelectedItem().getName() : null);
                }
            } else if (type.equals(WebForm.INPUT_CHECKBOX)) {
                KpiCheckBox[] checkBoxes = (KpiCheckBox[]) (widgets instanceof VerticalPanel ? getWidgets(widgets, WebForm.INPUT_CHECKBOX) : widgets);
                if (checkBoxes != null && checkBoxes.length > 0) {
                    customField.setFieldStringValue((String) null);
                    for (KpiCheckBox checkBox : checkBoxes) {
                        if (checkBox.getValue() != null && checkBox.getValue() && checkBox.getText() != null && !"".equals(checkBox.getText())) {
                            customField.setFieldStringValue(checkBox.getText(), true, true);
                        }
                    }
                }
            } else if (type.equals(WebForm.INPUT_RADIO_BUTTON)) {
                RadioButton[] radioButtons = (RadioButton[]) (widgets instanceof VerticalPanel ? getWidgets(widgets, WebForm.INPUT_RADIO_BUTTON) : widgets);
                if (radioButtons != null && radioButtons.length > 0) {
                    for (RadioButton radioButton : radioButtons) {
                        if (radioButton.getValue() != null && radioButton.getValue() && radioButton.getText() != null && !"".equals(radioButton.getText())) {
                            customField.setFieldStringValue(radioButton.getText());
                        }
                    }
                }
            }
            if (webField.isMandatory() && webField.isShowInForm()) {
                validate(webField, widgets);
            }
        }
    }

    private void prepareCustomFields() {
        if (customFields != null && customFields.size() > 0) {
            ArrayList<CompanyCustomFieldItem> customFieldsArray = new ArrayList<>();
            for (List wfmFieldCustomFieldAndWidgets : customFields.values()) {
                if (wfmFieldCustomFieldAndWidgets.size() > 1) {
                    CompanyCustomFieldItem customField = (CompanyCustomFieldItem) wfmFieldCustomFieldAndWidgets.get(0);
                    if (customField != null) {
                        customFieldsArray.add(customField);
                    }
                } else if (wfmFieldCustomFieldAndWidgets.size() == 1) {
                    if (wfmFieldCustomFieldAndWidgets.get(0) instanceof CompanyCustomFieldItem) {
                        CompanyCustomFieldItem customField = (CompanyCustomFieldItem) wfmFieldCustomFieldAndWidgets.get(0);
                        if (customField != null) {
                            customFieldsArray.add(customField);
                        }
                    }
                }
            }
            item.setCustomFields(customFieldsArray);
        }
    }

    public void save(String antibot) {
        LoadingPanel.loading(true);
        if (widgetsMap != null) {
            if (widgetsMap.size() > 0) {
                if (widgetsMap.get("SF_" + LeadField.FIELD_ATTACHMENT) != null && widgetsMap.get("SF_" + LeadField.FIELD_ATTACHMENT) instanceof WebFormsFileUpload) {
                    WebFormsFileUpload fileUpload = (WebFormsFileUpload) widgetsMap.get("SF_" + LeadField.FIELD_ATTACHMENT);
                    item.setAttachments(fileUpload.getAttachedFiles());
                }
            }
        }
        item.setWebFormID(webForm.getObjectId());
        prepareCustomFields();
        webFormItem.setContactListItem(item);
        webFormItem.setCompanyID(Integer.valueOf(Utils.getEncryptedCompanyID()));
        webFormItem.setWebformID(item.getWebFormID());
        webFormItem.setWebformType(WebFormConstants.LEAD_FORM);
        webFormItem.setEnableAccess(enableAccess ? Boolean.TRUE : Boolean.FALSE);
        webFormItem.setFromSubscriptionForm(fromSubscriptionForm ? Boolean.TRUE : Boolean.FALSE);
        if (haveErrors()) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            LoadingPanel.loading(false);
            if (errorOn != null) {
                errorOn.execute();
            }
        } else {
            antibot += "|" + Cookies.getCookie("JSESSIONID");
            webFormItem.setAntibot(webForm.getUseCatpcha() ? antibot : null);
            webFormsService.saveForm(webFormItem, new AsyncCallback<HashMap<String, String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                    if (errorOn != null) {
                        errorOn.execute();
                    }
                }

                @Override
                public void onSuccess(HashMap<String, String> result) {
                    LoadingPanel.loading(false);
                    if (result != null) {
                        boolean noError = false;
                        if (result.containsKey("ERROR_CAPTCHA")) {
                            Info.show(result.get("ERROR_CAPTCHA"), Info.Type.WARNING);
                        } else if (result.containsKey("ID")) {
                            if (addedSuccessfully != null) {
                                Cookies.setCookie(LEAD_ID_COOKIE, result.get("ID"));
                                addedSuccessfully.execute();
                            }
                            noError = true;
                        } else {
                            Info.show(wfmStrings.emailAlreadyExists(), Info.Type.WARNING);
                        }
                        if (!noError && errorOn != null) {
                            errorOn.execute();
                        } else {
                            if (addedSuccessfully != null) {
                                addedSuccessfully.execute();
                            }
                        }
                    } else {
                        if (addedSuccessfully != null) {
                            addedSuccessfully.execute();
                        }
                    }
                }
            });
        }
    }

    public Command getAddedSuccessfully() {
        return addedSuccessfully;
    }

    public void setAddedSuccessfully(Command addedSuccessfully) {
        this.addedSuccessfully = addedSuccessfully;
    }

    public Command getErrorOn() {
        return errorOn;
    }

    public void setErrorOn(Command errorOn) {
        this.errorOn = errorOn;
    }
}
