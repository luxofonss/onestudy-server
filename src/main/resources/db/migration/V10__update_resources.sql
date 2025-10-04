ALTER TABLE resources DROP COLUMN ownerId;
ALTER TABLE resources ADD COLUMN owner_id UUID;