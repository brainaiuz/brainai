package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsSeatTemporaryLock;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/16/12
 * Time: 3:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TemporaryLockManager extends Manager<EdsSeatTemporaryLock> {

    List<EdsSeatTemporaryLock> list();

    EdsSeatTemporaryLock getLockByBookingID(Integer bookingID);

    void deleteByBookingID(Integer bookingID);
}
