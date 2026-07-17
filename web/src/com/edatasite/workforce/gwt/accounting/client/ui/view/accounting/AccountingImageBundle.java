package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 3, 2009
 * Time: 4:50:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AccountingImageBundle extends ClientBundle {

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/public/images/up_green.gif")
    ImageResource upGreen();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/public/images/down_red.gif")
    ImageResource downRed();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/public/images/removeButton_red.png")
    ImageResource removeButtonRed();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/resource/pdf-icon.jpg")
    ImageResource pdfIcon();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/public/images/shippinglabel/postcard.png")
    ImageResource shippingPostcard();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/public/images/shippinglabel/letter.png")
    ImageResource shippingLetter();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/public/images/shippinglabel/largeenvelope.png")
    ImageResource shippingLargeEnvelope();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/public/images/shippinglabel/package.png")
    ImageResource shippingPackage();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/public/images/shippinglabel/largepackage.png")
    ImageResource shippingLargePackage();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/public/images/attachment.png")
    ImageResource attachment();

    @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/public/images/storefront-bucket.png")
    ImageResource storefront();

}
