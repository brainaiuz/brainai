/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.cell.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;

/**
 * An {@link AbstractCell} used to render a text input.
 */
public class TextInputCell extends AbstractInputCell<String, TextInputCell.ViewData> {

    private boolean enabled = true;
    private String width;
    private boolean placeholder;
    private String className;

    interface Template extends SafeHtmlTemplates {
        @Template ("<input type=\"text\" value=\"{0}\" class=\"{1}\" tabindex=\"-1\"></input>")
        SafeHtml input(String value, String className);

        @Template("<input type=\"text\" value=\"{0}\" tabindex=\"-1\" disabled=\"\"></input>")
        SafeHtml inputDisabled(String value);

        @Template ("<input type=\"text\" value=\"{0}\" style=\"width:{1}\" class=\"{2}\" tabindex=\"-1\"></input>")
        SafeHtml input2(String value, String width, String className);

        @Template("<input type=\"text\" value=\"{0}\" style=\"width:{1}\" tabindex=\"-1\" disabled=\"\"></input>")
        SafeHtml input2Disabled(String value, String width);
    }

    /**
     * The {@code ViewData} for this cell.
     */
    public static class ViewData {
        /**
         * The last value that was updated.
         */
        private String lastValue;

        /**
         * The current value.
         */
        private String curValue;

        /**
         * Construct a ViewData instance containing a given value.
         *
         * @param value a String value
         */
        public ViewData(String value) {
            this.lastValue = value;
            this.curValue = value;
        }

        /**
         * Return true if the last and current values of this ViewData object
         * are equal to those of the other object.
         */
        @Override
        public boolean equals(Object other) {
            if (!(other instanceof ViewData)) {
                return false;
            }
            ViewData vd = (ViewData) other;
            return equalsOrNull(lastValue, vd.lastValue)
                    && equalsOrNull(curValue, vd.curValue);
        }

        /**
         * Return the current value of the input element.
         *
         * @return the current value String
         * @see #setCurrentValue(String)
         */
        public String getCurrentValue() {
            return curValue;
        }

        /**
         * Return the last value sent to the {@link ValueUpdater}.
         *
         * @return the last value String
         * @see #setLastValue(String)
         */
        public String getLastValue() {
            return lastValue;
        }

        /**
         * Return a hash code based on the last and current values.
         */
        @Override
        public int hashCode() {
            return (lastValue + "_*!@HASH_SEPARATOR@!*_" + curValue).hashCode();
        }

        /**
         * Set the current value.
         *
         * @param curValue the current value
         * @see #getCurrentValue()
         */
        protected void setCurrentValue(String curValue) {
            this.curValue = curValue;
        }

        /**
         * Set the last value.
         *
         * @param lastValue the last value
         * @see #getLastValue()
         */
        protected void setLastValue(String lastValue) {
            this.lastValue = lastValue;
        }

        private boolean equalsOrNull(Object a, Object b) {
            return (a != null) ? a.equals(b) : ((b == null));
        }
    }

    private static Template template;

    /**
     * Constructs a TextInputCell that renders its text without HTML markup.
     */
    public TextInputCell() {
        this("inputCell form-control-sm");
    }

    public TextInputCell(String className) {
        super("change", "keyup");
        if (template == null) {
            template = GWT.create(Template.class);
        }
        setClassName(className);
    }

    /**
     * Constructs a TextInputCell that renders its text using the given
     * {@link SafeHtmlRenderer}.
     *
     * @param renderer parameter is ignored
     * @deprecated the value of a text input is never treated as html
     */
    @Deprecated
    public TextInputCell(SafeHtmlRenderer<String> renderer) {
        this();
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, String value,
                               NativeEvent event, ValueUpdater<String> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        // Ignore events that don't target the input.
        InputElement input = getInputElement(parent);
        Element target = event.getEventTarget().cast();
        if (!input.isOrHasChild(target)) {
            return;
        }

        String eventType = event.getType();
        Object key = context.getKey();
        if ("change".equals(eventType)) {
            finishEditing(parent, value, key, valueUpdater);
        } else if ("keyup".equals(eventType)) {
            // Record keys as they are typed.
            ViewData vd = getViewData(key);
            if (vd == null) {
                vd = new ViewData(value);
                setViewData(key, vd);
            }
            vd.setCurrentValue(input.getValue());
        } else if ("focus".equals(eventType) && ("00:00".equals(value) || "0".equals(value))) {
            getInputElement(parent).setValue("");
        } else if ("focus".equals(eventType) && placeholder) {
            getInputElement(parent).setValue("");
        } else if ("blur".equals(eventType) && placeholder) {
            getInputElement(parent).setValue(value);
        } else if ("blur".equals(eventType) && "".equals(getInputElement(parent).getValue()) && value.length() >= 4 && ":".equals(value.substring(2, 3))) {
            getInputElement(parent).setValue("00:00");
        }
    }

    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
        // Get the view data.
        Object key = context.getKey();
        ViewData viewData = getViewData(key);
        if (viewData != null && viewData.getCurrentValue().equals(value)) {
            clearViewData(key);
            viewData = null;
        }

        String s = (viewData != null) ? viewData.getCurrentValue() : value;
        if (s != null) {
            if (enabled) {
                sb.append(width != null && !"".equals(width) ? template.input2(s, width, getClassName()) : template.input(s, getClassName()));
            } else {
                sb.append(width != null && !"".equals(width) ? template.input2Disabled(s, width) : template.inputDisabled(s));
            }
        } else {
            sb.appendHtmlConstant("<input type=\"text\" tabindex=\"-1\"></input>");
        }
    }

    @Override
    protected void finishEditing(Element parent, String value, Object key,
                                 ValueUpdater<String> valueUpdater) {
        String newValue = getInputElement(parent).getValue();

        // Get the view data.
        ViewData vd = getViewData(key);
        if (vd == null) {
            vd = new ViewData(value);
            setViewData(key, vd);
        }
        vd.setCurrentValue(newValue);

        // Fire the value updater if the value has changed.
        if (valueUpdater != null && !vd.getCurrentValue().equals(vd.getLastValue())) {
            vd.setLastValue(newValue);
            valueUpdater.update(newValue);
        }

        // Blur the element.
        super.finishEditing(parent, newValue, key, valueUpdater);
    }

    @Override
    protected InputElement getInputElement(Element parent) {
        return super.getInputElement(parent).cast();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setPlaceholder(boolean placeholder) {
        this.placeholder = placeholder;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}