package com.edatasite.workforce.gwt.core.client.ui.editableTable.cell;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget.LinkableCrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import org.gwt.advanced.client.ui.widget.cell.AbstractCell;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 2/10/12
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class LookUpCell extends AbstractCell {

    public static WfmStrings wfmStrings = WfmStrings.App.get();
    private static String SEARCH_TYPE_MESSAGE = wfmStrings.searchTypeMessage();

    private LookUp lookUp = null;
    private HandlerRegistration changeHandlerRegistration;
    private String customStyle = null;

    public LookUpCell(String customStyle) {
        this.customStyle = customStyle;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof LookUp) {
            super.setValue(value);
            lookUp = (LookUp) value;
        } else if (value instanceof SelectItem) {
            if (lookUp != null) {
                lookUp.setSelected((SelectItem) value);
            }
        } else if (lookUp != null) {
            lookUp.getSuggestBox().setText(String.valueOf(value));
        }
    }

    @Override
    protected Widget createActive() {

        //lookUp.setEnabled(true);

        lookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (lookUp.getOnSelectListener() != null) {
                lookUp.getOnSelectListener().execute();
                //displayActive(false);
            }

            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SELECTION_LOOKUPCELL, null, null);
        });

        return lookUp;
    }

    @Override
    protected Widget createInactive() {
        lookUp = (LookUp) getValue();

        return getLookUpWidget(lookUp);
    }

    private Widget getLookUpWidget(LookUp lookUp) {
        Widget widget = getLabel();
        if (getValue() instanceof LinkableCrmAccountLookUp) {
            LinkableCrmAccountLookUp linkableLookUp = (LinkableCrmAccountLookUp) getValue();
            widget = new Div();
            Anchor label = new Anchor();
            label.addClickHandler(clickEvent -> {
                if (linkableLookUp.getClickHandler() != null) {
                    linkableLookUp.getClickHandler().execute();
                }
            });
            if (linkableLookUp != null && linkableLookUp.getSelectedItem() != null) {
                label.setText(linkableLookUp.getSelectedItem().getName());
            } else if (linkableLookUp != null) {
                label.setText(!linkableLookUp.getSuggestBox().getText().contains(SEARCH_TYPE_MESSAGE) ? linkableLookUp.getSuggestBox().getText() : null);
            }
            ((Div) widget).add(label);
        } else {
            if (lookUp != null && lookUp.getSelectedItem() != null) {
                ((Label) widget).setText(lookUp.getSelectedItem().getName());
            } else if (lookUp != null) {
                ((Label) widget).setText(!lookUp.getSuggestBox().getText().contains(SEARCH_TYPE_MESSAGE) ? lookUp.getSuggestBox().getText() : null);
            }
        }
        if (customStyle != null && !"".equals(customStyle)) {
            widget.setStyleName(customStyle);
        } else {
            widget.setStyleName("lookUp-Cell");
        }

        return widget;
    }

    @Override
    public void setFocus(boolean focus) {
        lookUp = (LookUp) getValue();

        if (lookUp != null) {
            lookUp.getTextBox().setFocus(focus);
        }
    }

    @Override
    public Object getNewValue() {
        if (lookUp.getSelectedItem() != null) {
            return lookUp.getSelectedItem();
        }
        return lookUp.getSuggestBox().getText();
    }

    public Widget InActive() {
        return createInactive();
    }

    public LookUp getLookUp() {
        return lookUp;
    }

    public SelectItem getSelectedItem() {
        return lookUp.getSelectedItem();
    }

    public Integer getSelectedItemID() {
        return lookUp.getSelectedItemID();
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
    }

    public void setWidget(LookUp newLookUp) {
        this.lookUp = newLookUp;
    }
}
