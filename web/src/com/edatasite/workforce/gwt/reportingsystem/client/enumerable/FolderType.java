package com.edatasite.workforce.gwt.reportingsystem.client.enumerable;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.ArrayList;

/**
 * User: ${Dilsh0d}
 * Date: 09-Mar-2010
 * Time: 20:50:27
 * <p/>
 * <br/> This Enum uses for Folder type
 */
public enum FolderType {
    System("tab-t2"),
    Public("tab-t1"),
    Private("tab-t3");

    FolderType(String type) {
        this.type = type;
    }

    private final String type;

    public String getType() {
        return type;
    }

    public static SelectItem[] asSelectItem(boolean all) {
        ArrayList<SelectItem> list = new ArrayList<>();
        int i = 0;
        String locale = null;
        for (FolderType item : values()) {
            switch (item.getType()) {
                case "tab-t1":
                    locale = WfmStrings.App.get().pub();
                    break;
                case "tab-t3":
                    locale = WfmStrings.App.get().priv();
                    break;
                case "tab-t2":
                    locale = WfmStrings.App.get().system();
                    break;
            }
            SelectItem selectItem = new SelectItem(i++, locale, item.name());
            if (System.equals(item) && all) {
                list.add(selectItem);
            } else if (!System.equals(item)) {
                list.add(selectItem);
            }
        }
        return list.toArray(new SelectItem[list.size()]);
    }
}
