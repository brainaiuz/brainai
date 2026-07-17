package com.edatasite.workforce.gwt.team.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * User: Xushnud Babaev
 * Date: 7/12/12
 * Time: 3:22 PM
 */
public class DepartmentView extends AddDepartmentView implements NoColapse {

    private HTML name, startDate, createdBy, location, email, departmentParent, departmentLeader, departmentLeader2, departmentLeader3, departmentLeader4, departmentLeader5, numberData,status;
    private final Integer departmentID;
    private KpiDataGrid<EmployeeListItem> departmentEmployees;
    private int lastScrollPos = 0;
    private TextArea2 description;
    private TextArea2 shortDescription;

    private final int limit = 200;
    private int offset = 0;
    private int employeeCount = 0;
    private boolean isEmpty = false;
    public static final ProvidesKey<EmployeeListItem> KEY_PROVIDER = (EmployeeListItem item) -> item == null ? null : item.getObjectID();

    private final String test_code_ID_name = "department_summary_view_";


    public DepartmentView(Integer departmentID) {
        super("summary");
        setDescription(property.getSingular(wfmStrings.summaryView(), wfmStrings.department()));
        this.departmentID = departmentID;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected void addButtons() {
        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return "/departmentViewPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                RequestObject requestObject = new RequestObject(departmentID);
                HashMap<String, String> parametrs = requestObject.getRequestParams();
                return parametrs;
            }
        });
        addRightButton(pdf);

        if (Utils.hasPermission(PermissionConstants.HRMS_DEPARTMENT_REMOVE)) {
            addRemoveButton().addClickHandler(event -> {
                LoadingPanel.loading(true);
                Integer headCount = item.getHeadCount() != null && !"".equals(item.getHeadCount()) ? Integer.valueOf(item.getHeadCount()) : 0;
                final DepartmentRemovePopup departmentDelete = new DepartmentRemovePopup(item.getObjectID(), headCount, null);
                DepartmentService.App.get().getChildDepartmentNames(item.getObjectID(), new AbstractAsyncCallback<String>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(String ch) {
                        LoadingPanel.loading(false);
                        if (ch != null && ch.length() > 0) {
                            Info.show("Current department is selected as parent department for \"" + ch + "\", please uncheck it as parent department and try again.", Info.Type.WARNING);
                        } else {
                            departmentDelete.selectitionListener(null);
                        }
                    }
                });
                departmentDelete.setCommand(() -> {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DEPARTMENT_EDIT, null, DepartmentView.this);
                    closeTab();
                });
            });
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_DEPARTMENT)) {
            //edit
            addEditButton().addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("department|edit/" + item.getObjectID()));
        }
    }

    @Override
    protected void getDataToFillFields() {
        getDepartment();
        getTeamEmployees();
    }

    public void getDepartment() {
        LoadingPanel.loading(true);
        DepartmentService.App.get().getTeamForEdit(departmentID,"SUMMARY", new AbstractAsyncCallback<TeamListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final TeamListItem o) {
                LoadingPanel.loading(false);
                item = o;
                fillFieldWithValue();
            }
        });
    }

    public void registerFields() {
        LoadingPanel.loading(true);
        //name
        name = initHTML();
        name.ensureDebugId(test_code_ID_name + "name");
        //department code
        numberData = new HTML();
        numberData.addStyleName(DEFAULT_WIDTH);
        numberData.ensureDebugId(test_code_ID_name + "departmentcode");
        //description
        description = new TextArea2(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        description.setEnabled(false);
        description.ensureDebugId(test_code_ID_name + "description");
        description.getElement().addClassName("file--DepartmentView");

        shortDescription = new TextArea2(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        shortDescription.setEnabled(false);
        shortDescription.ensureDebugId(test_code_ID_name + "shortDescription");
        shortDescription.getElement().addClassName("file--DepartmentView");

        //start date
        startDate = initHTML();
        startDate.ensureDebugId(test_code_ID_name + "start_date");
        //created by
        createdBy = initHTML();
        createdBy.ensureDebugId(test_code_ID_name + "created_by");
        //email
        email = initHTML();
        email.ensureDebugId(test_code_ID_name + "email");
        //location
        location = initHTML();
        location.ensureDebugId(test_code_ID_name + "location");
        // Department Parent
        departmentParent = initHTML();
        departmentParent.ensureDebugId(test_code_ID_name + "parent_team");
        // Department Leader
        departmentLeader = initHTML();
        departmentLeader.ensureDebugId(test_code_ID_name + "department_leader");
        // Department Leader 2
        departmentLeader2 = initHTML();
        departmentLeader2.ensureDebugId(test_code_ID_name + "department_leader2");
        // Department Leader 3
        departmentLeader3 = initHTML();
        departmentLeader3.ensureDebugId(test_code_ID_name + "department_leader3");
        // Department Leader 4
        departmentLeader4 = initHTML();
        departmentLeader4.ensureDebugId(test_code_ID_name + "department_leader4");
        // Department Leader 5
        departmentLeader5 = initHTML();
        departmentLeader5.ensureDebugId(test_code_ID_name + "department_leader5");

        status = initHTML();
        status.ensureDebugId(test_code_ID_name + "status");


        LoadingPanel.loading(true);

        departmentEmployees = new KpiDataGrid<>(KEY_PROVIDER);
        departmentEmployees.setSize("100%", "200px");
        departmentEmployees.addStyleName("cellBasedWidget-mod cellBasedWidget-mod--static-body");
        departmentEmployees.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        departmentEmployees.getScrollPanel().addScrollHandler(scroll -> {
            int oldScrollPos = lastScrollPos;
            lastScrollPos = departmentEmployees.getScrollPanel().getVerticalScrollPosition();
            if (oldScrollPos >= lastScrollPos) {
                return;
            }
            int maxScrollTop = departmentEmployees.getScrollPanel().getWidget().getOffsetHeight() - departmentEmployees.getScrollPanel().getOffsetHeight();
            if (lastScrollPos >= maxScrollTop) {
                getTeamEmployees();
            }
        });
        initTableColumns();

        addFieldsToForm();
        LoadingPanel.loading(false);
    }

    private void initTableColumns() {
        final Column<EmployeeListItem, String> employee = new Column<EmployeeListItem, String>(new TextCell()) {
            @Override
            public String getValue(EmployeeListItem object) {
                return (object.getEmployeeNumber() != null ? object.getEmployeeNumber() + " - " : "") + (object.getFirstName() + " " + object.getLastName());
            }
        };
        departmentEmployees.addColumn(employee, wfmStrings.employee());
        departmentEmployees.setColumnWidth(employee, 100, Style.Unit.PCT);


        Column<EmployeeListItem, String> email = new Column<EmployeeListItem, String>(new TextCell()) {
            @Override
            public String getValue(EmployeeListItem object) {
                return object.getEmail();
            }
        };
        departmentEmployees.addColumn(email, wfmStrings.email());
        departmentEmployees.setColumnWidth(email, 100, Style.Unit.PCT);

        Column<EmployeeListItem, String> position = new Column<EmployeeListItem, String>(new TextCell()) {
            @Override
            public String getValue(EmployeeListItem object) {
                return object.getPosition();
            }
        };
        departmentEmployees.addColumn(position, wfmStrings.position());
        departmentEmployees.setColumnWidth(position, 50, Style.Unit.PCT);

        Column<EmployeeListItem, String> location = new Column<EmployeeListItem, String>(new TextCell()) {
            @Override
            public String getValue(EmployeeListItem object) {
                return object.getLocation();
            }
        };
        departmentEmployees.addColumn(location, Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()));
        departmentEmployees.setColumnWidth(location, 50, Style.Unit.PCT);


        Column<EmployeeListItem, String> status = new Column<EmployeeListItem, String>(new TextCell()) {
            @Override
            public String getValue(EmployeeListItem object) {
                return object.getStatus();
            }
        };
        departmentEmployees.addColumn(status, wfmStrings.status());
        departmentEmployees.setColumnWidth(status, 50, Style.Unit.PCT);
    }

    private void getTeamEmployees() {
        if (!isEmpty) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStart(offset);
            fp.setLimit(limit);
            fp.setDepartmentId(departmentID);
            LoadingPanel.loading(true);
            ReportService.App.get().getEmployeesForGrid(fp, new AbstractAsyncCallback<EmployeeListItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    departmentEmployees.refresh();
                }

                @Override
                public void success(EmployeeListItem[] employeeListItems) {
                    LoadingPanel.loading(false);
                    employeeCount = employeeListItems.length;
                    if (employeeCount > 0) {
                        departmentEmployees.appendProvider(employeeListItems);
                        departmentEmployees.refresh();
                    } else {
                        isEmpty = true;
                    }
                    if (employeeCount < limit) {
                        isEmpty = true;
                    }
                }
            });
            offset += limit;
        }
    }

    public void addFieldsToForm() {
        addTitleField(CustomFormConstants.DEPARTMENT_DETAILS, wfmStrings.basicDetails());
        addField(CustomFormConstants.DEPARTMENT_NUMBER, numberData, getTitle(wfmStrings.number()));
        LinkedHashMap<String, FormProperty> formPropertyMap = getFormPropertyMap();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_NAME) != null) {
            addField(CustomFormConstants.DEPARTMENT_NAME, name, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_NAME).getTitle() : property.getSingular(wfmStrings.department(), wfmStrings.department())));
        } else {
            addField(CustomFormConstants.DEPARTMENT_NAME, name, getTitle(property.getSingular(wfmStrings.department(), wfmStrings.department())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_DESCRIPTION) != null) {
            addField(CustomFormConstants.DEPARTMENT_DESCRIPTION, description, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_DESCRIPTION).getTitle() : wfmStrings.description()));
        } else {
            addField(CustomFormConstants.DEPARTMENT_DESCRIPTION, description, getTitle(wfmStrings.description()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_SHORT_DESCRIPTION) != null) {
            addField(CustomFormConstants.DEPARTMENT_SHORT_DESCRIPTION, shortDescription, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_SHORT_DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_SHORT_DESCRIPTION).getTitle() : wfmStrings.shortDescription()));
        } else {
            addField(CustomFormConstants.DEPARTMENT_SHORT_DESCRIPTION, shortDescription, getTitle(wfmStrings.shortDescription()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE) != null) {
            addField(CustomFormConstants.DEPARTMENT_START_DATE, startDate, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE).getTitle() : wfmStrings.startDate()));
        } else {
            addField(CustomFormConstants.DEPARTMENT_START_DATE, startDate, getTitle(wfmStrings.startDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_EMAIL) != null) {
            addField(CustomFormConstants.DEPARTMENT_EMAIL, email, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_EMAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_EMAIL).getTitle() : wfmStrings.email()));
        } else {
            addField(CustomFormConstants.DEPARTMENT_EMAIL, email, getTitle(wfmStrings.email()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_CREATED_BY) != null) {
            addField(CustomFormConstants.DEPARTMENT_CREATED_BY, createdBy, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_CREATED_BY).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_CREATED_BY).getTitle() : wfmStrings.createdBy()));
        } else {
            addField(CustomFormConstants.DEPARTMENT_CREATED_BY, createdBy, getTitle(wfmStrings.createdBy()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LOCATION) != null) {
            addField(CustomFormConstants.DEPARTMENT_LOCATION, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), formPropertyMap.get(CustomFormConstants.DEPARTMENT_LOCATION).isRequired()));
        } else {
            addField(CustomFormConstants.DEPARTMENT_LOCATION, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
        }

        addField("DEPARTMENT_STATUS",status,wfmStrings.status());

        // DEPARTMENT_PARENT rename "REPORTS_TO"
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT) != null) {
            addField(CustomFormConstants.DEPARTMENT_PARENT, departmentParent, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT).getTitle() : wfmStrings.reportsTo()));
        } else {
            addField(CustomFormConstants.DEPARTMENT_PARENT, departmentParent, getTitle(wfmStrings.reportsTo()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_EMPLOYEES) != null) {
            addField(CustomFormConstants.DEPARTMENT_EMPLOYEES, null);
        } else {
            addField(CustomFormConstants.DEPARTMENT_EMPLOYEES, departmentEmployees, null, true);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER) != null) {
            addField(CustomFormConstants.DEPARTMENT_LEADER, departmentLeader, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER).getTitle() : property.getSingular(wfmStrings.departmentLeader(), wfmStrings.department())));
        } else {
            addField(CustomFormConstants.DEPARTMENT_LEADER, departmentLeader, getTitle(property.getSingular(wfmStrings.departmentLeader(), wfmStrings.department())));
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_DEPARTMENT_LEADER)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER2) != null) {
                addField(CustomFormConstants.DEPARTMENT_LEADER2, departmentLeader2, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER2).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER2).getTitle() : property.getSingular(wfmStrings.departmentLeader2(), wfmStrings.department())));
            } else {
                addField(CustomFormConstants.DEPARTMENT_LEADER2, departmentLeader2, getTitle(property.getSingular(wfmStrings.departmentLeader2(), wfmStrings.department())));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER3) != null) {
                addField(CustomFormConstants.DEPARTMENT_LEADER3, departmentLeader3, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER3).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER3).getTitle() : property.getSingular(wfmStrings.departmentLeader3(), wfmStrings.department())));
            } else {
                addField(CustomFormConstants.DEPARTMENT_LEADER3, departmentLeader3, getTitle(property.getSingular(wfmStrings.departmentLeader3(), wfmStrings.department())));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER4) != null) {
                addField(CustomFormConstants.DEPARTMENT_LEADER4, departmentLeader4, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER4).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER4).getTitle() : property.getSingular(wfmStrings.departmentLeader4(), wfmStrings.department())));
            } else {
                addField(CustomFormConstants.DEPARTMENT_LEADER4, departmentLeader4, getTitle(property.getSingular(wfmStrings.departmentLeader4(), wfmStrings.department())));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER5) != null) {
                addField(CustomFormConstants.DEPARTMENT_LEADER5, departmentLeader5, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER5).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER5).getTitle() : property.getSingular(wfmStrings.departmentLeader5(), wfmStrings.department())));
            } else {
                addField(CustomFormConstants.DEPARTMENT_LEADER5, departmentLeader5, getTitle(property.getSingular(wfmStrings.departmentLeader5(), wfmStrings.department())));
            }
        }
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        show();
    }

    public void fillFieldWithValue() {
        setInnerHTML(numberData, item.getNumberData() != null ? item.getNumberData().getFirstNumberString() : "");
        setInnerHTML(name, item.getName());
        description.setText(item.getDescription());
        shortDescription.setText(item.getShortDescription());
        setInnerHTML(startDate, DateUtils.format(item.getStartDate()) + Utils.getHijriDate(item.getStartDate()));
        setInnerHTML(createdBy, item.getCreator() != null ? item.getCreator().getName() : "");
        setInnerHTML(email, item.getEmail());
        setInnerHTML(status, item.isActive() ? wfmStrings.active() : wfmStrings.inactive());
        setInnerHTML(location, item.getLocation() != null ? item.getLocation().getName() : wfmStrings.notAvailable());
        setInnerHTML(departmentParent, item.getParentDepartment() != null ? item.getParentDepartment().getName() : wfmStrings.notAvailable());
        setInnerHTML(departmentLeader, item.getLeader());
        setInnerHTML(departmentLeader2, item.getLeader2());
        setInnerHTML(departmentLeader3, item.getLeader3());
        setInnerHTML(departmentLeader4, item.getLeader4());
        setInnerHTML(departmentLeader5, item.getLeader5());
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems(), true);
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

    @Override
    public String getPropertyCode() {
        return Constants.DEPARTMENT_LIST;
    }
}
