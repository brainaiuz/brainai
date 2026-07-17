package com.edatasite.workforce.gwt.core.client.ui.formWidgets;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.*;

import java.util.ArrayList;
import java.util.List;

public class MultiInputBox extends Composite {
    UnorderedList list;
    ArrayList<String> items;
    TextBox box;

    public MultiInputBox() {
        items = new ArrayList<>();
        list = new UnorderedList("token-list-fb token-list--projectPosition");
        init();
    }

    private void init() {
        Div div = new Div();
        div.add(list);
        initWidget(div);

        box = new TextBox();
        box.setPlaceHolder("Enter serials...");
        box.setFocus(true);
        box.addKeyUpHandler(e -> {
            if (!Utils.isNullOrEmpty(box.getText()) && (KeyCodes.KEY_SPACE == e.getNativeKeyCode() || KeyCodes.KEY_ENTER == e.getNativeKeyCode())) {
                if (addItem(box.getText().trim())) {
                    box.setText("");
                }
            }
        });

        ListItem li = new ListItem("input-fb");
        li.add(box);
        list.add(li);

        div.addClickHandler(e -> box.setFocus(true));
    }

    private boolean addItem(String text) {
        if (items.contains(text)) {
            return false;
        }
        ListItem item = new ListItem("token-fb");

        Span cancel = new Span();
        cancel.setStyleName("close");
        cancel.addClickHandler(e -> {
            items.remove(text);
            list.remove(item);
        });

        item.add(new Paragraph(text));
        item.add(cancel);
        list.insert(item, list.getWidgetCount() - 1);
        items.add(text);
        return true;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        if (items != null && items.size() > 0) {
            for (String item : items) {
                addItem(item);
            }
        }
    }

    public void clearItems() {
        list.clear();
        items.clear();
    }
}
