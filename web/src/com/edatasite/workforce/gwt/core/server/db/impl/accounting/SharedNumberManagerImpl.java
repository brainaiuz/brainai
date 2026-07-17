package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.accounting.EdsSharedNumber;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SharedNumberManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BANK_TRANSFER;
import static com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager.BATCH_PAYMENT;

/**
 * Created by Sherzod on 7/14/2015.
 */
@Repository("sharedNumberManager")
public class SharedNumberManagerImpl extends BaseManager<EdsSharedNumber> implements SharedNumberManager {
    @Autowired
    private AccountingManager accountingManager;

    public SharedNumberManagerImpl() {
        super(EdsSharedNumber.class);
    }

    @Override
    public boolean isNumberExists(String number, Integer objectID, String code, String type) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("select count(sn.objectID) from EdsSharedNumber sn where sn.code = :code and sn.number = :number and (sn.deleted is null or sn.deleted<>true) ");
        values.put("code", code);
        values.put("number", number);
        if (objectID != null) {
            sql.append(" and sn.entityID not in (").append(objectID).append(") ");
        }
        Calendar financialYearStart = accountingManager.getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            if (BANK_TRANSFER.equals(type)) {
                sql.append(" and (select bt.creationDate from EdsBankTransfer bt where bt.objectID = sn.entityID and bt.number= :number and bt.creationDate is not null) > :financialYearStart");
            } else if (BATCH_PAYMENT.equals(type)) {
                sql.append(" and (select bp.creationDate from EdsBatchPayment bp where bp.objectID = sn.entityID and bp.number= :number and bp.creationDate is not null) > :financialYearStart");
            }
        }
        Long count = (Long) findSingleByNamedParams(sql.toString(), values);
        return count != null && count.intValue() > 0;
    }

    @Override
    public Integer getLastIntNumber(String code, String type) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("SELECT sn.intNumber FROM EdsSharedNumber sn WHERE sn.code = :code and (sn.deleted is null or sn.deleted<>true) and sn.intNumber IS NOT NULL ");
        values.put("code", code);
        Calendar financialYearStart = accountingManager.getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            if (BANK_TRANSFER.equals(type)) {
                sql.append(" and (select bt.creationDate from EdsBankTransfer bt where bt.objectID = sn.entityID and bt.creationDate is not null) > :financialYearStart");
            } else if (BATCH_PAYMENT.equals(type)) {
                sql.append(" and (select bp.creationDate from EdsBatchPayment bp where bp.objectID = sn.entityID and bp.creationDate is not null) > :financialYearStart");
            }
        }
        sql.append(" order by sn.intNumber desc");

        Integer lastIntNumber = (Integer) findSingleByNamedParams(sql.toString(), values);
        return lastIntNumber != null ? lastIntNumber : 0;
    }

    @Override
    public Integer saveNumberData(String number, Integer intNumber, String code, Integer entityID, String entityCode) {
        EdsSharedNumber existingNumber = (EdsSharedNumber) findSingle("select sn from EdsSharedNumber sn where sn.code=? and sn.entityID=? and sn.entityCode=? and (sn.deleted is null or sn.deleted is not true)", code, entityID, entityCode);

        EdsSharedNumber edsSharedNumber = existingNumber == null ? new EdsSharedNumber() : existingNumber;
        edsSharedNumber.setNumber(number);
        edsSharedNumber.setIntNumber(intNumber);
        edsSharedNumber.setEntityID(entityID);
        edsSharedNumber.setEntityCode(entityCode);
        edsSharedNumber.setCode(code);
        createOrUpdate(edsSharedNumber);

        return edsSharedNumber.getObjectID();
    }

    @Override
    public EdsSharedNumber getByEntityID(Integer entityID, String entityCode) {
        return (EdsSharedNumber) findSingle("select sn from EdsSharedNumber sn where sn.entityID=? and sn.entityCode = ?", entityID, entityCode);
    }

    @Override
    public BankTransferNumberData generateNewNumber(BankTransferNumberData numberData) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("SELECT MAX(sn.intNumber) FROM EdsSharedNumber sn ");
        sql.append("where sn.code = :code and (sn.deleted is null or sn.deleted<>true) and sn.intNumber IS NOT NULL");
        values.put("code", numberData.getPrefix());
        Integer lastIntNumber = (Integer) findSingleByNamedParams(sql.toString(), values);
        lastIntNumber = (lastIntNumber != null ? lastIntNumber : 0) + 1;
        String formattedNumber = EdsNumberingSettings.decimalFormat.format(lastIntNumber);

        sql = new StringBuilder();
        values.clear();
        boolean exit = true;
        sql.append("SELECT sn.number FROM EdsSharedNumber sn WHERE sn.number = :number and (sn.deleted is null or sn.deleted<>true)");
        values.put("number", numberData.getPrefix() + formattedNumber);
        while (exit) {
            String checkNumberExists = (String) findSingleByNamedParams(sql.toString(), values);
            if (checkNumberExists == null || checkNumberExists.isEmpty()) {
                exit = false;
                numberData.setFourDigitNumber(formattedNumber);
                return numberData;
            } else {
                lastIntNumber++;
                formattedNumber = EdsNumberingSettings.decimalFormat.format(lastIntNumber);
                values.replace("number", numberData.getPrefix() + formattedNumber);
            }
        }

        return numberData;
    }
}
