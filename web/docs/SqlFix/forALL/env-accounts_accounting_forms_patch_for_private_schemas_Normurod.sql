-- Company -> 55276 : United Ajyal Majan Aluminum LLC, formId:salequote
insert into "55276".customlayout_new_ui(formid, title, layout) values
('salequote', 'Sale quote form',
'<div class="invoice-block">
    <div id="inv_form_header" class="invoice__options-section section-box section-box--collapsable section-box--collapse box-bg--1">
        <div class="section-box__content">
            <div class="invoice__main-options">
                <div class="group-box group-box--united">
                    <div class="group-box__items">
                        $$input:crmaccount$$
                        <div id="inv_date_range" class="group-box__item invoice__date-due-date dates-range">
                            <div id="inv_date" class="invoice__date">
                                <div class="group-box__item-label">$$label:date$$</div>
                                <div class="group-box__item-content">$$input:date$$</div>
                            </div>
                            <div id="inv_due_date" class="invoice__due-date">
                                <div class="group-box__item-label">$$label:duedate$$</div>
                                <div class="group-box__item-content">$$input:duedate$$</div>
                            </div>
                        </div>
                        $$input:number$$
                        $$input:progressinvoicing$$
                    </div>
                </div>
            </div>
            <div id="inv_moreoptions" class="invoice__more-options section-box__collapsable-content">
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:billaddress$$
                    </div>
                </div>
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:project$$
                        $$input:bank$$
                        $$input:balance$$
                        $$input:pdftemplate$$
                    </div>
                </div>
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:introduction$$
                    </div>
                </div>
                $$input:customfields$$
            </div>
        </div>
        <div class="section-box__footer">
            $$input:showmore$$
        </div>
    </div>
    <div class="section-box">
        <div class="group-box group-box--united">
            <div class="group-box__items">
                $$input:currency$$
                $$input:taxcalc$$
                $$input:pricelevel$$
                $$input:shippingmethod$$
                $$input:manager$$
            </div>
        </div>
    </div>
    <div class="section-box box-bg--1">
        <div class="invoice__products-table">
            <!-- place table here -->
            $$input:itemtable$$
        </div>
    </div>
    <div class="section-group box-bg--2 invoice__footer">
        <div class="section-box invoice__post-fields">
            $$input:attachments$$
            <div class="group-box box-bg--2">
                <div class="group-box__items">
                    $$input:overalldiscount$$
                </div>
            </div>
            <div class="group-box invoice__payment-history">
                <div class="group-box__items">
                    <div id="inv_all_history" class="group-box__item">
                        $$input:allhistorypanel$$
                    </div>
                    <div id="inv_related_links" class="group-box__item">
                        $$input:maillinkpanel$$
                    </div>
                </div>
            </div>
            <div class="form-group inv_payment_instruction">
                <div class="form-group__label">$$label:instruction$$</div>
                <div class="form-group__content">$$input:instruction$$</div>
            </div>
            <div class="invoice__payment-notes inv_payment_instruction">
                $$input:instructionlist$$
                $$input:agreetermspanel$$
            </div>
            <div class="form-group">
                <div class="form-group__label">$$label:notes$$</div>
                <div class="form-group__content">$$input:notes$$</div>
            </div>
        </div>
        <div class="section-box invoice__receipt-block">
            <div class="invoice__subtotal">
                $$input:totalstable$$
            </div>
        </div>
    </div>
</div>
<div class="section-box">

    <div class="btns-group text-right">
        $$input:closeButton$$
        $$input:saveButton$$
        $$input:saveOrderButton$$
        $$input:rejectButton$$
        $$input:picklistButton$$
        $$input:submitToManagerButton$$
        $$input:pdfVersionButton$$
        $$input:convertButton$$
        $$input:saveAndApproveButton$$
        $$input:approveAndSendButton$$
    </div>

</div>'),
('saleinvoice', 'Sale invoice form',
 '<div class="invoice-block">
    <div id="inv_form_header" class="invoice__options-section section-box section-box--collapsable section-box--collapse box-bg--1">
        <div class="section-box__content">
            <div class="invoice__main-options">
                <div class="group-box group-box--united">
                    <div class="group-box__items">
                        $$input:invoicetype$$
                        $$input:crmaccount$$
                        <div id="inv_date_range" class="group-box__item invoice__date-due-date">
                            <div id="inv_date" class="invoice__date">
                                <div class="group-box__item-label">$$label:date$$</div>
                                <div class="group-box__item-content">$$input:date$$</div>
                            </div>
                            <div id="inv_due_date" class="invoice__due-date">
                                <div class="group-box__item-label">$$label:duedate$$</div>
                                <div class="group-box__item-content">$$input:duedate$$</div>
                            </div>
                        </div>
                        $$input:number$$
                    </div>
                </div>
            </div>
            <div id="inv_moreoptions" class="invoice__more-options section-box__collapsable-content">
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:billaddress$$
                    </div>
                </div>
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:mailaddress$$
                    </div>
                </div>
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:project$$
                        $$input:receivablepayable$$
                        $$input:bank$$
                        $$input:balance$$
                        $$input:ponumber$$
                        $$input:pdftemplate$$
                    </div>
                </div>

                <div class="group-box">
                    <div class="group-box__items">
                        $$input:introduction$$
                    </div>
                </div>
                $$input:customfields$$
            </div>
        </div>
        <div class="section-box__footer">
            $$input:showmore$$
        </div>
    </div>
    <div class="section-box">
            $$input:recurringview$$
        <div class="group-box group-box--united">
            <div class="group-box__items">
                $$input:currency$$
                $$input:taxcalc$$
				$$input:clientdiscount$$
                $$input:reference$$
                $$input:quotenumber$$
                $$input:pricelevel$$
                $$input:shippingmethod$$
            </div>
        </div>
    </div>
    <div class="section-box box-bg--1">
        <div class="invoice__products-table">
            <!-- place table here -->
            $$input:itemtable$$
        </div>
    </div>
    $$input:paymentpanel$$
    <div class="section-group box-bg--2 invoice__footer">
        <div class="section-box invoice__post-fields">
            $$input:attachments$$
            <div class="group-box">
                <div class="group-box__items">
                    $$input:paymentexratepanel$$
                    $$input:overalldiscount$$
                    $$input:addBillExpButton$$
                </div>
            </div>
            <div class="group-box invoice__payment-history">
                <div class="group-box__items">
                    <div id="inv_payment_history" class="group-box__item">
                        $$input:paymenthistorypanel$$
                    </div>
                    <div id="inv_all_history" class="group-box__item">
                        $$input:allhistorypanel$$
                    </div>
                    <div id="inv_billable_expense_link" class="group-box__item">
                        $$input:billableexpenselinkpanel$$
                    </div>
                </div>
            </div>
            <div class="form-group inv_payment_instruction">
                <div class="form-group__label">$$label:instruction$$</div>
                <div class="form-group__content">$$input:instruction$$</div>
            </div>
            <div class="inv_payment_instruction">
                <div class="invoice__payment-notes">$$input:instructionlist$$</div>
            </div>
            <div class="form-group">
                <div class="form-group__label">$$label:notes$$</div>
                <div class="form-group__content">$$input:notes$$</div>
            </div>
        </div>
        <div class="section-box invoice__receipt-block">
            <div class="invoice__subtotal">
                $$input:totalstable$$
            </div>
        </div>
    </div>
</div>
<div class="section-box">

    <div class="btns-group text-right">

        $$input:saveButton$$
        $$input:sendToTargetButton$$
        $$input:copyToNewButton$$
        $$input:creditNoteAdd$$
        $$input:generateReceiptButton$$
        $$input:sendReceiptButton$$
        $$input:pdfVersionButton$$
        $$input:excelVersionButton$$
        $$input:saveAndApproveButton$$
        $$input:approveAndSendButton$$
        $$input:assignSerialsButton$$
    </div>

</div>');


-- Company -> 65033 : WUJHA REAL ESTATE DEVELOPERS, formId:saleinvoice
insert into "65033".customlayout_new_ui(formid, title, layout) values
('salequote', 'Sale quote form',
'<div class="invoice-block">
    <div id="inv_form_header" class="invoice__options-section section-box section-box--collapsable section-box--collapse box-bg--1">
        <div class="section-box__content">
            <div class="invoice__main-options">
                <div class="group-box group-box--united">
                    <div class="group-box__items">
                        $$input:crmaccount$$
                        <div id="inv_date_range" class="group-box__item invoice__date-due-date dates-range">
                            <div id="inv_date" class="invoice__date">
                                <div class="group-box__item-label">$$label:date$$</div>
                                <div class="group-box__item-content">$$input:date$$</div>
                            </div>
                            <div id="inv_due_date" class="invoice__due-date">
                                <div class="group-box__item-label">$$label:duedate$$</div>
                                <div class="group-box__item-content">$$input:duedate$$</div>
                            </div>
                        </div>
                        $$input:number$$
                        $$input:progressinvoicing$$
                    </div>
                </div>
            </div>
            <div id="inv_moreoptions" class="invoice__more-options section-box__collapsable-content">
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:billaddress$$
                    </div>
                </div>
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:project$$
                        $$input:bank$$
                        $$input:balance$$
                        $$input:pdftemplate$$
                    </div>
                </div>
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:introduction$$
                    </div>
                </div>
                $$input:customfields$$
            </div>
        </div>
        <div class="section-box__footer">
            $$input:showmore$$
        </div>
    </div>
    <div class="section-box">
        <div class="group-box group-box--united">
            <div class="group-box__items">
                $$input:currency$$
                $$input:taxcalc$$
                $$input:pricelevel$$
                $$input:shippingmethod$$
                $$input:manager$$
            </div>
        </div>
    </div>
    <div class="section-box box-bg--1">
        <div class="invoice__products-table">
            <!-- place table here -->
            $$input:itemtable$$
        </div>
    </div>
    <div class="section-group box-bg--2 invoice__footer">
        <div class="section-box invoice__post-fields">
            $$input:attachments$$
            <div class="group-box box-bg--2">
                <div class="group-box__items">
                    $$input:overalldiscount$$
                </div>
            </div>
            <div class="group-box invoice__payment-history">
                <div class="group-box__items">
                    <div id="inv_all_history" class="group-box__item">
                        $$input:allhistorypanel$$
                    </div>
                    <div id="inv_related_links" class="group-box__item">
                        $$input:maillinkpanel$$
                    </div>
                </div>
            </div>
            <div class="form-group inv_payment_instruction">
                <div class="form-group__label">$$label:instruction$$</div>
                <div class="form-group__content">$$input:instruction$$</div>
            </div>
            <div class="invoice__payment-notes inv_payment_instruction">
                $$input:instructionlist$$
                $$input:agreetermspanel$$
            </div>
            <div class="form-group">
                <div class="form-group__label">$$label:notes$$</div>
                <div class="form-group__content">$$input:notes$$</div>
            </div>
        </div>
        <div class="section-box invoice__receipt-block">
            <div class="invoice__subtotal">
                $$input:totalstable$$
            </div>
        </div>
    </div>
</div>
<div class="section-box">

    <div class="btns-group text-right">
        $$input:closeButton$$
        $$input:saveButton$$
        $$input:saveOrderButton$$
        $$input:rejectButton$$
        $$input:picklistButton$$
        $$input:submitToManagerButton$$
        $$input:pdfVersionButton$$
        $$input:convertButton$$
        $$input:saveAndApproveButton$$
        $$input:approveAndSendButton$$
    </div>

</div>'),
('saleinvoice', 'Sale invoice form',
 '<div class="invoice-block">
    <div id="inv_form_header" class="invoice__options-section section-box section-box--collapsable section-box--collapse box-bg--1">
        <div class="section-box__content">
            <div class="invoice__main-options">
                <div class="group-box group-box--united">
                    <div class="group-box__items">
                        $$input:invoicetype$$
                        $$input:crmaccount$$
                        <div id="inv_date_range" class="group-box__item invoice__date-due-date">
                            <div id="inv_date" class="invoice__date">
                                <div class="group-box__item-label">$$label:date$$</div>
                                <div class="group-box__item-content">$$input:date$$</div>
                            </div>
                            <div id="inv_due_date" class="invoice__due-date">
                                <div class="group-box__item-label">$$label:duedate$$</div>
                                <div class="group-box__item-content">$$input:duedate$$</div>
                            </div>
                        </div>
                        $$input:number$$
                    </div>
                </div>
            </div>
            <div id="inv_moreoptions" class="invoice__more-options section-box__collapsable-content">
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:billaddress$$
                    </div>
                </div>
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:mailaddress$$
                    </div>
                </div>
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:project$$
                        $$input:receivablepayable$$
                        $$input:bank$$
                        $$input:balance$$
                        $$input:ponumber$$
                        $$input:pdftemplate$$
                    </div>
                </div>

                <div class="group-box">
                    <div class="group-box__items">
                        $$input:introduction$$
                    </div>
                </div>
                $$input:customfields$$
            </div>
        </div>
        <div class="section-box__footer">
            $$input:showmore$$
        </div>
    </div>
    <div class="section-box">
            $$input:recurringview$$
        <div class="group-box group-box--united">
            <div class="group-box__items">
                $$input:currency$$
                $$input:taxcalc$$
				$$input:clientdiscount$$
                $$input:reference$$
                $$input:quotenumber$$
                $$input:pricelevel$$
                $$input:shippingmethod$$
            </div>
        </div>
    </div>
    <div class="section-box box-bg--1">
        <div class="invoice__products-table">
            <!-- place table here -->
            $$input:itemtable$$
        </div>
    </div>
    $$input:paymentpanel$$
    <div class="section-group box-bg--2 invoice__footer">
        <div class="section-box invoice__post-fields">
            $$input:attachments$$
            <div class="group-box">
                <div class="group-box__items">
                    $$input:paymentexratepanel$$
                    $$input:overalldiscount$$
                    $$input:addBillExpButton$$
                </div>
            </div>
            <div class="group-box invoice__payment-history">
                <div class="group-box__items">
                    <div id="inv_payment_history" class="group-box__item">
                        $$input:paymenthistorypanel$$
                    </div>
                    <div id="inv_all_history" class="group-box__item">
                        $$input:allhistorypanel$$
                    </div>
                    <div id="inv_billable_expense_link" class="group-box__item">
                        $$input:billableexpenselinkpanel$$
                    </div>
                </div>
            </div>
            <div class="form-group inv_payment_instruction">
                <div class="form-group__label">$$label:instruction$$</div>
                <div class="form-group__content">$$input:instruction$$</div>
            </div>
            <div class="inv_payment_instruction">
                <div class="invoice__payment-notes">$$input:instructionlist$$</div>
            </div>
            <div class="form-group">
                <div class="form-group__label">$$label:notes$$</div>
                <div class="form-group__content">$$input:notes$$</div>
            </div>
        </div>
        <div class="section-box invoice__receipt-block">
            <div class="invoice__subtotal">
                $$input:totalstable$$
            </div>
        </div>
    </div>
</div>
<div class="section-box">

    <div class="btns-group text-right">

        $$input:saveButton$$
        $$input:sendToTargetButton$$
        $$input:copyToNewButton$$
        $$input:creditNoteAdd$$
        $$input:generateReceiptButton$$
        $$input:sendReceiptButton$$
        $$input:pdfVersionButton$$
        $$input:excelVersionButton$$
        $$input:saveAndApproveButton$$
        $$input:approveAndSendButton$$
        $$input:assignSerialsButton$$
    </div>

</div>');

-- Company -> 65138 : Safa Al Boniyan LLC (SBC)
insert into "65138".customlayout_new_ui(formid, title, layout) values
('salequote', 'Sale quote form',
'<div class="invoice-block">
    <div id="inv_form_header" class="invoice__options-section section-box section-box--collapsable section-box--collapse box-bg--1">
        <div class="section-box__content">
            <div class="invoice__main-options">
                <div class="group-box group-box--united">
                    <div class="group-box__items">
                        $$input:crmaccount$$
                        <div id="inv_date_range" class="group-box__item invoice__date-due-date dates-range">
                            <div id="inv_date" class="invoice__date">
                                <div class="group-box__item-label">$$label:date$$</div>
                                <div class="group-box__item-content">$$input:date$$</div>
                            </div>
                            <div id="inv_due_date" class="invoice__due-date">
                                <div class="group-box__item-label">$$label:duedate$$</div>
                                <div class="group-box__item-content">$$input:duedate$$</div>
                            </div>
                        </div>
                        $$input:number$$
                        $$input:progressinvoicing$$
                    </div>
                </div>
            </div>
            <div id="inv_moreoptions" class="invoice__more-options section-box__collapsable-content">
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:billaddress$$
                    </div>
                </div>
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:project$$
                        $$input:bank$$
                        $$input:balance$$
                        $$input:pdftemplate$$
                    </div>
                </div>
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:introduction$$
                    </div>
                </div>
                $$input:customfields$$
            </div>
        </div>
        <div class="section-box__footer">
            $$input:showmore$$
        </div>
    </div>
    <div class="section-box">
        <div class="group-box group-box--united">
            <div class="group-box__items">
                $$input:currency$$
                $$input:taxcalc$$
                $$input:pricelevel$$
                $$input:shippingmethod$$
                $$input:manager$$
            </div>
        </div>
    </div>
    <div class="section-box box-bg--1">
        <div class="invoice__products-table">
            <!-- place table here -->
            $$input:itemtable$$
        </div>
    </div>
    <div class="section-group box-bg--2 invoice__footer">
        <div class="section-box invoice__post-fields">
            $$input:attachments$$
            <div class="group-box box-bg--2">
                <div class="group-box__items">
                    $$input:overalldiscount$$
                </div>
            </div>
            <div class="group-box invoice__payment-history">
                <div class="group-box__items">
                    <div id="inv_all_history" class="group-box__item">
                        $$input:allhistorypanel$$
                    </div>
                    <div id="inv_related_links" class="group-box__item">
                        $$input:maillinkpanel$$
                    </div>
                </div>
            </div>
            <div class="form-group inv_payment_instruction">
                <div class="form-group__label">$$label:instruction$$</div>
                <div class="form-group__content">$$input:instruction$$</div>
            </div>
            <div class="invoice__payment-notes inv_payment_instruction">
                $$input:instructionlist$$
                $$input:agreetermspanel$$
            </div>
            <div class="form-group">
                <div class="form-group__label">$$label:notes$$</div>
                <div class="form-group__content">$$input:notes$$</div>
            </div>
        </div>
        <div class="section-box invoice__receipt-block">
            <div class="invoice__subtotal">
                $$input:totalstable$$
            </div>
        </div>
    </div>
</div>
<div class="section-box">

    <div class="btns-group text-right">
        $$input:closeButton$$
        $$input:saveButton$$
        $$input:saveOrderButton$$
        $$input:rejectButton$$
        $$input:picklistButton$$
        $$input:submitToManagerButton$$
        $$input:pdfVersionButton$$
        $$input:convertButton$$
        $$input:saveAndApproveButton$$
        $$input:approveAndSendButton$$
    </div>

</div>'),
('saleinvoice', 'Sale invoice form',
 '<div class="invoice-block">
    <div id="inv_form_header" class="invoice__options-section section-box section-box--collapsable section-box--collapse box-bg--1">
        <div class="section-box__content">
            <div class="invoice__main-options">
                <div class="group-box group-box--united">
                    <div class="group-box__items">
                        $$input:invoicetype$$
                        $$input:crmaccount$$
                        <div id="inv_date_range" class="group-box__item invoice__date-due-date">
                            <div id="inv_date" class="invoice__date">
                                <div class="group-box__item-label">$$label:date$$</div>
                                <div class="group-box__item-content">$$input:date$$</div>
                            </div>
                            <div id="inv_due_date" class="invoice__due-date">
                                <div class="group-box__item-label">$$label:duedate$$</div>
                                <div class="group-box__item-content">$$input:duedate$$</div>
                            </div>
                        </div>
                        $$input:number$$
                    </div>
                </div>
            </div>
            <div id="inv_moreoptions" class="invoice__more-options section-box__collapsable-content">
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:billaddress$$
                    </div>
                </div>
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:mailaddress$$
                    </div>
                </div>
                <div class="group-box">
                    <div class="group-box__items">
                        $$input:project$$
                        $$input:receivablepayable$$
                        $$input:bank$$
                        $$input:balance$$
                        $$input:ponumber$$
                        $$input:pdftemplate$$
                    </div>
                </div>

                <div class="group-box">
                    <div class="group-box__items">
                        $$input:introduction$$
                    </div>
                </div>
                $$input:customfields$$
            </div>
        </div>
        <div class="section-box__footer">
            $$input:showmore$$
        </div>
    </div>
    <div class="section-box">
            $$input:recurringview$$
        <div class="group-box group-box--united">
            <div class="group-box__items">
                $$input:currency$$
                $$input:taxcalc$$
				$$input:clientdiscount$$
                $$input:reference$$
                $$input:quotenumber$$
                $$input:pricelevel$$
                $$input:shippingmethod$$
            </div>
        </div>
    </div>
    <div class="section-box box-bg--1">
        <div class="invoice__products-table">
            <!-- place table here -->
            $$input:itemtable$$
        </div>
    </div>
    $$input:paymentpanel$$
    <div class="section-group box-bg--2 invoice__footer">
        <div class="section-box invoice__post-fields">
            $$input:attachments$$
            <div class="group-box">
                <div class="group-box__items">
                    $$input:paymentexratepanel$$
                    $$input:overalldiscount$$
                    $$input:addBillExpButton$$
                </div>
            </div>
            <div class="group-box invoice__payment-history">
                <div class="group-box__items">
                    <div id="inv_payment_history" class="group-box__item">
                        $$input:paymenthistorypanel$$
                    </div>
                    <div id="inv_all_history" class="group-box__item">
                        $$input:allhistorypanel$$
                    </div>
                    <div id="inv_billable_expense_link" class="group-box__item">
                        $$input:billableexpenselinkpanel$$
                    </div>
                </div>
            </div>
            <div class="form-group inv_payment_instruction">
                <div class="form-group__label">$$label:instruction$$</div>
                <div class="form-group__content">$$input:instruction$$</div>
            </div>
            <div class="inv_payment_instruction">
                <div class="invoice__payment-notes">$$input:instructionlist$$</div>
            </div>
            <div class="form-group">
                <div class="form-group__label">$$label:notes$$</div>
                <div class="form-group__content">$$input:notes$$</div>
            </div>
        </div>
        <div class="section-box invoice__receipt-block">
            <div class="invoice__subtotal">
                $$input:totalstable$$
            </div>
        </div>
    </div>
</div>
<div class="section-box">

    <div class="btns-group text-right">

        $$input:saveButton$$
        $$input:sendToTargetButton$$
        $$input:copyToNewButton$$
        $$input:creditNoteAdd$$
        $$input:generateReceiptButton$$
        $$input:sendReceiptButton$$
        $$input:pdfVersionButton$$
        $$input:excelVersionButton$$
        $$input:saveAndApproveButton$$
        $$input:approveAndSendButton$$
        $$input:assignSerialsButton$$
    </div>

</div>');
