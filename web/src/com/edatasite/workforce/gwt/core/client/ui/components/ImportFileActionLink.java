package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;

public class ImportFileActionLink extends MaterialLink {

    public ImportFileActionLink() {
        this(null);
    }

    public ImportFileActionLink(String styleName) {
        super();

        MaterialIcon iIcon = new MaterialIcon();
        iIcon.ensureDebugId("import_button_id");
        if (styleName == null || styleName.isEmpty()) {
            iIcon.addStyleName("ficon--upload hasicon--left");

        } else {
            iIcon.setStylePrimaryName(styleName + " hasicon--left");
        }
        add(iIcon);
        setText(WfmStrings.App.get().importString());
    }
}
