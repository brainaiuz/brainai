package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.TCHtmlTemplates;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/27/12
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledCourseSummaryView extends CustomForm2 implements TCConstants, Constants {
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final TCStrings tcStrings = TCStrings.App.get();

    private HTML customer, quote, course, number, startDate, endDate, duration, location, venue, session, instructor, assessor, numberOfSeats, visibility, language;
    private HorizontalPanel pnlInvoicesContainer;
    private Label invoiceNumber;
    private KpiModal pdfPopup;
    private final Integer objectID;
    private ScheduledCourseItem scheduledCourseItem = null;
    private WfmButton2 exportToCSV, exportToPDF;
    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public ScheduledCourseSummaryView(Integer objectID) {
        super("summary", tcStrings.scheduledCourseSummary());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.ScheduledCourse,LayoutRPC.SCHEDULED_COURSE_FORM, new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                ScheduledCourseSummaryView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        drawForm();
    }

    private void drawForm() {
        customer = new HTML();
        quote = new HTML();
        course = new HTML();
        number = new HTML();
        startDate = new HTML();
        endDate = new HTML();
        duration = new HTML();
        location = new HTML();
        venue = new HTML();
        session = new HTML();
        instructor = new HTML();
        assessor = new HTML();
        numberOfSeats = new HTML();
        visibility = new HTML();
        language = new HTML();

        pnlInvoicesContainer = new HorizontalPanel();
        pnlInvoicesContainer.setSpacing(5);

        addTitleField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.SCHEDULED_COURSE_DETAILS, tcStrings.scheduledCourseDetails());
        if (formPropertyMap != null && formPropertyMap.get("location") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.LOCATION, location, getTitle(formPropertyMap.get("location").isChanged() ? formPropertyMap.get("location").getTitle() : wfmStrings.location(), formPropertyMap.get("location").isRequired()),false,
                    formPropertyMap.get("location").isInformation());
            if (formPropertyMap.get("location").isInformation()) {
                new KpiToolTip(location, formPropertyMap.get("location").getInformationText());
            }
        } else {
            addField("location", location, getTitle(wfmStrings.location(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get("course") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.COURSE, course, getTitle(formPropertyMap.get("course").isChanged() ? formPropertyMap.get("course").getTitle() : wfmStrings.courses(), formPropertyMap.get("course").isRequired()),false,
                    formPropertyMap.get("course").isInformation());
            if (formPropertyMap.get("course").isInformation()) {
                new KpiToolTip(course, formPropertyMap.get("course").getInformationText());
            }
        } else {
            addField("course", course, getTitle(wfmStrings.courses(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get("language") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.LANGUAGE, language, getTitle(formPropertyMap.get("language").isChanged() ? formPropertyMap.get("language").getTitle() : wfmStrings.language(), formPropertyMap.get("language").isRequired()),false,
                    formPropertyMap.get("language").isInformation());
            if (formPropertyMap.get("language").isInformation()) {
                new KpiToolTip(language, formPropertyMap.get("language").getInformationText());
            }
        } else {
            addField("language", language, getTitle(wfmStrings.language(), false));
        }


        if (formPropertyMap != null && formPropertyMap.get("instructor") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.INSTRUCTOR, instructor, getTitle(formPropertyMap.get("instructor").isChanged() ? formPropertyMap.get("instructor").getTitle() : wfmStrings.instructor(), formPropertyMap.get("instructor").isRequired()),false,
                    formPropertyMap.get("instructor").isInformation());
            if (formPropertyMap.get("instructor").isInformation()) {
                new KpiToolTip(instructor, formPropertyMap.get("instructor").getInformationText());
            }
        } else {
            addField("instructor", instructor, getTitle(wfmStrings.instructor(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get("assessor") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.ASSESSOR, assessor, getTitle(formPropertyMap.get("assessor").isChanged() ? formPropertyMap.get("assessor").getTitle() : wfmStrings.assessor(), formPropertyMap.get("assessor").isRequired()),false,
                    formPropertyMap.get("assessor").isInformation());
            if (formPropertyMap.get("assessor").isInformation()) {
                new KpiToolTip(assessor, formPropertyMap.get("assessor").getInformationText());
            }
        } else {
            addField("assessor", assessor, getTitle(wfmStrings.assessor(), false));
        }
//        if (formPropertyMap != null && formPropertyMap.get("startDate") != null) {
//            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.START_DATE, Utils.getInHorizontalPanel(0, 0, true, dateTime.startDate, dateTime.startTime), getTitle(formPropertyMap.get("startDate").isChanged() ? formPropertyMap.get("startDate").getTitle() : wfmStrings.startDate(), formPropertyMap.get("startDate").isRequired()), false,
//                    formPropertyMap.get("startDate").isInformation());
//            Utils.getInHorizontalPanel(0, 0, true, dateTime.startDate, dateTime.startTime).setVisible(true);
//            if (formPropertyMap.get("startDate").isInformation()) {
//                new KpiToolTip(Utils.getInHorizontalPanel(0, 0, true, dateTime.startDate, dateTime.startTime), formPropertyMap.get("startDate").getInformationText());
//            }
//        } else {
//            addField("startDate", Utils.getInHorizontalPanel(0, 0, true, dateTime.startDate, dateTime.startTime), getTitle(wfmStrings.start(), false));
//        }

        if (formPropertyMap != null && formPropertyMap.get("duration") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.DURATION, duration, getTitle(formPropertyMap.get("duration").isChanged() ? formPropertyMap.get("duration").getTitle() : wfmStrings.duration(), formPropertyMap.get("duration").isRequired()), false,
                    formPropertyMap.get("duration").isInformation());
            if (formPropertyMap.get("duration").isInformation()) {
                new KpiToolTip(duration, formPropertyMap.get("duration").getInformationText());
            }
        } else {
            addField("duration", duration, getTitle(wfmStrings.duration(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get("numberOfSeats") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.NUMBER_OF_SEATS, numberOfSeats, getTitle(formPropertyMap.get("numberOfSeats").isChanged() ? formPropertyMap.get("numberOfSeats").getTitle() : wfmStrings.numberOfSeats(), formPropertyMap.get("numberOfSeats").isRequired()), false,
                    formPropertyMap.get("numberOfSeats").isInformation());
            if (formPropertyMap.get("numberOfSeats").isInformation()) {
                new KpiToolTip(numberOfSeats, formPropertyMap.get("numberOfSeats").getInformationText());
            }
        } else {
            addField("numberOfSeats", numberOfSeats, getTitle(wfmStrings.numberOfSeats(), false));
        }

        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.CUSTOMER, customer, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), false));
        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.QUOTE, quote, getTitle(tcStrings.quotation(), false));
//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.COURSE, course, getTitle(wfmStrings.course(), false));
        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.NUMBER, number, getTitle(wfmStrings.number(), false));
        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.START_DATE, startDate, getTitle(wfmStrings.startDate(), false));
        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.END_DATE, endDate, getTitle(wfmStrings.endDate(), false));
