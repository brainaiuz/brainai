<%--
  Created by IntelliJ IDEA.
  User: Lochin copied form pricing.jsp
  Date: Feb 17, 2010
  Time: 6:04:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="frontEndLayoutNew">
<tiles:putAttribute name="title" value="Microsoft Project Alternative | CRM Software | CRM Online"/>
<tiles:putAttribute name="style">
    <script type="text/javascript" src="//kpi.com/misc/jquery.js?3"></script>
    <script type="text/javascript" src="//kpi.com/misc/drupal.js?3"></script>
    <script type="text/javascript" src="//kpi.com/sites/all/modules/nice_menus/nice_menus.js?3"></script>
    <script type="text/javascript">
        <!--//--><![CDATA[//><!--
        jQuery.extend(Drupal.settings,
                { "basePath":"http://www.workforcetrack.com/", "googleanalytics":{ "trackOutgoing":1,
                    "trackMailto":1,
                    "trackDownload":1,
                    "trackDownloadExtensions":"7z|aac|avi|csv|doc|exe|flv|gif|gz|jpe?g|js|mp(3|4|e?g)|mov|pdf|phps|png|ppt|rar|sit|tar|torrent|txt|wma|wmv|xls|xml|zip" }
                });
        //--><!]]>
    </script>
    <script type="text/javascript" src="//kpi.com/sites/all/themes/wft/highslide/highslide-with-gallery.js"></script>
    <script src="//kpi.com/sites/all/themes/wft/jquery.cookie.js"></script>

    <script>
        $(document).ready(function () {

            var COOKIE_NAME = 'own_cookie';
            var val = $.cookie(COOKIE_NAME);
            if (val == null) {
                $("#cover").css("font-size", "13px");
                $("#font-size-all option:nth-child(2)").attr("selected", "selected");
            }
            else {
                $("#cover").css("font-size", val + "px");
                switch (val) {
                    case "11":
                        $("#font-size-all option:nth-child(1)").attr("selected", "selected");
                        break;
                    case "13":
                        $("#font-size-all option:nth-child(2)").attr("selected", "selected");
                        break;
                    case "15":
                        $("#font-size-all option:nth-child(3)").attr("selected", "selected");
                        break;
                    default:
                        $("#font-size-all option:nth-child(2)").attr("selected", "selected");
                        break;
                }
            }

            $("#font-size-all").change(function () {
                var obj = $("#font-size-all").val();

                $("#cover").css("font-size", obj + "px");
                $.cookie(COOKIE_NAME, null);
                $.cookie(COOKIE_NAME, obj, { path:'/', expires:7 });
            });
        });
    </script>
    <script type="text/javascript">
        hs.graphicsDir = '/sites/all/themes/wft/highslide/graphics/';
        hs.align = 'center';
        hs.transitions = ['expand', 'crossfade'];
        hs.outlineType = 'rounded-white';
        hs.fadeInOut = true;
        hs.numberPosition = 'caption';
        hs.dimmingOpacity = 0.75;

        if (hs.addSlideshow) hs.addSlideshow({
            //slideshowGroup: 'group1',
            interval:5000,
            repeat:false,
            useControls:true,
            fixedControls:'fit',
            overlayOptions:{
                opacity:.75,
                position:'bottom center',
                hideOnMouseOut:true
            }
        });
    </script>
    <!--END Galery-->
    <%--<style type="text/css"
           media="print">@import "//www.workforcetrack.com/sites/all/themes/wft/print.css";</style>--%>
    <!--[if lt IE 7]>
    <style type="text/css" media="all">@import "//kpi.com/sites/all/themes/wft/fix-ie.css";</style>
    <![endif]-->
    <meta name="google-site-verification" content="jjw1ZnV5AryaQZs-KB64fCH3EpvkT94GKJAoFvLFzbU"/>
    <meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>
    <META name="y_key" content="d766be7156d8deef">
    <meta name="keywords"
          content="microsoft project alternative, alternative microsoft projects,  crm online, online crm, crm software, software crm.">
    <meta name="description"
          content="workforcetrack.com is your source for microsoft project alternative, crm software and crm online">
    <%--End of Drupal Head Content--%>
    <meta name="gwt:property" content="locale=${defaultLocale}">
    <%--<link rel="stylesheet" type="text/css" href="/mainStyles/Core.cache.css"/>--%>
    <script language="javascript" src="pricing/pricing.nocache.js"></script>
