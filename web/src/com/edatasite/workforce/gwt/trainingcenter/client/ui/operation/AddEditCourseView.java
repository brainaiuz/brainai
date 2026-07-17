package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomListItem;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelectIcons;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCServiceAsync;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Ilxom Lutfullaev
 * Date: 7/18/12
 * Time: 4:56 PM
 */

public class AddEditCourseView extends CustomForm2 implements Colapse, Constants {


    public static final TCServiceAsync tcService = TCService.App.get();
    private static final TCStrings tcStrings = TCStrings.App.get();

    private final Integer objectID;
    public MultiTableNewUI pricePerLocationStudent;

    private DataListBox category;
    private Numbering numbering;
    private TextBox name;
    private TextArea2 description;
    private TextArea2 otherPreRequisite;
    private TextBox duration;
    private TextBox validity;
    public MultiTableNewUI pre_Requisite;
    public SelectItem[] locationItems;
    public SelectItem[] preRequisites = new SelectItem[]{};
    private LinkedHashMap<String, FormProperty> formPropertyMap;


    private KpiCellTree instructors;
    private CourseItem courseItem;

    private CustomList crList;

    //not used widgets
    private TextBox aliasName;
    private KpiCheckBox examRequired;
    private KpiCheckBox opito;
    private KpiCheckBox medClearance;
    private AccountsLookUp accountLookUp;
    FormHasCustomField customFieldUtil;

    public AddEditCourseView(Integer objectID) {
        super("addcourse", objectID == null ? tcStrings.addCourseView() : tcStrings.editCourseView());
        this.objectID = objectID;
    }

    public AddEditCourseView(String name, String description, Integer objectID) {
        super(name, description);
        this.objectID = objectID;
    }

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
                AddEditCourseView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        initialize();
        onAccountAdded();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void initialize() {
        String course_add_edit_view = "course_add_edit_view_";
        //Subject
        category = new DataListBox();
        category.addStyleName(DEFAULT_WIDTH);
        category.ensureDebugId(course_add_edit_view + "category");
        category.addValueChangeHandler(event -> category.removeStyleName(ERROR_FORM_STYLE));
        //Code
        numbering = new Numbering();
        numbering.ensureDebugId(course_add_edit_view + "numbering");
        numbering.addHandler(event -> numbering.removeStyleName(ERROR_FORM_STYLE), ClickEvent.getType());
        //Name
        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);
        name.ensureDebugId(course_add_edit_view + "name");
        name.addClickHandler(event -> name.removeStyleName(ERROR_FORM_STYLE));
        //Description
        description = new TextArea2(TextArea2.AREA_LENGTH_2);
        description.addStyleName(DEFAULT_WIDTH);
        description.hideCharacterLimitPanel();
        description.setWidth("100%");
        description.setHeight("200px");
        description.ensureDebugId(course_add_edit_view + "description");

        //other pre requisite
        otherPreRequisite = new TextArea2(TextArea2.AREA_LENGTH_2);
        otherPreRequisite.addStyleName(DEFAULT_WIDTH);
        otherPreRequisite.hideCharacterLimitPanel();
        otherPreRequisite.setWidth("100%");
        otherPreRequisite.setHeight("200px");
        otherPreRequisite.ensureDebugId(course_add_edit_view + "otherprerequisite");

        //Validity
        validity = new TextBox();
        validity.setPlaceHolder(wfmStrings.months());
        validity.ensureDebugId(course_add_edit_view + "validity");
        Validation.addNumericKeyboardListener(validity);
        //Duration
        duration = new TextBox();
        duration.setPlaceHolder(wfmStrings.hours());
        duration.ensureDebugId(course_add_edit_view + "duration");
        Validation.addNumericKeyboardListener(duration);
        duration.addClickHandler(event -> duration.removeStyleName(ERROR_FORM_STYLE));

        pricePerLocationStudent = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getLocationsMap(null, null, null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        }, false);

        refreshLocationDropDowns(pricePerLocationStudent, null);

        HTML location = new HTML(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()));
        HTML price = new HTML(wfmStrings.price());
        HTML stopFee = new HTML(tcStrings.stopFee());
        location.setWidth("60px");
        price.setWidth("70px");
        stopFee.setWidth("100px");
