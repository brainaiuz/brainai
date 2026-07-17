package com.workforcetrack.mobile.rpc.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/7/11
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "results")
public class MIntegerList {

    List<Integer> result;

    public MIntegerList() {

    }

    public MIntegerList(List<Integer> items) {
        this.result = items;
    }

    public MIntegerList(Integer[] items) {
        this.result = Arrays.asList(items);
    }

    public List<Integer> getResult() {
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    public void setResult(List<Integer> result) {
        this.result = result;
    }
}
