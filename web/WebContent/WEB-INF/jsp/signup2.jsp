<%--
  Created by IntelliJ IDEA.
  User: Sherali
  Date: Jan 29, 2009
  Time: 4:13:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title">
        <fmt:message key="freeTrial.signUp"/> ${productName}
    </tiles:putAttribute>
    <tiles:putAttribute name="style">
        <%--<style type="text/css">--%>
        <%--.formsize {--%>
        <%--width: 185px;--%>
        <%--}--%>
        <%--</style>--%>
        <link href="/landing/signup/signup.css" rel="stylesheet" type="text/css"/>
        <%--<link href="/landing/signup/css.css" rel="stylesheet" type="text/css">--%>
        <%--<link href="/landing/signup/reset.css" rel="stylesheet" type="text/css">--%>
        <script type="text/javascript" src="/jsscript/landing.js"></script>
    </tiles:putAttribute>
    <tiles:putAttribute name="body">

        <%----------------------------------------------------------%>
        <%--BEGIN contentPage--%>
        <div id="contentPlace" class="clear-block">

        <!-- Start MAIN -->
        <div id="main">

            <div id="trailPage">
                    <%--<h1 class="title c">7 DAYS FREE TRIAL</h1>--%>

                <!-- START TRIAL FORM -->
                <form id="appTrialForm" method="post" action='<c:url value="/signup2.html"/>' name="signup">
                    <input type="hidden" value="<c:out value="${service}"/>" name="service"/>
                    <input type="hidden" value="<c:out value="${type}"/>" name="type"/>
                    <input type="hidden" value="<c:out value="${category}"/>" name="category"/>
                    <input type="hidden" value="<c:out value="${modules}"/>" name="modules"/>
                    <input type="hidden" value="<c:out value="${moduleLimit}"/>" name="moduleLimit"/>
                    <input type="hidden" value="<c:out value="${isGBP}"/>" name="isGBP"/>
                    <input type="hidden" value="<c:out value="${supportPackage}"/>" name="supportPackage"/>
                    <input type="hidden" name="usagePeriod" value="<c:out value="${usagePeriod}"/>"/>
                    <input type="hidden" name="usagePeriodID" value="<c:out value="${usagePeriodID}"/>"/>
                    <input type="hidden" name="users" value="<c:out value="${users}"/>"/>
                    <input type="hidden" name="storage" value="<c:out value="${storage}"/>"/>
                    <input type="hidden" name="usersCost" value="<c:out value="${usersCost}"/>"/>
                    <input type="hidden" name="discount" value="<c:out value="${discount}"/>"/>
                    <input type="hidden" name="tax" value="<c:out value="${tax}"/>"/>
                    <input type="hidden" name="total" value="<c:out value="${total}"/>"/>
                    <section>
                            <%--<fieldset class="strongBox trialFormFildset">--%>

                        <!-- START TRIAL NOTICE -->
                        <div class="sdbr-1 right">

                            <!-- START TRIAL NOTICE -->
                                <%-- <div class="trialNotice right">
                                     <c:if test="${pageContext.request.locale.language == 'ru'}">
                                         <p><fmt:message key="signup.pleaseBeAdvisedThatWeUseNOTE"/>

                                             <fmt:message key="signup.weDoNotSellTradeOrRentYourPersonalInformationNOTE"/></p>
                                     </c:if>
                                     <c:if test="${pageContext.request.locale.language != 'ru'}">
                                         <p><fmt:message key="signup.pleaseBeAdvisedThatWeUseNOTE"/></p>

                                         <p style="margin:0;"><fmt:message key="signup.weDoNotSellTradeOrRentYourPersonalInformationNOTE"/></p>
                                     </c:if>

                                 </div>--%>
                            <!-- END TRIAL NOTICE -->

                            <h2 class="c pre-title"><fmt:message key="signup.whatcomeswithyouraccount"/></h2>

                            <div class="your-accaunt plateBox2 right your-accaunt2ForRu">
                                <dl>
                                    <dt class='dtForRu'>
                                        <fmt:message key="signup.usagePeriod"/>
                                    </dt>
                                    <dd>
                                        <c:out value="${usagePeriod}"/>
                                        <fmt:message key="signup.month"/>
                                    </dd>
                                    <dt class='dtForRu'>
                                        <fmt:message key="signup.users"/>
                                    </dt>
                                    <dd>
                                        <c:out value="${users}"/>
                                    </dd>
                                    <dt class='dtForRu'>
                                        <fmt:message key="signup.storage"/>
                                    </dt>
                                    <dd>
                                        <c:out value="${storage}GB"/>
                                    </dd>
                                    <c:if test='${not empty usersCost}'>
                                        <dt class='dtForRu'>
                                            <fmt:message key="signup.userCost"/>
                                        </dt>
                                        <dd>
                                            <c:out value="${usersCost}"/>
                                        </dd>
                                    </c:if>
                                    <c:if test="${smebuOrTjiloHosts == 'true'}">
                                        <dt class='dtForRu'>
                                            <fmt:message key="signup.discounts"/>
                                        </dt>
                                        <dd>
                                            <c:out value="${discount}"/>
                                        </dd>
                                    </c:if>
                                    <dt class='dtForRu'>
                                        <fmt:message key="signup.tax"/>
                                    </dt>
                                    <dd>
                                        <c:out value="${taxCost}"/>
                                    </dd>
                                    <dt class='dtForRu'>
                                        <fmt:message key="signup.totalAmounts"/>
                                    </dt>
                                    <dd>
                                        <c:out value="${total}"/>
                                    </dd>
                                </dl>
                            </div>
                            <div class="clearBox"></div>

                            <h2 class="c pre-title"><fmt:message key="signup.servicesyoucanuse"/></h2>

                            <div class="<%--services-use plateBox2 right--%>">
                                <c:choose>
                                    <c:when test="${empty modules}">
                                        <ul class="tickList">
                                                <%--<!--${services}-->--%>
                                            <li>
                                                <fmt:message key="signup.CRM"/>
                                            </li>
                                            <li>
                                                <fmt:message key="signup.projectManagement"/>
                                            </li>
                                            <li>
                                                <fmt:message key="signup.accountingFinance"/>
                                            </li>
                                            <li>
                                                <fmt:message key="signup.hrms"/>
                                            </li>
                                            <li>
                                                <fmt:message key="signup.payroll"/>
                                            </li>
                                            <li>
                                                <fmt:message key="signup.dashboardAndReporting"/>
                                            </li>
                                            <li>
                                                <fmt:message key="signup.androidApp"/>
                                            </li>
                                            <li>
                                                <fmt:message key="signup.iPhoneApp"/>
                                            </li>
                                        </ul>
                                    </c:when>
                                    <c:otherwise>
                                        <ul class="tickList">
                                                <%--<!--${services}-->--%>
                                            <c:if test="${fn:contains(modules, 'crm')}">
                                                <li>
                                                    <fmt:message key="signup.CRM"/>
                                                </li>
                                            </c:if>
                                            <c:if test="${fn:contains(modules, 'projects')}">
                                                <li>
                                                    <fmt:message key="signup.projectManagement"/>
                                                </li>
                                            </c:if>
                                            <c:if test="${fn:contains(modules, 'accounting')}">
                                                <li>
                                                    <fmt:message key="signup.accountingFinance"/>
                                                </li>
                                            </c:if>
                                            <c:if test="${fn:contains(modules, 'hrms')}">
                                                <li>
                                                    <fmt:message key="signup.hrms"/>
                                                </li>
                                            </c:if>
                                            <c:if test="${fn:contains(modules, 'payroll')}">
                                                <li>
                                                    <fmt:message key="signup.payroll"/>
                                                </li>
                                            </c:if>
                                            <li>
                                                <fmt:message key="signup.dashboardAndReporting"/>
                                            </li>
                                            <li>
                                                <fmt:message key="signup.androidApp"/>
                                            </li>
                                            <li>
                                                <fmt:message key="signup.iPhoneApp"/>
                                            </li>
                                        </ul>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="clearBox"></div>

                            <div class="limitsBox pays plateBox2 right c">
                                <strong class=""><fmt:message key="signup.allpurchaseareMade"/> <fmt:message key="signup.secureThrough"/>
                                        <%--<a href="#"><img src="images/pay_pal.png" alt="" />--%>
                                        <%--<a href="#">--%><img src="/i/signup/images/paypal.gif" alt=""/>
                                    <img src="/i/signup/images/worldpay.gif" alt=""/>
                                        <%--</a>--%></strong><br/><br/>
                                <strong class=""><fmt:message key="signup.acceptedCreditCards"/>
                                        <%--<a href="#"><img src="images/visa.png" alt="" /></a>--%>
                                        <%--<a href="#"><img src="images/incognito-card.png" alt="" /></a>--%>
                                        <%--<a href="#"><img src="images/american-express.png" alt="" /></a>--%>
                                        <%--<a href="#"><img src="images/discover.png" alt="" /></a>--%>

                                        <%--<a href="#">--%><img src="/i/signup/images/visa.gif" alt=""/><%--</a>--%>
                                        <%--<a href="#">--%><img src="/i/signup/images/master.gif" alt=""/><%--</a>--%>
                                        <%--<a href="#">--%><img src="/i/signup/images/amexp.gif" alt=""/><%--</a>--%>
                                        <%--<a href="#">--%><img src="/i/signup/images/card.gif" alt=""/><%--</a>--%>
                                        <%--<a href="#">--%><img src="/i/signup/images/worldpaycart.gif" alt=""/><%--</a>--%>
                                        <%--<a href="#">--%><img src="/i/signup/images/worldpaycart1.gif" alt=""/><%--</a>--%>
                                        <%--<a href="#">--%><img src="/i/signup/images/worldpaycart2.gif" alt=""/><%--</a>--%>
                                </strong>
                                <!--[if lte IE 8]>
                                <span class="bgAngle topLeft"></span>
                                <span class="bgAngle topRight"></span>
                                <span class="bgAngle bottomLeft"></span>
                                <span class="bgAngle bottomRight">&nbsp;</span>
                                <![endif]-->
                            </div>
                            <div class="clearBox"></div>


                        </div>
                        <!-- END TRIAL NOTICE -->

                        <div class="overhide">
                            <div class="trialFormFull">
                                <div class="BoxIn">

                                    <h2 class="pre-title"><fmt:message key="signup.administratorAccountDetailsToUpperCase"/></h2>

                                    <label>
                                        <span><fmt:message key="signup.firstName"/>: *</span>
                                        <input type="text" class="form-text required" name="adminFName" value="<c:out value="${adminFName}"/>"
                                               id="edit-submitted-fname"/>
                                        <span class="error"><c:out value="${fNameError}"/></span>
                                    </label>

                                    <label>
                                        <span><fmt:message key="signup.lastName"/>: *</span>
                                        <input type="text" class="form-text required" name="adminLName" value="<c:out value="${adminLName}"/>"
                                               id="edit-submitted-lname"/>
                                        <span class="error"><c:out value="${lNameError}"/></span>
                                    </label>

                                    <div class="clearBox"></div>

                                    <label>
                                        <span><fmt:message key="signup.email"/>: *</span>
                                        <input type="text" class="form-text required" name="adminEmail" value="<c:out value="${adminEmail}"/>"
                                               id="edit-submitted-e-mail"/>
                                        <span class="error"><c:out value="${emailError}"/> </span>
                                    </label>

                                    <label>
                                        <span><fmt:message key="signup.companyName"/>: *</span>
                                        <input type="text" class="form-text required" name="name" value="<c:out value="${name}"/>"
                                               id="edit-submitted-company"/>
                                        <span class="error"><c:out value="${nameError}"/> </span>
                                    </label>

                                    <div class="clearBox"></div>

                                    <label>
                                        <span><fmt:message key="signup.phone"/>:*</span>
                                        <input type="text" onkeypress="return phoneValidation(event)"
                                               class="form-text required" name="phone" value="<c:out value="${phone}"/>"
                                               id="edit-submitted-phone"/>
                                        <span class="error"><c:out value="${phoneError}"/> </span>
                                    </label>

                                    <label>
                                        <span><fmt:message key="signup.country"/>: *</span>
                                        <form:select path="newCompany.countryID" onchange="changeStatePaid(this)"
                                                     cssClass="form-text required">
                                            <form:option value=""><fmt:message key="signup.pleaseSelect"/> </form:option>
                                            <!--<option selected="selected" value="45">United Kingdom</option>-->
                                            <form:options items="${countrys}" itemLabel="name" itemValue="objectID"
                                                          id="edit-submitted-country"/>
                                        </form:select>
                                        <span class="error"><c:out value="${countryError}"/> </span>
                                    </label>

                                    <div class="clearBox"></div>

                                    <label>
                                        <span><fmt:message key="signup.promutionalCode"/>:</span>
                                        <em class="note"><fmt:message key="signup.ifapplicable"/> </em>
                                        <input type="text" class="form-text required" value="" id="edit-submitted-pcode"
                                               name="companySignedUpFrom"/>
                                    </label>

                                        <%--Locale Selector--%>
                                    <label title="<fmt:message key="signup.languageDisclaimer"/>">
                <span><fmt:message key="signup.language"/> <sup
                        style="font-size:xx-small; vertical-align:super;">1</sup>: </span>
                                        <em class="note"> <fmt:message key="signup.DefaultlanguagefortheSystem"/> </em>
                                        <form:select id="localeSelector" path="locale" cssClass="form-text required">
                                            <c:forEach var="localeItem" items="${locales}">
                                                <c:choose>
                                                    <c:when test="${pageContext.request.locale.language == localeItem.description}">
                                                        <option value="${localeItem.description}"
                                                                selected="selected">${localeItem.name}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form:option value="${localeItem.description}">${localeItem.name}</form:option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </form:select>
                                        <form:errors path="locale" cssClass="error"/>
                                    </label>

                                        <%--Payment Methods Selector--%>
                                    <label>
                                        <span><fmt:message key="signup.payment.method"/>: *</span>
                                        <form:select name="paymentMethod" id="paymentSelector" path="paymentMethod" cssClass="form-text required">
                                            <c:forEach var="paymentItem" items="${paymentMethods}">
                                                <c:choose>
                                                    <c:when test="${paymentItem}==${paymentMethod}">
                                                        <option value="${paymentItem}" selected="selected">${paymentItem}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form:option value="${paymentItem}">${paymentItem}</form:option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </form:select>
                                    </label>

                                    <div class="clearBox"></div>


                                    <div class="clearBox"></div>
                                    <div for="terms" id="agree" style="width: 100% !important;">

                                        <div style="height: 25px;">
                                            <fmt:message key="signup.iAgreeWitThe"/>
                                            <input type="checkbox" onchange="checkBox(this)" id="checkbox"/>

                                            <c:if test="${pageContext.request.locale.language == 'ru'}">
                                                <a href="http://www.kpi.com.ru/content/usloviya-polzovaniya" target="_blank">
                                                    <fmt:message key="signup.termsAndConditions"/> *
                                                </a>
                                            </c:if>
                                            <c:if test="${pageContext.request.locale.language != 'ru'}">
                                                <a href="//www.${helpHost}/content/terms-of-use" target="_blank">
                                                    <fmt:message key="signup.termsAndConditions"/> *</a>
                                            </c:if>

                                            <input type="hidden" id="checkText" name="check" value="false">

                                        </div>
                                        <div class="right clear" style="font-size: 10px;"><span class="error noWrapp"><c:out
                                                value="${chekedError}"/></span>
                                        </div>
                                    </div>

                                    <center>
                                        <div id="recaptcha"></div>
                                        <div style="text-transform:uppercase; height: 20px; ">${captchaError}</div>
                                    </center>
                    <span class="submitCover">
                        <input class="btn-1" type="submit" value="<fmt:message key="signup.buy.now"/>"/>
                    </span>

                                </div>
                            </div>
                        </div>
                        <div class="clearBox"></div>

                            <%--</fieldset>--%>
                    </section>
                    <div class="clearBox"></div>

                </form>
                <!-- END TRIAL US FORM -->


                <div class="clearBox"></div>
            </div>
            <!--End #main -->


        </div>
        <%--END contentPage--%>
        <%----------------------------------------------------------%>

    </tiles:putAttribute>
    <tiles:putAttribute name="script">
        <%--<script type="text/javascript">
      var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
      document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
      </script>
      <script type="text/javascript">
      try {
      var pageTracker = _gat._getTracker("UA-355982-15");
      pageTracker._trackPageview();
      } catch(err) {}</script>--%>
        <!--New Google Analytics script-->

        <!--New Google Analytics script-->

        <!-- ClickTale Bottom part -->
        <div id="ClickTaleDiv" style="display: none;"></div>
        <script src="http://s.clicktale.net/WRb4.js" type="text/javascript"></script>
        <script type="text/javascript">
            if (typeof ClickTale == 'function') ClickTale(6057, 1, "www02");
            window.onload = function clearHeaderFooter() {
                var obj = document.querySelector('#header');
                obj.remove();
                obj = document.querySelector('#footer');
                obj.remove();
            }
        </script>
        <!-- ClickTale end of Bottom part -->


    </tiles:putAttribute>
</tiles:insertDefinition>