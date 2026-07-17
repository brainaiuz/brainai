<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/styleKPI.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/master.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/styleJF.css">
    <link href="//app.kpi.com/customisation/kpi.com/images/favicon.ico" rel="shortcut icon">
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/bootstrap.min.css">
    <!--[if lt IE 9]>
    <script type="text/javascript" src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <!-- <meta http-equiv="X-UA-Compatible" content="chrome=1" /> -->
    <!-- for ie -->

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/fix-ie.css"/>
    <![endif]-->

    <script type="text/javascript" src="/customisation/preprod.kpi.com/scripts/jquery.js"></script>
    <script type="text/javascript" src="/customisation/preprod.kpi.com/scripts/common.js"></script>

    <SCRIPT type=text/javascript src="/customisation/kpi.com/jquery.placeholder.min.js"></SCRIPT>
    <link href="/customisation/preprod.kpi.com/customize.css" rel="stylesheet">
    <link href="/customisation/preprod.kpi.com/font-awesome.min.css" rel="stylesheet">

    <tiles:insertAttribute name="style" ignore="true"/>

    <% String hostName1 = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName();
        if (hostName1 != null && hostName1.contains("kg.kpi.com")) { %>
    <c:set var="analytics_code" value="UA-59981695-23"/>
    <%} else { %>
    <c:set var="analytics_code" value="UA-355982-15"/>
    <% } %>

    <script type="text/javascript">
        (function (i, s, o, g, r, a, m) {
            i['GoogleAnalyticsObject'] = r;
            i[r] = i[r] || function () {
                    (i[r].q = i[r].q || []).push(arguments)
                }, i[r].l = 1 * new Date();
            a = s.createElement(o), m = s.getElementsByTagName(o)[0];
            a.async = 1;
            a.src = g;
            m.parentNode.insertBefore(a, m)
        })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');
        ga('create', '<c:out value="${analytics_code}"/>', 'auto');
        ga('send', 'pageview');
    </script>
    <% if (hostName1 != null && (hostName1.contains("app.kpi.com")) || hostName1.contains("aws.kpi.com")) { %>
    <script type="text/javascript">
        setTimeout(function () {
            var a = document.createElement("script");
            var b = document.getElementsByTagName("script")[0];
            a.src = document.location.protocol + "//script.crazyegg.com/pages/scripts/0043/1807.js?" + Math.floor(new Date().getTime() / 3600000);
            a.async = true;
            a.type = "text/javascript";
            b.parentNode.insertBefore(a, b)
        }, 1);
    </script>
    <% } %>
</head>

<body>

