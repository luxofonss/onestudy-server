ALTER TABLE quiz_attempts DROP COLUMN completed_at;
ALTER TABLE quiz_attempts ADD COLUMN completed_at TIMESTAMP WITH TIME ZONE;