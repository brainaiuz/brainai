package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.CompanyOpportunitySettings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 6/3/11
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class CRMSettings extends CustomForm implements Constants, CustomFormConstants {
    private final ProfileServiceAsync profileService = ProfileService.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private KpiSwitcher isFillOpportunityItems;
    private KpiSwitcher requireContractUpload;
    private KpiSwitcher isJoinExpanseClaimToOpportunity;
    private KpiSwitcher emailAutoLinking;
    private KpiSwitcher crmAccountNumbering;

    private TextBox oPrefix;
    private TextBox oNumCell1;
    private TextBox oNumCell2;
    private TextBox oNumCell3;
    private TextBox oNumCell4;
    private Numbering prefix;
    private KpiRadioButton skip;
    private KpiRadioButton overwrite;
    private KpiRadioButton clone;
    private KpiRadioButton overwriteByEmail;
    private KpiRadioButton overwriteByPhone;
    private DataListBox contactType;
    private DataListBox relationBox;
    private KpiModal opportunitySettings;
    private Integer selectedStage;
    private Integer selectedSource;
    private ReferenceLookUp stage;
    private ReferenceLookUp source;

    private final String DEF_OPPORTUNITY_PREFIX = String.valueOf((new Date()).getYear()).substring(1, 3);

    public CRMSettings() {
        super("CRMSettings", settingsStrings.salesSettings());
    }

    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        isFillOpportunityItems = new KpiSwitcher();
        isFillOpportunityItems.ensureDebugId("isFillOpportunityItems");
        isFillOpportunityItems.getElement().getStyle().setLineHeight(20, Style.Unit.PX);
        requireContractUpload = new KpiSwitcher();
        requireContractUpload.ensureDebugId("requireContractUpload");
        isJoinExpanseClaimToOpportunity = new KpiSwitcher();
        isJoinExpanseClaimToOpportunity.ensureDebugId("isJoinExpanseClaimToOpportunity");
        emailAutoLinking = new KpiSwitcher();
        emailAutoLinking.ensureDebugId("emailAutoLinking");

        crmAccountNumbering = new KpiSwitcher();
        crmAccountNumbering.ensureDebugId("crmAccountNumbering");

        HorizontalPanel prefixPanel = new HorizontalPanel();
        prefixPanel.setSpacing(3);
        prefixPanel.addStyleName(DEFAULT_WIDTH);
        oPrefix = new TextBox();
        oPrefix.setWidth("80px");
        oPrefix.setText(DEF_OPPORTUNITY_PREFIX);
        oNumCell1 = createNumCell();
        oNumCell2 = createNumCell();
        oNumCell3 = createNumCell();
        oNumCell4 = createNumCell();
        prefixPanel.add(oPrefix);
        prefixPanel.add(oNumCell1);
        prefixPanel.add(oNumCell2);
        prefixPanel.add(oNumCell3);
        prefixPanel.add(oNumCell4);

        prefix = new Numbering(true);
        prefix.ensureDebugId("prefix");
        prefix.addStyleName(DEFAULT_WIDTH);
        prefix.getTxtNumber().setEnabled(false);
        prefix.getLastTxt().setVisible(false);

        skip = new KpiRadioButton("importPreferenceType", wfmStrings.skip());
        overwrite = new KpiRadioButton("importPreferenceType", wfmStrings.overwrite());
        clone = new KpiRadioButton("importPreferenceType", wfmStrings.clonE());

        skip.setValue(true);

        HorizontalPanel importTypePanel = new HorizontalPanel();
        importTypePanel.setSpacing(10);
        importTypePanel.add(skip);
        importTypePanel.add(overwrite);
        importTypePanel.add(clone);

        overwriteByEmail = new KpiRadioButton("overwritePreferenceType", wfmStrings.email());
        overwriteByPhone = new KpiRadioButton("overwritePreferenceType", wfmStrings.phone());

        overwriteByEmail.setValue(true);

        contactType = new DataListBox();
        contactType.setItems(getContactTypes());



        opportunitySettings = new KpiModal();
        opportunitySettings.addStyleName("attendance_report_modal");
        opportunitySettings.setTitle(wfmStrings.opportunity());
        opportunitySettings.setWidth(400);
        VerticalPanel vp = new VerticalPanel();
        stage = new ReferenceLookUp("_OPPORTUNITY_STAGE");
        source = new ReferenceLookUp("_LEAD_SOURCE");
        vp.add(stage);
        vp.add(source);
        vp.add(createLabeledWidget(stage, wfmStrings.stage()));
        vp.add(createLabeledWidget(source, wfmStrings.source()));
        WfmButton2 btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSave.addClickHandler(clickEvent -> {
            if (stage.getSelectedItem() != null && source.getSelectedItem() != null) {
                opportunitySettings.close();
                selectedSource = source.getSelectedItemID();
                selectedStage = stage.getSelectedItemID();
            } else {
                stage.addStyleName(ERROR_FORM_STYLE);
                source.addStyleName(ERROR_FORM_STYLE);
                Info.warn(wfmStrings.fillAllRequiredFields());
            }
        });
        WfmButton2 btnClose = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        btnClose.addClickHandler(clickEvent -> {
            opportunitySettings.close();
        });
        opportunitySettings.addButton(btnSave);
        opportunitySettings.addButton(btnClose);
        opportunitySettings.add(vp);

        relationBox = new DataListBox();
        relationBox.setItems(getRelations());
        relationBox.addValueChangeHandler( event -> {
        opportunitySettings.open();
        });


        HorizontalPanel overwriteTypePanel = new HorizontalPanel();
        overwriteTypePanel.setSpacing(10);
        overwriteTypePanel.add(overwriteByEmail);
        overwriteTypePanel.add(overwriteByPhone);

        addTitleField(OPPORTUNITY_SETTINGS, settingsStrings.opportunitySettings());
        addField(FILL_OPPORTUNITY_ITEMS, isFillOpportunityItems, getTitle(settingsStrings.fillOpportunityItems()));
        addField(REQUIRE_CONTRACT_UPLOAD, requireContractUpload, getTitle(settingsStrings.requireContractUpload()));
        addField(JOIN_OPPORTUNITY_TO_EXPENSE_CLAIM, isJoinExpanseClaimToOpportunity, getTitle(settingsStrings.relateOpportunityToExpenseClaim()));
        addField(EMAIL_AUTOLINKING, emailAutoLinking, getTitle(settingsStrings.emailAutolinking()));
        addField(IMPORT_PREFERENCE, importTypePanel, settingsStrings.importPreference());
        addField(OVERWRITE_PREFERENCE, overwriteTypePanel, settingsStrings.importOverwriteOptionBy());
        addTitleField(NUMBERING_SETTINGS, wfmStrings.numberingSettings());
        addField(OPPORTUNITY_NUMBERING, prefixPanel, getTitle(settingsStrings.opportunityNumbering()));
        addField(TRACKER_PREFIX, prefix, getTitle(settingsStrings.trackerPrefix()));
        addTitleField(SIPUNI_SETTINGS,"Call Center Settings");
        addField(CONTACT_TYPE,contactType,wfmStrings.type());
        addField(RELATIONSHIP,relationBox,wfmStrings.convertTo());

        addField(CRM_ACCOUNT_NUMBER, crmAccountNumbering, getTitle(settingsStrings.generatetCrmAccountNumbering()));

        show();
    }

    @Override
    protected void addButtons() {
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.getElement().setId("Sales_setting_save_button");
        saveButton.addClickHandler(sender -> save());
        addButton(saveButton);
    }

    private Widget createLabeledWidget(Widget widget, String labelText) {
        MaterialPanel panel = new MaterialPanel();
        panel.add(new MaterialLabel(labelText));
        panel.add(widget);
        return panel;
    }

    @Override
    protected void getDataToFillFields() {
        profileService.getCompanyOpportunitySettings(new AbstractAsyncCallback<CompanyOpportunitySettings>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(CompanyOpportunitySettings settings) {
                selectedStage = settings.getOpportunityStageId();
                isFillOpportunityItems.setValue(settings.isFillOpportunitItems());
                requireContractUpload.setValue(settings.isRequireContractUpload());
                isJoinExpanseClaimToOpportunity.setValue(settings.isJoinOpportunityToExpenseClaim());
                emailAutoLinking.setValue(settings.isEmailAutoLinking());
                crmAccountNumbering.setValue(settings.isGenerateCrmAccountNumber());
                source.setSelected(settings.getSource());
                stage.setSelected(settings.getStage());
                parseAndSetData(settings.getOpportunityNumberingSettings(), DEF_OPPORTUNITY_PREFIX, oPrefix, oNumCell1, oNumCell2, oNumCell3, oNumCell4);
                contactType.setSelected(settings.getContactTypeId() != null ? new SelectItem(settings.getContactTypeId(),getContactName(settings.getContactTypeId())): null);
                relationBox.setSelected(setRelations(settings.getConvertsTo()));
                if (!Utils.isNullOrEmpty(settings.getPrefix())) {
                    prefix.getTxtPrefix().setText(settings.getPrefix());
                }
                prefix.getTxtNumber().setText("00000");
                if (settings.getImportPreference() != null) {
                    if (settings.getImportPreference().equals("CLONE")) {
                        clone.setValue(true);
                    } else if (settings.getImportPreference().equals("OVERWRITE")) {
                        overwrite.setValue(true);
                    } else {
                        skip.setValue(true);
                    }
                }
                if (settings.getOverwritePreference() != null && settings.getOverwritePreference().equals("BY_PHONE")) {
                    overwriteByPhone.setValue(true);
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CRM_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    private void save() {
        CompanyOpportunitySettings opportunitySettings = new CompanyOpportunitySettings();
        opportunitySettings.setFillOpportunitItems(isFillOpportunityItems.getValue());
        opportunitySettings.setRequireContractUpload(requireContractUpload.getValue());
        opportunitySettings.setJoinOpportunityToExpenseClaim(isJoinExpanseClaimToOpportunity.getValue());
        opportunitySettings.setEmailAutoLinking(emailAutoLinking.getValue());
        opportunitySettings.setGenerateCrmAccountNumber(crmAccountNumbering.getValue());
        opportunitySettings.setOpportunityNumberingSettings(getOpportunityNumberingFormat());
        opportunitySettings.setContactType(contactType.getSelectedItem());
        opportunitySettings.setOpportunitySourceId(selectedSource);
        opportunitySettings.setOpportunityStageId(selectedStage);
        opportunitySettings.setConvertsTo(relationBox.getSelectedItem() != null ? relationBox.getSelectedItemText() : null);
        if (!Utils.isNullOrEmpty(prefix.getTxtPrefix().getText())) {
            String pref = prefix.getTxtPrefix().getText();
            pref = pref.endsWith("-") ? pref : pref + "-";
            opportunitySettings.setPrefix(pref);
        }
        if (clone.getValue()) {
            opportunitySettings.setImportPreference("CLONE");
        } else if (overwrite.getValue()) {
            opportunitySettings.setImportPreference("OVERWRITE");
        } else {
            opportunitySettings.setImportPreference("SKIP");
        }
        if (overwriteByPhone.getValue()) {
            opportunitySettings.setOverwritePreference("BY_PHONE");
        } else {
            opportunitySettings.setOverwritePreference("BY_EMAIL");
        }
        LoadingPanel.loading(true);
        profileService.updateCompanyOpportunitySettings(opportunitySettings, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void aVoid) {
                LoadingPanel.loading(false);
                Info.show(settingsStrings.saveOpportunitySettings(), Info.Type.INFO);
            }
        });
    }

    private TextBox createNumCell() {
        final TextBox cell = new TextBox();
        cell.setWidth("45px");
        cell.setMaxLength(1);
        Validation.addNumericKeyboardListener(cell);
        return cell;
    }

    private void parseAndSetData(String numFormat, String defaultPrefix, TextBox prefix, TextBox cell1, TextBox cell2, TextBox cell3, TextBox cell4) {
        if (!Utils.isNullOrEmpty(numFormat)) {
            int splitterIndex = numFormat.lastIndexOf("_");
            prefix.setText(numFormat.substring(0, splitterIndex));
            char[] numbers = numFormat.substring(splitterIndex + 1).toCharArray();
            cell1.setText(String.valueOf(numbers[0]));
            cell2.setText(String.valueOf(numbers[1]));
            cell3.setText(String.valueOf(numbers[2]));
            cell4.setText(String.valueOf(numbers[3]));
        } else {
            prefix.setText(defaultPrefix);
            cell1.setText("0");
            cell2.setText("0");
            cell3.setText("0");
            cell4.setText("1");
        }
    }

    private String getOpportunityNumberingFormat() {
        String buffer = oPrefix.getText().trim() + "_" +
                (oNumCell1.getText().trim().length() > 0 ? oNumCell1.getText().trim() : "0") +
                (oNumCell2.getText().trim().length() > 0 ? oNumCell2.getText().trim() : "0") +
                (oNumCell3.getText().trim().length() > 0 ? oNumCell3.getText().trim() : "0") +
                (oNumCell4.getText().trim().length() > 0 ? oNumCell4.getText().trim() : "1");
        return buffer;
    }

    private SelectItem[] getContactTypes(){
        ArrayList<SelectItem> contactTypes = new ArrayList<>();
        contactTypes.add(new SelectItem(CrmConstants.TYPE_CRM_CONTACT,wfmStrings.contact()));
        contactTypes.add(new SelectItem(CrmConstants.TYPE_CANDIDATE,wfmStrings.candidate()));
        contactTypes.add(new SelectItem(CrmConstants.TYPE_LEAD_CONTACT,wfmStrings.lead()));
        contactTypes.add(new SelectItem(CrmConstants.TYPE_ACCOUNT,wfmStrings.account()));
       return contactTypes.toArray(new SelectItem[]{});
    }

    private SelectItem[] getRelations(){
        ArrayList<SelectItem> relations = new ArrayList<>();
        relations.add(new SelectItem(1,"opportunity"));
        return relations.toArray(new SelectItem[]{});
    }

    private SelectItem setRelations(String relation){
        SelectItem relationSelectItem = null;
        if (relation != null ){
          switch (relation){
              case "opportunity":
                  relationSelectItem = new SelectItem(1,"opportunity");
                  break;
          }
        }
        return relationSelectItem;
    }

    private String getContactName(Integer contactId){
        String contactName = null;
        switch (contactId){
            case CrmConstants.TYPE_CRM_CONTACT:
                contactName = wfmStrings.contact();
                break;
            case CrmConstants.TYPE_CANDIDATE:
                contactName = wfmStrings.candidate();
                break;
            case CrmConstants.TYPE_LEAD_CONTACT:
                contactName = wfmStrings.lead();
                break;

            case CrmConstants.TYPE_ACCOUNT:
                contactName = wfmStrings.account();
                break;
        }
        return contactName;
    }

    @Override
    public String getIconStyle() {
        return "icon-settings-user-credentials";
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
