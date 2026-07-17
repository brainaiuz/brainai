package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * User: Sher(sherali.pirnafaosov@gmail.com)
 * Date: 9/4/12
 * Time: 10:54 AM
 * Finnet Technologies
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "appraisal_rate")
public class EdsAppraisalRate extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(precision = 4, scale = 1)
    private BigDecimal rating;

    @Column(name = "name")
    private String name;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
}
