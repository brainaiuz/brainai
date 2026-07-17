package com.edatasite.workforce.gwt.core.server.db.impl.emailfetching;

import com.edatasite.workforce.core.domain.emailfetching.EdsTracker;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.TrackerManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Azazello on 3/30/15.
 */
@Repository("trackerManager")
public class TrackerManagerImpl extends BaseManager<EdsTracker> implements TrackerManager {
    public TrackerManagerImpl() {
        super(EdsTracker.class);
    }

    @Override
    public Integer getByMessageIDs(Integer emailSettingId, String... messageIDs) {
        if (emailSettingId == null || messageIDs == null || messageIDs.length == 0) {
            return null;
        }

        List<String> cleaned = Arrays.stream(messageIDs)
                .filter(StringUtils::isNotBlank)
                .map(id -> id.replaceAll("[<>]", "").toLowerCase())
                .collect(Collectors.toList());

        if (cleaned.isEmpty()) return null;

        // Named parameter bilan xavfsiz so'rov
        Map<String, Object> params = new HashMap<>();
        params.put("settingId", emailSettingId);
        params.put("messageIds", cleaned);

        try {
            return (Integer) findNativeSingleByNamedParams(
                    "select distinct tracker_id from " + getCompanyId() +
                            ".tracker where emailSetting_id = :settingId " +
                            "and lower(message_id) IN (:messageIds)", params);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }
}
