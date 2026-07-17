alter table hostbasedsetting add telegrambottoken varchar(255) null;

update hostbasedsetting set telegrambottoken = '384832227:AAFGQxPVz7OIGLrwXBoVtebzcHoMlhiURi4' where hostname = 'localhost:8080';
update hostbasedsetting set telegrambottoken = '362769682:AAG5QL6qYmWEbQ3kpyXk0pt9j5IgfF9IwrU' where hostname = 'aws.kpi.com';
update hostbasedsetting set telegrambottoken = '422240732:AAHuOZoswSmBDpxHiAsvYrRN7G2PgWMjsjI' where hostname = 'app.kpi.com';