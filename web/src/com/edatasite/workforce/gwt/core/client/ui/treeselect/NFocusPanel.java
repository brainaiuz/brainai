package com.edatasite.workforce.gwt.core.client.ui.treeselect;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherali
 * Date: 26.03.12
 * Time: 20:06
 * To change this template use File | Settings | File Templates.
 */
public class NFocusPanel extends FocusPanel {
    private NTreeSelectItem item;
    private List<FileResource> files;

    public NFocusPanel(Widget widget, NTreeSelectItem anItem) {
        super(widget);
        sinkEvents(Event.ONMOUSEDOWN);
        item = anItem;
    }

    /**
     * Retrieve the item.
     *
     * @return the item
     */
    public NTreeSelectItem getItem() {
        return item;
    }

    /**
     * Retrieve the files.
     *
     * @return the files
     */
    public List<FileResource> getFiles() {
        return files;
    }

    /**
     * Modify the files.
     *
     * @param newFiles the files to set
     */
    public void setFiles(List<FileResource> newFiles) {
        files = newFiles;
    }

    @Override
    public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEDOWN:
                if (DOM.eventGetButton(event) == NativeEvent.BUTTON_RIGHT || DOM.eventGetButton(event) == NativeEvent.BUTTON_LEFT) {
                    getItem().getTree().setSelectedItem(getItem());
                }
                break;
        }
        super.onBrowserEvent(event);

    }
}