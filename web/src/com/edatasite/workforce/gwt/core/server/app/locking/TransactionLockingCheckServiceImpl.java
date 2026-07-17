package com.edatasite.workforce.gwt.core.server.app.locking;

import com.edatasite.workforce.core.domain.locking.EdsTransactionLocking;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.server.db.locking.TransactionLockingManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CacheConstants;
import com.edatasite.workforce.utils.redis.RedisClient;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TransactionLockingCheckServiceImpl implements TransactionLockingCheckService {

    private final TransactionLockingManager transactionLockingManager;

    public TransactionLockingCheckServiceImpl(TransactionLockingManager transactionLockingManager) {
        this.transactionLockingManager = transactionLockingManager;
    }

    public Boolean lockedForSales() {
        return lockEnabledForModule("sales");
    }
    public Boolean lockedForPurchases() {
        return lockEnabledForModule("purchases");
    }
    public Boolean lockedForBanking() {
        return lockEnabledForModule("banking");
    }
    public Boolean lockedForEmployees() {
        return lockEnabledForModule("employees");
    }
    public Boolean lockedForAttendance() {
        return lockEnabledForModule("attendance");
    }
    public Boolean lockedForRecruitment() {
        return lockEnabledForModule("recruitment");
    }
    public Boolean lockedForPayslips() {
        return lockEnabledForModule("payslips");
    }
    public Boolean lockedForCashAdvances() {
        return lockEnabledForModule("cashAdvances");
    }
    public Boolean lockedForAdditionalPayments() {
        return lockEnabledForModule("additionalPayments");
    }

    public DateNonConvertable getLockDate() {
        String key = CacheConstants.TRANSACTION_LOCKING + "_lock_date_" + SecurityContext.getCompanyID();
        String dateString = RedisClient.getKey(key);
        if (dateString == null || "null".equals(dateString)) {
            return null;
        }
        Date lockDate = null;
        if (dateString != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                lockDate = sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (lockDate == null) {
            EdsTransactionLocking lock = transactionLockingManager.getLock();
            if (lock != null) {
                lockDate = lock.getLockDate();
                RedisClient.setKey(key, String.valueOf(lockDate));
            }
        }
        return lockDate != null && !"".equals(lockDate) ? new DateNonConvertable(lockDate) : null;
    }

    public Boolean lockEnabledForModule(String module) {
        Boolean enabled = RedisClient.getKey(generateKey(module), Boolean.class);

        if (enabled == null) {
            EdsTransactionLocking lock = transactionLockingManager.getLock();
            if (lock != null) {
                enabled = "locked".equals(lock.getModules().get(module).getStatus());
                RedisClient.setKey(generateKey(module), String.valueOf(enabled));
            }
        }
        return enabled != null && enabled;
    }

    private String generateKey(String module) {
        return CacheConstants.TRANSACTION_LOCKING + "_" + module + "_" + SecurityContext.getCompanyID();
    }
}
