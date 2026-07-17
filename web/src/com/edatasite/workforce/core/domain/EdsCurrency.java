package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: said
 * Date: May 14, 2007
 * Time: 4:59:31 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "currency")
public class EdsCurrency extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "symbol")
    private String symbol;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private Set<EdsCountry> countries = new HashSet<>();

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "parentid")
    private Integer parentid;

    @Column(name = "frname")
    private String frname;

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Set<EdsCountry> getCountries() {
        return countries;
    }

    public void setCountries(Set<EdsCountry> countries) {
        this.countries = countries;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public String getFrname() {
        return frname;
    }

    public void setFrname(String frname) {
        this.frname = frname;
    }

    public static Map<String, EdsCurrency> listToMap(List<EdsCurrency> list) {
        Map<String, EdsCurrency> currencyMap = new HashMap<>();
        if (list != null && list.size() > 0) {
            for (EdsCurrency currency : list) {
                currencyMap.put(currency.getName().toLowerCase(), currency);
            }
        }
        return currencyMap;
    }

    public CurrencyItem createCurrencyItem(){
        return new CurrencyItem(getObjectID(), getName(), getSymbol(), getFullName());
    }
}
