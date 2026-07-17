package com.edatasite.workforce.gwt.documents.client.dnd;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;


/**
 * @author Sherali
 */
public class DnDFocusPanel extends FocusPanel {
    private DnDTreeItem item;
    private List<FileResource> files;

    public DnDFocusPanel(Widget widget, DnDTreeItem anItem) {
        super(widget);
//        sinkEvents(Event.ONMOUSEDOWN);
        item = anItem;
    }

    public DnDFocusPanel(Widget widget) {
        super(widget);
    }

    /**
     * Retrieve the item.
     *
     * @return the item
     */
    public DnDTreeItem getItem() {
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

    public HTML cloneHTML() {
        if (getWidget() instanceof HTML) {
            HTML ht = (HTML) getWidget();
            return new HTML(ht.getHTML());
        }
        return null;
    }

//    @Override
//    public void onBrowserEvent(Event event) {
//        switch (DOM.eventGetType(event)) {
//            case Event.ONMOUSEDOWN:
//                if (DOM.eventGetButton(event) == NativeEvent.BUTTON_RIGHT || DOM.eventGetButton(event) == NativeEvent.BUTTON_LEFT) {
//                    getItem().tree.setSelectedItem(getItem());
//                }
//                break;
//        }
//        super.onBrowserEvent(event);
//
//    }
}