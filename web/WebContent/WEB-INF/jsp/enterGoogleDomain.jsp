<%--
  Created by IntelliJ IDEA.
  User: Aziz
  Date: Oct 02, 2009
  Time: 4:13:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title">
        <fmt:message key="pleaseEnterGoogleAppsDomainTitle"/> ${productName}
    </tiles:putAttribute>
    <tiles:putAttribute name="style">
        <style>
            .error {
                color: #ff0000;
                font-weight: bold;
            }

            span.btn-google-domain input {
                height: 36px;
                line-height: 36px;
                font-size: 18px;
                color: #fff;
                -webkit-text-shadow: 0 -1px 0 rgba(0, 0, 0, 1);
                -moz-text-shadow: 0 -1px 0 rgba(0, 0, 0, 1);
                -ms-text-shadow: 0 -1px 0 rgba(0, 0, 0, 1);
                -o-text-shadow: 0 -1px 0 rgba(0, 0, 0, 1);
                text-shadow: 0 -1px 0 rgba(0, 0, 0, 1);

                cursor: pointer;
                border: 0;
                background: transparent;
            }

            btn-google-domain:hover, .btn-google-domain:active {
                background-color: #74B13A;
                /*noinspection CssInvalidFunction*/
                background-image: -moz-linear-gradient(center top, #A0DB66, #74B13A);
                text-decoration: none;
            }

            .btn-google-domain {
                background-color: #66A32E;
                background-image: -moz-linear-gradient(center top, #8BC94E, #66A32E);
                border: 1px solid #65AA0A;
                border-radius: 18px 18px 18px 18px;
                box-shadow: 0 1px 1px #BCED8C inset;
                color: #FFFFFF;
                display: inline-block;
                font-size: 18px;
                height: 36px;
                line-height: 35px;
                padding: 0 50px;
                text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.36);
            }

        </style>
    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <div id="index-page" style="width:860px; margin: 15px auto; min-height: 300px;">
            <form:form method="post" action="/check" id="ddd">
                <div class="spiffyfg">
                    <h1 class="title" style="font-size:22px;">
                        <fmt:message key="pleaseEnterGoogleAppsDomain"/>
                    </h1>

                    <div id="grey1" style="margin:20px 7px;text-align:center;vertical-align:middle;">

                        <table style="width:100%;">
                            <tr>
                                <td>
                                    <img src="//www.google.com/intl/en/images/logos/apps_logo.gif">
                                </td>
                                <td valign="middle" style="vertical-align:middle;padding:0;">
                                    &nbsp;www.
                                    <input type="text" id="GD" name="GOOGLE_DOMAIN" size=30
                                           style="vertical-align:middle;margin:0;" value=""/>
                                    <input type="hidden" name="token"/>
                                    <span>
                                        <input class="btn-google-domain" type="submit"
                                               value="<fmt:message key="index.signIn"/>"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="2"><span
                                        style="color:#015681;font-size:11px;padding-left:50px;">(ex. yourcompanyname.com)</span>
                                </td>
                            </tr>
                        </table>

                    </div>
                </div>
            </form:form>
        </div>

    </tiles:putAttribute>


</tiles:insertDefinition>