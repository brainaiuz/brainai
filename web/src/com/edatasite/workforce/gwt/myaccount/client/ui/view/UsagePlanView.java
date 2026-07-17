package com.edatasite.workforce.gwt.myaccount.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountStrings;
import com.edatasite.workforce.gwt.myaccount.client.rpc.MyAccountService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Dec 5, 2008
 * Time: 11:13:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class UsagePlanView extends BaseListView implements Constants {

    private static final MyAccountStrings myAccountStrings = MyAccountStrings.App.get();
    public static NumberFormat numberFormat = NumberFormat.getFormat("$#.##");

    final String cmd = "cmd";                       //_xclick-subscriptions;
    final String business = "business";             //sales@workforcetrack.com
    final String currency_code = "currency_code";   //USD
    final String amount = "amount";                 //25$
    final String item_name = "item_name";           //
    final String item_number = "item_number";       //
    final String custom = "custom";                 //
    final String taxX = "tax";                       //
    final String a3 = "a3";                          //5.00
    final String p3 = "p3";                          //1  (1,3,6,12 - Months)
    final String t3 = "t3";                          //M (Month)
    final String src = "src";                       //1,2,3 (Limit the number of billing cycles.)
    final String cancel_return = "cancel_return";
    final String returnT = "return";

    private WfmForm table;

    private Integer int_usagePlanID;

    public UsagePlanView(Integer int_usagePlanID) {
        super("usagePlanSummary", myAccountStrings.paymentHistory()); //usagePlanHistory
        this.int_usagePlanID = int_usagePlanID;
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        MyAccountService.App.get().getUsagePlan(int_usagePlanID, new AbstractAsyncCallback<UsagePlanItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(UsagePlanItem result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    doPreview(result);
                }
            }
        });
        return null;
    }

    private void doPreview(final UsagePlanItem item) {
        clear();

        table = new WfmForm();
        table.setWidth("100%");
        table.setHeight("200px");
        setHeight("200px");

        final String currencyValue = item.isCurrencyGBP() ? "GBP" : Utils.getCurrencyCODEbyHOST();

        HorizontalPanel hp = new HorizontalPanel();
        HorizontalPanel payHp = new HorizontalPanel();
        hp.setWidth("200px");
        add(table);

        if (!item.isPaid() && !item.isFree()) {
            /*HTMLPanel stripeBtn = new HTMLPanel("<form action=\"/your-server-side-code\" method=\"POST\">\n" +
                    "  <script\n" +
                    "    src=\"https://checkout.stripe.com/checkout.js\" class=\"stripe-button\"\n" +
                    "    data-key=\""+Utils.getStripePublicKey()+"\"\n" +
                    "    data-amount=\"999\"\n" +
                    "    data-name=\"KPI\"\n" +
                    "    data-description=\"Widget\"\n" +
                    "    data-image=\"https://stripe.com/img/documentation/checkout/marketplace.png\"\n" +
                    "    data-locale=\"auto\">\n" +
                    "  </script>\n" +
                    "</form>");*/

            WfmButton2 payBt = new WfmButton2(myAccountStrings.payNow());
            hp.add(payHp);
            payHp.add(payBt);
            hp.setCellHorizontalAlignment(payHp, HasHorizontalAlignment.ALIGN_RIGHT);
            payBt.addClickHandler(sender -> {
                String contextPath = "https://" + Utils.getPayPalLink() + "?";
                Utils.redirect(contextPath + cmd + "=_xclick-subscriptions&"
                        + business + "=" + Utils.getPayPalAccount() + "&"         //      sales@workforcetrack.com
                        + currency_code + "=" + currencyValue + "&"
                        + amount + "=" + /*numberFormat.format*/getNumberFormatWithBigDecimal(item.getTotalAmount()) + "&"  ////test "tot"
                        + taxX + "=" + /*numberFormat.format*/getNumberFormatWithBigDecimal(item.getTax()) + "&"  ////test "taxC"
                        + item_name + "=" + Utils.getProductName() + " - " + item.getService() + ".&"
                        + item_number + "=1&"
                        + a3 + "=" + /*numberFormat.format*/getNumberFormatWithBigDecimal(item.getTotalAmount()) + "&"       ////test "tot"
                        + p3 + "=" + item.getUsageMonth() + "&"
                        + t3 + "=M&"
                        + src + "=1&"
                        + (item.isPaypalStatus() ? "modify=2&" : "")
                        + custom + "=" + Utils.getEncryptedCompanyID() + SUBSCRIPTION_ADD + item.getObjectID() + "&"
                        + returnT + "=" + Utils.getHostURL() + "Myaccount.html" + "&"
                        + cancel_return + "=" + Utils.getHostURL() + "Myaccount.html");
            });
        }

        add(hp);
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private String getNumberFormatWithBigDecimal(double value) {
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    @Override
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
}
