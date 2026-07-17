package com.workforcetrack.mobile.rpc.base;

import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 09.07.11
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MTreeSelectItem extends MSelectItem{

    private boolean showIn;

    public MTreeSelectItem() {}

    public MTreeSelectItem(TreeSelectItem treeSelectItem) {
        if (treeSelectItem != null) {
            this.setObjectID(treeSelectItem.getId());
            this.setDescription(treeSelectItem.getDescription());
            this.setName(treeSelectItem.getName());
            this.showIn = treeSelectItem.isShowInDropDown();
        }
    }

    public boolean isShowIn() {
        return showIn;
    }

    public void setShowIn(boolean showIn) {
        this.showIn = showIn;
    }
}
