package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsMeetingAgendaDiscussion;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/1/12
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MeetingAgendaDiscussionManager extends Manager<EdsMeetingAgendaDiscussion>{
	List<EdsMeetingAgendaDiscussion> getAgendDiscussionsByTopicID(Integer topicID);

	void deleteMeetingAgendaTopicDiscussions(Integer topicObjectID);

	void deleteMeetingAgendaTopicDiscussionsByMeetingID(Integer meetingID);
}
