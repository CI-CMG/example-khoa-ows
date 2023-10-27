create table ${schema_name}.FEATURE
(
    ID                     varchar(36) not null,
    NOTE                   varchar(500),
    GEBCO_FEATURE_STATE_ID varchar(36),
    primary key (ID)
);