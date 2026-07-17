package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.edatasite.workforce.gwt.crm.client.rpc.MessageTrackListItem;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Feb 15, 2011
 * Time: 5:40:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class MassMailMessagesExcelHandler extends BaseExcelHandler {
    private static final Logger log = LoggerFactory.getLogger(CrmLeadsExcelHandler.class);

    @Autowired
    private MassMailService massMailService;
    @Autowired
    private UserManager userManager;

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName() {
        //call setFileName(Object object);
    }

    @Override
    protected void setFileName(Object object) {
        super.setFileName(object);
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        String messageStatus = filterParametrs.getMessageStatus() == null ? "" : filterParametrs.getMessageStatus();
        switch (messageStatus) {
            case "WAITING" -> filename = "Waiting_Messages_List";
            case "SENT" -> filename = "Sent_Messages_List";
            case "UNSUBSCRIBED" -> filename = "Unsubscribed_Statistics";
            case "BOUNCED" -> filename = "Bounced_Statistics";
            case "VIEWED" -> filename = "View_Statistics";
            case "CLICKED" -> filename = "Click_Statistics";
            default -> filename = messageStatus + " Messages";
        }
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(1000);
        if ("WAITING".equals(filterParametrs.getMessageStatus()) || "SENT".equals(filterParametrs.getMessageStatus())) {
            return runMessages(filterParametrs, "SENT".equals(filterParametrs.getMessageStatus()));
        } else if ("UNSUBSCRIBED".equals(filterParametrs.getMessageStatus()) || "BOUNCED".equals(filterParametrs.getMessageStatus()) || "VIEWED".equals(filterParametrs.getMessageStatus())) {
            return runStatistics(filterParametrs);
        } else if ("CLICKED".equals(filterParametrs.getMessageStatus())) {
            return runClickStatistics(filterParametrs);
        }
        return null;
    }

    private HSSFWorkbook runMessages(ListingFilterParameter filterParametrs, boolean sent) {
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        List<String> header = filterParametrs.getListPanelTool().getColumnCodeName();
        header.remove(MailMessageItem.ACTION);
        if (sent) {
            header.remove(MailMessageItem.STATUS);
        }
        HashMap<String, String> mapColumnHeader = new HashMap<>();
        if (!sent) {
            mapColumnHeader.put(MailMessageItem.STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        }
        mapColumnHeader.put(MailMessageItem.SUBJECT, crmLocalizer.localize(PdfLocalizationName.subject));
        mapColumnHeader.put(MailMessageItem.FROM, commonLocalizer.localize(PdfLocalizationName.from));
        mapColumnHeader.put(MailMessageItem.SCHEDULED, crmLocalizer.localize(PdfLocalizationName.scheduledDate));
        mapColumnHeader.put(MailMessageItem.CREATED, commonLocalizer.localize(PdfLocalizationName.created));
        mapColumnHeader.put(MailMessageItem.UPDATED, commonLocalizer.localize(PdfLocalizationName.modifiedDate));
        mapColumnHeader.put(MailMessageItem.IS_SMS_MESSAGE, commonLocalizer.localize(PdfLocalizationName.type));

        filterParametrs.setActive(sent);//bu sent messagemasligini bilish uchun
        ListResult<MailMessageItem> messageList = massMailService.getMailMessageList(filterParametrs);
        ExcelData[] cellDatas = new ExcelData[header.size()];
        try {
            List<ExcelData[]> list = new LinkedList<>();
            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = getExcelDataHeader(new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            }
            list.add(cellDatas);
            for (MailMessageItem item : messageList.getList()) {
                cellDatas = new ExcelData[header.size()];
                for (int i = 0; i < header.size(); i++) {
                    if (MailMessageItem.STATUS.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getStatus() != null ? item.getStatus().getCode() : "N/A", ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.SUBJECT.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getSubject() != null ? item.getSubject() : "N/A", ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.FROM.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getFrom() != null ? item.getFrom() : "N/A", ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.SCHEDULED.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getScheduled() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.longDateFormat(item.getScheduled(), userManager.getUser())) : ServerUtils.longDateFormat(item.getScheduled(), userManager.getUser())) : "N/A", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.CREATED.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getCreationTime() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.longDateFormat(item.getCreationTime(), userManager.getUser())) : ServerUtils.longDateFormat(item.getCreationTime(), userManager.getUser())) : "N/A", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.UPDATED.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getUpdatedTime() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.longDateFormat(item.getUpdatedTime(), userManager.getUser())) : ServerUtils.longDateFormat(item.getUpdatedTime(), userManager.getUser())) : "N/A", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.IS_SMS_MESSAGE.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.isSmsMessage() ? commonLocalizer.localize(PdfLocalizationName.smsMessage) : commonLocalizer.localize(PdfLocalizationName.mailMessage), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        cellDatas[i] = getExcelRows(new ExcelData("", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                list.add(cellDatas);
            }
            WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate messages list excel report, exception: " + e);
        }
        return null;
    }

    private HSSFWorkbook runStatistics(ListingFilterParameter filterParametrs) {
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        List<String> header = filterParametrs.getListPanelTool().getColumnCodeName();
        header.remove(MailMessageItem.ACTION);
        HashMap<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(MailMessageItem.RECIPIENT, commonLocalizer.localize(PdfLocalizationName.email));
        mapColumnHeader.put(MailMessageItem.FIRSTNAME, commonLocalizer.localize(PdfLocalizationName.firstName));
        mapColumnHeader.put(MailMessageItem.LASTNAME, commonLocalizer.localize(PdfLocalizationName.lastName));
        mapColumnHeader.put(MailMessageItem.VIEW_COUNT, crmLocalizer.localize(PdfLocalizationName.viewCount));
        mapColumnHeader.put(MailMessageItem.COUNTRY, commonLocalizer.localize(PdfLocalizationName.country));

        ListResult<MessageTrackListItem> messageTrackList = new ListResult<>();
        if ("UNSUBSCRIBED".equals(filterParametrs.getMessageStatus())) {
            messageTrackList = massMailService.getUnsubscribedList(filterParametrs);
        } else if ("BOUNCED".equals(filterParametrs.getMessageStatus())) {
            messageTrackList = massMailService.getMessageBouncedList(filterParametrs);
        } else if ("VIEWED".equals(filterParametrs.getMessageStatus())) {
            messageTrackList = massMailService.getMessageViewTrackList(filterParametrs);
        }
        ExcelData[] cellDatas = new ExcelData[header.size()];
        try {
            List<ExcelData[]> list = new LinkedList<>();
            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = getExcelDataHeader(new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            }
            list.add(cellDatas);
            for (MessageTrackListItem item : messageTrackList.getList()) {
                cellDatas = new ExcelData[header.size()];
                for (int i = 0; i < header.size(); i++) {
                    if (MailMessageItem.RECIPIENT.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getEmail(), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.FIRSTNAME.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getFirstName(), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.LASTNAME.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getLastName(), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.VIEW_COUNT.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getOpenedCount() != null ? item.getOpenedCount() : 0, ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.COUNTRY.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getCountry(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        cellDatas[i] = getExcelRows(new ExcelData("", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                list.add(cellDatas);
            }
            WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate Message Statistics excel report, exception: " + ex);
        }
        return null;
    }

    private HSSFWorkbook runClickStatistics(ListingFilterParameter filterParametrs) {
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        List<String> header = filterParametrs.getListPanelTool().getColumnCodeName();
        header.remove(MailMessageItem.ACTION);
        HashMap<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(MailMessageItem.RECIPIENT, commonLocalizer.localize(PdfLocalizationName.email));
        mapColumnHeader.put(MailMessageItem.FIRSTNAME, commonLocalizer.localize(PdfLocalizationName.firstName));
        mapColumnHeader.put(MailMessageItem.LASTNAME, commonLocalizer.localize(PdfLocalizationName.lastName));
        mapColumnHeader.put(MailMessageItem.LINK, crmLocalizer.localize(PdfLocalizationName.link));
        mapColumnHeader.put(MailMessageItem.CLICK_COUNT, crmLocalizer.localize(PdfLocalizationName.clickCount));

        ListResult<MessageTrackListItem> messageTrackList = massMailService.getMessageClickTrackList(filterParametrs);
        ExcelData[] cellDatas = new ExcelData[header.size()];
        try {
            List<ExcelData[]> list = new LinkedList<>();
            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = getExcelDataHeader(new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            }
            list.add(cellDatas);
            for (MessageTrackListItem item : messageTrackList.getList()) {
                cellDatas = new ExcelData[header.size()];
                for (int i = 0; i < header.size(); i++) {
                    if (MailMessageItem.RECIPIENT.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getEmail(), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.FIRSTNAME.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getFirstName(), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.LASTNAME.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getLastName(), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.VIEW_COUNT.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getLink(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (MailMessageItem.COUNTRY.equals(header.get(i))) {
                        cellDatas[i] = getExcelRows(new ExcelData(item.getClickCount() != null ? item.getClickCount() : 0, ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        cellDatas[i] = getExcelRows(new ExcelData("", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                list.add(cellDatas);
            }
            WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate Message Statistics excel report, exception: " + ex);
        }
        return null;
    }
}
