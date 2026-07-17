package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCServiceAsync;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 11/08/12
 * Time: 16:08
 * To change this template use File | Settings | File Templates.
 */
public class CourseBookingSummaryView extends CustomForm2 implements TCConstants, Colapse, Constants {

    protected static TCServiceAsync tcService = TCService.App.get();
    protected static TCStrings tcStrings = TCStrings.App.get();

    private final Integer objectID;
    private Integer invoiceID;
    private KpiDataGrid<StudentItem> dataGrid;
    private ListDataProvider<StudentItem> dataProvider;

    private String masterCardPaymentURL = null;
    private WfmButton2 btnSubmitForApproval;
    private WfmButton2 btnApprove;
    private WfmButton2 btnReject;
    private WfmButton2 btnPayOnline;
    private WfmButton2 btnPdfGenerator;

    private String statusCode = null;

    private HTML number, companyName, companyNumber, phone, mobile, fax, companyEmail, contactName, contactPosition, contactRefInd, contactPhone, contactEmail, location, status, type, managerEmail;
    private Label invoiceNumber;
    private Image reGenerateInvoice;
    private HorizontalPanel pnlIvoice;
    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public static final ProvidesKey<StudentItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectId();

    public CourseBookingSummaryView(Integer objectID) {
        super(TC_VIEW_COURSE_BOOKING, tcStrings.courseBookingSummary());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.CourseBooking,getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                CourseBookingSummaryView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        initialize();
    }

    @Override
    protected void initPredefinedValues() {

    }

