package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsSignupMessage;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.mail.EdsTemplateException;

import java.util.Map;

/**
 * User: Murad Satimov
 * Date: 9/26/17 5:47 PM
 */
public interface SignupMessageManager extends Manager<EdsSignupMessage> {

    void sendFromMobileCompanyRegistrationNotification(EdsUser administrator,
                                                       Map<String, Object> companyInfo,
                                                       boolean hasAccount) throws EdsDbException;

    void sendCompanyRegistrationNotificationToSystem(EdsUser administrator,
                                                     EdsCompany company,
                                                     String remoteAddr) throws EdsDbException;

    void sendCompanyRegistrationNotificationToUser(String adminEmail,
                                                   String userName,
                                                   String companyName,
                                                   String locale,
                                                   String activationLink,
                                                   boolean isIncludeActivationLink,
                                                   boolean existingUser) throws EdsDbException;

    void sendEmployeeActivationMessage(EdsEmployee employee) throws EdsDbException, EdsTemplateException;
}
