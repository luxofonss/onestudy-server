ALTER TABLE resources ADD COLUMN id_new UUID DEFAULT uuid_generate_v4();

ALTER TABLE resources DROP CONSTRAINT resources_pkey;

ALTER TABLE resources DROP COLUMN id;

ALTER TABLE resources RENAME COLUMN id_new TO id;

ALTER TABLE resources ADD PRIMARY KEY (id);