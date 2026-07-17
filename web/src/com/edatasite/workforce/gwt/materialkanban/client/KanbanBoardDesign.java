package com.edatasite.workforce.gwt.materialkanban.client;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Anvar Akramov on 9/14/17.
 */
public abstract class KanbanBoardDesign<T> {

    public abstract void loadDefaultColumns(AbstractAsyncCallback<T> callback);

    public abstract Widget getBoardItem(T kanbanItem, KanbanBoard<T> kanbanBoard, Object... obj);

    public abstract boolean canDnD(T kanbanItem);
}
