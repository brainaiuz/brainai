package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsMeetingAgendaTopic;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: developer
 * Date: 4/20/12
 * Time: 7:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MeetingAgendaTopicManager extends Manager<EdsMeetingAgendaTopic>{
	List<EdsMeetingAgendaTopic> getAgendTopicByMeetingId(Integer projectId);

	void deleteMeetingAgendaTopic(Integer meetingID);
}
