package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Virus on 9/27/14.
 */
public abstract class AsyncWidget extends ComplexPanel {

    protected AsyncWidget() {
        this(null);
    }

    protected AsyncWidget(String tag) {
        this(tag, DOM.createUniqueId());
    }

    protected AsyncWidget(String tag, String id) {
        tag = tag == null ? "div" : tag;
        setElement(DOM.createElement(tag));
        if (id != null) {
            getElement().addClassName("gwt-wrapper");
            getElement().setId(id);
        }
    }

    @Override
    public void add(Widget child) {
        child.getElement().addClassName("gwt-wrapper");
        add(child, getElement());
    }

    boolean progress = false;
    private Command after;

    protected abstract Widget onInitialize();

    public void init() {
        progress = true;
        clear();
        asyncOnInitialize(new AsyncCallback<Widget>() {
            @Override
            public void onFailure(Throwable throwable) {
                progress = false;
            }

            @Override
            public void onSuccess(Widget widget) {
                progress = false;
                if (widget != null) {
                    add(widget);
                }
                if (after != null) {
                    after.execute();
                }
            }
        });
    }

    private void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public void setAfter(Command command) {
        this.after = command;
    }

    public boolean isProgress() {
        return progress;
    }
}
