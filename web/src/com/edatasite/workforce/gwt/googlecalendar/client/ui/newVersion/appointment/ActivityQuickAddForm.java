package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.ContactTypeForTwilio;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeForTwilio;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.OpportunityItemForTwilio;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Attendee;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.client.ui.communication.ContactDetailsItem;
import com.edatasite.workforce.gwt.core.client.ui.communication.handlers.CallCommand;
import com.edatasite.workforce.gwt.core.client.ui.communication.handlers.TwilioCallHandler;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextArea;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ContactLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceQuoteLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.shortcut.ShortcutItem;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.RequestForQuoteLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarServiceAsync;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_CANDIDATE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PRODUCT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_QUOTE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_QUOTE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.LOGISTICS_PRODUCT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.LOGISTICS_PURCHASE_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_ACCOUNT;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ET_CONTACT_MODULE;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ET_EVENT_MODULE;

/**
 * Author: Azazello
 * Date: 5/14/2018
 * Time: 6:18 PM
 */
public class ActivityQuickAddForm extends KpiSideNavBox {

    interface ActivityQuickAddFormUiBinder extends UiBinder<HTMLPanel, ActivityQuickAddForm> {
    }

    private static final ActivityQuickAddFormUiBinder ourUiBinder = GWT.create(ActivityQuickAddFormUiBinder.class);

    private final GoogleCalendarServiceAsync calendarService = GoogleCalendarService.App.get();
    private Command command;
    private SaveAppointmentHandler handler;
    private final int activityType;
    private ContactListItem contact;
    private String phoneNumber;
    private Appointment item;
    private String note;
    private ArrayList<RelationItem> relationItemsList = new ArrayList<>();
    private final String debug_id = "add_activity_";
    private InvoiceAdvancedOptions advancedOptions;
    private InvoiceCustomFieldsView customFieldsView;
    private EmployeeListItem employee;
    private final FlexTable relationsTable;
    private Integer zoomObjectId;
    private String zoomLink;


    @UiField
    HTMLPanel panel;
    @UiField
    HTMLPanel templatePanel;
    @UiField
    Label emailTemplatesLabel;
    @UiField
    DataListBox emailTemplates;
    @UiField
    HTMLPanel subjectPanel;
    @UiField
    Label subjectLabel;
    @UiField
    TextBox subject;
    @UiField
    HTMLPanel callTypeParentPanel;
    @UiField
    Label callTypeLabel;
    @UiField
    MaterialPanel callTypePanel;
    @UiField
    Label callRelatedLabel;
    @UiField
    MaterialPanel callRelatedPanel;
    @UiField
    HTMLPanel callRelatedParentPanel;
    @UiField
    Label callDetailLabel;
    @UiField
    MaterialPanel callDetailPanel;
    @UiField
    HTMLPanel callDetailParentPanel;
    @UiField
    HTMLPanel whenParentPanel;
    @UiField
    Label whenLabel;
    @UiField
    MaterialPanel whenPanel;
    @UiField
    Label descriptionLabel;
    @UiField
    KpiEditor description;
    @UiField
    HTMLPanel descriptionPanel;
    @UiField
    HTMLPanel shareParentPanel;
    @UiField
    HTMLPanel guestParentPanel;
    @UiField
    HTMLPanel reminderParentPanel;
    @UiField
    HTMLPanel cloneParentPanel;
    @UiField
    Label shareLabel;
    @UiField
    MaterialPanel sharePanel;
    @UiField
    Label guestLabel;
    @UiField
    MaterialPanel guestPanel;
    @UiField
    Label reminderLabel;
    @UiField
    MaterialPanel reminderPanel;
    @UiField
    Label cloneLabel;
    @UiField
    MaterialPanel clonePanel;
    @UiField
    MaterialPanel linkToPanel;
    @UiField
    HTMLPanel providerTempParentPanel;
    @UiField
    MaterialPanel providerTempPanel;
    @UiField
    HTMLPanel recepientPanel;
    @UiField
    Label recepientLabel;
    @UiField
    TextBox recepient;
    @UiField
    HTMLPanel contentParentPanel;
    @UiField
    MaterialPanel contentPanel;
    @UiField
    Label contentLabel;

    private KpiRadioButton inbound;
    private KpiRadioButton outbound;
    private KpiRadioButton missed;
    private KpiRadioButton currentCall;
    private KpiRadioButton complatedCall;
    private KpiRadioButton scheduleCall;
    private DatePicker startDate;
    private DatePicker endDate;
    private KpiTimePicker from;
    private KpiTimePicker to;
    private TextBox durationMin;
    private TextBox durationSec;
    private KpiSwitcher allDay;

    private DatePicker startDateClone;
    private DatePicker endDateClone;
    private KpiTimePicker fromClone;
    private KpiTimePicker toClone;
    private KpiSwitcher allDayClone;
    private KpiSwitcher clone;
    private MaterialPanel cloneWidget;

    private MultiSelectEmployeeLookUp assignee;
    private MultiTableNewUI reminders;
    private MultiTableNewUI guestTable;
    private WfmButton2 saveBtn;
    private SplitButton startCalling;
    private WfmButton2 sendSms;

    private RelationItem[] predefinedLinks;
    private DataListBox sectionList;
    private ArrayList<SelectItem> relationals;
    private List<SelectItem> customForms;
    private LookUp lookUp;
    private LookUp projectLookUp;
    private LookUp invoiceLookUp;
    private CRMLookUp taskLookUp;
    private CRMLookUp issueLookUp;
    private CRMLookUp eventLookUp;
    private DatePicker eventDateUp;
    private final Integer fromID = null;
    private final String fromType = RelationItem.TYPE_EVENT;
    private final String fromName = "";
    private final ArrayList<RelationItem> removedRelationItems = new ArrayList<>();
    private RelationItem firstPhoneRelationItem;
    private Integer saleInvoiceId;
    private ArrayList<CompanyCustomFieldItem> customFields = null;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private DataListBox smsProvider;
    private DataListBox template;
    private VerticalPanel textAreaPanel;
    public HorizontalPanel counter;
    public HorizontalPanel textPanel;
    public Label counterLabel;
    private int counterInt = 0;
    private KpiTextArea content;
    private RelationItem relationPhone;
    private String moduleType = null;
    private Boolean isfromSaleInvoice;
    private boolean hasCandidateRelation;

    private String invoiceNumber;
    private CrmAccountItem crmAccountItem;

    private boolean isForWhatsApp = false;

