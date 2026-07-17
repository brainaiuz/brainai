package com.edatasite.workforce.gwt.core.client.rpc.form;

import java.util.ArrayList;

/**
 * Created by Hayot on 2/20/14.
 */
public interface LayoutInterface {
    String getLayout();

    ArrayList<String> getRequiredCodes();

    boolean isButtonPanelDisabled();
}
