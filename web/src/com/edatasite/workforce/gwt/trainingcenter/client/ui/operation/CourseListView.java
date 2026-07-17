package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * User: Ilxom Lutfullaev
 * Date: 7/20/12
 * Time: 12:55 PM
 */

public class CourseListView extends BaseListView implements TCConstants {

    private static final TCStrings tcStrings = TCStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private int totalCount = 0;

    public CourseListView() {
        super(TC_COURSE);
        setDescription(property.getPlural(wfmStrings.courses()));
    }

    @Override
    protected Widget onInitialize() {
        final ListingPanel<CourseItem> listingPanel = new ListingPanel<>(ListPanelType.CourseListPanel, getColumns(), getListingRequestProvider(), getDesign());
        listingPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadCourseListExcel";
            ListingFilterParameter filterParametrs = listingPanel.getFilterParametrs();
            listingPanel.callListExcel(excelURL, filterParametrs);
        });
        listingPanel.setPDFListener(clickEvent -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL = CommandConstants.PDF_URL + "/courseListPDFHandler";
            ListingFilterParameter filterParametrs = listingPanel.getFilterParametrs();
            listingPanel.callListPDF(pdfURL, filterParametrs);
        });
        add(listingPanel);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_COURSE_ADD_EDIT, CourseListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_COURSE_DELETE, CourseListView.this, (sender, args) -> listingPanel.reloadPage());
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        CustomColumnDefinitionConfig[] columns = new CustomColumnDefinitionConfig[9];
        //action
        columns[0] = new ColumnDefinitionConfig<CourseItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CourseItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                int menuItemCount = 0;
                //Course summary
                final MenuPopItem studentSummary = new MenuPopItem(tcStrings.courseView(), "");
                studentSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_COURSE + "|summary/" + rowValue.getObjectID()));
                menuItemCount++;
                menuBar.addItem(studentSummary);
                //Edit Course item
                final MenuPopItem courseEdit = new MenuPopItem(tcStrings.editCourseView(), "icon-employee-edit-profile");
                courseEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_COURSE + "|add/add/" + rowValue.getObjectID()));
                menuItemCount++;
                menuBar.addItem(courseEdit);
                courseEdit.setVisible(Utils.hasPermission(PermissionConstants.TC_COURSE_EDIT));
                //Delete Course item
                final MenuPopItem deleteCourse = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                deleteCourse.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    //message.setSize(300, 150);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            TCService.App.get().deleteCourse(rowValue.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Boolean result) {
                                    LoadingPanel.loading(false);
                                    if (result) {
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.course()), Info.Type.INFO);
                                    } else {
                                        Info.show(tcStrings.cantDeleteCourse(), Info.Type.WARNING);
                                    }
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COURSE_DELETE, result, CourseListView.this);
                                }
                            });
                        }
                    });
                    message.open();
                });
                menuItemCount++;
                menuBar.addItem(deleteCourse);
                deleteCourse.setVisible(Utils.hasPermission(PermissionConstants.TC_COURSE_DELETE));

                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                Anchor action = toolItem.getAction();
                action.addClickHandler(event -> deleteCourse.setVisible(true));
                return action;  //return action menu items
            }
        };
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);
        //Number
        columns[1] = new ColumnDefinitionConfig<CourseItem, SimpleLink>(wfmStrings.number(), CourseItem.NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(CourseItem rowValue) {
                return new SimpleLink((rowValue.getNumberData() != null ? rowValue.getNumberData().getNumberString() : ""), (TC_COURSE + "|summary/" + rowValue.getObjectID()));
            }
        };
        columns[1].setMinimumColumnWidth(50);
        //Name
        columns[2] = new ColumnDefinitionConfig<CourseItem, SimpleLink>(wfmStrings.name(), CourseItem.NAME, 100) {
            @Override
            public SimpleLink getCellValue(CourseItem rowValue) {
                return new SimpleLink(rowValue.getCourseName(), (TC_COURSE + "|summary/" + rowValue.getObjectID()));
            }
        };
        columns[2].setMinimumColumnWidth(100);
        //Subject
        columns[3] = new ColumnDefinitionConfig<CourseItem, String>(wfmStrings.subject(), CourseItem.SUBJECT, 100) {
            @Override
            public String getCellValue(CourseItem rowValue) {
                return rowValue.getSubject() != null ? rowValue.getSubject().getName() : "";
            }
        };
        columns[3].setMinimumColumnWidth(100);
        //Duration
        columns[4] = new ColumnDefinitionConfig<CourseItem, String>(wfmStrings.duration(), CourseItem.DURATION, 100) {
            @Override
            public String getCellValue(CourseItem rowValue) {
                return rowValue.getDuration() != null ? rowValue.getDuration().toString() + " " + wfmStrings.hours() : "";
            }
        };
        columns[4].setMinimumColumnWidth(70);
        //Validity
        columns[5] = new ColumnDefinitionConfig<CourseItem, String>(wfmStrings.validity(), CourseItem.VALIDITY, 100) {
            @Override
            public String getCellValue(CourseItem rowValue) {
                return rowValue.getValidity() != null ? rowValue.getValidity().toString() : "";
            }
        };
        columns[5].setColumnSortable(false);
        columns[5].setMinimumColumnWidth(70);
        /*//Price per student
        columns[6] = new ColumnDefinitionConfig<CourseItem, String>(tcStrings.pricePerStudent(), CourseItem.PRICEPERSTUDENT, 100) {
            @Override
            public String getCellValue(CourseItem rowValue) {
                return rowValue.getPricePerStudent() != null ? rowValue.getPricePerStudent().toString() : "";
            }
        };
        columns[6].setMinimumColumnWidth(70);*/
        //Course Requirements
        columns[6] = new ColumnDefinitionConfig<CourseItem, String>(wfmStrings.courseRequirements(), CourseItem.COURSE_REQUIREMENTS, 100) {
            @Override
            public String getCellValue(CourseItem rowValue) {
                return rowValue.getCourseRequirementsAsString();
            }
        };
        columns[6].setMinimumColumnWidth(70);
        columns[6].setColumnSortable(false);
        //Pre-requisite
        columns[7] = new ColumnDefinitionConfig<CourseItem, String>(wfmStrings.preRequisite(), CourseItem.PREREQUISITE, 100) {
            @Override
            public String getCellValue(CourseItem rowValue) {
                StringBuilder buffer = new StringBuilder();
                if (rowValue.getPreRequisite() != null) {
                    for (SelectItem item : rowValue.getPreRequisite()) {
                        buffer.append(item.getName()).append(",");
                    }
                }
                return buffer.length() > 0 ? buffer.deleteCharAt(buffer.length() - 1).toString() : "";
            }
        };
        columns[7].setMinimumColumnWidth(70);
        columns[7].setColumnSortable(false);
        //instructors
        columns[8] = new ColumnDefinitionConfig<CourseItem, String>(wfmStrings.instructors(), CourseItem.INSTRUCTOR, 100) {
            @Override
            public String getCellValue(CourseItem rowValue) {
                return rowValue.getInstructorsAsString() != null ? rowValue.getInstructorsAsString() : "";
            }
        };
        columns[8].setMinimumColumnWidth(70);
        columns[8].setColumnSortable(false);
        return columns;
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
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
                if (Utils.hasPermission(PermissionConstants.TC_COURSE_ADD)){
                    ActionButton addNewPlacement = getAddNewButton();
                    addNewPlacement.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_COURSE + "|add/add"));
                    return addNewPlacement;
                }
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(wfmStrings.courses().toLowerCase() + "."));
                message.setHref("course|add/add");
                message.setTextBeforeLink(tcStrings.noCoursesLink());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<CourseItem> getListingRequestProvider() {
        return (filterParameter, callback) -> {
            TCService.App.get().getCourseList(filterParameter, new AbstractAsyncCallback<ListResult<CourseItem>>() {
                @Override
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void success(ListResult<CourseItem> result) {
                    totalCount = result.getTotal();
                    callback.onSuccess(result);
                }
            });
        };
    }

    @Override
    public String getIconStyle() {
        return "bgMark course-icon";
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
        return TC_COURSE;
    }
}
