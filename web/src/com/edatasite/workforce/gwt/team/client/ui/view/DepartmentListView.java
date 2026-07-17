package com.edatasite.workforce.gwt.team.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrDepartmentRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.HashSet;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.DEPARTMENT_CUSTOMIZE_LIST;

/**
 * Created by IntelliJ IDEA. User: Anvar Akramov Date: Jan 14, 2008 Time:
 * 7:17:09 PM To change this template use File | Settings | File Templates.
 */
public class DepartmentListView extends BaseListView implements Constants {

    private final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.DEPARTMENT, null);


    public static final String FROM_HRMS_SECTION = "FROM_HRMS_SECTION";
    public static final String FROM_PM_SECTION = "FROM_PM_SECTION";
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private String fromSection;
    private Integer locationId;

    private ListingPanel<TeamListItem> listPanel;
    protected HashSet selectedItems = new HashSet();

    public DepartmentListView(String fromSection) {
        super(DEPARTMENT_LIST);
        setDescription(property.getPlural(wfmStrings.departments()));
        this.fromSection = fromSection;
    }

    public DepartmentListView(Integer locationId) {
        super(DEPARTMENT_LIST);
        setDescription(property.getPlural(wfmStrings.departments()));
        this.locationId = locationId;
    }

    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.HRMS_CONTEXT, PermissionConstants.HRMS_DEPARTMENT_LIST);
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel<>(ListPanelType.DepartmentListPanel, getColumnConfig(), getListProvider(), getListDesign());

        listPanel.setPDFListener(event -> {
            String pdfURL = CommandConstants.PDF_URL + "/departmentListPDFHandler";
            ListingFilterParameter fp = listPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listPanel.callListPDF(pdfURL, fp);
        });
        listPanel.setExcelListener(event -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadDepartmentListExcel";
            ListingFilterParameter fp = listPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listPanel.callListExcel(excelURL, fp);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DEPARTMENT_ADD, DepartmentListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DEPARTMENT_EDIT, DepartmentListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADD, DepartmentListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_DELETE, DepartmentListView.this, (sender, args) -> listPanel.reloadPage());

        listPanel.addSelectionRowHandler(selectedRows -> selectedItems = selectedRows);

        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        String editPermission = "";
        String deletePermission = "";
        String summaryPermission = "";
        if (FROM_HRMS_SECTION.equals(fromSection)) {
            editPermission = PermissionConstants.HRMS_EDIT_DEPARTMENT;
            deletePermission = PermissionConstants.HRMS_DEPARTMENT_REMOVE;
            summaryPermission = PermissionConstants.HRMS_DEPARTMENT_SUMMARY_VIEW;
        }
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig columnConfig;

        final String finalEditPermission = editPermission;
        final String finalDeletePermission = deletePermission;
        final String finalSummaryPermission = summaryPermission;

        columnConfig = new ColumnDefinitionConfig<TeamListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final TeamListItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (Utils.hasPermission(finalSummaryPermission)) {
                    MenuPopItem teamSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-team-small");
                    teamSummary.getElement().setId("Department_summary_button");
                    teamSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("department|summary/" + item.getObjectID(), item.getDepartmentCode()));
                    actionItemCount++;
                    menuBar.addItem(teamSummary);
                }

                if (Utils.hasPermission(finalEditPermission) || Utils.isSettings()) {
                    MenuPopItem teamEdit = new MenuPopItem(wfmStrings.edit(), "icon-issue-edit-small");
                    teamEdit.getElement().setId("department_edit_button");
                    teamEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("department|edit/" + item.getObjectID(), item.getDepartmentCode()));
                    actionItemCount++;
                    menuBar.addItem(teamEdit);
                }
                  //department activate
                if (Utils.hasPermission(finalDeletePermission) || Utils.isSettings()) {
                    MenuPopItem activate = new MenuPopItem(wfmStrings.activate());
                    activate.getElement().setId("department_active_button");
                    activate.setCommand(() ->  DepartmentService.App.get().activateOrDisctivateTeam(item.getObjectID(),true, new AbstractAsyncCallback<Void>() {
                                @Override
                                public void failure(Throwable caught) {
                                    Info.show(wfmStrings.couldNotActivate(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Void result) {
                                    Info.show( item.getName() +" "+  wfmStrings.is()+" "+ wfmStrings.activated(), Info.Type.INFO);
                                    listPanel.reloadPage();
                                }
                            }));
                    actionItemCount++;
                    menuBar.addItem(activate);
                }
                //department inactive
                if (Utils.hasPermission(finalDeletePermission) || Utils.isSettings()) {
                    MenuPopItem inactivate = new MenuPopItem(wfmStrings.inactive());
                    inactivate.getElement().setId("department_edit_button");
                    inactivate.setCommand(() -> DepartmentService.App.get().activateOrDisctivateTeam(item.getObjectID(),false,new AbstractAsyncCallback<Void>()
                    {
                        @Override
                        public void failure(Throwable caught) {
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            Info.show(" Department " + item.getName() + " is "+ wfmStrings.inactive(), Info.Type.INFO);
                            listPanel.reloadPage();
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(inactivate);
                }

                if (Utils.hasPermission(finalDeletePermission)) {
                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile file--DepartmentListView");
                    removeItem.getElement().setId("department_delete_button");
                    Integer headCount = item.getHeadCount() != null && !"".equals(item.getHeadCount()) ? Integer.valueOf(item.getHeadCount()) : 0;
                    final DepartmentRemovePopup departmentDelete = new DepartmentRemovePopup(item.getObjectID(), headCount, listPanel);
                    removeItem.setCommand(() -> {
                        LoadingPanel.loading(true);
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
                    });
                    actionItemCount++;
                    menuBar.addItem(removeItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);

                return toolItem.getAction();
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<TeamListItem, Widget>(wfmStrings.number(), TeamListItem.CODE, 140) {
            @Override
            public Widget getCellValue(final TeamListItem item) {
                Label label = new Label(item.getDepartmentCode() != null ? item.getDepartmentCode() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(event -> {
                    if (Utils.hasPermission(finalSummaryPermission)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("department|summary/" + item.getObjectID(), item.getDepartmentCode());
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                });
                return label;
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<TeamListItem, Widget>(wfmStrings.name(), TeamListItem.NAME, 140) {
            @Override
            public Widget getCellValue(final TeamListItem item) {
                Label label = new Label(item.getName() != null ? item.getName() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(event -> {
                    if (Utils.hasPermission(finalSummaryPermission)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("department|summary/" + item.getObjectID(), item.getDepartmentCode());
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                });
                return label;
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);

        //PARENT_DEPARTMENT rename
        columnConfig = new ColumnDefinitionConfig<TeamListItem, String>(wfmStrings.reportsTo(), TeamListItem.PARENT_DEPARTMENT, 140) {
            @Override
            public String getCellValue(TeamListItem item) {
                return item.getParentDepartment() != null ? item.getParentDepartment().getName() : "";
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);
        columnConfig.setColumnSortable(true);

        columnConfig = new ColumnDefinitionConfig<TeamListItem, String>(wfmStrings.leader(), TeamListItem.LEADER_NAME, 120) {
            @Override
            public String getCellValue(TeamListItem item) {
                return item.getLeader();
            }
        };
        columnConfig.setColumnSortable(true);
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<TeamListItem, String>(wfmStrings.headCount(), TeamListItem.HEADCOUNT, 75) {
            @Override
            public String getCellValue(TeamListItem item) {
                return item.getHeadCount();
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setColumnSortable(true);
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<TeamListItem, String>(wfmStrings.status(), TeamListItem.STATUS, 40) {
            @Override
            public String getCellValue(TeamListItem item) {
                return item.isActive() ? wfmStrings.active() : wfmStrings.inactive();
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);


        columnConfig = new ColumnDefinitionConfig<TeamListItem, String>(wfmStrings.startDate(), TeamListItem.START_DATE, 75) {
            @Override
            public String getCellValue(TeamListItem item) {
                return DateUtils.format(item.getStartDate()) + Utils.getHijriDate(item.getStartDate());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(40);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_DEPARTMENT_LEADER)) {
            columnConfig = new ColumnDefinitionConfig<TeamListItem, String>(property.getSingular(wfmStrings.departmentLeader2(), wfmStrings.department()), TeamListItem.LEADER2_NAME, 110) {
                @Override
                public String getCellValue(TeamListItem item) {
                    return item.getLeader2();
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            columnConfig = new ColumnDefinitionConfig<TeamListItem, String>(property.getSingular(wfmStrings.departmentLeader3(), wfmStrings.department()), TeamListItem.LEADER3_NAME, 110) {
                @Override
                public String getCellValue(TeamListItem item) {
                    return item.getLeader3();
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            columnConfig = new ColumnDefinitionConfig<TeamListItem, String>(property.getSingular(wfmStrings.departmentLeader4(), wfmStrings.department()), TeamListItem.LEADER4_NAME, 110) {
                @Override
                public String getCellValue(TeamListItem item) {
                    return item.getLeader4();
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            columnConfig = new ColumnDefinitionConfig<TeamListItem, String>(property.getSingular(wfmStrings.departmentLeader5(), wfmStrings.department()), TeamListItem.LEADER5_NAME, 110) {
                @Override
                public String getCellValue(TeamListItem item) {
                    return item.getLeader5();
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }

        columnConfig = new ColumnDefinitionConfig<TeamListItem, String>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), TeamListItem.LOCATION_NAME, 80) {
            @Override
            public String getCellValue(TeamListItem item) {
                return item.getLocation() != null ? item.getLocation().getName() : "N/A";
            }
        };
        columnConfig.setMinimumColumnWidth(80);
        columnConfig.setShow(false);
        columns.add(columnConfig);



        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> RbacService.App.get().getDepartmentFacetFilterData(data, new AsyncCallback<FacetFilterRpc>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                if (callback != null) {
                                    callback.onFailure(throwable);
                                }
                            }

                            @Override
                            public void onSuccess(FacetFilterRpc facetFilterRpc) {
                                if (callback != null) {
                                    callback.onSuccess(facetFilterRpc);
                                }
                            }
                        });
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());
                        contentConfigure.addContentConfigure(FacetContentType.DepartmentFilter.getContentCode()[0],Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), new FacetFieldConfigure() {
                            @Override
                            public String getSolrFieldCriteriaName() {
                                return SolrDepartmentRepresenter.FIELD_LOCATION_ID;
                            }

                            @Override
                            public String getSolrFacetFieldName() {
                                return SolrDepartmentRepresenter.FIELD_LOCATION_ID_NAME;
                            }
                        });

                        contentConfigure.addContentConfigure(FacetContentType.DepartmentFilter.getContentCode()[1], wfmStrings.department(), new FacetFieldConfigure() {
                            @Override
                            public String getSolrFieldCriteriaName() {
                                return SolrDepartmentRepresenter.FIELD_DEPARTMENT_PARENT_ID;
                            }

                            @Override
                            public String getSolrFacetFieldName() {
                                return SolrDepartmentRepresenter.FIELD_DEPARTMENT_PARENT_ID_NAME;
                            }
                        });

                        contentConfigure.addContentConfigureDateListBox(SolrDepartmentRepresenter.FIELD_CREATED_DATE, wfmStrings.createdDate());
                        contentConfigure.addContentConfigureDateListBox(SolrDepartmentRepresenter.FIELD_MODIFIED_DATE, wfmStrings.modifiedDate());
                        return contentConfigure;
                    }

                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                String permissionRole = "";
                if (FROM_HRMS_SECTION.equals(fromSection)) {
                    permissionRole = PermissionConstants.HRMS_ADD_NEW_DEPARTMENT;
                }


                boolean hasChild = false;
                ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
                MenuBar menu = new MenuBar(true);
                if (Utils.hasPermission(permissionRole) || Utils.isSettings()) {
                    hasChild = true;
                    MenuPopItem addDepartment = new MenuPopItem(wfmStrings.add());
                    addDepartment.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("department|add/add"));
                    menu.addItem(addDepartment);
                }

                if (Utils.hasPermission(permissionRole)) {
                    MenuPopItem addNewMultiDepartment = new MenuPopItem("<span class='list-action-menu-icon'>" + wfmStrings.bulkAdd() + "</span>");
                    addNewMultiDepartment.setCommand(() -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("multidepartment|add/add");

                    });
                    menu.addItem(addNewMultiDepartment);
                    hasChild = true;
                }

                if (Utils.hasPermission(permissionRole)) {
                    MenuPopItem addNewMultiDepartment = new MenuPopItem("<span class='list-action-menu-icon'>" + wfmStrings.bulkUpdate() + "</span>");
                    addNewMultiDepartment.setCommand(() -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("multidepartment|add/add/0");

                    });
                    menu.addItem(addNewMultiDepartment);
                    hasChild = true;
                }
                newItem.setMenu(menu);
                if (hasChild) {
                    return newItem;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo("importdepartment|add/add/" + imp.getObjectId());
                    }
                });
                ImportFileActionLink link = new ImportFileActionLink();
                link.addClickHandler(ch -> imp.open());
                menuContainer.add(link);

                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message;

                if (Utils.isSettings()) {
                    message = new DefaultNoItemsMessage(property.getPlural(wfmStrings.noDepartmentTextAdmin(), wfmStrings.department()));
                    message.setTextBeforeLink(property.getPlural(wfmStrings.noDepartmentLink(), wfmStrings.department()));
                    message.setHref("department|add/add");
                } else {
                    message = new DefaultNoItemsMessage(property.getSingular(settingsStrings.notAssignedToDepartmentMessage(), wfmStrings.department()));
                    message.setTextBeforeLink(property.getSingular(settingsStrings.notAssignedToDepartmentBeforeLinkMessage(), wfmStrings.department()));
                }

                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(DEPARTMENT_CUSTOMIZE_LIST);
            }
        };
    }

    private ListingRequestProvider<TeamListItem> getListProvider() {

        return (filterParametrs, listingCallback) -> {
            if (this.locationId != null) {
                filterParametrs.setLocationId(this.locationId);
            }
            filterParametrs.setAscending(false);
            DepartmentService.App.get().getTeams(filterParametrs, new AbstractAsyncCallback<ListResult<TeamListItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(ListResult<TeamListItem> teamListItemListResult) {
                    listingCallback.onSuccess(teamListItemListResult);
                }
            });
        };
    }

    @Override
    public String getIconStyle() {
        return "departament departament-list";
    }

    public ImageResource getIconImage() {
        return null;
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

    @Override
    public String getPropertyCode() {
        return DEPARTMENT_LIST;
    }

}
