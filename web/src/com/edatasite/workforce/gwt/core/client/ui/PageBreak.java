package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.ui.MaterialCollapsibleItem;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class PageBreak extends Composite{
    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    private WfmButton2 next;
    private WfmButton2 prev;
    private InputGroup inputGroup;
    private Map<Integer, Set<String>> sectionMaps = new HashMap<>();
    private LinkedHashMap<String, MaterialCollapsibleItem> sectionFields = new LinkedHashMap<>();

    private Integer activeKey = 1;

    public PageBreak() {
        init();
    }

    public void clickedEvent(Integer activeKey) {
        visibilityOfPages(true,sectionMaps.get(activeKey));
        prev.setEnabled(false);
        clickedPrev();
        clickedNext();
    }


    private void init() {
        next = new WfmButton2(wfmStrings.next(), WfmButton2.BTN_PRIMARY);
        prev = new WfmButton2(wfmStrings.prev(), WfmButton2.BTN_PRIMARY);
        next.setMarginLeft(10);
        prev.setMarginRight(10);
        inputGroup = new InputGroup(new GRow(new GColumn(GColumnEnum.COL_6, prev), new GColumn(GColumnEnum.COL_6, next)));
        initWidget(inputGroup);
    }

    private void clickedPrev() {
        prev.addClickHandler(event -> {
            prevAction();
        });
    }

    private void prevAction() {
        next.setEnabled(true);
        Set<String> items = sectionMaps.get(activeKey);
        visibilityOfPages(false,items);
        this.activeKey -= 1;
        visibilityOfPages(true,sectionMaps.get(activeKey));
        if (this.activeKey == 1) {
            prev.setEnabled(false);
        }
    }

    private void clickedNext() {
        next.addClickHandler(event -> {
            nextAction();
        });
    }

    private void nextAction() {
        prev.setEnabled(true);
        visibilityOfPages(false,sectionMaps.get(activeKey));
        this.activeKey += 1;
        visibilityOfPages(true,sectionMaps.get(activeKey));
        if (this.activeKey == sectionMaps.keySet().size()) {
            next.setEnabled(false);
        }
    }

    public void setActivePageId(String sectionName) {
        next.setEnabled(true);
        prev.setEnabled(true);

        for (Integer keySection : sectionMaps.keySet()) {
            if (sectionMaps.get(keySection).contains(sectionName)){
                this.activeKey = keySection;
                visibilityOfPages(true,sectionMaps.get(keySection));
            }else{
                visibilityOfPages(false,sectionMaps.get(keySection));
            }
        }

        if (this.activeKey == 1) {
            prev.setEnabled(false);
        } else if (this.activeKey == sectionMaps.keySet().size()) {
            next.setEnabled(false);
        }
    }

    public Map<Integer, Set<String>> getSectionMaps() {
        return sectionMaps;
    }

    public void setSectionMaps(Map<Integer, Set<String>> sectionMaps) {
        this.sectionMaps = sectionMaps;
    }


    private void visibilityOfPages(Boolean isVisible, Set<String> pages) {
        for (String pageName : pages) {
            if (sectionFields.get(pageName) != null){
                GWT.log("Sections name " + pageName);
                sectionFields.get(pageName).setVisible(isVisible);
                sectionFields.get(pageName).setActive(isVisible);
            }
        }
    }

    public LinkedHashMap<String, MaterialCollapsibleItem> getSectionFields() {
        return sectionFields;
    }

    public void setSectionFields(LinkedHashMap<String, MaterialCollapsibleItem> sectionFields) {
        this.sectionFields = sectionFields;
    }
}
