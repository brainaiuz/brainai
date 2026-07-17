package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.multiquoteconverter.ConvertedQuotesDto;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 13-Aug-2010
 * Time: 14:56:17
 */
@Transactional
public class SaleInvoiceCustomEventListenerImpl extends CustomBusinessEventListenerAdapter implements Constants, AccountingConstants {
    public static WfmType<EdsSaleInvoice> TYPE = new WfmType<>(EventTypes.saleInvoiceCustomEventListener);
    public static String EVENT_SALEINVOICE_ADD_TO_SOLR = "SALEINVOICE_ADD_TO_SOLR";
    public static String EVENT_SALEINVOICE_DELETE_FROM_SOLR = "SALEINVOICE_DELETE_FROM_SOLR";
    public static String EVENT_SALEINVOICE_SEND_MESSAGE_TO_CLIENT = "EVENT_SALEINVOICE_SEND_MESSAGE_TO_CLIENT";
    public static String EVENT_UPDATE_SOQ_INVOICING_DATA = "EVENT_UPDATE_SOQ_INVOICING_DATA";

    @Autowired
    private SolrManager solrManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private SaleInvoiceSolrComponent saleInvoiceSolrComponent;
    @Autowired
    private ExecutorService executor;


    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_SALEINVOICE_ADD_TO_SOLR.equals(event.getEventType())) {
            onAddEvent(event);
        } else if (EVENT_SALEINVOICE_DELETE_FROM_SOLR.equals(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EVENT_SALEINVOICE_SEND_MESSAGE_TO_CLIENT.equals(event.getEventType())) {
            sendMessageToClient(event);
        } else if (EVENT_UPDATE_SOQ_INVOICING_DATA.equals(event.getEventType())) {
            updateSOQInvoiceData(event);
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsSaleInvoice saleInvoice = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());
        try {
            saleInvoiceSolrComponent.index(saleInvoice);
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (IOException | SolrServerException | InterruptedException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        try {
            solrManager.removeSaleInvoice(event.getEntityID(), event.getCompanyId());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (IOException | SolrServerException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void sendMessageToClient(EdsBusinessEvent event) {
        try {
            EdsSaleInvoice saleInvoice = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());
            EdsUser user = userManager.get(event.getSourceID());
            MessageItem item = new MessageItem();
            item.setSubject("Sales Invoice");
            item.setSendCopyToMe(true);
            item.setClient(true);
            item.setSenderID(user.getObjectID());
            item.setContactId(saleInvoice.getContact().getObjectID());
            item.setEmailTemplateID(emailTemplateManager.getDefaultEmailTemplateByCategory(SALES_INVOICE_CATEGORY).getObjectID());
            item.setInvoiceID(saleInvoice.getObjectID());

            EntityToEmailTemplate toTemplate = new EntityToEmailTemplate();
            toTemplate.setEntityId(saleInvoice.getObjectID());
            toTemplate.setEntityType(SALES_INVOICE_CATEGORY);
            toTemplate.setMailReceiverId(item.getContactId());
            toTemplate.setEmailTemplateId(item.getEmailTemplateID());

            EmailTemplateItem templateItem = emailTemplateService.generateEmailTemplateData(toTemplate, user.getObjectID());
            item.setMailContent(templateItem.getMessageHTML());

            invoiceService.sendToClient(item);

            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    void updateSOQInvoiceData(EdsBusinessEvent event) {
        try {
            EdsSaleInvoice saleInvoice = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());

            if (StringUtils.isNotBlank(event.getCustomStringField())) {
                ConvertedQuotesDto dto = new Gson().fromJson(event.getCustomStringField(), ConvertedQuotesDto.class);

                if (dto != null) {

                    if (CollectionUtils.isEmpty(dto.getQuoteIds())) {
                        dto.setQuoteIds(saleInvoice.getConvertedQuotes().stream().map(q -> q.getObjectID()).collect(Collectors.toCollection(ArrayList::new)));
                    }

                    if (!CollectionUtils.isEmpty(dto.getQuoteIds())) {

                        int THRESHOLD = 100;
                        int pageCount = dto.getQuoteIds().size() / THRESHOLD + (dto.getQuoteIds().size() % THRESHOLD > 0 ? 1 : 0);

                        for (int i = 0; i < pageCount; i++) {
                            int offset = i * THRESHOLD;
                            int toIndex = i * THRESHOLD + ((i == pageCount - 1) ? dto.getQuoteIds().size() % THRESHOLD : THRESHOLD);
                            String sessionId = ServerSecurityContext.getInstance().getDatabase() + "$" + ServerSecurityContext.getInstance().getCompanyId();
                            executor.submit(new UpdateSOQTask(saleInvoice.getObjectID(), new ConvertedQuotesDto(dto.getMethodKey(), dto.getProgInvoiceType(), dto.getQuoteIds().subList(offset, toIndex)), sessionId));
                        }
                    }
                }
            }
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    class UpdateSOQTask implements Runnable {
        private Integer saleInvoiceId;
        private ConvertedQuotesDto dto;
        private String sessionId;

        public UpdateSOQTask(Integer saleInvoiceId, ConvertedQuotesDto dto, String sessionId) {
            this.saleInvoiceId = saleInvoiceId;
            this.dto = dto;
            this.sessionId = sessionId;
        }

        @Override
        public void run() {
            ServerSecurityContext.getInstance().setSessionId(sessionId);

            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT) && invoiceManager.checkIsBatchConvert(saleInvoiceId)) {
                String methodKey = dto.getMethodKey();

                EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(saleInvoiceId);
                if (saleInvoice.isDeleted() || saleInvoice.getStatus() != null && REVERSED.equals(saleInvoice.getStatus().getCode())) {
                    methodKey = "deleteSI";
                }
                invoiceManager.updateConvertedQuotes(saleInvoiceId, dto.getQuoteIds(), methodKey);

                Thread solrThread = new Thread(() -> {
                    try {
                        ServerSecurityContext.getInstance().setSessionId(sessionId);
                        solrManager.indexAddSaleQuote(quoteManager.getSaleQuotesByIds(StringUtils.join(dto.getQuoteIds(), ",")), Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()), null);
                        ServerSecurityContext.getInstance().setSessionId(null);
                    } catch (IOException | SolrServerException e) {
                        e.printStackTrace();
                    }
                });
                solrThread.start();
            } else {
                invoiceServiceLocal.updateSalesQuoteProgressInvoicingData(saleInvoiceId, dto);
            }
            ServerSecurityContext.getInstance().setSessionId(null);
        }
    }
}