    public ActivityQuickAddForm(int activityType, RelationItem... relationItems) {
        super(550);
        ourUiBinder.createAndBindUi(this);
        this.activityType = activityType;
        this.isfromSaleInvoice = true;
        hasCandidateRelation = Arrays.stream(relationItems)
                .anyMatch(relationItem -> {
                    return relationItem != null && TYPE_CANDIDATE.equals(relationItem.getToType());
                });
        relationsTable = new FlexTable();
        relationsTable.addStyleName("RelatedTo-selection__wrapper");
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(Appointment.CALL_LOG == activityType ? ViewName.LogACall : ViewName.Activity, Appointment.CALL_LOG == activityType ? LayoutRPC.LOGACALL_FORM : LayoutRPC.ACTIVITY_FORM, new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                if (result != null) {
                    customFields = result.getCompanyCustomFieldItems();
                    formPropertyMap = result.getFormPropertyMap();
                }
                CommonService.App.get().getCustomForms(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                        getRelationList(relationItems);
                        initForm();
                    }

                    @Override
                    public void onSuccess(ArrayList<SelectItem> result) {
                        super.onSuccess(result);
                        customForms = result;
                        getRelationList(relationItems);
                        initForm();
                    }
                });
                setEmailTemplates(relationItems);
                runCloseCommand();
            }
        });
    }

    public ActivityQuickAddForm(int sms, CrmAccountItem profileItem, Integer saleInvoiceId, RelationItem newEventRelation) {
        super(550);
        ourUiBinder.createAndBindUi(this);
        this.activityType = sms;
        relationsTable = new FlexTable();
        relationsTable.addStyleName("RelatedTo-selection__wrapper");
        if (profileItem != null) {
            this.relationItemsList = new ArrayList<>(Collections.singletonList(RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, profileItem.getObjectId(), profileItem.getName())));
        }
        if (Appointment.SMS == sms) {

            String moduleType = "_WORKFLOW_MODULE_SALE_INVOICE";
            CRMService.App.get().getSMSItem(moduleType, new AbstractAsyncCallback<SmsSendItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    initForm();
                }

                @Override
                public void success(SmsSendItem result) {
                    LoadingPanel.loading(false);
                    customForms = result.getCustomForms();
                    if (profileItem != null) {
                        addRelationTable(fromType, fromID, newEventRelation);
                        addRelationTable(fromType, fromID, RelationItem.newEventRelation(RelationItem.TYPE_CLIENT, profileItem.getObjectId(), profileItem.getName()));
                    }
                    initForm();
                    smsProvider.setItems(result.getProviders());
                    template.setItems(result.getTemplates());
                    if (result.getProviders() != null && result.getProviders().length == 1) {
                        smsProvider.setSelected(result.getProviders()[0]);
                    }
                    if (result.getTemplates() != null && result.getDefaultSmsTemplate() != null) {
                        template.setSelected(result.getDefaultSmsTemplate());
                        generateSms(template, saleInvoiceId);
                    }

                    template.addValueChangeHandler(changeEvent -> {
                        if (template.isSomethingSelected()) {
                            generateSms(template, saleInvoiceId);
                        }
                    });
                    if (profileItem.getPhone() != null) {
                        recepient.setValue(profileItem.getPhone());
                    }
                    sectionList.setSelectedByDescription(RelationItem.TYPE_SALEINVOICE);
                    invoiceSelectedInDropDown();
                }
            });

        } else if (Appointment.WHATSAPP == sms) {
            this.invoiceNumber = newEventRelation.getToName();
            this.crmAccountItem = profileItem;
            isForWhatsApp = true;
            initForm();
        }
    }


    public ActivityQuickAddForm(int activityType, String phoneNumber, ContactListItem contact, RelationItem... relationItems) {
        this(activityType, phoneNumber, contact, null, relationItems);
    }

    public ActivityQuickAddForm(int activityType, String phoneNumber, ContactListItem contact, String note, RelationItem... relationItems) {
        super(550);
        ourUiBinder.createAndBindUi(this);
        this.activityType = activityType;
        this.phoneNumber = phoneNumber;
        this.contact = contact;
        relationsTable = new FlexTable();
        relationsTable.addStyleName("RelatedTo-selection__wrapper");
        if (Appointment.SMS == activityType) {
            String moduleType = contact != null && contact.isLeadContact() ? WorkflowRule._WORKFLOW_MODULE_LEAD : contact != null && contact.isClientContact() ? WorkflowRule._WORKFLOW_MODULE_ACCOUNT : contact != null && contact.isCandidate() ? WorkflowRule._WORKFLOW_MODULE_CANDIDATE : WorkflowRule._WORKFLOW_MODULE_CONTACT;
            CRMService.App.get().getSMSItem(moduleType, new AbstractAsyncCallback<SmsSendItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    getRelationList(relationItems);
                    initForm();
                }

                @Override
                public void success(SmsSendItem result) {
                    LoadingPanel.loading(false);
                    customForms = result.getCustomForms();
                    getRelationList(relationItems);
                    initForm();
                    smsProvider.setItems(result.getProviders());
                    template.setItems(result.getTemplates());
                    if (result.getProviders() != null && result.getProviders().length == 1) {
                        smsProvider.setSelected(result.getProviders()[0]);

                    }
                }
            });
        } else {
            if (note != null) {
                description.setData(note);
            }
            if (Appointment.EVENT == activityType) {
                EmailTemplateService.App.get().getMessageCenterEmailTemplates(new ArrayList<>(Collections.singletonList(getModuleCode())), new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(SelectItem[] templates) {
                        emailTemplates.setItems(templates);
                    }

                });
            }
            CommonService.App.get().getCompanyCustomFieldsAndFormProperties(Appointment.CALL_LOG == activityType ? ViewName.LogACall : ViewName.Activity, Appointment.CALL_LOG == activityType ? LayoutRPC.LOGACALL_FORM : LayoutRPC.ACTIVITY_FORM, new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(CompanyCfAndPropertyItems result) {
                    super.success(result);
                    if (result != null) {
                        customFields = result.getCompanyCustomFieldItems();
                        formPropertyMap = result.getFormPropertyMap();
                    }
                    CommonService.App.get().getCustomForms(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            super.onFailure(caught);
                            getRelationList(relationItems);
                            initForm();
                        }

                        @Override
                        public void onSuccess(ArrayList<SelectItem> result) {
                            super.onSuccess(result);
                            customForms = result;
                            getRelationList(relationItems);
                            initForm();
                        }
                    });
                }
            });
        }
    }

    private String getModuleCode() {
        return "ET_EMPLOYEE_MODULE";
    }

    public ActivityQuickAddForm(Appointment item, RelationItem... relationItems) {
        super(550);
        ourUiBinder.createAndBindUi(this);
        this.item = item;
        this.activityType = item.getActivityType();
        relationsTable = new FlexTable();
        if (relationItems != null) {
            for (RelationItem relationItem : relationItems) {
                if (relationItem != null && relationItem.getToID() != null) {
                    if (validateRelationItem(relationItem)) {
                        addRelationTable(fromType, fromID, relationItem);
                    }
                }
            }
        }

        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(Appointment.CALL_LOG == activityType ? ViewName.LogACall : ViewName.Activity, Appointment.CALL_LOG == activityType ? LayoutRPC.LOGACALL_FORM : LayoutRPC.ACTIVITY_FORM, new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                if (result != null) {
                    customFields = result.getCompanyCustomFieldItems();
                    formPropertyMap = result.getFormPropertyMap();
                }
                CommonService.App.get().getCustomForms(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                        initForm();
                    }

                    @Override
                    public void onSuccess(ArrayList<SelectItem> result) {
                        super.onSuccess(result);
                        customForms = result;
                        initForm();
                    }
                });
            }
        });
    }

    public ActivityQuickAddForm(int activityType, ContactListItem contact, String note, RelationItem... relationItems) {
        this(activityType, contact, "", note, false, relationItems);
    }

    public ActivityQuickAddForm(int activityType, EmployeeListItem employee, RelationItem... relationItems) {
        this(activityType, null, employee.getPhoneNumber(), null, true, relationItems);
        this.employee = employee;
    }

    public ActivityQuickAddForm(int activityType, String phoneNumber, ProfileItem contact, RelationItem... relationItems) {
        this(activityType, null, phoneNumber, null, true, relationItems);
        this.employee = new EmployeeListItem();
        employee.setObjectID(contact.getObjectId());
    }

    public ActivityQuickAddForm(int activityType, ContactListItem contact, String phoneNumber, String note, boolean isEmployee, RelationItem... relationItems) {
        super(550);
        ourUiBinder.createAndBindUi(this);
        this.activityType = activityType;
        this.contact = contact;
        this.note = note;
        this.phoneNumber = phoneNumber;
        relationsTable = new FlexTable();
        relationsTable.addStyleName("RelatedTo-selection__wrapper");
        String moduleType = isEmployee ? WorkflowRule._WORKFLOW_MODULE_HRMS_EMPLOYEE : contact != null && contact.isLeadContact() ? WorkflowRule._WORKFLOW_MODULE_LEAD : contact != null && contact.isClientContact() ? WorkflowRule._WORKFLOW_MODULE_ACCOUNT : contact != null && contact.isCandidate() ? WorkflowRule._WORKFLOW_MODULE_CANDIDATE : WorkflowRule._WORKFLOW_MODULE_CONTACT;
        if (Appointment.SMS == activityType) {
            CRMService.App.get().getSMSItem(moduleType, new AbstractAsyncCallback<SmsSendItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    getRelationList(relationItems);
                    initForm();
                }

                @Override
                public void success(SmsSendItem result) {
                    LoadingPanel.loading(false);
                    customForms = result.getCustomForms();
                    getRelationList(relationItems);
                    initForm();
                    smsProvider.setItems(result.getProviders());
                    template.setItems(result.getTemplates());
                    if (result.getProviders() != null && result.getProviders().length == 1) {
                        smsProvider.setSelected(result.getProviders()[0]);

                    }
                }
            });
        } else if (Appointment.EVENT == activityType) {
            EmailTemplateService.App.get().getMessageCenterEmailTemplates(new ArrayList<>(Collections.singletonList(getModuleCode())), new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {

                }

                @Override
                public void onSuccess(SelectItem[] templates) {
                    getRelationList(relationItems);
                    initForm();
                    emailTemplates.setItems(templates);
                }

            });
        }

    }


    private void getRelationList(RelationItem... relationItems) {
        if (relationItems != null && relationItems.length > 0) {
            relationPhone = relationItems[0];
            for (RelationItem relationItem : relationItems) {
                if (relationItem != null && relationItem.getToID() != null) {
                    if (validateRelationItem(relationItem)) {
                        addRelationTable(fromType, fromID, relationItem);
                    }
                    getRelationByType(relationItem);
                }
            }
        }
    }

    private void initForm() {
        setStyleName(getElement(), "quick-add file--AcrivityQuickAddForm", true);
        Heading header = new Heading(HeadingSize.H1);
        header.setText(this.activityType == Appointment.CALL_LOG ? Property.get(Constants.LOGACALL, wfmStrings.logCall()) : this.activityType == Appointment.INTERVIEW ? wfmStrings.interview() : this.activityType == Appointment.SMS ? wfmStrings.sms() : Property.get(Constants.EVENT_LIST, wfmStrings.createEvent(), wfmStrings.event()));
        addHeader(header);

        if (Appointment.SMS == this.activityType) {
            templatePanel.setVisible(false);
            subjectPanel.setVisible(false);
            whenParentPanel.setVisible(false);
            descriptionPanel.setVisible(false);
            shareParentPanel.setVisible(false);
            guestParentPanel.setVisible(false);
            reminderParentPanel.setVisible(false);
            cloneParentPanel.setVisible(false);
            providerTempParentPanel.setVisible(true);
            recepientPanel.setVisible(true);
            contentParentPanel.setVisible(true);

            smsProvider = new DataListBox();
            smsProvider.ensureDebugId("sms_provider");

            template = new DataListBox();
            template.ensureDebugId("template");
            template.addValueChangeHandler(changeEvent -> {
                if (template.isSomethingSelected()) {
                    LoadingPanel.loading(true);
                    CRMService.App.get().generateSMSTemplate(template.getSelectedId(), contact, employee, new AbstractAsyncCallback<String>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(String textContent) {
                            LoadingPanel.loading(false);
                            content.setText(textContent != null ? textContent : "");
                        }
                    });
                }
            });

            GRow row = new GRow();
            row.add(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.provider(), smsProvider)));
            row.add(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.template(), template)));
            providerTempPanel.add(row);

            counterInt = 0;
            textAreaPanel = new VerticalPanel();
            textAreaPanel.addStyleName("textAreaPanel");
            counter = new HorizontalPanel();
            textPanel = new HorizontalPanel();
            counter.setSpacing(0);
            counter.setBorderWidth(0);
            content = new KpiTextArea();
            content.setHeight("200px");
            if (note != null) {
                content.setText(note);
            }

            addListener();
            counterLabel = new Label();
            textAreaPanel.add(counter);
            textAreaPanel.add(content);
            textPanel.add(textAreaPanel);
            textAreaPanel.setCellHorizontalAlignment(content, HasHorizontalAlignment.ALIGN_LEFT);
            textAreaPanel.setCellHorizontalAlignment(counter, HasHorizontalAlignment.ALIGN_RIGHT);
            counter.setStyleName("counterGreen");
            counter.add(counterLabel);
            counter.setCellVerticalAlignment(counterLabel, HasVerticalAlignment.ALIGN_MIDDLE);
            counter.setCellHorizontalAlignment(counterLabel, HasHorizontalAlignment.ALIGN_RIGHT);
            counterLabel.setStyleName("counterLabel");

            recepientLabel.setText(wfmStrings.recipient());
            Validation.addPhoneNumberKeyboardListener(recepient);
            if (contact != null && contact.getPrimaryPhone() != null || phoneNumber != null) {
                String phone = phoneNumber != null && !phoneNumber.equals("") ? phoneNumber : contact != null && contact.getPrimaryPhone() != null ? contact.getPrimaryPhone() : null;
                if (phone != null) {
                    phone = phone.replaceAll("[^\\d]", "");
                }
                recepient.setText(phone);
            }

            contentLabel.setText(wfmStrings.content());
            contentPanel.add(textPanel);
        } else if (Appointment.WHATSAPP == this.activityType) {
            templatePanel.setVisible(false);
            subjectPanel.setVisible(false);
            whenParentPanel.setVisible(false);
            descriptionPanel.setVisible(false);
            shareParentPanel.setVisible(false);
            guestParentPanel.setVisible(false);
            reminderParentPanel.setVisible(false);
            cloneParentPanel.setVisible(false);
            providerTempParentPanel.setVisible(false);
            recepientPanel.setVisible(false);
            contentParentPanel.setVisible(true);
            callRelatedPanel.setVisible(false);
            callRelatedLabel.setVisible(false);


            textAreaPanel = new VerticalPanel();
            textAreaPanel.addStyleName("textAreaPanel");
            textPanel = new HorizontalPanel();
            content = new KpiTextArea();
            content.setHeight("200px");
            if (note != null) {
                content.setText(note);
            }
            String type = crmAccountItem.getSaleType();
            content.setText("Please find your " + type + " " + invoiceNumber + "\n\n\nDownload " + type + " as PDF:\n" + crmAccountItem.getShortLink());

            addListener();
            textAreaPanel.add(content);
            textPanel.add(textAreaPanel);
            textAreaPanel.setCellHorizontalAlignment(content, HasHorizontalAlignment.ALIGN_LEFT);

            contentLabel.setText(wfmStrings.content());
            contentPanel.add(textPanel);
        }
        subject.ensureDebugId(this.debug_id + "subject");

        sectionList = new DataListBox();
        callRelatedLabel.setText(wfmStrings.relatedTo());
        sectionList.setItems(getRelational());

        sectionList.setChangeEvent(() -> {
            if (sectionList.isSomethingSelected()) {
                if (lookUp != null) {
                    lookUp.removeFromParent();
                    lookUp = null;
                }
                if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_TASK)) {
                    removeTaskRelatedWidgets();
                    taskSelectedInDropDown();
                } else if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_SALEINVOICE)) {
                    removeTaskRelatedWidgets();
                    invoiceSelectedInDropDown();
                } else if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_ISSUE)) {
                    removeTaskRelatedWidgets();
                    issueSelectedInDropDown();
                } else if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_EVENT)) {
                    eventSelectedInDropDown();
                } else {
                    removeTaskRelatedWidgets();
                    if (lookUp == null) {
                        if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_REQUEST_FOR_QUOTE)) {
                            lookUp = new RequestForQuoteLookUp();
                        } else if (sectionList.getSelectedItem().getDescription().contains("_FORM")) {
                            lookUp = new CRMLookUp(true, sectionList.getSelectedItem().getDescription());
                        } else {
                            lookUp = new CRMLookUp(sectionList.getSelectedItem().getDescription());
                        }

                        lookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                            if (suggestionSelectionEvent.getSelectedItem() != null) {
                                SelectItem selected = lookUp.getSelectedItem();
                                selected.setDescription(sectionList.getSelectedItem().getDescription().contains("_FORM") ? sectionList.getSelectedItem().getName() : sectionList.getSelectedItem().getDescription());
                                compareToAdd(selected);
                                if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_REQUEST_FOR_QUOTE) && selected.getEntityId() != null) {
                                    compareToAdd(new SelectItem(selected.getEntityId(), selected.getCode(), RelationItem.TYPE_CLIENT));
                                }
                            }
                        });
                    }
                    GRow row = new GRow();
                    row.add(new GColumn(GColumnEnum.COL_6, sectionList));
                    row.add(new GColumn(GColumnEnum.COL_6, lookUp));
                    callRelatedPanel.clear();
                    callRelatedPanel.add(row);
                }
            }
        });
        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_6, sectionList));
        TextBox text = new TextBox();
        text.setEnabled(false);
        row.add(new GColumn(GColumnEnum.COL_6, text));
        callRelatedPanel.add(row);

        //when
        startDate = new DatePicker();
        startDate.setDate(new Date());
        startDate.ensureDebugId(this.debug_id + "startDate");
        startDate.addChangeHandler(changeEvent -> endDate.setDate(startDate.getDate()));
        endDate = new DatePicker();
        endDate.setDate(new Date());
        endDate.ensureDebugId(this.debug_id + "endDate");
        from = new KpiTimePicker(true);
        from.setMarginTop(0);
        from.setValue(KpiTimePicker.getHoursAndMinutes(new Date()));
        from.setPaddingLeft(8);
        from.setWidth("25%");
        from.setStyleName("timepicker input-group-content");
        from.ensureDebugId(this.debug_id + "from");
        from.setChangeCommand(() -> {
            if (from.getValue() != null) {
                int[] fromtime = from.getValue();
                int hour = fromtime[0];
                int minutes = fromtime[1];
                to.setValue(getToTime(hour, minutes));
            }
        });
        to = new KpiTimePicker(true);
        to.setMarginTop(0);
        int hour = new Date().getHours();
        int minutes = new Date().getMinutes();
        to.setValue(getToTime(hour, minutes));
        to.setWidth("25%");
        to.setPaddingLeft(8);
        to.setStyleName("timepicker input-group-content");
        to.ensureDebugId(this.debug_id + "to");
        allDay = new KpiSwitcher();
        allDay.setTooltip(wfmStrings.allDay());
        allDay.addValueChangeHandler(event -> fillWhenPanel(event.getValue()));
        allDay.setValue(false, true);
        allDay.ensureDebugId(this.debug_id + "allDay");
        if (this.activityType != Appointment.CALL_LOG) {
            fillWhenPanel(false);
        }

        assignee = new MultiSelectEmployeeLookUp();
        assignee.getFilterParametrs().setHRMS(true);
        assignee.setCustomPanel(panel);
        sharePanel.add(assignee);
        assignee.selectCurrentUser();
        assignee.ensureDebugId(this.debug_id + "share");
        description.setSimpleMode(true);
        description.ensureDebugId("description");

        emailTemplatesLabel.setText(wfmStrings.template());

        emailTemplates.addValueChangeHandler(changeEvent -> {
            if (!emailTemplates.isSomethingSelected()) {
                setDefaultForSubjectAndDescription();
                return;
            }
            LoadingPanel.loading(true);
            if ("ET_EVENT_MODULE".equals(moduleType)) {
                setCorrectHourToDate();
                contact.setEventStartDate(startDate.getDate());
                contact.setEventEndDate(endDate.getDate());
                contact.setCandidateZoomLink(zoomLink);
                CRMService.App.get().generateCandidateEventTemplate(emailTemplates.getSelectedId(), contact, new AbstractAsyncCallback<Appointment>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(Appointment item) {
                        LoadingPanel.loading(false);
                        zoomLink = item.getZoomLink();
                        if (item.getZoomObjectId() != null) {
                            zoomObjectId = item.getZoomObjectId();
                        }
                        subject.setText(item.getTemplateSubject());
                        description.setData(item.getTemplateValue());
                    }
                });
            } else {
                CRMService.App.get().generateEmployeeEventTemplate(emailTemplates.getSelectedId(), employee, new AbstractAsyncCallback<LinkedHashMap<String, String>>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(LinkedHashMap<String, String> subjectWithContent) {
                        LoadingPanel.loading(false);
                        for (String key : subjectWithContent.keySet()) {
                            subject.setText(key);
                            description.setData(subjectWithContent.get(key));
                        }
                    }
                });
            }
        });

        //Guest:
        guestTable = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getGuestWidget(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        }, false);
        guestTable.ensureDebugId(this.debug_id + "guestTable");
        guestPanel.add(guestTable);
        guestLabel.setText(wfmStrings.guests());

        reminders = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getReminderWidgets(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });
        reminders.ensureDebugId(this.debug_id + "reminders_");
        reminderPanel.add(reminders);
        reminderLabel.setText(wfmStrings.reminders());


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null) {
            subjectLabel.setText(formPropertyMap.get(CustomFormConstants.SUBJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.SUBJECT).getTitle() : wfmStrings.subject());
            subject.setEnabled(!formPropertyMap.get(CustomFormConstants.SUBJECT).isDisabled());
        } else {
            subjectLabel.setText(wfmStrings.subject());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WHEN) != null) {
            whenLabel.setText(formPropertyMap.get(CustomFormConstants.WHEN).isChanged() ? formPropertyMap.get(CustomFormConstants.WHEN).getTitle() : wfmStrings.timeAndDate());
        } else {
            whenLabel.setText(wfmStrings.timeAndDate());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CALL_TYPE) != null) {
            callTypeLabel.setText(formPropertyMap.get(CustomFormConstants.CALL_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CALL_TYPE).getTitle() : wfmStrings.callType());
        } else {
            callTypeLabel.setText(wfmStrings.callType());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            descriptionLabel.setText(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description());
            description.setEnabled(!formPropertyMap.get(CustomFormConstants.DESCRIPTION).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue() != null) {
                description.setData(formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue());
            }
        } else {
            descriptionLabel.setText(wfmStrings.description());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SHARED_WITH) != null) {
            shareLabel.setText(formPropertyMap.get(CustomFormConstants.SHARED_WITH).isChanged() ? formPropertyMap.get(CustomFormConstants.SHARED_WITH).getTitle() : wfmStrings.share());
            assignee.setEnabled(!formPropertyMap.get(CustomFormConstants.SHARED_WITH).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.SHARED_WITH).getDefaultValue() != null) {
                assignee.setSelectedItems(new SelectItem(formPropertyMap.get(CustomFormConstants.SHARED_WITH).getSelectedId(), formPropertyMap.get(CustomFormConstants.SHARED_WITH).getDefaultValue()));
            }
        } else {
            shareLabel.setText(wfmStrings.share());
        }


        if (formPropertyMap != null && formPropertyMap.get("CLONE") != null) {
            cloneLabel.setText(formPropertyMap.get("CLONE").isChanged() ? formPropertyMap.get("CLONE").getTitle() : wfmStrings.clonE());
        } else {
            cloneLabel.setText(wfmStrings.clonE());
        }

        clone = new KpiSwitcher();
        clone.addValueChangeHandler(booleanValueChangeEvent -> {
            cloneWidget.setVisible(booleanValueChangeEvent.getValue());
        });
        cloneWidget = new MaterialPanel();
        cloneWidget.getElement().getStyle().setPadding(0, Style.Unit.PX);
        cloneWidget.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
        cloneWidget.setVisible(false);
        initCloneTimeWidgets();

        clonePanel.add(clone);
        clonePanel.add(cloneWidget);


        if (item != null && item.getSubject() != null) {
            subject.setText(item.getSubject());
        } else {
            String sbj = "";
            if (relationItemsList.size() > 0) {
                sbj = relationItemsList.get(0).getToName();
            }
            if (this.activityType == Appointment.CALL_LOG) {
                if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null && formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue().length() > 0) {
                    subject.setText(formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue());
                } else {
                    subject.setText((wfmStrings.call() + (!Utils.isNullOrEmpty(sbj) ? " - " + sbj : "")));
                }
            } else if (this.activityType == Appointment.INTERVIEW) {
                subject.setText((wfmStrings.interviewWith() + (!Utils.isNullOrEmpty(sbj) ? " - " + sbj : "")));
            } else {
                if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null && formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue() != null) {
                    subject.setText(formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue());
                } else {
                    subject.setText(sbj);
                }
            }
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            String finalSubjectValue = subject.getText() + " - " + phoneNumber;
            subject.setText(finalSubjectValue);
        }
        durationMin = new TextBox();
        durationMin.setPlaceHolder(wfmStrings.minutes());
        validateMinute(durationMin);

        durationSec = new TextBox();
        durationSec.setText("0");
        durationSec.setPlaceHolder(wfmStrings.seconds());
        validateMinute(durationSec);


        saveBtn = new WfmButton2();
        saveBtn.setStyleName(WfmButton2.BTN_PRIMARY);
        saveBtn.ensureDebugId("saveButton");

        if (Utils.hasPermission("CRM_SEND_EMAIL")) {
            saveBtn.setText(wfmStrings.saveAndSend());
        } else {
            saveBtn.setText(wfmStrings.save());
        }

        if (isForWhatsApp) {
            saveBtn.setText(wfmStrings.send());
            saveBtn.addClickHandler(event -> {
                String encodedText = URL.encode(content.getText());
                enableButtons(false);
                if (crmAccountItem.getPhone() != null) {
                    Window.open("https://wa.me/" + crmAccountItem.getPhone().replaceAll("\\s+", "") + "?text=" + encodedText, "_blank", "");
                } else {
                    Window.open("https://wa.me/?text=" + encodedText, "_blank", "");
                }
                close();
            });
        } else {
            saveBtn.addClickHandler(event -> {
                enableButtons(false);
                save(false);
            });
        }

