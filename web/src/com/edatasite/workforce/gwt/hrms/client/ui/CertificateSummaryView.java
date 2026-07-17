package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NotesWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Khasan on 29.09.14.
 */
public class CertificateSummaryView extends CustomForm2 implements Colapse {

    private final Integer objectID;
    private CertificateItem item;
    private HTML number, employee, certificateType, approver;
    private HTMLPanel htmlPanel;
    private NotesWidget notesPanel;
    private HTML box1, box2, box3, box4, box5, box6, box7, box8, box9, box10, box11, box12, box13, box14, box15, box16, box17, box18;
    private HTML area1, area2, area3, area4, area5, area6, area7, area8;
    private HashMap<String, Widget> widgetsMap;
    private WfmButton2 approveButton, rejectButton;
    private WfmButton2 removeButton, editButton, pdfButton;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public CertificateSummaryView(Integer objectID) {
        super("summary", wfmStrings.hrLetters());
        this.objectID = objectID;

    }

    private FormHasCustomField customFieldUtil;

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFields(ViewName.Certificates, new AsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<CompanyCustomFieldItem> companyCustomFieldItems) {
                if (companyCustomFieldItems != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(companyCustomFieldItems);
                }
                CertificateSummaryView.super.onInitialize();
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ADD_OR_EDIT_CERTIFICATE, CertificateSummaryView.this, (sender, args) -> getDataToFillFields());
        show();
        return null;
    }

    @Override
    protected void registerFields() {
        number = new HTML();
        employee = new HTML();
        certificateType = new HTML();
        approver = new HTML();
        htmlPanel = new HTMLPanel((SafeHtml) () -> "");
        notesPanel = new NotesWidget(false);
        box1 = new HTML();
        box2 = new HTML();
        box3 = new HTML();
        box4 = new HTML();
        box5 = new HTML();
        box6 = new HTML();
        box7 = new HTML();
        box8 = new HTML();
        box9 = new HTML();
        box10 = new HTML();
        box11 = new HTML();
        box12 = new HTML();
        box13 = new HTML();
        box14 = new HTML();
        box15 = new HTML();
        box16 = new HTML();
        box17 = new HTML();
        box18 = new HTML();
        area1 = new HTML();
        area2 = new HTML();
        area3 = new HTML();
        area4 = new HTML();
        area5 = new HTML();
        area6 = new HTML();
        area7 = new HTML();
        area8 = new HTML();
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
        addFieldsToForm();
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void addFieldsToForm() {
        addTitleField(CERTIFICATE_OF_EMPLOYMENT.INFORMATION, wfmStrings.information());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE) != null) {
            addField(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE, certificateType, getTitle(formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE).getTitle() : wfmStrings.type()));
        } else {
            addField(CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE, certificateType, getTitle(wfmStrings.type(), true));
        }
        if (Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_SHOW_DETAILS_INFORMATION)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER) != null) {
                addField(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.NUMBER).getTitle() : wfmStrings.number()));
            } else {
                addField(CERTIFICATE_OF_EMPLOYMENT.NUMBER, number, getTitle(wfmStrings.number(), true));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE) != null) {
                addField(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE, employee, getTitle(formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).isChanged() ? formPropertyMap.get(CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE).getTitle() : wfmStrings.employee()));
            } else {
                addField(CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE, employee, getTitle(wfmStrings.employee(), true));
            }

            addField(CERTIFICATE_OF_EMPLOYMENT.CONTENT, htmlPanel, null, false);
            addTitleField(CustomFormConstants.VACANCY.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

            getCustomFieldUtil().drawCustomFields(this, objectID, true);
        }
    }

    private boolean isValue(String value) {
        return value != null && !"".equals(value);
    }

    @Override
    protected void addButtons() {

        removeButton = addRemoveButton();
        removeButton.addClickHandler(clickEvent -> {
            if (item != null) {
                final WfmMessageBox message = new WfmMessageBox(IconEnum.INFO, Action.YesNo, true);
                message.setTitle(wfmStrings.warning());
                message.setMessage(wfmStrings.sureYouWantToDelete());
                message.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        HrmsService.App.get().deleteCertificate(item.getObjectId(), new AbstractAsyncCallback<Boolean>() {
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
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.DELETE_CERTIFICATE, result, CertificateSummaryView.this);
                                    closeTab();
                                }
                            }
                        });
                    }
                });
                message.open();
            }
        });


        pdfButton = addPdfButton();
        pdfButton.addClickHandler(event -> generateCertificatePDF());

        editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
        editButton.addClickHandler(clickEvent -> {
            if (item != null) {
                String certificateNumber = item.getCertificateNumber() != null ? item.getCertificateNumber().getNumberString() : "";
                String certificateType = item.getCertificateType() != null && item.getCertificateType().getName() != null ? item.getCertificateType().getName() : "";
                SinksContainerFactory.entryPoint.onHistoryChanged("certificate|add/add/" + item.getObjectId() + "/" + item.getFormID(), certificateNumber, certificateType);
            }
        });
        addButton(editButton);


        approveButton = addButton(wfmStrings.approve(), WfmButton2.BTN_SUCCESS, clickEvent -> {
            updateStatus(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED);
        });
        rejectButton = addButton(wfmStrings.reject(), WfmButton2.BTN_REJECT, clickEvent -> {
            setButtonsEnabled(false);
            notesPanel.setNoteListener(() -> updateStatus(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED));
            notesPanel.setCloseListener(() -> setButtonsEnabled(true));
            notesPanel.noteShell();

        });

        pdfButton.setVisible(false);
        editButton.setVisible(false);
        removeButton.setVisible(false);
        approveButton.setVisible(false);
        rejectButton.setVisible(false);
    }

    private void setButtonsEnabled(boolean enabled) {
        pdfButton.setEnabled(enabled);
        removeButton.setEnabled(enabled);
        editButton.setEnabled(enabled);
        approveButton.setEnabled(enabled);
        rejectButton.setEnabled(enabled);
    }

    private void generateCertificatePDF() {
        RequestObject requestObject = new RequestObject(objectID);
        String pdfURL = CommandConstants.PDF_URL + "/generateCertificatePDF";
        final HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parametrs, "_blank");
    }

    public void updateStatus(String statusCode) {
        LoadingPanel.loading(true);
        String note = "";
        if (Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED.equals(statusCode)) {
            if (notesPanel.getLastHistoryItem() != null && notesPanel.getLastHistoryItem().getComment() != null
                    && !"".equals(notesPanel.getLastHistoryItem().getComment().trim())) {
                note = notesPanel.getLastHistoryItem().getComment();
            }
        }
        HrmsService.App.get().updateCertificateStatus(objectID, statusCode, note, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void aVoid) {
                LoadingPanel.loading(false);
                setButtonsEnabled(true);
                closeTab();
            }
        });
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        HrmsService.App.get().getCertificateData(objectID, new AbstractAsyncCallback<CertificateItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CertificateItem result) {
                LoadingPanel.loading(false);
                item = result;
                number.setHTML(result.getCertificateNumber().getNumberString());
                employee.setHTML(result.getEmployee().getName());
                certificateType.setHTML(result.getCertificateType().getName());

                if (result.getCurrentApproverEmployeeName() != null) {
                    approver.setHTML(result.getCurrentApproverEmployeeName());
                    addField(CERTIFICATE_OF_EMPLOYMENT.APPROVER, approver, getTitle(wfmStrings.approver()));
                }
                pdfButton.setVisible(Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_PDF) && Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED.equals(result.getStatusCode()));
                removeButton.setVisible(Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_DELETE));
                editButton.setVisible(Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_EDIT) && Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_DRAFT.equals(result.getStatusCode()));
                if (result.isCanApprove() && Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED.equals(result.getStatusCode())) {
                    approveButton.setVisible(true);
                    rejectButton.setVisible(true);
                }

                Map<String, String> defaultValueMap = new HashMap<>();
                if (result.getCustomHTMLcontent() != null && !"".equals(result.getCustomHTMLcontent())) {
                    WftHTMLPanel wftHTMLPanel = new WftHTMLPanel(result.getCustomHTMLcontent(), widgetsMap, true);
                    htmlPanel.clear();
                    htmlPanel.add(wftHTMLPanel.getContainer());
                    defaultValueMap = wftHTMLPanel.getValueMap();
                } else if (result.getContent() != null) {
                    WftHTMLPanel wftHTMLPanel = new WftHTMLPanel(result.getContent(), widgetsMap, true);
                    htmlPanel.clear();
                    htmlPanel.add(wftHTMLPanel.getContainer());
                    defaultValueMap = wftHTMLPanel.getValueMap();
                }

                if (defaultValueMap.size() > 0) {
                    area1.setText(isValue(result.getTextArea1()) ? result.getTextArea1() : defaultValueMap.get("inputtextarea1") != null ? defaultValueMap.get("inputtextarea1") : "");
                    area2.setText(isValue(result.getTextArea2()) ? result.getTextArea2() : defaultValueMap.get("inputtextarea2") != null ? defaultValueMap.get("inputtextarea2") : "");
                    area3.setText(isValue(result.getTextArea3()) ? result.getTextArea3() : defaultValueMap.get("inputtextarea3") != null ? defaultValueMap.get("inputtextarea3") : "");
                    area4.setText(isValue(result.getTextArea4()) ? result.getTextArea4() : defaultValueMap.get("inputtextarea4") != null ? defaultValueMap.get("inputtextarea4") : "");
                    area5.setText(isValue(result.getTextArea5()) ? result.getTextArea5() : defaultValueMap.get("inputtextarea5") != null ? defaultValueMap.get("inputtextarea5") : "");
                    area6.setText(isValue(result.getTextArea6()) ? result.getTextArea6() : defaultValueMap.get("inputtextarea6") != null ? defaultValueMap.get("inputtextarea6") : "");
                    area7.setText(isValue(result.getTextArea7()) ? result.getTextArea7() : defaultValueMap.get("inputtextarea7") != null ? defaultValueMap.get("inputtextarea7") : "");
                    area8.setText(isValue(result.getTextArea8()) ? result.getTextArea8() : defaultValueMap.get("inputtextarea8") != null ? defaultValueMap.get("inputtextarea8") : "");

                    box1.setText(isValue(result.getTextBox1()) && !"___________________".equals(result.getTextBox1()) ? result.getTextBox1() : defaultValueMap.get("inputtextbox1") != null ? defaultValueMap.get("inputtextbox1") : "___________________");
                    box2.setText(isValue(result.getTextBox2()) && !"___________________".equals(result.getTextBox2()) ? result.getTextBox2() : defaultValueMap.get("inputtextbox2") != null ? defaultValueMap.get("inputtextbox2") : "___________________");
                    box3.setText(isValue(result.getTextBox3()) && !"___________________".equals(result.getTextBox3()) ? result.getTextBox3() : defaultValueMap.get("inputtextbox3") != null ? defaultValueMap.get("inputtextbox3") : "___________________");
                    box4.setText(isValue(result.getTextBox4()) && !"___________________".equals(result.getTextBox4()) ? result.getTextBox4() : defaultValueMap.get("inputtextbox4") != null ? defaultValueMap.get("inputtextbox4") : "___________________");
                    box5.setText(isValue(result.getTextBox5()) && !"___________________".equals(result.getTextBox5()) ? result.getTextBox5() : defaultValueMap.get("inputtextbox5") != null ? defaultValueMap.get("inputtextbox5") : "___________________");
                    box6.setText(isValue(result.getTextBox6()) && !"___________________".equals(result.getTextBox6()) ? result.getTextBox6() : defaultValueMap.get("inputtextbox6") != null ? defaultValueMap.get("inputtextbox6") : "___________________");
                    box7.setText(isValue(result.getTextBox7()) && !"___________________".equals(result.getTextBox7()) ? result.getTextBox7() : defaultValueMap.get("inputtextbox7") != null ? defaultValueMap.get("inputtextbox7") : "___________________");
                    box8.setText(isValue(result.getTextBox8()) && !"___________________".equals(result.getTextBox8()) ? result.getTextBox8() : defaultValueMap.get("inputtextbox8") != null ? defaultValueMap.get("inputtextbox8") : "___________________");
                    box9.setText(isValue(result.getTextBox9()) && !"___________________".equals(result.getTextBox9()) ? result.getTextBox9() : defaultValueMap.get("inputtextbox9") != null ? defaultValueMap.get("inputtextbox9") : "___________________");
                    box10.setText(isValue(result.getTextBox10()) && !"___________________".equals(result.getTextBox10()) ? result.getTextBox10() : defaultValueMap.get("inputtextbox10") != null ? defaultValueMap.get("inputtextbox10") : "___________________");
                    box11.setText(isValue(result.getTextBox11()) && !"___________________".equals(result.getTextBox11()) ? result.getTextBox11() : defaultValueMap.get("inputtextbox11") != null ? defaultValueMap.get("inputtextbox11") : "___________________");
                    box12.setText(isValue(result.getTextBox12()) && !"___________________".equals(result.getTextBox12()) ? result.getTextBox12() : defaultValueMap.get("inputtextbox12") != null ? defaultValueMap.get("inputtextbox12") : "___________________");
                    box13.setText(isValue(result.getTextBox13()) && !"___________________".equals(result.getTextBox13()) ? result.getTextBox13() : defaultValueMap.get("inputtextbox13") != null ? defaultValueMap.get("inputtextbox13") : "___________________");
                    box14.setText(isValue(result.getTextBox14()) && !"___________________".equals(result.getTextBox14()) ? result.getTextBox14() : defaultValueMap.get("inputtextbox14") != null ? defaultValueMap.get("inputtextbox14") : "___________________");
                    box15.setText(isValue(result.getTextBox15()) && !"___________________".equals(result.getTextBox15()) ? result.getTextBox15() : defaultValueMap.get("inputtextbox15") != null ? defaultValueMap.get("inputtextbox15") : "___________________");
                    box16.setText(isValue(result.getTextBox16()) && !"___________________".equals(result.getTextBox16()) ? result.getTextBox16() : defaultValueMap.get("inputtextbox16") != null ? defaultValueMap.get("inputtextbox16") : "___________________");
                    box17.setText(isValue(result.getTextBox17()) && !"___________________".equals(result.getTextBox17()) ? result.getTextBox17() : defaultValueMap.get("inputtextbox17") != null ? defaultValueMap.get("inputtextbox17") : "___________________");
                    box18.setText(isValue(result.getTextBox18()) && !"___________________".equals(result.getTextBox18()) ? result.getTextBox18() : defaultValueMap.get("inputtextbox18") != null ? defaultValueMap.get("inputtextbox18") : "___________________");
                } else {
                    area1.setText(isValue(result.getTextArea1()) ? result.getTextArea1() : "");
                    area2.setText(isValue(result.getTextArea2()) ? result.getTextArea2() : "");
                    area3.setText(isValue(result.getTextArea3()) ? result.getTextArea3() : "");
                    area4.setText(isValue(result.getTextArea4()) ? result.getTextArea4() : "");
                    area5.setText(isValue(result.getTextArea5()) ? result.getTextArea5() : "");
                    area6.setText(isValue(result.getTextArea6()) ? result.getTextArea6() : "");
                    area7.setText(isValue(result.getTextArea7()) ? result.getTextArea7() : "");
                    area8.setText(isValue(result.getTextArea8()) ? result.getTextArea8() : "");

                    box1.setText(isValue(result.getTextBox1()) ? result.getTextBox1() : "___________________");
                    box2.setText(isValue(result.getTextBox2()) ? result.getTextBox2() : "___________________");
                    box3.setText(isValue(result.getTextBox3()) ? result.getTextBox3() : "___________________");
                    box4.setText(isValue(result.getTextBox4()) ? result.getTextBox4() : "___________________");
                    box5.setText(isValue(result.getTextBox5()) ? result.getTextBox5() : "___________________");
                    box6.setText(isValue(result.getTextBox6()) ? result.getTextBox6() : "___________________");
                    box7.setText(isValue(result.getTextBox7()) ? result.getTextBox7() : "___________________");
                    box8.setText(isValue(result.getTextBox8()) ? result.getTextBox8() : "___________________");
                    box9.setText(isValue(result.getTextBox9()) ? result.getTextBox9() : "___________________");
                    box10.setText(isValue(result.getTextBox10()) ? result.getTextBox10() : "___________________");
                    box11.setText(isValue(result.getTextBox11()) ? result.getTextBox11() : "___________________");
                    box12.setText(isValue(result.getTextBox12()) ? result.getTextBox12() : "___________________");
                    box13.setText(isValue(result.getTextBox13()) ? result.getTextBox13() : "___________________");
                    box14.setText(isValue(result.getTextBox14()) ? result.getTextBox14() : "___________________");
                    box15.setText(isValue(result.getTextBox15()) ? result.getTextBox15() : "___________________");
                    box16.setText(isValue(result.getTextBox16()) ? result.getTextBox16() : "___________________");
                    box17.setText(isValue(result.getTextBox17()) ? result.getTextBox17() : "___________________");
                    box18.setText(isValue(result.getTextBox18()) ? result.getTextBox18() : "___________________");
                }
                getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems(), true);
                drawFooter();
            }
        });
    }

    private void drawFooter() {
        NoteHistoryWidget noteHistoryWidget = new NoteHistoryWidget(callback -> {
            if (objectID == null) {
                return;
            }
            HrmsService.App.get().loadCertificateHistory(objectID, callback);
        });
        if (objectID != null) {
            noteHistoryWidget.setSaveIntoDatabase(historyItem -> {
                if (historyItem != null) {
                    LoadingPanel.loading(true);
                    HrmsService.App.get().createCertificateHistory(objectID, historyItem, new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Integer hisItemId) {
                            historyItem.setObjectID(hisItemId);
                            LoadingPanel.loading(false);
                        }
                    });
                }
            });

            noteHistoryWidget.setRemoveFromDatabase((hisItem) -> {
                if (hisItem != null && hisItem.getObjectID() != null) {
                    LoadingPanel.loading(true);
                    HrmsService.App.get().deleteCertificateComment(hisItem.getObjectID(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Void aVoid) {
                            LoadingPanel.loading(false);
                        }
                    });
                }
            });
        }

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.setInitialClasses("informer-item history-notes-container");
        footer.addToLeftSide(informer);
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CERTIFICATE_OF_EMPLOYMENT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
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
