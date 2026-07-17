package com.edatasite.workforce.gwt.profile.server.app;

import com.edatasite.workforce.core.domain.locking.EdsTransactionLocking;
import com.edatasite.workforce.core.domain.locking.EdsTransactionLockingReason;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.locking.TransactionLockingManager;
import com.edatasite.workforce.gwt.core.server.db.locking.TransactionLockingReasonManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CacheConstants;
import com.edatasite.workforce.gwt.profile.client.rpc.TransactionLockingService;
import com.edatasite.workforce.gwt.profile.client.rpc.locking.TransactionLocking;
import com.edatasite.workforce.gwt.profile.client.rpc.locking.TransactionLockingModule;
import com.edatasite.workforce.utils.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Transactional
@Service("transactionLockingService")
public class TransactionLockingServiceImpl implements TransactionLockingService {

    @Autowired
    private TransactionLockingManager transactionLockingManager;
    @Autowired
    private TransactionLockingReasonManager transactionLockingReasonManager;

    @Override
    public TransactionLocking getLock() {
        EdsTransactionLocking transactionLock = transactionLockingManager.getLock();
        TransactionLocking lock = new TransactionLocking();
        setItems(lock, transactionLock);
        if (transactionLock != null && transactionLock.getLockDate() != null) {
            lock.setStatus(transactionLock.getStatus());
            lock.setLockDate(new DateNonConvertable(transactionLock.getLockDate()));
        }
        return lock;
    }

    public String lock(TransactionLocking newLockData) {
        EdsTransactionLocking existingLock = transactionLockingManager.getLock();
        List<TransactionLockingRedisData> redisDataList = new ArrayList<>();

        if (existingLock == null) {
            existingLock = new EdsTransactionLocking();
            existingLock.setDescription(newLockData.getDescription());
            existingLock.setLockDate(newLockData.getLockDate().getNonConvertedDate());
            existingLock.setModules(newLockData.getModules());
            existingLock.setStatus(getStatus(newLockData, redisDataList));
        } else {
            existingLock.setStatus(getStatus(newLockData, redisDataList));
            existingLock.setLockDate(newLockData.getLockDate().getNonConvertedDate());
            existingLock.setModules(newLockData.getModules());
        }

        transactionLockingManager.createOrUpdate(existingLock);

        redisDataList.forEach(this::pushToRedis);

        String key = CacheConstants.TRANSACTION_LOCKING + "_lock_date_" + SecurityContext.getCompanyID();
        RedisClient.removeKey(key);
        RedisClient.setKey(key, existingLock.getLockDate(), Date.class);

        EdsTransactionLockingReason lockingReason = new EdsTransactionLockingReason();
        lockingReason.setReason(newLockData.getReason());
        lockingReason.setChanges(existingLock.getChanges());
        transactionLockingReasonManager.create(lockingReason);

        return existingLock.getStatus();
    }

    private String getStatus(TransactionLocking lock, List<TransactionLockingRedisData> list) {
        String status = "unlocked";
        int totalCount = lock.getModules().size();
        int countUnlocked = 0;
        for (Map.Entry<String, TransactionLockingModule> entry : lock.getModules().entrySet()) {
            TransactionLockingModule module = entry.getValue();
            if ("unlocked".equals(module.getStatus())) {
                countUnlocked++;
            }

            list.add(getForRedis(module.getModule(), module.getStatus()));
        }
        if (countUnlocked == 0) {
            status = "locked";
        } else if (countUnlocked < totalCount) {
            status = "partially_unlocked";
        } else {
            status = "unlocked";
        }
        return status;
    }

