package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCServiceAsync;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.AddEditCourseBookingItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 11/08/12
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class AddEditCourseBookingFormView extends CustomForm2 implements TCConstants, Colapse, Constants {

    protected static TCServiceAsync tcService = TCService.App.get();
    protected static TCStrings tcStrings = TCStrings.App.get();


    protected Integer objectID;

    private DataListBox dwBookingType;
    private CrmAccountLookUp customerLookUp;
    private TextBox companyNumbering;
    private TextBox emailBox;
    private PhoneNumber phoneNumberBox;
    private PhoneNumber faxNumberBox;
    private DataListBox locationBox;
    private KpiCheckBox pdoCompanyCheckBox;
    private Map<String, String> typeMap;

    private AddEditCourseBookingItem courseBookingItem;
    private SelectItem[] cashTypes;
    FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public AddEditCourseBookingFormView() {
        super("add", tcStrings.addCourseBooking());
    }

    public AddEditCourseBookingFormView(Integer objectID) {
        super("edit", tcStrings.editCourseBooking());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.CourseBooking,getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddEditCourseBookingFormView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        initialization();
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }


    private void initialization() {
        // COMPANY INFO
        customerLookUp = new CrmAccountLookUp(CrmAccountItem.CUSTOMER, true);
        customerLookUp.addStyleName(DEFAULT_WIDTH);

        dwBookingType = new DataListBox();
        dwBookingType.setWithoutNullLabel(true);
        dwBookingType.addStyleName(DEFAULT_WIDTH);

        companyNumbering = new TextBox();
        companyNumbering.addStyleName(DEFAULT_WIDTH);

        phoneNumberBox = new PhoneNumber("");
        phoneNumberBox.setEnabled(false);
        phoneNumberBox.addStyleName(DEFAULT_WIDTH);

        faxNumberBox = new PhoneNumber("");
        faxNumberBox.setEnabled(false);
        faxNumberBox.addStyleName(DEFAULT_WIDTH);

        emailBox = new TextBox();
        emailBox.setEnabled(false);
        emailBox.addStyleName(DEFAULT_WIDTH);

        //LOCATION
        locationBox = new DataListBox();
        locationBox.addStyleName(DEFAULT_WIDTH);

        pdoCompanyCheckBox = new KpiCheckBox();

        getCustomFieldUtil().drawCustomFields(this, objectID, false);

        addWidgetsToForm();
        addWidgetsEventListener();
    }


    public void setCustomerOtherFieldsEnabled(boolean enabled) {
        phoneNumberBox.setEnabled(enabled);
        faxNumberBox.setEnabled(enabled);
        emailBox.setEnabled(enabled);

    }

    private void addWidgetsToForm() {
        //1
        addTitleField(COURSE_BOOKING.CUSTOMER_DETAILS, tcStrings.courseBookingDetails());
        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME) != null) {
            addField(COURSE_BOOKING.COMPANY_NAME, customerLookUp, getTitle(formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).isChanged() ? formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).getTitle() : wfmStrings.companyName(), formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).isInformation());
            customerLookUp.setEnabled(!formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).isDisabled());
            if (formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).isInformation()) {
                new KpiToolTip(customerLookUp, formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.COMPANY_NAME, customerLookUp, getTitle(wfmStrings.name(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER) != null) {
            addField(COURSE_BOOKING.COMPANY_NUMBER, companyNumbering, getTitle(formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).isChanged() ? formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).getTitle() : wfmStrings.companyNumber(), formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).isInformation());
            companyNumbering.setEnabled(!formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).isDisabled());
            if (formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).isInformation()) {
                new KpiToolTip(companyNumbering, formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.COMPANY_NUMBER, companyNumbering, getTitle(wfmStrings.companyNumber(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER) != null) {
            addField(COURSE_BOOKING.PHONE_NUMBER, phoneNumberBox.getField(), getTitle(formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).isChanged() ? formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).getTitle() : wfmStrings.phone(), formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).isInformation());
            phoneNumberBox.getField().setEnabled(!formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).isDisabled());
            if (formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).isInformation()) {
                new KpiToolTip(phoneNumberBox.getField(), formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.PHONE_NUMBER, phoneNumberBox.getField(), getTitle(wfmStrings.phone(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER) != null) {
            addField(COURSE_BOOKING.FAX_NUMBER, faxNumberBox.getField(), getTitle(formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).isChanged() ? formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).getTitle() : wfmStrings.fax(), formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).isInformation());
            faxNumberBox.getField().setEnabled(!formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).isDisabled());
            if (formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).isInformation()) {
                new KpiToolTip(faxNumberBox.getField(), formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.FAX_NUMBER, faxNumberBox.getField(), getTitle(wfmStrings.fax(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL) != null) {
            addField(COURSE_BOOKING.CUSTOMER_EMAIL, emailBox, getTitle(formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).isChanged() ? formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).getTitle() : wfmStrings.email(), formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).isInformation());
            emailBox.setEnabled(!formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).isDisabled());
            if (formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).isInformation()) {
                new KpiToolTip(emailBox, formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.CUSTOMER_EMAIL, emailBox, getTitle(wfmStrings.email(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE) != null) {
            addField(COURSE_BOOKING.TRAINING_VENUE, locationBox, getTitle(formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).isChanged() ? formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).getTitle() : wfmStrings.traningVenue(), formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).isInformation());
            locationBox.setEnabled(!formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).isDisabled());
            if (formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).isInformation()) {
                new KpiToolTip(locationBox, formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.TRAINING_VENUE, locationBox, getTitle(wfmStrings.traningVenue(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.TYPE) != null) {
            addField(COURSE_BOOKING.TYPE, dwBookingType, getTitle(formPropertyMap.get(COURSE_BOOKING.TYPE).isChanged() ? formPropertyMap.get(COURSE_BOOKING.TYPE).getTitle() : wfmStrings.type(), formPropertyMap.get(COURSE_BOOKING.TYPE).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.TYPE).isInformation());
            dwBookingType.setEnabled(!formPropertyMap.get(COURSE_BOOKING.TYPE).isDisabled());
            if (formPropertyMap.get(COURSE_BOOKING.TYPE).isInformation()) {
                new KpiToolTip(dwBookingType, formPropertyMap.get(COURSE_BOOKING.TYPE).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.TYPE, dwBookingType, getTitle(wfmStrings.type(), false));
        }
//        addField(COURSE_BOOKING.COMPANY_NUMBER, companyNumbering, getTitle(wfmStrings.companyNumber()));
//        addField(COURSE_BOOKING.COMPANY_NAME, customerLookUp, getTitle(wfmStrings.companyName(), true));
//        addField(COURSE_BOOKING.PHONE_NUMBER, phoneNumberBox.getField(), getTitle(wfmStrings.phone()));
//        addField(COURSE_BOOKING.FAX_NUMBER, faxNumberBox.getField(), getTitle(wfmStrings.fax()));
//        addField(COURSE_BOOKING.CUSTOMER_EMAIL, emailBox, getTitle(wfmStrings.email()));
//        addField(COURSE_BOOKING.TRAINING_VENUE, locationBox, getTitle(wfmStrings.traningVenue(), true));
//        addField(COURSE_BOOKING.TYPE, dwBookingType, getTitle(wfmStrings.type(), false));


    }

    private void addWidgetsEventListener() {
        // Company Number search By Registration number
        companyNumbering.addChangeHandler(changeEvent -> {
            if (companyNumbering.getText() != null && !"".equals(companyNumbering.getText())) {
                LoadingPanel.loading(true);
                tcService.getCustomerByRegistrationNumber(companyNumbering.getText(), new AsyncCallback<CrmAccountItem>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(CrmAccountItem crmAccountItem) {
                        LoadingPanel.loading(false);
                        if (crmAccountItem != null) {
                            customerLookUp.setSelected(new SelectItem(crmAccountItem.getObjectId(), crmAccountItem.getName()));
                            fillCustomerData(crmAccountItem);
                        } else {
                            phoneNumberBox.clearPhoneData();
                            faxNumberBox.clearPhoneData();
                            emailBox.setText("");
                            phoneNumberBox.setEnabled(true);
                            faxNumberBox.setEnabled(true);
                            emailBox.setEnabled(true);
                        }
                    }
                });
            }
        });

        customerLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> customerChangeOrSelectionHandler());

        customerLookUp.getSuggestBox().addValueChangeHandler(stringValueChangeEvent -> customerChangeOrSelectionHandler());
    }

    private void customerChangeOrSelectionHandler() {
        if (customerLookUp.getSelectedItemID() != null) {
            customerAllFiledsEnabled(true);
            LoadingPanel.loading(true);
            tcService.getCustomerData(customerLookUp.getSelectedItemID(), new AsyncCallback<CrmAccountItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(CrmAccountItem result) {
                    LoadingPanel.loading(false);
                    clearCustomerOldSelectData();
                    fillCustomerData(result);
                }
            });
        } /*else {
            customerAllFiledsEnabled(true);
        }*/
    }


    private void customerAllFiledsEnabled(boolean enabled) {
        companyNumbering.setEnabled(enabled);
        phoneNumberBox.setEnabled(enabled);
        faxNumberBox.setEnabled(enabled);
        emailBox.setEnabled(enabled);
    }

    private void clearCustomerOldSelectData() {
        companyNumbering.setText("");
        phoneNumberBox.clearPhoneData();
        faxNumberBox.clearPhoneData();
        emailBox.setText("");
    }


    private void fillCustomerData(CrmAccountItem result) {
        companyNumbering.setText(result.getRegistrationNumber());
        phoneNumberBox.setData(result.getPhone());
        faxNumberBox.setData(result.getFax());
        emailBox.setText(result.getEmail());
        if (result.getPaymentMethod().equalsIgnoreCase("cash")) {
            dwBookingType.clear();
            dwBookingType.setItems(cashTypes);
            dwBookingType.setSelectedByValue(courseBookingItem.getTypeMap().get(BOOKING_PAY_UPON_ARRIVAL));
        } else {
            dwBookingType.clear();
            dwBookingType.setItems(courseBookingItem.getTypeList());
            dwBookingType.setSelectedByValue(courseBookingItem.getTypeMap().get(BOOKING_BY_APPROVAL));
        }

        phoneNumberBox.setEnabled(false);
        faxNumberBox.setEnabled(false);
        emailBox.setEnabled(false);
    }


    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        tcService.getCourseBookingAddEditData(objectID, new AsyncCallback<AddEditCourseBookingItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(AddEditCourseBookingItem result) {
                LoadingPanel.loading(false);
                courseBookingItem = result;


                //this changes for cash customers
                cashTypes = new SelectItem[2];
                int index = 0;
                for (SelectItem type : courseBookingItem.getTypeList()) {
                    if (BOOKING_PAY_UPON_ARRIVAL.equals(type.getDescription()) || BOOKING_PAY_ONLINE.equals(type.getDescription())) {
                        cashTypes[index++] = type;
                    }
                }

                fillCourseBooking(courseBookingItem);
            }
        });
    }

    private void fillCourseBooking(AddEditCourseBookingItem result) {
        locationBox.setItems(result.getLocationItems());
        dwBookingType.setItems(result.getTypeList());
        getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems());
    }

    @Override
    protected void addButtons() {
        WfmButton2 save = new WfmButton2(wfmStrings.saveNext(), WfmButton2.BTN_PRIMARY);
        save.addClickHandler(event -> saveCourseBooking());
        addButton(save);
    }

    private void saveCourseBooking() {
        if (!validation()) {
            return;
        }
        LoadingPanel.loading(true);
        CourseBookingItem courseBookingData = getSelectCoursebookingItem();
        tcService.saveCourseBooking(courseBookingData, new AsyncCallback<Integer[]>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(Integer[] courseBookingId) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), tcStrings.courseBooking()));
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COURSE_BOOKING_ADD_EDIT, courseBookingId, AddEditCourseBookingFormView.this);
                closeTab();
                if (courseBookingId != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(TC_COURSE_BOOKING + "|" + TCConstants.TC_ADD_STUDENT_COURSE_BOOKING + "/" + courseBookingId[0]+ "/" + courseBookingId[1]);
                }
            }
        });
    }

    private CourseBookingItem getSelectCoursebookingItem() {
        CourseBookingItem courseBookingItem = new CourseBookingItem();
        courseBookingItem.setObjectID(objectID);
        courseBookingItem.setStatusCode(BOOKING_DRAFT);
        courseBookingItem.setTypeID(dwBookingType.getSelectedId());
        courseBookingItem.setPDOCustomer(pdoCompanyCheckBox.getValue());
        if (locationBox.getSelectedId() != null) {
            courseBookingItem.setLocation(locationBox.getSelectedItem());
        }

        if (customerLookUp.getSelectedItemID() != null) {
            courseBookingItem.setCustomer(customerLookUp.getSelectedItem());
        } else {
            CrmAccountItem customerItem = new CrmAccountItem();
            customerItem.setRegistrationNumber(companyNumbering.getText());
            customerItem.setName(customerLookUp.getText());
            customerItem.setPhone(phoneNumberBox.toString());
            customerItem.setFax(faxNumberBox.toString());
            customerItem.setEmail(emailBox.getText());
            courseBookingItem.setCustomerItems(customerItem);
        }
        courseBookingItem.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());

        return courseBookingItem;
    }

    private boolean validation() {
        int validateSucces = 0;
        clearErrorStyle();

        validateSucces += markAsError(COURSE_BOOKING.COMPANY_NAME, customerLookUp, !Validation.validateLookUpRequired(customerLookUp));
        validateSucces += markAsError(COURSE_BOOKING.TRAINING_VENUE, locationBox, !Validation.validateListBoxRequired(locationBox, (HTML) null, ""));
        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL) != null && formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).isRequired()) {
            validateSucces += markAsError(COURSE_BOOKING.CUSTOMER_EMAIL, emailBox, !Validation.validateTextBoxRequired(emailBox));
        }
        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER) != null && formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).isRequired()) {
            validateSucces += markAsError(COURSE_BOOKING.PHONE_NUMBER, phoneNumberBox, !phoneNumberBox.getField().validate());
        }
        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.TYPE) != null && formPropertyMap.get(COURSE_BOOKING.TYPE).isRequired()) {
            validateSucces += markAsError(COURSE_BOOKING.TYPE, dwBookingType, !Validation.validateListBoxRequired(dwBookingType));
        }
        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER) != null && formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).isRequired()) {
            validateSucces += markAsError(COURSE_BOOKING.FAX_NUMBER, faxNumberBox, !faxNumberBox.validate());
        }

        validateSucces += getCustomFieldUtil().validateCustomFields();

        if (validateSucces > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.COURSE_BOOKING_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }


}
