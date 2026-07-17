<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<tiles:insertDefinition name="frontEndLayoutNew">
<tiles:putAttribute name="title">
    Thank you! Registration completed. Please activate your account now - ${productName}
</tiles:putAttribute>
<tiles:putAttribute name="style">
    <link href="/landing/css/welcomepage.css" rel="stylesheet" type="text/css">
    <link rel="shortcut icon" href="/customisation/${productNameLower}/images/favicon.ico" type="image/x-icon"/>

</tiles:putAttribute>
<tiles:putAttribute name="body">

    <div id="contwrap" align="center">
    <div id="contbody" align="center">
        <div id="main">
            <div id="cont-bann_1"></div>
            <div id="cont-bann_2"></div>
            <div class="thanks">
                <h2>Thank you for registering at ${helpHost}
                </h2>

                <div align="left" style="padding-left: 20px;">

                    <ul class="welcomeul">
                        <li> For security purposes, an activation link has been sent to your email (it may take a few minutes to arrive) </li>
                        <li> Please follow the link we have sent, in order to activate your account </li>
                        <li> Check your junk or spam folder if you do not see the activation email appear shortly </li>
                    </ul>
                    <br/>

                    <%--<h3>While you wait for your activation email, may we suggest you try our <a
                            title="Login with demo account"
                            href="http://www.${helpHost}/shadowLogin?id=gfH%2FC8og8Q0%3D">demo account</a><br>
                        (Username: demo, Password: demo) and see how the system works.
                    </h3>--%>
                    <br/>
                </div>
            </div>
            <!--/thanks-->

            <DIV id="contacts">

                <DIV class="cover"><!--left border-->
                    <div class="cover"><!--right border-->
                        <h3 class="h3class"><fmt:message key="welcome.questions"/></h3>
                        <TABLE>
                            <TBODY>
                            <TR>
                                <TD><STRONG><fmt:message key="pagenotfound.email"/> </STRONG></TD>
                                <TD><A href="mailto:${email}" class="aclass">
                                ${email}
                                </A>
                                </TD>
                            </TR>
                            <TR>
                                <TD><STRONG><fmt:message key="pagenotfound.skype"/> </STRONG></TD>
                                <TD> ${skype}
                                </TD>
                            </TR>
                            <TR>
                                <TD><STRONG><fmt:message key="pagenotfound.phone"/> </STRONG></TD>
                                <TD> ${supportPhone}
                                </TD>
                            </TR>

                            </TBODY>
                        </TABLE>

                        <p class="explanation">
                            Please feel free to contact us via email, phone or Skype/any other IM you like.
                            Would like to contact us while on site? That's easy! We integrated LiveHelp system
                            straight into ${productName} that enables you to chat with one of our operators with
                            one click. Just click on LiveHelp icon.
                        </p>

                        <p class="explanation"><fmt:message key="welcome.kindRegards"/><BR>
                            ${productName} Support Team</p>


                    </DIV>
                </DIV>
            </DIV>

        </div>
        <!--end main--></div>
</div>

</tiles:putAttribute>
<tiles:putAttribute name="script">
    <!--New Google Analytics script-->

    <!--New Google Analytics script-->
    <%--TODO: move to https, it was commented as we implemented https--%>
    <%--<img src="http://ad.retargeter.com/seg?add=132528" width="1" height="1"/>--%>
    <!-- begin adBrite, Sign-ups tracking --><img border="0" hspace="0" vspace="0" width="1" height="1"
    src="//stats.adbrite.com/stats/stats.gif?_uid=1075056&_pid=1"/><!-- end adBrite, Sign-ups tracking -->

</tiles:putAttribute>


</tiles:insertDefinition>