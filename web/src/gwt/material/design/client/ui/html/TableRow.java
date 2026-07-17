package gwt.material.design.client.ui.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.base.MaterialWidget;

public class TableRow extends MaterialWidget {
    public TableRow() {
        super(Document.get().createTRElement());
    }

    public TableRow(Widget... widgets) {
        this();
        if (widgets != null && widgets.length > 0) {
            for (Widget widget : widgets) {
                add(widget);
            }
        }
    }
}
