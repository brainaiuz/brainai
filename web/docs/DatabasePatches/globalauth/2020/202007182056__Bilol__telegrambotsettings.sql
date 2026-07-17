create table telegrambotsettings(
                                    id serial,
                                    token varchar(255) not null,
                                    botname varchar(255),
                                    companyid integer not null,
                                    deleted boolean default false not null
);