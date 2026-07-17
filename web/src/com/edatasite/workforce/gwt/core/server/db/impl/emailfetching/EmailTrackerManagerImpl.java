package com.edatasite.workforce.gwt.core.server.db.impl.emailfetching;

import com.edatasite.workforce.core.domain.emailfetching.EdsEmailTracker;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailTrackerManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 6/30/11
 * Time: 8:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("emailTrackerManager")
public class EmailTrackerManagerImpl extends BaseManager<EdsEmailTracker> implements EmailTrackerManager {

    public EmailTrackerManagerImpl() {
        super(EdsEmailTracker.class);
    }

    @Override
    public EdsEmailTracker getByCode(String[] codeArray) {
        if (codeArray != null && codeArray.length > 0) {
            StringBuilder codes = new StringBuilder();
            boolean addDelimitr = false;
            for (String c : codeArray) {
                if (!StringUtil.isEmpty(c.trim())) {
                    codes.append(addDelimitr ? "," : "").append("'").append(c).append("'").append(",").append("'#").append(c).append("'");
                }
                addDelimitr = true;
            }
            if (StringUtil.isEmpty(codes.toString().trim())) {
                return null;
            }
            return (EdsEmailTracker) findNativeSingle("select track.* from " + getCompanyId() + ".corpemailtracker track where track.code in (" + codes + ")", EdsEmailTracker.class);
        }
        return null;
    }

    @Override
    public Integer getCaseIDByTrackerID(Integer trackerID) {
        if (trackerID == null) {
            return null;
        }
        return (Integer) findNativeSingle("select id from " + getCompanyId() + ".crmcase where deleted is not true and tracker_id = " + trackerID);
    }
}
