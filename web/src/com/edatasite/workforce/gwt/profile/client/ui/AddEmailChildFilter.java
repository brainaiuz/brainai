package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.AddTaggingView;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.CollapsiblePanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.MultiSlideBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.EmailFilter;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 11/28/11
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddEmailChildFilter extends CustomForm implements Constants, Colapse {
    private static final String P_CASE_ASSIGNEE = "P_CASE_ASSIGNEE";
    private static final String P_CASE_RESOLVER = "P_CASE_RESOLVER";
    private static final String P_CASE_TO_TRASH = "P_CASE_TO_TRASH";
    private static final String P_CASE_EMAIL_TEMPLATE = "P_CASE_EMAIL_TEMPLATE";
    private static final String P_CASE_PROJECT = "P_CASE_PROJECT";
    private static final String P_TAGGING_SHELL = "P_TAGGING_SHELL";

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
    //widgets
    private TextBox name;
    private DataListBox parent;
    private CollapsiblePanel defaultColPanel;
    private MultiSlideBox filtersMultiTable;
    private SettingStrings settingsStrings;

//    private Map<Integer, Map<String, Widget>> parametrRows = new HashMap<>();

    public AddEmailChildFilter(Integer objectID) {
        super("addemailchildfilter", wfmStrings.childFilter());
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        name = new TextBox();
        parent = new DataListBox();

        filtersMultiTable = new MultiSlideBox(wfmStrings.rules(), new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return drawEmailFilter(null);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        defaultColPanel = new CollapsiblePanel(settingsStrings.defaultParameters());
        defaultColPanel.setActive(true);
        addTitleField(CustomFormConstants.DETAILS, getTitle(wfmStrings.childFilter()));
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.PARENT, parent, getTitle(wfmStrings.parent(), true));
        addField(CustomFormConstants.MAIN_TABLE_PANEL, defaultColPanel);
        addField(CustomFormConstants.MULTI_TABLE_PANEL, filtersMultiTable);
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
                parent.setItems(item.getEmailFilters());
                parent.setSelected(item.getParent());
                onTypeChange();
            }
        });
    }

    private boolean validate() {
        int error = 0;
        if (!Validation.validateTextBoxRequired(name)) {
            error++;
        }
        if (!Validation.validateDataListBoxRequired(parent)) {
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
        item.setParent(false);
        getChildFields();
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
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FILTER_ADDED, result, AddEmailChildFilter.this);
                closeTab();
            }
        });
    }

    private void getChildFields() {
        item.setParent(parent.getSelectedItem(true));
        item.setType(EmailFilter.CREATE_CASE);
        Map<String, Widget> map = defaultColPanel.getWidgetsMap().getWidgetsMap();
        item.setResolverID(((EmployeeLookUp) map.get(P_CASE_RESOLVER)).getSelectedItemID());
        SelectItem assignee = ((EmployeeLookUp) map.get(P_CASE_ASSIGNEE)).getSelectedItem();
        if (assignee != null) {
            if (assignee.getName().contains("(Department)")) {
                item.setDepartmentID(assignee.getId());
                item.setAssigneeID(null);
            } else {
                item.setAssigneeID(assignee.getId());
                item.setDepartmentID(null);
            }
        }
        item.setEmailTemplateID(((DataListBox) map.get(P_CASE_EMAIL_TEMPLATE)).getSelectedId());
        if (item.isProjectTemplateEnabled()) {
            item.setProjectTemplateID(((DataListBox) map.get(P_CASE_PROJECT)).getSelectedId());
        }
        item.setSendAutoresponse(((DataListBox) map.get(P_CASE_EMAIL_TEMPLATE)).getSelectedId() != null);
        item.setToTrash(((KpiSwitcher) map.get(P_CASE_TO_TRASH)).getValue());
        item.setRelationItems(((AddTaggingView) map.get(P_TAGGING_SHELL)).getSelectedRelations());
        item.setSubFilters(getSubEmailFilters());
    }

    private ArrayList<EmailFilter> getSubEmailFilters() {
        ArrayList<EmailFilter> emailFilters = new ArrayList<>();
        if (filtersMultiTable != null && filtersMultiTable.size() > 0) {
            for (WidgetsMap map : filtersMultiTable.getWidgetsMap()) {
                Map<String, Widget> widgets = map.getWidgetsMap();
                if (widgets != null) {
                    EmailFilter emailFilter = getSubEmailFilter(widgets);
                    emailFilter.setObjectID(map.getObjectID());
                    if (!emailFilters.contains(emailFilter)) {
                        emailFilters.add(emailFilter);
                    }
                }
            }
        }
        return emailFilters;
    }

    private EmailFilter getSubEmailFilter(Map<String, Widget> widgets) {
        EmailFilter subFilter = new EmailFilter();
        subFilter.setRules(getRulesOfFilter((MultiTableNewUI) widgets.get(RULE_TABLE)));
        if (item != null) {
            subFilter.setResolverID(((EmployeeLookUp) widgets.get(P_CASE_RESOLVER)).getSelectedItemID());
            SelectItem assignee = ((EmployeeLookUp) widgets.get(P_CASE_ASSIGNEE)).getSelectedItem();
            if (assignee != null) {
                if (assignee.getName().contains("(Department)")) {
                    subFilter.setDepartmentID(assignee.getId());
                    subFilter.setAssigneeID(null);
                } else {
                    subFilter.setAssigneeID(assignee.getId());
                    subFilter.setDepartmentID(null);
                }
            }
            subFilter.setEmailTemplateID(((DataListBox) widgets.get(P_CASE_EMAIL_TEMPLATE)).getSelectedId());
            if (item.isProjectTemplateEnabled()) {
                subFilter.setProjectTemplateID(((DataListBox) widgets.get(P_CASE_PROJECT)).getSelectedId());
            }
            subFilter.setSendAutoresponse(((DataListBox) widgets.get(P_CASE_EMAIL_TEMPLATE)).getSelectedId() != null);
            subFilter.setToTrash(((KpiSwitcher) widgets.get(P_CASE_TO_TRASH)).getValue());
            subFilter.setRelationItems(((AddTaggingView) widgets.get(P_TAGGING_SHELL)).getSelectedRelations());
        }
        return subFilter;
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

    private void onTypeChange() {
        defaultColPanel.removeAllRows();
        filtersMultiTable.removeAllRows();
        addChildFilterRelatedFields();
    }

    private WidgetsMap drawEmailFilter(EmailFilter filter) {
        WidgetsMap widgetsMap = new WidgetsMap();
        GRow row = new GRow();
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
                if (rule != null && !"".equals(rule)) {
                    ruleCriterias.addWidgets(getCriterias(filter, rule));
                }
            }
            onLinesAdded(ruleCriterias);
        }
        ruleCriterias.setOnLinesAdded(() -> onLinesAdded(ruleCriterias));

        Map<String, Widget> map = new HashMap<>();
        FormGroup ruleTable = new FormGroup(ruleCriterias);
        if (item != null) {
            map = addCaseParametrs(row, filter, false, ruleTable);
        } else {
            row.add(new GColumn(GColumnEnum.COL_12, ruleTable));
        }
        widgetsMap.addWidgetToMap(RULE_TABLE, ruleCriterias);
        widgetsMap.getWidgetsMap().putAll(map);

        widgetsMap.addWidgets(row);
        return widgetsMap;
    }

    private Map<String, Widget> addCaseParametrs(GRow row, EmailFilter emailFilter, boolean isDefault, FormGroup... formGroups) {
        Map<String, Widget> widgetsMap = new HashMap<>();
        EmployeeLookUp assignee = new EmployeeLookUp(true, true, false);
        EmployeeLookUp resolver = new EmployeeLookUp(true, false, false);
        KpiSwitcher moveToTrash = new KpiSwitcher();
        moveToTrash.setOffLabel(wfmStrings.moveToTrash());
        DataListBox emailTemplates = new DataListBox();
        emailTemplates.setSelectedIndex(0);
        emailTemplates.setItems(item.getEmailTemplates());
        DataListBox copyProjects = new DataListBox();
        copyProjects.setSelectedIndex(0);
        copyProjects.setItems(item.getProjects());
        final VerticalPanel linksPanel = new VerticalPanel();
        final AddTaggingView taggingShell = addlinkPopup(linksPanel, null, null, null);
        if (emailFilter != null) {
            if (emailFilter.getAssigneeID() != null) {
                assignee.setSelected(new SelectItem(emailFilter.getAssigneeID(), emailFilter.getAssigneeName()));
            } else if (emailFilter.getDepartmentID() != null) {
                SelectItem department = new SelectItem(emailFilter.getDepartmentID(), emailFilter.getDepartmentName());
                department.setNewItem(true);
                assignee.setSelected(department);
            }
            resolver.setSelected(new SelectItem(emailFilter.getResolverID(), emailFilter.getResolverName()));
            copyProjects.setSelected(emailFilter.getProjectTemplateID());
            emailTemplates.setSelected(emailFilter.getEmailTemplateID());
            copyProjects.setSelected(emailFilter.getProjectTemplateID());
            moveToTrash.setValue(emailFilter.isToTrash());
            taggingShell.setSelectedRelations(emailFilter.getRelationItems());
            taggingShell.setFromID(emailFilter.getObjectID());
            linksPanel.clear();
            linksPanel.add(AddTaggingView.getAddLinkButton(taggingShell, wfmStrings.addLinks()));
            if (taggingShell.getSelectedRelations() != null && taggingShell.getSelectedRelations().size() > 0) {
                linksPanel.add(AddTaggingView.drawRelationTags(taggingShell));
            }
        }
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_RELATION, taggingShell, (sender, args) -> {
            if (args != null && taggingShell.equals(sender)) {
                String ruleIDStr = sender.getElement().getId();
                Integer ruleID = null;
                if (ruleIDStr != null && !"".equals(ruleIDStr.trim())) {
                    ruleID = Integer.valueOf(ruleIDStr);
                }
                addlinkPopup(linksPanel, (AddTaggingView) sender, (ArrayList<RelationItem>) args, ruleID);
            }
        });

        GColumn column1 = new GColumn(GColumnEnum.COL_6);
        GColumn column2 = new GColumn(GColumnEnum.COL_6);
        if (isDefault) {
            column1.add(new FormGroup(wfmStrings.assignee(), assignee));
            column1.add(new FormGroup(wfmStrings.resolver(), resolver));
            if (item.isProjectTemplateEnabled()) {
                column2.add(new FormGroup(wfmStrings.projects(), copyProjects));
            }
            column2.add(new FormGroup(wfmStrings.sendAutoresponse(), emailTemplates));
            column2.add(new FormGroup("&nbsp;", moveToTrash));
        } else {
            column1.add(new FormGroup(wfmStrings.assignee(), assignee));
            column1.add(new FormGroup(wfmStrings.resolver(), resolver));
            column1.add(new FormGroup(linksPanel));
            if (item.isProjectTemplateEnabled()) {
                column2.add(new FormGroup(wfmStrings.projects(), copyProjects));
            }
            column2.add(new FormGroup(wfmStrings.sendAutoresponse(), emailTemplates));
            column2.add(new FormGroup("&nbsp;", moveToTrash));
            for (FormGroup widget : formGroups) {
                column2.add(widget);
            }
        }
        widgetsMap.put(P_CASE_ASSIGNEE, assignee);
        widgetsMap.put(P_CASE_RESOLVER, resolver);
        widgetsMap.put(P_CASE_EMAIL_TEMPLATE, emailTemplates);
        if (item.isProjectTemplateEnabled()) {
            widgetsMap.put(P_CASE_PROJECT, copyProjects);
        }
        widgetsMap.put(P_CASE_TO_TRASH, moveToTrash);
        widgetsMap.put(P_TAGGING_SHELL, taggingShell);

        row.add(column1);
        row.add(column2);

        return widgetsMap;
    }

    private AddTaggingView addlinkPopup(VerticalPanel linksPanel, AddTaggingView taggingShell, ArrayList<RelationItem> relations, Integer ruleID) {
        if (taggingShell == null) {
            taggingShell = new AddTaggingView(ruleID, RelationItem.TYPE_EMAIL_FILTER, item.getName(), wfmStrings.addLinks(), false);
            taggingShell.getElement().setId(ruleID != null ? ruleID.toString() : "");
        }
        taggingShell.setSelectedRelations(relations);
        linksPanel.clear();
        linksPanel.add(AddTaggingView.getAddLinkButton(taggingShell, wfmStrings.addLinks()));
        if (taggingShell.getSelectedRelations() != null && taggingShell.getSelectedRelations().size() > 0) {
            linksPanel.add(AddTaggingView.drawRelationTags(taggingShell));
        }
        return taggingShell;
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

    private void addChildFilterRelatedFields() {
        if (item != null && item.getSubFilters().size() > 0) {
            filtersMultiTable.removeAllRows();
            for (EmailFilter filter : item.getSubFilters()) {
                filtersMultiTable.addWidgets(drawEmailFilter(filter));
            }
        } else {
            filtersMultiTable.addWidgets(drawEmailFilter(null));
        }
        defaultColPanel.getWidgetsMap().getWidgetsMap().putAll(addCaseParametrs(defaultColPanel.getDefaultRow(), item, true));
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
