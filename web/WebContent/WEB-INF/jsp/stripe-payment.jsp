<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% String hostName = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName(); %>
<!doctype html>
<html class="no-js">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        Invoice Payment
    </title>

    <link rel="stylesheet"
          href="/mainStyles/new-ui/css/materialize.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>">
    <style>
        .stripePayment__modal {
            max-width: 326px;
        }

        .stripePayment__modal .modal-content {
            background-color: #6975e3;
            text-align: center;
            color: #fff;
        }

        .stripePayment__modal dl {
            margin: 30px 0;
        }

        .stripePayment__modal dt {
            font-size: 30px;
        }

        .stripePayment__modal dd {
            margin: 15px 0;
        }

        .stripePayment__modal .btn--outline {
            background-color: transparent;
            border-color: #fff;
            color: #fff;
        }

        .stripePayment__modal .btn--outline:focus,
        .stripePayment__modal .btn--outline:hover {
            color: inherit;
        }

        .stripePayment__logo {
            margin-top: 20px;
        }

        .stripePayment__secure {
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 40px 0;
        }

        .stripePayment__secure svg {
            margin-right: 10px;
        }

        .stripePayment__footer {
            color: #afbdec;
        }
    </style>


    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon">
</head>

<body class="stripePayment" toast="bottom-right">
<!--Import jQuery before materialize.js-->
<script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
<%--<!--<script type="text/javascript" src="js/jquery.easing.1.3.js"></script>-->--%>
<script type="text/javascript" src="/mainStyles/new-ui/js/materialize-0.97.5.min.js"></script>

<div class="modal-holder active">
    <div class="modal stripePayment__modal">
        <div class="modal-wrapper">
            <div class="modal-content">
                <div class="stripePayment__logo">
                    <img src="https://dev.kpi.com/mainStyles/new-ui/images/payment/stripe-big.png" alt="stripe logo"/>
                </div>

                <dl class="stripePayment__amount">
                    <dt>${currency} <fmt:formatNumber value="${totalininvoicecurrency}"
                                                      pattern=",##0.00" /></dt>
                    <dd>Amount to be paid</dd>
                </dl>
                <script src="https://checkout.stripe.com/checkout.js"></script>

                <%----%>
                <form action="/stripe-charge" method="POST" id="payment-form">
                    <input type="hidden" name="customtoken" value="${customtoken}"/>
                    <%--<script
                            src="https://checkout.stripe.com/checkout.js" class="stripe-button"
                            data-key="${stripe_public_key}"
                            data-amount="${totalininvoicecurrency*100}"
                            data-name="KPI"
                            data-currency="${currency}"
                            data-description="Payment for ${invoicenumber}"
                            data-image="https://stripe.com/img/documentation/checkout/marketplace.png"
                            data-locale="auto">
                    </script>--%>
                    <a href="#" class="btn btn-large btn--outline" id="customButton">
                        Pay Now
                    </a>
                </form>
                <script>
                    var handler = StripeCheckout.configure({
                        key: '${stripe_public_key}',
                        image: 'https://stripe.com/img/documentation/checkout/marketplace.png',
                        locale: 'auto',
                        token: function(token) {
                            // You can access the token ID with `token.id`.
                            // Get the token ID to your server-side code for use.

                            var form = document.getElementById('payment-form');
                            var tokenHiddenInput = document.createElement('input');
                            tokenHiddenInput.setAttribute('type', 'hidden');
                            tokenHiddenInput.setAttribute('name', 'stripeToken');
                            tokenHiddenInput.setAttribute('value', token.id);
                            form.appendChild(tokenHiddenInput);

                            var emailHiddenInput = document.createElement('input');
                            emailHiddenInput.setAttribute('type', 'hidden');
                            emailHiddenInput.setAttribute('name', 'stripeEmail');
                            emailHiddenInput.setAttribute('value', token.email);
                            form.appendChild(emailHiddenInput);

                            var typeHiddenInput = document.createElement('input');
                            typeHiddenInput.setAttribute('type', 'hidden');
                            typeHiddenInput.setAttribute('name', 'stripeTokenType');
                            typeHiddenInput.setAttribute('value', token.type);
                            form.appendChild(typeHiddenInput);

                            // Submit the form
                            form.submit();
                        }
                    });

                    document.getElementById('customButton').addEventListener('click', function(e) {
                        // Open Checkout with further options:
                        handler.open({
                            name: 'KPI',
                            description: 'Payment for ${invoicenumber}',
                            amount: ${totalininvoicecurrency*100},
                            currency: "${currency}"
                        });
                        e.preventDefault();
                    });

                    // Close Checkout on page navigation:
                    window.addEventListener('popstate', function() {
                        handler.close();

                    });
                </script>

                <figure class="stripePayment__secure">
                    <svg width="11" height="14" xmlns="http://www.w3.org/2000/svg">
                        <path d="M10.033 13.53V5.32h-.912V4.046C9.121 2.1 7.718.471 5.86.087a3.96 3.96 0 0 0-1.776.03C2.268.534.913 2.133.913 4.046V5.32H0v8.209h10.033zM2.738 4.045c0-.879.558-1.65 1.335-2.008.29-.134.607-.215.942-.215.3 0 .584.063.849.17.83.335 1.432 1.14 1.432 2.053V5.32H2.739V4.046zm1.33 7.976h-.146l.148-.807.406-2.208a1.108 1.108 0 0 1-.4-.39.999.999 0 0 1-.154-.54c0-.161.043-.315.108-.457.175-.368.545-.627.977-.627.44 0 .815.263.986.637.064.137.106.288.106.448 0 .251-.095.47-.24.647a1.09 1.09 0 0 1-.316.282l.307 1.667.25 1.348H4.067z"
                              fill="#FFF" fill-rule="nonzero"/>
                    </svg>
                    <figcaption>
                        Guaranteed Safe &amp; Secure
                    </figcaption>
                </figure>

                <div class="stripePayment__footer">
                    Powered by Stripe
                </div>
            </div>
        </div>
    </div>
    <div class="lean-overlay file--stripe-payment" id="materialize-lean-overlay-1" style="z-index: 1002; display: block; opacity: 0.5;"></div>
</div>


</body>

</html>