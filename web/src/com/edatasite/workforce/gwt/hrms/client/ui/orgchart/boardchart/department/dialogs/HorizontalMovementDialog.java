package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department.dialogs;

import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department.dnd.DraggableChildRow;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class HorizontalMovementDialog extends KpiModal {

    public interface HorizontalSaveHandler {
        void onSave(List<DepartmentNode> newOrder);
    }

    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public enum HorizontalDialogMode {
        SORT_SUB_FUNCTIONS,
        MOVE_HORIZONTAL
    }

    // Два разных контейнера для требуемой структуры HTML
    private final FlowPanel headerContainer = new FlowPanel();
    private final FlowPanel listContainer = new FlowPanel();

    private final DepartmentNode contextNode;
    private final DepartmentNode targetNode;
    private final HorizontalSaveHandler saveHandler;
    private final List<DepartmentNode> workingList;
    private final HorizontalDialogMode mode;
    private final Integer activeNodeId;

    private int dragIndex = -1;

    public HorizontalMovementDialog(DepartmentNode contextNode,
                                    DepartmentNode targetNode,
                                    HorizontalSaveHandler saveHandler,
                                    HorizontalDialogMode mode,
                                    Integer activeNodeId) {
        super();
        this.contextNode = contextNode;
        this.targetNode = targetNode;
        this.saveHandler = saveHandler;
        this.mode = mode;
        this.activeNodeId = activeNodeId;
        this.workingList = new ArrayList<>(targetNode.getChildren());

        addStyleName("orgSortDialog");
        addStyleName("orgBoardPopup");

        initUi();
    }

    private void initUi() {
        setTitle(mode == HorizontalDialogMode.MOVE_HORIZONTAL
                ? hrmsStrings.reorderFunction()
                : hrmsStrings.sortSubfunctions());

        // ПОЛУЧАЕМ ДОСТУП К SPAN ИЗ БАЗОВОГО КЛАССА И ДОБАВЛЯЕМ КЛАСС
        // modalTitle — это protected/private поле, но у нас есть доступ к header
        if (getModalHeader() != null && getModalHeader().getWidgetCount() > 0) {
            // Первый виджет в modalHeader — это и есть наш Span modalTitle
            Widget titleWidget = getModalHeader().getWidget(0);
            titleWidget.addStyleName("modal-title");
        }

        // Устанавливаем стили для контейнеров
        headerContainer.addStyleName("orgSortDialog-list-head");
        listContainer.addStyleName("orgSortDialog-list");

        getContent().add(headerContainer);
        getContent().add(listContainer);

        clearFooter();

        WfmButton2 saveBtn = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, e -> {
            if (saveHandler != null) {
                saveHandler.onSave(new ArrayList<>(workingList));
            }
            close();
        });

        addButton(saveBtn);
        addButton(new WfmButton2(wfmStrings.cancel(), e -> close()));

        rebuildChildren();
    }

    private void rebuildChildren() {
        headerContainer.clear();
        listContainer.clear();

        // 1. Рендерим заголовок (Context) в отдельный div
        if (contextNode != null) {
            DraggableChildRow headerRow = new DraggableChildRow(contextNode, null, SvgEnum.dragParrent);
            headerRow.addStyleName("orgSortDialog-row--root");
            headerContainer.add(headerRow);
        }

        // Задаем высоту контейнера, чтобы он не "схлопывался" при перетаскивании
        // Используем расчет на основе CSS-переменных (calc)
        if (workingList != null && !workingList.isEmpty()) {
            int n = workingList.size();
            // Добавляем + 2px для компенсации границ (border) и теней
            String calcHeight = "calc((" + n + " * var(--row-height)) + (" + (n - 1) + " * var(--row-gap)) + 2px)";
            listContainer.getElement().getStyle().setProperty("minHeight", calcHeight);
        }

        // 2. Рендерим список перемещаемых элементов
        for (DepartmentNode child : workingList) {
            boolean isActive = activeNodeId != null && activeNodeId.equals(child.getId());
            DraggableChildRow row = new DraggableChildRow(child, dragDelegate, SvgEnum.dragLayers);
            if (isActive) {
                row.addStyleName("is-active"); // Подсветка карточки, из которой пришли
            }
            listContainer.add(row);
        }
    }

    // Обработчик DND
    private final DraggableChildRow.DragDelegate dragDelegate = new DraggableChildRow.DragDelegate() {
        @Override
        public void onDragStart(DraggableChildRow row, com.google.gwt.event.dom.client.DragStartEvent e) {
            // Индекс берем напрямую из данных — это исключает смещение из-за DOM-структуры
            dragIndex = workingList.indexOf(row.getNode());
            e.getDataTransfer().setData("text/plain", "move");
            row.setDragging(true);
        }

        @Override
        public void onDragEnd(DraggableChildRow row, com.google.gwt.event.dom.client.DragEndEvent e) {
            dragIndex = -1;
            clearHoverStyles();
        }

        @Override
        public void onDragOver(DraggableChildRow row, com.google.gwt.event.dom.client.DragOverEvent e) {
            e.preventDefault();

            // Проверяем актуальный индекс по данным
            if (!row.getStyleName().contains("is-hover")) {
                clearHoverStyles();
                row.setHover(true);
            }
        }

        @Override
        public void onDrop(DraggableChildRow row, com.google.gwt.event.dom.client.DropEvent e) {
            e.preventDefault();
            int targetIndex = workingList.indexOf(row.getNode());

            if (dragIndex != -1 && targetIndex != -1 && dragIndex != targetIndex) {
                // Обновляем список данных
                DepartmentNode movedNode = workingList.remove(dragIndex);
                workingList.add(targetIndex, movedNode);

                // Вместо rebuildChildren() перемещаем виджет внутри контейнера
                // Это сохраняет DOM-узлы и их обработчики в рабочем состоянии
                Widget draggedWidget = listContainer.getWidget(dragIndex);
                listContainer.remove(draggedWidget);
                listContainer.insert(draggedWidget, targetIndex);
            }
            dragIndex = -1;
            clearHoverStyles();
        }
    };

    private void clearHoverStyles() {
        for (int i = 0; i < listContainer.getWidgetCount(); i++) {
            Widget w = listContainer.getWidget(i);
            if (w instanceof DraggableChildRow) {
                ((DraggableChildRow) w).setHover(false);
                ((DraggableChildRow) w).setDragging(false);
            }
        }
    }
}