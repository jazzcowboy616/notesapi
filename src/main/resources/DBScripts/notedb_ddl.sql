/* create table users and its constraints */
CREATE TABLE public.t_users
(
    id       bigint                 NOT NULL,
    email    character varying(100) NOT NULL,
    name     character varying(255) NOT NULL,
    password character varying(255) NOT NULL
);

ALTER TABLE public.t_users
    ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.users_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );

ALTER TABLE ONLY public.t_users
    ADD CONSTRAINT uk_user_email UNIQUE (email);

ALTER TABLE ONLY public.t_users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

/* create table notes and its constraints */
CREATE TABLE public.t_notes
(
    id                    bigint                 NOT NULL,
    title                 character varying(255) NOT NULL,
    author_id             bigint                 NOT NULL,
    content_search_vector tsvector,
    content               text
);

ALTER TABLE public.t_notes
    ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.notes_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );

ALTER TABLE ONLY public.t_notes
    ADD CONSTRAINT notes_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.t_notes
    ADD CONSTRAINT fk_note_author FOREIGN KEY (author_id) REFERENCES public.t_users (id);

CREATE TRIGGER tsvectorupdate
    BEFORE INSERT OR UPDATE
    ON public.t_notes
    FOR EACH ROW
EXECUTE PROCEDURE
    tsvector_update_trigger(content_search_vector, 'pg_catalog.english', title, 'content');

/* create table note_share and its constraints */
CREATE TABLE public.t_note_share
(
    id      bigint NOT NULL,
    note_id bigint NOT NULL,
    user_id bigint NOT NULL
);

ALTER TABLE public.t_note_share
    ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.note_share_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );

ALTER TABLE ONLY public.t_note_share
    ADD CONSTRAINT note_share_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.t_note_share
    ADD CONSTRAINT fk_share_user FOREIGN KEY (user_id) REFERENCES public.t_users (id);

ALTER TABLE ONLY public.t_note_share
    ADD CONSTRAINT fk_share_note FOREIGN KEY (note_id) REFERENCES public.t_notes (id);