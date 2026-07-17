package com.workforcetrack.api.base;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 31.01.12
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public interface RestServiceConstants {

    int COO_SCHEMA_NUMBER = 5377;
    String COO_APP_NAME = "COO_APP";
    String COO_EVENT = "coo_event";
    String COMMENT_LIST = "commentList";

    String MOBILE_DATE_FORMAT = "";

    //ERROR MSG CODES
    String PARAMS_IS_EMPTY = "Params is empty";
    String SESSION_ID_EMPTY = "SessionID is empty";

    // MOBILE ENTITY NAME
    String EVENT = "event";

    String ENTITY_NAME = "entityName";
    String TABLE_NAME = "tableName";

    int LIMIT_DEFAULT = 20;
    int LIMIT_MAX = 100;
    int START_DEFAULT = 0;

    String START = "start";
    String LIMIT = "limit";
    String TOTAL_COUNT = "totalCount";
    String ITEMS = "items";
    String SESSION_ID = "sessionID";
    String OBJECT_ID = "objectID";
    String UUIDs = "UUID";
    String UUIDsmall = "uuid";
    String RECORD_UUID = "recordUUID";
    String MSG_DATE = "msgDate";
    String AUTO_REFRESH = "autoRefresh";
    String PEER_2_PEER = "peer2peer";
    String FULL_NAME = "fullName";
    String ROLES = "roles";

    String DATE = "date";
    String COUNT = "count";
    String MSG = "msg";
    String TO_USER_UUID = "toUserUUID";
    String USER_UUID = "userUUID";
    String TO_USER_ID = "toUserID";
    String LAST_MSG_DATE = "lastMsgDate";
    String LAST_MSG_DATE_TIME = "lastMsgDateTime";
    String USER_ATTENDED_COUNT = "userAttendedCount";
    String ATTENDED_COUNT = "attendedCount";
    String ATTENDED = "attended";
    String SENDER_UUID = "senderUUID";


    int SESSION_ID_IS_EMPTY = 1;
    int ERROR_WITH_EXECUTING_METHOD = 2;
    int UNKNOWN_ERROR = 3;

    String ACCEPT_JSON_XML = "Accept=application/json, application/xml";
    String ACCEPT_JSON = "Accept=application/json";
    String ACCEPT_XML = "Accept=application/xml";

    String SEARCH_KEY = "searchkey";
    String START_DATE = "startdate";
    String END_DATE = "enddate";
    String FILTER = "filter";
    String TYPE = "type";



}
