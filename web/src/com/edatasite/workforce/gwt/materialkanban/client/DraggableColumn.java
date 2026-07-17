package com.edatasite.workforce.gwt.materialkanban.client;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.AsyncWidget;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anvar Akramov on 9/4/17.
 */
public class DraggableColumn<T> extends AsyncWidget implements KanbanDataRenderer<T>, Constants {

    private final PickupDragController widgetDragController;
    private final KanbanVerticalPanel kanbanVerticalPanel;
    private final KanbanBoard<T> kanbanBoard;
    private final KanbanDataLoader<T> kanbanDataLoader;
    private final KanbanBoardDesign<T> kanbanBoardDesign;
    private final KanbanColumn columnMetadata;

    private MaterialPanel headerTotalAmountPanel;
    private Double totalAmount = 0d;
    private Map<String, KanbanItemColumnConfigs> relatedFieldsMapBySectionName = new HashMap<>();
    private Integer position = 0;
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    DraggableColumn(KanbanColumn columnMetadata, KanbanDataLoader<T> kanbanDataLoader,
                    KanbanBoardDesign<T> kanbanBoardDesign, PickupDragController widgetDragController,
                    KanbanVerticalPanel verticalPanel, KanbanBoard<T> kanbanBoard, MaterialPanel headerTotalAmountPanel, Map<String, KanbanItemColumnConfigs> relatedFieldsMapBySectionName) {

        this(columnMetadata, kanbanDataLoader, kanbanBoardDesign, widgetDragController, verticalPanel, kanbanBoard);
        this.headerTotalAmountPanel = headerTotalAmountPanel;
        this.relatedFieldsMapBySectionName = relatedFieldsMapBySectionName;
    }

    private DraggableColumn(KanbanColumn columnMetadata, KanbanDataLoader<T> kanbanDataLoader,
                            KanbanBoardDesign<T> kanbanBoardDesign, PickupDragController widgetDragController,
                            KanbanVerticalPanel verticalPanel, KanbanBoard<T> kanbanBoard) {

        super(null, DOM.createUniqueId());
        this.columnMetadata = columnMetadata;
        this.kanbanBoardDesign = kanbanBoardDesign;
        this.kanbanDataLoader = kanbanDataLoader;
        this.widgetDragController = widgetDragController;
        this.kanbanVerticalPanel = verticalPanel;
        this.kanbanBoard = kanbanBoard;
        this.kanbanVerticalPanel.setDraggableColumn(this);
    }

    public void loadData() {
        if (kanbanDataLoader != null) {
            kanbanBoard.getFilterParameter().setStart(position);
            kanbanDataLoader.loadData(kanbanBoard.getFilterParameter(), this);
        }
    }

    void reloadData() {
        kanbanVerticalPanel.clear();
        position = 0;
        loadData();
    }

    public void setResults(ListResult<T> result) {
        if (result != null) {
//            getKanbanItemRelatedFieldsMap();

            kanbanVerticalPanel.setTotalCount(result.getTotal());
            if (position == 0) {
                kanbanVerticalPanel.clear();
            }

            if (kanbanBoardDesign != null && !columnMetadata.isMinimized()) {
                for (T item : result.getList()) {
                    Widget widget = kanbanBoardDesign.getBoardItem(item, kanbanBoard, relatedFieldsMapBySectionName);
                    if (widget.getLayoutData() == null) {
                        widget.setLayoutData(item);
                    }

                    kanbanVerticalPanel.add(widget);

                    //Make card draggable
                    if (kanbanBoardDesign.canDnD(item)) {
                        widgetDragController.makeDraggable(widget);
                        widgetDragController.addDragHandler(new DragHandlerAdapter() {

                            @Override
                            public void onDragStart(DragStartEvent event) {
                                super.onDragStart(event);
                                if (kanbanBoard != null) {
                                    kanbanBoard.showWinAndLose(true);
                                }

                            }

                            @Override
                            public void onDragEnd(DragEndEvent event) {
                                if (kanbanBoard != null) {
                                    kanbanBoard.showWinAndLose(false);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
        if (headerTotalAmountPanel != null) {
            headerTotalAmountPanel.clear();

            if (columnMetadata.getDescription() != null) {
                MaterialLabel probability = new MaterialLabel(columnMetadata.getDescription() + "%");
                probability.setStyleName("wg_canban__probability-total");
                headerTotalAmountPanel.add(probability);
            }

            MaterialLabel totalAmountLabel = new MaterialLabel(Utils.setTextInCenter(totalAmount));
            totalAmountLabel.setStyleName("wg_canban__oportunity-total txt-elem--ellipsis");
            headerTotalAmountPanel.add(totalAmountLabel);

        }
    }

    @Override
    public VerticalPanel getItemContainer() {
        return kanbanVerticalPanel;
    }

    @Override
    public SelectItem getColumnMetadata() {
        return columnMetadata;
    }

    @Override
    protected Widget onInitialize() {
        loadData();
        return null;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
}