    private void setItems(TransactionLocking lock, EdsTransactionLocking transactionLock) {
        if (ServerUtils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_SALES)) {
            sales:
            {
                TransactionLockingModule item = new TransactionLockingModule();
                item.setModule("sales");
                item.setStatus(lock.getStatus());
                lock.getModules().put("sales", item);
                if (transactionLock != null && transactionLock.getModules().get("sales") != null) {
                    lock.getModules().put("sales", transactionLock.getModules().get("sales"));
                }
            }
        }
        if (ServerUtils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_PURCHASES)) {
            purchases:
            {
                TransactionLockingModule item = new TransactionLockingModule();
                item.setModule("purchases");
                item.setStatus(lock.getStatus());
                lock.getModules().put("purchases", item);
                if (transactionLock != null && transactionLock.getModules().get("purchases") != null) {
                    lock.getModules().put("purchases", transactionLock.getModules().get("purchases"));
                }
            }
        }
        if (ServerUtils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_BANKING)) {
            banking:
            {
                TransactionLockingModule item = new TransactionLockingModule();
                item.setModule("banking");
                item.setStatus(lock.getStatus());
                lock.getModules().put("banking", item);
                if (transactionLock != null && transactionLock.getModules().get("banking") != null) {
                    lock.getModules().put("banking", transactionLock.getModules().get("banking"));
                }
            }
        }
        if (ServerUtils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_EMPLOYEES)) {
            employees:
            {
                TransactionLockingModule item = new TransactionLockingModule();
                item.setModule("employees");
                item.setStatus(lock.getStatus());
                lock.getModules().put("employees", item);
                if (transactionLock != null && transactionLock.getModules().get("employees") != null) {
                    lock.getModules().put("employees", transactionLock.getModules().get("employees"));
                }
            }
        }
        if (ServerUtils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_ATTENDANCE)) {
            attendance:
            {
                TransactionLockingModule item = new TransactionLockingModule();
                item.setModule("attendance");
                item.setStatus(lock.getStatus());
                lock.getModules().put("attendance", item);
                if (transactionLock != null && transactionLock.getModules().get("attendance") != null) {
                    lock.getModules().put("attendance", transactionLock.getModules().get("attendance"));
                }
            }
        }
        if (ServerUtils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_RECRUITMENT)) {
            recruitment:
            {
                TransactionLockingModule item = new TransactionLockingModule();
                item.setModule("recruitment");
                item.setStatus(lock.getStatus());
                lock.getModules().put("recruitment", item);
                if (transactionLock != null && transactionLock.getModules().get("recruitment") != null) {
                    lock.getModules().put("recruitment", transactionLock.getModules().get("recruitment"));
                }
            }
        }
        if (ServerUtils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_PAYSLIPS)) {
            payslips:
            {
                TransactionLockingModule item = new TransactionLockingModule();
                item.setModule("payslips");
                item.setStatus(lock.getStatus());
                lock.getModules().put("payslips", item);
                if (transactionLock != null && transactionLock.getModules().get("payslips") != null) {
                    lock.getModules().put("payslips", transactionLock.getModules().get("payslips"));
                }
            }
        }
        if (ServerUtils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_CASH_ADVANCES)) {
            cashAdvances:
            {
                TransactionLockingModule item = new TransactionLockingModule();
                item.setModule("cashAdvances");
                item.setStatus(lock.getStatus());
                lock.getModules().put("cashAdvances", item);
                if (transactionLock != null && transactionLock.getModules().get("cashAdvances") != null) {
                    lock.getModules().put("cashAdvances", transactionLock.getModules().get("cashAdvances"));
                }
            }
        }
        if (ServerUtils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_ADDITIONAL_PAYMENTS)) {
            additionalPayments:
            {
                TransactionLockingModule item = new TransactionLockingModule();
                item.setModule("additionalPayments");
                item.setStatus(lock.getStatus());
                lock.getModules().put("additionalPayments", item);
                if (transactionLock != null && transactionLock.getModules().get("additionalPayments") != null) {
                    lock.getModules().put("additionalPayments", transactionLock.getModules().get("additionalPayments"));
                }
            }
        }
    }

    private void pushToRedis(TransactionLockingRedisData data) {
        String key = CacheConstants.TRANSACTION_LOCKING + "_" + data.getModule() + "_" + SecurityContext.getCompanyID();
        RedisClient.removeKey(key);
        RedisClient.setKey(key, data.getLocked(), Boolean.class);
    }

    private TransactionLockingRedisData getForRedis(String module, String status) {
        return new TransactionLockingRedisData(module, "locked".equals(status));
    }

    public static class TransactionLockingRedisData implements Serializable {
        private String module;
        private Boolean locked;

        public TransactionLockingRedisData(String module, Boolean locked) {
            this.module = module;
            this.locked = locked;
        }

        public String getModule() {
            return module;
        }

        public Boolean getLocked() {
            return locked;
        }
    }
}
