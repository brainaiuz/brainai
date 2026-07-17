package com.edatasite.workforce.gwt.materialkanban.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

/**
 * Created by Anvar Akramov on 9/4/17.
 */
public class KanbanVerticalPanel extends VerticalPanel {
    private static final String CSS_KANBAN_DND_PANEL_SPACER = "kanban-dnd-panel-spacer";
    MaterialLabel totalCountLabel;
    private Integer totalCount = 0;
    private DraggableColumn draggableColumn;

    public KanbanVerticalPanel(MaterialLabel totalCountLabel) {
        this.totalCountLabel = totalCountLabel;
        Label spacerLabel = new Label("");
        spacerLabel.setStylePrimaryName(CSS_KANBAN_DND_PANEL_SPACER);
        super.add(spacerLabel);
    }

    @Override
    public void add(Widget w) {
        super.insert(w, getWidgetCount() - 1);
    }

    @Override
    public void insert(Widget w, int beforeIndex) {
        if (beforeIndex == getWidgetCount()) {
            beforeIndex--;
        }
        super.insert(w, beforeIndex);
    }

    @Override
    public void clear() {
        super.clear();
        for(int i = 0; i<getWidgetCount(); i++) {
            remove(i);
        }
        Label spacerLabel = new Label("");
        spacerLabel.setStylePrimaryName(CSS_KANBAN_DND_PANEL_SPACER);
        super.add(spacerLabel);
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        totalCountLabel.setText(totalCount.toString());
    }

    public void addTotalCount(int n) {
        totalCount = totalCount + n;
        totalCountLabel.setText( totalCount + "");
    }

    public DraggableColumn getDraggableColumn() {
        return draggableColumn;
    }

    public void setDraggableColumn(DraggableColumn draggableColumn) {
        this.draggableColumn = draggableColumn;
    }
}