//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.DURATION, duration, getTitle(wfmStrings.duration(), false));

//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.LOCATION, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), false));
        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.SESSION, session, getTitle(tcStrings.session(), false));
//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.INSTRUCTOR, instructor, getTitle(wfmStrings.instructor(), false));
        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.NUMBER_OF_SEATS, numberOfSeats, getTitle(wfmStrings.numberOfSeats(), false));
        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.VENUE, venue, getTitle(tcStrings.venue(), false));
        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.VISIBILITY, visibility, getTitle(wfmStrings.visibility(), false));
//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.LANGUAGE, language, getTitle(wfmStrings.language(), false));
//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.ASSESSOR, assessor, getTitle(wfmStrings.assessor(), false));
        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.INVOICES, pnlInvoicesContainer, getTitle(wfmStrings.invoices()));
        getCustomFieldUtil().drawCustomFields(this, objectID, true);
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }


    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        TCService.App.get().getCourseSchedule(objectID, true, new AsyncCallback<ScheduledCourseItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                GWT.log(caught.getMessage());
            }

            @Override
            public void onSuccess(ScheduledCourseItem result) {
                LoadingPanel.loading(false);
                scheduledCourseItem = result;
                fillFormWithData();
                initScheduleInvoices();
            }
        });
    }

    protected void fillFormWithData() {
        customer.setHTML(TCHtmlTemplates.getInstance().value(scheduledCourseItem.getCustomer() != null ? scheduledCourseItem.getCustomer() : "N/A"));
        quote.setHTML(TCHtmlTemplates.getInstance().value(scheduledCourseItem.getQuote() != null ? scheduledCourseItem.getQuote() : "N/A"));
        course.setHTML(TCHtmlTemplates.getInstance().value(scheduledCourseItem.getCourseName()));
        number.setHTML(TCHtmlTemplates.getInstance().value(scheduledCourseItem.getNumber()));
        startDate.setHTML(TCHtmlTemplates.getInstance().value(DateUtils.formatInternal(scheduledCourseItem.getStartDate())));
        endDate.setHTML(TCHtmlTemplates.getInstance().value(DateUtils.formatInternal(scheduledCourseItem.getEndDate())));
        duration.setHTML(TCHtmlTemplates.getInstance().durationValue(String.valueOf(scheduledCourseItem.getDuration())));

        location.setHTML(TCHtmlTemplates.getInstance().value(scheduledCourseItem.getLocationName()));
        venue.setHTML(TCHtmlTemplates.getInstance().value(scheduledCourseItem.getVenue() != null ? scheduledCourseItem.getVenue() : ""));
        instructor.setHTML(TCHtmlTemplates.getInstance().value(scheduledCourseItem.getInstructorName() != null ? scheduledCourseItem.getInstructorName() : "N/A"));
        assessor.setHTML(TCHtmlTemplates.getInstance().value(scheduledCourseItem.getAssessorName() != null ? scheduledCourseItem.getAssessorName() : "N/A"));
        numberOfSeats.setHTML(TCHtmlTemplates.getInstance().value(scheduledCourseItem.getNumberOfSeats() != null ? String.valueOf(scheduledCourseItem.getNumberOfSeats()) : ""));
        visibility.setHTML(TCHtmlTemplates.getInstance().value(SC_PUBLIC.equals(scheduledCourseItem.getVisibility()) ? SC_PUBLIC_STR : SC_PRIVATE_STR));
        language.setHTML(TCHtmlTemplates.getInstance().value(scheduledCourseItem.getLanguageName()));
        getCustomFieldUtil().fillCustomFieldsWithData(scheduledCourseItem.getCustomFieldItems(),true);
    }

