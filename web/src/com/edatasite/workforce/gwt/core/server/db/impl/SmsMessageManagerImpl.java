package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsMessage;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsSmsMessage;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.SmsMessageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Repository("smsSenderManager")
public class SmsMessageManagerImpl extends BaseManager<EdsSmsMessage> implements SmsMessageManager, Constants {

    private static Map<String, EdsReference> messageStatuses = new HashMap<>();
    private final String STATUS_SUCCESS = "status=0";
    private final String STATUS_FAILURE = "status=1";
    private static final String defaultSupportEmail = "support@kpi.com";

    static {
        messageStatuses.put(MESSAGE_STATUS_PENDING, new EdsReference(MESSAGE_STATUS_PENDING, "Pending"));
        messageStatuses.put(SENT, new EdsReference(SENT, "Sent"));
        messageStatuses.put(Constants.FAILED, new EdsReference(Constants.FAILED, "Failed"));
    }

    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;

    public SmsMessageManagerImpl() {
        super(EdsSmsMessage.class);
    }

    public void deleteSentMessage(EdsMessage message) throws EdsDbException {
        update("delete from EdsSmsMessage e where e=?", message);
    }

    public List<EdsSmsMessage> getPendingSmss() {
        return find("select m from EdsSmsMessage m where m.status.code = ? ", MESSAGE_STATUS_PENDING);
    }

    public String formatDate(Date date, EdsCompany company) {
        Format formatter;
        if (company.getCompanySettings() != null) {
            formatter = new SimpleDateFormat(company.getCompanySettings().getLongDateFormat());
        } else {
            formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);
        }
        return formatter.format(date);
    }

    public String formatDateShort(Date date, EdsCompany company) {
        Format formatter;
        if (company.getCompanySettings() != null) {
            formatter = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat());
        } else {
            formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        }
        return formatter.format(date);
    }

    public static String defaultShortDateFormat(Date date) {
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        return formatter.format(date);
    }

    public static String defaultLongDateFormat(Date date) {
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);
        return formatter.format(date);
    }
}
