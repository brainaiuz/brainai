package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
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
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ContractViewItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Faxriddin Taslimov  Date: 29-08-2015
 */
public class ContractSummaryView extends CustomForm2 implements Constants, Colapse {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private final String contract_summary = "contract_sumary";
    private ContractViewItem item;
    private final Integer contractID;
    private HTML number, allowancebyclient;
    private WfmButton2 convertToProject;
    private FlowPanel pnlEmployeeAssignmentContainer;
    private NoteWidget noteWidget;
    private FlexTable projectPositionTable;
    private HTML registrationDate;
    private HTML startDate;
    private HTML dueDate;
    private GeneralFileUpload fileUpload;
    private FormHasCustomField customFieldUtil;

    public ContractSummaryView(Integer contractID) {
        super("summary", wfmStrings.contract());
        this.contractID = contractID;

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTRACT_EDIT, ContractSummaryView.this, (sender, args) -> {
        });
    }

    @Override
    public String getIconStyle() {
        return "bgMark pm-welcome";
    }


    @Override
    public Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFields(ViewName.Contract, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                initialize();
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                }
                initialize();
            }
        });
        return null;
    }

    public void initialize() {
        super.onInitialize();
    }

    protected void registerFields() {

        number = new HTML();
        number.addStyleName(DEFAULT_WIDTH);
        number.ensureDebugId(contract_summary + "number");
        allowancebyclient = new HTML();
        allowancebyclient.addStyleName(DEFAULT_WIDTH);
        allowancebyclient.ensureDebugId(contract_summary + "allowancebyclient");
        pnlEmployeeAssignmentContainer = new FlowPanel();
        projectPositionTable = new FlexTable();
        projectPositionTable.setStyleName("table");
        projectPositionTable.setWidth("100%");
        noteWidget = new NoteWidget(contractID, RelationItem.TYPE_CONTRACT);

        registrationDate = new HTML();
        registrationDate.ensureDebugId(contract_summary + "registrationDate");

        startDate = new HTML();
        startDate.ensureDebugId(contract_summary + "startDate");

        dueDate = new HTML();
        dueDate.ensureDebugId(contract_summary + "dueDate");

        //attachments
        fileUpload = new GeneralFileUpload(F_CONTRACT, contractID, contractID);
        fileUpload.ensureDebugId("contract_attachments");
        getCustomFieldUtil().drawCustomFields(this, contractID, true);
        addFields();
    }

    private void addFields() {
        addTitleField(CustomFormConstants.DETAILS, getTitle(wfmStrings.contract() + " " + wfmStrings.details()));
        addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number()));
        addField(CustomFormConstants.NAME, allowancebyclient, getTitle(wfmStrings.allowancebytheclient()));
        addField(CustomFormConstants.REGISTRATION_DATE, registrationDate, getTitle(wfmStrings.dateOfRegistration()));
        addTitleField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES, wfmStrings.requirements());
        addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, pnlEmployeeAssignmentContainer, null);
        addField(CONTRACT.START_DATE, startDate, wfmStrings.startDate());
        addField(CONTRACT.END_DATE, dueDate, wfmStrings.dueDate());
        addTitleField(CONTRACT.NOTE, wfmStrings.notes());
        addField(CONTRACT.NOTE, noteWidget, wfmStrings.notes(), true);
        addField(ATTACHMENTS, fileUpload, getTitle(wfmStrings.attachments(), false), true);
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        show();
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_DELETE)) {
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
            if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_DELETE)) {
                MaterialLink deleteButton = new MaterialLink(wfmStrings.delete());
                deleteButton.addClickHandler(event -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);

                            ProjectService.App.get().deleteContract(contractID, new AbstractAsyncCallback<Void>() {
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void success(Void result) {
                                    LoadingPanel.loading(false);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTRACT_DELETE, result, ContractSummaryView.this);
                                    Info.show(wfmStrings.yourContractHasBeenDeleted(), Info.Type.INFO);
                                    closeTab();
                                }
                            });
                        }
                    });
                    message.open();
                });
                options.add(deleteButton);
            }
        }
