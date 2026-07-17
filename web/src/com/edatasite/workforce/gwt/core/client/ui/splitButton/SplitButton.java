package com.edatasite.workforce.gwt.core.client.ui.splitButton;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 5/22/13
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class SplitButton extends Composite {

    private SplitButtonItem defaultItem;
    private HashMap<String, SplitButtonItem> itemsMap;
    private HashMap<String, Widget> widgetsMap;

    private HorizontalPanel labelPanel;
    private boolean isClickable;
    private Integer itemWidth, itemsHeight = 0, topPosition = 0;

    private MaterialPanel pnlBtnGroup;
    private MaterialPanel pnlDDSplit;
    private WfmButton2 buttonForLabel;
    private String btnStyleType = WfmButton2.BTN_DEFAULT;

    /**
     * Create SplitButton with clickable label
     *
     * @param itemWidth
     */
    @UiConstructor
    public SplitButton(Integer itemWidth, String btnStyleType, boolean directionTop) {
        this(null, itemWidth, btnStyleType, directionTop);
    }

    public SplitButton(Integer itemWidth, String btnStyleType) {
        this(null, itemWidth, btnStyleType);
    }

    /**
     * Create SplitButton without clickable label
     *
     * @param labelText
     * @param itemWidth
     */
    public SplitButton(String labelText, Integer itemWidth, String btnStyleType) {
        this(labelText, itemWidth, btnStyleType, true);
    }

    public SplitButton(String labelText, Integer itemWidth, String btnStyleType, boolean directionTop) {

        if (directionTop) {
            pnlBtnGroup = new MaterialPanel("btn-group dropdown-split dropdown-split--top");
        } else {
            pnlBtnGroup = new MaterialPanel("btn-group dropdown-split");
        }
        pnlDDSplit = new MaterialPanel("btn-group dropdown-split__toggle");
        initWidget(pnlBtnGroup);

        this.defaultItem = new SplitButtonItem("", labelText, null, true);
        this.itemWidth = itemWidth;
        this.isClickable = Utils.isNullOrEmpty(labelText);
        this.btnStyleType = btnStyleType;

        itemsMap = new HashMap<>();
    }

    /**
     * Add SplitButton items as List
     *
     * @param itemList
     */
    /*public void addItemList(List<SplitButtonItem> itemList) {
        addItemList(itemList, true);
    }*/

    public void addItemList(List<SplitButtonItem> itemList) {
        addItemList(itemList, false);
    }

    public void addItemList(List<SplitButtonItem> itemList, boolean noDefaultItem) {

        if (itemList == null || itemList.isEmpty()) {
            return;
        }
        clear();

        for (SplitButtonItem menuItem : itemList) {

            if (isClickable && menuItem.isDefaultForLabel()) {
                defaultItem = menuItem;
            }
            itemsMap.put(menuItem.getKey(), menuItem);
        }

        if (!noDefaultItem) {

            if (defaultItem == null) {
                defaultItem = itemList.get(0);
            }
            itemsMap.remove(defaultItem.getKey());
            drawButton(!itemsMap.isEmpty());
        }

        if (!itemsMap.isEmpty()) {
            drawButtonSubItems(new ArrayList<>(itemsMap.values()));
        }
    }

    /**
     * Draw button label
     *
     * @param hasMoreItems
     */

    private void drawButton(boolean hasMoreItems) {
        buttonForLabel = new WfmButton2(defaultItem.getText(), btnStyleType);
        buttonForLabel.addClickHandler(ch -> {

            if (defaultItem.getScheduledCommand() != null) {
                defaultItem.getScheduledCommand().execute();
            }
        });
        pnlBtnGroup.add(buttonForLabel);
    }

    /**
     * Draw button menu items
     *
     * @param itemList
     */

    private void drawButtonSubItems(ArrayList<SplitButtonItem> itemList) {

        if (itemList != null && !itemList.isEmpty()) {

            WfmButton2 btnMore = new WfmButton2("", btnStyleType, "ficon--more-horiz");
            btnMore.addStyleName("dropdown-button");
            btnMore.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
                @Override
                public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                    if (pnlBtnGroup.getStyleName().contains("dropdown-split--open")) {
                        pnlBtnGroup.removeStyleName("dropdown-split--open");
                    } else {
                        pnlBtnGroup.addStyleName("dropdown-split--open");
                    }
                }
            });

            MaterialDropDown dropDown = new MaterialDropDown(btnMore);
            dropDown.setBelowOrigin(true);

            for (SplitButtonItem menuItem : itemList) {
                MaterialLink item = new MaterialLink(menuItem.getText());

                item.addDomHandler(new com.google.gwt.event.dom.client.TouchStartHandler() {
                    @Override
                    public void onTouchStart(com.google.gwt.event.dom.client.TouchStartEvent ev) {
                        ev.stopPropagation();
                        ev.preventDefault();
                        if (menuItem.getScheduledCommand() != null) menuItem.getScheduledCommand().execute();
                        pnlBtnGroup.removeStyleName("dropdown-split--open");
                    }
                }, TouchStartEvent.getType());

                item.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
                    @Override
                    public void onClick(com.google.gwt.event.dom.client.ClickEvent ch) {
                        ch.stopPropagation();
                        ch.preventDefault();
                        if (menuItem.getScheduledCommand() != null) {
                            menuItem.getScheduledCommand().execute();
                        }
                        pnlBtnGroup.removeStyleName("dropdown-split--open");
                    }
                });
                dropDown.add(item);
            }

            pnlDDSplit.add(btnMore);
            pnlDDSplit.add(dropDown);
            pnlBtnGroup.add(pnlDDSplit);
        }

    }

    /**
     * Show or hide split button menu item
     *
     * @param key
     * @param visible
     */
    public void showOrHideMenuItem(String key, boolean visible) {

        if (itemsMap.containsKey(key)) {
            itemsMap.get(key).setVisible(visible);
        }
    }

    public HashMap<String, SplitButtonItem> getItemsMap() {
        return itemsMap;
    }

    public void setEnabled(boolean b) {
        pnlBtnGroup.setEnabled(b);
        if (buttonForLabel != null) {
            buttonForLabel.setEnabled(b);
        }
    }

    public SplitButtonItem getDefaultItem() {
        return defaultItem;
    }

    public void clear() {
        defaultItem = null;
        itemsMap.clear();
        pnlBtnGroup.clear();
        pnlDDSplit.clear();
    }
}
