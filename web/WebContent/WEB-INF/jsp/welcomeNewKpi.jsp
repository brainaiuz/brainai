<%@ page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: Fatxulla Nigmatjonov
  Date: 11/12/14
  Time: 7:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<tiles:insertDefinition name="frontEndLayoutNew">
<tiles:putAttribute name="title">
    ${productName} | <fmt:message key="welcome.ThankYou"/>
</tiles:putAttribute>
<tiles:putAttribute name="style">

</tiles:putAttribute>
<tiles:putAttribute name="body">
    <script type="text/javascript">

        setTimeout("redirect()", 10000);

        function getCookie(c_name) {
            var i, x, y, ARRcookies = document.cookie.split(";");
            for (i = 0; i < ARRcookies.length; i++) {
                x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
                y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
                x = x.replace(/^\s+|\s+$/g, "");
                if (x == c_name) {
                    return unescape(y);
                }
            }
        }

        function redirect() {
            var section = getCookie('SECTION_HTML');
            var path = "<%=Constants.DEFAULT_SECTION%>.html";
            //alert(section);
            if (section != null && section.match("html") != null) {
                path = section;
            }
            window.location = '/' + path;
        }
        var isInIFrame = (window.location != window.parent.location) ? true : false;
        if (isInIFrame) {
            parent.location = document.location;
        }
    </script>

    <script type="text/javascript">
        if (!window.mstag) mstag = {loadTag:function () {
        }, time:(new Date()).getTime()};

    </script>
    <div class="breadcrumb">
        <ol class="container">
            <li><a href="http://new.kpi.com">Home</a></li>
            <li class="active">THANK YOU FOR REGISTERING AT kpi.com</li>
        </ol>
    </div>

    <div class="container">



        <article class="post-9492 page type-page status-publish hentry" id="post-9492">
            <h1 class="page_title">THANK YOU FOR REGISTERING AT KPI.COM</h1>

            <div class="row products_review bottomField">


                <div class="col-md-12 main-content"> <!-- Begin of main content area -->


                    <p><br></p>
                    <ul class="after-register">
                        <li>You will be redirected into your kpi.com account in 10 seconds or please click <a href="#">here</a> if you will not be automatically redirected</li>
                        <li>Please follow the link we have sent, in order to set your account password (it may take a few minutes to arrive)</li>
                        <li>Check your junk or spam folder if you do not see the activation email appear shortly</li>
                    </ul>
                    <hr>
                    <h3>Questions? Contact Us…</h3>
                    <div class="row" id="contact-us-info">
                        <div class="col-md-6">
                            <dl class="dl-horizontal">
                                <dt>E-mail:</dt>
                                <dd><a href="${email != null ? email : 'support@kpi.com'}">${email != null ? email : 'support@kpi.com'} </a></dd>
                                <dt>Skype:</dt>
                                <dd>kpi.com</dd>
                                <dt>Phone:</dt>
                                <dd>(UK) &nbsp; &nbsp;+44 (0) 207 096 1245<br>
                                    (US) &nbsp; &nbsp;+1 646 844 3330
                                    <br>
                                    (UAE) &nbsp;+971 4 424 3033<p></p>
                                </dd>
                            </dl>
                        </div>
                        <div class="col-md-6">
                            Please feel free to contact us via email, phone or Skype/any other IM you like. Would you like to contact us while on site? That’s easy! We integrated LiveHelp system straight into kpi.com that enables you to chat with one of our operations with one click. Just click on LiveHelp icon.<br><br>
                            Kind Regards, kpi.com Support Team<p></p>
                        </div>
                    </div>

                </div><!-- End of main content area -->
            </div><!-- #row products_review -->

        </article><!-- #post-## -->


    </div> <!-- End of main container -->

