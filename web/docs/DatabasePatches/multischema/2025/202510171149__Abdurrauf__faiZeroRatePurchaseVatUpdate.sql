update "anv".reference set name = 'Internal Purchases (Zero rate)' where name = 'Internal Purchases' and leavedays = 0;
update "anv".reference set name = '5% - RCM Service (Zero rate)' where name = '5% - RCM Service' and leavedays = 0;
update "anv".reference set name = '15% - RCM Service (Zero rate)' where name = '15% - RCM Service' and leavedays = 0;
update "anv".reference set name = '15% - RCM Goods (Zero rate)' where name = '15% - RCM Goods' and leavedays = 0;
update "anv".reference set name = 'Purchases OS (Zero rate)' where name = 'Purchases OS' and leavedays = 0;