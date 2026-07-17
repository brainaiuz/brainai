package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
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
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseSubjectItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Java6
 * Date: 27.12.12
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public class CourseSubjectListView extends BaseListView {

    public static final TCStrings tcStrings = TCStrings.App.get();
    private final TCServiceAsync tcService = TCService.App.get();
    private ListingPanel<CourseSubjectItem> listingPanel;

    public CourseSubjectListView() {
        super("coursesubject");
        setDescription(property.getPlural(tcStrings.courseSubject()));
    }


    @Override
    public String getIconStyle() {
        return "bgMark course-subject-icon";
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.CourseSubjectListPanel, getColumns(), getListingRequestProvider(), getDesign());
        add(listingPanel);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_COURSE_SUBJECT_DELETE, CourseSubjectListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_COURSE_SUBJECT, CourseSubjectListView.this, (sender, args) -> listingPanel.reloadPage());
        return null;
    }

    public CustomColumnDefinitionConfig[] getColumns() {
        CustomColumnDefinitionConfig[] columns = new CustomColumnDefinitionConfig[4];
        //Action
        columns[0] = new ColumnDefinitionConfig<CourseSubjectItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CourseSubjectItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                int menuItemCount = 0;
                //Edit Subject item
                final MenuPopItem subjectEdit = new MenuPopItem(tcStrings.editSubject(), "icon-employee-edit-profile");
                subjectEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("courseSubject" + "|add/add/" + rowValue.getObjectId()));
                menuItemCount++;
                menuBar.addItem(subjectEdit);
                subjectEdit.setVisible(Utils.hasPermission(PermissionConstants.TC_SUBJECT_EDIT));
                //Delete Subject item
                final MenuPopItem deleteSubject = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                deleteSubject.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    //message.setSize(300, 150);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            tcService.deleteCourseSubject(rowValue.getObjectId(), new AbstractAsyncCallback<Integer>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Integer result) {
                                    LoadingPanel.loading(false);
                                    if (result == 0) {
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), tcStrings.courseSubject()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COURSE_SUBJECT_DELETE, result, CourseSubjectListView.this);
                                    } else {
                                        Info.show(tcStrings.courseSubjectNotDeleted(), Info.Type.WARNING);
                                    }

                                }
                            });
                        }
                    });
                    message.open();
                });
                menuItemCount++;
                menuBar.addItem(deleteSubject);
                deleteSubject.setVisible(Utils.hasPermission(PermissionConstants.TC_SUBJECT_DELETE));

                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                Anchor action = toolItem.getAction();
                action.addClickHandler(event -> deleteSubject.setVisible(true));
                return action;
            }
        };
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);
        //Name
        columns[1] = new ColumnDefinitionConfig<CourseSubjectItem, String>(wfmStrings.name(), CourseSubjectItem.NAME, 100) {
            @Override
            public String getCellValue(CourseSubjectItem rowValue) {
                return rowValue.getName() != null ? rowValue.getName() : "";
            }
        };
        columns[1].setMinimumColumnWidth(50);
        //Descrption
        columns[2] = new ColumnDefinitionConfig<CourseSubjectItem, String>(wfmStrings.description(), CourseSubjectItem.DESCRIPTION, 100) {
            @Override
            public String getCellValue(CourseSubjectItem rowValue) {
                return rowValue.getDescription();
            }
        };
        columns[2].setMinimumColumnWidth(100);
        //Parent
        columns[3] = new ColumnDefinitionConfig<CourseSubjectItem, String>(wfmStrings.parent(), CourseSubjectItem.PARENT, 100) {
            @Override
            public String getCellValue(CourseSubjectItem rowValue) {
                return rowValue.getParent() != null ? rowValue.getParent().getName() : "";
            }
        };
        columns[3].setMinimumColumnWidth(100);
        return columns;
    }

    public ListingRequestProvider<CourseSubjectItem> getListingRequestProvider() {
        return (filterParameter, callback) -> {
            tcService.getCourseSubjectList(filterParameter, new AbstractAsyncCallback<ListResult<CourseSubjectItem>>() {
                @Override
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void success(ListResult<CourseSubjectItem> result) {
                    int totalCount = result.getTotal();
                    callback.onSuccess(result);
                }
            });
        };
    }


    public ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.TC_SUBJECT_ADD)){
                    ActionButton addNewPlacement = getAddNewButton();
                    addNewPlacement.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("courseSubject|add/add"));
                    return addNewPlacement;
                }
                return null;
            }
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
    @Override
    public String getPropertyCode() {
        return "coursesubject";
    }

}
