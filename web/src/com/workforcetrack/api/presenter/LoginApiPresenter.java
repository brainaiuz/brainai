package com.workforcetrack.api.presenter;

import com.workforcetrack.mobile.rpc.client.MSelectItem;
import com.workforcetrack.mobile.rpc.login.MUserCompanyDTO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 06/09/12
 * Time: 16:49
 * To change this template use File | Settings | File Templates.
 */
public class LoginApiPresenter extends BaseApiPresenter {

    public Map<String, Object> convertToMap(MUserCompanyDTO item){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(USER_NAME, item.getUserName());
        map.put(COMPANY_ID, item.getCompanyID());
        map.put(SESSION_ID, item.getSessionID());
        map.put(COMPANY_NAME, item.getCompanyName());
        map.put( COMPANY_DESCRIPTION, item.getCompanyDescription());
        map.put(LOGO, item.getLogo());

        if(item.getRoleItems()!= null && item.getRoleItems().size() > 0){
            ArrayList<Map<String, Object>> list = new ArrayList<>();
            for (MSelectItem role : item.getRoleItems()) {
                Map<String, Object> roleList = new LinkedHashMap<>();
                roleList.put(NAME, role.getName());
                roleList.put(OBJECT_ID, role.getObjectID());
                list.add(roleList);
            }
            map.put(ROLE_ITEMS, list);
        }
        map.put(ACTIVE, item.getActive());

        return map;
    }
}
