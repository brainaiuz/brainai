insert into "anv".customformsection (form_id, section, sorder, expanded) values('LOCATION_FORM', 'GEOLOCATION', 2, true);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOCATION_FORM', 'GEO_LATITUDE', false, 'COL_2', 'GEOLOCATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOCATION_FORM', 'GEO_LONGITUDE', false, 'COL_2', 'GEOLOCATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOCATION_FORM', 'GEO_RADIUS', false, 'COL_2', 'GEOLOCATION', 2);

update "anv".form_property
set settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "GEO_LATITUDE",
          "title": "Latitude",
          "aliasName": "GEO_LATITUDE",
          "changed": false,
          "required": false,
          "widget": "Numbering",
          "selectedId": null,
          "defaultValue": "",
          "disabled": false
        }'::jsonb,
        true)
where form_id = 'LOCATION_FORM'
  and not exists (select 1
                  from jsonb_array_elements(settingsjsondata::jsonb) AS element
                  where element ->> 'code' = 'GEO_LATITUDE');

update "anv".form_property
set settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "GEO_LONGITUDE",
          "title": "Longitude",
          "aliasName": "GEO_LONGITUDE",
          "changed": false,
          "required": false,
          "widget": "Numbering",
          "selectedId": null,
          "defaultValue": "",
          "disabled": false
        }'::jsonb,
        true)
where form_id = 'LOCATION_FORM'
  and not exists (select 1
                  from jsonb_array_elements(settingsjsondata::jsonb) AS element
                  where element ->> 'code' = 'GEO_LONGITUDE');

update "anv".form_property
set settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "GEO_RADIUS",
          "title": "Radius",
          "aliasName": "GEO_RADIUS",
          "changed": false,
          "required": false,
          "widget": "Numbering",
          "selectedId": null,
          "defaultValue": "",
          "disabled": false
        }'::jsonb,
        true)
where form_id = 'LOCATION_FORM'
  and not exists (select 1
                  from jsonb_array_elements(settingsjsondata::jsonb) AS element
                  where element ->> 'code' = 'GEO_RADIUS');