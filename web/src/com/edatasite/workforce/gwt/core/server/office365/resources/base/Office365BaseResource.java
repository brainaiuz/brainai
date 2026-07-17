package com.edatasite.workforce.gwt.core.server.office365.resources.base;

import com.edatasite.workforce.gwt.core.server.office365.utils.Office365Utils;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by umidbekkarimov on 11/19/15.
 */

public abstract class Office365BaseResource implements IsSerializable {
    public String toJSON() {
        return Office365Utils.toJSON(this);
    };
}
