package com.edatasite.workforce.gwt.meetingMinutes.server;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsMeetingAgendaDiscussion;
import com.edatasite.workforce.core.domain.EdsMeetingAgendaTopic;
import com.edatasite.workforce.core.domain.EdsMeetingAttendees;
import com.edatasite.workforce.core.domain.EdsMeetingMinutes;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsMeetingMinutesCustomFields;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.AgendaTopicDiscussionItem;
import com.edatasite.workforce.gwt.core.client.rpc.AgendaTopicItem;
import com.edatasite.workforce.gwt.core.client.rpc.BugReportService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.MeetingAttendeesItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.LayoutManager;
import com.edatasite.workforce.gwt.core.server.db.MeetingAgendaDiscussionManager;
import com.edatasite.workforce.gwt.core.server.db.MeetingAgendaTopicManager;
import com.edatasite.workforce.gwt.core.server.db.MeetingAttendeesManager;
import com.edatasite.workforce.gwt.core.server.db.MeetingManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.MeetingMinutesCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesItem;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesService;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamSingleItem;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Djuraev on 9/16/15.
 */
@Transactional
@Service("meetingMinutesService")
public class MeetingMinutesServiceImpl implements MeetingMinutesService, Constants {

    @Autowired
    private UserManager userManager;
    @Autowired
    private MeetingManager meetingManager;
    @Autowired
    private MeetingAgendaTopicManager meetingAgendaTopicManager;
    @Autowired
    private MeetingAttendeesManager meetingAttendeesManager;
    @Autowired
    @Qualifier("projectService")
    private ProjectServiceLocal projectServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private MeetingAgendaDiscussionManager meetingAgendaDiscussionManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private LayoutManager layoutManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private BugReportService bugReportService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    @Qualifier("taskService")
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    private MeetingMinutesCFManager meetingMinutesCFManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<MeetingMinutesItem> getMeetingMinutes(ListingFilterParameter fp) {
        EdsUser user = userManager.getUser();
        fp.setUserID(user.getObjectID());
        List<EdsMeetingMinutes> meetingMinutes = meetingManager.getMeetingMinutesList(fp);
        int totalCount = meetingMinutes.size();
        if (fp.getLimit() > 0) {
            meetingMinutes = ListUtils.getSublist(meetingMinutes, fp.getStart(), fp.getLimit());
        }
        ArrayList<MeetingMinutesItem> items = new ArrayList<>();
        for (EdsMeetingMinutes meeting : meetingMinutes) {
            MeetingMinutesItem item = new MeetingMinutesItem();
            item.setObjectID(meeting.getObjectID());
            item.setName(meeting.getTitle());
            item.setCalledBy(meeting.getCalledBy() != null ? meeting.getCalledBy().getAsSelectItem() : null);
            item.setLocation(meeting.getLocation());
            if (meeting.getType() != null) {
                item.setType(meeting.getType().getAsSelectItem());
            }
            if (meeting.getPrepairedBy() != null) {
                item.setPreparedBy(meeting.getPrepairedBy().getAsSelectItem());
            }
            item.setPurpose(meeting.getPurpose());
            item.setStartdate(meeting.getStartDate());
            item.setEnddate(meeting.getDueDate());
            item.setMeetingNumber(meeting.getMeetingNumber());
            items.add(item);
        }
        return new ListResult<>(items, totalCount);

    }

