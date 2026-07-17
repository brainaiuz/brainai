package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.ui.DOBWidget;
import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.TextBox2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.CountryStates;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.MatrixTable;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.ui.CheckboxMailingListDataGrid;
import com.edatasite.workforce.gwt.documents.client.upload.GWTFileUpload;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.forms.CandidateForm;
import com.edatasite.workforce.gwt.webforms.client.forms.CaseField;
import com.edatasite.workforce.gwt.webforms.client.forms.CaseForm;
import com.edatasite.workforce.gwt.webforms.client.forms.Form;
import com.edatasite.workforce.gwt.webforms.client.forms.LeadForm;
import com.edatasite.workforce.gwt.webforms.client.forms.WebField;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: User
 * Date: 9/17/12
 * Time: 3:57 PM
 */
public class AddWebFormView extends CustomForm2 implements Constants, Colapse {

    interface WebFormsResources extends ClientBundle {
        @CssResource.NotStrict
        @Source("com/edatasite/workforce/gwt/crm/client/newcss/webForm.css")
        CssResource core();
    }

    public static WebFormsResources webFormsResources = GWT.create(WebFormsResources.class);

    final static CRMServiceAsync crmService = CRMService.App.get();
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private DataListBox formType;
    private TextBox title;
    private KpiEditor description;
    private KpiEditor confirmationMessage;
    private TextBox2 redirectURL;
    private TextBox buttonText;
    private KpiCheckBox useCaptcha;
    private TextBox captchaLabel;
    private TextBox captchaDescription;
    private TextBox captchaCantReadText;
    private TextBox captchaTryAnotherLink;
    FlexTable captchaPanel;
    private HTML previewLabel;
    private HTML previewDescription;
    private HTML previewCantRead;
    private SimpleLink previewTryAnotherLink;

    //second form
    private static final int HORIZONTAL_PANEL_SPACING = 2;
    private static final int VERTICAL_PANEL_SPACING = 3;
    private static final String STYLE = "x-form-invalid";
    private Form form;
    private final LinkedHashMap<Integer, Widget> errors = new LinkedHashMap<>();
    private ArrayList<WebField> sorter = null;
    private HashMap<WebField, VerticalPanel> sortedPanels = null;
    private FlexTable tableOfFields = null;
    private final CountryStates countryStates = new CountryStates();
    private boolean forceUsingCustomLayout = false;
    //end

    private WebForm item;
    private final Integer objectId;
    private String type;
    private KpiEditor contentHTML;
    private DataListBox idPicker;
    private TextArea customCss;
    private KpiCheckBox useCustomLayout;
    //Case reasons group by types
    private SelectItem[] caseReasons;
    private DataListBox types;
    private DataListBox caseReason;

    public AddWebFormView(Integer objectID, String type) {
        super("addwebform", crmStrings.addCrmForm());

        this.type = type;
        if (objectID != null) {
            setDescription(crmStrings.editCrmForm());
            this.objectId = objectID;
        } else {
            this.objectId = null;
        }
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        webFormsResources.core().ensureInjected();
        return null;
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        return Localize.getInstance().localizeByFieldID(getFormID(), fieldID);
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CRM_WEB_FORM;
    }

    @Override
    protected String getFormType() {
        return objectId != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);

        crmService.editWebForm(objectId, type, new AbstractAsyncCallback<WebForm>() {
            @Override
            public void failure(Throwable caught) {
                Info.show(caught.getMessage(), Info.Type.WARNING);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final WebForm result) {
                LoadingPanel.loading(false);
                fillTable(result);
                executeExternalInjections();
            }
        });
    }

    @Override
    protected void addButtons() {

        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> save());


        addButton(wfmStrings.updateAvailableFields(), event -> reInitWebFields(true));
    }

    @Override
    protected String getWikiCode() {
        return objectId != null ? PermissionConstants.CRM_WEB_FORM_EDIT : PermissionConstants.CRM_WEB_FORM_ADD;
    }

    @Override
    protected void registerFields() {
        final VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(5);
//        vp.setWidth("100%");

        idPicker = new DataListBox();
        FlexTable idPickerPanel = new FlexTable();
        idPickerPanel.setCellPadding(5);
        idPickerPanel.setCellSpacing(5);
        final HTML idPickerLabel = new HTML();
        idPicker.addValueChangeHandler(changeEvent -> idPickerLabel.setHTML((idPicker.getSelectedItem() != null ? idPicker.getSelectedItem().getDescription() : wfmStrings.pleaseSelect())));
        idPickerPanel.setWidget(0, 0, idPicker);
        idPickerPanel.setWidget(1, 0, idPickerLabel);
        contentHTML = new KpiEditor(true);
//        contentHTML.setWidth("698px");
        FlexTable t = new FlexTable();
        t.addStyleName("addForm-editor-wrapper");
        t.getCellFormatter().addStyleName(0, 0, "addForm-editor-code");
        t.setWidget(0, 0, contentHTML);
        t.getCellFormatter().addStyleName(0, 1, "addForm-editor-add");
        t.setWidget(0, 1, idPickerPanel);
        t.setWidth("auto");
        t.getFlexCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.HorizontalAlignmentConstant.startOf(HasDirection.Direction.DEFAULT), HasVerticalAlignment.ALIGN_TOP);
        customCss = new TextArea();
        customCss.setCharacterWidth(100);
        customCss.setVisibleLines(20);

        useCustomLayout = new KpiCheckBox(" ");
        useCustomLayout.addValueChangeHandler(booleanValueChangeEvent -> showCustomHTML(booleanValueChangeEvent.getValue() != null && booleanValueChangeEvent.getValue()));
        formType = new DataListBox();
        formType.setAllowFirstItem(false);
        formType.setWithoutNullLabel(true);
        formType.ensureDebugId("add_web_forms_fromType");

        title = new TextBox();
        title.setMaxLength(254);
        title.ensureDebugId("add_web_forms_title");

        VerticalPanel descriptionWithDesc = new VerticalPanel();
        description = new KpiEditor(true);
        description.ensureDebugId("add_web_forms_description");

        descriptionWithDesc.add(description);

        VerticalPanel confirmationMessageWithDesc = new VerticalPanel();
        confirmationMessage = new KpiEditor(true);
        confirmationMessage.ensureDebugId("add_web_forms_confirmationMessage");

        confirmationMessage.addStyleName("backgroundWhite");
        confirmationMessageWithDesc.add(confirmationMessage);

        redirectURL = new TextBox2(250);
        redirectURL.addStyleName("addWebForm-pathToRedirect");
        redirectURL.ensureDebugId("add_web_forms_redirectURL");

        captchaPanel = new FlexTable();
        captchaPanel.addStyleName("addWebForm-captchaPanel");
        captchaLabel = new TextBox();
        KeyboardListenerAdapter adapter = new KeyboardListenerAdapter() {
            public void onKeyPress(Widget sender, char key, int modifiers) {
                initPreviewCaptcha();
            }

            @Override
            public void onKeyDown(Widget sender, char keyCode, int modifiers) {
                initPreviewCaptcha();
            }

            @Override
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                initPreviewCaptcha();
            }
        };
        captchaLabel.addKeyboardListener(adapter);
        captchaLabel.addStyleName(DEFAULT_WIDTH);
        captchaDescription = new TextBox();
        captchaDescription.addKeyboardListener(adapter);
        captchaDescription.addStyleName(DEFAULT_WIDTH);
        captchaCantReadText = new TextBox();
        captchaCantReadText.addKeyboardListener(adapter);
        captchaCantReadText.addStyleName(DEFAULT_WIDTH);
        captchaTryAnotherLink = new TextBox();
        captchaTryAnotherLink.addKeyboardListener(adapter);
        captchaTryAnotherLink.addStyleName(DEFAULT_WIDTH);
        useCaptcha = new KpiCheckBox();
        useCaptcha.addClickHandler(event -> {
            captchaPanel.getParent().setVisible(useCaptcha.getValue());
        });
        captchaPanel.setWidget(0, 0, new HTML("<b class='customLabel subLabel'>&nbsp;&nbsp;&nbsp;&nbsp;" + crmStrings.antibotLabel() + " :</b>"));
        captchaPanel.setWidget(0, 1, captchaLabel);
        captchaPanel.setWidget(1, 0, new HTML("<b class='customLabel subLabel'>&nbsp;&nbsp;&nbsp;&nbsp;" + crmStrings.antibotDescription() + " :</b>"));
        captchaPanel.setWidget(1, 1, captchaDescription);
        captchaPanel.setWidget(2, 0, new HTML("<b class='customLabel subLabel'>&nbsp;&nbsp;&nbsp;&nbsp;" + crmStrings.cannotReadText() + " :</b>"));
        captchaPanel.setWidget(2, 1, captchaCantReadText);
        captchaPanel.setWidget(3, 0, new HTML("<b class='customLabel subLabel'>&nbsp;&nbsp;&nbsp;&nbsp;" + crmStrings.tryAnotherLink() + " :</b>"));
        captchaPanel.setWidget(3, 1, captchaTryAnotherLink);
        HorizontalPanel captcha = new HorizontalPanel();
        captcha.setSpacing(0);

        captcha.add(useCaptcha);
        captcha.addStyleName("addWebForm-captcha");

        captcha.getElement().getFirstChildElement().getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
        captcha.setCellHorizontalAlignment(useCaptcha, HasHorizontalAlignment.ALIGN_CENTER);

        VerticalPanel editAndPreviewPanel = new VerticalPanel();
        editAndPreviewPanel.addStyleName("addWebForm-editCaptchaPanel");
        editAndPreviewPanel.setSpacing(10);
        editAndPreviewPanel.add(captchaPanel);
        captchaPanel.getParent().setVisible(false);
        FlexTable previewPanel = new FlexTable();
        previewPanel.setCellSpacing(5);
        editAndPreviewPanel.add(previewPanel);
        previewLabel = new HTML();
        previewPanel.setWidget(0, 0, previewLabel);
        previewDescription = new HTML();
        previewPanel.setWidget(0, 1, previewDescription);
        VerticalPanel previewPanel2 = new VerticalPanel();
        previewPanel2.setSpacing(5);
        previewPanel.setWidget(1, 1, previewPanel2);
        previewCantRead = new HTML();
        Image image = new Image("/jcaptcha/");
        previewTryAnotherLink = new SimpleLink(wfmStrings.tryAnother());
        HorizontalPanel previewPanel3 = new HorizontalPanel();
        previewPanel3.setSpacing(5);
        previewPanel3.add(previewCantRead);
        previewPanel3.add(new HTML("&nbsp;"));
        previewPanel3.add(previewTryAnotherLink);
        previewPanel2.add(image);
        previewPanel2.add(previewPanel3);
        previewPanel2.add(new TextBox());
        captcha.add(editAndPreviewPanel);

        VerticalPanel buttonTextPanel = new VerticalPanel();
        buttonText = new TextBox();
        buttonText.ensureDebugId("add_web_forms_buttonText");
