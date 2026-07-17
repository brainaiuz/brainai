package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

/**
 * User: unni
 * Date: Oct 21, 2009
 * Time: 10:03:57 PM
 */
public class DependentSummaryView extends AddDependentView {

    private final Integer int_objectID;
    private HTML dependent_first_name, dependent_middle_name, dependent_last_name, dependent_relationship, dependent_address_1, dependent_address_2,
            dependent_city, dependent_country, dependent_phone_1;

    private final String test_code_ID_name = "summary_dependent_view_";
    private GeneralFileUpload fileUpload;

    public DependentSummaryView(Integer int_objectID) {
        super("summary", hrmsStrings.dependentView(), "summary_dependent_view_", int_objectID);
        this.int_objectID = int_objectID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
        if (Utils.hasRole(Constants.ADMIN)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
            });
            options.add(customize);
        }
        //delete button
        if (Utils.hasPermission(PermissionConstants.HRMS_DEPENDENT_REMOVE)) {
            MaterialLink deleteButton = new MaterialLink(wfmStrings.delete());
            deleteButton.addClickHandler(event -> {
                //register delete logic
                final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                message.setTitle(wfmStrings.warning());
                message.setMessage(wfmStrings.sureYouWantToDelete());
                message.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        HrmsService.App.get().deleteDependent(item.getObjectId(), new AbstractAsyncCallback<Void>() {
                            public void failure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            public void success(Void result) {
                                LoadingPanel.loading(false);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DEPENDENT_DELETE, result, DependentSummaryView.this);
                                closeTab();
                                Info.show(hrmsStrings.DependentHasBeenDeleted(), Info.Type.INFO);
                            }
                        });
                    }
                });
                message.open();
            });
            options.add(deleteButton);
        }
        //edit button
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_DEPENDENT)) {
            WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
            editButton.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("dependent|edit/" + int_objectID, item.getFirstName(), item.getLastName()));
            addButton(editButton);
        }
    }


    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        HrmsService.App.get().getDependent(int_objectID, new AbstractAsyncCallback<DependentItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(DependentItem object) {
                LoadingPanel.loading(false);
                item = object;
                fillFormWithData();
            }
        });
    }

    @Override
    protected String getFormID() {
        return super.getFormID();
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected void fillFormWithData() {
        //dependent first name
        dependent_first_name.setHTML(item.getFirstName() != null ? item.getFirstName() : "");
        //dependent middle name
        dependent_middle_name.setHTML(item.getMiddleName() != null ? item.getMiddleName() : "");
        //dependent last name
        dependent_last_name.setHTML(item.getLastName() != null ? item.getLastName() : "");
        //dependent relationship
        dependent_relationship.setHTML(item.getRelationship() != null ? item.getRelationship() : "");
        //dependent address 1
        dependent_address_1.setHTML(item.getAddress() != null ? item.getAddress() : "");
        //dependent address 2
        dependent_address_2.setHTML(item.getAddressb() != null ? item.getAddressb() : "");
        //dependent city
        dependent_city.setHTML(item.getCity() != null ? item.getCity() : "");
        //dependent country name
        dependent_country.setHTML(item.getCountryName() != null ? item.getCountryName() : "");
        //dependent phone 1
        dependent_phone_1.setHTML(item.getPhone1() != null ? new PhoneNumber(item.getPhone1()).toString() : "");
        //dependent custom fields
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields(), true);
    }

    @Override
    protected void registerFields() {
        //dependent first name
        dependent_first_name = new HTML();
        dependent_first_name.addStyleName(DEFAULT_WIDTH);
        dependent_first_name.ensureDebugId(test_code_ID_name + "first_name");
        //dependent middle name
        dependent_middle_name = new HTML();
        dependent_middle_name.addStyleName(DEFAULT_WIDTH);
        dependent_middle_name.ensureDebugId(test_code_ID_name + "middle_name");
        //dependent last name
        dependent_last_name = new HTML();
        dependent_last_name.addStyleName(DEFAULT_WIDTH);
        dependent_last_name.ensureDebugId(test_code_ID_name + "last_name");
        //dependent relationship
        dependent_relationship = new HTML();
        dependent_relationship.addStyleName(DEFAULT_WIDTH);
        dependent_relationship.ensureDebugId(test_code_ID_name + "relationship");
        //dependent address 1
        dependent_address_1 = new HTML();
        dependent_address_1.addStyleName(DEFAULT_WIDTH);
        dependent_address_1.ensureDebugId(test_code_ID_name + "address_1");
        //dependent address 2
        dependent_address_2 = new HTML();
        dependent_address_2.addStyleName(DEFAULT_WIDTH);
        dependent_address_2.ensureDebugId(test_code_ID_name + "address_2");
        //dependent city
        dependent_city = new HTML();
        dependent_city.addStyleName(DEFAULT_WIDTH);
        dependent_city.ensureDebugId(test_code_ID_name + "city");
        //dependent country
        dependent_country = new HTML();
        dependent_country.addStyleName(DEFAULT_WIDTH);
        dependent_country.ensureDebugId(test_code_ID_name + "country");
        //dependent phone 1
        dependent_phone_1 = new HTML();
        dependent_phone_1.addStyleName(DEFAULT_WIDTH);
        dependent_phone_1.ensureDebugId(test_code_ID_name + "phone_1");

        fileUpload = new GeneralFileUpload(F_DEPENDENTS, int_objectID, int_objectID);
        initializeForms();
    }

    @Override
    protected void initializeForms() {
        //add field items
        //dependent details -> 1
        addTitleField(CustomFormConstants.DETAILS, wfmStrings.details());
        addField(CustomFormConstants.FIRST_NAME, dependent_first_name, getTitle(wfmStrings.firstName()));
        addField(CustomFormConstants.MIDDLE_NAME, dependent_middle_name, getTitle(wfmStrings.middleName()));
        addField(CustomFormConstants.LAST_NAME, dependent_last_name, getTitle(wfmStrings.lastName()));

        addField(CustomFormConstants.RELATIONSHIP, dependent_relationship, getTitle(wfmStrings.relationship()));
        addField(CustomFormConstants.ADDRESS, dependent_address_1, getTitle(wfmStrings.streetAddress1()));
        addField(CustomFormConstants.ADDRESS_2, dependent_address_2, getTitle(wfmStrings.streetAddress2()));

        addField(CustomFormConstants.CITY_, dependent_city, getTitle(wfmStrings.city()));
        addField(CustomFormConstants.COUNTRY_, dependent_country, getTitle(wfmStrings.country()));

        addField(CustomFormConstants.PHONE, dependent_phone_1, getTitle(wfmStrings.phone()));

        //custom fields -> 2
        getCustomFieldUtil().drawCustomFields(this, int_objectID, true);
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

        addField(CustomFormConstants.ATTACHMENTS, fileUpload, wfmStrings.attachments());

        show();
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

}