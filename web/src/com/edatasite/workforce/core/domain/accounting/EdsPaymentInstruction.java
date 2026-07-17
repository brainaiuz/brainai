package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * User: Sherzod
 * Date: 7/5/11
 * Time: 4:07 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "paymentInstruction")
public class EdsPaymentInstruction extends EdsObject {

    public static final Integer SALES_INVOICE_PAYMENT_INSTRUCTION = 0;
    public static final Integer SALES_QUOTE_TERMS_CONDITIONS = 1;
    //Purchase Order Terms and Conditions
    public static final Integer PURCHASE_ORDER_TERMS_CONDITIONS = 2;
    //Purchase Invoice Payment Instructions
    public static final Integer PURCHASE_INVOICE_PAYMENT_INSTRUCTION = 3;
    //Sales Invoice Introduction
    public static final Integer SALES_INVOICE_INTRODUCTION = 4;
    //Sales Quote Introduction
    public static final Integer SALES_QUOTE_INTRODUCTION = 5;
    //Sales Order Introduction
    public static final Integer SALES_ORDER_INTRODUCTION = 6;
    //Sales Order Payment Introduction
    public static final Integer SALES_ORDER_PAYMENT_INSTRUCTION = 7;
    //Request For Quote Introduction
    public static final Integer REQUEST_FOR_QUOTE_INTRODUCTION = 8;
    //Rfq Instructions
    public static final Integer REQUEST_FOR_QUOTE_INSTRUCTION = 9;




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Type(type = "text")
    private String text;

    private Integer type;// 0 = Sales Invoice Payment Instruction, 1 = Sales Quote Terms Conditions

    private Boolean deleted = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
