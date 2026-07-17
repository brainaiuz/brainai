<%--
  Created by IntelliJ IDEA.
  User: romeo
  Date: 10/9/12
  Time: 6:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form action="#" method="get" id="newTaskform" name="newTaskform"
      autocomplete="off">
    <div id="topContentContainer" class="inputForm">
        <fieldset class="topColumn">
            <div class="rowFields trioFields">

                <div class="selectBlock">
                    <label for="salesQuoteCustomerLabel">Customer
                        <span>*</span>:</label>
                    <input type="hidden" style="margin-top: 8px; width: 100%; display:block" tabindex="-1"
                           class="select2-offscreen salesQuoteCustomer" name="salesQuoteCustomer"/>

                    <div id="customerError" class="errorMessage" style="display: none;">Please, choose Customer</div>
                </div>
                <div class="selectBlock">
                    <label id="dateLabel" for="salesQuoteDate">Date
                        <span>*</span>:</label>
                    <input name="salesQuoteDate" id="salesQuoteDate"
                           type="text"/>

                    <div id="dateError" class="errorMessage" style="display: none;">Please, choose Date</div>
                </div>
                <div class="selectBlock">
                    <label id="dateLabel" for="salesQuoteValidUntil">Valid Until
                        <span>*</span>:</label>
                    <input name="salesQuoteValidUntil" id="salesQuoteValidUntil"
                           type="text"/>

                    <div id="dateUntilError" class="errorMessage" style="display: none;">Please, choose Date</div>
                </div>

                <div class="selectBlock labelLine currencySelect">
                    <label id="salesQuoteCurrencyValueLabel" for="salesQuoteCurrencyValue">${baseCurrency.name} =<span>*</span></label>
                    <select name="salesQuoteCurrency" id="salesQuoteCurrency">
                        <c:forEach items="${currencyItems}" var="currencyItem">
                            <option value="${currencyItem.id}"> ${currencyItem.name}</option>
                        </c:forEach>
                    </select>
                    <input name="salesQuoteCurrencyValue" id="salesQuoteCurrencyValue" type="text"/>

                    <div id="currencyError" class="errorMessage" style="display: none;">Please, choose Currency</div>
                </div>

            </div>


        </fieldset>

        <div class="bottomColumn">
            <div id="dinamicContentSalesQuote" class="rowFields">

                <div class="clear" style="margin-bottom:1em">
                    <a href="#" class="addLink" id="addSalesQuoteFields">+</a><!--<a href="#" class="addLinkText">Add</a>-->
                    <a href="#" class="removeLink" id="removeSalesQuoteFields">-</a><!--<a href="#" class="removeLinkText">Remove</a>-->
                </div>

                <div>
                    <div class="selectBlock">
                        <label for="salesQuoteItemLabel">Item<span>*</span>:</label>
                    </div>

                    <div class="selectBlock">
                        <label for="salesQuoteQtyLabel">Qty<span>*</span>:</label>
                    </div>

                    <div class="selectBlock">
                        <label for="salesQuotePriceLabel">Price<span>*</span>:</label>
                    </div>

                    <div class="selectBlock">
                        <label for="salesQuoteTaxLabel">Tax:</label>
                    </div>
                </div>


                <fieldset class="clear" id="dinamicContentSalesQuotePrototype">
                    <div class="selectBlock">
                        <c:choose>
                            <c:when test="${empty itemItems}">
                                <input type="text" class="salesQuoteItemText" name="salesQuoteItemText"/>
                            </c:when>
                            <c:otherwise>
                                <select name="salesQuoteItemDrop" class="salesQuoteItemDrop">
                                    <option value="">Please Select</option>
                                    <c:forEach items="${itemItems}" var="itemItem">
                                        <option value="${itemItem.id}">${itemItem.name}</option>
                                    </c:forEach>
                                </select>
                            </c:otherwise>
                        </c:choose>


                        <div class="errorMessage itemListError" style="display: none;">Please, choose Item</div>
                    </div>

                    <div class="selectBlock">
                        <input type="text" class="salesQuoteQty"
                               name="salesQuoteQty"/>

                        <div class="errorMessage qtyError" style="display: none;">Please, enter Qty</div>
                    </div>

                    <div class="selectBlock">
                        <input type="text" class="salesQuotePrice"
                               name="salesQuotePrice"/>

                        <div class="errorMessage priceError" style="display: none;">Please, enter Price</div>
                    </div>

                    <div class="selectBlock">
                        <select name="salesQuoteTax" class="salesQuoteTax">
                            <option value="">Please Select</option>
                            <c:forEach items="${taxItems}" var="taxItem">
                                <option percent="${taxItem.taxPercent}"
                                        value="${taxItem.id}">${taxItem.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </fieldset>


            </div>
        </div>

        <div class="selectBlock">

            <table align="right" style="margin-right: 20px;">
                <tr>
                    <td><label for="subTotal">Subtotal:</label></td>
                    <td>
                        <div id="subTotal" class="totalvalue">0.00</div>
                    </td>
                    <input type="hidden" name="salesQuoteSubTotalValue" id="salesQuoteSubTotalValue"
                           value="0"/>
                </tr>
                <tr>
                    <td><label for="taxTotal">Total Tax:</label></td>
                    <td>
                        <div id="taxTotal" class="totalvalue">0.00</div>
                    </td>
                    <input type="hidden" name="salesQuoteTaxTotalValue" id="salesQuoteTaxTotalValue"
                           value="0"/>
                </tr>
                <tr>
                    <td><label id="totalInInvoiceCurrencyLabel" for="totalInInvoiceCurrency">Total(${baseCurrency.name}):</label></td>
                    <td>
                        <div id="totalInInvoiceCurrency" class="totalvalue">0.00</div>
                    </td>
                    <input type="hidden" name="salesQuoteTotalInInvoiceCurrency" id="salesQuoteTotalInInvoiceCurrency"
                           value="0"/>
                </tr>

                <tr>
                    <td><label for="total">Total(${baseCurrency.name}):</label></td>
                    <td>
                        <div id="total" class="totalvalue">0.00</div>
                    </td>
                    <input type="hidden" name="salesQuoteTotalValue" id="salesQuoteTotalValue"
                           value="0"/>
                </tr>

            </table>
        </div>


        <div class="footButtons">
            <input type="hidden" name="saveSalesQuote" id="saveSalesQuote"
                   value="false"/>
            <input type="hidden" name="companyId" id="companyId"
                   value=""/>
            <input class="FormButton Blue" type="submit"
                   id="saveSalesQuoteButton" value="Save"/>
            <input class="FormButton Blue" type="submit"
                   id="cancelSalesQuoteButton" value="Cancel"/>
        </div>
    </div>
</form>