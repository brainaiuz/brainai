package com.edatasite.workforce.gwt.documents.client.gwtupload;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class UploadButton {
    private FileInputElement input;
    private Widget container;
    private boolean multiple;

    public UploadButton(Widget container, boolean multiple) {
        this.container = container;
        this.multiple = multiple;
        container.getElement().getStyle().setPosition(Style.Position.RELATIVE);
        container.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        container.getElement().setDir("ltr");
        this.input = createInput();
    }

    public void reset() {
        input.removeFromParent();
        input = createInput();
    }

    private FileInputElement createInput() {
        FileInputElement input = (FileInputElement) container.getElement().getOwnerDocument().createFileInputElement();
        if (multiple) {
            input.setAttribute("multiple", "multiple");
        }

        input.getStyle().setPosition(Style.Position.ABSOLUTE);
        input.getStyle().setRight(0, Style.Unit.PX);
        input.getStyle().setTop(0, Style.Unit.PX);
        input.getStyle().setMargin(0, Style.Unit.PX);
        input.getStyle().setPadding(0, Style.Unit.PX);
        input.getStyle().setCursor(Style.Cursor.POINTER);
        input.getStyle().setOpacity(0);
        input.getStyle().setHeight(52d, Style.Unit.PX);
        input.getStyle().setWidth(258d, Style.Unit.PX);
        container.getElement().appendChild(input);
        return input;
    }

    public FileInputElement getInput() {
        return input;
    }
}
