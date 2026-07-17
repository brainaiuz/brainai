package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.rpc.EmailFilter;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Author: Azazello
 * Date: 3/30/2018
 * Time: 11:23 AM
 */
public class AddEmailParentFilter extends CustomForm implements Constants, Colapse {
    private static final String RULE_TABLE = "RULE_TABLE";
    private static final String FILTER_RULE_SENDER_LIST_BOX = "sender";
    private static final String FILTER_RULE_CONTAINSS_LIST_BOX = "actions";
    private static final String SEARCHING_TEXT_BOX = "TEXT_BOX";
    private static final String SEARCHING_DATE_PICKER = "DATE_PICKER";
    private static final String FILTER_RULE_OPERATORS_LIST_BOX = "operators";
    private static final SelectItem[] emailParts = EmailFilter.FILTER.RULE.parts;
    private static final SelectItem[] emailActions = EmailFilter.FILTER.RULE.actions;
    private static final SelectItem[] emailReceivedActions = EmailFilter.FILTER.RULE.receivedDateActions;
    private static final SelectItem[] operators = EmailFilter.FILTER.RULE.operators;
    private static final Integer RECEIVED_DATE = 4;

    static {
        emailParts[0].setName(wfmStrings.sender());
        emailParts[1].setName(wfmStrings.recipient());
        emailParts[2].setName(wfmStrings.subject());
        emailParts[3].setName(wfmStrings.receivedDate());
        emailActions[0].setName(wfmStrings.contains());
        emailActions[1].setName(wfmStrings.notContains());
        emailActions[2].setName(wfmStrings.matches());
        emailActions[3].setName(wfmStrings.noMatches());
        operators[0].setName(wfmStrings.and());
        operators[1].setName(wfmStrings.or());
        emailReceivedActions[0].setName(wfmStrings.before());
        emailReceivedActions[1].setName(wfmStrings.after());
    }

    private final Integer objectID;
    private EmailFilter item;
    private TextBox name;
    private WidgetsMap widgetsMap;

    public AddEmailParentFilter(Integer objectID) {
        super("addemailparentfilter", wfmStrings.parentFilter());
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        name = new TextBox();

        addTitleField(CustomFormConstants.DETAILS, getTitle(wfmStrings.parentFilter()));
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        show();
    }

