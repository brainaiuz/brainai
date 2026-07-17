package com.edatasite.workforce.gwt.core.server.db.impl.myactivity;

import com.edatasite.workforce.core.domain.myactivity.EdsMyActivityMessage;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.myactivity.MyActivityMessageManager;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Dec 7, 2009
 * Time: 2:25:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyActivityMessageManagerImpl extends BaseManager<EdsMyActivityMessage> implements MyActivityMessageManager {

    public MyActivityMessageManagerImpl() {
        super(EdsMyActivityMessage.class);
    }
}
