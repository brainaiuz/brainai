package com.workforcetrack.api.presenter;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.workforcetrack.api.base.RestServiceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 04/09/12
 * Time: 14:41
 * To change this template use File | Settings | File Templates.
 */
public class NoteApiPresenter extends BaseApiPresenter {
    private Integer objectID;
    private String comment;
    private String subject;
    private NewsComment[] notesComments;
    private Boolean checked;
    private String employeePicture;
    private Date eventDate;
    private String eventDescription;

    public Map<String, Object> convertToMap(HistoryListItem[] historyList) {
        Map<String, Object> map = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> result = new ArrayList<>();

        for (HistoryListItem history : historyList) {
            result.add(convertToMap(history));
        }

        map.put(TOTAL_COUNT, historyList != null ? historyList.length : 0);
        map.put(ITEMS, result);

        return map;
    }

    public Map<String, Object> convertToMap(HistoryListItem history) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OBJECT_ID, history.getObjectID());
        map.put(COMMENT, history.getComment());
        map.put(SUBJECT, history.getSubject());
        map.put(EMPLOYEE, history.getEmployee());
        map.put(VISIBILITY, history.isVisibility());
        map.put(EDITABLE, history.isEditable());
        map.put(RELATED_ID, history.getRelatedId());
        map.put(RELATED_NAME, history.getRelatedName());
        map.put(RELATED_TO_ID, history.getRelatedToId());
        map.put(RELATED_TO_NAME, history.getRelatedToName());
        map.put(RELATED_TO_LINK, history.getRelatedToLink());
        map.put(ENTITY_ID, history.getEntityID());
        map.put(SECTION_LINK, history.getSectionLink());
        map.put(NOTES_COMMENTS, null);
        if (history.getNotesComments() != null && history.getNotesComments().length > 0) {
            Map<String, Object> comments = new LinkedHashMap<>();
            for (NewsComment comment : history.getNotesComments()) {
                comments.put(COMMENT_ID, comment.getCommentId());
                comments.put(USER_NAME, comment.getUsername());
                comments.put(DATE, comment.getDate());
                comments.put(COMMENT, comment.getComment());
                comments.put(EMPLOYEE_IMAGE_URL, comment.getEmployeeImageUrl());
            }
            map.put(NOTES_COMMENTS, comments);
        }
        map.put(CHECKED, history.getChecked());
        map.put(EMPLOYEE_PICTURE, history.getEmployeePicture());
        map.put(EVENT_DATE, history.getEventDate());
        map.put(EVENT_DESCRIPTION, history.getEventDescription());

        return map;
    }

    public HistoryListItem convertToItem(Map<String, Object> map) throws ParseException, ClassCastException  {
        HistoryListItem item = new HistoryListItem();
        item.setObjectID((Integer) map.get(OBJECT_ID));
        item.setEmployee((String) map.get(EMPLOYEE));

        SimpleDateFormat dateFormat = new SimpleDateFormat(RestServiceUtils.JSON_DATE_FORMAT);
        item.setEventDate((map.get(EVENT_DATE) != null ? dateFormat.parse((String) map.get(EVENT_DATE)) : null));

        item.setEventDescription((String) map.get(EVENT_DESCRIPTION));
        item.setComment((String) map.get(COMMENT));
        item.setVisibility(map.get(VISIBILITY) == null ? false : (Boolean) map.get(VISIBILITY));
        item.setEditable(map.get(EDITABLE) == null ? false : (Boolean) map.get(EDITABLE));
        item.setRelatedId((Integer) map.get(RELATED_ID));
        item.setRelatedName((String) map.get(RELATED_NAME));
        item.setRelatedToId((Integer) map.get(RELATED_TO_ID));
        item.setRelatedToName((String) map.get(RELATED_TO_NAME));
        item.setRelatedToLink((String) map.get(RELATED_TO_LINK));
        item.setSubject((String) map.get(SUBJECT));
        item.setNotesComments((NewsComment[]) map.get(NOTES_COMMENTS));
        item.setEmployeePicture((String) map.get(EMPLOYEE_PICTURE));
        item.setEntityID((Integer) map.get(ENTITY_ID));
        item.setChecked((Boolean) map.get(CHECKED));

        return item;
    }
}
