package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: 07-Apr-2018
 * Time: 17:39:36
 */
public class ViewSolutionForm extends AddSolutionView implements Colapse {
    private HTML assignee, status, title, questionHTML, answerHTML;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public ViewSolutionForm(Integer objectId) {
        super("viewsolution", wfmStrings.summaryView());
        this.objectId = objectId;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    public void initialize() {
        title = initHTML();
        assignee = initHTML();
        status = initHTML();
        questionHTML = initHTML();
        answerHTML = initHTML();

        attachment = new GeneralFileUpload(F_SOLUTION, objectId, objectId);

        addTitleField(CRM_SOLUTION_INFORMATION, wfmStrings.solutionInformation());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TITLE) != null) {
            addField(CustomFormConstants.TITLE, title, getTitle(formPropertyMap.get(CustomFormConstants.TITLE).isChanged() ? formPropertyMap.get(CustomFormConstants.TITLE).getTitle() : wfmStrings.title()));
        } else {
            addField(TITLE, title, getTitle(wfmStrings.title(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE) != null) {
            addField(CustomFormConstants.ASSIGNEE, assignee, getTitle(formPropertyMap.get(CustomFormConstants.ASSIGNEE).isChanged() ? formPropertyMap.get(CustomFormConstants.ASSIGNEE).getTitle() : wfmStrings.assignee()));
        } else {
            addField(ASSIGNEE, assignee, getTitle(wfmStrings.assignee()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status()));
        } else {
            addField(STATUS, status, getTitle(wfmStrings.status()));
        }
        addTitleField(CRM_SOLUTION_DESCRIPTION, wfmStrings.solutionDescription());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_QUESTION) != null) {
            addField(CustomFormConstants.CRM_SOLUTION_QUESTION, questionHTML, getTitle(formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_QUESTION).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_QUESTION).getTitle() : wfmStrings.question()));
        } else {
            addField(CRM_SOLUTION_QUESTION, questionHTML, getTitle(wfmStrings.question(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CRM_SOLUTION_QUESTION) != null) {
            addField(CRM_SOLUTION_ANSWER, answerHTML, getTitle(formPropertyMap.get(CRM_SOLUTION_ANSWER).isChanged() ? formPropertyMap.get(CRM_SOLUTION_ANSWER).getTitle() : wfmStrings.answer()));
        } else {
            addField(CRM_SOLUTION_ANSWER, answerHTML, getTitle(wfmStrings.answer(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(ATTACHMENTS) != null) {
            addField(ATTACHMENTS, attachment, getTitle(formPropertyMap.get(ATTACHMENTS).isChanged() ? formPropertyMap.get(ATTACHMENTS).getTitle() : wfmStrings.attachments()));
        } else {
            addField(ATTACHMENTS, attachment, wfmStrings.attachments(), true);
        }
        show();
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
        if (Utils.hasRole(Constants.ADMIN)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
            });
            options.add(customize);
        }
        if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_SOLUTION)) {
            MaterialLink delete = new MaterialLink(wfmStrings.delete());
            delete.addClickHandler(event -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        CRMService.App.get().deleteSolution(objectId, new AbstractAsyncCallback() {
                            @Override
                            public void failure(Throwable caught) {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void success(Object result) {
                                LoadingPanel.loading(false);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), crmStrings.solution()), Info.Type.INFO);
                                closeTab();
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SOLUTION_DELETED, result, ViewSolutionForm.this);
                            }
                        });
                    }
                });
                messageBox.open();
            });
            options.add(delete);
        }
        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_SOLUTION)) {
            addEditButton().addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("solution|add/add/" + objectId, item.getTitle()));
        }

    }

    @Override
    protected void fillFieldsWithData() {
        title.setHTML(item.getTitle());
        assignee.setHTML(item.getAssignee());
        status.setHTML(item.getStatus());
        questionHTML.setHTML(item.getQuestion());
        answerHTML.setHTML(item.getAnswer());
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