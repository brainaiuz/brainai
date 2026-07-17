package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 15.09.2010
 * Time: 13:49:28
 */
public class CreateEMLTemplatesView extends View implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private TextBox nameBox;
    private DataListBox templateCategoryBox;
    private DataListBox defaultyesOrNo;
    private TextBox subjectBox;
    private DataListBox fromUserBox;

    private final Integer zipFileId;

    private VerticalPanel templateTable;
    private VerticalPanel generalPanel;
    private SelectItem[] templateItems;
    private SelectItem[] fromUsersItems;
    private Button saveButton;
    private EmailTemplateItem[] generalTemplateItem;

    public CreateEMLTemplatesView(Integer zipFileId) {
        super("emlTemplatesView", settingsStrings.importEmailTemplates());
        this.zipFileId = zipFileId;
    }

    @Override
    protected Widget onInitialize() {

        generalPanel = new VerticalPanel();
        generalPanel.setSpacing(10);
        generalPanel.add(new HTML("<h1 class=customTitle>Import Email Template Details</h1>"));
        FlexTable textTable = new FlexTable();
        textTable.setCellSpacing(5);
        textTable.setWidth("710px");
        HTML space = new HTML("&nbsp;");
        space.setWidth("25px");
        textTable.setWidget(0, 0, space);
        textTable.setWidget(0, 1, generateHTML(wfmStrings.name(), true, "100px"));
        textTable.setWidget(0, 2, generateHTML(wfmStrings.category(), true, "100px"));
        textTable.setWidget(0, 3, generateHTML("Subject", true, "100px"));
        textTable.setWidget(0, 4, generateHTML(wfmStrings.from(), true, "100px"));
        textTable.setWidget(0, 5, generateHTML("Is Default", false, "90px"));
        textTable.setHTML(0, 6, "&nbsp;");
        textTable.getFlexCellFormatter().setWidth(0, 6, "25px");

        generalPanel.add(textTable);
        templateTable = new VerticalPanel();
        templateTable.setSpacing(5);
        generalPanel.add(templateTable);

        saveButton = new Button(wfmStrings.save());
        saveButton.addClickHandler(event -> save());
        HorizontalPanel buttonsPanel = new HorizontalPanel();
        buttonsPanel.setSpacing(10);
        buttonsPanel.add(saveButton);
        buttonsPanel.setCellHorizontalAlignment(saveButton, HasHorizontalAlignment.ALIGN_CENTER);
        generalPanel.add(buttonsPanel);
        generalPanel.setCellHorizontalAlignment(buttonsPanel, HasHorizontalAlignment.ALIGN_CENTER);

        ProfileService.App.get().getEmailTemplateCategories(null, new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable caught) {
            }

            public void success(SelectItem[] result) {
                if (result != null) {
                    templateItems = result;
                }
            }
        });
        ProfileService.App.get().getCurrentOrSomeUsers(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                if (result != null) {
                    fromUsersItems = new SelectItem[result.length + 1];
                    fromUsersItems[0] = new SelectItem(-1, wfmStrings.currentUser());
                    for (int i = 1; i < fromUsersItems.length; i++) {
                        fromUsersItems[i] = new SelectItem(result[i - 1].getId(), result[i - 1].getName());
                    }
                }
            }
        });

        LoadingPanel.loading(true);
        CommonService.App.get().getImportEMLFiles(zipFileId, new AbstractAsyncCallback<EmailTemplateItem[]>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(EmailTemplateItem[] result) {
                generalTemplateItem = result;
                drawEMLs(result);
                LoadingPanel.loading(false);
            }
        });
        add(generalPanel);
        return null;
    }

    private HTML generateHTML(String text, boolean required, String width) {
        HTML html = new HTML();
        html.setWidth(width);
        html.setHTML("<b class=customTitle>" + text + (required ? "<font color=red>*</font>" : "") + ":</b>");
        html.setWordWrap(false);
        return html;
    }

    private void drawEMLs(EmailTemplateItem[] templateItem) {
        if (templateItem != null) {
            for (int i = 0; i < templateItem.length; i++) {
                EmailTemplateItem item = templateItem[i];

                /*nameBox = new TextBox();
                nameBox.setWidth("110px");
                nameBox.setHeight("20px");
                templateCategoryBox = new DataListBox();
                templateCategoryBox.setWidth("110px");
                templateCategoryBox.setHeight("20px");

                defaultyesOrNo = new DataListBox();
                defaultyesOrNo.setWidth("80px");
                defaultyesOrNo.setHeight("20px");
                defaultyesOrNo.setWithoutNullLabel(true);
                defaultyesOrNo.setItems(YES_NO_LIST);
                if (item.isDefault()) {
                    defaultyesOrNo.setSelected(Integer.valueOf(0));
                } else {
                    defaultyesOrNo.setSelected(Integer.valueOf(1));
                }
                subjectBox = new TextBox();
                subjectBox.setWidth("110px");
                subjectBox.setHeight("20px");
                fromUserBox = new DataListBox();
                fromUserBox.setWidth("110px");
                fromUserBox.setHeight("20px");
                if (fromUsersItems != null) {
                    fromUserBox.setItems(fromUsersItems);
//                    fromUserBox.setSelected(Integer.valueOf(-1));
                    fromUserBox.setSelected(item.getFromUserID());
                }

                nameBox.setText(item.getName());
                subjectBox.setText(item.getSubject());
                if (templateItems != null) {
                    templateCategoryBox.setItems(templateItems);
//                    if (item.getCategoryId() == null || Integer.valueOf(-3).equals(item.getCategoryId())) {
//                        templateCategoryBox.setEnabled(false);
//                    } else {
//                        templateCategoryBox.setEnabled(true);
//                    }
                    templateCategoryBox.setSelected(item.getCategoryId());
                }
//                templateCategoryBox.addValueChangeHandler(new ChangeHandler() {
//                    @Override
//                    public void onChange(ChangeEvent event) {
//                        String tempDescription = templateCategoryBox.getSelectedItem().getDescription();
//                        if (EXPENSE_CLAIM_CATEGORY_SUBMIT.equals(tempDescription) || EXPENSE_CLAIM_CATEGORY_RESUBMIT.equals(tempDescription) ||
//                                CALENDAR_EVENT_ADD_CATEGORY.equals(tempDescription) || CALENDAR_EVENT_EDIT_CATEGORY.equals(tempDescription) ||
//                                CALENDAR_EVENT_DELETE_CATEGORY.equals(tempDescription) || CALENDAR_EVENT_SHARE_EDIT_CATEGORY.equals(tempDescription) ||
//                                CALENDAR_EVENT_REMINDER_CATEGORY.equals(tempDescription) || CALENDAR_EVENT_SHARE_CATEGORY.equals(tempDescription) ||
//                                TASK_ASSIGN_CATEGORY.equals(tempDescription) || PROJECT_ASSIGN_CATEGORY.equals(tempDescription) ||
//                                PROJECT_ADD_CATEGORY.equals(tempDescription) || BACKUP_MANAGER_ASSIGN_CATEGORY.equals(tempDescription) ||
//                                PROJECT_MANAGER_ASSIGN_CATEGORY.equals(tempDescription)) {
//                            fromUserBox.setSelected(Integer.valueOf(-1));
//                            fromUserBox.setEnabled(false);
//                        } else {
//                            fromUserBox.setEnabled(true);
//                        }
//                    }
//                });
                FlexTable tempTable = new FlexTable();
                tempTable.setCellSpacing(5);
                tempTable.setWidth("710px");
                HTML numberValue = new HTML("<b>" + String.valueOf(i + 1) + ".</b>");
                numberValue.setWidth("20px");
                tempTable.setWidget(0, 0, numberValue);
                tempTable.setWidget(0, 1, nameBox);
                tempTable.setWidget(0, 2, templateCategoryBox);
                tempTable.setWidget(0, 3, subjectBox);
                tempTable.setWidget(0, 4, fromUserBox);
                tempTable.setWidget(0, 5, defaultyesOrNo);

                TextArea bodyArea = new TextArea();
                bodyArea.setVisible(false);
                bodyArea.setText(item.getMessageHTML());
                tempTable.setWidget(0, 6, bodyArea);

                TextBox toBox = new TextBox();
                toBox.setVisible(false);
                toBox.setText(item.getTestEmail());
                tempTable.setWidget(0, 7, toBox);*/

                EMLItemsTable itemsTable = new EMLItemsTable(item, i);

                templateTable.add(itemsTable);
            }
        }
    }

    private void save() {
        if (!validate()) {
            return;
        }
        EmailTemplateItem[] items = new EmailTemplateItem[templateTable.getWidgetCount()];
        for (int i = 0; i < templateTable.getWidgetCount(); i++) {

            FlexTable fTable = (FlexTable) templateTable.getWidget(i);

            for (int j = 0; j < fTable.getRowCount(); j++) {
                items[i] = new EmailTemplateItem();
                TextBox nameBox = (TextBox) fTable.getWidget(j, 1);
                items[i].setName(nameBox.getText());

                DataListBox categoryBox = (DataListBox) fTable.getWidget(j, 2);
                items[i].setCategoryId(categoryBox.getSelectedItem().getId());
                items[i].setCategoryName(categoryBox.getSelectedItem().getName());

                TextBox subjectBox = (TextBox) fTable.getWidget(j, 3);
                items[i].setSubject(subjectBox.getText());

                DataListBox fromUserBox = (DataListBox) fTable.getWidget(j, 4);
                items[i].setFromUserID(fromUserBox.getSelectedItem().getId());
                items[i].setFromUserName(fromUserBox.getSelectedItem().getName());

                DataListBox defaultYesNoBox = (DataListBox) fTable.getWidget(j, 5);
                items[i].setDefault(Integer.valueOf(0).equals(defaultYesNoBox.getSelectedItem().getId()));

                TextArea bodyArea = (TextArea) fTable.getWidget(j, 6);
                items[i].setMessageHTML(bodyArea.getText());

                TextBox toBox = (TextBox) fTable.getWidget(j, 7);
                items[i].setTestEmail(toBox.getText());
            }
        }

        LoadingPanel.loading(true);
        ProfileService.App.get().createEmailTemplate(items, new AbstractAsyncCallback<Integer[]>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);

            }

            @Override
            public void success(Integer[] result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.emailTemplate()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_TEMPLATES_LIST_ADD, result, CreateEMLTemplatesView.this);
                closeTab();
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        for (int i = 0; i < templateTable.getWidgetCount(); i++) {
            FlexTable fTable = (FlexTable) templateTable.getWidget(i);

            for (int j = 0; j < fTable.getRowCount(); j++) {
                TextBox nameBox = (TextBox) fTable.getWidget(j, 1);
                if (!Validation.validateTextBoxRequired(nameBox)) {
                    errors++;
                }

                DataListBox categoryBox = (DataListBox) fTable.getWidget(j, 2);
                if (!Validation.validateListBoxRequired(categoryBox, new HTML(), "")) {
                    errors++;
                }

                TextBox subjectBox = (TextBox) fTable.getWidget(j, 3);
                if (!Validation.validateTextBoxRequired(subjectBox)) {
                    errors++;
                }

                DataListBox fromUserBox = (DataListBox) fTable.getWidget(j, 4);
                if (!Validation.validateListBoxRequired(fromUserBox, new HTML(), "")) {
                    errors++;
                }

                DataListBox defaultYesNoBox = (DataListBox) fTable.getWidget(j, 5);
                if (!Validation.validateListBoxRequired(defaultYesNoBox, new HTML(), "")) {
                    errors++;
                }
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    @Override
    public String getIconStyle() {
        return null;
    }


    public class EMLItemsTable extends FlexTable {
        private HTML numberCharacter;
        private TextBox name;
        private DataListBox templateCategory;
        private DataListBox defaultYesOrNo;
        private TextBox subject;
        private DataListBox fromUser;
        private TextArea body;
        private TextBox to;

        private final EmailTemplateItem emailTemplateItem;
        private final int count;

        public EMLItemsTable(EmailTemplateItem emailTemplateItem, int count) {
            this.emailTemplateItem = emailTemplateItem;
            this.count = count;
            initialize();
        }

        private void initialize() {
            setCellSpacing(5);
            setWidth("710px");
            numberCharacter = new HTML("<b>" + (count + 1) + ".</b>");
            numberCharacter.setWidth("20px");

            name = new TextBox();
            name.setWidth("110px");
            name.setHeight("20px");
            name.setText(emailTemplateItem.getName());
            templateCategory = new DataListBox();
            templateCategory.setWidth("110px");
            templateCategory.setHeight("20px");
            if (templateItems != null) {
                templateCategory.setItems(templateItems);
                templateCategory.setSelected(emailTemplateItem.getCategoryId());
            }
            defaultYesOrNo = new DataListBox();
            defaultYesOrNo.setWidth("80px");
            defaultYesOrNo.setHeight("20px");
            SelectItem[] YES_NO_LIST = new SelectItem[]{
                    new SelectItem(0, wfmStrings.yes()),
                    new SelectItem(1, wfmStrings.no())};

            defaultYesOrNo.setItems(YES_NO_LIST);
            if (emailTemplateItem.isDefault()) {
                defaultYesOrNo.setSelected(0);
            } else {
                defaultYesOrNo.setSelected(1);
            }
            subject = new TextBox();
            subject.setWidth("110px");
            subject.setHeight("20px");
            subject.setText(emailTemplateItem.getSubject());
            fromUser = new DataListBox();
            fromUser.setWidth("110px");
            fromUser.setHeight("20px");
            if (fromUsersItems != null) {
                fromUser.setItems(fromUsersItems);
                fromUser.setSelected(emailTemplateItem.getFromUserID());
            }
            body = new TextArea();
            body.setVisible(false);
            body.setText(emailTemplateItem.getMessageHTML());
            to = new TextBox();
            to.setVisible(false);
            to.setText(emailTemplateItem.getTestEmail());

            SimpleLink removeLink = new SimpleLink(wfmStrings.delete());
            if (generalTemplateItem != null && generalTemplateItem.length > 1) {
            } else {
                removeLink.setVisible(false);
            }
            removeLink.addClickHandler(event -> {
                if (templateTable.getWidgetCount() > 1) {
                    remove();
                } else {
                    Info.show(wfmStrings.youCanNotRemoveOneLineItem(), Info.Type.WARNING);
                }
            });
            setWidget(0, 0, numberCharacter);
            setWidget(0, 1, name);
            setWidget(0, 2, templateCategory);
            setWidget(0, 3, subject);
            setWidget(0, 4, fromUser);
            setWidget(0, 5, defaultYesOrNo);
            setWidget(0, 6, body);
            setWidget(0, 7, to);
            setWidget(0, 8, removeLink);
            getFlexCellFormatter().setWidth(0, 8, "45px");
        }

        private void remove() {
            removeFromParent();
        }
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
