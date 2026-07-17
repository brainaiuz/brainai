package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimePickerCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.edatasite.workforce.gwt.payroll.client.ui.PayrollContants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_EDIT_PLACEMENT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_PRINT_PDF_PLACEMENT;


public class PlacementListView extends BaseListView {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final String placementListViewID = "placement_list_view_";

    /**
     * Generate default constructor
     */
    public PlacementListView() {
        super("placementsListView");
        setDescription(property.getPlural(hrmsStrings.placementsOnly()));
        if (hasPermissionToAdd()) {
            setAddNew("placement|add/add");
        }
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.HRMS_ADD_PLACEMENT);
    }

    protected SimpleLink getLink(String name, String action, String tabName, String tabTitle) {
        return new SimpleLink(name, action, tabTitle, tabName, "");
    }

    @Override
    public String getIconStyle() {
        return "bgMark recruitment";
    }

    @Override
    protected Widget onInitialize() {
        final ListingPanel<PlacementItem> listingPanel = new GuideListingPanel(ListPanelType.PlacementListPanel, getColumns(), getProvider(), getDesign());
        listingPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> savePlacementsCellValue((PlacementItem) rowValue, columnCodeName));
        //
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PLACEMENT_ADD_EDIT, PlacementListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PLACEMENT_DELETE, PlacementListView.this, (sender, args) -> listingPanel.reloadPage());

        // Short List PDF
        listingPanel.setPDFListener(event -> {
            String pdfURL = CommandConstants.PDF_URL + "/placementListPDFHandler";
            ListingFilterParameter fp = listingPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listingPanel.callListPDF(pdfURL, fp);
        });

        listingPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadPlacementListExcel";
            ListingFilterParameter fp = listingPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listingPanel.callListExcel(excelURL, fp);
        });
        add(listingPanel);
        return null;
    }

    /**
     * Generate placement columns
     *
     * @return - columns
     */
    private CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig columnConfig;

        //standard action column
        columnConfig = new ColumnDefinitionConfig<PlacementItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final PlacementItem rowValue) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                //placement summary
                MenuPopItem placementSummary = null;
                if (!Constants.PLACEMENT_STATUS_SAVE_AS_DRAFT.equals(rowValue.getStatusCode())) {
                    placementSummary = new MenuPopItem(wfmStrings.summaryView());
                    placementSummary.getElement().setId(placementListViewID + "placement_summary");
                    placementSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("placement|summary/" + rowValue.getObjectID() + "/" + rowValue.isEditable(), rowValue.getCandidateName()));
                    actionItemCount++;
                    menuBar.addItem(placementSummary);
                }

                //placement edit
                final MenuPopItem placementEdit;
                if (rowValue.isEditable() && Utils.hasPermission(HRMS_EDIT_PLACEMENT)) {
                    placementEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    placementEdit.getElement().setId(placementListViewID + "edit_placement");
                    placementEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("placement|editPlacement/" + rowValue.getObjectID() + "/" + rowValue.isEditable(), rowValue.getCandidateName()));
                    actionItemCount++;
                    menuBar.addItem(placementEdit);
                } else {
                    placementEdit = null;
                }
                if ((Utils.hasPermission(PermissionConstants.HRMS_EXPORT_TO_PDF) && Utils.hasPermission(HRMS_PRINT_PDF_PLACEMENT)) || Utils.getUserID().equals(rowValue.getObjectID())) {
                    MenuPopItem employeePDF = new MenuPopItem(wfmStrings.pdf(), "icon-document-pdf");
                    employeePDF.setCommand(() -> new PDFTemplateSelector(PayrollContants.PLACEMENT, new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(id, rowValue.getObjectID());
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(employeePDF);
                }
                //placement delete
                final MenuPopItem placementDelete;
                if (rowValue.isEditable()) {
                    placementDelete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile file--PlacementListView");
                    placementDelete.getElement().setId(placementListViewID + "remove_placement");
                    placementDelete.setCommand(() -> {
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        wfmMessageBox.setTitle(wfmStrings.warning());
                        wfmMessageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        wfmMessageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                            }

                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                RecruitmentService.App.get().deletePlacement(rowValue.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(Boolean result) {
                                        LoadingPanel.loading(false);
                                        if (result) {
                                            Info.show(wfmMessages.yourSomethingHasBeenDeleted(wfmStrings.placement()));
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PLACEMENT_DELETE, result, PlacementListView.this);
                                        }
                                    }
                                });
                            }
                        });
                        wfmMessageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(placementDelete);
                } else {
                    placementDelete = null;
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                Anchor action = toolItem.getAction();
                MenuPopItem finalPlacementSummary = placementSummary;
                action.addClickHandler(event -> {
                    finalPlacementSummary.setVisible(true);
                    if (placementEdit != null) {
                        placementEdit.setVisible(true);
                    }
                    if (placementDelete != null) {
                        placementDelete.setVisible(true);
                    }
                });
                return action;//return action menu items
            }
        };
        columnConfig.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //placementCode
        columnConfig = new ColumnDefinitionConfig<PlacementItem, Widget>(wfmStrings.number(), PlacementItem.PLACEMENT_CODE, 150) {
            @Override
            public Widget getCellValue(final PlacementItem item) {
                Label label = new Label(item.getPlacementCode() != null ? item.getPlacementCode() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(event -> {
                    if (Utils.hasPermission(PermissionConstants.HRMS_SUMMARY_PLACEMENT)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("placement|summary/" + item.getObjectID(), item.getPlacementCode());
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                });
                return label;
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);
        //candidate
        columnConfig = new ColumnDefinitionConfig<PlacementItem, SimpleLink>(wfmStrings.candidate(), PlacementItem.PLACEMENT_CANDIDATE_NAME, 150) {
            @Override
            public SimpleLink getCellValue(PlacementItem rowValue) {
                boolean isDraft = Constants.PLACEMENT_STATUS_SAVE_AS_DRAFT.equals(rowValue.getStatusCode());
                String url = isDraft ? "placement|editPlacement/" : "placement|summary/";
                return new SimpleLink(rowValue.getCandidateName(), url + rowValue.getObjectID() + "/" + rowValue.isEditable(), rowValue.getCandidateName());//return candidate name, with placement summary link
            }
        };
        columnConfig.setMinimumColumnWidth(150);
        columnConfig.setColumnSortable(true);
        columns.add(columnConfig);
        //position offered
        columnConfig = new

                ColumnDefinitionConfig<PlacementItem, SelectItem>(wfmStrings.position(), PlacementItem.PLACEMENT_POSITION_OFFERED, 100) {
                    @Override
                    public SelectItem getCellValue(PlacementItem item) {
                        return new SelectItem(item.getPositionID(), item.getPositionName());
                    }

                    @Override
                    public void setCellValue(PlacementItem rowValue, SelectItem cellValue) {
                        rowValue.setPositionID(cellValue != null ? cellValue.getId() : null);
                        rowValue.setPositionName(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                        saveCellValue(rowValue);
                    }
                };
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);
        //offered date
        columnConfig = new

                ColumnDefinitionConfig<PlacementItem, String>(hrmsStrings.dateOffered(), PlacementItem.PLACEMENT_DATE_OFFERED, 100) {
                    @Override
                    public String getCellValue(PlacementItem rowValue) {
                        return DateUtils.format(rowValue.getDateOffed()) + Utils.getHijriDate(rowValue.getDateOffed());//return offered date
                    }

                    @Override
                    public void setCellValue(PlacementItem rowValue, String cellValue) {
                        try {
                            rowValue.setDateOffed(DateUtils.parse(cellValue));
                            saveCellValue(rowValue);
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                    }
                };
        columnConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);
        //offer status
        columnConfig = new

                ColumnDefinitionConfig<PlacementItem, SelectItem>(wfmStrings.status(), PlacementItem.PLACEMENT_STATUS_OFFER, 100) {
                    @Override
                    public SelectItem getCellValue(PlacementItem rowValue) {
                        return new SelectItem(rowValue.getStatusID(), rowValue.getStatusName());//return offer status
                    }

                    @Override
                    public void setCellValue(PlacementItem rowValue, SelectItem cellValue) {
                        rowValue.setStatusID(cellValue != null ? cellValue.getId() : null);
                        rowValue.setStatusName(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                        saveCellValue(rowValue);
                    }
                };
        columnConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        initCellEdit(CustomColumnDefinitionConfig.getEditableColumns(columns));
        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private void initCellEdit(Map<String, CustomColumnDefinitionConfig> columns) {
        for (final Map.Entry<String, CustomColumnDefinitionConfig> entry : columns.entrySet()) {
            InlineCellEditor widget = null;
            CustomColumnDefinitionConfig column = entry.getValue();
            if (PlacementItem.PLACEMENT_STATUS_OFFER.equals(entry.getKey())
                    || PlacementItem.PLACEMENT_POSITION_OFFERED.equals(entry.getKey())
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
            } else if (PlacementItem.PLACEMENT_DATE_OFFERED.equals(entry.getKey())) {
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

            if (widget != null) {
                column.setCellEditor(widget);
                column.setCellChangesSave((rowValue, columnCodeName) -> savePlacementsCellValue((PlacementItem) rowValue, columnCodeName));
            }
        }
    }

    private void setItemsAndSelect(final DataListBox listBox, String key, SelectItem cellValue) {
        if (listBox.getItems() == null || listBox.getItems().length < 1) {
            if (PlacementItem.PLACEMENT_STATUS_OFFER.equals(key)) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getPlacementStatus(new AbstractAsyncCallback<SelectItem[]>() {
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
            } else if (PlacementItem.PLACEMENT_POSITION_OFFERED.equals(key)) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getPlacementPosition(new AbstractAsyncCallback<SelectItem[]>() {
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
            }
        }
    }

    private void savePlacementsCellValue(PlacementItem rowValue, String columnCodeName) {
        RecruitmentService.App.get().savePlacementEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
        });
    }

    /**
     * Generate listing panel design
     *
     * @return - listing panel design
     */
    private GuideListingPanelDesign getDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? PlacementListView.this::addNewPlacement : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToAdd()) {
                    ActionButton addNewPlacement = getAddNewButton();
                    addNewPlacement.getElement().setId(placementListViewID + "add_placement");
                    addNewPlacement.addClickHandler(event -> addNewPlacement());
                    return addNewPlacement;
                }
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.currentlyYouDoNotHaveAnyData());
                if (hasPermissionToAdd()) {
                    message.setHref("placement|add/add");
                    message.setTextBeforeLink(wfmStrings.youCanStartAddingItemByClick());
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.adminOrDirector() || Utils.hasRole(Constants.HR);
            }
        };
    }

    private SinksContainer addNewPlacement() {
        return SinksContainerFactory.entryPoint.onHistoryChanged("placement|add/add");
    }

    /**
     * Generate listing request provider
     *
     * @return - request provider
     */
    private ListingRequestProvider<PlacementItem> getProvider() {
        return (filterParametrs, placementListItemListingCallback) -> {
            RecruitmentService.App.get().getPlacementList(filterParametrs, new AbstractAsyncCallback<ListResult<PlacementItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    placementListItemListingCallback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<PlacementItem> result) {
                    placementListItemListingCallback.onSuccess(result);
                }
            });
        };
    }

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

    public String getPropertyCode() {
        return Constants.PLACEMENT;
    }

    private void generatePDF(Integer pdfTemplateID, Integer objectID) {
        LeaveRequestObject requestObject = new LeaveRequestObject(objectID, null, pdfTemplateID);
        String pdfURL = CommandConstants.PDF_URL + "/placementInfoPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(this, pdfURL, parametrs, "_blank");
    }
}