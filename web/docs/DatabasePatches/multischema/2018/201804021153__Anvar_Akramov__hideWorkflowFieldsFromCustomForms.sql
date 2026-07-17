-- For default schema 
UPDATE "0".modelfield SET hideincustomizeform=true WHERE section='WORKFLOW_FIELDS';
-- For public schema
UPDATE modelfield SET hideincustomizeform=true WHERE section='WORKFLOW_FIELDS';
-- for all other private schemas
UPDATE "anv".modelfield SET hideincustomizeform=true WHERE section='WORKFLOW_FIELDS';