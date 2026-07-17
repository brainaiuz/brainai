package com.edatasite.workforce.gwt.hrms.client.ui;
//Position List logic

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPositionRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
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
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.HashMap;

public class PositionListView extends BaseListView implements CommandConstants, Constants {
    private Integer departmentId;
    private ListingPanel<PositionItem> listingTable; //get-set-all fields
    private int totalCount = 0;
    private final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.POSITION, null); //import widget sample

    public PositionListView() {
        super("hrmsPositions", Property.getPluralWithObjectCode(Constants.POSITION1, wfmStrings.positions()));
    }

    public PositionListView(Integer departmentId) {
        super("hrmsPositions", Property.getPluralWithObjectCode(Constants.POSITION1, wfmStrings.positions()));
        this.departmentId = departmentId;
    }

    @Override
    public String getIconStyle() {
        return "hrms position-list";
    }

    @Override
    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.PositionsPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());
        listingTable.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> savePositionEditCellValue((PositionItem) rowValue, columnCodeName));

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_POSITION_ADD_EDIT, PositionListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_POSITION_DELETE, PositionListView.this, (sender, args) -> listingTable.reloadPage());

        //download pdf
        listingTable.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/positionListPDFHandler";
            listingTable.callListPDF(pdfURL, listingTable.getFilterParametrs());
        });
        //download xls-url
        listingTable.setExcelListener(clickEvent -> {
            String excelHandler = CommandConstants.COMMON_URL + "/downloadPositionListExcel";
            listingTable.callListExcel(excelHandler, listingTable.getFilterParametrs());
        });

        add(listingTable);
        return null;
    }

    //column properties
    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columnConfigs = new ColumnDefinitionConfig[13];
        //action menu
        columnConfigs[0] = new ColumnDefinitionConfig<PositionItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final PositionItem positionItem) {
                Integer actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                //position summary view
                if (Utils.hasPermission(PermissionConstants.HRMS_POSITION_SUMMARRY)) {
                    MenuPopItem positionSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-position-small");
                    positionSummary.getElement().setId("Position_setting_summary_id");
                    positionSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("positionsummary|summary/" + positionItem.getObjectID(), positionItem.getNumberData().getNumberString() + "- " + positionItem.getName()));
                    actionItemCount++;
                    menuBar.addItem(positionSummary);
                }

                //edit position menu
                if (Utils.hasPermission(PermissionConstants.HRMS_POSITION_EDIT)) {
                    MenuPopItem positionEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    positionEdit.getElement().setId("Position_setting_edit_id");
                    positionEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("positions|editpositions/" + positionItem.getObjectID(), wfmStrings.edit() + " " + positionItem.getNumberData().getNumberString()));
                    actionItemCount++;
                    menuBar.addItem(positionEdit);
                }

                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_POSITION)) {
                    final MenuPopItem copyTask = new MenuPopItem(wfmStrings.copy(), "list-action-menu-icon icon-copy");
                    copyTask.ensureDebugId("copyPosition");
                    copyTask.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("position|add/add/copyPosition/" + positionItem.getObjectID().toString()));
                    actionItemCount++;
                    menuBar.addItem(copyTask);
                }

                //add vacancy
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_VACANCY)) {
                    MenuPopItem vacancyEdit = new MenuPopItem(wfmStrings.request(), "icon-edit");
                    vacancyEdit.getElement().setId("Vacancy_setting_edit_id");
                    vacancyEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("vacancy|add/add/positionId/" + positionItem.getObjectID(), wfmStrings.request() + "- " + positionItem.getName()));
                    actionItemCount++;
                    menuBar.addItem(vacancyEdit);
                }

                //PDF
                if (Utils.hasPermission(PermissionConstants.HRMS_POSITION_SUMMARRY)) {
                    MenuPopItem pdfVersion = new MenuPopItem(wfmStrings.pdf(), "icon-pdf-profile");
                    pdfVersion.ensureDebugId("exportToPDF");
                    pdfVersion.setCommand(() -> new PDFTemplateSelector(POSITION1, new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            LeaveRequestObject requestObject = new LeaveRequestObject(positionItem.getObjectID(), positionItem.getEmployeeId(), id);
                            HashMap<String, String> requestParams = requestObject.getRequestParams();
                            HashMap<String, String> parameters = requestObject.getRequestParams();
                            if (id != null) {
                                parameters.put("pdfTemplateID", String.valueOf(id));
                            }
                            String pdfUrl = CommandConstants.PDF_URL + "/positionListViewPDFHandler";
                            listingTable.callListPDF(pdfUrl, requestParams);
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(pdfVersion);
                }

                //delete position
                if (Utils.hasPermission(PermissionConstants.HRMS_POSITION_REMOVE)) {
                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    removeItem.getElement().setId("Position_setting_delete_id");
                    removeItem.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
                        message.setTitle(wfmStrings.deleting());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                HrmsService.App.get().deletePosition(positionItem.getObjectID(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.INFO);
                                    }

                                    @Override
                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_POSITION_DELETE, result, PositionListView.this);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.position()), Info.Type.INFO);
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(removeItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfigs[0].setMinimumColumnWidth(50);
        columnConfigs[0].setMaximumColumnWidth(50);
        columnConfigs[0].setShow(true);
        columnConfigs[0].setColumnSortable(false); //disabled for ACTION
        //position name
        columnConfigs[1] = new ColumnDefinitionConfig<PositionItem, Widget>(wfmStrings.position(), PositionItem.POSITION_TITLE, 120) {
            @Override
            public Widget getCellValue(PositionItem positionItem) {
                Label label = new Label(positionItem.getName() != null ? positionItem.getName() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> {
                    if (Utils.hasPermission(PermissionConstants.HRMS_POSITION_SUMMARRY)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("positionsummary|positionsummaryview/" + positionItem.getObjectID(), positionItem.getNumberData().getNumberString() + "- " + positionItem.getName());
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                });

                return label;
            }
        };
        columnConfigs[1].setMinimumColumnWidth(100);
        columnConfigs[1].setMaximumColumnWidth(150);
        columnConfigs[1].setShow(true);
        columnConfigs[1].setColumnSortable(true);

        //Status
        columnConfigs[2] = new ColumnDefinitionConfig<PositionItem, String>(wfmStrings.status(), PositionItem.STATUS, 50) {
            @Override
            public String getCellValue(PositionItem positionItem) {
                String status = "";
                if (positionItem.getStatus() != null) {
                    status = positionItem.getStatus().getName();
                }
                return status;
            }
        };
        columnConfigs[2].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfigs[2].setMinimumColumnWidth(50);
        columnConfigs[2].setMaximumColumnWidth(60);
        columnConfigs[2].setShow(true);
        columnConfigs[2].setColumnSortable(true);
        //Number of Active Employees
        columnConfigs[3] = new ColumnDefinitionConfig<PositionItem, Integer>(wfmStrings.headCount(), PositionItem.EMPLOYEE_COUNT, 50) {
            @Override
            public Integer getCellValue(PositionItem positionItem) {
                Integer count = 0;
                if (positionItem.getHeadCount() != null) {
                    count = positionItem.getHeadCount();
                }
                return count;
            }
        };
        columnConfigs[3].setMinimumColumnWidth(50);
        columnConfigs[3].setMaximumColumnWidth(60);
        columnConfigs[3].setShow(true);
        columnConfigs[3].setColumnSortable(true);
        //position number
        columnConfigs[4] = new ColumnDefinitionConfig<PositionItem, Widget>(wfmStrings.number(), PositionItem.POSITION_CODE, 50) {
            @Override
            public Widget getCellValue(PositionItem positionItem) {
                Label label = new Label(positionItem.getNumberData() != null ? positionItem.getNumberData().getNumberString() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> {
                    if (Utils.hasPermission(PermissionConstants.HRMS_POSITION_SUMMARRY)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("positionsummary|positionsummaryview/" + +positionItem.getObjectID(), positionItem.getNumberData().getNumberString() + "- " + positionItem.getName());
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                });

                return label;
            }

        };
        columnConfigs[4].setMinimumColumnWidth(50);
        columnConfigs[4].setMaximumColumnWidth(60);
        columnConfigs[4].setShow(true);
        columnConfigs[4].setColumnSortable(true);
        //Location
        columnConfigs[5] = new ColumnDefinitionConfig<PositionItem, String>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), PositionItem.LOCATION, 90) {
            @Override
            public String getCellValue(PositionItem positionItem) {
                return positionItem.getLocation() != null ? positionItem.getLocation().getName() : null;
            }
        };
        columnConfigs[5].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfigs[5].setMinimumColumnWidth(80);
        columnConfigs[5].setColumnSortable(true);
        columnConfigs[5].setMaximumColumnWidth(250);
        columnConfigs[5].setShow(false);

        //Department
        columnConfigs[6] = new ColumnDefinitionConfig<PositionItem, Widget>(wfmStrings.department(), PositionItem.DEPARTMENT, 90) {
            @Override
            public Widget getCellValue(PositionItem positionItem) {
                Label label = new Label(positionItem.getDepartment() != null ? positionItem.getDepartment().getName() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> {
                    if (Utils.hasPermission(PermissionConstants.HRMS_DEPARTMENT_SUMMARY_VIEW)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("department|summary/",positionItem.getDepartment().getName() );
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                });
                return label;
            }
        };
        columnConfigs[6].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfigs[6].setMinimumColumnWidth(80);
        columnConfigs[6].setMaximumColumnWidth(250);
        columnConfigs[6].setColumnSortable(true);
        columnConfigs[6].setShow(false);

        //Planned place count
        columnConfigs[7] = new ColumnDefinitionConfig<PositionItem, Integer>(wfmStrings.vacantPlaceCount(), PositionItem.POSITION_COUNT, 50) {
            @Override
            public Integer getCellValue(PositionItem positionItem) { //planned vacant place
                return positionItem.getEmployeeCount() != null ? positionItem.getEmployeeCount() : null;
            }
        };
        columnConfigs[7].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfigs[7].setMinimumColumnWidth(50);
        columnConfigs[7].setMaximumColumnWidth(60);
        columnConfigs[7].setColumnSortable(true);
        columnConfigs[7].setShow(true);

        //Position Created By
        columnConfigs[8] = new ColumnDefinitionConfig<PositionItem, String>(wfmStrings.createdBy(), PositionItem.CREATED_BY, 90) {
            @Override
            public String getCellValue(PositionItem positionItem) {
                return positionItem.getCreatedBy() != null ? positionItem.getCreatedBy() : null;
            }
        };
        columnConfigs[8].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfigs[8].setMinimumColumnWidth(70);
        columnConfigs[8].setMaximumColumnWidth(120);
        columnConfigs[8].setColumnSortable(true);
        columnConfigs[8].setShow(true);

        //Position Modified By
        columnConfigs[9] = new ColumnDefinitionConfig<PositionItem, String>(wfmStrings.modifiedBy(), PositionItem.MODIFIED_BY, 90) {
            @Override
            public String getCellValue(PositionItem positionItem) {
                return positionItem.getModifiedBy() != null ? positionItem.getModifiedBy() : null;
            }
        };
        columnConfigs[9].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfigs[9].setMinimumColumnWidth(70);
        columnConfigs[9].setMaximumColumnWidth(120);
        columnConfigs[9].setColumnSortable(true);
        columnConfigs[9].setShow(true);

        //Position Created Date
        columnConfigs[10] = new ColumnDefinitionConfig<PositionItem, String>(wfmStrings.createdDate(), PositionItem.CREATED_DATE, 90) {
            @Override
            public String getCellValue(PositionItem positionItem) {
                return positionItem.getCreatedDate() != null ? DateUtils.formatInternal(positionItem.getCreatedDate()) : null;
            }
        };
        columnConfigs[10].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfigs[10].setMinimumColumnWidth(70);
        columnConfigs[10].setMaximumColumnWidth(120);
        columnConfigs[10].setColumnSortable(true);
        columnConfigs[10].setShow(true);

        //Position Modified Date
        columnConfigs[11] = new ColumnDefinitionConfig<PositionItem, String>(wfmStrings.modifiedDate(), PositionItem.MODIFIED_DATE, 90) {
            @Override
            public String getCellValue(PositionItem positionItem) {
                return positionItem.getModifiedDate() != null ? DateUtils.formatInternal(positionItem.getModifiedDate()) : null;
            }
        };
        columnConfigs[11].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfigs[11].setMinimumColumnWidth(70);
        columnConfigs[11].setMaximumColumnWidth(120);
        columnConfigs[11].setColumnSortable(true);
        columnConfigs[11].setShow(false);

        //Position Type External/Internal
        columnConfigs[12] = new ColumnDefinitionConfig<PositionItem, String>(wfmStrings.type(), PositionItem.TYPE, 89) {
            @Override
            public String getCellValue(PositionItem positionItem) {
                return positionItem.getType() != null ? positionItem.getType().getName() : null;
            }
        };
        columnConfigs[12].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfigs[12].setMinimumColumnWidth(70);
        columnConfigs[12].setMaximumColumnWidth(100);
        columnConfigs[12].setColumnSortable(true);
        columnConfigs[12].setShow(false);
        return columnConfigs; //return column-configs

    }

    private void savePositionEditCellValue(PositionItem rowValue, String columnCodeName) {
        HrmsService.App.get().savePositionEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
        });
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_POSITION_ADD_EDIT, rowValue, PositionListView.this);
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> RbacService.App.get().getPositionFacetFilterData(data, new AsyncCallback<FacetFilterRpc>() {
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

                    //SOLR FACET FILTER
                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());
                        contentConfigure.addContentConfigure(FacetContentType.PositionFilter.getContentCode()[0], wfmStrings.department(), new FacetFieldConfigure() {
                            @Override
                            public String getSolrFieldCriteriaName() {
                                return SolrPositionRepresenter.FIELD_DEPARTMENT_ID;
                            }

                            @Override
                            public String getSolrFacetFieldName() {
                                return SolrPositionRepresenter.FIELD_DEPARTMENT_ID_NAME;
                            }
                        });

                        contentConfigure.addContentConfigure(FacetContentType.PositionFilter.getContentCode()[1], (Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())), new FacetFieldConfigure() {
                            @Override
                            public String getSolrFieldCriteriaName() {
                                return SolrPositionRepresenter.FIELD_LOCATION_ID;
                            }

                            @Override
                            public String getSolrFacetFieldName() {
                                return SolrPositionRepresenter.FIELD_LOCATION_ID_NAME;
                            }
                        });
                        contentConfigure.addContentConfigure(FacetContentType.PositionFilter.getContentCode()[2], wfmStrings.type(), new FacetFieldConfigure() {
                            @Override
                            public String getSolrFieldCriteriaName() {
                                return SolrPositionRepresenter.FIELD_TYPE_ID;
                            }

                            @Override
                            public String getSolrFacetFieldName() {
                                return SolrPositionRepresenter.FIELD_TYPE_ID_NAME;
                            }
                        });
                        contentConfigure.addContentConfigure(FacetContentType.PositionFilter.getContentCode()[3], wfmStrings.status(), new FacetFieldConfigure() {
                            @Override
                            public String getSolrFieldCriteriaName() {
                                return SolrPositionRepresenter.FIELD_STATUS_ID;
                            }

                            @Override
                            public String getSolrFacetFieldName() {
                                return SolrPositionRepresenter.FIELD_STATUS_ID_NAME;
                            }
                        });
                        contentConfigure.addContentConfigureDateListBox(SolrPositionRepresenter.FIELD_CREATED_DATE, wfmStrings.createdDate());
                        contentConfigure.addContentConfigureDateListBox(SolrPositionRepresenter.FIELD_MODIFIED_DATE, wfmStrings.modifiedDate());
                        return contentConfigure;
                    }

                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                boolean hasChild = false;
                ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
                MenuBar menu = new MenuBar(true);
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_POSITION)) {
                    hasChild = true;
                    MenuPopItem addPosition = new MenuPopItem(wfmStrings.add());
                    addPosition.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("position|add/add"));
                    menu.addItem(addPosition);
                }

                if (Utils.hasPermission(PermissionConstants.HRMS_POSITION_MULTI_ADD)) {
                    MenuPopItem addNewMultiTask = new MenuPopItem("<span class='list-action-menu-icon'>" + wfmStrings.bulkAdd() + "</span>");
                    addNewMultiTask.setCommand(() -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("multiposition|add/add");
                    });
                    menu.addItem(addNewMultiTask);
                    hasChild = true;
                }
                if (Utils.hasPermission(PermissionConstants.HRMS_POSITION_MULTI_ADD)) {
                    MenuPopItem addNewMultiTask = new MenuPopItem("<span class='list-action-menu-icon'>" + wfmStrings.bulkUpdate() + "</span>");
                    addNewMultiTask.setCommand(() -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("multiposition|add/add/0");
                    });
                    menu.addItem(addNewMultiTask);
                    hasChild = true;
                }
                newItem.setMenu(menu);
                if (hasChild) {
                    return newItem;
                }
                return null;
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.HRMS_POSITION_EDIT);
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo("importposition|add/add/" + imp.getObjectId());
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.currentlyYouDoNotHaveAnyData());
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_POSITION)) {
                    message.setHref("position|add/add");
                    message.setTextBeforeLink(wfmStrings.youCanStartAddingItemByClick());
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            //Customize List button permission
            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.POSITION_CUSTOMIZE_LIST);
            }
        };
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        initPositionList(new ListingFilterParameter(), null, container);
    }


    private ListingRequestProvider<PositionItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> initPositionList(filterParametrs, callback, null);
    }

    private void initPositionList(ListingFilterParameter filterParametrs, ListingCallback<PositionItem> callback, Span container) {
        filterParametrs.setDepartmentId(departmentId);
        HrmsService.App.get().getPositionList(filterParametrs, new AsyncCallback<ListResult<PositionItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<PositionItem> positionItemListResult) {
                if (callback != null) {
                    callback.onSuccess(positionItemListResult);
                }
                totalCount = positionItemListResult.getTotal();
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