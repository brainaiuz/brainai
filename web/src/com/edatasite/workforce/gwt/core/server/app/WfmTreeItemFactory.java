package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import org.springframework.context.support.WfmMessageSource;

public abstract class WfmTreeItemFactory<E> {

    /**
     * @param o domain object WfmTreeItem to be created based on
     * @return - wfm tree item
     */

    abstract public WfmTreeItem createItem(E o);

    public WfmTreeItem createItem(E o, WfmMessageSource wfmMessageSource) {
        return null;
    }
}