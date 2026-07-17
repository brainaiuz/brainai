package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationMultiLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.PAYROLL_ZONE_FORM;

public class AddPayrollZoneView extends CustomForm implements Constants, FittedContent, Colapse {
    private static final SettingStrings settingStrings = SettingStrings.App.get();
    private TextBox name;
    private LocationMultiLookUp location;
    private final Integer id;

    public AddPayrollZoneView(Integer id) {
        super("addPayrollZone", settingStrings.addPayrollZone());
        this.id = id;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        registerFields();
        return null;
    }

    private void registerFields() {
        name = new TextBox();
        location = new LocationMultiLookUp();

        addField(NAME, name, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.PROJECT.LOCATION, location, getTitle(wfmStrings.locations(), true));

        show();
    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        LoadingPanel.loading(true);
        ProfileService.App.get().savePayrollZone(getValues(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void unused) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.messSuccessfullySaved());
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYROLL_ZONE_ADD, null, AddPayrollZoneView.this);
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();

        errors += markAsError(NAME, name, !Validation.validateTextBoxRequired(name));
        errors += markAsError(CustomFormConstants.PROJECT.LOCATION, location, !Validation.validateMultiSelectRequired(location, null, wfmStrings.fillAllRequiredFields()));

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private SelectItem getValues() {
        SelectItem item = new SelectItem();
        item.setId(id);
        item.setName(name.getText());
        item.setRelatedItems(location.getSelectedItems().toArray(new SelectItem[]{}));

        return item;
    }

    @Override
    public String getIconStyle() {
        return "icon-edit";
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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

    @Override
    protected void addButtons() {
        addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> save()));
    }

    @Override
    protected void getDataToFillFields() {
        if (id != null) {
            ProfileService.App.get().getPayrollZone(id, new AsyncCallback<SelectItem>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(SelectItem result) {
                    name.setText(result.getName());
                    location.setSelectedItems(result.getRelatedItems());
                }
            });
        }
    }

    @Override
    protected String getFormID() {
        return PAYROLL_ZONE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }
}
