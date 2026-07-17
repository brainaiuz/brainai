package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseScheduleRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.*;
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
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
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
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/21/12
 * Time: 1:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledCourseListView extends BaseListView implements TCConstants, Constants, PermissionConstants {

    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final TCStrings tcStrings = TCStrings.App.get();
    private int totalCount = 0;

    protected ListingPanel<ScheduledCourseItem> listingPanel;
    protected HashSet<ScheduledCourseItem> selectedItems = new HashSet<>();
    protected ContextMenu actions;
    private ContextMenu emptyActions = null;
    private SelectItem[] statusList = null;

    public ScheduledCourseListView() {
        super("scheduledcourses");
        setDescription(property.getPlural(tcStrings.courseSchedules()));
    }

    public ScheduledCourseListView(String name, String description) {
        super(name, description);
    }


    @Override
    protected Widget onInitialize() {
        if (Utils.hasPermission(PermissionConstants.TC_SCHEDULE_COURSE_LIST_FOR_ADMIN)) {
            listingPanel = new ListingPanel<>(ListPanelType.ScheduledCourseListPanel, getColumns(), getProvider(), getDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
            listingPanel.addSelectionRowHandler(selectedRows -> {
                selectedItems = selectedRows;
                actions = null;
            });
        } else {
            listingPanel = new ListingPanel<>(ListPanelType.ScheduledCourseListPanel, getColumns(), getProvider(), getDesign());
        }

        listingPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadScheduledCourseListExcel";
            ListingFilterParameter filterParametrs = listingPanel.getFilterParametrs();
            listingPanel.callListExcel(excelURL, filterParametrs);
        });
        listingPanel.setPDFListener(clickEvent -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL = CommandConstants.PDF_URL + "/scheduledCourseListPDFHandler";
            ListingFilterParameter filterParametrs = listingPanel.getFilterParametrs();
            listingPanel.callListPDF(pdfURL, filterParametrs);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SCHEDULED_COURSE_SAVED, ScheduledCourseListView.this, (sender, args) -> listingPanel.reloadPage());

        add(listingPanel);
        return null;
    }

    protected CustomColumnDefinitionConfig[] getColumns() {
        CustomColumnDefinitionConfig[] columns = new CustomColumnDefinitionConfig[13];
        //action
        columns[0] = new ColumnDefinitionConfig<ScheduledCourseItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            public Anchor getCellValue(final ScheduledCourseItem rowValue) {
                return getActions(rowValue);
            }

        };
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<ScheduledCourseItem, SimpleLink>(wfmStrings.number(), ScheduledCourseItem.NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(ScheduledCourseItem rowValue) {
                return new SimpleLink(rowValue.getNumber(), (TC_SCHEDULED_COURSE + "|summary/" + rowValue.getObjectID() + "/" + (rowValue.getInstructorID() == null)));
            }
        };
        columns[1].setMinimumColumnWidth(50);

        columns[2] = new ColumnDefinitionConfig<ScheduledCourseItem, SimpleLink>(wfmStrings.course(), ScheduledCourseItem.COURSE, 100) {
            @Override
            public SimpleLink getCellValue(ScheduledCourseItem rowValue) {
                return getSimpleLink(rowValue);
            }
        };
        columns[2].setMinimumColumnWidth(50);

        columns[3] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(wfmStrings.language(), ScheduledCourseItem.LANGUAGE, 100) {
            @Override
            public String getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getLanguageName();
            }
        };
        columns[3].setMinimumColumnWidth(50);

        columns[4] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(wfmStrings.startDate(), ScheduledCourseItem.START_DATE, 100) {
            @Override
            public String getCellValue(final ScheduledCourseItem rowValue) {
                return DateUtils.formatInternal(rowValue.getStartDate());
            }
        };
        columns[4].setMinimumColumnWidth(50);

        columns[5] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(wfmStrings.duration(), ScheduledCourseItem.DURATION, 100) {
            @Override
            public String getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getDuration() != null ? (rowValue.getDuration() + wfmStrings.hours()) : wfmStrings.notAvailable();
            }
        };
        columns[5].setMinimumColumnWidth(50);

        columns[6] = new ColumnDefinitionConfig<ScheduledCourseItem, HTML>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), ScheduledCourseItem.LOCATION, 100) {
            @Override
            public HTML getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getLocationName() != null ? new HTML(rowValue.getLocationName()) : null;
            }
        };
        columns[6].setMinimumColumnWidth(50);
        columns[6].setColumnSortable(false);

        columns[7] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(wfmStrings.numberOfSeats(), ScheduledCourseItem.COUNT_OF_SETS, 80) {
            @Override
            public String getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getNumberOfSeats() != null ? String.valueOf(rowValue.getNumberOfSeats()) : "0";
            }
        };
        columns[7].setMinimumColumnWidth(50);

        columns[8] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(wfmStrings.instructor(), ScheduledCourseItem.INSTRUCTOR, 100) {
            @Override
            public String getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getInstructorName() != null ? rowValue.getInstructorName() : wfmStrings.notAvailable();
            }
        };
        columns[8].setMinimumColumnWidth(50);

        columns[9] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(wfmStrings.assessor(), ScheduledCourseItem.ASSESSOR, 100) {
            @Override
            public void setCellValue(ScheduledCourseItem rowValue, String cellValue) {
                rowValue.setAssessorName(cellValue);
                saveCellValue(rowValue);
            }

            @Override
            public String getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getAssessorName() != null ? rowValue.getAssessorName() : wfmStrings.notAvailable();
            }
        };
        columns[9].setMinimumColumnWidth(50);
        columns[9].setColumnSortable(false);

        columns[10] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(wfmStrings.status(), ScheduledCourseItem.COURSE_SCHEDULE_STATUS, 100) {
            @Override
            public void setCellValue(ScheduledCourseItem rowValue, String cellValue) {
                if (rowValue.hasInvoice() && !CS_DELIVERED.equals(getStatusCodeByName(cellValue))) {
                    Info.get().show("You can not change status, because the course schedule has been invoiced!", Info.Type.WARNING);
                } else if (CS_DELIVERED.equals(getStatusCodeByName(cellValue)) && (rowValue.getCountOfNotAddressedStudent() > 0 || rowValue.getCountOfStudent() == 0)) {
                    Info.get().show("You can not change status to Delivered, because the course schedule has not addressed students!", Info.Type.WARNING);
                } else {
                    rowValue.setStatusName(cellValue);
                    saveCellValue(rowValue);
                }
            }

            @Override
            public String getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getStatusName();
            }
        };
        columns[10].setMinimumColumnWidth(50);

        columns[11] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(tcStrings.countOfStudent(), ScheduledCourseItem.COUNT_OF_STUDENT, 80) {
            @Override
            public String getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getCountOfStudent() != null ? String.valueOf(rowValue.getCountOfStudent()) : "0";
            }
        };
        columns[11].setMinimumColumnWidth(50);

        columns[12] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(tcStrings.countOfConfirmedStudent(), ScheduledCourseItem.COUNT_OF_CONFIRMED_STUDENT, 100) {
            @Override
            public String getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getCountOfConfirmedStudent() != null ? String.valueOf(rowValue.getCountOfConfirmedStudent()) : "0";
            }
        };
        columns[12].setMinimumColumnWidth(50);

        initCellEdit(columns);
        return columns;
    }

    private void initCellEdit(final CustomColumnDefinitionConfig[] columnConfigs) {
        if (Utils.hasPermission(PermissionConstants.TC_SCHEDUL_COURSE_LIST_EDITABLES)){
            final DropDownCellEditor<String> assessorCellEditor = new DropDownCellEditor<String>() {
                @Override
                protected String getValue() {
                    return getListBox().getSelectedItem().getName();
                }

                @Override
                protected void setValue(String cellValue) {
                    if ("N/A".equals(cellValue)) {
                        cellValue = null;
                    }
                    getListBox().setSelectedByValue(cellValue);
                }
            };

            columnConfigs[9].setCellEditor(assessorCellEditor);
            columnConfigs[9].setCellChangesSave(new CellChange<ScheduledCourseItem>() {
                @Override
                public void saveCell(ScheduledCourseItem rowValue, String columnCodeName) {
                    rowValue.setAssessorID(assessorCellEditor.getSelectItem().getId());
                    saveCSCEditCellValue(rowValue, columnCodeName);
                }
            });

            final DropDownCellEditor<String> statusCellEditor = new DropDownCellEditor<String>() {
                @Override
                protected String getValue() {
                    return getListBox().getSelectedItem().getName();
                }

                @Override
                protected void setValue(String cellValue) {
                    getListBox().setSelectedByValue(cellValue);
                }
            };
            statusCellEditor.getListBox().setWithoutNullLabel(true);
            columnConfigs[10].setCellEditor(statusCellEditor);
            columnConfigs[10].setCellChangesSave(new CellChange<ScheduledCourseItem>() {
                @Override
                public void saveCell(ScheduledCourseItem rowValue, String columnCodeName) {
                    rowValue.setStatusID(statusCellEditor.getSelectItem().getId());
                    saveCSCEditCellValue(rowValue, columnCodeName);
                }
            });

            TCService.App.get().getCourseScheduleStatusList(new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(SelectItem[] result) {
                    statusList = result;
                    statusCellEditor.getListBox().setItems(statusList);
                }
            });

            TCService.App.get().getInstructorList(new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(SelectItem[] result) {
                    assessorCellEditor.getListBox().setItems(result);
                }
            });
        }
    }

    protected ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            RbacService.App.get().getCourseScheduleFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    throwable.printStackTrace();
                                }

                                @Override
                                public void success(FacetFilterRpc result) {
                                    callback.onSuccess(result);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.ScheduledCourse;
                    }
                };
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
                if (Utils.hasPermission(PermissionConstants.TC_SCHEDULE_COURSE_ADD)) {
                    ActionButton addNewPlacement = getAddNewButton();
                    addNewPlacement.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_SCHEDULED_COURSE + "|add/add"));
                    return addNewPlacement;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(PermissionConstants.TC_SCHEDULE_COURSE_LIST_MORE)) {
                    final ActionButton more = new ActionButton(ActionButton.getMoreString(), "", ActionButton.Type.TOOLMENU);
                    more.ensureDebugId("scheduled_course_list_more");
                    more.addClickHandler(clickEvent -> {
                        MenuBar menu = getActionsForSelections();
                        menu.setAutoOpen(true);
                        more.setMenu(menu);
                    });
                    return more;
                }
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(tcStrings.courseSchedules().toLowerCase()));
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    protected ListingRequestProvider<ScheduledCourseItem> getProvider() {
        return (filterParametrs, callback) -> {
            TCService.App.get().getCourseScheduleFromSolr(filterParametrs, new AsyncCallback<ListResult<ScheduledCourseItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<ScheduledCourseItem> result) {
                    totalCount = result.getTotal();
                    callback.onSuccess(result);
                }
            });
        };
    }

    public void refresh() {
        listingPanel.reloadPage();
    }

    protected Anchor getActions(final ScheduledCourseItem rowValue) {
        MenuBar menuBar = new MenuBar(true);
        menuBar.setAutoOpen(true);
        int menuItemCount = 0;
        //summary
        final MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-contact-small");
        summary.setCommand(

                () -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_SCHEDULED_COURSE + "|summary/" + rowValue.getObjectID() + "/" + (rowValue.getInstructorID() == null)));
        menuItemCount++;
        menuBar.addItem(summary);

        if (Utils.hasPermission(PermissionConstants.TC_EDIT_CONFIRMED_SCHEDULED_COURCE)) {
            //edit
            final MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
            edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_SCHEDULED_COURSE + "|edit/" + rowValue.getObjectID()));
            menuItemCount++;
            menuBar.addItem(edit);
        }
        if (Utils.hasPermission(PermissionConstants.TC_CLONE_CONFIRMED_SCHEDULED_COURCE)) {
            //clone
            final MenuPopItem clone = new MenuPopItem(wfmStrings.clonE(), "icon-contact-small");
            clone.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_SCHEDULED_COURSE + "|cloneCourseSchedule/" + rowValue.getObjectID()));
            menuItemCount++;
            menuBar.addItem(clone);
        }
        if (Utils.hasPermission(PermissionConstants.TC_UNASSIGN_CONFIRMED_SCHEDULED_COURCE)) {
            final MenuPopItem unassign = new MenuPopItem(tcStrings.unassign(), "icon-instructor-unassign");
            unassign.setCommand(() -> {
                WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                wfmMessageBox.setTitle(wfmStrings.confirmation());
                wfmMessageBox.setMessage(wfmMessages.sureYouWantToUnAssign(rowValue.getNumber()));
                wfmMessageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        TCService.App.get().unAssignInstructorFromScheduledCourse(rowValue.getObjectID(), new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            @Override
                            public void onSuccess(Boolean isUnAssigned) {
                                LoadingPanel.loading(false);
                                if (isUnAssigned) {
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SCHEDULED_COURSE_SAVED, isUnAssigned, ScheduledCourseListView.this);
                                } else {
                                    Info.show("You can't unassign the instructor from scheduled course!", Info.Type.WARNING);
                                }
                            }
                        });
                    }
                });
                wfmMessageBox.open();
            });
            menuItemCount++;
            menuBar.addItem(unassign);
        }

        //Student Roster Import functional
        if (Utils.hasPermission(TC_IMPORT_STUDENT_ROSTER)) {
            MenuPopItem importStudentRoster = new MenuPopItem("Import Student Roster", "icon-import");

            MenuBar importStudentMenuBar = new MenuBar(true);
            importStudentMenuBar.setAutoOpen(true);

            final MenuPopItem importXml = new MenuPopItem("from XML", "icon-xml-import");
            importXml.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("xmlimport|add/add"));
            // menuItemCount++;
            importStudentMenuBar.addItem(importXml);


            final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.SCHEDULED_COURSE, null);
            imp.setSubmitCompleted(() -> {
                if (imp.getObjectId() != null) {
                    //goTo("import|add/add/" + imp.getObjectId());
                    TCService.App.get().importStudent(rowValue.getObjectID(), imp.getObjectId(), new AsyncCallback<String>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void onSuccess(String result) {
                            LoadingPanel.loading(false);
                            Info.show(result);
                        }
                    });
                }
            });

            MenuPopItem cvsItem = new MenuPopItem("from CSV", "icon-csv-import");
            cvsItem.setCommand(() -> imp.open());
            importStudentMenuBar.addItem(cvsItem);
            // menuItemCount++;
            importStudentRoster.setSubMenu(importStudentMenuBar);
            menuItemCount++;
            menuBar.addItem(importStudentRoster);
            //menuBar.addItem(cvsItem);
        }

        if (Utils.hasPermission(PermissionConstants.TC_DELETE_CONFIRMED_SCHEDULED_COURCE)) {
            //delete
            final MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-remove");
            delete.setCommand(() -> {
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
                        TCService.App.get().deleteCourseSchedule(rowValue.getObjectID(), new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            @Override
                            public void onSuccess(Boolean isDeleted) {
                                LoadingPanel.loading(false);
                                if (isDeleted) {
                                    Info.show(wfmMessages.yourSomethingHasBeenDeleted(tcStrings.courseSchedules().toLowerCase()));
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SCHEDULED_COURSE_SAVED, isDeleted, ScheduledCourseListView.this);
                                } else {
                                    Info.show("You can't delete the scheduled course!", Info.Type.WARNING);
                                }
                            }
                        });
                    }
                });
                wfmMessageBox.open();
            });

            if (!rowValue.hasInvoice()) {
                menuItemCount++;
                menuBar.addItem(delete);
            }
        }

        ToolItem toolItem = new ToolItem(menuItemCount);
        toolItem.setWidget(menuBar);
        /* action.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                summary.setVisible(true);
            }
        });*/
        return toolItem.getAction();
    }

    protected SimpleLink getSimpleLink(ScheduledCourseItem rowValue) {
        return new SimpleLink(rowValue.getCourseName(), (TC_SCHEDULED_COURSE + "|summary/" + rowValue.getObjectID() + "/" + (rowValue.getInstructorID() == null)));
    }

    private void saveCSCEditCellValue(ScheduledCourseItem rowValue, String columnCodeName) {
        TCService.App.get().saveCSCEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
        });
    }

    private MenuBar getActionsForSelections() {
        if (selectedItems != null && selectedItems.size() > 0) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(false);
                actions.getMenuBar().addStyleName("my-menu");

                //REMOVE COURSE SCHEDULE
                com.google.gwt.user.client.ui.MenuItem remove = new com.google.gwt.user.client.ui.MenuItem("<span>&nbsp;&nbsp;" + wfmStrings.delete() + "</span>", true, (Command) () -> {
                    actions.hide();
                    deleteSelection();
                });
                actions.getMenuBar().addItem(remove);

            }
            return actions.getMenuBar();
        } else {
            if (emptyActions == null) {
                emptyActions = new ContextMenu();
                emptyActions.getMenuBar().setAutoOpen(false);
                emptyActions.addMenuItem(wfmStrings.selectAnyItemToActivateBatchActions(), null, true, null);
            }
            return emptyActions.getMenuBar();
        }

    }

    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow("Course Schedule"), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        messageBox.setMessage(wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords());
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                final java.util.ArrayList<Integer> ids = ScheduledCourseItem.getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    TCService.App.get().deleteCourseScheduleByIds(ids.toArray(new Integer[]{}), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                            caught.printStackTrace();
                        }

                        @Override
                        public void onSuccess(Void result) {
                            LoadingPanel.loading(false);
                            listingPanel.reloadPage();
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    private String getStatusCodeByName(String status) {
        if (statusList != null) {
            for (SelectItem item : statusList) {
                if (item.getName().equals(status)) {
                    return item.getDescription();
                }
            }
        }

        return null;
    }
    @Override
    public String getIconStyle() {
        return "bgMark schedule-course-icon";
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

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, tcStrings.courseScheduleFilter());
        contentConfigure.addContentConfigure(FacetContentType.CourseScheduleFaceFilter.getContentCode()[0], wfmStrings.course(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCourseScheduleRepresenter.FIELD_COURSE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCourseScheduleRepresenter.FIELD_COURSE_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CourseScheduleFaceFilter.getContentCode()[1], Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCourseScheduleRepresenter.FIELD_LOCATION_ID;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCourseScheduleRepresenter.FIELD_LOCATION_ID_NAME;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CourseScheduleFaceFilter.getContentCode()[2], wfmStrings.language(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCourseScheduleRepresenter.FIELD_LANGUAGE_ID;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCourseScheduleRepresenter.FIELD_LANGUAGE_ID_NAME;  //To change body of implemented methods use File | Settings | File Templates.
            }

        });
        contentConfigure.addContentConfigure(FacetContentType.CourseScheduleFaceFilter.getContentCode()[3], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCourseScheduleRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCourseScheduleRepresenter.FIELD_STATUS_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CourseScheduleFaceFilter.getContentCode()[4], wfmStrings.instructor(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCourseScheduleRepresenter.FIELD_INSTRUCTOR_ID;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCourseScheduleRepresenter.FIELD_INSTRUCTOR_ID_NAME;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        return contentConfigure;
    }
    @Override
    public String getPropertyCode() {
        return "scheduledcourses";
    }
}
