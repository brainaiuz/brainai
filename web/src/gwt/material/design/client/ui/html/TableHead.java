package gwt.material.design.client.ui.html;

import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.MaterialWidget;

public class TableHead extends MaterialWidget {
    public TableHead() {
        super(Document.get().createTHeadElement());
    }
}
