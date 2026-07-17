package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * Created by Khasan on 15.10.14.
 */
public class CertificateTemplateView extends CustomizeCertificateView {

    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private HTML name, description, type, content, customHtml;
    private CertificateItem item;

    public CertificateTemplateView(Integer objectID) {
        this.certificateTypeId = objectID;
    }

    @Override
    protected void addButtons() {

        addEditButton().addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customizeCertificate|add/add/" + certificateTypeId, item.getName()));


        addRemoveButton().addClickHandler(clickEvent -> {
            final WfmMessageBox message = new WfmMessageBox(IconEnum.INFO, Action.YesNo, true);

            message.setTitle(wfmStrings.warning());
            message.setMessage(wfmStrings.sureYouWantToDelete());
            message.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    ProfileService.App.get().deleteCertificateType(certificateTypeId, new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Boolean result) {
                            if (result) {
                                LoadingPanel.loading(false);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.certificate()), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.DELETE_CERTIFICATE_TYPE, result, CertificateTemplateView.this);
                                closeTab();
                            }
                        }
                    });
                }
            });
            message.open();
        });
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        HrmsService.App.get().getCertificateTypeData(certificateTypeId, employeeID, new AbstractAsyncCallback<CertificateItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(CertificateItem result) {
                LoadingPanel.loading(false);
                if (result != null) {
                   item = result;
                   name.setHTML(result.getName());
                   description.setHTML(result.getDescription());
                   type.setHTML(result.getType() != null ? result.getType().getName() : "");
                   content.setHTML(result.getContent());
                   customHtml.setHTML(result.getCustomHTMLcontent());
                }
            }
        });
    }

    protected void initialize() {
        name = new HTML();
        description = new HTML();
        type = new HTML();
        content = new HTML();
        templateMessageHTML = new HTML("<b>" + getTitle("HTML", true) + "</b>");
        templateMessageContentTable = new FlexTable();
        templateMessageContentTable.setWidget(0, 0, templateMessageHTML);
        templateMessageContentTable.setWidget(0, 1, content);
        templateMessageContentTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        templateMessageContentTable.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        templateMessageContentTable.getCellFormatter().setWidth(0, 0, "145px");

        customHTMLLabel = new HTML("<b>" + getTitle(hrmsStrings.customHTMLTemplate()) + "</b>");
        customHtml = new HTML();

        customHTMLContentTable = new FlexTable();
        customHTMLContentTable.setWidget(0, 0, customHTMLLabel);
        customHTMLContentTable.setWidget(0, 1, customHtml);
        customHTMLContentTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        customHTMLContentTable.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        customHTMLContentTable.getCellFormatter().setWidth(0, 0, "145px");

        addTitleField(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.TEMPLATE_INFORMATION, hrmsStrings.templateInformation());
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.DESCRIPTION, description, wfmStrings.description());
        addField(CustomFormConstants.TYPE, type, wfmStrings.type());
        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.certificateTemplate());
        addField(CustomFormConstants.MESSAGE_CONTENT, templateMessageContentTable, null);
        addTitleField(CustomFormConstants.CUSTOM_HTML_TEMPLATE, hrmsStrings.customHTMLTemplate());
        addField(CustomFormConstants.CUSTOM_HTML, customHTMLContentTable, null);

        show();
    }
}
