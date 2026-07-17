package com.google.gwt.cell.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;

/**
 * Created by Djuraev on 9/28/15.
 */
public class TextAreaEditCell extends AbstractEditableCell<String, TextAreaEditCell.ViewData> {

    interface Template extends SafeHtmlTemplates {
        @Template ("<textarea tabindex=\"-1\" style=\"width:100%\" class=\"{0}\">{1}</textarea>")
        SafeHtml input(String className, String value);
    }

    private String className = "textArea-cell";

    static class ViewData {
        private boolean isEditing;
        private boolean isEditingAgain;
        private String original;
        private String text;

        public ViewData(String text) {
            this.original = text;
            this.text = text;
            this.isEditing = true;
            this.isEditingAgain = false;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            ViewData vd = (ViewData) o;
            return equalsOrBothNull(original, vd.original)
                    && equalsOrBothNull(text, vd.text) && isEditing == vd.isEditing
                    && isEditingAgain == vd.isEditingAgain;
        }

        public String getOriginal() {
            return original;
        }

        public String getText() {
            return text;
        }

        @Override
        public int hashCode() {
            return original.hashCode() + text.hashCode()
                    + Boolean.valueOf(isEditing).hashCode() * 29
                    + Boolean.valueOf(isEditingAgain).hashCode();
        }

        public boolean isEditing() {
            return isEditing;
        }

        public boolean isEditingAgain() {
            return isEditingAgain;
        }

        public void setEditing(boolean isEditing) {
            boolean wasEditing = this.isEditing;
            this.isEditing = isEditing;

            if (!wasEditing && isEditing) {
                isEditingAgain = true;
                original = text;
            }
        }

        public void setText(String text) {
            this.text = text;
        }

        private boolean equalsOrBothNull(Object o1, Object o2) {
            return (o1 == null) ? o2 == null : o1.equals(o2);
        }
    }

    private static Template template;
    private int inputWidth;
    private int inputLength;

    public int getInputWidth() {
        return inputWidth;
    }

    public void setInputWidth(int inputWidth) {
        this.inputWidth = inputWidth;
    }

    public int getInputLength() {
        return inputLength;
    }

    public void setInputLength(int inputLength) {
        this.inputLength = inputLength;
    }

    private final SafeHtmlRenderer<String> renderer;

    public TextAreaEditCell() {
        this(null, 1, 20);
        this.inputWidth = 10;
        this.inputLength = 2;
    }

    public TextAreaEditCell(String className) {
        this(null, 1, 20);
        this.className = className;
    }

    private int rows, cols;

    public TextAreaEditCell(SafeHtmlRenderer<String> renderer, int r, int c) {
        super("click", "keyup", "keydown", "blur");
        rows = r;
        cols = c;
        if (template == null) {
            template = GWT.create(Template.class);
        }
//        if (renderer == null) {
//            throw new IllegalArgumentException("renderer == null");
//        }
        this.renderer = renderer;
    }

    @Override
    public boolean isEditing(Context context, Element parent, String value) {
        ViewData viewData = getViewData(context.getKey());
        return viewData != null && viewData.isEditing();
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {

        Object key = context.getKey();
        ViewData viewData = getViewData(key);
        if (viewData != null && viewData.isEditing()) {
            editEvent(context, parent, value, viewData, event, valueUpdater);
        } else {
            String type = event.getType();
            int keyCode = event.getKeyCode();
            boolean enterPressed = "keyup".equals(type);
            if ("click".equals(type) || enterPressed) {
                if (viewData == null) {
                    viewData = new ViewData(value);
                    setViewData(key, viewData);
                } else {
                    viewData.setEditing(true);
                }
                edit(context, parent, value);
            }
        }
    }

    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
        Object key = context.getKey();
        ViewData viewData = getViewData(key);
        if (viewData == null) {
            viewData = new ViewData(value);
            setViewData(key, viewData);
        } else {
            viewData.setEditing(true);
        }
        if (viewData != null && !viewData.isEditing() && value != null
                && value.equals(viewData.getText())) {
            clearViewData(key);
            viewData = null;
        }

        if (viewData != null) {
            String text = viewData.getText();
            if (viewData.isEditing()) {
                sb.append(template.input(className, text));
            } else {
                sb.append(renderer.render(text));
            }
        } else if (value != null) {
            sb.append(renderer.render(value));
        }
    }

    @Override
    public boolean resetFocus(Context context, Element parent, String value) {
        if (isEditing(context, parent, value)) {
            getInputElement(parent).focus();
            return true;
        }
        return false;
    }

    protected void edit(Context context, Element parent, String value) {
        setValue(context, parent, value);
        TextAreaElement input = getInputElement(parent);
        input.focus();
        input.select();
    }

    private void cancel(Context context, Element parent, String value) {
        clearInput(getInputElement(parent));
        setValue(context, parent, value);
    }

    private native void clearInput(Element input) /*-{
        if (input.selectionEnd)
            input.selectionEnd = input.selectionStart;
        else if ($doc.selection)
            $doc.selection.clear();
    }-*/;

    private void commit(Context context, Element parent, ViewData viewData, ValueUpdater<String> valueUpdater) {
        String value = updateViewData(parent, viewData, false);
        clearInput(getInputElement(parent));
        setValue(context, parent, viewData.getOriginal());
        if (valueUpdater != null) {
            valueUpdater.update(value);
        }
    }

    private void editEvent(Context context, Element parent, String value, ViewData viewData, NativeEvent event, ValueUpdater<String> valueUpdater) {
        String type = event.getType();
        boolean keyUp = "keyup".equals(type);
        boolean keyDown = "keydown".equals(type);
        if (keyUp || keyDown) {
            int keyCode = event.getKeyCode();
            if (keyUp && keyCode == KeyCodes.KEY_ESCAPE) {
                String originalText = viewData.getOriginal();
                if (viewData.isEditingAgain()) {
                    viewData.setText(originalText);
                    viewData.setEditing(false);
                } else {
                    setViewData(context.getKey(), null);
                }
                cancel(context, parent, value);
            } else {
                updateViewData(parent, viewData, true);
            }
        } else if ("blur".equals(type)) {
            EventTarget eventTarget = event.getEventTarget();
            if (Element.is(eventTarget)) {
                Element target = Element.as(eventTarget);
                if ("textarea".equals(target.getTagName().toLowerCase())) {
                    commit(context, parent, viewData, valueUpdater);
                }
            }
        }
    }

    private TextAreaElement getInputElement(Element parent) {
        return parent.getFirstChild().cast();
    }

    private String updateViewData(Element parent, ViewData viewData, boolean isEditing) {
        TextAreaElement input = (TextAreaElement) parent.getFirstChild();
        String value = input.getValue();
        viewData.setText(value);
        viewData.setEditing(isEditing);
        return value;
    }
}