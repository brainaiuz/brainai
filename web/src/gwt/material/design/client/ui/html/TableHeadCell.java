package gwt.material.design.client.ui.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.HasText;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.mixin.TextMixin;

public class TableHeadCell extends MaterialWidget{
    public TableHeadCell() {
        super(Document.get().createTHElement());
    }

    public TableHeadCell(String text) {
        this();
        setText(text);
    }

    public void setText(String str) {
        getElement().setInnerHTML(str);
    }
}
