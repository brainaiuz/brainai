package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

public class CustomizableSystemFieldSideNav extends KpiSideNavBox {
    private TextBox name;
    private TextBox aliasName;
    private KpiSwitcher required;
    private KpiSwitcher disabled;
    private KpiSwitcher active;

    private final Integer customFieldId;
    private CompanyCustomFieldItem companyCustomFieldItem;


    public CustomizableSystemFieldSideNav(Integer customFieldId) {
        this.customFieldId = customFieldId;
        initialize();
        loadData();
    }

    private void loadData() {
        CommonService.App.get().getCompanyCustomFieldById(customFieldId, new AsyncCallback<CompanyCustomFieldItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(CompanyCustomFieldItem result) {
                companyCustomFieldItem = result;
                fillValues();
            }
        });
    }

    private void initialize() {
        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.properties());
        addHeader(header);

        FlowPanel flowPanel = new FlowPanel();
        name = new TextBox();
        flowPanel.add(new FormGroup(wfmStrings.name(), name));

        aliasName = new TextBox();
        aliasName.setEnabled(false);
        flowPanel.add(new FormGroup(wfmStrings.aliasName(), aliasName));

        required = new KpiSwitcher();
        disabled = new KpiSwitcher();
        active = new KpiSwitcher();

        FormGroup requiredGroup = new FormGroup(wfmStrings.required(), required);
        FormGroup disabledGroup = new FormGroup(wfmStrings.disabled(), disabled);
        FormGroup activeGroup = new FormGroup(wfmStrings.active(), active);

        GRow gRow = new GRow();
        gRow.add(new GColumn(GColumnEnum.COL_4, requiredGroup));
        gRow.add(new GColumn(GColumnEnum.COL_4, disabledGroup));
        gRow.add(new GColumn(GColumnEnum.COL_4, activeGroup));

        flowPanel.add(gRow);
        addBody(flowPanel);

        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addFooter(save);
        show();
    }

    private void save() {
        if (validate()) {
            companyCustomFieldItem.setFieldName(name.getText());
            companyCustomFieldItem.setAliasName(aliasName.getText());
            companyCustomFieldItem.setRequired(required.getValue());
            companyCustomFieldItem.setDisabled(disabled.getValue());
            companyCustomFieldItem.setActive(active.getValue());

            ProfileService.App.get().saveCustomFields(null, companyCustomFieldItem, false, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(Void unused) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.customField()));
                    remove();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FIELD_ADD, null, null);
                }
            });
        }
    }

    @Override
    public boolean validate() {
        if (!Validation.validateTextBoxRequired(name)) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void fillValues() {
        name.setText(companyCustomFieldItem.getFieldName());
        aliasName.setText(companyCustomFieldItem.getAliasName());
        required.setValue(companyCustomFieldItem.isRequired());
        disabled.setValue(companyCustomFieldItem.isDisabled());
        active.setValue(companyCustomFieldItem.isActive());
    }
}
