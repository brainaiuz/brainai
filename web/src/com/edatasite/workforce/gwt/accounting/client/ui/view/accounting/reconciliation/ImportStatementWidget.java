package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.reconciliation;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccountAttachment;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;

import static com.edatasite.workforce.gwt.core.client.CommandConstants.BANK_ACCOUNT_ID;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.BANK_ACCOUNT_TYPE;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.SESSION_ID_PARAM_NAME;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SESSION_ID_COOKIE;
import static gwt.material.design.client.js.JsMaterialElement.$;

public class ImportStatementWidget extends Composite {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    interface ImportStatementWidgetUiBinder extends UiBinder<HTMLPanel, ImportStatementWidget> {
    }

    @UiField
    protected Button nextButton;

    @UiField
    protected Button backButton;
    @UiField
    protected HTMLPanel mainStepsContainer;
    @UiField
    protected HTMLPanel uploadStepTab;
    @UiField
    protected HTMLPanel importSettingsTab;
    @UiField
    protected HTMLPanel reviewStepTab;


    protected int currentStep = 1;
    private UploadWidget uploadWidget;
    private ImportSettingsWidget ImportSettingsWidget;
    private ReviewWidget ReviewWidget;
    private WfmFormPanel form;

    private Integer bankAccountID;
    private String bankAccountName;
    protected Integer bankAccountAttachmentID;
    private ImportStatementView importStatementView;

    private static final ImportStatementWidgetUiBinder uiBinder = GWT.create(ImportStatementWidgetUiBinder.class);


    public ImportStatementWidget(Integer bankAccountID, String bankAccountName,ImportStatementView importStatementView) {
        this.bankAccountID = bankAccountID;
        this.bankAccountName = bankAccountName;
        this.importStatementView = importStatementView;
        this.currentStep = importStatementView.getCurrentStep();
        if (importStatementView.getCurrentStep() >1) {
            bankAccountAttachmentID = importStatementView.getBankAccountAttachmentId();
        }
        initWidget(uiBinder.createAndBindUi(this));
        MainLayout.get().makeFrameContainerHaveTabsStyle(false);
        MainLayout.get().considerBodyHasFittedContent(true);
        initSteps();
        loadStep(currentStep);
        initialize();
    }

    private void initialize() {
        if (currentStep == 1) {
            form = uploadWidget.getForm();

            FileUpload fileInput1 = uploadWidget.getFileInput();
            loadFiles();
            form.addFormHandler(new FormHandler() {
                public void onSubmit(FormSubmitEvent event) {

                }

                public void onSubmitComplete(FormSubmitCompleteEvent event) {
                    LoadingPanel.loading(false);
                    Info.show(form.getErrorString() != null ? wfmStrings.messParseErrorCompareFile() : Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.file()), Info.Type.INFO);
                    if (form.getObjectID() != null) {
                        bankAccountAttachmentID = form.getObjectID();
                        importStatementView.setBankAccountAttachmentId(bankAccountAttachmentID);
                    }
                    loadFiles();
                }
            });

            form.setParameter(SESSION_ID_PARAM_NAME, Cookies.getCookie(SESSION_ID_COOKIE));
            form.setParameter(CommandConstants.DESCRIPTION_PARAM_NAME, "");
            form.setParameter(CommandConstants.UPLOAD_TYPE_PARAM_NAME, Utils.getUploadTypeParam());
            form.setParameter(BANK_ACCOUNT_ID, bankAccountID.toString());
            form.setParameter(BANK_ACCOUNT_TYPE, "CSV");


            fileInput1.addChangeHandler(changeEvent -> {
                if (fileInput1.getFilename() != null && !"".equals(fileInput1.getFilename())) {
                    if (".csv".equals(fileInput1.getFilename().substring(fileInput1.getFilename().lastIndexOf(".")))) {
                        $(form.getElement()).trigger("submit", null);
                        LoadingPanel.loading(true);
                    } else {
                        nextButton.setEnabled(false);
                        Info.show(wfmStrings.error() + " " + accountingStrings.wrongFileType(), Info.Type.WARNING);
                    }
                }
            });
        }
        nextButton.addClickHandler(event -> {
            if (currentStep < 4) {
                currentStep++;
                importStatementView.setCurrentStep(currentStep);
                if (currentStep != 3) {
                    initSteps();
                    loadStep(currentStep);
                }
            }
        });
        nextButton.setText("Continue to import settings");

