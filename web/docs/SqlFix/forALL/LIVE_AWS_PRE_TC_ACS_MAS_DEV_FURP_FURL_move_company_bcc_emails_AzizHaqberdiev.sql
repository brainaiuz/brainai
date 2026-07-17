--schema updatedan keyin urilsin
insert into companyEmail (companyid,email) select id,bccemail from company where active is true and bccemail is not null and bccemail !='';