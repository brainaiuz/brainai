insert into dynamic_query (query_name, query_text)
values ('UPDATE_POSITION_FIELDS_311555',
        'UPDATE "311555".position p
SET coefficient = cast(citc.string_value2 as numeric)
FROM "311555".custom_item_table cit
LEFT JOIN "311555".custom_item_table_customfields citc ON cit.customfieldsid = citc.id
WHERE cit.form_item_id = ?
AND citc.string_value4 IS NOT NULL
AND EXISTS (
SELECT 1
FROM "311555".custom_form_item cfItem
LEFT JOIN "311555".customform_customfields cc ON cfItem.form_customfieldsid = cc.id
LEFT JOIN "311555".locationcustomfields lc ON lc.id = p.locationId  -- Add this line
WHERE cfItem.form_id = ''TARIFNAJA_SETKA_FORM''
AND cfItem.id = ?
AND cast(cc.jsonEntities as json) ->> ''string_value2'' = cast(lc.jsonEntities as json) ->> ''string_value8''
)
AND p.positionNameId =  CAST(cast(citc.jsonEntities as json) ->> ''string_value4'' AS numeric);
CREATE TEMPORARY TABLE temp_v_names AS
SELECT cit.form_item_id  AS v_id
FROM "311555".custom_item_table cit
LEFT JOIN "311555".custom_item_table_customfields citc ON cit.customfieldsid = citc.id
WHERE cit.form_item_id = ? limit 1;
DO $$
DECLARE
v_name numeric;
v_value numeric;
v_pc_id numeric; -- Variable to store positioncustomfields id

BEGIN
FOR v_name IN (
SELECT CAST(cast(citc.jsonEntities as json) ->> ''string_value4'' AS numeric)
FROM "311555".custom_item_table cit
LEFT JOIN "311555".custom_item_table_customfields citc ON cit.customfieldsid = citc.id
WHERE cit.form_item_id = (select v_id from temp_v_names)
)
LOOP
SELECT citc.string_value3
INTO v_value
FROM "311555".custom_item_table cit
LEFT JOIN "311555".custom_item_table_customfields citc ON cit.customfieldsid = citc.id
WHERE cit.form_item_id = (select v_id from temp_v_names)
AND CAST(cast(citc.jsonEntities as json) ->> ''string_value4'' AS numeric) = v_name;

-- Check if corresponding records exist in positioncustomfields table
FOR v_pc_id IN (
SELECT pc.id
FROM "311555".position p
LEFT JOIN "311555".positioncustomfields pc ON pc.id = p.customfieldsid
LEFT JOIN "311555".reference r on p.positionNameId = r.id
LEFT JOIN "311555".reference_locale rl on r.localeId  = rl.id
LEFT JOIN "311555".location l ON p.locationId = l.id
LEFT JOIN "311555".locationcustomfields lc ON l.customfieldsid = lc.id
WHERE r.id = v_name  AND  cast(lc.jsonEntities as json) ->> ''string_value8'' = (
SELECT  cast(cc.jsonEntities as json) ->> ''string_value2''
FROM "311555".custom_form_item cfItem
LEFT JOIN "311555".customform_customfields cc ON cfItem.form_customfieldsid = cc.id
WHERE cfItem.form_id = ''TARIFNAJA_SETKA_FORM''
AND cfItem.id = (select v_id from temp_v_names)
)
)
LOOP
-- Insert a new record into positioncustomfields if v_pc_id is NULL
IF v_pc_id IS NULL THEN
INSERT INTO "311555".positioncustomfields (string_value2) VALUES (v_value) RETURNING id INTO v_pc_id;
UPDATE "311555".position p
SET customfieldsid = v_pc_id
WHERE
p.id IN (
SELECT p.id
FROM "311555".position p
LEFT JOIN "311555".positioncustomfields pc ON pc.id = p.customfieldsid
LEFT JOIN "311555".reference r on p.positionNameId = r.id
LEFT JOIN "311555".reference_locale rl on r.localeId  = rl.id
LEFT JOIN "311555".location l ON p.locationId = l.id
LEFT JOIN "311555".locationcustomfields lc ON l.customfieldsid = lc.id
WHERE r.id = v_name AND  p.customfieldsid IS NULL  AND  cast(lc.jsonEntities as json) ->> ''string_value8'' = (
SELECT cast(cc.jsonEntities as json) ->> ''string_value2''
FROM "311555".custom_form_item cfItem
LEFT JOIN "311555".customform_customfields cc ON cfItem.form_customfieldsid = cc.id
WHERE cfItem.form_id = ''TARIFNAJA_SETKA_FORM''
AND cfItem.id = (select v_id from temp_v_names)
)
ORDER BY id LIMIT 1
);
ELSE
UPDATE "311555".positioncustomfields pc
SET string_value2 = v_value
WHERE pc.id IN (
SELECT pc.id
FROM "311555".position p
LEFT JOIN "311555".positioncustomfields pc ON pc.id = p.customfieldsid
LEFT JOIN "311555".reference r on p.positionNameId = r.id
LEFT JOIN "311555".reference_locale rl on r.localeId  = rl.id
LEFT JOIN "311555".location l ON p.locationId = l.id
LEFT JOIN "311555".locationcustomfields lc ON l.customfieldsid = lc.id
WHERE r.id = v_name AND  cast(lc.jsonEntities as json) ->> ''string_value8'' = (
SELECT  cast(cc.jsonEntities as json) ->> ''string_value2''

FROM "311555".custom_form_item cfItem
LEFT JOIN "311555".customform_customfields cc ON cfItem.form_customfieldsid = cc.id
WHERE cfItem.form_id = ''TARIFNAJA_SETKA_FORM''
AND cfItem.id = (select v_id from temp_v_names)
)
);
END IF;
END LOOP;
END LOOP;
END $$;
DROP TABLE temp_v_names;
');







