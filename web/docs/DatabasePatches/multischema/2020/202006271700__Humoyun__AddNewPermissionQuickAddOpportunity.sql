
update permission set  sorder=1, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='See All' where code='CRM_SEE_ALL_OPPORTUNITIES_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Add' where code='CRM_ADD_NEW_OPPORTUNITIES';
update permission set  sorder=4, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Edit' where code='CRM_EDIT_OPPORTUNITIES';
update permission set  sorder=5, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Copy' where code='CRM_COPY_OPPORTUNITIES';
update permission set  sorder=6, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Delete' where code='CRM_REMOVE_OPPORTUNITIES';
update permission set  sorder=7, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Add Note' where code='CRM_ADD_OPPORTUNITY_NOTE';
update permission set  sorder=8, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Change Stage' where code='CRM_OPPORTUNITY_CHANGE_STAGE';
update permission set  sorder=9, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Change Campaign' where code='CHANGE_OPPORTUNITIES_CAMPAIGN';
update permission set  sorder=10, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to SQ' where code='CONVERT_OPPORTUNITY_TO_SQ';
update permission set  sorder=11, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to SO' where code='CONVERT_OPPORTUNITY_TO_SO';
update permission set  sorder=12, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to RFQ' where code='CONVERT_OPPORTUNITY_TO_RFQ';
update permission set  sorder=13, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to Project' where code='CONVERT_OPPORTUNITY_TO_PROJECT';
update permission set  sorder=14, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Import' where code='CRM_OPPORTUNITIES_IMPORT_LIST';
update permission set  sorder=15, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Export' where code='CRM_OPPORTUNITIES_EXPORT_LIST';
update permission set  sorder=16, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Expense Claim' where code='CRM_OPPORTUNITIES_EXPENSE_CLAIM_LIST';
update permission set  sorder=17, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='RFQ' where code='CRM_OPPORTUNITIES_RFQ_LIST';

delete from permission where code='CRM_QUICK_ADD_NEW_OPPORTUNITIES';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('CRM_QUICK_ADD_NEW_OPPORTUNITIES', 'CRM', 'Quick Add', 3,
        (select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),'CRM_MODULE');

delete from "anv".permission_context where permissioncode = 'CRM_QUICK_ADD_NEW_OPPORTUNITIES';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_QUICK_ADD_NEW_OPPORTUNITIES', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_QUICK_ADD_NEW_OPPORTUNITIES';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_QUICK_ADD_NEW_OPPORTUNITIES', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_QUICK_ADD_NEW_OPPORTUNITIES', 'ALLOW', 'SALESMAN');
