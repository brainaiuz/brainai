package com.edatasite.workforce.gwt.core.server.gwd;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/22/13
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class GWDData {

    private String sku;
    private String title;
    private BigDecimal price;
    private String fname;
    private String lname;
    private String email;
    private String phone;
    private String ref;
    private String creationdate;
    private BigDecimal quantity;
    private String mid;


    public String getSku() {
        return sku;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getFullName() {
        return this.fname != null ? fname : "" + this.lname != null ? lname : "";
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRef() {
        return ref;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getMid() {
        return mid;
    }

    public boolean validateForCreateNewCustomer() {
        return (getFname() != null && !"".equals(getFname())) || (getLname() != null && !"".equals(getLname())) || (getEmail() != null && !"".equals(getEmail()));
    }
}
