package com.edatasite.workforce.gwt.reportingsystem.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Created by Virus on 6/21/2016.
 */
public interface JsResources extends ClientBundle {

    JsResources instance =
            GWT.create(JsResources.class);

    @Source("com/edatasite/workforce/gwt/reportingsystem/resource/peg.js")
    TextResource pegJs();

    @Source("com/edatasite/workforce/gwt/reportingsystem/resource/criteria.expression.js")
    TextResource criteriaExpressionJs();
}
