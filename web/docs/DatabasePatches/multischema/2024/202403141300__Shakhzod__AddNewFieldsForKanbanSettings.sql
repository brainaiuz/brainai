UPDATE "anv".kanbanitemsettings
SET settingsjsondata = jsonb_set(
        settingsjsondata::jsonb,
        '{8}',
        '{"code":"CANDIDATE_POSITION", "title":"Position", "localizationCode":"position", "selected":true, "relatedField":null, "mandatory":false, "changeable":true}'::jsonb
    )
WHERE code = 'CANDIDATE_ITEM_SETTINGS';
UPDATE "anv".kanbanitemsettings
SET settingsjsondata = jsonb_set(
        settingsjsondata::jsonb,
        '{9}',
        '{"code":"CANDIDATE_DEPARTMENT", "title":"Department", "localizationCode":"department", "selected":true, "relatedField":null, "mandatory":false, "changeable":true}'::jsonb
    )
WHERE code = 'CANDIDATE_ITEM_SETTINGS';