package com.edatasite.workforce.gwt.hrms.client.ui;


import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.BrigadaViewSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectViewItem;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class BrigadaSummaryView extends CustomForm2 implements Constants, HasLinksInterface, Colapse {

    private final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    private ProjectViewItem item;
    private final Integer projectID;
    private HasLinks linkingUtil;
    private NoteWidget noteWidget;
    private FormHasCustomField customFieldUtil;
    private final boolean showCustomFields;

    private Widget richText;
    private HTML description;
    private HTML number, projectName, status, completed, pManager, bManager,
            createdBy, createdDate, updatedBy, updatedDate, employeeAssignment, owners;


    private KpiDataGrid<PositionsSelectItem> dataGrid;
    private ListDataProvider<PositionsSelectItem> dataProvider;
    private ColumnSortEvent.ListHandler<PositionsSelectItem> listHandler;
    public static final ProvidesKey<PositionsSelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId();


    private final BrigadaViewSinksContainer projectViewSinksContainer;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private SplitButton printPdfSplitButton;

    public BrigadaSummaryView(Integer projectID, BrigadaViewSinksContainer projectViewSinksContainer) {
        super("summary", wfmStrings.summaryView());
        this.projectViewSinksContainer = projectViewSinksContainer;
        this.projectID = projectID;
        showCustomFields = true;
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_MEMBER_ADD, BrigadaSummaryView.this, (sender, args) -> {
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_EDIT, BrigadaSummaryView.this, (sender, args) -> {
        });
    }

    @Override
    public String getIconStyle() {
        return "bgMark pm-welcome";
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(BrigadaSummaryView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                protected Integer getRelationID() {
                    return projectID;
                }

                @Override
                protected String getRelationType() {
                    return RelationItem.TYPE_PROJECT;
                }

                @Override
                protected String getRelationName() {
                    return item != null ? item.getName() : null;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    @Override
    public Widget onInitialize() {
        onAccessGranted();
        return null;
    }

    private void onAccessGranted() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.BrigadaList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                initialize();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                formPropertyMap = result.getFormPropertyMap();
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                }
                BrigadaSummaryView.super.onInitialize();
            }
        });
    }

    public void initialize() {
        super.onInitialize();
    }

    protected void registerFields() {
        number = new HTML();
        number.addStyleName(DEFAULT_WIDTH);
        String project_summary = "project_sumary";
        number.ensureDebugId(project_summary + "number");

        projectName = new HTML();
        projectName.addStyleName(DEFAULT_WIDTH);
        projectName.ensureDebugId(project_summary + "projectName");

        richText = createRichText();
        richText.addStyleName(DEFAULT_WIDTH);

        status = new HTML();
        status.addStyleName(DEFAULT_WIDTH);
        status.ensureDebugId(project_summary + "status");

        completed = new HTML();
        completed.addStyleName(DEFAULT_WIDTH);
        completed.ensureDebugId(project_summary + "completed");

        owners = new HTML();
        owners.addStyleName(DEFAULT_WIDTH);

        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.addStyleName("cellBasedWidget-mod cellBasedWidget-mod--static-body");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataGrid.ensureDebugId(project_summary + "assignees");
        listHandler = new ColumnSortEvent.ListHandler<>(dataProvider.getList());
        dataGrid.addColumnSortHandler(listHandler);
        dataProvider.addDataDisplay(dataGrid);

        pManager = new HTML();
        pManager.addStyleName(DEFAULT_WIDTH);
        pManager.ensureDebugId(project_summary + "pManager");

        bManager = new HTML();
        bManager.addStyleName(DEFAULT_WIDTH);
        bManager.ensureDebugId(project_summary + "bManager");

        employeeAssignment = new HTML();
        employeeAssignment.addStyleName(DEFAULT_WIDTH);
        employeeAssignment.ensureDebugId(project_summary + "employeeAssignment");

        createdBy = new HTML();
        createdBy.addStyleName(DEFAULT_WIDTH);
        createdBy.ensureDebugId(project_summary + "createdBy");

        createdDate = new HTML();
        createdDate.addStyleName(DEFAULT_WIDTH);
        createdDate.ensureDebugId(project_summary + "createdDate");

        updatedBy = new HTML();
        updatedBy.addStyleName(DEFAULT_WIDTH);
        updatedBy.ensureDebugId(project_summary + "updatedBy");

        updatedDate = new HTML();
        updatedDate.addStyleName(DEFAULT_WIDTH);
        updatedDate.ensureDebugId(project_summary + "updatedDate");

        if (showCustomFields) {
            getCustomFieldUtil().drawCustomFields(BrigadaSummaryView.this, projectID, true);
        }

        noteWidget = new NoteWidget(projectID, RelationItem.TYPE_PROJECT);

        addFields();
    }

    private Widget createRichText() {
        description = new HTML();
        description.setWidth("100%");
        return description;
    }

    private void addFields() {
        //Project Details
        addTitleField(CustomFormConstants.DETAILS, getTitle(property.getSingular(wfmStrings.details(), "brigada")));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, projectName, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(CustomFormConstants.NAME, projectName, wfmStrings.name());
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status()));
        } else {
            addField(CustomFormConstants.STATUS, status, getTitle(wfmStrings.status()));
        }

        //More details
        addTitleField(CustomFormConstants.MORE_DETAILS, getTitle(wfmStrings.moreDetails()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MANAGER) != null) {
            addField(CustomFormConstants.MANAGER, pManager, getTitle(formPropertyMap.get(CustomFormConstants.MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.MANAGER).getTitle() : wfmStrings.manager()));
        } else {
            addField(CustomFormConstants.MANAGER, pManager, getTitle(wfmStrings.manager()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, bManager, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).getTitle() : wfmStrings.backupManagers()));
        } else {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, bManager, getTitle(wfmStrings.backupManagers()));
        }

        addField(CustomFormConstants.PROJECT.CREATED_BY, createdBy, getTitle(wfmStrings.createdBy()));
        addField(CustomFormConstants.PROJECT.CREATED_DATE, createdDate, getTitle(wfmStrings.createdDate()));
        addField(CustomFormConstants.PROJECT.UPDATED_BY, updatedBy, getTitle(wfmStrings.modifiedBy()));
        addField(CustomFormConstants.PROJECT.UPDATED_DATE, updatedDate, getTitle(wfmStrings.modifiedDate()));
        addField(CustomFormConstants.PROJECT.PROJECT_NOTE, noteWidget, wfmStrings.notes(), true);
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        addField(CustomFormConstants.OWNER, owners, wfmStrings.owners());

        addTitleField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES, wfmStrings.involvedEmployees());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE) != null) {
            addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, dataGrid, null, false);
        } else {
            addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, dataGrid, null, false);
        }

        GeneralFileUpload fileUpload = new GeneralFileUpload(F_PROJECT, projectID, projectID);
        addField(ATTACHMENTS, fileUpload, wfmStrings.attachments(), true);

        show();
    }

    @Override
    protected void addButtons() {


        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        if (Utils.hasPermission(HRMS_BRIGADA_PDF)) {
            addRightButton(printPdfSplitButton);
        }


        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_REMOVE)) {
            MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
            if (Utils.hasRole(Constants.ADMIN)) {
                MaterialLink customize = new MaterialLink(wfmStrings.customize());
                customize.addClickHandler(click -> {
                    String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
                });
                options.add(customize);
            }

            //Remove
            if (Utils.hasPermission(PermissionConstants.PM_PROJECT_REMOVE)) {
                MaterialLink deleteButton = new MaterialLink(wfmStrings.delete());
                deleteButton.addClickHandler(event -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            boolean booleanRemoveIcon = Utils.hasPermission(PermissionConstants.HRMS_SHIFT_DELETE) && !projectID.equals(item.getDefaultProjectID()) && !projectID.equals(item.getCrmProjectID());
                            if (booleanRemoveIcon) {
                                ProjectService.App.get().deleteProject(projectID, new AbstractAsyncCallback<Void>() {
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_DELETE, result, BrigadaSummaryView.this);
                                        Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.team()), Info.Type.INFO);
                                        closeTab();
                                    }
                                });
                            } else {
                                LoadingPanel.loading(false);
                                if (projectID.equals(item.getDefaultProjectID()) || projectID.equals(item.getCrmProjectID())) {
                                    Info.show(wfmStrings.systemReferenceNotDeleted(), Info.Type.WARNING);
                                } else {
                                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);

                                }
                            }
                        }
                    });
                    message.open();
                });
                options.add(deleteButton);
            }

        }

        if (Utils.hasPermission(PermissionConstants.HRMS_BRIGADA_EDIT)) {
            addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> {
                closeTab("brigada|edit/" + projectID);

            });
        }
        customizeButton.setVisible(false);

    }


    public void pdfTool(ProjectViewItem result) {
        if (printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        LeaveRequestObject requestObject = new LeaveRequestObject(projectID);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("pdfTemplateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/brigadaViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        HrmsService.App.get().viewBrigada(projectID, new AbstractAsyncCallback<ProjectViewItem>() {
            @Override
            public void failure(Throwable caught) {
                caught.getMessage();
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ProjectViewItem o) {
                if (o.getPermissions() != null) {
                    Utils.setUserPermissions(o.getPermissions());
                }
                item = o;
                drawProjectView();
                initProjectMembersTableColumns();
                getCustomFieldUtil().fillCustomFieldsWithData(o.getCustomFields(), true);

                pdfTool(o);

                LoadingPanel.loading(false);
            }
        });
    }

    private void drawProjectView() {
        if (item.getObjectID() != null) {
            Utils.registrRelation(item);
        }
        String pName = item.getName() != null ? item.getName() : "";
        if (pName.length() > 45) {
            pName = pName.substring(0, 45) + "...";
        }
        if (item.getNumberData() != null) {
            number.setHTML(item.getNumberData() != null ? item.getNumberData().getNumberString() : "");
        } else {
            number.setHTML("N/A");
        }
        projectName.setHTML(pName);

        pManager.setHTML(item.getManager() != null ? item.getManager() : "N/A");

        FlexTable backupManagerLinks = new FlexTable();
        int i = 0;
        for (final SelectItem backupManager : item.getBackupManagers()) {
            HTML backupManagerHTML = new HTML();
            backupManagerHTML.setHTML(backupManager.getName());
            backupManagerLinks.setWidget(i, 0, backupManagerHTML);
            i++;
        }
        bManager.setHTML(item.getBackupManagers().size() > 0 ? backupManagerLinks.toString() : "N/A");

        description.setHTML(!Utils.isNullOrEmpty(item.getDescription()) ? item.getDescription().replace("\n", "<br/>") : "");

        status.setHTML(item.getStatus());
        employeeAssignment.setHTML(item.getEmployeeAssignment() != null ? item.getEmployeeAssignment().getTitle() : EmployeeAssignmentEnum.BY_EMPLOYEE.getTitle());
        if (item.getComplete() == null || item.getComplete().equals("0.0")) {
            completed.setHTML("0.00%");
        } else if (Double.valueOf(item.getComplete()) > 100) {
            completed.setHTML("<p style='color: red'>" + item.getComplete() + "%</p>");
        } else {
            completed.setHTML(formatToDouble(item.getComplete()) + "%");
        }


        PositionsSelectItem[] projectEmployees = item.getProjectEmployees();
        if (projectEmployees != null && projectEmployees.length > 0) {

            if (projectEmployees.length > 10) {
                dataGrid.setHeight("400px");
            }
            initDataProviderApply(projectEmployees);

        }
        if (item.getOwners() != null) {
            StringBuilder ownersName = new StringBuilder();
            for (SelectItem selectItem : item.getOwners()) {
                ownersName.append(selectItem.getName() + ",");
            }
            owners.setHTML(ownersName.toString());
        }

        moreDetails();

        dataProvider.refresh();
        createdBy.setHTML(item.getCreator());
        createdDate.setHTML(DateUtils.format(item.getCreationDate()));
        updatedBy.setHTML(item.getLastUpdaterName());
        updatedDate.setHTML(DateUtils.format(item.getLastUpdateTime()));

        if (showCustomFields) {
            if (projectID != null) {
                getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields(), true);
            }
        }

    }


    private void initDataProviderApply(PositionsSelectItem[] projectEmployees) {
        List<PositionsSelectItem> employeeItems = dataProvider.getList();
        employeeItems.clear();
        Collections.addAll(employeeItems, projectEmployees);
    }

    private void moreDetails() {
    }

    private void initProjectMembersTableColumns() {
        //employee name
        Column<PositionsSelectItem, String> employee = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return (object.getEmployeeNumber() != null ? object.getEmployeeNumber() + " - " : "") + (object.getName() != null ? object.getName() : "");
            }
        };
        dataGrid.addColumn(employee, wfmStrings.employee());
        dataGrid.setColumnWidth(employee, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        if (!EmployeeAssignmentEnum.BY_POSITION.equals(item.getEmployeeAssignment())) {
            Column<PositionsSelectItem, String> position = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(PositionsSelectItem object) {
                    return object.getPositionName();
                }
            };

            dataGrid.addColumn(position, wfmStrings.position());
            dataGrid.setColumnWidth(position, 15, com.google.gwt.dom.client.Style.Unit.PCT);


            Column<PositionsSelectItem, String> department = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(PositionsSelectItem object) {
                    return object.getDepartmentName();
                }
            };

            dataGrid.addColumn(department, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department(), wfmStrings.department()));
            dataGrid.setColumnWidth(department, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        }

        Column<PositionsSelectItem, String> label = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return object.getLabel();
            }
        };

        dataGrid.addColumn(label, wfmStrings.note());
        dataGrid.setColumnWidth(label, 20, com.google.gwt.dom.client.Style.Unit.PCT);
    }

    private String formatToDouble(String text) {
        return numberFormat.format(parseToDouble(text));
    }

    private double parseToDouble(String text) {
        return Double.parseDouble(text.replace(",", ""));
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BRIGADA_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.PM_PROJECT_ADD;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }


    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            Localize.getInstance().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    @Override
    protected void initPredefinedValues() {
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
        return Constants.BRIGADA;
    }
}
