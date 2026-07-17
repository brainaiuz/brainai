package com.edatasite.workforce.gwt.core.server.db.mongo;

import com.edatasite.workforce.core.domain.EdsAuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hurshid on 3/18/2019
 */
@Repository
public interface AuditLogRepository extends MongoRepository<EdsAuditLog, Integer> {

    List<EdsAuditLog> findAllByCompanyIdOrderByModificationDateDesc(String companyId);

    Long countAllByCompanyId(String companyId);
}
