package com.edatasite.workforce.gwt.materialkanban.client;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * Created by Anvar Akramov on 9/4/17.
 */
public interface KanbanDataRenderer<T> {
    void setResults(ListResult<T> result);

    void setTotalAmount(Double totalAmount);

    VerticalPanel getItemContainer();

    SelectItem getColumnMetadata();

    Integer getPosition();

    void setPosition(Integer position);
}
