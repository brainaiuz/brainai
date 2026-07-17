package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountTypeItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 10/22/11
 * Time: 6:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountTypeNumberSettingView extends CustomForm {
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();

    private List<AccountTypeItem> accountTypeItems = new ArrayList<>();

    private LinkedHashMap<String, List<AccountTypeItem>> map;

    private FlexTable table;

    private String accountTypeNumberSettings = "account_type_number_settings_";

    public AccountTypeNumberSettingView() {
        super("accounttypesettings", settingsStrings.accountNumbering());
    }

    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ACCOUNT_NUMBERING_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void addButtons() {
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.getElement().setId("Account_type_numbering_setting_save_button");
        saveButton.addClickHandler(sender -> save());
        addButton(saveButton);
    }

    @Override
    protected void getDataToFillFields() {

    }

    public void initialize() {
        table = new FlexTable();
        AccountingService.App.get().getAccountTypeItems(new AsyncCallback<AccountTypeItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(AccountTypeItem[] items) {
                initAccountTypeMap(items);
                drawForm();
            }
        });
    }

    private void drawForm() {
        int rows = 0;
        int cols = 0;

        table.addStyleName("table");

        table.setHTML(rows, cols++, wfmStrings.accountType());
        table.setHTML(rows, cols++, settingsStrings.startNumber());
        table.setHTML(rows, cols, "&nbsp;");
        table.getFlexCellFormatter().setWidth(rows, cols++, "30px");
        table.setHTML(rows, cols, wfmStrings.endNumber());
        table.getRowFormatter().setStyleName(rows, "thead");
        rows++;

        if (map != null) {
            for (Map.Entry<String, List<AccountTypeItem>> item : map.entrySet()) {
                String category = item.getKey();

                cols = 0;

                table.setHTML(rows, cols, "<b>" + category.toUpperCase() + "</b>");
                table.getFlexCellFormatter().setColSpan(rows++, cols, 4);

                if (item.getValue() != null && item.getValue().size() > 0) {
                    for (AccountTypeItem typeItem : item.getValue()) {

                        cols = 0;

                        table.setHTML(rows, cols++, typeItem.getName());

                        TextBox txtStart = new TextBox();
                        txtStart.ensureDebugId(accountTypeNumberSettings+typeItem.getName().toLowerCase().replace(" ","_")+"_start");
//                        txtStart.getElement().getStyle().setMargin(10, Style.Unit.PX);
                        txtStart.setValue(typeItem.getStart() != null ? String.valueOf(typeItem.getStart()) : null);
                        Validation.addNumericKeyboardListener(txtStart);

                        TextBox txtEnd = new TextBox();
                        txtEnd.ensureDebugId(accountTypeNumberSettings+typeItem.getName().toLowerCase().replace(" ","_")+"_end");
                        txtEnd.setValue(typeItem.getEnd() != null ? String.valueOf(typeItem.getEnd()) : null);
//                        txtEnd.getElement().getStyle().setMargin(10, Style.Unit.PX);
                        Validation.addNumericKeyboardListener(txtEnd);

                        Hidden hATID = new Hidden();
                        hATID.setValue(typeItem.getObjectID().toString());

                        table.setWidget(rows, cols++, txtStart);

                        table.setHTML(rows, cols++, "<div style=\"text-align:center;\">&mdash;</div>");
//                        table.getFlexCellFormatter().setWidth(rows, cols - 1, "20px");

                        Div lastCellWidgets = new Div();
                        lastCellWidgets.add(txtEnd);
                        lastCellWidgets.add(hATID);
                        table.setWidget(rows, cols, lastCellWidgets);
//                        table.setWidget(rows, cols, hATID);

                        rows++;
                    }
                }
            }
        }
        addTitleField(NUMBERING_SETTINGS, settingsStrings.accountNumbering());
        addField(ACCOUNT_NUMBERING_TABLE, table, "", true);
        show();
    }

    private void initAccountTypeMap(AccountTypeItem[] items) {
        map = new LinkedHashMap<>();

        if (items != null && items.length > 0) {
            for (AccountTypeItem item : items) {
                if (map.get(item.getCategory()) != null) {
                    map.get(item.getCategory()).add(item);
                } else {
                    map.put(item.getCategory(), new ArrayList<>(Arrays.asList(item)));
                }
            }
        }
    }

    private void save() {
        for (int row = 1; row < table.getRowCount(); row++) {
            if (table.getCellCount(row) >= 4) {
                AccountTypeItem item = new AccountTypeItem();
                TextBox startBox = (TextBox) table.getWidget(row, 1);

                Div div = (Div) table.getWidget(row, 3);
                List<Widget> childs = div.getChildrenList();
                TextBox endBox = (TextBox) childs.get(0);
                Hidden objectIdBox = (Hidden) childs.get(1);

                if (startBox.getValue() != null && !"".equals(startBox.getValue())) {
                    item.setStart(Integer.valueOf(startBox.getValue()));
                }
                if (endBox.getValue() != null && !"".equals(endBox.getValue())) {
                    item.setEnd(Integer.valueOf(endBox.getValue()));
                }
                if (objectIdBox.getValue() != null && !"".equals(objectIdBox.getValue())) {
                    item.setObjectID(Integer.valueOf(objectIdBox.getValue()));
                }
                accountTypeItems.add(item);
            }
        }


        LoadingPanel.loading(true);
        AccountingService.App.get().saveAccountTypeNumbering(accountTypeItems.toArray(new AccountTypeItem[]{}), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(Void aVoid) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.accountNumber()), Info.Type.INFO);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-type-num-settings";//return "icon-settings-invoice";
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
