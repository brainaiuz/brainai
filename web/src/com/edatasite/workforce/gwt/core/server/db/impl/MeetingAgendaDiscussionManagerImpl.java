package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsMeetingAgendaDiscussion;
import com.edatasite.workforce.gwt.core.server.db.MeetingAgendaDiscussionManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/1/12
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("meetingAgendaDiscussionManager")
public class MeetingAgendaDiscussionManagerImpl extends AttachmentSupportManager<EdsMeetingAgendaDiscussion> implements MeetingAgendaDiscussionManager {
	public MeetingAgendaDiscussionManagerImpl(){
		super(EdsMeetingAgendaDiscussion.class);
	}

	@Override
	public List<EdsMeetingAgendaDiscussion> getAgendDiscussionsByTopicID(Integer topicID) {
		return find("select mm from EdsMeetingAgendaDiscussion mm where mm.agendaTopic.objectID=? order by mm.objectID asc", topicID);
	}

	@Override
	public void deleteMeetingAgendaTopicDiscussions(Integer topicObjectID) {
		update("delete from EdsMeetingAgendaDiscussion d where d.agendaTopic.objectID=?", topicObjectID);
	}

	public void deleteMeetingAgendaTopicDiscussionsByMeetingID(Integer meetingID) {
		updateNative("delete from " +getCompanyId()+ ".agendadiscussion d where d.agendatopicid in " +
				"(select at.id from " + getCompanyId() + ".agendatopic at where at.meetingminutesid=" + meetingID.toString() + ")");
	}
}
