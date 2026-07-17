package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.TeammatesAvailability;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.StatusService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class TeamAvailabilityView extends View implements Constants, FittedContent {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public TeamAvailabilityView() {
        super(TEAM_AVAILABILITY_VIEW);
        setDescription(property.getPlural(wfmStrings.attendanceTracking()));
    }

    @Override
    public String getIconStyle() {
        return "availability team-availability";
    }

    @Override
    protected Widget onInitialize() {
        ListingPanel<TeammatesAvailability> listingPanel = new ListingPanel<>(ListPanelType.AttendanceTrackingListPanel, getDrawColumns(), getProvider(), getDesigner());
        add(listingPanel);
        return null;
    }

    private ColumnDefinitionConfig[] getDrawColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[6];
        //Employee name
        columns[0] = new ColumnDefinitionConfig<TeammatesAvailability, SimpleLink>(" " + wfmStrings.employee(), TeammatesAvailability.AT_EMPLOYEE_NAME, 150) {
            @Override
            public SimpleLink getCellValue(TeammatesAvailability rowValue) {
                return new SimpleLink(rowValue.getEmployee(), "teamRequest|/" + rowValue.getEmployeeId());
            }
        };
        //Department name
        columns[1] = new ColumnDefinitionConfig<TeammatesAvailability, String>(" " + Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), TeammatesAvailability.AT_DEPARTMENT_NAME, 150) {
            @Override
            public String getCellValue(TeammatesAvailability rowValue) {
                return rowValue.getDepartment();
            }
        };
        //TimeSlot name
        columns[2] = new ColumnDefinitionConfig<TeammatesAvailability, String>(" " + hrmsStrings.timeslotName(), TeammatesAvailability.AT_TIME_SLOT_NAME, 150) {
            @Override
            public String getCellValue(TeammatesAvailability rowValue) {
                return rowValue.getTimeslot();
            }
        };
        //From (duration)
        columns[3] = new ColumnDefinitionConfig<TeammatesAvailability, String>(" " + wfmStrings.from(), TeammatesAvailability.AT_FROM_DURATION, 80) {
            @Override
            public String getCellValue(TeammatesAvailability rowValue) {
                return rowValue.getFrom();
            }
        };
        columns[3].setColumnSortable(false);
        //To (duration)
        columns[4] = new ColumnDefinitionConfig<TeammatesAvailability, String>(" " + wfmStrings.to(), TeammatesAvailability.AT_TO_DURATION, 80) {
            @Override
            public String getCellValue(TeammatesAvailability rowValue) {
                return rowValue.getTo();
            }
        };
        columns[4].setColumnSortable(false);
        //Status
        if (Utils.hasRole(ADMIN) || Utils.hasRole(DR) || Utils.hasRole(HR)) {
            //Status for ADMIN, DR roles
            columns[5] = new ColumnDefinitionConfig<TeammatesAvailability, HorizontalPanelDiv>(" " + wfmStrings.status(), TeammatesAvailability.AT_STATUS_NAME, 80) {
                @Override
                public HorizontalPanelDiv getCellValue(TeammatesAvailability rowValue) {
                    ListBox listBox = getStatusListBox(rowValue.getStatus(), rowValue.getEmployeeId());
                    HorizontalPanelDiv statusPanelDiv = new HorizontalPanelDiv();
                    statusPanelDiv.add(listBox);
                    statusPanelDiv.setTextAlign("center");
                    return statusPanelDiv;
                }
            };
            columns[5].setColumnSortable(false);
        } else {
            //Status for other roles
            columns[5] = new ColumnDefinitionConfig<TeammatesAvailability, String>(" " + wfmStrings.status(), TeammatesAvailability.AT_STATUS_NAME, 80) {
                @Override
                public String getCellValue(TeammatesAvailability rowValue) {
                    return rowValue.getStatus();
                }
            };
            columns[5].setColumnSortable(false);
        }

        return columns;
    }

    private ListingPanelDesign getDesigner() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return ListingChooseFilter.ATTENDANCE_TRACKING_LIST;
                    }
                };
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.thereAreNoAttendanceTrackingInformationYet());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<TeammatesAvailability> getProvider() {
        return (filterParametrs, listingCallback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            AvailabilityService.App.get().getTeamMates(filterParametrs, new AbstractAsyncCallback<ListResult<TeammatesAvailability>>() {
                @Override
                public void failure(Throwable throwable) {
                    listingCallback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<TeammatesAvailability> result) {
                    listingCallback.onSuccess(result);
                }
            });
        };
    }

    private ListBox getStatusListBox(String statusString, final String employeeId) {
        final ListBox statusListBox = new ListBox();
        statusListBox.addItem(wfmStrings.checkedIn());
        statusListBox.addItem(wfmStrings.checkedOut());
        statusListBox.addItem(hrmsStrings.lunch());
        switch (statusString) {
            case "In":
                statusListBox.setSelectedIndex(0);
                break;
            case "Out":
                statusListBox.setSelectedIndex(1);
                break;
            default:
                statusListBox.setSelectedIndex(2);
                break;
        }
        statusListBox.addChangeHandler(sender -> {
            int selected = statusListBox.getSelectedIndex();
            String changedStatusCode;
            if (selected == 0) {
                changedStatusCode = AVAILABLE;
            } else if (selected == 1) {
                changedStatusCode = NOT_AVAILABLE;
            } else {
                changedStatusCode = BREAK;
            }
            StatusService.App.get().setUserStatus(Integer.valueOf(employeeId), changedStatusCode, false, new AbstractAsyncCallback<String>() {
                @Override
                public void failure(Throwable throwable) {
                    Info.show(hrmsStrings.statusNotChanged(), Info.Type.WARNING);
                }

                @Override
                public void success(String result) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ATTENDANCE_TRACKING_STATUS_CHANGE, result, TeamAvailabilityView.this);
                }
            });
        });
        return statusListBox;
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
        return TEAM_AVAILABILITY_VIEW;
    }
}
