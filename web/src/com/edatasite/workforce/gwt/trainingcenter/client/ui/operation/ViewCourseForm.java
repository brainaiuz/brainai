package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.TCHtmlTemplates;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Ilxom Lutfullaev
 * Date: 7/20/12
 * Time: 1:40 PM
 */

public class ViewCourseForm extends AddEditCourseView {

    private static final TCStrings tcStrings = TCStrings.App.get();
    private HTML subject, number, name, alias, type, validity, duration, description, pricePerStudent, otherPreRequisite, account, courseRequirements;
    private KpiDataGrid<SelectItem> instructors;
    private ListDataProvider<SelectItem> dataProvider;
    private KpiCheckBox examRequired, opito, medClearance;
    private final Integer objectID;
    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;




    public ViewCourseForm(Integer objectID) {
        super("summary", tcStrings.courseView(), objectID);
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Course,getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                ViewCourseForm.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        tcService.getCourseItem(objectID, new AbstractAsyncCallback<CourseItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final CourseItem result) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    //Pre-requisite
                    pre_Requisite = new MultiTableNewUI(new MultiTableWidgets() {
                        @Override
                        public WidgetsMap getWidgetsMaps() {
                            return getpreRequisitesMap(null);
                        }

                        @Override
                        public boolean isFilled() {
                            return false;
                        }
                    }, true);
                    addField(COURSE.PREREQUISITE, pre_Requisite, getTitle(wfmStrings.preRequisite()));

                    fillFormWithData2(result);
                });
            }
        });
    }

    @Override
    protected WidgetsMap getLocationsMap(Integer locationID, BigDecimal pricePerST, BigDecimal stopFeePerST) {
        WidgetsMap widgetsMap = new WidgetsMap();
        //location list box
        HTML locationsHTML = new HTML();
        locationsHTML.setWidth("235px");

        locationsHTML.addStyleName("form-control");
        locationsHTML.ensureDebugId("course_summary_view_price_per_location");
        DataListBox locationsBox = new DataListBox();
        if (locationItems != null) {
            locationsBox.setItems(locationItems);
        }
        if (locationID != null) {
            locationsBox.setSelected(locationID);
            locationsHTML.setHTML(locationsBox.getSelectedItem().getName() + ":&nbsp;&nbsp;");
        }
        //price text box
        final HTML pricePerStHTML = new HTML();
        pricePerStHTML.setWidth("85px");
        pricePerStHTML.addStyleName("form-control");
        pricePerStHTML.ensureDebugId("course_summary_view_price_per_student");
        pricePerStHTML.setHTML(pricePerST != null ? pricePerST + "&nbsp;&nbsp;&nbsp;&nbsp;" : null);
        //stop fee text box
        final HTML stopfeePerStHTML = new HTML();
        stopfeePerStHTML.setWidth("85px");
        stopfeePerStHTML.addStyleName("form-control");
        stopfeePerStHTML.ensureDebugId("course_summary_view_stop_fee_per_student");
        stopfeePerStHTML.setHTML(stopFeePerST != null ? stopFeePerST.toString() : null);

        widgetsMap.addWidgets(locationsHTML, pricePerStHTML, stopfeePerStHTML);
        widgetsMap.addWidgetToMap(MultiTable.LIST_BOX, locationsBox);
        widgetsMap.addWidgetToMap(MultiTable.TEXT_BOX, pricePerStHTML);
        widgetsMap.addWidgetToMap(MultiTable.RADION_BUTTON, stopfeePerStHTML);

        return widgetsMap;
    }

    @Override
    protected WidgetsMap getpreRequisitesMap(SelectItem preRequisiteItem) {
        WidgetsMap widgetsMap = new WidgetsMap();
        HTML preRequisite = new HTML();
        preRequisite.setWidth("100%");
        preRequisite.addStyleName("form-control");
        preRequisite.ensureDebugId("course_summary_view pre Requisites");
        DataListBox preRequisitesBox = new DataListBox();
        if (preRequisites != null) {
            preRequisitesBox.setItems(preRequisites);
        }
        if (preRequisiteItem != null) {
            preRequisitesBox.setSelected(preRequisiteItem);
            preRequisite.setHTML(preRequisitesBox.getSelectedItem().getName());
        }
        widgetsMap.addWidgets(preRequisite);
        widgetsMap.addToCenter(MultiTable.LIST_BOX, preRequisite);
        return widgetsMap;
    }
    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (this.customFieldUtil == null) {
            this.customFieldUtil = new FormHasCustomField();
        }
        return this.customFieldUtil;
    }

    protected void fillFormWithData2(CourseItem item) {
        subject.setHTML(item.getSubject() != null ? item.getSubject().getName() : "");
        number.setHTML(item.getNumberData() != null ? item.getNumberData().getNumberString() : "");
        name.setHTML(item.getCourseName() != null ? item.getCourseName() : "");
        description.setHTML(item.getDescription() != null ? item.getDescription() : "");
        otherPreRequisite.setText(item.getOtherPreRequisites() != null ? item.getOtherPreRequisites() : "");
        alias.setHTML(item.getAliasName() != null ? item.getAliasName() : "");
        type.setHTML(item.getCourseType() != null ? item.getCourseType().getName() : "");
        validity.setHTML(TCHtmlTemplates.getInstance().validityValue(item.getValidity() != null ? item.getValidity().toString() : "0"));
        duration.setHTML(TCHtmlTemplates.getInstance().durationValue(item.getDuration() != null ? item.getDuration().toString() : "0"));
//		pricePerStudent.setHTML(item.getPricePerStudent() != null ? Utils.getNumberFormat().format(item.getPricePerStudent()) : "");

        if (item.getPricePerLocationStudent() != null && item.getPricePerLocationStudent().size() > 0) {
            pricePerLocationStudent.removeAllRows();
            for (Integer locationID : item.getPricePerLocationStudent().keySet()) {
                BigDecimal pricePerST = item.getPricePerLocationStudent().get(locationID);
                BigDecimal stopFeePerST = item.getStopFeePerLocationStudent().get(locationID);
                pricePerLocationStudent.addWidgets(getLocationsMap(locationID, pricePerST, stopFeePerST));
            }
        }

        courseRequirements.setHTML(item.getCourseRequirementsAsString() != null ? item.getCourseRequirementsAsString() : "");

        if (item.getPreRequisite() != null) {
            pre_Requisite.removeAllRows();
            for (SelectItem items : item.getPreRequisite()) {
                pre_Requisite.addWidgets(getpreRequisitesMap(items));
            }
        }

        if (item.getInstructors() != null) {
            List<SelectItem> tableses = dataProvider.getList();
            tableses.clear();
            Collections.addAll(tableses, item.getInstructors().toArray(new SelectItem[]{}));
            dataProvider.refresh();
        }
        customFieldUtil.fillCustomFieldsWithData(item.getCustomFieldItems(), true);
    }


    @Override
    protected void initialize() {
        subject = new HTML();
        number = new HTML();
        name = new HTML();
        description = new HTML();
        alias = new HTML();
        type = new HTML();
        validity = new HTML();
        duration = new HTML();
        pricePerStudent = new HTML();

        pricePerLocationStudent = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getLocationsMap(null, null, null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        }, true);
        refreshLocationDropDowns(pricePerLocationStudent, null);

        HTML location = new HTML(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()));
        HTML price = new HTML(wfmStrings.price());
        HTML stopFee = new HTML(tcStrings.stopFee());
        location.setWidth("170px");
        price.setWidth("85px");
        price.getElement().getStyle().setMarginLeft(65, Style.Unit.PX);
        stopFee.setWidth("85px");
        HorizontalPanel locationHeaderPanel = new HorizontalPanel();
        locationHeaderPanel.setSpacing(3);
        locationHeaderPanel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//        locationHeaderPanel.setWidth("100%");
        locationHeaderPanel.add(location);
        locationHeaderPanel.add(price);
        locationHeaderPanel.add(stopFee);
        locationHeaderPanel.setCellHorizontalAlignment(location, HasHorizontalAlignment.ALIGN_LEFT);
        locationHeaderPanel.setCellHorizontalAlignment(price, HasHorizontalAlignment.ALIGN_LEFT);
        locationHeaderPanel.setCellHorizontalAlignment(stopFee, HasHorizontalAlignment.ALIGN_LEFT);
        VerticalPanel locationPanel = new VerticalPanel();
        locationPanel.add(locationHeaderPanel);
        locationPanel.add(pricePerLocationStudent);


        otherPreRequisite = new HTML();
        account = new HTML();
        instructors = new KpiDataGrid<>(KEY_PROVIDER);
        instructors.setSize("100%", "230px");
        instructors.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage("<center>" + tcStrings.thereAreNoInstructorsYet() + "</center>", "", null));
        instructors.getEmptyTableWidget().getElement().getStyle().setMarginTop(-50, com.google.gwt.dom.client.Style.Unit.PX);
        dataProvider = new ListDataProvider<>();
        dataProvider.addDataDisplay(instructors);
        initTableColumns();

        examRequired = new KpiCheckBox();
        examRequired.setEnabled(false);
        opito = new KpiCheckBox();
        opito.setEnabled(false);
        medClearance = new KpiCheckBox();
        medClearance.setEnabled(false);

        courseRequirements = new HTML();

        addTitleField(COURSE.GENERAL_DETAILS, tcStrings.courseDetails());
        addTitleField(COURSE.INSTRUCTOR_DETAILS, wfmStrings.instructors());

        //Add fields

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null) {
            addField(CustomFormConstants.SUBJECT, subject, getTitle(formPropertyMap.get(CustomFormConstants.SUBJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.SUBJECT).getTitle() : wfmStrings.subject(), formPropertyMap.get(CustomFormConstants.SUBJECT).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.SUBJECT).isInformation());
            if (formPropertyMap.get(CustomFormConstants.SUBJECT).isInformation()) {
                new KpiToolTip(subject, formPropertyMap.get(CustomFormConstants.SUBJECT).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SUBJECT, subject, getTitle(wfmStrings.subject(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CODE) != null) {
            addField(CustomFormConstants.CODE, number, getTitle(formPropertyMap.get(CustomFormConstants.CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.CODE).getTitle() : wfmStrings.code(), formPropertyMap.get(CustomFormConstants.CODE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.CODE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CODE).isInformation()) {
                new KpiToolTip(number, formPropertyMap.get(CustomFormConstants.CODE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CODE, number, getTitle(wfmStrings.code(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, name, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(CustomFormConstants.NAME).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.NAME).isInformation()) {
                new KpiToolTip(name, formPropertyMap.get(CustomFormConstants.NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, description, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation());
            if (formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation()) {
                new KpiToolTip(description, formPropertyMap.get(CustomFormConstants.DESCRIPTION).getInformationText());
            }
        } else {
            addField(CustomFormConstants.DESCRIPTION, description, getTitle(wfmStrings.description(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DURATION) != null) {
            addField(CustomFormConstants.DURATION, duration, getTitle(formPropertyMap.get(CustomFormConstants.DURATION).isChanged() ? formPropertyMap.get(CustomFormConstants.DURATION).getTitle() : wfmStrings.duration(), formPropertyMap.get(CustomFormConstants.DURATION).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.DURATION).isInformation());
            if (formPropertyMap.get(CustomFormConstants.DURATION).isInformation()) {
                new KpiToolTip(duration, formPropertyMap.get(CustomFormConstants.DURATION).getInformationText());
            }
        } else {
            addField(CustomFormConstants.DURATION, duration, getTitle(wfmStrings.duration(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE.VALIDITY) != null) {
            addField(COURSE.VALIDITY, validity, getTitle(formPropertyMap.get(COURSE.VALIDITY).isChanged() ? formPropertyMap.get(COURSE.VALIDITY).getTitle() : wfmStrings.validity(), formPropertyMap.get(COURSE.VALIDITY).isRequired()),false,
                    formPropertyMap.get(COURSE.VALIDITY).isInformation());
            if (formPropertyMap.get(COURSE.VALIDITY).isInformation()) {
                new KpiToolTip(validity, formPropertyMap.get(COURSE.VALIDITY).getInformationText());
            }
        } else {
            addField(COURSE.VALIDITY, validity, getTitle(wfmStrings.validity(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE.PRICEPERSTUDENT) != null) {
            addField(COURSE.PRICEPERSTUDENT, locationPanel, getTitle(formPropertyMap.get(COURSE.PRICEPERSTUDENT).isChanged() ? formPropertyMap.get(COURSE.PRICEPERSTUDENT).getTitle() : wfmStrings.pricePerStudent(), formPropertyMap.get(COURSE.PRICEPERSTUDENT).isRequired()),false,
                    formPropertyMap.get(COURSE.PRICEPERSTUDENT).isInformation());
            if (formPropertyMap.get(COURSE.PRICEPERSTUDENT).isInformation()) {
                new KpiToolTip(locationPanel, formPropertyMap.get(COURSE.PRICEPERSTUDENT).getInformationText());
            }
        } else {
            addField(COURSE.PRICEPERSTUDENT, locationPanel, getTitle(wfmStrings.pricePerStudent(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE.OTHER_PREREQUISITE) != null) {
            addField(COURSE.OTHER_PREREQUISITE, otherPreRequisite, getTitle(formPropertyMap.get(COURSE.OTHER_PREREQUISITE).isChanged() ? formPropertyMap.get(COURSE.OTHER_PREREQUISITE).getTitle() : wfmStrings.otherPreRequisite(), formPropertyMap.get(COURSE.OTHER_PREREQUISITE).isRequired()),false,
                    formPropertyMap.get(COURSE.OTHER_PREREQUISITE).isInformation());
            if (formPropertyMap.get(COURSE.OTHER_PREREQUISITE).isInformation()) {
                new KpiToolTip(otherPreRequisite, formPropertyMap.get(COURSE.OTHER_PREREQUISITE).getInformationText());
            }
        } else {
            addField(COURSE.OTHER_PREREQUISITE, otherPreRequisite, getTitle(wfmStrings.pricePerStudent(), false));
        }


//        addField(CustomFormConstants.SUBJECT, subject, getTitle(wfmStrings.subject()));
//        addField(CustomFormConstants.CODE, number, getTitle(wfmStrings.code()));
//        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name()));
//        addField(CustomFormConstants.DESCRIPTION, description, getTitle(wfmStrings.description()));
//        addField(COURSE.ALIAS_NAME, alias, getTitle(wfmStrings.aliasName()));
        addField(CustomFormConstants.TYPE, type, getTitle(tcStrings.courseType()));
//        addField(COURSE.VALIDITY, validity, getTitle(wfmStrings.validity()));
//        addField(COURSE.DURATION, duration, getTitle(wfmStrings.duration()));
//        addField(COURSE.PRICEPERSTUDENT, locationPanel, getTitle(wfmStrings.pricePerStudent()));

//        addField(COURSE.OTHER_PREREQUISITE, otherPreRequisite, getTitle(tcStrings.otherPreRequisites()));
        addField(COURSE.ACCOUNTS, account, getTitle(wfmStrings.account()));
        addField(COURSE.EXAM_REQUIRED, examRequired, getTitle(tcStrings.examRequired()));
        addField(COURSE.OPITO, opito, getTitle(tcStrings.opito()));
        addField(COURSE.MEDICAL_CLEARANCE, medClearance, getTitle(tcStrings.medClearance()));
        addField(COURSE.INSTRUCTORS, instructors, null);
        addField(COURSE.COURSE_REQUIREMENTS, courseRequirements, getTitle(wfmStrings.courseRequirements()));
        customFieldUtil.drawCustomFields(this, objectID, true);
        show();
    }

    @Override
    protected void addButtons() {
        WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
        editButton.addClickHandler(event -> {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("course|add/add/" + objectID);
        });
        addButton(editButton);
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.COURSE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private void initTableColumns() {
        // Name
        Column<SelectItem, String> name = new Column<SelectItem, String>(new TextCell()) {
            @Override
            public String getValue(SelectItem file) {
                return file.getName();
            }
        };
        instructors.addColumn(name, wfmStrings.instructor());
        instructors.setColumnWidth(name, 200, Style.Unit.PX);
    }

    public static final ProvidesKey<SelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

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
