package org.springframework.context.support;

import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsReference;
import org.springframework.context.MessageSource;

import java.util.Locale;

public interface WfmMessageSource extends MessageSource {
    String localizeWithParam(String code, Object... arguments);

    String localizeAccounting(String message);

    String localizeAccounting(String message, String defaultMessage);

    String localizeWithParamAccounting(String code, Object... arguments);

    String localize(String message);

    String localizeRef(EdsReference reference);

    String localizeRef(EdsLeaveReason leaveReason);

    String localize(String codeMessage, String defaultMessage, Locale locale);

    String localize(String codeMessage, String defaultMessage);

    Locale initializeUserLocale();
}
