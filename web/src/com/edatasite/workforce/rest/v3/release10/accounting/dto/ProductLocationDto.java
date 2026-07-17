package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;

import java.math.BigDecimal;

public class ProductLocationDto extends DynamicDto {
    private Integer objectId;
    private BigDecimal qtyOnHand = BigDecimal.ZERO;
    private BigDecimal reorderPoint = BigDecimal.ONE;
    private IdName warehouse;

    public ProductLocationDto() {
    }

    public ProductLocationDto(Integer objectId, BigDecimal qtyOnHand, BigDecimal reorderPoint, IdName warehouse) {
        this.objectId = objectId;
        this.qtyOnHand = qtyOnHand;
        this.reorderPoint = reorderPoint;
        this.warehouse = warehouse;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public BigDecimal getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(BigDecimal qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public BigDecimal getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(BigDecimal reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public IdName getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(IdName warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductLocationDto)) return false;

        ProductLocationDto that = (ProductLocationDto) o;

        if (getObjectId() != null ? !getObjectId().equals(that.getObjectId()) : that.getObjectId() != null)
            return false;
        if (getQtyOnHand() != null ? !getQtyOnHand().equals(that.getQtyOnHand()) : that.getQtyOnHand() != null)
            return false;
        if (getReorderPoint() != null ? !getReorderPoint().equals(that.getReorderPoint()) : that.getReorderPoint() != null)
            return false;
        if (getWarehouse() != null ? !getWarehouse().equals(that.getWarehouse()) : that.getWarehouse() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getObjectId() != null ? getObjectId().hashCode() : 0;
        result = 31 * result + (getQtyOnHand() != null ? getQtyOnHand().hashCode() : 0);
        result = 31 * result + (getReorderPoint() != null ? getReorderPoint().hashCode() : 0);
        result = 31 * result + (getWarehouse() != null ? getWarehouse().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductLocationDto{" +
                "objectId=" + objectId +
                ", qtyOnHand=" + qtyOnHand +
                ", reorderPoint=" + reorderPoint +
                ", warehouse=" + warehouse +
                '}';
    }
}
