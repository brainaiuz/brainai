insert into "anv".folder(foldertype, name, type, version, parent_id, owner_id)
values (78, 'Additional Payment', 1, 0, (select id from "anv".folder where foldertype = 65), 1);