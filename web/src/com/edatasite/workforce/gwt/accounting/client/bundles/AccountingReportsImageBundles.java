package com.edatasite.workforce.gwt.accounting.client.bundles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 26.11.2009
 * Time: 16:37:09
 * To change this template use File | Settings | File Templates.
 */
public interface AccountingReportsImageBundles extends ClientBundle {

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/client/bundles/icons/reconciled.png")
    ImageResource reconciled();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/client/bundles/icons/notreconciled.png")
    ImageResource notReconciled();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/client/bundles/icons/markedasreconciled.png")
    ImageResource markedAsReconsiled();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/resource/new_line.png")
    ImageResource newLine();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/resource/new_line_over.png")
    ImageResource newLineOver();

    class App {
        public static AccountingReportsImageBundles get() {
            return (AccountingReportsImageBundles) GWT.create(AccountingReportsImageBundles.class);
        }
    }

}
