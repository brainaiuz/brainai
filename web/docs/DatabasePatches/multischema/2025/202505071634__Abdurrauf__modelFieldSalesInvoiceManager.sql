insert into modelfield(form_id, field_id, sorder, isCustomField, widget, source, type, usableByWorkflow,
                       disableupdate)
select 'SALE_INVOICE_FORM',
       'MANAGER',
       1,
       false,
       'LOOKUP',
       null,
       'text',
       true,
       true
where not exists(select 1 from modelfield where form_id = 'SALE_INVOICE_FORM' and field_id = 'MANAGER');

insert into "anv".modelfield(form_id, field_id, sorder, isCustomField, widget, source, type, usableByWorkflow,
                             disableupdate)
select 'SALE_INVOICE_FORM',
       'MANAGER',
       1,
       false,
       'LOOKUP',
       null,
       'text',
       true,
       true
where not exists(select 1 from "anv".modelfield where form_id = 'SALE_INVOICE_FORM' and field_id = 'MANAGER');