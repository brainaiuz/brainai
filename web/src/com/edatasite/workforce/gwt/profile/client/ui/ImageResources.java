package com.edatasite.workforce.gwt.profile.client.ui;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created with IntelliJ IDEA.
 * User: Djuraev
 * Date: 6/14/14
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageResources extends ClientBundle {

    @Source("com/edatasite/workforce/gwt/profile/resource/settings-image-for-company.jpg")
    ImageResource companyLogo();

    @Source("com/edatasite/workforce/gwt/profile/resource/settings-image-for-pdflogo.jpg")
    ImageResource logoForPDF();
}