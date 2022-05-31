create table candidat
(
    id                int          not null primary key,
    language          varchar(255) not null,
    email             varchar(255) not null,
    experienceInYears int          not null
);

create table recruteur
(
    id                int          not null primary key,
    language          varchar(255) not null,
    email             varchar(255) not null,
    experienceInYears int          not null
);

create table Offer
(
    id varchar(255) not null primary key,
    company varchar(255) not null,
    title varchar(255) not null,
    description varchar(255) not null,
    email varchar(255) not null,
    address varchar(255) not null,
    disponibilite timestamp,
    expiration timestamp
);
