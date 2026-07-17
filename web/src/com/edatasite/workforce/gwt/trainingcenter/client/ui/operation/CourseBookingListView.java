package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseBookingRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCServiceAsync;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 11/08/12
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class CourseBookingListView extends BaseListView implements TCConstants, Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final TCStrings tcStrings = TCStrings.App.get();
    private static final TCServiceAsync tcService = TCService.App.get();

    private ListingPanel<CourseBookingItem> courseBookingListPanel;
    protected HashSet<CourseBookingItem> selectedItems = new HashSet<>();
    protected ContextMenu actions;
    private ContextMenu emptyActions = null;
    private int totalCount = 0;


    public CourseBookingListView() {
        super(TC_COURSE_BOOKING);
        setDescription(property.getPlural(tcStrings.courseBooking()));
    }

    @Override
    protected Widget onInitialize() {
        courseBookingListPanel = new ListingPanel<>(ListPanelType.CourseBookingListPanel, getColumnsConf(), getProvider(), getDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_COURSE_BOOKING_ADD_EDIT, CourseBookingListView.this, (sender, args) -> courseBookingListPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_COURSE_BOOKING_DELETE, CourseBookingListView.this, (sender, args) -> courseBookingListPanel.reloadPage());
        courseBookingListPanel.addSelectionRowHandler(selectedRows -> {
            selectedItems = selectedRows;
            actions = null;
        });
        courseBookingListPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadCourseBookingListExcel";
            ListingFilterParameter filterParametrs = courseBookingListPanel.getFilterParametrs();
            courseBookingListPanel.callListExcel(excelURL, filterParametrs);
        });
        courseBookingListPanel.setPDFListener(clickEvent -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL = CommandConstants.PDF_URL + "/courseBookingListPDFHandler";
            ListingFilterParameter filterParametrs = courseBookingListPanel.getFilterParametrs();
            courseBookingListPanel.callListPDF(pdfURL, filterParametrs);
        });
        add(courseBookingListPanel);
        return null;
    }

    private ColumnDefinitionConfig[] getColumnsConf() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[10];
        columns[0] = new ColumnDefinitionConfig<CourseBookingItem, Widget>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Widget getCellValue(final CourseBookingItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                // Course Boooking Summary View
                MenuPopItem courseBoookingSummaryView = new MenuPopItem(wfmStrings.summaryView(), "", () -> SinksContainerFactory.entryPoint.onHistoryChanged(TCConstants.TC_COURSE_BOOKING + "|" + TC_VIEW_COURSE_BOOKING + "/" + rowValue.getObjectID()));
                menuBar.addItem(courseBoookingSummaryView);

                // Course Boooking Attented Student
                MenuPopItem attendedStudent = new MenuPopItem(tcStrings.attendStudent(), "", () -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_COURSE_BOOKING + "|" + TCConstants.TC_ADD_STUDENT_COURSE_BOOKING + "/" + rowValue.getObjectID() + "/" + rowValue.getLocation().getId()));

                // Course Boooking Delete Action
                MenuPopItem cancelBooking = new MenuPopItem(wfmStrings.delete(), "icon-remove", () -> {
                    WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    wfmMessageBox.setTitle(wfmStrings.confirmation());
                    wfmMessageBox.setMessage(wfmMessages.sureYouWantToDelete(tcStrings.courseBooking().toLowerCase(), "?"));
                    wfmMessageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            tcService.deleteCourseBooking(rowValue.getObjectID(), new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmMessages.yourSomethingHasBeenDeleted(tcStrings.courseBooking().toLowerCase()));
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COURSE_BOOKING_DELETE, result, CourseBookingListView.this);
                                }
                            });
                        }
                    });
                    wfmMessageBox.open();
                });
                if (!BOOKING_REJECTED.equals(rowValue.getStatusCode()) && Utils.hasPermission(PermissionConstants.TC_COURSE_BOOKING_CANCLE)) {
                    menuBar.addItem(cancelBooking);
                }


                ToolItem toolItem = new ToolItem(2);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columns[1] = new ColumnDefinitionConfig<CourseBookingItem, SimpleLink>(wfmStrings.number(), CourseBookingItem.NUMBER, 80) {
            @Override
            public SimpleLink getCellValue(CourseBookingItem rowValue) {
                return new SimpleLink(rowValue.getNumber(), (TCConstants.TC_COURSE_BOOKING + "|" + TC_VIEW_COURSE_BOOKING + "/" + rowValue.getObjectID()));
            }
        };
        columns[1].setMinimumColumnWidth(60);

        columns[2] = new ColumnDefinitionConfig<CourseBookingItem, String>(wfmStrings.companyName(), CourseBookingItem.CUSTOMER, 120) {
            @Override
            public String getCellValue(CourseBookingItem rowValue) {
                return rowValue.getCustomer() != null ? rowValue.getCustomer().getName() : "N/A";
            }
        };
        columns[2].setMinimumColumnWidth(100);
        columns[2].setColumnSortable(false);

        columns[3] = new ColumnDefinitionConfig<CourseBookingItem, String>(Property.get(Constants.Contacts, wfmStrings.contactName(), wfmStrings.contact()), CourseBookingItem.CONTACT, 120) {
            @Override
            public String getCellValue(CourseBookingItem rowValue) {
                return rowValue.getContact() != null ? rowValue.getContact().getName() : "";
            }
        };
        columns[3].setMinimumColumnWidth(100);
        columns[3].setColumnSortable(false);

        columns[4] = new ColumnDefinitionConfig<CourseBookingItem, String>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), CourseBookingItem.LOCATION, 120) {
            @Override
            public String getCellValue(CourseBookingItem rowValue) {
                return rowValue.getLocation() != null ? rowValue.getLocation().getName() : "";
            }
        };
        columns[4].setMinimumColumnWidth(100);
        columns[4].setColumnSortable(false);

        columns[5] = new ColumnDefinitionConfig<CourseBookingItem, String>(wfmStrings.status(), CourseBookingItem.STATUS, 120) {
            @Override
            public String getCellValue(CourseBookingItem rowValue) {
                return rowValue.getStatus().getName();
            }
        };
        columns[5].setMinimumColumnWidth(100);
        columns[5].setColumnSortable(false);

        columns[6] = new ColumnDefinitionConfig<CourseBookingItem, String>(wfmStrings.type(), CourseBookingItem.TYPE, 120) {
            @Override
            public String getCellValue(CourseBookingItem rowValue) {
                return (rowValue.getType() != null && !rowValue.getType().getName().isEmpty()) ? rowValue.getType().getName() : "N/A";
            }
        };
        columns[6].setMinimumColumnWidth(100);
        columns[6].setColumnSortable(false);

        columns[7] = new ColumnDefinitionConfig<CourseBookingItem, String>(wfmStrings.createdDate(), CourseBookingItem.CREATIONDATE, 120) {
            @Override
            public String getCellValue(CourseBookingItem rowValue) {
                return DateUtils.formatInternal(rowValue.getCreationDate());
            }
        };
        columns[7].setMinimumColumnWidth(100);

        columns[8] = new ColumnDefinitionConfig<CourseBookingItem, String>(wfmStrings.createdBy(), CourseBookingItem.CREATOR, 120) {
            @Override
            public String getCellValue(CourseBookingItem rowValue) {
                return (rowValue.getCreator() != null && !rowValue.getCreator().getName().isEmpty()) ? rowValue.getCreator().getName() : "N/A";
            }
        };
        columns[8].setMinimumColumnWidth(100);
        columns[8].setColumnSortable(false);

        columns[9] = new ColumnDefinitionConfig<CourseBookingItem, String>("Approval By", CourseBookingItem.UPDATER, 120) {
            @Override
            public String getCellValue(CourseBookingItem rowValue) {
                return (rowValue.getUpdater() != null && !rowValue.getUpdater().getName().isEmpty()) ? rowValue.getUpdater().getName() : "N/A";
            }
        };
        columns[9].setMinimumColumnWidth(100);
        columns[9].setColumnSortable(false);
        return columns;
    }

    private ListingRequestProvider<CourseBookingItem> getProvider() {
        return (filterParametrs, callback) -> tcService.getCourseBookingListFromSolr(filterParametrs, new AsyncCallback<ListResult<CourseBookingItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<CourseBookingItem> result) {
                callback.onSuccess(result);
                totalCount = result.getTotal();
            }
        });
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            RbacService.App.get().getCourseBookingFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
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
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.TC_NEW_COURSE_BOOKING)){
                    ActionButton addButton = getAddNewButton();
                    addButton.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_COURSE_BOOKING + "|add/add"));
                    return addButton;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                final ActionButton more = new ActionButton(ActionButton.getMoreString(), "", ActionButton.Type.TOOLMENU);
                more.ensureDebugId("course_booking_list_more");
                more.addClickHandler(clickEvent -> {
                    MenuBar menu = getActionsForSelections();
                    menu.setAutoOpen(true);
                    more.setMenu(menu);
                });
                return more;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, tcStrings.courseBookingFilter());
        contentConfigure.addContentConfigure(FacetContentType.CourseBookingFacetFilter.getContentCode()[0], Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCourseBookingRepresenter.FIELD_CUSTOMER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCourseBookingRepresenter.FIELD_CUSTOMER_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CourseBookingFacetFilter.getContentCode()[1], Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCourseBookingRepresenter.FIELD_LOCATION_ID;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCourseBookingRepresenter.FIELD_LOCATION_ID_NAME;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CourseBookingFacetFilter.getContentCode()[2], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCourseBookingRepresenter.FIELD_STATUS_ID;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCourseBookingRepresenter.FIELD_STATUS_ID_NAME;  //To change body of implemented methods use File | Settings | File Templates.
            }

        });
        contentConfigure.addContentConfigure(FacetContentType.CourseBookingFacetFilter.getContentCode()[3], wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCourseBookingRepresenter.FIELD_TYPE_ID;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCourseBookingRepresenter.FIELD_TYPE_ID_NAME;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        return contentConfigure;
    }

    private MenuBar getActionsForSelections() {
        if (selectedItems != null && selectedItems.size() > 0) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(false);
                actions.getMenuBar().addStyleName("my-menu");

                //CANCEL BOOKING
                com.google.gwt.user.client.ui.MenuItem remove = new com.google.gwt.user.client.ui.MenuItem("<span>&nbsp;&nbsp;" + tcStrings.cancelBooking() + "</span>", true, (Command) () -> {
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
            Info.show(wfmMessages.pleaseSelectOneRow("Course Booking"), Info.Type.WARNING);
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
                final java.util.ArrayList<Integer> ids = CourseBookingItem.getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    tcService.deleteCourseBookingByIds(ids.toArray(new Integer[]{}), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                            caught.printStackTrace();
                        }

                        @Override
                        public void onSuccess(Void result) {
                            LoadingPanel.loading(false);
                            courseBookingListPanel.reloadPage();
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    @Override
    public String getIconStyle() {
        return "bgMark cource-booking-list";
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
    @Override
    public String getPropertyCode() {
        return TC_COURSE_BOOKING;
    }
}
