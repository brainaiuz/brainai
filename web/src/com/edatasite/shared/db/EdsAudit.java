package com.edatasite.shared.db;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsAuditLog;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.enums.HistoryType;
import com.edatasite.workforce.gwt.core.server.db.mongo.AuditLogRepository;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;

import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Hurshid on 3/13/2019
 */
@MappedSuperclass
public class EdsAudit extends EdsObject {

    private static AuditLogRepository getManager() {
        return (AuditLogRepository) ApplicationContextProvider.applicationContext.getBean("auditLogRepository");
    }

    @Override
    public Integer getObjectID() {
        return null;
    }

    public HistoryType getHistoryType() {
        return null;
    }

    public String getName() {
        return "";
    }

    public void addHistoryChange(String field, Object oldValue, Object newValue) {
        if (getObjectID() != null) {
            EdsAuditLog change = new EdsAuditLog();
            change.setField(field);
            change.setHistoryType(getHistoryType());
            change.setEntityID(getObjectID());
            change.setEntityName(getName());
            if (oldValue instanceof String || oldValue instanceof Double) {
                oldValue = oldValue == null ? "" : oldValue;
                change.setFromStringValue(String.valueOf(oldValue));
            } else if (oldValue instanceof Number) {
                change.setFromNumberValue((BigDecimal) oldValue);
            } else if (oldValue instanceof Date) {
                change.setFromDateValue((Date) oldValue);
            } else if (oldValue instanceof Boolean) {
                change.setFromStringValue((Boolean) oldValue ? "Yes" : "No");
            }
            if (newValue instanceof String || newValue instanceof Double) {
                newValue = newValue == null ? "" : newValue;
                change.setToStringValue(String.valueOf(newValue));
            } else if (newValue instanceof Number) {
                change.setToNumberValue((BigDecimal) newValue);
            } else if (newValue instanceof Date) {
                change.setToDateValue((Date) newValue);
            } else if (newValue instanceof Boolean) {
                change.setToStringValue((Boolean) newValue ? "Yes" : "No");
            }
            change.setModificationDate(new Date());
            EdsUser user = (EdsUser) SecurityContext.getInstance().getUser();
            if (user != null) {
                change.setUpdater(user.getObjectID());
                change.setUpdaterName(user.getName());
            }
            change.setCompanyId(SecurityContext.getInstance().getCompanyId());

            getManager().save(change);
        }
    }
}
