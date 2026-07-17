package com.edatasite.workforce.gwt.core.client.ui.search;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 19.11.2009
 * Time: 17:41:17
 * To change this template use File | Settings | File Templates.
 */
public class SearchViewSinksContainer extends SinksContainer {

    public SearchViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        super.addView(new OverallSearchView());
        setPreparedView("overalSearch");
    }
}
