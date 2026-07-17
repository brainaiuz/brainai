package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsChatRoom;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Feb 11, 2010
 * Time: 9:34:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ChatRoomManager extends Manager<EdsChatRoom> {

    //List<EdsChatRoom> getChatRoomsByCompany(EdsCompany company);

    List<EdsChatRoom> getFilteredChatRoomsByCompany(List<Integer> projectIDs, List<Integer> taskIDs, boolean isLiveChat);

    List<EdsChatRoom> getExpertChatRooms();

    List<EdsChatRoom> getPersonalChatRoomsByCompany();

    List getTotalRoomsCountPerProject();

    List getTotalRoomsCountPerTask();

    List getTotalCountOfAllRooms();

    List getTotalCountOfOthersRooms();

    boolean isHaveCompanyLiveChatRoom();

    EdsChatRoom getLiveChatRoom();

    String getLastDateMessage(String roomName, Integer userID, Integer companyID);

    void deleteLastMessage(List<String> roomName, Integer userID, Integer companyID);

    void updateLastMessageDate(List<Map<String, Object>> lastMessages, Integer userID, Integer companyID);
}
