package com.edatasite.workforce.gwt.core.client.interfaces;

import com.google.gwt.user.client.Command;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 15.05.15
 * Time: 10:43
 * To change this template use File | Settings | File Templates.
 */
public interface LinkedLinkableCellInterface {

    String getDisplayValue();

    String getLinkValue();

    void setItemValue(Object value);

    void setItemFocus(boolean focused);

    Command getClickHandler();

    boolean isShowLink();
}
