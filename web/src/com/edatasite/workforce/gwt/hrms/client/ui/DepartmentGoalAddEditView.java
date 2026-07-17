package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.IconCell;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DepartmentGoalAddEditView extends CustomForm2 implements FormHasCustomFieldInterface, Constants, Colapse {

    public String formView = "department_goal_add_edit_view_";
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    protected GoalItem item;
    protected Integer objectId;
    protected Integer departmentId;
    protected Integer locationId;
    protected Double totalActual = 0d;
    protected GeneralFileUpload attachment;

    private InputGroup targetGoalWidget, weightWidget;
    private TextBox title, avialableWeight, actualWeight, targetGoal, actualGoal;

    private boolean isActualWeight100 = false;

    private DatePicker startDate, endDate;

    private DepartmentLookUp department;
    private LocationLookUpWithCode location;
    private Div locationContainer, departmentContainer;

    private TextArea2 description;

    private DataListBox resolver;
    private KpiCellTree employeeSelector;
    private LinkedHashMap<String, FormProperty> fp;
    private final LinkedHashMap<KpiTreeInfo, GoalAssigneeItem> goalAssignees = new LinkedHashMap<>();
    private KpiDataGrid<KpiTreeInfo> selectedDataGrid;
    private final Set<Object> invalidTargetKeys = new HashSet<>();
    private final LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assignees = new LinkedHashMap<>();
    private FormHasCustomField customFieldUtil;

    public DepartmentGoalAddEditView(String viewName, String description) {
        super(viewName, description);
    }

    public DepartmentGoalAddEditView(Integer objectId) {
        super(DEPARTMENT_GOAL);
        this.objectId = objectId;
    }

    public DepartmentGoalAddEditView(Integer objectId, Integer departmentId, Integer locationId) {
        super(DEPARTMENT_GOAL);
        this.objectId = objectId;
        this.departmentId = departmentId;
        this.locationId = locationId;
    }


    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.DepartmentGoal, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                fp = result.getFormPropertyMap();
                initialize();
            }
        });
        return null;
    }


    public void initialize() {
        LoadingPanel.loading(true);

        title = new TextBox();
        title.addStyleName(DEFAULT_WIDTH);
        title.getElement().setId(formView + "title");

        startDate = new DatePicker(true);
        startDate.getElement().setId(formView + "startDate");
        startDate.addChangeHandler(handler -> {
            startDate.removeStyleName(ERROR_FORM_STYLE);
            endDate.removeStyleName(ERROR_FORM_STYLE);
        });

        endDate = new DatePicker(true);
        endDate.getElement().setId(formView + "toDate");
        endDate.addChangeHandler(handler -> {
            startDate.removeStyleName(ERROR_FORM_STYLE);
            endDate.removeStyleName(ERROR_FORM_STYLE);
        });

        targetGoal = new TextBox();
        Validation.addNumericKeyboardListener(targetGoal);
        targetGoal.getElement().setId(formView + "targetGoal");
        targetGoal.setPlaceHolder("0");

        actualGoal = new TextBox();
        actualGoal.getElement().setId(formView + "actualGoal");
        actualGoal.setReadOnly(true);
        actualGoal.setEnabled(false);
        actualGoal.setValue("0");
        targetGoalWidget = new InputGroup(targetGoal, actualGoal);


        avialableWeight = new TextBox();
        avialableWeight.getElement().setId(formView + "avialableWeight");
        avialableWeight.setReadOnly(true);
        avialableWeight.setEnabled(false);
        avialableWeight.setValue("100");

        actualWeight = new TextBox();
        Validation.addNumericKeyboardListener(actualWeight);
        actualWeight.getElement().setId(formView + "actualWeight");
        actualWeight.setPlaceHolder("0");

        actualWeight.addKeyUpHandler(event -> {
            TextBox textbox = (TextBox) event.getSource();
            String enteredText = textbox.getValue();
            String availableText = avialableWeight.getValue();

            if (enteredText.isEmpty()) return;

            try {
                if (!isActualWeight100) {
                    int enteredVal = Integer.parseInt(enteredText);
                    int maxAvailable = Integer.parseInt(availableText.isEmpty() ? "0" : availableText);

                    if (enteredVal > maxAvailable) {
                        textbox.setValue(String.valueOf(maxAvailable));
                        textbox.setCursorPos(textbox.getValue().length());
                    }
                }
            } catch (NumberFormatException e) {
                textbox.setValue("");
            }

        });


        weightWidget = new InputGroup(avialableWeight, actualWeight);

        locationContainer = new Div();
        departmentContainer = new Div();

        department = new DepartmentLookUp();
        department.addStyleName(DEFAULT_WIDTH);
        department.ensureDebugId(formView + "department");
        department.setEnabled(false);


        location = new LocationLookUpWithCode();
        location.addStyleName(DEFAULT_WIDTH);
        location.ensureDebugId(formView + "location");

        if (objectId != null) {
            department.setEnabled(false);
            location.setEnabled(false);
        }

        if (objectId == null) {
            location.getSuggestBox().addSelectionHandler(e -> {
                Integer selectedItemID = this.location.getSelectedItemID();

                department.getSuggestBox().setValue(null);
                department.clear();
                department.getFilterParametrs().setLocationId(selectedItemID);
                department.setEnabled(true);
                employeeSelector.clear();
                resolver.clear();
                resolver.clearSelected();
                resolver.setWithoutNullLabel(null);

            });

            department.getSuggestBox().addSelectionHandler(d -> {
                departmentSelectionHandlerActions();
            });
        }

        departmentContainer.add(department);
        locationContainer.add(location);

        resolver = new DataListBox();
        resolver.addStyleName(DEFAULT_WIDTH);
        resolver.getElement().setId(formView + "resolver");
        resolver.setWithoutNullLabel(null);

        employeeSelector = new KpiCellTree();
        employeeSelector.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                DepartmentGoalAddEditView.this.selectedDataGrid = selectedDataGrid;

                // Disable GWT's built-in keyboard selection. While enabled, the first
                // click on a target input is intercepted to "keyboard-select" the row,
                // which re-renders it and drops the input's focus — so the user had to
                // click twice before they could type. Disabled, the click reaches the
                // input directly.
                selectedDataGrid.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);

                // The target inputs render with tabindex="-1", so Tab would otherwise
                // jump straight out of the grid, and GWT's arrow handler only fires
                // while a cell is NOT being edited. Catch the keydown at the grid root
                // (this reliably fires even when the keydown originates on a focused
                // <input>, unlike cell-preview events) and move focus to the target
                // input of the adjacent row: Tab / ArrowDown (forward) and Shift+Tab /
                // ArrowUp (backward). Rows are found by walking the live DOM rather than
                // getRowElement(index), which is unreliable over a DataGrid's nested body.
                selectedDataGrid.addDomHandler(event -> {
                    NativeEvent nativeEvent = event.getNativeEvent();
                    int keyCode = nativeEvent.getKeyCode();
                    boolean forward = (keyCode == KeyCodes.KEY_TAB && !nativeEvent.getShiftKey())
                            || keyCode == KeyCodes.KEY_DOWN;
                    boolean backward = (keyCode == KeyCodes.KEY_TAB && nativeEvent.getShiftKey())
                            || keyCode == KeyCodes.KEY_UP;
                    if (!forward && !backward) {
                        return;
                    }
                    Element source = nativeEvent.getEventTarget().cast();
                    Element currentRow = getParentTableRow(source);
                    if (currentRow == null) {
                        return;
                    }
                    Element destRow = backward
                            ? currentRow.getPreviousSiblingElement()
                            : currentRow.getNextSiblingElement();
                    if (destRow == null) {
                        return; // first/last row — let the browser handle it
                    }
                    InputElement input = findFirstInput(destRow);
                    if (input == null) {
                        return;
                    }
                    // Drop any red "required target" border on the input being left,
                    // since it now holds a committed value.
                    if (source != null && InputElement.TAG.equalsIgnoreCase(source.getTagName())) {
                        source.removeAttribute("style");
                    }
                    nativeEvent.preventDefault();
                    nativeEvent.stopPropagation();
                    input.focus();
                }, KeyDownEvent.getType());

                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {

                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };
                employee.setSortable(true);

                sortHandler.setComparator(employee, Comparator.comparing(KpiTreeInfo::getName));
                selectedDataGrid.addColumn(employee, wfmStrings.employee());
                selectedDataGrid.setColumnWidth(employee, 120, Style.Unit.PX);

                // Custom cell so the "required target" error highlight lands ON the input
                // (a red border) rather than on the surrounding cell container.
                final TextInputCell targetCell = new TextInputCell() {
                    @Override
                    public void render(com.google.gwt.cell.client.Cell.Context context, String value, com.google.gwt.safehtml.shared.SafeHtmlBuilder sb) {
                        Object key = context.getKey();
                        com.google.gwt.cell.client.TextInputCell.ViewData viewData = getViewData(key);
                        String s = (viewData != null) ? viewData.getCurrentValue() : value;
                        if (s == null) {
                            s = "";
                        }
                        String styleAttr = invalidTargetKeys.contains(key)
                                ? " style=\"border:1px solid #e53935;background:#fff5f5;\""
                                : "";
                        sb.appendHtmlConstant("<input type=\"text\" tabindex=\"-1\"" + styleAttr + " value=\"");
                        sb.appendEscaped(s);
                        sb.appendHtmlConstant("\"></input>");
                    }
                };
                Column<KpiTreeInfo, String> target = new Column<KpiTreeInfo, String>(targetCell) {

                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        GoalAssigneeItem item = goalAssignees.get(object);
                        return item.getTarget() > 0 ? String.valueOf(item.getTarget()) : "";
                    }
                };

                target.setFieldUpdater((index, object, value) -> {
                    GoalAssigneeItem item = goalAssignees.get(object);

                    try {
                        item.setTarget(Double.valueOf(value));
                    } catch (NumberFormatException e) {
                        item.setTarget(0d);
                        targetCell.clearViewData(KpiTreeInfo.KEY_PROVIDER.getKey(object));
                    }
                    invalidTargetKeys.remove(KpiTreeInfo.KEY_PROVIDER.getKey(object));
                    // Refresh only this cell in place instead of redrawing the whole grid.
                    // A full redraw re-renders every row, which would replace the input
                    // the user is tabbing into and steal focus mid-navigation.
                    refreshTargetCellDom(index, item);
                });
                selectedDataGrid.addColumn(target, wfmStrings.target());
                selectedDataGrid.setColumnWidth(target, 90, Style.Unit.PX);

                Column<KpiTreeInfo, String> actual = new Column<KpiTreeInfo, String>(new TextCell()) {

                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        GoalAssigneeItem item = goalAssignees.get(object);
                        return item.getActual() > 0 ? String.valueOf(item.getActual()) : "0";
                    }
                };

                actual.setFieldUpdater((index, object, value) -> {
                    GoalAssigneeItem item = goalAssignees.get(object);
                    try {
                        item.setActual(Double.valueOf(value));
                    } catch (NumberFormatException ignore) {
                        item.setActual(0d);
                    }
                    selectedDataGrid.redraw();
                });
                selectedDataGrid.addColumn(actual, wfmStrings.actual());
                selectedDataGrid.setColumnWidth(actual, 90, Style.Unit.PX);


                final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new IconCell("ficon--trash pointer")) {

                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return null;
                    }
                };
                action.setFieldUpdater((index, object, value) -> {

                    GoalAssigneeItem item = goalAssignees.get(object);
                    if (item.getActual() > 0) {
                        Info.warn(wfmStrings.youCannotDeleteThisItem());
                        return;
                    }

                    List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                    if (contacts.size() > 1) {
                        contacts.remove(object);
                        object.setSelected(false);
                        selectionModel.setSelected(object, false);
                    } else {
                        Info.warn(wfmStrings.youMustHaveAtLeast1LineItem());
                    }
                });
                selectedDataGrid.addColumn(action, "");
                action.setCellStyleNames("center");
                selectedDataGrid.setColumnWidth(action, 70, Style.Unit.PX);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
            }

        });

        attachment = new GeneralFileUpload(F_DEP_GOAL, objectId, objectId);
        attachment.getPanel().getElement().setId(formView + "attachment");

        LoadingPanel.loading(true);
        getCustomFieldUtil().drawCustomFields(this, objectId);
        addFieldsToForm();
    }

    public void addFieldsToForm() {

        addTitleField(CustomFormConstants.GOAL_DETAILS, wfmStrings.basicDetails());
        addTitleField(CustomFormConstants.ASSIGNEES, getTitle(wfmStrings.assignees(), true));
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        addTitleField(CustomFormConstants.NOTES, wfmStrings.notes());
        addTitleField(CustomFormConstants.ATTACHMENTS_TITLE, wfmStrings.attachments());


        if (fp != null && fp.get(CustomFormConstants.GOAL_TITLE) != null) {
            addField(GOAL_TITLE, title, getTitle(fp.get(CustomFormConstants.GOAL_TITLE).isChanged() ? fp.get(CustomFormConstants.GOAL_TITLE).getTitle() : wfmStrings.title(),
                            fp.get(CustomFormConstants.GOAL_TITLE).isRequired()), false,
                    fp.get(CustomFormConstants.GOAL_TITLE).isInformation());
            title.setEnabled(!fp.get(CustomFormConstants.GOAL_TITLE).isDisabled());
            if (fp.get(CustomFormConstants.GOAL_TITLE).isInformation()) {
                new KpiToolTip(title, fp.get(CustomFormConstants.GOAL_TITLE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.GOAL_TITLE, title, getTitle(wfmStrings.title(), true));
        }

        if (fp != null && fp.get(CustomFormConstants.GOAL_DESCRIPTION) != null) {
            description = new TextArea2(1000, fp.get(CustomFormConstants.GOAL_DESCRIPTION).isChanged() ? fp.get(CustomFormConstants.GOAL_DESCRIPTION).getTitle() : wfmStrings.description());
            description.setEnabled(!fp.get(CustomFormConstants.GOAL_DESCRIPTION).isDisabled());
        } else {
            description = new TextArea2(1000, wfmStrings.description());
        }
        description.getTextArea().getElement().setId(formView + "description");
        description.setHeight(172);

        addField(CustomFormConstants.GOAL_DESCRIPTION, description, null);


        GRow gRow = new GRow(new GColumn(GColumnEnum.COL_6, startDate), new GColumn(GColumnEnum.COL_6, endDate));
        if (fp != null && fp.get(CustomFormConstants.GOAL_START_DATE) != null) {
            addField(GOAL_START_DATE, gRow, getTitle(fp.get(CustomFormConstants.GOAL_START_DATE).isChanged() ? fp.get(CustomFormConstants.GOAL_START_DATE).getTitle() : wfmStrings.period(),
                            fp.get(CustomFormConstants.GOAL_START_DATE).isRequired()), false,
                    fp.get(CustomFormConstants.GOAL_START_DATE).isInformation());
            if (fp.get(CustomFormConstants.GOAL_START_DATE).isInformation()) {
                new KpiToolTip(gRow, fp.get(CustomFormConstants.GOAL_START_DATE).getInformationText());
            }
            startDate.setEnabled(!fp.get(CustomFormConstants.GOAL_START_DATE).isDisabled());
            endDate.setEnabled(!fp.get(CustomFormConstants.GOAL_START_DATE).isDisabled());
        } else {
            addField(CustomFormConstants.GOAL_START_DATE, gRow, getTitle(wfmStrings.period(), true));
        }

        if (fp != null && fp.get(CustomFormConstants.GOAL_PROORDEP) != null) {
            addField(GOAL_PROORDEP, departmentContainer, getTitle(fp.get(CustomFormConstants.GOAL_PROORDEP).isChanged() ? fp.get(CustomFormConstants.GOAL_PROORDEP).getTitle() : wfmStrings.department(),
                            fp.get(CustomFormConstants.GOAL_PROORDEP).isRequired()), false,
                    fp.get(CustomFormConstants.GOAL_PROORDEP).isInformation());
            if (fp.get(CustomFormConstants.GOAL_PROORDEP).isInformation()) {
                new KpiToolTip(departmentContainer, fp.get(CustomFormConstants.GOAL_PROORDEP).getInformationText());
            }
        } else {
            addField(CustomFormConstants.GOAL_PROORDEP, departmentContainer, getTitle(wfmStrings.department(), true));
        }

        if (fp != null && fp.get(CustomFormConstants.GOAL_RESOLVER) != null) {
            addField(GOAL_RESOLVER, resolver, getTitle(fp.get(CustomFormConstants.GOAL_RESOLVER).isChanged() ? fp.get(CustomFormConstants.GOAL_RESOLVER).getTitle() : wfmStrings.manager(),
                            fp.get(CustomFormConstants.GOAL_RESOLVER).isRequired()), false,
                    fp.get(CustomFormConstants.GOAL_RESOLVER).isInformation());
            if (fp.get(CustomFormConstants.GOAL_RESOLVER).isInformation()) {
                new KpiToolTip(resolver, fp.get(CustomFormConstants.GOAL_RESOLVER).getInformationText());
            }
            resolver.setEnabled(!fp.get(CustomFormConstants.GOAL_RESOLVER).isDisabled());
        } else {
            addField(CustomFormConstants.GOAL_RESOLVER, resolver, getTitle(wfmStrings.manager()));
        }


        if (fp != null && fp.get(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET) != null) {
            addField(DEPARTMENT_TARGET_GOAL_WIDGET, targetGoalWidget, getTitle(fp.get(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET).isChanged() ? fp.get(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET).getTitle() : wfmStrings.targetAndActual(), fp.get(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET).isRequired()),
                    false, fp.get(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET).isInformation());
            if (fp.get(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET).isInformation()) {
                new KpiToolTip(targetGoalWidget, fp.get(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET).getInformationText());
            }
        } else {
            addField(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET, targetGoalWidget, getTitle(wfmStrings.targetAndActual()));
        }

        if (fp != null && fp.get(CustomFormConstants.DEPARTMENT_GOAL_WEIGHT_WIDGET) != null) {
            addField(DEPARTMENT_GOAL_WEIGHT_WIDGET, weightWidget, getTitle(fp.get(CustomFormConstants.DEPARTMENT_GOAL_WEIGHT_WIDGET).isChanged() ? fp.get(CustomFormConstants.DEPARTMENT_GOAL_WEIGHT_WIDGET).getTitle() : wfmStrings.weight(), fp.get(CustomFormConstants.DEPARTMENT_GOAL_WEIGHT_WIDGET).isRequired()),
                    false, fp.get(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET).isInformation());
            if (fp.get(CustomFormConstants.DEPARTMENT_GOAL_WEIGHT_WIDGET).isInformation()) {
                new KpiToolTip(weightWidget, fp.get(CustomFormConstants.DEPARTMENT_GOAL_WEIGHT_WIDGET).getInformationText());
            }
        } else {
            addField(CustomFormConstants.DEPARTMENT_GOAL_WEIGHT_WIDGET, targetGoalWidget, getTitle(wfmStrings.weight()));
        }

        if (fp != null && fp.get(CustomFormConstants.LOCATIONS) != null) {
            addField(LOCATIONS, locationContainer, getTitle(fp.get(CustomFormConstants.LOCATIONS).isChanged() ? fp.get(CustomFormConstants.LOCATIONS).getTitle() : wfmStrings.location(), fp.get(CustomFormConstants.LOCATIONS).isRequired()),
                    false, fp.get(CustomFormConstants.LOCATIONS).isInformation());
            if (fp.get(CustomFormConstants.LOCATIONS).isInformation()) {
                new KpiToolTip(locationContainer, fp.get(CustomFormConstants.LOCATIONS).getInformationText());
            }
        } else {
            addField(CustomFormConstants.LOCATIONS, locationContainer, getTitle(wfmStrings.location()));
        }

        addField(CustomFormConstants.GOAL_ASSIGNEES, employeeSelector, getTitle(wfmStrings.assignees(), true));
        addField(CustomFormConstants.ATTACHMENTS, attachment, null);


        show();
    }

    @Override
    protected void getDataToFillFields() {
        HrmsService.App.get().editGoal(objectId, DEPARTMENT_GOAL, new AbstractAsyncCallback<GoalItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                LoadingPanel.loading(false);
            }

            public void success(final GoalItem data) {
                LoadingPanel.loading(false);
                item = data;
                LoadingPanel.loading(false);
                fillFieldWithValue();
                if (objectId != null) {
                    getEmployees();
                }

            }
        });
    }

    public void fillFieldWithValue() {

        title.setText(item.getTitle());

        description.setText(item.getDescription());


        if (item.getFromDate() != null)
            startDate.setDate(item.getFromDate().getNonConvertedDate());

        if (item.getToDate() != null)
            endDate.setDate(item.getToDate().getNonConvertedDate());


        if (item.getGoalAssigneeItem() != null) {
            for (GoalAssigneeItem item : item.getGoalAssigneeItem()) {
                totalActual += item.getActual();
            }
            actualGoal.setValue(String.valueOf(totalActual));
        }

        if (item.getTargetGoal() != null) {
            targetGoal.setValue(String.valueOf(item.getTargetGoal()));
        }

        department.setItems(Utils.sortSelectItemByName(item.getDepartments()));

        if (item.getDepartmentId() != null) {
            department.setSelected(new SelectItem(item.getDepartmentId(), item.getDepartment()));
        } else if (departmentId != null) {
            department.setSelected(departmentId);
        }

        if (item.getLocationId() != null) {
            location.setSelected(new SelectItem(item.getLocationId(), item.getLocation()));
        } else if (locationId != null) {
            location.setItems(item.getLocations());
            location.setEnabled(false);
            location.setSelected(locationId);
        }

        if (department.getSelectedItemID() != null) {

            EmployeeService.App.get().getEmployeesAaSelectItemsByDepartmentId(department.getSelectedItemID(), new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void success(SelectItem[] result) {
                    resolver.setItems(result);
                    if (item.getResolverId() != null) {
                        resolver.setSelected(item.getResolverId());
                    }
                }
            });

            avialableWeight.setText(String.valueOf(item.getAvialableWeight()));
            actualWeight.setText(String.valueOf(item.getDepartmentGoalWeight()));
            if ("0".equals(avialableWeight.getValue())) {
                if ("100".equals(actualWeight.getValue())) {
                    isActualWeight100 = true;
                } else {
                    actualWeight.setEnabled(false);
                }
            }
        }

        if (this.departmentId != null) {
            departmentSelectionHandlerActions();
        }

        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields());
    }

    public GoalItem setValuesToRPC(GoalItem item) {
        if (objectId != null) {
            item.setObjectId(objectId);
        }

        item.setTitle(title.getText());

        item.setDescription(description.getText());


        item.setFromDate(new DateNonConvertable(startDate.getDate()));
        item.setToDate(new DateNonConvertable(endDate.getDate()));

        try {
            item.setTargetGoal(Integer.parseInt(targetGoal.getValue()));
        } catch (NumberFormatException e) {
            item.setTargetGoal(0);
        }

        try {
            item.setDepartmentGoalWeight(Integer.parseInt(actualWeight.getValue()));
        } catch (NumberFormatException e) {
            item.setDepartmentGoalWeight(0);
        }

        item.setResolverId(null);
        if (resolver.getSelectedItem() != null) {
            item.setResolverId(resolver.getSelectedItem().getId());
        }

        if (location.getSelectedItemID() != null) {
            item.setLocationId(location.getSelectedItemID());
            item.setLocation(location.getValue());
        }

        if (department.getSelectedItem() != null) {
            item.setDepartmentId(department.getSelectedItem().getId());
            item.setGoalCategoryId(446); // Reference Department Goal

        }

        item.setAttachments(attachment.getAttachedFiles());

        GoalAssigneeItem[] assigneeItems = new GoalAssigneeItem[employeeSelector.getSelectedData().size()];
        int i = 0;
        for (KpiTreeInfo info : employeeSelector.getSelectedData()) {
            assigneeItems[i++] = goalAssignees.get(info);
        }

        item.setGoalAssigneeItem(assigneeItems);

        return item;
    }

    private void save() {
        if (!validateFields()) return;

        LoadingPanel.loading(true);
        enableButton(false);
        item = setValuesToRPC(item);
        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
        HrmsService.App.get().saveGoal(item, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer o) {
                LoadingPanel.loading(false);
                enableButton(true);
                if (o == -1) {
                    Info.show(hrmsStrings.goalWithThisCodeAlreadyExists(), Info.Type.WARNING);
                } else if (o == -2) {
                    Info.show(hrmsStrings.goalPeriodCannotBeNarrowed(), Info.Type.WARNING);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.goal()), Info.Type.INFO);
                    closeTab();
                    if (objectId != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("goal|summary/" + objectId + "/" + DEPARTMENT_GOAL, item.getTitle());
                    }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GOAL_ADD, o, DepartmentGoalAddEditView.this);
                }
            }
        });
    }

    private void departmentSelectionHandlerActions() {
        employeeSelector.clear();
        resolver.clear();
        resolver.clearSelected();
        actualWeight.setValue(null);
        EmployeeService.App.get().getEmployeesAaSelectItemsByDepartmentId(department.getSelectedItemID(), new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void success(SelectItem[] result) {
                resolver.setItems(result);
                resolver.setSelected(result[0]);
            }
        });

        HrmsService.App.get().departmentGoalAvialableWeight(department.getSelectedItemID(), new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void success(Integer weight) {
                avialableWeight.setValue(String.valueOf(weight));
            }
        });

        getEmployees();


    }

    private void getEmployees() {
        ListingFilterParameter parameter = new ListingFilterParameter();
        if (department != null && department.getSelectedItem() != null) {
            parameter.setDepartmentId(department.getSelectedItem().getId());
        }

        parameter.setResignedEmployeesIncluded(false);
        parameter.setObjectId(objectId);
        HrmsService.App.get().getEmployees(parameter, new AbstractAsyncCallback<GoalAssigneeItem[]>() {
            public void success(GoalAssigneeItem[] result) {
                super.success(result);
                setAssigneeMembers(result);
            }
        });
    }

    public void setAssigneeMembers(GoalAssigneeItem[] members) {
        if (members == null) return;

        assignees.clear();
        goalAssignees.clear();
        Map<Integer, List<GoalAssigneeItem>> teams = new HashMap<>();
        for (GoalAssigneeItem member : members) {
            List<GoalAssigneeItem> teamMembers = teams.computeIfAbsent(member.getDepartmentId(), k -> new ArrayList<>());
            teamMembers.add(member);
        }

        for (Integer teamId : teams.keySet()) {
            List<GoalAssigneeItem> mm = teams.get(teamId);
            ArrayList<KpiTreeInfo> kpiTreeInfoList = new ArrayList<>();
            String tmpItem = null;
            for (GoalAssigneeItem item : mm) {
                tmpItem = item.getDepartmentName();
                KpiTreeInfo info = new KpiTreeInfo(item.getId(), item.getName());
                info.setSelected(item.isAssignee());
                info.setEmployeeId(item.getId());
                info.setDepartmentId(teamId);

                goalAssignees.put(info, item);

                kpiTreeInfoList.add(info);
            }
            if (tmpItem != null) {
                KpiTreeInfo teamItem = new KpiTreeInfo(teamId, tmpItem);
                assignees.put(teamItem, kpiTreeInfoList);
            }

        }

        if (employeeSelector != null) {
            employeeSelector.setItems(assignees);
        }
    }


    @Override
    protected void addButtons() {
        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.getElement().setId(formView + "save_and_close_button");
        save.addClickHandler(event -> save());
        addButton(save);
    }

    protected boolean validateFields() {
        int errors = 0;

        StringBuilder message = new StringBuilder(wfmStrings.sureEnteredAllData());
        clearErrorStyle();

        if (fp != null && fp.get(CustomFormConstants.GOAL_TITLE) != null && fp.get(CustomFormConstants.GOAL_TITLE).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_TITLE, title, !Validation.validateTextBoxRequired(title));
        }

        if (fp != null && fp.get(CustomFormConstants.LOCATIONS) != null && fp.get(CustomFormConstants.LOCATIONS).isRequired()) {
            errors += markAsError(CustomFormConstants.LOCATIONS, location, !Validation.validateLookUpRequired(location));
        }

        if (fp != null && fp.get(CustomFormConstants.GOAL_PROORDEP) != null && fp.get(CustomFormConstants.GOAL_PROORDEP).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_PROORDEP, department, !Validation.validateLookUpRequired(department));
        }

        if (fp != null && fp.get(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET) != null && fp.get(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET).isRequired()) {
            errors += markAsError(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET, targetGoal, !Validation.validateTextBoxRequired(targetGoal));
        }

        if (fp != null && fp.get(CustomFormConstants.DEPARTMENT_GOAL_WEIGHT_WIDGET) != null && fp.get(CustomFormConstants.DEPARTMENT_GOAL_WEIGHT_WIDGET).isRequired()) {
            errors += markAsError(CustomFormConstants.DEPARTMENT_GOAL_WEIGHT_WIDGET, actualWeight, !Validation.validateTextBoxRequired(actualWeight));
        }

        if (fp != null && fp.get(CustomFormConstants.GOAL_START_DATE) != null && fp.get(CustomFormConstants.GOAL_START_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_START_DATE, startDate, startDate.getDate() == null);
            errors += markAsError(CustomFormConstants.GOAL_START_DATE, endDate, endDate.getDate() == null);

        }

        if (fp != null && fp.get(CustomFormConstants.GOAL_DESCRIPTION) != null && fp.get(CustomFormConstants.GOAL_DESCRIPTION).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_DESCRIPTION, description, !Validation.validateTextAreaRequired(description));
        }

        if (fp != null && fp.get(CustomFormConstants.GOAL_RESOLVER) != null && fp.get(CustomFormConstants.GOAL_RESOLVER).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_RESOLVER, resolver, !Validation.validateListBoxRequired(resolver));
        }

        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(message.toString(), Info.Type.WARNING);
            return false;
        }

        if (employeeSelector.isEmpty()) {
            markAsError(employeeSelector, true);
            Info.warn(wfmStrings.pleaseSelectEmployee());
            return false;
        }

        if (startDate.getDate() != null && endDate.getDate() != null) {
            if (startDate.getDate().after(endDate.getDate())) {
                Info.warn(wfmStrings.pleaseChooseValidDate());
                markAsError(endDate, true);
                return false;
            }
        }

        // Per-employee target is mandatory — highlight (red) every selected employee
        // whose target is missing, and stop the save.
        invalidTargetKeys.clear();
        if (selectedDataGrid != null) {
            for (KpiTreeInfo info : selectedDataGrid.getList()) {
                GoalAssigneeItem item = goalAssignees.get(info);
                if (item == null || item.getTarget() == null || item.getTarget() <= 0) {
                    invalidTargetKeys.add(KpiTreeInfo.KEY_PROVIDER.getKey(info));
                }
            }
        }
        if (!invalidTargetKeys.isEmpty()) {
            if (selectedDataGrid != null) {
                selectedDataGrid.redraw();
            }
            Info.warn(hrmsStrings.pleaseEnterTargetForEmployees());
            return false;
        }

        return true;
    }

    /**
     * Walks up from an element to its containing grid row ({@code <tr>}). Used by
     * the Tab/Arrow navigation handler to locate the row of the input being edited.
     */
    private Element getParentTableRow(Element element) {
        Element e = element;
        while (e != null) {
            if ("tr".equalsIgnoreCase(e.getTagName())) {
                return e;
            }
            e = e.getParentElement();
        }
        return null;
    }

    /**
     * Returns the first {@code <input>} found within a row. The target column is the
     * only one whose cell renders an input, so this is the row's target input.
     */
    private InputElement findFirstInput(Element row) {
        if (row == null) {
            return null;
        }
        NodeList<Element> inputs = row.getElementsByTagName(InputElement.TAG);
        return inputs.getLength() > 0 ? InputElement.as(inputs.getItem(0)) : null;
    }

    /**
     * Refreshes a single target cell's DOM after its value is committed: drops the
     * "required target" error border and blanks the field when the value resolved
     * to 0. Done in place to avoid a full grid redraw, which would disrupt focus
     * during keyboard navigation between rows.
     */
    private void refreshTargetCellDom(int rowIndex, GoalAssigneeItem item) {
        if (selectedDataGrid == null || rowIndex < 0 || rowIndex >= selectedDataGrid.getRowCount()) {
            return;
        }
        TableRowElement row = selectedDataGrid.getRowElement(rowIndex);
        InputElement input = findFirstInput(row);
        if (input == null) {
            return;
        }
        input.removeAttribute("style"); // clears the red "required target" border/background
        if (item.getTarget() == null || item.getTarget() <= 0) {
            input.setValue("");
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.DEPARTMENT_GOAL_FORM;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        return customFieldUtil == null ? customFieldUtil = new FormHasCustomField() : customFieldUtil;
    }

    @Override
    protected String getFormType() {
        return objectId == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return "";
    }

    @Override
    public String getIconStyle() {
        return "hrms hrms-edit";
    }

    @Override
    protected void initPredefinedValues() {
    }

    @Override
    protected void registerFields() {
    }
}
