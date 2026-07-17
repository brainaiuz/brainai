package com.edatasite.workforce.gwt.team.client.services.client;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.team.client.services.dto.OrgBoardSettingsItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OrgBoardSettingsClient {

    void getOrgBoardSettings(AsyncCallback<ResultTO<OrgBoardSettingsItem>> callback);

    void updateOrgBoardSettings(OrgBoardSettingsItem settings, AsyncCallback<ResultTO<OrgBoardSettingsItem>> callback);
}
