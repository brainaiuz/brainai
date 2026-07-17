package com.edatasite.workforce.gwt.core.server.db.certificate;

import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmploymentType;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by Khasan on 30.09.14.
 */
public interface CertificateOfEmploymentTypeManager extends Manager<EdsCertificateOfEmploymentType> {


    List<EdsCertificateOfEmploymentType> getCertificateTypeList(ListingFilterParameter fp);

    Integer getCertificateTypeTotalCount(ListingFilterParameter fp);

    EdsCertificateOfEmploymentType getByName(String name);
}
