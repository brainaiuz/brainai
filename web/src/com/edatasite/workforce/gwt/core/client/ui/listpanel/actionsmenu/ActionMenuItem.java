package com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu;

import com.google.gwt.user.client.Command;

import java.util.HashSet;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 14-Jul-2011
 * Time: 22:30:21
 */
public abstract class ActionMenuItem<T> {
    private String name;
    private boolean isMultiSelection;

    public ActionMenuItem(String name, boolean isMultiSelection) {
        this.name = name;
        this.isMultiSelection = isMultiSelection;
    }

    public String getName() {
        return name;
    }

    public boolean isMultiSelection() {
        return isMultiSelection;
    }

    public T getSingleSelection(HashSet<T> rowValues) {
        return rowValues.iterator().next();
    }

    public boolean isDefaultShow() {
        return false;
    }

    public boolean isDefaultShowCriteria() {
        return true;
    }

    /**
     * @return Menu Item Icon Style
     */
    public abstract String getIconStyle();

    /**
     * @return
     */
    public abstract boolean isShowMenuItem(HashSet<T> rowValues);

    /**
     * If isMultiSelection equals true that rowValue.size>=1 else
     * rowValue.size=1
     *
     * @param rowValues
     * @return
     */
    public abstract Command onClickMenuItem(HashSet<T> rowValues);
}

