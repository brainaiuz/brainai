package com.edatasite.workforce.gwt.hrms.client.ui.talentprofile;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.Optional;

/**
 * User: unni
 * Date: Dec 2, 2009
 * Time: 3:12:24 PM
 */
public class AwardSummaryView extends CustomForm implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final TalentProfileServiceAsync talentProfileService = TalentProfileService.App.get();

    private final Integer objectID;
    private AwardItem item;

    private HTML awardName, issueDate, expiryDate, country, city;
    private TextArea2 description;

    private final String ensureDebugId = "award_summary_form_";

    public AwardSummaryView(Integer objectID) {
        super("summary", wfmStrings.summaryView());
        this.objectID = objectID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.edit(), null, ensureDebugId.concat("edit_button"), event -> SinksContainerFactory.entryPoint.onHistoryChanged(TalentProfileEnum.AWARD.name().toLowerCase().concat("|add/education/" + objectID)));
        addRemoveButton().addClickHandler(event -> {
            WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.warning());
            messageBox.setMessage(wfmStrings.sureYouWantToDelete() );
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    talentProfileService.deleteTalentProfileItem(objectID, TalentProfileEnum.AWARD, new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Boolean result) {
                            LoadingPanel.loading(false);
                            if (result) {
                                closeTab();
                                Info.show(property.getPlural(wfmStrings.messSuccessfulyyDeleted(), hrmsStrings.awards()), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TALENT_PROFILE_CHANGE, result, AwardSummaryView.this);
                            } else {
                                Info.show(wfmStrings.errorOccuredWhileDeleting(), Info.Type.INFO);
                            }
                        }
                    });
                }
            });
            messageBox.open();
        });
    }

    @Override
    protected void getDataToFillFields() {
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

    @Override
    protected String getFormID() {
        return LayoutRPC.AWARD_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
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

    private void fillFormWithData() {
        Optional.ofNullable(item.getCountry()).ifPresent(countryValue -> country.setHTML(item.getCountry().getName()));
        awardName.setHTML(Optional.ofNullable(item.getAwardName()).orElse(""));
        city.setHTML(Optional.ofNullable(item.getAwardName()).orElse(""));
        description.setText(Optional.ofNullable(item.getDescription()).orElse(""));

        Optional.ofNullable(item.getIssueDate()).ifPresent(issueDateValue -> issueDate.setHTML(DateUtils.format(item.getExpiryDate())));
        Optional.ofNullable(item.getExpiryDate()).ifPresent(expiryDateValue -> expiryDate.setHTML(DateUtils.format(item.getExpiryDate())));
    }

    private void initialize() {
        country = new HTML();
        country.addStyleName(DEFAULT_WIDTH);
        country.ensureDebugId(ensureDebugId.concat("country"));

        awardName = new HTML();
        awardName.addStyleName(DEFAULT_WIDTH);
        awardName.ensureDebugId(ensureDebugId.concat("awardName"));

        city = new HTML();
        city.addStyleName(DEFAULT_WIDTH);
        city.ensureDebugId(ensureDebugId.concat("city"));

        issueDate = new HTML();
        issueDate.addStyleName(DEFAULT_WIDTH);
        issueDate.ensureDebugId(ensureDebugId.concat("issueDate"));

        expiryDate = new HTML();
        expiryDate.addStyleName(DEFAULT_WIDTH);
        expiryDate.ensureDebugId(ensureDebugId.concat("expiryDate"));

        description = new TextArea2();
        description.setHeight("6em");
        description.setReadOnly(true);
        description.hideCharacterLimitPanel();
//        description.addStyleName(DEFAULT_WIDTH);
        description.ensureDebugId(ensureDebugId.concat("description"));

        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.awardDetails());
        addField(CustomFormConstants.NAME, awardName, getTitle(hrmsStrings.awardName()));
        addField(CustomFormConstants.DESCRIPTION, description, getTitle(wfmStrings.description()));
        addField(CustomFormConstants.START_DATE, issueDate, getTitle(hrmsStrings.issueDate()));
        addField(CustomFormConstants.EXPIRATION_DATE, expiryDate, getTitle(wfmStrings.expiryDate()));
        addField(CustomFormConstants.COUNTRY_, country, getTitle(wfmStrings.country()));
        addField(CustomFormConstants.CITY_, city, getTitle(wfmStrings.city()));

        show();
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