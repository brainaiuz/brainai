update "0".container
set defaultname = 'warehouses'
where modulecode = 'accounting'
  and code = 'warehouse';
update "anv".container
set defaultname = 'warehouses'
where modulecode = 'accounting'
  and code = 'warehouse';