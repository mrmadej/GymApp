create table club (id integer not null auto_increment, club_address varchar(255), club_name varchar(255), primary key (id)) engine=InnoDB;
create table in_club (club_id integer, id integer not null auto_increment, `users_count_currently_in_club)` integer, hour_of_download datetime(6), primary key (id)) engine=InnoDB;
alter table in_club add constraint FKgjrb8sqk7h6hqe4seti602w3y foreign key (club_id) references club (id);
