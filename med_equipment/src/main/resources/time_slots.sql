create table public.time_slots
(
    is_free     boolean,
    admin_id    bigint
        constraint company_admin_time_slot_fk
            references public.company_admins,
    calendar_id bigint
        constraint calendar_time_slot_fk
            references public.calendars,
    start       timestamp(6) ,
    version     bigint,
    primary key (admin_id, start)
) partition by range(start);

alter table public.time_slots
    owner to postgres;

CREATE TABLE time_slots_y2024m01 PARTITION OF public.time_slots
    FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');

CREATE TABLE time_slots_y2024m02 PARTITION OF public.time_slots
    FOR VALUES FROM ('2024-02-01') TO ('2024-03-01');

CREATE TABLE time_slots_y2024m03 PARTITION OF public.time_slots
    FOR VALUES FROM ('2024-03-01') TO ('2024-04-01');

CREATE TABLE time_slots_y2024m04 PARTITION OF public.time_slots
    FOR VALUES FROM ('2024-04-01') TO ('2024-05-01');

CREATE TABLE time_slots_y2024m05 PARTITION OF public.time_slots
    FOR VALUES FROM ('2024-05-01') TO ('2024-06-01');

CREATE TABLE time_slots_y2024m06 PARTITION OF public.time_slots
    FOR VALUES FROM ('2024-06-01') TO ('2024-07-01');

CREATE TABLE time_slots_y2024m07 PARTITION OF public.time_slots
    FOR VALUES FROM ('2024-07-01') TO ('2024-08-01');

CREATE TABLE time_slots_y2024m08 PARTITION OF public.time_slots
    FOR VALUES FROM ('2024-08-01') TO ('2024-09-01');

CREATE TABLE time_slots_y2024m09 PARTITION OF public.time_slots
    FOR VALUES FROM ('2024-09-01') TO ('2024-10-01');

CREATE TABLE time_slots_y2024m10 PARTITION OF public.time_slots
    FOR VALUES FROM ('2024-10-01') TO ('2024-11-01');

CREATE TABLE time_slots_y2024m11 PARTITION OF public.time_slots
    FOR VALUES FROM ('2024-11-01') TO ('2024-12-01');

CREATE TABLE time_slots_y2024m12 PARTITION OF public.time_slots
    FOR VALUES FROM ('2024-12-01') TO ('2025-01-01');