package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowCondition;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;

/**
 * Created by Hayot on 2/28/14.
 */
public class ViewWorkflowRule extends AddWorkflowRule implements NoColapse {
    private HTML name, status, description, executionCriteria, module;
    private FlexTable ruleCriteria;
    private MaterialDropDown stepMenu;

    public ViewWorkflowRule(Integer objectId, String fromType, boolean recurrence) {
        super(objectId, fromType, recurrence);
    }

    @Override
    protected void registerFields() {
        name = initHTML();
        status = initHTML();
        description = initHTML();
        executionCriteria = initHTML();
        module = initHTML();
        ruleCriteria = new FlexTable();
        ruleCriteria.setWidth("70%");
        addField(WORKFLOW_FORM.NAME, name, getTitle(wfmStrings.name()));
        addField(WORKFLOW_FORM.MODULE, module, getTitle(wfmStrings.apps()));
        addField(WORKFLOW_FORM.STATUS, status, getTitle(wfmStrings.status()));
        addField(WORKFLOW_FORM.DESCRIPTION, description, getTitle(wfmStrings.description()));
        addField(WORKFLOW_FORM.EXECUTION_CRITERIA, executionCriteria, getTitle(wfmStrings.executionCriteria()));
        addField(WORKFLOW_FORM.RULE_CRITERIA, ruleCriteria, getTitle(wfmStrings.ruleCriteria()));
        show();
    }

    @Override
    protected void setValuesToWidgets() {
        if (item != null) {
            if (item.getOnboardingSteps() != null && item.getOnboardingSteps().length > 0 && stepMenu != null) {
                for (final SelectItem it : item.getOnboardingSteps()) {
                    stepMenu.add(new KpiLink(it.getName(), clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged(EMPLOYEE_STEP + "|add/add/" + it.getId() + "/" + it.getDescription() + "/" + it.getName() + "/" + objectId, item.getName())));
                }
            }
            setInnerHTML(name, item.getName());
            setInnerHTML(description, item.getDescription());
            setInnerHTML(status, item.isActive() ? wfmStrings.active() : wfmStrings.inactive());
            setInnerHTML(module, WorkflowRuleListView.localize(item.getModule()));
            setInnerHTML(executionCriteria, enumStrings.getString(item.getExecutionCriteria().name()));
            initRuleCriteriaValues();
        }
    }

    private void initRuleCriteriaValues() {
        if (item.getConditions().size() > 0) {
            ruleCriteria.setCellSpacing(5);
            ruleCriteria.setCellPadding(5);
            int i = 0;
            for (WorkflowCondition condition : item.getConditions().values()) {
                if (condition.getColumn() != null) {
                    HTML index = new HTML();
                    HTML operator = new HTML();
                    HTML moduleColumn = new HTML();
                    HTML operand = new HTML();
                    HTML value = new HTML();
                    setInnerHTML(index, String.valueOf(condition.getConditionID() != null ? condition.getConditionID() : i+1));
                    setInnerHTML(operator, condition.getOperator());
                    if (condition.getColumn() != null && (condition.getColumn().startsWith("string_value")
                            || condition.getColumn().startsWith("double_value")
                            || condition.getColumn().startsWith("date_value"))) {
                        setInnerHTML(moduleColumn, condition.getCustomFieldName() != null ? condition.getCustomFieldName() : condition.getColumn());
                    } else {
                        String localized = getLocalizer().localizeByFieldID(getFormIDOfModule(item.getModule()), condition.getColumn());
                        setInnerHTML(moduleColumn, localized != null && !"".equals(localized) ? localized : condition.getColumn());
                    }
                    setInnerHTML(operand, getLocalizer().localizeByCode(condition.getOperand()));
                    if (condition.getValue() != null) {
                        if (condition.getValue().contains("@")) {
                            String[] s = condition.getValue().split("@");
                            if (s.length == 1) {
                                setInnerHTML(value, s[0]);
                            } else if (s.length > 1) {
                                setInnerHTML(value, s[1]);
                            }
                        } else {
                            setInnerHTML(value, condition.getValue());
                        }
                    } else {
                        setInnerHTML(value, "");
                    }
                    ruleCriteria.setWidget(i, 0, index);
                    ruleCriteria.setWidget(i, 1, operator);
                    ruleCriteria.setWidget(i, 2, moduleColumn);
                    ruleCriteria.setWidget(i, 3, operand);
                    ruleCriteria.setWidget(i, 4, value);
                    i++;
                }
                if (i > 0) {
//                    ruleCriteria.setWidget(i, 0, new HTML(item.getPattern() == null ? wfmStrings.na() : item.getPattern()));
                } else {
                    ruleCriteria.setWidget(0, 0, new HTML(wfmMessages.thereAreNoSomethingItemsYet(wfmStrings.ruleCriteria())));
                }
            }
        } else {
            ruleCriteria.setWidget(0, 0, new HTML(wfmMessages.thereAreNoSomethingItemsYet(wfmStrings.ruleCriteria())));
        }
    }

    @Override
    protected void addButtons() {

        customizeButton.setVisible(false);

        MaterialLink options = new MaterialLink(wfmStrings.options());
        MaterialSplitButton optionsButton = new MaterialSplitButton(options, Constants.BTN_DEFAULT_OUTLINE);
        if (Utils.hasRole(Constants.ADMIN)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
            });
            optionsButton.addItem(customize);
        }

        MaterialLink deleteButton = new MaterialLink(wfmStrings.delete());
        deleteButton.addClickHandler(event -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.warning());
            messageBox.setMessage(wfmMessages.sureYouWantToDelete(item.getName(), settingsStrings.workflow()));
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    if (item != null) {
                        ArrayList<Integer> ids = new ArrayList<>();
                        ids.add(objectId);
                        LoadingPanel.loading(true);
                        profileService.deleteWorkflows(ids, new AbstractAsyncCallback<ArrayList<Integer>>() {
                            @Override
                            public void failure(Throwable caught) {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void success(ArrayList<Integer> result) {
                                LoadingPanel.loading(false);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_DELETE, result, ViewWorkflowRule.this);
                                closeTab();
                            }
                        });
                    }
                }
            });
            messageBox.open();
        });
        optionsButton.addItem(deleteButton);

        addRightButton(optionsButton);

        addEditButton().addClickHandler(event -> closeTab("workflow|add/add/" + objectId + "/" + fromType + "/" + item.getExecutionCriteria(), item.getName()));

    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }
}
