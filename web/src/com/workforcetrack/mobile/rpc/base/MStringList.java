package com.workforcetrack.mobile.rpc.base;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 02.05.12
 * Time: 19:26
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MStringList {

    private List<String> result;

    public MStringList() {
    }

    public List<String> getResult() {
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