        nextButton.setStyleName("btn btn--primary");


        backButton.addClickHandler(event -> {
            if (currentStep > 1) {
                currentStep--;
                importStatementView.setCurrentStep(currentStep);
                loadStep(currentStep);
            }
        });
        backButton.setText(wfmStrings.back());
        backButton.setStyleName("btn btn--primary");
        if (currentStep == 1) {
            uploadStepTab.setStyleName("tab tab--active btn active");
        }
    }

    private void loadFiles() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                LoadingPanel.loading(true);
                AccountingService.App.get().getBankAccountFilesList(bankAccountID, new AbstractAsyncCallback<BankAccountAttachment[]>() {
                    public void onSuccess(BankAccountAttachment[] result) {
                        LoadingPanel.loading(false);
                        if (result != null && result.length > 0) {
                            FlexTable table = uploadWidget.getFiles();
                            table.setStyleName("table table--striped table--hover");
                            table.setCellPadding(5);
                            table.setCellSpacing(0);
                            table.setWidth("100%");
                            table.addStyleName("table table--striped table--hover");
                            table.setHTML(0, 0, "<b>" + wfmStrings.file() + "</b>");
                            table.setHTML(0, 1, "<b>" + wfmStrings.action() + "</b>");
                            for (int i = 0; i < result.length; i++) {
                                BankAccountAttachment attachment = result[i];
                                table.setHTML(i + 1, 0, attachment.getName());
                                Anchor download = new Anchor(wfmStrings.download());
                                download.addClickHandler(new ClickHandler() {
                                    public void onClick(ClickEvent event) {
                                        bankAccountAttachmentID = attachment.getObjectID();
                                        currentStep++;
                                        importStatementView.setBankAccountAttachmentId(bankAccountAttachmentID);
                                        importStatementView.setCurrentStep(currentStep);
                                        initSteps();
                                        loadStep(currentStep);
                                    }
                                });
                                table.setWidget(i + 1, 1, download);
                            }
                        }
                    }
                });
            }
        });
    }


    protected void initSteps() {
        if (currentStep == 1) {
            uploadWidget = new UploadWidget();
        } else if (currentStep == 2) {
            ImportSettingsWidget = new ImportSettingsWidget(this);
        } else if (currentStep == 3) {
            ReviewWidget = new ReviewWidget(bankAccountAttachmentID);
        }
    }

    protected void loadStep(int step) {
        // Tablar uchun UI elementlarini olish
        uploadStepTab.removeStyleName("active");
        importSettingsTab.removeStyleName("active");
        reviewStepTab.removeStyleName("active");

        uploadStepTab.removeStyleName("btn active");
        importSettingsTab.removeStyleName("btn active");
        reviewStepTab.removeStyleName("btn active");
//        uploadStepTab.getElement().removeAttribute();


        mainStepsContainer.clear();
        switch (step) {
            case 1:
                mainStepsContainer.add(uploadWidget);
                uploadStepTab.addStyleName("btn active");
                uploadStepTab.getElement().addClassName("active");
//                uploadStepTab.getElement().getParentElement().setAttribute("style", "tab active");
//                uploadStepTab.getElement().getParentElement().addClassName("active");
                nextButton.setText("Continue to import settings");
                backButton.setVisible(false);
                nextButton.setVisible(true);
                break;
            case 2:
                mainStepsContainer.add(ImportSettingsWidget);
                importSettingsTab.setStyleName("active");
                importSettingsTab.addStyleName("btn active");
                nextButton.setText("Continue to review");
                backButton.setVisible(true);
                nextButton.setVisible(true);
                break;
            case 3:
                mainStepsContainer.remove(uploadStepTab);
                mainStepsContainer.add(ReviewWidget);
                reviewStepTab.setStyleName("active");
                reviewStepTab.addStyleName("btn active");
                nextButton.setText("Complete");
                backButton.setVisible(true);
                nextButton.setVisible(false);
                break;
        }
    }

}