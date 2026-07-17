INSERT INTO "0".Reference(attendancelr, autoapprove, code, deleted, isactive, iscustombutton, isremovable,
                              issystemreference, leavedays, name, shared, sorder, parentid, hasprorata)
VALUES (false, false, 'REQUEST_FOR_QUOTE_CATEGORY', false, true, false, false, true, 0, 'Request For Quote', true, 1,(SELECT id FROM "0".Reference WHERE code = 'ET_RFQ_MODULE'), false);

INSERT INTO "anv".Reference(attendancelr, autoapprove, code, deleted, isactive, iscustombutton, isremovable,
                              issystemreference, leavedays, name, shared, sorder, parentid, hasprorata)
VALUES (false, false, 'REQUEST_FOR_QUOTE_CATEGORY', false, true, false, false, true, 0, 'Request For Quote', true, 1,(SELECT id FROM "anv".Reference WHERE code = 'ET_RFQ_MODULE'), false);