//        stopFee.getElement().getStyle().setMarginLeft(50, Style.Unit.PX);
        HorizontalPanel locationHeaderPanel = new HorizontalPanel();
        locationHeaderPanel.setSpacing(3);
        locationHeaderPanel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        locationHeaderPanel.setWidth("100%");
        locationHeaderPanel.add(location);
        locationHeaderPanel.add(price);
        locationHeaderPanel.add(stopFee);
        locationHeaderPanel.setCellHorizontalAlignment(location, HasHorizontalAlignment.ALIGN_LEFT);
        locationHeaderPanel.setCellHorizontalAlignment(price, HasHorizontalAlignment.ALIGN_LEFT);
        locationHeaderPanel.setCellHorizontalAlignment(stopFee, HasHorizontalAlignment.ALIGN_LEFT);
        VerticalPanel locationPanel = new VerticalPanel();
        locationPanel.add(locationHeaderPanel);
        locationPanel.add(pricePerLocationStudent);


        //Instructors
        instructors = new KpiCellTree();
        instructors.ensureDebugId(course_add_edit_view + "instructors");
        instructors.setSearchDefaultText(tcStrings.searchInstructors());
        instructors.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                final TextInputCell textInputCell = new TextInputCell();
                //Employee Name Blow
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {

                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };
                employee.setSortable(true);
                sortHandler.setComparator(employee, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                selectedDataGrid.addColumn(employee, wfmStrings.instructor());
                selectedDataGrid.setColumnWidth(employee, 40, Style.Unit.PCT);

                //Remove Action
                final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new SimpleLinkCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return wfmStrings.delete();
                    }
                };
                action.setFieldUpdater((index, object, value) -> {
                    List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                    contacts.remove(object);
                    object.setSelected(false);
                    selectionModel.setSelected(object, false);
                });
                selectedDataGrid.addColumn(action, wfmStrings.action());
                selectedDataGrid.setColumnWidth(action, 20, Style.Unit.PCT);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {

            }
        });
        instructors.addHandler(event -> instructors.removeStyleName(ERROR_FORM_STYLE), ClickEvent.getType());

        //Course Requirement list widget
        crList = new CustomList(Design.CHECK, true);
        crList.ensureDebugId(course_add_edit_view + "crList");
        crList.setSearchText(wfmStrings.searchCourses());
        crList.setHeight("180px");


        SimpleLink addNewSubjectLink = new SimpleLink(wfmStrings.add());
        addNewSubjectLink.setStyleName("addField");
        addNewSubjectLink.addClickHandler(event -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("courseSubject|add/add");
        });

     AdvancedInputGroup advancedInputGroup =   new AdvancedInputGroup(null, category, addNewSubjectLink, true, true);
        //Add title fields
        addTitleField(COURSE.GENERAL_DETAILS, tcStrings.courseDetails());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null) {
            addField(CustomFormConstants.SUBJECT, advancedInputGroup, getTitle(formPropertyMap.get(CustomFormConstants.SUBJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.SUBJECT).getTitle() : wfmStrings.subject(), formPropertyMap.get(CustomFormConstants.SUBJECT).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.SUBJECT).isInformation());
            advancedInputGroup.setEnabled(!formPropertyMap.get(CustomFormConstants.SUBJECT).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.SUBJECT).isInformation()) {
                new KpiToolTip(advancedInputGroup, formPropertyMap.get(CustomFormConstants.SUBJECT).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SUBJECT, advancedInputGroup, getTitle(wfmStrings.subject(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CODE) != null) {
            addField(CustomFormConstants.CODE, numbering, getTitle(formPropertyMap.get(CustomFormConstants.CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.CODE).getTitle() : wfmStrings.code(), formPropertyMap.get(CustomFormConstants.CODE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.CODE).isInformation());
            numbering.setEnabled(!formPropertyMap.get(CustomFormConstants.CODE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.CODE).isInformation()) {
                new KpiToolTip(numbering, formPropertyMap.get(CustomFormConstants.CODE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CODE, numbering, getTitle(wfmStrings.code(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, name, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(CustomFormConstants.NAME).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.NAME).isInformation());
            name.setEnabled(!formPropertyMap.get(CustomFormConstants.NAME).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.NAME).isInformation()) {
                new KpiToolTip(name, formPropertyMap.get(CustomFormConstants.NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, description, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation());
            description.setEnabled(!formPropertyMap.get(CustomFormConstants.DESCRIPTION).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation()) {
                new KpiToolTip(description, formPropertyMap.get(CustomFormConstants.DESCRIPTION).getInformationText());
            }
        } else {
            addField(CustomFormConstants.DESCRIPTION, description, getTitle(wfmStrings.description(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DURATION) != null) {
            addField(CustomFormConstants.DURATION, duration, getTitle(formPropertyMap.get(CustomFormConstants.DURATION).isChanged() ? formPropertyMap.get(CustomFormConstants.DURATION).getTitle() : wfmStrings.duration(), formPropertyMap.get(CustomFormConstants.DURATION).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.DURATION).isInformation());
            duration.setEnabled(!formPropertyMap.get(CustomFormConstants.DURATION).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.DURATION).isInformation()) {
                new KpiToolTip(duration, formPropertyMap.get(CustomFormConstants.DURATION).getInformationText());
            }
        } else {
            addField(CustomFormConstants.DURATION, duration, getTitle(wfmStrings.duration(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(COURSE.VALIDITY) != null) {
            addField(COURSE.VALIDITY, validity, getTitle(formPropertyMap.get(COURSE.VALIDITY).isChanged() ? formPropertyMap.get(COURSE.VALIDITY).getTitle() : wfmStrings.validity(), formPropertyMap.get(COURSE.VALIDITY).isRequired()),false,
                    formPropertyMap.get(COURSE.VALIDITY).isInformation());
            validity.setEnabled(!formPropertyMap.get(COURSE.VALIDITY).isDisabled());
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
            otherPreRequisite.setEnabled(!formPropertyMap.get(COURSE.VALIDITY).isDisabled());
            if (formPropertyMap.get(COURSE.OTHER_PREREQUISITE).isInformation()) {
                new KpiToolTip(otherPreRequisite, formPropertyMap.get(COURSE.OTHER_PREREQUISITE).getInformationText());
            }
        } else {
            addField(COURSE.OTHER_PREREQUISITE, otherPreRequisite, getTitle(wfmStrings.pricePerStudent(), false));
        }
//        addField(CustomFormConstants.SUBJECT, new AdvancedInputGroup(null, category, addNewSubjectLink, true, true), getTitle(wfmStrings.subject(), true));
//        addField(CustomFormConstants.CODE, numbering, getTitle(tcStrings.courseCode(), true));
//        addField(CustomFormConstants.NAME, name, getTitle(tcStrings.courseName(), true));
//        addField(CustomFormConstants.DESCRIPTION, description, wfmStrings.description());
//        addField(COURSE.DURATION, duration, getTitle(wfmStrings.duration(), true));
//        addField(COURSE.VALIDITY, validity, getTitle(wfmStrings.validity()));
//        addField(COURSE.PRICEPERSTUDENT, locationPanel, getTitle(wfmStrings.pricePerStudent(), true));
//        addField(COURSE.OTHER_PREREQUISITE, otherPreRequisite, tcStrings.otherPreRequisites());
        addField(COURSE.COURSE_REQUIREMENTS, crList, getTitle(wfmStrings.courseRequirements()));
        addField(COURSE.INSTRUCTORS, instructors, null);

        addTitleField(COURSE.INSTRUCTOR_DETAILS, wfmStrings.instructors());

        getCustomFieldUtil().drawCustomFields(this, objectID, false);

        show();
    }

    private void onAccountAdded() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_COURSE_SUBJECT, AddEditCourseView.this, (sender, args) -> {
            if (args instanceof Integer) {
                Integer courseSubjectId = (Integer) args;

                TCService.App.get().getCourseSubjectAsSelectItem(null, new AbstractAsyncCallback<TreeSelectItem[]>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        caught.printStackTrace();
                    }

                    @Override
                    public void onSuccess(TreeSelectItem[] result) {
                        category.setItems(result);
                        category.setSelected(courseSubjectId);
                    }
                });
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.COURSE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
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
                    if (result.getPreRequisite() != null) {
                        preRequisites = result.getPreRequisite();
                    }
                    if (result.getCourses() != null) {
                        preRequisites = result.getCourses();
                    }

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
                    }, false);
                    addField(COURSE.PREREQUISITE, pre_Requisite, getTitle(wfmStrings.preRequisite()));

                    fillFormWithData(result);
                });
            }
        });
    }

    protected WidgetsMap getpreRequisitesMap(SelectItem preRequisiteItem) {
        WidgetsMap widgetsMap = new WidgetsMap();

        DataListBox preRequisite = new DataListBox();
        preRequisite.ensureDebugId("course_add_edit_view_pre requisite");
        if (preRequisites != null) {
            preRequisite.setItems(preRequisites);
        }
        if (preRequisiteItem != null) {
            preRequisite.setSelected(preRequisiteItem);
        }
        widgetsMap.addToCenter(MultiTable.LIST_BOX, preRequisite);

        return widgetsMap;
    }


    protected WidgetsMap getLocationsMap(Integer locationID, BigDecimal pricePerST, BigDecimal stopFeePerST) {
        WidgetsMap widgetsMap = new WidgetsMap();
        //location list box
        DataListBox locationsBox = new DataListBox();
        locationsBox.ensureDebugId("course_add_edit_view_price_per_location");
        if (locationItems != null) {
            locationsBox.setItems(locationItems);
        }
        if (locationID != null) {
            locationsBox.setSelected(locationID);
        }

        //price text box
        final TextBox pricePerSt = new TextBox();


        pricePerSt.ensureDebugId("course_add_edit_view_price_per_student");
        Validation.checkToFocusTextBox(pricePerSt, AccountingUtils.get().formatUnitPrice(BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(pricePerSt, AccountingUtils.getUnitPriceScale());
        pricePerSt.addChangeHandler(changeEvent -> {
            String uPrice = (pricePerSt.getValue() != null && !pricePerSt.getValue().isEmpty()) ?
                    AccountingUtils.get().formatUnitPrice(new BigDecimal(pricePerSt.getValue())) :
                    AccountingUtils.get().formatUnitPrice(BigDecimal.ZERO);
            pricePerSt.setText(uPrice);
        });
        pricePerSt.setText(pricePerST != null ? pricePerST.toString() : null);

        //stopFee text box
        final TextBox stopFee = new TextBox();


        stopFee.ensureDebugId("course_add_edit_view_stop_fee_per_student");
        Validation.checkToFocusTextBox(stopFee, AccountingUtils.get().formatUnitPrice(BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(stopFee, AccountingUtils.getUnitPriceScale());
        stopFee.addChangeHandler(changeEvent -> {
            String uPrice = (stopFee.getValue() != null && !stopFee.getValue().isEmpty()) ?
                    AccountingUtils.get().formatUnitPrice(new BigDecimal(stopFee.getValue())) :
                    AccountingUtils.get().formatUnitPrice(BigDecimal.ZERO);
            stopFee.setText(uPrice);
        });
        stopFee.setText(stopFeePerST != null ? stopFeePerST.toString() : null);


        widgetsMap.addToCenter(MultiTable.LIST_BOX, locationsBox);
        widgetsMap.addToCenter(MultiTable.TEXT_BOX, pricePerSt);
        widgetsMap.addToCenter(MultiTable.TEXT_BOX, stopFee);

        return widgetsMap;
    }

    protected void fillFormWithData(final CourseItem courseItem) {

        if (courseItem.getNumberData() != null) {
            numbering.setNumberData(courseItem.getNumberData());
        }
        name.setText(courseItem.getCourseName());
        description.setText(courseItem.getDescription());
        otherPreRequisite.setText(courseItem.getOtherPreRequisites());
        duration.setText(courseItem.getDuration() != null ? courseItem.getDuration().toString() : "");
        validity.setText(courseItem.getValidity() != null ? courseItem.getValidity().toString() : "");

        if (courseItem.getPricePerLocationStudent() != null && courseItem.getPricePerLocationStudent().size() > 0) {
            pricePerLocationStudent.removeAllRows();
            for (Integer locationID : courseItem.getPricePerLocationStudent().keySet()) {
                BigDecimal pricePerST = courseItem.getPricePerLocationStudent().get(locationID);
                BigDecimal stopFeePerST = courseItem.getStopFeePerLocationStudent().get(locationID);
                pricePerLocationStudent.addWidgets(getLocationsMap(locationID, pricePerST, stopFeePerST));
            }
        }

        if (courseItem.getPreRequisite() != null) {
            pre_Requisite.removeAllRows();
            for (SelectItem item : courseItem.getPreRequisite()) {
                pre_Requisite.addWidgets(getpreRequisitesMap(item));
            }
        }
        instructors.setItems(courseItem.getInstructorMap());
        initCRWidget(courseItem.getCourseRequirementList(), courseItem.getCourseRequirements());

        getCustomFieldUtil().fillCustomFieldsWithData(courseItem.getCustomFieldItems());

        TCService.App.get().getCourseSubjectAsSelectItem(null, new AbstractAsyncCallback<TreeSelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(TreeSelectItem[] result) {
                category.setItems(result);
                category.setSelected(courseItem.getSubject());
            }
        });

    }

    @Override
    protected void addButtons() {
        if (objectID == null) {
            addButton(wfmStrings.saveAndNew(), event -> save(true));
        }
        addButton(wfmStrings.save(), event -> save(false));
    }


    protected void refreshLocationDropDowns(final MultiTableNewUI locationTable, Integer locationID) {
        ReportService.App.get().getLocationList(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] selectItems) {
                if (selectItems != null) {
                    locationItems = selectItems;
                    for (Map<String, Widget> row : locationTable.getWidgets()) {
                        if (row != null) {
                            DataListBox db = (DataListBox) row.get(MultiTable.LIST_BOX);
                            if (db != null) {
                                db.setItems(selectItems);
                            }
                        }
                    }
                }
            }
        });
    }

    private void save(final boolean saveAndNew) {
        if (!validate()) {
            return;
        }
        enableButton(false);
        CourseItem courseItem = new CourseItem();
        courseItem.setObjectID(objectID);
        courseItem.setSubject(category.getSelectedItem());
        courseItem.setNumberData(numbering.getNumberData(false));
        courseItem.setCourseName(name.getText());
        courseItem.setDescription(description.getText());
        courseItem.setOtherPreRequisites(otherPreRequisite.getText());

        if (duration.getText() != null && !"".equals(duration.getText())) {
            courseItem.setDuration(Integer.valueOf(duration.getText()));
        }
        if (validity.getText() != null && !"".equals(validity.getText())) {
            courseItem.setValidity(Integer.valueOf(validity.getText()));
        }

        HashMap<Integer, BigDecimal> locationWithPrices = new HashMap<>();
        HashMap<Integer, BigDecimal> locationWithStopFees = new HashMap<>();
        for (Map<String, Widget> row : pricePerLocationStudent.getWidgets()) {
            if (row != null) {
                DataListBox db = (DataListBox) row.get(MultiTable.LIST_BOX);
                TextBox priceBox = (TextBox) row.get(MultiTable.TEXT_BOX);
                TextBox stopFeeBox = (TextBox) row.get(MultiTable.TEXT_BOX);

                if (db != null && db.getSelectedItem() != null && db.getSelectedItem().getId() != null &&
                        priceBox.getText() != null && !"".equals(priceBox.getText()) && stopFeeBox.getText() != null && !"".equals(stopFeeBox.getText())) {

                    if (!locationWithPrices.containsKey(db.getSelectedItem().getId())) {
                        locationWithPrices.put(db.getSelectedItem().getId(), BigDecimal.valueOf(Double.parseDouble(priceBox.getText().replace(",", ""))));
                        locationWithStopFees.put(db.getSelectedItem().getId(), BigDecimal.valueOf(Double.parseDouble(stopFeeBox.getText().replace(",", ""))));
                    }
                }
            }
        }
        courseItem.setPricePerLocationStudent(locationWithPrices);
        courseItem.setStopFeePerLocationStudent(locationWithStopFees);

        ArrayList<SelectItem> items = new ArrayList<>();
        for (Map<String, Widget> row : pre_Requisite.getWidgets()) {
            if (row != null && row.size() > 1) {
                DataListBox db = (DataListBox) row.get(MultiTable.LIST_BOX);
                items.add(new SelectItem(db.getSelectedItem().getId(), db.getSelectedItem().getName()));
            }
        }
        courseItem.setPreRequisite(items.toArray(new SelectItem[]{}));

        courseItem.setInstructors(getSelectedInstructors());

        if (crList.getItems() != null && crList.getItems().size() > 0) {
            List<SelectItem> appliedClients = new ArrayList<>();
            for (CustomListItem client : crList.getItems()) {
                if (client.getValue()) {
                    appliedClients.add(client.getItem());
                }
            }
            courseItem.setCourseRequirements(appliedClients.toArray(new SelectItem[]{}));
        }
        courseItem.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        LoadingPanel.loading(true);
        tcService.saveCourse(courseItem, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.course()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COURSE_ADD_EDIT, result, AddEditCourseView.this);
                if (!saveAndNew) {
                    closeTab();
                    if (objectID != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("course|summary/" + objectID);
                    }
                } else {
                    closeTab("course|add/add");
                }
            }
        });

    }

    private ArrayList<SelectItem> getSelectedInstructors() {
        ArrayList<SelectItem> items = new ArrayList<>();
        SelectItem selectItem;
        if (instructors.getSelectedData() != null && instructors.getSelectedData().size() > 0) {
            for (KpiTreeInfo save : instructors.getSelectedData()) {
                selectItem = new SelectItem(save.getId(), save.getName());
                items.add(selectItem);
            }
        }
        return items;
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();
        errors += markAsError(CustomFormConstants.SUBJECT, category, category.getSelectedItem() == null);
        errors += markAsError(CustomFormConstants.CODE, numbering, !Validation.validateTextBoxRequired(numbering.getTxtPrefix()));
        errors += markAsError(CustomFormConstants.NAME, name, name.getText() == null || "".equals(name.getText()));

        if (formPropertyMap != null && formPropertyMap.get(COURSE.DURATION) != null && formPropertyMap.get(COURSE.DURATION).isRequired()) {
            errors += markAsError(COURSE.DURATION, duration, !Validation.validateTextBoxRequired(duration));
        }
        if (formPropertyMap != null && formPropertyMap.get(COURSE.VALIDITY) != null && formPropertyMap.get(COURSE.OTHER_PREREQUISITE).isRequired()) {
            errors += markAsError(COURSE.OTHER_PREREQUISITE, otherPreRequisite, otherPreRequisite.getText() == null || "".equals(otherPreRequisite.getText()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()) {
            errors += markAsError(CustomFormConstants.DESCRIPTION, description, !Validation.validateTextAreaRequired(description));
        }
        if (formPropertyMap != null && formPropertyMap.get(COURSE.VALIDITY) != null && formPropertyMap.get(COURSE.VALIDITY).isRequired()) {
            errors += markAsError(COURSE.VALIDITY, validity, !Validation.validateTextBoxRequired(validity));
        }

        errors += getCustomFieldUtil().validateCustomFields();

        if (pricePerLocationStudent.isEmpty()) {
            errors += markAsError(COURSE.PRICEPERSTUDENT, pricePerLocationStudent, true);
        } else if (pricePerLocationStudent.getWidgets() != null && pricePerLocationStudent.getWidgets().size() > 0) {
            for (Map<String, Widget> widgetMap : pricePerLocationStudent.getWidgets()) {
                if (widgetMap != null) {
                    DataListBox locationB = (DataListBox) widgetMap.get(MultiTable.LIST_BOX);
                    TextBox priceB = (TextBox) widgetMap.get(MultiTable.TEXT_BOX);
                    TextBox stopFeeB = (TextBox) widgetMap.get(MultiTable.TEXT_BOX);
                    if (!Validation.validateListBoxRequired(locationB, new HTML(), "")) {
                        errors++;
                    }
                    if (!Validation.validateTextBoxRequired(priceB)) {
                        errors++;
                    }
                    if (!Validation.validateTextBoxRequired(stopFeeB)) {
                        errors++;
                    }
                }
            }
        }


        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void resetValues() {
        tcService.getCourseItem(null, new AbstractAsyncCallback<CourseItem>() {
            @Override
            public void success(CourseItem courseItem) {
                numbering.setNumberData(courseItem.getNumberData());
                if (courseItem.getInstructorMap() != null && !courseItem.getInstructorMap().isEmpty()) {
                    instructors.setItems(courseItem.getInstructorMap());
                }
            }
        });

        category.clearSelected();
        name.setText(null);
        description.setText(null);
        aliasName.setText(null);
        validity.setText(null);
        duration.setText(null);
        pricePerLocationStudent.removeAllRows();
        pre_Requisite.removeAllRows();
        accountLookUp = new AccountsLookUp(RECEIVABLE);
        accountLookUp.addStyleName(DEFAULT_WIDTH);
        examRequired.setValue(true);
        opito.setValue(false);
        medClearance.setValue(false);
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

    public class MultipleDropdown extends VerticalPanel {
        private final TreeSelectIcons icons = GWT.create(TreeSelectIcons.class);
        private VerticalPanel mainPanel;
        private VerticalPanel coursePanel;
        private HorizontalPanel coursesPanel;
        private DataListBox dataListBox;
        private SimpleLink addLink;
        private Image removeLink;
        private int count = 0;

        public MultipleDropdown() {
            initialize();
        }

        private VerticalPanel initialize() {
            mainPanel = new VerticalPanel();
            coursePanel = new VerticalPanel();
            addLink = new SimpleLink(wfmStrings.add(), null, "", null, "addField");
            addLink.addClickHandler(event -> addDropDown(true));
            addDropDown(false);
            mainPanel.add(coursePanel);
            mainPanel.add(addLink);
            mainPanel.setCellHorizontalAlignment(addLink, HasHorizontalAlignment.ALIGN_RIGHT);
            return mainPanel;
        }

        private VerticalPanel getPanel() {
            return mainPanel;
        }

        private void clearAndAddDropdown() {
            coursePanel.clear();
            addDropDown1(false);
        }

        private void addDropDown(boolean addItems) {
            if (count > 0) {
                DataListBox first = (DataListBox) ((HorizontalPanel) coursePanel.getWidget(0)).getWidget(0);
                if (first.getItems() != null && first.getItems().length > 0 && count < first.getItems().length) {
                    addDropDown1(addItems);
                }
            } else {
                addDropDown1(addItems);
            }
        }

        private void addDropDown1(boolean addItems) {
            dataListBox = new DataListBox();
            dataListBox.setWidth("197px");
            if (addItems) {
                DataListBox listBox = (DataListBox) ((HorizontalPanel) coursePanel.getWidget(0)).getWidget(0);
                dataListBox.setItems(listBox.getItems());
            } else if (preRequisites != null && preRequisites.length > 0) {
                dataListBox.setItems(preRequisites);
            }

            removeLink = new Image(icons.removeIcon());
            removeLink.getElement().getStyle().setMarginLeft(5d, Style.Unit.PX);
            removeLink.getElement().getStyle().setMarginTop(3d, Style.Unit.PX);
            removeLink.addClickHandler(event -> {
                if (count > 1) {
                    coursePanel.remove(((Image) event.getSource()).getParent());
                    --count;
                }
            });
            coursesPanel = new HorizontalPanel();
            coursesPanel.getElement().getStyle().setMarginTop(3d, Style.Unit.PX);
            coursesPanel.add(dataListBox);
            coursesPanel.add(removeLink);
            coursesPanel.setCellHorizontalAlignment(removeLink, HasHorizontalAlignment.ALIGN_RIGHT);
            coursePanel.add(coursesPanel);
            ++count;
        }

        private void setItems(SelectItem[] items) {
            for (int i = 0; i < count; i++) {
                DataListBox listBox = (DataListBox) ((HorizontalPanel) coursePanel.getWidget(i)).getWidget(0);
                listBox.setItems(items);
            }
        }

        private ArrayList<SelectItem> getSelectedCourses() {
            HashMap<SelectItem, SelectItem> result = new HashMap<>();
            for (int i = 0; i < count; i++) {
                DataListBox listBox = (DataListBox) ((HorizontalPanel) coursePanel.getWidget(i)).getWidget(0);
                if (listBox.getSelectedItem() != null) {
                    result.put(listBox.getSelectedItem(), listBox.getSelectedItem());
                }
            }
            ArrayList<SelectItem> res = new ArrayList<>();
            res.addAll(result.keySet());
            return res;
        }

        public void setSelecteds(SelectItem[] preRequisite) {
            for (int i = 0; i < preRequisite.length; i++) {
                if (i != 0) {
                    addDropDown(true);
                }

                DataListBox listBox = (DataListBox) ((HorizontalPanel) coursePanel.getWidget(i)).getWidget(0);
                listBox.setSelected(preRequisite[i]);
            }
        }
    }

    /**
     * Initialize Course Requirements widget
     *
     * @param crs         - All Course Requirements List
     * @param selectedCRs - already Selected Course Requirement List
     */
    private void initCRWidget(SelectItem[] crs, SelectItem[] selectedCRs) {
        if (crList.getItems() != null) {
            crList.removeItems();
        }

        if (crs != null) {
            for (SelectItem cr : crs) {
                CustomListItem item = new CustomListItem(cr);
                crList.add(item);

                if (selectedCRs != null) {
                    for (SelectItem selectedCR : selectedCRs) {
                        if (selectedCR.getId().equals(cr.getId())) {
                            item.setCheck(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }
}
