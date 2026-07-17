package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department;

import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department.dialogs.ParentTreeDropdown;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

import java.util.Optional;
import java.util.Set;

public class DepartmentTreeLookup extends Composite {

    interface Binder extends UiBinder<Widget, DepartmentTreeLookup> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    FlowPanel root;
    @UiField
    FlowPanel trigger;
    @UiField
    TextBox input;
    @UiField
    InlineLabel caret;
    @UiField
    InlineLabel reset;
    @UiField
    FlowPanel dropBox;
    @UiField
    SimplePanel treeHost;

    private final ParentTreeDropdown treeDropdown;
    private DepartmentNode rootNode;
    private Set<Integer> forbiddenIds;
    private SelectionHandler selectionHandler;

    // to avoid search firing when we programmatically set text on selection
    private boolean suppressSearch = false;

    public interface SelectionHandler {
        void onSelected(DepartmentNode node);
    }

    public DepartmentTreeLookup() {
        initWidget(binder.createAndBindUi(this));

        // embed the tree widget inside dropBox
        treeDropdown = new ParentTreeDropdown();
        treeHost.setWidget(treeDropdown);

        // selection from tree → input + bubble up
        treeDropdown.setSelectionHandler(node -> {
            suppressSearch = true;
            input.setText(Optional.ofNullable(node.getName()).orElse(""));
            suppressSearch = false;
            close();
            if (selectionHandler != null) {
                selectionHandler.onSelected(node);
            }
        });

        // open on trigger click
        trigger.addDomHandler(e -> {
            e.stopPropagation();
            toggle();
        }, ClickEvent.getType());

        // open when input gets focus
        input.addFocusHandler(e -> open());

        // reset button
        reset.addDomHandler(e -> {
            e.stopPropagation();
            input.setText("");
            redrawTree("");
        }, ClickEvent.getType());

        // type to search
        input.addKeyUpHandler(e -> {
            if (suppressSearch) return;
            String q = input.getText() != null ? input.getText().trim() : "";
            redrawTree(q);
        });

        // close when clicking outside
        Event.addNativePreviewHandler(event -> {
            if (event.isCanceled()) return;
            if (event.getTypeInt() != Event.ONCLICK) return;

            Event nativeEvent = Event.as(event.getNativeEvent());
            Element target = Element.as(nativeEvent.getEventTarget());
            if (!root.getElement().isOrHasChild(target)) {
                close();
            }
        });
    }

    public void setSelectionHandler(SelectionHandler handler) {
        this.selectionHandler = handler;
    }

    public void setData(DepartmentNode rootNode, Set<Integer> forbiddenIds) {
        this.rootNode = rootNode;
        this.forbiddenIds = forbiddenIds;
        redrawTree(""); // full tree
    }

    // --- open/close helpers -------------------------------------------------

    private void open() {
        root.addStyleName("is-open");
    }

    private void close() {
        root.removeStyleName("is-open");
    }

    private void toggle() {
        if (root.getStyleName().contains("is-open")) {
            close();
        } else {
            open();
        }
    }

    // --- filter + redraw ----------------------------------------------------

    private void redrawTree(String query) {
        if (rootNode == null) return;
//        treeDropdown.buildTree(rootNode, forbiddenIds, query);
    }
}