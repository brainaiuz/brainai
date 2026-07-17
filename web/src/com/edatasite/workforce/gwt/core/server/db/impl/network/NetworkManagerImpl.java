package com.edatasite.workforce.gwt.core.server.db.impl.network;

import com.edatasite.workforce.core.domain.network.EdsNetwork;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.network.NetworkManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: May 5, 2010
 * Time: 2:50:57 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unchecked")
@Repository("networkManager")
public class NetworkManagerImpl extends BaseManager<EdsNetwork> implements NetworkManager {

    public NetworkManagerImpl() {
        super(EdsNetwork.class);
    }


}
