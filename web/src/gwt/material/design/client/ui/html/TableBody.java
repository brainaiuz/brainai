package gwt.material.design.client.ui.html;

import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.MaterialWidget;

public class TableBody extends MaterialWidget {
    public TableBody() {
        super(Document.get().createTBodyElement());
    }
}
