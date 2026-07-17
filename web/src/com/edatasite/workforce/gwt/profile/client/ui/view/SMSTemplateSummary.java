package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Azazello on 4/21/15.
 */
public class SMSTemplateSummary extends AddEditSMSTemplatesView implements Colapse {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private HTML name, module, isDefault, content;
    private final String test_code_ID_name = "summary_sms_template_view_";
    private HorizontalPanel contentPanel;

    public SMSTemplateSummary(Integer objectID) {
        super("summary", wfmStrings.summaryView());
        super.objectID = objectID;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SMS_TEMPLATE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected void addButtons() {
        addEditButton().addClickHandler(event -> {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("smstemplate|add/add/" + item.getObjectID(), item.getName());
        });
        addRemoveButton().addClickHandler(clickEvent -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.warning());
            messageBox.setMessage(wfmStrings.messAreDelete() + " <b>" + item.getName() + "</b> " + settingsStrings.mesSMSTemplate());
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    ProfileService.App.get().deleteSMSTemplate(item.getObjectID(), new AsyncCallback<Void>() {
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        public void onSuccess(Void result) {
                            LoadingPanel.loading(false);
                            closeTab();
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.smsTemplates()), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SMS_TEMPLATE_DELETE, result, SMSTemplateSummary.this);
                        }
                    });
                }
            });
            messageBox.open();
        });
    }

    public void registerFields() {
        name = initHTML();
        module = initHTML();
        isDefault = initHTML();
        content = initHTML();
        content.addStyleName(DEFAULT_WIDTH);
        contentPanel = new HorizontalPanel();
        contentPanel.add(content);
        contentPanel.addStyleName(DEFAULT_WIDTH);
        contentPanel.setCellVerticalAlignment(content, HasVerticalAlignment.ALIGN_TOP);
        drawForm();
    }

    @Override
    protected void fillFields() {
        name.setHTML(item.getName() != null ? item.getName() : wfmStrings.notAvailable());
        module.setHTML(item.getModuleName() != null ? item.getModuleName() : wfmStrings.notAvailable());
        isDefault.setHTML(item.isDefault() ? wfmStrings.yes() : wfmStrings.no());
        content.setHTML(item.getContent() != null ? item.getContent() : wfmStrings.notAvailable());
    }

    @Override
    protected void drawForm() {
        addTitleField(SMS_INFORMATION, wfmStrings.smsInformation());
        addField(NAME, name, getTitle(wfmStrings.name()));
        addField(MODULE, module, getTitle(wfmStrings.apps()));
        addField(IS_DEFAULT, isDefault, getTitle(wfmStrings.isDefault()));
        addField(CONTENT, contentPanel, getTitle(wfmStrings.content()));
        show();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
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
}
