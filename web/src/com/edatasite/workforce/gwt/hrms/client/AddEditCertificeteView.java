package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Khasan on 08.09.14.
 */
public class AddEditCertificeteView extends CustomForm2 implements Colapse, EmployeeProfileConstans, Constants {

    private Integer objectID;
    private String formID;
    private WfmButton2 submit, approve, draftButton;
    protected Integer convertFormId;
    protected String formType;
    private Numbering numberWidget;
    private DataListBox certificateTypeListBox;
    private EmployeeLookUpWithCode employeeListBox;
    private EmployeeDocumentDragView documentDragView;
    private ChosenApproversWidget approvers;
    private HTMLPanel htmlPanel;
    private TextBox box1, box2, box3, box4, box5, box6, box7, box8, box9, box10, box11, box12, box13, box14, box15, box16, box17, box18;
    private TextArea area1, area2, area3, area4, area5, area6, area7, area8;
    private HashMap<String, Widget> widgetsMap;
    private ArrayList<Integer> employeeDouments = new ArrayList<>();
    private boolean isSetupApproval = false;
    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();


    public AddEditCertificeteView() {
        super("addHrLetters", hrmsStrings.addHrLetters());
    }

    public AddEditCertificeteView(Integer objectID, String formID) {
        super("edit", hrmsStrings.editHrLetters());
        this.objectID = objectID;
        this.formID = formID;
    }

    public AddEditCertificeteView(String formType, Integer convertFormId) {
        super("addHrLetters", hrmsStrings.addHrLetters());
        this.convertFormId = convertFormId;
        this.formType = formType;
    }

    @Override
    protected void addButtons() {
        draftButton = addButton(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE, event -> save(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_DRAFT));
        submit = addButton(wfmStrings.submit(), event -> save(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED));
        approve = addButton(wfmStrings.approve(), event -> save(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED));
        approve.setVisible(false);
    }

    @Override
    public String getPropertyCode() {
        return CERTIFICATES_LIST;
    }

