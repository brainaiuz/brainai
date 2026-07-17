SELECT n.id, max(n.subject) subject, max(n.shortdescription), max(n.date) date,
count(nc.id)  as comments
from "$".news n
	left join "$".newscomment nc on nc.newsid=n.id
where deleted is not true
group by n.id
order by max(n.date) desc
limit ?;