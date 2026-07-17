package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
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
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.MailListItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

/**
 * Created by IntelliJ IDEA.
 * Date: 28.01.2010
 * Time: 22:47:14
 */
public class MailListSummary extends CustomForm2 implements CustomFormConstants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private final Integer objectId;
    private HTML name, description, status;
    private MailListItem item;

    public MailListSummary(Integer objectId) {
        super("viewmaillist", wfmStrings.summaryView());
        this.objectId = objectId;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        name = initHTML();
        description = initHTML();
        status = initHTML();
        addField(NAME, name);
        addField(DESCRIPTION, description);
        addField(STATUS, status);
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        return Localize.getInstance().localizeByFieldID(getFormID(), fieldID);
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        MassMailService.App.get().getMailList(objectId, new AbstractAsyncCallback<MailListItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(MailListItem result) {
                item = result;
                LoadingPanel.loading(false);
                name.setHTML(result.getName());
                description.setHTML(result.getDescription());
                status.setHTML(result.isActive() ? wfmStrings.active() : wfmStrings.inactive());
            }
        });
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
        if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_MAILING_LIST)) {
            MaterialLink delete = new MaterialLink(wfmStrings.delete());
            delete.addClickHandler(event -> {
                final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                wfmMessageBox.setTitle(wfmStrings.warning());
                wfmMessageBox.setMessage(wfmStrings.sureYouWantToDelete());
                wfmMessageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        MassMailService.App.get().deleteMailList(objectId, new AbstractAsyncCallback<Void>() {
                            @Override
                            public void failure(Throwable throwable) {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void success(Void result) {
                                LoadingPanel.loading(false);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.mailList()), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MAIL_LIST_EDIT, result, MailListSummary.this);
                                closeTab();
                            }
                        });
                    }
                });
                wfmMessageBox.open();
            });
            options.add(delete);
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.MAIL_LIST_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
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
