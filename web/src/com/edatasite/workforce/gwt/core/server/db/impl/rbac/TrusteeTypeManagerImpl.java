package com.edatasite.workforce.gwt.core.server.db.impl.rbac;

import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TrusteeTypeManager;
import org.springframework.stereotype.Repository;

/**
 * User: Abdulaziz
 * Date: Jan 7, 2010
 * Time: 12:43:05 PM
 */
@Repository("trusteeTypeManager")
public class TrusteeTypeManagerImpl extends BaseManager<EdsTrusteeType> implements TrusteeTypeManager {
    public TrusteeTypeManagerImpl() {
        super(EdsTrusteeType.class);
    }

    public EdsTrusteeType getTrusteeType(Integer typeID) {
        return (EdsTrusteeType) findSingle("SELECT trt FROM EdsTrusteeType trt WHERE trt.id = ?", typeID);
    }

    public EdsTrusteeType creageTrusteeType(String name, String description) {
        EdsTrusteeType type = new EdsTrusteeType();
        type.setName(name);
        type.setDescription(description);
        create(type);
        return type;
    }

}
