select c.id,c.photoId,coalesce(c.firstname,'')||' '|| coalesce(c.lastname,'') leadname,r.name roleName,ca.name companyName, c.primaryemail email
from "$".crmcontact c
left join "$".crmAccount ca on c.crmAccount=ca.id
left join "$".employeeprofile ep on c.id=ep.contact_id
left join (select u.id,min(r.sorder) sort_order
from "$".myuser u
left join "$".myuser_role ur on u.id=ur.users_id
left join "$".role r on ur.roles_id=r.id
group by u.id ) u on ep.employeeid=u.id
left join "$".role r on u.sort_order=r.sorder

 inner join
                (
                select distinct crmcontact_id from "$".crmcontact_contactcategory
                where categories_id in
                (
                select distinct category.id from "$".contactcategoryrbac ccrbac
                inner join "$".contactcategory category on ccrbac.contactCategory_id=category.id
				,(select distinct userid from "$".myUserSession where sessionid=?) mus
                where
                (
                category.doNotShow is null or category.doNotShow is not true
                ) or
                ccrbac.userid=mus.userid or category.owner_id=mus.userid or
                (
                ccrbac.groupid is not null and
                ccrbac.groupid in
                (
                select distinct edsgroup6_.id from "$".trusteegroup edsgroup6_
                inner join "$".trusteegroup_trustee members7_ on edsgroup6_.id=members7_.trusteegroup_id
                inner join "$".trustee edstrustee8_ on members7_.members_id=edstrustee8_.id
                inner join
                (select distinct edstrustee9_.id
                from "$".trustee edstrustee9_
				inner join "$".myUserSession mus on (edstrustee9_.trusteeID=mus.userid and sessionid=?)
                and edstrustee9_.trusteetype=2) abs on edstrustee8_.id=abs.id

                )
                )
                )
                ) as "contactids" on c.id = "contactids".crmcontact_id

where c.deleted is not true and c.contacttype!=5
order by c.id desc
limit ? ;