package com.edatasite.workforce.gwt.crm.client.ui.view.kanban;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactServiceAsync;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.Date;

/**
 * Created by Anvar Akramov on 4/25/18.
 */
public class LeadActivitiesDropdown extends Composite implements PermissionConstants {

    interface LeadActivitiesDropdownUiBinder extends UiBinder<Widget, LeadActivitiesDropdown> {
    }

    private static final LeadActivitiesDropdownUiBinder ourUiBinder = GWT.create(LeadActivitiesDropdownUiBinder.class);

    ContactListItem kanbanItem;
    ListResult<Appointment> activities;

    protected CRMServiceAsync crmService = CRMService.App.get();
    protected ContactServiceAsync contactService = ContactService.App.get();
    protected final TaskServiceAsync taskService = TaskService.App.get();

    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
//    
    protected static final CrmStrings crmStrings = CrmStrings.App.get();
    protected static final CrmMessages crmMessages = CrmMessages.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    @UiField
    MaterialLink addTaskLink;
    @UiField
    MaterialLabel addTaskLabel;
    @UiField
    MaterialLink addEventLink;
    @UiField
    MaterialLabel addEventLabel;
    @UiField
    MaterialPanel title;
    @UiField
    MaterialPanel checklistContainer;

    public LeadActivitiesDropdown(ContactListItem kanbanItem) {
        super();
        this.kanbanItem = kanbanItem;
        initWidget(ourUiBinder.createAndBindUi(this));

        addTaskLabel.setText(Property.get(Constants.TASK, wfmStrings.addMess(), wfmStrings.task()));
        addEventLabel.setText(Property.get(Constants.EVENT_LIST, wfmStrings.addMess(), wfmStrings.event()));
    }

    public void setActivities(ListResult<Appointment> activities) {
        this.activities = activities;
        if(activities!=null && activities.getList()!=null && activities.getList().size()>0) {
            title.getElement().setInnerText("Open activities");

//            MaterialPanel checklistContainer = new MaterialPanel("wg_canban__dropdown-text");
            MaterialPanel checklist = new MaterialPanel("wg_canban__dropdown-checklist");
            activities.getList().forEach(item -> {
                //item.getRelationName()
                MaterialPanel checklistItem = new MaterialPanel("wg_canban__dropdown-checklist-item");
                if(item.getStartDate()==null || item.getStartDate().before(new Date())/*RelationItem.TYPE_TASK.equals(item.getAction())*/) {
                    checklistItem.addStyleName("wg_canban__dropdown-checklist-item--task");
                } else {
                    checklistItem.addStyleName("wg_canban__dropdown-checklist-item--event");
                }
                checklist.add(checklistItem);
                MaterialPanel checkBoxContainer = new MaterialPanel("wg_canban__dropdown-checklist-aside");
                checklistItem.add(checkBoxContainer);
                KpiCheckBox checkBox = new KpiCheckBox();
                if(item.getAction()==RelationItem.TYPE_EVENT) {
                    checkBox.setEnabled(false);
                } else if(item.getAction()==RelationItem.TYPE_TASK) {
                    checkBox.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent clickEvent) {
                            TaskListItem taskListItem = new TaskListItem();
                            taskListItem.setObjectID(item.getObjectID());
                            taskListItem.setStatusCode("COMPLETED");
                            taskService.saveTaskEditCellValue(taskListItem, TaskListItem.OVERALL_STATUS_NAME, new AbstractAsyncCallback<Boolean>() {
                            });
                        }
                    });
                }
                checkBoxContainer.add(checkBox);

                MaterialPanel itemDetailsContainer = new MaterialPanel("wg_canban__dropdown-checklist-content");
                MaterialLabel detailsTitle = new MaterialLabel(item.getSubject());
                detailsTitle.setStyleName("wg_canban__dropdown-checklist-title");
                itemDetailsContainer.add(detailsTitle);

                MaterialPanel createdData = new MaterialPanel("wg_canban__dropdown-checklist-created");
                itemDetailsContainer.add(createdData);
                MaterialLabel date = new MaterialLabel();
                date.setStyleName("wg_canban__dropdown-checklist-date");
                MaterialLabel time = new MaterialLabel();
                time.setStyleName("wg_canban__dropdown-checklist-time");
                if(item.getStartDate()!=null) {
                    date.setText(DateUtils.preiewFormat(item.getStartDate()));
                    time.setText(DateUtils.getTimeFormatShort(item.getStartDate()));
                }
                createdData.add(date);
                createdData.add(time);
                checklistItem.add(itemDetailsContainer);
            });
            checklistContainer.add(checklist);

        } else {
            title.getElement().setInnerText("No activities");
        }
    }

    @UiHandler({"addTaskLink", "addTaskLabel"})
    public void addTaskLinkOurClick(ClickEvent event) {
        if (kanbanItem != null) {
            new TaskQuickAddView(RelationItem.newEventRelation(RelationItem.TYPE_LEAD, kanbanItem.getObjectId(), kanbanItem.getName()),
                    RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, kanbanItem.getCrmAccount() != null ? kanbanItem.getCrmAccount().getObjectId() : null,
                            kanbanItem.getCrmAccount() != null ? kanbanItem.getCrmAccount().getName() : null));
        }
    }

    @UiHandler({"addEventLink", "addEventLabel"})
    public void addEventLinkOurClick(ClickEvent event) {
        if (kanbanItem != null) {
            new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(RelationItem.TYPE_LEAD,
                    kanbanItem.getObjectId(), kanbanItem.getName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT,
                    kanbanItem.getCrmAccount().getObjectId(), kanbanItem.getCrmAccount().getName()));
        }
    }
}
