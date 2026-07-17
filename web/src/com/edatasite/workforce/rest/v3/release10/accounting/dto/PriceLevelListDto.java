package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

import java.util.List;

public class PriceLevelListDto {
    private Integer id;
    private String name;
    private String type;

    private String plCase;
    private Double percent;
    private String operationType;

    private List<PriceLevelPPDto> priceLevelPPs;
    private List<PriceLevelBBDto> priceLevelBBs;
    private List<ItemDto> clients;
    private List<ItemDto> clientTypes;

    private ItemDto currency;
    private String quickBookPriceLevelID;
    private String quickBookEditSequence;
    private String externalGUID;

    public PriceLevelListDto() {
    }

    public PriceLevelListDto(Integer id, String name, String type, String plCase, Double percent, String operationType, List<PriceLevelPPDto> priceLevelPPs, List<PriceLevelBBDto> priceLevelBBs, List<ItemDto> clients, List<ItemDto> clientTypes, ItemDto currency, String quickBookPriceLevelID, String quickBookEditSequence, String externalGUID) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.plCase = plCase;
        this.percent = percent;
        this.operationType = operationType;
        this.priceLevelPPs = priceLevelPPs;
        this.priceLevelBBs = priceLevelBBs;
        this.clients = clients;
        this.clientTypes = clientTypes;
        this.currency = currency;
        this.quickBookPriceLevelID = quickBookPriceLevelID;
        this.quickBookEditSequence = quickBookEditSequence;
        this.externalGUID = externalGUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlCase() {
        return plCase;
    }

    public void setPlCase(String plCase) {
        this.plCase = plCase;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public List<PriceLevelPPDto> getPriceLevelPPs() {
        return priceLevelPPs;
    }

    public void setPriceLevelPPs(List<PriceLevelPPDto> priceLevelPPs) {
        this.priceLevelPPs = priceLevelPPs;
    }

    public List<PriceLevelBBDto> getPriceLevelBBs() {
        return priceLevelBBs;
    }

    public void setPriceLevelBBs(List<PriceLevelBBDto> priceLevelBBs) {
        this.priceLevelBBs = priceLevelBBs;
    }

    public List<ItemDto> getClients() {
        return clients;
    }

    public void setClients(List<ItemDto> clients) {
        this.clients = clients;
    }

    public List<ItemDto> getClientTypes() {
        return clientTypes;
    }

    public void setClientTypes(List<ItemDto> clientTypes) {
        this.clientTypes = clientTypes;
    }

    public ItemDto getCurrency() {
        return currency;
    }

    public void setCurrency(ItemDto currency) {
        this.currency = currency;
    }

    public String getQuickBookPriceLevelID() {
        return quickBookPriceLevelID;
    }

    public void setQuickBookPriceLevelID(String quickBookPriceLevelID) {
        this.quickBookPriceLevelID = quickBookPriceLevelID;
    }

    public String getQuickBookEditSequence() {
        return quickBookEditSequence;
    }

    public void setQuickBookEditSequence(String quickBookEditSequence) {
        this.quickBookEditSequence = quickBookEditSequence;
    }

    public String getExternalGUID() {
        return externalGUID;
    }

    public void setExternalGUID(String externalGUID) {
        this.externalGUID = externalGUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceLevelListDto)) return false;

        PriceLevelListDto that = (PriceLevelListDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (plCase != null ? !plCase.equals(that.plCase) : that.plCase != null) return false;
        if (percent != null ? !percent.equals(that.percent) : that.percent != null) return false;
        if (operationType != null ? !operationType.equals(that.operationType) : that.operationType != null)
            return false;
        if (priceLevelPPs != null ? !priceLevelPPs.equals(that.priceLevelPPs) : that.priceLevelPPs != null)
            return false;
        if (priceLevelBBs != null ? !priceLevelBBs.equals(that.priceLevelBBs) : that.priceLevelBBs != null)
            return false;
        if (clients != null ? !clients.equals(that.clients) : that.clients != null) return false;
        if (clientTypes != null ? !clientTypes.equals(that.clientTypes) : that.clientTypes != null) return false;
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) return false;
        if (quickBookPriceLevelID != null ? !quickBookPriceLevelID.equals(that.quickBookPriceLevelID) : that.quickBookPriceLevelID != null)
            return false;
        if (quickBookEditSequence != null ? !quickBookEditSequence.equals(that.quickBookEditSequence) : that.quickBookEditSequence != null)
            return false;
        if (externalGUID != null ? !externalGUID.equals(that.externalGUID) : that.externalGUID != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (plCase != null ? plCase.hashCode() : 0);
        result = 31 * result + (percent != null ? percent.hashCode() : 0);
        result = 31 * result + (operationType != null ? operationType.hashCode() : 0);
        result = 31 * result + (priceLevelPPs != null ? priceLevelPPs.hashCode() : 0);
        result = 31 * result + (priceLevelBBs != null ? priceLevelBBs.hashCode() : 0);
        result = 31 * result + (clients != null ? clients.hashCode() : 0);
        result = 31 * result + (clientTypes != null ? clientTypes.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (quickBookPriceLevelID != null ? quickBookPriceLevelID.hashCode() : 0);
        result = 31 * result + (quickBookEditSequence != null ? quickBookEditSequence.hashCode() : 0);
        result = 31 * result + (externalGUID != null ? externalGUID.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PriceLevelListDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", plCase='" + plCase + '\'' +
                ", percent=" + percent +
                ", operationType='" + operationType + '\'' +
                ", priceLevelPPs=" + priceLevelPPs +
                ", priceLevelBBs=" + priceLevelBBs +
                ", clients=" + clients +
                ", clientTypes=" + clientTypes +
                ", currency=" + currency +
                ", quickBookPriceLevelID='" + quickBookPriceLevelID + '\'' +
                ", quickBookEditSequence='" + quickBookEditSequence + '\'' +
                ", externalGUID='" + externalGUID + '\'' +
                '}';
    }
}
