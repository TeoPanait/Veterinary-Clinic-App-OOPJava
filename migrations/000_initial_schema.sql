create table public.clients
(
    client_id    integer generated always as identity
        primary key,
    name         varchar(100) not null,
    surname      varchar(100) not null,
    phone_number varchar(30)  not null,
    email        varchar(150)
        unique,
    address      varchar(255)
);

alter table public.clients
    owner to postgres;

create table public.veterinarians
(
    vet_id         integer generated always as identity
        primary key,
    name           varchar(100) not null,
    surname        varchar(100) not null,
    phone_number   varchar(30)  not null,
    specialization varchar(100)
);

alter table public.veterinarians
    owner to postgres;

create table public.pets
(
    pet_id   integer generated always as identity
        primary key,
    owner_id integer       not null
        references public.clients
            on delete cascade,
    name_pet varchar(100)  not null,
    age      integer       not null
        constraint pets_age_check
            check (age > 0),
    weight   numeric(6, 2) not null
        constraint pets_weight_check
            check (weight > (0)::numeric),
    pet_type varchar(20)   not null
        constraint pets_pet_type_check
            check ((pet_type)::text = ANY ((ARRAY ['DOG'::character varying, 'CAT'::character varying])::text[]))
);

alter table public.pets
    owner to postgres;

create index idx_pets_owner_id
    on public.pets (owner_id);

create table public.dogs
(
    pet_id    integer      not null
        primary key
        references public.pets
            on delete cascade,
    dog_breed varchar(100) not null,
    size      varchar(50)  not null
);

alter table public.dogs
    owner to postgres;

create table public.cats
(
    pet_id    integer               not null
        primary key
        references public.pets
            on delete cascade,
    cat_breed varchar(100)          not null,
    fur_type  varchar(100)          not null,
    is_indoor boolean default false not null
);

alter table public.cats
    owner to postgres;

create table public.treatments
(
    treatment_id        integer generated always as identity
        primary key,
    name                varchar(100)   not null,
    administration_mode varchar(100)   not null,
    cost                numeric(10, 2) not null
        constraint treatments_cost_check
            check (cost >= (0)::numeric)
    );

alter table public.treatments
    owner to postgres;

create table public.medications
(
    medication_id    integer generated always as identity
        primary key,
    med_name         varchar(120)         not null
        unique,
    dosage           varchar(80)          not null,
    price            numeric(10, 2)       not null
        constraint medications_price_check
            check (price >= (0)::numeric),
    stock            integer              not null
        constraint medications_stock_check
            check (stock >= 0),
    req_prescription boolean default true not null
);

alter table public.medications
    owner to postgres;

create table public.appointments
(
    appointment_id   integer generated always as identity
        primary key,
    appointment_time timestamp                                     not null,
    pet_id           integer                                       not null
        references public.pets
            on delete cascade,
    vet_id           integer
                                                                   references public.veterinarians
                                                                       on delete set null,
    reason           varchar(255)                                  not null,
    status           varchar(30) default 'PROG'::character varying not null
);

alter table public.appointments
    owner to postgres;

create index idx_appointments_date
    on public.appointments (appointment_time);

create table public.medical_records
(
    record_id    integer generated always as identity
        primary key,
    pet_id       integer                             not null
        references public.pets
            on delete cascade,
    vet_id       integer
                                                     references public.veterinarians
                                                         on delete set null,
    diagnosis    text,
    treatment_id integer
                                                     references public.treatments
                                                         on delete set null,
    record_time  timestamp default CURRENT_TIMESTAMP not null
);

alter table public.medical_records
    owner to postgres;

create index idx_medical_records_pet_id
    on public.medical_records (pet_id);

create table public.prescriptions
(
    prescription_id serial
        primary key,
    record_id       integer not null
        references public.medical_records
            on delete cascade,
    medication_id   integer not null
        references public.medications
            on delete restrict,
    quantity        integer not null
        constraint prescriptions_quantity_check
            check (quantity > 0),
    prescribed_at   timestamp default CURRENT_TIMESTAMP
);

alter table public.prescriptions
    owner to postgres;

create index idx_prescriptions_record
    on public.prescriptions (record_id);

create index idx_prescriptions_med
    on public.prescriptions (medication_id);


