package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsMeetingAgendaTopic;
import com.edatasite.workforce.gwt.core.server.db.MeetingAgendaTopicManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: developer
 * Date: 4/20/12
 * Time: 7:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("meetingAgendaTopicManager")
public class MeetingAgendaTopicManagerImpl extends AttachmentSupportManager<EdsMeetingAgendaTopic> implements MeetingAgendaTopicManager{

	public MeetingAgendaTopicManagerImpl() {
		super(EdsMeetingAgendaTopic.class);
	}
	@Override
	public List<EdsMeetingAgendaTopic> getAgendTopicByMeetingId(Integer meetingminutesid) {
		return find("select mm from EdsMeetingAgendaTopic mm where mm.meetingMinutes.objectID=? order by mm.objectID asc", meetingminutesid);
	}

	@Override
	public void deleteMeetingAgendaTopic(Integer meetingID) {
		updateNative("delete from " + getCompanyId() + ".agendatopic d where d.meetingminutesid=" + meetingID.toString());
	}
}