//    private void initCourseRequirements() {
//        if (scheduledCourseItem.getReservations() != null && scheduledCourseItem.getReservations().length > 0) {
//            WfmForm table = new WfmForm(new String[]{"10%", "50%", "40%"});
//            pnlCRContainer.add(table);
//
//            for (ScheduledCourseReservation reservation : scheduledCourseItem.getReservations()) {
//                table.addField(reservation.getItemCategory(), new HTML(reservation.getItem()));
//            }
//        }
//    }

    private void initScheduleInvoices() {
        if (scheduledCourseItem.getInvoices() != null && scheduledCourseItem.getInvoices().length > 0) {
            for (final SelectItem inv : scheduledCourseItem.getInvoices()) {
                invoiceNumber = new Label();
                invoiceNumber.setStyleName("uploadLinkStyle2");
                invoiceNumber.setText(inv.getName());
                invoiceNumber.addClickHandler(event -> {
                    String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + SALE_INVOICE + "|summary/" + inv.getId();
                    Window.open(addSalesInvoice, "_blank", "");
                });
                pnlInvoicesContainer.add(invoiceNumber);
            }
        } else {
            pnlInvoicesContainer.add(new HTML("N/A"));
        }
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        MaterialLink options = new MaterialLink(wfmStrings.options());
        MaterialSplitButton optionsButton = new MaterialSplitButton(options, Constants.BTN_DEFAULT_OUTLINE);
        if (Utils.hasRole(Constants.ADMIN)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + URL.encodeQueryString(url));
            });
            optionsButton.addItem(customize);
        }


        MaterialLink delete = new MaterialLink(wfmStrings.delete());
        delete.addClickHandler(event -> {
            //register delete student logic
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            wfmMessageBox.setTitle(wfmStrings.warning());
            wfmMessageBox.setMessage(wfmStrings.sureYouWantToDelete());
            wfmMessageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onCancel() {
                }

                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    TCService.App.get().deleteCourseSchedule(objectID, new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Boolean result) {
                            LoadingPanel.loading(false);
                            if (result) {
                                Info.show(wfmMessages.yourSomethingHasBeenDeleted(wfmStrings.courseSchedule().toLowerCase()));
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SCHEDULED_COURSE_SAVED, null, null);
                                closeTab();
                            }
                        }
                    });
                }
            });
            wfmMessageBox.open();
        });
        optionsButton.addItem(delete);

        addButton(optionsButton);

        exportToCSV = new WfmButton2(wfmStrings.csv(), WfmButton2.BTN_WHITE_OUTLINE);
        exportToCSV.addClickHandler(event -> {
            GWT.log("wfmStrings.csv()");
            String csvURL = CommandConstants.COMMON_URL + "/downloadCourseUserCSVHandler";
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setScheduledCourseID(objectID);

            Utils.sendCSVRequest(panel, csvURL, filterParameter.getRequestParams(), "_blank");
        });
        addButton(exportToCSV);

        exportToPDF = addPdfButton();
        exportToPDF.addClickHandler(event -> openPDFPopup());
        addButton(exportToPDF);


        WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
        editButton.addClickHandler(event -> {
            //register edit student logic
            SinksContainerFactory.entryPoint.onHistoryChanged(TC_SCHEDULED_COURSE + "|edit/" + objectID);
        });
        addButton(editButton);
    }

    private void openPDFPopup() {
        final String pdfURL = CommandConstants.PDF_URL + "/downloadCourseUserPDFHandler";
        final ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setScheduledCourseID(objectID);
        int days = CalendarUtil.getDaysBetween(scheduledCourseItem.getStartDate(), scheduledCourseItem.getEndDate());
        if (days > 0) {
            drawPdfTable();
        } else {
            Utils.sendPDFOrExcelRequest(panel, pdfURL, filterParameter.getRequestParams(), "_blank");
        }
    }

    private void drawPdfTable() {
        final String pdfURL = CommandConstants.PDF_URL + "/downloadCourseUserPDFHandler";
        final ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setScheduledCourseID(objectID);
        final FlexTable pdfTable = new FlexTable();

        pdfPopup = new KpiModal();
        pdfPopup.setCloseButton(true);
        LoadingPanel.loading(true);
        TCService.App.get().checkDayForAvailibility(objectID, scheduledCourseItem.getStartDate(), scheduledCourseItem.getEndDate(), new AsyncCallback<Date[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(Date[] dates) {
                LoadingPanel.loading(false);
                if (dates != null && dates.length > 0) {
                    for (int i = 0; i < dates.length; i++) {
                        Label dateLabel = new Label(DateUtils.format(dates[i]));
                        final Anchor pdfLink = new Anchor("Download PDF");
                        pdfLink.setName(DateUtils.formatInternal(dates[i]));
                        pdfLink.setWordWrap(i == 0);
                        pdfLink.addClickHandler(clickEvent -> {
                            //for date format
                            filterParameter.setViewType(Utils.getLongDateFormat());
                            filterParameter.setStartDateNC(pdfLink.getName());
                            filterParameter.setActualStart(pdfLink.getWordWrap());
                            Utils.sendPDFOrExcelRequest(panel, pdfURL, filterParameter.getRequestParams(), "_blank");
                        });
                        pdfTable.setWidget(i, 0, dateLabel);
                        pdfTable.setWidget(i, 1, pdfLink);
                    }
                    pdfTable.setCellPadding(5);
                    pdfPopup.add(pdfTable);
                    pdfPopup.open();
                }
            }
        });
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
        return LayoutRPC.SCHEDULED_COURSE_FORM;
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
        return "bgMark scheduled-course-view-icon";
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
