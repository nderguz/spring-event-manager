--liquibase formatted sql

--changeset nderguz:1
create table if not exists users
(
    id bigserial primary key,
    login varchar(255) not null unique,
    password_hash varchar(255) not null,
    age int,
    role varchar(25)
);
--rollback DROP TABLE users;

--changeset nderguz:2
create table if not exists locations
(
    id bigserial primary key,
    name varchar(255) not null,
    address varchar(255) not null,
    capacity bigint,
    description text
);
--rollback DROP TABLE locations;

--changeset nderguz:3
create table if not exists events
(
    id bigserial primary key,
    location_id bigint references locations,
    name varchar(255) not null,
    status varchar(25) not null,
    owner_id bigint references users,
    max_places bigint not null,
    cost decimal not null,
    duration integer not null,
    date_start timestamp(0) not null,
    date_end timestamp(0) not null
);
--rollback DROP TABLE events;

--changeset nderguz:4
create table if not exists registrations
(
    id bigserial primary key,
    event_id bigint references events,
    user_id bigint references users,
    status varchar(25) not null
);
--rollback DROP TABLE registrations;