package com.edatasite.workforce.gwt.core.server.rpc;

import java.io.Serializable;
import java.util.List;

/**
 * User: iabdullo
 * Date: 24.11.14 14:21
 */
public class ListBoard<T> implements Serializable {
    String name; // status name;
    Integer total;
    List<T> cards; // grouped by status

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getCards() {
        return cards;
    }

    public void setCards(List<T> cards) {
        this.cards = cards;
    }
}
