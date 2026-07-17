package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCertificate;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CertificateManager extends Manager<EdsCertificate> {
    List<EdsCertificate> getCertificateList(ListingFilterParameter filterParameter);
    NumberData generateNumberData(Integer certificateTypeID);

    void deleteCertificateItems(Integer certificateID);
    Integer getCertificateTotalCount(ListingFilterParameter filterParameter);
    void deleteCertificate(Integer certificateID);

    boolean isCertificateNumberExists(String numberString, Integer certificateTypeId, Integer objectID);
}
