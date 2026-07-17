package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsChatRoom;
import com.edatasite.workforce.core.domain.EdsChatRoomAssignee;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Feb 16, 2010
 * Time: 7:09:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ChatRoomAssigneeManager extends Manager<EdsChatRoomAssignee> {

    List<EdsChatRoomAssignee> getChatAssigneesByRoom(EdsChatRoom room);
}
