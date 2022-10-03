create table Offer
(
    id                varchar(255) not null primary key,
    company_name      varchar(255) not null,
    title             varchar(255) not null,
    description       varchar(255) not null,
    email             varchar(255) not null,
    address           varchar(255) not null,
    availability_date timestamp,
    expiration_date   timestamp,
    status            varchar(30)
);