    @Override
    protected void addButtons() {
        addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save()));
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ProfileService.App.get().getEmailFilter(null, objectID, new AbstractAsyncCallback<EmailFilter>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(EmailFilter result) {
                LoadingPanel.loading(false);
                item = result;
                name.setText(item.getName());

                addField(CustomFormConstants.PARENT, drawEmailFilter(item), getTitle(wfmStrings.rules()));
            }
        });
    }

    private Widget drawEmailFilter(EmailFilter filter) {
        widgetsMap = new WidgetsMap();
        widgetsMap.setObjectID(filter != null ? filter.getObjectID() : null);

        final MultiTableNewUI ruleCriterias = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getCriterias(null, null);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        }, false);

        if (filter != null && filter.getRules() != null && filter.getRules().size() > 0) {
            ruleCriterias.removeAllRows();
            for (String rule : filter.getRules()) {
                if (!Utils.isNullOrEmpty(rule)) {
                    ruleCriterias.addWidgets(getCriterias(filter, rule));
                }
            }
            onLinesAdded(ruleCriterias);
        }
        ruleCriterias.setOnLinesAdded(() -> onLinesAdded(ruleCriterias));

        widgetsMap.addWidgetToMap(RULE_TABLE, ruleCriterias);
        return ruleCriterias;
    }

    private boolean validate() {
        int error = 0;
        if (!Validation.validateTextBoxRequired(name)) {
            error++;
        }
        if (error > 0) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }

    private void setValues() {
        item.setName(name.getText());
        item.setParent(true);
        Map<String, Widget> rowOfEmailFilter = widgetsMap.getWidgetsMap();
        item.setRules(getRulesOfFilter((MultiTableNewUI) rowOfEmailFilter.get(RULE_TABLE)));
    }

    private void save() {
        if (!validate()) {
            return;
        }
        setValues();
        enableButton(false);
        LoadingPanel.loading(true);
        ProfileService.App.get().saveEmailFilter(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                enableButton(true);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FILTER_ADDED, result, AddEmailParentFilter.this);
                closeTab();
            }
        });
    }

    private ArrayList<String> getRulesOfFilter(MultiTableNewUI ruleCriterias) {
        EmailFilter emailFilter = new EmailFilter();
        ArrayList<String> rules = new ArrayList<>();
        if (ruleCriterias != null && ruleCriterias.getWidgets() != null && ruleCriterias.getWidgets().size() > 0) {
            for (Map<String, Widget> rowOfRule : ruleCriterias.getWidgets()) {
                if (rowOfRule != null) {
                    TextBox searchText = (TextBox) rowOfRule.get(SEARCHING_TEXT_BOX);
                    DatePicker searchDate = (DatePicker) rowOfRule.get(SEARCHING_DATE_PICKER);
                    if ((searchText.getText() != null && !"".equals(searchText.getText())) || (searchDate.getDate() != null)) {
                        DataListBox emailPart = (DataListBox) rowOfRule.get(FILTER_RULE_SENDER_LIST_BOX);
                        DataListBox emailContains = (DataListBox) rowOfRule.get(FILTER_RULE_CONTAINSS_LIST_BOX);
                        DataListBox operator = (DataListBox) rowOfRule.get(FILTER_RULE_OPERATORS_LIST_BOX);
                        String s = emailFilter.getRuleAsString(emailPart.getSelectedItem(true), emailContains.getSelectedItem(true), searchText.getText(), searchDate.getDate(), (operator.isVisible() ? operator.getSelectedItem(true) : null));
                        if (!Utils.isNullOrEmpty(s)) {
                            rules.add(s);
                        }
                    }
                }
            }
        }
        return rules;
    }

    private WidgetsMap getCriterias(EmailFilter emailFilter, String filter) {
        EmailFilter.FilterRule rule = emailFilter != null ? emailFilter.getRuleFromString(filter) : null;
        WidgetsMap widgetsMap = new WidgetsMap();
        DataListBox senderListBox = new DataListBox();
        senderListBox.setWithoutNullLabel(true);
        senderListBox.setItems(emailParts);
        DataListBox containsListBox = new DataListBox();
        containsListBox.setWithoutNullLabel(true);
        containsListBox.setItems(emailActions);

        TextBox textBox = new TextBox();
        DataListBox operatorListBox = new DataListBox();
        operatorListBox.setWithoutNullLabel(true);
        operatorListBox.setItems(operators);
        operatorListBox.setVisible(false);
        DatePicker datePicker = new DatePicker();
        datePicker.setVisible(false);
        if (rule != null) {
            if (rule.getEmailPart() != null) {
                senderListBox.setSelected(rule.getEmailPart());
            }
            if (senderListBox.getSelectedId().equals(RECEIVED_DATE)) {
                textBox.setVisible(false);
                datePicker.setVisible(true);
                if (rule.getWord() != null) {
                    try {
                        datePicker.setDate(DateUtils.parse(rule.getWord()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (rule.getWord() != null) {
                textBox.setText(rule.getWord());
            }
            if (rule.getEmailAction() != null) {
                if (rule.getEmailAction().getId() > 4) {
                    containsListBox.setItems(emailReceivedActions);
                }
                containsListBox.setSelected(rule.getEmailAction());
            }
            if (rule.getOperator() != null) {
                operatorListBox.setSelected(rule.getOperator());
            }
        }
        senderListBox.addValueChangeHandler(ch -> {
            if (senderListBox.getSelectedId().equals(RECEIVED_DATE)) {
                datePicker.setVisible(true);
                textBox.setVisible(false);
                textBox.setText(null);
                containsListBox.setItems(emailReceivedActions);
            } else {
                datePicker.setVisible(false);
                textBox.setVisible(true);
                datePicker.setDate(null);
                containsListBox.setItems(emailActions);
            }
        });
        widgetsMap.addWidgetToMap(FILTER_RULE_SENDER_LIST_BOX, senderListBox);
        widgetsMap.addWidgetToMap(FILTER_RULE_CONTAINSS_LIST_BOX, containsListBox);
        widgetsMap.addWidgetToMap(SEARCHING_DATE_PICKER, datePicker);
        widgetsMap.addWidgetToMap(SEARCHING_TEXT_BOX, textBox);
        widgetsMap.addWidgetToMap(FILTER_RULE_OPERATORS_LIST_BOX, operatorListBox);
        widgetsMap.addWidgets(senderListBox, containsListBox, textBox, datePicker, operatorListBox);
        return widgetsMap;
    }

    private void onLinesAdded(final MultiTableNewUI ruleCriterias) {
        LinkedList<HashMap<String, Widget>> allWidgets = ruleCriterias.getWidgets();
        if (allWidgets != null && allWidgets.size() > 0) {
            int i = 0;
            for (HashMap<String, Widget> widgets : allWidgets) {
                if (widgets.get(FILTER_RULE_OPERATORS_LIST_BOX) instanceof DataListBox) {
                    i++;
                    DataListBox operator = (DataListBox) widgets.get(FILTER_RULE_OPERATORS_LIST_BOX);
                    operator.setVisible(allWidgets.size() > 1 && allWidgets.size() > i);
                }
            }
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EMAIL_PARENT_FILTER_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
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
