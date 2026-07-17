package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.PDFSettingsTransObject;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.PdfGenerateTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import static java.util.Arrays.asList;

public class UploadAIPhantomPdfView extends CustomForm implements Colapse {
    private static final BackendStrings backendStrings = BackendStrings.App.get();

    private final Integer companyId;
    public GeneralFileUpload uploadForm;
    private WfmDropdown pdfType;
    private TextBox templateName;
    private WfmButton2 saveBtn;

    public UploadAIPhantomPdfView(Integer companyId) {
        super("newpdftemplate", "Add AI Phantom Pdf " + companyId);
        this.companyId = companyId;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        uploadForm = new GeneralFileUpload(Constants.F_AIPHANTOM_PDF, null, null);
        uploadForm.ensureDebugId("aiphantom_pdf" + "uploadForm");

        pdfType = new WfmDropdown();
        templateName = new TextBox();

        addField(CustomFormConstants.ATTACHMENTS, uploadForm, getTitle(wfmStrings.uploadImage()));
        addField(CustomFormConstants.TYPE, pdfType, getTitle(backendStrings.pdfType(), true));
        addField(CustomFormConstants.NAME, templateName, getTitle(wfmStrings.template(), true));
        show();
        return null;

    }

    @Override
    protected void addButtons() {
        saveBtn = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveBtn.ensureDebugId("aiphantom_pdf" + "saveBtn");
        saveBtn.addClickHandler(event -> save());
        addButton(saveBtn);
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        BackendService.App.get().getCompanyPDFSettings(companyId, null, new AbstractAsyncCallback<PDFSettingsTransObject>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.errorOccurredSavingChanges(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(PDFSettingsTransObject result) {
                LoadingPanel.loading(false);
                pdfType.setItems(asList(result.getReferences()));
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();

        errors += markAsError(pdfType, !Validation.validateWfmDropdown(pdfType));
        errors += markAsError(templateName, !Validation.validateTextBoxRequired(templateName));

        if (uploadForm.getAttachedFiles().length == 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            errors++;
        } else {
//            for (FileItem attachedFile : uploadForm.getAttachedFiles()) {
//                attachedFile.get
//            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void save() {
        enableBtn(false);
        if (!validate()) {
            enableBtn(true);
            return;
        }

        PDFSettingsTransObject transObject = new PDFSettingsTransObject();
        transObject.setCompanyID(companyId);
        transObject.setPdfReferenceID(pdfType.getSelectedId());
        transObject.setTemplateName(templateName.getText());
        transObject.setGenerateType(PdfGenerateTypeEnum.PHANTOM_JS);
        transObject.setAttachedFiles(uploadForm.getAttachedFiles());

        LoadingPanel.loading(true);
        BackendService.App.get().saveAiPhantomPdfTemplate(transObject, new AbstractAsyncCallback<Integer>() {

            public void failure(Throwable caught) {
                enableBtn(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer result) {
                LoadingPanel.loading(false);
                if (result == null) {
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                    enableBtn(true);
                } else {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PDF_TEMPLATE_SAVED, null, UploadAIPhantomPdfView.this);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.template()));
                    closeTab();
                }
            }

        });
    }

    private void enableBtn(boolean enable) {
        saveBtn.setEnabled(enable);
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.AIPHANTOM_PDF_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
