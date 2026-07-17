package com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 14-Jul-2011
 * Time: 22:28:03
 */
public class ListingActionMenu {
    private ArrayList<String> codeNames = new ArrayList<>();
    private HashMap<String, ActionMenuItem<?>> actionItems = new HashMap<>();
    private boolean isAddShowCustomiseButton = true;

    public void addMenuItem(String code, ActionMenuItem<?> menuItem) {
        codeNames.add(code);
        actionItems.put(code, menuItem);
    }

    public ArrayList<String> getCodeNames() {
        return codeNames;
    }

    public HashMap<String, ActionMenuItem<?>> getActionItems() {
        return actionItems;
    }

    public boolean isAddShowCustomiseButton() {
        return isAddShowCustomiseButton;
    }

    public void setAddShowCustomiseButton(boolean showCustomiseButton) {
        isAddShowCustomiseButton = showCustomiseButton;
    }
}
