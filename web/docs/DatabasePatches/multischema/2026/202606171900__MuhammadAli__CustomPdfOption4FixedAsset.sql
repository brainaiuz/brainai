SELECT setval(
               '"anv".pdfreference_id_seq',
               (SELECT COALESCE(MAX(id), 0) FROM "anv".pdfreference)
       );

insert into "anv".pdfreference (code, name)
values ('FIXED_ASSET', 'Fixed Asset');

