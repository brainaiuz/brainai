package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsWFTPlagin;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 11.08.2010
 * Time: 17:21:29
 * To change this template use File | Settings | File Templates.
 */
public interface WFTPlaginManager extends Manager<EdsWFTPlagin> {

    List<EdsWFTPlagin> getLastVersionPlagin(ListingFilterParameter fp);

    EdsWFTPlagin getPlugin(String plugin);
}
