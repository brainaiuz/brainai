package com.edatasite.workforce.gwt.core.client.ui.viewFooter;

import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public interface IFooteredView {

    List<Widget> getFooterLeftSideWidgets();

    List<Widget> getFooterRightSideWidgets();
}
