package com.edatasite.workforce.gwt.core.client.ui.editableTable;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

public class EditableImageBox extends Image implements CustomCellInterface {

    public EditableImageBox(ImageResource resource) {
        super(resource);
    }

    @Override
    public String getDisplayValue() {
        return null;
    }

    @Override
    public void setItemValue(Object value) {

    }

    @Override
    public void setItemFocus(boolean focused) {

    }
}
