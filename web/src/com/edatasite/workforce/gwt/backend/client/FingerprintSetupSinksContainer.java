package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.FingerprintSetupView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by Muhammad on 09.04.2016.
 */
public class FingerprintSetupSinksContainer extends SinksContainer{

  public FingerprintSetupSinksContainer(String name, String description, String[] params){
      super(name, description, params, CLOSE);
  }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_FINGERPRINT)) {
            addView(new FingerprintSetupView(FingerprintSetupView.FINGERPRINT_SETUP, id));
        }
    }
}