    public MeetingMinutesItem getMeetingMinutesData(Integer meetingMinutesId) {
        MeetingMinutesItem meetingMinutes = new MeetingMinutesItem();
        meetingMinutes.setNumberData(generateMeetingMinutesNumber());
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setLookUpBy(Constants.BY_NAME);
        filterParametrs.setPM(true);
        meetingMinutes.setEmployees(Lists.newArrayList(projectServiceLocal.getLookUpItems(filterParametrs, LookUpConstants.PM_EMPLOYEE_ID)));

        List<EdsReference> meetingTypes = referenceManager.listReferences("_MEETING_TYPE");
        if (meetingTypes != null) {
            SelectItem[] typeItems = new SelectItem[meetingTypes.size()];
            int i = 0;
            for (EdsReference reference : meetingTypes) {
                typeItems[i++] = reference.getAsSelectItem();
            }
            meetingMinutes.setTypes(typeItems);
        }
        if (meetingMinutesId != null) {
            EdsMeetingMinutes item = meetingManager.get(meetingMinutesId);
            if (item != null) {
                meetingMinutes.setObjectID(item.getObjectID());
                meetingMinutes.setName(item.getTitle());
                meetingMinutes.setLocation(item.getLocation());
                if (item.getType() != null) {
                    meetingMinutes.setType(item.getType().getAsSelectItem());
                }
                if (item.getProject() != null) {
                    EdsProject project = item.getProject();
                    String name = project.getName() == null ? "" : project.getName().equals("") ? "" : project.getNumber() != null ? project.getNumber() + " - " + project.getName() : project.getName();
                    meetingMinutes.setProjectItem(new ProjectItem(project.getObjectID(), name));
                }
                meetingMinutes.setPurpose(item.getPurpose());
                meetingMinutes.setStartdate(item.getStartDate());
                meetingMinutes.setEnddate(item.getDueDate());
                if (item.getCalledBy() != null) {
                    meetingMinutes.setCalledBy(item.getCalledBy().getAsSelectItem());
                }
                if (item.getPrepairedBy() != null) {
                    meetingMinutes.setPreparedBy(item.getPrepairedBy().getAsSelectItem());
                }
                Date nextMeetingDate;
                nextMeetingDate = item.getNextMeetingDate();
                if (nextMeetingDate != null) {
                    meetingMinutes.setNextMeetingDate(nextMeetingDate);
                }
                if (item.getMeetingNumber() != null) {
                    meetingMinutes.setMeetingNumber(item.getMeetingNumber());
                    meetingMinutes.getNumberData().setNumberString(item.getMeetingNumber());
                    meetingMinutes.getNumberData().setIntNumber(item.getIntNumber());
                }

                List<EdsMeetingAttendees> edsMeetingAttendees = meetingAttendeesManager.getMeetingAttendesMeetingId(meetingMinutesId);
                ArrayList<MeetingAttendeesItem> absentList = new ArrayList<>();
                MeetingAttendeesItem absentItem;
                for (EdsMeetingAttendees attendeesListItem : edsMeetingAttendees) {
                    if (!attendeesListItem.isAttendees()) {
                        absentItem = new MeetingAttendeesItem();
                        absentItem.setObjectID(attendeesListItem.getObjectID());
                        if (attendeesListItem.getAttendeesEmployee() != null) {
                            absentItem.setAbsentEmployee(attendeesListItem.getAttendeesEmployee().getAsSelectItem());
                        }
                        absentItem.setAttendees(attendeesListItem.isAttendees());
                        absentItem.setMeetingMinutesId(attendeesListItem.getMeetingMinutes().getObjectID());
                        absentList.add(absentItem);
                    }
                }
                meetingMinutes.setMeetingAbsentItem(absentList);
                meetingMinutes.setNonCompanyAttendees(item.getNonCompanyAttendees());
                meetingMinutes.setSendNotifToAttendees(item.getNonCompanyAttendees() != null && item.getEmailTemplateID() != null);
                if (item.getEmailTemplateID() != null) {
                    EdsEmailTemplate emailTemplate = emailTemplateManager.get(item.getEmailTemplateID());
                    if (emailTemplate != null) {
                        meetingMinutes.setEmailTemplate(emailTemplate.getAsSelectItem());
                    }
                }

                List<EdsMeetingAgendaTopic> edsMeetingAgendaTopics = meetingAgendaTopicManager.getAgendTopicByMeetingId(meetingMinutesId);
                ArrayList<AgendaTopicItem> agendTopicDiscussionList = new ArrayList<>();
                for (EdsMeetingAgendaTopic agendaListItem : edsMeetingAgendaTopics) {
                    AgendaTopicItem agendaTopicItem = new AgendaTopicItem(agendaListItem.getObjectID(), agendaListItem.getName() != null ? agendaListItem.getName() : "");

                    List<EdsMeetingAgendaDiscussion> discussions = meetingAgendaDiscussionManager.getAgendDiscussionsByTopicID(agendaListItem.getObjectID());
                    if (discussions != null && !discussions.isEmpty()) {
                        ArrayList<AgendaTopicDiscussionItem> discussionItems = new ArrayList<>();
                        for (EdsMeetingAgendaDiscussion discussion : discussions) {
                            AgendaTopicDiscussionItem discussionItem = wrapEdsMeetingAgendaDiscussionToAgendaTopicDiscussionItem(discussion);
                            discussionItems.add(discussionItem);
                        }
                        agendaTopicItem.setDiscussionItems(discussionItems);
                    }
                    //				if (agendaTopicItem.getDiscussionItems() != null && !agendaTopicItem.getDiscussionItems().isEmpty()) {
                    agendTopicDiscussionList.add(agendaTopicItem);
                    //				}
                }
                meetingMinutes.setAgendaTopicItems(agendTopicDiscussionList);
                ListingFilterParameter parameter = new ListingFilterParameter();
                parameter.setRelationID(meetingMinutesId);
                parameter.setGroupById(EdsNoteHistory.MEETING_MINUTES);
                List<EdsNoteHistory> noteList = noteHistoryManager.getNoteList(parameter);
                if (noteList != null && !noteList.isEmpty()) {
                    ArrayList<HistoryListItem> result = new ArrayList<>();
                    for (EdsNoteHistory history : noteList) {
                        HistoryListItem historyItem = new HistoryListItem(history.getComment());
                        historyItem.setObjectID(history.getObjectID());
                        historyItem.setRelatedId(history.getRelatedId());
                        historyItem.setRelatedToId(history.getRelatedTo());
                        historyItem.setEmployee(history.getEmployee().getFullName());
                        historyItem.setEventDate(history.getEventDate());
                        result.add(historyItem);
                    }
                    meetingMinutes.setHistoryListItem(result.toArray(new HistoryListItem[]{}));
                }

                meetingMinutes.setCustomFieldItems((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(item.getEdsMeetingMinutesCustomFields(), commonService.getCompanyCustomFields(ViewName.MeetingMInutesView)));
            }

            /*List<FileResource> attachments = attachmentUtilsManager.getAttachments(F_MEETING_MINUTES, meetingMinutesId, meetingMinutesId);
               if (attachments != null) {
                   ArrayList<FileItem> files = new ArrayList<FileItem>();
                   for (FileResource resource : attachments) {
                       FileItem fileItem = new FileItem();
                       fileItem.setFileName(resource.getName());
                       fileItem.setAttachmentId(resource.getObjectId());
                       fileItem.setGoogleDocumentLink(resource.getDownloadUrl());
                       files.add(fileItem);
                   }
                   meetingMinutes.setAttachments(files.toArray(new FileItem[]{}));
               }*/
        }
        meetingMinutes.setEmailTemplates(getEmailTemplates(MEETING_MINUTES_NOTIFICATION));
        meetingMinutes.setLayoutHTML(layoutManager.getLayoutHTML("MEETINGMINUTES"));
        return meetingMinutes;
    }

    private SelectItem[] getEmailTemplates(String templateCategory){
        return emailTemplateService.getEmailTemplates(templateCategory);
    }

    private AgendaTopicDiscussionItem wrapEdsMeetingAgendaDiscussionToAgendaTopicDiscussionItem(EdsMeetingAgendaDiscussion discussion) {
        AgendaTopicDiscussionItem discussionItem = new AgendaTopicDiscussionItem();
        discussionItem.setObjectID(discussion.getObjectID());
        discussionItem.setDiscussionPoints(discussion.getDiscussionPoints());
        discussionItem.setActionPoints(discussion.getActionPoints());
        if (discussion.getAssignedTo() != null) {
            discussionItem.setAssignedTo(discussion.getAssignedTo().getAsSelectItem());
        }
        discussionItem.setStartDate(discussion.getStartDate());
        discussionItem.setDueDate(discussion.getDueDate());
        return discussionItem;
    }

    /**
     * Register new or existing meeting minutes
     *
     * @param meetingMinutesItem - meeting minutes item
     * @return - meeting minutes id
     */
    @Override
    public Integer saveMeetingMinutes(MeetingMinutesItem meetingMinutesItem) {
        EdsMeetingMinutes meetingMinutes = new EdsMeetingMinutes();
        boolean isNew = true;
        if (meetingMinutesItem.getObjectID() != null) {
            meetingMinutes = meetingManager.get(meetingMinutesItem.getObjectID());
            isNew = false;
        }
        meetingMinutes.setTitle(meetingMinutesItem.getName());
        meetingMinutes.setLocation(meetingMinutesItem.getLocation());
        if (meetingMinutesItem.getType() != null) {
            meetingMinutes.setType(referenceManager.get(meetingMinutesItem.getType().getId()));
        }
        if (meetingMinutesItem.getProjectItem() != null && meetingMinutesItem.getProjectItem().getId() != null){
            meetingMinutes.setProject(projectManager.get(meetingMinutesItem.getProjectItem().getId()));
        }
        if (meetingMinutesItem.getCalledBy() != null) {
            meetingMinutes.setCalledBy(userManager.get(meetingMinutesItem.getCalledBy().getId()));
        }
        meetingMinutes.setPurpose(meetingMinutesItem.getPurpose());
        meetingMinutes.setStartDate(meetingMinutesItem.getStartdate());
        meetingMinutes.setDueDate(meetingMinutesItem.getEnddate());
        meetingMinutes.setMeetingNumber(meetingMinutesItem.getNumberData().getNumberString());
        meetingMinutes.setIntNumber(meetingMinutesItem.getNumberData().getIntNumber());
        meetingMinutes.setNonCompanyAttendees(meetingMinutesItem.getNonCompanyAttendees());
        meetingMinutes.setEmailTemplateID(meetingMinutesItem.getEmailTemplate() != null ? meetingMinutesItem.getEmailTemplate().getId() : null);
        meetingMinutes.setLastUpdateTime(new Date());
        if (meetingMinutesItem.getPreparedBy() != null) {
            meetingMinutes.setPrepairedBy(userManager.get(meetingMinutesItem.getPreparedBy().getId()));
        }
        if (meetingMinutesItem.getNextMeetingDate() != null){
            meetingMinutes.setNextMeetingDate(meetingMinutesItem.getNextMeetingDate().getDate());
        }
        //register meeting minutes
        meetingManager.createOrUpdate(meetingMinutes);
        //delete meeting attendees, and add new meeting attendees
        if (meetingMinutes.getObjectID() != null) {
            meetingAttendeesManager.deleteMeetingAttendees(meetingMinutes.getObjectID());
        }

        if(meetingMinutesItem.isSendNotifToAttendees() && meetingMinutes.getNonCompanyAttendees() != null && !meetingMinutes.getNonCompanyAttendees().isEmpty()){
            for(String email : meetingMinutes.getNonCompanyAttendees().split(",")){
                EdsUser creator = userManager.getUser();
                try {
                    messageManager.sendMeetingMinutesNotification(creator, meetingMinutes, null, email);
                } catch (EdsDbException e) {
                    e.printStackTrace();
                }
            }
        }
        for (MeetingAttendeesItem absentItem : meetingMinutesItem.getMeetingAbsentItem()) {
            if (absentItem != null) {
                Integer employeeID = absentItem.getAbsentEmployee().getId();
                Integer meetingMinutesID = absentItem.getMeetingMinutesId();

                EdsMeetingAttendees newMeetingAbsent = new EdsMeetingAttendees();

                if (meetingMinutesID != null && employeeID != null) {
                    newMeetingAbsent = meetingAttendeesManager.getMeetingAttendeeByMeetingIDAndEmployeeID(meetingMinutesID, employeeID);
                    if (newMeetingAbsent == null) {
                        newMeetingAbsent = new EdsMeetingAttendees();
                    }
                }
                newMeetingAbsent.setAttendees(false);
                EdsUser attendeesEmployee = userManager.get(employeeID);
                newMeetingAbsent.setAttendeesEmployee(attendeesEmployee);
                newMeetingAbsent.setMeetingMinutes(meetingMinutes);
                meetingAttendeesManager.createOrUpdate(newMeetingAbsent);
            }
        }

        List<EdsMeetingAgendaTopic> edsMeetingAgendaTopics = meetingAgendaTopicManager.getAgendTopicByMeetingId(meetingMinutes.getObjectID());
        ArrayList<AgendaTopicItem> oldAgendaTopicItems = new ArrayList<>();
        for (EdsMeetingAgendaTopic agendaListItem : edsMeetingAgendaTopics) {
            AgendaTopicItem agendaTopicItem = new AgendaTopicItem(agendaListItem.getObjectID(), agendaListItem.getName() != null ? agendaListItem.getName() : "");

            List<EdsMeetingAgendaDiscussion> discussions = meetingAgendaDiscussionManager.getAgendDiscussionsByTopicID(agendaListItem.getObjectID());
            if (discussions != null && !discussions.isEmpty()) {
                ArrayList<AgendaTopicDiscussionItem> discussionItems = new ArrayList<>();
                for (EdsMeetingAgendaDiscussion discussion : discussions) {
                    AgendaTopicDiscussionItem discussionItem = wrapEdsMeetingAgendaDiscussionToAgendaTopicDiscussionItem(discussion);
                    discussionItems.add(discussionItem);
                }
                agendaTopicItem.setDiscussionItems(discussionItems);
            }
            oldAgendaTopicItems.add(agendaTopicItem);
        }

        ArrayList<AgendaTopicItem> newTopicItems = new ArrayList<AgendaTopicItem>(meetingMinutesItem.getAgendaTopicItems());
        ArrayList<AgendaTopicItem> nonChangedTopics = (ArrayList<AgendaTopicItem>) ServerUtils.intersect(newTopicItems, oldAgendaTopicItems);
        for (AgendaTopicItem oldTopicItem : oldAgendaTopicItems) {
            EdsMeetingAgendaTopic topic = meetingAgendaTopicManager.get(oldTopicItem.getObjectID());
            if (topic != null) {
                meetingAgendaDiscussionManager.deleteMeetingAgendaTopicDiscussions(topic.getObjectID());
                meetingAgendaTopicManager.delete(topic);
            }
        }
        for (AgendaTopicItem topicItem : newTopicItems) {
            EdsMeetingAgendaTopic agendaTopic = new EdsMeetingAgendaTopic();
            agendaTopic.setMeetingMinutes(meetingMinutes);
            agendaTopic.setName(topicItem.getName());
            meetingAgendaTopicManager.create(agendaTopic);

            for (AgendaTopicDiscussionItem discussionItem : topicItem.getDiscussionItems()) {
                EdsMeetingAgendaDiscussion discussion = new EdsMeetingAgendaDiscussion();
                if (discussionItem.getDiscussionPoints() != null) {
                    discussion.setDiscussionPoints(discussionItem.getDiscussionPoints());
                }
                if (discussionItem.getDiscussionPoints() != null) {
                    discussion.setActionPoints(discussionItem.getActionPoints());
                }
                if (discussionItem.getAssignedTo() != null && discussionItem.getAssignedTo().getId() != null) {
                    discussion.setAssignedTo(userManager.get(discussionItem.getAssignedTo().getId()));
                }
                discussion.setStartDate(discussionItem.getStartDate());
                discussion.setDueDate(discussionItem.getDueDate());
                discussion.setAgendaTopic(agendaTopic);
                meetingAgendaDiscussionManager.create(discussion);
            }
        }

        if (meetingMinutes.getObjectID() != null) {
            attachmentUtilsManager.saveAttachments(F_MEETING_MINUTES, meetingMinutes.getObjectID(), meetingMinutes.getObjectID(), meetingMinutesItem.getAttachments());
        }

        if (meetingMinutesItem.getHistoryListItem() != null) {
            HistoryListItem item = new HistoryListItem();
            for (int i = 0; i < meetingMinutesItem.getHistoryListItem().length; i++) {
                item.setRelatedId(meetingMinutes.getObjectID());
                item.setRelatedToId(EdsNoteHistory.MEETING_MINUTES);
                if (meetingMinutesItem.getHistoryListItem()[i].getObjectID() != null && meetingMinutesItem.getHistoryListItem()[i].getObjectID() > 0) {
                    item.setObjectID(meetingMinutesItem.getHistoryListItem()[i].getObjectID());
                }
                item.setVisibility(false);
                item.setSubject("");
                item.setComment(meetingMinutesItem.getHistoryListItem()[i].getComment());
                item.setEmployee(meetingMinutesItem.getHistoryListItem()[i].getEmployee());
                bugReportService.addNote(item);
            }
        }
        EdsMeetingMinutesCustomFields edsMeetingMinutesCustomFields = createMeetingMinutesCustomFields(meetingMinutesItem.getCustomFieldItems());
        meetingMinutes.setEdsMeetingMinutesCustomFields(edsMeetingMinutesCustomFields);
        return meetingMinutes.getObjectID();
    }

    @Transactional
    public EdsMeetingMinutesCustomFields createMeetingMinutesCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsMeetingMinutesCustomFields edsMeetingMinutesCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                edsMeetingMinutesCustomFields = meetingMinutesCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsMeetingMinutesCustomFields = new EdsMeetingMinutesCustomFields();
                meetingMinutesCFManager.create(edsMeetingMinutesCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsMeetingMinutesCustomFields, customFieldItems);
            return edsMeetingMinutesCustomFields;
        }
        return null;
    }

    @Override
    public NumberData generateMeetingMinutesNumber() {
        Integer intNumber = meetingManager.getProductLastIntNumber();
        return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_MEETING_PREFIX);
    }

    public void deleteMeetingMinutes(Integer meetingID) {
        meetingAgendaDiscussionManager.deleteMeetingAgendaTopicDiscussionsByMeetingID(meetingID);
        meetingAgendaTopicManager.deleteMeetingAgendaTopic(meetingID);
        meetingAttendeesManager.deleteMeetingAttendees(meetingID);
        meetingManager.delete(meetingManager.get(meetingID));
    }

    @Override
    public Integer convertMeetingMinutesToProject(Integer meetingID) {
        EdsMeetingMinutes meetingMinutes = meetingManager.get(meetingID);
        if (meetingMinutes != null) {
            ProjectSingleItem project = new ProjectSingleItem();
            project.setName(meetingMinutes.getTitle());
            project.setDescription(meetingMinutes.getPurpose());
            project.setNumberData(projectServiceLocal.generateProjectNumber(meetingMinutes.getStartDate(), null, null));

            HashMap<Integer, EdsUser> members = new HashMap<>();
            if (meetingMinutes.getCalledBy() != null) {
                project.setManagerId(meetingMinutes.getCalledBy().getObjectID());
                members.put(meetingMinutes.getCalledBy().getObjectID(), meetingMinutes.getCalledBy());
            }
            if (meetingMinutes.getPrepairedBy() != null) {
                ArrayList<Integer> backupManagerId = new ArrayList<>();
                backupManagerId.add(meetingMinutes.getPrepairedBy().getObjectID());
                project.setBackupManagerIDs(backupManagerId);
                if (!members.containsKey(meetingMinutes.getPrepairedBy().getObjectID())) {
                    members.put(meetingMinutes.getPrepairedBy().getObjectID(), meetingMinutes.getPrepairedBy());
                }
            }
            project.setStartDate(meetingMinutes.getStartDate());
            project.setEndDate(meetingMinutes.getDueDate());
            EdsReference notStarted = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED);
            project.setStatusId(notStarted.getObjectID());
            //Members
            List<EdsMeetingAgendaTopic> agendaTopic = meetingAgendaTopicManager.getAgendTopicByMeetingId(meetingMinutes.getObjectID());
            for (EdsMeetingAgendaTopic topic : agendaTopic) {
                List<EdsMeetingAgendaDiscussion> agendaDiscussion = meetingAgendaDiscussionManager.getAgendDiscussionsByTopicID(topic.getObjectID());
                for (EdsMeetingAgendaDiscussion discussion : agendaDiscussion) {
                    if (discussion != null && discussion.getAssignedTo() != null) {
                        if (members.keySet().size() > 0 && !members.containsKey(discussion.getAssignedTo().getObjectID())) {
                            members.put(discussion.getAssignedTo().getObjectID(), discussion.getAssignedTo());
                        } else if (members.keySet().size() == 0) {
                            members.put(discussion.getAssignedTo().getObjectID(), discussion.getAssignedTo());
                        }
                    }

                }
            }
            if (members.keySet().size() > 0) {
                ProjectMember[] projectMembers = new ProjectMember[members.keySet().size()];
                int i = 0;
                ProjectMember newMember;
                for (Integer userId : members.keySet()) {
                    newMember = new ProjectMember();
                    newMember.setId(members.get(userId).getObjectID());
                    projectMembers[i++] = newMember;
                }
                project.setProjectMembers(projectMembers);
            }
            //project source
            project.setProjectSource(PROJECT_SOURCE_CONVERT_FROM_MEETING_MINUTES + meetingMinutes.getObjectID());
            //Save project
            Integer projectID = null;
            Integer taskID = null;
            try {
                projectID = projectServiceLocal.saveProject(project);
            } catch (NumberExistingException e) {
                e.printStackTrace();
                return null;
            }
            //create task  && Workstream
            if (projectID != null && agendaTopic != null && agendaTopic.size() > 0) {
                taskID = createNewWorkstreamAndTask(meetingMinutes, agendaTopic, projectID);
            }
            meetingMinutes.setConvertedToProject(true);
            meetingManager.update(meetingMinutes);
            //save relation
            ArrayList<RelationItem> relations = EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_MEETING_MINUTES, meetingMinutes.getObjectID()));
            relations.add(new RelationItem(null, projectID, RelationItem.TYPE_PROJECT, project.getName(), meetingMinutes.getObjectID(), RelationItem.TYPE_MEETING_MINUTES, meetingMinutes.getTitle()));
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_MEETING_MINUTES, meetingMinutes.getObjectID(), meetingMinutes.getName(), relations);

            return projectID != null ? (agendaTopic != null && agendaTopic.size() > 0 ? (taskID != null ? projectID : null) : projectID) : null;
        }
        return null;
    }

    @Override
    public Boolean getMeetingConvertedStatus(Integer meetingID) {
        EdsMeetingMinutes meetingMinutes = meetingManager.get(meetingID);
        if (meetingMinutes != null) {
            return meetingMinutes.getConvertedToProject() != null ? meetingMinutes.getConvertedToProject() : false;
        }
        return false;
    }

    private Integer createNewWorkstreamAndTask(EdsMeetingMinutes meetingMinutes, List<EdsMeetingAgendaTopic> agendaTopic, Integer projectID) {
        ArrayList<Integer> workIds = new ArrayList<>();
        ArrayList<Integer> taskIds = new ArrayList<>();
        for (EdsMeetingAgendaTopic topic : agendaTopic) {
            //Create Workstream
            Integer streamId = createWorkstream(meetingMinutes, topic, projectID);
            workIds.add(streamId);
            List<EdsMeetingAgendaDiscussion> agendaDiscussion = meetingAgendaDiscussionManager.getAgendDiscussionsByTopicID(topic.getObjectID());
            for (EdsMeetingAgendaDiscussion discussion : agendaDiscussion) {
                if (discussion.getDiscussionPoints() != null && !discussion.getDiscussionPoints().isEmpty()) {
                    //Create Task
                    Integer taskId = createTask(discussion, streamId, projectID);
                    taskIds.add(taskId);
                }
            }
        }
        return validationIds(workIds, taskIds);
    }

    private Integer validationIds(ArrayList<Integer> workIds, ArrayList<Integer> taskIds) {
        if (workIds.size() > 0 && workIds.contains(null) || taskIds.size() > 0 && taskIds.contains(null)) {
            return null;
        }
        return 0;
    }

    private Integer createTask(EdsMeetingAgendaDiscussion discussion, Integer streamId, Integer projectID) {
        TaskSingleItem taskItem = new TaskSingleItem();
        taskItem.setProjectID(projectID);
        taskItem.setNumberData(taskServiceLocal.generateTaskNumber(projectID, discussion.getStartDate(), null));
        taskItem.setName(discussion.getDiscussionPoints());
        taskItem.setDescription(discussion.getActionPoints());
        taskItem.setStartDate(discussion.getStartDate());
        taskItem.setEndDate(discussion.getDueDate());
        taskItem.setDueDate(discussion.getDueDate());
        taskItem.setAllDay(true);
        taskItem.setBillable(true);
        EdsReference edsReference = referenceManager.getReferenceByParentCode(EdsTask.TASK_STATUS);
        taskItem.setStatusID(edsReference != null ? edsReference.getObjectID() : null);


        SelectItem[] priorities = taskServiceLocal.getPriorities();
        for (SelectItem priority : priorities) {
            if (priority.getName().trim().equals("Medium")) {
                taskItem.setPriorityID(priority.getId());
            }
        }
        Integer projectEmployeeID = null;
        List<EdsProjectEmployee> projectEmployees = projectManager.getEmployeesByProject(projectID);
        for (EdsProjectEmployee employee : projectEmployees) {
            if (discussion.getAssignedTo() != null && employee.getEmployeeDepartment().getEmployee().getObjectID().equals(discussion.getAssignedTo().getObjectID())) {
                projectEmployeeID = employee.getObjectID();
            }
        }
        if (projectEmployeeID != null) {
            IdTime[] projectEmployee = {new IdTime(projectEmployeeID, 0)};
            taskItem.setProjectEmployees(projectEmployee);
        }

        taskItem.setWorkstreamID(streamId == 0 || streamId == null ? null : streamId);
        try {
            taskServiceLocal.saveTask(taskItem);
            return 0;
        } catch (NumberExistingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Integer createWorkstream(EdsMeetingMinutes meetingMinutes, EdsMeetingAgendaTopic topic, Integer projectID) {
        WorkstreamSingleItem newWorkstream = new WorkstreamSingleItem();
        NumberData numberData = taskServiceLocal.generateWorkstreamNumber(projectID, meetingMinutes.getStartDate(), null);
        if (numberData != null) {
            newWorkstream.setNumberData(numberData);
        }
        newWorkstream.setName(topic.getName());
        newWorkstream.setStartDate(meetingMinutes.getStartDate());
        newWorkstream.setEndDate(meetingMinutes.getDueDate());
        newWorkstream.setProjectID(projectID);
        Integer id = 0;
        try {
            id = taskServiceLocal.createWorkstream(newWorkstream, null);
        } catch (NumberExistingException e) {
            e.printStackTrace();
            id = null;
        }
        return id;
    }
}
