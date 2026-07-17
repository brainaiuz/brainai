package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsLocalizationPermissions;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.LocalizationPermissionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla Nigmatjonov
 * Date: Jan 8, 2008
 * Time: 5:38:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("localizationPermissionManager")
public class LocalizationPermissionManagerImpl extends BaseManager<EdsLocalizationPermissions> implements LocalizationPermissionManager {

    @Autowired
    private CompanyManager companyManager;

    public LocalizationPermissionManagerImpl() {
        super(EdsLocalizationPermissions.class);
    }


    @Override
    public List<EdsLocalizationPermissions> list() {
        ArrayList<Object[]> result = (ArrayList<Object[]>) findNative("select c.id, lp.code, lp.defaultText, lp.en, lp.ru, lp.arabic, lp.fr," +
                "lp.ger, lp.ita, lp.neder, lp.por, lp.spa, lp.thai, lp.turkish " +
                " from " + getPublic() + ".company c " +
                " left join " + getPublic() + ".localizationpermissions lp on c.id=lp.company_id order by c.name");
        List<EdsLocalizationPermissions> items = new ArrayList<>();
        EdsLocalizationPermissions _item;
        for (Object[] item : result) {
            _item = new EdsLocalizationPermissions();
            _item.setCompany(companyManager.get(Integer.parseInt(item[0].toString())));
            _item.setCode(item[1] != null && Boolean.parseBoolean(item[1].toString()));
            _item.setDefaultText(item[2] != null && Boolean.parseBoolean(item[2].toString()));
            _item.setEn(item[3] != null && Boolean.parseBoolean(item[3].toString()));
            _item.setRu(item[4] != null && Boolean.parseBoolean(item[4].toString()));
            _item.setArabic(item[5] != null && Boolean.parseBoolean(item[5].toString()));
            _item.setFr(item[6] != null && Boolean.parseBoolean(item[6].toString()));
            _item.setGer(item[7] != null && Boolean.parseBoolean(item[7].toString()));
            _item.setIta(item[8] != null && Boolean.parseBoolean(item[8].toString()));
            _item.setNeder(item[9] != null && Boolean.parseBoolean(item[9].toString()));
            _item.setPor(item[10] != null && Boolean.parseBoolean(item[10].toString()));
            _item.setSpa(item[11] != null && Boolean.parseBoolean(item[11].toString()));
            _item.setThai(item[12] != null && Boolean.parseBoolean(item[12].toString()));
            _item.setTurkish(item[13] != null && Boolean.parseBoolean(item[13].toString()));
            items.add(_item);
        }
        return items;
    }

    @Override
    public EdsLocalizationPermissions getCompanyLocalization(Integer companyId) {
        return (EdsLocalizationPermissions) findSingle("SELECT lp FROM EdsLocalizationPermissions lp WHERE lp.company = ?", companyManager.get(companyId));
    }
}

