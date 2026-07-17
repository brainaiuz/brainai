<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.2">
    <link rel="stylesheet" href="/customisation/stefano/custom.css" type="text/css"/>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <link rel="stylesheet" href="/customisation/stefano/ie8-prev.css" type="text/css"/>
    <script src="/customisation/stefano/scripts/html5shiv.js"></script>
    <![endif]-->

    <!--[if gte IE 9]>
    <style type="text/css">
    </style>
    <![endif]-->

    <!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
            <script>window.jQuery || document.write('<script src="/customisation/stefano/scripts/jquery.js"><\/script>')</script>
     -->

    <%--<title>Log in</title>--%>

    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>
</head>
<body class="page_login">
<!--[if lt IE 7]>
<p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
<![endif]-->

<!--start #HEADER-->
<!--END #header-->


<div class="login_wrapper">
    <header>
        <a href="/" title="welcome to our site">
            <img id="logo" src="/customisation/stefano/images/logo_site-1.png" alt="Alternate text"/></a>
    </header>

    <tiles:insertAttribute name="body" ignore="false"/>

    <%--<form class="login" action="/">--%>
    <%--<div class="form-group">--%>
    <%--<input class="form-control" type="text" placeholder="username" />--%>
    <%--<em class="icon-user"></em>--%>
    <%--</div>--%>
    <%--<div class="form-group">--%>
    <%--<input class="form-control" type="password" placeholder="password" />--%>
    <%--<em class="icon-pass"></em>--%>
    <%--</div>--%>

    <%--<button class="btn btn_signIn">Sign In</button>--%>

    <%--<footer>--%>
    <%--<a href="/">Forgot password?</a>--%>
    <%--</footer>--%>
    <%--</form>--%>
</div>


<!--start #FOOTER-->
<!--END #footer-->

<tiles:insertAttribute name="script" ignore="true"/>

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

<%--ga('create', 'UA-59981695-11', 'auto');--%>
<%--ga('send', 'pageview');--%>

<%--</script>--%>
</body>
</html>