    public void initialize() {
        number = new HTML();
        invoiceNumber = new Label();
        invoiceNumber.setStyleName("uploadLinkStyle2");
        reGenerateInvoice = new Image("/mainStyles/images/refresh.png");
        reGenerateInvoice.setTitle("Regenerate Course Booking Invoice");
        reGenerateInvoice.setStyleName("regenerate-invoice");
        reGenerateInvoice.setVisible(false);

        pnlIvoice = new HorizontalPanel();
        pnlIvoice.setSpacing(5);
        pnlIvoice.add(invoiceNumber);
        pnlIvoice.add(reGenerateInvoice);

        companyName = new HTML();
        companyName = new HTML();
        companyNumber = new HTML();
        phone = new HTML();
        mobile = new HTML();
        fax = new HTML();
        companyEmail = new HTML();
        contactName = new HTML();
        contactEmail = new HTML();
        contactPhone = new HTML();
        contactPosition = new HTML();
        contactRefInd = new HTML();
        location = new HTML();
        status = new HTML();
        type = new HTML();
        managerEmail = new HTML();

        invoiceNumber.addClickHandler(event -> {
            if (invoiceID != null) {
                String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + SALE_INVOICE + "|summary/" + invoiceID;
                Window.open(addSalesInvoice, "_blank", "");
            }
        });
        reGenerateInvoice.addClickHandler(event -> {
            LoadingPanel.loading(true);
            InvoiceService.App.get().createInvoiceFromCourseBooking(objectID, APPROVE, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    GWT.log(caught.getMessage());
                    Info.show("ReGenerate course booking invoice is failed.", Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Integer result) {
                    LoadingPanel.loading(false);
                    Info.show("Course booking invoice is successfully regenerated.", Info.Type.INFO);
                }
            });
        });

        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
//        dataGrid.setWidth("100%");
//        dataGrid.setHeight("400px");
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage("<center>" + tcStrings.thereAreNoStudentsYet() + "</center>", "", null));
        dataGrid.getEmptyTableWidget().getElement().getStyle().setMarginTop(-50, com.google.gwt.dom.client.Style.Unit.PX);
        dataProvider = new ListDataProvider<>();
        dataProvider.addDataDisplay(dataGrid);
        initTableColumns();

        addTitleField(COURSE_BOOKING.CUSTOMER_DETAILS, tcStrings.courseBookingDetails());
        //1.1
        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME) != null) {
            addField(COURSE_BOOKING.COMPANY_NAME, companyName, getTitle(formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).isChanged() ? formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).getTitle() : wfmStrings.companyName(), formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).isInformation());
            if (formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).isInformation()) {
                new KpiToolTip(companyName, formPropertyMap.get(COURSE_BOOKING.COMPANY_NAME).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.COMPANY_NAME, companyName, getTitle(wfmStrings.name(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER) != null) {
            addField(COURSE_BOOKING.COMPANY_NUMBER, companyNumber, getTitle(formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).isChanged() ? formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).getTitle() : wfmStrings.companyNumber(), formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).isInformation());
            if (formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).isInformation()) {
                new KpiToolTip(companyNumber, formPropertyMap.get(COURSE_BOOKING.COMPANY_NUMBER).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.COMPANY_NUMBER, companyNumber, getTitle(wfmStrings.companyNumber(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER) != null) {
            addField(COURSE_BOOKING.PHONE_NUMBER, phone, getTitle(formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).isChanged() ? formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).getTitle() : wfmStrings.phone(), formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).isInformation());
            if (formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).isInformation()) {
                new KpiToolTip(phone, formPropertyMap.get(COURSE_BOOKING.PHONE_NUMBER).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.PHONE_NUMBER, phone, getTitle(wfmStrings.phone(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER) != null) {
            addField(COURSE_BOOKING.FAX_NUMBER, fax, getTitle(formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).isChanged() ? formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).getTitle() : wfmStrings.fax(), formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).isInformation());
            if (formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).isInformation()) {
                new KpiToolTip(fax, formPropertyMap.get(COURSE_BOOKING.FAX_NUMBER).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.FAX_NUMBER, fax, getTitle(wfmStrings.fax(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL) != null) {
            addField(COURSE_BOOKING.CUSTOMER_EMAIL, companyEmail, getTitle(formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).isChanged() ? formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).getTitle() : wfmStrings.email(), formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).isInformation());
            if (formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).isInformation()) {
                new KpiToolTip(companyEmail, formPropertyMap.get(COURSE_BOOKING.CUSTOMER_EMAIL).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.CUSTOMER_EMAIL, companyEmail, getTitle(wfmStrings.email(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE) != null) {
            addField(COURSE_BOOKING.TRAINING_VENUE, location, getTitle(formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).isChanged() ? formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).getTitle() : wfmStrings.traningVenue(), formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).isInformation());
            if (formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).isInformation()) {
                new KpiToolTip(location, formPropertyMap.get(COURSE_BOOKING.TRAINING_VENUE).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.TRAINING_VENUE, location, getTitle(wfmStrings.traningVenue(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE_BOOKING.TYPE) != null) {
            addField(COURSE_BOOKING.TYPE, type, getTitle(formPropertyMap.get(COURSE_BOOKING.TYPE).isChanged() ? formPropertyMap.get(COURSE_BOOKING.TYPE).getTitle() : wfmStrings.type(), formPropertyMap.get(COURSE_BOOKING.TYPE).isRequired()),false,
                    formPropertyMap.get(COURSE_BOOKING.TYPE).isInformation());
            if (formPropertyMap.get(COURSE_BOOKING.TYPE).isInformation()) {
                new KpiToolTip(type, formPropertyMap.get(COURSE_BOOKING.TYPE).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.TYPE, type, getTitle(wfmStrings.type(), false));
        }
//        addField(COURSE_BOOKING.COMPANY_NAME, companyName, getTitle(wfmStrings.companyName()));
        addField(COURSE_BOOKING.COURSE_BOOKING_NUMBER, number, wfmStrings.number());
//        addField(COURSE_BOOKING.COURSE_BOOKING_INVOICE_NUMBER, pnlIvoice, getTitle(wfmStrings.invoiceNumber()));
//        addField(COURSE_BOOKING.COMPANY_NUMBER, companyNumber, getTitle(wfmStrings.companyNumber()));
//        addField(COURSE_BOOKING.PHONE_NUMBER, phone, getTitle(wfmStrings.phone()));
//        addField(COURSE_BOOKING.FAX_NUMBER, fax, getTitle(wfmStrings.fax()));
        addField(COURSE_BOOKING.CUSTOMER_EMAIL, companyEmail, getTitle(wfmStrings.email()));
//        addField(COURSE_BOOKING.TRAINING_VENUE, location, getTitle(wfmStrings.traningVenue(), true));
        addField(COURSE_BOOKING.STATUS, status, getTitle(wfmStrings.status()));
        addField(COURSE_BOOKING.TYPE, type, getTitle(wfmStrings.type()));

        //1.2
        addTitleField(COURSE_BOOKING.CLIENT_AUTH, getTitle(tcStrings.clientAuthorisation()));
        addField(COURSE_BOOKING.CONTACT_NAME, contactName, getTitle(Property.get(Constants.Contacts, wfmStrings.contactName(), wfmStrings.contact())));
        addField(COURSE_BOOKING.POSITION, contactPosition, getTitle(wfmStrings.position()));
        addField(COURSE_BOOKING.REF_IND, contactRefInd, getTitle(tcStrings.refInd()));
        addField(COURSE_BOOKING.CONTACT_PHONE, contactPhone, getTitle(wfmStrings.phone()));
        addField(COURSE_BOOKING.CONTACT_EMAIL, contactEmail, getTitle(wfmStrings.email()));


        addTitleField(COURSE_BOOKING.STUDENT_COURSE_SCHEDULE_DETAILS, tcStrings.studentCourseScheduleDetails());
        addField(COURSE_BOOKING.STUDENT_COURSE_BOOKING, dataGrid, getTitle(tcStrings.courseBooking(), false));
        getCustomFieldUtil().drawCustomFields(this, objectID, true);
        show();
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        tcService.getBookingStudentItems(objectID, new AbstractAsyncCallback<CourseBookingItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                throwable.printStackTrace();
            }

            public void success(CourseBookingItem compLocation) {
                LoadingPanel.loading(false);

                statusCode = compLocation.getStatusCode();

                drawLocationData(compLocation);
            }
        });
    }

    private void drawLocationData(final CourseBookingItem courseBookingItem) {
        masterCardPaymentURL = courseBookingItem.getMasterCardPaymentURL();
        btnSubmitForApproval.setVisible(!BOOKING_PAY_ONLINE.equals(courseBookingItem.getTypeCode()) && BOOKING_DRAFT.equals(courseBookingItem.getStatusCode()));
        btnApprove.setVisible(!BOOKING_PAY_ONLINE.equals(courseBookingItem.getTypeCode()) && BOOKING_SUBMITTED_TO_MANAGER.equals(courseBookingItem.getStatusCode()));
        btnReject.setVisible(!BOOKING_PAY_ONLINE.equals(courseBookingItem.getTypeCode()) && BOOKING_SUBMITTED_TO_MANAGER.equals(courseBookingItem.getStatusCode()));
        btnPayOnline.setVisible(!courseBookingItem.isKeyClient() && BOOKING_PAY_ONLINE.equals(courseBookingItem.getTypeCode()) && masterCardPaymentURL != null);
        btnPdfGenerator.setVisible(!BOOKING_REJECTED.equals(courseBookingItem.getStatusCode()));

        companyName.setHTML(courseBookingItem.getCustomerItems().getName());
        number.setHTML(courseBookingItem.getNumber());
        companyNumber.setHTML(courseBookingItem.getCustomerItems().getNumber());
        phone.setHTML(Utils.getPhoneCallFormat2(courseBookingItem.getCustomerItems().getPhone()).toString());
        fax.setHTML(Utils.getPhoneCallFormat2(courseBookingItem.getCustomerItems().getFax()).toString());
        companyEmail.setHTML(courseBookingItem.getCustomerItems().getEmail());
        getCustomFieldUtil().fillCustomFieldsWithData(courseBookingItem.getCustomFieldItems(),true);

        if (courseBookingItem.getInvoiceID() != null) {
            invoiceID = courseBookingItem.getInvoiceID();
            invoiceNumber.setText(courseBookingItem.getInvoiceNumber());
        }

        if (courseBookingItem.getStatusCode().equals(BOOKING_APPROVED) && courseBookingItem.getTypeCode().equals(BOOKING_PAY_UPON_ARRIVAL) || courseBookingItem.getTypeCode().equals(BOOKING_PAY_ONLINE) && courseBookingItem.getInvoiceID() != null) {
            addField(COURSE_BOOKING.COURSE_BOOKING_INVOICE_NUMBER, pnlIvoice, getTitle(wfmStrings.invoiceNumber()));
            reGenerateInvoice.setVisible(true);
        }

        if (courseBookingItem.getContactItems() != null) {
            ContactListItem contactListItem = courseBookingItem.getContactItems();
            contactName.setHTML(contactListItem.getContactName());
            contactPosition.setHTML(contactListItem.getJobTitle());
            contactRefInd.setHTML(contactListItem.getRefIndNumber() != null ? contactListItem.getRefIndNumber() : "");
            contactPhone.setHTML(Utils.getPhoneCallFormat2(contactListItem.getPrimaryPhone()).toString());
            contactEmail.setHTML(contactListItem.getPrimaryEmail());
        }

        if (courseBookingItem.getLocation() != null) {
            location.setHTML(courseBookingItem.getLocation().getName());
        }
        status.setHTML(courseBookingItem.getStatus().getName());
        type.setHTML(courseBookingItem.getType().getName());
        getCustomFieldUtil().fillCustomFieldsWithData(courseBookingItem.getCustomFieldItems(),true);

        dataProvider.getList().clear();
        dataProvider.getList().addAll(courseBookingItem.getStudentItems());
        dataProvider.refresh();

    }


    private void initTableColumns() {

        Column<StudentItem, String> residenceNumber = new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem object) {
                return object.getSafetyPPNumber();
            }

        };
        dataGrid.addColumn(residenceNumber, tcStrings.residenceNumber());
        dataGrid.setColumnWidth(residenceNumber, 60, Style.Unit.PX);

        Column<StudentItem, String> compEmpNumber = new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem object) {
                return object.getCompEmpNum();
            }
        };
        dataGrid.addColumn(compEmpNumber, wfmStrings.companyEmployeeNumber());
        dataGrid.setColumnWidth(compEmpNumber, 100, Style.Unit.PX);

        Column<StudentItem, String> referenceIndNumber = new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem object) {
                return object.getRefIndNumber();
            }

        };
        dataGrid.addColumn(referenceIndNumber, tcStrings.refIndNumber());
        dataGrid.setColumnWidth(referenceIndNumber, 60, Style.Unit.PX);

        Column<StudentItem, String> depNumber = new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem object) {
                return object.getDepartmentCode();
            }
        };
        dataGrid.addColumn(depNumber, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.number(), wfmStrings.department()));
        dataGrid.setColumnWidth(depNumber, 60, Style.Unit.PX);


        Column<StudentItem, String> firstName = new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem object) {
                return object.getFirstName();
            }
        };
        dataGrid.addColumn(firstName, wfmStrings.firstName());
        dataGrid.setColumnWidth(firstName, 80, Style.Unit.PX);

        Column<StudentItem, String> lastName = new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem object) {
                return object.getLastName();
            }
        };
        dataGrid.addColumn(lastName, wfmStrings.lastName());
        dataGrid.setColumnWidth(lastName, 80, Style.Unit.PX);

        Column<StudentItem, String> courseScheduleNumber = new Column<StudentItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(StudentItem object) {
                return object.getCourseSchedulerNumber();
            }
        };
        courseScheduleNumber.setFieldUpdater((index, object, value) -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_SCHEDULED_COURSE + "|summary/" + object.getCourseScheduleID() + "/" + (object.getInstructor() == null)));
        dataGrid.addColumn(courseScheduleNumber, tcStrings.courseScheduleNumber());
        dataGrid.setColumnWidth(courseScheduleNumber, 70, Style.Unit.PX);

        Column<StudentItem, String> course = new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem object) {
                return object.getCourse();
            }
        };
        dataGrid.addColumn(course, wfmStrings.course());
        dataGrid.setColumnWidth(course, 100, Style.Unit.PX);

        Column<StudentItem, String> email = new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem object) {
                return object.getPrimaryEmail();
            }
        };
        dataGrid.addColumn(email, wfmStrings.email());
        dataGrid.setColumnWidth(email, 50, Style.Unit.PX);

        Column<StudentItem, SafeHtml> mobail = new Column<StudentItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(StudentItem object) {
                SafeHtmlBuilder builder = new SafeHtmlBuilder();
                builder.appendHtmlConstant(Utils.getPhoneCallFormat2(object.getPrimaryPhone()).toString());
                return builder.toSafeHtml();
            }
        };
        dataGrid.addColumn(mobail, wfmStrings.mobile());
        dataGrid.setColumnWidth(mobail, 50, Style.Unit.PX);

        Column<StudentItem, String> status = new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem object) {
                return object.getStatus();
            }
        };
        dataGrid.addColumn(status, wfmStrings.status());
        dataGrid.setColumnWidth(status, 50, Style.Unit.PX);

        Column<StudentItem, String> language = new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem object) {
                return object.getLanguage();
            }
        };
        dataGrid.addColumn(language, wfmStrings.language());
        dataGrid.setColumnWidth(language, 50, Style.Unit.PX);

        Column<StudentItem, String> courseDate = new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem object) {
                return DateUtils.formatInternal(object.getCourseSchedulerStartDate());
            }
        };
        dataGrid.addColumn(courseDate, tcStrings.courseDate());
        dataGrid.setColumnWidth(courseDate, 50, Style.Unit.PX);
    }

    @Override
    protected void addButtons() {
        btnSubmitForApproval = addButton(wfmStrings.submitForApproval(), BTN_SUCCESS, event -> {
            LoadingPanel.loading(true);
            TCService.App.get().updateCourseBookingStatus(objectID, BOOKING_SUBMITTED_TO_MANAGER, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(Void result) {
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COURSE_BOOKING_ADD_EDIT, objectID, CourseBookingSummaryView.this);
                    closeTab();
                }
            });
        });
        btnApprove = addButton(wfmStrings.approve(), WfmButton2.BTN_SUCCESS, event -> {
            LoadingPanel.loading(true);
            TCService.App.get().updateCourseBookingStatus(objectID, BOOKING_APPROVED, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(Void result) {
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COURSE_BOOKING_ADD_EDIT, objectID, CourseBookingSummaryView.this);
                    closeTab();
                }
            });
        });
        btnReject = addButton(wfmStrings.reject(), WfmButton2.BTN_REJECT, event -> {
            LoadingPanel.loading(true);
            TCService.App.get().updateCourseBookingStatus(objectID, BOOKING_REJECTED, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(Void result) {
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COURSE_BOOKING_ADD_EDIT, objectID, CourseBookingSummaryView.this);
                    closeTab();
                }
            });
        });
        btnPayOnline = addButton(tcStrings.payOnline(), BTN_DEFAULT_OUTLINE, event -> Window.open(masterCardPaymentURL, "_blank", ""));

        btnPdfGenerator = addButton(wfmStrings.pdfVersion(), BTN_DEFAULT_OUTLINE, event -> {
            RequestObject requestObject = new RequestObject(objectID);
            String pdfURL = CommandConstants.PDF_URL + "/courseBookingPDFHandler";
            HashMap<String, String> parametrs = requestObject.getRequestParams();
            Utils.sendPDFOrExcelRequest(panel, pdfURL, parametrs, "_blank");
        });
        btnSubmitForApproval.setVisible(false);
        btnApprove.setVisible(false);
        btnReject.setVisible(false);
        btnPayOnline.setVisible(false);
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
        return LayoutRPC.STUDENT_ATTENDED_COURSE_BOOKING;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