//      Convert To Project
        convertToProject = new WfmButton2(wfmStrings.convertTo() + " " + Property.get(Constants.PROJECT, wfmStrings.project()), WfmButton2.BTN_WHITE_OUTLINE);
        convertToProject.ensureDebugId("contractSummary" + "saveCloseButton");
        convertToProject.addClickHandler(sender -> {
            goTo("project|add/add/contract/" + contractID);
        });
        if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_CONVERT_TO_PROJECT)) {
            addButton(convertToProject);
        }
        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return "/contractViewPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                RequestObject requestObject = new RequestObject(contractID);
                return requestObject.getRequestParams();
            }
        });
        addRightButton(pdf);

        if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_ADD_EDIT)) {
            addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> {
                goTo("contract|edit/" + contractID);
            });
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ProjectService.App.get().viewContract(contractID, new AbstractAsyncCallback<ContractViewItem>() {
            @Override
            public void failure(Throwable caught) {
                caught.getMessage();
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ContractViewItem o) {
                item = o;
                drawContractView();
                LoadingPanel.loading(false);
            }
        });
    }

    private void drawContractView() {
        convertToProject.setVisible(item.getProjectId() == null);
        number.setHTML(item.getNumber());
        setAllowanceByClient();
        pnlEmployeeAssignmentContainer.clear();
        setTableValues(item.getProjectPositions());
        pnlEmployeeAssignmentContainer.add(projectPositionTable);
        if (item.getCreationTime() != null) {
            registrationDate.setHTML(DateUtils.format(item.getCreationTime()));
        }
        if (item.getStartDate() != null) {
            startDate.setHTML(DateUtils.format(item.getStartDate().getNonConvertedDate()));
        }
        if (item.getDueDate() != null) {
            dueDate.setHTML(DateUtils.format(item.getDueDate().getNonConvertedDate()));
        }

        SimpleLink projectLink = null;
        HTML projectHtml = new HTML("N/A");
        if (item.getProject() != null && !"N/A".equals(item.getProject())) {
            projectLink = new SimpleLink("<b>" + item.getProject() + "</b>");
            projectLink.addClickHandler(event -> {
                final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(item.getProjectStatusCode()));
                SinksContainerFactory.entryPoint.onHistoryChanged("project|summary/" + item.getProjectId() + "/" + item.getProjectParentId() + "/" + hasAccessToChange, item.getNumber(), item.getProject());
            });
            projectHtml.setHTML(item.getProject());
        }
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_LIST) && projectLink != null) {
            addField(CustomFormConstants.TASK.PROJECT, projectLink, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
        } else {
            addField(CustomFormConstants.TASK.PROJECT, projectHtml, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
        }

        SimpleLink clientLink = null;
        HTML clientHtml = new HTML("N/A");
        if (item.getClient() != null && !"N/A".equals(item.getClient())) {
            clientLink = new SimpleLink("<b>" + item.getClient() + "</b>");
            clientLink.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + item.getClientId(), item.getClient(), item.getClient()));
            clientHtml.setHTML(item.getClient());
        }
        if (Utils.hasPermission(PermissionConstants.CLIENT_NAME_CLICKABLE) && clientLink != null) {
            addField(CustomFormConstants.PROJECT.CLIENT, clientLink, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
        } else {
            addField(CustomFormConstants.PROJECT.CLIENT, clientHtml, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_RELATED_CASE_IN_CONTRACT)) {
            FlowPanel casePanel = new FlowPanel();

            if (item.getRelatedCases() != null && !item.getRelatedCases().isEmpty()) {
                for (SelectItem caseItem : item.getRelatedCases()) {

                    SimpleLink link = new SimpleLink("<b>" + caseItem.getName() + "</b>");
                    link.getElement().getStyle().setDisplay(Style.Display.BLOCK);  // <-- yangi qatorda chiqaradi

                    link.addClickHandler(event ->
                            Utils.openURL("Crm.html#case|summary/" + caseItem.getId())
                    );

                    casePanel.add(link);
                }
            } else {
                casePanel.add(new HTML("N/A"));
            }

            if (casePanel.getWidgetCount() > 0) {
                addField(CustomFormConstants.CASE_ID, casePanel, getTitle(wfmStrings.relatedCase()));
            }
        }

        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems(), true);
    }

    private void setTableValues(ProjectPosition[] projectPositions) {
        setTableHeader();
        projectPositionTable.getRowFormatter().setStyleName(0, "thead");
        int i = 1;
        for (ProjectPosition projectPosition : projectPositions) {
            projectPositionTable.setWidget(i, 0, new HTML(projectPosition.getPositionName()));
            projectPositionTable.getFlexCellFormatter().getElement(i, 0).getStyle().setHeight(20, Style.Unit.PX);
            projectPositionTable.setWidget(i, 1, new HTML(projectPosition.getContractStart() != null ? DateUtils.format(projectPosition.getContractStart()) : ""));
            projectPositionTable.setWidget(i, 2, new HTML(projectPosition.getContractEnd() != null ? DateUtils.format(projectPosition.getContractEnd()) : ""));
            projectPositionTable.setWidget(i, 3, new HTML(String.valueOf(projectPosition.getNumberOfWorker() != null ? projectPosition.getNumberOfWorker() : "")));
            projectPositionTable.setWidget(i, 4, new HTML(getPriceTypeName(projectPosition.getPriceType())));
            projectPositionTable.setWidget(i, 5, new HTML(Utils.getNumberFormat().format(projectPosition.getUnitPrice() != null ? projectPosition.getUnitPrice() : BigDecimal.ZERO)));
            projectPositionTable.setWidget(i, 6, new HTML(Utils.getNumberFormat().format(projectPosition.getUnitQTY() != null ? projectPosition.getUnitQTY() : BigDecimal.ZERO)));
            i++;
        }
    }

    private String getPriceTypeName(Integer priceTypeId) {
        if (priceTypeId != null) {
            DataListBox priceTypes = getPriceTypes();
            for (SelectItem si : priceTypes.getItems()) {
                if (priceTypeId.equals(si.getId())) {
                    return si.getName();
                }
            }
        }
        return "";
    }

    private DataListBox getPriceTypes() {
        DataListBox daysStringListBox = new DataListBox();
        daysStringListBox.setWidth("105px");
        daysStringListBox.addListItem(new SelectItem(0, " " + projectStrings.byHour(), "formula_byHour"));        //byHour
        daysStringListBox.addListItem(new SelectItem(1, " " + projectStrings.byMonth(), "formula_byMonth"));       //byMonth
        daysStringListBox.addListItem(new SelectItem(2, " " + projectStrings.byMarkup(), "formula_byMarkup"));    //byMarkup
        daysStringListBox.addListItem(new SelectItem(3, " " + projectStrings.byMarkupAndOT(), "formula_byMarkupAndOT"));    //byMarkupAndOT
        daysStringListBox.addListItem(new SelectItem(4, " " + projectStrings.byDayAndOT(), "formula_byDayAndOT"));    //byDayAndOT
        daysStringListBox.addListItem(new SelectItem(5, " " + projectStrings.byDayAndOTSpec(), "formula_byDayAndOTSpec"));    //byDayAndOTSpec
        return daysStringListBox;
    }

    private void setTableHeader() {
        for (int i = 0; i < 7; i++) {
            String title;
            switch (i) {
                case 0:
                    title = "Position";
                    break;
                case 1:
                    title = "Contract Start";
                    break;
                case 2:
                    title = "Contract End";
                    break;
                case 3:
                    title = "No.Workers";
                    break;
                case 4:
                    title = "Price Type";
                    break;
                case 5:
                    title = "Rate";
                    break;
                case 6:
                    title = "Unit Qty";
                    break;
                default:
                    title = "n/a";
                    break;
            }
            setHeaderItem(i, title);
        }
    }

    private void setHeaderItem(int i, String title) {
        projectPositionTable.setWidget(0, i, new HTML(title));
    }

    private void setAllowanceByClient() {
        String s = wfmStrings.notAvailable();
        boolean isaccomudation = false;
        if (item.getAccomodation()) {
            s = wfmStrings.accomodation();
            isaccomudation = true;
        }
        if (item.getFood()) {
            s = isaccomudation ? s + ", " : "";
            s = s + wfmStrings.food();
        }
        allowancebyclient.setHTML(s);
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.CONTRACT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.PM_CONTRACT_ADD_EDIT;
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.DETAILS.equals(fieldID)) {
                return wfmStrings.contract() + " " + wfmStrings.details();
            } else if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.contract() + " " + wfmStrings.number();
            } else if (CustomFormConstants.NAME.equals(fieldID)) {
                return wfmStrings.allowancebytheclient();
            } else if (CustomFormConstants.PROJECT.CLIENT.equals(fieldID)) {
                return Property.get(Constants.CLIENT_LIST, wfmStrings.customer());
            } else if (CustomFormConstants.TASK.PROJECT.equals(fieldID)) {
                return Property.get(Constants.PROJECT, wfmStrings.project());
            } else if (CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES.equals(fieldID)) {
                return wfmStrings.requirements();
            } else if (CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE.equals(fieldID)) {
                return wfmStrings.members();
            } else if (CustomFormConstants.CONTRACT.NOTE.equals(fieldID)) {
                return wfmStrings.notes();
            }
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
}