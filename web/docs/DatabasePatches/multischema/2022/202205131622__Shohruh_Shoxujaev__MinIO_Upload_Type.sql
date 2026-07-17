delete from "0".reference where code='MINIO';
insert into "0".reference (code, deleted, isactive, isremovable, issystemreference, leavedays, name, shared, parentid) values ('MINIO', false, true, false, true, 0.00, 'MinIO file storage', true, (select id from "0".reference where code = '_UPLOAD_TYPE'));

delete from "anv".reference where code='MINIO';
insert into "anv".reference (code, deleted, isactive, isremovable, issystemreference, leavedays, name, shared, parentid) values ('MINIO', false, true, false, true, 0.00, 'MinIO file storage', true, (select id from "anv".reference where code = '_UPLOAD_TYPE'));
