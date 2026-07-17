

--Schema updatedan keyin urilsin

UPDATE "0".reference SET attendanceLR=TRUE, autoApprove=TRUE WHERE code='LR_TYPE_UNAUTHORIZED_LEAVE';
UPDATE "anv".reference SET attendanceLR=TRUE, autoApprove=TRUE WHERE code='LR_TYPE_UNAUTHORIZED_LEAVE';