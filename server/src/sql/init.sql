VACUUM;
create table INFO_HASH
(
    PR_ID       integer not null
        constraint INFO_HASH_pk
            primary key autoincrement,
    CREATE_TIME TEXT,
    HEX_HASH    text,
    ADDRESS     TEXT
);


