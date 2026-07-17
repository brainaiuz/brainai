<%--
  Created by IntelliJ IDEA.
  User: Hasan Xo'janazarov
  Date: 04.04.13
  Time: 16:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Online Chat</title>

    <script type="text/javascript">
        function load() {
            var domenname = "";
            domenname = "${helpHost}";
            if (domenname == "kpi.com") {
                window.$zopim.livechat.window.show();
                $zopim.livechat.setLanguage('${defaultLocale}');
            } else if (domenname == "genesis-gifts.com") {
                window.$zopim.livechat.window.show();
                $zopim.livechat.setLanguage('${defaultLocale}');
            }
        }
    </script>

    <style type="text/css">
        .zopim {
            position: fixed !important;
            z-index: 10000009 !important;
            position: absolute !important;
            left: 40% !important;
            top: 20% !important;

        }

        #panel {
            margin: auto 15px;
            position: fixed;
            width: 95%;
        }

        #panel h2 {
            color: #476889;
            font-family: "Georgia";
            font-size: 22px;
            margin: 0 0 16px;
            text-align: center;
        }
    </style>


</head>
<body onload="load();">

<div id="panel">

    <h2>
        <fmt:message key="changepassword.welcomeTo"/> ${helpHost} <fmt:message key="index.onlineSupport"/>
    </h2>

    <div id=zopimContent>

        <!--Start of Zopim Live Chat Script-->
        <script type="text/javascript">
            var zopimPanel = "";
            zopimPanel = "${helpHost}";
            if (zopimPanel == "kpi.com") {
                window.$zopim || (function (d, s) {
                    var z = $zopim = function (c) {
                        z._.push(c)
                    }, $ = z.s =
                            d.createElement(s), e = d.getElementsByTagName(s)[0];
                    z.set = function (o) {
                        z.set.
                                _.push(o)
                    };
                    z._ = [];
                    z.set._ = [];
                    $.async = !0;
                    $.setAttribute('charset', 'utf-8');
                    $.src = '//cdn.zopim.com/?VFo2s9nYuh7nzXzmhXUzKIIeTLJ07PBu';
                    z.t = +new Date;
                    $.
                            type = 'text/javascript';
                    e.parentNode.insertBefore($, e)
                })(document, 'script');
            } else if (zopimPanel == "genesis-gifts.com") {
                window.$zopim || (function (d, s) {
                    var z = $zopim = function (c) {
                        z._.push(c)
                    }, $ = z.s =
                            d.createElement(s), e = d.getElementsByTagName(s)[0];
                    z.set = function (o) {
                        z.set.
                                _.push(o)
                    };
                    z._ = [];
                    z.set._ = [];
                    $.async = !0;
                    $.setAttribute("charset", "utf-8");
                    $.src = "//v2.zopim.com/?3bDOlzvoiuaSwW5bazCiyBwfr9bKYTed";
                    z.t = +new Date;
                    $.
                            type = "text/javascript";
                    e.parentNode.insertBefore($, e)
                })(document, "script");
            } else if (zopimPanel == "app.activira.com" || zopimPanel.indexOf("activira") != -1){
                        window.$zopim ||(function(d,s){var z=$zopim=function(c){z._.push(c)},$=z.s=
                        d.createElement(s),e=d.getElementsByTagName(s)[0];z.set=function(o){z.set.
                        _.push(o)};z._=[];z.set._=[];$.async=!0;$.setAttribute("charset","utf-8");
                    $.src="//v2.zopim.com/?3ifn5O72dwofspPgdI3GONh9bOCAHGWV";z.t=+new Date;$.
                            type="text/javascript";e.parentNode.insertBefore($,e)})(document,"script");

            }else {
                var zopimPanel = "";
                zopimPanel = "<div style='border:1px solid #5d5d5d;width:630px;margin:0 auto;padding:10px;text-align: center;'>";
                zopimPanel += "<fmt:message key="liveChatIsYetToBeConfiguredAtThisMoment"/> ";
                zopimPanel += "</div>"
                document.getElementById("zopimContent").innerHTML = zopimPanel;
            }


        </script>
        <!--End of Zopim Live Chat Script-->
    </div>

</div>
</body>
</html>