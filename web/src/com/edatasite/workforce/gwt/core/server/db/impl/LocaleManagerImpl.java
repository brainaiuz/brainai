package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsLocale;
import com.edatasite.workforce.gwt.core.server.db.LocaleManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: Nov 18, 2009
 * Time: 7:09:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("localeManager")
public class LocaleManagerImpl extends BaseManager<EdsLocale> implements LocaleManager {

    public LocaleManagerImpl() {
        super(EdsLocale.class);
    }

    public List<EdsLocale> list() {
        return find("select l from EdsLocale l order by l.language, l.country");
    }

    @Override
    public EdsLocale getLocaleBylanguageCode(String language) {
        return (EdsLocale)findSingle("select s from EdsLocale s where s.languageCode=?",language);
    }

    @Override
    public Integer getLocaleIdByCode(String code) {
        return (Integer) findSingle("select s.id from EdsLocale s where s.languageCode=?",code);
    }

}
