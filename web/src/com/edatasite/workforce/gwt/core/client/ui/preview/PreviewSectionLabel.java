package com.edatasite.workforce.gwt.core.client.ui.preview;

import com.google.gwt.user.client.ui.HTML;

public class PreviewSectionLabel extends HTML {
    public PreviewSectionLabel(String header, String description) {
        super();
        final StringBuilder sb = new StringBuilder();
        sb.append("<b>" + header + ":</b>");
        sb.append("</br>" + description);
        super.setHTML(sb.toString());
    }
}