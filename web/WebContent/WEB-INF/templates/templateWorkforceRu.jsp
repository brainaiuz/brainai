<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Yandex.Metrika counter -->
    <%--<script type="text/javascript"> (function (d, w, c) {
        (w[c] = w[c] || []).push(function () {
            try {
                w.yaCounter45824055 = new Ya.Metrika2({id: 45824055, clickmap: true, trackLinks: true, accurateTrackBounce: true, webvisor: true, trackHash: true});
            } catch (e) {
            }
        });
        var n = d.getElementsByTagName("script")[0], s = d.createElement("script"), f = function () {
            n.parentNode.insertBefore(s, n);
        };
        s.type = "text/javascript";
        s.async = true;
        s.src = "https://mc.yandex.ru/metrika/tag.js";
        if (w.opera == "[object Opera]") {
            d.addEventListener("DOMContentLoaded", f, false);
        } else {
            f();
        }
    })(document, window, "yandex_metrika_callbacks2"); </script>
    <noscript>
        <div><img src="https://mc.yandex.ru/watch/45824055" style="position:absolute; left:-9999px;" alt=""/></div>
    </noscript>--%>
    <!-- /Yandex.Metrika counter -->

    <meta content="text/html; charset=utf-8" http-equiv="Content-Typec:">
    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>

    <% if (hostName.contains("aws")) {%>
    <meta name="robots" content="noindex,nofollow">
    <%}%>

    <link rel="shortcut icon" href="/customisation/kpi.com/images/favicon.ico" type="image/x-icon"/>

    <link type="text/css" rel="stylesheet" media="all" href="//www.${helpHost}/sites/default/themes/wft_ru/style.css">
    <link type="text/css" rel="stylesheet" media="all" href="//www.${helpHost}/modules/system/defaults.css">
    <link type="text/css" rel="stylesheet" media="all" href="//www.${helpHost}/sites/default/modules/nice_menus/nice_menus.css">
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/style.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/master.css"/>


    <tiles:insertAttribute name="style" ignore="true"/>
    <link href="/loginpage/kpi/afterdeletedshell.css" media="all" rel="stylesheet" type="text/css"/>
    <%----------------------------------------%>
    <!--[if lte IE 7]>
    <link rel="stylesheet" href="/loginpage/kpi/ie7-andLeft.css" type="text/css"/>
    <![endif]-->
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/loginpage/kpi/ie8-andLeft.css" type="text/css"/>
    <![endif]-->
    <script src="//www.${helpHost}/sites/default/themes/wft_ru/jquery-1.3.2.min.js"></script>
    <%
        //Recaptcha script
        Boolean enableCaptcha = request.getAttribute("captcha") != null ? ((Boolean) request.getAttribute("captcha")) : false;
        String captchaTheme = request.getAttribute("captchaTheme") != null ? ((String) request.getAttribute("captchaTheme")) : "white";
        if (enableCaptcha) {
    %>
    <script type="text/javascript" src="https://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
    <% } %>
</head>
<body class="body-index">
<div id="wrapper">
<div id="header">
<div id="head">
    <div id="block-block-1" class="clear-block block block-block">


        <div class="content"><p><a href="/"><img src="/customisation/kpi/images/kpilogo.png"
                                                 alt="Workforcetrack.com"></a></p>
        </div>
    </div>

    <div id="block-block-2" class="clear-block block block-block">


        <div class="content">
            <ul class="log_in">
                <li>

                    <div id="LangLink">
                        <a class="choose" href="javascript:void(0);" id="ShowLang">Русский</a>
                    </div>

                    <div id="LangPlate" class="languages" style="display:none;">
                        <ul>
                            <li><a class="bann-bar f-1" href="#">Русский</a></li>
                            <li><a class="bann-bar f-2" href="http://www.kpi.com">Английский</a></li>
                            <li><a class="bann-bar f-5"
                                   href="//www.${helpHost}/content/o-que-o-workforcetrack-representa-e-por-que-precisamos-dele">Португальский</a>
                            </li>
                            <li><a class="bann-bar f-7"
                                   href="//www.${helpHost}/content/workforcetrack-nedir-ve-neden-ihtiyac%C4%B1m-var">Турецкий</a>
                            </li>
                            <li><a class="bann-bar f-3" href="#">Испанский</a></li>
                            <li><a class="bann-bar f-6" href="#">Итальянский</a></li>
                            <li><a class="bann-bar f-4" href="#">Французский</a></li>
                        </ul>
                    </div>


                </li>

                <li><a href="signup/freeSignup.html?locale=ru">Пробная версия</a>
                </li>
                <li><a class="no_br_r" href="//www.${helpHost}/contact-us">Свяжитесь с нами</a></li>
            </ul>
            <script type="text/javascript">
                $('a#ShowLang').click(function () {
                    if ($("div#LangPlate").css('display') == 'none') {
                        $('div#LangPlate').slideDown('slow', function () {
// Animation complete.
                        });
                    }
                    else {
                        $('div#LangPlate').slideUp('slow', function () {
// Animation complete.
                        });
                    }
                });
            </script>

        </div>
    </div>
