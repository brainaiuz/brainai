package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 2011-07-17
 * Time: 8:14 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "formula")
public class EdsFormula extends EdsObject {

    public static final String MULTI_RANGE_RATE = "MULTI_RANGE_RATE";
    public static final String SIMPLE_RATE = "SIMPLE_RATE";

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "formula")
    private List<EdsMultiRangeRate> multiRangeRates = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "simplerate_id")
    private EdsSimpleRate simpleRate;

    @Column(name = "type")
    private String type = SIMPLE_RATE;// Formula contains from two types: MULTI_RANGE_RATE and SIMPLE_RATE.

    public List<EdsMultiRangeRate> getMultiRangeRates() {
        return multiRangeRates;
    }

    public void setMultiRangeRates(List<EdsMultiRangeRate> multiRangeRates) {
        this.multiRangeRates = multiRangeRates;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsSimpleRate getSimpleRate() {
        return simpleRate;
    }

    public void setSimpleRate(EdsSimpleRate simpleRate) {
        this.simpleRate = simpleRate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
