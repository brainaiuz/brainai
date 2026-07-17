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
            overflow: hidden;
            position: relative;
        }
        .stripePayment__modal .modal-content {
            background-color: #6975e3;
            text-align: center;
            color: #fff;
        }
        .stripePayment__modal dl {
            margin: 20px 0 30px;
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
        .stripePayment__logo svg {
            width: 155px;
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

        }
        .stripePayment__powered {
            color: #afbdec;
        }


        /*THANKS STEP*/
        .stripePayment__logo {
            transform-origin: top;
            transition: transform .25s ease-in-out;
        }
        .stripePayment--thanks .stripePayment__logo,
        .stripePayment--thanks .stripePayment__amount {
            transform: scale(.45);
        }
        .stripePayment--thanks .stripePayment__footer {
            transition: transform .25s ease-in-out .25s;
            transform: translateY(100px);
        }
        .stripePayment--thanks .stripePayment__modal .btn--outline,
        .stripePayment--thanks .stripePayment__modal .stripePayment__amount {
            transition: opacity .25s ease-in-out .25s, transform .25s ease-in-out;
            opacity: 0;
        }
        .stripePayment__thanks {
            position: absolute;
            top: 30%;
            left: 0;
            right: 0;
            transform: scale(0);
            opacity: 0;
            transition: transform 0.25s ease-in-out .5s, opacity 0.25s  ease-in-out .5s;
        }
        .stripePayment--thanks .stripePayment__thanks {
            transform: scale(1);
            opacity: 1;
        }
        .stripePayment__thanks figure {
            margin: 0;
        }
        .stripePayment__thanks figure svg {
            width: 56px;
        }
        .stripePayment__thanks figcaption {
            font-size: 30px;
        }
        .stripePayment__thanks p {
            line-height: 1.8;
        }
    </style>


    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon">
</head>

<body class="stripePayment stripePayment--thanks" toast="bottom-right">
<!--Import jQuery before materialize.js-->
<script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
<%--<!--<script type="text/javascript" src="js/jquery.easing.1.3.js"></script>-->--%>
<script type="text/javascript" src="/mainStyles/new-ui/js/materialize-0.97.5.min.js"></script>

<div class="modal-holder active">
    <div class="modal stripePayment__modal">
        <div class="modal-wrapper">
            <div class="modal-content">
                <div class="stripePayment__logo">
                    <svg id="Layer_1" viewBox="0 0 468 222.5">
                        <style>
                            .st0{fill-rule:evenodd;clip-rule:evenodd;fill:#fff}
                        </style>
                        <path class="st0" d="M414 113.4c0-25.6-12.4-45.8-36.1-45.8-23.8 0-38.2 20.2-38.2 45.6 0 30.1 17 45.3 41.4 45.3 11.9 0 20.9-2.7 27.7-6.5v-20c-6.8 3.4-14.6 5.5-24.5 5.5-9.7 0-18.3-3.4-19.4-15.2h48.9c0-1.3.2-6.5.2-8.9zm-49.4-9.5c0-11.3 6.9-16 13.2-16 6.1 0 12.6 4.7 12.6 16h-25.8zM301.1 67.6c-9.8 0-16.1 4.6-19.6 7.8l-1.3-6.2h-22v116.6l25-5.3.1-28.3c3.6 2.6 8.9 6.3 17.7 6.3 17.9 0 34.2-14.4 34.2-46.1-.1-29-16.6-44.8-34.1-44.8zm-6 68.9c-5.9 0-9.4-2.1-11.8-4.7l-.1-37.1c2.6-2.9 6.2-4.9 11.9-4.9 9.1 0 15.4 10.2 15.4 23.3 0 13.4-6.2 23.4-15.4 23.4zM223.8 61.7l25.1-5.4V36l-25.1 5.3zM223.8 69.3h25.1v87.5h-25.1zM196.9 76.7l-1.6-7.4h-21.6v87.5h25V97.5c5.9-7.7 15.9-6.3 19-5.2v-23c-3.2-1.2-14.9-3.4-20.8 7.4zM146.9 47.6l-24.4 5.2-.1 80.1c0 14.8 11.1 25.7 25.9 25.7 8.2 0 14.2-1.5 17.5-3.3V135c-3.2 1.3-19 5.9-19-8.9V90.6h19V69.3h-19l.1-21.7zM79.3 94.7c0-3.9 3.2-5.4 8.5-5.4 7.6 0 17.2 2.3 24.8 6.4V72.2c-8.3-3.3-16.5-4.6-24.8-4.6C67.5 67.6 54 78.2 54 95.9c0 27.6 38 23.2 38 35.1 0 4.6-4 6.1-9.6 6.1-8.3 0-18.9-3.4-27.3-8v23.8c9.3 4 18.7 5.7 27.3 5.7 20.8 0 35.1-10.3 35.1-28.2-.1-29.8-38.2-24.5-38.2-35.7z"/>
                    </svg>
                </div>

                <dl class="stripePayment__amount">
                    <dt></dt>
                    <dd></dd>
                </dl>

                <%--<a href="#" class="btn btn-large btn--outline">
                    Pay Now
                </a>--%>

                <div class="stripePayment__footer">
                    <figure class="stripePayment__secure">
                        <svg width="11" height="14" xmlns="http://www.w3.org/2000/svg"><path d="M10.033 13.53V5.32h-.912V4.046C9.121 2.1 7.718.471 5.86.087a3.96 3.96 0 0 0-1.776.03C2.268.534.913 2.133.913 4.046V5.32H0v8.209h10.033zM2.738 4.045c0-.879.558-1.65 1.335-2.008.29-.134.607-.215.942-.215.3 0 .584.063.849.17.83.335 1.432 1.14 1.432 2.053V5.32H2.739V4.046zm1.33 7.976h-.146l.148-.807.406-2.208a1.108 1.108 0 0 1-.4-.39.999.999 0 0 1-.154-.54c0-.161.043-.315.108-.457.175-.368.545-.627.977-.627.44 0 .815.263.986.637.064.137.106.288.106.448 0 .251-.095.47-.24.647a1.09 1.09 0 0 1-.316.282l.307 1.667.25 1.348H4.067z" fill="#FFF" fill-rule="nonzero"/></svg>
                        <figcaption>
                            Guaranteed Safe &amp; Secure
                        </figcaption>
                    </figure>
                    <div class="stripePayment__powered">
                        Powered by Stripe
                    </div>
                </div>
                <div class="stripePayment__thanks">
                    <figure>
                        <c:if test="${error == null || empty error}">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1"
                                 stroke-linecap="round" stroke-linejoin="round" class="feather feather-check-circle">
                                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                                <path d="M22 4L12 14.01l-3-3"/>
                            </svg>
                        </c:if>
                        <figcaption>
                            <c:if test="${error != null && not empty error}">
                                Failed
                            </c:if>
                            <c:if test="${error == null || empty error}">
                                Thank You
                            </c:if>
                        </figcaption>
                    </figure>
                    <p>

                        <c:if test="${error != null && not empty error}">
                            <strong>An error occurred during the charge process</strong>
                            <br>
                            ${fn:escapeXml(error)}
                            <br>
                            Please contact support
                        </c:if>
                        <c:if test="${message != null && not empty message}">
                            ${message}
                        </c:if>

                    </p>
                </div>
            </div>
        </div>
    </div>
    <div class="lean-overlay file--stripe-charge" id="materialize-lean-overlay-1" style="z-index: 1002; display: block; opacity: 0.5;"></div>
</div>


</body>

</html>
