
DELETE FROM "0".permission_context WHERE permissioncode = 'RFQ_ADD_NEW_ACTIVITY_EVENT' AND contextcode = 'REQUEST_FOR_PURCHASES';
insert into "0".permission_context (permissioncode, contextcode) values ('RFQ_ADD_NEW_ACTIVITY_EVENT', 'ACCOUNTING');

DELETE FROM "anv".permission_context WHERE permissioncode = 'RFQ_ADD_NEW_ACTIVITY_EVENT' AND contextcode = 'REQUEST_FOR_PURCHASES';
insert into "anv".permission_context (permissioncode, contextcode) values ('RFQ_ADD_NEW_ACTIVITY_EVENT', 'ACCOUNTING');


DELETE FROM "0".permission_context WHERE permissioncode = 'RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL' AND contextcode = 'REQUEST_FOR_QUOTES';
insert into "0".permission_context (permissioncode, contextcode) values ('RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ACCOUNTING');


DELETE FROM "anv".permission_context WHERE permissioncode = 'RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL' AND contextcode = 'REQUEST_FOR_QUOTES';
insert into "anv".permission_context (permissioncode, contextcode) values ('RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ACCOUNTING');