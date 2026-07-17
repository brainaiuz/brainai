package com.edatasite.workforce.gwt.core.client.ui.treetable;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 28-Jul-2010
 * Time: 16:16:21
 */
public interface WfmTreeTableChildProvider {

    /**
     * Default value set true
     * @param object
     * @return Yes childs or No
     */
    boolean isHaveChilds(Object object);

    /**
     * @param object this is selected item
     * @return If isSelectGetRequest = false that method body return item childs  else return null;
     */
    Object[] getChilds(Object object);
}
