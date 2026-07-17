package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * Created by Khasan on 30.09.14.
 */
public class CustomizeCertificateView extends CustomForm implements Colapse, Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    protected Integer employeeID;
    private WfmButton2 saveButton;
    protected Integer certificateTypeId;
    protected FlexTable templateMessageContentTable;
    protected FlexTable customHTMLContentTable;
    protected HTML templateMessageHTML;
    protected HTML customHTMLLabel;
    private KpiEditor editorHTML;
    private TextBox name;
    private TextArea2 description;
    private DataListBox type;
    private KpiCheckBox pdfHeaderFooter;
    private CertificateItem certificateItem;
    private TextArea customHTML;
    private DataListBox personalizedItems;
    private DataListBox htmlAttributes;
    private VerticalPanel personalAttr, htmlAttr;

    public CustomizeCertificateView(Integer integer) {
        super("add", "Customize Certificate");
        this.certificateTypeId = integer;
    }

    public CustomizeCertificateView(Integer integer, Integer employeeID) {
        super("add", "Customize Certificate");
        this.certificateTypeId = integer;
        this.employeeID = employeeID;
    }

    public CustomizeCertificateView() {
        super("add", hrmsStrings.addCertificateTemplate());
    }

    @Override
    protected void addButtons() {
        saveButton = addButton(wfmStrings.save(), event -> save());

    }

    private void save() {
        if (valid()) {
            enableButton(false);
            LoadingPanel.loading(true);
            CertificateItem item = new CertificateItem();
            item.setObjectId(certificateItem != null ? certificateItem.getObjectId() : null);
            item.setName(name.getText());
            item.setDescription(description.getText());
            item.setType(type.getSelectedItem());
            item.setPdfHeaderFooter(pdfHeaderFooter.getValue());
            item.setContent(editorHTML.getData());
            String encodedHtml = SafeHtmlUtils.htmlEscape(customHTML.getText());
            item.setCustomHTMLcontent(encodedHtml);
            item.setCreationDate(new Date());
            item.setFormID(certificateItem != null ? certificateItem.getFormID() : null);
            if (item.getFormID() == null) {
                item.setFormID(item.getName().replace(" ", "_").toUpperCase() + "_FORM");
            }

            HrmsService.App.get().checkCertificateTypeName(item.getObjectId(), item.getName(), new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Boolean isChecked) {
                    if (isChecked) {
                        HrmsService.App.get().saveCertificateType(item, new AbstractAsyncCallback<Integer>() {
                            @Override
                            public void failure(Throwable throwable) {
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                saveButton.setEnabled(true);
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void success(Integer result) {
                                LoadingPanel.loading(false);
                                saveButton.setEnabled(true);
                                Info.show("Certificate Template saved successfully", Info.Type.INFO);
                                closeTab();
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.UPDATE_CERTIFICATE_TYPE, null, CustomizeCertificateView.this);
                            }
                        });
                    } else {
                        Info.show(hrmsStrings.certificateTemplateOrWorkflowWithThisNameAlreadyExists(), Info.Type.WARNING);
                        name.addStyleName(ERROR_FORM_STYLE);
                        LoadingPanel.loading(false);
                    }
                }
            });
        }
    }

    private boolean valid() {
        int errors = 0;
        clearErrorStyle();
        errors += markAsError(editorHTML, (!Validation.validateMaterialEditorRequired(editorHTML) && "".equals(customHTML.getText())));
        errors += markAsError(name, !Validation.validateTextBoxRequired(name));
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    protected void initialize() {
        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);

        description = new TextArea2(255, wfmStrings.description());

        type = new DataListBox();
        type.addStyleName(DEFAULT_WIDTH);

        pdfHeaderFooter = new KpiCheckBox();

        editorHTML = new KpiEditor();
        editorHTML.addStyleName(Constants.DEFAULT_WIDTH);

        personalizedItems = new DataListBox();
        personalizedItems.addStyleName(Constants.DEFAULT_WIDTH);
        personalizedItems.setVisible(false);
        personalizedItems.addValueChangeHandler(changeEvent -> {
            if (!personalAttr.isVisible()) {
                personalAttr.setVisible(true);
            }
            personalAttr.add(new HTML(personalizedItems.getSelectedItem().getName()));
        });

        htmlAttributes = new DataListBox();
        htmlAttributes.addStyleName(Constants.DEFAULT_WIDTH);
        htmlAttributes.setVisible(false);
        htmlAttributes.addValueChangeHandler(changeEvent -> {
            if (!htmlAttr.isVisible()) {
                htmlAttr.setVisible(true);
            }
            htmlAttr.add(new HTML(htmlAttributes.getSelectedItem().getName()));
        });

        personalAttr = new VerticalPanel();
        personalAttr.setVisible(false);
        htmlAttr = new VerticalPanel();
        htmlAttr.setVisible(false);

        customHTMLLabel = new HTML("<b>" + getTitle(hrmsStrings.customHTMLTemplate()) + "</b>");

        customHTML = new TextArea();
        customHTML.addStyleName(Constants.DEFAULT_WIDTH);
        customHTML.setHeight("400px");

        customHTMLContentTable = new FlexTable();
        customHTMLContentTable.setWidget(0, 0, customHTMLLabel);
        customHTMLContentTable.setWidget(1, 0, customHTML);
        customHTMLContentTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        customHTMLContentTable.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        customHTMLContentTable.getCellFormatter().setWidth(0, 1, Constants.DEFAULT_WIDTH);

        addTitleField(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.TEMPLATE_INFORMATION, hrmsStrings.templateInformation());
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.DESCRIPTION, description, null);
        addField(CustomFormConstants.TYPE, type, wfmStrings.type());
        addField(CustomFormConstants.PDF_HEADER_FOOTER, pdfHeaderFooter, hrmsStrings.pdfHeaderFooter());
        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.certificateTemplate());
        addField("PERSONALIZATION_ATTRIBUTES", personalizedItems, getTitle(wfmStrings.personalizationAttributes()));
        addField("HTML_ATTRIBUTES", htmlAttributes, getTitle(hrmsStrings.htmlAttributes()));
        addField("PERSONALIZATION_ATTRIBUTES_PANEL", personalAttr, null);
        addField("HTML_ATTRIBUTES_PANEL", htmlAttr, null);
        addField(CustomFormConstants.MESSAGE_CONTENT, editorHTML, getTitle("HTML"));
        addTitleField(CustomFormConstants.CUSTOM_HTML_TEMPLATE, hrmsStrings.customHTMLTemplate());
        addField(CustomFormConstants.CUSTOM_HTML, customHTMLContentTable, null);
        show();
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
                    certificateItem = result;
                    personalizedItems.setVisible(true);
                    htmlAttributes.setVisible(true);
                    name.setText(result.getName());
                    description.setText(result.getDescription());
                    type.setItems(result.getTypes());
                    if (result.getType() != null) {
                        type.setSelected(result.getType());
                    } else {
                        type.setSelectedNullLabel();
                    }
                    pdfHeaderFooter.setValue(result.isPdfHeaderFooter());
                    editorHTML.setData(result.getContent());
                    customHTML.setText(result.getCustomHTMLcontent());

                    Integer k = 0;
                    if (result.getFields() != null) {
                        SelectItem[] persItem = new SelectItem[result.getFields().length];
                        for (SelectItem item : result.getFields()) {
                            persItem[k] = new SelectItem(k, item.getDescription());
                            k++;
                        }
                        personalizedItems.setItems(persItem);
                    }

                    htmlAttributes.setItems(CertificateUtils.getCertificateFields());

                    boolean hasCustomField = true;
                    for (CompanyCustomFieldItem customField : result.getCustomFields()) {
                        if (hasCustomField) {
                            htmlAttributes.addListItem(new SelectItem(htmlAttributes.getItemCount() + 1, "--CUSTOM FIELDS--"));
                        }
                        hasCustomField = false;
                        htmlAttributes.addListItem(new SelectItem(htmlAttributes.getItemCount() + 1, "${" + Utils.refactor(customField.getFieldName(), false) + "}"));
                    }

                    boolean hasLr = true;
                    for (CompanyCustomFieldItem customField : result.getLeaveRequestCustomFields()) {
                        if (hasLr) {
                            htmlAttributes.addListItem(new SelectItem(htmlAttributes.getItemCount() + 1, "--LEAVE REQUEST CUSTOM FIELDS--"));
                        }
                        hasLr = false;
                        htmlAttributes.addListItem(new SelectItem(htmlAttributes.getItemCount() + 1, "${" + Utils.refactor(customField.getFieldName()) + "}"));
                    }
                    boolean hasAttachment = true;
                    for (int i = 1; i <= 3; i++) {
                        if (hasAttachment) {
                            htmlAttributes.addListItem(new SelectItem(htmlAttributes.getItemCount() + 1, "--ATTACHMENTS--"));
                        }
                        hasAttachment = false;
                        htmlAttributes.addListItem(new SelectItem(htmlAttributes.getItemCount() + 1, "${attachment" + i + "}"));
                    }
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CUSTOMIZE_CERTIFICATE;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
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
