--- For zero scheme
UPDATE  "0".reference set name='Sick' where code='LR_TYPE_SICK_LEAVE';
UPDATE  "0".reference set name='Annual' where code='LR_TYPE_ANNUAL_LEAVE';
UPDATE  "0".reference set name='Unauthorized' where code='LR_TYPE_UNAUTHORIZED_LEAVE';
UPDATE  "0".reference set name='Study' where code='LR_TYPE_STUDY_LEAVE';

UPDATE  "0".reference set name='Sick' where code='REASON_TYPE_SICK_LEAVE';
UPDATE  "0".reference set name='Annual' where code='REASON_TYPE_ANNUAL_LEAVE';
UPDATE  "0".reference set name='Study Leave' where code='REASON_TYPE_STUDY_LEAVE';


---For all
UPDATE  "anv".reference set name='Sick' where code='LR_TYPE_SICK_LEAVE';
UPDATE  "anv".reference set name='Annual' where code='LR_TYPE_ANNUAL_LEAVE';
UPDATE  "anv".reference set name='Unauthorized' where code='LR_TYPE_UNAUTHORIZED_LEAVE';
UPDATE  "anv".reference set name='Study' where code='LR_TYPE_STUDY_LEAVE';

UPDATE  "anv".reference set name='Sick' where code='REASON_TYPE_SICK_LEAVE';
UPDATE  "anv".reference set name='Annual' where code='REASON_TYPE_ANNUAL_LEAVE';
UPDATE  "anv".reference set name='Study Leave' where code='REASON_TYPE_STUDY_LEAVE';