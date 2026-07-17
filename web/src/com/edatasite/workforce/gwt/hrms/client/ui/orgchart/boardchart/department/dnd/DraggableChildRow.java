package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department.dnd;

import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.Optional;

public class DraggableChildRow extends Composite {

    interface RowBinder extends UiBinder<Widget, DraggableChildRow> {
    }

    private static final RowBinder rowBinder = GWT.create(RowBinder.class);

    public interface DragDelegate {
        void onDragStart(DraggableChildRow row, DragStartEvent e);

        void onDragEnd(DraggableChildRow row, DragEndEvent e);

        void onDragOver(DraggableChildRow row, DragOverEvent e);

        void onDrop(DraggableChildRow row, DropEvent e);
    }

    @UiField
    FlowPanel rowRoot;
    @UiField
    FlowPanel dragHandle;
    @UiField
    HTML titleHtml;
    @UiField
    HTML shortDescHtml;


    private final DepartmentNode node;
    private final DragDelegate dragDelegate;

    public DraggableChildRow(DepartmentNode node, DragDelegate dragDelegate, SvgEnum handleIcon) {
        this.node = node;
        this.dragDelegate = dragDelegate;

        initWidget(rowBinder.createAndBindUi(this));

        dragHandle.clear();
        dragHandle.add(new SvgIcon(handleIcon));

        // Название
        titleHtml.setHTML(SafeHtmlUtils.fromString(
                Optional.ofNullable(node.getName()).orElse("")
        ).asString());

        // Возвращаем описание на место
        shortDescHtml.setHTML(SafeHtmlUtils.fromString(
                Optional.ofNullable(node.getShortDescription()).orElse("")
        ).asString());

        if (dragDelegate != null) {
            rowRoot.getElement().setDraggable(Element.DRAGGABLE_TRUE);
            initDragHandlers();
        } else {
            // Силовое отключение для заголовка-короны
            rowRoot.getElement().setDraggable(Element.DRAGGABLE_FALSE);
            rowRoot.getElement().removeAttribute("draggable");
        }
    }

    public DepartmentNode getNode() {
        return node;
    }

    public void setHover(boolean hover) {
        if (hover) {
            rowRoot.addStyleName("is-hover");
        } else {
            rowRoot.removeStyleName("is-hover");
        }
    }

    public void setDragging(boolean dragging) {
        if (dragging) {
            rowRoot.addStyleName("is-dragging");
        } else {
            rowRoot.removeStyleName("is-dragging");
        }
    }

    private void initDragHandlers() {
        rowRoot.addDomHandler((DragStartEvent e) -> {
            if (dragDelegate != null) {
                dragDelegate.onDragStart(this, e);
            }
        }, DragStartEvent.getType());

        rowRoot.addDomHandler((DragEndEvent e) -> {
            if (dragDelegate != null) {
                dragDelegate.onDragEnd(this, e);
            }
        }, DragEndEvent.getType());

        rowRoot.addDomHandler((DragOverEvent e) -> {
            if (dragDelegate != null) {
                dragDelegate.onDragOver(this, e);
            }
        }, DragOverEvent.getType());

        rowRoot.addDomHandler((DropEvent e) -> {
            if (dragDelegate != null) {
                dragDelegate.onDrop(this, e);
            }
        }, DropEvent.getType());
    }
}
