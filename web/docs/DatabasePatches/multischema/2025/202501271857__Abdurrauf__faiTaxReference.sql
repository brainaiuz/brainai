insert into "anv".reference(code, name)
values ('_FAI_VAT', 'FAI VAT');

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_RCM050', 'SA_SA_RCM050', 176, (select id from "anv".reference where code = '_FAI_VAT'), 5);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_RCM0150', 'SA_SA_RCM0150', 174, (select id from "anv".reference where code = '_FAI_VAT'), 15);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_INTXXXX', 'Exempted', 158, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_NAAXXXX', 'SA_SA_NAAXXXX', 157, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_RETXXXX', 'SA_SA_RETXXXX', 156, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_EXMXXXX', 'SA_SA_EXMXXXX', 155, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_EXPXXXX', 'SA_SA_EXPXXXX', 154, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_TTZ0000', 'SA_SA_TTZ0000', 153, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_TZR0000', 'SA_SA_TZR0000', 152, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_TGB0000', 'SA_SA_TGB0000', 151, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_TSG0150', 'SA_SA_TSG0150', 150, (select id from "anv".reference where code = '_FAI_VAT'), 15);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_RET0150', 'SA_SA_RET0150', 149, (select id from "anv".reference where code = '_FAI_VAT'), 15);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_TSC0050', 'SA_SA_TSC0050', 146, (select id from "anv".reference where code = '_FAI_VAT'), 5);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_TSC0150', 'SA_SA_TSC0150', 145, (select id from "anv".reference where code = '_FAI_VAT'), 15);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('AX_SA_NAT', 'nat', 144, (select id from "anv".reference where code = '_FAI_VAT'), 15);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_SONLY', 'saudi only', 142, (select id from "anv".reference where code = '_FAI_VAT'), 15);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_897', '897', 141, (select id from "anv".reference where code = '_FAI_VAT'), 10);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_OPTIONAL', 'optional', 139, (select id from "anv".reference where code = '_FAI_VAT'), 10);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_NA00', '-% - Out of Scope Sales', 37, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_INSA', '-% - Tax Group Sales', 35, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_RESA', '-% - Real Estate Sales', 33, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_DREN', '-% - Qualified Resi. Rent', 31, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_LINS', '-% - Ex. Insu. Sales', 29, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_FNOT', '-% - Ex. Sec. Sales', 27, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_FSER', '-% - Ex. Fin. Sales', 25, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_GCCE', '-% - Exports to GCC', 23, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_INTE', '-% - Intl. Exports', 21, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_0PRM', '0% - P. Metals Sales', 19, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_0MED', '0% - Q. Medical Sales', 17, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_0TRS', '0% - Q. Trans. Sales', 15, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_0TSR', '0% - Intl. Trans. Sales', 13, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_GHOU', '-% - First House', 11, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_GEDU', '-% - Edu. for Citizens', 9, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_GMED', '-% - Health for Citizens', 7, (select id from "anv".reference where code = '_FAI_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_G150', '15% - Etimad Platform', 5, (select id from "anv".reference where code = '_FAI_VAT'), 15);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_S050', 'SA_SA_S050', 3, (select id from "anv".reference where code = '_FAI_VAT'), 5);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
values ('SA_SA_S150', '15% - Standard Sales', 1, (select id from "anv".reference where code = '_FAI_VAT'), 15);
