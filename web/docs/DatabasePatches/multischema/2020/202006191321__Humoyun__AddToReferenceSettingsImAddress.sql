
delete from "anv".reference where parentid=(select id from "anv".reference where code = '_IM_ADDRESSES');
delete from "anv".reference where code = '_IM_ADDRESSES';

insert into "anv".reference (antonym, code, description, isactive, issystemreference, name, shared) values ('ImAddress', '_IM_ADDRESSES', '_IM_ADDRESSES', true, true, 'ImAddress', true);

insert into "anv".reference (code, isactive, isremovable, name, shared, sorder, parentid)  values
    ('GTALK', true, true, 'Google Talk', true, 1, (select id from "anv".reference where code = '_IM_ADDRESSES')),
    ('AIM', true, true, 'AIM', true, 2, (select id from "anv".reference where code = '_IM_ADDRESSES')),
    ('YAHOO', true, true, 'Yahoo', true, 3, (select id from "anv".reference where code = '_IM_ADDRESSES')),
    ('SKYPE', true, true, 'Skype', true, 4, (select id from "anv".reference where code = '_IM_ADDRESSES')),
    ('QQ', true, true, 'QQ', true, 5, (select id from "anv".reference where code = '_IM_ADDRESSES')),
    ('MSN', true, true, 'MSN', true, 6, (select id from "anv".reference where code = '_IM_ADDRESSES')),
    ('JABBER', true, true, 'Jabber', true, 7, (select id from "anv".reference where code = '_IM_ADDRESSES'));
