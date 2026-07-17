package com.edatasite.workforce.gwt.core.server.rabbitmq.service;

import com.edatasite.shared.massmailler.MassMailerData;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.Consignment;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.ConsignmentItem;
import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.*;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.InOutQueue;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.QueueListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.sender.*;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import de.novanic.eventservice.client.event.domain.DefaultDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("rabbitMQService")
public class RabbitMQServiceImpl implements RabbitMQService {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQServiceImpl.class);

    @Autowired
    @Lazy
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private EmailFetchSender emailFetchSender;
    @Autowired
    private EmailConvertToCaseSender emailConvertToCaseSender;
    @Autowired
    private EmailTrackerRelationSender emailTrackerRelationSender;
    @Autowired
    private HandleStocksByFifoSender handleStocksByFifoSender;
    @Autowired
    private StocksByFifoSender stocksByFifoSender;
    @Autowired
    private ListPanelSettingsSender listPanelSettingsSender;
    @Autowired
    private UserRequestTrackingSender userRequestTrackingSender;
    @Autowired
    private MassMailSender massMailSender;
    @Autowired
    private MultiCurrencySender multiCurrencySender;
    @Autowired
    private ExchangeRateSender exchangeRateSender;
    @Autowired
    private SubsidiariesSender subsidiariesSender;
    @Autowired
    private SubsidariesProductSender subsidariesProductSender;
    @Autowired
    private SubsidiariesConsignmentSender subsidiariesConsignmentSender;
    @Autowired
    private InterCompanySalesSender interCompanySalesSender;
    @Autowired
    private MergeContactAndTrackerSender mergeContactAndTrackerSender;
    @Autowired
    private StealContactsSender stealContactsSender;
    @Autowired
    private LeadCompanySettingsSender leadCompanySettingsSender;
    @Autowired
    private EndOfYearProcessSender endOfYearProcessSender;
    @Autowired
    private WebPushNotificationsSender webPushNotificationsSender;
    @Autowired
    private PushServerEventSender pushServerEventSender;
    @Autowired
    private SinglePayrunSender singlePayrunSender;
    @Autowired
    private FixedAssetsDeprecationSender fixedAssetsDeprecationSender;


    public void emailFetchMQ(Integer emailSettingsId) {
        emailFetchSender.sendMessage(emailSettingsId, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    public void emailConvertToCaseMQ(String emailId) {
        emailConvertToCaseSender.sendMessage(emailId, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    public void emailTrackerRelationMQ(String emailId) {
        emailTrackerRelationSender.sendMessage(emailId, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    @Override
    public void outItemByFifoMQ(FifoItem fifoItem) {
        handleStocksByFifoSender.sendOutItemMessage(fifoItem, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    @Override
    public void sendToQueue(FIFODataMQ dataMQ, InOutQueue queue, QueueListener listener) {
        stocksByFifoSender.sendToQueue(dataMQ, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase(), queue, listener);
    }

    @Override
    public void deleteOutItemByFifoMQ(FifoItem fifoItem) {
        handleStocksByFifoSender.sendOutItemDeleteMessage(fifoItem, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    @Override
    public void inItemByFifoMQ(FifoItem fifoItem) {
        handleStocksByFifoSender.sendInItemMessage(fifoItem, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    @Override
    public void deleteInItemByFifoMQ(FifoItem fifoItem) {
        handleStocksByFifoSender.sendInItemDeleteMessage(fifoItem, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    @Override
    public void listPanelSettingsMQ(ListPanelItemMQ listPanelItemMQ) {
        listPanelSettingsSender.sendMessage(listPanelItemMQ, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    @Override
    public void userRequestTrackingMQ(UserRequestItemMQ userRequestItemMQ) {
        userRequestTrackingSender.sendMessage(userRequestItemMQ, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void emailSending(MassMailerData emailMailerData) {
        massMailSender.sendMessage(emailMailerData, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    /**
     * <b>... This is method subsidiarie company add multi currency send to parent company ...</b>
     * <br/>
     * <b>... Write by Dilshod.Tadjiev ...</b>
     * <br/>
     * <b>... Create Date 16/11/2012 20:05 ...</b>
     *
     * @param companyId
     * @param currencyIdList
     * @param sendCompanyId
     * @param sendCompanyCluster
     */
    @Override
    public void sendMultiCurrency(MultiCurrencyItemMQ itemMQ, Integer sendCompanyId, String sendCompanyCluster) {
        multiCurrencySender.sendMessage(itemMQ, sendCompanyId, sendCompanyCluster);
    }

    /**
     * <b>... This is method multi currency exchange rate send all subsidiaries company ...</b>
     * <br/>
     * <b>... Write by Dilshod.Tadjiev ...</b>
     * <br/>
     * <b>... Create Date 16/11/2012 20:17 ...</b>
     *
     * @param year
     * @param month
     * @param companyExchangeRateMap
     */
    @Override
    public void sendExchangeRate(ExchangeRateItemMQ exchangeRateItemMQ, Integer sendCompanyId, String sendCompanyCluster) {
        exchangeRateSender.sendMessage(exchangeRateItemMQ, sendCompanyId, sendCompanyCluster);
    }

    /**
     * <b>... This is method multicompany management group send subsidiaries info...</b>
     * <br/>
     * <b>... Write by Dilshod.Tadjiev ...</b>
     * <br/>
     * <b>... Create Date 16/11/2012 20:31 ...</b>
     *
     * @param subsidiaries
     * @param currentSubsidiary
     * @param parentCompanyId
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void sendSubsidiaries(List<SelectItem> subsidiaries, SelectItem currentSubsidiary, Integer parentCompanyId) {
        List<SelectItem> currenctSubsidiaryCompany = new ArrayList<>();
        currenctSubsidiaryCompany.add(currentSubsidiary);
        Map<Integer, String> subsidiariesClusterTypeMap = globalAuthJdbcSpringManager.getSubsidiariesCompanyClusterType(parentCompanyId);
        log.info(" Subsidiary Company List = " + ServerUtils.getSelectItemIdAsCommaDelimeted(subsidiaries.toArray(new SelectItem[]{})));
        log.info(" Send to subsidiary company to all companyId  = " + subsidiariesClusterTypeMap.keySet());
        for (Integer companyId : subsidiariesClusterTypeMap.keySet()) {
            if (currentSubsidiary.getId().equals(companyId)) {
                log.info("Send currenct company  =" + companyId);
                subsidiariesSender.sendMessage(subsidiaries, companyId, subsidiariesClusterTypeMap.get(companyId));
            } else if (!companyId.equals(parentCompanyId)) {
                log.info("Send old add company  =" + companyId);
                subsidiariesSender.sendMessage(currenctSubsidiaryCompany, companyId, subsidiariesClusterTypeMap.get(companyId));
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void sendSubsidiariesProduct(List<SelectItem> productListItem, Integer parentCompanyId) {
        Map<Integer, String> subsidiariesClusterTypeMap = globalAuthJdbcSpringManager.getSubsidiariesCompanyClusterType(parentCompanyId);
        for (Integer componuId : subsidiariesClusterTypeMap.keySet()) {
            subsidariesProductSender.sendMessage(productListItem, componuId, subsidiariesClusterTypeMap.get(componuId));
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void sendSubsidiariesConsignment(Consignment consignment, Integer parentCompanyID) {

        if (consignment.getItems() != null && consignment.getItems().length > 0) {
            Set<Integer> companyIds = new HashSet<>();
            for (ConsignmentItem item : consignment.getItems()) {

                if (item.getFromCompanyID() != null && item.getToCompanyID() != null) {
                    if (!parentCompanyID.equals(item.getFromCompanyID())) {
                        companyIds.add(item.getFromCompanyID());
                    }

                    if (!parentCompanyID.equals(item.getToCompanyID())) {
                        companyIds.add(item.getToCompanyID());
                    }
                }
            }
            System.out.println("Count of company Ids: " + companyIds.size());
            for (Integer companyID : companyIds) {
                subsidiariesConsignmentSender.sendMessage(consignment, companyID, globalAuthJdbcSpringManager.getCompanyClusterType(companyID));
            }
        }
    }

    @Override
    public void sendInterCompanySales(InterCompanyDataMQ interCompanyData, Integer companyID) {
        if (companyID != null) {
            String clusterType = globalAuthJdbcSpringManager.getCompanyClusterType(companyID);
            if (clusterType != null && !clusterType.isEmpty()) {
                interCompanySalesSender.sendMessage(interCompanyData, companyID, clusterType);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void mergeContactAndTracker(Map<Integer, String> data, Integer companyID, String clusterType) {
        mergeContactAndTrackerSender.sendMessage(data, companyID, clusterType);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void stealContacts(String fileName) {
        stealContactsSender.sendMessage(fileName, 1, SecurityContext.getInstance().getDatabase());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void sendCompanySettingsUpdate(CompanyData companyData, Integer companyId) {
        leadCompanySettingsSender.sendMessage(companyData, companyId, globalAuthJdbcSpringManager.getCompanyClusterType(companyId));
    }
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void endOfYearProcess(Integer emailSettingId, Integer companyId) {
        endOfYearProcessSender.sendMessage(emailSettingId, companyId, globalAuthJdbcSpringManager.getCompanyClusterType(companyId));
    }

    @Override
    public void sendWebPushNotification(WebSocketServerObject data) {
        webPushNotificationsSender.sendMessage(data, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    @Deprecated
    @Override
    public void sendPushServerEvent(DefaultDomain data) {
        pushServerEventSender.sendMessage(data, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    public void singlePayslipGenerateMQ(PayslipItemFilter itemFilter) {
        singlePayrunSender.sendMessage(itemFilter, SecurityContext.getInstance().getStaticUserID(), SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
    }

    @Override
    public void fixedAssetsDeprecationMQ(DeprecationItemMQ item) {
        fixedAssetsDeprecationSender.sendMessage(item, Integer.valueOf(item.getCompanyId()), item.getDatabase());
    }
}
