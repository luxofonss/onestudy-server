ALTER TABLE quizzes ADD COLUMN status TEXT default 'DRAFT';

ALTER TABLE quizzes DROP COLUMN is_public;