//        startCalling = new WfmButton2(wfmStrings.startCalling(), WfmButton2.BTN_PRIMARY);

        startCalling = new SplitButton(150, Constants.BTN_PRIMARY);
        List<SplitButtonItem> callButtons = new ArrayList<>();

        //Add option to cal via Asterisk
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ASTERISK) && Utils.getAsteriskSettings() != null && !Utils.getAsteriskSettings().isEmpty()) {
            for (AsteriskSettings asteriskSettings : Utils.getAsteriskSettings()) {
                //@TODO will call only from one number we must split
                GWT.log("111111111111111: " + asteriskSettings.getAsteriskUsername());
                SplitButtonItem asteriskCallButton = new SplitButtonItem("asterisk" + asteriskSettings.getAsteriskUsername(),
                        wfmStrings.call() + ": " + asteriskSettings.getAsteriskNumber(),
                        () -> callViaProvider(asteriskSettings.getAsteriskUsername(), WorkforceEntryPoint.asteriskCallHandler));
                callButtons.add(asteriskCallButton);
            }

        }

        //Add option to cal via Twilio
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_TWILIO) && Utils.getTwilioNumbers() != null) {
            for (SelectItem twilioNumber : Utils.getTwilioNumbers()) {
                SplitButtonItem twilioCallButton = new SplitButtonItem("twilio", wfmStrings.call() + ": " + twilioNumber.getName(), () -> callViaProvider(twilioNumber.getName(), new TwilioCallHandler(twilioNumber)));
                callButtons.add(twilioCallButton);
            }
        }
        //Enable SAVE option if none of ASTERISK OR TWILIO enabled
        if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ASTERISK) && !Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_TWILIO)) {
            SplitButtonItem saveOption = new SplitButtonItem("save", wfmStrings.save(), () -> {
                enableButtons(false);
                save(false);
            });
            callButtons.add(saveOption);
        }
        //Choose other APP option
        SplitButtonItem telItem = new SplitButtonItem("other", wfmStrings.chooseOtherAPP(), () -> Window.open("tel:" + phoneNumber, "_self", ""));
        callButtons.add(telItem);

        //Choose other WhasappAPP option
        SplitButtonItem whatsAppOption = new SplitButtonItem("whatsapp", wfmStrings.contactUsingWhatsapp(), () -> Window.open("https://wa.me/" + phoneNumber + "?text=Hi " + contact.getFirstName(), "_blank", ""));
        callButtons.add(whatsAppOption);

        // Choose telegram
        SplitButtonItem telegramOption = new SplitButtonItem("telegram", wfmStrings.contactUsingTelegram(), () -> Window.open("tg://resolve?phone=" + phoneNumber + (contact != null ? "&text=Hi " + contact.getFirstName() : ""), "_blank", ""));
        callButtons.add(telegramOption);

        SplitButtonItem sipuni = new SplitButtonItem("sipuni", "Contact using Sipuni", () -> {
            String number = phoneNumber.replaceAll(" ", "");
            Window.open("tel:" + number, "_self", "");
            close();
        });
        callButtons.add(sipuni);

        SplitButtonItem composeEmail = new SplitButtonItem("composeMail", "Compose email", () -> save(true));
        callButtons.add(composeEmail);


        startCalling.addItemList(callButtons);

        sendSms = new WfmButton2(wfmStrings.send(), WfmButton2.BTN_PRIMARY);

        sendSms.addClickHandler(event -> sendSms());
        sendSms.ensureDebugId("SmsButton");

        advancedOptions = createAdvancedOptions();
        initCallCustomFields();
        panel.add(advancedOptions);

        addBody(panel);

        if (this.activityType == Appointment.CALL_LOG) {
            guestParentPanel.setVisible(false);
            initCallRelationPanel();
            initCallTypePanel();
        } else if (this.activityType == Appointment.SMS) {
            addFooter(sendSms);
        } else {
            addFooter(saveBtn);
        }
        show();
    }

    private void setDefaultForSubjectAndDescription() {
        description.setData(formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue());
        if (item != null && item.getSubject() != null) {
            subject.setText(item.getSubject());
            return;
        }
        String sbj = "";
        if (relationItemsList.size() > 0) {
            sbj = relationItemsList.get(0).getToName();
        }
        if (this.activityType == Appointment.CALL_LOG) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null && formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue().length() > 0) {
                subject.setText(formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue());
            } else {
                subject.setText((wfmStrings.call() + (!Utils.isNullOrEmpty(sbj) ? " - " + sbj : "")));
            }
        } else if (this.activityType == Appointment.INTERVIEW) {
            subject.setText((wfmStrings.interviewWith() + (!Utils.isNullOrEmpty(sbj) ? " - " + sbj : "")));
        } else if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null && formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue() != null) {
            subject.setText(formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue());
        } else {
            subject.setText(sbj);
        }
    }

    private void initCallCustomFields() {

        if (customFields != null && customFields.size() > 0) {
            advancedOptions.createAndAppendEventAndCallCustomFieldsView(Appointment.CALL_LOG == activityType ? ViewAddFiledsCodeName.LogACallAdd : ViewAddFiledsCodeName.ActivityAdd, customFields);
            customFieldsView = advancedOptions.getCustomFieldsView();
        }
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {
            @Override
            public List<Widget> getOptionWidgets() {
                List<Widget> result = new ArrayList<>();
                return result;
            }
        }, false);
    }

    private void callViaProvider(String username, CallCommand callCommand) {
        if (!validateStartCalling()) {
            return;
        }
        remove();
        ContactDetailsItem contactDetailsItem = new ContactDetailsItem();
        if (phoneNumber != null && phoneNumber.length() > 0) {
            if (contact != null) {
                contactDetailsItem.setId(contact.getObjectId());
                contactDetailsItem.setContactType(contact.getContactType());
                contactDetailsItem.setName(contact.getName());
                contactDetailsItem.setMobile(contact.getPrimaryPhone());
                contactDetailsItem.setOwnerId(contact.getOwnerId());
                contactDetailsItem.setOwner(contact.getOwner());
                contactDetailsItem.setPhoneNumber(contact.getPrimaryPhone());
                contactDetailsItem.setPrimaryEmail(contact.getPrimaryEmail());
                contactDetailsItem.setStatus(contact.getLeadStatus() != null ? contact.getLeadStatus().getName() : null);
                contactDetailsItem.setCompany(contact.getCrmAccount() != null ? contact.getCrmAccount().getName() : null);
                if (contact.getOpportunity() != null) {
                    OpportunityItemForTwilio opportunity = new OpportunityItemForTwilio();
                    opportunity.setName(contact.getOpportunity().getOpportunityName());
                    opportunity.setAssignee(contact.getOpportunity().getAssignee());
                    opportunity.setAmount(contact.getOpportunity().getAmount().toString());
                    opportunity.setCurrency(contact.getOpportunity().getCurrency());
                    opportunity.setStage(contact.getOpportunity().getStage() != null ? contact.getOpportunity().getStage().getName() : null);
                    contactDetailsItem.setOpportunity(opportunity);
                }
                if (contact.getContactType().equals(TYPE_ACCOUNT)) {
                    contactDetailsItem.setAccountIndustry(contact.getAccountIndustry());
                    if (contact.getCrmAccount() != null) {
                        contactDetailsItem.setAccountId(contact.getCrmAccount().getObjectId());
                        contactDetailsItem.setPrimaryEmail(contact.getCrmAccount().getEmail());
                        contactDetailsItem.setAccountIndustry(contact.getCrmAccount().getIndustry());
                    }
                }
                if (contact.getEmployee() != null) {
                    EmployeeForTwilio employee = new EmployeeForTwilio();
                    employee.setEmail(contact.getPrimaryEmail());
                    employee.setPosition(contact.getEmployee().getPosition());
                    employee.setDepartment(contact.getEmployee().getDepartment());
                    employee.setSupervisor(contact.getEmployee().getSupervisorItem().getName());
                    contactDetailsItem.setEmployee(employee);
                    contactDetailsItem.setOwnerId(contact.getEmployee().getObjectID());
                    contactDetailsItem.setOwner(contact.getEmployee().getFullName());
                }
                if (contact.getVacancies() != null && contact.getVacancies().size() > 0) {
                    contactDetailsItem.setVacancy(contact.getVacancies().get(0).getName());
                }
                CommonService.App.get().getOtherContactTypes(phoneNumber, new AsyncCallback<ArrayList<ContactTypeForTwilio>>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(ArrayList<ContactTypeForTwilio> contactTypeForTwilios) {
                        int relationListSize = relationItemsList.size();
                        GWT.log(relationItemsList.get(0).getToType() + "," + relationItemsList.get(1).getToType() + ":" + relationListSize);
                        for (ContactTypeForTwilio contactType : contactTypeForTwilios) {
                            for (int i = 0; i < relationListSize; i++) {
                                if (!contactType.getId().equals(relationItemsList.get(i).getToID())) {
                                    RelationItem newItem = new RelationItem();
                                    newItem.setToID(contactType.getId());
                                    /*if (contactType.getContactType() == 5) {
                                        newItem.setToType(LinkTypeEnum.CRM_ACCOUNT.name());
                                    }*/
                                    newItem.setToType(RelationItem.getByContactType(contactType.getContactType()));
                                    newItem.setFromType(RelationItem.TYPE_EVENT);
                                    newItem.setToName(contactType.getName());
                                    relationItemsList.add(newItem);
                                    /*if (contactType.getAccount()!= null) {
                                        RelationItem accountRelation = new RelationItem();
                                        accountRelation.setFromType(RelationItem.TYPE_EVENT);
                                        accountRelation.setToID(contactType.getAccount().getId());
                                        accountRelation.setToType(LinkTypeEnum.CRM_ACCOUNT.name());
                                        accountRelation.setToName(contactType.getAccount().getName());
                                        relationItemsList.add(accountRelation);
                                    }*/
                                }
                            }
                        }
                        ArrayList<RelationItem> list = relationItemsList;
                        contactDetailsItem.setOtherFields(contactTypeForTwilios);
                    }
                });
            }

            contactDetailsItem.setRelations(relationItemsList);
            contactDetailsItem.setTaskCommand(this::addTaskCommand);
            contactDetailsItem.setCallCommand(this::addPhoneCommand);
            contactDetailsItem.setEventCommand(this::addEventCommand);
            contactDetailsItem.setSmsCommand(this::addSmsCommand);

            callCommand.call(username, phoneNumber, contactDetailsItem);
        } else {
            ContactService.App.get().getContact(firstPhoneRelationItem.getToID(), false, new AsyncCallback<ContactListItem>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(ContactListItem contactListItem) {
                    phoneNumber = contactListItem.getPrimaryPhone();
                    contactDetailsItem.setId(contactListItem.getObjectId());
                    contactDetailsItem.setContactType(contactListItem.getContactType());
                    contactDetailsItem.setName(contactListItem.getName());
                    contactDetailsItem.setMobile(contactListItem.getPrimaryPhone());
                    contactDetailsItem.setOwnerId(contactListItem.getOwnerId());
                    contactDetailsItem.setOwner(contactListItem.getOwner());
                    contactDetailsItem.setPhoneNumber(contactListItem.getPrimaryPhone());
                    contactDetailsItem.setPrimaryEmail(contactListItem.getPrimaryEmail());
                    contactDetailsItem.setStatus(contactListItem.getLeadStatus() != null ? contactListItem.getLeadStatus().getName() : null);
                    contactDetailsItem.setCompany(contactListItem.getCrmAccount() != null ? contactListItem.getCrmAccount().getName() : null);
                    if (contactListItem.getOpportunity() != null) {
                        OpportunityItemForTwilio opportunity = new OpportunityItemForTwilio();
                        opportunity.setName(contactListItem.getOpportunity().getOpportunityName());
                        opportunity.setAssignee(contactListItem.getOpportunity().getAssignee());
                        opportunity.setAmount(contactListItem.getOpportunity().getAmount().toString());
                        opportunity.setCurrency(contactListItem.getOpportunity().getCurrency());
                        opportunity.setStage(contactListItem.getOpportunity().getStage() != null ? contactListItem.getOpportunity().getStage().getName() : null);
                        contactDetailsItem.setOpportunity(opportunity);
                    }
                    if (contactListItem.getContactType().equals(TYPE_ACCOUNT)) {
                        contactDetailsItem.setAccountIndustry(contactListItem.getAccountIndustry());
                    }
                    if (contactListItem.getEmployee() != null) {
                        EmployeeForTwilio employee = new EmployeeForTwilio();
                        employee.setEmail(contactListItem.getPrimaryEmail());
                        employee.setPosition(contactListItem.getEmployee().getPosition());
                        employee.setDepartment(contactListItem.getEmployee().getDepartment());
                        employee.setSupervisor(contactListItem.getEmployee().getSupervisorItem().getName());
                        contactDetailsItem.setEmployee(employee);
                        contactDetailsItem.setOwnerId(contactListItem.getEmployee().getObjectID());
                        contactDetailsItem.setOwner(contactListItem.getEmployee().getFullName());
                    }
                    if (contactListItem.getVacancies() != null && contactListItem.getVacancies().size() > 0) {
                        contactDetailsItem.setVacancy(contactListItem.getVacancies().get(0).getName());
                    }
                    CommonService.App.get().getOtherContactTypes(phoneNumber, new AsyncCallback<ArrayList<ContactTypeForTwilio>>() {
                        @Override
                        public void onFailure(Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(ArrayList<ContactTypeForTwilio> contactTypeForTwilios) {
                            int relationListSize = relationItemsList.size();
                            for (ContactTypeForTwilio contactType : contactTypeForTwilios) {
                                for (int i = 0; i < relationListSize; i++) {
                                    if (!contactType.getId().equals(relationItemsList.get(i).getToID())) {
                                        RelationItem newItem = new RelationItem();
                                        newItem.setToID(contactType.getId());
                                        newItem.setToType(RelationItem.getByContactType(contactType.getContactType()));
                                        newItem.setFromType(RelationItem.TYPE_EVENT);
                                        newItem.setToName(contactType.getName());
                                        relationItemsList.add(newItem);
                                    }
                                }
                            }
                            contactDetailsItem.setOtherFields(contactTypeForTwilios);

                        }
                    });
                    contactDetailsItem.setRelations(relationItemsList);
                    contactDetailsItem.setTaskCommand(ActivityQuickAddForm.this::addTaskCommand);
                    contactDetailsItem.setCallCommand(ActivityQuickAddForm.this::addPhoneCommand);
                    contactDetailsItem.setEventCommand(ActivityQuickAddForm.this::addEventCommand);
                    contactDetailsItem.setSmsCommand(ActivityQuickAddForm.this::addSmsCommand);

                    callCommand.call(username, phoneNumber, contactDetailsItem);
                }
            });
        }

    }

    private void addTaskCommand(String note) {
        new TaskQuickAddView(note, relationPhone);
    }

    private void addPhoneCommand(String note) {
        this.note = note;
        if (contact == null) {
            contact = new ContactListItem();
        }
        contact.setCallModal(true);
        new ActivityQuickAddForm(Appointment.CALL_LOG, phoneNumber, contact, note, relationPhone);
    }

    private void addEventCommand(String note) {
        this.note = note;
        new ActivityQuickAddForm(Appointment.EVENT, phoneNumber, contact, note, relationPhone);
    }

    private void addSmsCommand(String note) {
        if (contact == null) {
            contact = new ContactListItem();
        }
        new ActivityQuickAddForm(Appointment.SMS, contact, phoneNumber, note, false, relationPhone);
    }

    private void sendSms() {
        if (!validateSms()) {
            return;
        }
        sendSms.setEnabled(false);
        SmsSendItem smsSendItem = new SmsSendItem();
        smsSendItem.setToNumber(Utils.cleanPhoneNumber(recepient.getText()));
        smsSendItem.setMessageText(content.getText());
        smsSendItem.setSettingID(smsProvider.getSelectedId());
        smsSendItem.setEntityID(contact != null ? contact.getObjectId() : null);
        smsSendItem.setUserName(contact != null ? contact.getName() : null);
        smsSendItem.setRelations(relationItemsList);
        smsSendItem.setHrms(Utils.isHRMS());
        LoadingPanel.loading(true);
        CRMService.App.get().smsSendTo(smsSendItem, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                sendSms.setEnabled(true);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Boolean s) {
                LoadingPanel.loading(false);
                if (s != null && s) {
                    Info.show(wfmStrings.messageHasBeenSent(), Info.Type.INFO);
                    remove();
                } else {
                    sendSms.setEnabled(true);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }
            }
        });
    }

    private void addListener() {
        if (content != null) {
            content.addKeyUpHandler(keyUpEvent -> changeCounter());
            content.addKeyDownHandler(keyUpEvent -> changeCounter());
            content.addKeyPressHandler(changeEvent -> changeCounter());
            content.addChangeHandler(changeEvent -> changeCounter());
            content.addBlurHandler(changeEvent -> changeCounter());
        }
    }

    private void changeCounter() {
        int lengthOfTextAreaText = content.getText().length();
        if (lengthOfTextAreaText <= 160) {
            counterInt = 160 - lengthOfTextAreaText;
            counterLabel.setText(counterInt >= 0 ? counterInt + " (1) " : String.valueOf(0));
        } else {
            counterInt = (lengthOfTextAreaText - 160) % 145 == 0 ? 0 : 145 - (lengthOfTextAreaText - 160) % 145;

            int smsCount = ((lengthOfTextAreaText - 160) / 145) > 0 && ((lengthOfTextAreaText - 160) % 145) == 0 ? ((lengthOfTextAreaText - 160) / 145) + 1
                    : ((lengthOfTextAreaText - 160) / 145) > 0 && ((lengthOfTextAreaText - 160) % 145) > 0 ? ((lengthOfTextAreaText - 160) / 145) + 2 : 2;
            counterLabel.setText(counterInt >= 0 ? counterInt + " (" + smsCount + ") " : String.valueOf(0));
        }
    }

    private void initCloneTimeWidgets() {
        startDateClone = new DatePicker();
        startDateClone.setDate(new Date());
        startDateClone.ensureDebugId(this.debug_id + "startDateClone");
        startDateClone.addChangeHandler(changeEvent -> {
            if (startDateClone.getDate().after(endDateClone.getDate())) {
                endDateClone.setDate(startDateClone.getDate());
            }
        });
        endDateClone = new DatePicker();
        endDateClone.setDate(new Date());
        endDateClone.ensureDebugId(this.debug_id + "endDateClone");
        fromClone = new KpiTimePicker(true);
        fromClone.setMarginTop(0);
        fromClone.setValue(KpiTimePicker.getHoursAndMinutes(new Date()));
        fromClone.setPaddingLeft(8);
        fromClone.setWidth("25%");
        fromClone.setStyleName("timepicker input-group-content");
        fromClone.ensureDebugId(this.debug_id + "fromClone");
        fromClone.setChangeCommand(() -> {
            if (fromClone.getValue() != null) {
                int[] fromtime = fromClone.getValue();
                int hour = fromtime[0];
                int minutes = fromtime[1];
                toClone.setValue(getToTime(hour, minutes));
            }
        });
        toClone = new KpiTimePicker(true);
        toClone.setMarginTop(0);
        int hour = new Date().getHours();
        int minutes = new Date().getMinutes();
        toClone.setValue(getToTime(hour, minutes));
        toClone.setWidth("25%");
        toClone.setPaddingLeft(8);
        toClone.setStyleName("timepicker input-group-content");
        toClone.ensureDebugId(this.debug_id + "toClone");
        allDayClone = new KpiSwitcher();
        allDayClone.setTooltip(wfmStrings.allDay());
        allDayClone.addValueChangeHandler(event -> drawClonePanel(event.getValue()));
        allDayClone.setValue(false, true);
        allDayClone.ensureDebugId(this.debug_id + "allDayClone");
        drawClonePanel(false);
    }

    private int[] getToTime(int hour, int minutes) {
        int[] result = new int[2];
        if (minutes < 30) {
            minutes += 30;
        } else {
            minutes = 30 - (60 - minutes);
            hour++;
        }
        result[0] = hour;
        result[1] = minutes;
        return result;
    }

    private void enableButtons(boolean enable) {
        saveBtn.setEnabled(enable);
    }

    private void closeSideNav() {
        remove();
        Info.show(Property.get(Constants.EVENT_LIST, wfmStrings.messSuccessfullyAdded(), wfmStrings.event()));
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CALENDAR_EVENT_ADD, null, ActivityQuickAddForm.this);
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, null, ActivityQuickAddForm.this);
        ShortcutItem shortcutItem = MainLayout.get().getCurrentContainer().getItemsByView().get(Constants.EVENT_LIST);
        if (shortcutItem != null && shortcutItem.getStatisticCommand() != null) {
            shortcutItem.getStatisticCommand().execute();
        }
    }


    private void initCallTypePanel() {
        callTypeParentPanel.setVisible(true);
        inbound = new KpiRadioButton("callType", wfmStrings.inbound());
        inbound.ensureDebugId(this.debug_id + "inbound");
        outbound = new KpiRadioButton("callType", wfmStrings.outbound());
        outbound.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        outbound.ensureDebugId(this.debug_id + "outbound");
        outbound.setValue(true);

        missed = new KpiRadioButton("callType", wfmStrings.missed());
        missed.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        missed.ensureDebugId(this.debug_id + "missed");
        if (contact != null && contact.isCallModal()) {
            scheduleCall.setValue(true);
            scheduleCall.setEnabled(false);
            currentCall.setEnabled(false);
            complatedCall.setEnabled(false);
        }

        if (currentCall.getValue()) {
            inbound.setEnabled(false);
            outbound.setEnabled(false);
            missed.setEnabled(false);
            outbound.setValue(true);
            whenPanel.setVisible(false);
            whenLabel.setVisible(false);
            callTypePanel.add(inbound);
            callTypePanel.add(outbound);
            callTypePanel.add(missed);
            reminderPanel.setVisible(false);
            reminderLabel.setVisible(false);
            getContentFooter().clear();
            addFooter(startCalling);

        }

        if (scheduleCall.getValue()) {
            inbound.setEnabled(false);
            missed.setEnabled(false);
            outbound.setEnabled(false);
            outbound.setValue(true);
            callTypePanel.clear();
            callTypePanel.add(inbound);
            callTypePanel.add(outbound);
            callTypePanel.add(missed);
            whenPanel.setVisible(true);
            whenLabel.setVisible(false);
            reminderPanel.setVisible(true);
            reminderLabel.setVisible(true);
            whenPanel.clear();
            whenPanel.add(new GRow(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.timeAndDate(), new InputGroup(startDate, from)))));
            getContentFooter().clear();
            addFooter(saveBtn);
        }

        currentCall.addValueChangeHandler(valueChangeEvent -> {
            if (currentCall.getValue()) {
                inbound.setEnabled(false);
                outbound.setEnabled(false);
                missed.setEnabled(false);
                outbound.setValue(true);
                whenPanel.setVisible(false);
                whenLabel.setVisible(false);
                callTypePanel.clear();
                callTypePanel.add(inbound);
                callTypePanel.add(outbound);
                callTypePanel.add(missed);
                reminderPanel.setVisible(false);
                reminderLabel.setVisible(false);
                getContentFooter().clear();
                addFooter(startCalling);
            }
        });

        complatedCall.addValueChangeHandler(valueChangeEvent -> {
            if (complatedCall.getValue()) {

                inbound.setEnabled(true);
                outbound.setEnabled(true);
                missed.setEnabled(true);
                callTypePanel.clear();
                callTypePanel.add(inbound);
                callTypePanel.add(outbound);
                callTypePanel.add(missed);
                reminderPanel.setVisible(false);
                reminderLabel.setVisible(false);
                whenPanel.setVisible(true);
                whenLabel.setVisible(false);
                whenPanel.clear();
                whenPanel.add(new GRow(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.timeAndDate(), new InputGroup(startDate, from))), new GColumn(GColumnEnum.COL_3, new FormGroup(wfmStrings.duration(), durationMin)), new GColumn(GColumnEnum.COL_3, new FormGroup(durationSec))));
                getContentFooter().clear();
                addFooter(saveBtn);
            }
        });

        scheduleCall.addValueChangeHandler(valueChangeEvent -> {
            if (scheduleCall.getValue()) {
                inbound.setEnabled(false);
                missed.setEnabled(false);
                outbound.setEnabled(false);
                outbound.setValue(true);
                callTypePanel.clear();
                callTypePanel.add(inbound);
                callTypePanel.add(outbound);
                callTypePanel.add(missed);
                whenPanel.setVisible(true);
                whenLabel.setVisible(false);
                reminderPanel.setVisible(true);
                reminderLabel.setVisible(true);
                whenPanel.clear();
                whenPanel.add(new GRow(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.timeAndDate(), new InputGroup(startDate, from)))));
                getContentFooter().clear();
                addFooter(saveBtn);
            }
        });

    }

    private void initCallRelationPanel() {
        callDetailParentPanel.setVisible(true);
        callDetailLabel.setText(wfmStrings.callDetails());


        currentCall = new KpiRadioButton("callDetails", wfmStrings.current());
        currentCall.ensureDebugId(this.debug_id + "currentCall");
        currentCall.setValue(true);

        complatedCall = new KpiRadioButton("callDetails", wfmStrings.completed());
        complatedCall.ensureDebugId(this.debug_id + "completedCall");

        scheduleCall = new KpiRadioButton("callDetails", wfmStrings.schedule());
        scheduleCall.ensureDebugId(this.debug_id + "scheduleCall");

        GRow callRow = new GRow();
        callRow.add(new GColumn(GColumnEnum.COL_4, currentCall));
        callRow.add(new GColumn(GColumnEnum.COL_4, complatedCall));
        callRow.add(new GColumn(GColumnEnum.COL_4, scheduleCall));
        callDetailPanel.add(callRow);

    }

    private SelectItem[] getRelational() {
        if (relationals != null && relationals.size() > 0) {
            return relationals.toArray(new SelectItem[]{});
        }
        relationals = new ArrayList<>();
        int count = 0;

        relationals.add(new SelectItem(++count, Property.get(Constants.LEADS, wfmStrings.lead()), RelationItem.TYPE_LEAD));
        relationals.add(new SelectItem(++count, Property.get(Constants.Contacts, wfmStrings.contact()), RelationItem.TYPE_CONTACT));
        relationals.add(new SelectItem(++count, wfmStrings.crmAccount(), RelationItem.TYPE_CRM_ACCOUNT));

        if (Utils.isHRMS() && Utils.hasPermission(PermissionConstants.HRMS_RECRUITMENT)) {
            relationals.add(new SelectItem(++count, wfmStrings.candidate(), RelationItem.TYPE_CANDIDATE));
        }
        relationals.add(new SelectItem(++count, wfmStrings.employee(), RelationItem.TYPE_EMPLOYEE));
        relationals.add(new SelectItem(++count, wfmStrings.campaigns(), RelationItem.TYPE_CAMPAIGN));
        relationals.add(new SelectItem(++count, Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), RelationItem.TYPE_CASE));
        relationals.add(new SelectItem(++count, wfmStrings.customer(), RelationItem.TYPE_CLIENT));
        if (Utils.isTrainingCenterEnabled()) {
            relationals.add(new SelectItem(++count, wfmStrings.course(), RelationItem.TYPE_COURCE_SCHEDULE));
        }

        relationals.add(new SelectItem(++count, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), RelationItem.TYPE_DEPARTMENT));
        relationals.add(new SelectItem(++count, Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.events()), RelationItem.TYPE_EVENT));
        relationals.add(new SelectItem(++count, Property.get(Constants.ISSUE, wfmStrings.issue()), RelationItem.TYPE_ISSUE));
        relationals.add(new SelectItem(++count, Property.get(Constants.Opportunities, wfmStrings.opportunity()), RelationItem.TYPE_OPPORTUNITY));
        relationals.add(new SelectItem(++count, wfmStrings.product(), RelationItem.TYPE_PRODUCT));
        relationals.add(new SelectItem(++count, Property.get(Constants.PROJECT, wfmStrings.project()), RelationItem.TYPE_PROJECT));
        relationals.add(new SelectItem(++count, Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()), RelationItem.TYPE_PURCHASE_ORDER));
        relationals.add(new SelectItem(++count, Property.get(Constants.REQUEST_FOR_QUOTE, wfmStrings.requestForQuote()), RelationItem.TYPE_REQUEST_FOR_QUOTE));
        relationals.add(new SelectItem(++count, Property.get(Constants.SALE_ORDER_CODE, wfmStrings.saleorder()), RelationItem.TYPE_SALEORDER));
        relationals.add(new SelectItem(++count, Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()), RelationItem.TYPE_SALEQUOTE));
        relationals.add(new SelectItem(++count, Property.get(Constants.SALE_INVOICE, wfmStrings.salesInvoice()), RelationItem.TYPE_SALEINVOICE));
        if (Utils.isTrainingCenterEnabled()) {
            relationals.add(new SelectItem(++count, wfmStrings.student(), RelationItem.TYPE_STUDENT));
        }
        relationals.add(new SelectItem(++count, Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), RelationItem.TYPE_SUPPLIER));
        relationals.add(new SelectItem(++count, Property.get(Constants.TASK, wfmStrings.task()), RelationItem.TYPE_TASK));


        if (customForms != null && customForms.size() > 0) {
            for (SelectItem customForm : customForms) {
                relationals.add(new SelectItem(++count, customForm.getName(), customForm.getCode()));
            }
        }
        return relationals.toArray(new SelectItem[]{});
    }

    private void fillWhenPanel(boolean isAllDay) {
        whenPanel.clear();
        Div toHTML = InputGroup.wrapIntoGroupContent(InputGroup.wrapIntoGroupText(new HTML(wfmStrings.to())));
        toHTML.addStyleName("width-auto");
        if (isAllDay) {
            whenPanel.add(new GRow(new GColumn(GColumnEnum.COL_5, startDate), new GColumn(GColumnEnum.COL_5, endDate), new GColumn(GColumnEnum.COL_2, allDay)));
        } else {
            whenPanel.add(new GRow(new GColumn(GColumnEnum.COL_5, new InputGroup(startDate, from)), new GColumn(GColumnEnum.COL_5, new InputGroup(endDate, to)), new GColumn(GColumnEnum.COL_2, allDay)));
        }
    }

    private void drawClonePanel(boolean isAllDay) {
        cloneWidget.clear();
        if (this.activityType != Appointment.CALL_LOG) {
            if (isAllDay) {
                cloneWidget.add(new GRow(new GColumn(GColumnEnum.COL_5, startDateClone),
                        new GColumn(GColumnEnum.COL_5, endDateClone),
                        new GColumn(GColumnEnum.COL_2, allDayClone)));
            } else {
                cloneWidget.add(new GRow(new GColumn(GColumnEnum.COL_5, new InputGroup(startDateClone, fromClone)),
                        new GColumn(GColumnEnum.COL_5, new InputGroup(endDateClone, toClone)),
                        new GColumn(GColumnEnum.COL_2, allDayClone)));
            }
        } else {
            cloneWidget.add(new GRow(new GColumn(GColumnEnum.COL_6, new InputGroup(startDateClone, fromClone))));
        }
    }

    private WidgetsMap getGuestWidget(SelectItem id) {
        WidgetsMap widgetsMap = new WidgetsMap();
        ContactLookUp relationTextBox = new ContactLookUp(Constants.BY_BOTH);
        if (id != null) {
            relationTextBox.setSelected(id);
        }
        widgetsMap.addToCenter(MultiTableNewUI.LOOK_UP_BOX, relationTextBox);
        return widgetsMap;
    }

    private WidgetsMap getReminderWidgets(CalendarEventReminder data) {
        WidgetsMap widgetsMap = new WidgetsMap();
        if (data == null) {
            data = new CalendarEventReminder();
        }
        Reminder reminder = new Reminder();
        reminder.setReminderData(data);
        widgetsMap.addToCenter("reminder", reminder);
        return widgetsMap;
    }

    public boolean validate() {
        int errors = 0;
        boolean dateValid = true;
        boolean complatedCallValid = true;
        boolean scheduleCallValid = true;
        assignee.removeStyleName(Constants.ERROR_FORM_STYLE);
        startDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        endDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        from.removeStyleName(Constants.ERROR_FORM_STYLE);
        to.removeStyleName(Constants.ERROR_FORM_STYLE);
        durationMin.removeStyleName(Constants.ERROR_FORM_STYLE);
        durationSec.removeStyleName(Constants.ERROR_FORM_STYLE);
        to.removeStyleName(Constants.ERROR_FORM_STYLE);
        sectionList.removeStyleName(Constants.ERROR_FORM_STYLE);
        subject.removeStyleName(Constants.ERROR_FORM_STYLE);

        if (this.activityType != Appointment.CALL_LOG) {
            if (allDay.getValue()) {
                if (endDate.getDate().before(startDate.getDate()) && !DateUtils.areOnTheSameDay(startDate.getDate(), endDate.getDate())) {
                    startDate.addStyleName(Constants.ERROR_FORM_STYLE);
                    endDate.addStyleName(Constants.ERROR_FORM_STYLE);
                    dateValid = false;
                }
            } else {
                if (DateUtils.areOnTheSameDay(startDate.getDate(), endDate.getDate())) {
                    Integer fromHour = from.getValue()[0];
                    Integer fromMinute = from.getValue()[1];
                    Integer toHour = to.getValue()[0];
                    Integer toMinute = to.getValue()[1];
                    if ((fromHour.equals(toHour) && fromMinute >= toMinute) || fromHour > toHour) {
                        from.addStyleName(Constants.ERROR_FORM_STYLE);
                        to.addStyleName(Constants.ERROR_FORM_STYLE);
                        dateValid = false;
                    }
                } else if (endDate.getDate().before(startDate.getDate())) {
                    startDate.addStyleName(Constants.ERROR_FORM_STYLE);
                    endDate.addStyleName(Constants.ERROR_FORM_STYLE);
                    dateValid = false;
                }
            }
        } else {
            if (scheduleCall != null && scheduleCall.getValue()) {
                boolean scheduleCallRelationValid = false;
                Date start = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(),
                        from.getValue()[0],
                        from.getValue()[1], 0);
                if (start.before(new Date())) {
                    startDate.addStyleName(Constants.ERROR_FORM_STYLE);
                    from.addStyleName(Constants.ERROR_FORM_STYLE);
                    scheduleCallValid = false;
                }

                for (RelationItem relationItem : relationItemsList) {
                    if (relationItem != null && (RelationItem.TYPE_LEAD.equals(relationItem.getToType()) || RelationItem.TYPE_CONTACT.equals(relationItem.getToType()))) {
                        scheduleCallRelationValid = true;
                        break;
                    }
                }
                if (!scheduleCallRelationValid) {
                    sectionList.addStyleName(Constants.ERROR_FORM_STYLE);
                    Info.warn(wfmStrings.scheduleCallRelationValid());
                    return false;
                }
            }
            if (complatedCall != null && complatedCall.getValue()) {

                Integer hour = from.getValue()[0];
                Integer min = from.getValue()[1];

                if (durationMin.getText() != null && durationMin.getText().trim().length() > 0) {
                    min += Integer.parseInt(durationMin.getText());
                }

                Date start = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(),
                        from.getValue()[0],
                        from.getValue()[1], 0);
                if (start.after(new Date())) {
                    startDate.addStyleName(Constants.ERROR_FORM_STYLE);
                    from.addStyleName(Constants.ERROR_FORM_STYLE);
                    complatedCallValid = false;
                }
                if (!Validation.validateTextBoxRequired(durationMin)) {
                    errors++;
                }
            }
        }
        if (guestTable.getWidgets() != null && guestTable.getWidgets().get(0).size() > 0) {
            ContactLookUp value = (ContactLookUp) guestTable.getWidgets().get(0).get(MultiTableNewUI.LOOK_UP_BOX);
            if (value.getSelectedItem() != null && !Validation.validateLookUpRequired(value)) {
                errors++;
            }
        }
        if (clone.getValue()) {
            startDateClone.removeStyleName(Constants.ERROR_FORM_STYLE);
            endDateClone.removeStyleName(Constants.ERROR_FORM_STYLE);
            fromClone.removeStyleName(Constants.ERROR_FORM_STYLE);
            toClone.removeStyleName(Constants.ERROR_FORM_STYLE);
            if (allDayClone.getValue()) {
                if (endDateClone.getDate().before(startDateClone.getDate()) && !DateUtils.areOnTheSameDay(startDateClone.getDate(), endDateClone.getDate())) {
                    startDateClone.addStyleName(Constants.ERROR_FORM_STYLE);
                    endDateClone.addStyleName(Constants.ERROR_FORM_STYLE);
                    dateValid = false;
                }
            } else {
                if (DateUtils.areOnTheSameDay(startDateClone.getDate(), endDateClone.getDate())) {
                    Integer fromHour = fromClone.getValue()[0];
                    Integer fromMinute = fromClone.getValue()[1];
                    Integer toHour = toClone.getValue()[0];
                    Integer toMinute = toClone.getValue()[1];
                    if ((fromHour.equals(toHour) && fromMinute >= toMinute) || fromHour > toHour) {
                        fromClone.addStyleName(Constants.ERROR_FORM_STYLE);
                        toClone.addStyleName(Constants.ERROR_FORM_STYLE);
                        dateValid = false;
                    }
                } else if (endDateClone.getDate().before(startDateClone.getDate())) {
                    startDateClone.addStyleName(Constants.ERROR_FORM_STYLE);
                    endDateClone.addStyleName(Constants.ERROR_FORM_STYLE);
                    dateValid = false;
                }
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null) {
            if (formPropertyMap.get(CustomFormConstants.SUBJECT).isRequired()) {
                if (!Validation.validateTextBoxRequired(subject)) {
                    errors++;
                }
            }
        } else {
            if (!Validation.validateTextBoxRequired(subject)) {
                errors++;
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()) {
            if (!Validation.validateHTMLTextAreaRequired(description)) {
                errors++;
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SHARED_WITH) != null) {
            if (formPropertyMap.get(CustomFormConstants.SHARED_WITH).isRequired()) {
                if (assignee.getSelectedItems() == null || assignee.getSelectedItems().isEmpty()) {
                    assignee.addStyleName(Constants.ERROR_FORM_STYLE);
                    errors++;
                }
            }
        } else {
            if (assignee.getSelectedItems() == null || assignee.getSelectedItems().isEmpty()) {
                assignee.addStyleName(Constants.ERROR_FORM_STYLE);
                errors++;
            }
        }


        if (customFieldsView != null && !customFieldsView.validateRequiredFields()) {
            advancedOptions.getCustomFieldContainer().setActive(0);
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        } else if (!dateValid) {
            Info.warn(wfmStrings.pleaseChooseValidDate());
            return false;
        } else if (!complatedCallValid) {
            Info.warn(wfmStrings.callStartTimeShould());
            return false;
        } else if (!scheduleCallValid) {
            Info.warn(wfmStrings.scheduleCallValid());
            return false;
        }
        return true;
    }

    public boolean validateStartCalling() {
        int errors = 0;
        boolean callValid = false;
        boolean firstPhoneRelation = true;
        firstPhoneRelationItem = null;
        sectionList.removeStyleName(Constants.ERROR_FORM_STYLE);
        subject.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (!Validation.validateTextBoxRequired(subject)) {
            errors++;
        }

        if (customFieldsView != null) {
            for (int i = 0; i < customFieldsView.getData().size(); i++) {
                if (customFieldsView.getData().get(i).isRequired() && (customFieldsView.getData().get(i).getFieldStringValue() == null || customFieldsView.getData().get(i).getFieldStringValue().equals(""))) {
                    errors++;
                }
            }
        }

        for (RelationItem relationItem : relationItemsList) {
            if (relationItem != null && (RelationItem.TYPE_LEAD.equals(relationItem.getToType()) || RelationItem.TYPE_CONTACT.equals(relationItem.getToType())
                    || RelationItem.TYPE_CRM_ACCOUNT.equals(relationItem.getToType()) || RelationItem.TYPE_CANDIDATE.equals(relationItem.getToType()) || RelationItem.TYPE_EMPLOYEE.equals(relationItem.getToType()))) {
                callValid = true;
                if (firstPhoneRelation) {
                    firstPhoneRelationItem = relationItem;
                    firstPhoneRelation = false;
                }
            }
        }
        if (!callValid) {
            sectionList.addStyleName(Constants.ERROR_FORM_STYLE);
        }

        if (errors > 0 || !callValid) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }

    public boolean validateSms() {
        int errors = 0;
        smsProvider.removeStyleName(Constants.ERROR_FORM_STYLE);
        recepient.removeStyleName(Constants.ERROR_FORM_STYLE);
        content.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (!Validation.validateListBoxRequired(smsProvider)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(recepient)) {
            errors++;
        }
        if (!Validation.validateTextAreaRequired(content)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            recepient.addStyleName(Constants.ERROR_FORM_STYLE);
            return false;
        } else if (!recepient.getText().matches(Constants.REGEX_PHONE)) {
            recepient.addStyleName(Constants.ERROR_FORM_STYLE);
            Info.show(wfmStrings.incorrectlyPHoneFormat(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void setValues() {
        Date start = startDate.getDate();
        Date end = endDate.getDate();

        if (item == null) {
            item = new Appointment();
        }
        item.setActivityType(activityType);
        item.setSubject(subject.getText());

        if (this.activityType == Appointment.CALL_LOG) {
            item.setCurrentCall(currentCall.getValue());
            item.setComplatedCall(complatedCall.getValue());
            item.setScheduleCall(scheduleCall.getValue());
            item.setInboundCall(inbound.getValue());
            item.setOutboundCall(outbound.getValue());
            item.setMissedCall(missed.getValue());
            if (!relationItemsList.isEmpty()) {
                item.setSubject((item.isOutboundCall() ? "Call to: " : "Call from: ") + relationItemsList.get(0).getToName() + "(" + phoneNumber + ")");
            }
            start = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(),
                    from.getValue()[0],
                    from.getValue()[1], 0);
            if (complatedCall != null && complatedCall.getValue()) {
                long duration = 0;
                if (durationMin.getText().trim().length() > 0) {
                    duration = duration + Long.valueOf(durationMin.getText()) * 60;
                }
                if (durationSec.getText().trim().length() > 0) {
                    duration = duration + Long.valueOf(durationSec.getText());
                }
                item.setCallDuration(duration);


                Integer min = from.getValue()[1];
                if (durationMin.getText() != null && durationMin.getText().trim().length() > 0) {
                    min += Integer.parseInt(durationMin.getText());
                }

                Date startTime = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(),
                        from.getValue()[0],
                        min, 0);
                if (startTime.after(new Date())) {
                    start = new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate(), new Date().getHours(), new Date().getMinutes() - Integer.parseInt(durationMin.getText()));
                }
            }
        } else {
            if (!allDay.getValue()) {
                start = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(),
                        allDay.getValue() ? 0 : from.getValue()[0],
                        allDay.getValue() ? 0 : from.getValue()[1], 0);

                end = new Date(endDate.getDate().getYear(), endDate.getDate().getMonth(), endDate.getDate().getDate(),
                        allDay.getValue() ? 59 : to.getValue()[0],
                        allDay.getValue() ? 59 : to.getValue()[1], 59);
            }
        }
        for (Map<String, Widget> emailRow : guestTable.getWidgets()) {
            ContactLookUp value = (ContactLookUp) emailRow.get(MultiTableNewUI.LOOK_UP_BOX);
            if (!Utils.isNullOrEmpty(value.getText()) && !wfmStrings.email().equals(value.getText())) {
                item.getGuests().add(new SelectItem(null, value.getText()));
            }
        }
        item.setSendEmailNotification(item.getGuests() != null && item.getGuests().size() > 0);

        item.setAllDay(allDay.getValue());
        item.setStartDate(start);
        if (this.activityType != Appointment.CALL_LOG) {
            item.setEndDate(end);
        }
        item.setDescription(description.getData());
        ArrayList<Attendee> attendees = new ArrayList<>();
        assignee.getSelectedItems().forEach(attendee -> attendees.add(new Attendee(attendee.getId(), true)));
        item.setAttendees(attendees);

        if (clone.getValue()) {
            Date startClone = new Date(startDateClone.getDate().getYear(), startDateClone.getDate().getMonth(), startDateClone.getDate().getDate(),
                    allDayClone.getValue() ? 0 : fromClone.getValue()[0], allDayClone.getValue() ? 0 : fromClone.getValue()[1], 0);

            Date endClone = new Date(endDateClone.getDate().getYear(),
                    endDateClone.getDate().getMonth(),
                    endDateClone.getDate().getDate(),
                    allDayClone.getValue() ? 59 : toClone.getValue()[0],
                    allDayClone.getValue() ? 59 : toClone.getValue()[1],
                    59);

            item.setClone(true);
            item.setAllDayClone(allDayClone.getValue());
            item.setStartDateClone(startClone);
            item.setEndDateClone(endClone);
        }


        if (customFieldsView != null) {
            item.setCustomFieldItems(customFieldsView.getData());
        }
        item.setRecurrenceJobItem(null);
        relationItemsList.addAll(removedRelationItems);
        item.setRelations(relationItemsList);
        item.setRegisterNestedWorkflowEvents(false);
        item.setCreatedFrom(Utils.isHRMS() ? Appointment.FROM_HRMS : Appointment.FROM_CRM);
        ArrayList<CalendarEventReminder> eventReminders = new ArrayList<>();
        boolean isReminderData = false;
        for (HashMap<String, Widget> widgetMap : reminders.getWidgets()) {
            Reminder reminder_ = (Reminder) widgetMap.get("reminder");
            if (reminder_.getReminderData().getValue() != null && reminder_.getReminderData().getReminderTimes() != null) {
                eventReminders.add(reminder_.getReminderData());
                isReminderData = true;
            }
        }
        if (isReminderData) {
            item.setReminder(eventReminders);
        }
    }

    public void save(boolean composeEmail) {
        if (!validate()) {
            enableButtons(true);
            return;
        }
        setValues();
        if (handler != null) {
            checkForHolidayAndSave();
        } else {
            item.setAction(Appointment.ADD_NEW_EVENT);
            ArrayList<Attendee> attendeeList = new ArrayList<>();
            Attendee attendee = new Attendee();
            attendee.setID(Utils.getUserID());
            attendeeList.add(attendee);
            Date start = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(), from.getValue()[0], from.getValue()[1]);
            Date end = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(), to.getValue()[0], to.getValue()[1]);
            item.setEndDate(end);
            item.setPhoneNumber(phoneNumber);
            LoadingPanel.loading(true, panel);
            calendarService.isAssigneeOnHoliday(attendeeList, start, end, false, new AbstractAsyncCallback<String>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false, panel);
                    enableButtons(true);
                }

                public void success(String result) {
                    LoadingPanel.loading(false, panel);
                    if (!Utils.isNullOrEmpty(result)) {
                        String haveAholidayContinueAnyway = wfmStrings.youHaveHolidayOnDate();
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.YesNo, haveAholidayContinueAnyway, new com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler() {
                            @Override
                            public void onSubmit() {
                                saveCalendarEvent(composeEmail);
                            }
                        });
                        wfmMessageBox.setTitle(wfmStrings.confirmation());
                        wfmMessageBox.open();
                    } else {
                        saveCalendarEvent(composeEmail);
                    }
                }
            });
        }
    }

    private void checkForHolidayAndSave() {
        if (item.getAttendees() != null && item.getAttendees().size() > 0) {
            Date startDate = item.getStartDate();
            Date endDate = item.getEndDate();
            LoadingPanel.loading(true, panel);
            calendarService.isAssigneeOnHoliday(item.getAttendees(), startDate, endDate, false, new AbstractAsyncCallback<String>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false, panel);
                    enableButtons(true);
                }

                public void success(String result) {
                    LoadingPanel.loading(false, panel);
                    if (!Utils.isNullOrEmpty(result)) {
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.YesNo, wfmStrings.followingEmployees() + result + " " + wfmStrings.haveHolidayOnDateProceed(), new com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler() {
                            @Override
                            public void onSubmit() {
                                handler.onSaveOrUpdate(item);
                                closeSideNav();
                            }
                        });
                        wfmMessageBox.setTitle(wfmStrings.confirmation());
                        wfmMessageBox.open();
                    } else {
                        handler.onSaveOrUpdate(item);
                        closeSideNav();
                    }
                }
            });
        } else {
            handler.onSaveOrUpdate(item);
            closeSideNav();
        }
    }

    private void saveCalendarEvent(boolean composeEmail) {
        LoadingPanel.loading(true, panel);
        item.setZoomObjectId(zoomObjectId);
        calendarService.saveCalendarEvent(null, item, false, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                enableButtons(true);
            }

            public void success(SelectItem event) {
                LoadingPanel.loading(false, panel);
                if (activityType == Appointment.INTERVIEW) {
                    if (command != null) {
                        command.execute();
                    }
                }
                if (activityType == Appointment.CALL_LOG || activityType == Appointment.EVENT) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CALENDAR_EVENT_ADD, item, ActivityQuickAddForm.this);
                }
                closeSideNav();
                if (composeEmail && Utils.hasPermission("CRM_SEND_EMAIL")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + RelationItem.TYPE_EVENT + "/" + event.getId());
                }
            }
        });
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void setHandler(SaveAppointmentHandler handler) {
        this.handler = handler;
    }

    private void compareToAdd(SelectItem selected) {
        linkToPanel.setVisible(true);
        RelationItem item = new RelationItem(null, selected.getId(), selected.getDescription(), selected.getName(), fromID, fromType, fromName);
        if (validateRelationItem(item)) {
            addRelationTable(fromType, fromID, item);
            getRelationByType(item);
        }
        if (relationItemsList.contains(item)) {
            RelationItem item_ = relationItemsList.get(relationItemsList.indexOf(item));
            if (item.equals(item_) && item_.getObjectID() != null && item_.isRemove()) {
                item_.setRemove(false);
                addRelationTable(fromType, fromID, item_);
            }
        }
    }

    private void addRelationTable(String fromType, Integer fromID, final RelationItem relation) {
        String relationType = null;
        String relationToName = null;
        Integer relationId = null;
        if (fromID == null || (fromType.equals(relation.getFromType()) && fromID.equals(relation.getFromID()))) {
            relationType = relation.getToType();
            relationToName = relation.getToName();
            relationId = relation.getToID();
        } else if (fromID.equals(relation.getToID()) && fromType.equals(relation.getToType())) {
            relationType = relation.getFromType();
            relationToName = relation.getFromName();
            relationId = relation.getFromID();
        }

        HTMLPanel name = new HTMLPanel("span", getReadableRelationType(relationType));
        HTML desc = new HTML(relationToName);

        if (RelationItem.TYPE_REQUEST_FOR_QUOTE.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Accounting.html#requestforquote|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_TASK.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + finalRelationId);
                } else {
                    Utils.openURL("ProjectManagement.html#task|summary/" + finalRelationId);
                }
            });

        } else if (RelationItem.TYPE_ISSUE.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("issue|summary/" + finalRelationId);
                } else {
                    Utils.openURL("ProjectManagement.html#issue|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_EVENT.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.addClickHandler(click -> SinksContainerFactory.entryPoint.onHistoryChanged("event|summary/" + finalRelationId));
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
        } else if (RelationItem.TYPE_CONTACT.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");

            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html") || Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("contact|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#contact|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_CAMPAIGN.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("campaign|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#campaign|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_LEAD.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("lead|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#lead|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("account|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#account|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_CLIENT.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#account|summary/" + finalRelationId + "/false/Customer");
                }
            });
        } else if (RelationItem.TYPE_SUPPLIER.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                Utils.openURL("Crm.html#account|summary/" + finalRelationId + "/false/Supplier");
            });
        } else if (RelationItem.TYPE_EMPLOYEE.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("employeeProfile|employeeProfileView/" + finalRelationId);
                } else {
                    Utils.openURL("Hrms.html#employeeProfile|employeeProfileView/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_DEPARTMENT.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html") || Utils.getPathName().contains("Hrms.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("department|summary/" + finalRelationId);
                } else {
                    Utils.openURL("ProjectManagement.html#department|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_OPPORTUNITY.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#opportunity|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_CASE.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Crm.html") || Utils.getPathName().contains("ProjectManagement.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("case|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#case|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_SALEQUOTE.equals(relationType)) {
            Integer finalRelationId = relationId;
            if (Utils.isCRM() ? Utils.hasPermission(CRM_SALES_QUOTE_SUMMARY) : (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_SUMMARY))) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("salequote|summary/" + finalRelationId);
                    } else {
                        Utils.openURL("Accounting.html#salequote|summary/" + finalRelationId);
                    }
                });
            }
        } else if (RelationItem.TYPE_SALEORDER.equals(relationType)) {
            Integer finalRelationId = relationId;
            if (Utils.hasPermission(ACCOUNTING_SALES_ORDER_SUMMARY)) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("saleorder|summary/" + finalRelationId);
                    } else {
                        Utils.openURL("Accounting.html#saleorder|summary/" + finalRelationId);
                    }
                });
            }
        } else if (RelationItem.TYPE_PRODUCT.equals(relationType)) {
            Integer finalRelationId = relationId;
            if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PRODUCT_SUMMARY : ACCOUNTING_PRODUCT_SUMMARY)) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + finalRelationId);
                    } else {
                        Utils.openURL("Accounting.html#product|summary/" + finalRelationId);
                    }
                });
            }
        } else if (RelationItem.TYPE_PURCHASE_ORDER.equals(relationType)) {
            Integer finalRelationId = relationId;
            if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_ORDER_SUMMARY : ACCOUNTING_PURCHASE_ORDER_SUMMARY)) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|summary/" + finalRelationId);
                    } else {
                        Utils.openURL("Accounting.html#purchaseorder|summary/" + finalRelationId);
                    }
                });
            }

        } else if (RelationItem.TYPE_CANDIDATE.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Hrms.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("candidate|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Hrms.html#candidate|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_MEETING_MINUTES.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Hrms.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("meetingMinutes|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Hrms.html#meetingMinutes|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_EMAIL_TRACKER.equals(relationType)) {
            Integer finalRelationId = relation.getFromID();
            String finalRelationToName1 = relationToName;
            CommonService.App.get().getEmailbyTrackerid(finalRelationId, new AbstractAsyncCallback<Email>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(Email result) {
                    super.success(result);
                    if (result != null) {
                        desc.setHTML(finalRelationToName1 != null ? "<a href=\"javascript:\">" + finalRelationToName1 + "</a>" : "");
                        desc.addClickHandler(click -> {
                            if (Utils.getPathName().contains("Crm.html")) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("email|summary/" + result.getObjectID());
                            } else {
                                Utils.openURL("MessageCenter.html#email|summary/" + result.getObjectID());
                            }
                        });
                    }
                }
            });
        } else {
            String finalRelationType = relationType;
            Integer finalRelationId = relationId;
            String finalRelationToName = relationToName;
            CommonService.App.get().getCustomForms(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                }

                @Override
                public void onSuccess(ArrayList<SelectItem> result) {
                    super.onSuccess(result);
                    if (result != null && result.size() > 0) {
                        for (SelectItem item : result) {
                            if (item.getName().equals(finalRelationType)) {
                                if (Utils.hasPermission(item.getCode() + "_SUMMARY_" + Utils.getCompanyID())) {
                                    desc.setHTML(finalRelationToName != null ? "<a href=\"javascript:\">" + finalRelationToName + "</a>" : "");
                                    desc.addClickHandler(click -> {
                                        SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|summary/" + finalRelationId + "/" + item.getId() + "/" + item.getCode() + "/" + finalRelationType);
                                    });
                                }
                            }
                        }
                    }
                }
            });
        }


        DynamicTable table = new DynamicTable(getColumns(), false);
        table.setWidth("100%");
        table.setStyleName("RelatedTo-selection"); //https://prnt.sc/rq1ghy

        Icon removeIcon = new Icon();
        removeIcon.addStyleName("btn--icon");
        SvgIcon trashIcon = new SvgIcon(SvgEnum.trash2);
        removeIcon.add(trashIcon);


        removeIcon.addClickHandler(event -> {
            ArrayList<RelationItem> relationItems = new ArrayList<>();
            table.removeAllRows();
//            table.remove(selectedTable);
            relationItemsList.forEach(relationItem -> {
                if (!(relationItem.getToType().equals(relation.getToType()) && relationItem.getToID() == relation.getToID())) {
                    relationItems.add(relationItem);
                }
            });
//            relationItemsList.remove(relation);
//            relation.setRemove(true);
//            if (relation.getObjectID() != null) {   // O`chirilgan relationlarni bazadan o`chirib tashlash uchun kerak.
//                relationItemsList.add(relation);
//            }
            relationItemsList.clear();
            relationItemsList = relationItems;
            if (relationItemsList != null && relationItemsList.isEmpty()) {
                linkToPanel.setVisible(false);
            }
        });

        LinkedHashMap<String, Widget> itemWidgetsMap = new LinkedHashMap<>();
        itemWidgetsMap.put("relatedTo", name);

        itemWidgetsMap.put("item", desc);
        itemWidgetsMap.put("remove", removeIcon);

        table.addRow(1, itemWidgetsMap.values().toArray(new Widget[]{}));

        int rowCount = relationsTable.getRowCount();
        relationsTable.setWidget(rowCount, 0, table);
        linkToPanel.add(relationsTable);

    }

    protected DynamicTableColumn[] getColumns() {
        ArrayList<DynamicTableColumn> headers = new ArrayList<>();
        headers.add(new DynamicTableColumn("", "relatedTo", 150, false));
        headers.add(new DynamicTableColumn("", "item", 350, false));
        headers.add(new DynamicTableColumn("", "delete", 50, false));
        return headers.toArray(new DynamicTableColumn[]{});
    }

    private String getReadableRelationType(String relationType) {
        if (relationType != null && !"".equals(relationType)) {
            if (RelationItem.TYPE_TASK.equals(relationType)) {
                return Property.get(Constants.TASK, wfmStrings.task());
            }
            if (RelationItem.TYPE_ISSUE.equals(relationType)) {
                return Property.get(Constants.ISSUE, wfmStrings.issue());
            }
            if (RelationItem.TYPE_EVENT.equals(relationType)) {
                return Property.get(Constants.EVENT_LIST, wfmStrings.event());
            }
            if (RelationItem.TYPE_CONTACT.equals(relationType)) {
                return Property.get(Constants.Contacts, wfmStrings.contact());
            }
            if (RelationItem.TYPE_CAMPAIGN.equals(relationType)) {
                return wfmStrings.campaign();
            }
            if (RelationItem.TYPE_LEAD.equals(relationType)) {
                return Property.get(Constants.LEADS, wfmStrings.lead());
            }
            if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
                return wfmStrings.crmAccount();
            }
            if (RelationItem.TYPE_CLIENT.equals(relationType)) {
                return wfmStrings.customer();
            }
            if (RelationItem.TYPE_SUPPLIER.equals(relationType)) {
                return Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier());
            }
            if (RelationItem.TYPE_EMPLOYEE.equals(relationType)) {
                return wfmStrings.employee();
            }
            if (RelationItem.TYPE_DEPARTMENT.equals(relationType)) {
                return Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
            }
            if (RelationItem.TYPE_OPPORTUNITY.equals(relationType)) {
                return Property.get(Constants.Opportunities, wfmStrings.opportunity());
            }
            if (RelationItem.TYPE_CASE.equals(relationType)) {
                return Property.get(Constants.CASE_LIST, wfmStrings.crmCase());
            }
            if (RelationItem.TYPE_EMAIL_TRACKER.equals(relationType)) {
                return wfmStrings.email();
            }
            if (RelationItem.TYPE_BOOKING.equals(relationType)) {
                return wfmStrings.booking();
            }
            if (RelationItem.TYPE_SALEQUOTE.equals(relationType)) {
                return wfmStrings.saleorder();
            }
            if (RelationItem.TYPE_SALEORDER.equals(relationType)) {
                return wfmStrings.saleorder();
            }
            if (RelationItem.TYPE_PRODUCT.equals(relationType)) {
                return wfmStrings.product();
            }
            if (RelationItem.TYPE_CANDIDATE.equals(relationType)) {
                return wfmStrings.candidate();
            }
            if (RelationItem.TYPE_COURCE_SCHEDULE.equals(relationType)) {
                return wfmStrings.course();
            }
            if (RelationItem.TYPE_EMAIL_FILTER.equals(relationType)) {
                return wfmStrings.emailFilters();
            }
            if (RelationItem.TYPE_PURCHASE_ORDER.equals(relationType)) {
                return wfmStrings.purchaseorder();
            }
            if (RelationItem.TYPE_REQUEST_FOR_QUOTE.equals(relationType)) {
                return wfmStrings.requestForQuote();
            }
            if (RelationItem.TYPE_STUDENT.equals(relationType)) {
                return wfmStrings.student();
            }
        }
        return relationType;
    }

    private void removeTaskRelatedWidgets() {
        projectLookUp = null;
        taskLookUp = null;
        issueLookUp = null;
        eventLookUp = null;
        eventDateUp = null;
        invoiceLookUp = null;
        sectionList = null;
    }

    private void taskSelectedInDropDown() {
        callRelatedLabel.setVisible(false);
        if (projectLookUp == null) {
            projectLookUp = new CRMLookUp(RelationItem.TYPE_PROJECT);
        }
        if (taskLookUp == null) {
            taskLookUp = new CRMLookUp(RelationItem.TYPE_TASK);
        }
        taskLookUp.setEnabled(false);

        projectLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                taskLookUp.clearAndClearItems();
                taskLookUp.refreshOracle(true);
                if (projectLookUp.getSelectedItem() != null) {
                    taskLookUp.setProjectID(projectLookUp.getSelectedItem().getId());
                    taskLookUp.setEnabled(true);
                }
            } else {
                taskLookUp.setProjectID(null);
                taskLookUp.setEnabled(false);
            }
        });

        taskLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                SelectItem selected = taskLookUp.getSelectedItem();
                selected.setDescription(RelationItem.TYPE_TASK);
                compareToAdd(selected);
            }
        });

        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(wfmStrings.relatedTo(), sectionList)));
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp)));
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(Property.get(Constants.TASK, wfmStrings.task()), taskLookUp)));
        callRelatedPanel.clear();
        callRelatedPanel.add(row);
    }

    private void invoiceSelectedInDropDown() {
        callRelatedLabel.setVisible(false);
        if (invoiceLookUp == null) {
            invoiceLookUp = new InvoiceQuoteLookUp(RelationItem.TYPE_SALEINVOICE);
        }
        invoiceLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
            if (selectionEvent.getSelectedItem() != null) {
                SelectItem selected = invoiceLookUp.getSelectedItem();
                selected.setDescription(RelationItem.TYPE_SALESINVOICE);
                getRelationsToSaleInvoice(selected);
            }
        });

        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.sections(), sectionList)));
        row.add(new GColumn(GColumnEnum.COL_6, new FormGroup(Property.get(Constants.SALE_INVOICE, wfmStrings.salesInvoice()), invoiceLookUp)));
        callRelatedPanel.clear();
        callRelatedPanel.add(row);

    }

    private void issueSelectedInDropDown() {
        callRelatedLabel.setVisible(false);

        if (projectLookUp == null) {
            projectLookUp = new CRMLookUp(RelationItem.TYPE_PROJECT);
        }
        if (issueLookUp == null) {
            issueLookUp = new CRMLookUp(RelationItem.TYPE_ISSUE);
        }
        issueLookUp.setEnabled(false);

        projectLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                issueLookUp.clearAndClearItems();
                issueLookUp.refreshOracle(true);
                if (projectLookUp.getSelectedItem() != null) {
                    issueLookUp.setProjectID(projectLookUp.getSelectedItem().getId());
                    issueLookUp.setEnabled(true);
                }
            } else {
                issueLookUp.setProjectID(null);
                issueLookUp.setEnabled(false);
            }
        });

        issueLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                SelectItem selected = issueLookUp.getSelectedItem();
                selected.setDescription(RelationItem.TYPE_ISSUE);
                compareToAdd(selected);
            }
        });

        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(wfmStrings.relatedTo(), sectionList)));
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp)));
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(Property.get(Constants.ISSUE, wfmStrings.issue()), issueLookUp)));
        callRelatedPanel.clear();
        callRelatedPanel.add(row);
    }

    private void eventSelectedInDropDown() {
        callRelatedLabel.setVisible(false);

        if (eventDateUp == null) {
            eventDateUp = new DatePicker();
            eventDateUp.setDate(null);
        }

        if (eventLookUp == null) {
            eventLookUp = new CRMLookUp(RelationItem.TYPE_EVENT);
        }
        eventLookUp.setEnabled(false);

        eventDateUp.addChangeHandler(event -> {
            if (eventDateUp.getDate() != null) {
                eventLookUp.clearAndClearItems();
                eventLookUp.refreshOracle(true);
                eventLookUp.setDate(eventDateUp.getDate());
                eventLookUp.setEnabled(true);
            } else {
                eventLookUp.setDate(null);
                eventLookUp.setEnabled(false);
            }
        });

        eventLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                SelectItem selected = eventLookUp.getSelectedItem();
                selected.setDescription(RelationItem.TYPE_EVENT);
                compareToAdd(selected);
            }
        });

        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(wfmStrings.relatedTo(), sectionList)));
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(wfmStrings.date(), eventDateUp)));
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.events()), eventLookUp)));
        callRelatedPanel.clear();
        callRelatedPanel.add(row);
    }

    private void getRelationsToSaleInvoice(SelectItem selected) {
        linkToPanel.setVisible(true);
        RelationItem item = new RelationItem(null, selected.getId(), selected.getDescription(), selected.getName(), fromID, fromType, fromName);

        if (validateRelationItem(item)) {
            addRelationTable(fromType, fromID, item);
        }
        if (relationItemsList.contains(item)) {
            RelationItem item_ = relationItemsList.get(relationItemsList.indexOf(item));
            if (item.equals(item_) && item_.getObjectID() != null && item_.isRemove()) {
                item_.setRemove(false);
                addRelationTable(fromType, fromID, item_);
            }
        }
    }

    private void validateMinute(TextBox textBox) {
        textBox.addKeyPressHandler(event -> {
            char key = event.getCharCode();
            if (Utils.isArabicLanguage()) {
                return;
            }

            if (key == (char) 0) {
                return;
            }

            if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE
                    && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_BACKSPACE
                    && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT
                    && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END
                    && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN
                    && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB) {
                ((TextBox) event.getSource()).cancelKey();
            }
            if (textBox.getText() != null && textBox.getText().indexOf('.') != -1 && key == '.') {
                ((TextBox) event.getSource()).cancelKey();
            }
            if (textBox.getText() != null && key == '\'') {
                ((TextBox) event.getSource()).cancelKey();
            }
            if (key == '.') {
                ((TextBox) event.getSource()).cancelKey();
            }

            if (Character.isDigit(key)) {
                boolean isTrue = Integer.valueOf(textBox.getValue() + key).compareTo(59) <= 0;
                if (!isTrue) {
                    ((TextBox) event.getSource()).cancelKey();
                }
            }
        });
    }

    private boolean validateRelationItem(RelationItem relationItem) {
        boolean haveRelationItem = false;

        for (RelationItem item : relationItemsList) {

            if (item.getToType().equals(relationItem.getToType()) && item.getToID().equals(relationItem.getToID())) {
                haveRelationItem = true;
                break;
            }
        }
        if (!haveRelationItem) {
            relationItemsList.add(relationItem);
            return true;
        }
        return false;
    }

    private void getRelationByType(RelationItem relationItem) {

        if (relationItem.getToType() != null && relationItem.getToID() != null) {
            AllInOneService.App.get().getAdditionalRelations(relationItem.getToID(), relationItem.getToType(), relationItem.getToName(), fromID, fromType, fromName, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                }

                @Override
                public void onSuccess(ArrayList<RelationItem> result) {
                    if (result != null && result.size() > 1) {
                        result.remove(0);
                        for (RelationItem item_ : result) {
                            if (validateRelationItem(item_)) {
                                addRelationTable(fromType, fromID, item_);
                            }
                        }
                    }
                }
            });
        }
    }

    private void setEmailTemplates(RelationItem... relationItems) {
        EmailTemplateService.App.get().getMessageCenterEmailTemplates(hasCandidateRelation ? new ArrayList<>(Arrays.asList(ET_EVENT_MODULE, ET_CONTACT_MODULE))
                : new ArrayList<>(Collections.singletonList(ET_EVENT_MODULE)), new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] templates) {
                emailTemplates.setItems(templates);
                moduleType = "ET_EVENT_MODULE";
                if (relationItems != null && relationItems.length > 0) {
                    RelationItem relationItem = relationItems[0];
                    if (relationItem != null && relationItem.getToID() != null) {
                        contact = new ContactListItem();
                        contact.setObjectId(relationItem.getToID());
                    }
                }
            }
        });
    }

    private void setCorrectHourToDate() {
        startDate.getDate().setHours(from.getValue()[0]);
        startDate.getDate().setMinutes(from.getValue()[1]);
        endDate.getDate().setHours(to.getValue()[0]);
        endDate.getDate().setMinutes(to.getValue()[1]);
    }

    private void runCloseCommand() {
        setCloseCommand(() -> {
            if (zoomObjectId != null) {
                CommonService.App.get().deleteZoomCall(zoomObjectId, zoomLink, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(Void result) {

                    }
                });
            }
        });
    }

    private void generateSms(DataListBox templateFrom, Integer saleInvoiceIdFrom) {
        CRMService.App.get().generateSMSTemplateForSalesInvoice(templateFrom.getSelectedId(), saleInvoiceIdFrom, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(String textContent) {
                LoadingPanel.loading(false);
                content.setText(textContent != null ? textContent : "");
            }
        });

    }
}
