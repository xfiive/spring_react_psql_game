create table stats
(
    stats_id    serial primary key,
    wins_total  integer not null,
    wins_white  integer not null,
    loses_total integer not null,
    loses_white integer not null,
    draws_total integer not null,
    draws_white integer not null,
    games_total integer not null
);

create table players
(
    player_id     serial primary key,
    stats_id      int unique references stats,
    nickname      varchar(100) not null,
    password_hash varchar(300) not null
);

CREATE TABLE IF NOT EXISTS RATING
(
    ident   serial primary key,
    player  text    NOT NULL,
    game    TEXT    NOT NULL,
    rating  INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    ratedOn DATE    NOT NULL
);

CREATE TABLE IF NOT EXISTS SCORE
(
    ident    serial primary key,
    player   text    NOT NULL,
    game     TEXT    NOT NULL,
    points   INTEGER NOT NULL,
    playedOn DATE    NOT NULL
);

CREATE TABLE IF NOT EXISTS COMMENT
(
    ident       serial primary key,
    player      text NOT NULL,
    game        TEXT NOT NULL,
    comment     TEXT NOT NULL,
    commentedOn DATE NOT NULL
);

drop table if exists public.players cascade;

drop table if exists public.stats cascade;

drop table if exists public.rating cascade;

drop table if exists public.score cascade;

drop table if exists public.comment cascade;

