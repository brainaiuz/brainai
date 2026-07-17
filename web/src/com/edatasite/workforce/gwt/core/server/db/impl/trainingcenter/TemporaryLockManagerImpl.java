package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsSeatTemporaryLock;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.TemporaryLockManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/16/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("temporaryLockManager")
public class TemporaryLockManagerImpl extends BaseManager<EdsSeatTemporaryLock> implements TemporaryLockManager {

    public TemporaryLockManagerImpl() {
        super(EdsSeatTemporaryLock.class);
    }

    @Override
    public List<EdsSeatTemporaryLock> list() {
        return find("SELECT stl FROM EdsSeatTemporaryLock stl ");
    }

    @Override
    public EdsSeatTemporaryLock getLockByBookingID(Integer bookingID) {
        return (EdsSeatTemporaryLock)findSingle("SELECT stl FROM EdsSeatTemporaryLock stl WHERE stl.bookingID = ?", bookingID);
    }

    @Override
    public void deleteByBookingID(Integer bookingID) {
        update("DELETE FROM EdsSeatTemporaryLock stl WHERE stl.bookingID = ?", bookingID);
    }
}
