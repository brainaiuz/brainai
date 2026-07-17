package com.edatasite.workforce.gwt.hrms.client.ui.talentprofile;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CountryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.AwardItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileEnum;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileService;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Optional;


/**
 * User: unni
 * Date: Dec 2, 2009
 * Time: 3:12:09 PM
 */

public class AwardAddView extends CustomForm implements Constants, Colapse {

    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    protected static final TalentProfileServiceAsync talentProfileService = TalentProfileService.App.get();

    private Integer objectID;
    private Integer employeeID;
    private AwardItem item;

    private TextBox awardName;
    private DatePicker issueDate;
    private DatePicker expiryDate;
    private TextArea2 description;
    private CountryLookUp country;
    private TextBox city;

    private final String ensureDebugId = "award_add_edit_form_";

    public AwardAddView(Integer employeeID) {
        super("add", hrmsStrings.addAward());
        this.employeeID = employeeID;
        item = new AwardItem();
    }

    public AwardAddView(String name, String description, Integer objectID) {
        super(name, description);
        this.objectID = objectID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        addButton(objectID == null ? wfmStrings.save() : wfmStrings.update(), null, ensureDebugId.concat("save_and_close_button"), event -> save());
    }

    @Override
    protected void getDataToFillFields() {
        if (objectID != null) {
            LoadingPanel.loading(true);
            talentProfileService.getAward(objectID, new AbstractAsyncCallback<AwardItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(AwardItem object) {
                    LoadingPanel.loading(false);
                    item = object;
                    fillFormWithData();
                }
            });
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.AWARD_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    protected void fillFormWithData() {
        Optional.ofNullable(item.getCountry()).ifPresent(selectedCountry -> country.setSelected(selectedCountry));
        awardName.setText(Optional.ofNullable(item.getAwardName()).orElse(""));
        city.setText(Optional.ofNullable(item.getCity()).orElse(""));
        Optional.ofNullable(item.getIssueDate()).ifPresent(issueDateValue -> issueDate.setDate(issueDateValue.getNonConvertedDate()));
        Optional.ofNullable(item.getExpiryDate()).ifPresent(expiryDateValue -> expiryDate.setDate(expiryDateValue.getNonConvertedDate()));
        description.setText(Optional.ofNullable(item.getDescription()).orElse(""));
    }

    protected void initialize() {
        country = new CountryLookUp();
        country.addStyleName(DEFAULT_WIDTH);
        country.ensureDebugId(ensureDebugId.concat("country"));

        city = new TextBox();
        city.addStyleName(DEFAULT_WIDTH);
        city.ensureDebugId(ensureDebugId.concat("city"));

        awardName = new TextBox();
        awardName.addStyleName(DEFAULT_WIDTH);
        awardName.ensureDebugId(ensureDebugId.concat("awardName"));

        issueDate = new DatePicker();
        issueDate.addStyleName(DEFAULT_WIDTH);
        issueDate.ensureDebugId(ensureDebugId.concat("issueDate"));

        expiryDate = new DatePicker();
        expiryDate.addStyleName(DEFAULT_WIDTH);
        expiryDate.ensureDebugId(ensureDebugId.concat("expiryDate"));

        description = new TextArea2(wfmStrings.description());
        description.setHeight("12em");
        description.setWidth(MAX_DEFAULT_WIDTH);
        description.addStyleName(DEFAULT_WIDTH);
        description.ensureDebugId(ensureDebugId.concat("description"));

        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.awardDetails());
        addField(CustomFormConstants.NAME, awardName, getTitle(hrmsStrings.awardName(), true));
        addField(CustomFormConstants.DESCRIPTION, description, null);
        addField(CustomFormConstants.START_DATE, issueDate, getTitle(hrmsStrings.issueDate()));
        addField(CustomFormConstants.EXPIRATION_DATE, expiryDate, getTitle(wfmStrings.expiryDate()));
        addField(CustomFormConstants.COUNTRY_, country, getTitle(wfmStrings.country()));
        addField(CustomFormConstants.CITY_, city, getTitle(wfmStrings.city()));

        show();
    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        setValues();
        LoadingPanel.loading(true);
        talentProfileService.saveAward(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void o) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), hrmsStrings.awards()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TALENT_PROFILE_CHANGE, o, AwardAddView.this);
                closeTab();
                if (objectID != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(TalentProfileEnum.AWARD.name().toLowerCase() + "|summary/" + objectID);
                }
            }
        });
    }

    private void setValues() {
        item.setObjectId(objectID);
        if (employeeID != null) {
            item.setEmployeeId(employeeID);
        }
        item.setAwardName(awardName.getText());
        if (issueDate.getDate() != null) {
            item.setIssueDate(new DateNonConvertable(issueDate.getDate()));
        } else {
            item.setIssueDate(null);
        }
        if (expiryDate.getDate() != null) {
            item.setExpiryDate(new DateNonConvertable(expiryDate.getDate()));
        } else {
            item.setExpiryDate(null);
        }
        item.setCountry(country.getSelectedItem());
        item.setCity(city.getText());
        item.setDescription(description.getText());
    }

    private boolean validate() {
        clearErrorStyle();
        int errors = 0;
        errors += markAsError(CustomFormConstants.NAME, awardName, !Validation.validateTextBoxRequired(awardName));

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
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
