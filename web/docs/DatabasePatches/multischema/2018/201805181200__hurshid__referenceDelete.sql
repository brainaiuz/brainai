
update "0".reference set deleted=true where code='TITLE_OTHER';
update "0".reference set deleted=true where code='ON_HOLD';
update "0".reference set deleted=true where code='CANCELLED';

update "anv".reference set deleted=true where code='TITLE_OTHER';
update "anv".reference set deleted=true where code='ON_HOLD';
update "anv".reference set deleted=true where code='CANCELLED';
