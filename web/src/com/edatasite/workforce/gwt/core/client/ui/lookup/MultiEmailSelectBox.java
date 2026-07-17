package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/28/16
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiEmailSelectBox extends MultiSelectLookUp{
    @Override
    public boolean onCondition(String text) {
        return Utils.validateEmail(text, true);
    }
}
