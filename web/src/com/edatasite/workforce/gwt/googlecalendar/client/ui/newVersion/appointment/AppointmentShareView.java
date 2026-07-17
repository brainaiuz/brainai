package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Attendee;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.ProjectEmployeesAvailabilityCheck;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarServiceAsync;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Feb 18, 2010
 * Time: 5:02:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentShareView extends Composite {

    private final GoogleCalendarServiceAsync calendarService = GoogleCalendarService.App.get();
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    //    private HashMap<Integer, Attendee> employees;
    private Integer appointmentID;
    private boolean fromEventPopup = false;
    private boolean fromCalendarPopup = false;
    private TableColumn[] columns;

    //tree and selected items
    private SelectPanel sharedsTree;
    private ArrayList<TeamEmployees> teamEmployees;
    private Date startDate;
    private Date endDate;
    private final String employeeText = wfmStrings.membersOnly();

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public AppointmentShareView(boolean fromCalendarPopup) {
        this.fromCalendarPopup = fromCalendarPopup;
        show(false);
    }

    public AppointmentShareView(Integer appointmentID, boolean fromEventPopup) {
        this.appointmentID = appointmentID;
        this.fromEventPopup = fromEventPopup;
        show(fromEventPopup);
    }

    private void show(boolean fromEventPopup) {
//        if (Utils.isFromWorkforcetrack()) {
//            employeeText = wfmStrings.employees();
//        }
        columns = new TableColumn[fromEventPopup ? 2 : 3];
        columns[0] = new TableColumn("employee", employeeText, 150);
        if (!fromEventPopup) {
            columns[1] = new TableColumn("color", wfmStrings.color(), 50);
        }
        columns[fromEventPopup ? 1 : 2] = new TableColumn("delete", wfmStrings.action(), 50);
        sharedsTree = new SelectPanel(columns);
        sharedsTree.setFromCalendar(!fromEventPopup);
        sharedsTree.setTreePanelWidth(fromEventPopup ? 290 : 310);//173
        sharedsTree.setHeight(210);//277
        sharedsTree.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        sharedsTree.setSearchBoxWidth("232px");
        sharedsTree.setTableWidth(fromEventPopup ? 300 : 380);
        if (!fromEventPopup) {
            sharedsTree.getTreeSelect().setEmployeeAvailabilityCheckHandler(new ProjectEmployeesAvailabilityCheck() {
                @Override
                public void onOnlyAvailableRadioButtonClick() {
                    getOnlyAvailableCompanyEmployeesWithTeams();
                }

                public void onAllRadioButtonClick() {
                    //it's OK do nothing
                }

                @Override
                public void onOnlyAvailableClickedSetStartAndEndDate() {
                    //it's OK do nothing
                }

            });
            sharedsTree.getTreeSelect().setAllEmployeesHandler(new ProjectEmployeesAvailabilityCheck() {
                @Override
                public void onOnlyAvailableRadioButtonClick() {
                    //it's OK do nothing
                }

                public void onAllRadioButtonClick() {
                    getAllCompanyEmployeesWithTeams();
                }

                @Override
                public void onOnlyAvailableClickedSetStartAndEndDate() {
                    //it's OK do nothing
                }
            });
        } else {
            sharedsTree.hideAvailablityCheckBox();
        }

        LoadingPanel.loading(true);
        calendarService.getCompanyEmployeesWithTeams(new AbstractAsyncCallback<ArrayList<TeamEmployees>>() {
            public void success(ArrayList<TeamEmployees> result) {
                loadTree(teamEmployees = result);
                LoadingPanel.loading(false);
            }
        });
        FlowPanel fp = new FlowPanel();
        fp.getElement().addClassName("single-column");
        fp.add(sharedsTree);
        initWidget(fp);
    }

    private void getAllCompanyEmployeesWithTeams() {
        sharedsTree.clearTreeView();
        if (teamEmployees.size() > 0) {
            teamEmployees.clear();
        }
        calendarService.getCompanyEmployeesWithTeams(new AbstractAsyncCallback<ArrayList<TeamEmployees>>() {
            public void success(ArrayList<TeamEmployees> result) {
                loadTree(teamEmployees = result);
            }
        });
    }

    public void getOnlyAvailableCompanyEmployeesWithTeams() {
        sharedsTree.clearTreeView();
        if (teamEmployees.size() > 0) {
            teamEmployees.clear();
        }
        calendarService.getAvailableCompanyEmployeesWithTeams(startDate, endDate, new AbstractAsyncCallback<ArrayList<TeamEmployees>>() {
            public void success(ArrayList<TeamEmployees> result) {
                loadTree(teamEmployees = result);
            }
        });
    }

    private void loadTree(ArrayList<TeamEmployees> teamEmployees) {
        TreeSelect.setTickAllVisible(teamEmployees.size() != 0);
        for (TeamEmployees teamEmployee : teamEmployees) {
            sharedsTree.addTreeItem(teamEmployee.getTeam(), teamEmployee.getMembers());
        }

        if (fromCalendarPopup) {
            LoadingPanel.loading(true);
            calendarService.getSelectedEmployees(new AbstractAsyncCallback<ArrayList<Integer>>() {
                public void success(ArrayList<Integer> ids) {
                    if (ids.size() > 0) {
                        for (int i = 0; i < sharedsTree.getTree().getItemCount(); i++) {
                            NTreeSelectItem parent = (NTreeSelectItem) sharedsTree.getTree().getItem(i);
                            for (int j = 0; j < parent.getChildCount(); j++) {
                                NTreeSelectItem child = (NTreeSelectItem) parent.getChild(j);
                                for (Integer id : ids) {
                                    if (child.getItem().getId().equals(id)) {
                                        child.setChecked(true);
                                        sharedsTree.onTreeItemSelection(child, null);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    sharedsTree.expandTreeView();
                    LoadingPanel.loading(false);
                }
            });
        } else if (appointmentID == null) {
            boolean isChecked = false;
            for (int i = 0; i < sharedsTree.getTree().getItemCount(); i++) {
                final NTreeSelectItem parent = (NTreeSelectItem) sharedsTree.getTree().getItem(i);
                for (int j = 0; j < parent.getChildCount(); j++) {
                    final NTreeSelectItem child = (NTreeSelectItem) parent.getChild(j);
                    if (child.getItem().getId().equals(Utils.getUserID())) {
                        child.setChecked(true);
                        sharedsTree.onTreeItemSelection(child, null);
                        isChecked = true;
                        break;
                    }
                }
                if (isChecked) {
                    break;
                }
            }
        }

        if (appointmentID != null) {
            LoadingPanel.loading(true);
            calendarService.getEmployeesIDByEvent(appointmentID, new AbstractAsyncCallback<ArrayList<Integer>>() {
                public void success(ArrayList<Integer> ids) {
                    if (ids.size() > 0) {
                        for (int i = 0; i < sharedsTree.getTree().getItemCount(); i++) {
                            NTreeSelectItem parent = (NTreeSelectItem) sharedsTree.getTree().getItem(i);
                            for (int j = 0; j < parent.getChildCount(); j++) {
                                NTreeSelectItem child = (NTreeSelectItem) parent.getChild(j);
                                for (Integer id : ids) {
                                    if (child.getItem().getId().equals(id)) {
                                        child.setChecked(true);
                                        sharedsTree.onTreeItemSelection(child, null);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    sharedsTree.expandTreeView();
                    LoadingPanel.loading(false);
                }
            });
        } else {
            sharedsTree.expandTreeView();
        }
    }

    public void shareAppointment(int appointmentID) {
        this.appointmentID = appointmentID;

        if (sharedsTree.getSelectedItems().length == 0) {
            Info.show(WfmMessages.App.get().pleaseChooseEmployeeToShare(employeeText), Info.Type.WARNING);
            return;
        }

        LoadingPanel.loading(true);
        final ArrayList<Attendee> checkedEmployees = getCheckedEmployees();
        calendarService.shareEventToEmployees(appointmentID, checkedEmployees, false, new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            public void success(ArrayList<SelectItem> result) {
                LoadingPanel.loading(false);

                if (result.size() == 0) //If there are no conflicted employees
                {
                    runSuccessMessage();
                } else {
                    drawConflictView(result);
                }
            }
        });
    }

    public void drawConflictView(ArrayList<SelectItem> conflictedEmployees) {
        StringBuilder message = new StringBuilder();
        message.append("You have ").append(conflictedEmployees.size()).append(" conflicts with these " + employeeText +
                " during sharing your event:");
        int counter = 1;
        for (SelectItem employee : conflictedEmployees) {
            message.append("<br><b style='font-size:11px;'>").append(counter++).append(". ").append(employee.getName()).append("</b>");
        }

        if (counter > 10) {
            counter = 10;
        }
        int height = counter * 12 + 100;

        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, message.toString(), wfmStrings.cancel(), wfmStrings.continueAnyway(),
                                                           new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        forciblySharing(getCheckedEmployees());
                    }
                });
        messageBox.setTitle(wfmStrings.warning());
        messageBox.open();
    }

    public ArrayList<Attendee> getCheckedEmployees() {
        final ArrayList<Attendee> relevantEmployees = new ArrayList<>();

        for (Integer attendeeId : sharedsTree.getSelectedItems()) {
            final Attendee attendee = new Attendee(attendeeId, true);
            relevantEmployees.add(attendee);
        }

        return relevantEmployees;
    }

    /*public void clearEmployees() {
        employees.clear();
    }*/

    /**
     * If the user insists to share despite conflicts,
     * events should be shared anyway.
     *
     * @param checkedEmployees
     */
    private void forciblySharing(ArrayList<Attendee> checkedEmployees) {
        LoadingPanel.loading(true);
        calendarService.shareEventToEmployees(appointmentID, checkedEmployees, true, new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            public void success(ArrayList<SelectItem> conflictedEmployees) {
                LoadingPanel.loading(false);

                runSuccessMessage();
            }
        });
    }

    private void runSuccessMessage() {
        Info.show(Property.get(Constants.EVENT_LIST, wfmStrings.youHaveSuccessfullySharedEvent(), wfmStrings.event().toLowerCase()), Info.Type.INFO);
    }

    @Override
    public void setWidth(String width) {
        sharedsTree.setWidth(width);
    }

    @Override
    public void setHeight(String height) {
        sharedsTree.setHeight(height);
    }

    @Override
    public void setSize(String width, String height) {
        sharedsTree.setSize(width, height);
    }

    public void checkAllItems(boolean checked) {
        sharedsTree.checkAllItems(checked);
    }

    public void clearAndLoad() {
        sharedsTree.clearTreeView();
        if (teamEmployees != null && teamEmployees.size() > 0) {
            loadTree(teamEmployees);
        }

    }

    public SelectPanel getSelectPanel() {
        return sharedsTree;
    }
}
