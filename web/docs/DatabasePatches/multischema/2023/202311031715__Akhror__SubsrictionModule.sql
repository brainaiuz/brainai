insert into "anv".container(changed, iscustom,code, defaultname, modulecode, preparedview, sorder) values(false, false,'subscription', 'subscription', 'hrms','vendorList', (select max(sorder) + 1 from "anv".container where modulecode = 'hrms'));


insert into "anv".mymodule(active, code, section) values(true, 'SUBSCRIPTION', 'hrms');


insert into "anv".property(isactive, defaultname, iscustom, modulecode, objectname, plural, shortcut,singular) values
                                                                                                                      (true, 'Vendors', false, 'hrms', 'subscriptionvendors', 'Vendors', 'SV', 'Vendor'),
                                                                                                                      (true, 'Subscriptions', false, 'hrms', 'subscriptions', 'Subscriptions', 'SS', 'Subscription'),
                                                                                                                      (true, 'Usages', false, 'hrms', 'subscriptionusages', 'Usages', 'SU', 'Usage');


insert into "anv".container_item(isactive, modulecode, sorder, containerid, moduleid, propertyid) values
                                                                                                         (true, 'hrms', 1, (select id from "anv".container where code = 'subscription'), (select id from "anv".mymodule where code = 'SUBSCRIPTION'), (select id from "anv".property where objectname = 'subscriptionvendors')),
                                                                                                         (true, 'hrms', 2, (select id from "anv".container where code = 'subscription'), (select id from "anv".mymodule where code = 'SUBSCRIPTION'), (select id from "anv".property where objectname = 'subscriptions')),
                                                                                                         (true, 'hrms', 3, (select id from "anv".container where code = 'subscription'), (select id from "anv".mymodule where code = 'SUBSCRIPTION'), (select id from "anv".property where objectname = 'subscriptionusages'));


