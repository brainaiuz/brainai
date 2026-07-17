package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla Nigmatjonov
 * Date: Jan 8, 2008
 * Time: 5:35:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LocalizationManager extends Manager<EdsLocalization> {
    List<EdsLocalization> list(ListingFilterParameter filter, String code, String untranslatedField);

    List<EdsLocalization> list();

    List<EdsLocalization> list(String property);

    List<EdsLocalization> listByPropertyCode(String propertyCode, String code);

    EdsLocalization getLocalization(Integer id);

    List<String> propertyList();

    EdsLocalization getLocalizationByCode(String propertyCode, String code);

    Date getModifiedDate();

    List<SelectItem> listByProperty(String propertyCode, String language);

    EdsLocalization get(String propertyCode, String code);
}
