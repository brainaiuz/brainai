/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/12 7:34:30                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSmsMessage;

import java.util.List;

public interface SmsMessageManager extends Manager<EdsSmsMessage> {

    String MESSAGE_STATUS = "MESSAGE_STATUS";
    String MESSAGE_STATUS_PENDING = "MESSAGE_STATUS_PENDING";
    String SENT = "SENT";
    String FAILED = "FAILED";
    String IN_PROGRESS = "MESSAGE_STATUS_IN_PROGRESS";
    String PREFERRED = "PREFERRED";
    String NON_PREFERRED = "NON_PREFERRED";
    String FROM_USER = "FROM_USER";

    List<EdsSmsMessage> getPendingSmss();
}
