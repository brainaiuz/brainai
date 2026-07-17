package com.edatasite.workforce.gwt.meetingMinutes.client.ui;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesItem;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.HashMap;
import java.util.HashSet;

/**
 * User: developer
 * Date: 4/18/12
 * Time: 4:14 PM
 */
public class MeetingMinutesListView extends BaseListView implements Constants {

//    private static final MeetingMinutesString meetingMinutesString = MeetingMinutesString.App.get();

    private ListingPanel<MeetingMinutesItem> listPanel;
    protected HashSet selectedItems = new HashSet();
    private final HorizontalPanel postPanel = new HorizontalPanel();
    String add, edit, convert, remove;
    private final boolean isFromHRMS;


    public MeetingMinutesListView(boolean isFromHRMS) {
        super("meetingMinutes");
        setDescription(property.getPlural(wfmStrings.meetingMinutes()));
        this.isFromHRMS = isFromHRMS;
        if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.ADD_MEETING_MINUTES : PermissionConstants.ADD_MEETING_MINUTES_WORKSPACE)) {
            setAddNew("meetingMinutes|add/add");
        }
    }

    @Override
    public String getIconStyle() {
        return "hrms meetingMinutes-list";
    }

    @Override
    protected Widget onInitialize() {
        add = Utils.isHRMS() ? PermissionConstants.ADD_MEETING_MINUTES : PermissionConstants.ADD_MEETING_MINUTES_WORKSPACE;
        edit = Utils.isHRMS() ? PermissionConstants.EDIT_MEETING_MINUTES : PermissionConstants.EDIT_MEETING_MINUTES_WORKSPACE;
        convert = Utils.isHRMS() ? PermissionConstants.CONVERT_MEETING_MINUTES : PermissionConstants.CONVERT_MEETING_MINUTES_WORKSPACE;
        remove = Utils.isHRMS() ? PermissionConstants.REMOVE_MEETING_MINUTES : PermissionConstants.REMOVE_MEETING_MINUTES_WORKSPACE;
        listPanel = new GuideListingPanel(ListPanelType.MeetingMinutesListPanel, getColumnConfig(), getListProvider(), getListDesign());

        listPanel.setPDFListener(event -> {
            String pdfURL = "pdf/downloadWorkspaceMeetingMinutesViewPDF";
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            listPanel.callListPDF(pdfURL, filterParametrs);
        });

        listPanel.setExcelListener(event -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadMeetingMinutesListExcelHandler";
            listPanel.callListExcel(excelURL, listPanel.getFilterParametrs());
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());

        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MEETING_MINUTES_ADD, MeetingMinutesListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MEETING_MINUTES_EDIT, MeetingMinutesListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MEETING_MINUTES_SAVED, MeetingMinutesListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MEETING_MINUTES_DELETED, MeetingMinutesListView.this, (sender, args) -> listPanel.reloadPage());

        listPanel.addSelectionRowHandler(selectedRows -> selectedItems = selectedRows);
        add(postPanel);
        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[9];
        //action
        columns[0] = new ColumnDefinitionConfig<MeetingMinutesItem, Anchor>(wfmStrings.action(), MeetingMinutesItem.ACTION, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final MeetingMinutesItem item) {

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);


                final MenuPopItem meetingMinutesSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-team-small");
                meetingMinutesSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("meetingMinutes|summary/" + item.getObjectID(), item.getMeetingNumber(), item.getName()));
                actionItemCount++;
                menuBar.addItem(meetingMinutesSummary);

                //edit meeting
                if (Utils.hasPermission(edit) || (item.getPreparedBy() != null && Utils.getUserID().equals(item.getPreparedBy().getId()))) {
                    MenuPopItem meetingMinutesEdit = new MenuPopItem(wfmStrings.edit(), "icon-issue-edit-small");
                    meetingMinutesEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("meetingMinutes|edit/" + item.getObjectID(), item.getMeetingNumber(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(meetingMinutesEdit);
                }
                //copy meeting

                if (Utils.hasPermission(add)) {
                    final MenuPopItem copyMeeting = new MenuPopItem(wfmStrings.copy(), "list-action-menu-icon icon-copy");
                    copyMeeting.ensureDebugId("copy_meeting_minutes");
                    copyMeeting.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("meetingMinutes|add/add/copyMeeting/" + item.getObjectID().toString()));
                    actionItemCount++;
                    menuBar.addItem(copyMeeting);
                }
                //Convert to project
                if (Utils.hasPermission(convert) || (item.getPreparedBy() != null && Utils.getUserID().equals(item.getPreparedBy().getId()))) {
                    MenuPopItem convertMeeting = new MenuPopItem(wfmStrings.convertTo() + " " + Property.get(Constants.PROJECT, wfmStrings.project()), "list-action-menu-icon icon-convert-small");
                    convertMeeting.ensureDebugId("convert_to_project_meeting_minutes");
                    convertMeeting.setCommand(() -> {
                        if (item.getObjectID() != null && item.getName() != null) {
                            LoadingPanel.loading(true);
                            MeetingMinutesService.App.get().getMeetingConvertedStatus(item.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void success(Boolean result) {
                                    LoadingPanel.loading(false);
                                    if (result) {
                                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                                        messageBox.setTitle(wfmStrings.information());
                                        messageBox.setMessage(wfmStrings.thisTenderAlreadyConverted());
                                        messageBox.addCloseHandler(new CloseHandler() {
                                            @Override
                                            public void onSubmit() {
                                                meetingMinutesConvertToProject(item.getObjectID());
                                            }
                                        });
                                        messageBox.open();
                                    } else {
                                        meetingMinutesConvertToProject(item.getObjectID());
                                    }
                                }
                            });
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(convertMeeting);
                }
                //remove
                if (Utils.hasPermission(remove) || (item.getPreparedBy() != null && Utils.getUserID().equals(item.getPreparedBy().getId()))) {
                    MenuPopItem meetingMinutesDelete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    meetingMinutesDelete.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        //message.setSize(300, 150);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                MeetingMinutesService.App.get().deleteMeetingMinutes(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(Void result) {
                                        Info.show(wfmStrings.messSuccessfulyyDeleted(), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MEETING_MINUTES_DELETED, null, MeetingMinutesListView.this);
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(meetingMinutesDelete);
                }

                final MenuPopItem pdfItem = new MenuPopItem(wfmStrings.pdf(), "icon-document-pdf");
                pdfItem.ensureDebugId("pdfVersion");
                pdfItem.setCommand(() -> {
                    String pdfURL = CommandConstants.PDF_URL + "/meetingMinutViewPDFHandler";
                    final RequestObject requestObject = new RequestObject(item.getObjectID());
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
        columns[0].setColumnSortable(false);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        //meeting id
        columns[1] = new ColumnDefinitionConfig<MeetingMinutesItem, Widget>(wfmStrings.number(), MeetingMinutesItem.MEETING_ID, 120) {
            @Override
            public Widget getCellValue(MeetingMinutesItem item) {
                Label label = new Label(item.getMeetingNumber());
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("meetingMinutes|summary/" + item.getObjectID(), item.getMeetingNumber(), item.getName()));
                return label;
            }
        };
//        columns[1].setColumnSortable(false);
        columns[1].setMinimumColumnWidth(40);
        columns[1].setColumnSortable(true);
        //name
        columns[2] = new ColumnDefinitionConfig<MeetingMinutesItem, Widget>(wfmStrings.name(), MeetingMinutesItem.NAME, 140) {
            @Override
            public Widget getCellValue(final MeetingMinutesItem item) {
                Label label = new Label(item.getName() != null ? item.getName() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("meetingMinutes|summary/" + item.getObjectID(), item.getMeetingNumber(), item.getName()));
                return label;
            }
        };
        columns[2].setMinimumColumnWidth(70);
        //location
        columns[3] = new ColumnDefinitionConfig<MeetingMinutesItem, String>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), MeetingMinutesItem.LOCATION, 120) {
            @Override
            public String getCellValue(MeetingMinutesItem item) {
                return item.getLocation();
            }
        };
//        columns[3].setColumnSortable(false);
        columns[3].setMinimumColumnWidth(70);
        //type
        columns[4] = new ColumnDefinitionConfig<MeetingMinutesItem, String>(wfmStrings.type(), MeetingMinutesItem.TYPE, 120) {
            @Override
            public String getCellValue(MeetingMinutesItem item) {
                return item.getType() != null ? item.getType().getName() : "";
            }
        };
//        columns[4].setColumnSortable(false);
        columns[4].setMinimumColumnWidth(70);
        //called by
        columns[5] = new ColumnDefinitionConfig<MeetingMinutesItem, String>(wfmStrings.calledBy(), MeetingMinutesItem.CALLED_BY, 120) {
            @Override
            public String getCellValue(MeetingMinutesItem rowValue) {
                return rowValue.getCalledBy() != null ? rowValue.getCalledBy().getName() : "";
            }
        };
//        columns[5].setColumnSortable(false);
        columns[5].setMinimumColumnWidth(60);
        //start date
        columns[6] = new ColumnDefinitionConfig<MeetingMinutesItem, String>(wfmStrings.startDate(), MeetingMinutesItem.DATE, 120) {
            @Override
            public String getCellValue(MeetingMinutesItem rowValue) {
                return rowValue.getStartdate() != null ? DateUtils.formatInternal(rowValue.getStartdate()) : "";
            }
        };
//        columns[6].setColumnSortable(false);
        columns[6].setMinimumColumnWidth(60);
        //end date
        columns[7] = new ColumnDefinitionConfig<MeetingMinutesItem, String>(wfmStrings.endDate(), MeetingMinutesItem.END_DATE, 120) {
            @Override
            public String getCellValue(MeetingMinutesItem rowValue) {
                return rowValue.getEnddate() != null ? DateUtils.formatInternal(rowValue.getEnddate()) : "";
            }
        };
//        columns[7].setColumnSortable(false);
        columns[7].setMinimumColumnWidth(60);
        //prepared by
        columns[8] = new ColumnDefinitionConfig<MeetingMinutesItem, String>(wfmStrings.preparedBy(), MeetingMinutesItem.PREPARED_BY, 120) {
            @Override
            public String getCellValue(MeetingMinutesItem rowValue) {
                return rowValue.getPreparedBy() != null ? rowValue.getPreparedBy().getName() : "";
            }
        };
//        columns[8].setColumnSortable(false);
        columns[8].setMinimumColumnWidth(60);

        return columns;
    }

    private void meetingMinutesConvertToProject(final Integer meetingID) {
        LoadingPanel.loading(true);
        MeetingMinutesService.App.get().convertMeetingMinutesToProject(meetingID, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer projectID) {
                LoadingPanel.loading(false);
                if (projectID != null) {
                    Info.show(wfmStrings.meetingMinutesHaveBeenConvertedToProject(), Info.Type.INFO);
                    String editProject = GWT.getHostPageBaseURL() + "ProjectManagement.html#" + Constants.PROJECT + "|edit/" + projectID;
                    Window.open(editProject, "_blank", "");
                } else {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }
            }
        });
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.ADD_MEETING_MINUTES : PermissionConstants.ADD_MEETING_MINUTES_WORKSPACE)) {
                    return () -> SinksContainerFactory.entryPoint.onHistoryChanged("meetingMinutes|add/add");
                }
                return null;
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
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(add)) {
                    ActionButton newMeetingMinutes = getAddNewButton();
                    newMeetingMinutes.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("meetingMinutes|add/add"));
                    return newMeetingMinutes;
                } else {
                    return null;
                }
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
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message;
                message = new DefaultNoItemsMessage(wfmStrings.noMeetingText());
                if (Utils.hasPermission(add)) {
                    message.setTextBeforeLink(wfmStrings.noMeetingLink());
                    message.setHref("meetingMinutes|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<MeetingMinutesItem> getListProvider() {
        return (filterParametrs, meetingMinutesItemListingCallback) -> {

            filterParametrs.setHRMS(isFromHRMS);
            MeetingMinutesService.App.get().getMeetingMinutes(filterParametrs, new AbstractAsyncCallback<ListResult<MeetingMinutesItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    meetingMinutesItemListingCallback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<MeetingMinutesItem> meetingMinutesList) {
                    meetingMinutesItemListingCallback.onSuccess(meetingMinutesList);
                }
            });
        };
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

    public String getPropertyCode() {
        return "meetingMinutes";
    }
}