package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsEmbassy;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface CountryManager extends Manager<EdsCountry> {

    List<EdsCountry> list();

    List<EdsCountry> list(ListingFilterParameter fp);

    EdsCountry getCountryByCode(String s);

    List<EdsCountry> getCountryByCodeIn(List<String> codes);

    EdsCountry getCountryByName(String countryName);

    String getCountryTimeZoneAndPhoneCode(EdsCountry country);

    SelectItem[] getCountryList(ListingFilterParameter listingFilterParameter);

    void deleteEmbasies(EdsCountry country);

    EdsEmbassy getEmbassyById(Integer id);

    void createOrUpdateEmbassy(EdsEmbassy embassy);

    List<EdsEmbassy> listEmbasies();

    EdsCountry getCountryByCallCode(String callCode);

    /* EdsWikiUrls getWikiUrl(String code);*/
}
