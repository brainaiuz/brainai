
update permission set  sorder=9, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Detect Duplicates' where code='CRM_CONTACTS_DETECT_DUBLICATES';
