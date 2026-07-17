package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department.dialogs;

import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department.NodeSelectionCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.html.UnorderedList;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MoveInHierarchyDialog extends KpiModal {

    private final DepartmentNode currentNode;
    private final DepartmentNode rootDep;
    private final SaveHandler saveHandler;
    private final Set<Integer> forbiddenIds = new HashSet<>();

    private DepartmentNode parentDep;
    private DepartmentNode currentTreeRoot;

    private FlowPanel content;
    private TextBox departmentTextBox;
    private FlowPanel container;
    private Span caret;
    private Span box;
    private FlowPanel treeContent;
    private FlowPanel colorContent;
    private Button colorButton;
    private KpiSwitcher kpiSwitcher;

    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public MoveInHierarchyDialog(DepartmentNode rootDep, DepartmentNode currentNode, SaveHandler handler) {
        this.rootDep = rootDep;
        this.currentNode = currentNode;
        this.saveHandler = handler;
        this.getElement().addClassName("modal--hasPop orgBoardPopup");

        setTitle(hrmsStrings.moveFunctionHierarchy());
        if (getModalHeader() != null && getModalHeader().getWidgetCount() > 0) {
            Widget titleWidget = getModalHeader().getWidget(0);
            titleWidget.addStyleName("modal-title");
        }

        content = new FlowPanel();
        content.setStyleName("customSelect customSelect--choosed");

        departmentTextBox = new TextBox();
        departmentTextBox.addStyleName("gwt-SuggestBox form-control");
        departmentTextBox.setPlaceHolder(hrmsStrings.chooseParentFunction());

        container = new FlowPanel();
        container.setStyleName("customControl");
        container.add(departmentTextBox);

        caret = new Span();
        caret.setStyleName("caret");
        container.add(caret);

        box = new Span();
        box.setStyleName("controlClear");
        box.add(new SvgIcon(SvgEnum.x));
        container.add(box);

        treeContent = new FlowPanel();
        treeContent.setStyleName("customSelect-dropBox");

        colorContent = createColorPanel();

        if (currentNode.getParentId() != null) {
            parentDep = findDepartmentById(rootDep, currentNode.getParentId());
            if (parentDep != null) {
                String name = Optional.ofNullable(parentDep.getName()).orElse("—");
                departmentTextBox.setText(name);
                currentTreeRoot = parentDep;
            } else {
                currentTreeRoot = rootDep;
            }
        } else {
            currentTreeRoot = rootDep;
        }

        NodeSelectionCallback selectionCallback = selectedNode -> {
            parentDep = selectedNode;
            currentTreeRoot = rootDep;

            String name = Optional.ofNullable(selectedNode.getName()).orElse("—");
            departmentTextBox.setText(name);

            colorButton.getElement().setAttribute("style", "background-color: " + selectedNode.getColor());
            content.removeStyleName("is-open");
            content.setStyleName("customSelect--choosed", true);
            container.removeStyleName("customControl--search");
        };

        departmentTextBox.addClickHandler(event -> {
            boolean isOpen = content.getElement().getClassName().contains("is-open");
            if (isOpen) {
                content.removeStyleName("is-open");
            } else {
                content.setStyleName("is-open", true);
                buildTree(currentTreeRoot, "", selectionCallback);
            }
        });

        box.addClickHandler(event -> {
            parentDep = null;
            currentTreeRoot = this.rootDep;
            departmentTextBox.setText("");

            if (content.getElement().getClassName().contains("is-open")) {
                buildTree(currentTreeRoot, "", selectionCallback);
            }
            container.removeStyleName("customControl--search");
            content.removeStyleName("customSelect--choosed");
        });

        departmentTextBox.addKeyUpHandler(event -> {
            String query = departmentTextBox.getText() != null
                    ? departmentTextBox.getText().trim()
                    : "";

            if (query.isEmpty()) {
                parentDep = null;
                currentTreeRoot = rootDep;
                content.removeStyleName("customSelect--choosed");
            }

            if (content.getElement().getClassName().contains("is-open")) {
                container.setStyleName("customControl--search", true);
                buildTree(currentTreeRoot, query, selectionCallback);
            }
        });

        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> {
            if (saveHandler != null) {
                saveHandler.onSave(parentDep, currentNode, kpiSwitcher.getValue());
                close();
            }
        });
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), event -> close());

        content.add(container);
        content.add(treeContent);
        addWidget(content, hrmsStrings.chooseParentFunction());
        addWidget(colorContent, null);

        modalFooter.addStyleName("text-right");
        addButton(save);
        addButton(cancel);

        initForbiddenIds();
        buildTree();
    }

    private FlowPanel createColorPanel() {
        FlowPanel colorContent = new FlowPanel();
        colorContent.addStyleName("colorContent");

        FlowPanel formGroup = new FlowPanel();

        FlowPanel label = new FlowPanel();
        Span onLabelWrapp = new Span(hrmsStrings.changeColorInMove());
        kpiSwitcher = new KpiSwitcher("", null, false);
        kpiSwitcher.getOnLabel().setClass("switch__label--right");
        kpiSwitcher.getOnLabel().clear();
        kpiSwitcher.getOnLabel().add(onLabelWrapp);

        colorButton = new Button();
        colorButton.setStyleName("colorPickItem active");
        colorButton.getElement().setAttribute("style", "background-color: " + currentNode.getColor());
        label.add(kpiSwitcher);
        kpiSwitcher.getOnLabel().add(colorButton);
        formGroup.add(label);
        colorContent.add(formGroup);

        return colorContent;
    }

    private void initForbiddenIds() {
        if (currentNode.getParentId() != null) {
            forbiddenIds.add(currentNode.getParentId());
        }
        if (currentNode.getId() != null) {
            forbiddenIds.add(currentNode.getId());
        }
        collectDescendants(currentNode);
    }

    private void collectDescendants(DepartmentNode node) {
        if (node.getChildren() == null) return;
        for (DepartmentNode child : node.getChildren()) {
            if (child.getId() != null) {
                forbiddenIds.add(child.getId());
            }
            collectDescendants(child);
        }
    }

    private void buildTree() {
        buildTree(rootDep, "", node -> {
        });
    }

    private void buildTree(DepartmentNode root, String filter, NodeSelectionCallback callback) {

        treeContent.clear();

        UnorderedList ul = new UnorderedList();
        ul.addStyleName("treeFold");

        treeContent.add(ul);

        String normalizedFilter = filter == null ? "" : filter.trim().toLowerCase();
        buildNode(root, ul, 0, forbiddenIds, normalizedFilter, callback);
    }

    private boolean buildNode(DepartmentNode node,
                              UnorderedList parentUl,
                              int depth,
                              Set<Integer> forbiddenIds,
                              String filter,
                              NodeSelectionCallback callback) {

        boolean forbidden = node.getId() != null && forbiddenIds.contains(node.getId());
        boolean hasChildren = node.getChildren() != null && !node.getChildren().isEmpty();

        String name = Optional.ofNullable(node.getName()).orElse("—");

        String searchable = name.toLowerCase();
        boolean selfMatches = filter.isEmpty() || searchable.contains(filter);

        UnorderedList childUl;
        boolean anyChildVisible = false;

        if (hasChildren) {
            childUl = new UnorderedList();
            childUl.addStyleName("treeFold__childs");

            for (DepartmentNode child : node.getChildren()) {
                boolean childVisible = buildNode(child, childUl, depth + 1, forbiddenIds, filter, callback);
                anyChildVisible = anyChildVisible || childVisible;
            }
        } else {
            childUl = null;
        }

        boolean shouldShow = selfMatches || anyChildVisible;
        if (!shouldShow) {
            return false;
        }

        ListItem li = new ListItem();
        li.addStyleName("treeFold__li treeFold__li--open");
        if (forbidden) {
            li.addStyleName("treeFold__li--disabled");
            li.addStyleName("org-tree-node-disabled");
        }
        parentUl.add(li);

        FlowPanel cur = new FlowPanel();
        cur.addStyleName("treeFold__cur");
        li.add(cur);

        Span toggle = new Span();
        toggle.getElement().setAttribute("data-id", "owner");
        cur.add(toggle);

        Span label = new Span();
        label.addStyleName("treeFold__text");
        label.setText(name);
        cur.add(label);

        label.addDomHandler((ClickEvent e) -> {
            if (forbidden) return;
            callback.onSelected(node);
        }, ClickEvent.getType());

        if (hasChildren && anyChildVisible && childUl != null) {
            toggle.addStyleName("treeFold__toggle expandedElement");
            li.add(childUl);

            toggle.addDomHandler((ClickEvent e) -> {
                e.getNativeEvent().preventDefault();
                e.getNativeEvent().stopPropagation();

                boolean isOpen = li.getStyleName().contains("treeFold__li--open");

                if (isOpen) {
                    li.removeStyleName("treeFold__li--open");
                    toggle.removeStyleName("expandedElement");
                    childUl.getElement().getStyle().setDisplay(Style.Display.NONE);
                } else {
                    li.addStyleName("treeFold__li--open");
                    toggle.addStyleName("expandedElement");
                    childUl.getElement().getStyle().setDisplay(Style.Display.BLOCK);
                }
            }, ClickEvent.getType());
        } else {
            toggle.addStyleName("treeFold__toggle treeFold__toggle--leaf");
        }

        return true;
    }

    private DepartmentNode findDepartmentById(DepartmentNode root, Integer id) {
        if (root == null || id == null) return null;
        if (id.equals(root.getId())) {
            return root;
        }
        if (root.getChildren() != null) {
            for (DepartmentNode child : root.getChildren()) {
                DepartmentNode found = findDepartmentById(child, id);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    public interface SaveHandler {
        void onSave(DepartmentNode newParent, DepartmentNode currentDep, boolean inheritColor);
    }

}
