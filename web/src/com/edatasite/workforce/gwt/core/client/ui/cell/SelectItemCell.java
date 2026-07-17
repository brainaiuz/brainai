package com.edatasite.workforce.gwt.core.client.ui.cell;

/**
 * Created by Sherali Pirnafasov.
 * User: Sherali Pirnafasov
 * Email: sherali.pirnafasov@gmail.com
 * Date: 10/29/11
 * Time: 6:21 PM
 */

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A {@link com.google.gwt.cell.client.Cell} used to render a drop-down list.
 */
public class SelectItemCell<E extends SelectItem> extends AbstractInputCell<SelectItem, SelectItem> {

    private String width;
    private String styleName;

    interface Template extends SafeHtmlTemplates {
        @Template("<option value=\"{0}\">{0}</option>")
        SafeHtml deselected(String option);

        @Template("<option value=\"{0}\" selected=\"selected\">{0}</option>")
        SafeHtml selected(String option);
    }

    private static Template template;

    private HashMap<Integer, SelectItem> indexForOption = new HashMap<>();

    private final List<SelectItem> options;

    /**
     * Construct a new {@link SelectItemCell} with the specified options.
     *
     * @param options the options in the cell
     */
    public SelectItemCell(List<E> options) {
        super("change");
        if (template == null) {
            template = GWT.create(Template.class);
        }
        this.options = new ArrayList<>(options);
        for (SelectItem option : options) {
            indexForOption.put(option.getId(), option);
        }
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, SelectItem value,
                               NativeEvent event, ValueUpdater<SelectItem> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        String type = event.getType();
        if ("change".equals(type)) {
            Object key = context.getKey();
            SelectElement select = parent.getFirstChild().cast();
            SelectItem newValue = options.get(select.getSelectedIndex());
            setViewData(key, newValue);
            finishEditing(parent, newValue, key, valueUpdater);
            if (valueUpdater != null) {
                valueUpdater.update(newValue);
            }
        }
    }

    @Override
    public void render(Context context, SelectItem value, SafeHtmlBuilder sb) {
        // Get the view data.
        Object key = context.getKey();
        SelectItem viewData = getViewData(key);
        if (viewData != null && viewData.equals(value)) {
            clearViewData(key);
            viewData = null;
        }

        SelectItem selectedIndex = getSelectedIndex(viewData == null ? value : viewData);
        sb.appendHtmlConstant("<select " + (styleName != null ? "class=\"" + styleName + "\"" : "") +
                (width != null ? "width:" + width + ";" : "") + " \" tabindex=\"-1\">");
        for (SelectItem option : options) {
            if (option.getId().equals(selectedIndex.getId())) {
                sb.append(template.selected(option.getName()));
            } else {
                sb.append(template.deselected(option.getName()));
            }
        }
        sb.appendHtmlConstant("</select>");
    }

    private SelectItem getSelectedIndex(SelectItem value) {
        SelectItem index = indexForOption.get(value.getId());
        if (index == null) {
            return new SelectItem();
        }
        return index;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
}
