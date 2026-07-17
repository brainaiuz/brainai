package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class CustomFormLocalizationView extends CustomForm implements CustomFormConstants, Constants, FittedContent, Colapse {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private final String formID;
    private ArrayList<CustomFormLocalization> fields;
    private String localization = "custom_form_localization_";
    private HashMap<String, GRow> sections = new HashMap<>();
    private String firstPaddingLeft = "padding-left:20px;";
    private String secondPaddingLeft = "padding-left:40px;";
    private String thirdPaddingLeft = "padding-left:60px;";
    private CustomFormLocalizationTable table;
    private LinkedList<CustomFormLocalization> fieldItems = null;

    public CustomFormLocalizationView(String formID) {
        super("cutomFormLocalization", settingsStrings.customFormLocalization());
        this.formID = formID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        AllInOneService.App.get().getCFFLocalizations(formID, new AbstractAsyncCallback<ArrayList<CustomFormLocalization>>() { //CFF - Custom Form Fields
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ArrayList<CustomFormLocalization> result) {
                if (result != null) {
                    fields = result;
                    initialize();
                }
            }
        });

        return null;
    }

    private void initialize() {
        table = new CustomFormLocalizationTable(fields);
        addField(CustomFormConstants.CONTENT, table);
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, click -> save());
    }

    private void save() {
        enableButton(false);
        fieldItems = table.save();
        LoadingPanel.loading(true);
        AllInOneService.App.get().saveCFLItems(fieldItems, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), settingsStrings.customFormLocalization()), Info.Type.INFO);
                closeTab();
            }
        });
    }


    @Override
    protected void getDataToFillFields() {

    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CUSTOM_FORM_LOCALIZATION;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
