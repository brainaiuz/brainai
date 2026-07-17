package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department.dialogs;

import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.UnorderedList;

import java.util.Optional;
import java.util.Set;

public class ParentTreeDropdown extends Composite {

    interface Binder extends UiBinder<Widget, ParentTreeDropdown> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    FlowPanel root;
    @UiField
    FlowPanel treeContainer;

    public interface SelectionHandler {
        void onSelected(DepartmentNode node);
    }

    private SelectionHandler selectionHandler;

    public ParentTreeDropdown() {
        initWidget(binder.createAndBindUi(this));

        root.addStyleName("org-tree-dropdown org-tree-hidden");
    }

    public void setSelectionHandler(SelectionHandler handler) {
        this.selectionHandler = handler;
    }

    public void setPosition(int left, int top) {
        Style style = root.getElement().getStyle();
        style.setPosition(Style.Position.ABSOLUTE);
        style.setLeft(left, Style.Unit.PX);
        style.setTop(top, Style.Unit.PX);
        style.setZIndex(1150);
    }

    public void show() {
        root.removeStyleName("org-tree-hidden");
    }

    public void hide() {
        if (!isHidden()) {
            root.addStyleName("org-tree-hidden");
        }
    }

    public boolean isHidden() {
        return root.getStyleName().contains("org-tree-hidden");
    }

    public void buildTree(DepartmentNode rootNode, Set<Integer> forbiddenIds) {
        treeContainer.clear();

        UnorderedList ul = new UnorderedList();
        ul.addStyleName("treeFold org-tree");          // root list

        treeContainer.add(ul);
        buildNode(rootNode, ul, 0, forbiddenIds);
    }

    private void buildNode(DepartmentNode node,
                           UnorderedList parentUl,
                           int depth,
                           Set<Integer> forbiddenIds) {

        boolean forbidden = node.getId() != null && forbiddenIds.contains(node.getId());
        boolean hasChildren = node.getChildren() != null && !node.getChildren().isEmpty();

        // <li class="treeFold__li org-tree-node ...">
        ListItem li = new ListItem();
        li.addStyleName("treeFold__li");
        li.addStyleName("org-tree-node");
        if (forbidden) {
            li.addStyleName("treeFold__li--disabled");
            li.addStyleName("org-tree-node-disabled");
        }
        parentUl.add(li);

        // <div class="treeFold__cur org-tree-cur">
        FlowPanel cur = new FlowPanel();
        cur.addStyleName("treeFold__cur");
        cur.addStyleName("org-tree-cur");
        li.add(cur);

        // стрелка
        FlowPanel switcher = new FlowPanel();
        switcher.addStyleName("treeFold__toggle");
        switcher.addStyleName("org-tree-switcher");
        cur.add(switcher);

        // подпись
        Label label = new Label(
                Optional.ofNullable(node.getName()).orElse("—")
        );
        label.addStyleName("treeFold__text");
        label.addStyleName("org-tree-label");
        cur.add(label);

        // выбор по тексту
        label.addDomHandler((ClickEvent e) -> {
            if (forbidden) return;
            if (selectionHandler != null) {
                selectionHandler.onSelected(node);
            }

            // подсветка active: снимаем со всех <li> внутри treeContainer
            NodeList<Element> liEls = treeContainer.getElement().getElementsByTagName("li");
            for (int i = 0; i < liEls.getLength(); i++) {
                liEls.getItem(i).removeClassName("active");
            }
            li.addStyleName("active");
        }, ClickEvent.getType());

        // дети
        if (hasChildren) {
            // <ul class="treeFold__childs org-tree-children">
            UnorderedList childUl = new UnorderedList();
            childUl.addStyleName("treeFold__childs");
            childUl.addStyleName("org-tree-children");
            li.add(childUl);

            // по умолчанию раскрыт
            li.addStyleName("treeFold__li--open");
            switcher.addStyleName("org-tree-switcher-open");
            switcher.addStyleName("expandedElement");
            // display оставляем по умолчанию (block), CSS допокажет

            for (DepartmentNode child : node.getChildren()) {
                buildNode(child, childUl, depth + 1, forbiddenIds);
            }

            // клик по стрелке — только раскрытие/сворачивание
            switcher.addDomHandler((ClickEvent e) -> {
                // не даём клику улететь в инпут/модалку
                e.getNativeEvent().preventDefault();
                e.getNativeEvent().stopPropagation();

                boolean isOpen = li.getStyleName().contains("treeFold__li--open");

                if (isOpen) {
                    li.removeStyleName("treeFold__li--open");
                    switcher.removeStyleName("org-tree-switcher-open");
                    switcher.removeStyleName("expandedElement");
                    switcher.addStyleName("org-tree-switcher-closed");

                    // на всякий случай принудительно скрываем детей
                    childUl.getElement().getStyle().setDisplay(Style.Display.NONE);
                } else {
                    li.addStyleName("treeFold__li--open");
                    switcher.removeStyleName("org-tree-switcher-closed");
                    switcher.addStyleName("org-tree-switcher-open");
                    switcher.addStyleName("expandedElement");

                    // и показываем обратно
                    childUl.getElement().getStyle().clearDisplay();
                }
            }, ClickEvent.getType());

        } else {
            // лист – без раскрытия
            switcher.addStyleName("treeFold__toggle--leaf");
            switcher.addStyleName("org-tree-switcher-leaf");
        }
    }


}
