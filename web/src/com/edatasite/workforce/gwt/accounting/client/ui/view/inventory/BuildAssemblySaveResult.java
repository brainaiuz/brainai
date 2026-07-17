package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/27/12
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuildAssemblySaveResult implements Serializable {
    private Integer objectID;
    private ArrayList<String> notValidatedProducts;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public ArrayList<String> getNotValidatedProducts() {
        return notValidatedProducts;
    }

    public void setNotValidatedProducts(ArrayList<String> notValidatedProducts) {
        this.notValidatedProducts = notValidatedProducts;
    }
}