    private void save(String status) {
        if (validation()) {
            submit.setEnabled(false);
            approve.setEnabled(false);
            draftButton.setEnabled(false);
            CertificateItem certificateItem = new CertificateItem();
            certificateItem.setObjectId(objectID);
            certificateItem.setCertificateNumber(numberWidget.getNumberData(false));
            certificateItem.setEmployee(employeeListBox.getSelectedItem());
            certificateItem.setCertificateType(certificateTypeListBox.getSelectedItem());
            certificateItem.setDucumentList(documentDragView.getSelectedDocuments());
            if (objectID == null) {
                certificateItem.setCreationDate(new Date());
            }
            certificateItem.setUpdatedDate(new Date());
            certificateItem.setTextBox1(box1.getText());
            certificateItem.setTextBox2(box2.getText());
            certificateItem.setTextBox3(box3.getText());
            certificateItem.setTextBox4(box4.getText());
            certificateItem.setTextBox5(box5.getText());
            certificateItem.setTextBox6(box6.getText());
            certificateItem.setTextBox7(box7.getText());
            certificateItem.setTextBox8(box8.getText());
            certificateItem.setTextBox9(box9.getText());
            certificateItem.setTextBox10(box10.getText());
            certificateItem.setTextBox11(box11.getText());
            certificateItem.setTextBox12(box12.getText());
            certificateItem.setTextBox13(box13.getText());
            certificateItem.setTextBox14(box14.getText());
            certificateItem.setTextBox15(box15.getText());
            certificateItem.setTextBox16(box16.getText());
            certificateItem.setTextBox17(box17.getText());
            certificateItem.setTextBox18(box18.getText());
            certificateItem.setTextArea1(area1.getText());
            certificateItem.setTextArea2(area2.getText());
            certificateItem.setTextArea3(area3.getText());
            certificateItem.setTextArea4(area4.getText());
            certificateItem.setTextArea5(area5.getText());
            certificateItem.setTextArea6(area6.getText());
            certificateItem.setTextArea7(area7.getText());
            certificateItem.setTextArea8(area8.getText());
            certificateItem.setApprovers(approvers.getChosenApprovers());
            certificateItem.setStatusCode(status);
            certificateItem.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
            HrmsService.App.get().saveCertificate(certificateItem, new AbstractAsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                    submit.setEnabled(true);
                    approve.setEnabled(true);
                    draftButton.setEnabled(true);
                }

                @Override
                public void onSuccess(Integer result) {
                    submit.setEnabled(true);
                    approve.setEnabled(true);
                    draftButton.setEnabled(true);
                    Info.show(hrmsStrings.certificateIssuedSuccessfully(), Info.Type.INFO);
                    closeTab();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ADD_OR_EDIT_CERTIFICATE, result, AddEditCertificeteView.this);
                }
            });
        }
    }

    private boolean validation() {
        int errors = 0;
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER) != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER, numberWidget, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER).getTitle() : wfmStrings.number(), numberWidget.getTxtNumber(), formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE) != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).isRequired()) {
            errors += markAsError(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE, employeeListBox, !Validation.validateLookUpRequired(employeeListBox));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE) != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).isRequired()) {
            errors += markAsError(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE, certificateTypeListBox, !Validation.validateListBoxRequired(certificateTypeListBox, new HTML(), ""));
        }

        if (isSetupApproval && !approvers.isValid()) {
            if (formPropertyMap != null && formPropertyMap.get(CERTIFICATE_OF_EMPLOYMENT.APPROVER) != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.APPROVER).isRequired()) {
                errors += markAsError(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.APPROVER, approvers, !approvers.isValid());
            }
            errors++;
        }

        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        HrmsService.App.get().getCertificateData(objectID, formType, convertFormId, new AbstractAsyncCallback<CertificateItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(CertificateItem result) {
                LoadingPanel.loading(false);

                if (objectID == null) {
                    setDefaultValues();
                }

                numberWidget.setNumberData(result.getCertificateNumber());
                if (result.getDucumentList() != null && result.getDucumentList().size() > 0) {
                    employeeDouments = result.getDucumentIds();
                }
                certificateTypeListBox.setItems(result.getTypes());
                if (result.getEmployee() != null) {
                    employeeListBox.setSelected(result.getEmployee());
                    documentDragView.reloadMenuCellTree(result.getEmployee().getId(), employeeDouments);
                } else if (objectID == null && result.getCurrentUserID() != null) {
                    employeeListBox.setSelected(result.getCurrentUserID(), result.getCurrentUserName());
                } else if (objectID == null && Utils.getUserID() != null) {
                    employeeListBox.setSelected(Utils.getUserID(), Utils.getUserFullName());
                }
                if (!Utils.hasPermission(PermissionConstants.HRMS_CERTIFICATES_ADD_FOR_OTHERS)) {
                    employeeListBox.setEnabled(false);
                }
                if (result.getCertificateType() != null) {
                    certificateTypeListBox.setSelected(result.getCertificateType());
                }
                onCertificateTypeChange(employeeListBox.getSelectedItemID(), certificateTypeListBox.getSelectedId());
                isSetupApproval = result.isSetupApproval();
                if (result.isSetupApproval()) {
                    if (result.getFormID() != null) {
                        certificateTypeListBox.setEnabled(false);
                        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.APPROVER) != null) {
                            addField(CustomFormConstants.APPROVER, approvers, getTitle(formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.APPROVER).isChanged() ? formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.APPROVER).getTitle() : wfmStrings.approver(), formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.APPROVER).isRequired()));
                            approvers.setEnabled(!formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.APPROVER).isDisabled());
                        } else {
                            addField(CERTIFICATE_OF_EMPLOYMENT.APPROVER, approvers, getTitle(wfmStrings.approver(), true));
                        }

                        Element element = DOM.getElementById(CERTIFICATE_OF_EMPLOYMENT.APPROVER);
                        if (element != null) {
                            element.getStyle().setDisplay(Style.Display.BLOCK);
                        }
                    }
                }
                if (result.getCurrentApprover() != null && result.getCurrentApprover().getExactEmployee() != null && Utils.getUserID().equals(result.getCurrentApprover().getExactEmployee().getId())) {
                    approve.setVisible(true);
                    submit.setVisible(false);
                }
                Map<String, String> defaultValueMap = new HashMap<>();
                if (result.getCustomHTMLcontent() != null && !"".equals(result.getCustomHTMLcontent())) {
                    WftHTMLPanel wftHTMLPanel = new WftHTMLPanel(result.getCustomHTMLcontent(), widgetsMap, true);
                    htmlPanel.setVisible(true);
                    htmlPanel.add(wftHTMLPanel.getContainer());
                    defaultValueMap = wftHTMLPanel.getValueMap();
                } else if (result.getContent() != null) {
                    WftHTMLPanel wftHTMLPanel = new WftHTMLPanel(result.getContent(), widgetsMap, true);
                    htmlPanel.setVisible(true);
                    htmlPanel.add(wftHTMLPanel.getContainer());
                    defaultValueMap = wftHTMLPanel.getValueMap();
                }

                if (!defaultValueMap.isEmpty()) {
                    area1.setText(isValue(result.getTextArea1()) ? result.getTextArea1() : defaultValueMap.get("inputtextarea1") != null ? defaultValueMap.get("inputtextarea1") : "");
                    area2.setText(isValue(result.getTextArea2()) ? result.getTextArea2() : defaultValueMap.get("inputtextarea2") != null ? defaultValueMap.get("inputtextarea2") : "");
                    area3.setText(isValue(result.getTextArea3()) ? result.getTextArea3() : defaultValueMap.get("inputtextarea3") != null ? defaultValueMap.get("inputtextarea3") : "");
                    area4.setText(isValue(result.getTextArea4()) ? result.getTextArea4() : defaultValueMap.get("inputtextarea4") != null ? defaultValueMap.get("inputtextarea4") : "");
                    area5.setText(isValue(result.getTextArea5()) ? result.getTextArea5() : defaultValueMap.get("inputtextarea5") != null ? defaultValueMap.get("inputtextarea5") : "");
                    area6.setText(isValue(result.getTextArea6()) ? result.getTextArea6() : defaultValueMap.get("inputtextarea6") != null ? defaultValueMap.get("inputtextarea6") : "");
                    area7.setText(isValue(result.getTextArea7()) ? result.getTextArea7() : defaultValueMap.get("inputtextarea7") != null ? defaultValueMap.get("inputtextarea7") : "");
                    area8.setText(isValue(result.getTextArea8()) ? result.getTextArea8() : defaultValueMap.get("inputtextarea8") != null ? defaultValueMap.get("inputtextarea8") : "");

                    box1.setText(isValue(result.getTextBox1()) && !"___________________".equals(result.getTextBox1()) ? result.getTextBox1() : defaultValueMap.get("inputtextbox1") != null ? defaultValueMap.get("inputtextbox1") : "");
                    box2.setText(isValue(result.getTextBox2()) && !"___________________".equals(result.getTextBox2()) ? result.getTextBox2() : defaultValueMap.get("inputtextbox2") != null ? defaultValueMap.get("inputtextbox2") : "");
                    box3.setText(isValue(result.getTextBox3()) && !"___________________".equals(result.getTextBox3()) ? result.getTextBox3() : defaultValueMap.get("inputtextbox3") != null ? defaultValueMap.get("inputtextbox3") : "");
                    box4.setText(isValue(result.getTextBox4()) && !"___________________".equals(result.getTextBox4()) ? result.getTextBox4() : defaultValueMap.get("inputtextbox4") != null ? defaultValueMap.get("inputtextbox4") : "");
                    box5.setText(isValue(result.getTextBox5()) && !"___________________".equals(result.getTextBox5()) ? result.getTextBox5() : defaultValueMap.get("inputtextbox5") != null ? defaultValueMap.get("inputtextbox5") : "");
                    box6.setText(isValue(result.getTextBox6()) && !"___________________".equals(result.getTextBox6()) ? result.getTextBox6() : defaultValueMap.get("inputtextbox6") != null ? defaultValueMap.get("inputtextbox6") : "");
                    box7.setText(isValue(result.getTextBox7()) && !"___________________".equals(result.getTextBox7()) ? result.getTextBox7() : defaultValueMap.get("inputtextbox7") != null ? defaultValueMap.get("inputtextbox7") : "");
                    box8.setText(isValue(result.getTextBox8()) && !"___________________".equals(result.getTextBox8()) ? result.getTextBox8() : defaultValueMap.get("inputtextbox8") != null ? defaultValueMap.get("inputtextbox8") : "");
                    box9.setText(isValue(result.getTextBox9()) && !"___________________".equals(result.getTextBox9()) ? result.getTextBox9() : defaultValueMap.get("inputtextbox9") != null ? defaultValueMap.get("inputtextbox9") : "");
                    box10.setText(isValue(result.getTextBox10()) && !"___________________".equals(result.getTextBox10()) ? result.getTextBox10() : defaultValueMap.get("inputtextbox10") != null ? defaultValueMap.get("inputtextbox10") : "");
                    box11.setText(isValue(result.getTextBox11()) && !"___________________".equals(result.getTextBox11()) ? result.getTextBox11() : defaultValueMap.get("inputtextbox11") != null ? defaultValueMap.get("inputtextbox11") : "");
                    box12.setText(isValue(result.getTextBox12()) && !"___________________".equals(result.getTextBox12()) ? result.getTextBox12() : defaultValueMap.get("inputtextbox12") != null ? defaultValueMap.get("inputtextbox12") : "");
                    box13.setText(isValue(result.getTextBox13()) && !"___________________".equals(result.getTextBox13()) ? result.getTextBox13() : defaultValueMap.get("inputtextbox13") != null ? defaultValueMap.get("inputtextbox13") : "");
                    box14.setText(isValue(result.getTextBox14()) && !"___________________".equals(result.getTextBox14()) ? result.getTextBox14() : defaultValueMap.get("inputtextbox14") != null ? defaultValueMap.get("inputtextbox14") : "");
                    box15.setText(isValue(result.getTextBox15()) && !"___________________".equals(result.getTextBox15()) ? result.getTextBox15() : defaultValueMap.get("inputtextbox15") != null ? defaultValueMap.get("inputtextbox15") : "");
                    box16.setText(isValue(result.getTextBox16()) && !"___________________".equals(result.getTextBox16()) ? result.getTextBox16() : defaultValueMap.get("inputtextbox16") != null ? defaultValueMap.get("inputtextbox16") : "");
                    box17.setText(isValue(result.getTextBox17()) && !"___________________".equals(result.getTextBox17()) ? result.getTextBox17() : defaultValueMap.get("inputtextbox17") != null ? defaultValueMap.get("inputtextbox17") : "");
                    box18.setText(isValue(result.getTextBox18()) && !"___________________".equals(result.getTextBox18()) ? result.getTextBox18() : defaultValueMap.get("inputtextbox18") != null ? defaultValueMap.get("inputtextbox18") : "");
                } else {
                    area1.setText(isValue(result.getTextArea1()) ? result.getTextArea1() : "");
                    area2.setText(isValue(result.getTextArea2()) ? result.getTextArea2() : "");
                    area3.setText(isValue(result.getTextArea3()) ? result.getTextArea3() : "");
                    area4.setText(isValue(result.getTextArea4()) ? result.getTextArea4() : "");
                    area5.setText(isValue(result.getTextArea5()) ? result.getTextArea5() : "");
                    area6.setText(isValue(result.getTextArea6()) ? result.getTextArea6() : "");
                    area7.setText(isValue(result.getTextArea7()) ? result.getTextArea7() : "");
                    area8.setText(isValue(result.getTextArea8()) ? result.getTextArea8() : "");

                    box1.setText(isValue(result.getTextBox1()) ? result.getTextBox1() : "");
                    box2.setText(isValue(result.getTextBox2()) ? result.getTextBox2() : "");
                    box3.setText(isValue(result.getTextBox3()) ? result.getTextBox3() : "");
                    box4.setText(isValue(result.getTextBox4()) ? result.getTextBox4() : "");
                    box5.setText(isValue(result.getTextBox5()) ? result.getTextBox5() : "");
                    box6.setText(isValue(result.getTextBox6()) ? result.getTextBox6() : "");
                    box7.setText(isValue(result.getTextBox7()) ? result.getTextBox7() : "");
                    box8.setText(isValue(result.getTextBox8()) ? result.getTextBox8() : "");
                    box9.setText(isValue(result.getTextBox9()) ? result.getTextBox9() : "");
                    box10.setText(isValue(result.getTextBox10()) ? result.getTextBox10() : "");
                    box11.setText(isValue(result.getTextBox11()) ? result.getTextBox11() : "");
                    box12.setText(isValue(result.getTextBox12()) ? result.getTextBox12() : "");
                    box13.setText(isValue(result.getTextBox13()) ? result.getTextBox13() : "");
                    box14.setText(isValue(result.getTextBox14()) ? result.getTextBox14() : "");
                    box15.setText(isValue(result.getTextBox15()) ? result.getTextBox15() : "");
                    box16.setText(isValue(result.getTextBox16()) ? result.getTextBox16() : "");
                    box17.setText(isValue(result.getTextBox17()) ? result.getTextBox17() : "");
                    box18.setText(isValue(result.getTextBox18()) ? result.getTextBox18() : "");
                }
                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems());
            }
        });
        if (objectID == null) {
            setDefaultValuesByFormProperty();
        }
    }

    protected void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER) != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER).getDefaultValue() != null) {
            numberWidget.getTxtNumber().setText(formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE) != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).getDefaultValue() != null) {
            certificateTypeListBox.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).getSelectedId(), formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).getDefaultValue()));
            onCertificateTypeChange(employeeListBox.getSelectedItemID(), certificateTypeListBox.getSelectedId());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE) != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).getDefaultValue() != null) {
            employeeListBox.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).getSelectedId(), formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).getDefaultValue()));
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CERTIFICATE_OF_EMPLOYMENT_FORM;
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
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Certificates, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddEditCertificeteView.super.onInitialize();
            }
        });
        return null;
    }

    protected void registerFields() {
        numberWidget = new Numbering();
        employeeListBox = new EmployeeLookUpWithCode();
        employeeListBox.ensureDebugId("add_certificate_view-employee");
        documentDragView = new EmployeeDocumentDragView();
        certificateTypeListBox = new DataListBox();
        certificateTypeListBox.ensureDebugId("add_certificate_view-type");
        approvers = new ChosenApproversWidget(formID != null ? formID : RelationItem.TYPE_CERTIFICATE_OF_EMPLOYMENT, objectID);
        approvers.setWidth("200px");
        htmlPanel = new HTMLPanel((SafeHtml) () -> "");
        htmlPanel.setVisible(false);

        box1 = new TextBox();
        box2 = new TextBox();
        box3 = new TextBox();
        box4 = new TextBox();
        box5 = new TextBox();
        box6 = new TextBox();
        box7 = new TextBox();
        box8 = new TextBox();
        box9 = new TextBox();
        box10 = new TextBox();
        box11 = new TextBox();
        box12 = new TextBox();
        box13 = new TextBox();
        box14 = new TextBox();
        box15 = new TextBox();
        box16 = new TextBox();
        box17 = new TextBox();
        box18 = new TextBox();
        area1 = new TextArea();
        area1.setHeight("80px");
        area2 = new TextArea();
        area2.setHeight("80px");
        area3 = new TextArea();
        area3.setHeight("80px");
        area4 = new TextArea();
        area4.setHeight("80px");
        area5 = new TextArea();
        area5.setHeight("80px");
        area6 = new TextArea();
        area6.setHeight("80px");
        area7 = new TextArea();
        area7.setHeight("80px");
        area8 = new TextArea();
        area8.setHeight("80px");
        widgetsMap = new HashMap<>();
        widgetsMap.put("inputtextbox1", box1);
        widgetsMap.put("inputtextbox2", box2);
        widgetsMap.put("inputtextbox3", box3);
        widgetsMap.put("inputtextbox4", box4);
        widgetsMap.put("inputtextbox5", box5);
        widgetsMap.put("inputtextbox6", box6);
        widgetsMap.put("inputtextbox7", box7);
        widgetsMap.put("inputtextbox8", box8);
        widgetsMap.put("inputtextbox9", box9);
        widgetsMap.put("inputtextbox10", box10);
        widgetsMap.put("inputtextbox11", box11);
        widgetsMap.put("inputtextbox12", box12);
        widgetsMap.put("inputtextbox13", box13);
        widgetsMap.put("inputtextbox14", box14);
        widgetsMap.put("inputtextbox15", box15);
        widgetsMap.put("inputtextbox16", box16);
        widgetsMap.put("inputtextbox17", box17);
        widgetsMap.put("inputtextbox18", box18);
        widgetsMap.put("inputtextarea1", area1);
        widgetsMap.put("inputtextarea2", area2);
        widgetsMap.put("inputtextarea3", area3);
        widgetsMap.put("inputtextarea4", area4);
        widgetsMap.put("inputtextarea5", area5);
        widgetsMap.put("inputtextarea6", area6);
        widgetsMap.put("inputtextarea7", area7);
        widgetsMap.put("inputtextarea8", area8);

        numberWidget.getTxtPrefix().setVisible(true);
        employeeListBox.addStyleName(Constants.DEFAULT_WIDTH);
        certificateTypeListBox.addStyleName(Constants.DEFAULT_WIDTH);
        certificateTypeListBox.getElement().getStyle().setMarginBottom(30, Style.Unit.PX);

        employeeListBox.getSuggestBox().addSelectionHandler(selectionEvent -> {
            if (employeeListBox.getSelectedItem() == null && certificateTypeListBox.getSelectedItem() != null) {
                htmlPanel.clear();
            } else if (employeeListBox.getSelectedItem() != null && certificateTypeListBox.getSelectedItem() != null) {
                documentDragView.reloadMenuCellTree(employeeListBox.getSelectedItemID(), employeeDouments);
                onCertificateTypeChange(employeeListBox.getSelectedItemID(), certificateTypeListBox.getSelectedId());
            }
        });

        certificateTypeListBox.addValueChangeHandler(changeEvent -> {
            if (!Validation.validateLookUpRequired(employeeListBox)) {
                Info.show(hrmsStrings.pleaseSelectTheEmployeeFirst(), Info.Type.WARNING);
                certificateTypeListBox.setSelectedNullLabel();
            } else {
                String formId = certificateTypeListBox.getSelectedItem().getDescription();
                if (formId != null && !"".equals(formId)) {
                    formID = formId;
                    approvers.reloadApproverWidgets(formID, objectID);
                    approve.setVisible(false);
                    submit.setVisible(true);
                    initApproverLoadHandler();
                }
                documentDragView.reloadMenuCellTree(employeeListBox.getSelectedItemID(), employeeDouments);
                onCertificateTypeChange(employeeListBox.getSelectedItemID(), certificateTypeListBox.getSelectedId());
            }
        });

        addTitleField(CERTIFICATE_OF_EMPLOYMENT.INFORMATION, wfmStrings.certificateDetails());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE) != null) {
            addField(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE, certificateTypeListBox, getTitle(formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).getTitle() : wfmStrings.type(),
                    formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).isRequired()),false,
                    formPropertyMap.get(CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).isInformation());
            certificateTypeListBox.setEnabled(!formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).isDisabled());
            if (formPropertyMap.get(CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).isInformation()){
                new KpiToolTip(certificateTypeListBox, formPropertyMap.get(CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).getInformationText());
            }
        } else {
            addField(CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE, certificateTypeListBox, getTitle(wfmStrings.type(), true));
        }

        if (Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_SHOW_DETAILS_INFORMATION)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER) != null) {
                addField(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER, numberWidget, getTitle(formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER).getTitle() : wfmStrings.number(),
                        formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER).isRequired()),false,
                        formPropertyMap.get(CERTIFICATE_OF_EMPLOYMENT.NUMBER).isInformation());
                numberWidget.setEnabled(!formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER).isDisabled());
                if (formPropertyMap.get(CERTIFICATE_OF_EMPLOYMENT.NUMBER).isInformation()){
                    new KpiToolTip(numberWidget,formPropertyMap.get(CERTIFICATE_OF_EMPLOYMENT.NUMBER).getInformationText());
                }
            } else {
                addField(CERTIFICATE_OF_EMPLOYMENT.NUMBER, numberWidget, getTitle(wfmStrings.number(), true));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE) != null) {
                addField(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE, employeeListBox, getTitle(formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).isChanged() ? formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).getTitle() : wfmStrings.employee(),
                        formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).isRequired()),false,
                        formPropertyMap.get(CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).isInformation());
                employeeListBox.setEnabled(!formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).isDisabled());
                if (formPropertyMap.get(CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).isInformation()){
                    new KpiToolTip(employeeListBox,formPropertyMap.get(CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).getInformationText());
                }
            } else {
                addField(CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE, employeeListBox, getTitle(wfmStrings.employee(), true));
            }

            addTitleField(CERTIFICATE_OF_EMPLOYMENT.DOCUMENT_CELL_TREE, "");
            addField(CERTIFICATE_OF_EMPLOYMENT.DOCUMENT_CELL_TREE, documentDragView.onInitialize(), wfmStrings.attachments());
            addField(CERTIFICATE_OF_EMPLOYMENT.CONTENT, htmlPanel, null, false);
        }

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID);
        show();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.UPDATE_CERTIFICATE_TYPE, AddEditCertificeteView.this, (sender, args) -> reloadPage());
    }

    private void initApproverLoadHandler() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddEditCertificeteView.this, (sender, args) -> {
            if (approvers.getFirstApproverLookUp() != null) {
                approvers.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approve.setVisible(true);
                        submit.setVisible(false);
                    } else {
                        approve.setVisible(false);
                        submit.setVisible(true);
                    }
                });
                if (approvers.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approve.setVisible(true);
                        submit.setVisible(false);
                    } else {
                        approve.setVisible(false);
                        submit.setVisible(true);
                    }
                }
            } else {
                approve.setVisible(false);
                submit.setVisible(true);
            }
        });
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void reloadPage() {
        documentDragView.reloadMenuCellTree(employeeListBox.getSelectedItemID(), employeeDouments);
        onCertificateTypeChange(employeeListBox.getSelectedItemID(), certificateTypeListBox.getSelectedId());
    }

    private void onCertificateTypeChange(Integer employeeId, Integer certificateTypeID) {
        LoadingPanel.loading(true);
        HrmsService.App.get().getCertificateHTML(employeeId, certificateTypeID, documentDragView.getSelectedDocuments(), new AbstractAsyncCallback<CertificateItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(CertificateItem result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    Map<String, String> defaultValueMap = new HashMap<>();

                    htmlPanel.clear();
                    WftHTMLPanel wftHTMLPanel = new WftHTMLPanel(result.getContent(), widgetsMap, true);
                    htmlPanel.add(wftHTMLPanel.getContainer());
                    htmlPanel.setVisible(true);
                    defaultValueMap = wftHTMLPanel.getValueMap();
                    isSetupApproval = result.isSetupApproval();
                    if (!existsFieldInForm(CERTIFICATE_OF_EMPLOYMENT.APPROVER) && isSetupApproval) {
                        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.APPROVER) != null) {
                            addField(CustomFormConstants.APPROVER, approvers, getTitle(formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.APPROVER).isChanged() ? formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.APPROVER).getTitle() : wfmStrings.approver(), formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.APPROVER).isRequired()));
                            approvers.setEnabled(!formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.APPROVER).isDisabled());
                        } else {
                            addField(CERTIFICATE_OF_EMPLOYMENT.APPROVER, approvers, getTitle(wfmStrings.approver(), true));
                        }
                    }
                    area1.setText(defaultValueMap.get("inputtextarea1") != null ? defaultValueMap.get("inputtextarea1") : "");
                    area2.setText(defaultValueMap.get("inputtextarea2") != null ? defaultValueMap.get("inputtextarea2") : "");
                    area3.setText(defaultValueMap.get("inputtextarea3") != null ? defaultValueMap.get("inputtextarea3") : "");
                    area4.setText(defaultValueMap.get("inputtextarea4") != null ? defaultValueMap.get("inputtextarea4") : "");
                    area5.setText(defaultValueMap.get("inputtextarea5") != null ? defaultValueMap.get("inputtextarea5") : "");
                    area6.setText(defaultValueMap.get("inputtextarea6") != null ? defaultValueMap.get("inputtextarea6") : "");
                    area7.setText(defaultValueMap.get("inputtextarea7") != null ? defaultValueMap.get("inputtextarea7") : "");
                    area8.setText(defaultValueMap.get("inputtextarea8") != null ? defaultValueMap.get("inputtextarea8") : "");

                    box1.setText(defaultValueMap.get("inputtextbox1") != null ? defaultValueMap.get("inputtextbox1") : "");
                    box2.setText(defaultValueMap.get("inputtextbox2") != null ? defaultValueMap.get("inputtextbox2") : "");
                    box3.setText(defaultValueMap.get("inputtextbox3") != null ? defaultValueMap.get("inputtextbox3") : "");
                    box4.setText(defaultValueMap.get("inputtextbox4") != null ? defaultValueMap.get("inputtextbox4") : "");
                    box5.setText(defaultValueMap.get("inputtextbox5") != null ? defaultValueMap.get("inputtextbox5") : "");
                    box6.setText(defaultValueMap.get("inputtextbox6") != null ? defaultValueMap.get("inputtextbox6") : "");
                    box7.setText(defaultValueMap.get("inputtextbox7") != null ? defaultValueMap.get("inputtextbox7") : "");
                    box8.setText(defaultValueMap.get("inputtextbox8") != null ? defaultValueMap.get("inputtextbox8") : "");
                    box9.setText(defaultValueMap.get("inputtextbox9") != null ? defaultValueMap.get("inputtextbox9") : "");
                    box10.setText(defaultValueMap.get("inputtextbox10") != null ? defaultValueMap.get("inputtextbox10") : "");
                    box11.setText(defaultValueMap.get("inputtextbox11") != null ? defaultValueMap.get("inputtextbox11") : "");
                    box12.setText(defaultValueMap.get("inputtextbox12") != null ? defaultValueMap.get("inputtextbox12") : "");
                    box13.setText(defaultValueMap.get("inputtextbox13") != null ? defaultValueMap.get("inputtextbox13") : "");
                    box14.setText(defaultValueMap.get("inputtextbox14") != null ? defaultValueMap.get("inputtextbox14") : "");
                    box15.setText(defaultValueMap.get("inputtextbox15") != null ? defaultValueMap.get("inputtextbox15") : "");
                    box16.setText(defaultValueMap.get("inputtextbox16") != null ? defaultValueMap.get("inputtextbox16") : "");
                    box17.setText(defaultValueMap.get("inputtextbox17") != null ? defaultValueMap.get("inputtextbox17") : "");
                    box18.setText(defaultValueMap.get("inputtextbox18") != null ? defaultValueMap.get("inputtextbox18") : "");
                }
            }
        });
    }

    private boolean isValue(String value) {
        return value != null && !"".equals(value);
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
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
