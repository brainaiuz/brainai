package gwt.material.design.client.ui.html;

import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.MaterialWidget;

public class Table extends MaterialWidget {
    public Table() {
        super(Document.get().createTableElement());
    }
}
