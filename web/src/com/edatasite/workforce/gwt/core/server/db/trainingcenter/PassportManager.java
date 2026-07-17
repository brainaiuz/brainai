package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsPassport;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 14/06/14
 * Time: 16:50
 * To change this template use File | Settings | File Templates.
 */
public interface PassportManager extends Manager<EdsPassport> {
    List<EdsPassport> getList(ListingFilterParameter filterParameter);

    Integer getTotalCount(ListingFilterParameter filterParameter);

    void deletePassport(Integer passportID);

    EdsPassport findPassportByNumber(String numberString, String number);
}
