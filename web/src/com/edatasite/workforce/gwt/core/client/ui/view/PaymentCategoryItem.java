package com.edatasite.workforce.gwt.core.client.ui.view;


import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class PaymentCategoryItem extends Composite {
    private HTMLPanel itemPanel;
    private KpiCheckBox checkBox;
    private HTMLPanel textPanel;

    private Integer itemId;
    private String categoryName;
    private String categoryCode;
    private PaymentDeductionSelectItem item;
    private boolean isSelected = false;
    private Command changeValueCommand;

    public PaymentCategoryItem(PaymentDeductionSelectItem item, Command command) {
        this.item = item;
        this.itemId = item.getId();
        this.categoryName = item.getName();
        this.categoryCode = item.getCode();
        this.changeValueCommand = command;
        onInitialize();
    }

    public PaymentCategoryItem(PaymentDeductionSelectItem item) {
        this.item = item;
        this.itemId = item.getId();
        this.categoryName = item.getName();
        this.categoryCode = item.getCode();
        onInitialize();
    }

    private void onInitialize(){
        if (itemId == -1){
            textPanel = new HTMLPanel("span", "<em style = 'font-style: normal; font-weight: bold;'>" + categoryName + "</em>");
        }else{
            textPanel = new HTMLPanel("span", "<em style = 'font-style: normal;'>" + categoryName + "</em>");
        }
        itemPanel = new HTMLPanel("li","");
        itemPanel.setWidth("100%");
        checkBox = new KpiCheckBox();
        checkBox.addValueChangeHandler(valueChangeEvent -> {
            isSelected = checkBox.getValue();
            if (changeValueCommand != null) {
                changeValueCommand.execute();
            }
        });
//        itemPanel.getElement().getStyle().setProperty("fontStyle", "normal");

        GRow gRow = new GRow(new GColumn(GColumnEnum.COL_11, textPanel), new GColumn(GColumnEnum.COL_1,checkBox));
        gRow.setPaddingBottom(3);
        gRow.setPaddingTop(3);
        gRow.setPaddingLeft(6);
        gRow.setPaddingRight(6);
//        gRow.getElement().getStyle().setProperty("fontStyle", "normal");/**/
        itemPanel.add(gRow);
    }

    public HTMLPanel getWidget(){
        return itemPanel;
    }

    public Integer getIdIfCheckBoxHasSelected(){
        return checkBox.getValue() ? this.itemId : null;
    }


    public boolean isSelected(){
        return checkBox.getValue();
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public PaymentDeductionSelectItem getItem() {
        return item;
    }

    public void setItem(PaymentDeductionSelectItem item) {
        this.item = item;
    }

    public KpiCheckBox getCheckBox() {
        return checkBox;
    }

    public void chooseAsSelected(){
        checkBox.setValue(true);
        isSelected = true;
    }

    public void chooseAsUnSelected(){
        checkBox.setValue(false);
        isSelected = false;
    }

    public void chooseSelected(boolean selected) {
        checkBox.setValue(selected);
        isSelected = selected;
    }

    public Command getChangeValueCommand() {
        return changeValueCommand;
    }

    public void setChangeValueCommand(Command changeValueCommand) {
        this.changeValueCommand = changeValueCommand;
    }
}