</div>
<div id="head-menu">
    <div class="clear-block block block-nice_menus" id="block-nice_menus-1">

        <div class="content">
            <ul id="nice-menu-1" class="nice-menu nice-menu-down">
                <li class="menu-path-front act" id="menu-697"><a class="active" title="Главная" href="//www.kpi.com.ru">Главная</a>
                </li>
                <li class="menuparent menu-path-node-283" id="menu-919">
                    <a title="О продукте" href="//www.kpi.com.ru/about">
                        О продукте
                    </a>

                    <div class="dd-menu">
                        <ul>
                            <li class="menu-path-partners" id="menu-523"><a title="" href="//www.kpi.com.ru/partners">Партнеры</a></li>
                            <li class="menu-path-node-435" id="menu-1520">
                                <a title="Почему kpi.com.ru ?" href="//www.kpi.com.ru/content/pochemu-kpicomru">
                                    Почему kpi.com.ru ?
                                </a>
                            </li>
                            <li class="menu-path-node-134" id="menu-1563">
                                <a title="Условия использования" href="//www.kpi.com.ru/content/usloviya-ispolzovaniya">
                                    Условия использования
                                </a>
                            </li>
                        </ul>
                        <div class="dd-shadow">
                            <div></div>
                        </div>
                    </div>
                </li>
                <li class="menu-path-node-195" id="menu-795"><a title="Цены" href="//www.kpi.com.ru/pricing">Цены</a></li>
                <li class="menuparent menu-path-node-282" id="menu-918">
                    <a title="Обзор продукта" href="//www.kpi.com.ru/product-tour">
                        Обзор продукта
                    </a>

                    <div class="dd-menu">
                        <ul>
                            <li class="menu-path-node-288" id="menu-924">
                                <a title="Управление проектами" href="//www.kpi.com.ru/online-project-management-tool">
                                    Управление проектами
                                </a>
                            </li>
                            <li class="menu-path-node-289" id="menu-925">
                                <a title="CRM система" href="//www.kpi.com.ru/online-crm-system">
                                    CRM система
                                </a>
                            </li>
                            <li class="menu-path-node-291" id="menu-927">
                                <a title="Управление персоналом" href="//www.kpi.com.ru/HRMS">
                                    Управление персоналом
                                </a>
                            </li>
                            <li class="menu-path-node-290" id="menu-926">
                                <a title="Бухгалтерия" href="//www.kpi.com.ru/accounting-and-finance-system">
                                    Бухгалтерия
                                </a>
                            </li>
                            <li class="menu-path-node-12" id="menu-570">
                                <a title="Платежные ведомости" href="//www.kpi.com.ru/payroll">
                                    Расчет Заработной платы
                                </a>
                            </li>
                            <li class="menu-path-node-14" id="menu-572">
                                <a title="Электронная коммерция" href="//www.kpi.com.ru/ecommerce">
                                    Электронная коммерция
                                </a>
                            </li>
                            <li class="menu-path-node-13" id="menu-571">
                                <a title="Рабочий стол" href="//www.kpi.com.ru/desktop">
                                    Рабочий стол
                                </a>
                            </li>
                            <li class="menu-path-node-16" id="menu-573">
                                <a title="Отчетность" href="//www.kpi.com.ru/reports">
                                    Отчетность
                                </a>
                            </li>
                            <li class="menu-path-node-17" id="menu-1574">
                                <a title="Управление документами" href="//www.kpi.com.ru/documents">
                                    Управление документами
                                </a>
                            </li>
                        </ul>
                        <div class="dd-shadow">
                            <div></div>
                        </div>
                    </div>
                </li>
                <li class="menuparent menu-path-node-284" id="menu-920">
                    <a title="Помощь и поддержка" href="//www.kpi.com.ru/help">
                        Помощь
                    </a>

                    <div class="dd-menu">
                        <ul>
                            <li class="menu-path-node-284" id="menu-1110">
                                <a title="" href="//www.kpi.com.ru/help">
                                    Статьи помощи
                                </a>
                            </li>
                            <li class="menu-path-node-285" id="menu-921">
                                <a title="ЧаВо" href="//www.kpi.com.ru/faq">
                                    ЧаВо
                                </a>
                            </li>
                        </ul>
                        <div class="dd-shadow">
                            <div></div>
                        </div>
                    </div>
                </li>
                <li class="menu-path-news" id="menu-522">
                    <a title="" href="//www.kpi.com.ru/news">
                        Блог
                    </a>
                </li>
                <li class="menu-path-node-437" id="menu-1524">
                    <a title="Сервисы" href="//www.kpi.com.ru/content/servisy">
                        Сервисы
                    </a>
                </li>
                <li class="menu-path-node-286" id="menu-922">
                    <a title="Связь" href="//www.kpi.com.ru/contact-us">
                        Связь
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <form id="search-theme-form" method="post" accept-charset="UTF-8" action="/">
        <div>
            <div class="container-inline" id="search">
                <div id="edit-search-theme-form-1-wrapper" class="form-item">
                    <label for="edit-search-theme-form-1">Поиск на сайте: </label>
                    <input type="text" class="form-text" title="Введите искомые термины." value="" size="15"
                           id="edit-search-theme-form-1" name="search_theme_form" maxlength="128">
                </div>
                <input type="submit" class="form-submit" value="" id="edit-submit" name="op">
                <input type="hidden" value="form-5b8728e25def3f457b0886f74fd07d4a"
                       id="form-5b8728e25def3f457b0886f74fd07d4a" name="form_build_id">
                <input type="hidden" value="search_theme_form" id="edit-search-theme-form" name="form_id">
            </div>

        </div>
    </form>

