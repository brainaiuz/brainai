package com.finnetlimited.reportservice.core.client.ui.refresh;

import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;

import java.util.HashMap;

/**
 * User: ${Dilsh0d}
 * Date: 26-Apr-2010
 * Time: 12:18:51
 */
public final class DRSRefresh {
    private static HashMap<String, Boolean> refresh = new HashMap<>();

    static {
        for (HistoryNamesType type : HistoryNamesType.values()) {
            refresh.put(type.name(), false);
        }
    }

    public static void refresh(HistoryNamesType type, RefreshEvent refreshEvent) {
        if (refresh.get(type.name())) {
            refreshEvent.refreshorMethods();
        }
        refresh.put(type.name(), false);
    }

    public static void registrationRefreshPages(HistoryNamesType type) {
        refresh.put(type.name(), true);
    }

    public interface RefreshEvent {
        void refreshorMethods();
    }
}
