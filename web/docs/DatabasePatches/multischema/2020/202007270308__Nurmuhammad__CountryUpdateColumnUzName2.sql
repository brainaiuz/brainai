--Update Table

update country set uzname = 'Solomon orollari' where name = 'Solomon Islands';

delete from country k using country y where k.id>y.id and k.uzname=y.uzname;
delete from country where uzname='Birma';
