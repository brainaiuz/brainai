package com.edatasite.workforce.gwt.core.client.interfaces;

import com.google.gwt.user.client.Command;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 11.06.14
 * Time: 17:18
 * To change this template use File | Settings | File Templates.
 */
public interface LinkableCellInterface {

    String getDisplayValue();

    void setItemValue(Object value);

    void setItemFocus(boolean focused);

    Command getClickHandler();
}
