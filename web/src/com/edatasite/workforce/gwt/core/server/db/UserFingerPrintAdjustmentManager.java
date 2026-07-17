package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUsersFingerPrintAdjustment;

import java.util.List;

public interface UserFingerPrintAdjustmentManager extends Manager<EdsUsersFingerPrintAdjustment> {

    EdsUsersFingerPrintAdjustment getByFingerprint(Integer fingerprintId);

    List<EdsUsersFingerPrintAdjustment> getByFingerprint(Integer[] fingerprintId);
}
