<%--
  Created by IntelliJ IDEA.
  User: Lochin
  Date: 18-Nov-2010
  Time: 20:29:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>My Page</title>
    <link href="/mainStyles/orgChart/orgChart.css" rel="stylesheet" type="text/css">
    <link href="/mainStyles/orgChart/reset.css" rel="stylesheet" type="text/css">
    <style type="text/css" charset="utf-8">/* See license.txt for terms of usage */

    .firebugCanvas {
        position: fixed;
        top: 0;
        left: 0;
        display: none;
        border: 0 none;
        margin: 0;
        padding: 0;
        outline: 0;
    }

    .firebugCanvas:before, .firebugCanvas:after {
        content: "";
    }

    .firebugHighlight {
        z-index: 2147483646;
        position: fixed;
        background-color: #3875d7;
        margin: 0;
        padding: 0;
        outline: 0;
        border: 0 none;
    }

    .firebugHighlight:before, .firebugHighlight:after {
        content: "";
    }

    .firebugLayoutBoxParent {
        z-index: 2147483646;
        position: fixed;
        background-color: transparent;
        border-top: 0 none;
        border-right: 1px dashed #E00 !important;
        border-bottom: 1px dashed #E00 !important;
        border-left: 0 none;
        margin: 0;
        padding: 0;
        outline: 0;
    }

    .firebugRuler {
        position: absolute;
        margin: 0;
        padding: 0;
        outline: 0;
        border: 0 none;
    }

    .firebugRuler:before, .firebugRuler:after {
        content: "";
    }

    .firebugRulerH {
        top: -15px;
        left: 0;
        width: 100%;
        height: 14px;
        background: url(image/123.png) repeat-x;
        border-top: 1px solid #BBBBBB;
        border-right: 1px dashed #BBBBBB;
        border-bottom: 1px solid #000000;
    }

    .firebugRulerV {
        top: 0;
        left: -15px;
        width: 14px;
        height: 100%;
        background: url(images/123.png) repeat-y;
        border-left: 1px solid #BBBBBB;
        border-right: 1px solid #000000;
        border-bottom: 1px dashed #BBBBBB;
    }

    .overflowRulerX > .firebugRulerV {
        left: 0;
    }

    .overflowRulerY > .firebugRulerH {
        top: 0;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    .firebugLayoutBox {
        margin: 0;
        padding: 0;
        border: 0 none;
        outline: 0;
    }

    .firebugLayoutBox:before, .firebugLayoutBox:after {
        content: "";
    }

    .firebugLayoutBoxOffset {
        z-index: 2147483646;
        position: fixed;
        opacity: 0.8;
    }

    .firebugLayoutBoxMargin {
        background-color: #EDFF64;
    }

    .firebugLayoutBoxBorder {
        background-color: #666666;
    }

    .firebugLayoutBoxPadding {
        background-color: SlateBlue;
    }

    .firebugLayoutBoxContent {
        background-color: SkyBlue;
    }

    .firebugLayoutLine {
        z-index: 2147483646;
        background-color: #000000;
        opacity: 0.4;
        margin: 0;
        padding: 0;
        outline: 0;
        border: 0 none;
    }

    .firebugLayoutLine:before, .firebugLayoutLine:after {
        content: "";
    }

    .firebugLayoutLineLeft, .firebugLayoutLineRight {
        position: fixed;
        width: 1px;
        height: 100%;
    }

    .firebugLayoutLineTop, .firebugLayoutLineBottom {
        position: fixed;
        width: 100%;
        height: 1px;
    }

    .firebugLayoutLineTop {
        margin-top: -1px;
        border-top: 1px solid #999999;
    }

    .firebugLayoutLineRight {
        border-right: 1px solid #999999;
    }

    .firebugLayoutLineBottom {
        border-bottom: 1px solid #999999;
    }

    .firebugLayoutLineLeft {
        margin-left: -1px;
        border-left: 1px solid #999999;
    }

    .fbProxyElement {
        position: absolute;
        background-color: transparent;
        z-index: 2147483646;
        margin: 0;
        padding: 0;
        outline: 0;
        border: 0 none;
    }

    </style>

</head>
<body>
<c:if test="${empty orgChart}">
    <style type="text/css">
        body {
            background: none repeat 0 0 #ffffff;
            height: auto !important;
            margin: 0 auto;
            min-height: 100%;
            overflow-x: hidden;
        }
        .main {
            width: 100%;
            text-align: center;
            padding-top: 30px;
            font-family: Verdana, sans-serif;
            font-size: 15px;
            font-weight: bold;
            color: #2568ac;
        }
        #myDiv {
            width: 300px;
            height: 58px;
            background: no-repeat;
            margin-left: 22px
        }

        #wrapper {
            position: relative;
            margin: auto 5%;
        }
    </style>
    <script>
        function myfunction(selector) {
            parent.document.querySelector(selector).click();
        }
    </script>
    <div id="wrapper" class="land-CRM page-thankYou">
        <div id="myDiv"></div>
        <div class="main">
            <img src="/mainStyles/images/OrganizationChart.png" width="300px" alt="Under Maintenance"/>
        </div>
        <div class="main">
            The organization chart appears only after setting up supervisor-employee relationships in your company.<br/>
            You can set employee supervisors from employee profile page.<br/>
            <br/>
            <h5>If you need help in setting up organization chart please <a href="#"><span
                    id="plscontactus" onclick="myfunction('#feedbackbutton')">contact us.</span></a>
            </h5>
        </div>
    </div>
</c:if>
<c:if test="${not empty orgChart}">
    ${orgChart}
</c:if>

</body>
</html>