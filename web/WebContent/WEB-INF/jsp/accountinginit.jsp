<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% String hostName = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName(); %>
<!doctype html>
<html class="no-js" lang="">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <fmt:message key="accountinginit.accountingSetup"> </fmt:message>
    </title>

    <link rel="stylesheet"
          href="/mainStyles/new-ui/css/materialize.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/css/jquery-ui.min.css">

    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon">

    <style>
        .invalid {
            border-bottom: 1px solid #F44336;
            box-shadow: 0 1px 0 0 #F44336;
        }
        .ui-datepicker {
            width: 20em;
        }
    </style>
</head>

<body toast="bottom-right" class="features-list-wrapper">
<!--Import jQuery before materialize.js-->
<script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/mainStyles/js/jquery-ui.min.js"></script>
<%--<!--<script type="text/javascript" src="js/jquery.easing.1.3.js"></script>-->--%>
<script type="text/javascript" src="/mainStyles/new-ui/js/materialize-0.97.5.min.js"></script>


<form action="/accountinginit.html" method="post" content="application/json" id="accounting-form" class="features-list features-list--accounting">
    <aside class="features-aside">
        <div class="features-list__heading">
            <a class="features-aside__title">
                <i class="ficon--calc main-modules__module-icon"></i>
                <span><fmt:message key="accounts"> </fmt:message></span>
            </a>
        </div>

        <dl class="features-aside__welcome">
            <dt><fmt:message key="accountinginit.welcome"> </fmt:message></dt>
            <dd>${fullname}!</dd>
        </dl>

        <ul class="features-aside__steps">
            <li id="orgdetails" class="status--current">
                <span><fmt:message key="accountinginit.organizationDetails"> </fmt:message></span>
            </li>
            <li id="features" class="status--not-passed">
                <span><fmt:message key="accountinginit.features"> </fmt:message></span>
            </li>
            <li id="paymentoptions" class="status--not-passed">
                <span><fmt:message key="accountinginit.paymentOptions"> </fmt:message></span>
            </li>
        </ul>
    </aside>
    <main id="firstpart">
        <div class="features-list__heading">
            <h2 class="features-list__title">
                <fmt:message key="accountinginit.accountingSetup"> </fmt:message>
            </h2>
            <h3 class="features-list__sub-title">
                <fmt:message key="accountinginit.RequiredInformationToCompleteYourAccountingSetup.ThisInformationWillShowUpOnYourInvoicesAndAccounts."> </fmt:message>
            </h3>
        </div>

        <div class="features-list__main-body">
            <div class="content-holder">
                <fieldset>
                    <div class="grid-row">
                        <div class="col-6">
                            <div class="form-group">
                                <label for="test-accountsStartDate" class="form-group__label">${productname}<fmt:message key="accountinginit.booksStartdate"> </fmt:message></label>
                                <div class="form-group__content">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="form-control input-field listbox-wrapper" id="yearWrapper">
                                                <select name="conversion_date_year" id="conversion_date_year"
                                                        class="select-wrapper gwt-ListBox">
                                                    <option value="-1" disabled><fmt:message key="accountinginit.year"> </fmt:message></option>
                                                    <%
                                                        Date date = new Date();
                                                        date.setYear(date.getYear() - 40);
                                                        Integer itemId = null;
                                                        for (int i = 1; i <= 41; i++) {
                                                            String year = new SimpleDateFormat("yyyy").format(date);
                                                    %>
                                                    <option value="<%=year%>" <%=year.equalsIgnoreCase(request.getAttribute("conversion_date_year").toString()) ? "selected" : ""%>><%=year%></option>
                                                    <%
                                                            date.setYear(date.getYear() + 1);
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-control input-field listbox-wrapper" id="monthWrapper">
                                            <select name="conversion_date_month" id="conversion_date_month"
                                                    class="select-wrapper gwt-ListBox">
                                                <option value="-1" disabled><fmt:message key="accountinginit.month"> </fmt:message></option>
                                                <option value="0" ${conversion_date_month == 0 ? 'selected' : ''} ><fmt:message key="accountinginit.january"> </fmt:message></option>
                                                <option value="1" ${conversion_date_month == 1 ? 'selected' : ''}><fmt:message key="accountinginit.february"> </fmt:message></option>
                                                <option value="2" ${conversion_date_month == 2 ? 'selected' : ''}><fmt:message key="accountinginit.march"> </fmt:message></option>
                                                <option value="3" ${conversion_date_month == 3 ? 'selected' : ''}><fmt:message key="accountinginit.april"> </fmt:message></option>
                                                <option value="4" ${conversion_date_month == 4 ? 'selected' : ''}><fmt:message key="accountinginit.may"> </fmt:message></option>
                                                <option value="5" ${conversion_date_month == 5 ? 'selected' : ''}><fmt:message key="accountinginit.june"> </fmt:message></option>
                                                <option value="6" ${conversion_date_month == 6 ? 'selected' : ''}><fmt:message key="accountinginit.july"> </fmt:message></option>
                                                <option value="7" ${conversion_date_month == 7 ? 'selected' : ''}><fmt:message key="accountinginit.august"> </fmt:message></option>
                                                <option value="8" ${conversion_date_month == 8 ? 'selected' : ''}><fmt:message key="accountinginit.september"> </fmt:message></option>
                                                <option value="9" ${conversion_date_month == 9 ? 'selected' : ''}><fmt:message key="accountinginit.october"> </fmt:message></option>
                                                <option value="10" ${conversion_date_month == 10 ? 'selected' : ''}><fmt:message key="accountinginit.november"> </fmt:message></option>
                                                <option value="11" ${conversion_date_month == 11 ? 'selected' : ''}><fmt:message key="accountinginit.december"> </fmt:message></option>
                                            </select>
                                        </div>
                                    </div>

                                </div>

                            </div>
                        </div>
                        <c:if test="${isGccCountry != true}">
                            <div class="col-6">
                                <div class="form-group">
                                    <div class="form-group__label">
                                        <fmt:message key="accountinginit.WhatMainCurrencyDoesYourOrganizationUse?"> </fmt:message>
                                    </div>
                                    <div class="form-group__content">
                                        <div class="form-control input-field listbox-wrapper">
                                            <div class="select-wrapper gwt-ListBox"><span class="caret">▼</span>
                                                <select id="cur" name="currency" class="gwt-ListBox initialized" inside-origin="false">
                                                    <option value="-1" disabled><fmt:message key="accountinginit.pleaseSelect"> </fmt:message></option>
                                                    <c:forEach items="${currencies}" var="currency">
                                                        <option value="${currency.description}" ${currency.description == companyCurrency ? 'selected' : ''}>${currency.name}</option>
                                                    </c:forEach>
                                                </select></div>
                                            <label></label><span class="material-label"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    <div class="grid-row">
                        <div class="col-6">
                            <div class="form-group">
                                <label class="form-group__label">
                                    <fmt:message key="accountinginit.industry"/>
                                </label>
                                <div class="form-group__content">
                                    <div class="form-control input-field listbox-wrapper">
                                        <select name="industry" class="select-wrapper gwt-ListBox"
                                        ${industryid != null ? 'disabled="disabled"' : ''}>
                                            <option value="-1">
                                                <fmt:message key="accountinginit.pleaseSelect"/>
                                            </option>
                                            <c:forEach items="${industries}" var="industry">
                                                <option value="${industry.id}"
                                                    ${industry.id == industryid ? 'selected="selected"' : ''}>
                                                        ${industry.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <c:if test="${industryid != null}">
                                            <input type="hidden" name="industry" value="${industryid}" />
                                        </c:if>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="form-group">
                                <label class="form-group__label"><fmt:message key="accountinginit.IDoMyAccountingUsing"> </fmt:message></label>
                                <div class="form-group__content">
                                    <div class="form-control input-field listbox-wrapper">
                                        <select name="accounting_tool" class="select-wrapper gwt-ListBox">
                                            <option value="-1"><fmt:message key="accountinginit.pleaseSelect"> </fmt:message></option>
                                            <option value="Pen and Paper" ${accounting_tool == 'Pen and Paper' ? 'selected' : ''}><fmt:message key="accountinginit.penAndPaper"> </fmt:message></option>
                                            <option value="Spreadsheet/Excel" ${accounting_tool == 'Spreadsheet/Excel' ? 'selected' : ''}><fmt:message key="accountinginit.Spreadsheet/Excel"> </fmt:message></option>
                                            <option value="Quickbooks" ${accounting_tool == 'Quickbooks' ? 'selected' : ''}><fmt:message key="accountinginit.quickbooks"> </fmt:message></option>
                                            <option value="Sage" ${accounting_tool == 'Sage' ? 'selected' : ''}><fmt:message key="accountinginit.sage"> </fmt:message></option>
                                            <option value="Xero" ${accounting_tool == 'Xero' ? 'selected' : ''}><fmt:message key="accountinginit.xero"> </fmt:message></option>
                                            <option value="Tally" ${accounting_tool == 'Tally' ? 'selected' : ''}><fmt:message key="accountinginit.tally"> </fmt:message></option>
                                            <option value="Other" ${accounting_tool == 'Other' ? 'selected' : ''}><fmt:message key="accountinginit.other"> </fmt:message></option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>

                <fieldset>
                    <h4 class="fs-3"><fmt:message key="accountinginit.addressInformation"> </fmt:message></h4>

                    <div class="grid-row">
                        <div class="col-6">
                            <div class="form-group">
                                <div class="form-group__content">
                                    <input type="text" class="form-control" placeholder="<fmt:message key="accountinginit.addressLine"> </fmt:message>"
                                           name="billing_addr_1" value="${address1}"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="form-group">
                                <div class="form-group__content">
                                    <input type="text" class="form-control" placeholder="<fmt:message key="accountinginit.addressLine2"> </fmt:message>"
                                           name="billing_addr_2" value="${address2}"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="grid-row">
                        <div class="col">
                            <div class="form-group">
                                <div class="form-group__content">
                                    <input type="text" class="form-control" placeholder="<fmt:message key="accountinginit.city"> </fmt:message>"
                                           name="billing_addr_city" value="${city}"/>
                                </div>
                            </div>
                        </div>

                        <c:if test="${states != null && not empty states}">
                            <div class="col" id="state-wrapper">
                                <div class="form-group">
                                    <div class="form-group__content">
                                            <%--<input type="text" class="form-control" placeholder="State/Province" />--%>
                                            <%--<div class="form-control input-field " id="state-wrapper">class="select-wrapper gwt-ListBox" --%>
                                        <div class="input-field">
                                            <select class="form-control listbox-wrapper" name="billing_addr_state">
                                                <option value="-1" disabled><fmt:message key="accountinginit.State/Province"> </fmt:message></option>
                                                <c:forEach items="${states}" var="state">
                                                    <option value="${state.id}" ${state.id == stateid ? 'selected' : ''}>${state.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <div class="col">
                            <div class="form-group">
                                <div class="form-group__content">
                                    <input type="text" class="form-control" placeholder="<fmt:message key="accountinginit.Zip/PostalCode"> </fmt:message>"
                                           name="billing_addr_zip" value="${zip}"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>

                <fieldset id="taxes">
                    <h4 class="fs-3">
                        <c:if test="${VatCountry}"><fmt:message key="accountinginit.TaxSettings"> </fmt:message> </c:if>
                        <c:if test="${!VatCountry}"><fmt:message key="accountinginit.doYouCollectTax?"> </fmt:message> </c:if>
                    </h4>
                    <c:if test="${VatCountry}">
                        <div class="grid-row gcc-tax-setup">
                            <div class="form-gro up col-4">
                                <label class="form-group__label"><fmt:message
                                        key="accountinginit.IsYourBusinessRegisteredForVAT?"> </fmt:message></label>
                                <div class="form-group__content stack-x">
                                    <label class="control control--radio">
                                        <input checked id="tax_registered" name="tax_registered" type="radio" value="REGISTERED" onclick="onChangeVATRegister(true)">
                                        <span class="control__indicator"></span>
                                        <span class="control__description"><fmt:message key="accountinginit.yes"> </fmt:message></span>
                                    </label>

                                    <label class="control control--radio">
                                        <input id="non_tax_registered" name="tax_registered" type="radio" value="NON_REGISTERED" onclick="onChangeVATRegister(false)">
                                        <span class="control__indicator"></span>
                                        <span class="control__description"><fmt:message key="accountinginit.no"> </fmt:message></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <%--FOR GCC Countries--%>
                    <c:if test="${isGccCountry}">
                        <div class="grid-row gcc-vat-register">
                            <div class="form-group col-6">
                                <label class="form-group__label"><fmt:message key="accountinginit.TaxDisplayName"> </fmt:message><em style="color: red; font-size: 13px">*</em></label>
                                <div class="form-group__content">
                                    <input type="text" class="form-control" placeholder="eg. TRN" name="tax_display_name"
                                           value="${tax_display_name != null ? tax_display_name : 'TRN'}"/>
                                </div>
                            </div>
                            <div class="form-group col-6">
                                <label class="form-group__label"><fmt:message
                                        key="accountinginit.TaxIdNumber"> </fmt:message><em
                                        style="color: red; font-size: 13px">*</em></label>
                                <div class="form-group__content">
                                    <input type="text" class="form-control" placeholder="eg. 123456789012345"
                                           name="tax_id_number"
                                           value="${tax_id_number}"/>
                                </div>
                            </div>
                            <c:if test="${isSA}">
                                <div class="form-group col-6">
                                    <label class="form-group__label"><fmt:message
                                            key="accountinginit.TaxRegisterName"> </fmt:message><em
                                            style="color: red; font-size: 13px">*</em></label>
                                    <div class="form-group__content">
                                        <input type="text" class="form-control" placeholder="eg. TIN"
                                               name="tax_register_name"
                                               value="${tax_register_name != null ? tax_register_name : 'TIN'}"/>
                                    </div>
                                </div>
                                <div class="form-group col-6">
                                    <label class="form-group__label"><fmt:message
                                            key="accountinginit.TaxRegisterNumber"> </fmt:message><em
                                            style="color: red; font-size: 13px">*</em></label>
                                    <div class="form-group__content">
                                        <input type="text" class="form-control" placeholder="eg. 1234567890"
                                               name="tax_register_number"
                                               value="${tax_register_number}"/>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${companyCode == 'AE'}">
                                <div class="form-group col-auto">
                                    <label class="from-group__label">&nbsp;</label>
                                    <div class="form-group__content">
                                        <a class="validate-trn-gcc" rel="noopener noreferrer"
                                           href="https://eservices.tax.gov.ae/en-us/trn-verify" target="_blank"
                                           data-test-title="validate-gcc-trn"> Validate TRN </a>
                                    </div>
                                </div>
                            </c:if>
                        </div>

                        <div class="grid-row gcc-vat-register">
                            <div class="form-group col-10">
                                <div class="form-group__label"><fmt:message key="accountinginit.InternationalTrade"> </fmt:message></div>
                                <div class="form-group__content">
                                    <div class="checkbox"><label> <input name="enable_contract_outside" class="ember-checkbox ember-view" type="checkbox"> <fmt:message key="accountinginit.EnableTradeWithContactsOutside"> </fmt:message> ${countryName} </label>
                                        <br/>
                                        <small class="help-block help-block-dark"><fmt:message key="accountinginit.EnableThisOptionIfYouAreDoingBusinessWithOtherGCC/Non-GCC_Ccountries"> </fmt:message></small> <!----> </div>
                                </div>
                            </div>
                        </div>

                        <div class="grid-row gcc-vat-register">
                            <div class="form-group col-4">
                                <div class="form-group__label"><fmt:message key="accountinginit.VatRegisteredOn"> </fmt:message><em style="color: red; font-size: 13px">*</em></div>
                                <div class="form-group__content">
                                    <input placeholder="mm/dd/yyyy" id="vat_registered_on" name="vat_registered_on" type="text" class="form-control">
                                </div>
                            </div>
                        </div>
                        <div class="grid-row gcc-vat-register">
                            <div class="form-group col-4">
                                <div class="form-group__label"><fmt:message key="accountinginit.GenerateFirstTaxReturnFrom"> </fmt:message><em style="color: red; font-size: 13px">*</em></div>
                                <div class="form-group__content">
                                    <input placeholder="mm/dd/yyyy" id="tax_generation_date" name="tax_generation_date" type="text" class="form-control">
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${isUk}">
                        <div class="gcc-vat-register">
                            <div class="grid-row">
                                <div class="form-group col-3">
                                    <label class="form-group__label form-label--required"><fmt:message key="accountinginit.VATRegistrationNumber"> </fmt:message></label>
                                    <div class="form-group__content">
                                        <input type="text" class="form-control" placeholder="eg. VAT" name="tax_display_name"
                                               value="${tax_display_name != null ? tax_display_name : 'VAT'}"/>
                                    </div>
                                </div>
                                <div class="form-group col-6">
                                    <label class="form-group__label"> </label>
                                    <div class="form-group__content">
                                        <input type="text" class="form-control" placeholder="eg. 123456789012345" name="tax_id_number"
                                              value="${tax_id_number}"/>
                                    </div>
                                </div>
                            </div>
                            <div class="grid-row">
                                <div class="form-group">
                                    <div class="form-group__content col">
                                        <div class="checkbox"><label> <input name="enable_contract_outside" class="ember-checkbox ember-view" type="checkbox"> <span style="padding: 5px"><fmt:message key="accountinginit.ImportExportGoodsFromOtherCountries"> </fmt:message></span></label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%--NON VAT Registered Countries--%>
                    <c:if test="${!VatCountry}">
                        <div class="grid-row">
                            <div class="form-group col-6">
                                <label class="form-group__label"><fmt:message key="accountinginit.TaxName"> </fmt:message></label>
                                <div class="form-group__content">
                                    <input type="text" class="form-control" placeholder="<fmt:message key="accountinginit.egStandardRate"> </fmt:message>" name="tax_name"
                                           value="${tax_name}"/>
                                </div>
                            </div>
                            <div class="form-group col-auto">
                                <label class="form-group__label"><fmt:message key="accountinginit.Rate"> </fmt:message></label>
                                <div class="form-group__content">
                                    <input type="number" class="form-control" placeholder="<fmt:message key="accountinginit.eg.10"> </fmt:message>" name="tax_percent"
                                           value="${tax_percent}"/>
                                </div>
                            </div>
                            <div class="form-group col-auto">
                                <label class="form-group__label">&nbsp;</label>
                                <div class="form-group__content">
                                    <a class="btn btn--icon" onclick="return addTaxRow()">
                                        <svg class="icon--plus">
                                            <use href="/mainStyles/new-ui/icons/sprite__panels.svg#plus"></use>
                                        </svg>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </fieldset>
            </div>
        </div>

        <div class="features-list__main-footer">
            <div class="btns-group">
                <%--<button class="btn btn--default btn--outline"><span>Cancel</span></button>--%>
                <button class="btn btn--primary" onclick="return openSecondPart();"><span><fmt:message key="accountinginit.SaveAndContinue"> </fmt:message></span>
                </button>
            </div>
            <div class="footnote">
                <svg class="icon--info">
                    <use href="/mainStyles/new-ui/icons/sprite__panels.svg#info"></use>
                </svg>
                <span><fmt:message key="accountinginit.YouCanAlwaysChangeYourPreferencesLaterInSettings"> </fmt:message></span>
            </div>
        </div>
    </main>

    <main id="secondpart">
        <div class="features-list__heading">
            <h2 class="features-list__title">
                <fmt:message key="accountinginit.EnableTheModulesRequiredForYourBusiness"> </fmt:message>
            </h2>
            <h3 class="features-list__sub-title">
                <fmt:message key="accountinginit.InvoicesCreditNotesExpensesCustomersAndMoreAreAvailableByDefaultInAccounting"> </fmt:message>
            </h3>
        </div>

        <div class="features-list__main-body">
            <div class="content-holder">
                <div class="content-holder">
                    <div class="col-8">
                        <div class="panel--bordered feature-panel">
                            <div class="panel__heading">
                                <div class="panel__title">
                                    <fmt:message key="accountinginit.SalesQuote"> </fmt:message>
                                </div>
                                <div class="panel__heading-actions">
                                    <div class="switch">
                                        <label>
                                            <span></span>
                                            <input type="checkbox" name="modules" value="SALES_QUOTES" checked="true"/>
                                            <span class="lever"></span>
                                            <span></span>
                                        </label>
                                        <span class="material-label"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel__body">
                                <fmt:message key="accountinginit.AllowsProspectiveBuyer"> </fmt:message>
                            </div>
                        </div>

                        <div class="panel--bordered feature-panel">
                            <div class="panel__heading">
                                <div class="panel__title">
                                    <fmt:message key="accountinginit.SalesOrders"> </fmt:message>
                                </div>
                                <div class="panel__heading-actions">
                                    <div class="switch">
                                        <label>
                                            <span></span>
                                            <input type="checkbox" name="modules" value="SALES_ORDERS"/>
                                            <span class="lever"></span>
                                            <span></span>
                                        </label>
                                        <span class="material-label"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel__body">
                                <fmt:message key="accountinginit.TheSalesOrderIsAConfirmationDocumentSentToTheCustomersBeforeDeliveringTheGoodsOrServices"> </fmt:message>
                            </div>
                        </div>

                        <div class="panel--bordered feature-panel">
                            <div class="panel__heading">
                                <div class="panel__title">
                                    <fmt:message key="accountinginit.RecurringInvoice"> </fmt:message>
                                </div>
                                <div class="panel__heading-actions">
                                    <div class="switch">
                                        <label>
                                            <span></span>
                                            <input type="checkbox" name="modules" value="RECCURING_INVOICES"/>
                                            <span class="lever"></span>
                                            <span></span>
                                        </label>
                                        <span class="material-label"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel__body">
                                <fmt:message key="accountinginit.NeedToSendOutInvoicesOnARegularBasis"> </fmt:message>
                                ${productname} <fmt:message key="accountinginit.makesItEasyToSetTheUpAutomatically"> </fmt:message>
                            </div>
                        </div>

                        <div class="panel--bordered feature-panel">
                            <div class="panel__heading">
                                <div class="panel__title">
                                    <fmt:message key="accountinginit.TimesheetInvoice"> </fmt:message>
                                </div>
                                <div class="panel__heading-actions">
                                    <div class="switch">
                                        <label>
                                            <span></span>
                                            <input type="checkbox" name="modules" value="TIMESHEET_INVOICES"/>
                                            <span class="lever"></span>
                                            <span></span>
                                        </label>
                                        <span class="material-label"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel__body">
                                <fmt:message key="accountinginit.TimesheetInvoiceIsUsedToBillCustomersBasedOnEmployeesWorkedHoursOnCustomersProjects"> </fmt:message>
                            </div>
                        </div>

                        <div class="panel--bordered feature-panel">
                            <div class="panel__heading">
                                <div class="panel__title">
                                    <fmt:message key="accountinginit.FixedAssets"> </fmt:message>
                                </div>
                                <div class="panel__heading-actions">
                                    <div class="switch">
                                        <label>
                                            <span></span>
                                            <input type="checkbox" name="modules" value="FIXED_ASSESTS" checked="true"/>
                                            <span class="lever"></span>
                                            <span></span>
                                        </label>
                                        <span class="material-label"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel__body">
                                <fmt:message key="accountinginit.AFixedAssetIsALong-termTangiblePieceOfProperty"> </fmt:message>
                            </div>
                        </div>

                        <div class="panel--bordered feature-panel">
                            <div class="panel__heading">
                                <div class="panel__title">
                                    <fmt:message key="accountinginit.PurchaseOrders"> </fmt:message>
                                </div>
                                <div class="panel__heading-actions">
                                    <div class="switch">
                                        <label>
                                            <span></span>
                                            <input type="checkbox" name="modules" value="PURCHASE_ORDERS"
                                                   checked="true"/>
                                            <span class="lever"></span>
                                            <span></span>
                                        </label>
                                        <span class="material-label"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel__body">
                                <fmt:message key="accountinginit.PurchaseOrderFunctionality"> </fmt:message>
                            </div>
                        </div>

                        <div class="panel--bordered feature-panel">
                            <div class="panel__heading">
                                <div class="panel__title">
                                    <fmt:message key="accountinginit.RecurringBills"> </fmt:message>
                                </div>
                                <div class="panel__heading-actions">
                                    <div class="switch">
                                        <label>
                                            <span></span>
                                            <input type="checkbox" name="modules" value="RECURRING_BILLS"/>
                                            <span class="lever"></span>
                                            <span></span>
                                        </label>
                                        <span class="material-label"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel__body">
                                <fmt:message key="accountinginit.NeedToSendOutBillsOnARegularBasis"> </fmt:message> ${productname}
                                <fmt:message key="accountinginit.makesItEasyToSetThemUpAutomatically"> </fmt:message>
                            </div>
                        </div>

                        <div class="panel--bordered feature-panel">
                            <div class="panel__heading">
                                <div class="panel__title">
                                    <fmt:message key="accountinginit.InventoryManagement"> </fmt:message>
                                </div>
                                <div class="panel__heading-actions">
                                    <div class="switch">
                                        <label>
                                            <span></span>
                                            <input type="checkbox" name="modules" value="INVENTORY_MANAGEMENT"
                                                   checked="true"/>
                                            <span class="lever"></span>
                                            <span></span>
                                        </label>
                                        <span class="material-label"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel__body">
                                <fmt:message key="accountinginit.ManageYourStockwith"> </fmt:message> ${productname}
                                <fmt:message key="accountinginit.'sSimpleInventoryManagementTools"> </fmt:message>
                            </div>
                        </div>

                        <div class="panel--bordered feature-panel">
                            <div class="panel__heading">
                                <div class="panel__title">
                                    <fmt:message key="accountinginit.RFQ(RequestForQuotes)"> </fmt:message>
                                </div>
                                <div class="panel__heading-actions">
                                    <div class="switch">
                                        <label>
                                            <span></span>
                                            <input type="checkbox" name="modules" value="REQUEST_FOR_QUOTES"/>
                                            <span class="lever"></span>
                                            <span></span>
                                        </label>
                                        <span class="material-label"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel__body">
                                <fmt:message key="accountinginit.RFQIsAStandardBusinessProcess"> </fmt:message>
                            </div>
                        </div>

                        <div class="panel--bordered feature-panel">
                            <div class="panel__heading">
                                <div class="panel__title">
                                    <fmt:message key="accountinginit.RFP(RequestForPurchases)"> </fmt:message>
                                </div>
                                <div class="panel__heading-actions">
                                    <div class="switch">
                                        <label>
                                            <span></span>
                                            <input type="checkbox" name="modules" value="REQUEST_FOR_PURCHASES"/>
                                            <span class="lever"></span>
                                            <span></span>
                                        </label>
                                        <span class="material-label"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel__body">
                                <fmt:message key="accountinginit.ARequestSentInternallyWithinACompany"> </fmt:message>
                            </div>
                        </div>

                        <div class="panel--bordered feature-panel">
                            <div class="panel__heading">
                                <div class="panel__title">
                                    <fmt:message key="accountinginit.Production"> </fmt:message>
                                </div>
                                <div class="panel__heading-actions">
                                    <div class="switch">
                                        <label>
                                            <span></span>
                                            <input type="checkbox" name="modules" value="PRODUCTION"/>
                                            <span class="lever"></span>
                                            <span></span>
                                        </label>
                                        <span class="material-label"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel__body">
                                <fmt:message key="accountinginit.ProductionDescription"> </fmt:message>
                            </div>
                        </div>

                        <%--                        <div class="panel--bordered feature-panel">--%>
                        <%--                            <div class="panel__heading">--%>
                        <%--                                <div class="panel__title">--%>
                        <%--                                    <fmt:message key="accountinginit.Consignments"> </fmt:message>--%>
                        <%--                                </div>--%>
                        <%--                                <div class="panel__heading-actions">--%>
                        <%--                                    <div class="switch">--%>
                        <%--                                        <label>--%>
                        <%--                                            <span></span>--%>
                        <%--                                            <input type="checkbox" name="modules" value="CONSIGNMENTS"/>--%>
                        <%--                                            <span class="lever"></span>--%>
                        <%--                                            <span></span>--%>
                        <%--                                        </label>--%>
                        <%--                                        <span class="material-label"></span>--%>
                        <%--                                    </div>--%>
                        <%--                                </div>--%>
                        <%--                            </div>--%>
                        <%--                            <div class="panel__body">--%>
                        <%--                                <fmt:message key="accountinginit.TheActOfConsigning"> </fmt:message>--%>
                        <%--                            </div>--%>
                        <%--                        </div>--%>

                        <%--                        <div class="panel--bordered feature-panel">--%>
                        <%--                            <div class="panel__heading">--%>
                        <%--                                <div class="panel__title">--%>
                        <%--                                    <fmt:message key="accountinginit.Storefront"> </fmt:message>--%>
                        <%--                                </div>--%>
                        <%--                                <div class="panel__heading-actions">--%>
                        <%--                                    <div class="switch">--%>
                        <%--                                        <label>--%>
                        <%--                                            <span></span>--%>
                        <%--                                            <input type="checkbox" name="modules" value="CONSIGNMENTS"/>--%>
                        <%--                                            <span class="lever"></span>--%>
                        <%--                                            <span></span>--%>
                        <%--                                        </label>--%>
                        <%--                                        <span class="material-label"></span>--%>
                        <%--                                    </div>--%>
                        <%--                                </div>--%>
                        <%--                            </div>--%>
                        <%--                            <div class="panel__body">--%>
                        <%--                                <fmt:message key="accountinginit.StorefrontDescription"> </fmt:message>--%>
                        <%--                            </div>--%>
                        <%--                        </div>--%>

                    </div>
                </div>
            </div>
        </div>
        </div>

        <div class="features-list__main-footer">
            <div class="btns-group">
                <button class="btn btn--default btn--outline" onclick="return openFirstPart();"><span><fmt:message
                        key="accountinginit.back"> </fmt:message></span>
                </button>
                <button class="btn btn--primary" onclick="return openThirdPart();"><span><fmt:message key="accountinginit.SaveAndContinue"> </fmt:message></span></button><%-- onclick="$('form').submit();"--%>
            </div>
            <div class="footnote">
                <svg class="icon--info">
                    <use href="/mainStyles/new-ui/icons/sprite__panels.svg#info"></use>
                </svg>
                <span><fmt:message key="accountinginit.YouCanAlwaysChangeYourPreferencesLaterInSettings"> </fmt:message></span>
            </div>
        </div>
    </main>

    <main id="thirdpart">
        <div class="features-list__heading">
            <h2 class="features-list__title">
                <fmt:message key="accountinginit.ConfigurePaymentGatewayToGetPaidOnlineQuicklyAndEasily"> </fmt:message>
            </h2>
            <h3 class="features-list__sub-title mt-2">
                <fmt:message key="accountinginit.AddAPaymentServiceToYourInvoices"> </fmt:message>
            </h3>
        </div>

        <div class="features-list__main-body">
            <div class="config-paid">
                <div class="config-paid__options">
                    <div class="form-row">
                        <div class="col-6">
                            <a href="#" class="config-paid-option" onclick="return paypalSelected();">
                                <span class="config-paid-option__icon">
                                    <svg viewBox="0 0 124 33" preserveAspectRatio="xMinYMin slice"><path fill="#253B80"
                                                                                                         d="M46.211 6.749h-6.839a.95.95 0 0 0-.939.802l-2.766 17.537a.57.57 0 0 0 .564.658h3.265a.95.95 0 0 0 .939-.803l.746-4.73a.95.95 0 0 1 .938-.803h2.165c4.505 0 7.105-2.18 7.784-6.5.306-1.89.013-3.375-.872-4.415-.972-1.142-2.696-1.746-4.985-1.746zM47 13.154c-.374 2.454-2.249 2.454-4.062 2.454h-1.032l.724-4.583a.57.57 0 0 1 .563-.481h.473c1.235 0 2.4 0 3.002.704.359.42.469 1.044.332 1.906zm19.654-.079h-3.275a.57.57 0 0 0-.563.481l-.145.916-.229-.332c-.709-1.029-2.29-1.373-3.868-1.373-3.619 0-6.71 2.741-7.312 6.586-.313 1.918.132 3.752 1.22 5.031.998 1.176 2.426 1.666 4.125 1.666 2.916 0 4.533-1.875 4.533-1.875l-.146.91a.57.57 0 0 0 .562.66h2.95a.95.95 0 0 0 .939-.803l1.77-11.209a.568.568 0 0 0-.561-.658zm-4.565 6.374c-.316 1.871-1.801 3.127-3.695 3.127-.951 0-1.711-.305-2.199-.883-.484-.574-.668-1.391-.514-2.301.295-1.855 1.805-3.152 3.67-3.152.93 0 1.686.309 2.184.892.499.589.697 1.411.554 2.317zm22.007-6.374h-3.291a.954.954 0 0 0-.787.417l-4.539 6.686-1.924-6.425a.953.953 0 0 0-.912-.678h-3.234a.57.57 0 0 0-.541.754l3.625 10.638-3.408 4.811a.57.57 0 0 0 .465.9h3.287a.949.949 0 0 0 .781-.408l10.946-15.8a.57.57 0 0 0-.468-.895z"/><path
                                            fill="#179BD7"
                                            d="M94.992 6.749h-6.84a.95.95 0 0 0-.938.802l-2.766 17.537a.569.569 0 0 0 .562.658h3.51a.665.665 0 0 0 .656-.562l.785-4.971a.95.95 0 0 1 .938-.803h2.164c4.506 0 7.105-2.18 7.785-6.5.307-1.89.012-3.375-.873-4.415-.971-1.142-2.694-1.746-4.983-1.746zm.789 6.405c-.373 2.454-2.248 2.454-4.062 2.454h-1.031l.725-4.583a.568.568 0 0 1 .562-.481h.473c1.234 0 2.4 0 3.002.704.359.42.468 1.044.331 1.906zm19.653-.079h-3.273a.567.567 0 0 0-.562.481l-.145.916-.23-.332c-.709-1.029-2.289-1.373-3.867-1.373-3.619 0-6.709 2.741-7.311 6.586-.312 1.918.131 3.752 1.219 5.031 1 1.176 2.426 1.666 4.125 1.666 2.916 0 4.533-1.875 4.533-1.875l-.146.91a.57.57 0 0 0 .564.66h2.949a.95.95 0 0 0 .938-.803l1.771-11.209a.571.571 0 0 0-.565-.658zm-4.565 6.374c-.314 1.871-1.801 3.127-3.695 3.127-.949 0-1.711-.305-2.199-.883-.484-.574-.666-1.391-.514-2.301.297-1.855 1.805-3.152 3.67-3.152.93 0 1.686.309 2.184.892.501.589.699 1.411.554 2.317zm8.426-12.219l-2.807 17.858a.569.569 0 0 0 .562.658h2.822c.469 0 .867-.34.939-.803l2.768-17.536a.57.57 0 0 0-.562-.659h-3.16a.571.571 0 0 0-.562.482z"/><path
                                            fill="#253B80"
                                            d="M7.266 29.154l.523-3.322-1.165-.027H1.061L4.927 1.292a.316.316 0 0 1 .314-.268h9.38c3.114 0 5.263.648 6.385 1.927.526.6.861 1.227 1.023 1.917.17.724.173 1.589.007 2.644l-.012.077v.676l.526.298a3.69 3.69 0 0 1 1.065.812c.45.513.741 1.165.864 1.938.127.795.085 1.741-.123 2.812-.24 1.232-.628 2.305-1.152 3.183a6.547 6.547 0 0 1-1.825 2c-.696.494-1.523.869-2.458 1.109-.906.236-1.939.355-3.072.355h-.73c-.522 0-1.029.188-1.427.525a2.21 2.21 0 0 0-.744 1.328l-.055.299-.924 5.855-.042.215c-.011.068-.03.102-.058.125a.155.155 0 0 1-.096.035H7.266z"/><path
                                            fill="#179BD7"
                                            d="M23.048 7.667c-.028.179-.06.362-.096.55-1.237 6.351-5.469 8.545-10.874 8.545H9.326c-.661 0-1.218.48-1.321 1.132L6.596 26.83l-.399 2.533a.704.704 0 0 0 .695.814h4.881c.578 0 1.069-.42 1.16-.99l.048-.248.919-5.832.059-.32c.09-.572.582-.992 1.16-.992h.73c4.729 0 8.431-1.92 9.513-7.476.452-2.321.218-4.259-.978-5.622a4.667 4.667 0 0 0-1.336-1.03z"/><path
                                            fill="#222D65"
                                            d="M21.754 7.151a9.757 9.757 0 0 0-1.203-.267 15.284 15.284 0 0 0-2.426-.177h-7.352a1.172 1.172 0 0 0-1.159.992L8.05 17.605l-.045.289a1.336 1.336 0 0 1 1.321-1.132h2.752c5.405 0 9.637-2.195 10.874-8.545.037-.188.068-.371.096-.55a6.594 6.594 0 0 0-1.017-.429 9.045 9.045 0 0 0-.277-.087z"/><path
                                            fill="#253B80"
                                            d="M9.614 7.699a1.169 1.169 0 0 1 1.159-.991h7.352c.871 0 1.684.057 2.426.177a9.757 9.757 0 0 1 1.481.353c.365.121.704.264 1.017.429.368-2.347-.003-3.945-1.272-5.392C20.378.682 17.853 0 14.622 0h-9.38c-.66 0-1.223.48-1.325 1.133L.01 25.898a.806.806 0 0 0 .795.932h5.791l1.454-9.225 1.564-9.906z"/></svg>
                                </span>
                                <span class="config-paid-option__caption">
                                    <fmt:message key="accountinginit.PayPal"> </fmt:message>
                                </span>
                            </a>
                        </div>
                        <div class="col-6">
                            <a href="#" class="config-paid-option config-paid-option--stripe" onclick="return submitForm('stripe')">
                                <span class="config-paid-option__icon">
                                    <svg viewBox="0 0 468 222.5" preserveAspectRatio="xMinYMin slice"
                                         class="icon--stripe">
                                        <path class="st0"
                                              d="M414 113.4c0-25.6-12.4-45.8-36.1-45.8-23.8 0-38.2 20.2-38.2 45.6 0 30.1 17 45.3 41.4 45.3 11.9 0 20.9-2.7 27.7-6.5v-20c-6.8 3.4-14.6 5.5-24.5 5.5-9.7 0-18.3-3.4-19.4-15.2h48.9c0-1.3.2-6.5.2-8.9zm-49.4-9.5c0-11.3 6.9-16 13.2-16 6.1 0 12.6 4.7 12.6 16h-25.8zM301.1 67.6c-9.8 0-16.1 4.6-19.6 7.8l-1.3-6.2h-22v116.6l25-5.3.1-28.3c3.6 2.6 8.9 6.3 17.7 6.3 17.9 0 34.2-14.4 34.2-46.1-.1-29-16.6-44.8-34.1-44.8zm-6 68.9c-5.9 0-9.4-2.1-11.8-4.7l-.1-37.1c2.6-2.9 6.2-4.9 11.9-4.9 9.1 0 15.4 10.2 15.4 23.3 0 13.4-6.2 23.4-15.4 23.4zM223.8 61.7l25.1-5.4V36l-25.1 5.3zM223.8 69.3h25.1v87.5h-25.1zM196.9 76.7l-1.6-7.4h-21.6v87.5h25V97.5c5.9-7.7 15.9-6.3 19-5.2v-23c-3.2-1.2-14.9-3.4-20.8 7.4zM146.9 47.6l-24.4 5.2-.1 80.1c0 14.8 11.1 25.7 25.9 25.7 8.2 0 14.2-1.5 17.5-3.3V135c-3.2 1.3-19 5.9-19-8.9V90.6h19V69.3h-19l.1-21.7zM79.3 94.7c0-3.9 3.2-5.4 8.5-5.4 7.6 0 17.2 2.3 24.8 6.4V72.2c-8.3-3.3-16.5-4.6-24.8-4.6C67.5 67.6 54 78.2 54 95.9c0 27.6 38 23.2 38 35.1 0 4.6-4 6.1-9.6 6.1-8.3 0-18.9-3.4-27.3-8v23.8c9.3 4 18.7 5.7 27.3 5.7 20.8 0 35.1-10.3 35.1-28.2-.1-29.8-38.2-24.5-38.2-35.7z"/>
                                    </svg>
                                </span>
                                <span class="config-paid-option__caption">
                                    Stripe
                                </span>
                            </a>
                        </div>
                    </div>

                    <div class="form-group" id="paypal-account">
                        <label class="form-group__label"><fmt:message key="accountinginit.YourPayPalAccount"> </fmt:message></label>
                        <div class="form-group__content">
                            <input type="text" class="form-control" name="paypal-account"/>
                        </div>
                    </div>

                </div>
                <figure class="config-paid__scheme">
                    <img src="/mainStyles/new-ui/images/notebook.png" alt="image">
                    <figcaption>
                        <fmt:message key="accountinginit.OnlinePaymentDescription"> </fmt:message>
                    </figcaption>
                </figure>
            </div>
        </div>

        <div class="features-list__main-footer">
            <div class="grid-row">
                <div class="btns-group col">
                    <button class="btn btn--default btn--outline" onclick="return openSecondPart();"><span><fmt:message key="accountinginit.Cancel"> </fmt:message></span></button>
                    <button class="btn btn--primary"><span><fmt:message key="accountinginit.SaveAndContinue"> </fmt:message></span></button>
                </div>
            </div>

            <div class="footnote">
                <svg class="icon--info">
                    <use href="/mainStyles/new-ui/icons/sprite__panels.svg#info"></use>
                </svg>
                <span><fmt:message key="accountinginit.YouCanAlwaysChangeYourPreferencesLaterInSettings"> </fmt:message></span>
            </div>
        </div>
    </main>
</form>


<!--SCRIPTS-->
<script>

    var NUMERIC_REG = /^\d*[0-9](|.\d*[0-9]|,\d*[0-9])?$/;

    $("#secondpart").hide();//toggleClass('d-none');
    $("#thirdpart").hide();//toggleClass('d-none');
    $("#paypal-account").hide();//toggleClass('d-none');

    jQuery(document).ready(function () {
        $('select').material_select();

        $('#vat_registered_on').datepicker({
            changeMonth: true,
            changeYear: true,
            onSelect: function (selectedDate) {
                var instance = $('#tax_generation_date'),
                    data = instance.data("datepicker"),
                    date = $.datepicker.parseDate(data.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, data.settings);
                instance.datepicker("option", "minDate", date);
            }
        });

        $('#tax_generation_date').datepicker({
            changeMonth: true,
            changeYear: true
        });

        $('input[name=tax_id_number]').keyup(function () {
            $('span.error-keyup-1').remove();
            var inputVal = $(this).val();

            if (!NUMERIC_REG.test(inputVal)) {
                $(this).after('<span class="invalid error-keyup-1"><fmt:message key="accountinginit.NumericCharactersOnly"> </fmt:message></span>');
            }
        });
        $('input[name=tax_register_number]').keyup(function () {
            $('span.error-keyup-1').remove();
            var inputVal = $(this).val();

            if (!NUMERIC_REG.test(inputVal)) {
                $(this).after('<span class="invalid error-keyup-1"><fmt:message key="accountinginit.NumericCharactersOnly"> </fmt:message></span>');
            }
        });
    });

    function openFirstPart() {
        $("#secondpart").hide();
        $("#orgdetails").addClass("status--current");
        $("#orgdetails").removeClass("status--passed");

        $("#features").addClass("status--not-passed");
        $("#features").removeClass("status--current");
        $("#features").removeClass("status--passed");
        $("#firstpart").show();
        return false;
    }

    function openSecondPart() {

        /**
         * Validation of the Tax settings
         */
        if ($('input[name=tax_registered]:checked').val() == 'REGISTERED') {
            var errors = 0;
            var taxIdNumber = $('input[name=tax_id_number]');
            var taxIdNumberValue = taxIdNumber.val();
            var tinNumber = $('input[name=tax_register_number]');
            var tinNumberValue = tinNumber.val();
            var isUk = ${isUk};
            var isSA = ${isSA};
            debugger;
            if (!taxIdNumberValue) {
                taxIdNumber.addClass("invalid");
                errors += 1;
            } else if (!NUMERIC_REG.test(taxIdNumberValue)) {
                $('span.error-keyup-1').remove();
                taxIdNumber.after('<span class="invalid error-keyup-1">Numeric characters only.</span>');
                errors += 1;
            } else if (isUk) {
                if (taxIdNumberValue.length !== 9 && taxIdNumberValue.length !== 12) {
                    $('span.error-keyup-1').remove();
                    taxIdNumber.after('<span class="invalid error-keyup-1">Invalid VRN - VRN parameters should be 9 or 12 digits.</span>');
                    errors += 1;
                } else {
                    taxIdNumber.removeClass("invalid");
                }
            } else if (taxIdNumberValue.length !== 15) {
                $('span.error-keyup-1').remove();
                taxIdNumber.after('<span class="invalid error-keyup-1">Please enter your 15 digit numeric TRN.</span>');
                errors += 1;
            } else {
                taxIdNumber.removeClass("invalid");
            }
            if (isSA) {
                if (!tinNumberValue) {
                    tinNumber.addClass("invalid");
                    errors += 1;
                } else if (!NUMERIC_REG.test(taxIdNumberValue)) {
                    $('span.error-keyup-1').remove();
                    tinNumber.after('<span class="invalid error-keyup-1">Numeric characters only.</span>');
                    errors += 1;
                } else if (tinNumberValue.length !== 10) {
                    $('span.error-keyup-1').remove();
                    tinNumber.after('<span class="invalid error-keyup-1">Please enter your 10 digit numeric TIN.</span>');
                    errors += 1;
                } else {
                    tinNumber.removeClass("invalid");
                }
                if (!$('input[name=tax_register_name]').val()) {
                    $('input[name=tax_register_name]').addClass("invalid");
                    errors += 1;
                } else {
                    $('input[name=tax_register_name]').removeClass("invalid");
                }
            }

            if (!$('input[name=tax_display_name]').val()) {
                $('input[name=tax_display_name]').addClass("invalid");
                errors += 1;
            } else {
                $('input[name=tax_display_name]').removeClass("invalid");
            }
            if (${isGccCountry}){
                if (!$('input[name=vat_registered_on]').val()) {
                    $('input[name=vat_registered_on]').addClass("invalid");
                    errors += 1;
                } else {
                    $('input[name=vat_registered_on]').removeClass("invalid");
                }
                if (!$('input[name=tax_generation_date]').val()) {
                    $('input[name=tax_generation_date]').addClass("invalid");
                    errors += 1;
                } else {
                    $('input[name=tax_generation_date]').removeClass("invalid");
                }
            }

            if (errors > 0) {
                return false;
            }
        }

        if ($("select#conversion_date_year option").filter(":selected").val() == '-1' || $("select#conversion_date_month option").filter(":selected").val() == '-1') {
            // alert('Please set BOOKS START DATE.');
            if ($("select#conversion_date_year option").filter(":selected").val() == '-1') {
                $("#yearWrapper").addClass('x-form-invalid');
            }
            if ($("select#conversion_date_month option").filter(":selected").val() == '-1') {
                $("#monthWrapper").addClass('x-form-invalid');
            }
            // Materialize.toast({html: '<i class=\'tick-toast__icon ficon--check\'></i>', classes:'tick-toast tick-toast--error'});
            Materialize.toast('Please set BOOKS START DATE.', 3000, 'tick-toast tick-toast--error');
            return false;
        } else {

            $("#yearWrapper").removeClass('x-form-invalid');
            $("#monthWrapper").removeClass('x-form-invalid');

            $("#firstpart").hide();
            $("#orgdetails").removeClass("status--current");
            $("#orgdetails").addClass("status--passed");

            $("#thirdpart").hide();
            $("#paymentoptions").addClass("status--not-passed");
            $("#paymentoptions").removeClass("status--current");

            $("#features").removeClass("status--not-passed");
            $("#features").addClass("status--current");
            $("#secondpart").show();
            return false;
        }
    }

    function openThirdPart() {

        $("#firstpart").hide();
        $("#secondpart").hide();

        $("#features").removeClass("status--current");
        $("#features").addClass("status--passed");

        $("#paymentoptions").removeClass("status--not-passed");
        $("#paymentoptions").addClass("status--current");
        $("#thirdpart").show();

        return false;
    }
    /*$('#billing_addr_state').on('contentChanged', function() {
        $(this).material_select();
    });*/

    function updateCountryStates() {
        $.ajax({
            url: "/services/api/v2/countries/" + $("select#billing_addr_country option").filter(":selected").val() + "/states",
            type: 'GET',
            success: function (response) {
                /*alert(response.data.list.length);
                $.each(response.data.list, function (item) {
                    console.log(this.id );
                });*/
                if (response.data.list == 0) {
                    $('#state-wrapper').hide();
                } else {
                    $('#state-wrapper').show();
                    var $states = $("select[name='billing_addr_state']");
                    // $states.material_select("destroy");
                    $states.material_select('destroy');

                    /*$("#billing_addr_state option")*/
                    $states.find('option').each(function () {
                        $(this).remove();
                    });

                    /*
                    $states.find('option')
                        .remove()
                        .end()
                        .append('<option value="-1">Please Select</option>')
                        .val('whatever');*/

                    var newOpt = $("<option>").attr("value", "-1").text("State/Province");
                    $states.append(newOpt);
                    $.each(response.data.list, function (item) {
                        // console.log(this.id + " " + this.title);
                        // $("#billing_addr_state").append('<option value="option6">option6</option>');
                        // $("#billing_addr_state").append("<option value='item" + this.id + "'>" + this.title + "</option>").val(this.title);
                        var newOpt = $("<option>").attr("value", this.id).text(this.title);
                        $states.append(newOpt);
                        // append($("<option></option>").val().text());
                    });
                    /*$states.find('option')
                        .remove()
                        .end()
                        .append('<option></option>')
                        .val('-1').text("Please Select");*/

                    // $('select')
                    // $("#billing_addr_state").trigger('contentChanged');
                    $states.material_select('destroy');
                    $states.material_select();
                    //$states.closest('.input-field').children('span.caret').remove();
                    // $("select").material_select();

                }
            },
            error: function (error) {
                alert(error);
            },
            beforeSend: setHeader
        });

        function setHeader(xhr) {
            xhr.setRequestHeader('accessToken', '22cfd8ef-2678-47ea-b750-738c59615598');
            xhr.setRequestHeader('x-auth', readCookie("SESSION_ID"));
        }

    }

    function readCookie(name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
        }
        return null;
    }

    var incr = 0;

    function addTaxRow() {
        incr++;
        var rowid = 'tax_' + incr;
        var newDiv = $("<div class=\"grid-row\" id='" + rowid + "'>\n" +
            "                        <div class=\"form-group col-6\">\n" +
            "                            <label class=\"form-group__label\">Tax Name</label>\n" +
            "                            <div class=\"form-group__content\">\n" +
            "                                <input type=\"text\" class=\"form-control\" placeholder=\"eg. Standard Rate\" name=\"tax_name\"/> \n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <div class=\"form-group col-auto\">\n" +
            "                            <label class=\"form-group__label\">Rate [%]</label>\n" +
            "                            <div class=\"form-group__content\">\n" +
            "                                <input type=\"number\" class=\"form-control\" placeholder=\"eg. 10\" name=\"tax_percent\" />\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <div class=\"form-group col-auto\">\n" +
            "                            <label class=\"form-group__label\">&nbsp;</label>\n" +
            "                            <div class=\"form-group__content\">\n" +
            "                                <div class=\"btn btn--icon\" onclick=\"return removeTaxRow('#" + rowid + "');\" >\n" +
            "                                    <svg class=\"icon--trash2\">\n" +
            "                                        <use href=\"/mainStyles/new-ui/icons/sprite__panels.svg#trash2\"></use>\n" +
            "                                    </svg>\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                    </div>");//.append(someText).click(function () { alert("click!"); });
        $("#taxes").append(newDiv);
        /*}
    )*/
    }

    function removeTaxRow(rowId) {
        $(rowId).remove();
    }

    function submitForm(paymentGateway) {
        var paymentGatewayHiddenInput = document.createElement('input');
        paymentGatewayHiddenInput.setAttribute('type', 'hidden');
        paymentGatewayHiddenInput.setAttribute('name', 'payment-gateway');
        paymentGatewayHiddenInput.setAttribute('value', paymentGateway);

        var form = document.getElementById('accounting-form');
        form.appendChild(paymentGatewayHiddenInput);
        // $('form').submit();
        form.submit();
    }

    function paypalSelected() {
        $("#paypal-account").show();
        return false;
    }


    function onChangeVATRegister(registred) {

        if (registred) {
            $('.gcc-vat-register').show();
        } else {
            $('.gcc-vat-register').hide();
        }
    }
</script>
</body>

</html>
