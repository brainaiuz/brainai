delete from "anv".genericsettings where key='ENABLE_NUMBER_CHANGE_ACCESS_SO_SQ';
delete from "anv".genericsettings where key='ENABLE_NUMBER_CHANGE_ACCESS_PO';

insert into "anv".genericsettings (key, value) VALUES ('ENABLE_NUMBER_CHANGE_ACCESS_SO_SQ', 'YES'),
                                                      ('ENABLE_NUMBER_CHANGE_ACCESS_PO', 'YES');