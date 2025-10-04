ALTER TABLE resources ADD COLUMN cloud_id_new text;

ALTER TABLE resources DROP CONSTRAINT resources_cloud_id_key;

ALTER TABLE resources DROP COLUMN cloud_id;

ALTER TABLE resources RENAME COLUMN cloud_id_new TO cloud_id;
