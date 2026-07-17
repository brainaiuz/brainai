package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationService;
import com.edatasite.workforce.gwt.core.server.db.LocalizationManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: iabdullo
 * Date: 05.12.14 19:49
 */
@Transactional
@Service("localizationService")
public class LocalizationServiceImpl implements LocalizationService {

    @Autowired
    private LocalizationManager localizationManager;

    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;

    @Override
    @Transactional(readOnly = true)
    public Date getLastUpdatedDate() {
        return localizationManager.getModifiedDate();
    }

    @Transactional
    @Override
    public void update(String name, String language, String path, HashMap<String, String> map, HashMap<String, EdsLocalization> valueMap, boolean importFromCsv) {
        for (Map.Entry<String, String> entity : map.entrySet()) {
            EdsLocalization localization = null;
            if (valueMap.containsKey(entity.getKey()))
                localization = valueMap.get(entity.getKey());
            else {
                localization = localizationManager.get(name, entity.getKey());
//                if (localization == null && !"defaultText".equals(language)) continue;
            }
            if (localization == null) {
                if (importFromCsv) continue;
                localization = new EdsLocalization();
                localization.setPropertyPath(path);
                localization.setActive(true);
                localization.setCode(entity.getKey() != null ? entity.getKey().trim() : null);
                localization.setPropertyCode(name);
            }
            valueMap.put(entity.getKey(), localization);
            try {
                Field field = EdsLocalization.class.getDeclaredField(language);
                field.setAccessible(true);
                field.set(localization, entity.getValue());
                if (localization.isNew())
                    localizationManager.persist(localization);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
//        localizationManager.flushAndClear();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public ArrayList<EdsLocalization> getResourceData(String resourceName) {
        return (ArrayList<EdsLocalization>) localizationManager.list(resourceName);
    }
}
