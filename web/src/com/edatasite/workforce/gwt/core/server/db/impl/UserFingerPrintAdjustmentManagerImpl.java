package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUsersFingerPrintAdjustment;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintAdjustmentManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository("userFingerPrintAdjustmentManager")
public class UserFingerPrintAdjustmentManagerImpl extends BaseManager<EdsUsersFingerPrintAdjustment> implements UserFingerPrintAdjustmentManager {
    public UserFingerPrintAdjustmentManagerImpl() {
        super(EdsUsersFingerPrintAdjustment.class);
    }


    @Override
    public EdsUsersFingerPrintAdjustment getByFingerprint(Integer fingerprintId) {
        return (EdsUsersFingerPrintAdjustment) findSingle("select ufpa from EdsUsersFingerPrintAdjustment ufpa left join ufpa.fingerprint uf where uf.objectID = " + fingerprintId); // fixme optimize join
    }

    @Override
    public List<EdsUsersFingerPrintAdjustment> getByFingerprint(Integer[] fingerprintId) {
        if (fingerprintId == null || fingerprintId.length == 0) return List.of();
        String sql = "select ufpa from EdsUsersFingerPrintAdjustment ufpa left join ufpa.fingerprint uf where uf.objectID in (" + Stream.of(fingerprintId).map(String::valueOf).collect(Collectors.joining(",")) + ")";
        return (List<EdsUsersFingerPrintAdjustment>) find(sql);
    }
}
