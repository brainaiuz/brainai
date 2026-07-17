package com.edatasite.workforce.gwt.materialkanban.client;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

/**
 * Created by Anvar Akramov on 9/4/17.
 */
public abstract class KanbanDataLoader<T> {

    public abstract void loadData(ListingFilterParameter filterParameter, KanbanDataRenderer<T> dataRenderer);

    public abstract void onDropKanbanItem(Object sourceColumnLayoutData, Object targetColumnLayoutData, Object widgetLayoutData,
                                          Integer widgetIndex, Object prevItem, Object afterItem, KanbanBoard kanbanBoard, KanbanBoard.OnDropCard onDropCard);

    public Double getAmount(Number[] number) {
        return 0d;
    }

}
