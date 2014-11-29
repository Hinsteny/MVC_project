-- quick EMPTY : truncate "user", chain, row, item RESTART IDENTITY cascade;
-- quick DROP  : drop table  "user", chain, row, item;

------------ user -------------------
CREATE TABLE "user"
(
	id bigserial NOT NULL,
	username character varying(128),
	"fullName" character varying(128),
	pwd character varying(256),
	CONSTRAINT user_pkey PRIMARY KEY (id)
);
create index on "user" (username);
------------ /user -------------------


-- --------- chain -------------------
CREATE TABLE chain
(
	id bigserial NOT NULL,
	name character varying(128),
	"userId" bigint,

	CONSTRAINT project_pkey PRIMARY KEY (id)
);
-- --------- /chain -------------------

-- --------- row ----------------------
CREATE TABLE row
(
	id bigserial NOT NULL,
	name character varying(128),
	"chainId" bigint,

	CONSTRAINT row_pkey PRIMARY KEY (id)
);
-- --------- /row ---------------------

-- --------- item ---------------------
CREATE TABLE item
(
	id bigserial NOT NULL,
	name character varying(128),
	"rowId" bigint,

	CONSTRAINT item_pkey PRIMARY KEY (id)
);
-- ---------- /item --------------------
