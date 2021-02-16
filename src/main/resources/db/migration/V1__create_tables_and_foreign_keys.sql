create table captcha_codes(
    id integer not null auto_increment,
    code varchar(255) not null,
    secret_code varchar(255) not null,
    time datetime(6) not null,
    primary key (id));

create table global_settings(
    id integer not null auto_increment,
    code varchar(255) not null,
    name varchar(255) not null,
    value varchar(255) not null,
    primary key (id));

create table hibernate_sequence(
    next_val bigint not null);

create table post_comments(
    id integer not null auto_increment,
    parent_id integer,
    post_id integer,
    text varchar(255) not null,
    time datetime(6) not null,
    user_id integer not null,
    primary key (id));

create table post_votes(
    id integer not null auto_increment,
    post_id integer not null,
    time datetime(6) not null,
    user_id integer not null,
    value tinyint not null,
    primary key (id));

create table posts(
    id integer not null auto_increment,
    is_active tinyint not null,
    moderator_status varchar(255),
    moderator_id integer,
    text varchar(255) not null,
    instant datetime(6) not null,
    title varchar(255) not null,
    user_id integer not null,
    view_count integer not null,
    primary key (id));

create table tag2post(
    id integer not null auto_increment,
    post_id integer,
    tag_id integer,
    primary key (id));

create table tags(
    id integer not null auto_increment,
    name varchar(255),
    primary key (id));

create table users(
    id integer not null auto_increment,
    code varchar(255),
    email varchar(255) not null,
    is_moderator tinyint not null,
    name varchar(255) not null,
    password varchar(255) not null,
    photo varchar(255),
    reg_time datetime(6) not null,
    primary key (id));

alter table post_comments add constraint FKg1bili8wkabgreiqorfs9a8c8 foreign key (post_id) references posts (id);
alter table posts add constraint FKpmwxerjkloqfpmwognesh3xsn foreign key (user_id) references users (id);
alter table tag2post add constraint FKjou6suf2w810t2u3l96uasw3r foreign key (tag_id) references tags (id);
alter table tag2post add constraint FKpjoedhh4h917xf25el3odq20i foreign key (post_id) references posts (id);