</tiles:putAttribute>
<tiles:putAttribute name="script">

    <script
            id="mstag_tops" type="text/javascript"
            src="//flex.atdmt.com/mstag/site/3386f3c4-86c8-40ba-8017-0b8c19c36e07/mstag.js"></script>
    <script type="text/javascript"> mstag.loadTag("conversion",
            {cp:"5050", dedup:"1"})</script>
    <noscript>
        <iframe
                src="//flex.atdmt.com/mstag/tag/3386f3c4-86c8-40ba-8017-0b8c19c36e07/conversion.html?cp=5050&dedup=1"
                frameborder="0" scrolling="no" width="1" height="1"
                style="visibility:hidden;display:none"></iframe>
    </noscript>


    <!-- Google Code for Lead Conversion Page -->
    <script type="text/javascript">
        /* <![CDATA[ */
        var google_conversion_id = 962237096;
        var google_conversion_language = "en";
        var google_conversion_format = "2";
        var google_conversion_color = "ffffff";
        var google_conversion_label = "yhlRCPiBgAMQqKXqygM";
        var google_conversion_value = 0;
        /* ] ]> */
    </script>
    <script type="text/javascript"
            src="//www.googleadservices.com/pagead/conversion.js">
    </script>
    <noscript>
        <div style="display:inline;">
            <img height="1" width="1" style="border-style:none;" alt=""
                 src="//www.googleadservices.com/pagead/conversion/962237096/?label=yhlRCPiBgAMQqKXqygM&guid=ON&script=0"/>
        </div>
    </noscript>

    <!-- Google Code for Sign up Conversion Page -->
    <script type="text/javascript">
        /* <![CDATA[ */
        var google_conversion_id = 952099144;
        var google_conversion_language = "en";
        var google_conversion_format = "2";
        var google_conversion_color = "ffffff";
        var google_conversion_label = "LoIoCPDEzAMQyML_xQM";
        var google_conversion_value = 0;
        /* ] ]> */
    </script>
    <script type="text/javascript" src="http://www.googleadservices.com/pagead/conversion.js">
    </script>
    <noscript>
        <div style="display:inline;">
            <img height="1" width="1" style="border-style:none;" alt=""
                 src="http://www.googleadservices.com/pagead/conversion/952099144/?label=LoIoCPDEzAMQyML_xQM&guid=ON&script=0"/>
        </div>
    </noscript>

    <!-- Google Code for Sign UP Conversion Page - JAY'S ADWORDS CONVERSIONS PAGE DO NOT TOUCH -->
    <script type="text/javascript">
        /* <![CDATA[ */
        var google_conversion_id = 1008859916;
        var google_conversion_language = "en";
        var google_conversion_format = "3";
        var google_conversion_color = "ffffff";
        var google_conversion_label = "iU8ECLTZ6AIQjPaH4QM";
        var google_conversion_value = 1;
        /* ] ]> */
    </script>
    <script type="text/javascript" src="http://www.googleadservices.com/pagead/conversion.js">
    </script>
    <noscript>
        <div style="display:inline;">
            <img height="1" width="1" style="border-style:none;" alt=""
                 src="http://www.googleadservices.com/pagead/conversion/1008859916/?value=1&label=iU8ECLTZ6AIQjPaH4QM&guid=ON&script=0"/>
        </div>
    </noscript>

    <!-- Google Code for Jay - sign up Conversion Page -->
    <script type="text/javascript">
        /* <![CDATA[ */
        var google_conversion_id = 1068037200;
        var google_conversion_language = "en";
        var google_conversion_format = "3";
        var google_conversion_color = "ffffff";
        var google_conversion_label = "e1PuCLD89AMQ0Oij_QM";
        var google_conversion_value = 10;
        /* ] ]> */
    </script>
    <script type="text/javascript" src="//www.googleadservices.com/pagead/conversion.js">
    </script>
    <noscript>
        <div style="display:inline;">
            <img height="1" width="1" style="border-style:none;" alt=""
                 src="//www.googleadservices.com/pagead/conversion/1068037200/?value=10&label=e1PuCLD89AMQ0Oij_QM&guid=ON&script=0"/>
        </div>
    </noscript>


    <%--JAY'S RETARGETER CONVERSIONS PAGE DO NOT TOUCH--%>
    <script type="text/javascript">
        adroll_adv_id = "RQZHNVO6DRHTJBBTUZLX7K";
        adroll_pix_id = "LXE4P2LX6RAJDPHCA2CTDV";
        (function () {
            var oldonload = window.onload;
            window.onload = function () {
                __adroll_loaded = true;
                var scr = document.createElement("script");
                var host = (("https:" == document.location.protocol) ? "https://s.adroll.com" : "http://a.adroll.com");
                scr.setAttribute('async', 'true');
                scr.type = "text/javascript";
                scr.src = host + "/j/roundtrip.js";
                ((document.getElementsByTagName('head') || [null])[0] ||
                        document.getElementsByTagName('script')[0].parentNode).appendChild(scr);
                if (oldonload) {
                    oldonload()
                }
            };
        }());
    </script>
    <!--New Google Analytics script-->
    <script type="text/javascript">
        var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
        document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
    </script>
    <script type="text/javascript">
        try {
            var pageTracker = _gat._getTracker("UA-355982-15");
            pageTracker._setDomainName(".kpi.com");
            pageTracker._trackPageview();
        } catch (err) {
        }</script>
    <!--New Google Analytics script-->
    <%--TODO: move to https, it was commented as we implemented https--%>
    <%--<img src="http://ad.retargeter.com/seg?add=132528" width="1" height="1"/>--%>
    <!-- begin adBrite, Sign-ups tracking --><img border="0" hspace="0" vspace="0" width="1" height="1"
    src="//stats.adbrite.com/stats/stats.gif?_uid=1075056&_pid=1"/><!-- end adBrite, Sign-ups tracking -->

</tiles:putAttribute>


</tiles:insertDefinition>