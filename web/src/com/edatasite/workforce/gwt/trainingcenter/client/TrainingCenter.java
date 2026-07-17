package com.edatasite.workforce.gwt.trainingcenter.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.factory.PermissionDenyContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.trainingcenter.client.factory.TCSinksContainerFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/13/12
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainingCenter extends WorkforceEntryPoint {

    public interface TrainingCenterResource extends ClientBundle {
        @CssResource.NotStrict
        @Source("com/edatasite/workforce/gwt/trainingcenter/client/TrainingCenter.css")
        CssResource trainingcenter();

        @CssResource.NotStrict
        @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/client/Accounting.css")
        CssResource accounting();
    }

    public static TrainingCenterResource resource = GWT.create(TrainingCenterResource.class);

    @Override
    public void initSinksContainerFactory() {
        if (Utils.hasPermission(PermissionConstants.TC_MAIN_MENU)) {
            containerFactory = new TCSinksContainerFactory(this);
        } else {
            this.containerFactory = new PermissionDenyContainerFactory(this);
            String section = Utils.getFirstAvailableSectionName();
            if (section != null) {
                Utils.redirect(GWT.getHostPageBaseURL() + section);
            }
        }

        resource.trainingcenter().ensureInjected();
        resource.accounting().ensureInjected();
    }
}
