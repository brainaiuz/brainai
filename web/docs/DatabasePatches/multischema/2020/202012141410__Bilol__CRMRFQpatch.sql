insert into "0".mymodule (code,name,section,active) values ('CRM_REQUEST_FOR_QUOTES','CRM Request for quotes','crm',false);
insert into "anv".mymodule (code,name,section,active) values ('CRM_REQUEST_FOR_QUOTES','CRM Request for quotes','crm',false);

update permission set  modulecode='CRM_REQUEST_FOR_QUOTES'  where code='CRM_REQUEST_FOR_QUOTE_LIST';
update permission set  modulecode='CRM_REQUEST_FOR_QUOTES'  where code='CRM_REQUEST_FOR_QUOTE_ADD';
update permission set  modulecode='CRM_REQUEST_FOR_QUOTES'  where code='CRM_REQUEST_FOR_QUOTE_EDIT';
update permission set  modulecode='CRM_REQUEST_FOR_QUOTES'  where code='CRM_REQUEST_FOR_QUOTE_DELETE';
update permission set  modulecode='CRM_REQUEST_FOR_QUOTES'  where code='CRM_REQUEST_FOR_QUOTE_SUMMARY';
update permission set  modulecode='CRM_REQUEST_FOR_QUOTES'  where code='CRM_REQUEST_FOR_QUOTE_PDF';

commit;

update "58835".mymodule set active = true where code = 'CRM_REQUEST_FOR_QUOTES';
