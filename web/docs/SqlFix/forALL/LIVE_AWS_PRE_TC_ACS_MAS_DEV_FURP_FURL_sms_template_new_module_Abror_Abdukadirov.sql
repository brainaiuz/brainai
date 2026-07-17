insert into "0".reference(code,     deleted, isremovable, name,          shared, sorder,  isactive)
							values('_SMS_TEMPLATE', false,   true,       'Sms Template', true,   1,       true);

insert into "anv".reference(code,     deleted, isremovable, name,          shared, sorder,  isactive)
							values('_SMS_TEMPLATE', false,   true,       'Sms Template', true,   1,       true);


insert into "0".reference(code,                       deleted, isremovable, name,               shared, sorder, parentid,                                                    isactive)
							values('SMS_TEMPLATE_CUSTOMER_BALANSE', false,   true,        'Customer Balance', true,   3,      (select id from "0".reference where code='_SMS_TEMPLATE'), true);

insert into "anv".reference(code,                       deleted, isremovable, name,               shared, sorder, parentid,                                                    isactive)
							values('SMS_TEMPLATE_CUSTOMER_BALANSE', false,   true,        'Customer Balance', true,   3,      (select id from "anv".reference where code='_SMS_TEMPLATE'), true);


insert into "0".reference(code,                     deleted, isremovable, name,               shared, sorder, parentid,                                                  isactive)
							values('SMS_TEMPLATE_SUPPLIER_BALANSE', false,   true,        'Supplier Balance', true,   3,      (select id from "0".reference where code='_SMS_TEMPLATE'), true);

insert into "anv".reference(code,                     deleted, isremovable, name,               shared, sorder, parentid,                                                  isactive)
							values('SMS_TEMPLATE_SUPPLIER_BALANSE', false,   true,        'Supplier Balance', true,   3,      (select id from "anv".reference where code='_SMS_TEMPLATE'), true);
