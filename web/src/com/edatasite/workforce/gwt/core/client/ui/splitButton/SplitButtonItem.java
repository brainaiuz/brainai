package com.edatasite.workforce.gwt.core.client.ui.splitButton;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 5/22/13
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class SplitButtonItem extends MenuItem {

    private boolean defaultForLabel;
    private String key;

    /**
     * Create SplitButton sub menu item
     *
     * @param key
     * @param text
     * @param cmd
     * @param defaultForLabel
     */
    public SplitButtonItem(String key, String text, Command cmd, boolean... defaultForLabel) {
        super(text, cmd);
        this.defaultForLabel = defaultForLabel.length > 0 && defaultForLabel[0];
        this.key = key;
    }

    public boolean isDefaultForLabel() {
        return defaultForLabel;
    }

    public void setDefaultForLabel(boolean defaultForLabel) {
        this.defaultForLabel = defaultForLabel;
    }

    public String getKey() {
        return key;
    }


}
