package com.workforcetrack.mobile.rpc.base;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.workforcetrack.mobile.rpc.client.MSelectItem;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/22/11
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebServiceUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    public static void setFacetItems(List items, boolean isConditionItemID, FacetFilterRpc facetFilterRpc, FacetContentType contentType, int contentCodeIndex) {
        if (items == null || items.size() == 0 || facetFilterRpc == null || contentType == null) {
            return;
        }

        FacetContentRpc contentRpc = facetFilterRpc.getFacetContentMap().get(contentType.getContentCode()[contentCodeIndex]);
        List<SelectItem> facetItems = new ArrayList<>();
        for (Object item : items) {
            if (item == null) {
                continue;
            }
            if (isConditionItemID) {
                facetItems.add(new SelectItem((Integer) item));
            } else {
                String value = item.toString().trim();
                if (!"".equals(value)) {
                    facetItems.add(new SelectItem(null, value));
                }
            }
        }
        contentRpc.setFacetItems(facetItems.toArray(new SelectItem[]{}));
    }


    public static Integer getIndexOf(List<MSelectItem> items, Integer itemID) {
        if (itemID != null && items != null && items.size() > 0) {
            int i = 0;
            for (MSelectItem selectItem : items) {
                if (selectItem != null) {
                    if (selectItem.getObjectID() != null && selectItem.getObjectID().equals(itemID)) {
                        return i;
                    }
                }
                i++;
            }
        }

        return -1;
    }


    public static List<MSelectItem> getAsMSelectItemList(List<SelectItem> selectItems) {
        return WebServiceUtils.getAsMSelectItemList(selectItems.toArray(new SelectItem[]{}));
    }

    public static Integer getNotZeroValue(Integer value) {
        if (value != null && !value.equals(0)) {
            return value;
        }
        return null;
    }

    public static List<MSelectItem> getAsMSelectItemList(SelectItem[] selectItems) {
        if (selectItems == null) {
            return null;
        }

        List<MSelectItem> mSelectItems = new ArrayList<>();
        for (SelectItem selectItem : selectItems) {
            mSelectItems.add(new MSelectItem(selectItem));
        }

        return mSelectItems;
    }

    public static String removeHtmlTags(String str) {
        if (str == null || "".equals(str.trim())) {
            return str;
        }

        //String resultStr1 = str.replaceAll("<.+?>", "");
        return Jsoup.clean(str, Safelist.none());
    }

    public static boolean isEmptyOrNull(Object... objects) {
        if (objects == null) {
            return true;
        }
        for (Object obj : objects) {
            if (obj == null) {
                return true;
            } else if (obj instanceof Integer) {
                return obj.equals(0);
            } else if (obj instanceof String) {
                return obj.equals("");
            }
        }

        return false;
    }

}
