//package com.edatasite.workforce.gwt.profile.client.ui.view;
//
//import com.edatasite.workforce.gwt.core.client.Validation;
//import com.edatasite.workforce.gwt.core.client.View;
//import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
//import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSettings;
//import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
//import com.edatasite.workforce.gwt.core.client.ui.IconsBundle;
//import com.edatasite.workforce.gwt.core.client.ui.KpiHelpButton;
//import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
//import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
//import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
//import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
//import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
//import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.event.dom.client.ChangeEvent;
//import com.google.gwt.event.dom.client.ChangeHandler;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.ui.*;
//
///**
// * Created by IntelliJ IDEA.
// * User: Virus
// * Date: 7/14/11
// * Time: 12:26 PM
// * To change this template use File | Settings | File Templates.
// */
//public abstract class AbstractAddSmsView extends View {
//    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
//    protected FlexTable generalTable;
//    private WfmForm tableHeader;
//    private VerticalPanel dialogPanel;
//    private WfmForm.Field entityNameField;
//    protected WfmForm table;
//    protected WfmForm.Field smsFromNameField;
//    protected DataListBox providers;
//    protected TextBox smsFromNameBox;
//    protected WfmForm.Field smsApiIdField;
//    protected TextBox smsApiIdBox;
//    private WfmForm.Field smsApiUserNameField;
//    protected TextBox smsApiUserNameBox;
//    private WfmForm.Field smsApiPasswordField;
//    protected WfmForm.Field smsCreditCountField;
//    protected PasswordTextBox smsApiPasswordBox;
//    private static IconsBundle imageBundle = (IconsBundle) GWT.create(IconsBundle.class);
//    protected WfmMessageBox dialogBoxError = null;
//    private WfmButton2 saveAndClose;
//    private WfmButton2 save;
//    protected Label creditcount;
////    protected SimpleLink link;
//
//    public AbstractAddSmsView(String viewName, String description) {
//        super(viewName, description);
//    }
//
//    protected abstract String getFormName();
//
//    protected abstract KpiHelpButton addHelpButton();
//
//    protected abstract void getSmsProviders();
//
//    protected abstract void fillData();
//
//    protected abstract void saveSmsSettings();
//
//    /**
//     * Save And Another Buttons initializations
//     */
//    private void drawCustomFieldsSaveBotton() {
//        saveAndClose = new WfmButton2(wfmStrings.saveClose());
//        saveAndClose.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent sender) {
//                if (valid()) {
//                    saveSmsSettings();
//                    closeTab();
//                }
//            }
//        });
//        save = new WfmButton2(wfmStrings.save());
//        save.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent sender) {
//                if (valid()) {
//                    saveSmsSettings();
//                }
//            }
//        });
//
//        table.addButton(save);
//        table.addButton(saveAndClose);
//    }
//
//    private void drawGeneralCustomFields() {
//        generalTable.setCellPadding(5);
//        generalTable.setCellSpacing(5);
//        generalTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
//        generalTable.setWidget(0, 0, table);
//        generalTable.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
//
//        if (dialogBoxError != null) {
//            dialogPanel.add(generalTable);
//            dialogPanel.add(new HTML("<div class=line></div>"));
//            dialogPanel.add(new HTML("<HR/>"));
//        } else {
//            add(generalTable);
//            add(new HTML("<div class=line></div>"));
//        }
//    }
//
//    private void drawCustomFieldsForm() {
//
//        entityNameField = table.addField(wfmStrings.smsProvider(), providers, true);
////        entityNameField = table.addField(crmStrings.smsProvider(), new Widget[]{providers, new HTML("&nbsp&nbsp&nbsp&nbsp"), link}, true);
//        smsFromNameField = table.addField(wfmStrings.smsFromName(), smsFromNameBox, true);
//        smsApiIdField = table.addField(wfmStrings.smsApiId(), smsApiIdBox, true);
//        smsApiUserNameField = table.addField(wfmStrings.smsApiUserName(), smsApiUserNameBox, true);
//        smsApiPasswordField = table.addField(wfmStrings.smsApiPassword(), smsApiPasswordBox, true);
//        smsCreditCountField = table.addField(wfmStrings.creatditLeft(), creditcount, false);
//    }
//
//    @Override
//    public String getIconStyle() {
//        return "icon-settings-user-credentials";
//    }
//
//    private void drawFormHeader() {
//        tableHeader = new WfmForm(new String[]{"100%"});
//        tableHeader.addButton(addHelpButton());
//        tableHeader.addWidget(new HTML("<b class=customTitle><font size=+1>" + getFormName() + " </font></b>"));
//        if (dialogPanel != null) {
//            dialogPanel.add(tableHeader);
//            dialogPanel.add(new HTML("<div class=line></div>"));
//        } else {
//            add(tableHeader);
//            add(new HTML("<div class=line></div>"));
//        }
//    }
//
//
//
//    @Override
//    protected Widget onInitialize() {
//        initInternal();
//        dialogBoxError = new WfmMessageBox(IconEnum.ERROR, Action.OK);
//        return null;
//    }
//
//    private void smsProviderNameBox_select() {
//        providers.addValueChangeHandler(new ChangeHandler() {
//            @Override
//            public void onChange(ChangeEvent event) {
//                smsProviderSetSelected();
//            }
//        });
//    }
//
//    protected void smsProviderSetSelected() {
//        smsFromNameBox.setText("");
//        smsApiIdBox.setText("");
//        smsApiUserNameBox.setText("");
//        smsApiPasswordBox.setText("");
//        creditcount.setText("");
//        if(null== providers.getSelectedItem()){
//
//            smsApiIdField.setVisible(false);
//            smsFromNameField.setVisible(false);
//            smsCreditCountField.setVisible(false);
//            smsApiPasswordField.setVisible(false);
//            smsApiUserNameField.setVisible(false);
//            save.setVisible(false);
//            saveAndClose.setVisible(false);
//            return;}
//        if (providers.getSelectedItem().getName().toLowerCase().equals(SmsSettings.CLICKATELL.toLowerCase())) {
//            smsCreditCountField.setVisible(true);
//            smsApiUserNameField.setVisible(true);
//            smsApiPasswordField.setVisible(true);
//            smsFromNameField.setVisible(true);
//            smsFromNameField.setLabelText(wfmStrings.smsFromName());
//            smsApiIdField.setVisible(true);
//        } else if (providers.getSelectedItem().getName().toLowerCase().equals(SmsSettings.MVAAYOO.toLowerCase())) {
//            smsCreditCountField.setVisible(true);
//            smsApiUserNameField.setVisible(true);
//            smsApiPasswordField.setVisible(true);
//            smsApiIdField.setVisible(false);
//            smsFromNameField.setVisible(true);
//            smsFromNameField.setLabelText("SMS sender ID");
//        }
//        save.setVisible(true);
//        saveAndClose.setVisible(true);
//        fillData();
//    }
//
//    protected abstract void creditLeft();
//
//    protected void initInternal() {
//        generalTable = new FlexTable();
//
//        table = new WfmForm();
//        table.setLabelSize("150px");
//        table.setLabelAlignment(WfmForm.ALIGN_RIGHT);
//
//        creditcount = new Label();
//
//        providers = new DataListBox();
////        getSmsProviders();//providers ga tabledan fill qilish
//        providers.setWidth("250px");
//
////        link = new SimpleLink(crmStrings.registration());
////        link.getElement().getStyle().setFontSize(10, Style.Unit.PT);
////        link.getElement().getStyle().setFontWeight(Style.FontWeight.BOLDER);
////        link.addClickHandler(new ClickHandler() {
////            @Override
////            public void onClick(ClickEvent clickEvent) {
////                if (providers.getSelectedItem() == null) {
////                    dialogBoxError = new WfmMessageBox(IconEnum.WARN, Action.OK, crmStrings.chooseSmsProvider());
////                    dialogBoxError.center();
////                    return;
////                }
////                String url = SmsSettings.chooseRegistration(providers.getSelectedItem().getName());
////                if (Utils.isNullOrEmpty(url)) {
////                    dialogBoxError = new WfmMessageBox(IconEnum.WARN, Action.OK, crmStrings.chooseSmsProvider());
////                    dialogBoxError.center();
////                } else {
////                    Utils.openURL(url);
////                }
////            }
////        });
//
//        smsFromNameBox = new TextBox();
//        smsFromNameBox.setWidth("250px");
//
//        smsApiIdBox = new TextBox();
//        smsApiIdBox.setWidth("250px");
//
//        smsApiUserNameBox = new TextBox();
//        smsApiUserNameBox.setWidth("250px");
//
//        smsApiPasswordBox = new PasswordTextBox();
//        smsApiPasswordBox.setWidth("250px");
//
//        if (dialogBoxError != null) {
//            dialogPanel = new VerticalPanel();
//        }
//
//        drawFormHeader();
//        drawCustomFieldsForm();
//        drawGeneralCustomFields();
//        drawCustomFieldsSaveBotton();
//        smsProviderNameBox_select();
//        getSmsProviders();//providers ga tabledan fill qilish
//        if (dialogBoxError != null) {
//            dialogBoxError.add(dialogPanel);
//            dialogBoxError.center();
//        }
//    }
//
//    private boolean valid() {
//        int errors = 0;
//        table.cleanupErrors();
//        if (!providers.getSelectedItem().getName().toLowerCase().equals(SmsSettings.MVAAYOO.toLowerCase())) {
//            if (!Validation.validateTextBoxRequired(smsApiIdBox, smsApiIdField)) {
//                errors++;
//            }
//            if (!Validation.validateTextBoxRequired(smsFromNameBox, smsFromNameField)) {
//                errors++;
//            }
//        }
//
//        if (!Validation.validateTextBoxRequired(smsApiUserNameBox, smsApiUserNameField)) {
//            errors++;
//        }
//        if (!Validation.validateTextBoxRequired(smsApiPasswordBox, smsApiPasswordField)) {
//            errors++;
//        }
//        if (errors > 0) {
//             Info.show("", wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
//            return false;
//        }
//        return true;
//    }
//}