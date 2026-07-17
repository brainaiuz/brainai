package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Dec 10, 2009
 * Time: 10:26:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class OvalPanel extends Composite {
    private MaterialPanel contentPanel;
    private Label closeButton;

    public OvalPanel() {
        initialize();
    }

    public void add(Widget widget) {
        contentPanel.add(widget);
    }

    public void addCloseButtonClickHandler(ClickHandler handler) {
        closeButton.addClickHandler(handler);
    }

    public void clear() {
        contentPanel.clear();
    }

    private void initialize() {
        contentPanel = new MaterialPanel();
        initWidget(contentPanel);

        closeButton = new Label();
        closeButton.setStyleName("close-x");
        closeButton.getElement().setInnerHTML("<svg class=\"icon--x\">\n" + "<use href=\"mainStyles/new-ui/icons/sprite__panels.svg#x\"></use>\n" + "</svg>");

        contentPanel.add(closeButton);
    }
}
