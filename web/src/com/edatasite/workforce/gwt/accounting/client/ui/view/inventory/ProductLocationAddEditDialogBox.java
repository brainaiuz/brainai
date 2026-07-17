package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;


import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseLocationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 16, 2010
 * Time: 3:18:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductLocationAddEditDialogBox extends KpiModal implements Constants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private Integer objectID;

    private TextBox aisleTxtBox;
    private TextBox rackTxtBox;
    private TextBox shelfTxtBox;
    private TextBox binTxtBox;

    private final Integer warehouseID;
    private Command updateLocationsProvider;

    public ProductLocationAddEditDialogBox(Integer warehouseID) {
        super();
        setCloseButton(true);
        this.warehouseID = warehouseID;
        initialize();
    }

    private void initialize() {

        setTitle(accountingStrings.locationDetails());

        //Initialize fields and add first section
        aisleTxtBox = new TextBox();
        rackTxtBox = new TextBox();
        shelfTxtBox = new TextBox();
        binTxtBox = new TextBox();

        aisleTxtBox.addStyleName(DEFAULT_WIDTH);
        rackTxtBox.addStyleName(DEFAULT_WIDTH);
        shelfTxtBox.addStyleName(DEFAULT_WIDTH);
        binTxtBox.addStyleName(DEFAULT_WIDTH);

        WfmForm wfmForm = new WfmForm();

        wfmForm.addField("Aisle", aisleTxtBox);
        wfmForm.addField("Rack", rackTxtBox);
        wfmForm.addField("Shelf", shelfTxtBox);
        wfmForm.addField("Bin", binTxtBox);

        WfmButton2 saveLocationButton = new WfmButton2(accountingStrings.saveLocation(), WfmButton2.BTN_PRIMARY);
        saveLocationButton.addClickHandler(event -> {
            WarehouseLocationItem productLocation = new WarehouseLocationItem();
//                productLocation.setObjectID(objectId);
            productLocation.setAisle(aisleTxtBox.getText());
            productLocation.setRack(rackTxtBox.getText());
            productLocation.setShelf(shelfTxtBox.getText());
            productLocation.setBin(binTxtBox.getText());
            productLocation.setWarehouseID(warehouseID);

            AccountingService.App.get().saveWarehouseLocation(productLocation, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    WfmWindow.error(wfmStrings.errorOccurredSavingChanges());
                }

                @Override
                public void onSuccess(Integer result) {
                    objectID = result;
                    updateLocationsProvider.execute();
                    close();
                }
            });
        });

        addButton(saveLocationButton);

        HorizontalPanel mainPanel = new HorizontalPanel();
        mainPanel.add(wfmForm);
        mainPanel.add(new Image(ProductLocationImageBundle.App.get().productLocation()));
        mainPanel.setSpacing(10);

        add(mainPanel);
        center();
    }

    public void setUpdateLocationsProvider(Command updateLocationsProvider) {
        this.updateLocationsProvider = updateLocationsProvider;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public interface ProductLocationImageBundle extends ClientBundle {

        @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/client/bundles/product/product_location.png")
        ImageResource productLocation();

        class App {
            public static ProductLocationImageBundle get() {
                return GWT.create(ProductLocationImageBundle.class);
            }
        }
    }
}
