package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeStepRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Azazello on 7/11/15.
 */
public class EmployeeStepListView extends BaseListView implements Constants {
    private static final HrmsServiceAsync hrmsService = HrmsService.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private int totalCount = 0;
    private final Integer stepID;
    private final String name;
    private final String formID;
    private final String icon;
    private ListingPanel<EmployeeStepItem> listingTable;
    private FlowPanel panel;
    private boolean isEnableApprovers = false;
    private boolean hasAddPermission;
    private boolean hasEditPermission = false;
    private boolean hasDeletePermission;
    private boolean hasExportPermission;

    public EmployeeStepListView(Integer stepID, String name, String formID, String icon) {
        super(name, name);
        this.stepID = stepID;
        this.name = name;
        this.formID = formID;
        this.icon = icon;
        checkPermissions();
        if (hasAddPermission) {
            setAddNew(EMPLOYEE_STEP + "|add/add/" + stepID + "/" + formID + "/" + name);
        }
    }

    private void checkPermissions() {
        if (formID != null) {
            String p = formID.replaceAll(Constants.ONBOARDING_STEP_FORM, "");
            hasAddPermission = Utils.hasPermission(PermissionConstants.EMPLOYEE_STEP_ + p + "_ADD") || Utils.hasRole(HR) || Utils.hasRole(DR);
            hasEditPermission = Utils.hasPermission(PermissionConstants.EMPLOYEE_STEP_ + p + "_EDIT") || Utils.hasRole(HR) || Utils.hasRole(DR);
            hasDeletePermission = Utils.hasPermission(PermissionConstants.EMPLOYEE_STEP_ + p + "_DELETE") || Utils.hasRole(HR) || Utils.hasRole(DR);
            hasExportPermission = Utils.hasPermission(PermissionConstants.EMPLOYEE_STEP_ + p + "_EXPORT") || Utils.hasRole(HR) || Utils.hasRole(DR);
        }
    }

