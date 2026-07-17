package com.edatasite.workforce.rest.v3.release10.core.service;

import com.edatasite.workforce.core.domain.EdsGymFingerPrint;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUsersFingerPrint;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.server.app.StatusServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.GymFingerPrintManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintmanager;
import com.edatasite.workforce.gwt.core.server.rpc.FingerPrintItem;
import com.edatasite.workforce.gwt.core.server.rpc.FingerPrintUserItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApiGymService {
    private static final String fingerprintDateFormat = "yyyy-MM-dd'T'HH:mm:ss";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fingerprintDateFormat);

    private final StatusServiceLocal statusServiceLocal;
    private final UserFingerPrintmanager userFingerPrintManager;
    private final GymFingerPrintManager gymFingerPrintManager;

    @Autowired
    public ApiGymService(StatusServiceLocal statusServiceLocal, UserFingerPrintmanager userFingerPrintManager, GymFingerPrintManager gymFingerPrintManager) {
        this.statusServiceLocal = statusServiceLocal;
        this.userFingerPrintManager = userFingerPrintManager;
        this.gymFingerPrintManager = gymFingerPrintManager;
    }


    @Transactional
    public void createCase(EdsUser user, Optional<String> paidStatusOptional) {
        FingerPrintItem printItem = new FingerPrintItem();
        List<FingerPrintUserItem> users = new ArrayList<>();
        FingerPrintUserItem e = new FingerPrintUserItem();
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        e.setLogTime(currentTime.format(DateTimeFormatter.ofPattern(fingerprintDateFormat)));
        e.setUserId(user.getObjectID().toString());
        e.setUserName(user.getFullName());
        users.add(e);
        printItem.setUsers(users);
        CompanyDomain companyDomain = new CompanyDomain();
        companyDomain.setFingerprintDateFormat(fingerprintDateFormat);
        companyDomain.setDynamicStatus(true);
        companyDomain.setCompanyUniqueID("19cba919-ca10-4a57-af05-186db3074718");
        companyDomain.setCompanyBranchName("Praaktis");
        saveFingerprint(paidStatusOptional, printItem, e, companyDomain, "IN");
        e.setLogTime(currentTime.plusSeconds(1).format(DateTimeFormatter.ofPattern(fingerprintDateFormat)));
        saveFingerprint(paidStatusOptional, printItem, e, companyDomain, "OUT");
    }

    @Transactional
    public void saveFingerprint(Optional<String> paidStatusOptional, FingerPrintItem printItem, FingerPrintUserItem e, CompanyDomain companyDomain, String status) {
        e.setStatus(status);
        statusServiceLocal.addFingerPrintItemsToTimeTrackAll(printItem, companyDomain);
        if (paidStatusOptional.isPresent()) {
            try {
                EdsUsersFingerPrint latestTimeEntry = userFingerPrintManager.getLatestTimeEntry(e.getUserId(), simpleDateFormat.parse(e.getLogTime()), companyDomain.getCompanyUniqueID());
                EdsGymFingerPrint edsGymFingerPrint = new EdsGymFingerPrint();
                edsGymFingerPrint.setFingerprintId(latestTimeEntry.getObjectID());
                edsGymFingerPrint.setPaidStatus(paidStatus(paidStatusOptional.get()));
                gymFingerPrintManager.create(edsGymFingerPrint);
            } catch (ParseException ex) {
                // ignore
            }
        }
    }

    private String paidStatus(String paidStatus) {
        return switch (paidStatus) {
            case "Paid", "Оплачено", "To'langan" -> "Paid";
            case "Expired", "Просрочен", "Muddati tugagan" -> "Expired";
            case "Not Paid", "Не оплачено", "To'lanmagan" -> "Not Paid";
            case "Pending", "В ожидании", "Kutilmoqda" -> "Pending";
            default -> "";
        };
    }
}
