package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import org.springframework.context.support.WfmMessageSource;

import java.util.*;

/**
 * User: iskan
 * Date: Jan 7, 2008
 * Time: 9:00:57 PM
 */

public class ListUtils {

    public static <T> List<T> getSublist(List<T> source, int startIndex, int maxCount) {
        if (source.size() == 0) {
            return source;
        }
        int endIndex = startIndex + maxCount;//- 1;
        if (endIndex > source.size() || endIndex == 0) {
            endIndex = source.size();
        }
        if (startIndex > source.size()) {
            return source;
        }
        return source.subList(startIndex, endIndex);
    }

    /**
     * This method is uses for list pagination
     *
     * @param list  data list
     * @param start start param of filter parameter
     * @param limit limit param filter parameter
     * @param <T>
     * @return sublist of ArrayList<T>
     */
    public static <T> ArrayList<T> getSublistSmart(ArrayList<T> list, int start, int limit) {
        if (list.size() == 0) {
            return list;
        }
        Integer fromIndex = start;
        Integer toIndex = start + limit;
        int listSize = list.size();
        if (fromIndex >= listSize || toIndex <= 0 || fromIndex >= toIndex) {
            return new ArrayList<T>();
        }

        fromIndex = Math.max(0, fromIndex);
        toIndex = Math.min(listSize, toIndex);

        return new ArrayList<T>(list.subList(fromIndex, toIndex));
    }

    public static LinkedList<WfmTreeItem> createTreeItemArray(List list, WfmTreeItemFactory factory) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List mustn't be empty!");
        }
        Iterator iter = list.iterator();

        WfmTreeItem item = factory.createItem(iter.next());
        LinkedList<WfmTreeItem> result = new LinkedList<>();
        result.add(item);
//        int i = 1;
        while (iter.hasNext()) {
            result.add(factory.createItem(iter.next()));
//            i++;
        }
        return result;
    }

    public static <P extends WfmTreeItem> List<P> createTreeItemList(List list, WfmTreeItemFactory factory) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List mustn't be empty!");
        }
        Iterator iter = list.iterator();
        List<P> items = new ArrayList<>();
        while (iter.hasNext()) {
            items.add((P) factory.createItem(iter.next()));
        }
        return items;
    }

    public static <P extends WfmTreeItem> List<P> createTreeItemList(List list, WfmMessageSource wfmMessageSource, WfmTreeItemFactory factory) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List mustn't be empty!");
        }
        Iterator iter = list.iterator();
        List<P> items = new ArrayList<>();
        while (iter.hasNext()) {
            items.add((P) factory.createItem(iter.next(), wfmMessageSource));
        }
        return items;

    }

    public static List<String> getCFUITypesForKanbanItem() {
        return Arrays.asList(Constants.UI_TYPE_TEXTBOX, Constants.UI_TYPE_DATEPICKER, Constants.UI_TYPE_DROPDOWN, Constants.UI_TYPE_TEXTBOX_EMAIL, Constants.UI_TYPE_LOOKUP);
    }
}