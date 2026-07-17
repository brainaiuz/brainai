package com.edatasite.workforce.gwt.core.server.db.impl.fifo;

import com.edatasite.workforce.core.domain.fifo.EdsFifoFailure;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.fifo.FifoFailureManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EntityType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository("fifoFailureManager")
public class FifoFailureManagerImpl extends BaseManager<EdsFifoFailure> implements FifoFailureManager {

    public FifoFailureManagerImpl() {
        super(EdsFifoFailure.class);
    }

    @Override
    public EdsFifoFailure getByEntityId(Integer entityId, EntityType entityType, String companyId) {
        return (EdsFifoFailure) findSingle("select f from EdsFifoFailure f where f.entityId = ? and type=? and companyId=? and (f.deleted is null or f.deleted = false) ", entityId, entityType, Integer.valueOf(companyId));
    }

    @Override
    public EdsFifoFailure getAnyByEntityId(Integer entityId, EntityType entityType, String companyId) {
        return (EdsFifoFailure) findSingle("select f from EdsFifoFailure f where f.entityId = ? and type=? and companyId=? ", entityId, entityType, Integer.valueOf(companyId));
    }

    @Override
    public List<FIFODataMQ> getPendingFailures() {
        List<EdsFifoFailure> failureList = find("SELECT f FROM EdsFifoFailure f where " + ServerUtils.checkForDeleted("f.deleted") +" and (f.onQue is null or f.onQue <> true ) order by id ");
        return failureList.stream().map(f -> f.toRPC()).collect(Collectors.toList());
    }

    @Override
    public void deleteFifoFailure(Integer id) {
        EdsFifoFailure edsFifoFailure = get(id);
        edsFifoFailure.setDeleted(true);
        edsFifoFailure.setOnQue(false);
        update(edsFifoFailure);
    }

    @Override
    public void delete(EdsFifoFailure edsFifoFailure) {
        edsFifoFailure.setDeleted(true);
        edsFifoFailure.setOnQue(false);
        update(edsFifoFailure);
    }

    @Override
    public void updateFifoFailure(Integer id) {
        EdsFifoFailure edsFifoFailure = get(id);
        edsFifoFailure.setOnQue(true);
        edsFifoFailure.setLastAttemptAt(new Date());
        update(edsFifoFailure);
    }

}
