create table if not exists users
(
    id bigserial
    constraint users_pk
    primary key,
    username varchar(30),
    password varchar(100),
    failed_attempt integer default 0,
    non_locked boolean default true,
    lock_time timestamp
    );

alter table users owner to postgres;

create unique index if not exists users_username_uindex
    on users (username);

create unique index if not exists users_id_uindex
    on users (id);

create table if not exists animals
(
    id bigserial
    constraint animals_pk
    primary key,
    name varchar(30),
    gender varchar(30),
    birth_date date,
    owner_id bigint
    constraint animals_users_id_fk
    references users
    on update set null on delete set null
    );

alter table animals owner to postgres;

create unique index if not exists animals_name_uindex
    on animals (name);

create unique index if not exists animals_id_uindex
    on animals (id);

