package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSettings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 12/12/12
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddSmsSettingView extends CustomForm implements Colapse {
    private static final ProfileServiceAsync profileService = ProfileService.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final String FIELD_KEY = "Key";
    private static final String FIELD_VALUE = "Value";
    private static final String[] OUR_SMS_ERROR_CODES = {"101", "102", "103", "104", "111", "113", "114", "115", "116", "117"};
    private static final Set<String> OUR_SMS_ERROR_CODES_SET = new HashSet<>(Arrays.asList(OUR_SMS_ERROR_CODES));

    private Integer objectID;
    private SmsSettings item;
    private TextBox name;
    private DataListBox providers;
    private EditableTable keyValues;
    private EditableGrid grid;
    private HTML creditLeft;
    private VerticalPanel keyValuePanel;
    private HorizontalPanel creditLeftPanel;
    private WfmButton2 checkBox;

    public AddSmsSettingView(Integer objectID) {
        super("smsSettings", settingsStrings.addSMSAccount());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        name = new TextBox();
        name.addStyleName(Constants.DEFAULT_WIDTH);
        providers = new DataListBox();
        providers.addStyleName(Constants.DEFAULT_WIDTH);
        providers.setItems(SmsSettings.PROVIDERS);
        keyValues = new EditableTable(getKeyValuesColumnConfig(), false);
        grid = keyValues.getGrid();
        creditLeft = new HTML();
        creditLeftPanel = new HorizontalPanel();
        creditLeftPanel.setSpacing(5);
        checkBox = new WfmButton2(wfmStrings.checkBalance());
        checkBox.addClickHandler(clickEvent -> creditLeft());
        creditLeftPanel.add(checkBox);
        creditLeftPanel.add(creditLeft);
        creditLeftPanel.setCellVerticalAlignment(checkBox, HasVerticalAlignment.ALIGN_TOP);
        creditLeftPanel.setCellVerticalAlignment(creditLeft, HasVerticalAlignment.ALIGN_TOP);
        creditLeftPanel.setVisible(false);
        keyValuePanel = new VerticalPanel();
        keyValuePanel.setSpacing(5);
        keyValuePanel.add(keyValues);
        keyValuePanel.add(creditLeftPanel);
        providers.addValueChangeHandler(event -> {
            LoadingPanel.loading(true);
            onProviderChange();
        });
        addFields();
    }

    private void addFields() {
        addTitleField(BACKEND.SMS.PROVIDER_INFORMATION, settingsStrings.providerInformation());
        addField(BACKEND.SMS.PROVIDER_NAME, providers, wfmStrings.provider() + "<em class=\"redTitle\">*</em>:");
        addField(BACKEND.SMS.NAME, name, wfmStrings.name() + "<em class=\"redTitle\">*</em>:");
        addField(BACKEND.SMS.KEY_VALUES, keyValuePanel, settingsStrings.requiredFields());
        show();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), (ClickHandler) event -> save());
    }

    @Override
    protected void getDataToFillFields() {
        profileService.getSmsSetting(objectID, new AsyncCallback<SmsSettings>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SmsSettings result) {
                item = result;
                fillWithData();
            }
        });
    }

    private void fillWithData() {
        name.setText(item.getName());
        providers.setSelected(item.getProviderID());
        creditLeft.setHTML("");
        creditLeftPanel.setVisible(providers.getSelectedId() != null && (SmsSettings.SP_CLICKATELL.equals(providers.getSelectedId()) || SmsSettings.SP_MVAAYOO.equals(providers.getSelectedId()) || SmsSettings.SP_OURSMS.equals(providers.getSelectedId())));
        keyValues.removeAllRows();
        if (item.getKeyValues() != null && !"".equals(item.getKeyValues())) {
            //Syntax = ${username}=hayot.rahimov;${password}=secretCode
            String[] keyValue = item.getKeyValues().split(";");
            if (keyValue != null && keyValue.length > 0) {
                for (String kV : keyValue) {
                    if (isNotEmpty(kV) && kV.contains("=")) {
                        String key = kV.substring(0, kV.indexOf("="));
                        String value = kV.substring(kV.indexOf("=") + 1);
                        if (isNotEmpty(key) && isNotEmpty(value)) {
                            key = key.replace("^${|}$", "");
                            keyValues.addRow(getKeyValueAsWidget(key, value));
                        }
                    }
                }
            }
        } else {
            String[] requiredFieldIDs = SmsSettings.requiredFieldIDs.get(providers.getSelectedId());
            if (requiredFieldIDs != null && requiredFieldIDs.length > 0) {
                for (String key : requiredFieldIDs) {
                    if (isNotEmpty(key)) {
                        this.keyValues.addRow(getKeyValueAsWidget(key, ""));
                    }
                }
            } else {
                this.keyValues.addRow(getKeyValueAsWidget(null, null));
            }
        }
        LoadingPanel.loading(false);
    }

    private void onProviderChange() {
        creditLeft.setHTML("");
        creditLeftPanel.setVisible(providers.getSelectedId() != null && (SmsSettings.SP_CLICKATELL.equals(providers.getSelectedId()) || SmsSettings.SP_MVAAYOO.equals(providers.getSelectedId()) || SmsSettings.SP_OURSMS.equals(providers.getSelectedId())));
        keyValues.removeAllRows();
        if (providers.getSelectedId() != null && SmsSettings.requiredFieldIDs.get(providers.getSelectedId()) != null) {
            String[] requiredFieldIDs = SmsSettings.requiredFieldIDs.get(providers.getSelectedId());
            if (requiredFieldIDs.length > 0) {
                for (String key : requiredFieldIDs) {
                    if (isNotEmpty(key)) {
                        this.keyValues.addRow(getKeyValueAsWidget(key, ""));
                    }
                }
            } else {
                this.keyValues.addRow(getKeyValueAsWidget(null, null));
            }
        }
        LoadingPanel.loading(false);
    }

    private void save() {
        if (!validate(false)) {
            return;
        }
        item.setName(name.getText());
        item.setProviderID(providers.getSelectedId());
        item.setKeyValues(getKeyValuesAsString());
        LoadingPanel.loading(true);
        profileService.saveSmsSettings(item, new AsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Object result) {
                LoadingPanel.loading(false);
                closeTab();
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), item.getName()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SMS_SETTINGS_ADD_EDIT, result, AddSmsSettingView.this);
            }
        });
    }

    private boolean validate(boolean forCheckLimit) {
        keyValues.setValidRows(0);
        if (!forCheckLimit) {
            name.removeStyleName(Constants.ERROR_FORM_STYLE);
        }
        providers.removeStyleName(Constants.ERROR_FORM_STYLE);
        int errors = 0;
        for (int i = 0; i < grid.getRowCount(); i++) {
            EditableTextBox key = (EditableTextBox) keyValues.getColumnById(i, FIELD_KEY);
            EditableTextBox value = (EditableTextBox) keyValues.getColumnById(i, FIELD_VALUE);
            if (key != null && value != null && isNotEmpty(key.getText()) && !isNotEmpty(value.getText())) {
                keyValues.setColumnValid(FIELD_VALUE);
                keyValues.notValid(i, FIELD_VALUE);
                errors++;
            }
            if (providers.getSelectedItem() != null && providers.getSelectedItem().getId() == SmsSettings.SP_ESKIZSMS) {
                if (key != null && "email".equals(key.getText()) && !Validation.validateEmailRequired(value)) {
                    keyValues.setColumnValid(FIELD_VALUE);
                    keyValues.notValid(i, FIELD_VALUE);
                    errors++;
                }
            }
        }
        if (!forCheckLimit && !isNotEmpty(name.getText())) {
            name.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (providers.getSelectedItem() == null) {
            providers.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    protected void getCreditCount(String url) {
        LoadingPanel.loading(true);
        profileService.getWebContentByUrl(url, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(String s) {
                LoadingPanel.loading(false);
                ///Credit : XXXX.X ko`rinishida keladi. formirovat qilish uchun...
                if (s == null) {
                    return;
                }
                if (providers.getSelectedItem() == null) {
                    return;
                }
                if (SmsSettings.SP_CLICKATELL.equals(providers.getSelectedId())) {
                    String result = s;//For ex: ERR: 108, Invalid or missing api_id
                    s = s.split(":")[1];
                    try {
                        if (!result.startsWith("ERR")) {
                            s = "" + Double.valueOf(s).intValue();
                            Info.show("Credit balance is " + s, Info.Type.INFO);
                        } else {
                            s = s.split(",")[1].trim();
                            Info.show(s, Info.Type.WARNING);
                        }
                    } catch (Exception exp) {
                        s = s.split(",")[1];
                        Info.show(wfmStrings.warning() + "<br/>" + s, Info.Type.INFO);
                    }
                    creditLeft.setHTML(s);
                } else if (SmsSettings.SP_OURSMS.equals(providers.getSelectedId())) {
                    if (OUR_SMS_ERROR_CODES_SET.contains(s)) {
                        String error = getOurSMSErrorMessage(s);
                        creditLeft.setHTML(error);
                        Info.show(error, Info.Type.WARNING);
                    } else {
                        creditLeft.setHTML(s);
                        if (s.toLowerCase().contains("status=0")) {
                            Info.show("Credit balance is " + s, Info.Type.INFO);
                        } else {
                            Info.show(s, Info.Type.WARNING);
                        }
                    }
                } else {
                    if (s.toLowerCase().contains("status=0")) {
                        //Success
                        s = s.substring(("Status=0,Credit balance is").length());
                        creditLeft.setHTML(s);
                        Info.show(s, Info.Type.INFO);
                    } else {
                        if (s.toLowerCase().contains("status=1")) {
                            //Error
                            s = s.substring(("status=1").length() + 1);
                            creditLeft.setHTML(s);
                            Info.show(s, Info.Type.WARNING);
                        }
                    }
                }
            }
        });
    }

    private String getOurSMSErrorMessage(String s) {
        if ("101".equals(s)) {
            return "Incomplete data";
        } else if ("102".equals(s)) {
            return "Incorrect user name";
        } else if ("103".equals(s)) {
            return "Incorrect password";
        } else if ("104".equals(s)) {
            return "There is no credit in the account";
        } else if ("111".equals(s)) {
            return "Sending is closed";
        } else if ("113".equals(s)) {
            return "The account is inactive";
        } else if ("114".equals(s)) {
            return "The account is disabled";
        } else if ("115".equals(s)) {
            return "Mobile not activated";
        } else if ("116".equals(s)) {
            return "Email not activated";
        } else if ("117".equals(s)) {
            return "Credit obtained";
        }
        return "";
    }

    protected void creditLeft() {
        if (validate(true)) {
            if (SmsSettings.SP_CLICKATELL.equals(providers.getSelectedId())) {
                getCreditCount("http://api.clickatell.com/http/getbalance?api_id=" + getValue("apiid") + "&user=" + getValue("username") + "&password=" + getValue("password"));
            } else if (SmsSettings.SP_OURSMS.equals(providers.getSelectedId())) {
                getCreditCount("http://www.OurSms.net/api/getbalance.php?username=" + getValue("username") + "&password=" + getValue("password") + "&return=");
            } else {
                getCreditCount("http://api.mvaayoo.com/mvaayooapi/APIUtil?user=" + getValue("username") + ":" + getValue("password") + "&type=0");
            }
        }
    }

    private String getValue(String key) {
        for (int i = 0; i < grid.getRowCount(); i++) {
            EditableTextBox keyField = (EditableTextBox) this.keyValues.getColumnById(i, FIELD_KEY);
            EditableTextBox valueField = (EditableTextBox) this.keyValues.getColumnById(i, FIELD_VALUE);
            if (key != null && keyField.getText().contains(key)) {
                return valueField.getText();
            }
        }
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SMS_SETTINGS_FORM;
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

    private ColumnConfig[] getKeyValuesColumnConfig() {
        ColumnConfig[] columns = new ColumnConfig[2];
        columns[0] = new ColumnConfig(CustomCell.class, FIELD_KEY, wfmStrings.key(), 235, false);
        columns[1] = new ColumnConfig(CustomCell.class, FIELD_VALUE, wfmStrings.value(), 235, true);
        return columns;
    }

    private Object[] getKeyValueAsWidget(String key, String value) {
        Object[] objects = new Object[2];
        EditableTextBox keyBox = new EditableTextBox();
        final EditableTextBox valueBox = new EditableTextBox();
        keyBox.setEnabled(false);
        keyBox.setValue(key == null ? "" : key);
        valueBox.setValue(value == null ? "" : value);
        Validation.checkToFocusTextBox(valueBox, "");
        objects[0] = keyBox;
        objects[1] = valueBox;
        return objects;
    }


    public String getKeyValuesAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.getRowCount(); i++) {
            EditableTextBox key = (EditableTextBox) keyValues.getColumnById(i, FIELD_KEY);
            EditableTextBox value = (EditableTextBox) keyValues.getColumnById(i, FIELD_VALUE);
            if (value != null && isNotEmpty(value.getText())) {
                sb.append(key.getText()).append("=").append(value.getText()).append(";");
            }
        }
        String result = sb.toString();
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }
}