</tiles:putAttribute>
<tiles:putAttribute name="body">
    <input type="hidden" id="isukclient" value="${isukclient}"/>
    <input type="hidden" id="productname" value="${productname}"/>
    <input type="hidden" id="vatN" value="${vatN}"/>
    <input type="hidden" id="hostName" value="${hostName}"/>
    <input type="hidden" id="freeTrialDays" value="${freeTrialDays}"/>
    <input type="hidden" id="currencyCODE" value="${currencyCODE}"/>
    <iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>
    <!--begin content and ZoneIn-inner-->
    <div id="cover">
        <!--begin #main-->
        <div class="price-main">
            <!--start Pricing GWT content-->
                <%--<div id="contbody">--%>
            <div id="container">

            </div>
                <%------------------------------------------------------------------------------------------------%>
            <!--Start SUB FOOTER REGION-->
                <%--<div id="supFooter">

                    <!-- Start All Directories List -->
                    <div class="directList plateBox">
                        <ul>
                            <li>
                                <h2><fmt:message key="pricing.projectManagement"/></h2>
                                <ul>
                                    <li>
                                        <a href="//www.${helpHost}/content/take-project-management-software-screenshot-tour#projects"><fmt:message key="pricing.projects"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesPMProjects"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/take-project-management-software-screenshot-tour#tasks"><fmt:message key="pricing.tasks"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesPMTasks"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/take-project-management-software-screenshot-tour#timesheet"><fmt:message key="pricing.timesheets"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesPMTimesheets"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/take-project-management-software-screenshot-tour#issues"><fmt:message key="pricing.issues"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesPMIssues"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/take-project-management-software-screenshot-tour#events"><fmt:message key="pricing.events"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesPMEvents"/>&ndash;%&gt;
                                    </li>
                                </ul>
                            </li>
                            <li class="even">
                                <h2><fmt:message key="pricing.CRM"/></h2>
                                <ul>
                                    <li>
                                        <a href="//www.${helpHost}/content/crm-campaign-contact-and-notes#leads"><fmt:message key="pricing.leads"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesCRMLeads"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/crm-campaign-contact-and-notes#contacts"><fmt:message key="pricing.contacts"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesCRMContacts"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/crm-campaign-contact-and-notes#marketing"><fmt:message key="pricing.marketing"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesCRMMarketing"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/crm-campaign-contact-and-notes#case-management"><fmt:message key="pricing.caseManagement"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesCRMCaseManagement"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/crm-campaign-contact-and-notes#case-management"><fmt:message key="pricing.customForms"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesCRMCustomForms"/>&ndash;%&gt;
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <h2><fmt:message key="pricing.basicAccounting"/></h2>
                                <ul>
                                    <li>
                                        <a href="//www.${helpHost}/content/accounting-and-finance-0#sb-Dashboard"><fmt:message key="pricing.dashboard"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesAccountingDashboard"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/accounting-and-finance-0#sb-Invoicing "><fmt:message key="pricing.invoicing"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesAccountingInvoicing"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/accounting-and-finance-0#sb-Chart-of-accounts"><fmt:message key="pricing.chartOfAccounts"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesAccountingChartOfAccounts"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/accounting-and-finance-0#sb-Fixed-asset-register"><fmt:message key="pricing.fixedAssetRegister"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesAccountingFixedAssetRegister"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/accounting-and-finance-0#sb-Discounts"><fmt:message key="pricing.discounts"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesAccountingDiscounts"/>&ndash;%&gt;
                                    </li>
                                </ul>
                            </li>
                            <li class="even">
                                <h2><fmt:message key="pricing.hrAndCollaboration"/></h2>
                                <ul>
                                    <li>
                                        <a href="//www.${helpHost}/content/workspace#message-centre"><fmt:message key="pricing.messageCentre"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesHRAndCollaborationMessageCentre"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/workspace#calendar"><fmt:message key="pricing.calendar"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesHRAndCollaborationCalendar"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="#"><fmt:message key="pricing.docs"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesHRAndCollaborationDocs"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="#"><fmt:message key="pricing.surveys"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesHRAndCollaborationSurveys"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/hr-management"><fmt:message key="pricing.hrms"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesHRAndCollaborationHRMS"/>&ndash;%&gt;
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <h2><fmt:message key="pricing.ecommerce"/></h2>
                                <ul>
                                    <li>
                                        <a href="//www.${helpHost}/content/ecommerce#websites"><fmt:message key="pricing.websites"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesEcommerceWebsites"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/ecommerce#widgets"><fmt:message key="pricing.widgets"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesEcommerceWidgets"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/ecommerce#storefront"><fmt:message key="pricing.storefront"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesEcommerceStorefront"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/ecommerce#stock-control"><fmt:message key="pricing.stockControl"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesEcommerceStockControl"/>&ndash;%&gt;
                                    </li>
                                    <li>
                                        <a href="//www.${helpHost}/content/ecommerce#payments"><fmt:message key="pricing.payments"/></a>
                                        &lt;%&ndash;<g:Anchor href="javascript:;" ui:field="subDirectoriesEcommercePayments"/>&ndash;%&gt;
                                    </li>
                                </ul>
                            </li>
                        </ul>

                        <!--[if lte IE 8]><span class="bgAngle topLeft"></span>
                    <span class="bgAngle topRight"></span>
                    <span class="bgAngle bottomLeft"></span>
                    <span class="bgAngle bottomRight">&nbsp;</span><![endif]-->
                    </div>
                    <!-- End All Directories List -->

                    <!-- Start Our Clients List -->
                    <ul class="ourClients">
                        <li class="bann-mediacom">mediacom<a href="/"></a></li>
                        <li class="bann-4flying">4flying<a href="/"></a></li>
                        <li class="bann-cooconnect">cooconnect<a href="/"></a></li>
                        <li class="bann-kio">revuelta<a href="/"></a></li>
                        <li class="bann-revuelta">revuelta<a href="/"></a></li>
                        <li class="bann-catenate">catenate<a href="/"></a></li>
                        <li class="bann-hw">hw<a href="/"></a></li>
                    </ul>
                    <!-- End Our Clients List -->

                </div>--%>
            <!--End SUB FOOTER REGION-->
                <%------------------------------------------------------------------------------------------------%>

            <!-- End wrapper content -->
                <%--</div>--%>
            <!--end Pricing GWT Content #contbody-->

            <script type="text/javascript" src="/pricing/jquery.js"></script>

            <script type="text/javascript">
                $('a#showDiv').click(function () {
                    if ($("div#test").css('display') == 'none') {
                        $('div#test').slideDown('slow', function () {
                            // Animation complete.
                        });
                    }
                    else {
                        $('div#test').slideUp('slow', function () {
                            // Animation complete.
                        });
                    }
                });
            </script>

        </div>
        <!--END #main-->
    </div>
    <!--END #ZoneIn-->

</tiles:putAttribute>
<tiles:putAttribute name="script">


    <%--End of Drupal Content--%>
</tiles:putAttribute>
</tiles:insertDefinition>