CREATE TABLE IF NOT EXISTS public.links(
    id uuid primary key not null DEFAULT gen_random_uuid(),
    original_url varchar not null ,
    short_code varchar(20) not null,
    created_at timestamp not null,
    click_count int8 not null DEFAULT 0,
    user_id uuid not null,
    qrcode_id uuid
)