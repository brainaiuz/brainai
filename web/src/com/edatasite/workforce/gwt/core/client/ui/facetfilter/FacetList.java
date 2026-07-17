package com.edatasite.workforce.gwt.core.client.ui.facetfilter;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 12-Jun-2010
 * Time: 19:22:28
 */
public class FacetList extends HTMLPanel {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final String ID = "wfm-FacetList";
    private static int num = 0;
    private final String id;
    private final List<FacetListClick> facetListClicks = new ArrayList<>();

    private final Map<KpiCheckBox, SelectItem> checkboxList = new HashMap<>();
    private final Map<Integer, SelectItem> checkedList = new HashMap<>();

    public FacetList() {
        super("");
        this.id = (ID + num++);
        getElement().setAttribute("id", id);
        getElement().getStyle().setPaddingLeft(4d, Style.Unit.PX);
        getElement().getStyle().setPaddingTop(4d, Style.Unit.PX);
    }

    /**
     * @param list
     */
    public void setItems(SelectItem[] list, Map<Integer, Integer> map) {
        clear();
        checkboxList.clear();
        for (SelectItem aList : list) {
            final KpiCheckBox check = new KpiCheckBox("&nbsp;" + aList.getDescription() + "<br/>", true);
            check.addClickHandler(event -> {
                SelectItem item = checkboxList.get(check);
                if (check.getValue()) {
                    checkedList.put(item.getId(), item);
                } else {
                    checkedList.remove(item.getId());
                }
                clickEvent(check.getValue());
            });
            checkboxList.put(check, aList);

            if (checkedList.containsKey(aList.getId())) {
                check.setValue(true);
            } else if (map != null && map.containsKey(aList.getId())) {
                check.setValue(true);
                checkedList.put(aList.getId(), aList);
            }

            add(check, id);
        }
        if (list.length == 0) {
            emptyFacetContent();
        }
    }

    /**
     * If facet content empty this is method working
     */
    private void emptyFacetContent() {
        HTML emptyWidget = new HTML(wfmStrings.noDataAvailable());
        emptyWidget.addStyleName("center");
        VerticalPanel emptyPanel = new VerticalPanel();
        emptyPanel.setSize("100%", "98%");
        emptyPanel.add(emptyWidget);
        emptyPanel.setCellHorizontalAlignment(emptyWidget, HasAlignment.ALIGN_CENTER);
        emptyPanel.setCellVerticalAlignment(emptyWidget, HasAlignment.ALIGN_MIDDLE);
        add(emptyPanel, id);
    }

    /**
     * @param list
     */
    public void setItemsName(SelectItem[] list, Map<Integer, Integer> map) {
        clear();
        checkboxList.clear();
        for (SelectItem aList : list) {
            final KpiCheckBox check = new KpiCheckBox("<b style='vertical-align:top;'>" + aList.getName() + "</b><br/>", true);
            check.addClickHandler(event -> {
                SelectItem item = checkboxList.get(check);
                if (check.getValue()) {
                    checkedList.put(item.getId(), item);
                } else {
                    checkedList.remove(item.getId());
                }
                clickEvent(check.getValue());
            });
            checkboxList.put(check, aList);

            if (checkedList.containsKey(aList.getId())) {
                check.setValue(true);
            } else if (map != null && map.containsKey(aList.getId())) {
                check.setValue(true);
                checkedList.put(aList.getId(), aList);
            }
            add(check, id);
        }
    }

    private void clickEvent(Boolean value) {
        for (FacetListClick click : facetListClicks) {
            click.click(value);
        }
    }

    /**
     * Select All List
     *
     * @param value
     */
    public void setAllClick(Boolean value) {
        checkedList.clear();
        for (KpiCheckBox check : checkboxList.keySet()) {
            check.setValue(value);
            SelectItem item = checkboxList.get(check);
            if (value) {
                checkedList.put(item.getId(), item);
            } else {
                checkedList.remove(item.getId());
            }
        }
    }

    /**
     * Clear old selected data
     */
    public void clearOldSelectedData() {
        checkedList.clear();
    }

    /**
     * @return Select Items
     */
    public SelectItem[] getSelectItems() {
        List<SelectItem> items = new ArrayList<>();
        for (Integer key : checkedList.keySet()) {
            items.add(checkedList.get(key));
        }
        return items.toArray(new SelectItem[]{});
    }

    public void addClickCheckBox(FacetListClick facetListClick) {
        facetListClicks.add(facetListClick);
    }

    public void clearClickEvents() {
        facetListClicks.clear();
    }
}
