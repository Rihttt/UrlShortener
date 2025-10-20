CREATE TABLE IF NOT EXISTS public.links(
    id uuid primary key not null,
    url varchar not null ,
    image_data bytea,
    url_id uuid not null
)