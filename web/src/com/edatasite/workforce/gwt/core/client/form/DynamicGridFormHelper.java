package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.form.panel.DynamicGridItemPanel;

/**
 * User: Abror Abdukadirov
 * Date: 27.08.2019 20:02
 */
public interface DynamicGridFormHelper {

    DynamicGridItemPanel getItemById(String elementId);

    void removeDroppedItem(String elementId, Boolean fromInactive);

    void itemAdded(DynamicGridItemPanel itemPanel);
}
