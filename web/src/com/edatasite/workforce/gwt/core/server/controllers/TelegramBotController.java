package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.TelegramBlackListService;
import com.edatasite.workforce.gwt.timesheet.server.app.TimesheetServiceLocal;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.exceptions.BaseApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * User: Abror Abdukadirov
 * Date: 03.06.2017 0:43
 */
@Controller
@RequestMapping(value = "/telegram-bot")
public class TelegramBotController implements Constants {

    @Autowired
    private TimesheetServiceLocal timesheetService;
    @Autowired
    private TelegramBlackListService telegramBlackListService;

    @ResponseBody
    @RequestMapping(value = "/blacklist", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object saveChatToBlackList(@RequestBody Map<String, Object> params) throws BaseApiException {
        String tgId = (String) params.get("chatId");
        String chatName = (String) params.get("chatName");
        if (tgId != null && chatName != null) {
            String decryptChatId = EncryptionHelper.decryptURL(tgId);
            String decryptChatName = EncryptionHelper.decryptURL(chatName);
            if (!StringUtils.isEmpty(decryptChatId)) {
                Long chatId = null;
                try {
                    chatId = Long.valueOf(decryptChatId);
                } catch (NumberFormatException ignored) {
                }
                telegramBlackListService.saveChat(chatId, decryptChatName);
            }
        }
        return "Success";
    }

    @ResponseBody
    @RequestMapping(value = "/daily-report", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object getDailyReportForTelegramBot(
            @RequestParam(value = "id", required = false) int id) throws BaseApiException {
        return timesheetService.getDailyTimesheets(id);
    }
}
