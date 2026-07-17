package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsSignature;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 11.02.13
 * Time: 12:05
 * To change this template use File | Settings | File Templates.
 */
public interface SignatureManager extends Manager<EdsSignature> {
    List<EdsSignature> getSignatures(ListingFilterParameter fp);

    EdsSignature getByUser(EdsUser user);
}
