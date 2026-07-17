package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 21/11/12
 * Time: 15:52
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "subsidiaryproduct")
public class EdsSubsidiaryProduct extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "uniqNumber")
    private String uniqNumber;

    @Column(name = "productName")
    private String productName;

    @Column(name = "companyId")
    private Integer companyId;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getUniqNumber() {
        return uniqNumber;
    }

    public void setUniqNumber(String uniqNumber) {
        this.uniqNumber = uniqNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
