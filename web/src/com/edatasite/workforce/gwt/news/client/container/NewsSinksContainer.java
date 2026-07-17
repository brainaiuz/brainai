package com.edatasite.workforce.gwt.news.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.news.client.news.NewsCategoryListView;
import com.edatasite.workforce.gwt.news.client.news.NewsListView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 4:18:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class NewsSinksContainer extends SinksContainer implements Colapse {

    public NewsSinksContainer(String name, String description, String[] param) {
        super(name, description, param, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new NewsListView());
        addView(new NewsCategoryListView());
    }
}
