package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrVacancyRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimePickerCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.LookUpCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.edatasite.workforce.gwt.hrms.client.ui.quickadd.VacancyQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class VacancyListView extends BaseListView implements Constants {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final NumberFormat salaryFormat = NumberFormat.getFormat(",##0.00");
    private HashMap<Integer, SelectItem[]> map = null;
    private ListingPanel<VacancyItem> listingTable;
    private final String vacancyListViewID = "vacancy_list_view_";
    private Integer objectID;
    private Integer positionId;
    private KpiSideNavBox quickAddBox;
    private int totalCount = 0;
    private final HorizontalPanel postPanel = new HorizontalPanel();
    private boolean isFromPosition = false;
    private boolean isStatusDisabled = false;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public VacancyListView() {
        super("vacancyAnnouncements");
        setDescription(property.getPlural(hrmsStrings.vacancies()));
        if (hasPermissionToAdd()) {
            setAddNew("vacancy|add/add");
        }
    }

    public VacancyListView(Integer positionId, boolean fromPosition) {
        super(VACANCY_LIST);
        this.positionId = positionId;
        setDescription(property.getPlural(hrmsStrings.vacancies()));
        if (fromPosition) {
            isFromPosition = true;
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_VACANCY)) {
            setAddNew("vacancy|add/add");
        }
    }

    //add permission
    private boolean hasPermissionToAdd() {
        if (isFromPosition) {
            return Utils.hasPermission(PermissionConstants.HRMS_ADD_VACANCY_FOR_CURRENT_POSITION);
        }
        return Utils.hasPermission(PermissionConstants.HRMS_ADD_VACANCY) || Utils.hasPermission(PermissionConstants.HRMS_QUICK_ADD_VACANCY);
    }

    public String getIconStyle() {
        return "bgMark vacancyAnnouncements";
    }

    protected Widget onInitialize() {
        listingTable = new GuideListingPanel(ListPanelType.VacancyListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VACANCY_ADDED, VacancyListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VACANCY_DELETE, VacancyListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VACANCY_MATCHED, VacancyListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VACANCY_REJECTED_OR_APROVED, VacancyListView.this, (sender, args) -> listingTable.reloadPage());

        listingTable.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveVacancyEditCellValue((VacancyItem) rowValue, columnCodeName));

        listingTable.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/vacancyListPDFHandler";
            ListingFilterParameter fp = listingTable.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listingTable.callListPDF(pdfURL, fp);
        });

        listingTable.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadVacancyListExcel";
            ListingFilterParameter fp = listingTable.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());

            listingTable.callListExcel(excelURL, fp);
        });
        add(postPanel);
        add(listingTable);

        fillFormPropertyMap();
        initQuickAddView();

        return null;
    }

    //Load Default Columns-1st SignUp & Vacancy List configuration
    private ColumnDefinitionConfig[] getColumnConfigs() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig colConfig;

        //Action Menu #1
        colConfig = new ColumnDefinitionConfig<VacancyItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final VacancyItem rowValue) {
                final MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);
                int actionItemCount = 0;
                boolean isDraft = Constants.VACANCY_APPROVAL_STATUS_DRAFT.equals(rowValue.getApprovalStatusCode());
                //Summary
                final MenuPopItem vacancySummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                vacancySummary.getElement().setId(vacancyListViewID + "vacancy_summary");
                vacancySummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("vacancy|summary/" + rowValue.getObjectID(), rowValue.getNumberData() != null ? rowValue.getNumberData().getNumberString() : rowValue.getJobTitle(), rowValue.getJobTitle()));
                actionItemCount++;
                if (!isDraft) {
                    menuBar.addItem(vacancySummary);
                }
                //Edit
                if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_VACANCY)) {
                    MenuPopItem vacancyEdit = new MenuPopItem(wfmStrings.edit(), "icon-personal-goal-small");
                    vacancyEdit.getElement().setId(vacancyListViewID + "edit_vacancy");
                    vacancyEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("vacancy|editVacancy/" + rowValue.getObjectID(), rowValue.getNumberData() != null ? rowValue.getNumberData().getNumberString() : rowValue.getJobTitle(), rowValue.getJobTitle()));
                    actionItemCount++;
                    menuBar.addItem(vacancyEdit);
                }

                //Copy
                if (Utils.hasPermission(PermissionConstants.HRMS_VACANCY_COPY)) {
                    MenuPopItem copy = new MenuPopItem(wfmStrings.copy(), "icon-copy", () -> SinksContainerFactory.entryPoint.onHistoryChanged("vacancy|add/add/" + rowValue.getObjectID() + "/" + COPY));
                    copy.getElement().setId("copy_vacancy");
                    actionItemCount++;
                    menuBar.addItem(copy);
                }
                //change status
                if (Utils.hasPermission(PermissionConstants.HRMS_CHANGE_STATUS_VACANCY) && !isStatusDisabled) {
                    final MenuPopItem vacancyStatus = new MenuPopItem(wfmStrings.changeStatus(), "icon-employee-edit-profile");
                    vacancyStatus.getElement().setId(vacancyListViewID + "change_status");
                    MenuBar changeStat = new MenuBar(true);
                    changeStat.setAutoOpen(true);
                    for (final SelectItem status : rowValue.getStatuses()) {
                        final MenuPopItem menuItem = new MenuPopItem(status.getName(), "icon-something");
                        if (rowValue.getStatus() != null && status.getId() != null &&
                                status.getId().equals(rowValue.getStatus().getObjectID())) {
                            menuItem.setSelection(true);
                        }
                        menuItem.getElement().setId(status.getDescription() != null ? status.getDescription().toLowerCase() : status.getName().toLowerCase().replace(" ", ""));
                        menuItem.setCommand(() -> {
                            menuItem.closeAll(menuBar);
                            rowValue.setStatus(new ReferenceItem(status.getId(), status.getName()));
                            RecruitmentService.App.get().changeVacancyStatus(rowValue.getObjectID(), status.getId(), new AbstractAsyncCallback<Void>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Void result) {
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.status()), Info.Type.INFO);
                                    listingTable.reloadPage();
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VACANCY_DELETE, result, VacancyListView.this);
                                }
                            });
                        });
                        changeStat.addItem(menuItem);
                    }
                    vacancyStatus.setSubMenu(changeStat);
                    actionItemCount++;
                    menuBar.addItem(vacancyStatus);
                }
                //delete vacancy
                if (Utils.hasPermission(PermissionConstants.HRMS_DELETE_VACANCY)) {
                    final MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    removeItem.getElement().setId(vacancyListViewID + "remove_vacancy");
                    removeItem.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                RecruitmentService.App.get().deleteVacancy(rowValue.getObjectID(), new AbstractAsyncCallback<Void>() {
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.vacancy()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VACANCY_DELETE, result, VacancyListView.this);
                                        listingTable.reloadPage();
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(removeItem);
                }

                MenuPopItem pdfItem = new MenuPopItem(wfmStrings.pdf());
                pdfItem.setCommand(() -> {
                    String pdfURL = CommandConstants.PDF_URL + "/vacancyViewPDFHandler";
                    final RequestObject requestObject = new RequestObject(rowValue.getObjectID());
                    final HashMap<String, String> parametrs = requestObject.getRequestParams();
                    Utils.sendPDFOrExcelRequest(postPanel, pdfURL, parametrs, "_blank");
                });
                actionItemCount++;
                menuBar.addItem(pdfItem);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        colConfig.setMinimumColumnWidth(70);
        colConfig.setMaximumColumnWidth(70);
        colConfig.setColumnSortable(false); //this column must be not sortable
        colConfig.setShow(true); //must be always True
        columns.add(colConfig); //1st column

        //Vacancy Number #2
        colConfig = new ColumnDefinitionConfig<VacancyItem, SimpleLink>(wfmStrings.number(), VacancyItem.VACANCY_ID, 70) {
            @Override
            public SimpleLink getCellValue(VacancyItem rowValue) {
                boolean isDraft = Constants.VACANCY_APPROVAL_STATUS_DRAFT.equals(rowValue.getApprovalStatusCode());
                if (isDraft) {
                    return new SimpleLink(rowValue.getNumberData() != null ? rowValue.getNumberData().getNumberString() : "", "vacancy|editVacancy/" + rowValue.getObjectID());
                } else {
                    return new SimpleLink(rowValue.getNumberData() != null ? rowValue.getNumberData().getNumberString() : "", "vacancy|summary/" + rowValue.getObjectID(), rowValue.getJobTitle(), rowValue.getNumberData() != null ? rowValue.getNumberData().getNumberString() : rowValue.getJobTitle());
                }
            }
        };
        colConfig.setMinimumColumnWidth(40);
        colConfig.setMaximumColumnWidth(60);
        colConfig.setColumnSortable(true);
        colConfig.setShow(true);
        columns.add(colConfig);

        //Vacancy Name #3
        colConfig = new ColumnDefinitionConfig<VacancyItem, SimpleLink>(wfmStrings.name(), VacancyItem.VACANCY_JOB_TITLE, 120) {
            @Override
            public SimpleLink getCellValue(VacancyItem rowValue) {
                boolean isDraft = Constants.VACANCY_APPROVAL_STATUS_DRAFT.equals(rowValue.getApprovalStatusCode());
                if (isDraft) {
                    return new SimpleLink(rowValue.getJobTitle(), "vacancy|editVacancy/" + rowValue.getObjectID());
                } else {
                    return getLink(rowValue.getJobTitle(), "vacancy|summary/" + rowValue.getObjectID(), rowValue.getNumberData() != null ? rowValue.getNumberData().getNumberString() : rowValue.getJobTitle(), rowValue.getJobTitle());
                }
            }
        };
        colConfig.setMinimumColumnWidth(70);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(true);
        columns.add(colConfig);

        //Position Title #4
        colConfig = new ColumnDefinitionConfig<VacancyItem, SelectItem>(wfmStrings.position(), VacancyItem.VACANCY_POSITION, 120) {

            @Override
            public SelectItem getCellValue(VacancyItem rowValue) {
                return new SelectItem(rowValue.getPositionItem().getId(), rowValue.getPositionItem().getName());
            }

            @Override
            public void setCellValue(VacancyItem rowValue, SelectItem cellValue) {
                rowValue.getPositionItem().setId(cellValue != null ? cellValue.getId() : null);
                rowValue.getPositionItem().setName(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };
        colConfig.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        colConfig.setMinimumColumnWidth(70);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(true);
        columns.add(colConfig);

        //Department #5
        colConfig = new ColumnDefinitionConfig<VacancyItem, SelectItem>(wfmStrings.department(), (VacancyItem.VACANCY_DEPARTMENT), 120) {
            @Override
            public SelectItem getCellValue(VacancyItem rowValue) {
                return new SelectItem(rowValue.getDepartment().getId(), rowValue.getDepartment().getName());
            }

            @Override
            public void setCellValue(VacancyItem rowValue, SelectItem cellValue) {
                rowValue.getDepartment().setId(cellValue != null ? cellValue.getId() : null);
                rowValue.getDepartment().setName(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };
        colConfig.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        colConfig.setMinimumColumnWidth(70);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(true);
        columns.add(colConfig);

        //Manager- Requester #6
        colConfig = new ColumnDefinitionConfig<VacancyItem, SelectItem>(hrmsStrings.orderedBy(), VacancyItem.VACANCY_MANAGER, 100) {
            @Override
            public SelectItem getCellValue(VacancyItem rowValue) {
                return new SelectItem(rowValue.getManager().getId(), rowValue.getManager().getName());
            }

            @Override
            public void setCellValue(VacancyItem rowValue, SelectItem cellValue) {
                rowValue.getManager().setId(cellValue != null ? cellValue.getId() : null);
                rowValue.getManager().setName(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };
        colConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        colConfig.setMinimumColumnWidth(70);
        colConfig.setColumnSortable(true);
        colConfig.setShow(true);
        columns.add(colConfig);

        //Status #7
        colConfig = new ColumnDefinitionConfig<VacancyItem, SelectItem>(wfmStrings.status(), VacancyItem.VACANCY_STATUS, 80) {
            @Override
            public SelectItem getCellValue(VacancyItem rowValue) {
                return (rowValue.getStatus() != null) ? new SelectItem(rowValue.getStatus().getId(), rowValue.getStatus().getName()) : null;
            }

            @Override
            public void setCellValue(VacancyItem rowValue, SelectItem cellValue) {
                rowValue.getStatus().setId(cellValue != null ? cellValue.getId() : null);
                rowValue.getStatus().setName(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };
        colConfig.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        colConfig.setMinimumColumnWidth(60);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(true);
        columns.add(colConfig);

        //Load Hidden Columns- it'll be hidden in to configure settings, user can activate it from settings/per user.

        //Approver field
        colConfig = new ColumnDefinitionConfig<VacancyItem, String>(wfmStrings.approver(), VacancyItem.VACANCY_APPROVER, 100) {
            @Override
            public String getCellValue(VacancyItem rowValue) {
                return rowValue.getApprover() != null ? rowValue.getApprover().getName() : "";
            }
        };
        colConfig.setMinimumColumnWidth(70);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        //Approval Status
        colConfig = new ColumnDefinitionConfig<VacancyItem, String>(hrmsStrings.approvalStatus(), VacancyItem.VACANCY_APPROVAL_STATUS, 70) {
            @Override
            public String getCellValue(VacancyItem rowValue) {
                return rowValue.getOverallStatus() != null && rowValue.getOverallStatus().getCode() != null ? rowValue.getOverallStatus().getCode() : "";
            }
        };
        colConfig.setMinimumColumnWidth(70);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        // project
        colConfig = new ColumnDefinitionConfig<VacancyItem, SelectItem>(Property.get(Constants.PROJECT, wfmStrings.project()), VacancyItem.PROJECT, 75) {
            @Override
            public SelectItem getCellValue(VacancyItem rowValue) {
                return new SelectItem(rowValue.getProjectId(), rowValue.getProjectName());
            }

            @Override
            public void setCellValue(VacancyItem rowValue, SelectItem cellValue) {
                rowValue.setProjectId(cellValue != null ? cellValue.getId() : null);
                rowValue.setProjectName(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };
        colConfig.setMinimumColumnWidth(75);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);


        //currency
        colConfig = new ColumnDefinitionConfig<VacancyItem, SelectItem>(wfmStrings.currency(), VacancyItem.VACANCY_CURRENCY, 75) {
            @Override
            public SelectItem getCellValue(VacancyItem rowValue) {
                return rowValue.getCurrency();
            }
        };
        colConfig.setMinimumColumnWidth(75);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        // Vacancy Close/Start Date
        colConfig = new ColumnDefinitionConfig<VacancyItem, String>(wfmStrings.startDate(), VacancyItem.VACANCY_START_DATE, 61) {
            @Override
            public String getCellValue(VacancyItem rowValue) {
                return DateUtils.format(rowValue.getStartDate()) + Utils.getHijriDate(rowValue.getStartDate());
            }

            @Override
            public void setCellValue(VacancyItem rowValue, String cellValue) {
                try {
                    if (cellValue != null && !"".equals(cellValue)) {
                        rowValue.setStartDate(DateUtils.parse(cellValue));
                        if (rowValue.getEndDate() != null)
                            if (!validate(rowValue.getStartDate(), rowValue.getEndDate()))
                                saveCellValue(rowValue);
                        if (rowValue.getEndDate() == null)
                            saveCellValue(rowValue);
                    }
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        colConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        colConfig.setMinimumColumnWidth(61);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        //Vacancy Close End Date
        colConfig = new ColumnDefinitionConfig<VacancyItem, String>(wfmStrings.endDate(), VacancyItem.VACANCY_END_DATE, 61) {
            @Override
            public String getCellValue(VacancyItem rowValue) {
                return DateUtils.format(rowValue.getEndDate()) + Utils.getHijriDate(rowValue.getEndDate());
            }

            @Override
            public void setCellValue(VacancyItem rowValue, String cellValue) {
                try {
                    if (cellValue != null && !"".equals(cellValue)) {
                        rowValue.setEndDate(DateUtils.parse(cellValue));
                        if (rowValue.getStartDate() != null)
                            if (!validate(rowValue.getStartDate(), rowValue.getEndDate()))
                                saveCellValue(rowValue);
                        if (rowValue.getStartDate() == null)
                            saveCellValue(rowValue);
                    }
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        colConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        colConfig.setMinimumColumnWidth(61);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        // Contract Start Date
        colConfig = new ColumnDefinitionConfig<VacancyItem, String>(wfmStrings.contractStart(), VacancyItem.VACANCY_CONTRACT_FROM, 61) {
            @Override
            public String getCellValue(VacancyItem rowValue) {
                return DateUtils.format(rowValue.getContractFrom());
            }

            @Override
            public void setCellValue(VacancyItem rowValue, String cellValue) {
                try {
                    if (cellValue != null && !"".equals(cellValue)) {
                        rowValue.setContractFrom(DateUtils.parse(cellValue));
                        if (rowValue.getContractTo() != null)
                            if (!validate(rowValue.getContractFrom(), rowValue.getContractTo()))
                                saveCellValue(rowValue);
                        if (rowValue.getContractTo() == null)
                            saveCellValue(rowValue);
                    }

                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        colConfig.setMinimumColumnWidth(61);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        // Contract End Date
        colConfig = new ColumnDefinitionConfig<VacancyItem, String>(wfmStrings.contractEnd(), VacancyItem.VACANCY_CONTRACT_TO, 61) {
            @Override
            public String getCellValue(VacancyItem rowValue) {
                return DateUtils.format(rowValue.getContractTo());
            }

            @Override
            public void setCellValue(VacancyItem rowValue, String cellValue) {
                try {
                    if (cellValue != null && !"".equals(cellValue)) {
                        rowValue.setContractTo(DateUtils.parse(cellValue));
                        if (rowValue.getContractFrom() != null)
                            if (!validate(rowValue.getContractFrom(), rowValue.getContractTo()))
                                saveCellValue(rowValue);
                        if (rowValue.getContractFrom() == null)
                            saveCellValue(rowValue);
                    }
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        colConfig.setMinimumColumnWidth(61);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        //Location
        colConfig = new ColumnDefinitionConfig<VacancyItem, SelectItem>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), VacancyItem.VACANCY_LOCATION, 75) {
            @Override
            public SelectItem getCellValue(VacancyItem rowValue) {
                return new SelectItem(rowValue.getLocationItem().getID(), rowValue.getLocationItem().getName());
            }

        };
        colConfig.setMinimumColumnWidth(75);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(false);
        colConfig.setShow(false);
        columns.add(colConfig);

        //Required degree
        colConfig = new ColumnDefinitionConfig<VacancyItem, SelectItem>(wfmStrings.requiredDegree(), VacancyItem.VACANCY_REQUIRED_DEGREE, 69) {
            @Override
            public SelectItem getCellValue(VacancyItem rowValue) {
                if (rowValue.getRequiredDegree() != null)
                    return new SelectItem(rowValue.getRequiredDegree().getId(), rowValue.getRequiredDegree().getName());
                return null;
            }

            public void setCellValue(VacancyItem rowValue, SelectItem cellValue) {
                rowValue.getRequiredDegree().setId(cellValue != null ? cellValue.getId() : null);
                rowValue.getRequiredDegree().setName(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };
        colConfig.setMinimumColumnWidth(69);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        //Vacancy Type
        colConfig = new ColumnDefinitionConfig<VacancyItem, SelectItem>(wfmStrings.vacancyType(), VacancyItem.VACANCY_TYPE, 70) {

            @Override
            public SelectItem getCellValue(VacancyItem rowValue) {
                return new SelectItem(rowValue.getVacancyType(), rowValue.getVacancyTypeName());
            }

            @Override
            public void setCellValue(VacancyItem rowValue, SelectItem cellValue) {
                rowValue.setVacancyType(cellValue != null ? cellValue.getId() : null);
                rowValue.setVacancyTypeName(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };
        colConfig.setMinimumColumnWidth(70);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        // Proposed Salary
        colConfig = new ColumnDefinitionConfig<VacancyItem, String>(wfmStrings.proposedSalary(), (VacancyItem.VACANCY_PROPOSED_SALARY), 80) {
            @Override
            public String getCellValue(VacancyItem rowValue) {
                try {
                    return salaryFormat.format(rowValue.getProposedSalary() != null ? new BigDecimal(rowValue.getProposedSalary()) : BigDecimal.ZERO);
                } catch (Exception e) {
                    return rowValue.getProposedSalary();
                }
            }

            @Override
            public void setCellValue(VacancyItem rowValue, String cellValue) {
                BigDecimal amount = BigDecimal.valueOf(salaryFormat.parse(cellValue));
                rowValue.setProposedSalary(amount.toString());
                saveCellValue(rowValue);
            }
        };
        colConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        colConfig.setMinimumColumnWidth(60);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        // Job Type
        colConfig = new ColumnDefinitionConfig<VacancyItem, SelectItem>(wfmStrings.jobType(), VacancyItem.VACANCY_JOB_TYPE, 70) {
            @Override
            public SelectItem getCellValue(VacancyItem rowValue) {
                if (rowValue.getJobType() != null) {
                    return new SelectItem(rowValue.getJobType().getId(), rowValue.getJobType().getName());
                } else {
                    return null;
                }
            }

            @Override
            public void setCellValue(VacancyItem rowValue, SelectItem cellValue) {
                rowValue.getJobType().setId(cellValue != null ? cellValue.getId() : null);
                rowValue.getJobType().setName(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                saveCellValue(rowValue);

            }
        };
        colConfig.setMinimumColumnWidth(70);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        // Requested Gender
        colConfig = new ColumnDefinitionConfig<VacancyItem, SelectItem>(wfmStrings.sexDesire(), VacancyItem.VACANCY_GENDER, 70) {
            @Override
            public SelectItem getCellValue(VacancyItem item) {
                if (item.getGender() != null) {

                    switch (item.getGender()) {
                        case Constants.MALE:
                            return new SelectItem(0, wfmStrings.male(), Constants.MALE);
                        case Constants.FEMALE:
                            return new SelectItem(1, wfmStrings.female(), Constants.FEMALE);
                        case Constants.IRRELEVANT_GENDER:
                            return new SelectItem(2, wfmStrings.irrelevantgender(), Constants.IRRELEVANT_GENDER);
                    }
                }
                return null;
            }

            @Override
            public void setCellValue(VacancyItem rowValue, SelectItem cellValue) {
                if (wfmStrings.male().equals(cellValue.getName())) {
                    rowValue.setGender(Constants.MALE);
                } else if (wfmStrings.female().equals(cellValue.getName())) {
                    rowValue.setGender(Constants.FEMALE);
                } else if (wfmStrings.irrelevantgender().equals(cellValue.getName())) {
                    rowValue.setGender(Constants.IRRELEVANT_GENDER);
                } else {
                    rowValue.setGender(cellValue.getName());
                }
                saveCellValue(rowValue);
            }
        };
        colConfig.setMinimumColumnWidth(60);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);
        //Historical Data-Columns
        //Created By
        colConfig = new ColumnDefinitionConfig<VacancyItem, String>(wfmStrings.createdBy(), PositionItem.CREATED_BY, 100) {
            @Override
            public String getCellValue(VacancyItem rowValue) {
                return rowValue.getCreatedBy() != null ? rowValue.getCreatedBy() : "";
            }
        };
        colConfig.setMinimumColumnWidth(70);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        //Created Date
        colConfig = new ColumnDefinitionConfig<VacancyItem, String>(wfmStrings.createdDate(), PositionItem.CREATED_DATE, 100) {
            @Override
            public String getCellValue(VacancyItem rowValue) {
                return rowValue.getCreatedDate() != null ? DateUtils.formatInternalShort1(rowValue.getCreatedDate()) : "";
            }
        };
        colConfig.setMinimumColumnWidth(70);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        //Modified By
        colConfig = new ColumnDefinitionConfig<VacancyItem, String>(wfmStrings.modifiedBy(), PositionItem.MODIFIED_BY, 100) {
            @Override
            public String getCellValue(VacancyItem rowValue) {
                return rowValue.getModifiedBy() != null ? rowValue.getModifiedBy() : "";
            }
        };
        colConfig.setMinimumColumnWidth(70);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        //Modified Date
        colConfig = new ColumnDefinitionConfig<VacancyItem, String>(wfmStrings.modifiedDate(), PositionItem.MODIFIED_DATE, 100) {
            @Override
            public String getCellValue(VacancyItem rowValue) {
                return rowValue.getModifiedDate() != null ? DateUtils.formatInternalShort1(rowValue.getModifiedDate()) : "";
            }
        };
        colConfig.setMinimumColumnWidth(70);
        colConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        colConfig.setColumnSortable(true);
        colConfig.setShow(false);
        columns.add(colConfig);

        initCellEdit(CustomColumnDefinitionConfig.getEditableColumns(columns));
        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    //quick edit function
    private void initCellEdit(Map<String, CustomColumnDefinitionConfig> columns) {
        for (final Map.Entry<String, CustomColumnDefinitionConfig> entry : columns.entrySet()) {
            InlineCellEditor widget = null;
            CustomColumnDefinitionConfig column = entry.getValue();
            if (VacancyItem.VACANCY_JOB_TITLE.equals(entry.getKey()) || VacancyItem.VACANCY_PROPOSED_SALARY.equals(entry.getKey())
                    || VacancyItem.VACANCY_JOB_REQUIREMENT.equals(entry.getKey())) {
                widget = new TextBoxCellEditor<String>() {
                    @Override
                    protected String getValue() {
                        return getText();
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        setText(cellValue);
                    }
                };
            } else if (VacancyItem.VACANCY_STATUS.equals(entry.getKey())
                    || VacancyItem.VACANCY_MANAGER.equals(entry.getKey())
                    || VacancyItem.VACANCY_JOB_TYPE.equals(entry.getKey())
                    || VacancyItem.VACANCY_REQUIRED_DEGREE.equals(entry.getKey())
                    || VacancyItem.VACANCY_TYPE.equals(entry.getKey())

            ) {
                widget = new DropDownCellEditor<SelectItem>() {
                    @Override
                    protected SelectItem getValue() {
                        return getListBox().getSelectedItem(true);
                    }

                    @Override
                    protected void setValue(SelectItem cellValue) {
                        getListBox().setAllowFirstItem(true);
                        setItemsAndSelect(getListBox(), entry.getKey(), cellValue);
                        if (cellValue == null || cellValue.getId() == null) {
                            getListBox().setSelectedNullLabel();
                        } else {
                            getListBox().setSelected(cellValue.getId());
                        }
                    }
                };

            } else if (VacancyItem.VACANCY_GENDER.equals(entry.getKey())) {
                widget = getGenderCell();
                getQuickGenderSaveData((DropDownCellEditor) widget);
            } else if (VacancyItem.VACANCY_START_DATE.equals(entry.getKey()) || VacancyItem.VACANCY_END_DATE.equals(entry.getKey())
                    || VacancyItem.VACANCY_CONTRACT_FROM.equals(entry.getKey()) || VacancyItem.VACANCY_CONTRACT_TO.equals(entry.getKey())) {
                widget = new DateTimePickerCellEditor<String>(true) {
                    @Override
                    protected String getValue() {
                        return DateUtils.format1(getDate());
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        try {
                            if (cellValue != null && !"".equals(cellValue)) {
                                setDate(DateUtils.parse(cellValue), true);
                            }
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }
            if (VacancyItem.PROJECT.equals(entry.getKey())) {
                widget = getLookUpWidget();
            }
            if (widget != null) {
                column.setCellEditor(widget);
                column.setCellChangesSave((CellChange<VacancyItem>) this::saveVacancyEditCellValue);
            }
        }
    }

    private void setItemsAndSelect(final DataListBox listBox, String key, SelectItem selectedItem) {
        if (listBox.getItems() == null || listBox.getItems().length < 1) {
            if (VacancyItem.VACANCY_STATUS.equals(key) && !isStatusDisabled) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getVacancyStatusListItem(new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        if (result != null && result.length > 0) {
                            listBox.setItems(result);
                        }
                        LoadingPanel.loading(false);
                    }
                });
            } else if (VacancyItem.VACANCY_MANAGER.equals(key)) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getVacancyItem(objectID, new AbstractAsyncCallback<VacancyItem>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(VacancyItem result) {
                        if (result != null) {
                            listBox.setItems(result.getManagers());
                        }
                        LoadingPanel.loading(false);
                    }
                });
            } else if (VacancyItem.VACANCY_POSITION.equals(key)) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getVacancyItem(objectID, new AbstractAsyncCallback<VacancyItem>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(VacancyItem result) {
                        if (result != null) {
                            listBox.setItems(result.getPositions());
                        }
                        LoadingPanel.loading(false);
                    }
                });
            } else if (VacancyItem.VACANCY_LOCATION.equals(key)) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getVacancyItem(objectID, new AbstractAsyncCallback<VacancyItem>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(VacancyItem result) {
                        if (result != null) {
                            listBox.setItems(result.getLocations());
                        }
                        LoadingPanel.loading(false);
                    }
                });
            } else if (VacancyItem.VACANCY_TYPE.equals(key)) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getVacancyTypes(new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        if (result != null && result.length > 0) {
                            listBox.setItems(result);
                        }
                        LoadingPanel.loading(false);
                    }
                });
            } else if (VacancyItem.VACANCY_JOB_TYPE.equals(key)) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getVacancyJobType(new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        if (result != null && result.length > 0) {
                            listBox.setItems(result);
                        }
                        LoadingPanel.loading(false);
                    }
                });
            } else if (VacancyItem.VACANCY_REQUIRED_DEGREE.equals(key)) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getVacancyReqDegree(new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        if (result != null && result.length > 0) {
                            listBox.setItems(result);
                        }
                        LoadingPanel.loading(false);
                    }
                });
            } else if (VacancyItem.VACANCY_DEPARTMENT.equals(key)) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getVacancyItem(objectID, new AbstractAsyncCallback<VacancyItem>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(VacancyItem result) {
                        if (result != null) {
                            listBox.setItems(result.getDepartmentItems());
                        }
                        LoadingPanel.loading(false);
                    }
                });
            }
        }
    }

    private LookUpCellEditor<SelectItem> getLookUpWidget() {
        final CRMLookUp lookUp = new CRMLookUp(LookUpConstants.PROJECT);
        return new LookUpCellEditor<SelectItem>(lookUp) {
            @Override
            protected SelectItem getValue() {
                return getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                lookUp.clear();
                setSelectItem(cellValue);
            }
        };
    }

    private DropDownCellEditor<SelectItem> getGenderCell() {
        return new DropDownCellEditor<SelectItem>() {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                getListBox().setSelected(cellValue.getId());
            }
        };
    }
    private void getQuickGenderSaveData(final DropDownCellEditor<String> gen) {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem(0, wfmStrings.male());
        items[1] = new SelectItem(1, wfmStrings.female());
        items[2] = new SelectItem(2, wfmStrings.irrelevantgender());
        gen.setItems(items);
    }

    private void saveVacancyEditCellValue(VacancyItem rowValue, String columnCodeName) {
        RecruitmentService.App.get().saveVacancyEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
        });
    }

    private boolean validate(Date startDate, Date endDate) {
        if (!startDate.before(endDate)) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return true;
        }
        return false;
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        initVacancyList(new ListingFilterParameter(), null, container);
    }

    private ListingRequestProvider<VacancyItem> getListingRequestProvider() {
        return (filterParameters, callback) -> initVacancyList(filterParameters, callback, null);
    }

    private void initVacancyList(ListingFilterParameter filterParameters, ListingCallback<VacancyItem> callback, Span container) {
        filterParameters.setPositionID(positionId);
        RecruitmentService.App.get().getVacancyList(filterParameters, new AsyncCallback<ListResult<VacancyItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<VacancyItem> result) {
                totalCount = result.getTotal();
                if (callback != null) {
                    callback.onSuccess(result);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (totalCount > 0) {
                        statisticShortcut.setText(countFormat(totalCount));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? VacancyListView.this::addNewVacancy : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            RbacService.App.get().getVacancyFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    callback.onFailure(throwable);
                                }

                                @Override
                                public void success(FacetFilterRpc facetFilterRpc) {
                                    callback.onSuccess(facetFilterRpc);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return Utils.hasPermission(PermissionConstants.VACANCY_LIST_FILTER) ? getFacetContentConfigure() : null;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToAdd()) {
                    ActionButton addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                    MenuBar menuBar = new MenuBar(true);

                    if (Utils.hasPermission(PermissionConstants.HRMS_ADD_VACANCY)) {
                        MenuPopItem newAdd = new MenuPopItem(wfmStrings.vacancy());
                        newAdd.getElement().setId("add_vacancy");
                        newAdd.setCommand(() -> addNewVacancy());
                        menuBar.addItem(newAdd);
                    }

                    if (Utils.hasPermission(PermissionConstants.HRMS_QUICK_ADD_VACANCY)) {
                        MenuPopItem quickAdd = new MenuPopItem(wfmStrings.quickAdd());
                        quickAdd.getElement().setId("quickAdd");
                        quickAdd.setCommand(() -> quickAddBox.show());
                        menuBar.addItem(quickAdd);
                    }

                    addNew.setMenu(menuBar);

                    return addNew;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption option, MaterialDropDown menuContainer) {
                option.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                if (hasPermissionToAdd()) {
                    DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.noVacanciesText());
                    message.setHref(clickEvent -> quickAddBox.show());
                    message.setTextBeforeLink(hrmsStrings.noVacanciesLink());
                    emptyDataTable.initEmptyDataTable(message);
                }
            }

            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.VACANCY_CUSTOMIZE_LIST);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.HRMS_EDIT_VACANCY);
            }

            @Override
            public Integer getTypeParentId() {
                return null;
            }
        };
    }

    private SinksContainer addNewVacancy() {
        if (positionId != null) {
            return SinksContainerFactory.entryPoint.onHistoryChanged("vacancy|add/add/positionId/" + positionId);
        } else {
            return SinksContainerFactory.entryPoint.onHistoryChanged("vacancy|add/add");
        }
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());

        contentConfigure.addContentConfigure(FacetContentType.VacancyFacetFilter.getContentCode()[6], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrVacancyRepresenter.FIELD_VACANCY_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrVacancyRepresenter.FIELD_VACANCY_STATUS_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.VacancyFacetFilter.getContentCode()[0], wfmStrings.name(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrVacancyRepresenter.FIELD_JOB_TITLE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrVacancyRepresenter.FIELD_JOB_TITLE;
            }

            @Override
            public boolean isConditionItemId() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.VacancyFacetFilter.getContentCode()[5], wfmStrings.position(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrVacancyRepresenter.FIELD_POSITION_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrVacancyRepresenter.FIELD_POSITION_ID_NAME;
            }
        });


        contentConfigure.addContentConfigure(FacetContentType.VacancyFacetFilter.getContentCode()[8], wfmStrings.department(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrVacancyRepresenter.FIELD_DEPARTMENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrVacancyRepresenter.FIELD_DEPARTMENT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

        });


        contentConfigure.addContentConfigure(FacetContentType.VacancyFacetFilter.getContentCode()[1], wfmStrings.jobType(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrVacancyRepresenter.FIELD_JOB_TYPE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrVacancyRepresenter.FIELD_JOB_TYPE_NAME_ID;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

        });

        contentConfigure.addContentConfigure(FacetContentType.VacancyFacetFilter.getContentCode()[2], wfmStrings.jobFamily(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrVacancyRepresenter.FIELD_JOB_FAMILY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrVacancyRepresenter.FIELD_JOB_FAMILY_NAME_ID;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

        });


        contentConfigure.addContentConfigure(FacetContentType.VacancyFacetFilter.getContentCode()[3], hrmsStrings.orderedBy(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrVacancyRepresenter.FIELD_MANAGER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrVacancyRepresenter.FIELD_MANAGER_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

        });

        contentConfigure.addContentConfigure(FacetContentType.VacancyFacetFilter.getContentCode()[4], Property.get(Constants.PROJECT, wfmStrings.project()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrVacancyRepresenter.FIELD_PROJECT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrVacancyRepresenter.FIELD_PROJECT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

        });

        contentConfigure.addContentConfigure(FacetContentType.VacancyFacetFilter.getContentCode()[9], wfmStrings.currency(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrVacancyRepresenter.FIELD_CURRENCY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrVacancyRepresenter.FIELD_CURRENCY_ID_NAME;
            }

//            @Override
//            public boolean isShowFacetConttentFilter() {
//                return false;
//            }

        });

        contentConfigure.addContentConfigure(FacetContentType.VacancyFacetFilter.getContentCode()[7], wfmStrings.requiredDegree(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrVacancyRepresenter.FIELD_RDEGREE_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrVacancyRepresenter.FIELD_RDEGREE_STATUS_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        return contentConfigure;
    }

    private void initQuickAddView() {

        quickAddBox = new KpiSideNavBox();
        setStyleName(quickAddBox.getElement(), "quick-add", true);
        VacancyQuickAddForm quickAddForm = new VacancyQuickAddForm();
        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.add() + " " + Property.get(VACANCY, wfmStrings.vacancy()));
        WfmButton2 saveBtn = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        saveBtn.addClickHandler(event -> {
            saveBtn.setEnabled(false);
            if (quickAddForm.validate()) {
                quickAddForm.save();
            } else {
                saveBtn.setEnabled(true);
            }
        });

        quickAddForm.setCommand(new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                saveBtn.setEnabled(true);
                quickAddForm.clearForm();
                quickAddBox.hide();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VACANCY_ADDED, id, VacancyListView.this);
            }
        });
        quickAddBox.addOpeningHandler(event -> quickAddForm.getVacancyQuickData());
        quickAddBox.addHeader(header);
        quickAddBox.addBody(quickAddForm);
        quickAddBox.addFooter(saveBtn);
        saveBtn.getElement().setId("saveButton");
    }

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

    public String getPropertyCode() {
        return VACANCY;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    // For the future it might be change
    private void fillFormPropertyMap() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Vacancy, LayoutRPC.VACANCY_FORM, new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(final CompanyCfAndPropertyItems result) {
                if (result != null) {
                    formPropertyMap = result.getFormPropertyMap();
                    if (formPropertyMap != null) {
                        isStatusDisabled = formPropertyMap.get(CustomFormConstants.VACANCY.STATUS).isDisabled();
                    }
                }
            }
        });


    }
}
