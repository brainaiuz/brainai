package com.edatasite.workforce.gwt.core.server.servlets.vcard;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import ezvcard.VCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Sher
 */
public class VCardTransferObject {
    private List<String> titles;
    private List<VCard> rows;
    private ArrayList<Integer> objectIDs;
    private ListingFilterParameter filterParameters;
    private boolean toBeContinued = false;
    private boolean titlesSet = false;
    private int step = 0;
    public static final int limitDB = 500;
    public static final int limitSOLR = 1000;

    public List<String> getTitles() {
        if (titles == null) {
            titles = new ArrayList<>();
        }
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public void setTitles(Map<String, List<String>> titles) {
        for (Map.Entry<String, List<String>> entry : titles.entrySet()) {
            for (String title : entry.getValue()) {
                getTitles().add(title);
            }
        }
    }

    public void setTitles(String... titles) {
        if (titles != null && titles.length > 0) {
            if (this.titles == null) {
                this.titles = new ArrayList<>();
            }
            this.titles.addAll(Arrays.asList(titles));
        }
    }

    public List<VCard> getRows() {
        if (rows == null) {
            rows = new ArrayList<>();
        }
        return rows;
    }

    public void setRows(List<VCard> rows) {
        this.rows = rows;
    }

    public List<VCard> getAll() {
        List<VCard> all = new ArrayList<>();
//        String[] titles = getTitles().toArray(new String[]{});
        int i = 0;
//        all.add(i++, titles);
        for (VCard row : getRows()) {
            all.add(i++, row);
        }
        return all;
    }

    public ListingFilterParameter getFilterParameters() {
        return filterParameters;
    }

    public void setFilterParameters(ListingFilterParameter filterParameters) {
        this.filterParameters = filterParameters;
    }

    public boolean isToBeContinued() {
        return toBeContinued;
    }

    public void setToBeContinued(boolean toBeContinued) {
        this.toBeContinued = toBeContinued;
    }

    public boolean isTitlesSet() {
        return titlesSet;
    }

    public void setTitlesSet(boolean titlesSet) {
        this.titlesSet = titlesSet;
    }

    public ArrayList<Integer> getObjectIDs() {
        if (objectIDs == null) {
            objectIDs = new ArrayList<>();
        }
        return objectIDs;
    }

    public void setObjectIDs(ArrayList<Integer> objectIDs) {
        this.objectIDs = objectIDs;
    }

    public int nextStep() {
        return step++;
    }

    public boolean isFirstStep() {
        return step == 1;
    }
}
