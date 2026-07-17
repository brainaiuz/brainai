package gwt.material.design.client.ui.html;

import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.MaterialWidget;

public class FigureWidget extends MaterialWidget {
    public FigureWidget() {
        super(Document.get().createElement("figure"));
    }
}