//        buttonText.setWidth("300px");
        buttonTextPanel.add(buttonText);

        tableOfFields = new FlexTable();
        tableOfFields.addStyleName("webFormTableFields file--AddWebFormVIew");
        tableOfFields.getElement().setId("webFormTableFields");

        VerticalPanel availableFields = new VerticalPanel();
        availableFields.setSpacing(15);

        addField(WEBFORM.FORM_TYPE, formType);
        addField(WEBFORM.TITLE, title);
        addField(WEBFORM.DESCRIPTION, descriptionWithDesc);
        addField(WEBFORM.CONFIRMATION_MESSAGE, confirmationMessageWithDesc);
        addField(WEBFORM.REDIRECT, redirectURL);
        addField(WEBFORM.CAPTCHA, captcha);
        addField(WEBFORM.BUTTON_TEXT, buttonTextPanel);
        addField(WEBFORM.TABLE_OF_FIELDS, tableOfFields);
        addField(WEBFORM.LAYOUT_PICKER, useCustomLayout);
        addField(WEBFORM.CUSTOM_LAYOUT, t);
        addField(WEBFORM.CUSTOM_LAYOUT_CSS, customCss);
        if (forceUsingCustomLayout && objectId == null) {
            useCustomLayout.setValue(true, true);
        }
        show();
    }

    private SelectItem[] initFormIDs(String type) {
        SelectItem[] result = null;
        if (WebFormConstants.LEAD_FORM.equals(type)) {
            result = initLeadFormIDs();
        } else if (WebFormConstants.CASE_FORM.equals(type)) {
            result = initCaseFormIDs();
        } else if (WebFormConstants.CANDIDATE_FORM.equals(type)) {
            result = initCandidateFormIDs();
        }
        return result == null ? new SelectItem[]{} : result;
    }

    private SelectItem[] initCaseFormIDs() {
        ArrayList<SelectItem> ids = new ArrayList<>();
        ids.add(getFieldAsSelectItem(wfmStrings.assignee(), CustomFormConstants.ASSIGNEE));
        ids.add(getFieldAsSelectItem(wfmStrings.attachment(), CustomFormConstants.ATTACHMENTS));
        ids.add(getFieldAsSelectItem(wfmStrings.subject(), CustomFormConstants.SUBJECT));
        ids.add(getFieldAsSelectItem(wfmStrings.note(), CustomFormConstants.CRM_NOTE));
        ids.add(getFieldAsSelectItem(wfmStrings.description(), CustomFormConstants.CASE_DESCRIPTION));
        ids.add(getFieldAsSelectItem(crmStrings.origin(), CustomFormConstants.CASE_ORIGIN));
        ids.add(getFieldAsSelectItem(wfmStrings.billable(), CustomFormConstants.BILLABLE));
        ids.add(getFieldAsSelectItem(wfmStrings.status(), CustomFormConstants.STATUS));
        ids.add(getFieldAsSelectItem(wfmStrings.type(), CustomFormConstants.TYPE));
        ids.add(getFieldAsSelectItem(wfmStrings.sla(), CustomFormConstants.SLA));
        ids.add(getFieldAsSelectItem(wfmStrings.reportedBy(), CustomFormConstants.REPORTED_BY));
        ids.add(getFieldAsSelectItem(wfmStrings.priority(), CustomFormConstants.PRIORITY));
        ids.add(getFieldAsSelectItem(Property.get(Constants.CASE_LIST, wfmStrings.caseReason(), wfmStrings.crmCase()), CustomFormConstants.CASE_REASON));
        ids.add(getFieldAsSelectItem(wfmStrings.resolver(), CustomFormConstants.RESOLVER));
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            for (CompanyCustomFieldItem cfItem : item.getCustomFields()) {
                if (cfItem.getColumnCode() != null && !"".equals(cfItem.getColumnCode())) {
                    ids.add(getFieldAsSelectItem(cfItem.getFieldName(), cfItem.getColumnCode()));
                }
            }
        }
        return ids.toArray(new SelectItem[]{});
    }

    private SelectItem[] initLeadFormIDs() {
        ArrayList<SelectItem> ids = new ArrayList<>();
        ids.addAll(Arrays.asList(initContactFormIDs()));
        ids.add(getFieldAsSelectItem(wfmStrings.assignee(), CustomFormConstants.ASSIGNEE));
        ids.add(getFieldAsSelectItem(wfmStrings.backupAssignee(), CustomFormConstants.BACKUP_ASSIGNEE));
        ids.add(getFieldAsSelectItem(Property.get(Constants.LEADS, wfmStrings.leadSource(), wfmStrings.lead()), CustomFormConstants.LEAD_SOURCE));
        ids.add(getFieldAsSelectItem(wfmStrings.status(), CustomFormConstants.STATUS));
        ids.add(getFieldAsSelectItem(wfmStrings.rating(), CustomFormConstants.RATING));
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            for (CompanyCustomFieldItem cfItem : item.getCustomFields()) {
                if (cfItem.getColumnCode() != null && !"".equals(cfItem.getColumnCode())) {
                    ids.add(getFieldAsSelectItem(cfItem.getFieldName(), cfItem.getColumnCode()));
                }
            }
        }
        return ids.toArray(new SelectItem[]{});
    }

    private SelectItem[] initCandidateFormIDs() {
        ArrayList<SelectItem> ids = new ArrayList<>();
        ids.addAll(Arrays.asList(initContactFormIDs()));
        ids.add(getFieldAsSelectItem(wfmStrings.number(), CustomFormConstants.NUMBER));
        ids.add(getFieldAsSelectItem(wfmStrings.createdDate(), CustomFormConstants.CREATED_DATE));
        ids.add(getFieldAsSelectItem(wfmStrings.source(), CustomFormConstants.LEAD_SOURCE));
        ids.add(getFieldAsSelectItem(wfmStrings.workExperience(), CustomFormConstants.CANDIDATE.WORK_EXPERIENCE));
        ids.add(getFieldAsSelectItem(wfmStrings.currentEmployer(), CustomFormConstants.CANDIDATE.CURRENT_EMPLOYER));
        ids.add(getFieldAsSelectItem(wfmStrings.expectedSalary(), CustomFormConstants.CANDIDATE.EXPECTED_SALARY));
        ids.add(getFieldAsSelectItem(wfmStrings.skills(), CustomFormConstants.CANDIDATE.SKILLS));
        ids.add(getFieldAsSelectItem(wfmStrings.location(), CustomFormConstants.CANDIDATE.LOCATION));
        ids.add(getFieldAsSelectItem(wfmStrings.status(), CustomFormConstants.STATUS));
        ids.add(getFieldAsSelectItem(wfmStrings.matchedVacancies(), CustomFormConstants.CANDIDATE.VACANCIES));
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            for (CompanyCustomFieldItem cfItem : item.getCustomFields()) {
                if (cfItem.getColumnCode() != null && !"".equals(cfItem.getColumnCode())) {
                    ids.add(getFieldAsSelectItem(cfItem.getFieldName(), cfItem.getColumnCode()));
                }
            }
        }
        return ids.toArray(new SelectItem[]{});
    }

    private SelectItem[] initContactFormIDs() {
        ArrayList<SelectItem> ids = new ArrayList<>();
        ids.add(getFieldAsSelectItem(wfmStrings.firstName(), CustomFormConstants.FIRST_NAME));
        ids.add(getFieldAsSelectItem(wfmStrings.lastName(), CustomFormConstants.LAST_NAME));
        ids.add(getFieldAsSelectItem(wfmStrings.title(), CustomFormConstants.TITLE));
        ids.add(getFieldAsSelectItem(wfmStrings.middleName(), CustomFormConstants.MIDDLE_NAME));
        ids.add(getFieldAsSelectItem(wfmStrings.otherName(), CustomFormConstants.OTHER_NAME));
        ids.add(getFieldAsSelectItem(wfmStrings.dateOfBirth(), CustomFormConstants.BIRTH_DAY));
        ids.add(getFieldAsSelectItem(wfmStrings.accountName(), CustomFormConstants.CRM_ACCOUNT_NAME));
        ids.add(getFieldAsSelectItem(wfmStrings.jobTitle(), CustomFormConstants.JOB_TITLE));
        ids.add(getFieldAsSelectItem(wfmStrings.organizationType(), CustomFormConstants.CRM_ACCOUNT_ORGANIZATION_TYPE));
        ids.add(getFieldAsSelectItem(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), CustomFormConstants.DEPARTMENT));
        ids.add(getFieldAsSelectItem(wfmStrings.annualRevenue(), CustomFormConstants.CRM_ACCOUNT_ANNUAL_REVENUE));
        ids.add(getFieldAsSelectItem(wfmStrings.industry(), CustomFormConstants.CRM_ACCOUNT_INDUSTRY));
        ids.add(getFieldAsSelectItem(wfmStrings.ownership(), CustomFormConstants.CRM_ACCOUNT_OWNERSHIP));
        ids.add(getFieldAsSelectItem(wfmStrings.numberOfEmployees(), CustomFormConstants.CRM_ACCOUNT_NUMBER_OF_EMPLOYEE));
        ids.add(getFieldAsSelectItem(wfmStrings.email(), CustomFormConstants.EMAIL));
        ids.add(getFieldAsSelectItem(wfmStrings.phone(), CustomFormConstants.PHONE));
        ids.add(getFieldAsSelectItem(wfmStrings.imAddress(), CustomFormConstants.IM_ADDRESS));
        ids.add(getFieldAsSelectItem(wfmStrings.webAddress(), CustomFormConstants.WEB_ADDRESS));
        ids.add(getFieldAsSelectItem(Property.get(Constants.LEADS, wfmStrings.leadOwner(), wfmStrings.lead()), CustomFormConstants.LEAD_OWNER));
        ids.add(getFieldAsSelectItem(Property.get(Constants.LEADS, wfmStrings.name(), wfmStrings.lead()), CustomFormConstants.LEAD_NAME));
        ids.add(getFieldAsSelectItem(wfmStrings.address(), CustomFormConstants.ADDRESS));
        ids.add(getFieldAsSelectItem(wfmStrings.category(), CustomFormConstants.CATEGORY));
        ids.add(getFieldAsSelectItem(wfmStrings.relationship(), CustomFormConstants.RELATIONSHIP));
        ids.add(getFieldAsSelectItem(wfmStrings.reportsTo(), CustomFormConstants.REPORTS_TO));
        ids.add(getFieldAsSelectItem(wfmStrings.owner(), CustomFormConstants.OWNER));
        ids.add(getFieldAsSelectItem(wfmStrings.campaign(), CustomFormConstants.CRM_CAMPAIGN_NAME));
        ids.add(getFieldAsSelectItem(wfmStrings.emailOptOut(), CustomFormConstants.EMAIL_OPT_OUT));
        ids.add(getFieldAsSelectItem(wfmStrings.subscriptionLists(), CustomFormConstants.SUBSCRIPTION_LIST));
        ids.add(getFieldAsSelectItem(wfmStrings.note(), CustomFormConstants.CRM_NOTE));
        ids.add(getFieldAsSelectItem(wfmStrings.attachment(), CustomFormConstants.ATTACHMENTS));
        ids.add(getFieldAsSelectItem(wfmStrings.attachment(), CustomFormConstants.ATTACHMENTS_MINI));
        return ids.toArray(new SelectItem[]{});
    }

    private SelectItem getFieldAsSelectItem(String title, String widgetID) {
        return new SelectItem(null, title, "<table class=\"spacing5-padding5\"><tr><td>LabelID:</td><td><b> $$label:" + widgetID + "$$</b></td></tr><tr><td>ElementID: </td><td><b>$$input:" + widgetID + "$$</b></td></tr></table>");
    }

    private void showCustomHTML(boolean show) {
        showSection(show ? "CUSTOM_LAYOUT" : "CONFIGURE_WEB_FIELD");
        hideSection(!show ? "CUSTOM_LAYOUT" : "CONFIGURE_WEB_FIELD");
        openSection(show ? "CUSTOM_LAYOUT" : "CONFIGURE_WEB_FIELD");
    }

    private native void executeExternalInjections()
        /*-{
            if($wnd.getAatribut){
                $wnd.getAatribut();
            }
        }-*/;

    public void reInitWebFields(boolean update) {
        sorter = new ArrayList<>();
        sortedPanels = new HashMap<>();

        LoadingPanel.loading(true);
        if (WebFormConstants.LEAD_FORM.equals(type)) {
            setLeadForm(this.item.getWebFields(), update);
        } else if (WebFormConstants.CASE_FORM.equals(type)) {
            setCaseForm(this.item.getWebFields(), update);
        } else if (WebFormConstants.CANDIDATE_FORM.equals(type)) {
            setCandidateForm(this.item.getWebFields(), update);
        }
        com.google.gwt.user.client.Timer timer = null;
        timer = new com.google.gwt.user.client.Timer() {
            @Override
            public void run() {
                if (form != null && form.isDropDownsFilled()) {
                    LoadingPanel.loading(false);
                    drawConfigurablePanel();
                    this.cancel();
                } else if (form == null) {
                    this.cancel();
                    LoadingPanel.loading(false);
                    forceUseringCustomLayout();
                }
            }
        };
        timer.scheduleRepeating(1500);
    }

    private void forceUseringCustomLayout() {
        forceUsingCustomLayout = true;
        if (useCustomLayout != null) {
            useCustomLayout.setValue(true, true);
        }
    }

    private void drawConfigurablePanel() {
        tableOfFields.removeAllRows();
        tableOfFields.setCellSpacing(VERTICAL_PANEL_SPACING);

        FlexTable horizontalPanel = new FlexTable();
        horizontalPanel.addStyleName("bulletin-GWTCode simpleThead");

        horizontalPanel.setText(0, 0, wfmStrings.sectionName());
        horizontalPanel.setText(0, 1, wfmStrings.originalLabel());
        horizontalPanel.setText(0, 2, wfmStrings.labelInForm());
        horizontalPanel.setText(0, 3, wfmStrings.showHide());
        horizontalPanel.setText(0, 4, wfmStrings.mandatory());
        horizontalPanel.setText(0, 5, wfmStrings.defaultValue());
        horizontalPanel.setText(0, 6, wfmStrings.line());
        horizontalPanel.setText(0, 7, wfmStrings.order());
        horizontalPanel.getFlexCellFormatter().setWidth(0, 0, "150px");
        horizontalPanel.getFlexCellFormatter().setWidth(0, 1, "150px");
        horizontalPanel.getFlexCellFormatter().setWidth(0, 2, "150px");
        horizontalPanel.getFlexCellFormatter().setWidth(0, 3, "70px");
        horizontalPanel.getFlexCellFormatter().setWidth(0, 4, "70px");
        horizontalPanel.getFlexCellFormatter().setWidth(0, 5, "200px");
        horizontalPanel.getFlexCellFormatter().setWidth(0, 6, "50px");
        horizontalPanel.getFlexCellFormatter().setWidth(0, 7, "50px");

        horizontalPanel.getFlexCellFormatter().setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_CENTER);
        horizontalPanel.getFlexCellFormatter().setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_CENTER);
        horizontalPanel.getFlexCellFormatter().setHorizontalAlignment(0, 7, HasHorizontalAlignment.ALIGN_CENTER);
        horizontalPanel.getRowFormatter().setStyleName(0, "thead");

        tableOfFields.setWidget(0, 0, horizontalPanel);
        for (WebField webField : form.getWebFields()) {
            addFieldToPanel(webField);
        }
        if (WebFormConstants.CASE_FORM.equals(type)) {
            addListeners();
        }
        this.item.setWebFields(form.getWebFields());
    }

    private void addFieldToPanel(final WebField webField) {
        if (sorter.size() == 0) {
            sorter.add(null);
        }
        sorter.add(webField.getSortOrder() != null && webField.getSortOrder() <= sorter.size() ? webField.getSortOrder() : sorter.size(), webField);
        final VerticalPanel verticalPanel = new VerticalPanel();
        final HorizontalPanel addGroupTitle = new HorizontalPanel();
        final FlexTable sorterPanel = getSorterPanel(webField);
        final TextBox groupTitleTextBox = new TextBox();
        addGroupTitle.add(groupTitleTextBox);
        groupTitleTextBox.setStyleName("customTitle");
        groupTitleTextBox.addBlurHandler(event -> webField.setGroupTitle(groupTitleTextBox.getText()));
        if (webField.getGroupTitle() != null && !"".equals(webField.getGroupTitle())) {
            groupTitleTextBox.setText(webField.getGroupTitle());
            addGroupTitle.setVisible(true);
        } else {
            addGroupTitle.setVisible(false);
        }
        final HTML addGroupTitleLink = new HTML(addGroupTitle.isVisible() ? wfmStrings.removeSection() : wfmStrings.addSection());
        addGroupTitleLink.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        addGroupTitleLink.getElement().getStyle().setColor("#15628F");
        addGroupTitleLink.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        addGroupTitleLink.addClickHandler(event -> {
            addGroupTitleLink.setText(addGroupTitle.isVisible() ? wfmStrings.addSection() : wfmStrings.removeSection());
            if (addGroupTitle.isVisible()) {
                groupTitleTextBox.setText("");
                webField.setGroupTitle("");
            }
            addGroupTitle.setVisible(!addGroupTitle.isVisible());
        });
        verticalPanel.add(addGroupTitle);
        final TextBox newLabel = new TextBox();
//        newLabel.setWidth("150px");
        newLabel.addBlurHandler(event -> webField.setLabel(newLabel.getText()));

        final KpiCheckBox showInForm = new KpiCheckBox("");
        if (!webField.isUnchangable()) {
            showInForm.addBlurHandler(event -> webField.setShowInForm(showInForm.getValue()));
            showInForm.addClickHandler(event -> webField.setShowInForm(showInForm.getValue()));
        }
        final KpiCheckBox mandatory = new KpiCheckBox("");
        mandatory.addBlurHandler(event -> webField.setMandatory(mandatory.getValue()));
        mandatory.addClickHandler(event -> webField.setMandatory(mandatory.getValue()));
        final KpiCheckBox drawLine = new KpiCheckBox("");
        drawLine.addBlurHandler(event -> webField.setDrawLine(drawLine.getValue()));
        drawLine.addClickHandler(event -> webField.setDrawLine(drawLine.getValue()));

        HTML originalLabel = new HTML("<span class='customLabel'>" + webField.getOriginalLabel() + "</span>");

        newLabel.setText(webField.getLabel());

        showInForm.setValue(webField.isShowInForm());

        mandatory.setValue(webField.isMandatory());

        drawLine.setValue(webField.isDrawLine());

        if (webField.isUnchangable()) {
            mandatory.setEnabled(false);
        }

        Widget defaultValueWidget = null;
        if (webField.getType().equals(WebFormConstants.INPUT_DROPDOWN)) {
            final DataListBox dataListBox = new DataListBox();
            dataListBox.setStyleName("form-control");
            dataListBox.addBlurHandler(event -> webField.setDefaultValue(dataListBox.getSelectedId() != null ? webField.isCustomField() ? dataListBox.getSelectedItem().getName() : dataListBox.getSelectedId().toString() : null));
            if (webField.getValues() != null) {
                dataListBox.setItems(webField.getValues());
                if (webField.getDefaultValue() != null) {
                    if (webField.getDefaultValue() instanceof SelectItem) {
                        dataListBox.setSelected((SelectItem) webField.getDefaultValue());
                    } else if (webField.getDefaultValue() instanceof Integer) {
                        if (webField.isCustomField()) {
                            dataListBox.setSelectedByValue(String.valueOf(webField.getDefaultValue()));
                        } else {
                            dataListBox.setSelected((Integer) webField.getDefaultValue());
                        }
                    } else if (webField.getDefaultValue() instanceof String) {
                        dataListBox.setSelectedByValue((String) webField.getDefaultValue());
                    }
                }
            }
            if (isCountryField(webField.getSavingField())) {
                countryStates.setCountryField(dataListBox);
                countryStates.setCountries(countryStates.getCountryField().getItems());
            }
            if (isStateField(webField.getSavingField())) {
                Integer countryID = countryStates.getCountryField() != null ? countryStates.getCountryField().getSelectedId() : null;
                SelectItem selectedState = countryStates.getStateField().getSelectedItem();
                countryStates.setStateField(dataListBox);
                countryStates.setStates(countryStates.getStateField().getItems());
                countryStates.getStateField().removeListItems();
                countryStates.checkForStates(countryID, countryStates.getStateField());
                if (selectedState != null) {
                    countryStates.getStateField().setSelected(selectedState);
                }
            }
            defaultValueWidget = dataListBox;
            if (WebFormConstants.CASE_FORM.equals(type)) {
                if (CaseField.FIELD_TYPE == webField.getSavingField()) {
                    types = dataListBox;
                } else if (CaseField.FIELD_CASE_REASON == webField.getSavingField()) {
                    caseReason = dataListBox;
                    caseReasons = webField.getValues();
                }
            }
        } else if (webField.getType().equals(WebFormConstants.INPUT_DATEPICKER)) {
            final DatePicker datePicker = new DatePicker();
            datePicker.addStyleName(DEFAULT_WIDTH);
            datePicker.addChangeHandler(event -> {
                if (datePicker.getDate() != null) {
                    webField.setDefaultValue("" + datePicker.getDate().getTime());
                } else {
                    webField.setDefaultValue("");
                }
            });
            if (webField.getDefaultValue() != null) {
                if (webField.getDefaultValue() instanceof Date) {
                    datePicker.setDate((Date) webField.getDefaultValue());
                }
            }
            defaultValueWidget = datePicker;
        } else if (webField.getType().equals(WebFormConstants.INPUT_TEXTBOX)) {
            final TextBox textBox = new TextBox();
            textBox.addStyleName(DEFAULT_WIDTH);
            textBox.addBlurHandler(event -> {
                if (webField != null && webField.isOnlyIntegerAllowed()) {
                    textBox.setText(removeOtherThen(textBox.getText(), "^[0-9]$", "^\\.$"));
                }
                webField.setDefaultValue(textBox.getText());
            });
            if (webField.getDefaultValue() != null) {
                textBox.setText((String) webField.getDefaultValue());
            }
            defaultValueWidget = textBox;
        } else if (webField.getType().equals(WebFormConstants.INPUT_TEXTBOX2)) {
            final TextBox2 textBox2 = new TextBox2();
            textBox2.addStyleName(DEFAULT_WIDTH);
            textBox2.addBlurHandler(event -> {
                if (webField != null && webField.isOnlyIntegerAllowed()) {
                    textBox2.setText(removeOtherThen(textBox2.getText(), "^[0-9]$", "^\\.$"));
                }
                webField.setDefaultValue(textBox2.getText());
            });
            if (webField.getDefaultValue() != null) {
                textBox2.setText((String) webField.getDefaultValue());
            }
            defaultValueWidget = textBox2;
        } else if (webField.getType().equals(WebFormConstants.INPUT_TEXTAREA)) {
            final TextArea textArea = new TextArea();
            textArea.addBlurHandler(event -> webField.setDefaultValue(textArea.getText()));
            if (webField.getDefaultValue() != null) {
                textArea.setText((String) webField.getDefaultValue());
            }
            defaultValueWidget = textArea;
        } else if (webField.getType().equals(WebFormConstants.INPUT_TEXTAREA2)) {
            final TextArea2 textArea2 = new TextArea2(1000);
            textArea2.addBlurHandler(event -> webField.setDefaultValue(textArea2.getText()));
            if (webField.getDefaultValue() != null) {
                textArea2.setText((String) webField.getDefaultValue());
            }
            defaultValueWidget = textArea2;
        } else if (webField.getType().equals(WebFormConstants.INPUT_PHONENUMBER)) {
            final PhoneNumber phoneNumber = new PhoneNumber(webField.getDefaultValue() != null ? (String) webField.getDefaultValue() : "");
            phoneNumber.addBlurHandler(event -> webField.setDefaultValue(phoneNumber.toString()));
            defaultValueWidget = phoneNumber.getField();
        } else if (webField.getType().equals(WebFormConstants.INPUT_DOB)) {
            final DOBWidget dobWidget = new DOBWidget();
//            dobWidget.setWidth("50px", "107px", "70px");
            if (webField.getDefaultValue() != null) {
                if (webField.getDefaultValue() instanceof Date) {
                    Date defaultValueD = (Date) webField.getDefaultValue();
                    dobWidget.setSelected(defaultValueD.getDate(), defaultValueD.getMonth(), defaultValueD.getYear());
                } else if (webField.getDefaultValue() instanceof DateNonConvertable) {
                    DateNonConvertable defaultValueDN = (DateNonConvertable) webField.getDefaultValue();
                    if (defaultValueDN != null && defaultValueDN.getDate() != null) {
                        dobWidget.setSelected(defaultValueDN.getDate().getDate(), defaultValueDN.getDate().getMonth(), defaultValueDN.getDate().getYear());
                    }
                }
            }
            dobWidget.addBlurHandler(event -> {
                if (dobWidget.getConvertableDOBDate() != null) {
                    webField.setDefaultValue(dobWidget.getConvertableDOBDate().toString());
                }
            });
            defaultValueWidget = dobWidget;
        } else if (webField.getType().equals(WebFormConstants.INPUT_VACANCIES)) {
            final MatrixTable vacanciesTable = new MatrixTable(1);
            if (webField.getValues() != null) {
                if (webField.getValues() instanceof SelectItem[]) {
                    SelectItem[] vacanciesS = webField.getValues();
                    if (vacanciesS != null && vacanciesS.length > 0) {
                        vacanciesTable.addItems(CRMUtils.getSelectItemsAsCheckBoxMap(false, vacanciesS), true);
                    } else {
                        vacanciesTable.clear();
                    }
                }
            }
            vacanciesTable.addBlurHandler(event -> {
                if (vacanciesTable.getValuesMap() != null) {
                    webField.setDefaultValue(vacanciesTable.getValuesMap().toString());
                }
            });
            defaultValueWidget = vacanciesTable;
        } else if (webField.getType().equals(WebFormConstants.INPUT_CHECKBOX)) {
            if (webField.isCustomField() && webField.getValues() != null) {
                SelectItem[] defaultValues = Utils.getAsSelectItem((String) webField.getDefaultValue(true), webField.getDefaultValue() != null && ((String) webField.getDefaultValue(true)).contains(",") ? "," : "'");
                List<KpiCheckBox> checkBoxes = new ArrayList<>();
                if (webField.getValues() != null) {
                    for (SelectItem predefinedValue : webField.getValues()) {
                        if (predefinedValue != null) {
                            final KpiCheckBox checkBox = new KpiCheckBox(predefinedValue.getName());
                            checkBox.setName(predefinedValue.getName());
                            if (defaultValues != null) {
                                for (SelectItem defaultValue : defaultValues) {
                                    if (defaultValue != null && defaultValue.getName() != null && predefinedValue.getName() != null && predefinedValue.getName().equals(defaultValue.getName())) {
                                        checkBox.setValue(true);
                                    }
                                }
                            }
                            checkBox.addBlurHandler(event -> {
                                if (checkBox.getValue()) {
                                    webField.setDefaultValue(checkBox.getText(), true);
                                } else {
                                    webField.removeDefaultValue(checkBox.getText(), true);
                                }
                            });
                            checkBox.addClickHandler(event -> {
                                if (checkBox.getValue()) {
                                    webField.setDefaultValue(checkBox.getText(), true);
                                } else {
                                    webField.removeDefaultValue(checkBox.getText(), true);
                                }
                            });
                            checkBoxes.add(checkBox);
                        }
                    }
                }
                VerticalPanel vp = new VerticalPanel();
                for (KpiCheckBox checkBox : checkBoxes) {
                    vp.add(checkBox);
                }
                defaultValueWidget = vp;
            } else {
                final KpiCheckBox checkBox = new KpiCheckBox();
                checkBox.addBlurHandler(event -> webField.setDefaultValue(checkBox.getValue().toString()));
                if (webField.getDefaultValue() != null) {
                    checkBox.setValue((Boolean) webField.getDefaultValue());
                }
                defaultValueWidget = checkBox;
            }
        } else if (webField.getType().equals(WebFormConstants.INPUT_RADIO_BUTTON)) {
            if (webField.isCustomField() && webField.getValues() != null) {
                SelectItem[] defaultValues = Utils.getAsSelectItem((String) webField.getDefaultValue(true), webField.getDefaultValue() != null && ((String) webField.getDefaultValue(true)).contains(",") ? "," : "'");
                List<RadioButton> radioButtons = new ArrayList<>();
                String radioButtonName = "rb" + (new Date()).getTime();
                for (SelectItem predefinedValue : webField.getValues()) {
                    if (predefinedValue != null) {
                        final RadioButton radioButton = new KpiRadioButton(predefinedValue.getName());
                        radioButton.setText(predefinedValue.getName());
                        radioButton.setName(radioButtonName);
                        if (defaultValues != null) {
                            for (SelectItem defaultValue : defaultValues) {
                                if (defaultValue != null && defaultValue.getName() != null && predefinedValue.getName() != null && predefinedValue.getName().equals(defaultValue.getName())) {
                                    radioButton.setValue(true);
                                }
                            }
                        }
                        radioButton.addBlurHandler(event -> {
                            if (radioButton.getValue()) {
                                webField.setDefaultValue(radioButton.getText(), true);
                            } else {
                                webField.removeDefaultValue(radioButton.getText(), true);
                            }
                        });
                        radioButton.addClickHandler(event -> {
                            if (radioButton.getValue()) {
                                webField.setDefaultValue(radioButton.getText(), true);
                            } else {
                                webField.removeDefaultValue(radioButton.getText(), true);
                            }
                        });
                        radioButtons.add(radioButton);
                    }
                }
                VerticalPanel vp = new VerticalPanel();
                for (RadioButton radioButton : radioButtons) {
                    vp.add(radioButton);
                }
                defaultValueWidget = vp;
            } else {
                final RadioButton radioButton = new KpiRadioButton(webField.getOriginalLabel());
                radioButton.addBlurHandler(event -> webField.setDefaultValue(radioButton.getValue().toString()));
                if (webField.getDefaultValue() != null) {
                    radioButton.setValue((Boolean) webField.getDefaultValue());
                }
                defaultValueWidget = radioButton;
            }
        } else if (webField.getType().equals(WebFormConstants.INPUT_ATTACHMENT)) {
            final GWTFileUpload uploadFile = new GWTFileUpload(true);
            uploadFile.setStyleName("GWTUpld-abs");
            uploadFile.setEnabled(true);
            defaultValueWidget = uploadFile;
        } else if (webField.getType().equals(WebFormConstants.INPUT_MAILING_LIST)) {
            final CheckboxMailingListDataGrid mailingListTable = new CheckboxMailingListDataGrid(null, false, webField.getDefaultValue() != null ? webField.getDefaultValue().toString() : "");
            mailingListTable.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
            mailingListTable.getElement().getStyle().setBorderColor("#DDDDDD");
            mailingListTable.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
            mailingListTable.addStyleName(DEFAULT_WIDTH);
            com.google.gwt.user.client.Timer timer = new com.google.gwt.user.client.Timer() {
                @Override
                public void run() {
                    if (mailingListTable != null) {
                        mailingListTable.refresh();
                    }
                }
            };
            timer.scheduleRepeating(500);

            mailingListTable.setCheckBoxCellFieldUpdater((index, object, value) -> {
                StringBuilder valueStr = new StringBuilder();
                String delimitr = "";
                mailingListTable.updateChecked(object, value);
                for (SelectItem item : mailingListTable.getSelectItemsList()) {
                    valueStr.append(delimitr).append(item.getId().toString());
                    delimitr = ",";
                }
                webField.setDefaultValue(valueStr.toString());
            });
            defaultValueWidget = mailingListTable;
        }
        if (webField.isUnchangable()) {
            addBlurHandler(webField, showInForm, defaultValueWidget, webField.getType());
        }

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setStyleName("webFormTableFields-child");
        horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        horizontalPanel.setSpacing(HORIZONTAL_PANEL_SPACING);
        horizontalPanel.add(addGroupTitleLink);

        horizontalPanel.add(originalLabel);
        horizontalPanel.add(newLabel);
        horizontalPanel.add(showInForm);
        horizontalPanel.add(mandatory);
        if (WebFormConstants.CASE_FORM.equals(type) && CaseField.FIELD_ASSIGNEE == webField.getSavingField()) {
            showInForm.setEnabled(false);
            mandatory.setEnabled(false);
        }
        if (defaultValueWidget != null) {
            horizontalPanel.add(defaultValueWidget);
            horizontalPanel.setCellWidth(defaultValueWidget, "200px");
        }
        horizontalPanel.add(drawLine);
        horizontalPanel.add(sorterPanel);
        horizontalPanel.setCellWidth(addGroupTitleLink, "150px");
        horizontalPanel.setCellWidth(originalLabel, "150px");
        horizontalPanel.setCellWidth(newLabel, "150px");
        horizontalPanel.setCellWidth(showInForm, "70px");
        horizontalPanel.setCellWidth(mandatory, "70px");
        horizontalPanel.setCellWidth(drawLine, "50px");
        horizontalPanel.setCellWidth(sorterPanel, "50px");
        horizontalPanel.setCellHorizontalAlignment(showInForm, HasHorizontalAlignment.ALIGN_CENTER);
        horizontalPanel.setCellHorizontalAlignment(mandatory, HasHorizontalAlignment.ALIGN_CENTER);
        horizontalPanel.setCellHorizontalAlignment(drawLine, HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.add(horizontalPanel);
        sortedPanels.put(webField, verticalPanel);
        if (webField.getSortOrder() == null) {
            webField.setSortOrder(tableOfFields.getRowCount());
        }

        tableOfFields.setWidget(sorter.size(), 0, verticalPanel);
    }

    private void addListeners() {
        if (types != null && types.getSelectedId() != null) {
            if (caseReasons != null && caseReasons.length > 0) {
                Integer caseReasonSelected = caseReason.getSelectedId();
                ArrayList<SelectItem> list = new ArrayList<>();
                for (SelectItem val : caseReasons) {
                    ReferenceItem rItem = (ReferenceItem) val;
                    if (rItem.getRelative() == null || "".equals(rItem.getRelative())) {
                        list.add(val);
                    } else {
                        String[] ids = rItem.getRelative().split(",");
                        for (String id : ids) {
                            if (types.getSelectedId().toString().equals(id)) {
                                list.add(val);
                                break;
                            }
                        }
                    }
                }
                caseReason.setItems(list.toArray(new SelectItem[]{}));
                caseReason.setSelected(caseReasonSelected);
            }
        }
        types.addValueChangeHandler(changeEvent -> {
            if (types.getSelectedId() == null && caseReasons != null) {
                caseReason.setItems(caseReasons);
            } else {
                if (caseReasons != null && caseReasons.length > 0) {
                    ArrayList<SelectItem> list = new ArrayList<>();
                    for (SelectItem val : caseReasons) {
                        ReferenceItem rItem = (ReferenceItem) val;
                        if (rItem.getRelative() == null || "".equals(rItem.getRelative())) {
                            list.add(val);
                        } else {
                            String[] ids = rItem.getRelative().split(",");
                            for (String id : ids) {
                                if (types.getSelectedId().toString().equals(id)) {
                                    list.add(val);
                                    break;
                                }
                            }
                        }
                    }
                    caseReason.setItems(list.toArray(new SelectItem[]{}));
                }
            }
        });
    }

    private void addClickHandler(MaterialLink image, final WebField webField, final boolean direction) {
        image.addClickHandler(event -> sortPanel(webField, direction));
    }

    private void sortPanel(final WebField webField, boolean direction) {
        if (webField.getSortOrder() == null) {
            webField.setSortOrder(sorter.indexOf(webField));
        }
        if ((direction && webField.getSortOrder() > 1) || (!direction && webField.getSortOrder() < this.item.getWebFields().length)) {
            WebField temp;
            if (direction) {
                webField.setSortOrder(webField.getSortOrder() - 1);
                temp = sorter.get(webField.getSortOrder());
                temp.setSortOrder(webField.getSortOrder() + 1);
                tableOfFields.setWidget(webField.getSortOrder() + 1, 0, sortedPanels.get(webField));
                tableOfFields.setWidget(temp.getSortOrder() + 1, 0, sortedPanels.get(temp));
            } else {
                webField.setSortOrder(webField.getSortOrder() + 1);
                temp = sorter.get(webField.getSortOrder());
                temp.setSortOrder(webField.getSortOrder() - 1);
                tableOfFields.setWidget(temp.getSortOrder() + 1, 0, sortedPanels.get(temp));
                tableOfFields.setWidget(webField.getSortOrder() + 1, 0, sortedPanels.get(webField));
            }
            sorter.remove(webField);
            sorter.add(webField.getSortOrder(), webField);
        }
    }

    private void addBlurHandler(final WebField webField, final KpiCheckBox showInForm, final Widget widget, final Integer type) {
        showInForm.addBlurHandler(event -> showInFormEventAdder(webField, showInForm, widget, type));
        showInForm.addClickHandler(event -> showInFormEventAdder(webField, showInForm, widget, type));
    }

    private void showInFormEventAdder(final WebField webField, final KpiCheckBox showInForm, final Widget widget, final Integer type) {
        widget.removeStyleName(STYLE);
        webField.setShowInForm(showInForm.getValue());
        if (!showInForm.getValue()) {
            if (type.equals(WebFormConstants.INPUT_DROPDOWN)) {
                final DataListBox dataListBox = (DataListBox) widget;
                if (dataListBox.getSelectedItem() == null) {
                    errors.put(webField.getSavingField(), widget);
                }
                dataListBox.addBlurHandler(event -> {
                    errors.remove(webField.getSavingField());
                    widget.removeStyleName(STYLE);
                    webField.setDefaultValue(dataListBox.getSelectedId() != null ? dataListBox.getSelectedId().toString() : null);
                    if (dataListBox.getSelectedItem() == null) {
                        errors.put(webField.getSavingField(), widget);
                        widget.addStyleName(STYLE);
                    }
                });
            } else if (type.equals(WebFormConstants.INPUT_TEXTBOX)) {
                final TextBox textBox = (TextBox) widget;
                if (textBox.getText() == null || textBox.getText().equals("")) {
                    errors.put(webField.getSavingField(), widget);
                }
                textBox.addBlurHandler(event -> {
                    errors.remove(webField.getSavingField());
                    widget.removeStyleName(STYLE);
                    webField.setDefaultValue(textBox.getText());
                    if (textBox.getText() == null || textBox.getText().equals("")) {
                        errors.put(webField.getSavingField(), widget);
                        widget.addStyleName(STYLE);
                    }
                });
            } else if (type.equals(WebFormConstants.INPUT_DATEPICKER)) {
                final DatePicker datePicker = (DatePicker) widget;
                if (datePicker.getDate() == null || datePicker.getText().equals("")) {
                    errors.put(webField.getSavingField(), widget);
                }
                datePicker.addChangeHandler(event -> {
                    errors.remove(webField.getSavingField());
                    widget.removeStyleName(STYLE);
                    if (datePicker.getDate() != null) {
                        webField.setDefaultValue("" + datePicker.getDate().getTime());
                    } else {
                        webField.setDefaultValue("");
                    }
                    if (datePicker.getDate() == null || datePicker.getText().equals("")) {
                        errors.put(webField.getSavingField(), widget);
                        widget.addStyleName(STYLE);
                    }
                });
            } else if (type.equals(WebFormConstants.INPUT_PHONENUMBER)) {
                final PhoneNumber phoneNumber = (PhoneNumber) widget;
                if (phoneNumber.toString() == null || phoneNumber.toString().equals("")) {
                    errors.put(webField.getSavingField(), widget);
                }
                phoneNumber.addBlurHandler(event -> {
                    errors.remove(webField.getSavingField());
                    widget.removeStyleName(STYLE);
                    webField.setDefaultValue(phoneNumber.toString());
                    if (phoneNumber.toString() == null || phoneNumber.toString().equals("")) {
                        errors.put(webField.getSavingField(), widget);
                        widget.addStyleName(STYLE);
                    }
                });
            } else if (type.equals(WebFormConstants.INPUT_DOB)) {
                final DOBWidget dobWidget = (DOBWidget) widget;
                if (dobWidget.getConvertableDOBDate() == null || dobWidget.getConvertableDOBDate().getDate() == null) {
                    errors.put(webField.getSavingField(), widget);
                }
                dobWidget.addBlurHandler(event -> {
                    errors.remove(webField.getSavingField());
                    widget.removeStyleName(STYLE);

                    if (dobWidget.getConvertableDOBDate() != null && dobWidget.getConvertableDOBDate().getDate() != null) {
                        webField.setDefaultValue("" + dobWidget.getConvertableDOBDate().getDate().getTime());
                    } else {
                        webField.setDefaultValue("");
                    }
                    if (dobWidget.getConvertableDOBDate() == null || dobWidget.getConvertableDOBDate().getDate() == null) {
                        errors.put(webField.getSavingField(), widget);
                        widget.addStyleName(STYLE);
                    }
                });
            } else if (type.equals(WebFormConstants.INPUT_CHECKBOX)) {
                final KpiCheckBox checkBox = (KpiCheckBox) widget;
                if (checkBox.getValue() != null || !checkBox.getValue()) {
                    errors.put(webField.getSavingField(), widget);
                }
                checkBox.addBlurHandler(event -> {
                    errors.remove(webField.getSavingField());
                    widget.removeStyleName(STYLE);
                    webField.setDefaultValue(checkBox.getValue().toString());
                    if (checkBox.getValue() != null || !checkBox.getValue()) {
                        errors.put(webField.getSavingField(), widget);
                        widget.addStyleName(STYLE);
                    }
                });
            } else if (type.equals(WebFormConstants.INPUT_RADIO_BUTTON)) {
                final RadioButton radioButton = (RadioButton) widget;
                if (radioButton.getValue() != null || !radioButton.getValue()) {
                    errors.put(webField.getSavingField(), widget);
                }
                radioButton.addBlurHandler(event -> {
                    errors.remove(webField.getSavingField());
                    widget.removeStyleName(STYLE);
                    webField.setDefaultValue(radioButton.getValue().toString());
                    if (radioButton.getValue() != null || !radioButton.getValue()) {
                        errors.put(webField.getSavingField(), widget);
                        widget.addStyleName(STYLE);
                    }
                });
            }
            if (errors.containsKey(webField.getSavingField())) {
                widget.setStyleName(STYLE);
            }
        } else {
            widget.removeStyleName(STYLE);
            errors.remove(webField.getSavingField());
        }
    }

    private boolean isCountryField(int field) {
        return field == LeadForm.FIELD_COUNTRY;
    }

    private boolean isStateField(int field) {
        return field == LeadForm.FIELD_STATE;
    }

    private FlexTable getSorterPanel(WebField webField) {
        final FlexTable hpSorter = new FlexTable();
        MaterialLink up = new MaterialLink();
        up.addStyleName("dropdown-kit--arrow--below");
        MaterialLink down = new MaterialLink();
        down.addStyleName("dropdown-kit--arrow--below--reverse");
        addClickHandler(up, webField, true);
        addClickHandler(down, webField, false);
        hpSorter.setWidget(0, 0, up);
        hpSorter.setWidget(0, 1, down);
        return hpSorter;
    }

    private void setLeadForm(WebField[] webFields, boolean update) {
        form = new LeadForm(update, webFields);
    }

    private void setCaseForm(WebField[] webFields, boolean update) {
        form = new CaseForm(update, webFields);
    }

    private void setCandidateForm(WebField[] webFields, boolean update) {
        form = new CandidateForm(update, webFields);
    }

    private void fillTable(WebForm result) {
        item = result;
        if (objectId != null && item.getWebFormType() != null) {
            type = item.getWebFormType().getCode();
        }
        reInitWebFields(false);
        idPicker.setItems(initFormIDs(type));
        title.setText(item.getTitle());
        description.setData(item.getDescription() != null ? item.getDescription() : "");
        confirmationMessage.setData(item.getConfirmationMessage() != null ? item.getConfirmationMessage() : "");
        redirectURL.setText(item.getRedirectURL());
        formType.setItems(item.getFormTypes());
        buttonText.setText(item.getButtonText());
        useCaptcha.setValue(item.getUseCatpcha());
        if (item.getUseCatpcha()) {
            captchaPanel.getParent().setVisible(true);
        }
        captchaLabel.setText(item.getCaptchaLabel());
        captchaDescription.setText(item.getCaptchaDescription());
        captchaCantReadText.setText(item.getCaptchaCantRead());
        captchaTryAnotherLink.setText(item.getCaptchaTryAnother());
        initPreviewCaptcha();
        if (item.getWebFormType() != null) {
            formType.setSelected(item.getWebFormType());
        } else {
            for (SelectItem selectItem : item.getFormTypes()) {
                ReferenceItem referenceItem = (ReferenceItem) selectItem;
                if (type.equals(referenceItem.getCode())) {
                    formType.setSelected(selectItem);
                }
            }
        }
        formType.setEnabled(false);

        if (item.getCustomForm() != null && item.getCustomForm().getObjectID() != null) {
            contentHTML.setData(item.getCustomForm().getLayout());
            customCss.setText(item.getCustomForm().getCustomCss());
            useCustomLayout.setValue(true);
        }
        com.google.gwt.user.client.Timer timer = new Timer() {
            @Override
            public void run() {
                showCustomHTML(item.getCustomForm() != null && item.getCustomForm().getObjectID() != null);
            }
        };
        timer.schedule(500);
    }

    private void initPreviewCaptcha() {
        previewLabel.setHTML("<b style=\"width:124px;\" class='customLabel subLabel'>&nbsp;&nbsp;&nbsp;&nbsp;" + captchaLabel.getText() + " : </b>");
        previewDescription.setHTML(captchaDescription.getText());
        previewCantRead.setHTML(captchaCantReadText.getText());
        previewTryAnotherLink.setHTML(captchaTryAnotherLink.getText());
    }

    private String removeOtherThen(String text, String regex, String regex2OnlyOnceTimeMustBeAdded) {
        if (text != null && !"".equals(text)) {
            StringBuilder newText = new StringBuilder();
            boolean pointAdded = false;
            for (int i = 0; i < text.length(); i++) {
                String s = text.substring(i, i + 1);
                if (s.matches(regex)) {
                    newText.append(s);
                }
                if (s.matches(regex2OnlyOnceTimeMustBeAdded) && !pointAdded) {
                    newText.append(s);
                    pointAdded = true;
                }
            }
            return newText.toString();
        }
        return null;
    }

    private void save() {
        if (!validate()) {
            return;
        }
        if (item == null) {
            item = new WebForm();
        }
        item.setTitle(title.getText());
        item.setDescription(description.getData());
        if (confirmationMessage.getData() != null && !"".equals(confirmationMessage.getData())) {
            if (confirmationMessage.getData().toLowerCase().startsWith("http://")) {
                item.setConfirmationMessage(confirmationMessage.getData().replace("<br>", ""));
            } else {
                item.setConfirmationMessage(confirmationMessage.getData());
            }
        }
        item.setRedirectURL(redirectURL.getText());
        if (item.getRedirectURL() == null || "".equals(item.getRedirectURL().trim())) {
            item.setRedirectURL(null);
        } else if (!item.getRedirectURL().toLowerCase().startsWith("http://")) {
            item.setRedirectURL("http://" + item.getRedirectURL());
        }
        item.setUseCatpcha(useCaptcha.getValue());
        item.setCaptchaLabel(captchaLabel.getText());
        item.setCaptchaDescription(captchaDescription.getText());
        item.setCaptchaCantRead(captchaCantReadText.getText());
        item.setCaptchaTryAnother(captchaTryAnotherLink.getText());
        item.setButtonText(buttonText.getText());
        if (useCustomLayout.getValue()) {
            if (item.getCustomForm() == null) {
                item.setCustomForm(new LayoutRPC());
                item.getCustomForm().setWebForm(true);
                item.getCustomForm().setWebFormUrl(item.getiFrameUrl());
            }
            item.getCustomForm().setLayout(contentHTML.getData());
            item.getCustomForm().setCustomCss(customCss.getText());
        } else {
            item.setCustomForm(null);
        }
        item.setWebFormType(formType.getSelectedItem() == null ? formType.getItems()[0] : formType.getSelectedItem());
        formType.setEnabled(false);
        if (form != null) {
            this.item.setWebFields(form.getWebFields());//its second form
        }
        LoadingPanel.loading(true);
        crmService.saveWebForm(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), crmStrings.webFormView()));
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WEB_FORM_ADD_EDIT, result, AddWebFormView.this);
                closeTab();
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();
        errors += markAsError(WEBFORM.FORM_TYPE, formType, formType.getSelectedId() == null || "".equals(formType.getSelectedId()));
        errors += markAsError(WEBFORM.TITLE, title, title.getValue() == null || "".equals(title.getValue()));
        errors += markAsError(WEBFORM.BUTTON_TEXT, buttonText, title.getValue() == null || "".equals(buttonText.getValue()));
        if (useCustomLayout.getValue() && (contentHTML.getData() == null || "".equals(contentHTML.getData().trim()))) {
            contentHTML.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    public String getIconStyle() {
        return "bgMark icon-task";
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
