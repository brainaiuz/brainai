

delete from "anv".rolepermission where permissioncode='CRM_OPPORTUNITIES_LIST' and rolecode='CREATOR';
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_OPPORTUNITIES_LIST', 'CREATOR', 'ALLOW');
