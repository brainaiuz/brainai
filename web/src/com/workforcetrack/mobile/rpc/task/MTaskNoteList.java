package com.workforcetrack.mobile.rpc.task;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.workforcetrack.api.base.APIRepresentation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 24/08/12
 * Time: 20:50
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "taskNoteList")
public class MTaskNoteList implements APIRepresentation {
    public static final String TOTAL_COUNT = "totalCount";
    public static final String ITEMS = "items";

    private Integer totalCount;
    private List<MTaskNote> items;

    public MTaskNoteList() {

    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public List<MTaskNote> getItems() {
        return items;
    }

    public void setItems(List<MTaskNote> items) {
        this.items = items;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public Map<String, Object> getAsMap(String... ignoreFields) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put(TOTAL_COUNT, getTotalCount());
        resultMap.put(ITEMS, getItems());
        return resultMap;
    }

    @Override
    public List<String> getFieldsName() {
        String[] fields = new String[]{TOTAL_COUNT, ITEMS};
        return Arrays.asList(fields);
    }

    public void setItems(HistoryListItem[] noteList){
         if(noteList != null){
             items = new ArrayList<>();
             for (HistoryListItem item : noteList){
                items.add(new MTaskNote(item));
             }

             this.setTotalCount(items.size());
         }
    }
}
