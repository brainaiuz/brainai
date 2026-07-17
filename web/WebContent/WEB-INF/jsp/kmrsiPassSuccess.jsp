<%--
  User: Ilhombek
  Date: 4/2/13
  Time: 4:13 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.2">

    <link rel="stylesheet" href="/wfp/templates/kmrsi/css/custom.css" type="text/css"/>
    <!--[if lte IE 7]>
    <link rel="stylesheet" href="/wfp/templates/kmrsi/css/ie7-andLeft.css" type="text/css"/><![endif]-->
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/wfp/templates/kmrsi/css/ie8-andLeft.css" type="text/css"/><![endif]-->
    <!--[if gte IE 9]>
    <style type="text/css">
    </style>
    <![endif]-->

    <!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
  <script>window.jQuery || document.write('<script src="js/vendor/jquery-1.8.3.min.js"><\/script>')</script>
  <script src="js/vendor/modernizr-2.6.2.min.js"></script> -->

    <title>KMRSI Password Reminder</title>
</head>
<body>
<!--[if lt IE 7]>
<p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to
    improve your experience.</p>
<![endif]-->

<div id="wrapper" class="page-login">

    <!--start #HEADER-->
    <div id="header">
        <a href="http://production.kmrsi.com"><img id="logo-site-1" src="/wfp/templates/kmrsi/img/logo_site-1.png"
                                                   alt="KEVIN-MICHAEL-REED-STUDIO LOGO"/></a>
    </div>
    <!--END #header-->


    <div id="content">
        <form action="/forgot/forgotPassword.html" class="loginForm-cover" method="post">
            <div class="tabs-nav">
                <strong class="tab">Password Reminder Success</strong>
            </div>
            <div class="loginForm tabs-content group">

                <p>Your password has been reset. An email has been sent to your registered email with new password.</p>

                <table style="margin-bottom:15px;">
                    <tr>
                        <td style="vertical-align:middle;padding-bottom: 10px;">
                            <a href="http://production.kmrsi.com">Go back to Sign In Page</a>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </div>
    <!--END div#content-->

</div>
<!-- End #wrapper -->

<!--start #FOOTER-->
<div id="footer">
    <div class="c-box  group">
        <address>
            KEVIN MICHAEL REED STUDIO INC <br/>
            70 COMMERCIAL ST, STE 203 <br/>
            BROOKLYN, NEW YORK 11222
            <p>
                888-4-KMRNYC <br/>
                NY 212.947.6972 <br/>
                LA 323.417.0200
            </p>

            <a href="www.kevinmichaelreed.com">www.KevinMichaelReed.com</a> <br/>
            blog: <a href="www.exposingfashion.com">www.ExposingFashion.com</a>
        </address>

        <a href="http://production.kmrsi.com"><img id="logo-site-2" src="/wfp/templates/kmrsi/img/logo_site-2.png"
                                                   alt="Production.KMRSI.com"/></a>
    </div>
</div>
<!--END #footer-->


</body>
</html>