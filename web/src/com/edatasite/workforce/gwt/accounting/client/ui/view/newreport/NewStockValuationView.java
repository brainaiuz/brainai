package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.StockValuation.NewStockValuation;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by admin on 20.09.2014.
 */
public class NewStockValuationView extends View implements Constants, FittedContent {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private boolean isProductView = false;
    private boolean isGrnGdnView = false;
    private Integer grnGdnId;
    private Integer productId;
    private Integer warehouseId;
    private boolean isFromReportingSinks = false;

    public NewStockValuationView(boolean isFromReportingSinks, Integer productId) {
        super("stockValuation");
        setDescription(property.getPlural(accountingStrings.stockValuation()));
        this.isFromReportingSinks = isFromReportingSinks;
        if (productId != null) {
            this.productId = productId;
        }
    }

    public NewStockValuationView(Integer productId, Integer warehouseId, boolean isProductView) {
        super("stockValuation");
        setDescription(property.getPlural(accountingStrings.stockValuation()));
        this.isProductView = isProductView;
        this.productId = productId;
        this.warehouseId = warehouseId;
    }

    public NewStockValuationView(Integer productId, Integer warehouseId, boolean isGrnGdnView, Integer grnGdnId) {
        super("stockValuation");
        setDescription(property.getPlural(accountingStrings.stockValuation()));
        this.isGrnGdnView = isGrnGdnView;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.grnGdnId = grnGdnId;
        this.isFromReportingSinks = false;
        this.isProductView = false;
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        final NewStockValuation[] newStockValuation = new NewStockValuation[1];
        if (isFromReportingSinks) {
            if (productId == null) {
                newStockValuation[0] = new NewStockValuation();
            } else {
                newStockValuation[0] = new NewStockValuation(productId);
            }
            add(newStockValuation[0]);
        } else if (isProductView) {
            newStockValuation[0] = new NewStockValuation(productId, warehouseId, true);
            add(newStockValuation[0]);
        } else if (isGrnGdnView) {
            LoadingPanel.loading(true);
            QuoteService.App.get().getShippingDate(grnGdnId, new AbstractAsyncCallback<ShippingData>() {
                @Override
                public void failure(Throwable throwable) {
                    failure(throwable);
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(ShippingData result) {
                    LoadingPanel.loading(false);
                    newStockValuation[0] = new NewStockValuation(productId, warehouseId, false, result.getShippingDate());
                    add(newStockValuation[0]);
                }
            });

        } else {
            newStockValuation[0] = new NewStockValuation();
            add(newStockValuation[0]);
        }

        return null;
    }

    @Override
    public FlowPanel getHelpContainer() {
        if (this.isFromReportingSinks) {
            FlowPanel panel = new FlowPanel();
            panel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
            Anchor anchor = new Anchor();
            anchor.setText("+" + wfmStrings.moreReports());
            anchor.addClickHandler((clickEvent -> {
                if (Utils.hasPermission(PermissionConstants.REPORTING_SYSTEM) || Utils.hasPermission(PermissionConstants.REPORTING_MAIN_MENU)) {
                    Utils.openURL(Utils.getHostURL() + Constants.ACCOUTING_REPORT);
                } else {
                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                }
            }));
            anchor.getElement().getStyle().setPaddingLeft(10, Style.Unit.PX);
            panel.add(anchor);
            return panel;
        }
        return null;
    }

    @Override
    public String getIconStyle() {
        return "accountMark report-list";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reInitialize() {
        Utils.frame_affix_fixed_top();
    }

    public void asyncOnInitialize() {
        asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(Throwable reason) {
                //BillboardPanel.get().hide();
                Info.show(wfmStrings.failedToDownloadCodeForThisWidget() + "&nbsp;(" + reason + ")", Info.Type.WARNING);
            }

            public void success(Widget result) {
                //BillboardPanel.get().hide();
            }
        });
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        if (!isFromReportingSinks) {
            MainLayout.get().makeFrameContainerHaveTabsStyle(false);
            MainLayout.get().getFrameContainer().addStyleName("has-tabs file--NewStockValuationView");
        }
        MainLayout.get().mutateBodyWithFrameContent2(true);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
//        MainLayout.get().getFrameContainer().removeStyleName("pageContent_reporting");
        MainLayout.get().getFrameContainer().removeStyleName("file--NewStockValuationView");
        MainLayout.get().mutateBodyWithFrameContent2(false);
        MainLayout.get().considerBodyHasPageOpers(true);
        MainLayout.get().considerBodyHasFittedContent(true);
    }

    @Override
    public String getPropertyCode() {
        return "stockValuation";
    }
}
