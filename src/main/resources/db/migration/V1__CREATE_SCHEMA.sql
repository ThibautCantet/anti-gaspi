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

create table Contact
(
    id             varchar(255) not null primary key,
    last_name       varchar(255) not null,
    first_name      varchar(255) not null,
    phone_number    varchar(255) not null,
    message_content varchar(255) not null,
    offer_id        varchar(255) not null
);
