package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class InventoryStockInformationDto extends DynamicDto {
    @NotNull(message = "assetAccount is required")
    private IdCode assetAccount;
    @NotNull(message = "asOf date is required.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date asOf = new Date();
    private boolean trackSerialnumber = false;
    private boolean batchSerialnumber = false;
    private boolean trackBatches = false;
    @NotNull(message = "productLocations is required")
    private List<ProductLocationDto> productLocations;

    public InventoryStockInformationDto() {
    }

    public IdCode getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(IdCode assetAccount) {
        this.assetAccount = assetAccount;
    }

    public Date getAsOf() {
        return asOf;
    }

    public void setAsOf(Date asOf) {
        this.asOf = asOf;
    }

    public boolean isTrackSerialnumber() {
        return trackSerialnumber;
    }

    public void setTrackSerialnumber(boolean trackSerialnumber) {
        this.trackSerialnumber = trackSerialnumber;
    }

    public boolean isBatchSerialnumber() {
        return batchSerialnumber;
    }

    public void setBatchSerialnumber(boolean batchSerialnumber) {
        this.batchSerialnumber = batchSerialnumber;
    }

    public boolean isTrackBatches() {
        return trackBatches;
    }

    public void setTrackBatches(boolean trackBatches) {
        this.trackBatches = trackBatches;
    }

    public List<ProductLocationDto> getProductLocations() {
        return productLocations;
    }

    public void setProductLocations(List<ProductLocationDto> productLocations) {
        this.productLocations = productLocations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryStockInformationDto)) return false;

        InventoryStockInformationDto that = (InventoryStockInformationDto) o;

        if (isTrackSerialnumber() != that.isTrackSerialnumber()) return false;
        if (isBatchSerialnumber() != that.isBatchSerialnumber()) return false;
        if (isTrackBatches() != that.isTrackBatches()) return false;
        if (getAssetAccount() != null ? !getAssetAccount().equals(that.getAssetAccount()) : that.getAssetAccount() != null)
            return false;
        if (getAsOf() != null ? !getAsOf().equals(that.getAsOf()) : that.getAsOf() != null) return false;
        if (getProductLocations() != null ? !getProductLocations().equals(that.getProductLocations()) : that.getProductLocations() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getAssetAccount() != null ? getAssetAccount().hashCode() : 0;
        result = 31 * result + (getAsOf() != null ? getAsOf().hashCode() : 0);
        result = 31 * result + (isTrackSerialnumber() ? 1 : 0);
        result = 31 * result + (isBatchSerialnumber() ? 1 : 0);
        result = 31 * result + (isTrackBatches() ? 1 : 0);
        result = 31 * result + (getProductLocations() != null ? getProductLocations().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InventoryStockInformationDto{" +
                "assetAccount=" + assetAccount +
                ", asOf=" + asOf +
                ", trackSerialnumber=" + trackSerialnumber +
                ", batchSerialnumber=" + batchSerialnumber +
                ", trackBatches=" + trackBatches +
                ", productLocations=" + productLocations +
                '}';
    }
}
