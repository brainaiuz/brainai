update "anv".reference set shortname = 'SI', color='e91e63' where code = 'LR_TYPE_SICK_LEAVE' and parentid = (select id from "anv".reference where code = '_LEAVE_REQUEST_TYPE') and shortname is null and color is null;
update "anv".reference set shortname = 'ST', color='ff9800' where code = 'LR_TYPE_STUDY_LEAVE' and parentid = (select id from "anv".reference where code = '_LEAVE_REQUEST_TYPE') and shortname is null and color is null;
update "anv".reference set shortname = 'AL', color='009688' where code = 'LR_TYPE_ANNUAL_LEAVE' and parentid = (select id from "anv".reference where code = '_LEAVE_REQUEST_TYPE') and shortname is null and color is null;
update "anv".reference set shortname = 'LA', color='673ab7' where code = 'LR_TYPE_LATE' and parentid = (select id from "anv".reference where code = '_LEAVE_REQUEST_TYPE') and shortname is null and color is null;
update "anv".reference set shortname = 'SP', color='03a9f4' where code = 'LR_TYPE_SPECIAL' and parentid = (select id from "anv".reference where code = '_LEAVE_REQUEST_TYPE') and shortname is null and color is null;
update "anv".reference set shortname = 'OL', color='607d8b' where code = 'LR_TYPE_OTHER_LEAVE' and parentid = (select id from "anv".reference where code = '_LEAVE_REQUEST_TYPE') and shortname is null and color is null;
update "anv".reference set shortname = 'UN', color='f44336' where code = 'LR_TYPE_UNAUTHORIZED_LEAVE' and parentid = (select id from "anv".reference where code = '_LEAVE_REQUEST_TYPE') and shortname is null and color is null;

update "0".reference set shortname = 'SI', color='e91e63' where code = 'LR_TYPE_SICK_LEAVE';
update "0".reference set shortname = 'ST', color='ff9800' where code = 'LR_TYPE_STUDY_LEAVE';
update "0".reference set shortname = 'AL', color='009688' where code = 'LR_TYPE_ANNUAL_LEAVE';
update "0".reference set shortname = 'LA', color='673ab7' where code = 'LR_TYPE_LATE';
update "0".reference set shortname = 'SP', color='03a9f4' where code = 'LR_TYPE_SPECIAL';
update "0".reference set shortname = 'OL', color='607d8b' where code = 'LR_TYPE_OTHER_LEAVE';
update "0".reference set shortname = 'UN', color='f44336' where code = 'LR_TYPE_UNAUTHORIZED_LEAVE';