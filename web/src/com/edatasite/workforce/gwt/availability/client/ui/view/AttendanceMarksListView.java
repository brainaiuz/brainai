package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AttendanceMarkListItem;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
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
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Attendance Marks (Отметки) listing view.
 */
public class AttendanceMarksListView extends BaseListView {

    private ListingPanel<AttendanceMarkListItem> listingPanel;
    private HashSet<AttendanceMarkListItem> selectedItems = new HashSet<>();

    public AttendanceMarksListView() {
        super("attendanceMarksListView", wfmStrings.attendanceMarks());
    }

    @Override
    public String getIconStyle() {
        return "availability";
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.AttendanceMarksListPanel, buildColumns(), buildProvider(), buildDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);

        listingPanel.addSelectionRowHandler(rows -> selectedItems = rows);

        listingPanel.setPDFListener(event ->
                listingPanel.callListPDF(CommandConstants.PDF_URL + "/attendanceMarksListPDFHandler", listingPanel.getFilterParametrs()));
        listingPanel.setExcelListener(event ->
                listingPanel.callListExcel(CommandConstants.COMMON_URL + "/downloadAttendanceMarksListExcel", listingPanel.getFilterParametrs()));

        add(listingPanel);
        return null;
    }

    private void approveSelected() {
        if (selectedItems == null || selectedItems.isEmpty()) {
            Info.show(wfmStrings.pleaseSelect(), Info.Type.INFO);
            return;
        }
        ArrayList<Integer> ids = selectedItems.stream()
                .map(AttendanceMarkListItem::getObjectId)
                .collect(Collectors.toCollection(ArrayList::new));
        LoadingPanel.loading(true);
        AvailabilityService.App.get().approveAttendanceMarks(ids, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                listingPanel.reloadPage();
                Info.show(wfmStrings.approved(), Info.Type.INFO);
            }
        });
    }

    private ColumnDefinitionConfig<?, ?>[] buildColumns() {
        ColumnDefinitionConfig<?, ?>[] cols = new ColumnDefinitionConfig[23];

        cols[0] = new ColumnDefinitionConfig<AttendanceMarkListItem, SimpleLink>(
                wfmStrings.employeeCode(), AttendanceMarkListItem.EMPLOYEE_CODE, 90) {
            @Override
            public SimpleLink getCellValue(AttendanceMarkListItem item) {
                String code = item.getEmployeeCode() != null ? item.getEmployeeCode() : "";
                if (item.getEmployeeId() != null && Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE)) {
                    String tabName = !code.isEmpty() ? code : (item.getEmployeeName() != null ? item.getEmployeeName() : "");
                    return new SimpleLink(code,
                            "employeeProfile|" + Constants.EMPLOYEE_PROFILE_VIEW + "/" + item.getEmployeeId(),
                            item.getEmployeeName() != null ? item.getEmployeeName() : "",
                            tabName);
                }
                return new SimpleLink(code);
            }
        };

        cols[1] = new ColumnDefinitionConfig<AttendanceMarkListItem, SimpleLink>(
                wfmStrings.employee(), AttendanceMarkListItem.EMPLOYEE_NAME, 130) {
            @Override
            public SimpleLink getCellValue(AttendanceMarkListItem item) {
                String name = item.getEmployeeName() != null ? item.getEmployeeName() : "";
                if (item.getEmployeeId() != null && Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE)) {
                    String tabName = item.getEmployeeCode() != null && !item.getEmployeeCode().isEmpty()
                            ? item.getEmployeeCode() : name;
                    return new SimpleLink(name,
                            "employeeProfile|" + Constants.EMPLOYEE_PROFILE_VIEW + "/" + item.getEmployeeId(),
                            name,
                            tabName);
                }
                return new SimpleLink(name);
            }
        };

        cols[2] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.date(), AttendanceMarkListItem.DATE, 130) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getDate() != null ? DateUtils.getDateAndTimeFormatFull(item.getDate().getNonConvertedDate()) : "";
            }
        };

        cols[3] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.location(), AttendanceMarkListItem.LOCATION, 110) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getLocation() != null ? item.getLocation() : "";
            }
        };

        cols[4] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.department(), AttendanceMarkListItem.DEPARTMENT, 120) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getDepartment() != null ? item.getDepartment() : "";
            }
        };

        cols[5] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.position(), AttendanceMarkListItem.POSITION, 130) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getPosition() != null ? item.getPosition() : "";
            }
        };

        cols[6] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.timeslot(), AttendanceMarkListItem.TIMESLOT, 100) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getTimeslotName() != null ? item.getTimeslotName() : "";
            }
        };

        cols[7] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.supervisor(), AttendanceMarkListItem.SUPERVISOR, 130) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getSupervisor() != null ? item.getSupervisor() : "";
            }
        };

        cols[8] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.deviceId(), AttendanceMarkListItem.DEVICE_ID, 200) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getDeviceId() != null ? item.getDeviceId() : "";
            }
        };

        cols[9] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.terminal(), AttendanceMarkListItem.TERMINAL, 120) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getTerminal() != null ? item.getTerminal() : "";
            }
        };

        cols[10] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.approvalStatus(), AttendanceMarkListItem.APPROVAL_STATUS, 90) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return "APPROVED".equals(item.getApprovalStatus()) ? wfmStrings.approved() : wfmStrings.pending();
            }
        };
        cols[10].setColumnSortable(true);

        cols[11] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.role(), AttendanceMarkListItem.ROLE, 120) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getRole() != null ? item.getRole() : "";
            }
        };

        cols[12] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.inOut(), AttendanceMarkListItem.VALIDATED, 60) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getValidated() != null ? item.getValidated() : "";
            }
        };

        cols[13] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.source(), AttendanceMarkListItem.SOURCE, 80) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return formatSource(item.getSource());
            }
        };

        cols[14] = new ColumnDefinitionConfig<AttendanceMarkListItem, Widget>(
                wfmStrings.photo(), "pictureUrl", 60) {
            @Override
            public Widget getCellValue(AttendanceMarkListItem item) {
                if (item.getPictureUrl() == null || item.getPictureUrl().isEmpty()) {
                    return new Label("");
                }
                Image thumbnail = new Image(item.getPictureUrl());
                thumbnail.getElement().setAttribute("height", "auto");
                thumbnail.getElement().setAttribute("width", "80%");
                thumbnail.getElement().setAttribute("max-height", "40px");
                thumbnail.addClickHandler(event -> {
                    Image fullImage = new Image(item.getPictureUrl());
                    KpiModal dialog = new KpiModal();
                    dialog.setCloseButton(true);
                    VerticalPanel panel = new VerticalPanel();
                    panel.setStyleName("imagePanel");
                    panel.setSpacing(2);
                    panel.add(fullImage);
                    dialog.add(panel);
                    dialog.center();
                });
                return thumbnail;
            }
        };
        cols[14].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        cols[14].addStyleAttribute("paddingRight", "5px");
        cols[14].setColumnSortable(false);

        cols[15] = new ColumnDefinitionConfig<AttendanceMarkListItem, Widget>(
                wfmStrings.map(), AttendanceMarkListItem.MAP, 60) {
            @Override
            public Widget getCellValue(AttendanceMarkListItem item) {
                if (!"MOBILE".equals(item.getSource()) || item.getLatitude() == null || item.getLongitude() == null) {
                    return new Label("");
                }
                Anchor link = new Anchor("Map");
                link.setHref("https://www.google.com/maps?q=" + item.getLatitude() + "," + item.getLongitude());
                link.setTarget("_blank");
                return link;
            }
        };
        cols[15].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        cols[15].setColumnSortable(false);

        cols[16] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.createdDate(), AttendanceMarkListItem.CREATED_DATE, 130) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getCreatedDate() != null ? DateUtils.getDateAndTimeFormatFull(item.getCreatedDate().getNonConvertedDate()) : "";
            }
        };
        cols[16].setColumnSortable(true);

        cols[17] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.createdBy(), AttendanceMarkListItem.CREATED_BY_NAME, 120) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getCreatedByName() != null ? item.getCreatedByName() : wfmStrings.system();
            }
        };
        cols[17].setColumnSortable(true);

        cols[18] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.note(), AttendanceMarkListItem.NOTE, 150) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getNote() != null ? item.getNote() : "";
            }
        };
        cols[18].setColumnSortable(true);

        cols[19] = new ColumnDefinitionConfig<AttendanceMarkListItem, Widget>(
                wfmStrings.profilePhoto(), AttendanceMarkListItem.PROFILE_PICTURE_URL, 60) {
            @Override
            public Widget getCellValue(AttendanceMarkListItem item) {
                if (item.getProfilePictureUrl() == null || item.getProfilePictureUrl().isEmpty()) {
                    return new Label("");
                }
                Image thumbnail = new Image(item.getProfilePictureUrl());
                thumbnail.getElement().setAttribute("height", "auto");
                thumbnail.getElement().setAttribute("width", "80%");
                thumbnail.getElement().setAttribute("max-height", "40px");
                thumbnail.addClickHandler(event -> {
                    Image fullImage = new Image(item.getProfilePictureUrl());
                    KpiModal dialog = new KpiModal();
                    dialog.setCloseButton(true);
                    VerticalPanel panel = new VerticalPanel();
                    panel.setStyleName("imagePanel");
                    panel.setSpacing(2);
                    panel.add(fullImage);
                    dialog.add(panel);
                    dialog.center();
                });
                return thumbnail;
            }
        };
        cols[19].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        cols[19].addStyleAttribute("paddingRight", "5px");
        cols[19].setColumnSortable(false);

        cols[20] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.modifiedDate(), AttendanceMarkListItem.UPDATED_AT, 130) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getUpdatedAt() != null ? DateUtils.formatInternal(item.getUpdatedAt().getNonConvertedDate()) : "";
            }
        };
        cols[20].setColumnSortable(true);

        cols[21] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.modifiedBy(), AttendanceMarkListItem.UPDATED_BY_NAME, 120) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return item.getUpdatedByName() != null ? item.getUpdatedByName() : "";
            }
        };
        cols[21].setColumnSortable(true);

        cols[22] = new ColumnDefinitionConfig<AttendanceMarkListItem, String>(
                wfmStrings.isAuto(), AttendanceMarkListItem.IS_AUTO, 70) {
            @Override
            public String getCellValue(AttendanceMarkListItem item) {
                return Boolean.TRUE.equals(item.getIsAuto()) ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        cols[22].setColumnSortable(true);

        return cols;
    }

    private static FacetFieldConfigure field(String criteriaName, String facetName) {
        return new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return criteriaName;
            }

            @Override
            public String getSolrFacetFieldName() {
                return facetName;
            }
        };
    }

    private String formatSource(String source) {
        if (source == null) return "";
        switch (source) {
            case "FACE_ID":
                return "Face ID";
            case "MOBILE":
                return "Mobile";
            case "WEB":
                return "Web";
            case "UNKNOWN":
                return "Unknown";
            default:
                return source;
        }
    }

    private ListingRequestProvider<AttendanceMarkListItem> buildProvider() {
        return (fp, callback) -> AvailabilityService.App.get().getAttendanceMarkList(fp,
                new AbstractAsyncCallback<ListResult<AttendanceMarkListItem>>() {
                    @Override
                    public void success(ListResult<AttendanceMarkListItem> result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void failure(Throwable t) {
                        callback.onFailure(t);
                    }
                });
    }

    private ListingPanelDesign buildDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> AvailabilityService.App.get().getAttendanceMarkFacetData(data,
                                new AbstractAsyncCallback<FacetFilterRpc>() {
                                    @Override
                                    public void success(FacetFilterRpc result) {
                                        callback.onSuccess(result);
                                    }

                                    @Override
                                    public void failure(Throwable t) {
                                        callback.onFailure(t);
                                    }
                                });
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        FacetContentConfigure cfg = new FacetContentConfigure(2, wfmStrings.filter());
                        cfg.addContentConfigure("employee", wfmStrings.employee(), field("employee", "employee"));
                        cfg.addContentConfigure("department", Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), field("department", "department"));
                        cfg.addContentConfigure("position", wfmStrings.position(), field("position", "position"));
                        cfg.addContentConfigure("location", Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), field("location", "location"));
                        cfg.addContentConfigure("timeslot", wfmStrings.timeslot(), field("timeslot", "timeslot"));
                        cfg.addContentConfigure("supervisor", wfmStrings.supervisor(), field("supervisor", "supervisor"));
                        cfg.addContentConfigure("terminal", wfmStrings.terminal(), field("terminal", "terminal"));
                        cfg.addContentConfigure("source", wfmStrings.source(), field("source", "source"));
                        cfg.addContentConfigure("isAuto", wfmStrings.isAuto(), field("isAuto", "isAuto"));
                        cfg.addContentConfigure("status", wfmStrings.status(), field("status", "status"));
                        cfg.addContentConfigure("role", wfmStrings.role(), field("role", "role"));
                        cfg.addContentConfigureDateListBox("date", wfmStrings.date());
                        return cfg;
                    }
                };
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown ignoredMenuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                emptyDataTable.initEmptyDataTable(new DefaultNoItemsMessage(wfmStrings.noAttendanceMarksFound()));
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (!Utils.hasPermission(PermissionConstants.HRMS_ATTENDANCE_MARKS) || !Utils.hasPermission(PermissionConstants.HRMS_APPROVE_ATTENDANCE_MARKS))
                    return null;
                ActionButton more = new ActionButton(ActionButton.getMoreString(), ActionButton.Type.TOOLMENU);
                more.addDomHandler(event -> {
                    MenuBar menu = new MenuBar(true);
                    menu.setAutoOpen(true);
                    MenuPopItem approveItem = new MenuPopItem(wfmStrings.approveSelected());
                    approveItem.setScheduledCommand(() -> approveSelected());
                    menu.addItem(approveItem);
                    more.setMenu(menu);
                }, MouseOverEvent.getType());
                return more;
            }
        };
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
