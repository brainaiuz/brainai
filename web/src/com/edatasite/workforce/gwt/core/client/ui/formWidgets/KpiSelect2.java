package com.edatasite.workforce.gwt.core.client.ui.formWidgets;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hurshid on 10/3/2018.
 * https://select2.org/
 */
public class KpiSelect2 extends Composite {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    HTMLPanel select2Panel;

    private static KpiSelect2UiBinder ourUiBinder = GWT.create(KpiSelect2UiBinder.class);
    private HashMap<Integer, OptionElement> optionMap = new HashMap<>();
    private HashMap<Integer, SelectItem> itemMap = new HashMap<>();
    private EventListener eventListener;
    private boolean fireEvent;
    private boolean multiple;
    private String NULL_VALUE = wfmStrings.pleaseSelect();

    public KpiSelect2() {
        this(false);
    }

    public KpiSelect2(boolean multiple) {
        this.multiple = multiple;
        initWidget(ourUiBinder.createAndBindUi(this));

        reload();

        setMultiple(this.multiple);
    }

    private void reload() {
        select2Panel.addAttachHandler(attachEvent -> {
            if (multiple) {
                select2HandlerMultiple(select2Panel.getElement(), NULL_VALUE);
            } else {
                select2Handler(select2Panel.getElement(), NULL_VALUE);
            }
        });
    }

    public static void fireChangeEvent(EventListener eventListener) {
        if (eventListener != null) {
            eventListener.onBrowserEvent(null);
        }
    }

    private native void select2Handler(Element element, String nullable) /*-{
        $wnd.$(element).select2({
            placeholder: nullable,
            allowClear: true,
            width: '100%'
        });
    }-*/;

    private native void select2HandlerMultiple(Element element, String nullable) /*-{
        $wnd.$(element).select2({
            placeholder: nullable,
            allowClear: true,
            closeOnSelect: false,
            width: '100%'
        });
    }-*/;

    public void setMultiple(boolean multiple) {
        if (this.multiple = multiple) {
            this.addStyleName("js-example-basic-multiple");
            this.getElement().setAttribute("multiple", "multiple");
        } else {
            this.addStyleName("js-example-basic-simple");
        }
    }

    public void onValueChangeHandler(EventListener eventListener) {
        this.eventListener = eventListener;
        bindHandler(this.getElement(), eventListener);
    }

    private native void bindHandler(Element element, EventListener eventListener) /*-{
        $wnd.$(element).change(function () {
            @com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2::fireChangeEvent(*)(eventListener);
        })
    }-*/;

    private void insertOption(SelectItem item, Boolean selected) {

        OptionElement option;
        if (item.getId() != null && optionMap.containsKey(item.getId())) {
            if (selected != null) {
                option = optionMap.get(item.getId());
                option.setSelected(selected);
            }
        } else {
            if (item.getName() == null || item.getName().trim().length() == 0 || item.getId() == null) return;

            if (this.getElement().getChildCount() == 0) {
                this.getElement().appendChild(Document.get().createOptionElement());
            }
            option = Document.get().createOptionElement();
            if (selected != null)
                option.setSelected(selected);
            option.setInnerHTML(item.getName());
            option.setValue(item.getId() + "");
            this.getElement().appendChild(option);
            optionMap.put(item.getId(), option);
            itemMap.put(item.getId(), item);
        }
    }

    public void setSelected(Integer id) {

        setSelected(select2Panel.getElement(), id);

        if (fireEvent && eventListener != null) {
            eventListener.onBrowserEvent(null);
        }
    }

    public void setSelectedItems(List<SelectItem> items) {
       if(items !=null && items.size() > 0){
           for (SelectItem selectedItem : items) {
               optionMap.get(selectedItem.getId()).setSelected(true);
           }
       }
    }

    public void clear() {
        this.getElement().removeAllChildren();
        optionMap.clear();
        itemMap.clear();
    }

    private native String[] getValues(Element element) /*-{
        return $wnd.$(element).val();
    }-*/;

    private native String getValue(Element element) /*-{
        return $wnd.$(element).val();
    }-*/;

    private native void setSelected(Element element, Integer id) /*-{
        $wnd.$(element).val(id + '').trigger("change");
    }-*/;

    public ArrayList<SelectItem> getSelectedItems() {
        ArrayList<String> selected = new ArrayList<>();
        if (multiple) {
            String[] vals = getValues(this.getElement());
            Arrays.stream(vals).forEach(s -> selected.add(s.replace("\"", "")));
        } else {
            String val = getValue(this.getElement());
            if (val != null) {
                selected.add(val.replace("\"", ""));
            }
        }
        ArrayList<SelectItem> items = new ArrayList<>();
        NodeList<OptionElement> options = ((SelectElement) this.getElement().cast()).getOptions();
        for (int i = 0; i < options.getLength(); i++) {
            OptionElement option = options.getItem(i);
            if (option.getValue() != null && option.getValue().length() > 0 && selected.contains(option.getValue())) {
                Integer itemId = Integer.parseInt(option.getValue());
                if (itemMap.get(itemId) != null) {
                    items.add(itemMap.get(itemId));
                } else {
                    items.add(new SelectItem(itemId, option.getLabel()));
                }
            }
        }
        return items;
    }

    public SelectItem getSelectedItem() {
        ArrayList<SelectItem> list = getSelectedItems();
        return list.isEmpty() ? null : list.get(0);
    }

    public Long getSelectedId() {
        SelectItem item = getSelectedItem();
        return item == null || "".equals(item.getName().trim()) ? null : Long.parseLong(item.getName());
    }

    public void setItems(ArrayList<SelectItem> values) {
        if (values == null) return;

        values.forEach(item -> insertOption(item, item.isSelected()));
        reload();
    }

    /**
     * Lazy loading items see https://select2.org/data-sources/ajax
     *
     * @param url
     */
    public void setUrl(String url) {
        getElement().setAttribute("data-ajax-url", url);
        getElement().setAttribute("data-ajax-cache", "true");
    }

    public void setFireEvent(boolean fireEvent) {
        this.fireEvent = fireEvent;
    }

    public SelectItem[] getItems() {
        List<SelectItem> items = new ArrayList<>();
        NodeList<OptionElement> options = ((SelectElement) this.getElement().cast()).getOptions();
        for (int i = 0; i < options.getLength(); i++) {
            OptionElement option = options.getItem(i);
            if (option.getValue() != null && option.getValue().length() > 0) {
                items.add(new SelectItem(Integer.parseInt(option.getValue()), option.getLabel()));
            }
        }
        return items.toArray(new SelectItem[]{});
    }

    interface KpiSelect2UiBinder extends UiBinder<HTMLPanel, KpiSelect2> {
    }

    public void validate(String className, boolean hasError) {
        addErrorClass(this.select2Panel.getElement(), className, hasError);
    }

    private native void addErrorClass(Element element, String className, boolean hasError) /*-{
        if (hasError) {
            $wnd.$(element).parent().find('span.select2-container').addClass(className);
        } else {
            $wnd.$(element).parent().find('span.select2-container').removeClass(className);
        }
    }-*/;

    public void setEnabled(boolean enabled) {
        if (enabled) {
            getElement().setPropertyString("disabled", "");
        } else {
            getElement().setPropertyString("disabled", "disabled");
        }
//        getElement().setClassName();
    }
}
