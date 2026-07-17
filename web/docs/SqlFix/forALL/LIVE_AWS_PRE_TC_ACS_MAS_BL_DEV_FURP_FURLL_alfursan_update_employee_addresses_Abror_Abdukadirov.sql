update "200138".address set address = 'Mezzanine Floor, Al Hilal Bank Building', addressb = 'Airport Road'
                        where id in (select a.id from "200138".myuser mu
                                                left join "200138".employee e on mu.id = e.id
                                                left join "200138".employeeprofile ep on e.profileId = ep.id
                                                left join "200138".crmContact cc on ep.contact_id = cc.id
                                                left join "200138".address a on (cc.id = a.contactID and a.entityType = 'contact')
                                                where mu.deleted = false and cc.deleted = false and a.deleted = false and cc.contactType = 4);