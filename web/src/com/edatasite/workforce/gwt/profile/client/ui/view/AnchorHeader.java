package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.cellview.client.Header;

/**
 * Created with IntelliJ IDEA.
 * User: Fathulla
 * Date: 24.09.14
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
public class AnchorHeader extends Header<String> {
    /**
     * Construct a Header with a given {@link com.google.gwt.cell.client.Cell}.
     *
     * @param cell the {@link com.google.gwt.cell.client.Cell} responsible for rendering items in the header
     */
    private String value;
    public AnchorHeader(Cell cell) {
        super(cell);
    }

    @Override
    public String getValue() {
        return value != null ? value : "";
    }

    public void setValue(String value){
        this.value = value;
    }
}
