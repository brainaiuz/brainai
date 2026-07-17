package com.edatasite.workforce.gwt.documents.client.clipboard;


/**
 * @author Sherali
 */
public class Clipboard {
    public final static int CUT = 1;
    public final static int COPY = 2;
    private ClipboardItem item;

    /**
     * Retrieve the item.
     *
     * @return the item
     */
    public ClipboardItem getItem() {
        return item;
    }

    /**
     * Modify the item.
     *
     * @param item the item to set
     */
    public void setItem(ClipboardItem item) {
        this.item = item;
    }

    public boolean hasFolderOrFileItem() {
        if (item != null) {
            return item.isFileOrFolder();
        }
        return false;
    }

    public boolean hasFileItem() {
        if (item != null) {
            return item.isFile();
        }
        return false;
    }

    public boolean hasUserItem() {
        if (item != null) {
            return item.isUser();
        }
        return false;
    }

    public boolean hasMemberItem() {
        if (item != null) {
            return item.isMember();
        }
        return false;
    }

    public boolean isEmpty() {
        return item == null;
    }

    public void clear() {
        item = null;
    }
}
