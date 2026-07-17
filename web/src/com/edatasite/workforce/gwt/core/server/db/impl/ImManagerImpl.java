package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsIm;
import com.edatasite.workforce.core.domain.EdsProfileIm;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.db.ImManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("imManager")
public class ImManagerImpl extends BaseManager<EdsIm> implements ImManager {
    public ImManagerImpl() {
        super(EdsIm.class);
    }

    public List<EdsIm> imList() {
        return find("from EdsIm i");
    }

    public List<EdsProfileIm> getImList() {
        EdsUser user = getUser();
        if (!user.isEmployee() || (user.getEmployee() != null && user.getEmployee().getProfile() == null)) {
            return new ArrayList<>();
        }
        return find("from EdsProfileIm pi where pi.profile=?", user.getEmployee().getProfile());
    }
}