</div>
</div>

<tiles:insertAttribute name="body" ignore="false"/>

<div id="footer">
    <div class="clear-block block block-block" id="block-block-3">


        <div class="content">
            <div class="content">
                <ul class="cols">
                    <li class="parent">
                        <h2 class=" f-title">Главная</h2>
                        <ul>
                            <li><a href="//www.kpi.com.ru/about">О продукте</a></li>
                            <li><a href="//www.kpi.com.ru/product-tour">Обзор продукта</a></li>
                            <li><a href="//www.kpi.com.ru/content/pochemu-kpicomru">Почему kpi.com.ru?</a></li>
                            <li><a href="//www.kpi.com.ru/news">Новости/Блоги</a></li>
                            <li><a href="//www.kpi.com.ru/price-comparison">Сравните нас</a></li>
                            <li><a href="//www.kpi.com.ru/pricing">Прайс-лист</a></li>
                        </ul>
                    </li>
                    <li class="parent">
                        <h2 class=" f-title">Мы предлагаем</h2>
                        <ul>
                            <li><a href="//www.kpi.com.ru/timesheet">Расписание</a></li>
                            <li><a href="//www.kpi.com.ru/attendance-tracking">Отслеживание посещаемости</a></li>
                            <li><a href="//www.kpi.com.ru/dashboards">Генерирование отчетов</a></li>
                            <li><a href="//www.kpi.com.ru/online-report-generating-system">Расчет заработной платы</a></li>
                        </ul>
                    </li>
                    <li class="parent">
                        <h2 class=" f-title">Наши продукты</h2>
                        <ul>
                            <li><a href="//www.kpi.com.ru/online-project-management-tool">Управление проектами</a></li>
                            <li><a href="//www.kpi.com.ru/online-crm-system">CRM-система</a></li>
                            <li><a href="//www.kpi.com.ru/HRMS">Управление персоналом</a></li>
                            <li><a href="//www.kpi.com.ru/accounting-and-finance">Ведение бухгалтерии</a></li>
                            <li><a href="//www.kpi.com.ru/ecommerce">Электронная коммерция</a></li>

                        </ul>
                    </li>
                    <li class="parent">
                        <h2 class=" f-title">Оставайтесь на связи</h2>
                        <ul>
                            <li><a href="http://www.facebook.com/kpiru">Facebook</a></li>
                            <li><a href="http://twitter.com/#!/kpi_ru">Twitter</a></li>
                            <li><a href="http://www.linkedin.com/groups/WorkforceTrack-%D0%A3%D0%BF%D1%80%D0%B0%D0%B2%D0%BB%D1%8F%D0%B9%D1%82%D0%B5-%D0%B1%D0%B8%D0%B7%D0%BD%D0%B5%D1%81%D0%BE%D0%BC-%D0%9B%D0%B5%D0%B3%D0%BA%D0%BE-4106716?gid=4106716&amp;report.success=7GcSc9MenMD5-zxa-kN_TToM-x5janvu_HMEw0LwSaLTiMXToFk2hhLiKanEipLCLCi2aHOMY2Q0YQnTDIu0oJnh-x5wKBYTHQ">LinkedIn</a></li>
                            <li><a href="http://vk.com/club30660260">ВКонтакте</a></li>
                        </ul>
                    </li>
                </ul>
                <a class="key-GoogleApp"
                   href="https://www.google.com/enterprise/marketplace/search?categoryId=6&amp;orderBy=RATING&amp;offset=10/">Google
                    apps</a>
                <br>
                <a href="http://www.saasdir.co.uk/search/profile.aspx?spid=20180" class="medal"><img src="http://kpi.com/sites/all/themes/wft/images/art_medal-1.png" alt="Most Popular, July 2011 - saasdir.co.uk"></a>
            </div>


            <div class="copyright">
                <div class="copyright_in">
                    <a href="kpi.com.ru=" class="logo-finnet href=">
                        <img alt="Finnetlimited.com" src="/sites/default/themes/wft_ru/images/finnet_logo.png">
                    </a>

                    <p>2007 - 2012 &copy; <a href="//www.kpi.com.ru">kpi.com.ru</a>
                        <br> Все права защищены
                    </p>
                </div>


            </div>
        </div>
    </div>
