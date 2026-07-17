package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentDataDto extends DynamicDto {
    private Integer id;
    private IdName paymentAccount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date paymentDate;
    private BigDecimal amount;

    public PaymentDataDto() {
    }

    public PaymentDataDto(Integer id, IdName paymentAccount, Date paymentDate, BigDecimal amount) {
        this.id = id;
        this.paymentAccount = paymentAccount;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public IdName getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(IdName paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentDataDto)) return false;

        PaymentDataDto that = (PaymentDataDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (paymentAccount != null ? !paymentAccount.equals(that.paymentAccount) : that.paymentAccount != null)
            return false;
        if (paymentDate != null ? !paymentDate.equals(that.paymentDate) : that.paymentDate != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (paymentAccount != null ? paymentAccount.hashCode() : 0);
        result = 31 * result + (paymentDate != null ? paymentDate.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PaymentDataDto{" +
                "id=" + id +
                ", paymentAccount=" + paymentAccount +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                '}';
    }
}
