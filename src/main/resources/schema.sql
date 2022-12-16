create table if not exists MPA
(
    MPA_ID   INTEGER auto_increment,
    MPA_NAME CHARACTER VARYING(10) not null,
    constraint MPA_PK
        primary key (MPA_ID)
);


create table if not exists FILMS
(
    FILM_ID      INTEGER auto_increment,
    NAME         CHARACTER VARYING(50)  not null,
    DESCRIPTION  CHARACTER VARYING(100) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    MPA_ID INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILM_MPA
        foreign key (MPA_ID) references MPA
);

create table if not exists GENRE
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(30) not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);




create table if not exists USERS
(
    USER_ID  INTEGER auto_increment,
    EMAIL    CHARACTER VARYING(50) not null,
    LOGIN    CHARACTER VARYING(50) not null,
    NAME     CHARACTER VARYING(50),
    BIRTHDAY DATE                  not null,
    constraint USERS_PK
        primary key (USER_ID)
);

create table if not exists FILM_GENRES
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint FILM_GENRES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILM_GENRES_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
);

create table  if not exists FRIENDS
(
    USER_ID   INTEGER,
    FRIEND_ID INTEGER,
    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS,
    constraint FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
);

create table if not exists LIKES
(
    USER_ID INTEGER,
    FILM_ID INTEGER,
    constraint LIKES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);

