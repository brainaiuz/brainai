update "anv".container_item
set moduleid = (select id from "anv".mymodule where code = 'PRODUCTS_SERVICES_CRM' limit 1)
where modulecode = 'crm'
  and containerid = (select id
                     from "anv".container
                     where code = 'crmWelcome'
                       and defaultname = 'sales'
                       and modulecode = 'crm'
                       and preparedview = 'leadList'
                     limit 1)
  and propertyid =
      (select id
       from "anv".property
       where defaultname = 'Product/Service'
         and modulecode = 'accounting'
         and objectname = 'productsOrServices');