</div>

<tiles:insertAttribute name="script" ignore="true"/>

<%
    if (enableCaptcha != null && enableCaptcha) {%>
<script type="text/javascript">
    Recaptcha.create("<%=EdsContextParams.getRecaptchaPublicKey()%>", "recaptcha", {
        theme: "<%=captchaTheme%>"});
</script>
<% } %>

<%if ((request.getRequestURI().endsWith("welcomePage.jsp") || (request.getRequestURI().endsWith("freeTrial.jsp"))) && isRussian) {%>
<!-- Yandex.Metrika counter -->
<script type="text/javascript">
    (function (d, w, c) {
        (w[c] = w[c] || []).push(function () {
            try {
                w.yaCounter7791613 = new Ya.Metrika({id: 7791613, enableAll: true, webvisor: true});
            } catch (e) {
            }
        });

        var n = d.getElementsByTagName("script")[0],
                s = d.createElement("script"),
                f = function () {
                    n.parentNode.insertBefore(s, n);
                };
        s.type = "text/javascript";
        s.async = true;
        s.src = (d.location.protocol == "https:" ? "https:" : "http:") + "//mc.yandex.ru/metrika/watch.js";

        if (w.opera == "[object Opera]") {
            d.addEventListener("DOMContentLoaded", f);
        } else {
            f();
        }
    })(document, window, "yandex_metrika_callbacks");
</script>
    <%--<noscript>
        <div><img src="//mc.yandex.ru/watch/7791613" style="position:absolute; left:-9999px;" alt=""/></div>
    </noscript>--%>
<%--<script type="text/javascript">--%>
<%--(function (i, s, o, g, r, a, m) {--%>
<%--i['GoogleAnalyticsObject'] = r;--%>
<%--i[r] = i[r] || function () {--%>
<%--(i[r].q = i[r].q || []).push(arguments)--%>
<%--}, i[r].l = 1 * new Date();--%>
<%--a = s.createElement(o),--%>
<%--m = s.getElementsByTagName(o)[0];--%>
<%--a.async = 1;--%>
<%--a.src = g;--%>
<%--m.parentNode.insertBefore(a, m)--%>
<%--})(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');--%>

<%--ga('create', 'UA-59981695-6', 'auto');--%>
<%--ga('send', 'pageview');--%>

<%--</script>--%>
<!-- /Yandex.Metrika counter -->
<% } %>

</div>

<div style="position: fixed; bottom: 10px; right: 10px; z-index: 100;">

    <iframe src="//www.google.com/talk/service/badge/Show?tk=z01q6amlqdjssqo8lgsm2gs0lt7ejg3fhqnq3invb8mepsoh6cm5ni27p5tbjunjur6j3d996quore2mvbbqvldekik1vfklqanmhrnn8tcvcqluen8bde2o923f31ft4ch1m1a23grobeqgkq6s5og39c31ran4v34pvbii8s7p1k68fl6tvtmbt1j6perk82v5lqvfprkaf0lo8v7mj2fmclr726e2ge6vka12vo4rijmveprqg2&amp;w=200&amp;h=60"
            frameborder="0" allowtransparency="true" width="200" height="60">
    </iframe>
</div>

</body>
</html>