<!-- begin wrapper -->
<div id="wrapper">

    <!--begin header-->
    <header id="header">
        <div class="topHeader">
            <div class="container">
                <div class="sitePhone">
                    <em>Contact Us:</em>
                    <span>(UK) +44 (0) 2070961245</span>
                    <span>(US) +1 646 844 3330</span>
                    <span>(UAE) +971 4 424 3033</span>
                </div>
                <div class="socials">
                    <a href="https://www.facebook.com/KpiServices?ref=tn_tnmn" class="fa fa-facebook" target="_blank"></a>
                    <a href="https://twitter.com/kpi_online" class="fa fa-twitter" target="_blank"></a>
                    <a href="https://www.linkedin.com/company/260375?trk=tyah&trkInfo=idx%3A1-1-1%2CtarId%3A1424337700540%2Ctas%3Akpi.com"
                       class="fa fa-linkedin" target="_blank"></a>
                    <a href="https://plus.google.com/u/0/111235257208481299823/posts" class="fa fa-google-plus"
                       target="_blank"></a>
                    <a href="https://www.youtube.com/channel/UC611go2FxyIh50rVzhgghMg" class="fa fa-youtube"
                       target="_blank"></a>
                </div>
            </div>
        </div>
        <div class="container midHeader">
            <div class="ZoneIn">
                <!-- begin logo site -->


                <div class="langBar" id="langbar" style="display: none;">
                    <ul>
                        <li><a href="/" class="langEN">English</a></li>
                        <li><a href="//kpi.com/o-que-o-workforcetrack-representa-e-por-que-precisamos-dele"
                               class="langPo">Portuguese</a></li>
                        <li><a href="http://www.kpi.com.ru/" class="langRU">Russian</a></li>
                        <li><a href="//kpi.com/kpi-com-nedir-ve-neden-ihtiyacim-var" class="langTU">Turkey</a></li>
                    </ul>
                </div>

                <a href="//www.kpi.com" id="logo">
                    <img src="/customisation/preprod.kpi.com/images/logo.png">
                </a>
                <!-- end logo site -->

            </div>
            <style>
                /* Start "Style" */
                hgroup {
                    border-bottom: 1px solid #dcdcdc;
                    background: #eff4f7;
                    color: #b1b4b6;
                    padding: 10px 0;
                }

                hgroup > div {
                    position: relative;
                    width: 964px;
                    margin: 0 auto;
                }

                header .ZoneIn {
                    padding: 25px 0;
                }

                a#logo {
                    top: 10px;
                }

                .ZoneIn .appActs .btn-1 {
                    background-image: none;
                    border: 1px solid transparent;
                    white-space: nowrap;
                    padding: 6px 12px;
                    font-size: 14px;
                    line-height: 1.42857143;
                    border-radius: 4px;
                    color: #fff;
                    background-color: #00acee;
                    border-color: #009ad5;
                    height: auto;
                    box-shadow: none;
                    font-family: 'Roboto', sans-serif;
                    min-width: 116px;
                    text-align: center;
                    vertical-align: middle;
                    -webkit-box-sizing: border-box;
                    -moz-box-sizing: border-box;
                    box-sizing: border-box;
                }

                .ZoneIn .appActs .btn-1:hover {
                    color: #fff;
                    background-color: #0087bb;
                    border-color: #006d97;
                }

                .ZoneIn .appActs {
                    margin: 0 0 0 35px;
                    clear: left;
                }

                .ZoneIn nav.menu-main-menu-container {
                    margin: 5px 0 0;
                }

                header nav > ul > li > a {
                    font-family: 'Roboto', sans-serif;
                    font-size: 14px;
                    text-transform: uppercase;
                    padding: 5px 0;
                }

                header .sitePhone span {
                    color: #b1b4b6;
                    font-family: 'Roboto', sans-serif;
                    font-size: 14px;
                    font-weight: normal;
                    margin-right: 25px;
                }

                .langBar {
                    height: 30px;
                    font-size: 12px;
                    line-height: 1.5;
                    border-radius: 3px;
                }

                .langBar ul {
                    padding: 10px 10px 9px;
                    font-size: 12px;
                    line-height: 1.5;
                    border-radius: 3px;
                    background-position: 100% 15px;
                    background-size: 11px 5px;
                }

                .langBar ul li {
                    margin: 0 0 15px;
                }

                .langBar:hover ul {
                    height: auto;
                }

                .langBar ul li:last-child {
                    margin: 0;
                }

                /* End "Style" */
            </style>
        </div>
    </header>
    <!--END header-->

    <!--begin #cover-->
    <div id="cover">

        <!--begin #content-->
        <div id="content" class="full-main">

            <tiles:insertAttribute name="body" ignore="false"/>
            <!--begin footer-->

            <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
            <script src="/customisation/preprod.kpi.com/scripts/jquery.min.js"></script>
            <!-- Include all compiled plugins (below), or include individual files as needed -->
            <script src="/customisation/preprod.kpi.com/scripts/bootstrap.min.js"></script>

            <!--END footer-->

        </div>
        <!--END #content-->

    </div>
    <!--END #cover-->


</div>
<!-- END #wrapper -->

<tiles:insertAttribute name="script" ignore="true"/>

<%
    //Recaptcha script
    Boolean enableCaptcha = request.getAttribute("captcha") != null ? ((Boolean) request.getAttribute("captcha")) : false;
    String captchaTheme = request.getAttribute("captchaTheme") != null ? ((String) request.getAttribute("captchaTheme")) : "white";

    if (enableCaptcha) {%>
<script type="text/javascript">
    var recaptchaCallback = function () {
        //console.log('recaptcha is ready'); // not showing
        grecaptcha.render("recaptcha", {
            sitekey: '<%=EdsContextParams.getRecaptchaPublicKey()%>',
            callback: function () {
                console.log('recaptcha callback');
            }
        });
    }
</script>
<script src="https://www.google.com/recaptcha/api.js?onload=recaptchaCallback&render=explicit" async defer></script>

<% } %>

<%if ((request.getRequestURI().endsWith("welcomePage.jsp") || (request.getRequestURI().endsWith("freeTrial.jsp"))) && isRussian) {%>
<!-- Google Code for Free Trial Sign up Conversion Page-->
<script type="text/javascript">
    /* <![CDATA[ */
    var google_conversion_id = 952099144;
    var google_conversion_language = "en";
    var google_conversion_format = "2";
    var google_conversion_color = "ffffff";
    var google_conversion_label = "LheeCLj90QMQyML_xQM";
    var google_conversion_value = 0;
    /*] ]> */
</script>
<script type="text/javascript" src="//www.googleadservices.com/pagead/conversion.js">
</script>
<noscript>
    <div style="display:inline;">
        <img height="1" width="1" style="border-style:none;" alt=""
             src="//www.googleadservices.com/pagead/conversion/952099144/?label=LheeCLj90QMQyML_xQM&guid=ON&script=0"/>
    </div>
</noscript>
<% } %>

</body>
</html>
