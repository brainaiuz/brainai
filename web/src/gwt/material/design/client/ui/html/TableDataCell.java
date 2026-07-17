package gwt.material.design.client.ui.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.HasText;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.mixin.TextMixin;

public class TableDataCell extends MaterialWidget {

    public TableDataCell() {
        super(Document.get().createTDElement());
    }

    public void setText(String str) {
        getElement().setInnerHTML(str);
    }
}
