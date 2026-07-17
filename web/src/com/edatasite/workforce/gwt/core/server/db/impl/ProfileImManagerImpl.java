package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsProfileIm;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.db.ProfileImManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("profileImManager")
public class ProfileImManagerImpl extends BaseManager<EdsProfileIm> implements ProfileImManager {

    public ProfileImManagerImpl() {
        super(EdsProfileIm.class);
    }

    public List<EdsProfileIm> getImList() {
        EdsUser user = getUser();
        return find("from EdsProfileIm pi where pi.profile=?", user.getEmployee().getProfile());
    }

    public List<EdsProfileIm> accountListByImId(Integer imId) {
        EdsUser user = getUser();
        return find("from EdsProfileIm pi where pi.im.objectID=? and pi.profile=?", imId,
                user.getEmployee().getProfile());

    }
}
