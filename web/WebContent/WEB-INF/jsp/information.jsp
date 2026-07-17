<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page import="com.edatasite.workforce.core.domain.EdsHostBasedSetting" %>
<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<% EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(request.getServerName());
    String version = SpringPropertiesUtil.getProperty("cssVersion");%>

<!doctype html>
<html class="no-js" lang="">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Information page</title>

    <script type="text/javascript">
        var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
        document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
    </script>

    <script type="text/javascript">
        try {
            var pageTracker = _gat._getTracker("UA-355982-15");
            pageTracker._trackPageview("/404.html?page=" + document.location.pathname + document.location.search + "&from=" + document.referrer);
        } catch(err) {
        }
    </script>

    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/css/materialize.css?v=<%=version%>">
    <link rel="stylesheet" href="/mainStyles/new-ui/login/css/main.css?v=<%=version%>"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/css/transition.css">
    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon">
    <style>
        body {
            background-color: white;
            box-sizing: content-box;
        }
    </style>

    <script type="text/javascript" language="JavaScript">
        function closeWindow() {
            window.setTimeout(function () {
                window.close();
            }, 5000);
        }
        var i = 5;
        var myInterval = setInterval(startVr, 1000);
        function startVr() {
            document.getElementById('txt').innerHTML = "Window will close automatically in " + i + " seconds.";
            if (i == 0) {
                clearInterval(myInterval);
            }
            i--;
        }
    </script>
</head>

<%
    Boolean fromGoogleGadget = (Boolean) request.getAttribute("fromGoogleGadget");
    if (fromGoogleGadget != null && fromGoogleGadget) {
%>
<body onload="closeWindow();">
    <%
    } else {
%>
<body>
<%
    }
%>
<script type="text/javascript">
    var WRInitTime = (new Date()).getTime();
</script>

<div class="pg_landing pg_landing--wizard pg_landing--wizard-modules">
    <div class="cp_colorpanel">
        <div class="cp_slider__frame cp_slider__frame--top">
            <svg width="454" height="198" viewBox="0 0 454 198" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                <g id="Canvas" transform="translate(-586 -84)">
                    <g id="Vector">
                        <use xlink:href="#path_slide-frame" transform="translate(586.5 84.5)" fill="#ffffff"></use>
                    </g>
                </g>
                <defs>
                    <path id="path_slide-frame" d="M 369.5 88L 0 0L 453.5 0L 453.5 197.5C 453.5 100.5 369.5 88 369.5 88Z"></path>
                </defs>
            </svg>
        </div>
        <div class="cp_slider__frame cp_slider__frame--bottom">
            <svg width="454" height="198" viewBox="0 0 454 198" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                <g id="Canvas" transform="translate(-586 -84)">
                    <g id="Vector">
                        <use xlink:href="#path_slide-frame" transform="translate(586.5 84.5)" fill="#ffffff"></use>
                    </g>
                </g>
                <defs>
                    <path id="path_slide-frame" d="M 369.5 88L 0 0L 453.5 0L 453.5 197.5C 453.5 100.5 369.5 88 369.5 88Z"></path>
                </defs>
            </svg>
        </div>
        <div class="cp_colorpanel__items-container">
            <div class="cp_colorpanel__items-innerbox" style="bottom:30%;">


            </div>
        </div>
    </div>

    <div class="pg_landing__container">
        <div class="pg_landing__header">
            <div class="pg_landing__logo ">
                <a href="#">
                    <img src="/mainStyles/new-ui/images/new-kpi-logo.svg?v=2" alt="logo"/>
                </a>
            </div>
        </div>
        <div class="pg_landing__main">
            <div class="pg_landing__main-innerbox">
                <div class="pg_landing__content">
                </div>
                <div class="pg_landing__sidebar">
                    <div class="cp_login">
                        <div class="cp_login__content">

                            <div class="cp_login__title">Information Page</div>
                            <div style="margin-top:50px;">
                                <%
                                    if (fromGoogleGadget != null && fromGoogleGadget) {
                                %>
                                <%=request.getAttribute("message")%>
                                <div id="txt"></div>
                                <form>
                                    <div class="cp_login__form-item">
                                        <input class="cp_login__submit elm_btn" type="submit"
                                               value="Finish Authorization" onclick="closeWindow();"/>
                                    </div>
                                </form>
                                <%
                                } else {
                                %>

                                <%=request.getAttribute("message")%>

                                <p><strong>E-mail:</strong>
                                    <a href="mailto:<%=hostSetting.getEmail()%>"style="color:#1f4f8f; text-decoration:none; font-size:15px;"><%=hostSetting.getEmail()%> </a>
                                </p>

                                <p><strong>Skype:</strong>
                                        <%=hostSetting.getSkype()%>
                                <p/>

                                <p><strong>Phone:</strong>
                                        <%=hostSetting.getPhone()%>
                                <p/>
                                <form action="/index.html" method="post">
                                    <div class="cp_login__form-item">
                                        <input class="cp_login__submit elm_btn" type="submit"
                                               value="Go to login page"/>
                                    </div>
                                </form>
                                <%
                                    }
                                %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/mainStyles/new-ui/login/js/jquery.min.js"></script>
<script src="/mainStyles/new-ui/login/js/slick.min.js"></script>
<script src="/mainStyles/new-ui/login/js/main.js"></script>
</body>
</html>
