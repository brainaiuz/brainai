create table password_change(
                                    id serial,
                                    company_id integer not null,
                                    user_name varchar(255) not null,
                                    user_id integer not null,
                                    old_password varchar(255),
                                    new_password varchar(255) not null,
                                    actor_username varchar(255),
                                    actor_company_id integer ,
                                    actor_user_id integer ,
                                    action_date date
);