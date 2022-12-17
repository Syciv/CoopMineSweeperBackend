--liquibase formatted sql

--changeset ivan_sych:1
CREATE SCHEMA IF NOT EXISTS sudoku;

--changeset ivan_sych:2
CREATE TABLE sudoku.field
(
    id             bigserial PRIMARY KEY,
    unsolved       text,
    solved         text,
    difficulty     integer
);

--changeset ivan_sych:3
ALTER TABLE sudoku.field RENAME TO game_field;