package com.edatasite.workforce.gwt.core.server.rabbitmq.service;

import com.edatasite.shared.massmailler.MassMailerData;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.Consignment;
import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.*;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.InOutQueue;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.QueueListener;
import de.novanic.eventservice.client.event.domain.DefaultDomain;

import java.util.List;
import java.util.Map;

public interface RabbitMQService {
    void emailFetchMQ(Integer emailSettingsId);

    void emailConvertToCaseMQ(String emailId);

    void emailTrackerRelationMQ(String emailId);

    void outItemByFifoMQ(FifoItem fifoItem);

    void sendToQueue(FIFODataMQ dataMQ, InOutQueue queue, QueueListener listener);

    void deleteOutItemByFifoMQ(FifoItem fifoItem);

    void inItemByFifoMQ(FifoItem fifoItem);

    void deleteInItemByFifoMQ(FifoItem fifoItem);

    void listPanelSettingsMQ(ListPanelItemMQ listPanelItemMQ);

    void userRequestTrackingMQ(UserRequestItemMQ userRequestItemMQ);

    void emailSending(MassMailerData emailMaillerData);

    void sendMultiCurrency(MultiCurrencyItemMQ itemMQ, Integer sendCompanyId, String sendCompanyCluster);

    void sendExchangeRate(ExchangeRateItemMQ exchangeRateItemMQ, Integer sendCompanyId, String sendCompanyCluster);

    void sendSubsidiaries(List<SelectItem> subsidiaries, SelectItem currentSubsidiary, Integer parentCompanyId);

    void sendSubsidiariesProduct(List<SelectItem> productListItem, Integer parentCompanyId);

    void sendSubsidiariesConsignment(Consignment consignment, Integer parentCompanyID);

    void sendInterCompanySales(InterCompanyDataMQ interCompanyData, Integer companyID);

    void mergeContactAndTracker(Map<Integer, String> data, Integer companyID, String clusterType);

    void stealContacts(String fileName);

    void sendCompanySettingsUpdate(CompanyData companyData, Integer companyId);

    void endOfYearProcess(Integer emailSettingId, Integer companyId);

    void sendWebPushNotification(WebSocketServerObject data);

    void sendPushServerEvent(DefaultDomain data);

    void singlePayslipGenerateMQ(PayslipItemFilter itemFilter);

    void fixedAssetsDeprecationMQ(DeprecationItemMQ item);
}
