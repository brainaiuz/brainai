package com.edatasite.workforce.gwt.profile.client.ui.view.customfields;

import com.google.gwt.user.client.ui.Label;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 19-Nov-2010
 * Time: 21:44:46
 */
public class CustomFieldLabel extends Label {

    private String feildCodeName;

    public CustomFieldLabel(String text, boolean wordwrap) {
        super(text, wordwrap);
    }

    public String getFeildCodeName() {
        return feildCodeName;
    }

    public void setFeildCodeName(String feildCodeName) {
        this.feildCodeName = feildCodeName;
    }
}
