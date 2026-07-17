package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLocale;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: Nov 18, 2009
 * Time: 7:08:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LocaleManager extends Manager<EdsLocale> {

    List<EdsLocale> list();

    EdsLocale getLocaleBylanguageCode(String language);

    Integer getLocaleIdByCode(String code);
}