    protected Widget onInitialize() {
        hrmsService.isEnableApprovers(formID, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                initData();
            }

            @Override
            public void onSuccess(Boolean enableApprovers) {
                isEnableApprovers = enableApprovers;
                initData();
            }
        });

        return null;
    }

    private void initData() {
        listingTable = new ListingPanel<>(ListPanelType.EmployeeStepListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign(), stepID, stepID);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_STEP_ADD_EDIT_DELETE, EmployeeStepListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ONBOARDING_STEP_ADD_EDIT, EmployeeStepListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ONBOARDING_STEP_DELETE, EmployeeStepListView.this, (sender, args) -> listingTable.reloadPage());
        listingTable.setCustomFieldsEditCellSaveChanges((CellChange<EmployeeStepItem>) this::saveStepEditCellValue);
        listingTable.setPDFListener(clickEvent -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            listingTable.getFilterParametrs().setStepID(stepID);
            listingTable.getFilterParametrs().setRelationName(name);
            listingTable.callListPDF(CommandConstants.PDF_URL + "/employeeStepListPDFHandler", listingTable.getFilterParametrs());
        });
        listingTable.setExcelListener(clickEvent -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            listingTable.getFilterParametrs().setStepID(stepID);
            listingTable.getFilterParametrs().setRelationName(name);
            listingTable.callListExcel(CommandConstants.COMMON_URL + "/downloadEmployeeStepsExcel", listingTable.getFilterParametrs());
        });
        add(listingTable);
    }

    @Override
    public FlowPanel getHelpContainer() {
        return panel = new FlowPanel();
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ArrayList<CustomColumnDefinitionConfig> columnConfigs = new ArrayList<>();
        ColumnDefinitionConfig columnConfig;
        columnConfig = new ColumnDefinitionConfig<EmployeeStepItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final EmployeeStepItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                int menuItemCount = 0;
                //Edit  item
                if (hasEditPermission) {
                    final MenuPopItem onboardingStepEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    onboardingStepEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(EMPLOYEE_STEP + "|add/add/" + rowValue.getObjectID() + "/" + stepID + "/" + formID + "/" + name));
                    menuItemCount++;
                    menuBar.addItem(onboardingStepEdit);
                }
                //PDF  item
                final MenuPopItem pdf = new MenuPopItem(wfmStrings.pdfVersion(), "icon-pdf");
                pdf.setCommand(() -> {
                    String pdfURL = CommandConstants.PDF_URL + "/employeeStepViewPDFHandler";
                    RequestObject requestObject = new RequestObject(rowValue.getObjectID());
                    HashMap<String, String> parametrs = requestObject.getRequestParams();
                    Utils.sendPDFOrExcelRequest(panel, pdfURL, parametrs, "_blank");
                });
                menuItemCount++;
                menuBar.addItem(pdf);

                if (rowValue.isCanApprove()) {
                    if (rowValue.getStatusID() == null || !rowValue.getStatusID().equals(rowValue.getAppoveStatusId())) {
                        //Approve item
                        final MenuPopItem approveEmployeeStep = new MenuPopItem(wfmStrings.approve(), "");
                        approveEmployeeStep.setCommand(() -> approveOrRejectAction(rowValue, true));
                        menuItemCount++;
                        menuBar.addItem(approveEmployeeStep);
                    }

                    if (rowValue.getStatusID() == null || !rowValue.getStatusID().equals(rowValue.getRejectStatusId())) {
                        //reject item
                        final MenuPopItem rejectEmployeeStep = new MenuPopItem(wfmStrings.reject(), "");
                        rejectEmployeeStep.setCommand(() -> approveOrRejectAction(rowValue, false));
                        menuItemCount++;
                        menuBar.addItem(rejectEmployeeStep);
                    }
                }

                //Delete item
                if (hasDeletePermission) {
                    final MenuPopItem deleteOnboardingStep = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteOnboardingStep.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                hrmsService.deleteEmployeeStep(rowValue.getObjectID(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        listingTable.reloadPage();
                                        Info.show(name + Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.stage()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_STEP_ADD_EDIT_DELETE, result, EmployeeStepListView.this);
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    menuItemCount++;
                    menuBar.addItem(deleteOnboardingStep);
                }

                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<EmployeeStepItem, SimpleLink>(wfmStrings.employee(), EmployeeStepItem.EMPLOYEE, 200) {
            @Override
            public SimpleLink getCellValue(EmployeeStepItem rowValue) {
                return getLink(rowValue.getEmployeeName(), EMPLOYEE_STEP + "|add/add/" + rowValue.getObjectID() + "/" + stepID + "/" + formID + "/" + name);
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<EmployeeStepItem, SimpleLink>(wfmStrings.employeeCode(), EmployeeStepItem.EMPLOYEE_CODE, 100) {
            @Override
            public SimpleLink getCellValue(EmployeeStepItem rowValue) {
                return getLink(rowValue.getEmployeeCode() != null ? rowValue.getEmployeeCode() : "", rowValue.getEmployeeCode() != null ? "employeeProfile|" + EMPLOYEE_PROFILE_VIEW + "/" + rowValue.getEmployeeID() : "");
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<EmployeeStepItem, SimpleLink>(wfmStrings.code(), EmployeeStepItem.CANDIDATE_CODE, 100) {
            @Override
            public SimpleLink getCellValue(EmployeeStepItem rowValue) {
                return getLink(rowValue.getCandidateCode() != null ? rowValue.getCandidateCode() : "", rowValue.getCandidateCode() != null ? "candidate|summary/" + rowValue.getEmployeeID() : "");
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<EmployeeStepItem, String>(wfmStrings.type(), EmployeeStepItem.TYPE, 100) {
            @Override
            public String getCellValue(EmployeeStepItem rowValue) {
                return rowValue.getTypeName();
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<EmployeeStepItem, String>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), EmployeeStepItem.EMPLOYEE_LOCATION, 200) {
            @Override
            public String getCellValue(EmployeeStepItem rowValue) {
                return rowValue.getLocation();
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        if (isEnableApprovers) {
            columnConfig = new ColumnDefinitionConfig<EmployeeStepItem, String>(wfmStrings.status(), EmployeeStepItem.STATUS, 120) {
                @Override
                public String getCellValue(EmployeeStepItem rowValue) {
                    return rowValue.getStatusName();
                }
            };
            columnConfig.setMinimumColumnWidth(50);
            columnConfigs.add(columnConfig);
        } else {
            columnConfig = new ColumnDefinitionConfig<EmployeeStepItem, SelectItem>(wfmStrings.status(), EmployeeStepItem.STATUS, 120) {
                @Override
                public SelectItem getCellValue(EmployeeStepItem rowValue) {
                    return new SelectItem(rowValue.getStatusID(), rowValue.getStatusName());
                }

                @Override
                public void setCellValue(EmployeeStepItem rowValue, SelectItem cellValue) {
                    if (cellValue != null) {
                        rowValue.setStatusName(cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                        rowValue.setStatusID(cellValue.getId());
                    } else {
                        rowValue.setStatusName(null);
                        rowValue.setStatusID(null);
                    }
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(50);
            columnConfigs.add(columnConfig);
        }

        columnConfig = new ColumnDefinitionConfig<EmployeeStepItem, HTML>(wfmStrings.approverStatus(), EmployeeStepItem.ASSIGN_STATUS, 120) {
            @Override
            public HTML getCellValue(EmployeeStepItem rowValue) {
                return new HTML(rowValue.getAssignStatues());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<EmployeeStepItem, String>(wfmStrings.createdDate(), EmployeeStepItem.CREATION_DATE, 100) {
            @Override
            public String getCellValue(EmployeeStepItem rowValue) {
                return DateUtils.format(rowValue.getCreationDate());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<EmployeeStepItem, String>(wfmStrings.modifiedDate(), EmployeeStepItem.UPDATED_DATE, 100) {
            @Override
            public String getCellValue(EmployeeStepItem rowValue) {
                return DateUtils.format(rowValue.getUpdatedDate());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);

        if (!isEnableApprovers && hasEditPermission) {
            initCellEdit(CustomColumnDefinitionConfig.getEditableColumns(columnConfigs));
        }
        return columnConfigs.toArray(new ColumnDefinitionConfig[]{});
    }

    private void approveOrRejectAction(final EmployeeStepItem rowValue, final boolean approve) {
        hrmsService.approveOrRejectEmployeeStep(rowValue.getObjectID(), rowValue.getStepID(), approve, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                listingTable.reloadPage();
                if (approve) {
                    Info.show(wfmMessages.employeeStepHasBeenApprovedSuccessfully(), Info.Type.INFO);
                } else {
                    Info.show(wfmMessages.employeeStepHasBeenRejectedSuccessfully(), Info.Type.INFO);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_STEP_ADD_EDIT_DELETE, result, EmployeeStepListView.this);
            }
        });
    }

    private void initCellEdit(LinkedHashMap<String, CustomColumnDefinitionConfig> editableColumns) {
        CustomColumnDefinitionConfig column = editableColumns.get(EmployeeStepItem.STATUS);
        InlineCellEditor widget = new DropDownCellEditor<SelectItem>() {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                getListBox().setAllowFirstItem(true);
                if (getListBox().getItems() == null || getListBox().getItems().length < 1 && listingTable.getDefaultOne() != null) {
                    getListBox().setItems(listingTable.getDefaultOne().getStatuses());
                }
                getListBox().setSelectedIndex(0);
                if (cellValue == null || cellValue.getId() == null) {
                    getListBox().setSelectedNullLabel();
                } else {
                    getListBox().setSelected(cellValue.getId());
                }
            }
        };
        if (widget != null) {
            column.setCellEditor(widget);
            column.setCellChangesSave(new CellChange<EmployeeStepItem>() {
                @Override
                public void saveCell(EmployeeStepItem rowValue, String columnCodeName) {
                    saveStepEditCellValue(rowValue, columnCodeName);
                }
            });
        }
    }

    public void saveStepEditCellValue(EmployeeStepItem rowValue, String columnCodeName) {
        hrmsService.saveStepColumnChanges(rowValue, columnCodeName, new AbstractAsyncCallback() {
            @Override
            public void onSuccess(Object result) {
                listingTable.reloadPage();
            }
        });
    }

    private ListingRequestProvider<EmployeeStepItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            filterParametrs.setStepID(stepID);
            LoadingPanel.loading(true);
            hrmsService.getEmployeeStepList(filterParametrs, new AbstractAsyncCallback<ListResult<EmployeeStepItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<EmployeeStepItem> result) {
                    LoadingPanel.loading(false);
                    totalCount = result.getTotal();
                    listingTable.setDefaultOne(result.getDefaultOne());
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            RbacService.App.get().getEmployeeStepFacetFilterData(listingTable.getFilterParametrs(), data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                public void failure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                public void success(FacetFilterRpc data) {
                                    callback.onSuccess(data);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }

            @Override
            public Integer getTypeParentId() {
                return stepID;
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return hasEditPermission;
            }

            @Override
            public Widget getAddAdditionalPanel() {
                final HorizontalPanelDiv panel = new HorizontalPanelDiv();
//                KpiCheckBox archiveCheckBox = new KpiCheckBox(hrmsStrings.showArchived(), true);
//                archiveCheckBox.addValueChangeHandler(event -> {
//                    listingTable.getFilterParametrs().setShowArchived(event.getValue());
//                    listingTable.reloadPage();
//                });
//                HorizontalPanelDiv divPanel = new HorizontalPanelDiv();
//                divPanel.add(archiveCheckBox);
//                divPanel.setStyleName("showAllCheckBox");
//                panel.add(divPanel);
                panel.setSpacing(5);
                panel.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
                return panel;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasAddPermission) {
                    ActionButton addNewStep = getAddNewButton();
                    addNewStep.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(EMPLOYEE_STEP + "|add/add/" + stepID + "/" + formID + "/" + name));
                    return addNewStep;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, hasExportPermission);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(name) + "s.");
                if (hasAddPermission) {
                    message.setTextBeforeLink(wfmMessages.addingByClicking(name));
                    message.setHref(EMPLOYEE_STEP + "|add/add/" + stepID + "/" + formID + "/" + name);
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, hrmsStrings.stepFilter());
        contentConfigure.addContentConfigure(FacetContentType.EmployeeStepFacetFilter.getContentCode()[0], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEmployeeStepRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEmployeeStepRepresenter.FIELD_STATUS_ID_NAME;
            }
            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.EmployeeStepFacetFilter.getContentCode()[1], Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_LOCATION_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_LOCATION_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.EmployeeStepFacetFilter.getContentCode()[2], wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEmployeeStepRepresenter.FIELD_TYPE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEmployeeStepRepresenter.FIELD_TYPE_ID_NAME;
            }
            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });
        //creator
        contentConfigure.addContentConfigureDateListBox(SolrEmployeeStepRepresenter.FIELD_CREATION_DATE, wfmStrings.createdDate());
        contentConfigure.addContentConfigureDateListBox(SolrEmployeeStepRepresenter.FIELD_MODIFICATION_DATE, wfmStrings.modifiedDate());
        return contentConfigure;
    }

    @Override
    public String getIconStyle() {
        return icon;
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
}
