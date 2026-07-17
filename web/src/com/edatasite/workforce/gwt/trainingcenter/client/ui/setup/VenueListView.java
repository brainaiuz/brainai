package com.edatasite.workforce.gwt.trainingcenter.client.ui.setup;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/16/12
 * Time: 9:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class VenueListView extends BaseListView implements TCConstants {
    private static TCStrings tcStrings = TCStrings.App.get();

    public VenueListView() {
        super(TC_VENUES, tcStrings.venues());
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
